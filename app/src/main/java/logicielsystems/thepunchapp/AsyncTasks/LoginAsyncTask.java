package logicielsystems.thepunchapp.AsyncTasks;

/**
 * Created by Deep on 07/05/2016.
 */

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import logicielsystems.thepunchapp.Listners.LoginListner;
import logicielsystems.thepunchapp.Schema.CompanySchema;
import logicielsystems.thepunchapp.Schema.Employees;
import logicielsystems.thepunchapp.Schema.Tasks;
import logicielsystems.thepunchapp.Support.CommonFunctions;
import logicielsystems.thepunchapp.Support.Constants;

public class LoginAsyncTask extends AsyncTask<Void, Void, String> {

    private String email;
    private String password;
    private LoginListner listner;

    public LoginAsyncTask(String email, String password, LoginListner listner) {
        this.email = email;
        this.password = password;
        this.listner = listner;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listner.beforeLogin();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected String doInBackground(Void... params) {

        try {
            ContentValues values = new ContentValues();
            values.put(Constants.AsyncParams.EMAIL, email);
            values.put(Constants.AsyncParams.PASSWORD, password);
            URL url = new URL(Constants.ServerUrl.LOGIN_URL);
            URLConnection urlConn = url.openConnection();

            if (!(urlConn instanceof HttpURLConnection)) {
                throw new IOException("URL is not an Http URL");
            }
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("USER-AGENT", "Mozilla/5.0");
            httpConn.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
            httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpConn.setDoOutput(true);
            DataOutputStream dStream = new DataOutputStream(httpConn.getOutputStream());
            dStream.writeBytes(CommonFunctions.getQuery(values));
            dStream.flush();
            dStream.close();

            int code = httpConn.getResponseCode();

            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
            String response = reader.readLine();
            reader.close();
            return response;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        boolean isSuccess = false;

        if (response != null) {
            try {
                JSONObject companyObject = new JSONObject(response);

                JSONObject employees = companyObject.getJSONObject("employees");
                JSONObject tasks = companyObject.getJSONObject("tasks");

                CompanySchema company = new Gson().fromJson(response, CompanySchema.class);

                CompanySchema tempCompany=getCompany(company.getEmail());
                if (tempCompany!=null)
                    company=tempCompany;
                else
                    company.save();


                setEmployees(employees,company);

                setTasks(tasks,company);

                isSuccess = true;
            } catch (JSONException e) {
                e.printStackTrace();
                isSuccess = false;
            }
        } else {
            isSuccess = false;
        }

        listner.afterLogin(isSuccess);

    }

    private CompanySchema getCompany(String email) {
        try{
            return CompanySchema.find(CompanySchema.class, "email = ?", new String[]{email}).get(0);
        }catch (Exception ex){
            return null;
        }
    }

    private void setTasks(JSONObject tasks, CompanySchema company) {
        Iterator<String> taskKeys = tasks.keys();

        try {
            while (taskKeys.hasNext()) {
                String taskId = taskKeys.next();
                String taskName = tasks.getString(taskId);

                Tasks task=new Tasks(company.getId(),taskId,taskName);
                long count = Tasks.count(Tasks.class, "taskId = ?", new String[]{task.getTaskId()});
                if (count <= 0){
                    long recordNum= task.save();
                    Log.e("Task Id", recordNum+"");
                }

                Log.e("Task", taskId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setEmployees(JSONObject employees,CompanySchema company) {
        Iterator<String> employeeKeys = employees.keys();

        try {
            while (employeeKeys.hasNext()) {
                String employeeId = employeeKeys.next();
                String employeeName = employees.getString(employeeId);

                Employees employee=new Employees(company.getId(),employeeId,employeeName);
                long count = Employees.count(Employees.class, "employeeId = ?", new String[]{employee.getEmpId()});
                if (count <= 0){
                    long recordNum= employee.save();
                    Log.e("Employee Id", recordNum+"");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}


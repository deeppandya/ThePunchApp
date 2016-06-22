package logicielsystems.thepunchapp.AsyncTasks;

/**
 * Created by Deep on 22/06/2016.
 */

import android.content.ContentValues;
import android.os.AsyncTask;

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
import java.util.HashMap;

import logicielsystems.thepunchapp.Listners.AddEmployeeListner;
import logicielsystems.thepunchapp.Listners.AddTaskListner;
import logicielsystems.thepunchapp.Listners.LoginListner;
import logicielsystems.thepunchapp.Schema.CompanySchema;
import logicielsystems.thepunchapp.Schema.Employees;
import logicielsystems.thepunchapp.Schema.Tasks;
import logicielsystems.thepunchapp.Support.CommonFunctions;
import logicielsystems.thepunchapp.Support.Constants;

/**
 * Created by Deep on 21/06/2016.
 */
public class AddTaskAsyncTask extends AsyncTask<Void, Void, String> {

    private String email;
    private Tasks task;
    private AddTaskListner listner;

    public AddTaskAsyncTask(String email, Tasks task, AddTaskListner listner) {
        this.email = email;
        this.task = task;
        this.listner = listner;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listner.beforeAddTask();
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
            values.put(Constants.AsyncParams.TASKS, new Gson().toJson(task));
            URL url = new URL(Constants.ServerUrl.ADD_EMPLOYEE_URL);
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

                isSuccess = true;
            } catch (Exception e) {
                e.printStackTrace();
                isSuccess = false;
            }
        } else {
            isSuccess = false;
        }

        listner.afterAddTask(isSuccess);
    }
}


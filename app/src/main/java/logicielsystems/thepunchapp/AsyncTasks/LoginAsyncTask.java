package logicielsystems.thepunchapp.AsyncTasks;

/**
 * Created by Deep on 07/05/2016.
 */

import android.content.ContentValues;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import logicielsystems.thepunchapp.Listners.LoginListner;
import logicielsystems.thepunchapp.Schema.CompanySchema;
import logicielsystems.thepunchapp.Support.CommonFunctions;
import logicielsystems.thepunchapp.Support.Constants;

public class LoginAsyncTask extends AsyncTask<Void,Void,String> {

    private String email;
    private String password;
    private LoginListner listner;

    public LoginAsyncTask(String email, String password, LoginListner listner) {
        this.email = email;
        this.password = password;
        this.listner=listner;
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
            httpConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            httpConn.setDoOutput(true);
            DataOutputStream dStream = new DataOutputStream(httpConn.getOutputStream());
            dStream.writeBytes(CommonFunctions.getQuery(values));
            dStream.flush();
            dStream.close();

            int code=httpConn.getResponseCode();

            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
            String response=reader.readLine();
            reader.close();
            return response;
        }

        catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        boolean isSuccess=false;

        if(response!=null)
        {
           /* UserSchema user=new Gson().fromJson(response,UserSchema.class);
            long count=UserSchema.count(UserSchema.class,"email = ?",new String[]{user.getEmail()});
            if(count<0)
                user.save();*/
            CompanySchema company=new Gson().fromJson(response,CompanySchema.class);
            long count=CompanySchema.count(CompanySchema.class,"email = ?",new String[]{company.getEmail()});
            if(count<=0)
                company.save();
            isSuccess=true;
        }
        else{
            isSuccess=false;
        }

        listner.afterLogin(isSuccess);

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}


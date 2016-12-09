package bg.softuni.softuniada.studyrise.register;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import bg.softuni.softuniada.studyrise.Activities.MainActivity;

public class BackgroundWorker extends AsyncTask<String, Void, String> {

    Context context;
    AlertDialog alertDialog;
    String type;
    String username;

    public BackgroundWorker(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        type = params[0];
        String login_url = "http://192.168.0.106/login.php";
        String register_url = "http://192.168.0.106/registration.php";
        if (type.equals("login")) {
            try {
                username = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (type.equals("register")) {
            try {
                username = params[1];
                String password = params[2];
                String email = params[3];
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&"
                        + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line;
                line = bufferedReader.readLine();
                result += line;
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
    }

    @Override
    protected void onPostExecute(String result) {
        if (type.equals("login")) {
            alertDialog.setTitle("Login Status");

            if (result == null) {
                alertDialog.setMessage("No connection");
                alertDialog.show();
            } else if (result.equals("login success")) {
                alertDialog.setMessage(result);
                alertDialog.show();
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    public void onCancel(DialogInterface dialog) {

                        SharedPreferences pref = context.getSharedPreferences("MainActivity", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("login", username);
                        editor.commit();

                        Intent i = new Intent(context, MainActivity.class);
                        i.putExtra("LOGIN", username);
                        context.startActivity(i);
                    }
                });
            }

        } else {
            alertDialog.setTitle("Registration Status");
            alertDialog.setMessage(result);
            alertDialog.show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}

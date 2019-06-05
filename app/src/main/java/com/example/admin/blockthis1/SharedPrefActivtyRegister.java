package com.example.admin.blockthis1;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_PHONE_STATE;

public class SharedPrefActivtyRegister extends AppCompatActivity {
    private boolean firstTime = true;
    EditText edit_username,edit_mobile,edit_email;
    Button btn_register;
    private static final String REGISTER_URL="https://vehicleparking.000webhostapp.com/UserRegistration/register.php";
    public static final int RequestPermissionCode = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_pref_activty_register);
        if(checkPermission()){
            Toast.makeText(SharedPrefActivtyRegister.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
        }
        else {
            requestPermission();
        }
        edit_username = (EditText)findViewById(R.id.editText_user);
        edit_email = (EditText)findViewById(R.id.editText_email);
        edit_mobile = (EditText)findViewById(R.id.editText_mobile);
        btn_register = (Button)findViewById(R.id.button_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(isEmailValid(edit_email.getText().toString()) && isPhoneNumberValid(edit_mobile.getText().toString()))
                    registerUser();

                //else
                    //Toast.makeText(SharedPrefActivtyRegister.this, "enter valid data", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void registerUser() {
        String name = edit_username.getText().toString().trim().toLowerCase();
        String email = edit_email.getText().toString().trim().toLowerCase();
        String mobile = edit_mobile.getText().toString().trim().toLowerCase();

        register(name,email,mobile);

    }

    public boolean idDataValid(String email, String mobile){
        if(isEmailValid(email) && isPhoneNumberValid(mobile)) {
            return true;
        }

            if(!isEmailValid(email)){
                edit_email = (EditText)findViewById(R.id.editText_email);
                edit_email.getText().clear();
                Toast.makeText(this, "enter valid email", Toast.LENGTH_SHORT).show();
            }
            else if(!isPhoneNumberValid(mobile)){
                edit_mobile = (EditText)findViewById(R.id.editText_mobile);
                edit_mobile.getText().clear();
                Toast.makeText(this, "enter valid mobile no.", Toast.LENGTH_SHORT).show();
            }


        return false;
    }
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        //Initialize regex for email.
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        //Make the comparison case-insensitive.
        Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches()){
            isValid = true;
            System.out.println("valid email");
        }
        return isValid;
    }
    public static boolean isPhoneNumberValid(String phoneNumber){
        boolean isValid = false;

         /*Examples: Matches following phone numbers:
         *(123)456-7890, 123-456-7890, 1234567890, (123)-456-7890
         */

        //Initialize reg ex for phone number.
        String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches()){
            isValid = true;
            System.out.println("valid phone");
        }
        return isValid;
    }
    private void register(String username,String email, String password)
    {
        String urlSuffix= "?username="+username+"&password="+password+"&email="+email;
        System.out.println("urlSuffix :"+urlSuffix);

        @SuppressWarnings("WrongThread")
        class RegisterUser extends AsyncTask<String,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading= ProgressDialog.show(SharedPrefActivtyRegister.this,"please wait...",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.cancel();
            }

            @Override
            protected String doInBackground(String... params) {

                String s = params[0];
                char c[]=s.toCharArray();

                BufferedReader bufferedReader;
                try{
                    URL url=new URL(REGISTER_URL+s);
                    HttpURLConnection con=(HttpURLConnection)url.openConnection();
                    Log.d("LOG", "HttpPost -> getname.php");
                    int statusCode = con.getResponseCode();
                    if(statusCode != 200) {
                        //Log.d(TAG, "doInBackground Internet Error "+TAG);
                    }
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String result;
                    result = bufferedReader.readLine();
                    Log.d("From background",".......");
                    return result;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        RegisterUser ur=new RegisterUser();
        ur.execute(urlSuffix);
    }


        private void requestPermission() {

        ActivityCompat.requestPermissions(SharedPrefActivtyRegister.this, new String[]
        {
        READ_CONTACTS,
        READ_PHONE_STATE
        }, RequestPermissionCode);

        }

        @Override
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

        case RequestPermissionCode:

        if (grantResults.length > 0) {

        boolean ReadContactsPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        boolean ReadPhoneStatePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

        if (ReadContactsPermission && ReadPhoneStatePermission) {

        Toast.makeText(SharedPrefActivtyRegister.this, "Permission Granted", Toast.LENGTH_LONG).show();
        }
        else {
        Toast.makeText(SharedPrefActivtyRegister.this,"Permission Denied",Toast.LENGTH_LONG).show();

        }
        }

        break;
        }
        }

public boolean checkPermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED;
        }

private boolean isFirstTime() {
        if (firstTime == true) {
            SharedPreferences mPreferences = this.getSharedPreferences("first_time", Context.MODE_PRIVATE);
            firstTime = mPreferences.getBoolean("firstTime", true);
            if (firstTime) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("firstTime", false);
                editor.commit();
            }
        }
        return firstTime;
    }
}

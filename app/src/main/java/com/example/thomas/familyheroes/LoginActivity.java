package com.example.thomas.familyheroes;

import static com.example.thomas.familyheroes.CommonUtilities.SENDER_ID;
import static com.example.thomas.familyheroes.CommonUtilities.SERVER_URL;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends Activity{

    // alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();

    // Internet detector
    ConnectionDetector cd;

    // UI elements
    EditText txtLogin;
    EditText txtMdp;

    // Register button
    Button btnRegister;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Login = "loginKey";
    public static final String Mdp = "mdpKey";

    SharedPreferences sharedpreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(LoginActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }

        // Check if GCM configuration is set
        if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() == 0
                || SENDER_ID.length() == 0) {
            // GCM sernder id / server url is missing
            alert.showAlertDialog(LoginActivity.this, "Configuration Error!",
                    "Please set your Server URL and GCM Sender ID", false);
            // stop executing code by return
            return;
        }


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String login = sharedpreferences.getString(Login, "");
        String mdp = sharedpreferences.getString(Mdp, "");


        if(login.equals("") && mdp.equals(""))
        {
            txtLogin = (EditText) findViewById(R.id.txtLogin);
            txtMdp = (EditText) findViewById(R.id.txtMdp);
            btnRegister = (Button) findViewById(R.id.btnRegister);

           /*
            * Click event on Register button
            * */
            btnRegister.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // Read EditText dat
                    String login = txtLogin.getText().toString();
                    String mdp = txtMdp.getText().toString();

                    // Check if user filled the form
                    if(login.trim().length() > 0 && mdp.trim().length() > 0){
                        // Launch Main Activity
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);


                        Editor editor = sharedpreferences.edit();
                        editor.putString(Login, login);
                        editor.putString(Mdp, mdp);

                        editor.commit();


                        // Registering user on our server
                        // Sending registraiton details to MainActivity
                        i.putExtra("login", login);
                        i.putExtra("mdp", mdp);
                        startActivity(i);
                        finish();
                    }else{
                        // user doen't filled that data
                        // ask him to fill the form
                        alert.showAlertDialog(LoginActivity.this, "Registration Error!", "Please enter your details", false);
                    }
                }
            });
        }
        else
        {

            txtLogin = (EditText) findViewById(R.id.txtLogin);
            txtMdp = (EditText) findViewById(R.id.txtMdp);


            Intent i = new Intent(getApplicationContext(), MainActivity.class);


            // Registering user on our server
            // Sending registraiton details to MainActivity
            i.putExtra("login", txtLogin.getText().toString());
            i.putExtra("mdp", txtMdp.getText().toString());
            startActivity(i);
            finish();
        }

    }

}


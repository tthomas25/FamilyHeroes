package com.example.thomas.familyheroes.MainActivity;

import static com.example.thomas.familyheroes.MainActivity.CommonUtilities.SENDER_ID;
import static com.example.thomas.familyheroes.MainActivity.CommonUtilities.SERVER_URL;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.thomas.familyheroes.Utilities.JSONParser;
import com.example.thomas.familyheroes.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;




public class LoginActivity extends Activity{

    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

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
    public static final String Mail = "mailKey";
    public static final String Mdp = "mdpKey";
    public static final String idUser = "idKey";
    public static final String prenomUser = "prenomKey";

    SharedPreferences sharedpreferences;

    JSONParser jsonParser = new JSONParser();

    private static final String url_connexion = "http://thomaslanternier.fr/family_heroes/app/login.php";

    private static final String TAG_SUCCESS = "success";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.setThreadPolicy(policy);

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

        String login = sharedpreferences.getString(Mail, "");
        String mdp = sharedpreferences.getString(Mdp, "");
        String id = sharedpreferences.getString(idUser, "");

        if(login.equals("") && mdp.equals("") && id.equals(""))
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
                    String mail = txtLogin.getText().toString();
                    String mdp = txtMdp.getText().toString();

                    // Check if user filled the form
                    if(mail.trim().length() > 0 && mdp.trim().length() > 0){

                        Editor editor = sharedpreferences.edit();
                        editor.putString(Mail, mail);
                        editor.putString(Mdp, mdp);

                        editor.commit();

                        new connexion().execute();

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
            Intent i = new Intent(getApplicationContext(), MainActivity.class);

            startActivity(i);
            finish();
        }

    }

    class connexion extends AsyncTask<String, String, String> {


        /**
         * Getting product details in background thread
         */
        protected String doInBackground(final String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> param = new ArrayList<NameValuePair>();
                        param.add(new BasicNameValuePair("mail", txtLogin.getText().toString()));
                        param.add(new BasicNameValuePair("mdp", txtMdp.getText().toString()));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_connexion, "POST", param);

                        // check your log for json response
                        Log.d("Connexion", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {

                            // successfully received product details
                            JSONArray userObj = json
                                    .getJSONArray("user"); // JSON Array

                            // get first product object from JSON Array
                            JSONObject user = userObj.getJSONObject(0);


                            // Launch Main Activity
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);

                            Editor editor = sharedpreferences.edit();
                            editor.putString(idUser, user.getString("id"));
                            editor.putString(prenomUser, user.getString("prenom"));

                            editor.commit();
                            startActivity(i);
                            finish();

                        } else {
                            // product with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }
    }


}

package com.example.thomas.familyheroes;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thomas.familyheroes.FragmentsMenu.NavDrawerItem;
import com.example.thomas.familyheroes.FragmentsMenu.NavDrawerListAdapter;
import com.example.thomas.familyheroes.Utilities.JSONParser;
import com.google.android.gcm.GCMRegistrar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Thomas on 14/11/2014.
 */
public class AlerteActivity extends Activity {

    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

    public static final String MyPREFERENCES = "MyPrefs";

    SharedPreferences sharedpreferences;

    public static final String Mail = "mailKey";
    public static final String Mdp = "mdpKey";
    public static final String idUser = "idKey";
    public static final String prenomUser = "prenomKey";

    public static String mail;
    public static String mdp;
    public static String id;
    public static String prenom;

    // used to store app title
    private CharSequence mTitle;

    JSONParser jsonParser = new JSONParser();

    private static final String url_alerte = "http://thomaslanternier.fr/family_heroes/app/getAlerte.php";
    private static final String url_valide_alerte = "http://thomaslanternier.fr/family_heroes/app/setAlerte.php";

    TextView date_alerte;
    TextView lieu_alerte;
    ImageView image;
    Button show_photo;
    Button valide_alerte;
    Button decline_alerte;
    String lieu;
    String alerte_id;
    TextView alerte_validee;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerte);
        StrictMode.setThreadPolicy(policy);

        Intent i = getIntent();

        alerte_id = i.getStringExtra("alerte_id");
        lieu = i.getStringExtra("lieu");



        getActionBar().setTitle("");

        LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflator.inflate(R.layout.view_logo, null);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);

        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setCustomView(view, params);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        mail = sharedpreferences.getString(Mail, "");
        mdp = sharedpreferences.getString(Mdp, "");
        id = sharedpreferences.getString(idUser, "");
        prenom = sharedpreferences.getString(prenomUser, "");


        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        getActionBar().setIcon(R.drawable.ic_launcher);



        date_alerte = (TextView) findViewById(R.id.date_alerte);
        lieu_alerte = (TextView) findViewById(R.id.lieu_alerte);
        show_photo = (Button) findViewById(R.id.voir_position);
        valide_alerte = (Button) findViewById(R.id.got_it);
        decline_alerte = (Button) findViewById(R.id.call_over_hero);
        image = (ImageView) findViewById(R.id.imageView);

        new getAlerte().execute();

        show_photo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        image.setVisibility(View.VISIBLE);
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        image.setVisibility(View.INVISIBLE);
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });

        valide_alerte.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                new valideAlerte().execute();

                return true;
            }
        });

        decline_alerte.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //new declineAlerte().execute();

                return true;
            }
        });



    }

    class getAlerte extends AsyncTask<String, String, String> {


        /**
         * Getting product details in background thread
         */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("alerte_id", alerte_id));


                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_alerte, "GET", params);

                        Log.e("JSON : ",json.toString());

                        // json success tag
                        success = json.getInt("success");
                        if (success == 1) {
                            // successfully received product details
                            JSONArray alerteObj = json
                                    .getJSONArray("alerte"); // JSON Array


                            JSONObject alerte = alerteObj.getJSONObject(0);

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat dateNewFormat = new SimpleDateFormat("dd/MM/yyyy");
                            Date date = new Date();
                            try {
                                date = dateFormat.parse(alerte.getString("date"));
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            date_alerte.setText(dateNewFormat.format(date)+" - "+alerte.getString("heure")+"H"+alerte.getString("minute"));
                            lieu_alerte.setText(alerte.getString("libelle")+" - "+lieu);

                            URL url = null;
                            try {
                                url = new URL("http://thomaslanternier.fr/family_heroes/app/images/image_kinect.jpg");
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            Bitmap bmp = null;
                            try {
                                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            image.setImageBitmap(bmp);

                            if(alerte.getString("vue").equals("1"))
                            {
                                decline_alerte.setVisibility(View.INVISIBLE);
                                valide_alerte.setVisibility(View.INVISIBLE);

                                alerte_validee= (TextView) findViewById(R.id.alerte_validee);

                                alerte_validee.setText("L'alerte à été validée par " + alerte.getString("prenom")+" "+alerte.getString("nom"));
                                alerte_validee.setVisibility(View.VISIBLE);


                            }


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

    class valideAlerte extends AsyncTask<String, String, String> {


        /**
         * Getting product details in background thread
         */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("alerte_id", alerte_id));
                        params.add(new BasicNameValuePair("user_id", id));



                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_valide_alerte, "GET", params);

                        Log.e("JSON : ",json.toString());

                        // json success tag
                        success = json.getInt("success");
                        if (success == 1) {


                            String phone = json.getString("phone");

                            Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                            phoneIntent.setData(Uri.parse("tel:"+phone));

                            try {
                                startActivity(phoneIntent);
                                finish();
                                Log.i("Finished making a call...", "");
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(AlerteActivity.this,
                                        "Call faild, please try again later.", Toast.LENGTH_SHORT).show();
                            }

                            decline_alerte.setVisibility(View.INVISIBLE);
                            valide_alerte.setVisibility(View.INVISIBLE);

                            alerte_validee= (TextView) findViewById(R.id.alerte_validee);

                            alerte_validee.setText("Vous venez de valider l'alerte n'oubliez pas de vous assurer que la personne est prise en charge !");
                            alerte_validee.setTextSize(20);
                            alerte_validee.setVisibility(View.VISIBLE);

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

package com.example.thomas.familyheroes.FragmentsMenu.FragmentsHome;

/**
 * Created by Thomas on 09/10/2014.
 */

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.thomas.familyheroes.Utilities.JSONParser;
import com.example.thomas.familyheroes.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class HomeFragment extends Fragment {


    private PagerAdapter mPagerAdapter;

    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

    TextView txtProfil;
    TextView txtRythme;
    TextView txtRythme1;
    TextView txtRythme2;
    TextView txtTension;
    TextView txtTension1;
    TextView txtTension2;
    TextView txtTemperature;
    TextView txtTemperature1;
    TextView txtTemperature2;

    TextView age;
    TextView nom;

    ImageView imageProfil;
    ImageView imageRythme;
    ImageView imageTension;
    ImageView imageTemperature;
    ImageView backProfil;
    ImageView fleche;


    String id_personne_age = "1";

    JSONParser jsonParser = new JSONParser();

    private static final String url_sauvegarde_details = "http://thomaslanternier.fr/family_heroes/app/getSauvegarde.php";
    private static final String url_personneAgee_details = "http://thomaslanternier.fr/family_heroes/app/getPersonne.php";

    private static final String TAG_SUCCESS = "success";



    String id = "";
    String id_personne="";
    String prenom = "";

    private int width;
    private int height;


    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        StrictMode.setThreadPolicy(policy);

        id = this.getArguments().getString("id");
        prenom = this.getArguments().getString("prenom");

        new getSauvegardeDetails().execute();
        new getPersonneAgeeDetails().execute();


        // Création de la liste de Fragments que fera défiler le PagerAdapter
        List fragments = new Vector();

        Fragment gauche = Fragment.instantiate(this.getActivity(),PageGaucheFragment.class.getName());
        Fragment milieu = Fragment.instantiate(this.getActivity(),PageMilieuFragment.class.getName());
        Fragment droite = Fragment.instantiate(this.getActivity(),PageDroiteFragment.class.getName());

        // Ajout des Fragments dans la liste
        fragments.add(gauche);
        fragments.add(droite);
        fragments.add(milieu);

        // Création de l'adapter qui s'occupera de l'affichage de la liste de
        // Fragments
        this.mPagerAdapter = new MyPagerAdapter(this.getActivity().getSupportFragmentManager(),fragments);

        Bundle args = new Bundle();
        args.putString("id_user", id);
        args.putString("id_personne", id_personne);
        gauche.setArguments(args);
        milieu.setArguments(args);
        droite.setArguments(args);

        ViewPager pager = (ViewPager) rootView.findViewById(R.id.viewpager);
        // Affectation de l'adapter au ViewPager
        pager.setAdapter(this.mPagerAdapter);


        return rootView;
    }


    class getSauvegardeDetails extends AsyncTask<String, String, String> {


        /**
         * Getting product details in background thread
         */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("id_personne_age", id_personne_age));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_sauvegarde_details, "GET", params);

                        // check your log for json response
                        Log.d("Single Sauvegarde Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray sauvegardeObj = json
                                    .getJSONArray("signe_vitaux"); // JSON Array

                            // get first product object from JSON Array
                            JSONObject sauvegarde = sauvegardeObj.getJSONObject(0);

                            /* First, get the Display from the WindowManager */
                            Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

                            // check display size to figure out what image resolution will be loaded
                            DisplayMetrics metrics = new DisplayMetrics();
                            display.getMetrics(metrics);

                            Point size = new Point();
                            display.getRealSize(size);
                            display.getSize(size);
                            width = size.x;
                            height = size.y;


                            txtProfil = (TextView) getActivity().findViewById(R.id.profil);
                            txtRythme = (TextView) getActivity().findViewById(R.id.rythme_cardiaque);
                            txtRythme1 = (TextView) getActivity().findViewById(R.id.txtRythme);
                            txtRythme2 = (TextView) getActivity().findViewById(R.id.txtRythme2);
                            txtTemperature = (TextView) getActivity().findViewById(R.id.temperature);
                            txtTemperature1 = (TextView) getActivity().findViewById(R.id.txtTemperature1);
                            txtTemperature2 = (TextView) getActivity().findViewById(R.id.txtTemperature2);
                            txtTension = (TextView) getActivity().findViewById(R.id.tension);
                            txtTension1 = (TextView) getActivity().findViewById(R.id.txtTension);
                            txtTension2 = (TextView) getActivity().findViewById(R.id.txtTension2);

                            imageProfil = (ImageView) getActivity().findViewById(R.id.imageProfil);
                            imageRythme = (ImageView) getActivity().findViewById(R.id.imageRythme);
                            imageTension = (ImageView) getActivity().findViewById(R.id.imageTension);
                            imageTemperature = (ImageView) getActivity().findViewById(R.id.imageTemp);

                            imageProfil.getLayoutParams().height = width/2;
                            imageProfil.getLayoutParams().width = width/2;


                            imageRythme.getLayoutParams().height = width/2;
                            imageRythme.getLayoutParams().width = width/2;
                            txtRythme1.getLayoutParams().width = width/2;
                            txtRythme2.getLayoutParams().width = width/2;

                            imageTension.getLayoutParams().height = width/2;
                            imageTension.getLayoutParams().width = width/2;
                            txtTension1.getLayoutParams().width = width/2;
                            txtTension2.getLayoutParams().width = width/2;

                            RelativeLayout.LayoutParams llp3 = (RelativeLayout.LayoutParams)txtTension.getLayoutParams();
                            llp3.setMargins(0, 0, width/6, width/9); // llp.setMargins(left, top, right, bottom);
                            txtTension.setLayoutParams(llp3);


                            imageTemperature.getLayoutParams().height = width/2;
                            imageTemperature.getLayoutParams().width = width/2;
                            txtTemperature1.getLayoutParams().width = width/2;
                            txtTemperature2.getLayoutParams().width = width/2;

                            RelativeLayout.LayoutParams llp2 = (RelativeLayout.LayoutParams)txtTemperature.getLayoutParams();
                            llp2.setMargins(0, 0, width/8, width/6); // llp.setMargins(left, top, right, bottom);
                            txtTemperature.setLayoutParams(llp2);


                            RelativeLayout.LayoutParams llp = (RelativeLayout.LayoutParams)txtRythme.getLayoutParams();


                            int rythme = Integer.parseInt(sauvegarde.getString("rythme_cardiaque"));
                            int temperature = Integer.parseInt(sauvegarde.getString("temperature"));
                            int tension = Integer.parseInt(sauvegarde.getString("tension"));

                            imageProfil.setImageResource(R.drawable.profil);
                            txtProfil.setText(prenom);

                            if(rythme<100)
                            {
                                llp.setMargins(0, 0, width/25, width/8); // llp.setMargins(left, top, right, bottom);
                                txtRythme.setLayoutParams(llp);
                            }
                            else if(rythme>100)
                            {
                                llp.setMargins(0, 0, width/20, width/9); // llp.setMargins(left, top, right, bottom);
                                txtRythme.setTextSize(17);
                                txtRythme.setLayoutParams(llp);
                            }


                            if(rythme < 70)
                            {
                                imageRythme.setImageResource(R.drawable.rc_bleu);
                                txtRythme1.setText("Rythme cardiaque : Faible");
                            }
                            else if(rythme >= 70 && rythme <= 110)
                            {
                                imageRythme.setImageResource(R.drawable.rc_vert);
                                txtRythme1.setText("Rythme cardiaque : Normal");
                            }
                            else if(rythme > 110)
                            {
                                imageRythme.setImageResource(R.drawable.rc_rouge);
                                txtRythme1.setText("Rythme cardiaque : Elevé");
                            }
                            txtRythme.setText(""+rythme);
                            txtRythme2.setText(""+rythme+" battements");

                            if(temperature <= 36)
                            {
                                imageTemperature.setImageResource(R.drawable.temp_bas);
                                txtTemperature1.setText("Température : Basse");
                            }
                            else if(temperature>36 && temperature<=38)
                            {
                                imageTemperature.setImageResource(R.drawable.temp_moyen);
                                txtTemperature1.setText("Température : Normale");
                            }
                            else if(temperature>38)
                            {
                                imageTemperature.setImageResource(R.drawable.temp_haut);
                                txtTemperature1.setText("Température : Haute");
                            }
                            txtTemperature.setText(""+temperature);
                            txtTemperature2.setText(""+temperature+"° celsius");

                            imageTension.setImageResource(R.drawable.tension_normale);


                            txtTension.setText(""+(tension/10));

                            if((tension/10)<9)
                            {
                                txtTension1.setText("Tension : Faible");
                                imageTension.setImageResource(R.drawable.tension_faible);
                            }
                            else if((tension/10) >=9 && (tension/10) <=15)
                            {
                                txtTension1.setText("Tension : Normale");
                                imageTension.setImageResource(R.drawable.tension_normale);
                            }
                            else if((tension/10) > 15)
                            {
                                txtTension1.setText("Tension : Elevée");
                                imageTension.setImageResource(R.drawable.tension_elevee);
                            }

                            txtTension2.setText(""+tension+" mm de mercure");


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

    class getPersonneAgeeDetails extends AsyncTask<String, String, String> {



        /**
         * Getting product details in background thread
         */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("id_user", id));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_personneAgee_details, "GET", params);

                        // check your log for json response
                        Log.d("Single Sauvegarde Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray personneAgeeObj = json
                                    .getJSONArray("details_personne"); // JSON Array

                            // get first product object from JSON Array
                            JSONObject personneAgee = personneAgeeObj.getJSONObject(0);


                            age = (TextView) getActivity().findViewById(R.id.age);
                            nom = (TextView) getActivity().findViewById(R.id.nom);
                            backProfil = (ImageView) getActivity().findViewById(R.id.backProfil);

                            RelativeLayout.LayoutParams llp1 = (RelativeLayout.LayoutParams)age.getLayoutParams();
                            llp1.setMargins(width/14, width/9, 0, 0); // llp.setMargins(left, top, right, bottom)
                            age.setLayoutParams(llp1);

                            id_personne = personneAgee.getString("id");
                            age.setText(personneAgee.getString("age"));
                            nom.setText(personneAgee.getString("prenom") + " " + personneAgee.getString("nom"));

                            String url = personneAgee.getString("photo");

                            try {
                                URL newurl = new URL(url);
                                backProfil.setImageBitmap(BitmapFactory.decodeStream(newurl.openConnection().getInputStream()));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
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





}





package com.example.thomas.familyheroes.FragmentsMenu.FragmentsHeart;

/**
 * Created by Thomas on 20/10/2014.
 */

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thomas.familyheroes.Utilities.JSONParser;
import com.example.thomas.familyheroes.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HeartFragment extends Fragment {

    JSONParser jsonParser = new JSONParser();

    String id = "";
    String prenom = "";

    private static final String url_heart_details = "http://thomaslanternier.fr/family_heroes/app/getSauvegarde.php";

    TextView rythme_cardiaque;

    public HeartFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_heart, container, false);

        new getHeartDetails().execute();

        id = this.getArguments().getString("id");
        prenom = this.getArguments().getString("prenom");

        return rootView;
    }

    public void onDestroyView() {
        super.onDestroyView();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment fragment = (fm.findFragmentById(R.id.tabs_fragment));
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
    }

    class getHeartDetails extends AsyncTask<String, String, String> {



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
                        params.add(new BasicNameValuePair("id_personne_age", id));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_heart_details, "GET", params);

                        // check your log for json response
                        Log.d("Single Heart Details", json.toString());

                        // json success tag
                        success = json.getInt("success");
                        if (success == 1) {
                            // successfully received product details
                            JSONArray heartObj = json
                                    .getJSONArray("signe_vitaux"); // JSON Array

                            // get first product object from JSON Array
                            JSONObject heart = heartObj.getJSONObject(0);

                            rythme_cardiaque = (TextView) getActivity().findViewById(R.id.rythme);


                            int rythme = Integer.parseInt(heart.getString("rythme_cardiaque"));

                            if(rythme < 70)
                            {

                                rythme_cardiaque.setBackgroundResource(R.drawable.normal);

                            }
                            else if(rythme >= 70 && rythme <= 110)
                            {
                                rythme_cardiaque.setBackgroundResource(R.drawable.normal);
                            }
                            else if(rythme > 110)
                            {
                                rythme_cardiaque.setBackgroundResource(R.drawable.danger);
                            }

                            rythme_cardiaque.setText(""+rythme);


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
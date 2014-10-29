package com.example.thomas.familyheroes.FragmentsMenu.FragmentsTension;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

/**
 * Created by Thomas on 28/10/2014.
 */
public class TensionFragment extends Fragment {

    JSONParser jsonParser = new JSONParser();

    String id = "";
    String prenom = "";

    private static final String url_tension_details = "http://thomaslanternier.fr/family_heroes/app/getSauvegarde.php";

    TextView tension_cardiaque;

    public TensionFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_tension, container, false);

        new getTensionDetails().execute();

        id = this.getArguments().getString("id");
        prenom = this.getArguments().getString("prenom");

        return rootView;
    }

    public void onDestroyView() {
        super.onDestroyView();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment fragment = (fm.findFragmentById(R.id.tabs_fragment_tension));
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
    }

    class getTensionDetails extends AsyncTask<String, String, String> {



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
                                url_tension_details, "GET", params);


                        // json success tag
                        success = json.getInt("success");
                        if (success == 1) {
                            // successfully received product details
                            JSONArray tensionObj = json
                                    .getJSONArray("signe_vitaux"); // JSON Array

                            // get first product object from JSON Array
                            JSONObject tension = tensionObj.getJSONObject(0);

                            tension_cardiaque = (TextView) getActivity().findViewById(R.id.tension);


                            int tension1 = Integer.parseInt(tension.getString("tension"));

                            if(tension1 < 100)
                            {

                                tension_cardiaque.setBackgroundResource(R.drawable.normal);

                            }
                            else if(tension1 >= 100 && tension1 <= 150)
                            {
                                tension_cardiaque.setBackgroundResource(R.drawable.normal);
                            }
                            else if(tension1 > 150)
                            {
                                tension_cardiaque.setBackgroundResource(R.drawable.danger);
                            }

                            tension_cardiaque.setText(""+tension1);


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

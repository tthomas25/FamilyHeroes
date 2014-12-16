package com.example.thomas.familyheroes.FragmentsMenu.FragmentsTemperature;

/**
 * Created by Thomas on 02/11/2014.
 */

import android.content.Context;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.familyheroes.R;
import com.example.thomas.familyheroes.Utilities.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
public class TemperatureFragment extends Fragment {

    JSONParser jsonParser = new JSONParser();

    String id = "";
    String prenom = "";

    private static final String url_temperature_details = "http://thomaslanternier.fr/family_heroes/app/getSauvegarde.php";

    TextView temperature;
    ImageView temperature_image;

    public TemperatureFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_temperature, container, false);

        new getTemperatureDetails().execute();

        id = this.getArguments().getString("id");
        prenom = this.getArguments().getString("prenom");



        return rootView;
    }

    public void onDestroyView() {
        super.onDestroyView();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment fragment = (fm.findFragmentById(R.id.tabs_fragment_temperature));
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
    }

    class getTemperatureDetails extends AsyncTask<String, String, String> {



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
                                url_temperature_details, "GET", params);


                        // json success tag
                        success = json.getInt("success");
                        if (success == 1) {
                            // successfully received product details
                            JSONArray temperatureObj = json
                                    .getJSONArray("signe_vitaux"); // JSON Array

                            // get first product object from JSON Array
                            JSONObject temp = temperatureObj.getJSONObject(0);

                            temperature = (TextView) getActivity().findViewById(R.id.temperature);
                            temperature_image = (ImageView) getActivity().findViewById(R.id.imageTemperature);


         /* First, get the Display from the WindowManager */
                            Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

                            // check display size to figure out what image resolution will be loaded
                            DisplayMetrics metrics = new DisplayMetrics();
                            display.getMetrics(metrics);

                            Point size = new Point();
                            display.getRealSize(size);
                            display.getSize(size);
                            int width = size.x;
                            int height = size.y;

                            temperature.setHeight(height/2);
                            temperature.setWidth(width);

                            temperature_image.getLayoutParams().height = height/2;
                            temperature_image.getLayoutParams().width = width;


                            int temp1 = Integer.parseInt(temp.getString("temperature"));

                            if(temp1 < 37)
                            {

                                temperature_image.setBackgroundResource(R.drawable.normal_temperature);

                            }
                            else if(temp1 >= 37 && temp1 <= 38)
                            {
                                temperature_image.setBackgroundResource(R.drawable.normal_temperature);
                            }
                            else if(temp1 > 38)
                            {
                                temperature_image.setBackgroundResource(R.drawable.danger_temperature);
                            }

                            temperature.setText(""+temp1);


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

package com.example.thomas.familyheroes.FragmentsMenu.FragmentsHardware;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thomas.familyheroes.Classes.Hardware;
import com.example.thomas.familyheroes.R;
import com.example.thomas.familyheroes.Utilities.AsyncRequest;
import com.example.thomas.familyheroes.Utilities.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 08/11/2014.
 */
public class HardwareFragment extends Fragment{

    String id = "";
    String prenom = "";
    ListView list;
    CustomAdapter adapter;
    public ArrayList<Hardware> CustomListViewValuesArr = new ArrayList<Hardware>();

    JSONParser jsonParser = new JSONParser();

    private static final String url_hardware = "http://thomaslanternier.fr/family_heroes/app/getHardware.php";
    ArrayList<NameValuePair> params;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_hardware, container, false);

        /******** Take some data in Arraylist ( CustomListViewValuesArr ) ***********/
        id = this.getArguments().getString("id");
        prenom = this.getArguments().getString("prenom");

       /* params = getParams();
        AsyncRequest getHardware = new AsyncRequest(getActivity(), "GET", params);
        getHardware.execute(url_hardware);*/

        new getHardwareDetails().execute();

        return rootView;

    }


   /* @Override
    public void asyncResponse(String response) {

        try {

            JSONArray objects = new JSONArray(response);



            for (int i = 0; i < objects.length(); i++) {
                JSONObject object = objects.getJSONObject(i);
                Hardware hdr = new Hardware();

                hdr.setNom(object.getString("nom"));
                hdr.setLieu(object.getString("libelle"));
                hdr.setImage("kinect");
                if(object.getString("etat")=="1")
                {
                    hdr.setEtat(true);
                }
                else
                {
                    hdr.setEtat(false);
                }


                CustomListViewValuesArr.add(hdr);
            }

            list = ( ListView ) getActivity().findViewById( R.id.listView );  // List defined in XML ( See Below )


            adapter=new CustomAdapter(getActivity().getBaseContext(), CustomListViewValuesArr);

            list.setAdapter( adapter );

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // here you specify and return a list of parameter/value pairs supported by
    // the API
    private ArrayList<NameValuePair> getParams() {
        // define and ArrayList whose elements are of type NameValuePair
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id_user", id));

        return params;
    }*/

    class getHardwareDetails extends AsyncTask<String, String, String> {



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
                                url_hardware, "GET", params);

                        // check your log for json response
                        Log.d("Single Heart Details", json.toString());

                        // json success tag
                        success = json.getInt("success");
                        if (success == 1) {

                            JSONArray objects = json.getJSONArray("hardware");


                            for (int i = 0; i < objects.length(); i++) {
                                JSONObject object = objects.getJSONObject(i);
                                Hardware hdr = new Hardware();

                                if(i==objects.length()-1)
                                {
                                    hdr.setNom(object.getString("nom"));
                                    hdr.setLieu("");
                                    hdr.setImage("bracelet");
                                    if(object.getString("etat").equals("1"))
                                    {
                                        hdr.setEtat(true);
                                    }
                                    else
                                    {
                                        hdr.setEtat(false);
                                    }
                                }
                                else
                                {
                                    hdr.setNom(object.getString("nom"));
                                    hdr.setLieu(object.getString("libelle"));
                                    hdr.setImage("kinect");
                                    if(object.getString("etat").equals("1"))
                                    {
                                        hdr.setEtat(true);
                                    }
                                    else
                                    {
                                        hdr.setEtat(false);
                                    }
                                }



                                CustomListViewValuesArr.add(hdr);
                            }

                            list = ( ListView ) getActivity().findViewById( R.id.listView );  // List defined in XML ( See Below )


                            adapter=new CustomAdapter(getActivity().getBaseContext(), CustomListViewValuesArr);

                            list.setAdapter( adapter );


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
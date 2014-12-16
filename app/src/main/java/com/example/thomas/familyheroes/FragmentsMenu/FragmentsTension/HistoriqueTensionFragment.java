package com.example.thomas.familyheroes.FragmentsMenu.FragmentsTension;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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
public class HistoriqueTensionFragment extends Fragment{

    public static final String MyPREFERENCES = "MyPrefs" ;

    SharedPreferences sharedpreferences;

    public static final String idUser = "idKey";

    public static String id;

    private static final String url_alerte_details = "http://thomaslanternier.fr/family_heroes/app/getHistoriqueAlerte.php";

    JSONParser jsonParser = new JSONParser();

    RelativeLayout rl;

    public HistoriqueTensionFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_historique_tension, container, false);

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        id = sharedpreferences.getString(idUser, "");

        rl=(RelativeLayout) rootView.findViewById(R.id.rl_historique);

        new getAlerteDetails().execute();

        return rootView;
    }

    class getAlerteDetails extends AsyncTask<String, String, String> {


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
                        params.add(new BasicNameValuePair("type_alerte", "1"));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_alerte_details, "GET", params);



                        // json success tag
                        success = json.getInt("success");
                        if (success == 1) {
                            // successfully received product details
                            JSONArray alerteObj = json
                                    .getJSONArray("alerte"); // JSON Array

                            String date[] = new String[alerteObj.length()];
                            String libelle[] = new String[alerteObj.length()];
                            String identite[] = new String[alerteObj.length()];

                            for(int i=0;i<alerteObj.length();i++)
                            {
                                JSONObject alerte = alerteObj.getJSONObject(i);

                                date[i] = alerte.getString("date");
                                libelle[i] = alerte.getString("libelle");
                                identite[i] = alerte.getString("prenom") +" "+ alerte.getString("nom");

                            }
                            String column[] = {"Date","Libellé","HERO"};


                            TableLayout tableLayout = createTableLayout(column,date.length+1, 4, date, libelle, identite);
                            rl.setGravity(Gravity.CENTER);
                            rl.addView(tableLayout);



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


    private TableLayout createTableLayout(String [] cv,int rowCount, int columnCount, String [] dateValue, String [] libelleValue, String [] identiteValue) {
        // 1) Create a tableLayout and its params
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        TableLayout tableLayout = new TableLayout(getActivity());
        tableLayout.setBackgroundColor(Color.TRANSPARENT);


        // 2) create tableRow params
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(3,3,3,3);
        tableRowParams.weight = 2;


        for (int i = 0; i < rowCount; i++) {
            // 3) create tableRow
            TableRow tableRow = new TableRow(getActivity());
            tableRow.setBackgroundResource(R.color.list_background);


            for (int j= 1; j < columnCount; j++) {
                // 4) create textView
                TextView textView = new TextView(getActivity());
                //  textView.setText(String.valueOf(j));
                textView.setBackgroundColor(Color.WHITE);
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(10,20,10,20);

                if(i==0){

                    textView.setText(cv[j-1]);
                }else {
                    if(j==1)
                    {
                        textView.setText(dateValue[i-1]);
                    }
                    if(j==2)
                    {
                        textView.setText(libelleValue[i-1]);
                    }
                    if(j==3)
                    {
                        textView.setText(identiteValue[i-1]);
                    }

                }

                // 5) add textView to tableRow
                tableRow.addView(textView, tableRowParams);
            }

            // 6) add tableRow to tableLayout
            tableLayout.addView(tableRow, tableLayoutParams);
        }

        return tableLayout;
    }
}

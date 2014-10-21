package com.example.thomas.familyheroes;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Thomas on 13/10/2014.
 */
public class PageMilieuFragment extends Fragment {

    String id = "";
    String id_personne="";

    JSONParser jsonParser = new JSONParser();

    private static final String url_user_tasks = "http://thomaslanternier.fr/family_heroes/app/getTasks.php";

    TextView date;
    TextView heure_debut;
    TextView heure_fin;
    TextView task;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.page_milieu_layout, container, false);

        id = this.getArguments().getString("id_user");
        id_personne = this.getArguments().getString("id_personne");

        int success;
        try {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_user", id));
            params.add(new BasicNameValuePair("id_personne", id_personne));

            // getting product details by making HTTP request
            // Note that product details url will use GET request
            JSONObject json = jsonParser.makeHttpRequest(
                    url_user_tasks, "GET", params);



            // json success tag
            success = json.getInt("success");
            if (success == 1) {
                // successfully received product details
                JSONArray tasksObj = json
                        .getJSONArray("tasks"); // JSON Array

                // get first product object from JSON Array
                JSONObject tasks = tasksObj.getJSONObject(0);


                JSONArray tasksObj2 = tasks
                        .getJSONArray("task");

                JSONObject tasks1 = tasksObj2.getJSONObject(1);

                // check your log for json response
                Log.d("Tasks Details", tasks1.toString());

                date = (TextView) view.findViewById(R.id.date);
                heure_debut = (TextView) view.findViewById(R.id.heure_debut);
                heure_fin = (TextView) view.findViewById(R.id.heure_fin);
                task = (TextView) view.findViewById(R.id.tache);

                String dateStr = tasks1.getString("date");
                Date dateTache = null;
                try {
                    dateTache = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                DateFormat simpleDateFormat = new SimpleDateFormat("d MMM yyyy");

                date.setText(simpleDateFormat.format(dateTache));
                heure_debut.setText(tasks1.getString("heure_debut") + "h" + tasks1.getString("minute_debut"));
                heure_fin.setText("- "+tasks1.getString("heure_fin")+"h"+tasks1.getString("minute_fin"));
                task.setText(tasks1.getString("libelle_tache"));



            } else {
                // product with pid not found
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;

    }


}
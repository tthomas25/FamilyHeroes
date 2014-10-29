package com.example.thomas.familyheroes.FragmentsMenu.FragmentsHeart;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.thomas.familyheroes.R;


/**
 * Created by Thomas on 21/10/2014.
 */
public class GraphiqueCardiaqueFragment extends Fragment {

    public static final String MyPREFERENCES = "MyPrefs" ;

    SharedPreferences sharedpreferences;

    public static final String idUser = "idKey";


    public GraphiqueCardiaqueFragment(){}




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_graph_cardiaque, container, false);

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String id = sharedpreferences.getString(idUser, "");

        WebView wv = (WebView) rootView.findViewById(R.id.graph_web);
        wv.getSettings().setJavaScriptEnabled(true);

        wv.loadUrl("http://thomaslanternier.fr/family_heroes/app/graph_cardiaque.php?id_user="+id);

        return rootView;
    }


}


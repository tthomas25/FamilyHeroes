package com.example.thomas.familyheroes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Thomas on 21/10/2014.
 */
public class GraphiqueCardiaqueFragment extends Fragment {

    public GraphiqueCardiaqueFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_graph_cardiaque, container, false);

        return rootView;
    }
}


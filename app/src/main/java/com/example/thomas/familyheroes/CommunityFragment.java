package com.example.thomas.familyheroes;

/**
 * Created by Thomas on 09/10/2014.
 */
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CommunityFragment extends Fragment {

    public CommunityFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_community, container, false);

        return rootView;
    }
}
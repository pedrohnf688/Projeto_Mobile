package com.pedrohnf688.appconfiguracao.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pedrohnf688.appconfiguracao.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListarFragment extends Fragment {


    public ListarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listar, container, false);

        //Listar Pessoas

        return view;
    }

}

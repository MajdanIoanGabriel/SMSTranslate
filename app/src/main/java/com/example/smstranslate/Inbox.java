package com.example.smstranslate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class Inbox extends Fragment {

    public Inbox() {
        // Required empty public constructor
    }

    public static Inbox newInstance() {
        Inbox fragment = new Inbox();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.inbox_fragment, container, false);
    }
}

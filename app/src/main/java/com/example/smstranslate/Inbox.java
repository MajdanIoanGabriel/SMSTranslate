package com.example.smstranslate;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;

import java.util.ArrayList;
import java.util.Objects;


public class Inbox extends ListFragment {

    private ListView list;
    public static ArrayAdapter<Message> adapter;
    private ArrayList<Message> conversations;

    public Inbox() {}

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

        return inflater.inflate(R.layout.inbox_fragment, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        list = getListView();

        conversations = Message.getConversations();

        adapter = new ArrayAdapter<Message>(Objects.requireNonNull(getActivity()),
                android.R.layout.simple_list_item_2, android.R.id.text2, conversations) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);

                Translate translate = new Translate(FirebaseTranslateLanguage.RO, FirebaseTranslateLanguage.EN);
                translate.translate(conversations.get(position));


                text1.setText(conversations.get(position).author);
                text2.setText(conversations.get(position).body);

                return view;
            }
        };
        list.setAdapter(adapter);
        Message.IncomingSms.getInboxInstance(this);
    }

    private void refreshAdapter() {
        conversations = Message.getConversations();
        adapter.clear();
        adapter.addAll(conversations);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        conversations = Message.getConversations();
        refreshAdapter();
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(getActivity(), InboxItem.class);
        intent.putExtra("author", conversations.get(position).author);
        startActivity(intent);
    }



}

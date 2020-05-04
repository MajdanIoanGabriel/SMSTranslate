package com.example.smstranslate;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Inbox extends ListFragment {

    ListView list;
    ArrayAdapter<Message> adapter;
    ArrayList<Message> conversations;

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

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        list = getListView();
        conversations = Message.getConversations(getContext());

        adapter = new ArrayAdapter<Message>(getActivity(),
                android.R.layout.simple_list_item_2, android.R.id.text2, conversations) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(conversations.get(position).author);
                text2.setText(conversations.get(position).body);


                return view;
            }
        };
        list.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        conversations = Message.getConversations(getContext());
        adapter.clear();
        adapter.addAll(conversations);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(getActivity(), InboxItem.class);
        intent.putExtra("author", conversations.get(position).author);
        startActivity(intent);
    }

}

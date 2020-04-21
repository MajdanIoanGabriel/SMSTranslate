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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Inbox extends ListFragment {

    ListView list;
    ArrayList<ArrayList<String>> mobileArray;

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

        mobileArray = getConversations();

        list = getListView();

        ArrayAdapter<ArrayList<String>> adapter = new ArrayAdapter<ArrayList<String>>(getActivity(),
                android.R.layout.simple_list_item_2, android.R.id.text2, mobileArray) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(mobileArray.get(position).get(0));
                text2.setText(mobileArray.get(position).get(1));


                return view;
            }
        };
        list.setAdapter(adapter);
    }

    public ArrayList<ArrayList<String>> getConversations() {
        ArrayList<ArrayList<String>> conversations = new ArrayList<>();

        Uri uri = Uri.parse( "content://sms/inbox/" );
        Cursor cursor = getActivity().getContentResolver().query( uri, null, null ,null, null );

        if( cursor.getCount() > 0 ) {
            String count = Integer.toString( cursor.getCount() );

            while( cursor.moveToNext() ) {
                String address = cursor.getString( cursor.getColumnIndex("Address") );
                String body = cursor.getString( cursor.getColumnIndex("Body"));

                boolean ok = false;
                for(ArrayList<String> conv: conversations) {
                    if (conv.get(0).equals(address)) {
                        conv.add(1,body);
                        ok = true;
                    }
                }

                if (!ok) {
                    conversations.add(new ArrayList<String>());
                    conversations.get(conversations.size()-1).add(address);
                    conversations.get(conversations.size()-1).add(body);
                }

            }
        }

        cursor.close();
        return conversations;
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(getActivity(), InboxItem.class);
        intent.putStringArrayListExtra("messages",mobileArray.get(position));
        startActivity(intent);
    }
}

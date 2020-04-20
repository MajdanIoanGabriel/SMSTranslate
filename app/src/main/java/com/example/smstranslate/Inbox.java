package com.example.smstranslate;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

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
import java.util.Map;


public class Inbox extends ListFragment {

    ListView list;
    ArrayList<HashMap<String,String>> mobileArray;

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

        //mobileArray = getAllSmsFromProvider();
        mobileArray = getConversations();

        list = getListView();

        ArrayAdapter<HashMap<String,String>> adapter = new ArrayAdapter<HashMap<String,String>>(getActivity(),
                android.R.layout.simple_list_item_2, android.R.id.text2, mobileArray) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(mobileArray.get(position).get("Address"));
                text2.setText(mobileArray.get(position).get("Body"));
                return view;
            }
        };
        list.setAdapter(adapter);
    }

    public ArrayList<String> getAllSmsFromProvider() {
        ArrayList<String> list = new ArrayList<String>();
        ContentResolver cr = getActivity().getContentResolver();

        Cursor c = cr.query(Telephony.Sms.Inbox.CONTENT_URI, // Official CONTENT_URI from docs
                new String[] { Telephony.Sms.Inbox.BODY }, // Select body text
                null,
                null,
                Telephony.Sms.Inbox.DEFAULT_SORT_ORDER); // Default sort order

        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {
                list.add(c.getString(0));
                c.moveToNext();
            }
        } else {
            throw new RuntimeException("You have no SMS in Inbox");
        }
        c.close();

        return list;
    }

    public ArrayList<HashMap<String,String>> getConversations() {
        ArrayList<HashMap<String,String>> conversation = new ArrayList<>();

        Uri uri = Uri.parse( "content://sms/inbox/" );
        Cursor cursor = getActivity().getContentResolver().query( uri, null, null ,null, null );

        //startManagingCursor( cursor );
        if( cursor.getCount() > 0 ) {
            String count = Integer.toString( cursor.getCount() );

            while( cursor.moveToNext() ) {
                String address = cursor.getString( cursor.getColumnIndex("Address") );
                String body = cursor.getString( cursor.getColumnIndex("Body"));

                HashMap<String,String> result = new HashMap<>();
                result.put("Address",address);
                result.put("Body",body);

//                if (conversation.contains(result)) {
//                    continue;
//                }
                conversation.add(result);
                //addreses.add(address);
            }
        }

        cursor.close();
        return conversation;
    }
}

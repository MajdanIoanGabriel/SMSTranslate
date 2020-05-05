package com.example.smstranslate;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


public class Contacts extends ListFragment {

    private ListView list;
    private ArrayList<String> mobileArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.contacts_fragment, container, false);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mobileArray = getAllContacts();

        list = getListView();

        ArrayAdapter adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                android.R.layout.simple_list_item_1, android.R.id.text1, mobileArray);
        list.setAdapter(adapter);
    }

    private ArrayList<String> getAllContacts() {
        ArrayList<String> nameList = new ArrayList<>();
        ContentResolver cr = Objects.requireNonNull(getActivity()).getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                nameList.add(name);
                if (cur.getInt(cur.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (Objects.requireNonNull(pCur).moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
        Collections.sort(nameList);
        return nameList;
    }

    public Contacts() {}

    public static Contacts newInstance() {
        Contacts fragment = new Contacts();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}

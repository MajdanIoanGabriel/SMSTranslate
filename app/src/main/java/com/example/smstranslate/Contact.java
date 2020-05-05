package com.example.smstranslate;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Consumer;

public class Contact {
    String name;
    String number;

    public static ArrayList<Contact> contactList = new ArrayList<>();

    public Contact(String c_name, String c_number) {
        name = c_name;
        number = c_number;
    }


    public static String findNameByNumber(String number) {

        for(Contact contact: contactList) {
            if(contact.number.contains(number) || number.contains(contact.number))
                return contact.name;
        }

        return number;
    }

    public static String findNumberByName(String name) {

        for(Contact contact: contactList) {
            if(contact.name.equals(name))
                return contact.number;
        }

        return name;
    }

    public static void updateContacts(Activity activity) {
        ArrayList<Contact> contactArrayList = new ArrayList<>();
        ContentResolver cr = Objects.requireNonNull(activity.getContentResolver());
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                String phoneNo = "Unknown";

                if (cur.getInt(cur.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (Objects.requireNonNull(pCur).moveToNext()) {
                         phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    pCur.close();
                }
                contactArrayList.add(new Contact(name,phoneNo));
            }
        }
        if (cur != null) {
            cur.close();
        }
        Collections.sort(contactArrayList, new Comparator<Contact>() {
            @Override
            public int compare(Contact a, Contact b) {
                return a.name.compareTo(b.name);
            }
        });

        contactList = contactArrayList;
    }


}

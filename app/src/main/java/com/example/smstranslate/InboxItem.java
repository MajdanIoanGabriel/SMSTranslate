package com.example.smstranslate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class InboxItem extends ListActivity {

    public ArrayList<Message> messages;
    public ListView list;
    public String address;
    public TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_item);

        Intent intent = getIntent();
        address =intent.getStringExtra("author");
        messages = Message.getFromSender(getApplicationContext(), address);


        name = findViewById(R.id.name);
        name.setText(address);

        list = getListView();

        ArrayAdapter adapter = new ArrayAdapter<Message>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, messages) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);

                text1.setText(messages.get(position).body);

                return view;
            }
        };
        list.setAdapter(adapter);
    }

}

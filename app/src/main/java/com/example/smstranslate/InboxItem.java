package com.example.smstranslate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class InboxItem extends ListActivity {

    public ArrayList<String> messages;
    public ListView list;
    public String address;
    public TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_item);

        Intent intent = getIntent();
        messages = intent.getStringArrayListExtra("messages");
        address = messages.get(0);
        messages.remove(address);

        name = findViewById(R.id.name);
        name.setText(address);

        list = getListView();

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, messages);
        list.setAdapter(adapter);
    }

}

package com.example.smstranslate;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class InboxItem extends ListActivity {

    public ArrayList<Message> messages;
    public ListView list;
    public String address;
    public TextView name;
    public EditText input;
    public ImageView send;

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
        input = findViewById(R.id.text_keyboard_input);
        send = findViewById(R.id.image_send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message(getApplicationContext(), address, input.getText().toString(), Message.MESSAGE_SENT);
                message.send();
                input.setText("");
            }
        });

        ArrayAdapter adapter = new ArrayAdapter<Message>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, messages) {
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);

                text1.setText(messages.get(position).body);

                return view;
            }
        };
        list.setAdapter(adapter);
    }

}

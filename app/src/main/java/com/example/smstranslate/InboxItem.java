package com.example.smstranslate;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InboxItem extends AppCompatActivity {

    public ArrayList<Message> messages;
    public RecyclerView recyclerView;
    public String address;
    public TextView name;
    public EditText input;
    public ImageView send;
    public static InboxItemAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_item);

        Intent intent = getIntent();
        address =intent.getStringExtra("author");
        messages = Message.getFromSender(address);


        name = findViewById(R.id.name);
        name.setText(address);

        recyclerView = findViewById(R.id.recycler_view);
        input = findViewById(R.id.text_keyboard_input);
        send = findViewById(R.id.image_send);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new InboxItemAdapter(messages, this);
        recyclerView.setAdapter(recyclerAdapter);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!input.getText().toString().isEmpty()) {
                    Message message = new Message(address, input.getText().toString(), Message.MESSAGE_SENT);
                    message.send();
                    input.setText("");
                    recyclerAdapter.add(message);
                    recyclerAdapter.notifyDataSetChanged();
                }
            }
        });

    }


}

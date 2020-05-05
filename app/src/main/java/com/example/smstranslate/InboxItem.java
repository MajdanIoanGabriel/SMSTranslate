package com.example.smstranslate;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InboxItem extends AppCompatActivity {

    public ArrayList<Message> messages;
    public ListView list;
    public RecyclerView recyclerView;
    public String address;
    public TextView name;
    public EditText input;
    public ImageView send;
    ArrayAdapter<Message> adapter;
    InboxItemAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_item);

        Intent intent = getIntent();
        address =intent.getStringExtra("author");
        messages = Message.getFromSender(getApplicationContext(), address);


        name = findViewById(R.id.name);
        name.setText(address);

        //list = getListView();
        recyclerView = findViewById(R.id.recycler_view);
        input = findViewById(R.id.text_keyboard_input);
        send = findViewById(R.id.image_send);

//        adapter = new ArrayAdapter<Message>(this,
//                R.id.chat_bubble_left, messages) {
//            @NonNull
//            @Override
//            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
//                TextView text1 = view.findViewById(R.id.chat_bubble_left);
//
//                text1.setText(messages.get(position).body);
//
//                return view;
//            }
//        };
//        list.setAdapter(adapter);
//        list.setStackFromBottom(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
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
                    adapter.add(message);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }


}

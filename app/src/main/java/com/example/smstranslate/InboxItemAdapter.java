package com.example.smstranslate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;

import java.util.ArrayList;

public class InboxItemAdapter extends RecyclerView.Adapter<InboxItemAdapter.MessageHolder> {

    ArrayList<Message> messageList;
    Context mContext;

    public InboxItemAdapter(ArrayList<Message> messageList, Context context) {
        this.messageList = messageList;
        this.mContext = context;
    }
    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view;

        if (viewType  == Message.MESSAGE_RECEIVED)
            view = layoutInflater.inflate(R.layout.chat_bubble_left, parent, false);
        else
            view = layoutInflater.inflate(R.layout.chat_bubble_right, parent, false);
        return new MessageHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).type;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        final Message message = messageList.get(position);

        // Set the data to the views here
        final int translate_to = FirebaseTranslateLanguage.languageForLanguageCode(MainActivity.sourceLang.getValue().getCode());

        Translate translate = new Translate(FirebaseTranslateLanguage.RO, translate_to);
        translate.translate(message);


        holder.body.setText(message.translated_body);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void add(Message message) {
        messageList.add(message);
    }

    public class MessageHolder extends RecyclerView.ViewHolder {

        private TextView body;

        public MessageHolder(View itemView) {
            super(itemView);

            body = itemView.findViewById(R.id.chat_text);
        }

    }
}



package com.example.smstranslate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.view.View;

import java.util.ArrayList;

@SuppressLint("ViewConstructor")
public class Message extends View {

    public String author;
    public String body;
    public Integer type;


    public static ArrayList<Message> messageList = new ArrayList<>();
    public static Integer MESSAGE_RECEIVED = 1;
    public static Integer MESSAGE_SENT = 2;
    private static SmsManager smsManager= SmsManager.getDefault();

    public Message(Context context, String auth, String bdy, Integer tp) {
        super(context);
        author = auth;
        body = bdy;
    }

    public static void addMessage(Context context, String auth, String bdy, Integer tp) {
        messageList.add(new Message(context, auth, bdy, tp));
    }

    public static void readAllMessages(Context context) {
        messageList = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI, null, null ,null, "date desc" );

        if(cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                String m_address = cursor.getString( cursor.getColumnIndex("address"));
                String m_body = cursor.getString( cursor.getColumnIndex("body"));
                Integer m_type = cursor.getInt( cursor.getColumnIndex("type"));

                addMessage(context, m_address, m_body, m_type);
            }
        }

        cursor.close();
    }

    public static ArrayList<Message> getConversations(Context context) {
        readAllMessages(context);
        ArrayList<Message> conversations = new ArrayList<>();
        for (Message message: messageList) {
            boolean last = true;
            for (Message last_message: conversations) {
                if (message.author.equals(last_message.author)) {
                    last = false;
                    break;
                }
            }

            if(last)
                conversations.add(message);
        }

        return conversations;
    }

    public static ArrayList<Message> getFromSender(Context context, String auth) {
        readAllMessages(context);
        ArrayList<Message> messages = new ArrayList<>();
        for (Message message: messageList) {
            if (message.author.equals(auth))
                messages.add(message);
        }

        return messages;
    }

    public void send() {
        smsManager.sendTextMessage(author,null, body, null, null);
        messageList.add(this);
    }
}

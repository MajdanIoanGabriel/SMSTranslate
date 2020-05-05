package com.example.smstranslate;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

@SuppressLint("ViewConstructor")
public class Message {

    public String author;
    public String body;
    public Integer type;


    public static ArrayList<Message> messageList = new ArrayList<>();
    public static Integer MESSAGE_RECEIVED = 1;
    public static Integer MESSAGE_SENT = 2;
    private static SmsManager smsManager= SmsManager.getDefault();

    public Message(String auth, String bdy, Integer tp) {
        author = auth;
        body = bdy;
    }

    public static void addMessage(String auth, String bdy, Integer tp) {
        messageList.add(new Message(auth, bdy, tp));
    }

    public static void readAllMessages(Context context) {
        messageList = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI, null, null ,null, "date desc" );

        if(cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                String m_address = cursor.getString( cursor.getColumnIndex("address"));
                String m_body = cursor.getString( cursor.getColumnIndex("body"));
                Integer m_type = cursor.getInt( cursor.getColumnIndex("type"));

                addMessage(m_address, m_body, m_type);
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
                messages.add(0,message);
        }

        return messages;
    }

    public void send() {
        smsManager.sendTextMessage(author,null, body, null, null);
        messageList.add(this);
    }

    public static class IncomingSms extends BroadcastReceiver {

        public IncomingSms() {
            super();
        }
        public static void getInboxInstance(Inbox inboxInstance) {
            inbox = inboxInstance;
        }

        // Get the object of SmsManager
        final SmsManager sms = SmsManager.getDefault();
        public static Inbox inbox;

        public void onReceive(Context context, Intent intent) {

            // Retrieves a map of extended data from the intent.
            final Bundle bundle = intent.getExtras();

            try {

                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for (int i = 0; i < pdusObj.length; i++) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                        String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();

                        Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);


                        // Show Alert
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context,
                                "senderNum: "+ senderNum + ", message: " + message, duration);
                        toast.show();

                        addMessage(senderNum, message, MESSAGE_RECEIVED);
                        inbox.getFragmentManager().beginTransaction().detach(inbox).attach(inbox).commit();


                    } // end for loop
                } // bundle is null

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" +e);

            }
        }
    }
}

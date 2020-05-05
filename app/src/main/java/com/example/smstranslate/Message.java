package com.example.smstranslate;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Objects;

@SuppressLint("ViewConstructor")
public class Message {

    String author;
    String body;
    Integer type;


    private static ArrayList<Message> messageList = new ArrayList<>();
    static Integer MESSAGE_RECEIVED = 1;
    static Integer MESSAGE_SENT = 2;
    private static SmsManager smsManager= SmsManager.getDefault();

    Message(String auth, String bdy, Integer tp) {
        author = auth;
        body = bdy;
        type = tp;
    }

    private static void addMessage(String auth, String bdy, Integer tp) {
        messageList.add(new Message(auth, bdy, tp));
    }

    private static void readAllMessages(Context context) {
        messageList = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI, null, null ,null, "date desc" );

        if(Objects.requireNonNull(cursor).getCount() > 0) {
            while(cursor.moveToNext()) {
                String m_address = cursor.getString( cursor.getColumnIndex("address"));
                String m_body = cursor.getString( cursor.getColumnIndex("body"));
                Integer m_type = cursor.getInt( cursor.getColumnIndex("type"));

                addMessage(Contact.findNameByNumber(m_address), m_body, m_type);
            }
        }

        cursor.close();
    }

    static ArrayList<Message> getConversations(Context context) {
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

    static ArrayList<Message> getFromSender(Context context, String auth) {
        readAllMessages(context);
        ArrayList<Message> messages = new ArrayList<>();
        for (Message message: messageList) {
            if (message.author.equals(auth))
                messages.add(0,message);
        }

        return messages;
    }

    void send() {
        smsManager.sendTextMessage(Contact.findNumberByName(author),null, body, null, null);
        messageList.add(this);
    }

    public static class IncomingSms extends BroadcastReceiver {

        public IncomingSms() {
            super();
        }
        public static void getInboxInstance(Inbox inboxInstance) {
            inbox = inboxInstance;
        }

        @SuppressLint("StaticFieldLeak")
        public static Inbox inbox;

        public void onReceive(Context context, Intent intent) {

            final Bundle bundle = intent.getExtras();
            try {
                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for (Object o : Objects.requireNonNull(pdusObj)) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) o);

                        String senderNum = currentMessage.getDisplayOriginatingAddress();
                        String message = currentMessage.getDisplayMessageBody();

                        Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context,
                                "senderNum: " + senderNum + ", message: " + message, duration);
                        toast.show();

                        addMessage(senderNum, message, MESSAGE_RECEIVED);
                        Objects.requireNonNull(inbox.getFragmentManager()).beginTransaction().detach(inbox).attach(inbox).commit();

                    }
                }

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" +e);

            }
        }
    }
}

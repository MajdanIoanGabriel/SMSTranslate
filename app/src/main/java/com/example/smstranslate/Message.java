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
    boolean translated;


    public static ArrayList<Message> messageList = new ArrayList<>();
    public static final int MESSAGE_RECEIVED = 1;
    public static final int MESSAGE_SENT = 2;
    private static SmsManager smsManager= SmsManager.getDefault();



    Message(String auth, String bdy, Integer tp) {
        author = auth;
        body = bdy;
        type = tp;
        translated = false;
    }

    private static void addMessage(String auth, String bdy, Integer tp) {
        messageList.add(0, new Message(auth, bdy, tp));
    }

    public static void readAllMessages(Context context) {
        messageList = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI, null, null ,null, "date desc" );

        if(Objects.requireNonNull(cursor).getCount() > 0) {
            while(cursor.moveToNext()) {
                String m_address = cursor.getString( cursor.getColumnIndex("address"));
                String m_body = cursor.getString( cursor.getColumnIndex("body"));
                Integer m_type = cursor.getInt( cursor.getColumnIndex("type"));

                Message message = new Message(Contact.findNameByNumber(m_address), m_body, m_type);
                messageList.add(message);

            }
        }

        cursor.close();
    }

    static ArrayList<Message> getConversations() {
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

    static ArrayList<Message> getFromSender(String auth) {
        ArrayList<Message> messages = new ArrayList<>();
        for (Message message: messageList) {
            if (message.author.equals(auth))
                messages.add(0,message);
        }

        return messages;
    }

    void send() {
        smsManager.sendTextMessage(Contact.findNumberByName(author),null, body, null, null);
        messageList.add(0,this);

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

                        Message m = new Message(Contact.findNameByNumber(senderNum), message, MESSAGE_RECEIVED);
                        messageList.add(0,m);
                        Objects.requireNonNull(inbox.getFragmentManager()).beginTransaction().detach(inbox).attach(inbox).commit();
                            if(InboxItem.instance != null)
                                InboxItem.instance.recyclerAdapter.add(m);

                    }
                }

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" +e);

            }
        }
    }

}

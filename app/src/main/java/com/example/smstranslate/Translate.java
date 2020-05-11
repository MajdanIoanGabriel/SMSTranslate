package com.example.smstranslate;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;


public class Translate {

    public Translate(int from, int to) {
        FirebaseTranslatorOptions options =
                new FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(from)
                        .setTargetLanguage(to)
                        .build();
        translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
    }

    public static FirebaseTranslator translator;



    public void translate(final Message message) {

        if (message.translated)
            return;

        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();

        translator.downloadModelIfNeeded(conditions);

        translator.translate(message.body)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@NonNull String translatedText) {

                                message.body = translatedText;
                                message.translated = true;

                                Inbox.adapter.notifyDataSetChanged();
                                if(InboxItem.recyclerAdapter != null)
                                    InboxItem.recyclerAdapter.notifyDataSetChanged();


                            }
                        });

    }
}
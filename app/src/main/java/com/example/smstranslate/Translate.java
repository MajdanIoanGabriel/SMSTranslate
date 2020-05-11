package com.example.smstranslate;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;


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
                                if(InboxItem.instance != null)
                                    InboxItem.instance.recyclerAdapter.notifyDataSetChanged();


                            }
                        });

    }

    public static ArrayList<Language> getAvailableLanguages() {
        ArrayList<Language> languages = new ArrayList<>();
        Set<Integer> languageIds = FirebaseTranslateLanguage.getAllLanguages();
        for (Integer languageId : languageIds) {
            languages.add(
                    new Language(FirebaseTranslateLanguage.languageCodeForLanguage(languageId)));
        }
        return languages;
    }

    static class Language implements Comparable<Language> {
        private String code;

        Language(String code) {
            this.code = code;
        }

        String getDisplayName() {
            return new Locale(code).getDisplayName();
        }

        String getCode() {
            return code;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }

            if (!(o instanceof Language)) {
                return false;
            }

            Language otherLang = (Language) o;
            return otherLang.code.equals(code);
        }

        @NonNull
        public String toString() {
            return code + " - " + getDisplayName();
        }

        @Override
        public int hashCode() {
            return code.hashCode();
        }

        @Override
        public int compareTo(@NonNull Language o) {
            return this.getDisplayName().compareTo(o.getDisplayName());
        }
    }
}
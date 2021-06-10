# SMSTranslate

## Introduction
SMSTranslate is an application that offers the possibility to converse via SMS messages, even if the user does not know the language of the recipient, because the application uses a translation system to convert the messages into the user's language.

## Purpose

The purpose of this application is to facilitate communication between people who do not speak a common language.
The application can be used by anyone, with or without the translation function, but it was designed for employees of larger companies, who frequently communicate with people of other nationalities.

## Functionality

Class diagram:

![image](https://user-images.githubusercontent.com/53294181/121595153-a8983180-ca46-11eb-8008-ebb9bb4323fb.png)

## Running the application

When starting the application, the user is displayed the phone's messages in the Inbox fragment.
The messages are automatically translated into English.
To change the language in which messages are translated, the user must press the button with the name of the current language (eg: en - English), and choose one of the options offered by the application (eg: es - Spanish).
The user can also access the contact list of the phone through the application, by swiping to the left.

![image](https://user-images.githubusercontent.com/53294181/121595448-f57c0800-ca46-11eb-821a-091b6f7730b5.png)

After choosing your preferred language, the application will refresh the message list and display the messages translated into the selected language.
If the user wants to enter a conversation, all they have to do is click on a message in the inbox and the application will open the conversation with that recipient.
When you open the conversation, the translator will translate all the messages in that conversation and display them in an easy-to-understand format.
The user can send messages as in other messaging applications, and when he receives a message, it is added to the Inbox and translated automatically.

![image](https://user-images.githubusercontent.com/53294181/121595644-25c3a680-ca47-11eb-9822-29a563720d6a.png)

The application is structured in several activities and fragments, as well as POJO (plain old java object) classes and a custom adapter for displaying messages in the conversation.

## Future improvements

Improvements in runtime are expected in the future, as the translation function slows down runtime quite a bit.
Another improvement is the ability to choose a language from which to translate messages for each contact and save these preferences, most likely in files or a database.
It also needs work on how permissions are checked and requested, because the app doesn't pause until permissions are granted, which causes it to crash before permissions are granted.

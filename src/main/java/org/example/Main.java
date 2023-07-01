package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.converters.GsonConverter;
import org.example.converters.JsonConverter;
import org.example.models.Contact;
import org.example.models.ContactsDataSource;
import org.example.repositories.AppContactRepository;
import org.example.repositories.ContactRepository;

import java.nio.file.Path;
import java.util.List;

public class Main {
    private static final String DATE_PATH = "contacts.json";

    public static void main(String[] args) {
        Gson gson = new GsonBuilder()

                .create();
        JsonConverter gsonConverter = new GsonConverter(gson);
        ContactsDataSource contactsDataSource = new ContactsDataSource(gsonConverter, Path.of(DATE_PATH));
        List<Contact> contacts = ContactsDataSource.readContacts();
        ContactRepository contactRepository = new AppContactRepository(contactsDataSource, contacts);



    }
}
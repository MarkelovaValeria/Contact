package org.example.models;

import org.example.converters.JsonConverter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ContactsDataSource {
    private final JsonConverter jsonConverter;
    private final Path path;

    public ContactsDataSource(JsonConverter jsonConverter, Path path) {
        this.path = path;
        this.jsonConverter = jsonConverter;
    }

    public List<Contact> readContacts(){
        try {
            String jsonContent = Files.readString(path);
            return jsonConverter.fromJson(jsonContent);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeContacts(List<Contact> contacts) {
        //convert list of Book to json
        String jsonContacts = jsonConverter.toJson(contacts);
        try {
            Files.writeString(path,jsonContacts);
        }catch (IOException e){
            e.printStackTrace();
        }
        //write json from file

    }
}
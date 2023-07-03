package org.example.repositories;

import org.example.models.Contact;
import org.example.models.ContactsDataSource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AppContactRepository implements ContactRepository {


    private final ContactsDataSource contactsDataSource;

    private final List<Contact> contacts;


    public AppContactRepository(ContactsDataSource contactsDataSource, List<Contact> contacts) {
        this.contactsDataSource = contactsDataSource;
        this.contacts = contacts;
    }


    @Override
    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    @Override
    public void editContact(Contact name, Contact new_name) {
        int in = contacts.indexOf(name);
        contacts.set(in, new_name);
    }

    @Override
    public void deleteContact(Contact contact) {
        contacts.remove(contact);
    }

    @Override
    public List<Contact> searchContact(String name) {
        List<Contact> search = new ArrayList<>();
        for (Contact contact : contacts){
            if(contact.user().name().contains(name) ||
               contact.user().surName().contains(name) ||
               contact.phone().contains(name) ||
               contact.group().contains(name)){
                search.add(contact);
            }
        }
        return search;
    }

    @Override
    public void sortContact(Comparator<Contact> comparator) {
        contacts.sort(comparator);
    }

    @Override
    public void saveChanges() {
        contactsDataSource.writeContacts(contacts);
    }
}


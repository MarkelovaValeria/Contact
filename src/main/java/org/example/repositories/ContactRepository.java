package org.example.repositories;

import org.example.models.Contact;

import java.util.Comparator;
import java.util.List;

public interface ContactRepository {
    void addContact(Contact contact);

    void editContact(Contact group, Contact new_group);

    void deleteContact(Contact contact);

    List<Contact> searchContact(String name);

    void sortContact(Comparator<Contact> comparator);

    void saveChanges();
}

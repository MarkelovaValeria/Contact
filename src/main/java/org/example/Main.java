package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.converters.GsonConverter;
import org.example.converters.JsonConverter;
import org.example.models.Birthday;
import org.example.models.Contact;
import org.example.models.ContactsDataSource;
import org.example.models.User;
import org.example.repositories.AppContactRepository;
import org.example.repositories.ContactRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import static java.nio.file.Path.of;

public class Main {
    private static final String DATE_PATH = "contacts.json";
    private static final DateTimeFormatter date = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new Birthday())
                .create();
        JsonConverter gsonConverter = new GsonConverter(gson);
        Path path = of(DATE_PATH);
        ContactsDataSource contactsDataSource = new ContactsDataSource(gsonConverter, path);
        if (!Files.exists(path)) contactsDataSource.writeContacts(List.of());
        List<Contact> contacts = contactsDataSource.readContacts();
        ContactRepository contactRepository = new AppContactRepository(contactsDataSource, contacts);

        System.out.println("            Contact List            ");
        while (true) {
            System.out.println("""
                    Commands that can be used:
                    0. Exit
                    1. Add contact;
                    2. Edit contact;
                    3. Delete contact;
                    4. Sort contact;
                    5. Search contact;
                    6. Show all contacts;
                    Select the required command""");

            int number = scanner.nextInt();
            if (number == 0) {
                System.out.println("Exit...");
                System.exit(0);
            } else if (number == 1) {
                addContact(scanner, contactRepository);
            } else if (number == 2) {
                editContact(scanner, contactRepository);
            } else if (number == 3) {
                deleteContact(scanner,contactRepository);
            } else if (number == 4) {
                sortContact(contactRepository,scanner);
            } else if (number == 5) {
                searchContact(scanner, contactRepository);
            }
            else if(number == 6){
                showContact(contactRepository);
            }
            else{
                System.out.println("No command found");
            }
        }

    }

    private static void addContact(Scanner scanner, ContactRepository contactRepository) {
        System.out.println("Enter a name: ");
        scanner.nextLine();
        String name = scanner.nextLine();

        System.out.println("Enter a surname: ");
        String surName = scanner.nextLine();

        System.out.println("Enter a phone number: ");
        String phone = scanner.nextLine();

        System.out.println("Enter a date of birth: ");
        String birthday = scanner.nextLine();
        LocalDate birthDay = LocalDate.parse(birthday, date);

        System.out.println("Enter a group: ");
        String group = scanner.nextLine();

        Contact contact = new Contact(new User(name, surName), phone, birthDay, group);
        contactRepository.addContact(contact);

        contactRepository.saveChanges();
        System.out.println("Contact has been added");
    }

    private static void editContact(Scanner scanner, ContactRepository contactRepository) {
        System.out.println("Enter the name of the contact you want to change");
        String name = scanner.nextLine();

        List<Contact> search = contactRepository.searchContact(name);

        if (!search.isEmpty()) {

            for (int i = 0; i < search.size(); i++) {
                Contact contact = search.get(i);
                System.out.println((i + 1) + ". " + contact.user().name() + " " + contact.user().surName());
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice >= 1 && choice <= search.size()) {
                Contact selectedContact = search.get(choice - 1);

                System.out.println("Enter a new name: ");
                String Name = scanner.nextLine();

                System.out.println("Enter a new surname: ");
                String surName = scanner.nextLine();

                System.out.println("Enter a new phone number: ");
                String phone = scanner.nextLine();

                System.out.println("Enter a new date of birth: ");
                String birthday = scanner.nextLine();
                LocalDate birthDay = LocalDate.parse(birthday, date);

                System.out.println("Enter a new group: ");
                String group = scanner.nextLine();


                Contact updatedContact = new Contact(new User(Name, surName), phone, birthDay, group);
                contactRepository.editContact(selectedContact, updatedContact);
                contactRepository.saveChanges();
                System.out.println("The contact has been changed");
            }
        } else {
            System.out.println("Contact will be not found");
        }
    }

    private static void deleteContact(Scanner scanner, ContactRepository contactRepository) {
        System.out.println("Enter the name of the contact you want to delete");
        scanner.nextLine();
        String name = scanner.nextLine();

        List<Contact> search = contactRepository.searchContact(name);

        if (!search.isEmpty()) {
            System.out.println("Found " + search.size() + " contact(s)");
            System.out.println("Select the contact number to delete:");

            for (int i = 0; i < search.size(); i++) {
                Contact contact = search.get(i);
                System.out.println((i + 1) + ". " + contact.user().name() + " " + contact.user().surName());
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice >= 1 && choice <= search.size()) {
                Contact selectedContact = search.get(choice - 1);
                contactRepository.deleteContact(selectedContact);
                contactRepository.saveChanges();
                System.out.println("The contact has been deleted");
            } else {
                System.out.println("Contact will be not found");
            }
        }

    }

    public static void searchContact(Scanner scanner, ContactRepository contactRepository) {
        System.out.println("Enter the name of the contact: ");
        scanner.nextLine();
        String name = scanner.nextLine();

        List<Contact> search = contactRepository.searchContact(name);

        if (!search.isEmpty()) {
            System.out.println("Found " + search.size() + " contact(s)");

            for (int i = 0; i < search.size(); i++) {
                Contact contact = search.get(i);
                System.out.println("Contact " + (i + 1) + ":\n" +
                        "Name: " + contact.user().name() + "\n" +
                        "Surname: " + contact.user().surName() + "\n" +
                        "Phone: " + contact.phone() + "\n" +
                        "Birthday: " + contact.birthDay().format(date) + "\n");
            }
        } else {
            System.out.println("Contact will be not found");
        }
    }

    private static void sortContact(ContactRepository contactRepository, Scanner scanner) {
        System.out.println("""
                Enter
                1.Name;
                2.Surname;
                3.Phone;
                4.Birthday;""");

        int number = scanner.nextInt();

        Comparator<Contact> comparator;

        if (number == 1) {
            comparator = Comparator.comparing(c -> c.user().name().toLowerCase());
        } else if (number == 2) {
            comparator = Comparator.comparing(c -> c.user().surName().toLowerCase());
        } else if (number == 3) {
            comparator = Comparator.comparing(Contact::phone);
        } else if (number == 4) {
            comparator = Comparator.comparing(Contact::birthDay);
        } else {
            System.out.println("The search criteria will not be found");
            return;
        }

        contactRepository.sortContact(comparator);
        System.out.println("Contacts have been sorted");
        showContact(contactRepository);
    }
    private static void showContact(ContactRepository contactRepository){
        List<Contact> contacts = contactRepository.searchContact("");
        System.out.println("Contacts:");

        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = contacts.get(i);
            System.out.println("Contacts " + (i + 1) + ":\n" +
                    "Name: " + contact.user().name() + "\n" +
                    "Surname: " + contact.user().surName() + "\n" +
                    "Phone: " + contact.phone() + "\n" +
                    "Birthday: " + contact.birthDay().format(date) + "\n");
        }

    }
}


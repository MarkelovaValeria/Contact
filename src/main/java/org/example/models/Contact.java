package org.example.models;

import java.time.LocalDate;

public record Contact(User user, String phone, LocalDate birthDay, String group) {

}

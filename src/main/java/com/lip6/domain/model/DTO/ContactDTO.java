package com.lip6.domain.model.DTO;

public class ContactDTO {
    private final long id;
    private final String email;
    private final String firstName;
    private final String lastName;

    public ContactDTO(long id, String email, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "ContactDTO [id=" + id + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }
}

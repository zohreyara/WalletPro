package com.wallet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private int mobileNumber;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull(message = "Date of birth cannot be null")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    private Gender gender;

    private MilitaryStatus militaryStatus;

    @NotBlank
    @Email(message = "Email should be valid")
    private String email;

    // getters and setters
    public Long getId() {

        return id;
    }

    public void setId(long id) {

        this.id = id;
    }

    public int getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(int mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public MilitaryStatus getMilitaryStatus() {
        return militaryStatus;
    }

    public void setMilitaryStatus(MilitaryStatus militaryStatus) {
        this.militaryStatus = militaryStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    // Validation logic for military status and age
    @PrePersist
    @PreUpdate
    private void validateMilitaryStatus() {
        if ("male".equalsIgnoreCase(String.valueOf(this.gender)) && isOver18()) {
            if (this.militaryStatus == null || this.militaryStatus.isEmpty()) {
                throw new IllegalArgumentException("Military status must be specified for men over 18 years old.");
            }
        }
    }

    private boolean isOver18() {
        // Calculate age based on dateOfBirth and check if over 18
        Date currentDate = new Date();
        long ageInMillis = currentDate.getTime() - this.dateOfBirth.getTime();
        int age = (int) (ageInMillis / (1000L * 60 * 60 * 24 * 365));
        return age >= 18;
    }
}

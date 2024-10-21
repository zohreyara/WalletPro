package com.wallet.service;

import com.wallet.entity.Person;
import com.wallet.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    // Get all persons
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    // Get person by ID
    public Person getPersonById(Long id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        return optionalPerson.orElse(null); // Return null if not found
    }

    // Create a new person with validation
    public Person createPerson(Person person) {
        validatePerson(person); // Validate the person before saving
        return personRepository.save(person);
    }

    // Update an existing person with validation
    public Person updatePerson(Long id, Person updatedPerson) {
        if (personRepository.existsById(id)) {
            updatedPerson.setId(id); // Set the ID for the updated entity
            validatePerson(updatedPerson); // Validate before updating
            return personRepository.save(updatedPerson);
        }
        return null;
    }

    // Delete a person by ID
    public boolean deletePerson(Long id) {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Validate the Person entity
    private void validatePerson(Person person) {
        Set<ConstraintViolation<Person>> violations = validator.validate(person);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Person> violation : violations) {
                sb.append(violation.getMessage()).append("\n");
            }
            throw new IllegalArgumentException("Validation errors: \n" + sb.toString());
        }

        if ("male".equalsIgnoreCase(String.valueOf(person.getGender())) && isOver18(person)) {
            if (person.getMilitaryStatus() == null || person.getMilitaryStatus().isEmpty()) {
                throw new IllegalArgumentException("Military status must be specified for men over 18 years old.");
            }
        }
    }

    private boolean isOver18(Person person) {
        long ageInMillis = new Date().getTime() - person.getDateOfBirth().getTime();
        int age = (int) (ageInMillis / (1000L * 60 * 60 * 24 * 365));
        return age >= 18;
    }

}
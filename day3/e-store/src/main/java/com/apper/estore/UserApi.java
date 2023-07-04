package com.apper.estore;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("user")
public class UserApi {

    @PostMapping
    public CreateUserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        LocalDate birthDate = LocalDate.parse(request.getBirthDate());
        LocalDate minimumAgeDate = LocalDate.now().minusYears(15);

        if (birthDate.isAfter(minimumAgeDate)) {
            throw new InvalidUserAgeException("User must be at least 15 years old.");
        }

        return new CreateUserResponse("RANDOM_STRING");
    }
}

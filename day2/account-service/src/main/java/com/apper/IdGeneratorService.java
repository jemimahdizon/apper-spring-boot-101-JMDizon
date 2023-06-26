package com.apper;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@Service
public class IdGeneratorService {
    private static final Set<String> generatedCodes = new HashSet<>();

    // Generates a random code of the specified length that is not already generated.
    public String generateRandomCharacters(int length) {
        String randomCharacters;
        Random random = new Random();

        do {
            randomCharacters = UUID.randomUUID().toString().replaceAll("-", "").substring(0, length);
        } while (generatedCodes.contains(randomCharacters));

        generatedCodes.add(randomCharacters);

        return randomCharacters;
    }

    // Generates the next unique ID using a random UUID.
    public String getNextId() {
        return UUID.randomUUID().toString();
    }
}

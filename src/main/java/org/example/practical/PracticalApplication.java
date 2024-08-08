package org.example.practical;

import org.example.practical.entities.Saving;
import org.example.practical.repositories.SavingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class PracticalApplication {

    private AtomicInteger custnoCounter = new AtomicInteger(101); // Start from 101

    public static void main(String[] args) {
        SpringApplication.run(PracticalApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(SavingRepository savingRepository) {
        return args -> {
            savingRepository.save(new Saving(generateCustno(), "Jasper Diaz", 15000.0, 5, "Saving-Deluxe"));
            savingRepository.save(new Saving(generateCustno(), "Zanip Mendez", 5000.0, 2, "Saving-Deluxe"));
            savingRepository.save(new Saving(generateCustno(), "Geronima Esper", 6000.0, 5, "Saving-Regular"));
            savingRepository.findAll().forEach(p -> {
                System.out.println(p.getCustname());
            });
        };
    }

    private String generateCustno() {
        return String.valueOf(custnoCounter.getAndIncrement());
    }
}

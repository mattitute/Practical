package org.example.practical.repositories;

import org.example.practical.entities.Saving;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingRepository extends JpaRepository<Saving, String> {
}

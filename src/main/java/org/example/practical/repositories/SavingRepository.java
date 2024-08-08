package org.example.practical.repositories;
import org.example.practical.entities.Saving;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface SavingRepository extends JpaRepository<Saving, Long> {
    List<Saving> findSavingByCustno (long kw);
}

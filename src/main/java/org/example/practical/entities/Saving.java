package org.example.practical.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Saving {
    @Id
    private String custno;
    private String custname;
    private double cdep;
    private int nyears;
    private String savtype;
}

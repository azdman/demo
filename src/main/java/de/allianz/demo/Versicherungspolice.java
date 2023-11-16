package de.allianz.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Versicherungspolice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String versicherungsnummer;
    private int versicherungsdauer;
    private double versicherungsbetrag;
    private double beitragssatz;
}

package de.allianz.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VersicherungspoliceRepository extends JpaRepository<Versicherungspolice, Long> {
}

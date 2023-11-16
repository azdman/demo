package de.allianz.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/versicherungspolicen")
public class VersicherungspoliceController {

    @Autowired
    private VersicherungspoliceRepository versicherungspoliceRepository;
    
    @Autowired
    private JmsTemplate jmsTemplate;

    @PostMapping
    public ResponseEntity<?> erstelleVersicherungspolice(@RequestBody Versicherungspolice versicherungspolice) {
        try {
            Versicherungspolice erstellteVersicherungspolice = versicherungspoliceRepository.save(versicherungspolice);
            
            jmsTemplate.convertAndSend("reportingQueue", new ReportingNachricht("Versicherungpolice erstellt"));
            return new ResponseEntity<>(erstellteVersicherungspolice, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Fehler beim Erstellen der Versicherungspolice: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Holt eine Police anhand der Versicherungsnummer.
     * 
     * @param id die Versicherungsnummer
     * @return die Police
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> holeVersicherungspolice(@PathVariable Long id) {
        try {
            Versicherungspolice versicherungspolice = versicherungspoliceRepository.getById(id);
            jmsTemplate.convertAndSend("reportingQueue", new ReportingNachricht("Versicherungpolice gelesen"));
            return new ResponseEntity<>(versicherungspolice, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Versicherungspolice nicht gefunden: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @JmsListener(destination = "reportingQueue")
    public void receiveReportingNachricht(ReportingNachricht reportingNachricht) {
        System.out.println("Reportingnachricht empfangen: " + reportingNachricht.getNachricht());
    }
}

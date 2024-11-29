package org.iit.ticketKeeper.controller.vendor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.iit.ticketKeeper.dto.Session;
import org.iit.ticketKeeper.dto.User;
import org.iit.ticketKeeper.entity.SessionEntity;
import org.iit.ticketKeeper.protection.SessionProjection;
import org.iit.ticketKeeper.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/vendor")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class SessionController {

    private final SessionService service;

    @PostMapping("/session")
    public ResponseEntity<Map<String, String>> createSession(@RequestParam("ticketImage") MultipartFile file,  @RequestParam("userData") String userData) {
        System.out.println("this is session : \n\n\n\n\n\n\n");
        System.out.println("this is image : "+ file);
        System.out.println("this is user Data : "+ userData);

        Map<String, String> response = new HashMap<>();

        try {
            // Initialize ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();

            // Convert JSON string to Java object
            Session eventDetails = objectMapper.readValue(userData, Session.class);
            eventDetails.setImageType(file.getContentType());
            eventDetails.setTicketImage(file.getBytes());

            service.saveSession(eventDetails);
            response.put("message", "Done");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Session create failed!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    @GetMapping("/data")
    public ResponseEntity<Map<String, List<Session>>> getSessionData(){
        Map<String, List<Session>> response = new HashMap<>();
        List<Session> sessions = service.getSessionDetails();

        response.put("data", sessions);
        return ResponseEntity.ok(response);
    }

    @GetMapping("data/{id}")
    public ResponseEntity<Map<String , Session>> getSessionById(@PathVariable Long id){
        System.out.println("this is id : " + id.getClass().getName());
        Session session = service.getSessionById(id);
        Map<String , Session> response = new HashMap<>();

        if (session == null)  ResponseEntity.notFound().build();

        response.put("data", session);
        return ResponseEntity.ok(response);
    }
}

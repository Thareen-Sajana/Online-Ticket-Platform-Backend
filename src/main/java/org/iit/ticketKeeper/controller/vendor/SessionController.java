package org.iit.ticketKeeper.controller.vendor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.iit.ticketKeeper.ProducerConsumer.Consumer;
import org.iit.ticketKeeper.ProducerConsumer.Producer;
import org.iit.ticketKeeper.ProducerConsumer.TicketPool;
import org.iit.ticketKeeper.dto.Buy;
import org.iit.ticketKeeper.dto.Session;
import org.iit.ticketKeeper.dto.SessionManage;
import org.iit.ticketKeeper.dto.User;
import org.iit.ticketKeeper.protection.BuyDetailProtection;
import org.iit.ticketKeeper.repository.SessionRepository;
import org.iit.ticketKeeper.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api/v1/vendor")
//@RequiredArgsConstructor
public class SessionController {

    private Map<Long, TicketPool> ticketPools = new HashMap();

    @Autowired
    private SessionService service;

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

    @GetMapping("/data/{id}")
    public ResponseEntity<Map<String , Session>> getSessionById(@PathVariable Long id){
        System.out.println("this is id : " + id.getClass().getName());
        Session session = service.getSessionById(id);
        Map<String , Session> response = new HashMap<>();

        if (session == null)  ResponseEntity.notFound().build();

        response.put("data", session);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/session-data")
    public ResponseEntity<Map<String, List<SessionManage>>> getVendorSessionData(@RequestBody User user){
        Map<String, List<SessionManage>> response = new HashMap<>();
        System.out.println("this is required endpoint : *************************");
        List<SessionManage> sessions = service.getSessionDataByEmail(user);

        response.put("data", sessions);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/remove-session/{id}")
    public ResponseEntity<Map<String , String >> removeSession(@PathVariable Long id){
        System.out.println("this is id : " + id.getClass().getName());
        service.removeSession(id);
        Map<String , String > response = new HashMap<>();

        response.put("data", "Done");
        return ResponseEntity.ok(response);
    }


    @GetMapping("/start-session/{id}")
    public ResponseEntity<Map<String , String >> startSession(@PathVariable Long id){
        System.out.println("this is id : " + id.getClass().getName());
        service.startSession(id);
        Map<String , String > response = new HashMap<>();

        Session session = service.getSessionById(id);

        if(!ticketPools.containsKey(id)){
            TicketPool pool = new TicketPool(service, session.getTicketPoolCapacity(), id);

            Runnable producer = new Producer(pool, session.getTotalTicket(),session.getTicketReleaseRate()*1000);
            ticketPools.put(id, pool);

            System.out.println("this is map : " + ticketPools);
            Thread producerThread = new Thread(producer);
            producerThread.start();
        }



        response.put("data", "Done");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stop-session/{id}")
    public ResponseEntity<Map<String , String >> stopSession(@PathVariable Long id){
        System.out.println("this is id : " + id.getClass().getName());
        service.stopSession(id);
        Map<String , String > response = new HashMap<>();

        response.put("data", "Done");
        return ResponseEntity.ok(response);
    }


    SessionController() {
        System.out.println("The constructor executed......");
    }

    @GetMapping("test/{id}")
    public String test(@PathVariable Integer id){

//        Runnable consumer = new Consumer(pool,id,1000);
//        Thread thread = new Thread(consumer);
//        thread.start();
        return "Done" + id;
    }

    //@Autowired
    //private SessionRepository repository;

    @PostMapping("/buy")
    public ResponseEntity<Map<String , String >> buy(@RequestBody Buy request){
        System.out.println("this is working qty");
        Map<String , String > response = new HashMap<>();
//        Optional<BuyDetailProtection> data = repository.findBySessionId(request.getId());
//        System.out.println("this is data : "+ data.get().getTicketPoolCapacity());

        Session session = service.getSessionById(request.getId());

        Runnable consumer = new Consumer(ticketPools.get(request.getId()), request.getQty(), session.getCustomerReleaseRate()*1000);
        Thread thread = new Thread(consumer);
        thread.start();

        response.put("data", "You buy " + request.getQty() + " tickets and ID : " + request.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/hello")
    public String hello(){
        System.out.println("this is working ");
        return "Hello word";
    }
}

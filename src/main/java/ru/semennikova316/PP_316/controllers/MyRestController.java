package ru.semennikova316.PP_316.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.semennikova316.PP_316.Model.User;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class MyRestController {

    private static String cookie;
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/users")
    public String getUsers() {

        User user3 = new User(3L, "James", "Brown", (byte)11);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response =
                restTemplate.exchange("http://94.198.50.185:7081/api/users", HttpMethod.GET, entity, String.class);

        cookie = response.getHeaders().get("Set-Cookie").get(0).split(";")[0];
        HttpHeaders my = new HttpHeaders();
        my.add("Cookie", cookie);
        my.add("Content-Type", "application/json");

        String post = restTemplate.exchange(
                "http://94.198.50.185:7081/api/users", HttpMethod.POST, new HttpEntity<>(user3, my), String.class).getBody();

        ResponseEntity<String> response1 =
                restTemplate.exchange("http://94.198.50.185:7081/api/users", HttpMethod.GET, new HttpEntity<>(my), String.class);

        user3.setName("Thomas");
        user3.setLastName("Shelby");
        String put = restTemplate.exchange(
                "http://94.198.50.185:7081/api/users", HttpMethod.PUT, new HttpEntity<>(user3, my), String.class).getBody();
        ResponseEntity<String> response2 =
                restTemplate.exchange("http://94.198.50.185:7081/api/users", HttpMethod.GET, new HttpEntity<>(my), String.class);

        String delete = restTemplate.exchange(
                "http://94.198.50.185:7081/api/users/3", HttpMethod.DELETE, new HttpEntity<User>(my), String.class
        ).getBody();
        ResponseEntity<String> response3 =
                restTemplate.exchange("http://94.198.50.185:7081/api/users", HttpMethod.GET, new HttpEntity<>(my), String.class);
        return post + put + delete;
    }

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", cookie);
        HttpEntity<User> entity = new HttpEntity<>(user,headers);
        System.out.println(cookie);
        return restTemplate.exchange(
                "http://94.198.50.185:7081/api/users", HttpMethod.POST, entity, String.class);

    }

    @PutMapping("/users")
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", cookie);
        HttpEntity<User> entity = new HttpEntity<>(user,headers);
        System.out.println(cookie);
        return restTemplate.exchange(
                "http://94.198.50.185:7081/api/users", HttpMethod.PUT, entity, String.class, Map.of("id", user.getId()));
    }

}

package dio.jpa.jpa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping
    public String welcome() {
        return "testing Spring Boot Web API";
    }

    @GetMapping("/test/users")
    public String users() {
        return "Authorized user";
    }

    @GetMapping("/test/managers")
    public String managers() {
        return "Authorized manager";
    }
}

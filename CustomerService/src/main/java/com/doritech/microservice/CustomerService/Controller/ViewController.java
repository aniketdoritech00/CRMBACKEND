package com.doritech.microservice.CustomerService.Controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin(allowedHeaders = "*")	
public class ViewController {

    @GetMapping(value = {"/", "/{path:[^\\.]*}"})
    public String forwardToIndex() {
        return "forward:/index.html";
    }
}
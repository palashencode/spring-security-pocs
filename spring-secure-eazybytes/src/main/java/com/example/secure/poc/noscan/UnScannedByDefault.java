package com.example.secure.poc.noscan;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * For this to be scanned along with default package, both need to be mentioned here
 * @ComponentScan(basePackages = {"com.example.secure.poc.code", "com.example.secure.poc.main"})
 */
@RestController
public class UnScannedByDefault {
    @GetMapping("/unscannedclass/info")
    public String info(){
        return "This class is not scanned by default as it is outside the EazyBankBackendApplication package, use @ComponentScan";
    }

}

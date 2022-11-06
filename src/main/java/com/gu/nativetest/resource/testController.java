package com.gu.nativetest.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class testController {

    @GetMapping("/show")
    public Mono<String> show(){
        return Mono.just("ok");
    }
}

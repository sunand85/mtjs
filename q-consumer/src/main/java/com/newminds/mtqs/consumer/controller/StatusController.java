package com.newminds.mtqs.consumer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Created by Sunand on 12/09/20
 **/
@Slf4j
@RestController
@RequestMapping("/consumer")
public class StatusController {

  @GetMapping("/status")
  public Mono<String> statusCheck() {
    return Mono.just("Thumbs Up!");
  }
}

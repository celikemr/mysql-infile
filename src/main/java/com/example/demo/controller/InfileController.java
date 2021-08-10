package com.example.demo.controller;

import com.example.demo.service.InfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/infile")
@RequiredArgsConstructor
public class InfileController {
  private final InfileService infileService;

  @GetMapping(value = "/test")
  public ResponseEntity<Object> convertToWebUrl() {
    infileService.insertWithInfile();
    return ResponseEntity.ok().build();
  }
}

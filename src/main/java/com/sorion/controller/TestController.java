package com.sorion.controller;

import com.sorion.dto.SoundResponse;
import com.sorion.dto.SoundResultRequest;
import com.sorion.service.SoundTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tests")
public class TestController {

    private final SoundTestService soundTestService;


    @GetMapping("/{categoryId}")
    public ResponseEntity<SoundResponse> findByCategoryId(@PathVariable("categoryId") Long categoryId) {
        return new ResponseEntity<>(soundTestService.findTestSounds(categoryId), HttpStatus.OK);
    }

    @PostMapping("/result")
    public ResponseEntity<Void> saveResult(@RequestBody SoundResultRequest request) {
        soundTestService.testResultSave(request.userId(), request.soundId(), request.sensitivity());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

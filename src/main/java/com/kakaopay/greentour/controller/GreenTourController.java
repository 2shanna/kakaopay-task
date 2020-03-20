package com.kakaopay.greentour.controller;

import com.kakaopay.greentour.dto.EcoInformation;
import com.kakaopay.greentour.service.GreenTourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("greentour")
public class GreenTourController {

    @Autowired
    private
    GreenTourService greenTourService;

    @GetMapping("ecoinfo/region/{regionCd}")
    public ResponseEntity findEcoInfoByRegionCd(@PathVariable String regionCd) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(greenTourService.findEcoInfoByRegionCd(regionCd), headers, HttpStatus.OK);
    }

    @PostMapping("ecoinfo")
    public ResponseEntity registerEcoInfo(@RequestBody EcoInformation ecoInfo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(greenTourService.registerEcoInfo(ecoInfo), headers, HttpStatus.CREATED);
    }

    @PutMapping("ecoinfo")
    public ResponseEntity updateEcoInfo(@RequestBody EcoInformation ecoInfo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(greenTourService.updateEcoInfo(ecoInfo), headers, HttpStatus.CREATED);
        // TODO return 404 when there was no data to update
    }

    @GetMapping("search/region")
    public ResponseEntity findByRegionName(@RequestParam String regionName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(greenTourService.findByRegionName(regionName), headers, HttpStatus.OK);
    }

    @GetMapping("search/keyword")
    public ResponseEntity findByKeyword(@RequestParam String keyword) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(greenTourService.findByKeyword(keyword), headers, HttpStatus.OK);
    }
}

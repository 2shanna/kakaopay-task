package com.kakaopay.greentour.controller;

import com.kakaopay.greentour.dto.EcoInformation;
import com.kakaopay.greentour.service.GreenTourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("greentour")
public class GreenTourController {

    @Autowired
    private
    GreenTourService greenTourService;

    @GetMapping(value = "ecoinfo/region/{regionCd}")
    public ResponseEntity findEcoInfoByRegionCd(@PathVariable String regionCd) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(greenTourService.findEcoInfoByRegionCd(regionCd), headers, HttpStatus.OK);
    }

    @PostMapping("ecoinfo")
    public ResponseEntity registerEcoInfo(@RequestBody EcoInformation ecoInfo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        EcoInformation result = greenTourService.registerEcoInfo(ecoInfo);
        if (result == null) {
            return new ResponseEntity<>("program id is already in use.", headers, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(greenTourService.registerEcoInfo(ecoInfo), headers, HttpStatus.CREATED);
        }
    }

    @PutMapping("ecoinfo")
    public ResponseEntity updateEcoInfo(@RequestBody EcoInformation ecoInfo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        EcoInformation result = greenTourService.updateEcoInfo(ecoInfo);
        if (result == null) {
            return new ResponseEntity<>("program id to update does not exist.", headers, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(greenTourService.updateEcoInfo(ecoInfo), headers, HttpStatus.CREATED);
        }
    }

    @GetMapping("search/region")
    public ResponseEntity findByRegionName(@RequestParam String regionName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(greenTourService.findByRegionName(regionName), headers, HttpStatus.OK);
    }

    @GetMapping("search/outline")
    public ResponseEntity findByOutline(@RequestParam String keyword) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(greenTourService.findProgramByOutlineKeyword(keyword), headers, HttpStatus.OK);
    }

    @GetMapping("search/detail")
    public ResponseEntity findByDetail(@RequestParam String keyword) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(greenTourService.findProgramByDetailKeyword(keyword), headers, HttpStatus.OK);
    }
}

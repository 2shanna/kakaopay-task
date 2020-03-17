package com.kakaopay.greentour.controller;

import com.kakaopay.greentour.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("upload")
public class FileUploadController {

    @Autowired
    FileUploadService fileUploadService;

    @PostMapping(value = "/")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        fileUploadService.store(file).forEach(System.out::println);
        return fileUploadService.store(file).toString();
    }
}

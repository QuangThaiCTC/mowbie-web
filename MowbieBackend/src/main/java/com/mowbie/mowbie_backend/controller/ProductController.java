package com.mowbie.mowbie_backend.controller;

import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/products")
public class ProductController {
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + File.separator + "uploads";


}



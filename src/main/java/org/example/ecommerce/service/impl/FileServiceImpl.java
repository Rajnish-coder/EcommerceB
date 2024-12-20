package org.example.ecommerce.service.impl;

import org.example.ecommerce.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        // File name of current/original file
        String originalFilename = file.getOriginalFilename();

        // Generate a unique file name
        String randomId = UUID.randomUUID().toString().substring(0,8);
        String fileName = randomId.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));
        String filePath = path + File.separator + fileName;

        // Check if folder exists otherwise create it
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdirs();
        }

        // Upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));

        // return the file name
        return fileName;
    }
}

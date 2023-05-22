package com.example.Ex28;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${fileDownloadFolder}")
    private String fileDownloadFolder;

    @Value("${fileUploadFolder}")
    private String fileUploadFolder;


    public String upload(MultipartFile file) throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String newFileName = UUID.randomUUID().toString();
        String completeFileName = newFileName + "." + extension;


        Path finalFolder = Path.of(fileUploadFolder);

        if (!Files.exists(finalFolder)) {
            throw new IOException("Final folder does not exists");
        }
        if (!Files.isDirectory(finalFolder)) {
            throw new IOException("Final folder is not a directory");
        }

        Path finalDestination = finalFolder.resolve(completeFileName);
        if (Files.exists(finalDestination)) {
            throw new IOException("File conflict");
        }

        file.transferTo(finalDestination.toFile());
        return completeFileName;
    }

    public byte[] download(String fileName) throws IOException {
        File fileFromRepository = new File(fileDownloadFolder + "\\" + fileName);
        if (!fileFromRepository.exists()) throw new IOException("File does not exists");
        return IOUtils.toByteArray(new FileInputStream(fileFromRepository));

    }

}
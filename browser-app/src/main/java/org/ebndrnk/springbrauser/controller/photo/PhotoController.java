package org.ebndrnk.springbrauser.controller.photo;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.springbrauser.service.photo.PhotoService;
import org.ebndrnk.springbrauser.service.util.LogService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;
    private final LogService logService;

    @PostMapping("/add-photo")
    public ResponseEntity<String> addPhoto(@RequestParam(value = "file") MultipartFile multipartFile) {
        logService.logInfo("Trying to add photo");
        logService.logDebug("Received file: " + multipartFile.getOriginalFilename());

        try {
            String photoUrl = photoService.addPhoto(multipartFile);
            logService.logInfo("Photo added successfully: " + photoUrl);
            return ResponseEntity.ok(photoUrl);
        } catch (IOException e) {
            logService.logError("Error adding photo" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get-photo")
    public ResponseEntity<byte[]> getPhoto() {
        logService.logInfo("Trying to retrieve photo");

        byte[] photoData = photoService.getPhoto();
        if (photoData != null) {
            logService.logInfo("Photo retrieved successfully");
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(photoData);
        } else {
            logService.logWarn("Photo not found");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/delete-photo")
    public ResponseEntity<Void> deletePhoto() {
        logService.logInfo("Trying to delete photo");

        photoService.deletePhoto();
        logService.logInfo("Photo deleted successfully");
        return ResponseEntity.noContent().build();
    }
}
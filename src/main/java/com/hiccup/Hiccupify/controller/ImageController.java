package com.hiccup.Hiccupify.controller;

import com.hiccup.Hiccupify.dto.ImageDto;
import com.hiccup.Hiccupify.exception.ResourceNotFound;
import com.hiccup.Hiccupify.model.Image;
import com.hiccup.Hiccupify.response.ApiResponse;
import com.hiccup.Hiccupify.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final IImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages( @RequestParam Long productId,@RequestBody List<MultipartFile> files){
        try {
            List<ImageDto> imageDtos=imageService.saveImages(files,productId);
            return ResponseEntity.ok(new ApiResponse("Upload Sucessful!!",imageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload failed",e.getMessage()));
        }
    }

    @Transactional(readOnly = true)
    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
        Image image= imageService.getImageById(imageId);
        ByteArrayResource resource=new ByteArrayResource(image.getImage().getBytes(1,(int)image.getImage().length()));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                //this is to tell browser whether download or display the
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+image.getFileName()+"\"")
        .body(resource);
    }

    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file){
        try {
            Image image=imageService.getImageById(imageId);
            if(image!=null){
                imageService.updateImage(file, imageId);
                return ResponseEntity.ok().body(new ApiResponse("Update success!!",null));
            }
        } catch (ResourceNotFound e) {
           return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Update failed!!",null));
    }

    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId){
        try {
            Image image=imageService.getImageById(imageId);
            if(image!=null){
                imageService.deleteImageById(imageId);
                return ResponseEntity.ok(new ApiResponse("Image deleted!",null));
            }
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Delete failed!",null));
    }
}

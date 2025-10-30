package com.hiccup.Hiccupify.service.image;

import com.hiccup.Hiccupify.dto.ImageDto;
import com.hiccup.Hiccupify.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(List<MultipartFile> files, Long productId);
    void updateImage(MultipartFile file, Long imageId);
}

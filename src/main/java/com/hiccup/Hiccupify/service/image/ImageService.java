package com.hiccup.Hiccupify.service.image;

import com.hiccup.Hiccupify.dto.ImageDto;
import com.hiccup.Hiccupify.exception.ResourceNotFound;
import com.hiccup.Hiccupify.model.Image;
import com.hiccup.Hiccupify.model.Product;
import com.hiccup.Hiccupify.repository.ImageRepository;
import com.hiccup.Hiccupify.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{
    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;
    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(()->new ResourceNotFound("Image not found with id: "+id));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete,()-> {throw new ResourceNotFound("Image not found with id: "+id);});
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        Product product=productRepository.findById(productId).orElseThrow();
        List<ImageDto> saveImageDtos=new ArrayList<>();
        try {
            for(MultipartFile file: files){
                Image image=new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setProduct(product);
                image.setImage(new SerialBlob(file.getBytes()));
                String buildDownloadUrl="/api/v1/images/image/download/";
                //the id of image is null here so we need to set the downloadurl 2 time
                //to create proper download with proper image id.
                String downloadUrl=buildDownloadUrl+image.getId();
                image.setDownloadUrl(downloadUrl);

                Image savedImage=imageRepository.save(image);

                savedImage.setDownloadUrl(buildDownloadUrl+savedImage.getImage());

                imageRepository.save(savedImage);

                ImageDto imageDto=new ImageDto();
                imageDto.setImageName(savedImage.getFileName());
                imageDto.setImageId(savedImage.getId());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                saveImageDtos.add(imageDto);
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }

        return saveImageDtos;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image=imageRepository.findById(imageId).orElseThrow(()->new ResourceNotFound("Image not found with id: "+imageId));
        try{
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        }
        catch (IOException | SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}

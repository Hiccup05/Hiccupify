package com.hiccup.Hiccupify.repository;

import com.hiccup.Hiccupify.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}

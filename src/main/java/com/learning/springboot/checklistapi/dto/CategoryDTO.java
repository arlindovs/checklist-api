package com.learning.springboot.checklistapi.dto;

import com.learning.springboot.checklistapi.entity.CategoryEntity;
import lombok.Builder;
import lombok.Getter;

import jakarta.validation.constraints.NotBlank;

@Getter
@Builder
public class CategoryDTO {

    private String guid;

    @NotBlank(message = "Nome da categoria não pode ser null ou vazio")
    private String name;

    public static CategoryDTO toDTO(CategoryEntity categoryEntity){
        return CategoryDTO.builder()
                .guid(categoryEntity.getGuid())
                .name(categoryEntity.getName())
                .build();
    }
}
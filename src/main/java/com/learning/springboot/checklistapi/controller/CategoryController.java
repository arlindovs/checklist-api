package com.learning.springboot.checklistapi.controller;

import com.learning.springboot.checklistapi.dto.CategoryDTO;
import com.learning.springboot.checklistapi.dto.NewResourceDTO;
import com.learning.springboot.checklistapi.entity.CategoryEntity;
import com.learning.springboot.checklistapi.service.CategoryService;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Classe responsável por gerenciar as requisições relacionadas a categorias.
 */
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Retorna todas as categorias cadastradas.
     * @return ResponseEntity<List<CategoryDTO>> - Lista de categorias.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> resp = StreamSupport.stream(this.categoryService.findAllCategories().spliterator(), false)
                .map(categoryEntity -> CategoryDTO.toDTO(categoryEntity))
                .collect(Collectors.toList());
        return new ResponseEntity<List<CategoryDTO>>(resp, HttpStatus.OK);
    }

    /**
     * Adiciona uma nova categoria.
     * @param categoryDTO - Categoria a ser adicionada.
     * @return ResponseEntity<String> - GUID da nova categoria.
     */
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NewResourceDTO> addNewCategory(@RequestBody CategoryDTO categoryDTO) {

        CategoryEntity newCategory = this.categoryService.addNewCategory(categoryDTO.getName());

        return new ResponseEntity<>(new NewResourceDTO(newCategory.getGuid()), HttpStatus.CREATED);
    }

    /**
     * Atualiza uma categoria existente.
     * @param categoryDTO - Categoria a ser atualizada.
     * @return ResponseEntity<Void> - Resposta vazia.
     */
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateCategory(@RequestBody CategoryDTO categoryDTO) {

        if(!StringUtils.hasText(categoryDTO.getGuid())){
            throw new ValidationException("Categoria não pode ser null ou vazio");
        }

        this.categoryService.updateCategory(categoryDTO.getGuid(), categoryDTO.getName());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Deleta uma categoria existente.
     * @param guid - GUID da categoria a ser deletada.
     * @return ResponseEntity<Void> - Resposta vazia.
     */
    @DeleteMapping(value = "{guid}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String guid) {

        this.categoryService.deleteCategory(guid);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

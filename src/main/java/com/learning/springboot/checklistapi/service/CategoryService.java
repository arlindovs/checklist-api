package com.learning.springboot.checklistapi.service;

import com.learning.springboot.checklistapi.entity.CategoryEntity;
import com.learning.springboot.checklistapi.entity.ChecklistItemEntity;
import com.learning.springboot.checklistapi.exception.CategoryServiceException;
import com.learning.springboot.checklistapi.exception.ResourceNotFoundException;
import com.learning.springboot.checklistapi.repository.CategoryRepository;
import com.learning.springboot.checklistapi.repository.ChecklistItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CategoryService {

    private ChecklistItemRepository checklistItemRepository;
    private CategoryRepository categoryRepository;

    public CategoryService(ChecklistItemRepository checklistItemRepository, CategoryRepository categoryRepository) {
        this.checklistItemRepository = checklistItemRepository;
        this.categoryRepository = categoryRepository;
    }

    public CategoryEntity addNewCategory(String name){
        if(!StringUtils.hasText(name)){
            throw new IllegalArgumentException("O nome da categoria não pode ficar vazio");
        }

        CategoryEntity newCategory = new CategoryEntity();
        newCategory.setGuid(UUID.randomUUID().toString());
        newCategory.setName(name);

        log.debug("Adding a new category with name [ name = {} ]", name);

        return this.categoryRepository.save(newCategory);
    }

    public CategoryEntity updateCategory(String guid, String name){
        if( guid == null || !StringUtils.hasText(name)){
            throw new IllegalArgumentException("Parâmetros inválidos fornecidos para atualizar uma categoria");
        }

        CategoryEntity retrievedCategory = this.categoryRepository.findByGuid(guid).orElseThrow(
                () -> new ResourceNotFoundException("Categoria não encontrada")
        );

        retrievedCategory.setName(name);

        log.debug("Updating category with guid [ guid = {}, newName = {} ]", guid, name);

        return this.categoryRepository.save(retrievedCategory);
    }

    public void deleteCategory(String guid){
        if(!StringUtils.hasText(guid)){
            throw new IllegalArgumentException("Category guid não pode ficar vazio");
        }

        CategoryEntity retrievedCategory = this.categoryRepository.findByGuid(guid).orElseThrow(
                () -> new ResourceNotFoundException("Category not found")
        );

        List<ChecklistItemEntity> checklistItens = this.checklistItemRepository.findByCategoryGuid(guid);
        if(!CollectionUtils.isEmpty(checklistItens)){
            throw new CategoryServiceException("Não é possível excluir a categoria com itens da lista de verificação associados");
        }

        log.debug("Deleting category with guid [ guid = {} ]", guid);

        this.categoryRepository.delete(retrievedCategory);
    }

    public List<CategoryEntity> findAllCategories(){
        log.debug("Finding all categories");

        return this.categoryRepository.findAll();
    }

    public CategoryEntity findCategoryByGuid(String guid){
        if(!StringUtils.hasText(guid)){
            throw new IllegalArgumentException("Category guid não pode ficar vazio");
        }

        log.debug("Finding category with guid [ guid = {} ]", guid);

        return this.categoryRepository.findByGuid(guid).orElseThrow(
                () -> new ResourceNotFoundException("Category not found")
        );
    }
}

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

/**
 * Classe responsável por prover serviços relacionados a entidade CategoryEntity.
 */
@Slf4j
@Service
public class CategoryService {

    private final ChecklistItemRepository checklistItemRepository;
    private final CategoryRepository categoryRepository;

    public CategoryService(ChecklistItemRepository checklistItemRepository, CategoryRepository categoryRepository) {
        this.checklistItemRepository = checklistItemRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Adiciona uma nova categoria.
     * @param name Nome da categoria.
     * @return A nova categoria adicionada.
     * @throws IllegalArgumentException Se o nome da categoria estiver vazio.
     */
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

    /**
     * Atualiza uma categoria existente.
     * @param guid GUID da categoria a ser atualizada.
     * @param name Novo nome da categoria.
     * @return A categoria atualizada.
     * @throws IllegalArgumentException Se o GUID da categoria ou o novo nome estiverem vazios.
     * @throws ResourceNotFoundException Se a categoria não for encontrada.
     */
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

    /**
     * Deleta uma categoria existente.
     * @param guid GUID da categoria a ser deletada.
     * @throws IllegalArgumentException Se o GUID da categoria estiver vazio.
     * @throws ResourceNotFoundException Se a categoria não for encontrada.
     * @throws CategoryServiceException Se a categoria possuir itens de lista de verificação associados.
     */
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

    /**
     * Retorna todas as categorias existentes.
     * @return Lista de todas as categorias.
     */
    public List<CategoryEntity> findAllCategories(){
        log.debug("Finding all categories");

        return this.categoryRepository.findAll();
    }

    /**
     * Retorna uma categoria existente pelo seu GUID.
     * @param guid GUID da categoria a ser encontrada.
     * @return A categoria encontrada.
     * @throws IllegalArgumentException Se o GUID da categoria estiver vazio.
     * @throws ResourceNotFoundException Se a categoria não for encontrada.
     */
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

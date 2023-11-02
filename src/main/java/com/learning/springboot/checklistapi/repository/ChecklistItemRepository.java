package com.learning.springboot.checklistapi.repository;

import com.learning.springboot.checklistapi.entity.ChecklistItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface que estende PagingAndSortingRepository e JpaRepository para realizar operações de CRUD na entidade ChecklistItemEntity.
 */
@Repository
public interface ChecklistItemRepository extends PagingAndSortingRepository<ChecklistItemEntity, Long>, JpaRepository<ChecklistItemEntity, Long> {

    /**
     * Busca um item de checklist pelo seu guid.
     * @param guid O guid do item de checklist.
     * @return Um Optional contendo o item de checklist encontrado, ou vazio caso não exista.
     */
    Optional<ChecklistItemEntity> findByGuid(String guid);

    /**
     * Busca uma lista de itens de checklist pelo seu descrição e se está completo ou não.
     * @param description A descrição do item de checklist.
     * @param isCompleted Indica se o item de checklist está completo ou não.
     * @return Uma lista contendo os itens de checklist encontrados.
     */
    List<ChecklistItemEntity> findByDescriptionAndIsCompleted(String description, Boolean isCompleted);

    /**
     * Busca uma lista de itens de checklist pelo guid da sua categoria.
     * @param guid O guid da categoria dos itens de checklist.
     * @return Uma lista contendo os itens de checklist encontrados.
     */
    List<ChecklistItemEntity> findByCategoryGuid(String guid);
}

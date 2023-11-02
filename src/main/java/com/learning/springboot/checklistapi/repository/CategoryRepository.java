package com.learning.springboot.checklistapi.repository;

import com.learning.springboot.checklistapi.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interface que estende as interfaces PagingAndSortingRepository e JpaRepository para a entidade CategoryEntity.
 * Responsável por fornecer métodos de acesso ao banco de dados para a entidade CategoryEntity.
 */
@Repository
public interface CategoryRepository extends PagingAndSortingRepository<CategoryEntity, Long>, JpaRepository<CategoryEntity, Long> {

    /**
     * Busca uma entidade CategoryEntity pelo nome.
     * @param name nome da categoria a ser buscada.
     * @return um Optional contendo a entidade CategoryEntity encontrada ou vazio caso não exista.
     */
    Optional<CategoryEntity> findByName(String name);

    /**
     * Busca uma entidade CategoryEntity pelo guid.
     * @param guid guid da categoria a ser buscada.
     * @return um Optional contendo a entidade CategoryEntity encontrada ou vazio caso não exista.
     */
    Optional<CategoryEntity> findByGuid(String guid);
}
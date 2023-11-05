package com.learning.springboot.checklistapi.service;

import com.learning.springboot.checklistapi.entity.CategoryEntity;
import com.learning.springboot.checklistapi.entity.ChecklistItemEntity;
import com.learning.springboot.checklistapi.exception.ResourceNotFoundException;
import com.learning.springboot.checklistapi.repository.CategoryRepository;
import com.learning.springboot.checklistapi.repository.ChecklistItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ChecklistItemService {

    /**
     * Classe responsável por gerenciar as operações de CRUD para a entidade ChecklistItemEntity.
     */

    /*Na category service esta utilizando o construtor pois eh o mais recomendavel,
     * pois forca a injecao de dependencia, porem aqui vou utilizar o @Autowired
     * para ilustração das duas formas.*/
    @Autowired
    private ChecklistItemRepository checklistItemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Método responsável por validar os dados de um item de checklist.
     *
     * @param description descrição do item de checklist.
     * @param isCompleted status do item de checklist.
     * @param dateEnd data de conclusão do item de checklist.
     * @param categoryGuid guid da categoria do item de checklist.
     * @throws IllegalArgumentException caso algum dos parâmetros esteja inválido.
     */
    private void validateChecklistItemData(String description, Boolean isCompleted, LocalDate dateEnd, String categoryGuid){
        if(!StringUtils.hasText(description)){
            throw new IllegalArgumentException("A descrição do item da checklist não pode ficar vazia");
        }

        if(!StringUtils.hasText(categoryGuid)){
            throw new IllegalArgumentException("O guid da categoria não pode ficar vazio");
        }

        if(isCompleted == null){
            throw new IllegalArgumentException("O status do item da checklist não pode ficar vazio");
        }

        if(dateEnd == null){
            throw new IllegalArgumentException("A data de conclusão do item da checklist não pode ficar vazia");
        }
    }

    /**
     * Método responsável por atualizar um item de checklist.
     *
     * @param guid guid do item de checklist a ser atualizado.
     * @param description nova descrição do item de checklist.
     * @param isCompleted novo status do item de checklist.
     * @param dateEnd nova data de conclusão do item de checklist.
     * @param categoryGuid novo guid da categoria do item de checklist.
     * @return o item de checklist atualizado.
     * @throws IllegalArgumentException caso o guid esteja vazio.
     * @throws ResourceNotFoundException caso o item de checklist não seja encontrado.
     */
    public ChecklistItemEntity updateChecklistItem(String guid, String description, Boolean isCompleted, LocalDate dateEnd, String categoryGuid) {

        if (!StringUtils.hasText(guid)) {
            throw new IllegalArgumentException("O guid do item da checklist não pode ficar vazio");
        }

        ChecklistItemEntity retrievedItem = this.checklistItemRepository.findByGuid(guid).orElseThrow(
                () -> new ResourceNotFoundException("Item da checklist não encontrado")
        );

        if(!StringUtils.hasText(description)){
            retrievedItem.setDescription(description);
        }

        if(isCompleted != null){
            retrievedItem.setIsCompleted(isCompleted);
        }

        if(dateEnd != null){
            retrievedItem.setDateEnd(dateEnd);
        }

        if(!StringUtils.hasText(categoryGuid)){
            CategoryEntity retrievedCategory = this.categoryRepository.findByGuid(categoryGuid).orElseThrow(
                    () -> new ResourceNotFoundException("Categoria não encontrada")
            );

            retrievedItem.setCategory(retrievedCategory);
        }

        log.debug("Updating checklist item [ checklistItem = {} ]", retrievedItem.toString());

        return this.checklistItemRepository.save(retrievedItem);

    }

    /**
     * Método responsável por adicionar um novo item de checklist.
     *
     * @param description descrição do novo item de checklist.
     * @param isCompleted status do novo item de checklist.
     * @param dateEnd data de conclusão do novo item de checklist.
     * @param categoryGuid guid da categoria do novo item de checklist.
     * @return o novo item de checklist adicionado.
     * @throws IllegalArgumentException caso algum dos parâmetros esteja inválido.
     * @throws ResourceNotFoundException caso a categoria não seja encontrada.
     */
    public ChecklistItemEntity addNewChecklistItem(String description, Boolean isCompleted, LocalDate dateEnd, String categoryGuid){

        this.validateChecklistItemData(description, isCompleted, dateEnd, categoryGuid);

        CategoryEntity retrievedCategory = this.categoryRepository.findByGuid(categoryGuid).orElseThrow(
                () -> new ResourceNotFoundException("Categoria não encontrada")
        );

        ChecklistItemEntity checklistItemEntity = new ChecklistItemEntity();
        checklistItemEntity.setGuid(UUID.randomUUID().toString());
        checklistItemEntity.setDescription(description);
        checklistItemEntity.setDateEnd(dateEnd);
        checklistItemEntity.setDatePost(LocalDate.now());
        checklistItemEntity.setCategory(retrievedCategory);
        checklistItemEntity.setIsCompleted(isCompleted);

        log.debug("Adding a new checklist item [ checklistItem = {} ]", checklistItemEntity);

        return this.checklistItemRepository.save(checklistItemEntity);
    }

    /**
     * Método responsável por buscar um item de checklist pelo guid.
     *
     * @param guid guid do item de checklist a ser buscado.
     * @return o item de checklist encontrado.
     * @throws IllegalArgumentException caso o guid esteja vazio.
     * @throws ResourceNotFoundException caso o item de checklist não seja encontrado.
     */
    public ChecklistItemEntity findChecklistItemByGuid(String guid){
        if(!StringUtils.hasText(guid)){
            throw new IllegalArgumentException("O guid do item da checklist não pode ficar vazio");
        }

        return this.checklistItemRepository.findByGuid(guid).orElseThrow(
                () -> new ResourceNotFoundException("Item da checklist não encontrado")
        );
    }

    /**
     * Método responsável por buscar todos os itens de checklist.
     *
     * @return uma lista com todos os itens de checklist.
     */
    public List<ChecklistItemEntity> findAllChecklistItens(){
        return this.checklistItemRepository.findAll();
    }

    /**
     * Método responsável por deletar um item de checklist.
     *
     * @param guid guid do item de checklist a ser deletado.
     * @throws IllegalArgumentException caso o guid esteja vazio.
     * @throws ResourceNotFoundException caso o item de checklist não seja encontrado.
     */
    public void deleteChecklistItem(String guid){
        if(!StringUtils.hasText(guid)){
            throw new IllegalArgumentException("O guid do item da checklist não pode ficar vazio");
        }

        ChecklistItemEntity retrievedItem = this.checklistItemRepository.findByGuid(guid).orElseThrow(
                () -> new ResourceNotFoundException("Item da checklist não encontrado")
        );

        log.debug("Deleting checklist item [ guid = {} ]", guid);

        this.checklistItemRepository.delete(retrievedItem);
    }

    public void updateIsCompleteStatus(String guid, Boolean isComplete) {
        if(!StringUtils.hasText(guid)){
            throw new IllegalArgumentException("O guid do item da checklist não pode ficar vazio");
        }

        ChecklistItemEntity retrievedItem = this.checklistItemRepository.findByGuid(guid).orElseThrow(
                () -> new ResourceNotFoundException("Item da checklist não encontrado")
        );

        log.debug("Updating checklist item completed status [ guid = {}, isComplete = {} ]", guid, isComplete);

        retrievedItem.setIsCompleted(isComplete);

        this.checklistItemRepository.save(retrievedItem);
    }
}

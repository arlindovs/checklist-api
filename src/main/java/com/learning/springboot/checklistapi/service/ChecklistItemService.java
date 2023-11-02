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

    /*Na category service esta utilizando o construtor pois eh o mais recomendavel,
    * pois forca a injecao de dependencia, porem aqui vou utilizar o @Autowired
    * para ilustração das duas formas.*/
    @Autowired
    private ChecklistItemRepository checklistItemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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

        log.debug("Adding a new checklist item [ checklistItem = {} ]", checklistItemEntity);

        return this.checklistItemRepository.save(checklistItemEntity);
    }

    public ChecklistItemEntity findChecklistItemByGuid(String guid){
        if(!StringUtils.hasText(guid)){
            throw new IllegalArgumentException("O guid do item da checklist não pode ficar vazio");
        }

        return this.checklistItemRepository.findByGuid(guid).orElseThrow(
                () -> new ResourceNotFoundException("Item da checklist não encontrado")
        );
    }

    private List<ChecklistItemEntity> findAllChecklistItens(){
        return this.checklistItemRepository.findAll();
    }

    public void deleteChecklistitem(String guid){
        if(!StringUtils.hasText(guid)){
            throw new IllegalArgumentException("O guid do item da checklist não pode ficar vazio");
        }

        ChecklistItemEntity retrievedItem = this.checklistItemRepository.findByGuid(guid).orElseThrow(
                () -> new ResourceNotFoundException("Item da checklist não encontrado")
        );

        log.debug("Deleting checklist item [ guid = {} ]", guid);

        this.checklistItemRepository.delete(retrievedItem);
    }
}

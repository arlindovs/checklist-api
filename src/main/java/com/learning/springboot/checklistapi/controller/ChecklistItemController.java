package com.learning.springboot.checklistapi.controller;

import com.learning.springboot.checklistapi.dto.ChecklistItemDTO;
import com.learning.springboot.checklistapi.entity.ChecklistItemEntity;
import com.learning.springboot.checklistapi.service.ChecklistItemService;
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
 * Classe responsável por controlar as requisições relacionadas aos itens de checklist.
 */
@RestController
@RequestMapping("/api/v1/checklist-items")
public class ChecklistItemController {

    private final ChecklistItemService checklistItemService;

    public ChecklistItemController(ChecklistItemService checklistItemService) {
        this.checklistItemService = checklistItemService;
    }

    /**
     * Retorna todos os itens de checklist cadastrados.
     * @return ResponseEntity<List<ChecklistItemDTO>> - Lista de itens de checklist.
     */
    @GetMapping(value="", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ChecklistItemDTO>> getAllChecklistItems() {

        List<ChecklistItemDTO> resp = StreamSupport.stream(this.checklistItemService.findAllChecklistItens().spliterator(), false)
                .map(checklistItemEntity -> ChecklistItemDTO.toDTO(checklistItemEntity))
                .collect(Collectors.toList());

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    /**
     * Cria um novo item de checklist.
     * @param checklistItemDTO - DTO com as informações do item de checklist a ser criado.
     * @return ResponseEntity<String> - GUID do item de checklist criado.
     */
    @PostMapping(value="", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createNewChecklistItem(@RequestBody ChecklistItemDTO checklistItemDTO) {

        if(checklistItemDTO.getCategory() == null) {
            throw new ValidationException("Categoria não informada.");
        }
        ChecklistItemEntity newChecklistItem = this.checklistItemService.addNewChecklistItem(
                checklistItemDTO.getDescription(),
                checklistItemDTO.getIsCompleted(),
                checklistItemDTO.getDateEnd(),
                checklistItemDTO.getCategory().getGuid()
        );

        return new ResponseEntity<>(newChecklistItem.getGuid(), HttpStatus.CREATED);

    }

    /**
     * Atualiza um item de checklist existente.
     * @param checklistItemDTO - DTO com as informações do item de checklist a ser atualizado.
     * @return ResponseEntity<Void> - Resposta vazia com status NO_CONTENT.
     */
    @PutMapping(value="", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateChecklistItem(@RequestBody ChecklistItemDTO checklistItemDTO) {

        if(!StringUtils.hasText(checklistItemDTO.getGuid())) {
            throw new ValidationException("Checklist item guid é obrigatorio.");
        }

        this.checklistItemService.updateChecklistItem(
                checklistItemDTO.getGuid(),
                checklistItemDTO.getDescription(),
                checklistItemDTO.getIsCompleted(),
                checklistItemDTO.getDateEnd(),
                checklistItemDTO.getCategory() != null ? checklistItemDTO.getCategory().getGuid() : null
        );

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    /**
     * Deleta um item de checklist existente.
     * @param guid - GUID do item de checklist a ser deletado.
     * @return ResponseEntity<Void> - Resposta vazia com status NO_CONTENT.
     */
    @DeleteMapping(value="", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deteleChecklistItem(@PathVariable String guid){
        this.checklistItemService.deleteChecklistItem(guid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

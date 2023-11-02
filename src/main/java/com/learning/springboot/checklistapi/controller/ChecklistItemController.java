package com.learning.springboot.checklistapi.controller;

import com.learning.springboot.checklistapi.dto.ChecklistItemDTO;
import com.learning.springboot.checklistapi.service.ChecklistItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Classe responsável por controlar as requisições relacionadas aos itens de checklist.
 */
@RestController("/api/v1/checklist-items")
public class ChecklistItemController {

    private final ChecklistItemService checklistItemService;

    public ChecklistItemController(ChecklistItemService checklistItemService) {
        this.checklistItemService = checklistItemService;
    }

    /**
     * Método responsável por retornar todos os itens de checklist cadastrados.
     * @return ResponseEntity contendo a lista de itens de checklist e o status da requisição.
     */
    @GetMapping(value="", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ChecklistItemDTO>> getAllChecklistItems() {

        List<ChecklistItemDTO> resp = StreamSupport.stream(this.checklistItemService.findAllChecklistItens().spliterator(), false)
                .map(checklistItemEntity -> ChecklistItemDTO.toDTO(checklistItemEntity))
                .collect(Collectors.toList());

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
}

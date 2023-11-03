package com.learning.springboot.checklistapi.dto;

import com.learning.springboot.checklistapi.entity.ChecklistItemEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * Classe DTO que representa um item de checklist.
 */
@Builder
@Getter
public class ChecklistItemDTO {

    /**
     * Identificador único do item.
     */
    private String guid;

    /**
     * Descrição do item.
     */
    @NotBlank(message = "Descrição do checklist não pode ser null ou vazio")
    private String description;

    /**
     * Indica se o item foi concluído.
     */
    @NotNull(message = "Completado é obrigatório")
    private Boolean isCompleted;

    /**
     * Data de término do item.
     */
    @NotNull(message = "Data Fim é obrigatório")
    private LocalDate dateEnd;

    /**
     * Data de postagem do item.
     */
    private LocalDate datePost;

    /**
     * Identificador único da categoria do item.
     */
    private CategoryDTO category;

    /**
     * Converte uma entidade de item de checklist para um DTO de item de checklist.
     *
     * @param checklistItemEntity a entidade de item de checklist a ser convertida
     * @return o DTO de item de checklist convertido
     */
    public static ChecklistItemDTO toDTO(ChecklistItemEntity checklistItemEntity) {

        return ChecklistItemDTO.builder()
                .guid(checklistItemEntity.getGuid())
                .description(checklistItemEntity.getDescription())
                .isCompleted(checklistItemEntity.getIsCompleted())
                .dateEnd(checklistItemEntity.getDateEnd())
                .datePost(checklistItemEntity.getDatePost())
                .category(checklistItemEntity.getCategory() != null ?
                        CategoryDTO.builder()
                                .guid(checklistItemEntity.getCategory().getGuid())
                                .name(checklistItemEntity.getCategory().getName())
                                .build() :
                        null)
                .build();

    }
}

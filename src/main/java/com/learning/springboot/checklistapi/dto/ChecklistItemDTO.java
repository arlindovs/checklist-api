package com.learning.springboot.checklistapi.dto;

import com.learning.springboot.checklistapi.entity.ChecklistItemEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class ChecklistItemDTO {

    private String guid;
    private String description;
    private Boolean isCompleted;
    private LocalDate dateEnd;
    private LocalDate datePost;
    private String categoryGuid;

    public static ChecklistItemDTO toDTO(ChecklistItemEntity checklistItemEntity) {

        return ChecklistItemDTO.builder()
                .guid(checklistItemEntity.getGuid())
                .description(checklistItemEntity.getDescription())
                .isCompleted(checklistItemEntity.getIsCompleted())
                .dateEnd(checklistItemEntity.getDateEnd())
                .datePost(checklistItemEntity.getDatePost())
                .categoryGuid(checklistItemEntity.getCategory().getGuid())
                .build();

    }
}

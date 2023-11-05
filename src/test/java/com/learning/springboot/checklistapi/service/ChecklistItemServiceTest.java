package com.learning.springboot.checklistapi.service;

import com.learning.springboot.checklistapi.entity.ChecklistItemEntity;
import com.learning.springboot.checklistapi.repository.CategoryRepository;
import com.learning.springboot.checklistapi.entity.CategoryEntity;
import com.learning.springboot.checklistapi.repository.ChecklistItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class ChecklistItemServiceTest {

    private CategoryService categoryService;

    private ChecklistItemService checklistItemService;

    @Mock
    private ChecklistItemRepository checklistItemRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void initTest(){
        this.categoryService = new CategoryService(
                checklistItemRepository,
                categoryRepository);
        this.checklistItemService = new ChecklistItemService(
                checklistItemRepository,
                categoryRepository);
    }

    @Test
    public void shouldCreateAChecklistItemSuccessfully() {
        //having
        String guid = UUID.randomUUID().toString();
        String name = "Category 1";

        String description = "Item 1";
        Boolean isCompleted = true;
        LocalDate dateEnd = LocalDate.now();

        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(new CategoryEntity());
        when(checklistItemRepository.save(any(ChecklistItemEntity.class))).thenReturn(new ChecklistItemEntity());

        //when
        CategoryEntity categoryEntity = this.categoryService.addNewCategory(name);

        ChecklistItemEntity checklistItemEntity =
                this.checklistItemService.addNewChecklistItem(
                        description,
                        isCompleted,
                        dateEnd,
                        guid);

        //then
        Assertions.assertNotNull(categoryEntity);
        verify(categoryRepository, times(1)).save(
                argThat(categoryEntityArg -> categoryEntityArg.getName().equals("Category 1")
                        && categoryEntityArg.getGuid() != null)
        );

        Assertions.assertNotNull(checklistItemEntity);
        verify(checklistItemRepository, times(1)).save(
                argThat(checklistItemEntityArg -> checklistItemEntityArg.getDescription().equals("Item 1")
                        && checklistItemEntityArg.getGuid() != null)
        );

    }

}

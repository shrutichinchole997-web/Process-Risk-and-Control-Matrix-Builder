package com.internship.tool.service;

import com.internship.tool.entity.ProcessRiskControl;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.exception.ValidationException;
import com.internship.tool.repository.ProcessRiskControlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessRiskControlServiceTest {

    @Mock
    private ProcessRiskControlRepository repository;

    @InjectMocks
    private ProcessRiskControlService service;

    private ProcessRiskControl validRecord;

    @BeforeEach
    void setUp() {
        validRecord = ProcessRiskControl.builder()
                .id(1L)
                .processName("Test Process")
                .riskDescription("Test Risk")
                .controlDescription("Test Control")
                .priority("High")
                .status("Active")
                .build();
    }

    @Test
    void getAll_ReturnsPage() {
        Page<ProcessRiskControl> page = new PageImpl<>(Collections.singletonList(validRecord));
        when(repository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<ProcessRiskControl> result = service.getAll(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void getById_ExistingId_ReturnsRecord() {
        when(repository.findById(1L)).thenReturn(Optional.of(validRecord));

        ProcessRiskControl result = service.getById(1L);

        assertNotNull(result);
        assertEquals("Test Process", result.getProcessName());
    }

    @Test
    void getById_NonExistingId_ThrowsException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test
    void create_ValidRequest_ReturnsCreatedRecord() {
        when(repository.save(any(ProcessRiskControl.class))).thenReturn(validRecord);

        ProcessRiskControl result = service.create(validRecord);

        assertNotNull(result);
        assertEquals("Test Process", result.getProcessName());
        verify(repository, times(1)).save(validRecord);
    }

    @Test
    void create_EmptyProcessName_ThrowsValidationException() {
        validRecord.setProcessName("");

        assertThrows(ValidationException.class, () -> service.create(validRecord));
        verify(repository, never()).save(any());
    }

    @Test
    void create_NullProcessName_ThrowsValidationException() {
        validRecord.setProcessName(null);

        assertThrows(ValidationException.class, () -> service.create(validRecord));
        verify(repository, never()).save(any());
    }

    @Test
    void getAll_EmptyDatabase_ReturnsEmptyPage() {
        Page<ProcessRiskControl> page = new PageImpl<>(Collections.emptyList());
        when(repository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<ProcessRiskControl> result = service.getAll(PageRequest.of(0, 10));

        assertTrue(result.isEmpty());
    }

    @Test
    void create_SavesWithCorrectValues() {
        when(repository.save(any(ProcessRiskControl.class))).thenReturn(validRecord);

        ProcessRiskControl newRecord = ProcessRiskControl.builder()
                .processName("New Process")
                .riskDescription("Risk")
                .controlDescription("Control")
                .build();
        
        service.create(newRecord);
        verify(repository).save(newRecord);
    }
    
    @Test
    void getById_ExceptionMessageIsCorrect() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
        assertEquals("Record not found with id 99", ex.getMessage());
    }
    
    @Test
    void create_ValidationExceptionMessageIsCorrect() {
        validRecord.setProcessName("");

        ValidationException ex = assertThrows(ValidationException.class, () -> service.create(validRecord));
        assertEquals("Process name cannot be empty", ex.getMessage());
    }
}

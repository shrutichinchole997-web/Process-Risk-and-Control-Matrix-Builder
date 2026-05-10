package com.internship.tool.service;

import com.internship.tool.entity.ProcessRiskControl;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.exception.ValidationException;
import com.internship.tool.repository.ProcessRiskControlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessRiskControlService {
    private final ProcessRiskControlRepository repository;
    private final AuditService auditService;

    @Cacheable(value = "prc", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ProcessRiskControl> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<ProcessRiskControl> getAllList() {
        return repository.findAll();
    }

    @Cacheable(value = "prcSingle", key = "#id")
    public ProcessRiskControl getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id " + id));
    }

    @CacheEvict(value = {"prc", "prcSingle"}, allEntries = true)
    public ProcessRiskControl create(ProcessRiskControl request) {
        if (request.getProcessName() == null || request.getProcessName().isBlank()) {
            throw new ValidationException("Process name cannot be empty");
        }
        ProcessRiskControl saved = repository.save(request);
        auditService.log("CREATE", "ProcessRiskControl", "Created record with ID: " + saved.getId());
        return saved;
    }

    public java.util.Map<String, Long> getStats() {
        List<ProcessRiskControl> all = repository.findAll();
        java.util.Map<String, Long> stats = new java.util.HashMap<>();
        stats.put("total", (long) all.size());
        stats.put("completed", all.stream().filter(r -> "COMPLETED".equalsIgnoreCase(r.getStatus())).count());
        stats.put("in_progress", all.stream().filter(r -> "IN_PROGRESS".equalsIgnoreCase(r.getStatus())).count());
        stats.put("high_priority", all.stream().filter(r -> "HIGH".equalsIgnoreCase(r.getPriority())).count());
        return stats;
    }
}

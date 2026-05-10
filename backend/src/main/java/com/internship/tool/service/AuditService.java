package com.internship.tool.service;

import com.internship.tool.entity.AuditLog;
import com.internship.tool.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {
    private final AuditLogRepository repository;

    public void log(String action, String resource, String details) {
        String username = SecurityContextHolder.getContext().getAuthentication() != null ? 
                SecurityContextHolder.getContext().getAuthentication().getName() : "SYSTEM";
        
        AuditLog auditLog = AuditLog.builder()
                .username(username)
                .action(action)
                .resource(resource)
                .details(details)
                .build();
        repository.save(auditLog);
    }
}

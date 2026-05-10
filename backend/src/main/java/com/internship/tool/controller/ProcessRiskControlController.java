package com.internship.tool.controller;

import com.internship.tool.entity.ProcessRiskControl;
import com.internship.tool.service.ProcessRiskControlService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prc")
@RequiredArgsConstructor
public class ProcessRiskControlController {
    private final ProcessRiskControlService service;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Page<ProcessRiskControl>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getAll(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ProcessRiskControl> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProcessRiskControl> create(@RequestBody ProcessRiskControl request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Map<String, Long>> getStats() {
        return ResponseEntity.ok(service.getStats());
    }

    @GetMapping("/export")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void exportCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=prc_report.csv");
        PrintWriter writer = response.getWriter();
        writer.println("ID,Process Name,Risk Description,Control Description,Priority,Status");
        List<ProcessRiskControl> allRecords = service.getAllList();
        for (ProcessRiskControl record : allRecords) {
            writer.println(String.format("%d,\"%s\",\"%s\",\"%s\",%s,%s",
                    record.getId(), record.getProcessName(), record.getRiskDescription(),
                    record.getControlDescription(), record.getPriority(), record.getStatus()));
        }
    }
}

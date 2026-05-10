package com.internship.tool.repository;

import com.internship.tool.entity.ProcessRiskControl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessRiskControlRepository extends JpaRepository<ProcessRiskControl, Long> {
    Page<ProcessRiskControl> findByProcessNameContainingIgnoreCase(String keyword, Pageable pageable);
}

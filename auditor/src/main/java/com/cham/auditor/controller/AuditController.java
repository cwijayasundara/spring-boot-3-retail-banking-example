package com.cham.auditor.controller;

import com.cham.auditor.domain.Audit;
import com.cham.auditor.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AuditController {

    @Autowired
    private AuditService auditService;

    @PostMapping("/audit")
    public Audit createAuditEvent(@RequestBody Audit audit) {
        return auditService.save(audit);
    }
    @GetMapping("/audit/{id}")
    public Audit getAuditeById(@PathVariable Long id) {
        return auditService.findById(id);
    }

    @GetMapping("/audit")
    public List<Audit> getAllAuditRecords() {
        return auditService.getAllAuditRecords();
    }

}


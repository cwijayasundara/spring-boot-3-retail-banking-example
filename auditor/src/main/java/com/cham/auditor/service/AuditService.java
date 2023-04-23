package com.cham.auditor.service;

import com.cham.auditor.domain.Audit;
import com.cham.auditor.repository.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {

    @Autowired
    private AuditRepository auditRepository;

    public Audit save(Audit audit) {
        return auditRepository.save(audit);
    }

    public Audit findById(Long id) {
        return auditRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        auditRepository.deleteById(id);
    }

    public List<Audit> getAllAuditRecords(){
        return auditRepository.findAll();
    }
}


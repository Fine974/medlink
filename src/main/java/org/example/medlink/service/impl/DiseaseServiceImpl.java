package org.example.medlink.service.impl;

import org.example.medlink.entity.Disease;
import org.example.medlink.repository.DiseaseRepository;
import org.example.medlink.service.DiseaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiseaseServiceImpl implements DiseaseService {

    private final DiseaseRepository diseaseRepository;

    public DiseaseServiceImpl(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    @Override
    public List<Disease> getAllDiseases() {
        return diseaseRepository.findAll();
    }

    @Override
    public Disease getDiseaseById(Long id) {
        return diseaseRepository.findById(id).orElse(null);
    }

    @Override
    public Disease saveDisease(Disease disease) {
        return diseaseRepository.save(disease);
    }

    @Override
    public void deleteDisease(Long id) {
        diseaseRepository.deleteById(id);
    }
}

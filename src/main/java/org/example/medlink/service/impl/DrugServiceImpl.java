package org.example.medlink.service.impl;

import org.example.medlink.entity.Drug;
import org.example.medlink.repository.DrugRepository;
import org.example.medlink.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DrugServiceImpl implements DrugService {

    @Autowired
    private DrugRepository drugRepository;

    @Override
    public List<Drug> getAllDrugs() {
        return drugRepository.findAll();
    }

    @Override
    public Optional<Drug> getDrugById(Long id) {
        return drugRepository.findById(id);
    }

    @Override
    public Drug createDrug(Drug drug) {
        return drugRepository.save(drug);
    }

    @Override
    public Drug updateDrug(Long id, Drug updatedDrug) {
        return drugRepository.findById(id).map(drug -> {
            drug.setDbId(updatedDrug.getDbId());
            drug.setEnglishName(updatedDrug.getEnglishName());
            drug.setChineseName(updatedDrug.getChineseName());
            return drugRepository.save(drug);
        }).orElseThrow(() -> new RuntimeException("Drug not found with id: " + id));
    }

    @Override
    public void deleteDrug(Long id) {
        drugRepository.deleteById(id);
    }
}

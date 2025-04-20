package org.example.medlink.service.impl;

import org.example.medlink.entity.DrugDiseaseRelation;
import org.example.medlink.repository.DrugDiseaseRelationRepository;
import org.example.medlink.service.DrugDiseaseRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;


import java.util.Collections;
import java.util.List;

@Service
public class DrugDiseaseRelationServiceImpl implements DrugDiseaseRelationService {

    private final DrugDiseaseRelationRepository relationRepository;

    @Autowired
    public DrugDiseaseRelationServiceImpl(DrugDiseaseRelationRepository relationRepository) {
        this.relationRepository = relationRepository;
    }

    @Override
    public List<DrugDiseaseRelation> searchByDrugOrDiseaseName(String drugName, String diseaseName) {
        if (isNotBlank(drugName) && isNotBlank(diseaseName)) {
            return relationRepository.findByDrugAndDiseaseNames(drugName, diseaseName);
        } else if (isNotBlank(drugName)) {
            return relationRepository.findByDrug_ChineseNameContainingIgnoreCaseOrDrug_EnglishNameContainingIgnoreCase(
                    drugName, drugName);
        } else if (isNotBlank(diseaseName)) {
            return relationRepository.findByDisease_ChineseNameContainingIgnoreCaseOrDisease_EnglishNameContainingIgnoreCase(
                    diseaseName, diseaseName);
        } else {
            return Collections.emptyList();
        }
    }

    // 自定义 isBlank 方法替代 StringUtils.isBlank
    public boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }


}


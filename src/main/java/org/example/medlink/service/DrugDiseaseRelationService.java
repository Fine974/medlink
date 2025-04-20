package org.example.medlink.service;

import org.example.medlink.entity.DrugDiseaseRelation;

import java.util.List;

public interface DrugDiseaseRelationService {
    List<DrugDiseaseRelation> searchByDrugOrDiseaseName(String drugName, String diseaseName);
}


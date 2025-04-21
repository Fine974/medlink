package org.example.medlink.service;

import org.example.medlink.dto.DrugDiseaseRelationDTO;

import java.util.List;

public interface DrugDiseaseRelationService {

    List<DrugDiseaseRelationDTO> findRelationsByDrugName(String drugNameZhOrEn);

    List<DrugDiseaseRelationDTO> findRelationsByDiseaseName(String diseaseNameZhOrEn);
}

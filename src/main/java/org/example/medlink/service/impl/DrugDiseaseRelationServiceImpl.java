package org.example.medlink.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.medlink.dto.DrugDiseaseRelationDTO;
import org.example.medlink.entity.Disease;
import org.example.medlink.entity.Drug;
import org.example.medlink.entity.DrugDiseaseRelation;
import org.example.medlink.repository.DiseaseRepository;
import org.example.medlink.repository.DrugDiseaseRelationRepository;
import org.example.medlink.repository.DrugRepository;
import org.example.medlink.service.DrugDiseaseRelationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DrugDiseaseRelationServiceImpl implements DrugDiseaseRelationService {

    private final DrugRepository drugRepository;
    private final DiseaseRepository diseaseRepository;
    private final DrugDiseaseRelationRepository relationRepository;

    @Override
    public List<DrugDiseaseRelationDTO> findRelationsByDrugName(String drugNameZhOrEn) {
        List<Drug> drugs = drugRepository.findByChineseNameContainingIgnoreCaseOrEnglishNameContainingIgnoreCase(drugNameZhOrEn, drugNameZhOrEn);
        return drugs.stream()
                .flatMap(drug -> relationRepository.findByDrugDbId(drug.getDbId()).stream()
                        .map(rel -> toDTO(drug, diseaseRepository.findByOmimId(rel.getDiseaseOmimId()), rel.getRelationType())))
                .collect(Collectors.toList());
    }

    @Override
    public List<DrugDiseaseRelationDTO> findRelationsByDiseaseName(String diseaseNameZhOrEn) {
        List<Disease> diseases = diseaseRepository
                .findByChineseNameContainingIgnoreCaseOrEnglishNameContainingIgnoreCase(diseaseNameZhOrEn, diseaseNameZhOrEn);

        return diseases.stream()
                .flatMap(disease ->
                        relationRepository.findByDiseaseOmimId(disease.getOmimId()).stream()
                                .map(rel -> {
                                    Optional<Drug> drugOpt = drugRepository.findByDbId(rel.getDrugDbId());
                                    return drugOpt.map(drug -> toDTO(drug, disease, rel.getRelationType()))
                                            .orElse(null); // 或者抛异常、跳过等你自己定
                                })
                                .filter(Objects::nonNull)
                )
                .collect(Collectors.toList());
    }


    private DrugDiseaseRelationDTO toDTO(Drug drug, Disease disease, String relationType) {
        return new DrugDiseaseRelationDTO(
                drug.getDbId(),
                drug.getChineseName(),
                drug.getEnglishName(),
                disease.getOmimId(),
                disease.getChineseName(),
                disease.getEnglishName(),
                relationType
        );
    }
}

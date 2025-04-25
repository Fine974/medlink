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

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DrugDiseaseRelationServiceImpl implements DrugDiseaseRelationService {

    private final DrugRepository drugRepository;
    private final DiseaseRepository diseaseRepository;
    private final DrugDiseaseRelationRepository relationRepository;

    @Override
    public List<DrugDiseaseRelationDTO> findRelationsByDrugName(String drugNameZhOrEn) {
        // 1. 优先精确匹配
        List<Drug> drugs = drugRepository
                .findByChineseNameEqualsIgnoreCaseOrEnglishNameEqualsIgnoreCase(drugNameZhOrEn, drugNameZhOrEn);

        // 2. 无精确匹配时，使用模糊匹配兜底
        if (drugs.isEmpty()) {
            drugs = drugRepository
                    .findByChineseNameContainingIgnoreCaseOrEnglishNameContainingIgnoreCase(drugNameZhOrEn, drugNameZhOrEn);
        }

        // 3. 构建结果并去重
        return drugs.stream()
                .flatMap(drug ->
                        relationRepository.findByDrugDbId(drug.getDbId()).stream()
                                .map(rel -> {
                                    Disease disease = diseaseRepository.findByOmimId(rel.getDiseaseOmimId());
                                    return toDTO(drug, disease, rel.getRelationType());
                                })
                )
                .filter(Objects::nonNull)
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(
                                Comparator.comparing(dto ->
                                        Objects.toString(dto.getDrugDbId(), "") + "_" +
                                        Objects.toString(dto.getDiseaseOmimId(), "") + "_" +
                                        Objects.toString(dto.getRelationType(), "")
                                ))),
                        ArrayList::new
                ));
    }


    @Override
    public List<DrugDiseaseRelationDTO> findRelationsByDiseaseName(String diseaseNameZhOrEn) {
        List<Disease> diseases = diseaseRepository
                .findByChineseNameEqualsIgnoreCaseOrEnglishNameEqualsIgnoreCase(diseaseNameZhOrEn, diseaseNameZhOrEn);

        if (diseases.isEmpty()) {
            // 兜底模糊匹配
            diseases = diseaseRepository
                    .findByChineseNameContainingIgnoreCaseOrEnglishNameContainingIgnoreCase(diseaseNameZhOrEn, diseaseNameZhOrEn);
        }


        return diseases.stream()
                .flatMap(disease ->
                        relationRepository.findByDiseaseOmimId(disease.getOmimId()).stream()
                                .map(rel -> {
                                    Optional<Drug> drugOpt = drugRepository.findByDbId(rel.getDrugDbId());
                                    return drugOpt.map(drug -> toDTO(drug, disease, rel.getRelationType()))
                                            .orElse(null);
                                })
                                .filter(Objects::nonNull)
                )
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(
                                Comparator.comparing(dto ->
                                        dto.getDrugDbId() + "_" +
                                        dto.getDiseaseOmimId() + "_" +
                                        dto.getRelationType()
                                ))),
                        ArrayList::new
                ));
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

package org.example.medlink.controller;

import org.example.medlink.entity.Disease;
import org.example.medlink.entity.Drug;
import org.example.medlink.entity.DrugDiseaseRelation;
import org.example.medlink.repository.DiseaseRepository;
import org.example.medlink.repository.DrugDiseaseRelationRepository;
import org.example.medlink.repository.DrugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/compare")
public class ComparisonController {

    @Autowired
    private DrugDiseaseRelationRepository relationRepository;

    @Autowired
    private DrugRepository drugRepository;

    @Autowired
    private DiseaseRepository diseaseRepository;

    /**
     * 药物比对：比较两个药物的疾病关联
     */
    @GetMapping("/drugs")
    public ResponseEntity<Map<String, Object>> compareDrugs(
            @RequestParam String drug1,
            @RequestParam String drug2) {

        List<DrugDiseaseRelation> rel1 = relationRepository.findByDrugDbId(drug1);
        List<DrugDiseaseRelation> rel2 = relationRepository.findByDrugDbId(drug2);

        Set<String> diseaseSet1 = rel1.stream().map(DrugDiseaseRelation::getDiseaseOmimId).collect(Collectors.toSet());
        Set<String> diseaseSet2 = rel2.stream().map(DrugDiseaseRelation::getDiseaseOmimId).collect(Collectors.toSet());

        // 计算交集
        Set<String> intersection = new HashSet<>(diseaseSet1);
        intersection.retainAll(diseaseSet2);

        // 查询疾病信息
        Map<String, Disease> allDiseases = Stream.concat(rel1.stream(), rel2.stream())
                .map(r -> diseaseRepository.findByOmimId(r.getDiseaseOmimId()))
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toMap(Disease::getOmimId, d -> d, (a, b) -> a));

        Map<String, Object> result = new HashMap<>();
        result.put("drug1Diseases", diseaseSet1.stream().map(allDiseases::get).collect(Collectors.toList()));
        result.put("drug2Diseases", diseaseSet2.stream().map(allDiseases::get).collect(Collectors.toList()));
        result.put("commonDiseases", intersection.stream().map(allDiseases::get).collect(Collectors.toList()));

        return ResponseEntity.ok(result);
    }

    /**
     * 疾病比对：比较两个疾病的药物关联
     */
    @GetMapping("/diseases")
    public ResponseEntity<Map<String, Object>> compareDiseases(
            @RequestParam String disease1,
            @RequestParam String disease2) {

        List<DrugDiseaseRelation> rel1 = relationRepository.findByDiseaseOmimId(disease1);
        List<DrugDiseaseRelation> rel2 = relationRepository.findByDiseaseOmimId(disease2);

        Set<String> drugSet1 = rel1.stream().map(DrugDiseaseRelation::getDrugDbId).collect(Collectors.toSet());
        Set<String> drugSet2 = rel2.stream().map(DrugDiseaseRelation::getDrugDbId).collect(Collectors.toSet());

        Set<String> intersection = new HashSet<>(drugSet1);
        intersection.retainAll(drugSet2);

        Map<String, Drug> allDrugs = Stream.concat(rel1.stream(), rel2.stream())
                .map(r -> drugRepository.findByDbId(r.getDrugDbId()).orElse(null))
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toMap(Drug::getDbId, d -> d, (a, b) -> a));

        Map<String, Object> result = new HashMap<>();
        result.put("disease1Drugs", drugSet1.stream().map(allDrugs::get).collect(Collectors.toList()));
        result.put("disease2Drugs", drugSet2.stream().map(allDrugs::get).collect(Collectors.toList()));
        result.put("commonDrugs", intersection.stream().map(allDrugs::get).collect(Collectors.toList()));

        return ResponseEntity.ok(result);
    }
}

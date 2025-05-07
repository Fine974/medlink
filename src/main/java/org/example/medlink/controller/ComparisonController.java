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
     * 药物比对：通过名称查询 drugDbId，再比对关联疾病
     */
    @GetMapping("/drugs")
    public ResponseEntity<?> compareDrugs(
            @RequestParam String drug1,
            @RequestParam String drug2) {

        Optional<Drug> d1Opt = drugRepository.findByChineseNameOrEnglishName(drug1, drug1);
        Optional<Drug> d2Opt = drugRepository.findByChineseNameOrEnglishName(drug2, drug2);

        if (d1Opt.isEmpty() || d2Opt.isEmpty()) {
            return ResponseEntity.badRequest().body("找不到对应药物，请检查名称是否正确");
        }

        String dbId1 = d1Opt.get().getDbId();
        String dbId2 = d2Opt.get().getDbId();

        List<DrugDiseaseRelation> rel1 = relationRepository.findByDrugDbId(dbId1);
        List<DrugDiseaseRelation> rel2 = relationRepository.findByDrugDbId(dbId2);

        Set<String> diseaseSet1 = rel1.stream().map(DrugDiseaseRelation::getDiseaseOmimId).collect(Collectors.toSet());
        Set<String> diseaseSet2 = rel2.stream().map(DrugDiseaseRelation::getDiseaseOmimId).collect(Collectors.toSet());

        Set<String> intersection = new HashSet<>(diseaseSet1);
        intersection.retainAll(diseaseSet2);

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
     * 疾病比对：通过名称查询 omimId，再比对关联药物
     */
    @GetMapping("/diseases")
    public ResponseEntity<?> compareDiseases(
            @RequestParam String disease1,
            @RequestParam String disease2) {

        Optional<Disease> d1Opt = diseaseRepository.findByChineseNameOrEnglishName(disease1, disease1);
        Optional<Disease> d2Opt = diseaseRepository.findByChineseNameOrEnglishName(disease2, disease2);

        if (d1Opt.isEmpty() || d2Opt.isEmpty()) {
            return ResponseEntity.badRequest().body("找不到对应疾病，请检查名称是否正确");
        }

        String omimId1 = d1Opt.get().getOmimId();
        String omimId2 = d2Opt.get().getOmimId();

        List<DrugDiseaseRelation> rel1 = relationRepository.findByDiseaseOmimId(omimId1);
        List<DrugDiseaseRelation> rel2 = relationRepository.findByDiseaseOmimId(omimId2);

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

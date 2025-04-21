package org.example.medlink.controller;

import lombok.RequiredArgsConstructor;
import org.example.medlink.entity.Disease;
import org.example.medlink.entity.Drug;
import org.example.medlink.entity.DrugDiseaseRelation;
import org.example.medlink.repository.DiseaseRepository;
import org.example.medlink.repository.DrugDiseaseRelationRepository;
import org.example.medlink.repository.DrugRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/relation-query")
@RequiredArgsConstructor
public class RelationQueryController {

    private final DrugRepository drugRepository;
    private final DiseaseRepository diseaseRepository;
    private final DrugDiseaseRelationRepository relationRepository;

    /**
     * 查询指定药物名称关联的所有疾病
     * @param name
     * @return
     */
    @GetMapping("/by-drug")
    public List<Disease> queryDiseasesByDrugName(@RequestParam("name") String name) {
        List<Drug> drugs = drugRepository.findByChineseNameContainingIgnoreCaseOrEnglishNameContainingIgnoreCase(name, name);
        Set<String> drugDbIds = new HashSet<>();
        for (Drug d : drugs) {
            drugDbIds.add(d.getDbId());
        }

        Set<Disease> result = new HashSet<>();
        for (String dbId : drugDbIds) {
            List<DrugDiseaseRelation> relations = relationRepository.findByDrugDbId(dbId);
            for (DrugDiseaseRelation rel : relations) {
                Disease disease = diseaseRepository.findByOmimId(rel.getDiseaseOmimId());
                if (disease != null) {
                    result.add(disease);
                }
            }
        }
        return new ArrayList<>(result);
    }

    /**
     * 查询指定疾病名称关联的所有药物
     * @param name
     * @return
     */
    @GetMapping("/by-disease")
    public List<Drug> queryDrugsByDiseaseName(@RequestParam("name") String name) {
        List<Disease> diseases = diseaseRepository.findByChineseNameContainingIgnoreCaseOrEnglishNameContainingIgnoreCase(name, name);
        Set<String> omimIds = new HashSet<>();
        for (Disease d : diseases) {
            omimIds.add(d.getOmimId());
        }

        Set<Drug> result = new HashSet<>();
        for (String omimId : omimIds) {
            List<DrugDiseaseRelation> relations = relationRepository.findByDiseaseOmimId(omimId);
            for (DrugDiseaseRelation rel : relations) {
                Drug drug = drugRepository.findByDbId(rel.getDrugDbId());
                if (drug != null) {
                    result.add(drug);
                }
            }
        }
        return new ArrayList<>(result);
    }
}

package org.example.medlink.controller;

import org.example.medlink.dto.PagedResponse;
import org.example.medlink.entity.Disease;
import org.example.medlink.entity.Drug;
import org.example.medlink.entity.DrugDiseaseRelation;
import org.example.medlink.repository.DiseaseRepository;
import org.example.medlink.repository.DrugDiseaseRelationRepository;
import org.example.medlink.repository.DrugRepository;
import org.example.medlink.service.DiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/diseases")
public class DiseaseController {

    @Autowired
    private DiseaseService diseaseService;
    @Autowired
    private DiseaseRepository diseaseRepository;
    @Autowired
    private DrugDiseaseRelationRepository relationRepository;
    @Autowired
    private DrugRepository drugRepository;


    public DiseaseController(DiseaseService diseaseService) {
        this.diseaseService = diseaseService;
    }

    /**
     * 获取所有疾病
     * @param page
     * @param size
     * @return
     */
    @GetMapping
    public PagedResponse<Disease> getDiseases(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());
        Page<Disease> diseasesPage = diseaseRepository.findAll(pageable);

        return new PagedResponse<>(diseasesPage);
    }

    /**
     * 通过 omimId 查询疾病
     * @param omimId
     * @return
     */
    @GetMapping("/{omimId}")
    public ResponseEntity<Disease> getDiseaseByOmimId(@PathVariable String omimId) {
        Disease disease = diseaseService.getDiseaseByOmimId(omimId);
        if (disease == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(disease);
    }


    @PostMapping
    public Disease createDisease(@RequestBody Disease disease) {
        return diseaseService.saveDisease(disease);
    }

    @DeleteMapping("/{id}")
    public void deleteDisease(@PathVariable Long id) {
        diseaseService.deleteDisease(id);
    }

    /**
     * 获取疾病关系图
     * @param omimId 疾病 OMIM ID
     * @return 疾病与相关药物的图谱数据
     */
    @GetMapping("/disease-graph/{omimId}")
    public ResponseEntity<Map<String, Object>> getDiseaseRelationGraph(@PathVariable String omimId) {
        Disease disease = diseaseRepository.findByOmimId(omimId);
        if (disease == null) return ResponseEntity.notFound().build();

        List<DrugDiseaseRelation> relations = relationRepository.findByDiseaseOmimId(omimId);
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> links = new ArrayList<>();

        // 疾病节点
        Map<String, Object> diseaseNode = new HashMap<>();
        diseaseNode.put("id", disease.getOmimId());
        diseaseNode.put("name", disease.getChineseName());
        diseaseNode.put("category", "疾病");
        nodes.add(diseaseNode);

        for (DrugDiseaseRelation rel : relations) {
            Drug drug = drugRepository.findByDbId(rel.getDrugDbId()).orElse(null);
            if (drug == null) continue;

            // 药物节点
            Map<String, Object> drugNode = new HashMap<>();
            drugNode.put("id", drug.getDbId());
            drugNode.put("name", drug.getChineseName());
            drugNode.put("category", "药物");
            nodes.add(drugNode);

            // 关系边
            Map<String, Object> link = new HashMap<>();
            link.put("source", disease.getOmimId());
            link.put("target", drug.getDbId());
            link.put("relation", rel.getRelationType());
            links.add(link);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("nodes", nodes);
        result.put("links", links);
        return ResponseEntity.ok(result);
    }

}

package org.example.medlink.controller;

import org.example.medlink.dto.PagedResponse;
import org.example.medlink.entity.Disease;
import org.example.medlink.entity.Drug;
import org.example.medlink.entity.DrugDiseaseRelation;
import org.example.medlink.repository.DiseaseRepository;
import org.example.medlink.repository.DrugDiseaseRelationRepository;
import org.example.medlink.repository.DrugRepository;
import org.example.medlink.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/drugs")
public class DrugController {

    @Autowired
    private DrugRepository drugRepository;
    @Autowired
    private DiseaseRepository diseaseRepository;
    @Autowired
    private DrugDiseaseRelationRepository relationRepository;
    @Autowired
    private DrugService drugService;

    /**
     * 获取所有药物
     * @param page
     * @param size
     * @return
     */
    @GetMapping
    public PagedResponse<Drug> getDrugs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());
        Page<Drug> drugPage = drugRepository.findAll(pageable);

        return new PagedResponse<>(drugPage);
    }

    // 根据dbId获取药物
    @GetMapping("/{dbId}")
    public Drug getDrugByDbId(@PathVariable String dbId) {
        return drugService.getDrugByDbId(dbId);
    }


    // 添加药物
    @PostMapping
    public Drug createDrug(@RequestBody Drug drug) {
        return drugRepository.save(drug);
    }

    // 更新药物
    @PutMapping("/{id}")
    public Drug updateDrug(@PathVariable Long id, @RequestBody Drug updatedDrug) {
        return drugRepository.findById(id).map(drug -> {
            drug.setDbId(updatedDrug.getDbId());
            drug.setEnglishName(updatedDrug.getEnglishName());
            drug.setChineseName(updatedDrug.getChineseName());
            return drugRepository.save(drug);
        }).orElseThrow(() -> new RuntimeException("Drug not found with id " + id));
    }

    // 删除药物
    @DeleteMapping("/{id}")
    public void deleteDrug(@PathVariable Long id) {
        drugRepository.deleteById(id);
    }

    /**
     * 获取药物关系图
     * @param dbId
     * @return
     */
    @GetMapping("/drug-graph/{dbId}")
    public ResponseEntity<Map<String, Object>> getDrugRelationGraph(@PathVariable String dbId) {
        Drug drug = drugRepository.findByDbId(dbId).orElse(null);
        if (drug == null) return ResponseEntity.notFound().build();

        List<DrugDiseaseRelation> relations = relationRepository.findByDrugDbId(dbId);
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> links = new ArrayList<>();

        Map<String, Object> drugNode = new HashMap<>();
        drugNode.put("id", drug.getDbId());
        drugNode.put("name", drug.getChineseName());
        drugNode.put("category", "药物");
        nodes.add(drugNode);

        for (DrugDiseaseRelation rel : relations) {
            Disease disease = diseaseRepository.findByOmimId(rel.getDiseaseOmimId());
            if (disease == null) continue;

            Map<String, Object> diseaseNode = new HashMap<>();
            diseaseNode.put("id", disease.getOmimId());
            diseaseNode.put("name", disease.getChineseName());
            diseaseNode.put("category", "疾病");
            nodes.add(diseaseNode);

            Map<String, Object> link = new HashMap<>();
            link.put("source", drug.getDbId());
            link.put("target", disease.getOmimId());
            link.put("relation", rel.getRelationType());
            links.add(link);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("nodes", nodes);
        result.put("links", links);
        return ResponseEntity.ok(result);
    }
}

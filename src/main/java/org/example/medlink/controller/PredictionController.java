package org.example.medlink.controller;

import org.example.medlink.dto.DrugDiseaseRelationDTO;
import org.example.medlink.entity.DrugDiseaseRelation;
import org.example.medlink.repository.DrugDiseaseRelationRepository;
import org.example.medlink.service.DrugDiseaseRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/predict")
public class PredictionController {

    private final DrugDiseaseRelationService relationService;

    @Autowired
    public PredictionController(DrugDiseaseRelationService relationService) {
        this.relationService = relationService;
    }

    /**
     * 通过疾病名或药物名模糊查询关联数据
     * 示例请求：/api/predict/search?drugName=aspirin&diseaseName=cancer
     */
    @GetMapping("/search")
    public ResponseEntity<List<DrugDiseaseRelation>> searchRelations(
            @RequestParam(required = false) String drugName,
            @RequestParam(required = false) String diseaseName) {

        List<DrugDiseaseRelation> results = relationService.searchByDrugOrDiseaseName(drugName, diseaseName);
        return ResponseEntity.ok(results);
    }
}


package org.example.medlink.controller;

import org.example.medlink.dto.DiseaseSuggestionDTO;
import org.example.medlink.dto.DrugSuggestionDTO;
import org.example.medlink.entity.Disease;
import org.example.medlink.entity.Drug;
import org.example.medlink.service.DiseaseService;
import org.example.medlink.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/relation-query")
public class RelationQueryController {

    @Autowired
    private DiseaseService diseaseService;
    @Autowired
    private DrugService drugService;

    /**
     * 疾病联想查询接口
     * @param name
     * @return
     */
    @GetMapping("/disease-suggestions")
    public ResponseEntity<List<DiseaseSuggestionDTO>> getDiseaseSuggestions(@RequestParam String name) {
        List<DiseaseSuggestionDTO> suggestions = diseaseService.searchDiseases(name).stream()
                .map(d -> new DiseaseSuggestionDTO(d.getChineseName(), d.getEnglishName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(suggestions);
    }

    /**
     * 药物联想查询接口
     * @param name
     * @return
     */
    @GetMapping("/drug-suggestions")
    public ResponseEntity<List<DrugSuggestionDTO>> getDrugSuggestions(@RequestParam String name) {
        List<DrugSuggestionDTO> suggestions = drugService.searchDrugs(name).stream()
                .map(d -> new DrugSuggestionDTO(d.getChineseName(), d.getEnglishName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(suggestions);
    }
}
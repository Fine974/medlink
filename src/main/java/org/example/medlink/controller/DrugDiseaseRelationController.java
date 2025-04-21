package org.example.medlink.controller;

import lombok.RequiredArgsConstructor;
import org.example.medlink.dto.DrugDiseaseRelationDTO;
import org.example.medlink.service.DrugDiseaseRelationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relation-query")
@RequiredArgsConstructor
public class DrugDiseaseRelationController {

    private final DrugDiseaseRelationService relationService;

    /**
     * 根据药物名称模糊查询关联的疾病信息
     */
    @GetMapping("/by-drug")
    public List<DrugDiseaseRelationDTO> queryByDrugName(@RequestParam("name") String name) {
        return relationService.findRelationsByDrugName(name);
    }

    /**
     * 根据疾病名称模糊查询关联的药物信息
     */
    @GetMapping("/by-disease")
    public List<DrugDiseaseRelationDTO> queryByDiseaseName(@RequestParam("name") String name) {
        return relationService.findRelationsByDiseaseName(name);
    }
}

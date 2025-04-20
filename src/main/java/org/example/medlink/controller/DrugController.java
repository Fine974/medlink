package org.example.medlink.controller;

import org.example.medlink.entity.Drug;
import org.example.medlink.repository.DrugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/drugs")
public class DrugController {

    @Autowired
    private DrugRepository drugRepository;

    // 获取所有药物
    @GetMapping
    public List<Drug> getAllDrugs() {
        return drugRepository.findAll();
    }

    // 根据 ID 获取药物
    @GetMapping("/{id}")
    public Optional<Drug> getDrugById(@PathVariable Long id) {
        return drugRepository.findById(id);
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
}

package org.example.medlink.controller;

import org.example.medlink.dto.PagedResponse;
import org.example.medlink.entity.Drug;
import org.example.medlink.repository.DrugRepository;
import org.example.medlink.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/drugs")
public class DrugController {

    @Autowired
    private DrugRepository drugRepository;
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

//    /**
//     * 根据id获取药物
//     * @param id
//     * @return
//     */
//    @GetMapping("/{id}")
//    public Optional<Drug> getDrugById(@PathVariable Long id) {
//        return drugRepository.findById(id);
//    }

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
}

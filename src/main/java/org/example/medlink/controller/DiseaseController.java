package org.example.medlink.controller;

import org.example.medlink.dto.PagedResponse;
import org.example.medlink.entity.Disease;
import org.example.medlink.repository.DiseaseRepository;
import org.example.medlink.service.DiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diseases")
public class DiseaseController {

    @Autowired
    private DiseaseService diseaseService;
    @Autowired
    private DiseaseRepository diseaseRepository;


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


    @GetMapping("/{id}")
    public Disease getDiseaseById(@PathVariable Long id) {
        return diseaseService.getDiseaseById(id);
    }

    @PostMapping
    public Disease createDisease(@RequestBody Disease disease) {
        return diseaseService.saveDisease(disease);
    }

    @DeleteMapping("/{id}")
    public void deleteDisease(@PathVariable Long id) {
        diseaseService.deleteDisease(id);
    }
}

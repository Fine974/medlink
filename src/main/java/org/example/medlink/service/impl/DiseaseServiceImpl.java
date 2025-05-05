package org.example.medlink.service.impl;

import org.example.medlink.entity.Disease;
import org.example.medlink.repository.DiseaseRepository;
import org.example.medlink.service.DiseaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiseaseServiceImpl implements DiseaseService {

    private final DiseaseRepository diseaseRepository;

    public DiseaseServiceImpl(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    @Override
    public List<Disease> getAllDiseases() {
        return diseaseRepository.findAll();
    }

    @Override
    public Disease getDiseaseById(Long id) {
        return diseaseRepository.findById(id).orElse(null);
    }

    @Override
    public Disease saveDisease(Disease disease) {
        return diseaseRepository.save(disease);
    }

    @Override
    public void deleteDisease(Long id) {
        diseaseRepository.deleteById(id);
    }

    /**
     * 疾病联想查询
     * @param name
     * @return
     */
    @Override
    public List<Disease> searchDiseases(String name) {
        // 这里通过疾病的中文名或英文名进行模糊查询
        return diseaseRepository.findByChineseNameContainingIgnoreCaseOrEnglishNameContainingIgnoreCase(name, name);
    }

    /**
     * 通过OMIM ID查询
     * @param omimId
     * @return
     */
    @Override
    public Disease getDiseaseByOmimId(String omimId) {
        return diseaseRepository.findByOmimId(omimId);
    }

}

package org.example.medlink.service;

import org.example.medlink.entity.Disease;

import java.util.List;

public interface DiseaseService {

    List<Disease> getAllDiseases();

    Disease getDiseaseById(Long id);

    Disease saveDisease(Disease disease);

    void deleteDisease(Long id);

    /**
     * 疾病联想查询
     * @param name
     * @return
     */
    List<Disease> searchDiseases(String name);

    /**
     * 根据 OMIM ID 获取疾病信息
     * @param omimId
     * @return
     */
    Disease getDiseaseByOmimId(String omimId);
}

package org.example.medlink.service;

import org.example.medlink.entity.Disease;

import java.util.List;

public interface DiseaseService {

    List<Disease> getAllDiseases();

    Disease getDiseaseById(Long id);

    Disease saveDisease(Disease disease);

    void deleteDisease(Long id);
}

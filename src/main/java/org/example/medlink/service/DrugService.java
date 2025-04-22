package org.example.medlink.service;

import org.example.medlink.entity.Drug;

import java.util.List;
import java.util.Optional;

public interface DrugService {
    List<Drug> getAllDrugs();
    Optional<Drug> getDrugById(Long id);
    Drug createDrug(Drug drug);
    Drug updateDrug(Long id, Drug updatedDrug);
    void deleteDrug(Long id);

    /**
     * 药物联想查询
     * @param name
     * @return
     */
    List<Drug> searchDrugs(String name);
}

package org.example.medlink.repository;

import org.example.medlink.entity.DrugDiseaseRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DrugDiseaseRelationRepository extends JpaRepository<DrugDiseaseRelation, Long> {

    /**
     * 根据药物和疾病数据库ID查询关联关系
     * @param drugDbId
     * @return
     */
    List<DrugDiseaseRelation> findByDrugDbId(String drugDbId);

    /**
     * 根据疾病OMIM ID查询关联关系
     * @param diseaseOmimId
     * @return
     */
    List<DrugDiseaseRelation> findByDiseaseOmimId(String diseaseOmimId);

}

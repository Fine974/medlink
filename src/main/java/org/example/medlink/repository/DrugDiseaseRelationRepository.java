package org.example.medlink.repository;

import org.example.medlink.entity.DrugDiseaseRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DrugDiseaseRelationRepository extends JpaRepository<DrugDiseaseRelation, Long> {

    // 根据药物中/英⽂名模糊查询
    List<DrugDiseaseRelation> findByDrug_ChineseNameContainingIgnoreCaseOrDrug_EnglishNameContainingIgnoreCase(
            String chineseName, String englishName);

    // 根据疾病中/英⽂名模糊查询
    List<DrugDiseaseRelation> findByDisease_ChineseNameContainingIgnoreCaseOrDisease_EnglishNameContainingIgnoreCase(
            String chineseName, String englishName);

    // 同时根据药物名和疾病名查询：⾃定义 JPQL，确保逻辑无歧义
    @Query("SELECT r FROM DrugDiseaseRelation r " +
            "WHERE " +
            "  (LOWER(r.drug.chineseName) LIKE LOWER(CONCAT('%', :drugName, '%')) " +
            "   OR LOWER(r.drug.englishName) LIKE LOWER(CONCAT('%', :drugName, '%'))) " +
            "AND " +
            "  (LOWER(r.disease.chineseName) LIKE LOWER(CONCAT('%', :diseaseName, '%')) " +
            "   OR LOWER(r.disease.englishName) LIKE LOWER(CONCAT('%', :diseaseName, '%')))")
    List<DrugDiseaseRelation> findByDrugAndDiseaseNames(
            @Param("drugName") String drugName,
            @Param("diseaseName") String diseaseName);
}

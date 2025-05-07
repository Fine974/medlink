package org.example.medlink.repository;

import org.example.medlink.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiseaseRepository extends JpaRepository<Disease, Long> {
    /**
     * 根据OMIM ID查询
     * @param omimId
     * @return
     */
    Disease findByOmimId(String omimId);

    /**
     * 根据中文名或英文名查询
     * @param chineseName
     * @param englishName
     * @return
     */
    List<Disease> findByChineseNameContainingIgnoreCaseOrEnglishNameContainingIgnoreCase(String chineseName, String englishName);

    List<Disease> findByChineseNameEqualsIgnoreCaseOrEnglishNameEqualsIgnoreCase(String diseaseNameZhOrEn, String diseaseNameZhOrEn1);

    Optional<Disease> findByChineseNameOrEnglishName(String disease1, String disease11);
}

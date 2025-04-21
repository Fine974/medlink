package org.example.medlink.repository;

import org.example.medlink.entity.Drug;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DrugRepository extends JpaRepository<Drug, Long> {

    // 可选方法，用于根据 dbId 查询
    Drug findByDbId(String dbId);

    // 可选方法，用于根据英文名或中文名查询
    Optional<Drug> findByEnglishName(String englishName);
    Optional<Drug> findByChineseName(String chineseName);

    /**
     * 根据英文名或中文名模糊查询
     * @param englishName
     * @param chineseName
     * @return
     */
    List<Drug> findByChineseNameContainingIgnoreCaseOrEnglishNameContainingIgnoreCase(String chineseName, String englishName);

}

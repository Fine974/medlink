package org.example.medlink.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrugDiseaseRelationDTO {

    private String drugDbId;
    private String drugChineseName;
    private String drugEnglishName;

    private String diseaseOmimId;
    private String diseaseChineseName;
    private String diseaseEnglishName;

    private String relationType;

    // 可拓展字段（如未来添加）
    // private String confidenceScore;
    // private String source;
    // private String evidence;
}

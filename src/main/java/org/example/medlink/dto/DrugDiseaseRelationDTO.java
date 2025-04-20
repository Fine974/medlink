package org.example.medlink.dto;

import lombok.Data;

@Data
public class DrugDiseaseRelationDTO {
    private Long relationId;
    private String drugNameCn;
    private String drugNameEn;
    private String diseaseNameCn;
    private String diseaseNameEn;
    private String relationType;
}

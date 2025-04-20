package org.example.medlink.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class DrugDiseaseRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 多对一关联药物
    @ManyToOne
    @JoinColumn(name = "drug_id", nullable = false)
    private Drug drug;

    // 多对一关联疾病
    @ManyToOne
    @JoinColumn(name = "disease_id", nullable = false)
    private Disease disease;

    // 关系类型，例如“治疗”、“副作用”等
    @Enumerated(EnumType.STRING)
    private RelationType relationType;
}

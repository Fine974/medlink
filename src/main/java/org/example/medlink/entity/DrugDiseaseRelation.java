package org.example.medlink.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "drug_disease_relation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrugDiseaseRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "drug_db_id", nullable = false)
    private String drugDbId;

    @Column(name = "disease_omim_id", nullable = false)
    private String diseaseOmimId;

    @Column(name = "relation_type", nullable = false)
    private String relationType; // 例如 "known" 或 "predicted"

    @Column(name = "confidence_score")
    private Double confidenceScore;

    @Column(name = "source")
    private String source;

    @Column(columnDefinition = "TEXT")
    private String evidence;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "status")
    private String status;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}

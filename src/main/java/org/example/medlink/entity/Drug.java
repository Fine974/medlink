package org.example.medlink.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "drug")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Drug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "db_id", nullable = false)
    private String dbId;

    @Column(name = "english_name", nullable = false)
    private String englishName;

    @Column(name = "chinese_name", nullable = false)
    private String chineseName;

    // 新增字段：generic_name
    @Column(name = "generic_name")
    private String genericName;

    // 新增字段：summary
    @Column(columnDefinition = "TEXT")
    private String summary;

    // 新增字段：background
    @Column(columnDefinition = "TEXT")
    private String background;

    // 新增字段：type
    @Column(length = 50)
    private String type;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String indication;

    @Column(name = "mechanism_of_action", columnDefinition = "TEXT")
    private String mechanismOfAction;

    @Column(name = "group_name", columnDefinition = "TEXT")
    private String groupName;

    // 新增字段：pharmacodynamics
    @Column(columnDefinition = "TEXT")
    private String pharmacodynamics;

}

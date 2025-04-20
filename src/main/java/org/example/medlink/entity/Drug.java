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
}

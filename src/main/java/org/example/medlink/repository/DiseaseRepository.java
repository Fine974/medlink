package org.example.medlink.repository;

import org.example.medlink.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiseaseRepository extends JpaRepository<Disease, Long> {
    Disease findByOmimId(String omimId);
}

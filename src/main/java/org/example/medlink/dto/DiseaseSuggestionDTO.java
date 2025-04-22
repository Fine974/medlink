package org.example.medlink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseSuggestionDTO {
    private String diseaseChineseName;
    private String diseaseEnglishName;
}

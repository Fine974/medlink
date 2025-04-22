package org.example.medlink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrugSuggestionDTO {
    private String drugChineseName;
    private String drugEnglishName;
}

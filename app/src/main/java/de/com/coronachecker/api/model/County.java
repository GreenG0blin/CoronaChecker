package de.com.coronachecker.api.model;

import de.com.coronachecker.persistence.entities.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class County {

    private String name;
    private float casesPer100k;
    private Status status;
    private String lastUpdated;

}

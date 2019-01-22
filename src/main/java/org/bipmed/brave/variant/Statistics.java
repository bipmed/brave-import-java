package org.bipmed.brave.variant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Statistics {
    private Double min;
    private Double q25;
    private Double median;
    private Double q75;
    private Double max;
    private Double mean;
}

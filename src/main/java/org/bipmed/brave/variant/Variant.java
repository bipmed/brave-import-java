package org.bipmed.brave.variant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Variant {

    @JsonIgnore
    private String id;
    private List<String> snpIds;
    private String datasetId;
    private String assemblyId;
    private Integer totalSamples;
    private String referenceName;
    private Long start;
    private String referenceBases;
    private List<String> alternateBases;
    private List<String> geneSymbol;
    private List<Float> alleleFrequency;
    private Long sampleCount;

    private Statistics coverage;
    private Statistics genotypeQuality;

    private String clnsig;
    private List<String> hgvs;
    private List<String> type;
}


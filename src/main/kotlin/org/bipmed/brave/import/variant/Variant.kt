package org.bipmed.brave.import.variant

import com.fasterxml.jackson.annotation.JsonIgnore

data class Variant(
        @JsonIgnore
        val id: String? = null,
        val snpIds: List<String> = emptyList(),
        val datasetId: String? = null,
        val assemblyId: String? = null,
        val totalSamples: Int? = null,
        val referenceName: String? = null,
        val start: Long? = null,
        val referenceBases: String? = null,
        val alternateBases: List<String> = emptyList(),
        val geneSymbol: String? = null,
        val alleleFrequency: List<Number> = emptyList(),
        val sampleCount: Long? = null,

        val coverage: Statistics? = null,
        val genotypeQuality: Statistics? = null,

        val clnsig: String? = null
) {
    data class Statistics(
            val min: Int? = null,
            val q25: Number? = null,
            val median: Number? = null,
            val q75: Number? = null,
            val max: Int? = null,
            val mean: Number? = null
    )
}
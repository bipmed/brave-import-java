package org.bipmed.import.vcf

import htsjdk.variant.variantcontext.VariantContext
import htsjdk.variant.vcf.VCFFileReader
import org.bipmed.import.variant.Variant
import org.nield.kotlinstatistics.median
import org.nield.kotlinstatistics.percentile
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Component
import java.io.File

@Component
class VcfImporter(private val mongoTemplate: MongoTemplate) {


    fun import(filename: String, datasetId: String, assemblyId: String): Long {
        val vcfFileReader = VCFFileReader(File(filename), false)

        var count = 0L

        vcfFileReader.forEach {
            val coverages = it.genotypes.map { genotype -> genotype.dp }
            val genotypeQualities = it.genotypes.map { genotype -> genotype.gq }

            val variant = Variant(
                    snpIds = if (it.id != ".") it.id.split(';') else emptyList(),
                    geneSymbol = getGeneSymbol(it),
                    sampleCount = it.getAttributeAsString("NS", null)?.toLong(),
                    start = it.start.toLong(),
                    referenceName = it.contig.removePrefix("chr"),
                    datasetId = datasetId,
                    alleleFrequency = it.getAttributeAsStringList("AF", null).map { af -> af.toDouble() },
                    alternateBases = it.alternateAlleles.map { allele -> allele.baseString },
                    referenceBases = it.reference.baseString,
                    assemblyId = assemblyId,

                    coverage = Variant.Statistics(
                            min = coverages.min(),
                            q25 = coverages.percentile(25.0),
                            median = coverages.median(),
                            q75 = coverages.percentile(75.0),
                            max = coverages.max(),
                            mean = coverages.average()
                    ),

                    genotypeQuality = Variant.Statistics(
                            min = genotypeQualities.min(),
                            q25 = genotypeQualities.percentile(25.0),
                            median = genotypeQualities.median(),
                            q75 = genotypeQualities.percentile(75.0),
                            max = genotypeQualities.max(),
                            mean = genotypeQualities.average()
                    ),

                    clnsig = it.getAttributeAsString("CLNSIG", null)
            )

            mongoTemplate.insert(variant)
            count++
        }

        return count
    }

    private fun getGeneSymbol(variant: VariantContext): String? {
        return if (variant.hasAttribute("ANN")) {
            variant.getAttribute("ANN").toString().split('|')[3] // Gene Name
        } else {
            null
        }
    }

}
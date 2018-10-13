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
            val coverage = it.genotypes.map { genotype -> genotype.dp }
            val genQual = it.genotypes.map { genotype -> genotype.gq }

            val variant = Variant(
                    variantIds = listOf(it.id),
                    geneSymbol = getGeneSymbol(it),
                    sampleCount = it.getAttributeAsString("NS", null)?.toLong(),
                    start = it.start.toLong(),
                    referenceName = it.contig.removePrefix("chr"),
                    datasetId = datasetId,
                    alleleFrequency = it.getAttributeAsStringList("AF", null).map { af -> af.toDouble() },
                    alternateBases = it.alternateAlleles.map { allele -> allele.baseString },
                    referenceBases = it.reference.baseString,
                    assemblyId = assemblyId,

                    minCov = coverage.min(),
                    q25Cov = coverage.percentile(25.0),
                    medianCov = coverage.median(),
                    q75Cov = coverage.percentile(75.0),
                    maxCov = coverage.max(),
                    meanCov = coverage.average(),

                    minGenQual = genQual.min(),
                    q25GenQual = genQual.percentile(25.0),
                    medianGenQual = genQual.median(),
                    q75GenQual = genQual.percentile(75.0),
                    maxGenQual = genQual.max(),
                    meanGenQual = genQual.average()
            )

            mongoTemplate.insert(variant)
            count++
        }

        return count
    }

    private fun getGeneSymbol(it: VariantContext): String? {
        return if (it.hasAttribute("ANN")) {
            it.getAttribute("ANN").toString().split('|')[3]
        } else {
            null
        }
    }

}
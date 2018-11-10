package org.bipmed.brave.import.vcf

import htsjdk.variant.variantcontext.VariantContext
import htsjdk.variant.vcf.VCFFileReader
import org.bipmed.brave.import.variant.Variant
import org.nield.kotlinstatistics.median
import org.nield.kotlinstatistics.percentile
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.io.File
import kotlin.system.exitProcess

@Component
class VcfImporter(private val restTemplate: RestTemplate) {


    fun import(filename: String, datasetId: String, assemblyId: String): Long {
        val vcfFileReader = VCFFileReader(File(filename), false)

        var count = 0L

        vcfFileReader.forEach {
            val coverages = it.genotypes.map { genotype -> genotype.dp }
            val genotypeQualities = it.genotypes.map { genotype -> genotype.gq }

            val variant = Variant(
                    snpIds = if (it.id != ".") it.id.split(';') else emptyList(),
                    datasetId = datasetId,
                    assemblyId = assemblyId,
                    totalSamples = vcfFileReader.fileHeader.nGenotypeSamples,
                    referenceName = it.contig.removePrefix("chr"),
                    geneSymbol = getGeneSymbol(it),
                    sampleCount = it.getAttributeAsString("NS", null)?.toLong(),
                    start = it.start.toLong(),
                    alleleFrequency = it.getAttributeAsStringList("AF", null).map { af -> af.toDouble() },
                    alternateBases = it.alternateAlleles.map { allele -> allele.baseString },
                    referenceBases = it.reference.baseString,

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

            val responseEntity = restTemplate.postForEntity("/variants", variant, Variant::class.java)
            if (responseEntity.statusCode.isError) {
                println("ERROR: ${responseEntity.statusCode.reasonPhrase}.")
                exitProcess(1)
            }

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
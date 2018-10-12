package org.bipmed.import.vcf

import htsjdk.variant.variantcontext.VariantContext
import htsjdk.variant.vcf.VCFFileReader
import org.bipmed.import.variant.Variant
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Component
import java.io.File

@Component
class VcfImporter(private val mongoTemplate: MongoTemplate) {


    fun import(filename: String, datasetId: String, assemblyId: String): Long {
        val vcfFileReader = VCFFileReader(File(filename), false)

        val geneSymbolIndex = if (vcfFileReader.fileHeader.hasInfoLine("ANN")) {
            vcfFileReader
                    .fileHeader
                    .getInfoHeaderLine("ANN")
                    .description.split('|')
                    .indexOf(" Gene_Name ")
        } else {
            null
        }

        var count = 0L

        vcfFileReader.forEach {
            val variant = Variant(
                    variantIds = listOf(it.id),
                    geneSymbol = getGeneSymbol(geneSymbolIndex, it),
                    sampleCount = it.getAttributeAsString("NS", null)?.toLong(),
                    start = it.start.toLong(),
                    referenceName = it.contig.removePrefix("chr"),
                    datasetId = datasetId,
                    alleleFrequency = it.getAttributeAsStringList("AF", null).map { af -> af.toDouble() },
                    alternateBases = it.alternateAlleles.map { allele -> allele.baseString },
                    referenceBases = it.reference.baseString,
                    assemblyId = assemblyId)

            mongoTemplate.insert(variant)
            count++
        }

        return count
    }

    private fun getGeneSymbol(geneSymbolIndex: Int?, it: VariantContext): String? {
        return if (it.hasAttribute("ANN") && geneSymbolIndex != null) {
            it.getAttributeAsString("ANN", null).split('|')[geneSymbolIndex]
        } else {
            null
        }
    }

}
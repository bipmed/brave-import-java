package org.bipmed.import

import org.assertj.core.api.Assertions.assertThat
import org.bipmed.import.variant.Variant
import org.bipmed.import.vcf.VcfImporter
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@ActiveProfiles("test")
@SpringBootTest
class BipmedImportApplicationTests {

    @Autowired
    lateinit var vcfImporter: VcfImporter

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    val filename = this.javaClass.classLoader.getResource("test.ann.vcf.gz").path.toString()

    @Test
    fun import() {
        val count = vcfImporter.import(filename, "test", "hg19")
        assertThat(mongoTemplate.find(Query(), Variant::class.java)).isEqualTo(variants)
        assertThat(count).isEqualTo(5)
    }

    private val variants = listOf(
            Variant(
                    variantIds = listOf("rs6054257"),
                    datasetId = "test",
                    assemblyId = "hg19",
                    referenceName = "20",
                    start = 14370,
                    referenceBases = "G",
                    alternateBases = listOf("A"),
                    geneSymbol = "CHR_START-DEFB125",
                    alleleFrequency = listOf(0.5),
                    sampleCount = 3,

                    coverage = Variant.Statistics(
                            min = 1,
                            q25 = 1.0,
                            median = 5.0,
                            q75 = 8.0,
                            max = 8,
                            mean = 4.666666666666667
                    ),

                    genotypeQuality = Variant.Statistics(
                            min = 43,
                            q25 = 43.0,
                            median = 48.0,
                            q75 = 48.0,
                            max = 48,
                            mean = 46.333333333333336
                    )
            ),
            Variant(
                    datasetId = "test",
                    assemblyId = "hg19",
                    referenceName = "20",
                    start = 17330,
                    referenceBases = "T",
                    alternateBases = listOf("A"),
                    geneSymbol = "CHR_START-DEFB125",
                    alleleFrequency = listOf(0.017),
                    sampleCount = 3,

                    coverage = Variant.Statistics(
                            min = 3,
                            q25 = 3.0,
                            median = 3.0,
                            q75 = 5.0,
                            max = 5,
                            mean = 3.6666666666666665
                    ),

                    genotypeQuality = Variant.Statistics(
                            min = 3,
                            q25 = 3.0,
                            median = 41.0,
                            q75 = 49.0,
                            max = 49,
                            mean = 31.0
                    )
            ),
            Variant(
                    variantIds = listOf("rs6040355"),
                    datasetId = "test",
                    assemblyId = "hg19",
                    referenceName = "20",
                    start = 1110696,
                    referenceBases = "A",
                    alternateBases = listOf("G", "T"),
                    geneSymbol = "PSMF1",
                    alleleFrequency = listOf(0.333, 0.667),
                    sampleCount = 2,

                    coverage = Variant.Statistics(
                            min = 0,
                            q25 = 0.0,
                            median = 4.0,
                            q75 = 6.0,
                            max = 6,
                            mean = 3.3333333333333335
                    ),

                    genotypeQuality = Variant.Statistics(
                            min = 2,
                            q25 = 2.0,
                            median = 21.0,
                            q75 = 35.0,
                            max = 35,
                            mean = 19.333333333333332
                    )
            ),
            Variant(
                    datasetId = "test",
                    assemblyId = "hg19",
                    referenceName = "20",
                    start = 1230237,
                    referenceBases = "T",
                    alternateBases = listOf(),
                    geneSymbol = null,
                    sampleCount = 3,

                    coverage = Variant.Statistics(
                            min = 2,
                            q25 = 2.0,
                            median = 4.0,
                            q75 = 7.0,
                            max = 7,
                            mean = 4.333333333333333
                    ),

                    genotypeQuality = Variant.Statistics(
                            min = 48,
                            q25 = 48.0,
                            median = 54.0,
                            q75 = 61.0,
                            max = 61,
                            mean = 54.333333333333336
                    )
            ),
            Variant(
                    variantIds = listOf("microsat1"),
                    datasetId = "test",
                    assemblyId = "hg19",
                    referenceName = "20",
                    start = 1234567,
                    referenceBases = "GTC",
                    alternateBases = listOf("G", "GTCT"),
                    geneSymbol = "RAD21L1",
                    sampleCount = 3,

                    coverage = Variant.Statistics(
                            min = 2,
                            q25 = 2.0,
                            median = 3.0,
                            q75 = 4.0,
                            max = 4,
                            mean = 3.0
                    ),

                    genotypeQuality = Variant.Statistics(
                            min = 17,
                            q25 = 17.0,
                            median = 35.0,
                            q75 = 40.0,
                            max = 40,
                            mean = 30.666666666666668
                    )
            )
    )

}

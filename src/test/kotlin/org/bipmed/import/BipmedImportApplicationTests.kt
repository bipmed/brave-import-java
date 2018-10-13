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

                    minCov = 1,
                    q25Cov = 1.0,
                    medianCov = 5.0,
                    q75Cov = 8.0,
                    maxCov = 8,
                    meanCov = 4.666666666666667,

                    minGenQual = 43,
                    q25GenQual = 43.0,
                    medianGenQual = 48.0,
                    q75GenQual = 48.0,
                    maxGenQual = 48,
                    meanGenQual = 46.333333333333336
            ),
            Variant(
                    variantIds = listOf("."),
                    datasetId = "test",
                    assemblyId = "hg19",
                    referenceName = "20",
                    start = 17330,
                    referenceBases = "T",
                    alternateBases = listOf("A"),
                    geneSymbol = "CHR_START-DEFB125",
                    alleleFrequency = listOf(0.017),
                    sampleCount = 3,

                    minCov = 3,
                    q25Cov = 3.0,
                    medianCov = 3.0,
                    q75Cov = 5.0,
                    maxCov = 5,
                    meanCov = 3.6666666666666665,

                    minGenQual = 3,
                    q25GenQual = 3.0,
                    medianGenQual = 41.0,
                    q75GenQual = 49.0,
                    maxGenQual = 49,
                    meanGenQual = 31.0
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

                    minCov = 0,
                    q25Cov = 0.0,
                    medianCov = 4.0,
                    q75Cov = 6.0,
                    maxCov = 6,
                    meanCov = 3.3333333333333335,

                    minGenQual = 2,
                    q25GenQual = 2.0,
                    medianGenQual = 21.0,
                    q75GenQual = 35.0,
                    maxGenQual = 35,
                    meanGenQual = 19.333333333333332
            ),
            Variant(
                    variantIds = listOf("."),
                    datasetId = "test",
                    assemblyId = "hg19",
                    referenceName = "20",
                    start = 1230237,
                    referenceBases = "T",
                    alternateBases = listOf(),
                    geneSymbol = null,
                    sampleCount = 3,

                    minCov = 2,
                    q25Cov = 2.0,
                    medianCov = 4.0,
                    q75Cov = 7.0,
                    maxCov = 7,
                    meanCov = 4.333333333333333,

                    minGenQual = 48,
                    q25GenQual = 48.0,
                    medianGenQual = 54.0,
                    q75GenQual = 61.0,
                    maxGenQual = 61,
                    meanGenQual = 54.333333333333336
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

                    minCov = 2,
                    q25Cov = 2.0,
                    medianCov = 3.0,
                    q75Cov = 4.0,
                    maxCov = 4,
                    meanCov = 3.0,

                    minGenQual = 17,
                    q25GenQual = 17.0,
                    medianGenQual = 35.0,
                    q75GenQual = 40.0,
                    maxGenQual = 40,
                    meanGenQual = 30.666666666666668
            )
    )

}

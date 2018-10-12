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
        assertThat(mongoTemplate.find(Query(), Variant::class.java).size).isEqualTo(5)
        assertThat(count).isEqualTo(5)
    }

}

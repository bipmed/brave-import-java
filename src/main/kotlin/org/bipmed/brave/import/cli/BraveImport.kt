package org.bipmed.brave.import.cli

import org.bipmed.brave.import.vcf.VcfImporter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import kotlin.system.exitProcess

@Component
@Profile("!test")
class BraveImport(private val vcfImporter: VcfImporter) : CommandLineRunner {

    @Value("\${filename}")
    val filename: String = ""

    @Value("\${datasetId}")
    val datasetId: String = ""

    @Value("\${assemblyId}")
    val assemblyId: String = ""

    override fun run(vararg args: String) {
        if (filename.isNotBlank() && datasetId.isNotBlank() && assemblyId.isNotBlank()) {
            val count = vcfImporter.import(filename, datasetId, assemblyId)
            println("Imported $count variants.")
        } else {
            println("USAGE: java -jar brave-import.jar --url http://localhost:8080 --filename file.vcf.gz --datasetId bipmed --datasetId hg19")
            exitProcess(1)
        }
    }
}
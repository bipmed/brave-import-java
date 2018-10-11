package org.bipmed.import.cli

import org.bipmed.import.vcf.VcfImporter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import kotlin.system.exitProcess

@Component
@Profile("!test")
class BipmedImport(private val vcfImporter: VcfImporter) : CommandLineRunner {

    @Value("\${filename}")
    val filename: String = ""

    @Value("\${datasetId}")
    val datasetId: String = ""

    @Value("\${assemblyId}")
    val assemblyId: String = ""

    override fun run(vararg args: String) {
        if (filename.isNotBlank() && datasetId.isNotBlank() && assemblyId.isNotBlank()) {
            vcfImporter.import(filename, datasetId, assemblyId)
        } else {
            println("USAGE: java -jar bipmed-import.jar --filename file.vcf.gz --datasetId bipmed --datasetId hg19")
            exitProcess(1)
        }
    }
}
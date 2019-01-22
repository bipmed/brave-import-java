package org.bipmed.brave.cli;

import org.bipmed.brave.vcf.VcfImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class BraveImport implements CommandLineRunner {

    @Value("${filename}")
    private String filename;

    @Value("${datasetId}")
    private String datasetId;

    @Value("${assemblyId}")
    private String assemblyId;
    private VcfImporter vcfImporter;

    @Autowired
    public BraveImport(VcfImporter vcfImporter) {
        this.vcfImporter = vcfImporter;
    }

    @Override
    public void run(String... args) {
        if (!filename.isEmpty() && !datasetId.isEmpty() && !assemblyId.isEmpty()) {
            long count = vcfImporter.importVariants(filename, datasetId, assemblyId);
            System.out.println("Imported " + count + " variants.");
        } else {
            System.out.println("USAGE: java -jar brave-import.jar --url http://localhost:8080 --filename file.vcf.gz --datasetId bipmed --datasetId hg19");
            System.exit(1);
        }
    }
}

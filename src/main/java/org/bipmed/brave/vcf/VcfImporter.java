package org.bipmed.brave.vcf;

import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.Genotype;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFFileReader;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.bipmed.brave.variant.Statistics;
import org.bipmed.brave.variant.Variant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class VcfImporter {

    private RestTemplate restTemplate;

    @Autowired
    public VcfImporter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public long importVariants(String filename, String datasetId, String assemblyId) {
        VCFFileReader vcfFileReader = new VCFFileReader(new File(filename), false);

        long count = 0L;

        for (VariantContext variantContext : vcfFileReader) {
            DescriptiveStatistics coverages = new DescriptiveStatistics(variantContext.getGenotypes().stream().mapToDouble(Genotype::getDP).toArray());
            DescriptiveStatistics genotypeQualities = new DescriptiveStatistics(variantContext.getGenotypes().stream().mapToDouble(Genotype::getGQ).toArray());

            Variant variant = Variant.builder()
                    .snpIds(!variantContext.getID().equals(".") ? Arrays.asList(variantContext.getID().split(";")) : Collections.emptyList())
                    .datasetId(datasetId)
                    .assemblyId(assemblyId)
                    .totalSamples(vcfFileReader.getFileHeader().getNGenotypeSamples())
                    .referenceName(variantContext.getContig().replaceFirst("^chr", ""))
                    .geneSymbol(getAnnotationColumn(variantContext, 3))
                    .sampleCount(Long.parseLong(variantContext.getAttributeAsString("NS", null)))
                    .start((long) variantContext.getStart())
                    .alleleFrequency(variantContext.getAttributeAsStringList("AF", null).stream().map(Float::parseFloat).collect(Collectors.toList()))
                    .alternateBases(variantContext.getAlternateAlleles().stream().map(Allele::getBaseString).collect(Collectors.toList()))
                    .referenceBases(variantContext.getReference().getBaseString())

                    .coverage(Statistics.builder()
                            .min(coverages.getMin())
                            .q25(coverages.getPercentile(25))
                            .median(coverages.getPercentile(50))
                            .q75(coverages.getPercentile(75))
                            .max(coverages.getMax())
                            .mean(coverages.getMean())
                            .build())

                    .genotypeQuality(Statistics.builder()
                            .min(genotypeQualities.getMin())
                            .q25(genotypeQualities.getPercentile(25))
                            .median(genotypeQualities.getPercentile(50))
                            .q75(genotypeQualities.getPercentile(75))
                            .max(genotypeQualities.getMax())
                            .mean(genotypeQualities.getMean())
                            .build())

                    .clnsig(variantContext.getAttributeAsString("CLNSIG", null))
                    .hgvs(getAnnotationColumn(variantContext, 9))
                    .type(getAnnotationColumn(variantContext, 5))
                    .build();

            try {
                restTemplate.postForLocation("/variants", variant);
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
                System.exit(1);

            }

            count++;
        }

        return count;
    }

    private List<String> getAnnotationColumn(VariantContext variant, Integer index) {
        if (variant.hasAttribute("ANN")) {
            return Arrays.stream(variant.getAttribute("ANN").toString().split(","))
                    .map(it -> it.split("\\|")[index])
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }
}

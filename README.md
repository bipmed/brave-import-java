# Utility tool for importing annotated VCF files into BIPMed Server

## VCF requirements

Info columns:

- `NS` - Number of Samples With Data
- `AF` - Allele Frequency
- `ANN` - (Optional) Column added by snpEff tool

## Annotate VCF

```bash
curl -L http://sourceforge.net/projects/snpeff/files/snpEff_latest_core.zip > snpEff_latest_core.zip
unzip snpEff_latest_core.zip 
java -Xmx4g -jar snpEff/snpEff.jar hg19 src/test/resources/test.vcf.gz > src/test/resources/test.ann.vcf
bgzip src/test/resources/test.ann.vcf
tabix src/test/resources/test.ann.vcf.gz
```

## Build Jar file

```bash
mvn package -DskipTests
```

## Build docker image
   
```bash
mvn install -DskipTests dockerfile:build
```

## Import VCF

```bash
java -jar target/bipmed-import.jar \
    --filename=src/test/resources/test.ann.vcf.gz \
    --datasetId=bipmed \
    --assemblyId=hg19
```

## Import VCF to BIPMed DB container

```bash
docker container run \
    --rm \
    --name bipmed_db \
    --network bipmed \
    mongo
```

```bash
docker run --rm \
    -v `pwd`/src/test/resources/test.ann.vcf.gz:/test.ann.vcf.gz \
    --network bipmed \
    -e SPRING_DATA_MONGODB_URI=mongodb://bipmed_db:27017/bipmed \
    welliton/bipmed-import \
    --filename=/test.ann.vcf.gz \
    --datasetId=bipmed \
    --assemblyId=hg19
```
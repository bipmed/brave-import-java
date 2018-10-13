# Utility tool for importing annotated VCF files into BIPMed Server

## VCF requirements

`INFO` columns:

- `NS` - Number of samples with data,
- `AF` - Allele frequency
- `ANN` - Standard annotation format. Added by snpEff and ClinEff
- `CLNSIG` Variant clinical significance. Added by ClinEff

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
package org.phenopackets.phenopackettools.cli.examples;

import org.phenopackets.phenopackettools.builder.PhenopacketBuilder;
import org.phenopackets.phenopackettools.builder.builders.*;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.*;

import java.util.ArrayList;
import java.util.List;

import static org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder.ontologyClass;

/**
 * Léonard A, et al. Prenatal diagnosis of fetal cataract: case report and review of the literature.
 * Fetal Diagn Ther. 2009;26(2):61-7.  PMID:19752522.
 */
public class WarburgMicroSyndrome implements PhenopacketExample {
    private final static String PHENOPACKET_ID = "id.1";
    private final TimeElement age4days = TimeElements.age("P4D");
    private final Phenopacket phenopacket;
    public WarburgMicroSyndrome() {
        var metadata = MetaDataBuilder.builder("2022-04-17T10:35:00Z", "biocurator")
                .addResource(Resources.hpoVersion("2022-04-15"))
                .addResource(Resources.mondoVersion("v2022-04-04"))
                .build();
        Individual proband = IndividualBuilder.builder("case1").
                ageAtLastEncounter("P4D").
                male().
                build();


        phenopacket = PhenopacketBuilder.create(PHENOPACKET_ID, metadata)
                .individual(proband)
                .addDisease(getDisease())
                .addPhenotypicFeatures(getPhenotypicFeatures())
                .build();
    }



    List<PhenotypicFeature> getPhenotypicFeatures() {

        List<PhenotypicFeature> features = new ArrayList<>();
        //Increased fetal lens echogenicity  HP:0034248
        //  27 2/7
        TimeElement gestationalAge = TimeElements.gestationalAge(27, 2);
        PhenotypicFeature increasedFetalLensEcho = PhenotypicFeatureBuilder
                .builder("HP:0034248", "Increased fetal lens echogenicity")
                .onset(gestationalAge)
                .build();
        features.add(increasedFetalLensEcho);

        PhenotypicFeature anteNatalMicrophthalmia = PhenotypicFeatureBuilder
                .builder("HP:0000568", "Microphthalmia")
                .onset(gestationalAge)
                .excluded()
                .build();
        features.add(anteNatalMicrophthalmia);
        /*
        Postnatal clinical examination re-
vealed the presence of bilateral lower limbs clinodactyly
(toes 4 and 5), low implantation of the ears, micropenis
associated with a small right testicle. Development retar-
dation and major axial hypotonia were also observed,
with permanently clenched fists and positive Babinski’s
sign. Postnatal ophthalmological examination showed
the additional presence of microphthalmia, not observed
antenatally, with an ocular globe axis of 12 mm (5th cen-
tile: 16 mm; 95th centile: 21 mm) [
         */


        PhenotypicFeature lowSetEars = PhenotypicFeatureBuilder
                .builder("HP:0000369", "Low-set ears")
                .onset(age4days)
                .build();
        features.add(lowSetEars);

        PhenotypicFeature axialHypo = PhenotypicFeatureBuilder
                .builder("HP:0008936", "Axial hypotonia")
                .onset(age4days)
                .build();
        features.add(axialHypo);

        PhenotypicFeature microphthalmia = PhenotypicFeatureBuilder
                .builder("HP:0000568", "Microphthalmia")
                .onset(age4days)
                .build();
        features.add(microphthalmia);
        return features;
    }



    Disease getDisease() {
        var warburgMS = ontologyClass("MONDO:0016649", "Warburg micro syndrome");
        return DiseaseBuilder.builder(warburgMS)
                .onset(age4days)
                .build();
    }



    @Override
    public Phenopacket getPhenopacket() {
        return phenopacket;
    }
}

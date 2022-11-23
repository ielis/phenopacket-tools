package org.phenopackets.phenopackettools.cli.examples;

import org.phenopackets.phenopackettools.builder.PhenopacketBuilder;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.phenopackettools.builder.builders.*;
import org.phenopackets.phenopackettools.builder.constants.Laterality;
import org.phenopackets.phenopackettools.builder.constants.Unit;
import org.phenopackets.schema.v2.core.*;

import java.util.List;

import static org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder.ontologyClass;

/**
 * Late-Onset Anterior Dislocation  of a Posterior Chamber
 * Intraocular Lens in a Patient with Pseudoexfoliation
 * Syndrome Case Rep Ophthalmol 2011;2:1–4  DOI: 10.1159/000323861
 * RA: Cataract and PEX -> Cataractsurgery -> Emmetropia -> 1J1M Myopia + elevated eyeIop -> Brimonidine -> Yag-IT -> normal Iop
 * LA: Cataract -> Cataractsurgery -> Emmetropia
 * Result: RA/LA: Monovision
 * Vorderkammertiefe!!!!!!!!!!
 *
 * @author Markus Ladewig
 */

public class Pseudoexfoliation implements PhenopacketExample {
    private static final String PHENOPACKET_ID = "arbitrary.id";
    private static final String PROBAND_ID = "proband A";

    private static final OntologyClass Pseudoexfoliation = ontologyClass("HP:0012627", "Pseudoexfoliation");
    private static final OntologyClass Pseudophakia = ontologyClass("HP:0500081", "Pseudophakia");
    private static final OntologyClass LEFT_EYE = ontologyClass("UBERON:0004548", "left eye");
    private static final OntologyClass RIGHT_EYE = ontologyClass("UBERON:0004549", "right eye");
    private static final OntologyClass Cataract = ontologyClass("HP:0000518", "cataract");
    private static final OntologyClass visusPercent = ontologyClass("NCIT:C48570", "Percent Unit");
    private static final OntologyClass ocularHypertension = ontologyClass("HP:0007906", "Ocular hypertension");


    //
    private final Phenopacket phenopacket;

    public Pseudoexfoliation() {
        // Metadaten
        var authorAssertion = EvidenceBuilder.authorStatementEvidence("PMID:21532993", "Late-Onset Anterior Dislocation  of a Posterior Chamber Intraocular Lens in a Patient Pseudoexfoliation Syndrome");
        var metadata = MetaDataBuilder.builder("2021-05-14T10:35:00Z", "anonymous biocurator")
                .addResource(Resources.uberonVersion("2022-08-19"))
                .addResource(Resources.ncitVersion("22.07d"))
                .addResource(Resources.hpoVersion("2022-06-11"))
                .addResource(Resources.ucum())
                .addResource(Resources.loincVersion("2.73"))
                .addResource(Resources.mondoVersion("2022-04-04"))
                .addResource(Resources.drugCentralVersion("2022-08-22"))

                .build();
        Individual proband = IndividualBuilder.builder(PROBAND_ID).
                ageAtLastEncounter("P70Y").
                male().
                XY().
                build();

        phenopacket = PhenopacketBuilder.create(PHENOPACKET_ID, metadata)
                .individual(proband)
                .addMeasurements(getMeasurements())
                .addMeasurements(getAcuityAndRefractionMeasurements())
                .addMeasurements(getMeasurementsYears2())
                .addPhenotypicFeatures(getPhenotypicFeatures())
                .addDisease(getDiseaseExfoliationSyndrome())
                .addMedicalAction(cataractsurgeryRight())
                .addMedicalAction(cataractsurgeryLeft())
                .addMedicalAction(brimonidine())
                .addMedicalAction(nd_yag_iridotomy())
                .build();

    }

    Disease getDiseaseExfoliationSyndrome() {
        OntologyClass exfoliationSyndrome = ontologyClass("MONDO:0008327", "exfoliation syndrome");
        TimeElement adult = TimeElements.adultOnset();
        return DiseaseBuilder.builder(exfoliationSyndrome)
                .onset(adult)
                .primarySite(RIGHT_EYE)
                .build();
    }

    @Override
    public Phenopacket getPhenopacket() {
        return phenopacket;
    }

    /**
     * Uneventful clear-cornea phacoemulsification with PC/IOL
     * implantation in the right eye (OD) in January 2006.
     */
    MedicalAction cataractsurgeryRight() {
        ProcedureBuilder builder = ProcedureBuilder.builder("NCIT:C157809", "Cataract Surgery");
        TimeElement age = TimeElements.age("P70Y");
        builder.bodySite(RIGHT_EYE).performed(age);
        MedicalActionBuilder mabuilder = MedicalActionBuilder.builder(builder.build())
                .treatmentTarget(Cataract)
                .treatmentIntent(Pseudophakia);
        return mabuilder.build();
    }

    /**
     * "Six weeks later (Feb./Marc 2006, the patient was subjected to an uncomplicated cataract surgery OS."
     * No other event on the left eye
     */
    MedicalAction cataractsurgeryLeft() {
        ProcedureBuilder builder = ProcedureBuilder.builder("NCIT:C157809", "Cataract Surgery");
        TimeElement age = TimeElements.age("P70Y6W"); //6 Weeks later Cat surgery OS
        builder.bodySite(LEFT_EYE).performed(age);
        MedicalActionBuilder mabuilder = MedicalActionBuilder.builder(builder.build())
                .treatmentTarget(Cataract)
                .treatmentIntent(Pseudophakia);
        return mabuilder.build();
    }


    /**
     * Note: Most measurements of the left eye are omitted for brevity
     *
     * @return
     */
    List<Measurement> getAcuityAndRefractionMeasurements() {
        // visual acuity 1,0 right eye one week after Cataract surgery

        TypedQuantity visus100 = TypedQuantityBuilder.of(ontologyClass("NCIT:C87149", "Visual Acuity"),
                QuantityBuilder.of(visusPercent, 100));
        TimeElement years70weeks1 = TimeElements.age("P70Y1W"); // 1 Week after surgery OD
        var visionAssessment = ontologyClass("NCIT:C156778", "Vision Assessment");
        Measurement visusMeasurement = MeasurementBuilder
                .builder(visionAssessment, ComplexValueBuilder.of(visus100))
                .timeObserved(years70weeks1)
                .build();

        // Refraction after 1 week right eye -0.25/-0.5/110 degrees
        OntologyClass sphericalrefraction = ontologyClass("LOINC:79895-9", "Subjective refraction method");
        ReferenceRange ref = ReferenceRangeBuilder.of(sphericalrefraction, -30, 30);
        OntologyClass rightEyesphericalrefraction = ontologyClass("LOINC:79850-4", "Right eye spherical refraction");
        Value rightEyeValue = ValueBuilder.of(Unit.diopter(), -0.25, ref);
        var sphericalRefractionMeasurement = MeasurementBuilder
                .builder(sphericalrefraction, rightEyeValue)
                .timeObserved(years70weeks1)
                .build();

        OntologyClass rightEyecylindricalrefraction = ontologyClass("LOINC:79846-2", "Right eye cylindrical refraction");
        Quantity rightEyeValueCylinder = QuantityBuilder.of(Unit.diopter(), -0.5, ref);
        var reCylinderTypedValue = TypedQuantityBuilder.of(rightEyecylindricalrefraction, rightEyeValueCylinder);
        OntologyClass reCylindricalDegree = ontologyClass("LOINC:9829-8", "Recht eye cylindrical degree");
        var reCylindricalDegreeQuantity = QuantityBuilder.of(Unit.degreeOfAngle(), 110, ref);
        var reCylindricalDegreeTypedQ = TypedQuantityBuilder.of(reCylindricalDegree, reCylindricalDegreeQuantity);
        var cylRef = ComplexValueBuilder.of(List.of(reCylinderTypedValue, reCylindricalDegreeTypedQ));
        var refractionAssessment = ontologyClass("SNOMEDCT:252886007", "Refraction assessment (procedure)");

        var rightEyeCylindricalRefractionMeasurement = MeasurementBuilder
                .builder(refractionAssessment, cylRef)
                .timeObserved(years70weeks1)
                .build();

        Measurement rightEyecylindricalmeasurement = MeasurementBuilder
                .builder(rightEyesphericalrefraction, rightEyeValue)
                .timeObserved(years70weeks1)
                .build();
        //leftEyeMeasurement, rightEyeMeasurement,  TODO -- add to list of returned items
        return List.of(visusMeasurement, sphericalRefractionMeasurement, rightEyeCylindricalRefractionMeasurement,
                rightEyecylindricalmeasurement);
    }


    //  ONE YEAR AFTER WITH SHALLOW ANTERIOR CHAMBER: visual acuity 1,0 right eye
    List<Measurement> getAcuityAndRefractionMeasurementsOneYearLater() {
        TypedQuantity visus100 = TypedQuantityBuilder.of(ontologyClass("NCIT:C87149", "Visual Acuity"),
                QuantityBuilder.of(visusPercent, 100));
        TimeElement age = TimeElements.age("P71Y2M"); // Feb. 2007
        var visionAssessment_plus_one_year = ontologyClass("NCIT:C156778", "Vision Assessment");
        Measurement visusMeasurement = MeasurementBuilder
                .builder(visionAssessment_plus_one_year, ComplexValueBuilder.of(visus100)).build();


        // ONE YEAR AFTER WITH SHALLOW ANTERIOR CHAMBER: Refraction after 1 year right eye –3.75/–0.5/110°.


        OntologyClass sphericalrefraction = ontologyClass("LOINC:79895-9", "Subjective refraction method");
        ReferenceRange ref = ReferenceRangeBuilder.of(sphericalrefraction, -30, 30);
        OntologyClass rightEyesphericalrefraction_plus_one_year =
                OntologyClassBuilder.ontologyClass("LOINC:79850-4", "Right eye spherical refraction");
        Value rightEyeValue = ValueBuilder.of(Unit.diopter(), -3.75, ref);
        OntologyClass rightEyecylindricalrefraction_plus_one_year =
                OntologyClassBuilder.ontologyClass("LOINC:79846-2", "Right eye cylindrical refraction");
        Value rightEyeValueCylinder = ValueBuilder.of(Unit.diopter(), -0.5, ref);
        OntologyClass rightEyecylindricaldegree_plus_one_year =
                OntologyClassBuilder.ontologyClass("LOINC:9829-8", "Recht eye cylindrical degree");
        Value rightEyeVauleCylindricaldegree_plus_one_year = ValueBuilder.of(Unit.degreeOfAngle(), 110, ref);

        TimeElement age71years1month = TimeElements.age("P71Y1M");
        Measurement rightEyecylindricalmeasurement_plus_one_year = MeasurementBuilder
                .builder(rightEyesphericalrefraction_plus_one_year, rightEyeValue).build();

        // ONE YEAR AFTER WITH SHALLOW ANTERIOR CHAMBER: Refraction after Yag-IT right eye: –2.75/–0.75/110°

        OntologyClass rightEyesphericalrefraction_plus_one_year_after_Yag =
                OntologyClassBuilder.ontologyClass("LOINC:79850-4", "Right eye spherical refraction");
        Value rightEyeValue_after_Yag = ValueBuilder.of(Unit.diopter(), -2.75, ref);
        OntologyClass rightEyecylindricalrefraction_plus_one_year_after_Yag =
                OntologyClassBuilder.ontologyClass("LOINC:79846-2", "Right eye cylindrical refraction");
        Value rightEyeValueCylinder_after_Yag = ValueBuilder.of(Unit.diopter(), -0.75, ref);
        OntologyClass rightEyecylindricaldegree_plus_one_year_after_Yag =
                OntologyClassBuilder.ontologyClass("LOINC:9829-8", "Recht eye cylindrical degree");
        Value rightEyeVauleCylindricaldegree_plus_one_year_after_Yag = ValueBuilder.of(Unit.degreeOfAngle(), 110, ref);

        TimeElement age71years1month6h = TimeElements.age("P71Y1M0W0DT6H");
        Measurement rightEyecylindricalmeasurement_plus_one_year_after_Yag = MeasurementBuilder
                .builder(rightEyesphericalrefraction_plus_one_year_after_Yag, rightEyeValue).build();

        return List.of();
    }


    /**
     * Measurements made ONE YEAR AFTER WITH SHALLOW ANTERIOR CHAMBER:
     * The intraocular pressure was 29 mmHg in the right eye before YAG
     *
     * @return
     */
    List<Measurement> getMeasurementsYears2() {
        OntologyClass iop = ontologyClass("LOINC:56844-4", "Intraocular pressure of Eye");
        ReferenceRange ref = ReferenceRangeBuilder.of(iop, 10, 21);
        OntologyClass rightEyeIop =
                OntologyClassBuilder.ontologyClass("LOINC:79892-6", "Right eye Intraocular pressure");
        Value rightEyeValue = ValueBuilder.of(Unit.mmHg(), 29, ref);
        TimeElement age = TimeElements.age("P71Y1M"); //Druckerhöhung 1J1M nach Cataractsurgery
        Measurement rightEyeMeasurement = MeasurementBuilder.builder(rightEyeIop, rightEyeValue).timeObserved(age).build();

        return List.of(rightEyeMeasurement);
    }



    /**
     * IOP was successfully regulated OD after Nd:YAG iridotomy (direct postoperative IOP: 14 mm Hg).
     * @return
     */
    List<Measurement> getMeasurements() {
        OntologyClass iop = ontologyClass("LOINC:56844-4", "Intraocular pressure of Eye");
        ReferenceRange ref = ReferenceRangeBuilder.of(iop, 10, 21);
        // The following is postYAG
        OntologyClass rightEyeIop =
                OntologyClassBuilder.ontologyClass("LOINC:79892-6", "Right eye Intraocular pressure");
        Value rightEyeValue = ValueBuilder.of(Unit.mmHg(), 14, ref);// after Nd:YAG iridotomy
        TimeElement p71Y1M1D = TimeElements.age("P71Y1M1D");



        //IOP was successfully regulated OD after Nd:YAG iridotomy (direct postoperative IOP: 14 mm Hg).

        // Anterior chamber depth was 3.93 mm. OS anterior chamber depth was 5.21 mm
        OntologyClass acdod = ontologyClass("SCTID: 397312009", "Intraocular lens anterior chamber depth");
        ReferenceRange ref2 = ReferenceRangeBuilder.of(acdod, 0, 10);

        OntologyClass reAcDepth =
                OntologyClassBuilder.ontologyClass("SCTID:397312009", "Intraocular lens anterior chamber depth");
        Value reAcdValue = ValueBuilder.of(Unit.millimeter(), 3.93, ref);//
        var reAntChamberDepthMeasurement = MeasurementBuilder
                .builder(reAcDepth, reAcdValue)
                .timeObserved(p71Y1M1D)
                .build();

        OntologyClass leAcDepth =
                OntologyClassBuilder.ontologyClass("SCTID:397312009", "Intraocular lens anterior chamber depth");
        Value leAcdValue = ValueBuilder.of(Unit.millimeter(), 5.21, ref);//
        var leAntChamberDepthMeasurement = MeasurementBuilder
                .builder(leAcDepth, leAcdValue)
                .timeObserved(p71Y1M1D)
                .build();

        // Measurement leftEyeMeasurement = MeasurementBuilder.builder(leftEyeIop, leftEyeValue).timeObserved(age).build();
        Measurement rightEyeMeasurement = MeasurementBuilder.builder(rightEyeIop, rightEyeValue).timeObserved(p71Y1M1D).build();


        // ONE YEAR AFTER: visual acuity 1,0 right eye

        TypedQuantity visus100 = TypedQuantityBuilder.of(ontologyClass("NCIT:C87149", "Visual Acuity"),
                QuantityBuilder.of(visusPercent, 100));
        var visionAssessment = ontologyClass("NCIT:C156778", "Vision Assessment");
        Measurement visusMeasurement = MeasurementBuilder
                .builder(visionAssessment, ComplexValueBuilder.of(visus100)).build();
        return List.of(rightEyeMeasurement, visusMeasurement, reAntChamberDepthMeasurement, leAntChamberDepthMeasurement);
    }

/* Result: Monovision Although  the myopic shift OD was not eliminated, the patient was satisfied with the monovision,
which was achieved unintentionally and, therefore, we did not proceed to an exchange surgery of the PC/IOL.
 */

    MedicalAction brimonidine() {
        OntologyClass brimonidine = ontologyClass("DrugCentral:395", "brimonidine");
        OntologyClass administration = ontologyClass("NCIT:C29302", "Ophthalmic Solution"); //Eye drop
        Quantity quantity = QuantityBuilder.of(Unit.mgPerKg(), 0.002);// quantity of eye drop?
        TimeInterval interval = TimeIntervalBuilder.of("2022-07-07", "2022-07-07"); //omit?
        TimeElement age = TimeElements.age("P71Y1M");
        OntologyClass once = ontologyClass("NCIT:C64576", "Once");

        DoseInterval doseInterval = DoseIntervalBuilder.of(quantity, once, interval);

        Treatment treatment = TreatmentBuilder.builder(brimonidine)
                .routeOfAdministration(administration)
                .addDoseInterval(doseInterval).build();

        return MedicalActionBuilder.builder(treatment)
                .addAdverseEvent(ontologyClass("HP:0025637", "Vasospasm"))
                .treatmentTarget(ocularHypertension)
                .treatmentTerminationReason(ontologyClass("NCIT:C41331", "Adverse Event"))
                .build();
    }

    /* Medical action Nd:YAG iridotomy (one year after initial Cataractsurgery)*/

    MedicalAction nd_yag_iridotomy() {
        ProcedureBuilder builder = ProcedureBuilder.builder("LOINC:29031-2", "Right eye YAG mode");
        TimeElement age = TimeElements.age("P71Y1M");
        builder.bodySite(RIGHT_EYE).performed(age);
        MedicalActionBuilder mabuilder = MedicalActionBuilder.builder(builder.build())
                .treatmentTarget(Cataract)
                .treatmentIntent(Pseudophakia);
        return mabuilder.build();
    }

    List<PhenotypicFeature> getPhenotypicFeatures() {
        TimeElement age70years = TimeElements.age("P70Y");
        PhenotypicFeature emmetropia = PhenotypicFeatureBuilder.
                builder("HP:0000539", "Abnormality of refraction") // Verneinung, NO Abnormality...
                .addModifier(Laterality.right())
                .onset(age70years)
                .excluded()
                .build();
        TimeElement age71years = TimeElements.age("P71Y1M");
        PhenotypicFeature myopia = PhenotypicFeatureBuilder.
                builder("HP:0000545", "Myopia")
                .addModifier(Laterality.right())
                .onset(age71years)
                .build();
        PhenotypicFeature iopi = PhenotypicFeatureBuilder. // iopi = Intraocular Pressure Increased
                builder("NCIT:C50618", "Intraocular Pressure Increased")
                .addModifier(Laterality.right())
                .onset(age71years)
                .build();
        // OD Anterior chamber depth was 3.93 mm. OS anterior chamber depth was 5.21 mm.


        PhenotypicFeature excludedPhacodonesis = PhenotypicFeatureBuilder.
                builder("HP:0012629", "Phakodonesis")
                .excluded()
                .build();
        PhenotypicFeature excludedpupilabnormality = PhenotypicFeatureBuilder.
                builder("HP:0007686", "Abnormal pupillary function")
                .excluded()
                .build();
        PhenotypicFeature monovision = PhenotypicFeatureBuilder.
                builder("SCTID:414775001", "monovision")// alternative to snomed?
                .excluded()
                .build();
        return List.of(emmetropia, myopia, iopi, excludedpupilabnormality, excludedPhacodonesis, monovision);
    }

}


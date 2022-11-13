package org.phenopackets.phenopackettools.builder.builders;

import org.phenopackets.schema.v2.core.Evidence;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.PhenotypicFeature;
import org.phenopackets.schema.v2.core.TimeElement;

import java.util.List;

/**
 * This has convenience methods for building PhenotypicFeature messages with some
 * commonly used options.
 *
 * @author Peter N Robinson
 */
public class PhenotypicFeatureBuilder {

    private final PhenotypicFeature.Builder builder;

    private PhenotypicFeatureBuilder(OntologyClass feature) {
        builder = PhenotypicFeature.newBuilder().setType(feature);
    }

    public static PhenotypicFeature of(OntologyClass feature) {
        return PhenotypicFeature.newBuilder().setType(feature).build();
    }

    public static PhenotypicFeature of(String id, String label) {
        OntologyClass ontologyClass = OntologyClassBuilder.ontologyClass(id, label);
        return of(ontologyClass);
    }

    public static PhenotypicFeatureBuilder builder(OntologyClass feature) {
        return new PhenotypicFeatureBuilder(feature);
    }

    public static PhenotypicFeatureBuilder builder(String id, String label) {
        OntologyClass ontologyClass = OntologyClassBuilder.ontologyClass(id, label);
        return builder(ontologyClass);
    }

    public PhenotypicFeatureBuilder onset(TimeElement time) {
        builder.setOnset(time);
        return this;
    }

    /**
     * @param iso8601 A string such as P10Y4M2D representing the age of onset/observation
     */
    public PhenotypicFeatureBuilder iso8601onset(String iso8601) {
        builder.setOnset(TimeElements.age(iso8601));
        return this;
    }

    public PhenotypicFeatureBuilder congenitalOnset() {
        TimeElement time = TimeElements.congenitalOnset();
        builder.setOnset(time);
        return this;
    }

    public PhenotypicFeatureBuilder embryonalOnset() {
        TimeElement time = TimeElements.embryonalOnset();
        builder.setOnset(time);
        return this;
    }

    public PhenotypicFeatureBuilder fetalOnset() {
        TimeElement time = TimeElements.fetalOnset();
        builder.setOnset(time);
        return this;
    }

    public PhenotypicFeatureBuilder infantileOnset() {
        TimeElement time = TimeElements.infantileOnset();
        builder.setOnset(time);
        return this;
    }

    public PhenotypicFeatureBuilder childhoodOnset() {
        TimeElement time = TimeElements.childhoodOnset();
        builder.setOnset(time);
        return this;
    }

    public PhenotypicFeatureBuilder adultOnset() {
        TimeElement time = TimeElements.adultOnset();
        builder.setOnset(time);
        return this;
    }

    public PhenotypicFeatureBuilder resolution(TimeElement time) {
        builder.setResolution(time);
        return this;
    }

    public PhenotypicFeatureBuilder severity(String id, String label) {
        OntologyClass severity = OntologyClassBuilder.ontologyClass(id, label);
        return severity(severity);

    }

    public PhenotypicFeatureBuilder severity(OntologyClass severity) {
        builder.setSeverity(severity);
        return this;
    }

    public PhenotypicFeatureBuilder excluded() {
        builder.setExcluded(true);
        return this;
    }

    public PhenotypicFeatureBuilder addEvidence(Evidence evidence) {
        builder.addEvidence(evidence);
        return this;
    }

    public PhenotypicFeatureBuilder addAllEvidence(List<Evidence> evidenceList) {
        builder.addAllEvidence(evidenceList);
        return this;
    }

    public PhenotypicFeatureBuilder addModifier(OntologyClass modifier) {
        builder.addModifiers(modifier);
        return this;
    }

    public PhenotypicFeatureBuilder addAllModifiers(List<OntologyClass> modifiers) {
        builder.addAllModifiers(modifiers);
        return this;
    }

    public PhenotypicFeatureBuilder description(String text) {
        builder.setDescription(text);
        return this;
    }

    public PhenotypicFeature build() {
        return builder.build();
    }

}

package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.PhenotypicFeature;
import org.phenopackets.schema.v2.core.TimeElement;

import java.util.ArrayList;
import java.util.List;

import static org.phenopackets.phenotools.builder.builders.PhenoBuilder.ontologyClass;

/**
 * This has convenience methods for building PhenotypicFeature messages with some
 * commonly used options.
 * @author Peter N Robinson
 */
public class PhenotypicFeatureBuilder {

    public static final OntologyClass SEVERE = ontologyClass("HP:0012828", "Severe");

    private PhenotypicFeature.Builder builder;

    public PhenotypicFeatureBuilder(String id, String label) {
        OntologyClass clz = ontologyClass(id,label);
        builder = PhenotypicFeature.newBuilder().setType(clz);
    }

    public PhenotypicFeatureBuilder onset(TimeElement time) {
        builder = builder.mergeFrom(builder.build()).setOnset(time);
        return this;
    }

    public PhenotypicFeatureBuilder congenitalOnset() {
        TimeElement time = TimeElementBuilder.congenitalOnset();
        builder = builder.mergeFrom(builder.build()).setOnset(time);
        return this;
    }

    public PhenotypicFeatureBuilder childhoodOnset() {
        TimeElement time = TimeElementBuilder.childhoodOnset();
        builder = builder.mergeFrom(builder.build()).setOnset(time);
        return this;
    }

    public PhenotypicFeatureBuilder adultOnset() {
        TimeElement time = TimeElementBuilder.adultOnset();
        builder = builder.mergeFrom(builder.build()).setOnset(time);
        return this;
    }

    public PhenotypicFeatureBuilder severity(String id, String label) {
        OntologyClass severity = ontologyClass(id, label);
        builder = builder.mergeFrom(builder.build()).setSeverity(severity);
        return this;
    }

    public PhenotypicFeatureBuilder severe() {
        builder = builder.mergeFrom(builder.build()).setSeverity(SEVERE);
        return this;
    }

    public PhenotypicFeature build() {
        return builder.build();
    }

    public static PhenotypicFeatureBuilder create(String id, String label) {
        return new PhenotypicFeatureBuilder(id, label);
    }
}

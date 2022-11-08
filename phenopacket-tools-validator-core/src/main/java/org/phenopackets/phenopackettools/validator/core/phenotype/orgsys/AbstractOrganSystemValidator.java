package org.phenopackets.phenopackettools.validator.core.phenotype.orgsys;

import com.google.protobuf.MessageOrBuilder;
import org.monarchinitiative.phenol.base.PhenolRuntimeException;
import org.monarchinitiative.phenol.ontology.algo.OntologyAlgorithm;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.monarchinitiative.phenol.ontology.data.Term;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;
import org.phenopackets.phenopackettools.validator.core.phenotype.base.BaseHpoValidator;
import org.phenopackets.phenopackettools.validator.core.phenotype.util.PhenotypicFeaturesByExclusionStatus;
import org.phenopackets.phenopackettools.validator.core.phenotype.util.Util;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.PhenotypicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * The base class for an organ system validator to check if each phenopacket or family/cohort member have annotation
 * for an organ system represented by a top-level HPO term
 * (e.g. <a href="https://hpo.jax.org/app/browse/term/HP:0040064">Abnormality of limbs</a>).
 * The annotation comprises either one or more observed descendants
 * (e.g. <a href="https://hpo.jax.org/app/browse/term/HP:0001166">Arachnodactyly</a>),
 * or excluded top-level HPO term
 * (<em>NOT</em> <a href="https://hpo.jax.org/app/browse/term/HP:0040064">Abnormality of limbs</a>).
 */
public abstract class AbstractOrganSystemValidator<T extends MessageOrBuilder> extends BaseHpoValidator<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractOrganSystemValidator.class);

    private static final ValidatorInfo VALIDATOR_INFO = ValidatorInfo.of(
            "HpoOrganSystemValidator",
            "HPO organ system validator",
            "Validate annotation of selected organ systems");

    private static final String MISSING_ORGAN_SYSTEM_CATEGORY = "Missing organ system annotation";

    protected final List<TermId> organSystemTermIds;

    protected AbstractOrganSystemValidator(Ontology hpo,
                                           Collection<TermId> organSystemTermIds) {
        super(hpo);
        this.organSystemTermIds = Objects.requireNonNull(organSystemTermIds).stream()
                .distinct()
                .filter(organSystemTermIdIsInOntology(hpo))
                .sorted()
                .toList();
    }

    private static Predicate<TermId> organSystemTermIdIsInOntology(Ontology hpo) {
        return organSystemTermId -> {
            if (hpo.containsTerm(organSystemTermId)) {
                return true;
            } else {
                LOGGER.warn("{} is not present in the ontology", organSystemTermId.getValue());
                return false;
            }
        };
    }

    @Override
    public ValidatorInfo validatorInfo() {
        return VALIDATOR_INFO;
    }

    @Override
    public List<ValidationResult> validate(T component) {
        return getPhenopackets(component)
                .flatMap(p -> checkPhenotypicFeatures(p.getSubject().getId(), p.getPhenotypicFeaturesList()))
                .toList();
    }

    protected abstract Stream<? extends PhenopacketOrBuilder> getPhenopackets(T component);

    private Stream<ValidationResult> checkPhenotypicFeatures(String individualId, List<PhenotypicFeature> features) {
        PhenotypicFeaturesByExclusionStatus featuresByExclusion = Util.partitionByExclusionStatus(features);

        Stream.Builder<ValidationResult> results = Stream.builder();
        // Check we have at least one phenotypeFeature (pf) that is a descendant of given organSystemId
        // and report otherwise.
        organSystemLoop:
        for (TermId organSystemId : organSystemTermIds) {
            // Check if the organ system abnormality has been specifically excluded.
            if (featuresByExclusion.excludedPhenotypicFeatures().contains(organSystemId))
                continue; // Yes, it was. Let's check the next organ system

            // Check if we have at least one observed annotation for the organ system.
            for (TermId pf : featuresByExclusion.observedPhenotypicFeatures()) {
                if (OntologyAlgorithm.existsPath(hpo, pf, organSystemId)) {
                    continue organSystemLoop; // It only takes one termId to annotate an organ system.
                }
            }

            // The organSystemId is neither annotated nor excluded. We report a validation error.
            Term organSystem = hpo.getTermMap().get(organSystemId);
            ValidationResult result = ValidationResult.error(VALIDATOR_INFO,
                    MISSING_ORGAN_SYSTEM_CATEGORY,
                    "Missing annotation for %s [%s] in '%s'"
                            .formatted(organSystem.getName(), organSystem.id().getValue(), individualId));
            results.add(result);
        }

        return results.build();
    }

    /**
     * @return a function that maps {@link OntologyClass} into a {@link TermId} and emit warning otherwise.
     */
    private static Function<OntologyClass, Optional<TermId>> toTermId(String individualId) {
        return oc -> {
            try {
                return Optional.of(TermId.of(oc.getId()));
            } catch (PhenolRuntimeException e) {
                LOGGER.warn("Invalid term ID {} in individual {}", oc.getId(), individualId);
                return Optional.empty();
            }
        };
    }
}

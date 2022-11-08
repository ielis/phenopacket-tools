/**
 * The module provides a {@link org.phenopackets.phenopackettools.validator.core.ValidationWorkflowRunner} implementation
 * backed by a JSON schema validator.
 *
 * @see org.phenopackets.phenopackettools.validator.jsonschema.JsonSchemaValidationWorkflowRunner
 */
module org.phenopackets.phenopackettools.validator.jsonschema {
    requires org.phenopackets.phenopackettools.util;
    requires transitive org.phenopackets.phenopackettools.validator.core;
    requires org.phenopackets.schema;
    requires com.google.protobuf.util;
    requires com.fasterxml.jackson.databind;
    requires json.schema.validator;
    requires org.slf4j;

    exports org.phenopackets.phenopackettools.validator.jsonschema;

    opens org.phenopackets.phenopackettools.validator.jsonschema;
    opens org.phenopackets.phenopackettools.validator.jsonschema.v2;
}
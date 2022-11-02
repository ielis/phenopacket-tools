package org.phenopackets.phenopackettools.builder.constants;

import org.phenopackets.phenopackettools.builder.builders.OntologyClassBuilder;
import org.phenopackets.schema.v2.core.OntologyClass;

public class AdministrationRoute {

  private static final OntologyClass INTRAVENOUS_ROUTE = OntologyClassBuilder.ontologyClass("NCIT:C38276", "Intravenous Route of Administration");
  private static final OntologyClass INTRAARTERIAL_ROUTE = OntologyClassBuilder.ontologyClass("NCIT:C38222", "Intraarterial Route of Administration");
  private static final OntologyClass WOUND_IRRIGATION_ROUTE = OntologyClassBuilder.ontologyClass("NCIT:C183503", "Administration via Wound Irrigation");
  private static final OntologyClass NEBULIZER_ROUTE = OntologyClassBuilder.ontologyClass("NCIT:C149695", "Nebulizer Route of Administration");
  private static final OntologyClass ORAL_ROUTE = OntologyClassBuilder.ontologyClass("NCIT:C38288", "Oral Route of Administration");
  private static final OntologyClass INTRATHECAL_ROUTE = OntologyClassBuilder.ontologyClass("NCIT:C38267", "Intrathecal Route of Administration");
  private static final OntologyClass PERIDURAL_ROUTE = OntologyClassBuilder.ontologyClass("NCIT:C38677", "Peridural Route of Administration");
  private static final OntologyClass TOPICAL_ROUTE = OntologyClassBuilder.ontologyClass("NCIT:C38304", "Topical Route of Administration");
  private static final OntologyClass TRANSDERMAL = OntologyClassBuilder.ontologyClass("NCIT:C38305", "Transdermal Route of Administration");


  public static OntologyClass intravenous() { return INTRAVENOUS_ROUTE; }
  public static OntologyClass intraarterial() { return INTRAARTERIAL_ROUTE; }
  public static OntologyClass woundIrrigation() { return WOUND_IRRIGATION_ROUTE; }
  public static OntologyClass nebulizer() { return NEBULIZER_ROUTE; }
  public static OntologyClass oral() { return ORAL_ROUTE; }
  public static OntologyClass intrathecal() { return INTRATHECAL_ROUTE; }
  public static OntologyClass peridural() { return PERIDURAL_ROUTE; }
  public static OntologyClass topical() { return TOPICAL_ROUTE; }
  public static OntologyClass transdermal() { return TRANSDERMAL; }

}

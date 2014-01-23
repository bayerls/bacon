package de.uop.code.cubemerging.vocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;

public class PROV {

    private static final String PREFIX = "prov";
    private static final String URI = "http://www.w3.org/ns/prov#";


    public static String getPrefix() {
        return PREFIX;
    }

    public static String getURI() {
        return URI;
    }

    private static final Model m = ModelFactory.createDefaultModel();

    public static final Property WAS_DERIVED_FROM = m.createProperty(URI + "wasDerivedFrom");
    public static final Property WAS_GENERATED_BY = m.createProperty(URI + "wasGeneratedBy");
    public static final Property WAS_STARTED_BY = m.createProperty(URI + "wasStartedBy");

}

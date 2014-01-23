package de.uop.code.cubemerging.dao;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import de.uop.code.cubemerging.Observation;
import de.uop.code.cubemerging.domain.DatasetDescription;
import de.uop.code.cubemerging.domain.EntityDefinition;
import de.uop.code.cubemerging.domain.cube.Cube;
import de.uop.code.cubemerging.domain.cube.DatasetStructureDefinition;
import de.uop.code.cubemerging.domain.cube.Dimension;
import de.uop.code.cubemerging.domain.cube.Measure;
import de.uop.code.cubemerging.util.Properties;
import de.uop.code.cubemerging.vocabulary.QB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
public class MergeDao {

    private final static String TRIPLE_STORE = Properties.getInstance().getTripleStore();
    private final static String PREFIXES = "PREFIX qb: <http://purl.org/linked-data/cube#> PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX code: <http://code-research.eu/resource/> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX prov: <http://www.w3.org/ns/prov#> ";
    private final static String GRAPH_QUERY =  PREFIXES + "SELECT DISTINCT ?g WHERE { GRAPH ?g { ?s rdf:type qb:DataSet }}";
    private final static String DATASET_QUERY = PREFIXES + "SELECT DISTINCT ?s WHERE { GRAPH ?g { ?s rdf:type qb:DataSet }}";
    private final static String GENERIC_CONSTRUCT = PREFIXES + "CONSTRUCT { ?s ?p ?o } WHERE { GRAPH ?g { ?s ?p ?o }}";
    private final static String COMPONENTS_QUERY = PREFIXES + "SELECT ?component ?concept WHERE { GRAPH ?g { ?d rdf:type qb:DataSet ; qb:structure ?dsd . ?dsd rdf:type qb:DataStructureDefinition ; qb:component ?comp . ?comp ?component ?concept }}";
    private final static String OBSERVATION_QUERY = PREFIXES + "SELECT ?s ?p ?o WHERE { GRAPH ?g { ?s rdf:type qb:Observation . ?s ?p ?o }}";
    private final static String DIMENSION_PROPERTY_QUERY = PREFIXES + "CONSTRUCT {?s ?p ?o} WHERE { GRAPH ?g { ?s rdf:type qb:DimensionProperty . ?s ?p ?o }}";
    private final static String MEASURE_PROPERTY_QUERY = PREFIXES + "CONSTRUCT {?s ?p ?o} WHERE { GRAPH ?g { ?s rdf:type qb:MeasureProperty . ?s ?p ?o }}";
    private final static String ENTITY_QUERY = PREFIXES + "SELECT ?s WHERE { GRAPH ?g { ?s rdf:type code:Entity }}";
    private final static String ENTITY_LABEL_QUERY = PREFIXES + "SELECT ?s ?d ?l WHERE { GRAPH ?g { ?s rdf:type code:Entity . ?s rdfs:label ?l . ?s rdfs:isDefinedBy ?d }}";
    private final static String ENTITY_CONSTRUCT_QUERY = PREFIXES + "CONSTRUCT {?s ?p ?o} WHERE { GRAPH ?g { ?s rdf:type code:Entity . ?s ?p ?o }}";
    private final static String GRAPH_LABEL = PREFIXES + "SELECT ?label WHERE { GRAPH ?g { ?s rdf:type qb:DataSet . ?s rdfs:label ?label }}";
    private final static String QUERY_DATASET_DESCRIPTIONS = PREFIXES + "SELECT DISTINCT ?g ?id ?label ?description WHERE { GRAPH ?g { ?id rdf:type qb:DataSet . ?id prov:wasGeneratedBy ?activity . ?activity prov:wasStartedBy ?agent . ?agent rdfs:label ?auth . ?id rdfs:label ?label . ?id rdfs:comment ?description }}";
    private final static String QUERY_DATASET_DESCRIPTION = PREFIXES + "SELECT DISTINCT ?id ?label ?description WHERE { GRAPH ?g { ?id rdf:type qb:DataSet . ?id prov:wasGeneratedBy ?activity . ?activity prov:wasStartedBy ?agent . ?agent rdfs:label ?auth . ?id rdfs:label ?label . ?id rdfs:comment ?description }}";
    private final static String COMPONENT_LABEL = PREFIXES + "SELECT ?label WHERE { GRAPH ?g {?s rdfs:label ?label}}";
    private final static String COMPONENT_RANGE = PREFIXES + "SELECT ?range WHERE { GRAPH ?g {?s rdfs:subPropertyOf ?range}}";

    // queries for the cube info
    private final static String QUERY_CUBE_INFO = PREFIXES + "SELECT DISTINCT ?id ?label ?description WHERE { GRAPH ?g { ?id rdf:type qb:DataSet . ?id prov:wasGeneratedBy ?activity . ?activity prov:wasStartedBy ?agent . ?agent rdfs:label ?auth . ?id rdfs:label ?label . ?id rdfs:comment ?description }}";
    private final static String QUERY_DIMENSION_INFO = PREFIXES + "SELECT ?id ?label ?subproperty WHERE { GRAPH ?g { ?id rdf:type qb:DimensionProperty . ?id rdfs:label ?label . ?id rdfs:subPropertyOf ?subproperty}}";
    private final static String QUERY_MEASURE_INFO = PREFIXES + "SELECT ?id ?label ?subproperty WHERE { GRAPH ?g { ?id rdf:type qb:MeasureProperty . ?id rdfs:label ?label . ?id rdfs:subPropertyOf ?subproperty}}";


    private final Logger logger = LoggerFactory.getLogger(MergeDao.class);


    public Cube getCubeInformation(String graph, String userId) {
        Cube cube = addBasicInfo(graph, userId);
        DatasetStructureDefinition datasetStructureDefinition = new DatasetStructureDefinition();
        datasetStructureDefinition.setDimensions(getDimensionsInfo(graph));
        datasetStructureDefinition.setMeasures(getMeasuresInfo(graph));
        cube.setDatasetStructureDefinition(datasetStructureDefinition);

        return cube;
    }



    private Cube addBasicInfo(String graph, String userId) {
        ParameterizedSparqlString prepareQuery = new ParameterizedSparqlString(QUERY_CUBE_INFO);
        prepareQuery.setIri("g", graph);
        prepareQuery.setLiteral("auth", userId, XSDDatatype.XSDstring);

        QueryEngineHTTP qeHTTP = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(TRIPLE_STORE, prepareQuery.toString());
        ResultSet rs = qeHTTP.execSelect();
        Cube cube = new Cube();

        if (rs.hasNext()) {
            QuerySolution result = rs.next();
            cube.setGraph(graph);
            cube.setAuth(userId);
            cube.setLabel(result.get("label").toString());
            cube.setId(result.get("id").toString());
            cube.setDescription(result.get("description").toString());
        }

        qeHTTP.close();

        return cube;
    }


    private List<Dimension> getDimensionsInfo(String graph) {
        ParameterizedSparqlString prepareQuery = new ParameterizedSparqlString(QUERY_DIMENSION_INFO);
        prepareQuery.setIri("g", graph);

        QueryEngineHTTP qeHTTP = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(TRIPLE_STORE, prepareQuery.toString());
        ResultSet rs = qeHTTP.execSelect();

        List<Dimension> dims = new LinkedList<Dimension>();

        while (rs.hasNext()) {
            QuerySolution result = rs.next();

            Dimension dim = new Dimension();
            dim.setLabel(result.get("label").toString());
            dim.setSubpropertyOf(result.get("subproperty").toString());
            dim.setUrl(result.get("id").toString());

            dims.add(dim);
        }

        qeHTTP.close();

        return dims;
    }

    private List<Measure> getMeasuresInfo(String graph) {
        ParameterizedSparqlString prepareQuery = new ParameterizedSparqlString(QUERY_MEASURE_INFO);
        prepareQuery.setIri("g", graph);

        QueryEngineHTTP qeHTTP = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(TRIPLE_STORE, prepareQuery.toString());
        ResultSet rs = qeHTTP.execSelect();

        List<Measure> measures = new LinkedList<Measure>();

        while (rs.hasNext()) {
            QuerySolution result = rs.next();

            Measure measure = new Measure();
            measure.setLabel(result.get("label").toString());
            measure.setSubpropertyOf(result.get("subproperty").toString());
            measure.setUrl(result.get("id").toString());

            measures.add(measure);
        }

        qeHTTP.close();

        return measures;
    }

    public String getLabelForResource(String graph, String resource) {

        ParameterizedSparqlString prepareQuery = new ParameterizedSparqlString(COMPONENT_LABEL);
        prepareQuery.setIri("g", graph);
        prepareQuery.setIri("s", resource);

        QueryEngineHTTP qeHTTP = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(TRIPLE_STORE, prepareQuery.toString());
        ResultSet rs = qeHTTP.execSelect();

        String label = "";

        if (rs.hasNext()) {
            QuerySolution result = rs.next();
            label = result.get("label").toString();
        }

        qeHTTP.close();

        return label;
    }

    public String getRangeForResource(String graph, String resource) {

        ParameterizedSparqlString prepareQuery = new ParameterizedSparqlString(COMPONENT_RANGE);
        prepareQuery.setIri("g", graph);
        prepareQuery.setIri("s", resource);

        QueryEngineHTTP qeHTTP = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(TRIPLE_STORE, prepareQuery.toString());
        ResultSet rs = qeHTTP.execSelect();

        String label = "";

        if (rs.hasNext()) {
            QuerySolution result = rs.next();
            label = result.get("range").toString();
        }

        qeHTTP.close();

        return label;
    }

    public DatasetDescription getDatasetFromUser(String graph, String userId) {
        ParameterizedSparqlString prepareQuery = new ParameterizedSparqlString(QUERY_DATASET_DESCRIPTION);
        prepareQuery.setIri("g", graph);
        prepareQuery.setLiteral("auth", userId, XSDDatatype.XSDstring);

        QueryEngineHTTP qeHTTP = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(TRIPLE_STORE, prepareQuery.toString());
        ResultSet rs = qeHTTP.execSelect();

        DatasetDescription dd = new DatasetDescription();

        if (rs.hasNext()) {
            QuerySolution result = rs.next();

            dd.setNamedGraph(graph);
            dd.setDatasetId(result.get("id").toString());
            dd.setLabel(result.get("label").toString());
            dd.setDescription(result.get("description").toString());
        }

        qeHTTP.close();

        return dd;
    }


    public List<DatasetDescription> getDatasetsFromUser(String userId) {
        ParameterizedSparqlString prepareQuery = new ParameterizedSparqlString(QUERY_DATASET_DESCRIPTIONS);
        prepareQuery.setLiteral("auth", userId, XSDDatatype.XSDstring);

        QueryEngineHTTP qeHTTP = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(TRIPLE_STORE, prepareQuery.toString());
        ResultSet rs = qeHTTP.execSelect();
        List<DatasetDescription> descriptions = new LinkedList<DatasetDescription>();

        while (rs.hasNext()) {
            QuerySolution result = rs.next();
            DatasetDescription dd = new DatasetDescription();
            dd.setNamedGraph(result.get("g").toString());
            dd.setDatasetId(result.get("id").toString());
            dd.setLabel(result.get("label").toString());
            dd.setDescription(result.get("description").toString());

            descriptions.add(dd);
        }

        qeHTTP.close();

        return descriptions;
    }


    public String getLabel(String graph) {
        logger.debug("getLabel: " + graph);

        ParameterizedSparqlString prepareQuery = new ParameterizedSparqlString(GRAPH_LABEL);
        prepareQuery.setIri("g", graph);

        QueryEngineHTTP qeHTTP = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(TRIPLE_STORE, prepareQuery.toString());
        ResultSet rs = qeHTTP.execSelect();

        List<String> labels = new LinkedList<String>();

        while (rs.hasNext()) {
            QuerySolution result = rs.next();
            labels.add(result.get("label").toString());
        }

        qeHTTP.close();

        if (labels == null || labels.size() != 1) {
            logger.warn("label size != 1");
        }

        return labels.get(0);
    }

    public Model getAllMeasures(String graph) {
        return getMeasure(graph, null);
    }

    public Model getAllEntityDefinitions(String graph) {
        return getEntityDefinition(graph, null);
    }

    public Model getMeasure(String graph, String subject) {
        logger.debug("getMeaure: " + graph + " " + subject);
        ParameterizedSparqlString prepareQuery = new ParameterizedSparqlString(MEASURE_PROPERTY_QUERY);
        prepareQuery.setIri("g", graph);

        if (subject != null) {
            prepareQuery.setIri("s", subject);
        }

        Model model = null;
        try {
            QueryEngineHTTP qeHTTP = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(TRIPLE_STORE, prepareQuery.toString());
            model = qeHTTP.execConstruct();
        } catch (QueryParseException exception) {
            logger.error("QueryParseException: ", exception);
        }

        return model;
    }


    public Model getDimension(String graph, String subject) {
        logger.debug("getDimensions: " + graph);
        ParameterizedSparqlString prepareQuery = new ParameterizedSparqlString(DIMENSION_PROPERTY_QUERY);
        prepareQuery.setIri("g", graph);
        prepareQuery.setIri("s", subject);

        Model model = null;
        try {
            QueryEngineHTTP qeHTTP = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(TRIPLE_STORE, prepareQuery.toString());
            model = qeHTTP.execConstruct();
        } catch (QueryParseException exception) {
            logger.error("QueryParseException: ", exception);
        }

        return model;
    }

    public Model getDimensions(String graph) {
        logger.debug("getDimensions: " + graph);
        ParameterizedSparqlString prepareQuery = new ParameterizedSparqlString(DIMENSION_PROPERTY_QUERY);
        prepareQuery.setIri("g", graph);

        Model model = null;
        try {
            QueryEngineHTTP qeHTTP = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(TRIPLE_STORE, prepareQuery.toString());
            model = qeHTTP.execConstruct();
        } catch (QueryParseException exception) {
            logger.error("QueryParseException: ", exception);
        }

        return model;
    }

    public List<EntityDefinition> getEntityDefs(String graph) {
        logger.debug("getEntityDefs: " + graph);
        ParameterizedSparqlString prepareQuery = new ParameterizedSparqlString(ENTITY_LABEL_QUERY);
        prepareQuery.setIri("g", graph);

        QueryEngineHTTP qeHTTP = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(TRIPLE_STORE, prepareQuery.toString());
        ResultSet rs = qeHTTP.execSelect();

        List<EntityDefinition> entities = new LinkedList<EntityDefinition>();

        while (rs.hasNext()) {
            QuerySolution result = rs.next();

            EntityDefinition ed = new EntityDefinition();
            ed.setResource(result.get("s").toString());
            ed.setLabel(result.getLiteral("l").getString());
            ed.setDefinedBy(result.get("d").toString());

            entities.add(ed);
        }

        qeHTTP.close();

        return entities;
    }

    public Model getEntityDefinition(String graph, String entity) {
        logger.debug("getEntityDefinition: " + graph + " " + entity);
        ParameterizedSparqlString prepareQuery = new ParameterizedSparqlString(ENTITY_CONSTRUCT_QUERY);
        prepareQuery.setIri("g", graph);

        if (entity != null) {
            prepareQuery.setIri("s", entity);
        }

        Model model = null;
        try {
            QueryEngineHTTP qeHTTP = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(TRIPLE_STORE, prepareQuery.toString());
            model = qeHTTP.execConstruct();
        } catch (QueryParseException exception) {
            logger.error("QueryParseException: ", exception);
        }

        return model;
    }

    public List<String> getEntityDefinitions(String graph) {
        logger.debug("getEntityDefinitions: " + graph);
        ParameterizedSparqlString prepareQuery = new ParameterizedSparqlString(ENTITY_QUERY);
        prepareQuery.setIri("g", graph);

        QueryEngineHTTP qeHTTP = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(TRIPLE_STORE, prepareQuery.toString());
        ResultSet rs = qeHTTP.execSelect();

        List<String> entities = new LinkedList<String>();

        while(rs.hasNext()) {
            QuerySolution result = rs.next();
            entities.add(result.get("s").toString());
        }

        qeHTTP.close();

        return entities;
    }

    public List<Observation> getObservations(String graph) {
        logger.debug("getObservations: " + graph);
        ParameterizedSparqlString prepareQuery = new ParameterizedSparqlString(OBSERVATION_QUERY);
        prepareQuery.setIri("g", graph);

        QueryEngineHTTP qeHTTP = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(TRIPLE_STORE, prepareQuery.toString());
        ResultSet rs = qeHTTP.execSelect();
        Map<String, String> components = getComponents(graph);
        Map<String, Observation> observations = new HashMap<String, Observation>();

        while(rs.hasNext()) {
            QuerySolution result = rs.next();
            String obs = result.get("s").toString();
            String predicate = result.get("p").toString();
            String object = result.get("o").toString();

            if (!observations.containsKey(obs)) {
                observations.put(obs, new Observation());
            }

            Observation o = observations.get(obs);

            if (!object.equals(QB.OBSERVATION.getURI())) {
                if (predicate.equals(QB.DATASET_PROPERTY.getURI())) {
                    o.setDataset(object);
                    o.setResource(obs);
                } else {
                    if (components.get(predicate).equals(QB.MEASURE.getURI())) {
                        o.getMeasures().put(predicate, object);
                    } else if (components.get(predicate).equals(QB.DIMENSION.getURI())) {
                        o.getDimensions().put(predicate, object);
                    } else {
                        throw new IllegalStateException("unknown component: " + components.get(predicate));
                    }
                }
            }
        }

        qeHTTP.close();

        List<Observation> result = new LinkedList<Observation>();
        result.addAll(observations.values());

        return result;
    }

    public Model getCompleteGraph(String graph) {
        logger.debug("getCompleteGraph: " + graph);
        ParameterizedSparqlString prepareQuery = new ParameterizedSparqlString(GENERIC_CONSTRUCT);
        prepareQuery.setIri("g", graph);

        QueryEngineHTTP qeHTTP = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(TRIPLE_STORE, prepareQuery.toString());

        return qeHTTP.execConstruct();
    }

    /**
     *
     * @param graph The current named graph
     * @return Key: concept; Value: component
     */
    public Map<String, String> getComponents(String graph) {
        logger.debug("getComponents: " + graph);
        ParameterizedSparqlString prepareQuery = new ParameterizedSparqlString(COMPONENTS_QUERY);
        prepareQuery.setIri("g", graph);

        QueryEngineHTTP qeHTTP = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(TRIPLE_STORE, prepareQuery.toString());
        ResultSet rs = qeHTTP.execSelect();

        Map<String, String> components = new HashMap<String, String>();

        while(rs.hasNext()) {
            QuerySolution result = rs.next();
            components.put(result.get("concept").toString(), result.get("component").toString());
        }

        qeHTTP.close();

        return components;
    }

    /**
     *
     * @param graph The current named graph
     * @return Null if there is not exactly one dataset.
     */
    public String getDataset(String graph) {
        logger.debug("getDataset: " + graph);
        ParameterizedSparqlString prepareQuery = new ParameterizedSparqlString(DATASET_QUERY);
        prepareQuery.setIri("g", graph);

        logger.debug(prepareQuery.toString());

        QueryEngineHTTP qeHTTP = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(TRIPLE_STORE, prepareQuery.toString());
        ResultSet rs = qeHTTP.execSelect();

        List<String> datasets = new LinkedList<String>();

        while(rs.hasNext()) {
            QuerySolution result = rs.next();
            datasets.add(result.get("s").toString());
        }

        String dataset = null;

        if (datasets.size() > 1) {
            logger.warn("There is more than one dataset in this graph. Dismiss potentially invalid graph. " + graph);
        } else {
            dataset = datasets.get(0);
        }

        qeHTTP.close();

        return dataset;
    }

    public List<String> getGraphs() {
        logger.debug("getGraphs");
        QueryEngineHTTP qeHTTP = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(TRIPLE_STORE, GRAPH_QUERY);
        ResultSet rs = qeHTTP.execSelect();

        List<String> graphs = new LinkedList<String>();

        while(rs.hasNext()) {
            QuerySolution result = rs.next();
            graphs.add(result.get("g").toString());
        }

        qeHTTP.close();

        return graphs;
    }

}

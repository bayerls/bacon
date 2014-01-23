package de.uop.code.cubemerging.service;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import de.uop.code.cubemerging.Observation;
import de.uop.code.cubemerging.StructureDefinition;
import de.uop.code.cubemerging.dao.MergeDao;
import de.uop.code.cubemerging.domain.*;
import de.uop.code.cubemerging.domain.cube.Cube;
import de.uop.code.cubemerging.vocabulary.CODE;
import de.uop.code.cubemerging.vocabulary.PROV;
import de.uop.code.cubemerging.vocabulary.QB;
import de.uop.code.cubemerging.vocabulary.VA;
import org.openjena.riot.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.*;

@Service
public class MergeService {

    private final Logger logger = LoggerFactory.getLogger(MergeService.class);

    private final static String CONTEXT_REFIX = "http://code-research.eu/cubes/";
    private final static String VERSION = "codeCube/1.1";
    private final static String RELATION = "Cube was merged with the CODE CubeMerger";
    private final static String SOURCE = "http://zaire.dimis.fim.uni-passau.de:8181/cube-merging/select";
    private final static String ENITIY_DEFINED_PREFIX = "http://code-research.eu/resource/";

    @Autowired
    private MergeDao mergeDao;

    @Autowired
    private MergeStructureService mergeStructureService;

    private Model mergedDataset;
    private MergedDSD mergedDSD;
    private MergeLog log;
    private String auth;

    public String getAuth() {
        return auth;
    }

    public Cube getCubeInfo(String graph, String auth) {
           return mergeDao.getCubeInformation(graph, auth);
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public MergeLog getLog() {
        return log;
    }

    public Model createGenericMergedDataset(String label, String description) {
        log = new MergeLog();
        log.setDims(mergedDSD.getMatchingDimensions().size());
        log.setMeas(mergedDSD.getMatchingMeasures().size());

        mergedDataset = ModelFactory.createDefaultModel();
        mergedDataset = setNamespaces(mergedDataset);

        if (label == null) {
            logger.debug("label == null");
            label = "unknown";
        }

        if (description == null) {
            logger.debug("description == null");
            description = "unknown";
        }

        if (auth == null) {
            logger.debug("auth == null");
            auth = "public";
        }

        // generate new dataset resource
        Resource ds = createDataset(label, description);

        // generate wasCreateBy information
        createDerivedFrom(ds, mergeDao.getDataset(mergedDSD.getG1()), mergeDao.getDataset(mergedDSD.getG2()));

        // create provenance information
        addProvenanceInformation(ds, auth);

        // generate structure
        Resource dsd = createDatasetStructureDefintion(ds);

        // add dimensions and measures to structure
        addComponents(mergedDSD, dsd);

        EntityDefinitionWrapper edw = updateEntityDefinitions(mergedDSD);

        // add the merged overlapping observations and all others from both sets
        mergedDataset.add(createObservations(mergedDSD, ds, edw));

//        System.out.println(convertModelToString(mergedDataset));

        return mergedDataset;
    }


    private EntityDefinitionWrapper updateEntityDefinitions(MergedDSD mergedDSD) {

        List<EntityDefinition> entityDefinitions = new LinkedList<EntityDefinition>();
        Map<String, String> renameEntity = new HashMap<String, String>();

        // generate new ones for added dimensions
        for (MatchingComponent mc : mergedDSD.getMatchingDimensions()) {
            if (mc.getMatchingType() == MatchingType.MANUAL) {

                if (mc.getComponent1().getValue() != null) {
                    String url1 = ENITIY_DEFINED_PREFIX + mc.getComponent1().getValue();
                    EntityDefinition ed1 = new EntityDefinition();
                    ed1.setResource(CODE.ENTITY + "-" + getRandomId());
                    ed1.setLabel(mc.getComponent1().getValue());
                    ed1.setDefinedBy(url1);
                    entityDefinitions.add(ed1);
                    mc.getComponent1().setValue(ed1.getResource());
                }

                if (mc.getComponent2().getValue() != null) {
                    String url2 = ENITIY_DEFINED_PREFIX + mc.getComponent2().getValue();
                    EntityDefinition ed2 = new EntityDefinition();
                    ed2.setResource(CODE.ENTITY + "-" + getRandomId());
                    ed2.setLabel(mc.getComponent2().getValue());
                    ed2.setDefinedBy(url2);
                    entityDefinitions.add(ed2);
                    mc.getComponent2().setValue(ed2.getResource());
                }
            }
        }

        // get all from the second cube
        List<EntityDefinition> eds2 = mergeDao.getEntityDefs(mergedDSD.getG2());
        entityDefinitions.addAll(eds2);

        // get additional from the first cube
        // put overlapping entities into rename list
        List<EntityDefinition> eds1 = mergeDao.getEntityDefs(mergedDSD.getG1());

        for (EntityDefinition ed1 : eds1) {
            boolean renamed = false;
            // check if entity must be renamed
            for (EntityDefinition current : entityDefinitions) {
                if (ed1.getDefinedBy().equals(current.getDefinedBy())) {
                    renamed = true;
                    renameEntity.put(ed1.getResource(), current.getResource());
                }
            }

            if (!renamed) {
                entityDefinitions.add(ed1);
            }
        }

        for (EntityDefinition ed : entityDefinitions) {
            Resource res = mergedDataset.createResource(ed.getResource());
            res.addLiteral(RDFS.label, ed.getLabel());
            res.addProperty(RDFS.isDefinedBy, mergedDataset.createResource(ed.getDefinedBy()));
            res.addProperty(RDF.type, CODE.ENTITY);
        }

        EntityDefinitionWrapper edw = new EntityDefinitionWrapper();
        edw.setEntityDefinitionList(entityDefinitions);
        edw.setRenameMap(renameEntity);

        return edw;
    }

    private void deleteObservationComponents(List<Observation> observations, List<Component> components, String type) {
        for (Observation ob : observations) {
            for (Component comp : components) {
                if (type.equals("dimension")) {
                    ob.getDimensions().remove(comp.getResource());
                } else if (type.equals("measure")) {
                    ob.getMeasures().remove(comp.getResource());
                } else {
                    throw new IllegalStateException("unknown type: " + type);
                }
            }
        }
    }

    private void addObservationComponents(MergedDSD mergedDSD, List<Observation> observations, boolean first) {

        for (Observation obs : observations) {
            for (MatchingComponent mc : mergedDSD.getMatchingDimensions()) {
                if (mc.getMatchingType() == MatchingType.MANUAL) {
                    Component comp;
                    if (first) {
                        comp = mc.getComponent1();
                    } else {
                        comp = mc.getComponent2();
                    }

                    if (comp.getValue() != null) {
                        obs.getDimensions().put(mc.getResource(), comp.getValue());
                    }
                }
            }

            for (MatchingComponent mc : mergedDSD.getMatchingMeasures()) {
                if (mc.getMatchingType() == MatchingType.MANUAL) {
                    Component comp;
                    if (first) {
                        comp = mc.getComponent1();
                    } else {
                        comp = mc.getComponent2();
                    }

                    if (comp.getValue() != null) {
                        obs.getMeasures().put(mc.getResource(), comp.getValue());
                    }
                }
            }
        }
    }


    private List<Observation> getMergedObservations(MergedDSD mergedDSD, EntityDefinitionWrapper edw) {
        List<Observation> observations1 = mergeDao.getObservations(mergedDSD.getG1());
        List<Observation> observations2 = mergeDao.getObservations(mergedDSD.getG2());

        // remove components if needed
        deleteObservationComponents(observations1, mergedDSD.getDimensions1(), "dimension");
        deleteObservationComponents(observations1, mergedDSD.getMeasures1(), "measure");

        deleteObservationComponents(observations2, mergedDSD.getDimensions2(), "dimension");
        deleteObservationComponents(observations2, mergedDSD.getMeasures2(), "measure");

        // add components if needed
        addObservationComponents(mergedDSD, observations1, true);
        addObservationComponents(mergedDSD, observations2, false);

        List<Observation> result = new LinkedList<Observation>();

        Set<String> mergedObservation = new HashSet<String>();

        // update entities
        Map<String, String> updateMap = new HashMap<String, String>();

        for (Observation ob : observations1) {
            for (String dimension : ob.getDimensions().keySet()) {
               if (edw.getRenameMap().containsKey(ob.getDimensions().get(dimension))) {
                   updateMap.put(dimension, edw.getRenameMap().get(ob.getDimensions().get(dimension)));
               }
            }

            for (String key : updateMap.keySet()) {
                ob.getDimensions().put(key, updateMap.get(key));
            }

            updateMap.clear();
        }

        if (log.getDims() != 0) {
            log.setObsC1(observations1.size());
            log.setObsC2(observations2.size());

            for (Observation o1 : observations1) {
                boolean mergeOccured = false;

                for (Observation o2 : observations2) {
                    // check for overlapping observations
                    // none overlapping can be added without modification
                    // when equal observations are found merge them and put into result
                    // every other observations belongs into the result
                    // remember the oberlapping resources from the second set. all others belong to the result set
                    if (checkEqualObservations(o1, o2)) {
                        result.add(mergeObservations(o1, o2));
                        mergedObservation.add(o2.getResource());
                        mergeOccured = true;
                        log.setObsMerged(log.getObsMerged() + 1);
                    }
                }

                if (!mergeOccured) {
                    result.add(o1);
                }
            }
        }

        for (Observation o2 : observations2) {
            if (!mergedObservation.contains(o2.getResource())) {
                result.add(o2);
            }
        }

        return result;
    }

    private boolean checkEqualObservations(Observation o1, Observation o2) {
        Map<String, String> dimensions1 = o1.getDimensions();
        Map<String, String> dimensions2 = o2.getDimensions();

        // try to find an equal observation (same dimension values)
        for (String key : dimensions1.keySet()) {
            // if the values vary, observations can not be equal
            if (!dimensions1.get(key).equals(dimensions2.get(key))) {
                return false;
            }
        }

        return true;
    }

    private Observation mergeObservations(Observation o1, Observation o2) {
        Map<String, String> measures1 = o1.getMeasures();
        Map<String, String> measures2 = o2.getMeasures();

        // add measures from the first to the second observation
        for (String key : measures1.keySet()) {
            if (!measures2.containsKey(key)) {
                measures2.put(key, measures1.get(key));
            }
        }

        return o2;
    }

    private Model createObservations(MergedDSD mergedDSD, Resource dataset, EntityDefinitionWrapper edw) {
        List<Observation> obs = getMergedObservations(mergedDSD, edw);
        Model model = ModelFactory.createDefaultModel();
        int obsCounter = 0;

        for (Observation o : obs) {
            obsCounter++;
            Resource resource = model.createResource(CODE.OBS + obsCounter);

            for (String key : o.getDimensions().keySet()) {
                resource.addProperty(model.createProperty(key), model.createResource(o.getDimensions().get(key)));
            }

            for (String key : o.getMeasures().keySet()) {
                resource.addLiteral(model.createProperty(key), o.getMeasures().get(key));
            }

            // create dataset link and type definition
            resource.addProperty(RDF.type, QB.OBSERVATION);
            resource.addProperty(QB.DATASET_PROPERTY, dataset);
        }

        return model;
    }

    private void addComponents(MergedDSD mergedDSD, Resource dsd) {

        // Add dimension components
        for (MatchingComponent mc :  mergedDSD.getMatchingDimensions()) {
            String resource;
            String label;
            String range;

            if (mc.getMatchingType() == MatchingType.MANUAL) {
                resource = mc.getResource();
                label = mc.getLabel() ;
                range = mc.getRange();
            } else {
                Component comp2 = mc.getComponent2();
                resource = comp2.getResource();
                label =  comp2.getLabel();
                range = comp2.getRange();
            }

            Property p = mergedDataset.createProperty(resource);
            p.addProperty(RDF.type, RDF.Property);
            p.addProperty(RDF.type, QB.DIM_PROPERTY);
            p.addProperty(RDFS.label, label);
            p.addProperty(RDFS.subPropertyOf, mergedDataset.createProperty(range));

            dsd.addProperty(QB.COMPONENT, mergedDataset.createResource().addProperty(QB.DIMENSION, p));
        }

        // Add measure components
        for (MatchingComponent mc :  mergedDSD.getMatchingMeasures()) {
            String resource;
            String label;
            String range;

            if (mc.getMatchingType() == MatchingType.MANUAL) {
                resource = mc.getResource();
                label = mc.getLabel() ;
                range = mc.getRange();
            } else {
                Component comp2 = mc.getComponent2();
                resource = comp2.getResource();
                label =  comp2.getLabel();
                range = comp2.getRange();
            }

            Property p = (Property) mergedDataset.createProperty(resource)
                    .addProperty(RDF.type, RDF.Property)
                    .addProperty(RDF.type, QB.MEASURE_PROPERTY)
                    .addProperty(RDFS.label, label)
                    .addProperty(RDFS.subPropertyOf, mergedDataset.createProperty(range));

            dsd.addProperty(QB.COMPONENT, mergedDataset.createResource().addProperty(QB.MEASURE, p));
        }
    }

    private Model setNamespaces(Model model) {
        model.setNsPrefix(QB.getPrefix(), QB.getURI());
        model.setNsPrefix(CODE.getPrefix(), CODE.getURI());
        model.setNsPrefix("rdf", RDF.getURI());
        model.setNsPrefix("rdfs", RDFS.getURI());
        model.setNsPrefix(VA.getPrefix(), VA.getURI());
        model.setNsPrefix(PROV.getPrefix(), PROV.getURI());

        return model;
    }

    private Resource createDataset(String label, String description) {
        Resource dataset = mergedDataset.createResource(CODE.DATASET + getRandomId());
        dataset.addProperty(RDF.type, QB.DATASET)
                .addProperty(RDFS.label, label)
                .addProperty(RDFS.comment, description)
                .addProperty(DC.format, VERSION)
                .addProperty(DC.relation, RELATION)
                .addProperty(DC.source, SOURCE);

        return dataset;
    }

    private String getRandomId() {
        return UUID.randomUUID().toString();
    }

    private void createDerivedFrom(Resource dMerged, String d1, String d2) {
        Resource dataset1 = mergedDataset.createResource(d1);
        Resource dataset2 = mergedDataset.createResource(d2);

        dMerged.addProperty(PROV.WAS_DERIVED_FROM, dataset1);
        dMerged.addProperty(PROV.WAS_DERIVED_FROM, dataset2);
    }

    private void addProvenanceInformation(Resource dataset, String auth) {
        String id = getRandomId();
        Resource importerAgent = mergedDataset.createResource(CODE.IMPORTER + "-" + id);
        importerAgent.addLiteral(RDFS.label, auth);
        Resource importActivity = mergedDataset.createResource(CODE.IMPORT + "-" + id);
        importActivity.addProperty(PROV.WAS_STARTED_BY, importerAgent);

        dataset.addProperty(PROV.WAS_GENERATED_BY, importActivity);
    }

    private Resource createDatasetStructureDefintion(Resource dataset) {
        Resource dsd = mergedDataset.createResource(CODE.DSD + getRandomId());
        dataset.addProperty(QB.STRUCTURE, dsd);
        dsd.addProperty(RDF.type, QB.DSD);

        return dsd;
    }

    public MergedDSD getMergedDSD(String g1, String g2) {
        MergedDSD result = new MergedDSD();
        result.setG1(g1);
        result.setG2(g2);

        StructureDefinition sd1 = getStructureDetails(g1);
        StructureDefinition sd2 = getStructureDetails(g2);

        // handle dimensions
        for (String dimension : sd1.getDimensions()) {
            if (sd2.getDimensions().contains(dimension)) {
                Component c1 = new Component();
                Component c2 = new Component();

                c1.setResource(dimension);
                c1.setLabel(mergeDao.getLabelForResource(g1, dimension));
                c1.setRange(mergeDao.getRangeForResource(g1, dimension));

                c2.setResource(dimension);
                c2.setLabel(mergeDao.getLabelForResource(g2, dimension));
                c2.setRange(mergeDao.getRangeForResource(g2, dimension));

                MatchingComponent mc = new MatchingComponent();
                mc.setComponent1(c1);
                mc.setComponent2(c2);
                mc.setMatchingType(MatchingType.AUTO);
                result.getMatchingDimensions().add(mc);
            } else {
                Component c1 = new Component();
                c1.setResource(dimension);
                c1.setLabel(mergeDao.getLabelForResource(g1, dimension));
                c1.setRange(mergeDao.getRangeForResource(g1, dimension));

                result.getDimensions1().add(c1);
            }
        }

        for (String dimension : sd2.getDimensions()) {
            if (!sd1.getDimensions().contains(dimension)) {
                Component c2 = new Component();
                c2.setResource(dimension);
                c2.setLabel(mergeDao.getLabelForResource(g2, dimension));
                c2.setRange(mergeDao.getRangeForResource(g2, dimension));

                result.getDimensions2().add(c2);
            }
        }

        // handle measures
        for (String measure : sd1.getMeasures()) {
            if (sd2.getMeasures().contains(measure)) {
                Component c1 = new Component();
                Component c2 = new Component();

                c1.setResource(measure);
                c1.setLabel(mergeDao.getLabelForResource(g1, measure));
                c1.setRange(mergeDao.getRangeForResource(g1, measure));

                c2.setResource(measure);
                c2.setLabel(mergeDao.getLabelForResource(g2, measure));
                c2.setRange(mergeDao.getRangeForResource(g2, measure));

                MatchingComponent mc = new MatchingComponent();
                mc.setComponent1(c1);
                mc.setComponent2(c2);
                mc.setMatchingType(MatchingType.AUTO);
                result.getMatchingMeasures().add(mc);
            } else {
                Component c1 = new Component();
                c1.setResource(measure);
                c1.setLabel(mergeDao.getLabelForResource(g1, measure));
                c1.setRange(mergeDao.getRangeForResource(g1, measure));

                result.getMeasures1().add(c1);
            }
        }

        for (String measure : sd2.getMeasures()) {
            if (!sd1.getMeasures().contains(measure)) {
                Component c2 = new Component();
                c2.setResource(measure);
                c2.setLabel(mergeDao.getLabelForResource(g2, measure));
                c2.setRange(mergeDao.getRangeForResource(g2, measure));

                result.getMeasures2().add(c2);
            }
        }

        this.mergedDSD = result;

        return result;
    }

    public StructureDefinition getStructureDetails(String graph) {
        Map<String, String> components = mergeDao.getComponents(graph);
        List<String> dimensions = new LinkedList<String>();
        List<String> measures = new LinkedList<String>();

        StructureDefinition structureDefinition = new StructureDefinition();
        for (String key : components.keySet()) {
            String value = components.get(key);

            if (value.equals(QB.DIMENSION.getURI())) {
                dimensions.add(key);
            } else if (value.equals(QB.MEASURE.getURI())) {
                measures.add(key);
            } else {
                logger.warn("dataset is possible not valid due to unsupported component type: " + value + " Graph: " + graph);
                //return structureDefinition;
            }
        }

        structureDefinition.setDimensions(dimensions);
        structureDefinition.setMeasures(measures);

        return structureDefinition;
    }

    public void addDimension(String graph, String label, String resource, String value) {
        mergeStructureService.addDimension(mergedDSD, graph, label, resource, value);
    }

    public MergedDSD getMergedDSD() {
        return mergedDSD;
    }

    public String getMergedDataset() {
        return convertModelToString(mergedDataset);
    }

    private String convertModelToString(Model model) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        model.write(baos, Lang.N3.getName());

        return baos.toString();
    }

    public ComponentType isMergable(String resource1, String resource2) {
        ComponentType type1 = mergeStructureService.getComponentType(mergedDSD, true, resource1);
        ComponentType type2 = mergeStructureService.getComponentType(mergedDSD, false, resource2);

        if (type1 == type2) {
            return type1;
        } else {
            return ComponentType.UNKNOWN;
        }
    }

    public void mergeComponents(ComponentType type, String resource1, String resource2, String label, String range, String resource) {
        mergeStructureService.mergeComponents(mergedDSD, resource1, resource2, label, range, resource, type);
    }

    public List<DatasetDescription> getDatasetDescriptions(String userId) {
        return mergeDao.getDatasetsFromUser(userId);
    }

    public String generateContext() {
        return CONTEXT_REFIX + getRandomId();
    }

    public DatasetDescription getDatasetDescription(String graph, String auth) {
        return mergeDao.getDatasetFromUser(graph, auth);
    }

}

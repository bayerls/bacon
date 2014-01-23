package de.uop.code.cubemerging.service;

import de.uop.code.cubemerging.domain.Component;
import de.uop.code.cubemerging.domain.ComponentType;
import de.uop.code.cubemerging.domain.MatchingComponent;
import de.uop.code.cubemerging.domain.MergedDSD;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MergeStructureService {

    public void addDimension(MergedDSD mergedDSD, String graph, String label, String resource, String value) {
        Component component = new Component();
        component.setLabel(label);
        component.setValue(value);
        component.setResource(resource);

        if (mergedDSD.getG1().equals(graph)) {
            mergedDSD.getDimensions1().add(component);
        } else {
            mergedDSD.getDimensions2().add(component);
        }
    }

    public void mergeComponents(MergedDSD mergedDSD, String resource1, String resource2, String label, String range, String resource, ComponentType type) {

        if (type == ComponentType.DIMENSION) {
            Component comp1 = getComponent(mergedDSD.getDimensions1(), resource1);
            Component comp2 = getComponent(mergedDSD.getDimensions2(), resource2);
            mergedDSD.getDimensions1().remove(comp1);
            mergedDSD.getDimensions2().remove(comp2);

            MatchingComponent mc = new MatchingComponent();
            mc.setComponent1(comp1);
            mc.setComponent2(comp2);
            mc.setLabel(label);
            mc.setResource(resource);
            mc.setRange(range);
            mc.setMatchingType(MatchingType.MANUAL);
            mergedDSD.getMatchingDimensions().add(mc);
        } else {
            Component comp1 = getComponent(mergedDSD.getMeasures1(), resource1);
            Component comp2 = getComponent(mergedDSD.getMeasures2(), resource2);
            mergedDSD.getMeasures1().remove(comp1);
            mergedDSD.getMeasures2().remove(comp2);

            MatchingComponent mc = new MatchingComponent();
            mc.setComponent1(comp1);
            mc.setComponent2(comp2);
            mc.setLabel(label);
            mc.setResource(resource);
            mc.setMatchingType(MatchingType.MANUAL);
            mc.setRange(range);
            mergedDSD.getMatchingMeasures().add(mc);
        }
    }

    private Component getComponent(List<Component> list, String resource) {

        for (Component component : list) {
            if (component.getResource().equals(resource)) {
                return component;
            }
        }

        return null;
    }

    public ComponentType getComponentType(MergedDSD mergedDSD, boolean firstGraph, String resource) {
        List<Component> dims;
        List<Component> meas;


        if (firstGraph) {
            dims = mergedDSD.getDimensions1();
            meas = mergedDSD.getMeasures1();
        } else {
            dims = mergedDSD.getDimensions2();
            meas = mergedDSD.getMeasures2();
        }

        for (Component c : dims) {
            if (c.getResource().equals(resource)) {
                return ComponentType.DIMENSION;
            }
        }

        for (Component c : meas) {
            if (c.getResource().equals(resource)) {
                return ComponentType.MEASURE;
            }
        }

        return ComponentType.UNKNOWN;
    }
}

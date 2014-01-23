package de.uop.code.cubemerging.domain;

import java.util.List;
import java.util.Map;

public class EntityDefinitionWrapper {

    private List<EntityDefinition> entityDefinitionList;
    private Map<String, String> renameMap;

    public List<EntityDefinition> getEntityDefinitionList() {
        return entityDefinitionList;
    }

    public void setEntityDefinitionList(List<EntityDefinition> entityDefinitionList) {
        this.entityDefinitionList = entityDefinitionList;
    }

    public Map<String, String> getRenameMap() {
        return renameMap;
    }

    public void setRenameMap(Map<String, String> renameMap) {
        this.renameMap = renameMap;
    }
}

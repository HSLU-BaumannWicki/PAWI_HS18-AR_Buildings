package commonlib.model.texture;

import eu.kudan.kudan.ARLightMaterial;
import eu.kudan.kudan.ARModelNode;

public class TexturizerModelBlack implements Texturizer {
    @Override
    public ARModelNode setTexture(ARModelNode model) {
        ARLightMaterial concreteMaterial = new ARLightMaterial();
        concreteMaterial.setAmbient(1f, 100f, 100f);
        model.getMeshNodes().forEach(node -> node.setMaterial(concreteMaterial));
        return model;
    }
}

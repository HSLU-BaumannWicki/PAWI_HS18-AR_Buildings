package commonlib.model.texture;

import eu.kudan.kudan.ARLightMaterial;
import eu.kudan.kudan.ARModelNode;
import eu.kudan.kudan.ARTexture2D;

public class TexturizerModelConcreteGray implements Texturizer {
    @Override
    public ARModelNode setTexture(ARModelNode model) {
        // Add Texture to the model
        ARTexture2D concreteTexture = new ARTexture2D();
        concreteTexture.loadFromAsset("concrete.jpg");

        // Add ambient lighting
        ARLightMaterial concreteMaterial = new ARLightMaterial();
        concreteMaterial.setTexture(concreteTexture);
        concreteMaterial.setAmbient(0.8f, 0.8f, 0.8f);

        model.getMeshNodes().forEach(node -> node.setMaterial(concreteMaterial));
        return model;
    }
}

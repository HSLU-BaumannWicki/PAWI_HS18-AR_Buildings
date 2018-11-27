package commonlib.model.texture;

import eu.kudan.kudan.ARLightMaterial;
import eu.kudan.kudan.ARModelNode;
import eu.kudan.kudan.ARTexture2D;

public class TexturizerModelConcreteGlassWood implements Texturizer {
    private ARTexture2D concreteTexture;
    private ARTexture2D glassTexture;
    private ARTexture2D woodTexture;
    private ARLightMaterial concreteMaterial;
    private ARLightMaterial glassMaterial;
    private ARLightMaterial woodMaterial;


    public TexturizerModelConcreteGlassWood(ARTexture2D concreteTexture, ARTexture2D glassTexture,
                                        ARTexture2D woodTexture, ARLightMaterial concreteMaterial,
                                        ARLightMaterial glassMaterial, ARLightMaterial woodMaterial){
        this.concreteTexture = concreteTexture;
        this.glassTexture = glassTexture;
        this.woodTexture = woodTexture;
        this.concreteMaterial = concreteMaterial;
        this.glassMaterial = glassMaterial;
        this.woodMaterial = woodMaterial;

        concreteTexture.loadFromAsset("concrete.jpg");
        glassTexture.loadFromAsset("glass.jpg");
        woodTexture.loadFromAsset("wood.jpg");

        concreteMaterial.setTexture(concreteTexture);
        concreteMaterial.setAmbient(0.8f, 0.8f, 0.8f);

        woodMaterial.setTexture(woodTexture);
        woodMaterial.setAmbient(0.8f, 0.8f, 0.8f);

        glassMaterial.setTexture(glassTexture);
        glassMaterial.setAlpha(0.4f);
        glassMaterial.setReflectivity(0.5f);
        glassMaterial.setAmbient(0.8f,0.8f,0.8f);
    }

    @Override
    public ARModelNode setTexture(ARModelNode model) {
        model.getMeshNodes().forEach(arMeshNode -> arMeshNode.setMaterial(concreteMaterial));
        model.getMeshNodes().forEach(
                arMeshNode -> {if (arMeshNode.getName().contains("glass")) arMeshNode.setMaterial(glassMaterial);});
        model.getMeshNodes().forEach(
                arMeshNode -> {if (arMeshNode.getName().contains("wood")) arMeshNode.setMaterial(woodMaterial);});
        return model;
    }
}

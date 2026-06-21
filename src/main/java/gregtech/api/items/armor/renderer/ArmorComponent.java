package gregtech.api.items.armor.renderer;

import java.awt.image.BufferedImage;

import com.google.gson.Gson;

public final class ArmorComponent {

    public final String componentId;
    public BedrockArmorModel jsonModel;
    public BufferedImage texture;

    public ArmorComponent(String componentId, BedrockArmorModel jsonModel, BufferedImage texture) {
        this.componentId = componentId;

        this.jsonModel = jsonModel;
        this.texture = texture;
    }

    public BufferedImage getTexture() {
        return texture;
    }

    public BedrockArmorModel getModel() {
        return jsonModel;
    }

    public BedrockArmorModel getClone() {
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(jsonModel), BedrockArmorModel.class);
    }
}

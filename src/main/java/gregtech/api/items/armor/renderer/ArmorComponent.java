package gregtech.api.items.armor.renderer;

import net.minecraft.util.ResourceLocation;

import com.google.gson.Gson;

public final class ArmorComponent {

    public final String componentId;

    public final ResourceLocation modelLocation;
    public final ResourceLocation textureLocation;

    public ArmorComponent(String componentId, String modelPath, String texturePath) {
        this.componentId = componentId;

        this.modelLocation = new ResourceLocation("gregtech", modelPath);
        this.textureLocation = new ResourceLocation("gregtech", texturePath);
    }

    public BedrockArmorModel cloneModel(BedrockArmorModel original) {
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(original), BedrockArmorModel.class);
    }
}

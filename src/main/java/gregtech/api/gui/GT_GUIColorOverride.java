package gregtech.api.gui;

import gregtech.api.GregTech_API;
import gregtech.api.util.ColorsMetadataSection;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

public class GT_GUIColorOverride {

    private ColorsMetadataSection cmSection;

    public GT_GUIColorOverride(String guiTexturePath) {
        try {
            IResource ir =
                    Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(guiTexturePath));
            if (ir.hasMetadata()) {
                cmSection = (ColorsMetadataSection) ir.getMetadata("colors");
            }
        } catch (IOException ignore) {
        }
    }

    public int getTextColorOrDefault(String textType, int defaultColor) {
        return sLoaded() ? cmSection.getTextColorOrDefault(textType, defaultColor) : defaultColor;
    }

    public int getGuiTintOrDefault(String key, int defaultColor) {
        return sLoaded() ? cmSection.getGuiTintOrDefault(key, defaultColor) : defaultColor;
    }

    public boolean sGuiTintingEnabled() {
        return sLoaded() ? cmSection.sGuiTintingEnabled() : GregTech_API.sColoredGUI;
    }

    public boolean sLoaded() {
        return cmSection != null;
    }
}

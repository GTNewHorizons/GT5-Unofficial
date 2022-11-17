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
            // this is dumb, but CombTypeTest causes cascading class load
            // and leads to instantiation of GT_CoverBehaviorBase
            if (Minecraft.getMinecraft() == null) return;
            IResource ir =
                    Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(guiTexturePath));
            if (ir.hasMetadata()) {
                cmSection = (ColorsMetadataSection) ir.getMetadata("colors");
            }
        } catch (IOException | NoClassDefFoundError ignore) {
            // this is also dumb, but FMLCommonHandler#getEffectiveSide doesn't work during test
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

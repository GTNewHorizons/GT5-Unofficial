package gtPlusPlus.api.objects.minecraft;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.NotNull;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTLanguageManager;
import gtPlusPlus.core.material.Material;

public class FluidGT6 extends Fluid implements Runnable {

    private final short[] mRGBa;
    public final String mTextureName;
    private String orePrefixKey;
    private Material material;

    public FluidGT6(final @NotNull String aName, final String aTextureName, final short[] aRGBa, String aLocalName) {
        super(aName);
        this.mRGBa = aRGBa;
        this.mTextureName = aTextureName;
        if (GregTechAPI.sGTBlockIconload != null) {
            GregTechAPI.sGTBlockIconload.add(this);
        }
        generateLocalizedName(aLocalName);
    }

    @Override
    public int getColor() {
        return (Math.clamp(this.mRGBa[0], 0, 255) << 16) | (Math.clamp(this.mRGBa[1], 0, 255) << 8)
            | Math.clamp(this.mRGBa[2], 0, 255);
    }

    @Override
    public void run() {
        this.setIcons(GregTechAPI.sBlockIcons.registerIcon(GTPlusPlus.ID + ":" + "fluids/fluid." + this.mTextureName));
    }

    private void generateLocalizedName(String aLocalName) {
        Material material = Material.mMaterialCache.get(aLocalName.toLowerCase());
        if (material != null) {
            this.material = material;
            return;
        }
        if (generateLocalizedNameHasOreprefix(aLocalName, "Molten %s")) return;
        if (generateLocalizedNameHasOreprefix(aLocalName, "%s Plasma")) return;
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName(), aLocalName);
    }

    private boolean generateLocalizedNameHasOreprefix(String aLocalName, String orePrefixFormat) {
        String prefixRemoved = orePrefixFormat.replace("%s", "");
        if (aLocalName.contains(prefixRemoved)) {
            String materialName = aLocalName.replace(prefixRemoved, "");
            Material material = Material.mMaterialCache.get(materialName.toLowerCase());
            if (material != null) {
                this.material = material;
                this.orePrefixKey = orePrefixFormat;
                return true;
            }
        }
        return false;
    }

    @Override
    public String getLocalizedName() {
        if (material != null) {
            return orePrefixKey == null ? material.getLocalizedName()
                : OrePrefixes.getLocalizedNameForItem(orePrefixKey, "%s", material);
        }
        return super.getLocalizedName();
    }

    public Material getMaterial() {
        return material;
    }
}

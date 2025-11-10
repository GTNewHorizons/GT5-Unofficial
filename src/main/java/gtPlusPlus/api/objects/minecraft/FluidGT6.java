package gtPlusPlus.api.objects.minecraft;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.function.Supplier;

import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.NotNull;

import gregtech.api.GregTechAPI;
import gregtech.api.util.GTLanguageManager;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

public class FluidGT6 extends Fluid implements Runnable {

    private final short[] mRGBa;
    public final String mTextureName;
    private String materialName;
    private String oreprefixKey;
    private Supplier<String> localizedName;

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
        return (Math.max(0, Math.min(255, this.mRGBa[0])) << 16) | (Math.max(0, Math.min(255, this.mRGBa[1])) << 8)
            | Math.max(0, Math.min(255, this.mRGBa[2]));
    }

    @Override
    public void run() {
        this.setIcons(GregTechAPI.sBlockIcons.registerIcon(GTPlusPlus.ID + ":" + "fluids/fluid." + this.mTextureName));
    }

    private void generateLocalizedName(String aLocalName) {
        String materialKey = MaterialUtils.getMaterialLocalizedNameKey(aLocalName);
        if (StatCollector.translateToFallback(materialKey)
            .equals(aLocalName)) {
            materialName = aLocalName;
            return;
        }
        if (generateLocalizedNameHasOreprefix(aLocalName, "Molten %s")) return;
        if (generateLocalizedNameHasOreprefix(aLocalName, "%s Plasma")) return;
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName(), aLocalName);
    }

    private boolean generateLocalizedNameHasOreprefix(String aLocalName, String oreprefixFormat) {
        String oreprefixFormatRemoved = oreprefixFormat.replace("%s", "");
        if (aLocalName.contains(oreprefixFormatRemoved)) {
            String materialName = aLocalName.replace(oreprefixFormatRemoved, "");
            String materialKey = MaterialUtils.getMaterialLocalizedNameKey(materialName);
            if (aLocalName.equals(String.format(oreprefixFormat, StatCollector.translateToFallback(materialKey)))) {
                this.materialName = materialName;
                this.oreprefixKey = materialKey;
                return true;
            }
        }
        return false;
    }

    @Override
    public String getLocalizedName() {
        if (materialName != null) {
            final String materialLocalizedName = MaterialUtils.getMaterialLocalizedName(materialName);
            return oreprefixKey == null ? materialLocalizedName
                : StatCollector.translateToLocalFormatted(oreprefixKey, materialLocalizedName);
        }
        return super.getLocalizedName();
    }

    public Material getMaterial() {
        if (materialName == null) return null;
        return Material.mMaterialCache.get(materialName.toLowerCase());
    }
}

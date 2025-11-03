package gtPlusPlus.api.objects.minecraft;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.NotNull;

import gregtech.api.GregTechAPI;
import gregtech.api.util.GTLanguageManager;
import gtPlusPlus.core.util.Utils;

public class FluidGT6 extends Fluid implements Runnable {

    private final short[] mRGBa;
    public final String mTextureName;
    private String mNameKey;
    private String mFormatKey;

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
        String materialKey = Utils.getGTPPMaterialLocalizedNameKey(aLocalName);
        if (StatCollector.translateToLocal(materialKey)
            .equals(aLocalName)) {
            this.mNameKey = materialKey;
            return;
        }
        if (generateLocalizedNameHasOreprefix(aLocalName, "Molten %s")) return;
        if (generateLocalizedNameHasOreprefix(aLocalName, "%s Plasma")) return;
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName(), aLocalName);
    }

    private boolean generateLocalizedNameHasOreprefix(String aLocalName, String oreprefixName) {
        String oreprefixNameRemovedFormat = oreprefixName.replace("%s", "");
        if (aLocalName.contains(oreprefixNameRemovedFormat)) {
            String materialName = aLocalName.replace(oreprefixNameRemovedFormat, "");
            String materialKey = Utils.getGTPPMaterialLocalizedNameKey(materialName);
            if (aLocalName.equals(String.format(oreprefixName, StatCollector.translateToLocal(materialKey)))) {
                this.mNameKey = materialKey;
                this.mFormatKey = "gt.oreprefix." + oreprefixName.toLowerCase()
                    .replace("%s", "material")
                    .replace(" ", "_");
                return true;
            }
        }
        return false;
    }

    @Override
    public String getLocalizedName() {
        if (mNameKey == null) return super.getLocalizedName();
        return mFormatKey == null ? StatCollector.translateToLocal(mNameKey)
            : StatCollector.translateToLocalFormatted(mFormatKey, StatCollector.translateToLocal(mNameKey));
    }
}

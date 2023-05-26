package gtPlusPlus.xmod.gregtech.common.tileentities.generators.ULV;

import static gregtech.api.enums.GT_Values.V;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.tileentities.generators.GT_MetaTileEntity_DieselGenerator;
import gtPlusPlus.core.lib.CORE;

public class GT_MetaTileEntity_ULV_CombustionGenerator extends GT_MetaTileEntity_DieselGenerator {

    public GT_MetaTileEntity_ULV_CombustionGenerator(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public GT_MetaTileEntity_ULV_CombustionGenerator(String aName, int aTier, String[] aDescription,
            ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.addAll(
                this.mDescriptionArray,
                "Produces " + (this.getPollution() * 20) + " pollution/sec",
                "Fuel Efficiency: " + this.getEfficiency() + "%",
                CORE.GT_Tooltip.get());
    }

    @Override
    public long maxEUStore() {
        return Math.max(getEUVar(), V[1] * 80L + getMinimumStoredEU());
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_ULV_CombustionGenerator(
                this.mName,
                this.mTier,
                this.mDescriptionArray,
                this.mTextures);
    }

    @Override
    public int getCapacity() {
        return 16000;
    }

    @Override
    public void onConfigLoad() {
        this.mEfficiency = GregTech_API.sMachineFile
                .get(ConfigCategories.machineconfig, "DieselGenerator.efficiency.tier." + this.mTier, 95);
    }
}

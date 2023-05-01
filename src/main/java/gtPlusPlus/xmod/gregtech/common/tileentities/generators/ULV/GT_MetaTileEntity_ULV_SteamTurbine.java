package gtPlusPlus.xmod.gregtech.common.tileentities.generators.ULV;

import static gregtech.api.enums.GT_Values.V;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.tileentities.generators.GT_MetaTileEntity_SteamTurbine;

public class GT_MetaTileEntity_ULV_SteamTurbine extends GT_MetaTileEntity_SteamTurbine {

    public GT_MetaTileEntity_ULV_SteamTurbine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 0);
    }

    public GT_MetaTileEntity_ULV_SteamTurbine(String aName, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, 0, aDescription, aTextures);
    }

    @Override
    public long maxEUStore() {
        return Math.max(getEUVar(), V[1] * 80L + getMinimumStoredEU());
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_ULV_SteamTurbine(this.mName, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public int getCapacity() {
        return 16000;
    }

    @Override
    public void onConfigLoad() {
        this.mEfficiency = GregTech_API.sMachineFile
                .get(ConfigCategories.machineconfig, "SteamTurbine.efficiency.tier." + this.mTier, 6 + 1);
    }
}

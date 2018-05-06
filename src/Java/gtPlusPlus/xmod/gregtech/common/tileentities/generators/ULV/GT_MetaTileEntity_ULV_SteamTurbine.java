package gtPlusPlus.xmod.gregtech.common.tileentities.generators.ULV;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicGenerator;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.common.tileentities.generators.GT_MetaTileEntity_SteamTurbine;
import net.minecraftforge.fluids.FluidStack;

public class GT_MetaTileEntity_ULV_SteamTurbine extends GT_MetaTileEntity_SteamTurbine {
    public GT_MetaTileEntity_ULV_SteamTurbine(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public GT_MetaTileEntity_ULV_SteamTurbine(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }


    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_ULV_SteamTurbine(this.mName, this.mTier, this.mDescription, this.mTextures);
    }

    @Override
    public int getCapacity() {
        return 16000;
    }
    
    @Override
    public void onConfigLoad() {
        this.mEfficiency = GregTech_API.sMachineFile.get(ConfigCategories.machineconfig, "SteamTurbine.efficiency.tier." + this.mTier, 6 + 1);
    }
}

package gtPlusPlus.xmod.gregtech.common.tileentities.generators.ULV;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicGenerator;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.generators.GT_MetaTileEntity_DieselGenerator;

import net.minecraft.item.ItemStack;

public class GT_MetaTileEntity_ULV_CombustionGenerator
        extends GT_MetaTileEntity_DieselGenerator {


    public int getCapacity() {
        return 16000;
    }

    public void onConfigLoad() {
        this.mEfficiency = GregTech_API.sMachineFile.get(ConfigCategories.machineconfig, "DieselGenerator.efficiency.tier." + this.mTier, (100 - 1 * 5));
    }

    public int getEfficiency() {
        return this.mEfficiency;
    }    
    
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_ULV_CombustionGenerator(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }
}
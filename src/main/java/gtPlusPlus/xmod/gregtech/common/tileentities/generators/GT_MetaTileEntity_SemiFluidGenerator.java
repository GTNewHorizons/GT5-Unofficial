package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicGenerator;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.lib.CORE;

public class GT_MetaTileEntity_SemiFluidGenerator extends GT_MetaTileEntity_BasicGenerator {

    public int mEfficiency;

    public GT_MetaTileEntity_SemiFluidGenerator(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, "Requires semi-fluid Fuel", new ITexture[0]);
        onConfigLoad();
    }

    public GT_MetaTileEntity_SemiFluidGenerator(String aName, int aTier, String[] aDescription,
            ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        onConfigLoad();
    }

    @Override
    public int getPollution() {
        return (int) (CORE.ConfigSwitches.basePollutionPerSecondSemiFluidGenerator
                * CORE.ConfigSwitches.pollutionReleasedByTierSemiFluidGenerator[this.mTier]);
    }

    @Override
    public int getCapacity() {
        return 16000;
    }

    public void onConfigLoad() {
        this.mEfficiency = GregTech_API.sMachineFile.get(
                ConfigCategories.machineconfig,
                "SemiFluidGenerator.efficiency.tier." + this.mTier,
                100 - (this.mTier * 5));
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_SemiFluidGenerator(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        // Logger.WARNING("Fuel Count: "+Gregtech_Recipe_Map.sSemiFluidLiquidFuels.mRecipeList.size());
        return GTPPRecipeMaps.semiFluidFuels;
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.addAll(
                this.mDescriptionArray,
                "Produces " + (this.getPollution()) + " pollution/sec",
                "Fuel Efficiency: " + this.getEfficiency() + "%",
                CORE.GT_Tooltip.get());
    }

    @Override
    public int getEfficiency() {
        return this.mEfficiency;
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return (side == getBaseMetaTileEntity().getFrontFacing());
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, GT_ItemStack aCover) {
        if (side != this.getBaseMetaTileEntity().getFrontFacing()) {
            return true;
        }
        return super.allowCoverOnSide(side, aCover);
    }

    @Override
    public int getFuelValue(ItemStack aStack) {
        if ((GT_Utility.isStackInvalid(aStack)) || (getRecipeMap() == null)) {
            Logger.WARNING("Bad Fuel?");
            return 0;
        }
        int rValue = Math.max(GT_ModHandler.getFuelValue(aStack) * 6 / 5, super.getFuelValue(aStack));
        if (ItemList.Fuel_Can_Plastic_Filled.isStackEqual(aStack, false, true)) {
            rValue = Math.max(rValue, GameRegistry.getFuelValue(aStack) * 3);
        }
        Logger.WARNING("Good Fuel: " + rValue);
        return rValue;
    }

    @Override
    public ITexture[] getFront(byte aColor) {
        return new ITexture[] { super.getFront(aColor)[0],
                new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_FRONT),
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT[this.mTier] };
    }

    @Override
    public ITexture[] getBack(byte aColor) {
        return new ITexture[] { super.getBack(aColor)[0],
                new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_TOP) };
    }

    @Override
    public ITexture[] getBottom(byte aColor) {
        return new ITexture[] { super.getBottom(aColor)[0],
                new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_BOTTOM) };
    }

    @Override
    public ITexture[] getTop(byte aColor) {
        return new ITexture[] { super.getTop(aColor)[0],
                new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_SIDE) };
    }

    @Override
    public ITexture[] getSides(byte aColor) {
        return new ITexture[] { super.getSides(aColor)[0],
                new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_TOP) };
    }

    @Override
    public ITexture[] getFrontActive(byte aColor) {
        return new ITexture[] { super.getFrontActive(aColor)[0],
                new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_FRONT_ACTIVE),
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT[this.mTier] };
    }

    @Override
    public ITexture[] getBackActive(byte aColor) {
        return new ITexture[] { super.getBackActive(aColor)[0],
                new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_TOP_ACTIVE) };
    }

    @Override
    public ITexture[] getBottomActive(byte aColor) {
        return new ITexture[] { super.getBottomActive(aColor)[0],
                new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_BOTTOM_ACTIVE) };
    }

    @Override
    public ITexture[] getTopActive(byte aColor) {
        return new ITexture[] { super.getTopActive(aColor)[0],
                new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_SIDE_ACTIVE) };
    }

    @Override
    public ITexture[] getSidesActive(byte aColor) {
        return new ITexture[] { super.getSidesActive(aColor)[0],
                new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_TOP_ACTIVE) };
    }
}

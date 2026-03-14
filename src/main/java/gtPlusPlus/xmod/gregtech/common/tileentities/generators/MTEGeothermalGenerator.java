package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicGenerator;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEGeothermalGenerator extends MTEBasicGenerator {

    public MTEGeothermalGenerator(final int aID, final String aName, final String aNameRegional, final int aTier) {
        super(aID, aName, aNameRegional, aTier, "Requires Pahoehoe Lava or Normal Lava as Fuel");
    }

    public MTEGeothermalGenerator(final String aName, final int aTier, final String[] aDescription,
        final ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        String aPollution = "Causes " + addFormattedString(String.valueOf(this.getPollution()))
            + " Pollution per second";
        return ArrayUtils.addAll(
            this.mDescriptionArray,
            "Generates power at " + addFormattedString(String.valueOf(this.getEfficiency())) + "%% Efficiency per tick",
            aPollution,
            GTPPCore.GT_Tooltip.get());
    }

    @Override
    public int getCapacity() {
        // return MathUtils.roundToClosestMultiple(32000*(this.mTier/2), 25000);
        return 5000 * this.mTier;
    }

    @Override
    public int getEfficiency() {
        return 100 - (this.mTier * 7);
    }

    @Override
    public int getFuelValue(final ItemStack aStack) {
        int rValue = Math.max((GTModHandler.getFuelValue(aStack) * 6) / 5, super.getFuelValue(aStack));
        if (ItemList.Fuel_Can_Plastic_Filled.isStackEqual(aStack, false, true)) {
            rValue = Math.max(rValue, GameRegistry.getFuelValue(aStack) * 3);
        }
        return rValue;
    }

    @Override
    public boolean isOutputFacing(final ForgeDirection side) {
        return side == this.getBaseMetaTileEntity()
            .getFrontFacing();
    }

    @Override
    public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEGeothermalGenerator(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public ITexture[] getFront(final byte aColor) {
        return new ITexture[] { super.getFront(aColor)[0], TextureFactory.of(Textures.BlockIcons.DIESEL_GENERATOR_SIDE),
            Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_2A[this.mTier + 1] };
    }

    @Override
    public ITexture[] getBack(final byte aColor) {
        return new ITexture[] { super.getBack(aColor)[0], TextureFactory.of(Textures.BlockIcons.DIESEL_GENERATOR_BACK),
            TextureFactory.of(TexturesGtBlock.Overlay_Machine_Diesel_Vertical) };
    }

    @Override
    public ITexture[] getBottom(final byte aColor) {
        return new ITexture[] { super.getBottom(aColor)[0],
            TextureFactory.of(Textures.BlockIcons.DIESEL_GENERATOR_BOTTOM) };
    }

    @Override
    public ITexture[] getTop(final byte aColor) {
        return new ITexture[] { super.getTop(aColor)[0], TextureFactory.of(Textures.BlockIcons.DIESEL_GENERATOR_SIDE),
            TextureFactory.of(Textures.BlockIcons.OVERLAY_FRONT_ROCK_BREAKER) };
    }

    @Override
    public ITexture[] getSides(final byte aColor) {
        return new ITexture[] { super.getSides(aColor)[0], TextureFactory.of(Textures.BlockIcons.BOILER_LAVA_FRONT) };
    }

    @Override
    public ITexture[] getFrontActive(final byte aColor) {
        return new ITexture[] { super.getFrontActive(aColor)[0],
            TextureFactory.of(Textures.BlockIcons.DIESEL_GENERATOR_SIDE_ACTIVE),
            Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_2A[this.mTier + 1] };
    }

    @Override
    public ITexture[] getBackActive(final byte aColor) {
        return new ITexture[] { super.getBackActive(aColor)[0],
            TextureFactory.of(Textures.BlockIcons.DIESEL_GENERATOR_BACK_ACTIVE),
            TextureFactory.of(TexturesGtBlock.Overlay_Machine_Diesel_Vertical_Active) };
    }

    @Override
    public ITexture[] getBottomActive(final byte aColor) {
        return new ITexture[] { super.getBottomActive(aColor)[0],
            TextureFactory.of(Textures.BlockIcons.DIESEL_GENERATOR_BOTTOM_ACTIVE) };
    }

    @Override
    public ITexture[] getTopActive(final byte aColor) {
        return new ITexture[] { super.getTopActive(aColor)[0],
            TextureFactory.of(Textures.BlockIcons.DIESEL_GENERATOR_SIDE),
            TextureFactory.of(Textures.BlockIcons.OVERLAY_FRONT_ROCK_BREAKER_ACTIVE) };
    }

    @Override
    public ITexture[] getSidesActive(final byte aColor) {
        return new ITexture[] { super.getSidesActive(aColor)[0],
            TextureFactory.of(Textures.BlockIcons.BOILER_LAVA_FRONT_ACTIVE) };
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.hotFuels;
    }

    @Override
    public int getPollution() {
        return (int) (PollutionConfig.basePollutionPerSecondGeothermalGenerator
            * PollutionConfig.pollutionReleasedByTierGeothermalGenerator[mTier]);
    }
}

package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GTRenderedTexture;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTModHandler;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.generators.MTERocketFuelGeneratorBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTERocketFuelGenerator extends MTERocketFuelGeneratorBase {

    public int mEfficiency;

    public MTERocketFuelGenerator(final int aID, final String aName, final String aNameRegional, final int aTier) {
        super(aID, aName, aNameRegional, aTier, "Requires GT++ Rocket Fuels", new ITexture[0]);
    }

    public MTERocketFuelGenerator(final String aName, final int aTier, final String[] aDescription,
        final ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        this.mEfficiency = getEfficiency();
    }

    @Override
    public boolean isOutputFacing(final ForgeDirection side) {
        return side == this.getBaseMetaTileEntity()
            .getFrontFacing();
    }

    @Override
    public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTERocketFuelGenerator(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.rocketFuels;
    }

    @Override
    public int getCapacity() {
        return 32000;
    }

    @Override
    public int getEfficiency() {
        return 80 - (10 * (this.mTier - 4));
    }

    @Override
    public int getFuelValue(final ItemStack aStack) {
        int rValue = Math.max((GTModHandler.getFuelValue(aStack) * 6) / 5, super.getFuelValue(aStack));
        if (ItemList.Fuel_Can_Plastic_Filled.isStackEqual(aStack, false, true)) {
            rValue = Math.max(rValue, GameRegistry.getFuelValue(aStack) * 3);
        }
        return rValue;
    }

    private GTRenderedTexture getCasingTexture() {
        if (this.mTier <= 4) {
            return new GTRenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Top);
        } else if (this.mTier == 5) {

            return new GTRenderedTexture(TexturesGtBlock.Casing_Machine_Advanced);
        } else {

            return new GTRenderedTexture(TexturesGtBlock.Casing_Machine_Ultra);
        }
        // return new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Top);
    }

    @Override
    public ITexture[] getFront(final byte aColor) {
        return new ITexture[] { super.getFront(aColor)[0], this.getCasingTexture(),
            Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier] };
    }

    @Override
    public ITexture[] getBack(final byte aColor) {
        return new ITexture[] { super.getBack(aColor)[0], this.getCasingTexture(),
            new GTRenderedTexture(TexturesGtBlock.Overlay_Machine_Vent) };
    }

    @Override
    public ITexture[] getBottom(final byte aColor) {
        return new ITexture[] { super.getBottom(aColor)[0],
            new GTRenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    @Override
    public ITexture[] getTop(final byte aColor) {
        return new ITexture[] { super.getTop(aColor)[0],
            new GTRenderedTexture(TexturesGtBlock.Casing_Machine_Redstone_Off) };
    }

    @Override
    public ITexture[] getSides(final byte aColor) {
        return new ITexture[] { super.getSides(aColor)[0], this.getCasingTexture(),
            new GTRenderedTexture(TexturesGtBlock.Overlay_Machine_Diesel_Horizontal) };
    }

    @Override
    public ITexture[] getFrontActive(final byte aColor) {
        return new ITexture[] { super.getFrontActive(aColor)[0], this.getCasingTexture(),
            Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier] };
    }

    @Override
    public ITexture[] getBackActive(final byte aColor) {
        return new ITexture[] { super.getBackActive(aColor)[0], this.getCasingTexture(),
            new GTRenderedTexture(TexturesGtBlock.Overlay_Machine_Vent_Fast) };
    }

    @Override
    public ITexture[] getBottomActive(final byte aColor) {
        return new ITexture[] { super.getBottomActive(aColor)[0],
            new GTRenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    @Override
    public ITexture[] getTopActive(final byte aColor) {
        return new ITexture[] { super.getTopActive(aColor)[0],
            new GTRenderedTexture(TexturesGtBlock.Casing_Machine_Redstone_On) };
    }

    @Override
    public ITexture[] getSidesActive(final byte aColor) {
        return new ITexture[] { super.getSidesActive(aColor)[0], this.getCasingTexture(),
            new GTRenderedTexture(TexturesGtBlock.Overlay_Machine_Diesel_Horizontal_Active) };
    }
}

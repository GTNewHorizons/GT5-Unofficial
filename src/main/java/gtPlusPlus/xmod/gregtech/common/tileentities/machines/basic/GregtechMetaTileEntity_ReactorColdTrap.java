package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntity_ReactorColdTrap extends GT_MetaTileEntity_BasicMachine {

    public GregtechMetaTileEntity_ReactorColdTrap(int aID, String aName, String aNameRegional, int aTier) {
        super(
                aID,
                aName,
                aNameRegional,
                aTier,
                1,
                "Just like the Arctic",
                2,
                9,
                new ITexture[] { new GT_RenderedTexture(TexturesGtBlock.OVERLAY_REACTOR_COLDTRAP_SIDE_ACTIVE),
                        new GT_RenderedTexture(TexturesGtBlock.OVERLAY_REACTOR_COLDTRAP_SIDE),
                        new GT_RenderedTexture(TexturesGtBlock.OVERLAY_REACTOR_COLDTRAP_FRONT_ACTIVE),
                        new GT_RenderedTexture(TexturesGtBlock.OVERLAY_REACTOR_COLDTRAP_FRONT),
                        new GT_RenderedTexture(TexturesGtBlock.OVERLAY_REACTOR_COLDTRAP_TOP_ACTIVE),
                        new GT_RenderedTexture(TexturesGtBlock.OVERLAY_REACTOR_COLDTRAP_TOP),
                        new GT_RenderedTexture(TexturesGtBlock.OVERLAY_REACTOR_COLDTRAP_TOP_ACTIVE),
                        new GT_RenderedTexture(TexturesGtBlock.OVERLAY_REACTOR_COLDTRAP_TOP) });
    }

    public GregtechMetaTileEntity_ReactorColdTrap(String aName, int aTier, String[] aDescription,
            ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures, 2, 9);
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.addAll(this.mDescriptionArray, "Does not require ice cubes", CORE.GT_Tooltip.get());
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_ReactorColdTrap(
                this.mName,
                this.mTier,
                this.mDescriptionArray,
                this.mTextures);
    }

    @Override
    public boolean allowSelectCircuit() {
        return true;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeList() {
        return GTPP_Recipe.GTPP_Recipe_Map.sColdTrapRecipes;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
            ItemStack aStack) {
        return (super.allowPutStack(aBaseMetaTileEntity, aIndex, side, aStack))
                && (getRecipeList().containsInput(aStack));
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return super.isFluidInputAllowed(aFluid);
    }

    @Override
    public int getCapacity() {
        return 16000 * Math.max(1, this.mTier);
    }
}

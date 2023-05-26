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

public class GregtechMetaTileEntity_ReactorProcessingUnit extends GT_MetaTileEntity_BasicMachine {

    public GregtechMetaTileEntity_ReactorProcessingUnit(int aID, String aName, String aNameRegional, int aTier) {
        super(
                aID,
                aName,
                aNameRegional,
                aTier,
                1,
                "Processes Nuclear things",
                2,
                9,
                "Dehydrator.png",
                "",
                new ITexture[] { new GT_RenderedTexture(TexturesGtBlock.OVERLAY_REACTOR_PROCESSINGUNIT_SIDE_ACTIVE),
                        new GT_RenderedTexture(TexturesGtBlock.OVERLAY_REACTOR_PROCESSINGUNIT_SIDE),
                        new GT_RenderedTexture(TexturesGtBlock.OVERLAY_REACTOR_PROCESSINGUNIT_FRONT_ACTIVE),
                        new GT_RenderedTexture(TexturesGtBlock.OVERLAY_REACTOR_PROCESSINGUNIT_FRONT),
                        new GT_RenderedTexture(TexturesGtBlock.OVERLAY_REACTOR_PROCESSINGUNIT_TOP_ACTIVE),
                        new GT_RenderedTexture(TexturesGtBlock.OVERLAY_REACTOR_PROCESSINGUNIT_TOP),
                        new GT_RenderedTexture(TexturesGtBlock.OVERLAY_REACTOR_PROCESSINGUNIT_TOP_ACTIVE),
                        new GT_RenderedTexture(TexturesGtBlock.OVERLAY_REACTOR_PROCESSINGUNIT_TOP) });
    }

    public GregtechMetaTileEntity_ReactorProcessingUnit(String aName, int aTier, String[] aDescription,
            ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 2, 9, aGUIName, aNEIName);
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.add(this.mDescriptionArray, CORE.GT_Tooltip.get());
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_ReactorProcessingUnit(
                this.mName,
                this.mTier,
                this.mDescriptionArray,
                this.mTextures,
                this.mGUIName,
                this.mNEIName);
    }

    @Override
    public boolean allowSelectCircuit() {
        return true;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeList() {
        return GTPP_Recipe.GTPP_Recipe_Map.sReactorProcessingUnitRecipes;
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
        return 8000 * Math.max(1, this.mTier);
    }
}

package goodgenerator.blocks.tileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.common.widget.FluidSlotWidget;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.util.GTSplit;
import gregtech.api.util.GTUtility;

@IMetaTileEntity.SkipGenerateDescription
public class AntimatterOutputHatch extends MTEHatchOutput {

    private static final FluidStack ANTIMATTER = Materials.Antimatter.getFluid(1);

    public AntimatterOutputHatch(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 11);
    }

    public AntimatterOutputHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        super.setLockedFluidName(
            Materials.Antimatter.getFluid(1)
                .getFluid()
                .getName());
    }

    @Override
    public void setLockedFluidName(String lockedFluidName) {
        this.lockedFluidName = Materials.Antimatter.getFluid(1)
            .getFluid()
            .getName();
        markDirty();
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new AntimatterOutputHatch(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public int getCapacity() {
        return 16_384_000;
    }

    @Override
    public boolean isFluidLocked() {
        return true;
    }

    @Override
    protected void onEmptyingContainerWhenEmpty() {
        // Disable removing the lock
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (!getBaseMetaTileEntity().getCoverAtSide(side)
            .isGUIClickable()) return;
        mMode ^= 1;
        GTUtility.sendChatTrans(
            aPlayer,
            mMode == 1 ? "gg.chat.antimatter_output_hatch.front_face_input.enable"
                : "gg.chat.antimatter_output_hatch.front_face_input.disable");
    }

    @Override
    public boolean isLiquidInput(ForgeDirection side) {
        return mMode == 1 || side != this.getBaseMetaTileEntity()
            .getFrontFacing();
    }

    @Override
    public boolean isLiquidOutput(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public String[] getDescription() {
        return GTSplit.splitLocalized("gt.blockmachines.output_hatch_antimatter.desc");
    }

    @Override
    protected FluidSlotWidget createFluidSlot() {
        return super.createFluidSlot().setFilter(f -> f == Materials.Antimatter.mFluid);
    }
}

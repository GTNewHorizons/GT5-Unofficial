package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.FLUID_VOID_SIGN;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTSplit;
import gregtech.common.gui.modularui.hatch.MTEHatchOutputGui;

@IMetaTileEntity.SkipGenerateDescription
public class MTEHatchVoid extends MTEHatchOutput {

    public MTEHatchVoid(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 1, null, 1);
    }

    public MTEHatchVoid(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(FLUID_VOID_SIGN) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(FLUID_VOID_SIGN) };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchVoid(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean doesFillContainers() {
        return false;
    }

    @Override
    public boolean canTankBeEmptied() {
        return false;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aindex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aindex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public int getCapacity() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {}

    @Override
    public boolean outputsItems() {
        return false;
    }

    @Override
    public int fill(FluidStack aFluid, boolean doFill) {
        return aFluid.amount;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public boolean isEmptyAndAcceptsAnyFluid() {
        return mMode == 0;
    }

    @Override
    public boolean canStoreFluid(@Nonnull FluidStack fluidStack) {
        if (isFluidLocked()) {
            if (lockedFluid == null) {
                return false;
            }
            return lockedFluid.equals(fluidStack.getFluid());
        }
        return false;
    }

    @Override
    protected void onEmptyingContainerWhenEmpty() {}

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchOutputGui(this) {

            @Override
            protected boolean supportsScreen() {
                return false;
            }

            @Override
            protected boolean supportsIO() {
                return false;
            }
        }.build(guiData, syncManager, uiSettings);
    }

    @Override
    public String[] getDescription() {
        return GTSplit.splitLocalized("gt.blockmachines.output_hatch_void.desc");
    }
}

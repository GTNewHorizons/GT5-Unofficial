package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_HATCH_IN_DEBUG;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.fluid.FluidStackTank;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.enums.GTAuthors;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTSplit;
import gregtech.common.gui.modularui.hatch.MTEHatchInputDebugGui;

@IMetaTileEntity.SkipGenerateDescription
public class MTEHatchInputDebug extends MTEHatchInput {

    private static final int SLOT_COUNT = 16;
    public final FluidStackTank[] fluidTankList = new FluidStackTank[SLOT_COUNT];
    public final FluidStack[] fluidList = new FluidStack[SLOT_COUNT];

    public MTEHatchInputDebug(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, 1, aName, aNameRegional, aTier, null);
    }

    public MTEHatchInputDebug(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, 1, aTier, aDescription, aTextures);
        for (int i = 0; i < SLOT_COUNT; i++) {
            final int index = i;
            fluidTankList[i] = new FluidStackTank(() -> fluidList[index], fluid -> fluidList[index] = fluid, 1);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (fluidList != null) {
            for (int i = 0; i < fluidList.length; i++) {
                if (aNBT.hasKey("debugFluid" + i)) {
                    fluidList[i] = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("debugFluid" + i));
                }
            }
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (fluidList != null) {
            for (int i = 0; i < fluidList.length; i++) {
                if (fluidList[i] != null) {
                    aNBT.setTag("debugFluid" + i, fluidList[i].writeToNBT(new NBTTagCompound()));
                }
            }
        }
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_HATCH_IN_DEBUG) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_HATCH_IN_DEBUG) };
    }

    @Override
    public boolean canTankBeEmptied() {
        return false;
    }

    @Override
    public boolean canTankBeFilled() {
        return false;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    public FluidStack[] getFluidList() {
        return fluidList;
    }

    @Override
    public FluidStack getFillableStack() {
        return this.getFluid();
    }

    @Override
    public FluidStack getDrainableStack() {
        return this.getFluid();
    }

    @Override
    public FluidStack getFluid() {
        for (FluidStack fluid : fluidList) {
            if (fluid == null) continue;

            FluidStack copied = fluid.copy();
            copied.amount = Integer.MAX_VALUE;
            return copied;
        }
        return null;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchInputDebug(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public String[] getDescription() {
        return GTSplit.splitLocalizedWithSuffix(
            "gt.blockmachines.input_hatch_debug.desc",
            GTAuthors.buildAuthorsWithFormat(GTAuthors.AuthorChrom));
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack aFluid, boolean doDrain) {

        if (aFluid == null) return null;
        for (FluidStack stack : fluidList) {
            if (stack != null && stack.getFluid() == aFluid.getFluid()) {
                return aFluid.copy();
            }
        }
        return null;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchInputDebugGui(this).build(data, syncManager, uiSettings);
    }
}

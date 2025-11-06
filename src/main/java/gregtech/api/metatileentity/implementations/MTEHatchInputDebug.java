package gregtech.api.metatileentity.implementations;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.BOLD;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GREEN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_HATCH_IN_DEBUG;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.common.gui.modularui.hatch.MTEHatchInputDebugGui;

public class MTEHatchInputDebug extends MTEHatchInput {

    private static final int SLOT_COUNT = 16;
    public final FluidTank[] fluidTankList = new FluidTank[SLOT_COUNT];

    public MTEHatchInputDebug(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, 1, aName, aNameRegional, aTier, getDescriptionArray());
        populateSyncerList();
    }

    public MTEHatchInputDebug(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, 1, aTier, aDescription, aTextures);
        populateSyncerList();
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        // for(int i = 0; i < SLOT_COUNT;i++)
        // {
        // fluidTankList[i].readFromNBT(aNBT);
        // if(fluidTankList[i]!=null && fluidTankList[i].getFluid() != null )System.out.println("Fluid "+i+"
        // :"+fluidTankList[i].getFluid().getLocalizedName());
        // }
        fluidTankList[0].writeToNBT(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        // for(int i = 0; i < SLOT_COUNT;i++)
        // {
        //
        // if(fluidTankList[i]!=null && fluidTankList[i].getFluid() != null )System.out.println("Fluid "+i+"
        // :"+fluidTankList[i].getFluid().getLocalizedName());
        // fluidTankList[i].writeToNBT(aNBT);
        // }
        fluidTankList[0].writeToNBT(aNBT);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_HATCH_IN_DEBUG) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_HATCH_IN_DEBUG) };
    }

    private void populateSyncerList() {
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (fluidTankList[i] == null) fluidTankList[i] = new FluidTank(1);
        }
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

    @Override
    public FluidStack getFillableStack() {
        return this.getFluid();
    }

    @Override
    public FluidStack getFluid() {
        for (FluidTank tank : fluidTankList) {
            if (tank == null) continue;
            FluidStack fluidStack = tank.getFluid();
            if (fluidStack != null) {
                FluidStack copied = fluidStack.copy();
                copied.amount = Integer.MAX_VALUE;
                return copied;
            }
        }
        return null;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchInputDebug(mName, mTier, mDescriptionArray, mTextures);
    }

    private static String[] getDescriptionArray() {
        return new String[] { EnumChatFormatting.GRAY + "Stocks Fluids internally",
            EnumChatFormatting.GRAY + "Configure Fluids in the UI",
            EnumChatFormatting.GRAY + "Configured Fluids will not be consumed in processing",
            EnumChatFormatting.ITALIC + "Who knew it was this easy???", "Author: " + GREEN + BOLD + "Chrom" };
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

package gregtech.common.covers;

import com.google.common.io.ByteArrayDataInput;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiFakeItemButton;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconButton;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.net.GT_Packet_TileEntityCoverNew;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

import static gregtech.api.enums.GT_Values.E;

public class GT_Cover_Fluidfilter extends GT_CoverBehaviorBase<GT_Cover_Fluidfilter.FluidFilterData> {

    // Uses the lower 3 bits of the cover variable, so we have 8 options to work with (0-7)
    private final int FILTER_INPUT_DENY_OUTPUT = 0; //  000
    private final int INVERT_INPUT_DENY_OUTPUT = 1; //  001
    private final int FILTER_INPUT_ANY_OUTPUT = 2;  //  010
    private final int INVERT_INPUT_ANY_OUTPUT = 3;  //  011
    private final int DENY_INPUT_FILTER_OUTPUT = 4; //  100
    private final int DENY_INPUT_INVERT_OUTPUT = 5; //  101
    private final int ANY_INPUT_FILTER_OUTPUT = 6;  //  110
    private final int ANY_INPUT_INVERT_OUTPUT = 7;  //  111

    public GT_Cover_Fluidfilter() {
        super(FluidFilterData.class);
    }

    @Override
    public FluidFilterData createDataObject() {
        return new FluidFilterData(-1, 0);
    }

    @Override
    public FluidFilterData createDataObject(int aLegacyData) {
        return new FluidFilterData(aLegacyData >>> 3, aLegacyData & 0x7);
    }

    @Override
    protected String getDescriptionImpl(byte aSide, int aCoverID, FluidFilterData aCoverVariable, ICoverable aTileEntity) {
        final Fluid fluid = FluidRegistry.getFluid(aCoverVariable.mFluidID);
        if (fluid == null) return E;

        final FluidStack sFluid = new FluidStack(fluid, 1000);
        return (String.format("Filtering Fluid: %s  Mode: %s", sFluid.getLocalizedName(), getFilterMode(aCoverVariable.mFilterMode)));
    }

    @Override
    protected boolean isRedstoneSensitiveImpl(byte aSide, int aCoverID, FluidFilterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return false;
    }

    @Override
    protected FluidFilterData doCoverThingsImpl(byte aSide, byte aInputRedstone, int aCoverID, FluidFilterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return aCoverVariable;
    }

    public String getFilterMode(int aFilterMode) {
        switch (aFilterMode) {
            case FILTER_INPUT_DENY_OUTPUT:
                return (trans("043", "Filter input, Deny output"));
            case INVERT_INPUT_DENY_OUTPUT:
                return (trans("044", "Invert input, Deny output"));
            case FILTER_INPUT_ANY_OUTPUT:
                return (trans("045", "Filter input, Permit any output"));
            case INVERT_INPUT_ANY_OUTPUT:
                return (trans("046", "Invert input, Permit any output"));
            case DENY_INPUT_FILTER_OUTPUT:
                return (trans("219", "Deny input, Filter output"));
            case DENY_INPUT_INVERT_OUTPUT:
                return (trans("220", "Deny input, Invert output"));
            case ANY_INPUT_FILTER_OUTPUT:
                return (trans("221", "Permit any input, Filter output"));
            case ANY_INPUT_INVERT_OUTPUT:
                return (trans("222", "Permit any input, Invert output"));
            default:
                return ("UNKNOWN");
        }

    }

    @Override
    protected FluidFilterData onCoverScrewdriverClickImpl(byte aSide, int aCoverID, FluidFilterData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable.mFilterMode = (aCoverVariable.mFilterMode + (aPlayer.isSneaking() ? -1 : 1)) % 8;
        if (aCoverVariable.mFilterMode < 0) {
            aCoverVariable.mFilterMode = 7;
        }

        GT_Utility.sendChatToPlayer(aPlayer, getFilterMode(aCoverVariable.mFilterMode));

        return aCoverVariable;
    }

    @Override
    protected boolean onCoverRightClickImpl(byte aSide, int aCoverID, FluidFilterData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (
                ((aX > 0.375D) && (aX < 0.625D)) ||
                        ((aSide > 3) && ((aY > 0.375D) && (aY < 0.625D))) ||
                        ((aSide < 2) && ((aZ > 0.375D) && (aZ < 0.625D))) ||
                        (aSide == 2) ||
                        (aSide == 3)
        ) {
            ItemStack tStack = aPlayer.inventory.getCurrentItem();
            if (tStack == null) return true;

            FluidStack tFluid = GT_Utility.getFluidForFilledItem(tStack, true);
            if (tFluid != null) {
                int aFluid = tFluid.getFluidID();
                aCoverVariable.mFluidID = aFluid;
                aTileEntity.setCoverDataAtSide(aSide, aCoverVariable);
                FluidStack sFluid = new FluidStack(FluidRegistry.getFluid(aFluid), 1000);
                GT_Utility.sendChatToPlayer(aPlayer, trans("047", "Filter Fluid: ") + sFluid.getLocalizedName());
            }
            return true;
        }
        return false;
    }

    @Override
    protected boolean letsFluidInImpl(byte aSide, int aCoverID, FluidFilterData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        if (aFluid == null) return true;

        int aFilterMode = aCoverVariable.mFilterMode;
        int aFilterFluid = aCoverVariable.mFluidID;

        if (aFilterMode == DENY_INPUT_FILTER_OUTPUT || aFilterMode == DENY_INPUT_INVERT_OUTPUT)
            return false;
        else if (aFilterMode == ANY_INPUT_FILTER_OUTPUT || aFilterMode == ANY_INPUT_INVERT_OUTPUT)
            return true;
        else if (aFluid.getID() == aFilterFluid)
            return aFilterMode == FILTER_INPUT_DENY_OUTPUT || aFilterMode == FILTER_INPUT_ANY_OUTPUT;
        else
            return aFilterMode == INVERT_INPUT_DENY_OUTPUT || aFilterMode == INVERT_INPUT_ANY_OUTPUT;
    }

    @Override
    protected boolean letsFluidOutImpl(byte aSide, int aCoverID, FluidFilterData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        if (aFluid == null) return true;

        int aFilterMode = aCoverVariable.mFilterMode;
        int aFilterFluid = aCoverVariable.mFluidID;

        if (aFilterMode == FILTER_INPUT_DENY_OUTPUT || aFilterMode == INVERT_INPUT_DENY_OUTPUT)
            return false;
        else if (aFilterMode == FILTER_INPUT_ANY_OUTPUT || aFilterMode == INVERT_INPUT_ANY_OUTPUT)
            return true;
        else if (aFluid.getID() == aFilterFluid)
            return aFilterMode == DENY_INPUT_FILTER_OUTPUT || aFilterMode == ANY_INPUT_FILTER_OUTPUT;
        else
            return aFilterMode == DENY_INPUT_INVERT_OUTPUT || aFilterMode == ANY_INPUT_INVERT_OUTPUT;
    }

    @Override
    protected boolean alwaysLookConnectedImpl(byte aSide, int aCoverID, FluidFilterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected int getTickRateImpl(byte aSide, int aCoverID, FluidFilterData aCoverVariable, ICoverable aTileEntity) {
        return 0;
    }

    /**
     * GUI Stuff
     */

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    protected Object getClientGUIImpl(byte aSide, int aCoverID, FluidFilterData coverData, ICoverable aTileEntity, EntityPlayer aPlayer, World aWorld) {
        return new GT_FluidFilterGUICover(aSide, aCoverID, coverData, aTileEntity);
    }

    private class GT_FluidFilterGUICover extends GT_GUICover {
        private final byte side;
        private final int coverID;
        private final FluidFilterData coverVariable;
        private final GT_GuiFakeItemButton fluidFilterButton;
        protected String fluidFilterName;

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public GT_FluidFilterGUICover(byte aSide, int aCoverID, FluidFilterData aCoverVariable, ICoverable aTileEntity) {
            super(aTileEntity, 176, 107, GT_Utility.intToStack(aCoverID));
            this.side = aSide;
            this.coverID = aCoverID;
            this.coverVariable = aCoverVariable;

            GT_GuiIconButton b;
            b = new GT_GuiIconButton(this, 0, startX + spaceX*0, startY+spaceY*0, GT_GuiIcon.IMPORT).setTooltipText(trans("232","Filter Input"));
            b = new GT_GuiIconButton(this, 1, startX + spaceX*1, startY+spaceY*0, GT_GuiIcon.EXPORT).setTooltipText(trans("233","Filter Output"));
            b = new GT_GuiIconButton(this, 2, startX + spaceX*0, startY+spaceY*2, GT_GuiIcon.BLOCK_INPUT).setTooltipText(trans("234", "Block Output"));
            b = new GT_GuiIconButton(this, 3, startX + spaceX*1, startY+spaceY*2, GT_GuiIcon.ALLOW_INPUT).setTooltipText(trans("235", "Allow Output"));
            b = new GT_GuiIconButton(this, 4, startX + spaceX*0, startY+spaceY*1, GT_GuiIcon.WHITELIST).setTooltipText(trans("236","Whitelist Fluid"));
            b = new GT_GuiIconButton(this, 5, startX + spaceX*1, startY+spaceY*1, GT_GuiIcon.BLACKLIST).setTooltipText(trans("237","Blacklist Fluid"));

            fluidFilterButton = new GT_GuiFakeItemButton(this, startX, startY + spaceY * 3 + 2, GT_GuiIcon.SLOT_DARKGRAY);
            fluidFilterButton.setMimicSlot(true);
        }

        private int getNewFilterMode(int id) {
            switch (id) {
                case 0:
                    return (coverVariable.mFilterMode & 0x3);
                case 1:
                    return (coverVariable.mFilterMode | 0x4);
                case 2:
                    return (coverVariable.mFilterMode & 0x5);
                case 3:
                    return (coverVariable.mFilterMode | 0x2);
                case 4:
                    return (coverVariable.mFilterMode & 0x6);
                case 5:
                    return (coverVariable.mFilterMode | 0x1);
            }
            return coverVariable.mFilterMode;
        }

        private boolean getClickable(int id) {
            switch (id) {
                case 0:
                case 1:
                    return (coverVariable.mFilterMode >> 2 & 0x1) != (id & 0x1);
                case 2:
                case 3:
                    return (coverVariable.mFilterMode >> 1 & 0x1) != (id & 0x1);
                case 4:
                case 5:
                    return (coverVariable.mFilterMode & 0x1) != (id & 0x1);
            }
            return false;
        }


        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            this.fontRendererObj.drawString(trans("238","Filter Direction" ),   startX + spaceX*2, 3+startY+spaceY*0, 0xFF555555);
            this.fontRendererObj.drawString(trans("239", "Filter Type"),        startX + spaceX*2, 3+startY+spaceY*1, 0xFF555555);
            this.fontRendererObj.drawString(trans("240","Block Flow"),          startX + spaceX*2, 3+startY+spaceY*2, 0xFF555555);
            this.fontRendererObj.drawSplitString(fluidFilterName,                                startX + spaceX+3, 4+startY+spaceY*3, gui_width-40 , 0xFF222222);
        }

        @Override
        protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
            updateButtons();
        }

        @Override
        public void buttonClicked(GuiButton btn) {
            if (getClickable(btn.id)) {
                coverVariable.mFilterMode = (byte) getNewFilterMode(btn.id);
                GT_Values.NW.sendToServer(new GT_Packet_TileEntityCoverNew(side, coverID, coverVariable, tile));
            }
            updateButtons();
        }

        private void updateButtons() {
            GT_GuiIconButton b;
            for (Object o : buttonList) {
                if (o instanceof GT_GuiIconButton) {
                    b = (GT_GuiIconButton) o;
                    b.enabled = getClickable(b.id);
                    if (getClickable(1)) { //filtering input
                        if (b.id == 2)
                            b.setTooltipText(trans("219", "Block Output"));
                        else if (b.id == 3)
                            b.setTooltipText(trans("220", "Allow Output"));
                    } else {
                        if (b.id == 2)
                            b.setTooltipText(trans("221", "Block Input"));
                        else if (b.id == 3)
                            b.setTooltipText(trans("222", "Allow Input"));
                    }
                }
            }
            Fluid f = FluidRegistry.getFluid(coverVariable.mFluidID);
            if (f != null) {
                ItemStack item = GT_Utility.getFluidDisplayStack(f);
                if (item != null) {
                    fluidFilterButton.setItem(item);
                    fluidFilterName = item.getDisplayName();
                    return;
                }
            }
            fluidFilterButton.setItem(null);
            fluidFilterName = trans("224", "Filter Empty");
        }
    }

    public static class FluidFilterData implements ISerializableObject {
        private int mFluidID;
        private int mFilterMode;

        public FluidFilterData(int mFluidID, int mFilterMode) {
            this.mFluidID = mFluidID;
            this.mFilterMode = mFilterMode;
        }

        @Override
        @Nonnull
        public ISerializableObject copy() {
            return new FluidFilterData(mFluidID, mFilterMode);
        }

        @Override
        @Nonnull
        public NBTBase saveDataToNBT() {
            NBTTagCompound tNBT = new NBTTagCompound();
            tNBT.setInteger("mFilterMode", mFilterMode);
            tNBT.setString("mFluid", FluidRegistry.getFluid(mFluidID).getName());
            return tNBT;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeByte(mFilterMode).writeInt(mFluidID);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            if (aNBT instanceof NBTTagCompound) {
                NBTTagCompound tNBT = (NBTTagCompound) aNBT;
                mFilterMode = tNBT.getByte("mFilterMod");
                mFluidID = FluidRegistry.getFluidID(tNBT.getString("mFluid"));
            }
        }

        @Override
        @Nonnull
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer) {
            mFilterMode = aBuf.readByte();
            mFluidID = aBuf.readInt();
            return this;
        }
    }
}

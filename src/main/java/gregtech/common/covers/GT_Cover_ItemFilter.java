package gregtech.common.covers;

import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.network.ByteBufUtils;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiFakeItemButton;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconCheckButton;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

import static gregtech.api.util.GT_Utility.intToStack;
import static gregtech.api.util.GT_Utility.moveMultipleItemStacks;

public class GT_Cover_ItemFilter extends GT_CoverBehaviorBase<GT_Cover_ItemFilter.ItemFilterData> {

    private final boolean mExport;

    public GT_Cover_ItemFilter(boolean isExport) {
        super(ItemFilterData.class);
        this.mExport = isExport;
    }

    @Override
    public ItemFilterData createDataObject(int aLegacyData) {
        return new ItemFilterData((aLegacyData & 0x1) == 0, intToStack(aLegacyData >>> 1));
    }

    @Override
    public ItemFilterData createDataObject() {
        return new ItemFilterData();
    }

    @Override
    protected boolean isRedstoneSensitiveImpl(byte aSide, int aCoverID, ItemFilterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return false;
    }

    @Override
    protected ItemFilterData doCoverThingsImpl(byte aSide, byte aInputRedstone, int aCoverID, ItemFilterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        TileEntity tTileEntity = aTileEntity.getTileEntityAtSide(aSide);
        Object fromEntity = mExport ? aTileEntity : tTileEntity,
                toEntity = !mExport ? aTileEntity : tTileEntity;
        byte fromSide = !mExport ? GT_Utility.getOppositeSide(aSide) : aSide,
                toSide = mExport ? GT_Utility.getOppositeSide(aSide) : aSide;

        List<ItemStack> Filter = Collections.singletonList(aCoverVariable.mFilter);

        moveMultipleItemStacks(fromEntity, toEntity, fromSide, toSide, Filter, aCoverVariable.mWhitelist, (byte) 64, (byte) 1, (byte) 64, (byte) 1, 64);

        return aCoverVariable;
    }

    @Override
    protected boolean onCoverRightClickImpl(byte aSide, int aCoverID, ItemFilterData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        ItemStack tStack = aPlayer.inventory.getCurrentItem();
        if (tStack != null) {
            aCoverVariable.mFilter = tStack;
            GT_Utility.sendChatToPlayer(aPlayer, trans("301", "Item Filter: ") + tStack.getDisplayName());
        } else {
            aCoverVariable.mFilter = null;
            GT_Utility.sendChatToPlayer(aPlayer, trans("300", "Filter Cleared!"));
        }
        return true;
    }

    @Override
    protected ItemFilterData onCoverScrewdriverClickImpl(byte aSide, int aCoverID, ItemFilterData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable.mWhitelist = !aCoverVariable.mWhitelist;
        GT_Utility.sendChatToPlayer(aPlayer, aCoverVariable.mWhitelist ? trans("125", "Whitelist Mode") : trans("124", "Blacklist Mode"));
        return aCoverVariable;
    }

    @Override
    protected boolean letsRedstoneGoInImpl(byte aSide, int aCoverID, ItemFilterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsRedstoneGoOutImpl(byte aSide, int aCoverID, ItemFilterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsEnergyInImpl(byte aSide, int aCoverID, ItemFilterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsEnergyOutImpl(byte aSide, int aCoverID, ItemFilterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidInImpl(byte aSide, int aCoverID, ItemFilterData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return false;
    }

    @Override
    protected boolean letsFluidOutImpl(byte aSide, int aCoverID, ItemFilterData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return false;
    }

    @Override
    protected boolean letsItemsInImpl(byte aSide, int aCoverID, ItemFilterData aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsItemsOutImpl(byte aSide, int aCoverID, ItemFilterData aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean alwaysLookConnectedImpl(byte aSide, int aCoverID, ItemFilterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected int getTickRateImpl(byte aSide, int aCoverID, ItemFilterData aCoverVariable, ICoverable aTileEntity) {
        return 1;
    }

    /**
     * GUI Stuff
     */

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    protected Object getClientGUIImpl(byte aSide, int aCoverID, ItemFilterData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, World aWorld) {
        return new GT_Cover_ItemFilter.GUI(aSide, aCoverID, aCoverVariable, aTileEntity);
    }

    public static class ItemFilterData implements ISerializableObject {
        private boolean mWhitelist;
        private ItemStack mFilter;

        public ItemFilterData() {
        }

        public ItemFilterData(boolean mWhitelist, ItemStack mFilter) {
            this.mWhitelist = mWhitelist;
            this.mFilter = mFilter;
        }

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new ItemFilterData(mWhitelist, mFilter);
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setBoolean("mWhitelist", mWhitelist);
            if (mFilter != null)
                tag.setTag("mFilter", mFilter.writeToNBT(new NBTTagCompound()));
            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeBoolean(mWhitelist);
            ByteBufUtils.writeItemStack(aBuf, mFilter);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            NBTTagCompound tag = (NBTTagCompound) aNBT;
            mWhitelist = tag.getBoolean("mWhitelist");
            if (tag.hasKey("mFilter", Constants.NBT.TAG_COMPOUND))
                mFilter = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("mFilter"));
            else
                mFilter = null;
        }

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer) {
            mWhitelist = aBuf.readBoolean();
            mFilter = ISerializableObject.readItemStackFromGreggyByteBuf(aBuf);
            return this;
        }
    }

    private class GUI extends GT_GUICover {
        private final byte side;
        private final int coverID;
        private final GT_GuiIconCheckButton btnMode;
        private final ItemFilterData coverVariable;
        private final GT_GuiFakeItemButton itemFilterButtons;

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public GUI(byte aSide, int aCoverID, ItemFilterData aCoverVariable, ICoverable aTileEntity) {
            super(aTileEntity, 176, 107, GT_Utility.intToStack(aCoverID));
            this.side = aSide;
            this.coverID = aCoverID;
            this.coverVariable = aCoverVariable;

            btnMode = new GT_GuiIconCheckButton(this, 0, startX + spaceX * 0, startY + spaceY * 0, GT_GuiIcon.WHITELIST, GT_GuiIcon.BLACKLIST, trans("125", "Whitelist Mode"), trans("124", "Blacklist Mode"));

            itemFilterButtons = new GT_GuiFakeItemButton(this, startX + spaceX * 0, startY + spaceY * 2, GT_GuiIcon.SLOT_GRAY);
            itemFilterButtons.setMimicSlot(true);
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            this.fontRendererObj.drawString(trans("303", "Filter: "),    startX + spaceX*0, 3+startY+spaceY*1, 0xFF555555);
            this.fontRendererObj.drawString(trans("302", "Check Mode"),  startX + spaceX*2, 3+startY+spaceY*0, 0xFF555555);
        }

        @Override
        protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
            updateButtons();
        }

        @Override
        public void buttonClicked(GuiButton btn) {
            if (btn == btnMode) {
                if (coverVariable.mWhitelist != btnMode.isChecked()) {
                    coverVariable.mWhitelist = btnMode.isChecked();
                    GT_Values.NW.sendToServer(new GT_Packet_TileEntityCoverNew(side, coverID, coverVariable, tile));
                }
            }
            updateButtons();
        }

        private void updateButtons() {
            btnMode.setChecked(coverVariable.mWhitelist);
            itemFilterButtons.setItem(coverVariable.mFilter);
        }
    }
}

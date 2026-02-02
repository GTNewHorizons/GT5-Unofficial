package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.utils.item.LimitingItemStackHandler;
import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizon.gtnhlib.item.ItemStackPredicate;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import cpw.mods.fml.common.network.ByteBufUtils;
import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTByteBuffer;
import gregtech.api.util.GTItemTransfer;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.modes.FilterType;
import gregtech.common.gui.modularui.cover.CoverItemFilterGui;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.gui.mui1.cover.ItemFilterUIFactory;
import io.netty.buffer.ByteBuf;

public class CoverItemFilter extends Cover {

    private final boolean mExport;

    /**
     * This variable is extremely confusing... mWhitelist == false means whitelist mode, true means blacklist mode
     */
    private boolean mWhitelist;
    private ItemStackHandler filter = new LimitingItemStackHandler(1, 1);

    public CoverItemFilter(CoverContext context, boolean isExport, ITexture coverTexture) {
        super(context, coverTexture);
        this.mExport = isExport;
        this.mWhitelist = false;
    }

    public boolean isWhitelist() {
        return mWhitelist;
    }

    public CoverItemFilter setWhitelist(boolean whitelist) {
        this.mWhitelist = whitelist;
        return this;
    }

    public ItemStackHandler getFilter() {
        return filter;
    }

    public CoverItemFilter setFilter(ItemStackHandler filter) {
        this.filter = filter;
        return this;
    }

    public FilterType getFilterType() {
        return mWhitelist ? FilterType.BLACKLIST : FilterType.WHITELIST;
    }

    public void setFilterType(FilterType filterType) {
        mWhitelist = filterType == FilterType.BLACKLIST;
    }

    @Override
    protected void readDataFromNbt(NBTBase nbt) {
        NBTTagCompound tag = (NBTTagCompound) nbt;
        mWhitelist = tag.getBoolean("mWhitelist");
        if (tag.hasKey("mFilter", Constants.NBT.TAG_COMPOUND)) {
            // Old format
            filter = new ItemStackHandler(1);
            filter.setStackInSlot(0, ItemStack.loadItemStackFromNBT(tag.getCompoundTag("mFilter")));
        } else {
            filter.deserializeNBT(tag.getCompoundTag("filter"));
        }
    }

    @Override
    public void readDataFromPacket(ByteArrayDataInput byteData) {
        mWhitelist = byteData.readBoolean();
        filter.deserializeNBT(GTByteBuffer.readCompoundTagFromGreggyByteBuf(byteData));
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("mWhitelist", mWhitelist);
        tag.setTag("filter", filter.serializeNBT());
        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeBoolean(mWhitelist);
        ByteBufUtils.writeTag(byteBuf, filter.serializeNBT());
    }

    public boolean isRedstoneSensitive() {
        return false;
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }

        GTItemTransfer transfer = new GTItemTransfer();

        if (mExport) {
            transfer.push(coverable, coverSide);
        } else {
            transfer.pull(coverable, coverSide);
        }

        ItemStackPredicate predicate = ItemStackPredicate.matches(this.filter.getStackInSlot(0));

        if (mWhitelist) {
            predicate = predicate.negate();
        }

        transfer.setFilter(predicate);

        transfer.setStacksToTransfer(64);

        transfer.transfer();
    }

    @Override
    public boolean onCoverRightClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        final ItemStack tStack = aPlayer.inventory.getCurrentItem();
        if (tStack != null) {
            filter.setStackInSlot(0, tStack);
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("299", "Item Filter: ") + tStack.getDisplayName());
        } else {
            filter.setStackInSlot(0, null);
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("300", "Filter Cleared!"));
        }
        return true;
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        mWhitelist = !mWhitelist;
        GTUtility.sendChatToPlayer(
            aPlayer,
            mWhitelist ? GTUtility.trans("124.1", "Blacklist Mode") : GTUtility.trans("125.1", "Whitelist Mode"));
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return true;
    }

    @Override
    public boolean letsRedstoneGoOut() {
        return true;
    }

    @Override
    public boolean letsEnergyIn() {
        return true;
    }

    @Override
    public boolean letsEnergyOut() {
        return true;
    }

    @Override
    public boolean letsItemsIn(int aSlot) {
        return true;
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return true;
    }

    @Override
    public boolean alwaysLookConnected() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return 1;
    }

    // GUI stuff

    @Override
    protected @NotNull CoverBaseGui<?> getCoverGui() {
        return new CoverItemFilterGui(this);
    }

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new ItemFilterUIFactory(buildContext).createWindow();
    }

}

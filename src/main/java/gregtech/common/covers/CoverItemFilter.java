package gregtech.common.covers;

import static gregtech.api.util.GTUtility.moveMultipleItemStacks;

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import cpw.mods.fml.common.network.ByteBufUtils;
import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTByteBuffer;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.mui1.cover.ItemFilterUIFactory;
import io.netty.buffer.ByteBuf;

public class CoverItemFilter extends Cover {

    private final boolean mExport;
    private boolean mWhitelist;
    private ItemStack mFilter;

    public CoverItemFilter(CoverContext context, boolean isExport, ITexture coverTexture) {
        super(context, coverTexture);
        this.mExport = isExport;
        initializeData(context.getCoverInitializer());
    }

    public boolean isWhitelist() {
        return mWhitelist;
    }

    public CoverItemFilter setWhitelist(boolean whitelist) {
        this.mWhitelist = whitelist;
        return this;
    }

    public ItemStack getFilter() {
        return mFilter;
    }

    public CoverItemFilter setFilter(ItemStack filter) {
        this.mFilter = filter;
        return this;
    }

    @Override
    protected void initializeData() {
        mWhitelist = false;
        mFilter = null;
    }

    @Override
    protected void readFromNbt(NBTBase nbt) {
        NBTTagCompound tag = (NBTTagCompound) nbt;
        mWhitelist = tag.getBoolean("mWhitelist");
        if (tag.hasKey("mFilter", Constants.NBT.TAG_COMPOUND))
            mFilter = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("mFilter"));
        else mFilter = null;
    }

    @Override
    public void readFromPacket(ByteArrayDataInput byteData) {
        mWhitelist = byteData.readBoolean();
        mFilter = GTByteBuffer.readItemStackFromGreggyByteBuf(byteData);
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("mWhitelist", mWhitelist);
        if (mFilter != null) tag.setTag("mFilter", mFilter.writeToNBT(new NBTTagCompound()));
        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeBoolean(mWhitelist);
        ByteBufUtils.writeItemStack(byteBuf, mFilter);
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
        final TileEntity tTileEntity = coverable.getTileEntityAtSide(coverSide);
        final Object fromEntity = mExport ? coverable : tTileEntity;
        final Object toEntity = !mExport ? coverable : tTileEntity;
        final ForgeDirection fromSide = !mExport ? coverSide.getOpposite() : coverSide;
        final ForgeDirection toSide = mExport ? coverSide.getOpposite() : coverSide;

        final List<ItemStack> filter = Collections.singletonList(mFilter);

        moveMultipleItemStacks(
            fromEntity,
            toEntity,
            fromSide,
            toSide,
            filter,
            mWhitelist,
            (byte) 64,
            (byte) 1,
            (byte) 64,
            (byte) 1,
            64);
    }

    @Override
    public boolean onCoverRightClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        final ItemStack tStack = aPlayer.inventory.getCurrentItem();
        if (tStack != null) {
            mFilter = tStack;
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("299", "Item Filter: ") + tStack.getDisplayName());
        } else {
            mFilter = null;
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("300", "Filter Cleared!"));
        }
        return true;
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        mWhitelist = !mWhitelist;
        GTUtility.sendChatToPlayer(
            aPlayer,
            mWhitelist ? GTUtility.trans("125.1", "Whitelist Mode") : GTUtility.trans("124.1", "Blacklist Mode"));
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
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new ItemFilterUIFactory(buildContext).createWindow();
    }

}

package gtPlusPlus.xmod.gregtech.common.tileentities.storage.creative;

import java.util.OptionalInt;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.gtnhlib.capability.item.AbstractInventorySourceIterator;
import com.gtnewhorizon.gtnhlib.capability.item.IItemIO;
import com.gtnewhorizon.gtnhlib.capability.item.IItemSink;
import com.gtnewhorizon.gtnhlib.capability.item.IItemSource;
import com.gtnewhorizon.gtnhlib.capability.item.InventorySourceIterator;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.util.sys.KeyboardUtils;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.MTETieredChest;

public class MTEInfiniteItemHolder extends MTETieredChest {

    public MTEInfiniteItemHolder(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEInfiniteItemHolder(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.getWorld().isRemote) {
            return false;
        }

        if (!KeyboardUtils.isShiftKeyDown()) {
            if (this.mItemStack == null) {
                if (aPlayer.getHeldItem() != null) {
                    this.mItemStack = aPlayer.getHeldItem()
                        .copy();
                    this.mItemCount = Short.MAX_VALUE;
                    aPlayer.setCurrentItemOrArmor(0, null);
                    GTUtility.sendChatToPlayer(
                        aPlayer,
                        "Now holding " + this.mItemStack.getDisplayName() + " x" + Short.MAX_VALUE + ".");
                    return true;
                }
            } else {
                if (aPlayer.getHeldItem() == null) {
                    aPlayer.entityDropItem(mItemStack, 1);
                    this.mItemStack = null;
                    this.mItemCount = 0;
                    GTUtility.sendChatToPlayer(aPlayer, "Emptying.");
                    return true;
                }
            }
        }

        GTUtility.sendChatToPlayer(
            aPlayer,
            "Currently holding: " + (this.mItemStack != null ? this.mItemStack.getDisplayName() : "Nothing")
                + " x"
                + this.mItemCount);
        return true;
        // return super.onRightclick(aBaseMetaTileEntity, aPlayer);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (mItemStack != null) {
            setItemCount(0);
        }
        super.onPostTick(aBaseMetaTileEntity, aTimer);
    }

    @Override
    public void setItemCount(int aCount) {
        super.setItemCount(Short.MAX_VALUE);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return true;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEInfiniteItemHolder(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    protected IItemSink getItemSink(ForgeDirection side) {
        return new ItemIO();
    }

    @Override
    protected IItemSource getItemSource(ForgeDirection side) {
        return new ItemIO();
    }

    @Override
    protected IItemIO getItemIO(ForgeDirection side) {
        return new ItemIO();
    }

    class ItemIO implements IItemIO {

        @Override
        public ItemStack store(ItemStack stack) {
            return null;
        }

        @Override
        public OptionalInt getStoredAmount(@Nullable ItemStack stack) {
            return GTUtility.areStacksEqual(stack, mItemStack) ? OptionalInt.of(Integer.MAX_VALUE) : ZERO;
        }

        @Override
        public @NotNull InventorySourceIterator iterator() {
            return new AbstractInventorySourceIterator(new int[] { 0 }) {

                @Override
                protected ItemStack getStackInSlot(int slot) {
                    if (slot != 0) return null;

                    return GTUtility.copyAmountUnsafe(Integer.MAX_VALUE, mItemStack);
                }

                @Override
                protected void setInventorySlotContents(int slot, ItemStack stack) {
                    // do nothing
                }
            };
        }
    }
}

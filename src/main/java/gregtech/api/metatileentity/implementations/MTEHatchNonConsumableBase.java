package gregtech.api.metatileentity.implementations;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;

public abstract class MTEHatchNonConsumableBase extends MTEHatch {

    private ItemStack itemStack = null;
    private int itemCount = 0;

    public MTEHatchNonConsumableBase(int ID, String name, String nameRegional, int tier, String description) {
        super(ID, name, nameRegional, tier, 3, description);
    }

    public MTEHatchNonConsumableBase(String name, int tier, String[] description, ITexture[][][] textures) {
        super(name, tier, 3, description, textures);
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    protected int clientItemCount;

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.PICTURE_SCREEN_BLACK)
                .setPos(7, 16)
                .setSize(71, 45))
            .widget(
                new SlotWidget(inventoryHandler, 0)
                    .setBackground(getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_IN)
                    .setPos(79, 16))
            .widget(
                new SlotWidget(inventoryHandler, 1).setAccess(true, false)
                    .setBackground(getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_OUT)
                    .setPos(79, 52))
            .widget(
                SlotWidget.phantom(inventoryHandler, 2)
                    .disableInteraction()
                    .setBackground(GTUITextures.TRANSPARENT)
                    .setPos(59, 42))
            .widget(
                new TextWidget("Item Amount").setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(10, 20))
            .widget(
                new TextWidget().setStringSupplier(() -> numberFormat.format(clientItemCount))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(10, 30))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> itemCount, value -> clientItemCount = value));

    }

    protected ItemStack getItemStack() {
        return itemStack;
    }

    protected void setItemStack(ItemStack stack) {
        itemStack = stack;
    }

    protected int getItemCount() {
        return itemCount;
    }

    @Override
    public void setItemCount(int amount) {
        itemCount = amount;
    }

    protected abstract int getItemCapacity();

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {

        if (getBaseMetaTileEntity().isServerSide() && getBaseMetaTileEntity().isAllowedToWork()) {
            if ((getItemCount() <= 0)) {
                setItemStack(null);
                setItemCount(0);
            }
            if (getItemStack() == null && mInventory[0] != null) {
                setItemStack(mInventory[0].copy());
            }
            int count = getItemCount();
            ItemStack stack = getItemStack();
            int savedCount = count;

            if ((mInventory[0] != null) && ((count < getItemCapacity()))
                && GTUtility.areStacksEqual(mInventory[0], stack)) {
                count += mInventory[0].stackSize;
                if (count <= getItemCapacity()) {
                    mInventory[0] = null;
                } else {
                    mInventory[0].stackSize = (count - getItemCapacity());
                    count = getItemCapacity();
                }
            }
            if (mInventory[1] == null && stack != null) {
                mInventory[1] = stack.copy();
                mInventory[1].stackSize = Math.min(stack.getMaxStackSize(), count);
                count -= mInventory[1].stackSize;
            } else if ((count > 0) && GTUtility.areStacksEqual(mInventory[1], stack)
                && mInventory[1].getMaxStackSize() > mInventory[1].stackSize) {
                    int tmp = Math.min(count, mInventory[1].getMaxStackSize() - mInventory[1].stackSize);
                    mInventory[1].stackSize += tmp;
                    count -= tmp;
                }
            setItemCount(count);
            if (stack != null) {
                mInventory[2] = stack.copy();
                mInventory[2].stackSize = Math.min(stack.getMaxStackSize(), count);
            } else {
                mInventory[2] = null;
            }

            // notifyListeners(count - savedCount, stack);
            if (count != savedCount) getBaseMetaTileEntity().markDirty();
        }
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GTUIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("itemCount", getItemCount());
        if (getItemStack() != null) aNBT.setTag("itemStack", getItemStack().writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey("itemCount")) setItemCount(aNBT.getInteger("itemCount"));
        if (aNBT.hasKey("itemStack"))
            setItemStack(ItemStack.loadItemStackFromNBT((NBTTagCompound) aNBT.getTag("itemStack")));
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        // if (GTValues.disableDigitalChestsExternalAccess && hasActiveMEConnection()) return false;
        return aIndex == 1;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        // if (GTValues.disableDigitalChestsExternalAccess && hasActiveMEConnection()) return false;
        if (aIndex != 0) return false;
        if ((mInventory[0] != null && !GTUtility.areStacksEqual(mInventory[0], aStack))) return false;
        if (getItemStack() == null) return mInventory[1] == null || GTUtility.areStacksEqual(mInventory[1], aStack);
        return GTUtility.areStacksEqual(getItemStack(), aStack);
    }

    @Override
    public ItemStack[] getStoredItemData() {
        return mInventory;
    }
}

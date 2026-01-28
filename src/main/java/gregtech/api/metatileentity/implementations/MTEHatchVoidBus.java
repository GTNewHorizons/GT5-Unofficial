package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.ITEM_VOID_SIGN;

import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;

import gregtech.api.enums.OutputBusType;
import gregtech.api.gui.widgets.PhantomItemButton;
import gregtech.api.interfaces.IOutputBus;
import gregtech.api.interfaces.IOutputBusTransaction;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTSplit;
import gregtech.api.util.GTUtility;

@IMetaTileEntity.SkipGenerateDescription
public class MTEHatchVoidBus extends MTEHatchOutputBus {

    private static final String DATA_STICK_DATA_TYPE = "voidBusFilter";
    private static final String LOCKED_ITEMS_NBT_KEY = "lockedItems";

    private final ItemStack[] lockedItems = new ItemStack[4];
    private final IItemHandlerModifiable lockedInventoryHandler = new ItemStackHandler(lockedItems);

    public MTEHatchVoidBus(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 1, null, 0);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(ITEM_VOID_SIGN) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(ITEM_VOID_SIGN) };
    }

    public MTEHatchVoidBus(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchVoidBus(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    protected void setDataStickName(ItemStack dataStick) {
        dataStick.setStackDisplayName("Void Bus Configuration");
    }

    @Override
    public NBTTagCompound getCopiedData(EntityPlayer player) {
        final NBTTagCompound nbt = new NBTTagCompound();
        final NBTTagList lockedItemList = new NBTTagList();

        nbt.setString("type", DATA_STICK_DATA_TYPE);
        for (int i = 0; i < lockedItems.length; i++) {
            if (lockedItems[i] == null) continue;
            NBTTagCompound itemTag = new NBTTagCompound();
            itemTag.setByte("Slot", (byte) i);
            lockedItems[i].writeToNBT(itemTag);
            lockedItemList.appendTag(itemTag);
        }

        nbt.setTag(LOCKED_ITEMS_NBT_KEY, lockedItemList);
        return nbt;
    }

    @Override
    public boolean pasteCopiedData(EntityPlayer player, NBTTagCompound nbt) {
        if (nbt == null || !DATA_STICK_DATA_TYPE.equals(nbt.getString("type"))) return false;
        if (!nbt.hasKey(LOCKED_ITEMS_NBT_KEY)) return false;

        // Clear current configuration
        Arrays.fill(lockedItems, null);

        NBTTagList lockedItemList = nbt.getTagList(LOCKED_ITEMS_NBT_KEY, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < lockedItemList.tagCount(); i++) {
            NBTTagCompound itemTag = lockedItemList.getCompoundTagAt(i);
            int slot = itemTag.getByte("Slot");
            if (slot < lockedItems.length) {
                lockedItems[slot] = ItemStack.loadItemStackFromNBT(itemTag);
            }
        }
        return true;
    }

    @Override
    public String getCopiedDataIdentifier(EntityPlayer player) {
        return DATA_STICK_DATA_TYPE;
    }

    @Override
    public boolean pushOutputInventory() {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        NBTTagList lockedItemList = new NBTTagList();
        for (int i = 0; i < lockedItems.length; i++) {
            if (lockedItems[i] == null) continue;
            NBTTagCompound itemTag = new NBTTagCompound();
            itemTag.setByte("Slot", (byte) i);
            lockedItems[i].writeToNBT(itemTag);
            lockedItemList.appendTag(itemTag);
        }
        aNBT.setTag(LOCKED_ITEMS_NBT_KEY, lockedItemList);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        NBTTagList lockedItemList = aNBT.getTagList(LOCKED_ITEMS_NBT_KEY, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < lockedItemList.tagCount(); i++) {
            NBTTagCompound itemTag = lockedItemList.getCompoundTagAt(i);
            int slot = itemTag.getByte("Slot");
            if (slot < lockedItems.length) {
                lockedItems[slot] = ItemStack.loadItemStackFromNBT(itemTag);
            }
        }
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        if (lockedInventoryHandler == null) return;

        builder.widget(
            SlotGroup.ofItemHandler(lockedInventoryHandler, 2)
                .startFromSlot(0)
                .endAtSlot(3)
                .background(PhantomItemButton.FILTER_BACKGROUND)
                .phantom(true)
                .build()
                .setPos(70, 25));
    }

    @Override
    public boolean isLocked() {
        for (ItemStack lockedItem : lockedItems) {
            if (lockedItem != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isFiltered() {
        return true;
    }

    @Override
    public boolean isFilteredToItem(GTUtility.ItemId id) {
        for (ItemStack lockedItem : lockedItems) {
            if (lockedItem != null && id.matches(lockedItem)) return true;
        }

        return false;
    }

    @Override
    public OutputBusType getBusType() {
        return OutputBusType.Void;
    }

    @Override
    public boolean storePartial(ItemStack stack, boolean simulate) {
        for (ItemStack lockedItem : lockedItems) {
            if (lockedItem != null && lockedItem.isItemEqual(stack)) {
                stack.stackSize = 0;
                return true;
            }
        }
        return false;
    }

    @Override
    public IOutputBusTransaction createTransaction() {
        return new VoidingTransaction();
    }

    class VoidingTransaction implements IOutputBusTransaction {

        @Override
        public IOutputBus getBus() {
            return MTEHatchVoidBus.this;
        }

        @Override
        public boolean hasAvailableSpace() {
            return true;
        }

        @Override
        public boolean storePartial(GTUtility.ItemId id, ItemStack stack) {
            for (ItemStack lockedItem : lockedItems) {
                if (lockedItem != null && lockedItem.isItemEqual(stack)) {
                    stack.stackSize = 0;
                    return true;
                }
            }
            return false;
        }

        @Override
        public void completeItem(GTUtility.ItemId id) {
            // do nothing
        }

        @Override
        public void commit() {
            // do nothing
        }
    }

    @Override
    public String[] getDescription() {
        return GTSplit.splitLocalized("gt.blockmachines.output_bus_void.desc");
    }
}

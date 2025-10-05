package gregtech.common.covers;

import static gregtech.api.modularui2.GTGuis.GLOBAL_SWITCH_MUI2;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.utils.item.LimitingItemStackHandler;
import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import cpw.mods.fml.common.network.ByteBufUtils;
import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTByteBuffer;
import gregtech.common.covers.gui.CoverChestGui;
import gregtech.common.covers.gui.CoverGui;
import gregtech.common.gui.mui1.cover.ChestUIFactory;
import io.netty.buffer.ByteBuf;

public class CoverChest extends Cover {

    private final int slots;
    private final int stackSizeLimit = 1;
    private LimitingItemStackHandler items;
    // TODO: REMOVE AFTER 2.9
    private LegacyLimitingItemStackHandler legacyItems;
    private boolean firstTick;

    public CoverChest(CoverContext context, int slots, ITexture coverTexture) {
        super(context, coverTexture);
        if (slots <= 0) throw new IllegalArgumentException("slots must be greater than 0");
        this.slots = slots;
        this.legacyItems = new LegacyLimitingItemStackHandler(slots, stackSizeLimit);
        this.items = new LimitingItemStackHandler(slots, stackSizeLimit);
    }

    public int getSlotCount() {
        return slots;
    }

    public IItemHandlerModifiable getLegacyItems() {
        return this.legacyItems;
    }

    public LimitingItemStackHandler getItems() {
        return items;
    }

    @Override
    protected void readDataFromNbt(NBTBase nbt) {
        if (!(nbt instanceof NBTTagCompound)) return;
        items.deserializeNBT((NBTTagCompound) nbt);
        legacyItems.deserializeNBT((NBTTagCompound) nbt);
        firstTick = true;
    }

    @Override
    public void readDataFromPacket(ByteArrayDataInput byteData) {
        if (GLOBAL_SWITCH_MUI2) {
            items.deserializeNBT(GTByteBuffer.readCompoundTagFromGreggyByteBuf(byteData));
        } else {
            legacyItems.deserializeNBT(GTByteBuffer.readCompoundTagFromGreggyByteBuf(byteData));
        }
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        return GLOBAL_SWITCH_MUI2 ? items.serializeNBT() : legacyItems.serializeNBT();
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        if (GLOBAL_SWITCH_MUI2) {
            ByteBufUtils.writeTag(byteBuf, items.serializeNBT());
        } else {
            ByteBufUtils.writeTag(byteBuf, legacyItems.serializeNBT());
        }
    }

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public boolean allowsCopyPasteTool() {
        return false;
    }

    @Override
    public boolean allowsTickRateAddition() {
        return false;
    }

    @Override
    public void onCoverRemoval() {
        ICoverable iCoverable = coveredTile.get();
        if (iCoverable == null || iCoverable.getWorld().isRemote) return;
        dropAll(iCoverable, coverSide);
    }

    private void dropAll(ICoverable coverable, ForgeDirection direction) {
        if (!GLOBAL_SWITCH_MUI2) {
            dropAllLegacy(coverable, direction);
            return;
        }

        for (int i = 0; i < items.getSlots(); i++) {
            ItemStack tItem = items.getStackInSlot(i);
            if (tItem == null) {
                continue;
            }
            dropItem(coverable, direction, tItem);
            items.setStackInSlot(i, null);
        }
    }

    private void dropAllLegacy(ICoverable coverable, ForgeDirection direction) {
        for (int i = 0; i < legacyItems.getSlots(); i++) {
            ItemStack tItem = legacyItems.getStackInSlot(i);
            if (tItem == null) {
                continue;
            }
            dropItem(coverable, direction, tItem);
            legacyItems.setStackInSlot(i, null);
        }
    }

    @Override
    public int getMinimumTickRate() {
        return firstTick ? 1 : 0;
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        // migrate slots. mostly needed while in development. still can be useful if we ever resize the inventory in the
        // future
        if (!GLOBAL_SWITCH_MUI2) {
            doCoverThingsLegacy(aInputRedstone, aTimer, coverable);
        }
        if (items.getSlots() != slots) {
            if (items.getSlots() > slots) {
                for (int i = slots; i < items.getSlots(); i++) {
                    ItemStack item = items.getStackInSlot(i);
                    if (item != null) {
                        dropItem(coverable, coverSide, item);
                    }
                }
            }
            this.items = new LimitingItemStackHandler(slots, stackSizeLimit);
            int toCopy = Math.min(items.getSlots(), items.getSlots());
            for (int i = 0; i < toCopy; i++) {
                items.setStackInSlot(i, items.getStackInSlot(i));
            }
        }
        firstTick = false;
    }

    private void doCoverThingsLegacy(byte aInputRedstone, long aTimer, ICoverable coverable) {
        if (legacyItems.getSlots() != slots) {
            if (legacyItems.getSlots() > slots) {
                for (int i = slots; i < legacyItems.getSlots(); i++) {
                    ItemStack item = legacyItems.getStackInSlot(i);
                    if (item != null) {
                        dropItem(coverable, coverSide, item);
                    }
                }
            }
            this.legacyItems = new LegacyLimitingItemStackHandler(slots, stackSizeLimit);
            int toCopy = Math.min(legacyItems.getSlots(), legacyItems.getSlots());
            for (int i = 0; i < toCopy; i++) {
                legacyItems.setStackInSlot(i, legacyItems.getStackInSlot(i));
            }
        }
    }

    @Override
    protected @NotNull CoverGui<?> getCoverGui() {
        return new CoverChestGui(this);
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new ChestUIFactory(buildContext).createWindow();
    }

    private static class LegacyLimitingItemStackHandler extends ItemStackHandler {

        private final int slotLimit;

        private LegacyLimitingItemStackHandler(int slots, int slotLimit) {
            super(slots);
            this.slotLimit = slotLimit;
        }

        @Override
        public int getSlotLimit(int slot) {
            return slotLimit;
        }
    }

    private static void dropItem(ICoverable coverable, ForgeDirection direction, ItemStack tItem) {
        final EntityItem tItemEntity = new EntityItem(
            coverable.getWorld(),
            coverable.getXCoord() + XSTR_INSTANCE.nextFloat() * 0.8F + 0.1F + direction.offsetX,
            coverable.getYCoord() + XSTR_INSTANCE.nextFloat() * 0.8F + 0.1F + direction.offsetY,
            coverable.getZCoord() + XSTR_INSTANCE.nextFloat() * 0.8F + 0.1F + direction.offsetZ,
            new ItemStack(tItem.getItem(), tItem.stackSize, tItem.getItemDamage()));
        if (tItem.hasTagCompound()) {
            tItemEntity.getEntityItem()
                .setTagCompound(
                    (NBTTagCompound) tItem.getTagCompound()
                        .copy());
        }
        tItemEntity.motionX = (XSTR_INSTANCE.nextGaussian() * 0.05D);
        tItemEntity.motionY = (XSTR_INSTANCE.nextGaussian() * 0.05D + 0.2D);
        tItemEntity.motionZ = (XSTR_INSTANCE.nextGaussian() * 0.05D);
        tItemEntity.hurtResistantTime = 999999;
        tItemEntity.lifespan = 60000;
        coverable.getWorld()
            .spawnEntityInWorld(tItemEntity);
    }
}

package gregtech.common.covers;

import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import cpw.mods.fml.common.network.ByteBufUtils;
import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTByteBuffer;
import gregtech.common.gui.modularui.cover.CoverChestGui;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.gui.mui1.cover.ChestUIFactory;
import io.netty.buffer.ByteBuf;

public class CoverChest extends Cover {

    private final int slots;
    private final int stackSizeLimit = 1;
    private LegacyLimitingItemStackHandler items;
    private boolean firstTick;

    public CoverChest(CoverContext context, int slots, ITexture coverTexture) {
        super(context, coverTexture);
        if (slots <= 0) throw new IllegalArgumentException("slots must be greater than 0");
        this.slots = slots;
        this.items = new LegacyLimitingItemStackHandler(slots, stackSizeLimit);
    }

    public int getSlotCount() {
        return slots;
    }

    public LegacyLimitingItemStackHandler getItems() {
        return items;
    }

    @Override
    protected void readDataFromNbt(NBTBase nbt) {
        if (!(nbt instanceof NBTTagCompound)) return;
        items.deserializeNBT((NBTTagCompound) nbt);
        firstTick = true;
    }

    @Override
    public void readDataFromPacket(ByteArrayDataInput byteData) {
        items.deserializeNBT(GTByteBuffer.readCompoundTagFromGreggyByteBuf(byteData));
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        return items.serializeNBT();
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        ByteBufUtils.writeTag(byteBuf, items.serializeNBT());
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
        for (int i = 0; i < items.getSlots(); i++) {
            ItemStack tItem = items.getStackInSlot(i);
            if (tItem == null) {
                continue;
            }
            dropItem(coverable, direction, tItem);
            items.setStackInSlot(i, null);
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
        if (items.getSlots() != slots) {
            if (items.getSlots() > slots) {
                for (int i = slots; i < items.getSlots(); i++) {
                    ItemStack item = items.getStackInSlot(i);
                    if (item != null) {
                        dropItem(coverable, coverSide, item);
                    }
                }
            }
            this.items = new LegacyLimitingItemStackHandler(slots, stackSizeLimit);
            int toCopy = Math.min(items.getSlots(), items.getSlots());
            for (int i = 0; i < toCopy; i++) {
                items.setStackInSlot(i, items.getStackInSlot(i));
            }
        }
        firstTick = false;
    }

    @Override
    protected @NotNull CoverBaseGui<?> getCoverGui() {
        return new CoverChestGui(this);
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new ChestUIFactory(buildContext).createWindow();
    }

    private static class LegacyLimitingItemStackHandler extends ItemStackHandler implements IItemHandlerModifiable {

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

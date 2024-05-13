package gregtech.common.covers;

import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;

import cpw.mods.fml.common.network.ByteBufUtils;
import gregtech.api.gui.modularui.GT_CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import io.netty.buffer.ByteBuf;

public class GT_Cover_Chest extends GT_CoverBehaviorBase<GT_Cover_Chest.ChestInventory> {

    private final int slots;
    private final int stackSizeLimit = 1;

    public GT_Cover_Chest(int slots, ITexture coverTexture) {
        super(ChestInventory.class, coverTexture);
        if (slots <= 0) throw new IllegalArgumentException("slots must be greater than 0");
        this.slots = slots;
    }

    @Override
    public ChestInventory createDataObject(int aLegacyData) {
        return new ChestInventory(slots, stackSizeLimit);
    }

    @Override
    public ChestInventory createDataObject() {
        return new ChestInventory(slots, stackSizeLimit);
    }

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public boolean isSimpleCover() {
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
    protected void onDroppedImpl(ForgeDirection side, int aCoverID, ChestInventory aCoverVariable,
        ICoverable aTileEntity) {
        if (aTileEntity.getWorld().isRemote) return;
        aCoverVariable.dropAll(aTileEntity, side);
    }

    @Override
    protected void onBaseTEDestroyedImpl(ForgeDirection side, int aCoverID, ChestInventory aCoverVariable,
        ICoverable aTileEntity) {
        if (aTileEntity.getWorld().isRemote) return;
        aCoverVariable.dropAll(aTileEntity, ForgeDirection.UNKNOWN);
    }

    @Override
    public ModularWindow createWindow(GT_CoverUIBuildContext buildContext) {
        return new ChestUIFactory(buildContext).createWindow();
    }

    public class ChestUIFactory extends UIFactory {

        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public ChestUIFactory(GT_CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @Override
        protected int getGUIHeight() {
            return slots / 3 * 18 + 12;
        }

        @Override
        protected void addTitleToUI(ModularWindow.Builder builder) {}

        @Override
        protected int getGUIWidth() {
            return 18 * 3 + 22;
        }

        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            CoverDataControllerWidget<ChestInventory> w = new CoverDataControllerWidget<>(
                this::getCoverData,
                this::setCoverData,
                GT_Cover_Chest.this);
            ChestInventory d = getCoverData();
            LimitingItemStackHandler h;
            if (d == null) {
                // ???
                return;
            }
            h = d.items;
            w.addChild(
                SlotGroup.ofItemHandler(h, 3)
                    .build()
                    .setPos(4, 4));
            builder.widget(w);

            builder.setPos(
                (p, t) -> Alignment.Center
                    .getAlignedPos(new Size(t.getPos().x, p.height), new Size(getGUIWidth(), getGUIHeight())));
        }
    }

    private static class LimitingItemStackHandler extends ItemStackHandler {

        private final int slotLimit;

        private LimitingItemStackHandler(int slots, int slotLimit) {
            super(slots);
            this.slotLimit = slotLimit;
        }

        @Override
        public int getSlotLimit(int slot) {
            return slotLimit;
        }
    }

    public static class ChestInventory implements ISerializableObject {

        final LimitingItemStackHandler items;

        public ChestInventory(int slots, int stackSize) {
            items = new LimitingItemStackHandler(slots, stackSize);
        }

        @NotNull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, @Nullable EntityPlayerMP aPlayer) {
            items.deserializeNBT(ISerializableObject.readCompoundTagFromGreggyByteBuf(aBuf));
            return this;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            ByteBufUtils.writeTag(aBuf, items.serializeNBT());
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            if (!(aNBT instanceof NBTTagCompound)) return;
            items.deserializeNBT((NBTTagCompound) aNBT);
        }

        @NotNull
        @Override
        public NBTBase saveDataToNBT() {
            return items.serializeNBT();
        }

        @NotNull
        @Override
        public ISerializableObject copy() {
            ChestInventory copy = new ChestInventory(items.getSlots(), items.getSlotLimit(0));
            for (int i = 0; i < items.getSlots(); i++) {
                copy.items.setStackInSlot(i, items.getStackInSlot(i));
            }
            return copy;
        }

        public void dropAll(ICoverable coverable, ForgeDirection direction) {
            for (int i = 0; i < items.getSlots(); i++) {
                ItemStack tItem = items.getStackInSlot(i);
                if (tItem == null) {
                    continue;
                }
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
                items.setStackInSlot(i, null);
            }
        }
    }
}

package gregtech.common.covers;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FLUID_STORAGE_MONITOR0;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FLUID_STORAGE_MONITOR1;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FLUID_STORAGE_MONITOR10;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FLUID_STORAGE_MONITOR11;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FLUID_STORAGE_MONITOR12;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FLUID_STORAGE_MONITOR13;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FLUID_STORAGE_MONITOR14;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FLUID_STORAGE_MONITOR2;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FLUID_STORAGE_MONITOR3;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FLUID_STORAGE_MONITOR4;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FLUID_STORAGE_MONITOR5;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FLUID_STORAGE_MONITOR6;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FLUID_STORAGE_MONITOR7;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FLUID_STORAGE_MONITOR8;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FLUID_STORAGE_MONITOR9;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.ITextureBuilder;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.tileentities.storage.MTEDigitalTankBase;
import io.netty.buffer.ByteBuf;

/**
 * TODO: Implement overlay rendering only with
 * {@link CoverBehaviorBase#getOverlayTexture()}
 */
public class CoverFluidStorageMonitor extends CoverBehaviorBase<CoverFluidStorageMonitor.FluidStorageData> {

    private static final IIconContainer[] icons = new IIconContainer[] { OVERLAY_FLUID_STORAGE_MONITOR0,
        OVERLAY_FLUID_STORAGE_MONITOR1, OVERLAY_FLUID_STORAGE_MONITOR2, OVERLAY_FLUID_STORAGE_MONITOR3,
        OVERLAY_FLUID_STORAGE_MONITOR4, OVERLAY_FLUID_STORAGE_MONITOR5, OVERLAY_FLUID_STORAGE_MONITOR6,
        OVERLAY_FLUID_STORAGE_MONITOR7, OVERLAY_FLUID_STORAGE_MONITOR8, OVERLAY_FLUID_STORAGE_MONITOR9,
        OVERLAY_FLUID_STORAGE_MONITOR10, OVERLAY_FLUID_STORAGE_MONITOR11, OVERLAY_FLUID_STORAGE_MONITOR12,
        OVERLAY_FLUID_STORAGE_MONITOR13, OVERLAY_FLUID_STORAGE_MONITOR14, };

    public CoverFluidStorageMonitor(CoverContext context) {
        super(context, FluidStorageData.class, null);
    }

    @Override
    protected FluidStorageData initializeData() {
        return new FluidStorageData();
    }

    @Override
    public FluidStorageData doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return coverData;
        }
        final FluidTankInfo[] tanks = getValidFluidTankInfosForDisplay(coverable, coverData.coverSide);
        if (tanks == null) {
            return coverData.disable()
                .issueCoverUpdateIfNeeded(coverable, coverSide);
        }
        assert 0 < tanks.length;

        if (coverData.slot < 0 || tanks.length <= coverData.slot) {
            coverData.setSlot(0);
        }

        final FluidTankInfo tank = tanks[coverData.slot];
        if (tank == null) {
            return coverData.setNullTank()
                .issueCoverUpdateIfNeeded(coverable, coverSide);
        }

        return coverData.setFluid(tank.fluid)
            .setScale(getTankScale(tank))
            .issueCoverUpdateIfNeeded(coverable, coverSide);
    }

    @Override
    public ITexture getOverlayTexture() {
        return getSpecialFaceTexture();
    }

    @Override
    public ITexture getSpecialFaceTexture() {
        if (coverData.slot == -1 || coverData.fluid == null || coverData.scale == 0) {
            return TextureFactory.of(OVERLAY_FLUID_STORAGE_MONITOR0);
        }
        final IIconContainer fluidIcon = new IIconContainer() {

            @Override
            public IIcon getIcon() {
                return coverData.fluid.getStillIcon();
            }

            @Override
            public IIcon getOverlayIcon() {
                return null;
            }

            @Override
            public ResourceLocation getTextureFile() {
                return TextureMap.locationBlocksTexture;
            }
        };

        final short[] fluidRGBA = colorToRGBA(coverData.fluid.getColor());
        final ITextureBuilder fluidTextureBuilder = TextureFactory.builder()
            .addIcon(fluidIcon)
            .setRGBA(fluidRGBA);
        if (coverData.fluid.getLuminosity() > 0) fluidTextureBuilder.glow();
        return TextureFactory.of(fluidTextureBuilder.build(), TextureFactory.of(icons[coverData.scale]));
    }

    @Override
    public boolean onCoverRightClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null || aPlayer == null || aPlayer.worldObj == null || aPlayer.worldObj.isRemote) {
            return false;
        }
        final ItemStack heldItem = aPlayer.getHeldItem();
        if (aPlayer.isSneaking() || heldItem == null) {
            return false;
        }
        final FluidTankInfo[] tanks = getValidFluidTankInfos(coverable, coverData.coverSide);
        if (tanks == null) {
            return false;
        }
        if (coverData.slot < 0 || tanks.length <= coverData.slot) {
            return false;
        }
        final FluidTankInfo tank = tanks[coverData.slot];
        if (tank == null) {
            return false;
        }
        IFluidHandler tankHandler = (IFluidHandler) coverable;

        final ItemStack heldItemSizedOne = GTUtility.copyAmount(1, heldItem);
        if (heldItemSizedOne == null || heldItemSizedOne.stackSize <= 0) {
            return false;
        }

        ItemStack result;
        result = fillToTank(heldItemSizedOne, tankHandler, coverData.coverSide);
        if (result != null) {
            replaceHeldItemStack(aPlayer, heldItem, result);
            return true;
        }
        result = fillToContainer(heldItemSizedOne, tank, tankHandler, coverData.coverSide);
        if (result != null) {
            replaceHeldItemStack(aPlayer, heldItem, result);
            return true;
        }
        return false;
    }

    protected static ItemStack fillToTank(@Nonnull ItemStack container, @Nonnull IFluidHandler tank,
        ForgeDirection coverSide) {
        final FluidStack fluidToFill = GTUtility.getFluidForFilledItem(container, true);
        if (fluidToFill == null || fluidToFill.getFluid() == null || fluidToFill.amount <= 0) {
            return null;
        }
        if (!tank.canFill(coverSide, fluidToFill.getFluid())) {
            return null;
        }

        if (container.getItem() instanceof IFluidContainerItem containerItem) {
            final int filled = tank.fill(coverSide, fluidToFill, true);
            if (filled == 0) {
                return null;
            }
            containerItem.drain(container, filled, true);
            return container;
        } else {
            final int filled = tank.fill(coverSide, fluidToFill, false);
            if (filled != fluidToFill.amount) {
                return null;
            }
            tank.fill(coverSide, fluidToFill, true);
            return GTUtility.getContainerForFilledItem(container, false);
        }
    }

    protected static ItemStack fillToContainer(@Nonnull ItemStack container, @Nonnull FluidTankInfo tankInfo,
        @Nonnull IFluidHandler tank, ForgeDirection coverSide) {
        if (tankInfo.fluid == null || tankInfo.fluid.getFluid() == null || tankInfo.fluid.amount <= 0) {
            return null;
        }
        if (!tank.canDrain(coverSide, tankInfo.fluid.getFluid())) {
            return null;
        }

        if (container.getItem() instanceof IFluidContainerItem containerItem) {
            final int filled = Math.min(
                Optional
                    .ofNullable(
                        tank.drain(
                            coverSide,
                            new FluidStack(tankInfo.fluid.getFluid(), containerItem.getCapacity(container)),
                            false))
                    .filter(fs -> GTUtility.areFluidsEqual(fs, tankInfo.fluid))
                    .map(fs -> fs.amount)
                    .orElse(0),
                containerItem.fill(
                    container,
                    new FluidStack(tankInfo.fluid.getFluid(), containerItem.getCapacity(container)),
                    false));
            if (filled == 0) {
                return null;
            }
            containerItem.fill(container, new FluidStack(tankInfo.fluid.getFluid(), filled), true);
            tank.drain(coverSide, new FluidStack(tankInfo.fluid.getFluid(), filled), true);
            return container;
        } else {
            final ItemStack filledContainer = GTUtility.fillFluidContainer(tankInfo.fluid, container, false, false);
            if (filledContainer == null) {
                return null;
            }
            final FluidStack filledFluid = GTUtility.getFluidForFilledItem(filledContainer, false);
            if (filledFluid == null || filledFluid.getFluid() == null || filledFluid.amount <= 0) {
                return null;
            }
            if (Optional.ofNullable(tank.drain(coverSide, filledFluid, false))
                .filter(fs -> GTUtility.areFluidsEqual(fs, filledFluid))
                .map(fs -> fs.amount)
                .orElse(0) != filledFluid.amount) {
                return null;
            }
            tank.drain(coverSide, filledFluid, true);
            return filledContainer;
        }
    }

    protected static void replaceHeldItemStack(@Nonnull EntityPlayer player, @Nonnull ItemStack heldItem,
        @Nonnull ItemStack result) {
        heldItem.stackSize--;
        GTUtility.addItemToPlayerInventory(player, result);
        player.inventoryContainer.detectAndSendChanges();
    }

    @Override
    public FluidStorageData onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return coverData;
        }
        if (aPlayer.isSneaking()) {
            coverData
                .setSide(ForgeDirection.values()[(coverData.coverSide.ordinal() + 1) % ForgeDirection.values().length])
                .setSlot(0);
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("SIDE", "Side: ") + coverData.coverSide.name());
            return coverData;
        }
        final FluidTankInfo[] tanks = getValidFluidTankInfos(coverable, coverData.coverSide);
        if (tanks == null) {
            return coverData.disable();
        }
        assert 0 < tanks.length;
        if (coverData.slot < 0 || tanks.length <= coverData.slot) {
            coverData.setSlot(0);
        } else {
            coverData.setSlot((coverData.slot + tanks.length + (aPlayer.isSneaking() ? -1 : 1)) % tanks.length);
        }
        GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("053", "Slot: ") + coverData.slot);
        return coverData;
    }

    @Override
    public boolean isDataNeededOnClient() {
        return true;
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        return true;
    }

    @Override
    public boolean letsFluidOut(Fluid aFluid) {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return 10;
    }

    @Nullable
    protected static FluidTankInfo[] getValidFluidTankInfos(@Nullable ICoverable tileEntity,
        @Nonnull ForgeDirection coverSide) {
        if (tileEntity instanceof IFluidHandler) {
            final FluidTankInfo[] tanks = ((IFluidHandler) tileEntity).getTankInfo(coverSide);
            if (tanks != null && tanks.length > 0) {
                return tanks;
            }
        }
        return null;
    }

    @Nullable
    protected static FluidTankInfo[] getValidFluidTankInfosForDisplay(@Nullable ICoverable tileEntity,
        @Nonnull ForgeDirection coverSide) {
        if (tileEntity instanceof IGregTechTileEntity baseMetaTileEntity
            && baseMetaTileEntity.getMetaTileEntity() instanceof MTEDigitalTankBase digitalTank) {
            return digitalTank.getRealTankInfo(coverSide);
        }
        return getValidFluidTankInfos(tileEntity, coverSide);
    }

    protected static int getTankScale(@Nonnull FluidTankInfo tank) {
        if (tank.fluid == null || tank.capacity <= 0) {
            return 0;
        }
        return (int) Math.ceil(tank.fluid.amount / (double) tank.capacity * (icons.length - 1));
    }

    protected short[] colorToRGBA(int color) {
        return new short[] { (short) (color >> 16 & 0xFF), (short) (color >> 8 & 0xFF), (short) (color & 0xFF),
            (short) (0xFF) };
    }

    public static class FluidStorageData implements ISerializableObject {

        private ForgeDirection coverSide;
        private int slot;
        private Fluid fluid;
        private int scale;
        private boolean dirty;

        public FluidStorageData() {
            this(ForgeDirection.UNKNOWN, 0, null, 0, false);
        }

        public FluidStorageData(ForgeDirection coverSide, int slot, Fluid fluid, int scale, boolean dirty) {
            this.coverSide = coverSide;
            this.slot = slot;
            this.fluid = fluid;
            this.scale = scale;
            this.dirty = dirty;
        }

        public FluidStorageData setSide(ForgeDirection coverSide) {
            this.coverSide = coverSide;
            return this;
        }

        /**
         * @param slot 0-based index of the tank, -1 for no correct FluidTankInfo[] for the current coverSide.
         */
        public FluidStorageData setSlot(int slot) {
            if (this.slot != slot) {
                if (this.slot == -1 || slot == -1) {
                    this.dirty = true;
                }
                this.slot = slot;
            }
            return this;
        }

        /**
         * Means there is no correct FluidTankInfo[] for the current coverSide.
         */
        public FluidStorageData disable() {
            setSlot(-1);
            return this;
        }

        public FluidStorageData setFluid(@Nullable Fluid fluid) {
            if (!Util.areFluidsEqual(this.fluid, fluid)) {
                this.fluid = fluid;
                this.dirty = true;
            }
            return this;
        }

        public FluidStorageData setFluid(@Nullable FluidStack fluidStack) {
            final Fluid fluid = fluidStack == null ? null : fluidStack.getFluid();
            return setFluid(fluid);
        }

        public FluidStorageData setScale(int scale) {
            if (this.scale != scale) {
                this.scale = scale;
                this.dirty = true;
            }
            return this;
        }

        public FluidStorageData setNullTank() {
            return this.setFluid((Fluid) null)
                .setScale(0);
        }

        public FluidStorageData issueCoverUpdateIfNeeded(ICoverable tileEntity, ForgeDirection coverSide) {
            if (this.dirty) {
                tileEntity.issueCoverUpdate(coverSide);
                this.dirty = false;
            }
            return this;
        }

        // region ISerializableObject
        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new FluidStorageData(coverSide, slot, fluid, scale, dirty);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            NBTTagCompound tag = (NBTTagCompound) aNBT;
            coverSide = ForgeDirection.getOrientation(tag.getByte("coverSide"));
            slot = tag.getInteger("slot");
            fluid = Util.getFluid(tag.getString("fluidName"));
            scale = tag.getInteger("scale");
            dirty = tag.getBoolean("dirty");
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setByte("coverSide", (byte) coverSide.ordinal());
            tag.setInteger("slot", slot);
            tag.setString("fluidName", Util.getFluidName(fluid));
            tag.setInteger("scale", scale);
            tag.setBoolean("dirty", dirty);
            return tag;
        }

        @Override
        public void readFromPacket(ByteArrayDataInput aBuf) {
            this.coverSide = ForgeDirection.getOrientation(aBuf.readByte());
            this.slot = aBuf.readInt();
            this.fluid = Util.getFluid(aBuf.readInt());
            this.scale = aBuf.readInt();
            this.dirty = aBuf.readBoolean();
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeByte(coverSide.ordinal());
            aBuf.writeInt(slot);
            aBuf.writeInt(Util.getFluidID(fluid));
            aBuf.writeInt(scale);
            aBuf.writeBoolean(dirty);
        }
        // endregion

    }

    protected static class Util {

        public static int getFluidID(@Nullable Fluid fluid) {
            return fluid == null ? -1 : fluid.getID();
        }

        public static String getFluidName(@Nullable Fluid fluid) {
            return fluid == null ? "" : fluid.getName();
        }

        public static Fluid getFluid(int id) {
            return id == -1 ? null : FluidRegistry.getFluid(id);
        }

        public static Fluid getFluid(String name) {
            return name.isEmpty() ? null : FluidRegistry.getFluid(name);
        }

        public static boolean areFluidsEqual(@Nullable Fluid a, @Nullable Fluid b) {
            return getFluidID(a) == getFluidID(b);
        }
    }
}

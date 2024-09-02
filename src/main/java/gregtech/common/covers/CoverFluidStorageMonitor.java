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
import net.minecraft.entity.player.EntityPlayerMP;
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

import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.ITextureBuilder;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.CoverBehaviorBase;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.tileentities.storage.MTEDigitalTankBase;
import io.netty.buffer.ByteBuf;

/**
 * TODO: Implement overlay rendering only with
 * {@link CoverBehaviorBase#getSpecialCoverFGTextureImpl(ForgeDirection, int, ISerializableObject, ICoverable)}
 */
public class CoverFluidStorageMonitor extends CoverBehaviorBase<CoverFluidStorageMonitor.FluidStorageData> {

    private static final IIconContainer[] icons = new IIconContainer[] { OVERLAY_FLUID_STORAGE_MONITOR0,
        OVERLAY_FLUID_STORAGE_MONITOR1, OVERLAY_FLUID_STORAGE_MONITOR2, OVERLAY_FLUID_STORAGE_MONITOR3,
        OVERLAY_FLUID_STORAGE_MONITOR4, OVERLAY_FLUID_STORAGE_MONITOR5, OVERLAY_FLUID_STORAGE_MONITOR6,
        OVERLAY_FLUID_STORAGE_MONITOR7, OVERLAY_FLUID_STORAGE_MONITOR8, OVERLAY_FLUID_STORAGE_MONITOR9,
        OVERLAY_FLUID_STORAGE_MONITOR10, OVERLAY_FLUID_STORAGE_MONITOR11, OVERLAY_FLUID_STORAGE_MONITOR12,
        OVERLAY_FLUID_STORAGE_MONITOR13, OVERLAY_FLUID_STORAGE_MONITOR14, };

    public CoverFluidStorageMonitor() {
        super(FluidStorageData.class);
    }

    @Override
    public FluidStorageData createDataObject(int aLegacyData) {
        return new FluidStorageData();
    }

    @Override
    public FluidStorageData createDataObject() {
        return new FluidStorageData();
    }

    @Override
    protected FluidStorageData doCoverThingsImpl(ForgeDirection side, byte aInputRedstone, int aCoverID,
        FluidStorageData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        final FluidTankInfo[] tanks = getValidFluidTankInfosForDisplay(aTileEntity, aCoverVariable.side);
        if (tanks == null) {
            return aCoverVariable.disable()
                .issueCoverUpdateIfNeeded(aTileEntity, side);
        }
        assert 0 < tanks.length;

        if (aCoverVariable.slot < 0 || tanks.length <= aCoverVariable.slot) {
            aCoverVariable.setSlot(0);
        }

        final FluidTankInfo tank = tanks[aCoverVariable.slot];
        if (tank == null) {
            return aCoverVariable.setNullTank()
                .issueCoverUpdateIfNeeded(aTileEntity, side);
        }

        return aCoverVariable.setFluid(tank.fluid)
            .setScale(getTankScale(tank))
            .issueCoverUpdateIfNeeded(aTileEntity, side);
    }

    @Override
    protected ITexture getSpecialCoverFGTextureImpl(ForgeDirection side, int aCoverID, FluidStorageData aCoverVariable,
        ICoverable aTileEntity) {
        return getSpecialCoverTextureImpl(side, aCoverID, aCoverVariable, aTileEntity);
    }

    @Override
    protected ITexture getSpecialCoverTextureImpl(ForgeDirection side, int aCoverID, FluidStorageData aCoverVariable,
        ICoverable aTileEntity) {
        if (aCoverVariable.slot == -1 || aCoverVariable.fluid == null || aCoverVariable.scale == 0) {
            return TextureFactory.of(OVERLAY_FLUID_STORAGE_MONITOR0);
        }
        final IIconContainer fluidIcon = new IIconContainer() {

            @Override
            public IIcon getIcon() {
                return aCoverVariable.fluid.getStillIcon();
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

        final short[] fluidRGBA = colorToRGBA(aCoverVariable.fluid.getColor());
        final ITextureBuilder fluidTextureBuilder = TextureFactory.builder()
            .addIcon(fluidIcon)
            .setRGBA(fluidRGBA);
        if (aCoverVariable.fluid.getLuminosity() > 0) fluidTextureBuilder.glow();
        return TextureFactory.of(fluidTextureBuilder.build(), TextureFactory.of(icons[aCoverVariable.scale]));
    }

    @Override
    protected boolean onCoverRightClickImpl(ForgeDirection side, int aCoverID, FluidStorageData aCoverVariable,
        ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer == null || aPlayer.worldObj == null || aPlayer.worldObj.isRemote) {
            return false;
        }
        final ItemStack heldItem = aPlayer.getHeldItem();
        if (aPlayer.isSneaking() || heldItem == null) {
            return false;
        }
        final FluidTankInfo[] tanks = getValidFluidTankInfos(aTileEntity, aCoverVariable.side);
        if (tanks == null) {
            return false;
        }
        if (aCoverVariable.slot < 0 || tanks.length <= aCoverVariable.slot) {
            return false;
        }
        final FluidTankInfo tank = tanks[aCoverVariable.slot];
        if (tank == null) {
            return false;
        }
        IFluidHandler tankHandler = (IFluidHandler) aTileEntity;

        final ItemStack heldItemSizedOne = GTUtility.copyAmount(1, heldItem);
        if (heldItemSizedOne == null || heldItemSizedOne.stackSize <= 0) {
            return false;
        }

        ItemStack result;
        result = fillToTank(heldItemSizedOne, tankHandler, aCoverVariable.side);
        if (result != null) {
            replaceHeldItemStack(aPlayer, heldItem, result);
            return true;
        }
        result = fillToContainer(heldItemSizedOne, tank, tankHandler, aCoverVariable.side);
        if (result != null) {
            replaceHeldItemStack(aPlayer, heldItem, result);
            return true;
        }
        return false;
    }

    protected static ItemStack fillToTank(@Nonnull ItemStack container, @Nonnull IFluidHandler tank,
        ForgeDirection side) {
        final FluidStack fluidToFill = GTUtility.getFluidForFilledItem(container, true);
        if (fluidToFill == null || fluidToFill.getFluid() == null || fluidToFill.amount <= 0) {
            return null;
        }
        if (!tank.canFill(side, fluidToFill.getFluid())) {
            return null;
        }

        if (container.getItem() instanceof IFluidContainerItem containerItem) {
            final int filled = tank.fill(side, fluidToFill, true);
            if (filled == 0) {
                return null;
            }
            containerItem.drain(container, filled, true);
            return container;
        } else {
            final int filled = tank.fill(side, fluidToFill, false);
            if (filled != fluidToFill.amount) {
                return null;
            }
            tank.fill(side, fluidToFill, true);
            return GTUtility.getContainerForFilledItem(container, false);
        }
    }

    protected static ItemStack fillToContainer(@Nonnull ItemStack container, @Nonnull FluidTankInfo tankInfo,
        @Nonnull IFluidHandler tank, ForgeDirection side) {
        if (tankInfo.fluid == null || tankInfo.fluid.getFluid() == null || tankInfo.fluid.amount <= 0) {
            return null;
        }
        if (!tank.canDrain(side, tankInfo.fluid.getFluid())) {
            return null;
        }

        if (container.getItem() instanceof IFluidContainerItem containerItem) {
            final int filled = Math.min(
                Optional
                    .ofNullable(
                        tank.drain(
                            side,
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
            tank.drain(side, new FluidStack(tankInfo.fluid.getFluid(), filled), true);
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
            if (Optional.ofNullable(tank.drain(side, filledFluid, false))
                .filter(fs -> GTUtility.areFluidsEqual(fs, filledFluid))
                .map(fs -> fs.amount)
                .orElse(0) != filledFluid.amount) {
                return null;
            }
            tank.drain(side, filledFluid, true);
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
    protected FluidStorageData onCoverScrewdriverClickImpl(ForgeDirection side, int aCoverID,
        FluidStorageData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            aCoverVariable
                .setSide(ForgeDirection.values()[(aCoverVariable.side.ordinal() + 1) % ForgeDirection.values().length])
                .setSlot(0);
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("SIDE", "Side: ") + aCoverVariable.side.name());
            return aCoverVariable;
        }
        final FluidTankInfo[] tanks = getValidFluidTankInfos(aTileEntity, aCoverVariable.side);
        if (tanks == null) {
            return aCoverVariable.disable();
        }
        assert 0 < tanks.length;
        if (aCoverVariable.slot < 0 || tanks.length <= aCoverVariable.slot) {
            aCoverVariable.setSlot(0);
        } else {
            aCoverVariable
                .setSlot((aCoverVariable.slot + tanks.length + (aPlayer.isSneaking() ? -1 : 1)) % tanks.length);
        }
        GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("053", "Slot: ") + aCoverVariable.slot);
        return aCoverVariable;
    }

    @Override
    protected boolean isDataNeededOnClientImpl(ForgeDirection side, int aCoverID, FluidStorageData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidInImpl(ForgeDirection side, int aCoverID, FluidStorageData aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidOutImpl(ForgeDirection side, int aCoverID, FluidStorageData aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected int getTickRateImpl(ForgeDirection side, int aCoverID, FluidStorageData aCoverVariable,
        ICoverable aTileEntity) {
        return 10;
    }

    @Nullable
    protected static FluidTankInfo[] getValidFluidTankInfos(@Nullable ICoverable tileEntity,
        @Nonnull ForgeDirection side) {
        if (tileEntity instanceof IFluidHandler) {
            final FluidTankInfo[] tanks = ((IFluidHandler) tileEntity).getTankInfo(side);
            if (tanks != null && tanks.length > 0) {
                return tanks;
            }
        }
        return null;
    }

    @Nullable
    protected static FluidTankInfo[] getValidFluidTankInfosForDisplay(@Nullable ICoverable tileEntity,
        @Nonnull ForgeDirection side) {
        if (tileEntity instanceof IGregTechTileEntity baseMetaTileEntity
            && baseMetaTileEntity.getMetaTileEntity() instanceof MTEDigitalTankBase digitalTank) {
            return digitalTank.getRealTankInfo(side);
        }
        return getValidFluidTankInfos(tileEntity, side);
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

        private ForgeDirection side;
        private int slot;
        private Fluid fluid;
        private int scale;
        private boolean dirty;

        public FluidStorageData() {
            this(ForgeDirection.UNKNOWN, 0, null, 0, false);
        }

        public FluidStorageData(ForgeDirection side, int slot, Fluid fluid, int scale, boolean dirty) {
            this.side = side;
            this.slot = slot;
            this.fluid = fluid;
            this.scale = scale;
            this.dirty = dirty;
        }

        public FluidStorageData setSide(ForgeDirection side) {
            this.side = side;
            return this;
        }

        /**
         * @param slot 0-based index of the tank, -1 for no correct FluidTankInfo[] for the current side.
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
         * Means there is no correct FluidTankInfo[] for the current side.
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

        public FluidStorageData issueCoverUpdateIfNeeded(ICoverable tileEntity, ForgeDirection side) {
            if (this.dirty) {
                tileEntity.issueCoverUpdate(side);
                this.dirty = false;
            }
            return this;
        }

        // region ISerializableObject
        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new FluidStorageData(side, slot, fluid, scale, dirty);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            NBTTagCompound tag = (NBTTagCompound) aNBT;
            side = ForgeDirection.getOrientation(tag.getByte("side"));
            slot = tag.getInteger("slot");
            fluid = Util.getFluid(tag.getString("fluidName"));
            scale = tag.getInteger("scale");
            dirty = tag.getBoolean("dirty");
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setByte("side", (byte) side.ordinal());
            tag.setInteger("slot", slot);
            tag.setString("fluidName", Util.getFluidName(fluid));
            tag.setInteger("scale", scale);
            tag.setBoolean("dirty", dirty);
            return tag;
        }

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, @Nullable EntityPlayerMP aPlayer) {
            final ForgeDirection side = ForgeDirection.getOrientation(aBuf.readByte());
            final int slot = aBuf.readInt();
            final Fluid fluid = Util.getFluid(aBuf.readInt());
            final int scale = aBuf.readInt();
            final boolean dirty = aBuf.readBoolean();
            return new FluidStorageData(side, slot, fluid, scale, dirty);
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeByte(side.ordinal());
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

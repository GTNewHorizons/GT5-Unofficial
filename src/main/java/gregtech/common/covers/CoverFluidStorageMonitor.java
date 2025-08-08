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

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.ITextureBuilder;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.storage.MTEDigitalTankBase;
import io.netty.buffer.ByteBuf;

/**
 * TODO: Implement overlay rendering only with {@link Cover#getOverlayTexture()}
 */
public class CoverFluidStorageMonitor extends Cover {

    private static final IIconContainer[] icons = new IIconContainer[] { OVERLAY_FLUID_STORAGE_MONITOR0,
        OVERLAY_FLUID_STORAGE_MONITOR1, OVERLAY_FLUID_STORAGE_MONITOR2, OVERLAY_FLUID_STORAGE_MONITOR3,
        OVERLAY_FLUID_STORAGE_MONITOR4, OVERLAY_FLUID_STORAGE_MONITOR5, OVERLAY_FLUID_STORAGE_MONITOR6,
        OVERLAY_FLUID_STORAGE_MONITOR7, OVERLAY_FLUID_STORAGE_MONITOR8, OVERLAY_FLUID_STORAGE_MONITOR9,
        OVERLAY_FLUID_STORAGE_MONITOR10, OVERLAY_FLUID_STORAGE_MONITOR11, OVERLAY_FLUID_STORAGE_MONITOR12,
        OVERLAY_FLUID_STORAGE_MONITOR13, OVERLAY_FLUID_STORAGE_MONITOR14, };

    private ForgeDirection fluidLookupSide;
    private int slot;
    private Fluid fluid;
    private int scale;
    private boolean dirty;

    public CoverFluidStorageMonitor(CoverContext context) {
        super(context, null);
        this.fluidLookupSide = ForgeDirection.UNKNOWN;
        this.slot = 0;
        this.fluid = null;
        this.scale = 0;
        this.dirty = false;
    }

    public CoverFluidStorageMonitor setSide(ForgeDirection coverSide) {
        this.fluidLookupSide = coverSide;
        return this;
    }

    /**
     * @param slot 0-based index of the tank, -1 for no correct FluidTankInfo[] for the current coverSide.
     */
    public CoverFluidStorageMonitor setSlot(int slot) {
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
    public CoverFluidStorageMonitor disable() {
        setSlot(-1);
        return this;
    }

    public CoverFluidStorageMonitor setFluid(@Nullable Fluid fluid) {
        if (!Util.areFluidsEqual(this.fluid, fluid)) {
            this.fluid = fluid;
            this.dirty = true;
        }
        return this;
    }

    public CoverFluidStorageMonitor setFluid(@Nullable FluidStack fluidStack) {
        final Fluid fluid = fluidStack == null ? null : fluidStack.getFluid();
        return setFluid(fluid);
    }

    public CoverFluidStorageMonitor setScale(int scale) {
        if (this.scale != scale) {
            this.scale = scale;
            this.dirty = true;
        }
        return this;
    }

    public CoverFluidStorageMonitor setNullTank() {
        return this.setFluid((Fluid) null)
            .setScale(0);
    }

    public CoverFluidStorageMonitor issueCoverUpdateIfNeeded(ICoverable tileEntity, ForgeDirection coverSide) {
        if (this.dirty) {
            tileEntity.issueCoverUpdate(coverSide);
            this.dirty = false;
        }
        return this;
    }

    @Override
    protected void readDataFromNbt(NBTBase nbt) {
        NBTTagCompound tag = (NBTTagCompound) nbt;
        fluidLookupSide = ForgeDirection.getOrientation(tag.getByte("coverSide"));
        slot = tag.getInteger("slot");
        fluid = Util.getFluid(tag.getString("fluidName"));
        scale = tag.getInteger("scale");
        dirty = tag.getBoolean("dirty");
    }

    @Override
    public void readDataFromPacket(ByteArrayDataInput byteData) {
        this.fluidLookupSide = ForgeDirection.getOrientation(byteData.readByte());
        this.slot = byteData.readInt();
        this.fluid = Util.getFluid(byteData.readInt());
        this.scale = byteData.readInt();
        this.dirty = byteData.readBoolean();
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte("coverSide", (byte) fluidLookupSide.ordinal());
        tag.setInteger("slot", slot);
        tag.setString("fluidName", Util.getFluidName(fluid));
        tag.setInteger("scale", scale);
        tag.setBoolean("dirty", dirty);
        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeByte(fluidLookupSide.ordinal());
        byteBuf.writeInt(slot);
        byteBuf.writeInt(Util.getFluidID(fluid));
        byteBuf.writeInt(scale);
        byteBuf.writeBoolean(dirty);
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        final FluidTankInfo[] tanks = getValidFluidTankInfosForDisplay(coverable, fluidLookupSide);
        if (tanks == null) {
            disable().issueCoverUpdateIfNeeded(coverable, coverSide);
            return;
        }
        assert 0 < tanks.length;

        if (slot < 0 || tanks.length <= slot) {
            setSlot(0);
        }

        final FluidTankInfo tank = tanks[slot];
        if (tank == null) {
            setNullTank().issueCoverUpdateIfNeeded(coverable, coverSide);
            return;
        }

        setFluid(tank.fluid).setScale(getTankScale(tank))
            .issueCoverUpdateIfNeeded(coverable, coverSide);
    }

    @Override
    public ITexture getOverlayTexture() {
        return getSpecialFaceTexture();
    }

    @Override
    public ITexture getSpecialFaceTexture() {
        if (slot == -1 || fluid == null || scale == 0) {
            return TextureFactory.of(OVERLAY_FLUID_STORAGE_MONITOR0);
        }
        final IIconContainer fluidIcon = new IIconContainer() {

            @Override
            public IIcon getIcon() {
                return fluid.getStillIcon();
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

        final short[] fluidRGBA = colorToRGBA(fluid.getColor());
        final ITextureBuilder fluidTextureBuilder = TextureFactory.builder()
            .addIcon(fluidIcon)
            .setRGBA(fluidRGBA);
        if (fluid.getLuminosity() > 0) fluidTextureBuilder.glow();
        return TextureFactory.of(fluidTextureBuilder.build(), TextureFactory.of(icons[scale]));
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
        final FluidTankInfo[] tanks = getValidFluidTankInfos(coverable, fluidLookupSide);
        if (tanks == null) {
            return false;
        }
        if (slot < 0 || tanks.length <= slot) {
            return false;
        }
        final FluidTankInfo tank = tanks[slot];
        if (tank == null) {
            return false;
        }
        IFluidHandler tankHandler = (IFluidHandler) coverable;

        final ItemStack heldItemSizedOne = GTUtility.copyAmount(1, heldItem);
        if (heldItemSizedOne == null || heldItemSizedOne.stackSize <= 0) {
            return false;
        }

        ItemStack result;
        result = fillToTank(heldItemSizedOne, tankHandler, fluidLookupSide);
        if (result != null) {
            replaceHeldItemStack(aPlayer, heldItem, result);
            return true;
        }
        result = fillToContainer(heldItemSizedOne, tank, tankHandler, fluidLookupSide);
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
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            setSide(ForgeDirection.values()[(fluidLookupSide.ordinal() + 1) % ForgeDirection.values().length])
                .setSlot(0);
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("SIDE", "Side: ") + fluidLookupSide.name());
            return;
        }
        final FluidTankInfo[] tanks = getValidFluidTankInfos(coveredTile.get(), fluidLookupSide);
        if (tanks == null) {
            disable();
            return;
        }
        assert 0 < tanks.length;
        if (slot < 0 || tanks.length <= slot) {
            setSlot(0);
        } else {
            setSlot((slot + tanks.length + (aPlayer.isSneaking() ? -1 : 1)) % tanks.length);
        }
        GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("053", "Slot: ") + slot);
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

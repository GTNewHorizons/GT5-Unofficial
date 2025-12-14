package tectech.thing.cover;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.covers.Cover;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.gui.mui1.cover.EnderFluidLinkUIFactory;
import gtPlusPlus.core.tileentities.base.TileEntityBase;
import io.netty.buffer.ByteBuf;
import tectech.mechanics.enderStorage.EnderLinkTag;
import tectech.mechanics.enderStorage.EnderWorldSavedData;
import tectech.thing.gui.CoverEnderFluidLinkGui;

public class CoverEnderFluidLink extends Cover {

    private static final int L_PER_TICK = 8000;
    private boolean export;
    private boolean privateChannel;
    // TODO: REMOVE AFTER 2.9
    public static final int IMPORT_EXPORT_MASK = 0b0001;
    public static final int PUBLIC_PRIVATE_MASK = 0b0010;

    public CoverEnderFluidLink(@NotNull CoverContext context, ITexture coverFGTexture) {
        super(context, coverFGTexture);
    }

    private void transferFluid(IFluidHandler source, ForgeDirection coverSide, IFluidHandler target,
        ForgeDirection tSide) {
        int drainAmount = CoverEnderFluidLink.L_PER_TICK * getTickRate();
        FluidStack fluidStack = source.drain(coverSide, drainAmount, false);

        if (fluidStack != null) {
            int fluidTransferred = target.fill(tSide, fluidStack, true);
            source.drain(coverSide, fluidTransferred, true);
        }
    }

    public static boolean testBit(int coverData, int bitMask) {
        return (coverData & bitMask) != 0;
    }

    public static int toggleBit(int coverData, int bitMask) {
        return (coverData ^ bitMask);
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        if (!(coverable instanceof IFluidHandler teTank)) {
            return;
        }

        EnderLinkTag tag = EnderWorldSavedData.getEnderLinkTag(teTank);
        if (tag == null) {
            return;
        }

        boolean isPrivate = tag.getUUID() != null;

        if (privateChannel != isPrivate) {
            tag = new EnderLinkTag(tag.getFrequency(), privateChannel ? getOwner(coverable) : null);
            EnderWorldSavedData.bindEnderLinkTag(teTank, tag);
        }

        IFluidHandler enderTank = EnderWorldSavedData.getEnderFluidContainer(tag);

        if (export) {
            transferFluid(teTank, coverSide, enderTank, ForgeDirection.UNKNOWN);
        } else {
            transferFluid(enderTank, ForgeDirection.UNKNOWN, teTank, coverSide);
        }
    }

    public static UUID getOwner(Object te) {
        if (te instanceof IGregTechTileEntity igte) {
            return igte.getOwnerUuid();
        } else if (te instanceof TileEntityBase teb) {
            return teb.getOwnerUUID();
        } else {
            return null;
        }
    }

    @Override
    public void onBaseTEDestroyed() {
        if (coveredTile.get() instanceof IFluidHandler fluidHandlerSelf) {
            EnderWorldSavedData.unbindTank(fluidHandlerSelf);
        }
    }

    @Override
    public void onCoverRemoval() {
        if (coveredTile.get() instanceof IFluidHandler fluidHandlerSelf) {
            EnderWorldSavedData.unbindTank(fluidHandlerSelf);
        }
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
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        export = !export;

        if (aPlayer instanceof EntityPlayerMP) {
            aPlayer.addChatMessage(
                new ChatComponentTranslation(
                    export ? "tt.cover.ender_fluid_link.fill" : "tt.cover.ender_fluid_link.suck"));
        }
    }

    public boolean isExport() {
        return export;
    }

    public void setExport(boolean export) {
        this.export = export;
    }

    public boolean isPrivateChannel() {
        return privateChannel;
    }

    public void setPrivateChannel(boolean privateChannel) {
        this.privateChannel = privateChannel;
    }

    @Override
    protected void readDataFromNbt(NBTBase nbt) {
        if (nbt instanceof NBTTagInt nbtInt) {
            readLegacyDataFromNbt(nbtInt);
            return;
        }
        NBTTagCompound tag = (NBTTagCompound) nbt;
        this.export = tag.getBoolean("export");
        this.privateChannel = tag.getBoolean("privateChannel");
    }

    private void readLegacyDataFromNbt(NBTTagInt nbtInt) {
        int data = nbtInt.func_150287_d();
        this.export = !testBit(data, IMPORT_EXPORT_MASK);
        this.privateChannel = testBit(data, PUBLIC_PRIVATE_MASK);
    }

    @Override
    protected void readDataFromPacket(ByteArrayDataInput byteData) {
        this.export = byteData.readBoolean();
        this.privateChannel = byteData.readBoolean();
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("export", this.export);
        tag.setBoolean("privateChannel", this.privateChannel);

        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeBoolean(this.export);
        byteBuf.writeBoolean(this.privateChannel);
    }

    @Override
    public int getMinimumTickRate() {
        // Runs each tick
        return 1;
    }

    // region GUI

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    protected @NotNull CoverBaseGui<?> getCoverGui() {
        return new CoverEnderFluidLinkGui(this);
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        // Only open gui if we're placed on a fluid tank
        if (buildContext.getTile() instanceof IFluidHandler) {
            return new EnderFluidLinkUIFactory(buildContext).createWindow();
        }
        return null;
    }

}

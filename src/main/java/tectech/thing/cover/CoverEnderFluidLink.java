package tectech.thing.cover;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import eu.usrv.yamcore.auxiliary.PlayerChatHelper;
import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.covers.CoverLegacyData;
import gregtech.common.gui.mui1.cover.EnderFluidLinkUIFactory;
import gtPlusPlus.core.tileentities.base.TileEntityBase;
import tectech.mechanics.enderStorage.EnderLinkTag;
import tectech.mechanics.enderStorage.EnderWorldSavedData;

public class CoverEnderFluidLink extends CoverLegacyData {

    private static final int L_PER_TICK = 8000;
    public static final int IMPORT_EXPORT_MASK = 0b0001;
    public static final int PUBLIC_PRIVATE_MASK = 0b0010;

    public CoverEnderFluidLink(CoverContext context) {
        super(context);
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
        if ((coverable instanceof IFluidHandler teTank)) {
            EnderLinkTag tag = EnderWorldSavedData.getEnderLinkTag(teTank);

            if (tag != null) {
                boolean shouldBePrivate = testBit(this.coverData, PUBLIC_PRIVATE_MASK);
                boolean isPrivate = tag.getUUID() != null;

                if (shouldBePrivate != isPrivate) {
                    tag = new EnderLinkTag(tag.getFrequency(), shouldBePrivate ? getOwner(coverable) : null);
                    EnderWorldSavedData.bindEnderLinkTag(teTank, tag);
                }

                IFluidHandler enderTank = EnderWorldSavedData.getEnderFluidContainer(tag);

                if (testBit(this.coverData, IMPORT_EXPORT_MASK)) {
                    transferFluid(enderTank, ForgeDirection.UNKNOWN, teTank, coverSide);
                } else {
                    transferFluid(teTank, coverSide, enderTank, ForgeDirection.UNKNOWN);
                }
            }
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
        this.coverData = toggleBit(this.coverData, IMPORT_EXPORT_MASK);

        if (testBit(this.coverData, IMPORT_EXPORT_MASK)) {
            PlayerChatHelper.SendInfo(aPlayer, "Ender Suction Engaged!"); // TODO Translation support
        } else {
            PlayerChatHelper.SendInfo(aPlayer, "Ender Filling Engaged!");
        }
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
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        // Only open gui if we're placed on a fluid tank
        if (buildContext.getTile() instanceof IFluidHandler) {
            return new EnderFluidLinkUIFactory(buildContext).createWindow();
        }
        return null;
    }

}

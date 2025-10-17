package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.modes.PlayerDetectionMode;
import gregtech.common.gui.modularui.cover.CoverPlayerDetectorGui;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.gui.mui1.cover.PlayerDetectorUIFactory;

public class CoverPlayerDetector extends CoverLegacyData {

    private String placer = "";
    private int range = 8;

    public CoverPlayerDetector(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    public PlayerDetectionMode getPlayerDetectionMode() {
        int coverVariable = coverData;
        if (coverVariable >= 0 && coverVariable < PlayerDetectionMode.values().length) {
            return PlayerDetectionMode.values()[coverVariable];
        }
        return PlayerDetectionMode.ANY_PLAYER;
    }

    public void setPlayerDetectionMode(PlayerDetectionMode mode) {
        setVariable(mode.ordinal());
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        boolean playerDetected = false;

        if (coverable instanceof IGregTechTileEntity gtte) {
            if (gtte.isUniversalEnergyStored(20)) {
                gtte.decreaseStoredEnergyUnits(20, true);
                range = 32;
            } else {
                range = 8;
            }
            placer = gtte.getOwnerName();
        }
        for (Object tObject : coverable.getWorld().playerEntities) {
            if ((tObject instanceof EntityPlayerMP tEntity)) {
                int dist = Math.max(
                    1,
                    (int) tEntity.getDistance(
                        coverable.getXCoord() + 0.5D,
                        coverable.getYCoord() + 0.5D,
                        coverable.getZCoord() + 0.5D));
                if (dist < range) {
                    if (this.coverData == 0) {
                        playerDetected = true;
                        break;
                    }
                    if (tEntity.getDisplayName()
                        .equalsIgnoreCase(placer)) {
                        if (this.coverData == 1) {
                            playerDetected = true;
                            break;
                        }
                    } else if (this.coverData == 2) {
                        playerDetected = true;
                        break;
                    }
                }
            }
        }

        coverable.setOutputRedstoneSignal(coverSide, (byte) (playerDetected ? 15 : 0));
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        this.coverData = (this.coverData + (aPlayer.isSneaking() ? -1 : 1)) % 3;
        if (this.coverData < 0) {
            this.coverData = 2;
        }
        switch (this.coverData) {
            case 0 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("068.1", "Emit if any Player is close"));
            case 1 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("069.1", "Emit if other Player is close"));
            case 2 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("070", "Emit if you are close"));
        }
    }

    @Override
    public boolean letsEnergyIn() {
        return true;
    }

    @Override
    public boolean letsEnergyOut() {
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
    public boolean letsItemsIn(int aSlot) {
        return true;
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return true;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return 20;
    }

    // GUI stuff

    @Override
    protected @NotNull CoverBaseGui<?> getCoverGui() {
        return new CoverPlayerDetectorGui(this);
    }

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new PlayerDetectorUIFactory(buildContext).createWindow();
    }

}

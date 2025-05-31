package gregtech.common.covers;

import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.mui1.cover.RedstoneWirelessBaseUIFactory;

public abstract class CoverRedstoneWirelessBase extends CoverLegacyData {

    public static final int MAX_CHANNEL = 65535;
    private static final int PRIVATE_MASK = 0xFFFE0000;
    public static final int PUBLIC_MASK = 0x0000FFFF;
    public static final int CHECKBOX_MASK = 0x00010000;

    public CoverRedstoneWirelessBase(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    @Override
    public boolean onCoverRightClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (((aX > 0.375D) && (aX < 0.625D)) || ((coverSide.offsetX != 0) && ((aY > 0.375D) && (aY < 0.625D)))) {
            GregTechAPI.sWirelessRedstone.put(coverData, (byte) 0);
            coverData = (coverData & (PRIVATE_MASK | CHECKBOX_MASK))
                | (((Integer) GTUtility.stackToInt(aPlayer.inventory.getCurrentItem())).hashCode() & PUBLIC_MASK);
            GTUtility.sendChatToPlayer(aPlayer, translateToLocalFormatted("gt.interact.desc.freq_format", coverData));
            return true;
        }
        return false;
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (((aX > 0.375D) && (aX < 0.625D)) || ((coverSide.offsetX == 0)
            || (((aY > 0.375D) && (aY < 0.625D)) || ((((aZ <= 0.375D) || (aZ >= 0.625D))))))) {
            GregTechAPI.sWirelessRedstone.put(this.coverData, (byte) 0);
            final float[] tCoords = GTUtility.getClickedFacingCoords(coverSide, aX, aY, aZ);

            final short tAdjustVal = switch ((byte) ((byte) (int) (tCoords[0] * 2.0F)
                + 2 * (byte) (int) (tCoords[1] * 2.0F))) {
                case 0 -> -32;
                case 1 -> 32;
                case 2 -> -1024;
                case 3 -> 1024;
                default -> 0;
            };

            final int tPublicChannel = (this.coverData & PUBLIC_MASK) + tAdjustVal;

            if (tPublicChannel < 0) {
                this.coverData = this.coverData & ~PUBLIC_MASK;
            } else if (tPublicChannel > MAX_CHANNEL) {
                this.coverData = (this.coverData & (PRIVATE_MASK | CHECKBOX_MASK)) | MAX_CHANNEL;
            } else {
                this.coverData = (this.coverData & (PRIVATE_MASK | CHECKBOX_MASK)) | tPublicChannel;
            }
        }
        GTUtility.sendChatToPlayer(
            aPlayer,
            translateToLocalFormatted("gt.interact.desc.freq_format", this.coverData & PUBLIC_MASK));
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
    public String getDescription() {
        return translateToLocalFormatted("gt.interact.desc.freq_format", coverData);
    }

    @Override
    public int getMinimumTickRate() {
        return 1;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new RedstoneWirelessBaseUIFactory(buildContext).createWindow();
    }

}

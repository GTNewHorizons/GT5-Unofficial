package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.cover.CoverFluidLimiterGui;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.gui.mui1.cover.FluidLimiterUIFactory;
import io.netty.buffer.ByteBuf;

/***
 * @author TrainerSnow#5086
 */
public class CoverFluidLimiter extends Cover {

    private float threshold;

    public CoverFluidLimiter(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
        threshold = 1F;
    }

    public float getThreshold() {
        return this.threshold;
    }

    public CoverFluidLimiter setThreshold(float threshold) {
        this.threshold = threshold;
        return this;
    }

    @Override
    protected void readDataFromNbt(NBTBase nbt) {
        if (nbt instanceof NBTTagCompound tag) {
            this.threshold = tag.getFloat("threshold");
        }
    }

    @Override
    public void readDataFromPacket(ByteArrayDataInput byteData) {
        this.threshold = byteData.readFloat();
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setFloat("threshold", this.threshold);
        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeFloat(this.threshold);
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (coveredTile.get() instanceof IFluidHandler) {
            adjustThreshold(!aPlayer.isSneaking());
            GTUtility.sendChatToPlayer(aPlayer, String.format("Threshold: %f", threshold));
        }
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        if (coveredTile.get() instanceof IFluidHandler fluidHandler) {
            return threshold > getFillLevelInputSlots(fluidHandler);
        }
        return false;
    }

    @Override
    public boolean alwaysLookConnected() {
        return true;
    }

    /*
     * Helpers
     */

    private void adjustThreshold(boolean way) {
        if (way) {
            if ((threshold + 0.05f) > 1F) {
                threshold = 0F;
                return;
            }
            threshold += 0.05F;
        } else {
            if ((Math.abs(threshold) - 0.05F) < 0F) {
                threshold = 1F;
                return;
            }
            threshold -= 0.05F;
        }
    }

    private float getFillLevelInputSlots(IFluidHandler fh) {
        FluidTankInfo[] tankInfo = fh.getTankInfo(ForgeDirection.UNKNOWN);
        long tMax;
        long tUsed;
        if (tankInfo != null) {
            // 0 Because we acces first slot only
            FluidTankInfo inputSlot = tankInfo[0];
            if (inputSlot.fluid != null) {
                tMax = inputSlot.capacity;
                tUsed = inputSlot.fluid.amount;
                return (float) tUsed / (float) tMax;
            }
        }
        return 0F;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    protected @NotNull CoverBaseGui<?> getCoverGui() {
        return new CoverFluidLimiterGui(this);
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new FluidLimiterUIFactory(buildContext).createWindow();
    }

}

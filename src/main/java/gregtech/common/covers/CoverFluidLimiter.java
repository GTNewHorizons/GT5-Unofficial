package gregtech.common.covers;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerNumericWidget;
import io.netty.buffer.ByteBuf;

/***
 * @author TrainerSnow#5086
 */
public class CoverFluidLimiter extends CoverBehaviorBase<CoverFluidLimiter.FluidLimiterData> {

    public CoverFluidLimiter(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public float getThreshold() {
        return this.coverData.threshold;
    }

    public CoverFluidLimiter setThreshold(float threshold) {
        this.coverData.threshold = threshold;
        return this;
    }

    @Override
    protected FluidLimiterData initializeData() {
        return new CoverFluidLimiter.FluidLimiterData(1F);
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (coveredTile.get() instanceof IFluidHandler) {
            adjustThreshold(!aPlayer.isSneaking());
            GTUtility.sendChatToPlayer(aPlayer, String.format("Threshold: %f", coverData.threshold));
        }
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        if (coveredTile.get() instanceof IFluidHandler fluidHandler) {
            return coverData.threshold > getFillLevelInputSlots(fluidHandler);
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
            if ((coverData.threshold + 0.05f) > 1F) {
                coverData.threshold = 0F;
                return;
            }
            coverData.threshold += 0.05F;
        } else {
            if ((Math.abs(coverData.threshold) - 0.05F) < 0F) {
                coverData.threshold = 1F;
                return;
            }
            coverData.threshold -= 0.05F;
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

    /*
     * Data
     */

    public static class FluidLimiterData implements ISerializableObject {

        private float threshold;

        public FluidLimiterData(float threshold) {
            this.threshold = threshold;
        }

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new FluidLimiterData(threshold);
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setFloat("threshold", this.threshold);
            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeFloat(this.threshold);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            if (aNBT instanceof NBTTagCompound tag) {
                this.threshold = tag.getFloat("threshold");
            }
        }

        @Override
        public void readFromPacket(ByteArrayDataInput aBuf) {
            this.threshold = aBuf.readFloat();
        }
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new FluidLimiterUIFactory(buildContext).createWindow();
    }

    private class FluidLimiterUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public FluidLimiterUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @Override
        protected CoverFluidLimiter adaptCover(Cover cover) {
            if (cover instanceof CoverFluidLimiter adapterCover) {
                return adapterCover;
            }
            return null;
        }

        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            builder
                .widget(
                    new CoverDataControllerWidget<>(this::adaptCover, getUIBuildContext()).addFollower(
                        new CoverDataFollowerNumericWidget<>(),
                        coverData -> (double) Math.round(coverData.getThreshold() * 100),
                        (coverData, val) -> coverData.setThreshold(val.floatValue() / 100),
                        widget -> widget.setBounds(0, 100)
                            .setFocusOnGuiOpen(true)
                            .setPos(startX, startY + spaceY * 2 - 24)
                            .setSize(spaceX * 4 - 3, 12)))
                .widget(
                    new TextWidget("Percent threshold").setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX, startY + spaceY * 2 - 35));
        }
    }
}

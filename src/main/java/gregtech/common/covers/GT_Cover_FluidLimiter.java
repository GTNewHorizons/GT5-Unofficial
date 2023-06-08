package gregtech.common.covers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.math.MathExpression;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.GT_CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollower_TextFieldWidget;
import io.netty.buffer.ByteBuf;

/***
 * @author TrainerSnow#5086
 */
public class GT_Cover_FluidLimiter extends GT_CoverBehaviorBase<GT_Cover_FluidLimiter.FluidLimiterData> {

    /**
     * @deprecated use {@link #GT_Cover_FluidLimiter(ITexture coverTexture)} instead
     */
    @Deprecated
    public GT_Cover_FluidLimiter() {
        this(null);
    }

    public GT_Cover_FluidLimiter(ITexture coverTexture) {
        super(FluidLimiterData.class, coverTexture);
    }

    @Override
    protected FluidLimiterData onCoverScrewdriverClickImpl(ForgeDirection side, int aCoverID,
        FluidLimiterData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aTileEntity instanceof IFluidHandler) {
            adjustThreshold(aCoverVariable, !aPlayer.isSneaking());
            GT_Utility.sendChatToPlayer(aPlayer, String.format("Threshold: %f", aCoverVariable.threshold));
        }
        return aCoverVariable;
    }

    @Override
    protected boolean letsFluidInImpl(ForgeDirection side, int aCoverID, FluidLimiterData aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return allowsFluidIn(aCoverVariable, aTileEntity);
    }

    @Override
    protected boolean alwaysLookConnectedImpl(ForgeDirection side, int aCoverID, FluidLimiterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    /*
     * Helpers
     */

    private boolean allowsFluidIn(FluidLimiterData aCoverVariable, ICoverable c) {
        if (c instanceof IFluidHandler) {
            return aCoverVariable.threshold > getFillLevelInputSlots((IFluidHandler) c);
        }
        return false;
    }

    private void adjustThreshold(FluidLimiterData coverVariable, boolean way) {
        if (way) {
            if ((coverVariable.threshold + 0.05f) > 1F) {
                coverVariable.threshold = 0F;
                return;
            }
            coverVariable.threshold += 0.05F;
        } else {
            if ((Math.abs(coverVariable.threshold) - 0.05F) < 0F) {
                coverVariable.threshold = 1F;
                return;
            }
            coverVariable.threshold -= 0.05F;
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

    @Override
    public FluidLimiterData createDataObject(int aLegacyData) {
        return createDataObject();
    }

    @Override
    public FluidLimiterData createDataObject() {
        return new FluidLimiterData(1F);
    }

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

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, @Nullable EntityPlayerMP aPlayer) {
            this.threshold = aBuf.readFloat();
            return this;
        }
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(GT_CoverUIBuildContext buildContext) {
        return new FluidLimiterUIFactory(buildContext).createWindow();
    }

    private class FluidLimiterUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public FluidLimiterUIFactory(GT_CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            builder.widget(
                new CoverDataControllerWidget<>(this::getCoverData, this::setCoverData, GT_Cover_FluidLimiter.this)
                    .addFollower(
                        new CoverDataFollower_TextFieldWidget<>(),
                        coverData -> String.valueOf(Math.round(coverData.threshold * 100)),
                        (coverData, val) -> {
                            coverData.threshold = (float) (MathExpression.parseMathExpression(val) / 100);
                            return coverData;
                        },
                        widget -> widget.setNumbers(0, 100)
                            .setFocusOnGuiOpen(true)
                            .setPos(startX, startY + spaceY * 2 - 24)
                            .setSize(spaceX * 4 - 3, 12)))
                .widget(
                    new TextWidget("Percent threshold").setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX, startY + spaceY * 2 - 35));
        }
    }
}

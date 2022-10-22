package gregtech.common.covers;

import com.google.common.io.ByteArrayDataInput;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiIntegerTextBox;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.net.GT_Packet_TileEntityCoverNew;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import io.netty.buffer.ByteBuf;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

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
    protected FluidLimiterData onCoverScrewdriverClickImpl(
            byte aSide,
            int aCoverID,
            FluidLimiterData aCoverVariable,
            ICoverable aTileEntity,
            EntityPlayer aPlayer,
            float aX,
            float aY,
            float aZ) {
        if (aTileEntity instanceof IFluidHandler) {
            adjustThreshold(aCoverVariable, !aPlayer.isSneaking());
            GT_Utility.sendChatToPlayer(aPlayer, String.format("Threshold: %f", aCoverVariable.threshold));
        }
        return aCoverVariable;
    }

    @Override
    protected boolean letsFluidInImpl(
            byte aSide, int aCoverID, FluidLimiterData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return allowsFluidIn(aCoverVariable, aTileEntity);
    }

    @Override
    protected boolean alwaysLookConnectedImpl(
            byte aSide, int aCoverID, FluidLimiterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    /*
    Helpers
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
    Data
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
            if (aNBT instanceof NBTTagCompound) {
                NBTTagCompound tag = (NBTTagCompound) aNBT;
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

    /*
    GUI
     */

    @Override
    protected Object getClientGUIImpl(
            byte aSide,
            int aCoverID,
            FluidLimiterData aCoverVariable,
            ICoverable aTileEntity,
            EntityPlayer aPlayer,
            World aWorld) {
        return new GUI(aSide, aCoverID, aCoverVariable, aTileEntity);
    }

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    private static class GUI extends GT_GUICover {
        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;
        private final byte side;
        private final int coverID;
        private final FluidLimiterData coverVariable;
        private final GT_GuiIntegerTextBox thresholdBox;

        private final int textColor = this.getTextColorOrDefault("text", 0xFF555555);

        public GUI(byte aSide, int aCoverID, FluidLimiterData aCoverVariable, ICoverable aTileEntity) {
            super(aTileEntity, 176, 107, GT_Utility.intToStack(aCoverID));
            this.side = aSide;
            this.coverID = aCoverID;
            this.coverVariable = aCoverVariable;

            thresholdBox = new GT_GuiIntegerTextBox(this, 2, startX, startY + spaceY * 2 - 24, spaceX * 4 - 3, 12) {
                @Override
                public boolean validChar(char c, int key) {
                    return super.validChar(c, key) || c == '-';
                }
            };
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            this.getFontRenderer().drawString("Percent threshold", startX, startY + spaceY * 2 - 35, textColor);
        }

        @Override
        protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
            thresholdBox.setFocused(true);
            String text;
            text = this.coverVariable != null ? String.valueOf(Math.round(this.coverVariable.threshold * 100)) : "";
            thresholdBox.setText(text);
        }

        @Override
        public void applyTextBox(GT_GuiIntegerTextBox box) {
            int percent;
            try {
                percent = Integer.parseInt(box.getText().trim());
            } catch (NumberFormatException ignored) {
                resetTextBox(thresholdBox);
                return;
            }

            if (percent > 100 || percent <= 0) return;
            this.coverVariable.threshold = percent / 100F;

            box.setText(String.valueOf(percent));

            GT_Values.NW.sendToServer(new GT_Packet_TileEntityCoverNew(side, coverID, coverVariable, tile));
        }
    }
}

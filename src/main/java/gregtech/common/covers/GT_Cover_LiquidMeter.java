package gregtech.common.covers;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_DigitalTankBase;
import io.netty.buffer.ByteBuf;

/**
 * TODO: Implement overlay rendering only with
 * {@link GT_CoverBehaviorBase#getSpecialCoverFGTextureImpl(ForgeDirection, int, ISerializableObject, ICoverable)}
 */
public class GT_Cover_LiquidMeter extends GT_CoverBehaviorBase<GT_Cover_LiquidMeter.LiquidMeterData> {

    public GT_Cover_LiquidMeter(ITexture coverTexture) {
        super(LiquidMeterData.class, coverTexture);
    }

    @Override
    public LiquidMeterData createDataObject(int aLegacyData) {
        return new LiquidMeterData(aLegacyData == 0, 0);
    }

    @Override
    public LiquidMeterData createDataObject() {
        return new LiquidMeterData();
    }

    @Override
    protected boolean isRedstoneSensitiveImpl(ForgeDirection side, int aCoverID, LiquidMeterData aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        return false;
    }

    public static byte computeSignalBasedOnFluid(ICoverable tileEntity, boolean inverted, int threshold) {
        if (tileEntity instanceof IFluidHandler) {
            FluidTankInfo[] tanks = ((IFluidHandler) tileEntity).getTankInfo(ForgeDirection.UNKNOWN);
            long max = 0;
            long used = 0;
            if (tanks != null) {
                for (FluidTankInfo tank : tanks) {
                    if (tank != null) {
                        if (tileEntity instanceof BaseMetaTileEntity && ((BaseMetaTileEntity) tileEntity)
                            .getMetaTileEntity() instanceof GT_MetaTileEntity_DigitalTankBase) {
                            max += ((GT_MetaTileEntity_DigitalTankBase) ((BaseMetaTileEntity) tileEntity)
                                .getMetaTileEntity()).getRealCapacity();
                        } else max += tank.capacity;
                        FluidStack tLiquid = tank.fluid;
                        if (tLiquid != null) {
                            used += tLiquid.amount;
                        }
                    }
                }
            }

            return GT_Utility.convertRatioToRedstone(used, max, threshold, inverted);
        } else {
            return 0;
        }
    }

    @Override
    protected LiquidMeterData doCoverThingsImpl(ForgeDirection side, byte aInputRedstone, int aCoverID,
        LiquidMeterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        byte signal = computeSignalBasedOnFluid(aTileEntity, aCoverVariable.inverted, aCoverVariable.threshold);
        aTileEntity.setOutputRedstoneSignal(side, signal);

        return aCoverVariable;
    }

    @Override
    protected LiquidMeterData onCoverScrewdriverClickImpl(ForgeDirection side, int aCoverID,
        LiquidMeterData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aCoverVariable.inverted) {
            aCoverVariable.inverted = false;
            GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("055", "Normal"));
        } else {
            aCoverVariable.inverted = true;
            GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("054", "Inverted"));
        }
        return aCoverVariable;
    }

    @Override
    protected boolean letsEnergyInImpl(ForgeDirection side, int aCoverID, LiquidMeterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsEnergyOutImpl(ForgeDirection side, int aCoverID, LiquidMeterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidInImpl(ForgeDirection side, int aCoverID, LiquidMeterData aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidOutImpl(ForgeDirection side, int aCoverID, LiquidMeterData aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsItemsInImpl(ForgeDirection side, int aCoverID, LiquidMeterData aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsItemsOutImpl(ForgeDirection side, int aCoverID, LiquidMeterData aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean manipulatesSidedRedstoneOutputImpl(ForgeDirection side, int aCoverID,
        LiquidMeterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected int getTickRateImpl(ForgeDirection side, int aCoverID, LiquidMeterData aCoverVariable,
        ICoverable aTileEntity) {
        return 5;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    // @Override
    // public ModularWindow createWindow(GT_CoverUIBuildContext buildContext) {
    // return new LiquidMeterUIFactory(buildContext).createWindow();
    // }

    // private class LiquidMeterUIFactory extends UIFactory {
    //
    // private static final int startX = 10;
    // private static final int startY = 25;
    // private static final int spaceX = 18;
    // private static final int spaceY = 18;
    //
    // public LiquidMeterUIFactory(GT_CoverUIBuildContext buildContext) {
    // super(buildContext);
    // }
    //
    // @SuppressWarnings("PointlessArithmeticExpression")
    // @Override
    // protected void addUIWidgets(ModularWindow.Builder builder) {
    // final String INVERTED = GT_Utility.trans("INVERTED", "Inverted");
    // final String NORMAL = GT_Utility.trans("NORMAL", "Normal");
    // final int maxCapacity;
    //
    // if (getUIBuildContext().getTile() instanceof IFluidHandler) {
    // FluidTankInfo[] tanks = ((IFluidHandler) getUIBuildContext().getTile())
    // .getTankInfo(ForgeDirection.UNKNOWN);
    // maxCapacity = Arrays.stream(tanks)
    // .mapToInt(tank -> tank.capacity)
    // .sum();
    // } else {
    // maxCapacity = -1;
    // }
    //
    // builder.widget(
    // new CoverDataControllerWidget<>(this::getCoverData, this::setCoverData, GT_Cover_LiquidMeter.this)
    // .addFollower(
    // CoverDataFollower_ToggleButtonWidget.ofRedstone(),
    // coverData -> coverData.inverted,
    // (coverData, state) -> {
    // coverData.inverted = state;
    // return coverData;
    // },
    // widget -> widget.addTooltip(0, NORMAL)
    // .addTooltip(1, INVERTED)
    // .setPos(spaceX * 0, spaceY * 0))
    // .addFollower(
    // new CoverDataFollower_TextFieldWidget<>(),
    // coverData -> String.valueOf(coverData.threshold),
    // (coverData, state) -> {
    // coverData.threshold = (int) MathExpression.parseMathExpression(state);
    // return coverData;
    // },
    // widget -> widget.setOnScrollNumbers(1000, 100, 100000)
    // .setNumbers(0, maxCapacity > 0 ? maxCapacity : Integer.MAX_VALUE)
    // .setFocusOnGuiOpen(true)
    // .setPos(spaceX * 0, spaceY * 1 + 2)
    // .setSize(spaceX * 4 + 5, 12))
    // .setPos(startX, startY))
    // .widget(
    // TextWidget
    // .dynamicString(() -> getCoverData() != null ? getCoverData().inverted ? INVERTED : NORMAL : "")
    // .setSynced(false)
    // .setDefaultColor(COLOR_TEXT_GRAY.get())
    // .setPos(startX + spaceX * 1, 4 + startY + spaceY * 0))
    // .widget(
    // new TextWidget(GT_Utility.trans("222", "Fluid threshold")).setDefaultColor(COLOR_TEXT_GRAY.get())
    // .setPos(startX + spaceX * 5 - 10, startY + spaceY * 1 + 4));
    // }
    // }

    public static class LiquidMeterData implements ISerializableObject {

        private boolean inverted;
        /**
         * The special value {@code 0} means threshold check is disabled.
         */
        private int threshold;

        public LiquidMeterData() {
            inverted = false;
            threshold = 0;
        }

        public LiquidMeterData(boolean inverted, int threshold) {
            this.inverted = inverted;
            this.threshold = threshold;
        }

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new LiquidMeterData(inverted, threshold);
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setBoolean("invert", inverted);
            tag.setInteger("threshold", threshold);
            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeBoolean(inverted);
            aBuf.writeInt(threshold);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            NBTTagCompound tag = (NBTTagCompound) aNBT;
            inverted = tag.getBoolean("invert");
            threshold = tag.getInteger("threshold");
        }

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer) {
            inverted = aBuf.readBoolean();
            threshold = aBuf.readInt();
            return this;
        }
    }
}

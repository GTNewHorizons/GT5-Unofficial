package gtPlusPlus.xmod.gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.metatileentity.CommonMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicTank;
import gregtech.api.metatileentity.implementations.MTEFluid;
import gregtech.api.util.CoverBehaviorBase;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerNumericWidget;
import io.netty.buffer.ByteBuf;

public class CoverOverflowValve extends CoverBehaviorBase<CoverOverflowValve.OverflowValveData> {

    private final int minOverflowPoint = 0;
    private final int maxOverflowPoint;

    private boolean allowfluidOutput = true;
    private boolean allowfluidInput = true;

    public CoverOverflowValve(int maxOverflowPoint) {
        super(OverflowValveData.class);
        this.maxOverflowPoint = maxOverflowPoint;
    }

    @Override
    public OverflowValveData createDataObject() {
        return new OverflowValveData(0, 0);
    }

    @Override
    public OverflowValveData createDataObject(int aLegacyData) {
        return new OverflowValveData(0, 0);
    }

    private FluidStack doOverflowThing(FluidStack fluid, OverflowValveData data) {
        if (fluid != null && fluid.amount > data.overflowPoint)
            fluid.amount -= Math.min(fluid.amount - data.voidingRate, data.voidingRate);
        return fluid;
    }

    private void doOverflowThings(FluidStack[] fluids, OverflowValveData data) {
        for (FluidStack fluid : fluids) doOverflowThing(fluid, data);
    }

    @Override
    protected OverflowValveData doCoverThingsImpl(ForgeDirection side, byte aInputRedstone, int aCoverID,
        OverflowValveData data, ICoverable aTileEntity, long aTimer) {
        if (data == null) return new OverflowValveData(0, 0);

        if (data.overflowPoint == 0) return data;

        if (aTileEntity instanceof CommonMetaTileEntity common) {
            IMetaTileEntity tile = common.getMetaTileEntity();
            if (tile instanceof MTEBasicTank fluidTank) {
                fluidTank.setDrainableStack(doOverflowThing(fluidTank.getDrainableStack(), data));
                return data;
            } else if (tile instanceof MTEFluid fluidPipe && fluidPipe.isConnectedAtSide(side)) {
                doOverflowThings(fluidPipe.mFluids, data);
                return data;
            }
        }

        return data;
    }

    // Overrides methods

    @Override
    protected boolean alwaysLookConnectedImpl(ForgeDirection side, int aCoverID, OverflowValveData data,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected int getTickRateImpl(ForgeDirection side, int aCoverID, OverflowValveData data, ICoverable aTileEntity) {
        return 5;
    }

    @Override
    protected boolean letsItemsOutImpl(ForgeDirection side, int aCoverID, OverflowValveData data, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsItemsInImpl(ForgeDirection side, int aCoverID, OverflowValveData data, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidOutImpl(ForgeDirection side, int aCoverID, OverflowValveData data, Fluid aFluid,
        ICoverable aTileEntity) {
        return allowfluidOutput;
    }

    @Override
    protected boolean letsFluidInImpl(ForgeDirection side, int aCoverID, OverflowValveData data, Fluid aFluid,
        ICoverable aTileEntity) {
        return allowfluidInput;
    }

    @Override
    protected boolean letsEnergyOutImpl(ForgeDirection side, int aCoverID, OverflowValveData data,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsEnergyInImpl(ForgeDirection side, int aCoverID, OverflowValveData data,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsRedstoneGoOutImpl(ForgeDirection side, int aCoverID, OverflowValveData data,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsRedstoneGoInImpl(ForgeDirection side, int aCoverID, OverflowValveData data,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected OverflowValveData onCoverScrewdriverClickImpl(ForgeDirection side, int aCoverID, OverflowValveData data,
        ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (GTUtility.getClickedFacingCoords(side, aX, aY, aZ)[0] >= 0.5F) {
            data.overflowPoint += (int) (maxOverflowPoint * (aPlayer.isSneaking() ? 0.1f : 0.01f));
        } else {
            data.overflowPoint -= (int) (maxOverflowPoint * (aPlayer.isSneaking() ? 0.1f : 0.01f));
        }

        if (data.overflowPoint > maxOverflowPoint) data.overflowPoint = minOverflowPoint;
        if (data.overflowPoint <= minOverflowPoint) data.overflowPoint = maxOverflowPoint;

        GTUtility.sendChatToPlayer(
            aPlayer,
            GTUtility.trans("322", "Overflow point: ") + data.overflowPoint + GTUtility.trans("323", "L"));
        return data;
    }

    @Override
    protected boolean onCoverRightClickImpl(ForgeDirection side, int aCoverID, OverflowValveData data,
        ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        boolean aShift = aPlayer.isSneaking();
        int aAmount = aShift ? 128 : 8;
        if (GTUtility.getClickedFacingCoords(side, aX, aY, aZ)[0] >= 0.5F) {
            data.overflowPoint += aAmount;
        } else {
            data.overflowPoint -= aAmount;
        }

        if (data.overflowPoint > maxOverflowPoint) data.overflowPoint = minOverflowPoint;
        if (data.overflowPoint <= minOverflowPoint) data.overflowPoint = maxOverflowPoint;

        GTUtility.sendChatToPlayer(
            aPlayer,
            GTUtility.trans("322", "Overflow point: ") + data.overflowPoint + GTUtility.trans("323", "L"));
        aTileEntity.setCoverDataAtSide(side, new ISerializableObject.LegacyCoverData(data.overflowPoint));
        return true;
    }

    // GUI

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new OverflowUIFactory(buildContext).createWindow();
    }

    private final class OverflowUIFactory extends UIFactory {

        private static final int startX = 6;
        private static final int startY = 23;

        private static final int spaceY = 12;
        // width and height of text input for "Overflow Point" and "Voiding Rate"
        private static final int width = 73;
        private static final int height = 12;

        public OverflowUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            builder
                .widget(
                    new TextWidget(GTUtility.trans("322", "Overflow point: ")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + 1, startY + spaceY * 0))
                .widget(
                    new CoverDataControllerWidget<>(this::getCoverData, this::setCoverData, CoverOverflowValve.this)
                        .addFollower(
                            new CoverDataFollowerNumericWidget<>(),
                            coverData -> (double) coverData.overflowPoint,
                            (coverData, state) -> {
                                coverData.overflowPoint = state.intValue();
                                return coverData;
                            },
                            widget -> widget.setBounds(minOverflowPoint, maxOverflowPoint)
                                .setScrollValues(1000, 144, 100000)
                                .setFocusOnGuiOpen(true)
                                .setPos(startX, startY + spaceY * 1 - 2)
                                .setSize(width, height)))
                .widget(
                    new TextWidget(GTUtility.trans("322.1", "Voiding rate: ")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + 1, startY + spaceY * 2 + 1))
                .widget(
                    new CoverDataControllerWidget<>(this::getCoverData, this::setCoverData, CoverOverflowValve.this)
                        .addFollower(
                            new CoverDataFollowerNumericWidget<>(),
                            coverData -> (double) coverData.voidingRate,
                            (coverData, state) -> {
                                coverData.voidingRate = state.intValue();
                                return coverData;
                            },
                            widget -> widget.setBounds(minOverflowPoint, maxOverflowPoint)
                                .setScrollValues(1000, 144, 100000)
                                .setFocusOnGuiOpen(true)
                                .setPos(startX, startY + spaceY * 3 - 2)
                                .setSize(width, height)));
        }
    }

    public static class OverflowValveData implements ISerializableObject {

        public int overflowPoint;
        public int voidingRate;

        public OverflowValveData(int overflowPoint, int voidingRate) {
            this.overflowPoint = overflowPoint;
            this.voidingRate = voidingRate;
        }

        @Override
        @NotNull
        public ISerializableObject copy() {
            return new OverflowValveData(overflowPoint, voidingRate);
        }

        @Override
        @NotNull
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("overflowPoint", overflowPoint);
            tag.setInteger("voidingRate", voidingRate);
            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeInt(overflowPoint)
                .writeInt(voidingRate);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            if (aNBT instanceof NBTTagCompound tag) {
                overflowPoint = tag.getInteger("overflowPoint");
                voidingRate = tag.getInteger("voidingRate");
            }
        }

        @Override
        @NotNull
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, @Nullable EntityPlayerMP aPlayer) {
            overflowPoint = aBuf.readInt();
            voidingRate = aBuf.readInt();
            return this;
        }
    }
}

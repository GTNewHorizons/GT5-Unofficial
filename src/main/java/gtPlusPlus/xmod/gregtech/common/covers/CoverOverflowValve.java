package gtPlusPlus.xmod.gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicTank;
import gregtech.api.metatileentity.implementations.MTEFluid;
import gregtech.api.util.CoverBehaviorBase;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerNumericWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import io.netty.buffer.ByteBuf;

public class CoverOverflowValve extends CoverBehaviorBase<CoverOverflowValve.OverflowValveData> {

    private final int minOverflowPoint = 0;
    private final int maxOverflowPoint;

    public CoverOverflowValve(int maxOverflowPoint) {
        super(OverflowValveData.class);
        this.maxOverflowPoint = maxOverflowPoint;
    }

    @Override
    public OverflowValveData createDataObject() {
        return new OverflowValveData(maxOverflowPoint, maxOverflowPoint / 10, true, true);
    }

    @Override
    public OverflowValveData createDataObject(int aLegacyData) {
        return new OverflowValveData(aLegacyData, maxOverflowPoint / 10, false, true);
    }

    private FluidStack doOverflowThing(FluidStack fluid, OverflowValveData data) {
        if (fluid != null && fluid.amount > data.overflowPoint)
            fluid.amount = Math.max(fluid.amount - data.voidingRate, data.overflowPoint);
        return fluid;
    }

    private void doOverflowThings(FluidStack[] fluids, OverflowValveData data) {
        for (FluidStack fluid : fluids) doOverflowThing(fluid, data);
    }

    @Override
    protected OverflowValveData doCoverThingsImpl(ForgeDirection side, byte aInputRedstone, int aCoverID,
        OverflowValveData data, ICoverable aTileEntity, long aTimer) {
        if (data == null) return new OverflowValveData(maxOverflowPoint, maxOverflowPoint / 10, true, true);

        if (data.overflowPoint == 0 || data.voidingRate == 0) return data;

        if (aTileEntity instanceof IGregTechTileEntity gregTE) {
            IMetaTileEntity tile = gregTE.getMetaTileEntity();
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
    protected boolean alwaysLookConnectedImpl(ForgeDirection side, int aCoverID, OverflowValveData aCoverVariable,
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
        return data.canFluidOutput;
    }

    @Override
    protected boolean letsFluidInImpl(ForgeDirection side, int aCoverID, OverflowValveData data, Fluid aFluid,
        ICoverable aTileEntity) {
        return data.canFluidInput;
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
            StatCollector
                .translateToLocalFormatted("GTPP.chat.text.cover_overflow_valve_overflow_point", data.overflowPoint));
        return data;
    }

    @Override
    protected boolean onCoverRightClickImpl(ForgeDirection side, int aCoverID, OverflowValveData data,
        ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int amount = aPlayer.isSneaking() ? 128 : 8;
        if (GTUtility.getClickedFacingCoords(side, aX, aY, aZ)[0] >= 0.5F) {
            data.overflowPoint += amount;
        } else {
            data.overflowPoint -= amount;
        }

        if (data.overflowPoint > maxOverflowPoint) data.overflowPoint = minOverflowPoint;
        if (data.overflowPoint <= minOverflowPoint) data.overflowPoint = maxOverflowPoint;

        GTUtility.sendChatToPlayer(
            aPlayer,
            StatCollector
                .translateToLocalFormatted("GTPP.chat.text.cover_overflow_valve_overflow_point", data.overflowPoint));
        aTileEntity.setCoverDataAtSide(side, data);
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

        // width and height of text input for "Overflow Point" and "Voiding Rate"
        private static final int width = 71;
        private static final int height = 10;

        // fluid input buttons coordinates
        private static final int xFI = 43;
        private static final int yFI = 81;

        // fluid output buttons coordinates
        private static final int xFO = 6;
        private static final int yFO = 81;

        // Overflow Point 2x text + input coordinates
        private static final int xOP = 6;
        private static final int yOP = 27;

        // Voiding Rate 2x text + input coordinates
        private static final int xVR = 6;
        private static final int yVR = 53;

        public OverflowUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            builder
                .widget(
                    new TextWidget(StatCollector.translateToLocal("GTPP.gui.text.cover_overflow_valve_overflow_point"))
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(xOP, yOP))
                .widget(
                    new TextWidget(StatCollector.translateToLocal("GTPP.gui.text.cover_overflow_valve_liter"))
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(xOP + width + 3, yOP + 11))
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
                                .setPos(xOP, yOP + 10)
                                .setSize(width, height)))
                .widget(
                    new TextWidget(StatCollector.translateToLocal("GTPP.gui.text.cover_overflow_valve_voiding_rate"))
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(xVR + 6, yVR))
                .widget(
                    new TextWidget(StatCollector.translateToLocal("GTPP.gui.text.cover_overflow_valve_l_per_update"))
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(xVR + width + 3, yVR + 11))
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
                                .setPos(xVR, yVR + 10)
                                .setSize(width, height)))
                .widget(
                    new CoverDataControllerWidget.CoverDataIndexedControllerWidget_ToggleButtons<>(
                        this::getCoverData,
                        this::setCoverData,
                        CoverOverflowValve.this,
                        this::getClickable,
                        this::updateData)
                            .addToggleButton(
                                0,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_ALLOW_INPUT)
                                    .addTooltip(
                                        StatCollector
                                            .translateToLocal("GTPP.gui.text.cover_overflow_valve_allow_fluid_input"))
                                    .setPos(xFI + 18, yFI))
                            .addToggleButton(
                                1,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_BLOCK_INPUT)
                                    .addTooltip(
                                        StatCollector
                                            .translateToLocal("GTPP.gui.text.cover_overflow_valve_block_fluid_input"))
                                    .setPos(xFI, yFI))
                            .addToggleButton(
                                2,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_ALLOW_OUTPUT)
                                    .addTooltip(
                                        StatCollector
                                            .translateToLocal("GTPP.gui.text.cover_overflow_valve_allow_fluid_output"))
                                    .setPos(xFO, yFO))
                            .addToggleButton(
                                3,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_BLOCK_OUTPUT)
                                    .addTooltip(
                                        StatCollector
                                            .translateToLocal("GTPP.gui.text.cover_overflow_valve_block_fluid_output"))
                                    .setPos(xFO + 18, yFO)));
        }

        private boolean getClickable(int id, OverflowValveData data) {
            return switch (id) {
                case 0 -> data.canFluidInput;
                case 1 -> !data.canFluidInput;
                case 2 -> data.canFluidOutput;
                case 3 -> !data.canFluidOutput;
                default -> throw new IllegalStateException("Wrong button id: " + id);
            };
        }

        private OverflowValveData updateData(int id, OverflowValveData data) {
            return switch (id) {
                case 0 -> {
                    data.canFluidInput = true;
                    yield data;
                }
                case 1 -> {
                    data.canFluidInput = false;
                    yield data;
                }
                case 2 -> {
                    data.canFluidOutput = true;
                    yield data;
                }
                case 3 -> {
                    data.canFluidOutput = false;
                    yield data;
                }
                default -> throw new IllegalStateException("Wrong button id: " + id);
            };
        }
    }

    public static class OverflowValveData implements ISerializableObject {

        private int overflowPoint;
        private int voidingRate;
        private boolean canFluidInput;
        private boolean canFluidOutput;

        public OverflowValveData(int overflowPoint, int voidingRate, boolean canFluidInput, boolean canFluidOutput) {
            this.overflowPoint = overflowPoint;
            this.voidingRate = voidingRate;
            this.canFluidInput = canFluidInput;
            this.canFluidOutput = canFluidOutput;
        }

        @Override
        @NotNull
        public ISerializableObject copy() {
            return new OverflowValveData(overflowPoint, voidingRate, canFluidInput, canFluidOutput);
        }

        @Override
        @NotNull
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("overflowPoint", overflowPoint);
            tag.setInteger("voidingRate", voidingRate);
            tag.setBoolean("canFluidInput", canFluidInput);
            tag.setBoolean("canFluidOutput", canFluidOutput);
            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeInt(overflowPoint)
                .writeInt(voidingRate)
                .writeBoolean(canFluidInput)
                .writeBoolean(canFluidOutput);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            if (aNBT instanceof NBTTagCompound tag) {
                overflowPoint = tag.getInteger("overflowPoint");
                voidingRate = tag.getInteger("voidingRate");
                canFluidInput = tag.getBoolean("canFluidInput");
                canFluidOutput = tag.getBoolean("canFluidOutput");
            }
        }

        @Override
        @NotNull
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, @Nullable EntityPlayerMP aPlayer) {
            overflowPoint = aBuf.readInt();
            voidingRate = aBuf.readInt();
            canFluidInput = aBuf.readBoolean();
            canFluidOutput = aBuf.readBoolean();
            return this;
        }
    }
}

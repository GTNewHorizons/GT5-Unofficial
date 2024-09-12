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
import gregtech.api.gui.modularui.GTUITextures;
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
        return new OverflowValveData(0, 0, false, false);
    }

    @Override
    public OverflowValveData createDataObject(int aLegacyData) {
        return new OverflowValveData(0, 0, false, false);
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
        if (data == null) return new OverflowValveData(0, 0, false, false);

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
                        .setPos(startX + 93 - 20 - 22, startY + spaceY * 0 + 12))
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
                                .setPos(startX + 92 - 20 - 21, startY + spaceY * 1 + 10)
                                .setSize(width, height)))
                .widget(
                    new TextWidget(GTUtility.trans("322.1", "Voiding rate: ")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + 93 - 21 - 20 + 7 - 2, startY + spaceY * 2 + 13 + 20 - 3))
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
                                .setPos(startX + 92 - 21 - 20, startY + spaceY * 3 + 10 + 21 - 3)
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
                                    .addTooltip(GTUtility.trans("322.2", "Allow fluid Input"))
                                    .setPos(118 - 97, spaceY * 3 + 3 - 16 + 45 + 1))
                            .addToggleButton(
                                1,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_BLOCK_INPUT)
                                    .addTooltip(GTUtility.trans("322.3", "Block fluid Input"))
                                    .setPos(100 - 96, spaceY * 3 + 3 - 16 + 45 + 1))
                            .addToggleButton(
                                2,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_ALLOW_OUTPUT)
                                    .addTooltip(GTUtility.trans("322.4", "Allow fluid output"))
                                    .setPos(118 - 97, spaceY * 4 + 9 - 16 + 44 + 1))
                            .addToggleButton(
                                3,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_BLOCK_OUTPUT)
                                    .addTooltip(GTUtility.trans("322.5", "Block fluid output"))
                                    .setPos(100 - 96, spaceY * 4 + 9 - 16 + 44 + 1)));
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

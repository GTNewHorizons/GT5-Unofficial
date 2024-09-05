package gregtech.common.covers;

import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.CoverBehavior;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class CoverPump extends CoverBehavior {

    public final int mTransferRate;

    public CoverPump(int aTransferRate, ITexture coverTexture) {
        super(coverTexture);
        this.mTransferRate = aTransferRate;
    }

    @Override
    public boolean isRedstoneSensitive(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        long aTimer) {
        return false;
    }

    @Override
    public int doCoverThings(ForgeDirection side, byte aInputRedstone, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        if ((aCoverVariable % 6 > 1) && ((aTileEntity instanceof IMachineProgress))) {
            if (((IMachineProgress) aTileEntity).isAllowedToWork() != aCoverVariable % 6 < 4) {
                return aCoverVariable;
            }
        }

        if (aTileEntity instanceof IFluidHandler current) {
            final IFluidHandler toAccess = aTileEntity.getITankContainerAtSide(side);
            if (toAccess == null) return aCoverVariable;

            transferFluid(current, toAccess, side, aCoverVariable % 2 == 0);
        }
        return aCoverVariable;
    }

    protected void transferFluid(IFluidHandler current, IFluidHandler toAccess, ForgeDirection side, boolean export) {
        IFluidHandler source = export ? current : toAccess;
        IFluidHandler dest = export ? toAccess : current;
        ForgeDirection drainSide = export ? side : side.getOpposite();
        GTUtility.moveFluid(source, dest, drainSide, mTransferRate, this::canTransferFluid);
    }

    protected boolean canTransferFluid(FluidStack fluid) {
        return true;
    }

    @Override
    public int onCoverScrewdriverclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable = (aCoverVariable + (aPlayer.isSneaking() ? -1 : 1)) % 12;
        if (aCoverVariable < 0) {
            aCoverVariable = 11;
        }
        switch (aCoverVariable) {
            case 0 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("006", "Export"));
            case 1 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("007", "Import"));
            case 2 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("008", "Export (conditional)"));
            case 3 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("009", "Import (conditional)"));
            case 4 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("010", "Export (invert cond)"));
            case 5 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("011", "Import (invert cond)"));
            case 6 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("012", "Export allow Input"));
            case 7 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("013", "Import allow Output"));
            case 8 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("014", "Export allow Input (conditional)"));
            case 9 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("015", "Import allow Output (conditional)"));
            case 10 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("016", "Export allow Input (invert cond)"));
            case 11 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("017", "Import allow Output (invert cond)"));
        }
        return aCoverVariable;
    }

    @Override
    public boolean letsRedstoneGoIn(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsRedstoneGoOut(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyIn(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyOut(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsIn(ForgeDirection side, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsOut(ForgeDirection side, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidIn(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        if ((aCoverVariable > 1) && ((aTileEntity instanceof IMachineProgress))) {
            if (((IMachineProgress) aTileEntity).isAllowedToWork() != aCoverVariable % 6 < 4) {
                return false;
            }
        }
        return (aCoverVariable >= 6) || (aCoverVariable % 2 != 0);
    }

    @Override
    public boolean letsFluidOut(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        if ((aCoverVariable > 1) && ((aTileEntity instanceof IMachineProgress))) {
            if (((IMachineProgress) aTileEntity).isAllowedToWork() != aCoverVariable % 6 < 4) {
                return false;
            }
        }
        return (aCoverVariable >= 6) || (aCoverVariable % 2 == 0);
    }

    @Override
    public boolean alwaysLookConnected(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 1;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new PumpUIFactory(buildContext).createWindow();
    }

    private class PumpUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        private CoverDataFollowerToggleButtonWidget<ISerializableObject.LegacyCoverData> mBlockWidget = null;
        private CoverDataFollowerToggleButtonWidget<ISerializableObject.LegacyCoverData> mAllowWidget = null;

        public PumpUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            builder.widget(
                new CoverDataControllerWidget.CoverDataIndexedControllerWidget_ToggleButtons<>(
                    this::getCoverData,
                    this::setCoverData,
                    CoverPump.this,
                    (id, coverData) -> !getClickable(id, convert(coverData)),
                    (id, coverData) -> new ISerializableObject.LegacyCoverData(
                        getNewCoverVariable(id, convert(coverData))))
                            .addToggleButton(
                                0,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_EXPORT)
                                    .addTooltip(GTUtility.trans("006", "Export"))
                                    .setPos(spaceX * 0, spaceY * 0))
                            .addToggleButton(
                                1,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_IMPORT)
                                    .addTooltip(GTUtility.trans("007", "Import"))
                                    .setPos(spaceX * 1, spaceY * 0))
                            .addToggleButton(
                                2,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_CHECKMARK)
                                    .addTooltip(GTUtility.trans("224", "Always On"))
                                    .setPos(spaceX * 0, spaceY * 1))
                            .addToggleButton(
                                3,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_USE_PROCESSING_STATE)
                                    .addTooltip(GTUtility.trans("343", "Use Machine Processing State"))
                                    .setPos(spaceX * 1, spaceY * 1))
                            .addToggleButton(
                                4,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget
                                    .setStaticTexture(GTUITextures.OVERLAY_BUTTON_USE_INVERTED_PROCESSING_STATE)
                                    .addTooltip(GTUtility.trans("343.1", "Use Inverted Machine Processing State"))
                                    .setPos(spaceX * 2, spaceY * 1))
                            .addToggleButton(5, CoverDataFollowerToggleButtonWidget.ofDisableable(), widget -> {
                                mAllowWidget = widget;
                                widget.setTextureGetter(i -> {
                                    ISerializableObject.LegacyCoverData coverData = getCoverData();
                                    return coverData == null || coverData.get() % 2 == 0
                                        ? GTUITextures.OVERLAY_BUTTON_ALLOW_INPUT
                                        : GTUITextures.OVERLAY_BUTTON_ALLOW_OUTPUT;
                                })
                                    .dynamicTooltip(() -> {
                                        ISerializableObject.LegacyCoverData coverData = getCoverData();
                                        return Arrays.asList(
                                            coverData == null || coverData.get() % 2 == 0
                                                ? GTUtility.trans("314", "Allow Input")
                                                : GTUtility.trans("312", "Allow Output"));
                                    })
                                    .setPos(spaceX * 0, spaceY * 2);
                            })
                            .addToggleButton(6, CoverDataFollowerToggleButtonWidget.ofDisableable(), widget -> {
                                mBlockWidget = widget;
                                widget.setTextureGetter(i -> {
                                    ISerializableObject.LegacyCoverData coverData = getCoverData();
                                    return coverData == null || coverData.get() % 2 == 0
                                        ? GTUITextures.OVERLAY_BUTTON_BLOCK_INPUT
                                        : GTUITextures.OVERLAY_BUTTON_BLOCK_OUTPUT;
                                })
                                    .dynamicTooltip(() -> {
                                        ISerializableObject.LegacyCoverData coverData = getCoverData();
                                        return Arrays.asList(
                                            coverData == null || coverData.get() % 2 == 0
                                                ? GTUtility.trans("313", "Block Input")
                                                : GTUtility.trans("311", "Block Output"));
                                    })
                                    .setPos(spaceX * 1, spaceY * 2);
                            })
                            .setPos(startX, startY))
                .widget(
                    new TextWidget(GTUtility.trans("229", "Export/Import")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 3, 4 + startY + spaceY * 0))
                .widget(
                    new TextWidget(GTUtility.trans("230", "Conditional")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 3, 4 + startY + spaceY * 1))
                .widget(TextWidget.dynamicString(() -> {
                    ISerializableObject.LegacyCoverData coverData = getCoverData();
                    return coverData == null || coverData.get() % 2 == 0 ? GTUtility.trans("344", "Input Blocking")
                        : GTUtility.trans("344.1", "Output Blocking");
                })
                    .setSynced(false)
                    .setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(3 + startX + spaceX * 3, 4 + startY + spaceY * 2));
        }

        private int getNewCoverVariable(int id, int coverVariable) {
            switch (id) {
                case 0 -> {
                    if (mBlockWidget != null) {
                        mBlockWidget.notifyTooltipChange();
                    }
                    if (mAllowWidget != null) {
                        mAllowWidget.notifyTooltipChange();
                    }
                    return coverVariable & ~0x1;
                }
                case 1 -> {
                    if (mBlockWidget != null) {
                        mBlockWidget.notifyTooltipChange();
                    }
                    if (mAllowWidget != null) {
                        mAllowWidget.notifyTooltipChange();
                    }
                    return coverVariable | 0x1;
                }
                case 2 -> {
                    if (coverVariable > 5) return 0x6 | (coverVariable & ~0xE);
                    return (coverVariable & ~0xE);
                }
                case 3 -> {
                    if (coverVariable > 5) return 0x8 | (coverVariable & ~0xE);
                    return 0x2 | (coverVariable & ~0xE);
                }
                case 4 -> {
                    if (coverVariable > 5) return 0xA | (coverVariable & ~0xE);
                    return (0x4 | (coverVariable & ~0xE));
                }
                case 5 -> {
                    if (coverVariable <= 5) return coverVariable + 6;
                }
                case 6 -> {
                    if (coverVariable > 5) return coverVariable - 6;
                }
            }
            return coverVariable;
        }

        private boolean getClickable(int id, int coverVariable) {
            if (coverVariable < 0 | 11 < coverVariable) return false;
            return switch (id) {
                case 0, 1 -> (0x1 & coverVariable) != id;
                case 2 -> (coverVariable % 6) >= 2;
                case 3 -> (coverVariable % 6) < 2 | 4 <= (coverVariable % 6);
                case 4 -> (coverVariable % 6) < 4;
                case 5 -> coverVariable < 6;
                case 6 -> coverVariable >= 6;
                default -> false;
            };
        }
    }
}

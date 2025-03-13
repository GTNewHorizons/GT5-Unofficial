package gregtech.common.covers;

import static gregtech.api.util.GTUtility.moveMultipleItemStacks;

import java.util.Collections;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject.LegacyCoverData;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class CoverConveyor extends CoverBehavior {

    public final int mTickRate;
    private final int mMaxStacks;

    public CoverConveyor(CoverContext context, int aTickRate, int maxStacks, ITexture coverTexture) {
        super(context, coverTexture);
        this.mTickRate = aTickRate;
        this.mMaxStacks = maxStacks;
    }

    @Override
    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        int coverDataValue = coverData.get();
        if ((coverDataValue % 6 > 1) && ((coverable instanceof IMachineProgress machine))) {
            if (machine.isAllowedToWork() != coverDataValue % 6 < 4) {
                return;
            }
        }
        final TileEntity tTileEntity = coverable.getTileEntityAtSide(coverSide);
        final Object fromEntity = coverDataValue % 2 == 0 ? coverable : tTileEntity;
        final Object toEntity = coverDataValue % 2 != 0 ? coverable : tTileEntity;
        final ForgeDirection fromSide = coverDataValue % 2 != 0 ? coverSide.getOpposite() : coverSide;
        final ForgeDirection toSide = coverDataValue % 2 == 0 ? coverSide.getOpposite() : coverSide;

        moveMultipleItemStacks(
            fromEntity,
            toEntity,
            fromSide,
            toSide,
            null,
            false,
            (byte) 64,
            (byte) 1,
            (byte) 64,
            (byte) 1,
            this.mMaxStacks);
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int coverDataValue = coverData.get();
        coverDataValue = (coverDataValue + (aPlayer.isSneaking() ? -1 : 1)) % 12;
        if (coverDataValue < 0) {
            coverDataValue = 11;
        }
        switch (coverDataValue) {
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
        coverData.set(coverDataValue);
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return true;
    }

    @Override
    public boolean letsRedstoneGoOut() {
        return true;
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
        int coverDataValue = coverData.get();
        return (coverDataValue >= 6) || (coverDataValue % 2 != 0);
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        int coverDataValue = coverData.get();
        return (coverDataValue >= 6) || (coverDataValue % 2 == 0);
    }

    @Override
    public boolean alwaysLookConnected() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return this.mTickRate;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new ConveyorUIFactory(buildContext).createWindow();
    }

    private class ConveyorUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        private CoverDataFollowerToggleButtonWidget<LegacyCoverData> mBlockWidget = null;
        private CoverDataFollowerToggleButtonWidget<LegacyCoverData> mAllowWidget = null;

        public ConveyorUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            builder.widget(
                new CoverDataControllerWidget.CoverDataIndexedControllerWidget_ToggleButtons<>(
                    this::getCoverData,
                    this::setCoverData,
                    CoverConveyor.this::loadFromNbt,
                    (id, coverData) -> !getClickable(id, convert(coverData)),
                    (id, coverData) -> new LegacyCoverData(getNewCoverVariable(id, convert(coverData))))
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
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_USE_INVERTED_PROCESSING_STATE)
                                .addTooltip(GTUtility.trans("343.1", "Use Inverted Machine Processing State"))
                                .setPos(spaceX * 2, spaceY * 1))
                        .addToggleButton(5, CoverDataFollowerToggleButtonWidget.ofDisableable(), widget -> {
                            mAllowWidget = widget;
                            widget.setTextureGetter(i -> {
                                LegacyCoverData coverData = getCoverData();
                                return coverData == null || convert(coverData) % 2 == 0
                                    ? GTUITextures.OVERLAY_BUTTON_ALLOW_INPUT
                                    : GTUITextures.OVERLAY_BUTTON_ALLOW_OUTPUT;
                            })
                                .dynamicTooltip(() -> {
                                    LegacyCoverData coverData = getCoverData();
                                    return Collections.singletonList(
                                        coverData == null || convert(coverData) % 2 == 0
                                            ? GTUtility.trans("314", "Allow Input")
                                            : GTUtility.trans("312", "Allow Output"));
                                })
                                .setPos(spaceX * 0, spaceY * 2);
                        })
                        .addToggleButton(6, CoverDataFollowerToggleButtonWidget.ofDisableable(), widget -> {
                            mBlockWidget = widget;
                            widget.setTextureGetter(i -> {
                                LegacyCoverData coverData = getCoverData();
                                return coverData == null || convert(coverData) % 2 == 0
                                    ? GTUITextures.OVERLAY_BUTTON_BLOCK_INPUT
                                    : GTUITextures.OVERLAY_BUTTON_BLOCK_OUTPUT;
                            })
                                .dynamicTooltip(() -> {
                                    LegacyCoverData coverData = getCoverData();
                                    return Collections.singletonList(
                                        coverData == null || convert(coverData) % 2 == 0
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
                    LegacyCoverData coverData = getCoverData();
                    return coverData == null || convert(coverData) % 2 == 0 ? GTUtility.trans("344", "Input Blocking")
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
            if (coverVariable < 0 || 11 < coverVariable) return false;
            return switch (id) {
                case 0, 1 -> (0x1 & coverVariable) != id;
                case 2 -> (coverVariable % 6) >= 2;
                case 3 -> (coverVariable % 6) < 2 || 4 <= (coverVariable % 6);
                case 4 -> (coverVariable % 6) < 4;
                case 5 -> coverVariable < 6;
                case 6 -> coverVariable >= 6;
                default -> false;
            };
        }
    }
}

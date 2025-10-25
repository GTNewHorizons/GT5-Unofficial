package gregtech.common.gui.mui1.cover;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.text.FieldPosition;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.common.covers.CoverArm;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerNumericWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class ArmUIFactory extends CoverLegacyDataUIFactory {

    private static final int startX = 10;
    private static final int startY = 25;
    private static final int spaceX = 18;
    private static final int spaceY = 18;

    private int maxSlot;

    /**
     * Display the text "Any" instead of a number when the slot is set to -1.
     */
    protected static final NumberFormatMUI numberFormatAny = new NumberFormatMUI() {

        @Override
        public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
            if (number < 0) {
                return toAppendTo.append(translateToLocal("gt.interact.desc.arm.any"));
            } else {
                return super.format(number, toAppendTo, pos);
            }
        }
    };

    public ArmUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        maxSlot = getMaxSlot();
        builder.widget(
            new CoverDataControllerWidget<>(this::getCover, getUIBuildContext()).addFollower(
                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                cover -> getFlagExport(cover.getVariable()) > 0,
                (cover, state) -> {
                    if (state) {
                        return cover.setVariable(cover.getVariable() | CoverArm.EXPORT_MASK | CoverArm.CONVERTED_BIT);
                    } else {
                        return cover.setVariable(cover.getVariable() & ~CoverArm.EXPORT_MASK | CoverArm.CONVERTED_BIT);
                    }
                },
                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_EXPORT)
                    .addTooltip(translateToLocal("gt.interact.desc.export.tooltip"))
                    .setPos(spaceX * 0, spaceY * 0))
                .addFollower(
                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                    cover -> getFlagExport(cover.getVariable()) == 0,
                    (cover, state) -> {
                        if (state) {
                            return cover
                                .setVariable(cover.getVariable() & ~CoverArm.EXPORT_MASK | CoverArm.CONVERTED_BIT);
                        } else {
                            return cover
                                .setVariable(cover.getVariable() | CoverArm.EXPORT_MASK | CoverArm.CONVERTED_BIT);
                        }
                    },
                    widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_IMPORT)
                        .addTooltip(translateToLocal("gt.interact.desc.import.tooltip"))
                        .setPos(spaceX * 1, spaceY * 0))
                .addFollower(
                    new CoverDataFollowerNumericWidget<>(),
                    cover -> (double) (getFlagInternalSlot(cover.getVariable()) - 1),
                    (cover, state) -> {
                        final int coverVariable = cover.getVariable();
                        return cover.setVariable(
                            getFlagExport(coverVariable) | ((state.intValue() + 1) & CoverArm.SLOT_ID_MASK)
                                | (getFlagAdjacentSlot(coverVariable) << 14)
                                | CoverArm.CONVERTED_BIT);
                    },
                    widget -> widget.setBounds(-1, maxSlot)
                        .setDefaultValue(-1)
                        .setScrollValues(1, 100, 10)
                        .setNumberFormat(numberFormatAny)
                        .setPos(spaceX * 0, spaceY * 1 + 2)
                        .setSize(spaceX * 2 + 5, 12))
                .addFollower(
                    new CoverDataFollowerNumericWidget<>(),
                    cover -> (double) (getFlagAdjacentSlot(cover.getVariable()) - 1),
                    (cover, state) -> {
                        final int coverVariable = cover.getVariable();
                        return cover.setVariable(
                            getFlagExport(coverVariable) | getFlagInternalSlot(coverVariable)
                                | (((state.intValue() + 1) & CoverArm.SLOT_ID_MASK) << 14)
                                | CoverArm.CONVERTED_BIT);
                    },
                    widget -> widget.setValidator(val -> {
                        // We need to check the adjacent inventory
                        // here, and can't simply set a maximum value,
                        // because it can change while this cover is
                        // alive.
                        final int adjacentMaxSlot;
                        final ICoverable tile = getUIBuildContext().getTile();
                        if (tile instanceof TileEntity && !tile.isDead()) {
                            TileEntity adj = tile.getTileEntityAtSide(getUIBuildContext().getCoverSide());
                            if (adj instanceof IInventory) adjacentMaxSlot = ((IInventory) adj).getSizeInventory() - 1;
                            else adjacentMaxSlot = -1;
                        } else {
                            adjacentMaxSlot = -1;
                        }
                        return Math.min(val, adjacentMaxSlot);
                    })
                        .setMinValue(-1)
                        .setDefaultValue(-1)
                        .setScrollValues(1, 100, 10)
                        .setNumberFormat(numberFormatAny)
                        .setPos(spaceX * 0, spaceY * 2 + 2)
                        .setSize(spaceX * 2 + 5, 12))
                .setPos(startX, startY))
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        getCoverString(
                            c -> (c.getVariable() & CoverArm.EXPORT_MASK) > 0
                                ? translateToLocal("gt.interact.desc.export")
                                : translateToLocal("gt.interact.desc.import")))
                    .setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 3, 4 + startY + spaceY * 0))
            .widget(
                new TextWidget(translateToLocal("gt.interact.desc.arm.Internal_Slot"))
                    .setPos(startX + spaceX * 3, 4 + startY + spaceY * 1))
            .widget(
                new TextWidget(translateToLocal("gt.interact.desc.arm.Adjacent_Slot"))
                    .setPos(startX + spaceX * 3, 4 + startY + spaceY * 2));
    }

    private int getMaxSlot() {
        final ICoverable tile = getUIBuildContext().getTile();
        if (tile instanceof TileEntity && !tile.isDead()) {
            return tile.getSizeInventory() - 1;
        } else {
            return -1;
        }
    }

    private int getFlagExport(int coverVariable) {
        return coverVariable & CoverArm.EXPORT_MASK;
    }

    private int getFlagInternalSlot(int coverVariable) {
        return coverVariable & CoverArm.SLOT_ID_MASK;
    }

    private int getFlagAdjacentSlot(int coverVariable) {
        return (coverVariable >> 14) & CoverArm.SLOT_ID_MASK;
    }
}

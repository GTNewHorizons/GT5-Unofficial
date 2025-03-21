package gregtech.common.covers;

import java.text.FieldPosition;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerNumericWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class CoverArm extends CoverBehavior {

    public final int mTickRate;
    // msb converted, 2nd : direction (1=export)
    // right 14 bits: internalSlot, next 14 bits adjSlot, 0 = all, slot = -1
    protected static final int EXPORT_MASK = 0x40000000;
    protected static final int SLOT_ID_MASK = 0x3FFF;
    protected static final int SLOT_ID_MIN = 0;
    protected static final int CONVERTED_BIT = 0x80000000;

    public CoverArm(CoverContext context, int aTickRate, ITexture coverTexture) {
        super(context, coverTexture);
        this.mTickRate = aTickRate;
    }

    @Override
    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null || (((coverable instanceof IMachineProgress machine)) && (!machine.isAllowedToWork()))
            || !(coverable instanceof TileEntity tileEntity)) {
            return;
        }

        final TileEntity toTile;
        final TileEntity fromTile;
        final int toSlot;
        final int fromSlot;

        if ((this.coverData & EXPORT_MASK) > 0) {
            fromTile = tileEntity;
            toTile = coverable.getTileEntityAtSide(coverSide);
            fromSlot = this.coverData & SLOT_ID_MASK;
            toSlot = (this.coverData >> 14) & SLOT_ID_MASK;
        } else {
            fromTile = coverable.getTileEntityAtSide(coverSide);
            toTile = tileEntity;
            fromSlot = (this.coverData >> 14) & SLOT_ID_MASK;
            toSlot = this.coverData & SLOT_ID_MASK;
        }

        if (fromSlot > 0 && toSlot > 0) {
            if (fromTile instanceof IInventory fromInventory && toTile instanceof IInventory toInventory)
                GTUtility.moveFromSlotToSlot(
                    fromInventory,
                    toInventory,
                    fromSlot - 1,
                    toSlot - 1,
                    null,
                    false,
                    (byte) 64,
                    (byte) 1,
                    (byte) 64,
                    (byte) 1);
        } else if (toSlot > 0) {
            final ForgeDirection toSide;
            if ((this.coverData & EXPORT_MASK) > 0) toSide = coverSide;
            else toSide = coverSide.getOpposite();
            GTUtility.moveOneItemStackIntoSlot(
                fromTile,
                toTile,
                toSide,
                toSlot - 1,
                null,
                false,
                (byte) 64,
                (byte) 1,
                (byte) 64,
                (byte) 1);
        } else if (fromSlot > 0) {
            final ForgeDirection toSide;
            if ((this.coverData & EXPORT_MASK) > 0) toSide = coverSide;
            else toSide = coverSide.getOpposite();
            if (fromTile instanceof IInventory fromInventory) GTUtility.moveFromSlotToSide(
                fromInventory,
                toTile,
                fromSlot - 1,
                toSide,
                null,
                false,
                (byte) 64,
                (byte) 1,
                (byte) 64,
                (byte) 1);
        } else {
            final ForgeDirection fromSide;
            final ForgeDirection toSide;
            if ((this.coverData & EXPORT_MASK) > 0) {
                fromSide = coverSide;
                toSide = coverSide.getOpposite();
            } else {
                fromSide = coverSide.getOpposite();
                toSide = coverSide;
            }
            GTUtility.moveOneItemStack(
                fromTile,
                toTile,
                fromSide,
                toSide,
                null,
                false,
                (byte) 64,
                (byte) 1,
                (byte) 64,
                (byte) 1);
        }
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int step = 0;
        if (GTUtility.getClickedFacingCoords(coverSide, aX, aY, aZ)[0] >= 0.5F) {
            step += aPlayer.isSneaking() ? 256 : 16;
        } else {
            step -= aPlayer.isSneaking() ? 256 : 16;
        }
        int newCoverData = getNewVar(getVariable(), step);
        sendMessageToPlayer(aPlayer, newCoverData);
        coverData = newCoverData;
    }

    @Override
    public boolean onCoverRightClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int step = (GTUtility.getClickedFacingCoords(coverSide, aX, aY, aZ)[0] >= 0.5F) ? 1 : -1;
        int tCoverVariable = getNewVar(getVariable(), step);
        sendMessageToPlayer(aPlayer, tCoverVariable);
        coverData = tCoverVariable;
        return true;
    }

    private void sendMessageToPlayer(EntityPlayer aPlayer, int var) {
        if ((var & EXPORT_MASK) != 0) GTUtility.sendChatToPlayer(
            aPlayer,
            GTUtility.trans("001", "Puts out into adjacent Slot #") + (((var >> 14) & SLOT_ID_MASK) - 1));
        else GTUtility
            .sendChatToPlayer(aPlayer, GTUtility.trans("002", "Grabs in for own Slot #") + ((var & SLOT_ID_MASK) - 1));
    }

    private int getNewVar(int var, int step) {
        int intSlot = (var & SLOT_ID_MASK);
        int adjSlot = (var >> 14) & SLOT_ID_MASK;
        if ((var & EXPORT_MASK) == 0) {
            int x = (intSlot + step);
            if (x > SLOT_ID_MASK) return createVar(0, SLOT_ID_MASK, 0);
            else if (x < 1) return createVar(-step - intSlot + 1, 0, EXPORT_MASK);
            else return createVar(0, x, 0);
        } else {
            int x = (adjSlot - step);
            if (x > SLOT_ID_MASK) return createVar(SLOT_ID_MASK, 0, EXPORT_MASK);
            else if (x < 1) return createVar(0, step - adjSlot + 1, 0);
            else return createVar(x, 0, EXPORT_MASK);
        }
    }

    private int createVar(int adjSlot, int intSlot, int export) {
        return CONVERTED_BIT | export | ((adjSlot & SLOT_ID_MASK) << 14) | (intSlot & SLOT_ID_MASK);
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return true;
    }

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
    public boolean letsFluidIn(Fluid fluid) {
        return true;
    }

    @Override
    public boolean letsFluidOut(Fluid fluid) {
        return true;
    }

    @Override
    public boolean letsItemsIn(int slot) {
        return true;
    }

    @Override
    public boolean letsItemsOut(int slot) {
        return true;
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
        return new ArmUIFactory(buildContext).createWindow();
    }

    private static class ArmUIFactory extends CoverBehaviorUIFactory {

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
                    return toAppendTo.append(GTUtility.trans("ANY", "Any"));
                } else {
                    return super.format(number, toAppendTo, pos);
                }
            }
        };

        protected ArmUIFactory(CoverUIBuildContext buildContext) {
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
                            return cover.setVariable(cover.getVariable() | EXPORT_MASK | CONVERTED_BIT);
                        } else {
                            return cover.setVariable(cover.getVariable() & ~EXPORT_MASK | CONVERTED_BIT);
                        }
                    },
                    widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_EXPORT)
                        .addTooltip(GTUtility.trans("006", "Export"))
                        .setPos(spaceX * 0, spaceY * 0))
                    .addFollower(
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        cover -> getFlagExport(cover.getVariable()) == 0,
                        (cover, state) -> {
                            if (state) {
                                return cover.setVariable(cover.getVariable() & ~EXPORT_MASK | CONVERTED_BIT);
                            } else {
                                return cover.setVariable(cover.getVariable() | EXPORT_MASK | CONVERTED_BIT);
                            }
                        },
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_IMPORT)
                            .addTooltip(GTUtility.trans("007", "Import"))
                            .setPos(spaceX * 1, spaceY * 0))
                    .addFollower(
                        new CoverDataFollowerNumericWidget<>(),
                        cover -> (double) (getFlagInternalSlot(cover.getVariable()) - 1),
                        (cover, state) -> {
                            final int coverVariable = cover.getVariable();
                            return cover.setVariable(
                                getFlagExport(coverVariable) | ((state.intValue() + 1) & SLOT_ID_MASK)
                                    | (getFlagAdjacentSlot(coverVariable) << 14)
                                    | CONVERTED_BIT);
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
                                    | (((state.intValue() + 1) & SLOT_ID_MASK) << 14)
                                    | CONVERTED_BIT);
                        },
                        widget -> widget.setValidator(val -> {
                            // We need to check the adjacent inventory here, and can't simply set a maximum value,
                            // because it can change while this cover is alive.
                            final int adjacentMaxSlot;
                            final ICoverable tile = getUIBuildContext().getTile();
                            if (tile instanceof TileEntity && !tile.isDead()) {
                                TileEntity adj = tile.getTileEntityAtSide(getUIBuildContext().getCoverSide());
                                if (adj instanceof IInventory)
                                    adjacentMaxSlot = ((IInventory) adj).getSizeInventory() - 1;
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
                                c -> (c.getVariable() & EXPORT_MASK) > 0 ? GTUtility.trans("006", "Export")
                                    : GTUtility.trans("007", "Import")))
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 3, 4 + startY + spaceY * 0))
                .widget(
                    new TextWidget(GTUtility.trans("254.1", "Internal slot#")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 3, 4 + startY + spaceY * 1))
                .widget(
                    new TextWidget(GTUtility.trans("255", "Adjacent slot#")).setDefaultColor(COLOR_TEXT_GRAY.get())
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
            return coverVariable & EXPORT_MASK;
        }

        private int getFlagInternalSlot(int coverVariable) {
            return coverVariable & SLOT_ID_MASK;
        }

        private int getFlagAdjacentSlot(int coverVariable) {
            return (coverVariable >> 14) & SLOT_ID_MASK;
        }
    }
}

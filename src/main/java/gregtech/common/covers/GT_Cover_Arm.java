package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;

public class GT_Cover_Arm extends GT_CoverBehavior {

    public final int mTickRate;
    // msb converted, 2nd : direction (1=export)
    // right 14 bits: internalSlot, next 14 bits adjSlot, 0 = all, slot = -1
    protected static final int EXPORT_MASK = 0x40000000;
    protected static final int SLOT_ID_MASK = 0x3FFF;
    protected static final int SLOT_ID_MIN = 0;
    protected static final int CONVERTED_BIT = 0x80000000;

    // This used to be translatable, but now that cover GUI is synced with server, having conflicting texts
    // among players doesn't make sense.
    private static final String ANY_TEXT = "Any";

    /**
     * @deprecated use {@link #GT_Cover_Arm(int aTickRate, ITexture coverTexture)} instead
     */
    @Deprecated
    public GT_Cover_Arm(int aTickRate) {
        this(aTickRate, null);
    }

    public GT_Cover_Arm(int aTickRate, ITexture coverTexture) {
        super(coverTexture);
        this.mTickRate = aTickRate;
    }

    @Override
    public boolean isRedstoneSensitive(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        long aTimer) {
        return false;
    }

    @Override
    public int doCoverThings(ForgeDirection side, byte aInputRedstone, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        if ((((aTileEntity instanceof IMachineProgress)) && (!((IMachineProgress) aTileEntity).isAllowedToWork()))) {
            return aCoverVariable;
        }

        // Convert from ver. 5.09.33.50, check if 3 last bits are equal
        if ((aCoverVariable >>> 29) == 0) {
            aCoverVariable = CONVERTED_BIT | (((aCoverVariable + 1) & SLOT_ID_MASK) << 14) | EXPORT_MASK;
        } else if ((aCoverVariable >>> 29) == 7) {
            aCoverVariable = CONVERTED_BIT | Math.min(Math.abs(aCoverVariable - 1), SLOT_ID_MASK);
        }

        final TileEntity toTile;
        final TileEntity fromTile;
        final int toSlot;
        final int fromSlot;

        if ((aCoverVariable & EXPORT_MASK) > 0) {
            fromTile = (TileEntity) aTileEntity;
            toTile = aTileEntity.getTileEntityAtSide(side);
            fromSlot = aCoverVariable & SLOT_ID_MASK;
            toSlot = (aCoverVariable >> 14) & SLOT_ID_MASK;
        } else {
            fromTile = aTileEntity.getTileEntityAtSide(side);
            toTile = (TileEntity) aTileEntity;
            fromSlot = (aCoverVariable >> 14) & SLOT_ID_MASK;
            toSlot = aCoverVariable & SLOT_ID_MASK;
        }

        if (fromSlot > 0 && toSlot > 0) {
            if (fromTile instanceof IInventory fromInventory && toTile instanceof IInventory toInventory)
                GT_Utility.moveFromSlotToSlot(
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
            if ((aCoverVariable & EXPORT_MASK) > 0) toSide = side;
            else toSide = side.getOpposite();
            GT_Utility.moveOneItemStackIntoSlot(
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
            if ((aCoverVariable & EXPORT_MASK) > 0) toSide = side;
            else toSide = side.getOpposite();
            if (fromTile instanceof IInventory) GT_Utility.moveFromSlotToSide(
                (IInventory) fromTile,
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
            if ((aCoverVariable & EXPORT_MASK) > 0) {
                fromSide = side;
                toSide = side.getOpposite();
            } else {
                fromSide = side.getOpposite();
                toSide = side;
            }
            GT_Utility.moveOneItemStack(
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

        return aCoverVariable;
    }

    @Override
    public int onCoverScrewdriverclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int step = 0;
        if (GT_Utility.getClickedFacingCoords(side, aX, aY, aZ)[0] >= 0.5F) {
            step += aPlayer.isSneaking() ? 256 : 16;
        } else {
            step -= aPlayer.isSneaking() ? 256 : 16;
        }
        aCoverVariable = getNewVar(aCoverVariable, step);
        sendMessageToPlayer(aPlayer, aCoverVariable);
        return aCoverVariable;
    }

    @Override
    protected boolean onCoverRightClickImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX,
        float aY, float aZ) {
        int step = (GT_Utility.getClickedFacingCoords(side, aX, aY, aZ)[0] >= 0.5F) ? 1 : -1;
        int tCoverVariable = getNewVar(aCoverVariable.get(), step);
        sendMessageToPlayer(aPlayer, tCoverVariable);
        aCoverVariable.set(tCoverVariable);
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCoverRightclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        final int step = (GT_Utility.getClickedFacingCoords(side, aX, aY, aZ)[0] >= 0.5F) ? 1 : -1;
        aCoverVariable = getNewVar(aCoverVariable, step);
        sendMessageToPlayer(aPlayer, aCoverVariable);
        aTileEntity.setCoverDataAtSide(side, aCoverVariable);
        return true;
    }

    @Override
    protected boolean isGUIClickableImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    private void sendMessageToPlayer(EntityPlayer aPlayer, int var) {
        if ((var & EXPORT_MASK) != 0) GT_Utility.sendChatToPlayer(
            aPlayer,
            GT_Utility.trans("001", "Puts out into adjacent Slot #") + (((var >> 14) & SLOT_ID_MASK) - 1));
        else GT_Utility
            .sendChatToPlayer(aPlayer, GT_Utility.trans("002", "Grabs in for own Slot #") + ((var & SLOT_ID_MASK) - 1));
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
    public boolean letsFluidIn(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidOut(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
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
    public boolean alwaysLookConnected(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return this.mTickRate;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    // @Override
    // public ModularWindow createWindow(GT_CoverUIBuildContext buildContext) {
    // return new ArmUIFactory(buildContext).createWindow();
    // }

    // private class ArmUIFactory extends UIFactory {
    //
    // private static final int startX = 10;
    // private static final int startY = 25;
    // private static final int spaceX = 18;
    // private static final int spaceY = 18;
    //
    // private int maxSlot;
    //
    // protected ArmUIFactory(GT_CoverUIBuildContext buildContext) {
    // super(buildContext);
    // }
    //
    // @SuppressWarnings("PointlessArithmeticExpression")
    // @Override
    // protected void addUIWidgets(ModularWindow.Builder builder) {
    // maxSlot = getMaxSlot();
    // builder.widget(
    // new CoverDataControllerWidget<>(this::getCoverData, this::setCoverData, GT_Cover_Arm.this).addFollower(
    // CoverDataFollower_ToggleButtonWidget.ofDisableable(),
    // coverData -> getFlagExport(convert(coverData)) > 0,
    // (coverData, state) -> {
    // if (state) {
    // return new ISerializableObject.LegacyCoverData(
    // convert(coverData) | EXPORT_MASK | CONVERTED_BIT);
    // } else {
    // return new ISerializableObject.LegacyCoverData(
    // convert(coverData) & ~EXPORT_MASK | CONVERTED_BIT);
    // }
    // },
    // widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_EXPORT)
    // .addTooltip(GT_Utility.trans("006", "Export"))
    // .setPos(spaceX * 0, spaceY * 0))
    // .addFollower(
    // CoverDataFollower_ToggleButtonWidget.ofDisableable(),
    // coverData -> getFlagExport(convert(coverData)) == 0,
    // (coverData, state) -> {
    // if (state) {
    // return new ISerializableObject.LegacyCoverData(
    // convert(coverData) & ~EXPORT_MASK | CONVERTED_BIT);
    // } else {
    // return new ISerializableObject.LegacyCoverData(
    // convert(coverData) | EXPORT_MASK | CONVERTED_BIT);
    // }
    // },
    // widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_IMPORT)
    // .addTooltip(GT_Utility.trans("007", "Import"))
    // .setPos(spaceX * 1, spaceY * 0))
    // .addFollower(
    // new CoverDataFollower_TextFieldWidget<>(),
    // coverData -> getTextFieldContent(getFlagInternalSlot(convert(coverData)) - 1),
    // (coverData, state) -> {
    // final int coverVariable = convert(coverData);
    // return new ISerializableObject.LegacyCoverData(
    // getFlagExport(coverVariable) | ((getIntFromText(state) + 1) & SLOT_ID_MASK)
    // | (getFlagAdjacentSlot(coverVariable) << 14)
    // | CONVERTED_BIT);
    // },
    // widget -> widget.setOnScrollText()
    // .setValidator(val -> {
    // final int valSlot = getIntFromText(val);
    // if (valSlot > -1) {
    // return widget.getDecimalFormatter()
    // .format(Math.min(valSlot, maxSlot));
    // } else {
    // return ANY_TEXT;
    // }
    // })
    // .setPattern(BaseTextFieldWidget.NATURAL_NUMS)
    // .setFocusOnGuiOpen(true)
    // .setPos(spaceX * 0, spaceY * 1 + 2)
    // .setSize(spaceX * 2 + 5, 12))
    // .addFollower(
    // new CoverDataFollower_TextFieldWidget<>(),
    // coverData -> getTextFieldContent(getFlagAdjacentSlot(convert(coverData)) - 1),
    // (coverData, state) -> {
    // final int coverVariable = convert(coverData);
    // return new ISerializableObject.LegacyCoverData(
    // getFlagExport(coverVariable) | getFlagInternalSlot(coverVariable)
    // | (((getIntFromText(state) + 1) & SLOT_ID_MASK) << 14)
    // | CONVERTED_BIT);
    // },
    // widget -> widget.setValidator(val -> {
    // final int valSlot = getIntFromText(val);
    // final int adjacentMaxSlot;
    // final ICoverable tile = getUIBuildContext().getTile();
    // if (tile instanceof TileEntity && !tile.isDead()) {
    // TileEntity adj = tile.getTileEntityAtSide(getUIBuildContext().getCoverSide());
    // if (adj instanceof IInventory)
    // adjacentMaxSlot = ((IInventory) adj).getSizeInventory() - 1;
    // else adjacentMaxSlot = -1;
    // } else {
    // adjacentMaxSlot = -1;
    // }
    // if (valSlot > -1) {
    // return widget.getDecimalFormatter()
    // .format(Math.min(valSlot, adjacentMaxSlot));
    // } else {
    // return ANY_TEXT;
    // }
    // })
    // .setOnScroll((text, direction) -> {
    // final int val = getIntFromText(text);
    // int step = (GuiScreen.isShiftKeyDown() ? 50 : GuiScreen.isCtrlKeyDown() ? 5 : 1)
    // * direction;
    // return widget.getDecimalFormatter()
    // .format(val + step);
    // })
    // .setPattern(BaseTextFieldWidget.NATURAL_NUMS)
    // .setPos(spaceX * 0, spaceY * 2 + 2)
    // .setSize(spaceX * 2 + 5, 12))
    // .setPos(startX, startY))
    // .widget(
    // TextWidget
    // .dynamicString(
    // () -> (convert(getCoverData()) & EXPORT_MASK) > 0 ? GT_Utility.trans("006", "Export")
    // : GT_Utility.trans("007", "Import"))
    // .setSynced(false)
    // .setDefaultColor(COLOR_TEXT_GRAY.get())
    // .setPos(startX + spaceX * 3, 4 + startY + spaceY * 0))
    // .widget(
    // new TextWidget(GT_Utility.trans("254.1", "Internal slot#")).setDefaultColor(COLOR_TEXT_GRAY.get())
    // .setPos(startX + spaceX * 3, 4 + startY + spaceY * 1))
    // .widget(
    // new TextWidget(GT_Utility.trans("255", "Adjacent slot#")).setDefaultColor(COLOR_TEXT_GRAY.get())
    // .setPos(startX + spaceX * 3, 4 + startY + spaceY * 2));
    // }
    //
    // private int getMaxSlot() {
    // final ICoverable tile = getUIBuildContext().getTile();
    // if (tile instanceof TileEntity && !tile.isDead()) {
    // return tile.getSizeInventory() - 1;
    // } else {
    // return -1;
    // }
    // }
    //
    // private String getTextFieldContent(int val) {
    // return val < 0 ? ANY_TEXT : String.valueOf(val);
    // }
    //
    // private int getIntFromText(String text) {
    // try {
    // return (int) MathExpression.parseMathExpression(text, -1);
    // } catch (Exception e) {
    // return -1;
    // }
    // }
    //
    // private int getFlagExport(int coverVariable) {
    // return coverVariable & EXPORT_MASK;
    // }
    //
    // private int getFlagInternalSlot(int coverVariable) {
    // return coverVariable & SLOT_ID_MASK;
    // }
    //
    // private int getFlagAdjacentSlot(int coverVariable) {
    // return (coverVariable >> 14) & SLOT_ID_MASK;
    // }
    // }
}

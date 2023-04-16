package gregtech.common.covers;

import static gregtech.api.util.GT_Utility.moveMultipleItemStacks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.GT_CoverUIBuildContext;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollower_ToggleButtonWidget;

public class GT_Cover_Conveyor extends GT_CoverBehavior {

    public final int mTickRate;
    private final int mMaxStacks;

    /**
     * @deprecated use {@link #GT_Cover_Conveyor(int aTickRate, int maxStacks, ITexture coverTexture)} instead
     */
    @Deprecated
    public GT_Cover_Conveyor(int aTickRate) {
        this(aTickRate, 1, null);
    }

    /**
     * @deprecated use {@link #GT_Cover_Conveyor(int aTickRate, int maxStacks, ITexture coverTexture)} instead
     */
    @Deprecated
    public GT_Cover_Conveyor(int aTickRate, int maxStacks) {
        this(aTickRate, maxStacks, null);
    }

    public GT_Cover_Conveyor(int aTickRate, int maxStacks, ITexture coverTexture) {
        super(coverTexture);
        this.mTickRate = aTickRate;
        this.mMaxStacks = maxStacks;
    }

    @Override
    public boolean isRedstoneSensitive(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        long aTimer) {
        return false;
    }

    @Override
    public int doCoverThings(ForgeDirection aSide, byte aInputRedstone, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        if ((aCoverVariable % 6 > 1) && ((aTileEntity instanceof IMachineProgress))) {
            if (((IMachineProgress) aTileEntity).isAllowedToWork() != aCoverVariable % 6 < 4) {
                return aCoverVariable;
            }
        }
        final TileEntity tTileEntity = aTileEntity.getTileEntityAtSide(aSide);
        final Object fromEntity = aCoverVariable % 2 == 0 ? aTileEntity : tTileEntity;
        final Object toEntity = aCoverVariable % 2 != 0 ? aTileEntity : tTileEntity;
        final byte fromSide = aCoverVariable % 2 != 0 ? GT_Utility.getOppositeSide(aSide) : aSide;
        final byte toSide = aCoverVariable % 2 == 0 ? GT_Utility.getOppositeSide(aSide) : aSide;

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

        return aCoverVariable;
    }

    @Override
    public int onCoverScrewdriverclick(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable = (aCoverVariable + (aPlayer.isSneaking() ? -1 : 1)) % 12;
        if (aCoverVariable < 0) {
            aCoverVariable = 11;
        }
        switch (aCoverVariable) {
            case 0 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("006", "Export"));
            case 1 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("007", "Import"));
            case 2 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("008", "Export (conditional)"));
            case 3 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("009", "Import (conditional)"));
            case 4 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("010", "Export (invert cond)"));
            case 5 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("011", "Import (invert cond)"));
            case 6 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("012", "Export allow Input"));
            case 7 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("013", "Import allow Output"));
            case 8 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("014", "Export allow Input (conditional)"));
            case 9 -> GT_Utility
                .sendChatToPlayer(aPlayer, GT_Utility.trans("015", "Import allow Output (conditional)"));
            case 10 -> GT_Utility
                .sendChatToPlayer(aPlayer, GT_Utility.trans("016", "Export allow Input (invert cond)"));
            case 11 -> GT_Utility
                .sendChatToPlayer(aPlayer, GT_Utility.trans("017", "Import allow Output (invert cond)"));
        }
        return aCoverVariable;
    }

    @Override
    public boolean letsRedstoneGoIn(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsRedstoneGoOut(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyIn(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyOut(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidIn(ForgeDirection aSide, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidOut(ForgeDirection aSide, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsIn(ForgeDirection aSide, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return (aCoverVariable >= 6) || (aCoverVariable % 2 != 0);
    }

    @Override
    public boolean letsItemsOut(ForgeDirection aSide, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return (aCoverVariable >= 6) || (aCoverVariable % 2 == 0);
    }

    @Override
    public boolean alwaysLookConnected(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return this.mTickRate;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(GT_CoverUIBuildContext buildContext) {
        return new ConveyorUIFactory(buildContext).createWindow();
    }

    private class ConveyorUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public ConveyorUIFactory(GT_CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            builder
                .widget(
                    new CoverDataControllerWidget.CoverDataIndexedControllerWidget_ToggleButtons<>(
                        this::getCoverData,
                        this::setCoverData,
                        GT_Cover_Conveyor.this,
                        (id, coverData) -> !getClickable(id, convert(coverData)),
                        (id, coverData) -> new ISerializableObject.LegacyCoverData(
                            getNewCoverVariable(id, convert(coverData))))
                                .addToggleButton(
                                    0,
                                    CoverDataFollower_ToggleButtonWidget.ofDisableable(),
                                    widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_EXPORT)
                                        .addTooltip(GT_Utility.trans("006", "Export"))
                                        .setPos(spaceX * 0, spaceY * 0))
                                .addToggleButton(
                                    1,
                                    CoverDataFollower_ToggleButtonWidget.ofDisableable(),
                                    widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_IMPORT)
                                        .addTooltip(GT_Utility.trans("007", "Import"))
                                        .setPos(spaceX * 1, spaceY * 0))
                                .addToggleButton(
                                    2,
                                    CoverDataFollower_ToggleButtonWidget.ofDisableable(),
                                    widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_CHECKMARK)
                                        .addTooltip(GT_Utility.trans("224", "Always On"))
                                        .setPos(spaceX * 0, spaceY * 1))
                                .addToggleButton(
                                    3,
                                    CoverDataFollower_ToggleButtonWidget.ofDisableable(),
                                    widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_REDSTONE_ON)
                                        .addTooltip(GT_Utility.trans("225", "Active with Redstone Signal"))
                                        .setPos(spaceX * 1, spaceY * 1))
                                .addToggleButton(
                                    4,
                                    CoverDataFollower_ToggleButtonWidget.ofDisableable(),
                                    widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_REDSTONE_OFF)
                                        .addTooltip(GT_Utility.trans("226", "Inactive with Redstone Signal"))
                                        .setPos(spaceX * 2, spaceY * 1))
                                .addToggleButton(
                                    5,
                                    CoverDataFollower_ToggleButtonWidget.ofDisableable(),
                                    widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_ALLOW_INPUT)
                                        .addTooltip(GT_Utility.trans("227", "Allow Input"))
                                        .setPos(spaceX * 0, spaceY * 2))
                                .addToggleButton(
                                    6,
                                    CoverDataFollower_ToggleButtonWidget.ofDisableable(),
                                    widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_BLOCK_INPUT)
                                        .addTooltip(GT_Utility.trans("228", "Block Input"))
                                        .setPos(spaceX * 1, spaceY * 2))
                                .setPos(startX, startY))
                .widget(
                    new TextWidget(GT_Utility.trans("229", "Import/Export")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 3, 3 + startY + spaceY * 0))
                .widget(
                    new TextWidget(GT_Utility.trans("230", "Conditional")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 3, 3 + startY + spaceY * 1))
                .widget(
                    new TextWidget(GT_Utility.trans("231", "Enable Input")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 3, 3 + startY + spaceY * 2));
        }

        private int getNewCoverVariable(int id, int coverVariable) {
            switch (id) {
                case 0 -> {
                    return coverVariable & ~0x1;
                }
                case 1 -> {
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

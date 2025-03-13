package tectech.thing.cover;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

import eu.usrv.yamcore.auxiliary.PlayerChatHelper;
import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject.LegacyCoverData;
import gregtech.common.covers.CoverBehavior;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import gtPlusPlus.core.tileentities.base.TileEntityBase;
import tectech.mechanics.enderStorage.EnderLinkTag;
import tectech.mechanics.enderStorage.EnderWorldSavedData;

public class CoverEnderFluidLink extends CoverBehavior {

    private static final int L_PER_TICK = 8000;
    private static final int IMPORT_EXPORT_MASK = 0b0001;
    private static final int PUBLIC_PRIVATE_MASK = 0b0010;

    public CoverEnderFluidLink(CoverContext context) {
        super(context);
    }

    private void transferFluid(IFluidHandler source, ForgeDirection coverSide, IFluidHandler target,
        ForgeDirection tSide, int amount) {
        FluidStack fluidStack = source.drain(coverSide, amount, false);

        if (fluidStack != null) {
            int fluidTransferred = target.fill(tSide, fluidStack, true);
            source.drain(coverSide, fluidTransferred, true);
        }
    }

    private static boolean testBit(int coverDataValue, int bitMask) {
        return (coverDataValue & bitMask) != 0;
    }

    private static int toggleBit(int coverDataValue, int bitMask) {
        return (coverDataValue ^ bitMask);
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        int coverDataValue = coverData.get();
        if ((coverable instanceof IFluidHandler teTank)) {
            EnderLinkTag tag = EnderWorldSavedData.getEnderLinkTag(teTank);

            if (tag != null) {
                boolean shouldBePrivate = testBit(coverDataValue, PUBLIC_PRIVATE_MASK);
                boolean isPrivate = tag.getUUID() != null;

                if (shouldBePrivate != isPrivate) {
                    tag = new EnderLinkTag(tag.getFrequency(), shouldBePrivate ? getOwner(coverable) : null);
                    EnderWorldSavedData.bindEnderLinkTag(teTank, tag);
                }

                IFluidHandler enderTank = EnderWorldSavedData.getEnderFluidContainer(tag);

                if (testBit(coverDataValue, IMPORT_EXPORT_MASK)) {
                    transferFluid(enderTank, ForgeDirection.UNKNOWN, teTank, coverSide, L_PER_TICK);
                } else {
                    transferFluid(teTank, coverSide, enderTank, ForgeDirection.UNKNOWN, L_PER_TICK);
                }
            }
        }
    }

    private static UUID getOwner(Object te) {
        if (te instanceof IGregTechTileEntity igte) {
            return igte.getOwnerUuid();
        } else if (te instanceof TileEntityBase teb) {
            return teb.getOwnerUUID();
        } else {
            return null;
        }
    }

    @Override
    public void onBaseTEDestroyed() {
        if (coveredTile.get() instanceof IFluidHandler fluidHandlerSelf) {
            EnderWorldSavedData.unbindTank(fluidHandlerSelf);
        }
    }

    @Override
    public void onCoverRemoval() {
        if (coveredTile.get() instanceof IFluidHandler fluidHandlerSelf) {
            EnderWorldSavedData.unbindTank(fluidHandlerSelf);
        }
    }

    @Override
    public String getDescription() {
        return "";
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
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int coverDataValue = coverData.get();
        int newCoverVariable = toggleBit(coverDataValue, IMPORT_EXPORT_MASK);

        if (testBit(coverDataValue, IMPORT_EXPORT_MASK)) {
            PlayerChatHelper.SendInfo(aPlayer, "Ender Suction Engaged!"); // TODO Translation support
        } else {
            PlayerChatHelper.SendInfo(aPlayer, "Ender Filling Engaged!");
        }
        coverData.set(newCoverVariable);
    }

    @Override
    public int getMinimumTickRate() {
        // Runs each tick
        return 1;
    }

    // region GUI

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        // Only open gui if we're placed on a fluid tank
        if (buildContext.getTile() instanceof IFluidHandler) {
            return new EnderFluidLinkUIFactory(buildContext).createWindow();
        }
        return null;
    }

    private class EnderFluidLinkUIFactory extends UIFactory {

        private static final int START_X = 10;
        private static final int START_Y = 25;
        private static final int SPACE_X = 18;
        private static final int SPACE_Y = 18;
        private static final int PUBLIC_BUTTON_ID = 0;
        private static final int PRIVATE_BUTTON_ID = 1;
        private static final int IMPORT_BUTTON_ID = 2;
        private static final int EXPORT_BUTTON_ID = 3;

        public EnderFluidLinkUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            TextFieldWidget frequencyField = new TextFieldWidget();
            builder.widget(frequencyField.setGetter(() -> {
                ICoverable te = getUIBuildContext().getTile();
                if (!frequencyField.isClient() && te instanceof IFluidHandler) {
                    EnderLinkTag tag = EnderWorldSavedData.getEnderLinkTag((IFluidHandler) te);

                    return tag == null ? "" : tag.getFrequency();
                }
                return "";
            })
                .setSetter(val -> {
                    if (!frequencyField.isClient() && getUIBuildContext().getTile() instanceof IFluidHandler tank) {
                        UUID uuid = null;

                        if (testBit(convert(getCoverData()), PUBLIC_PRIVATE_MASK)) {
                            uuid = getUUID();
                            if (!uuid.equals(getOwner(tank))) return;
                        }

                        EnderWorldSavedData.bindEnderLinkTag(tank, new EnderLinkTag(val, uuid));
                    }
                })
                .setTextColor(Color.WHITE.dark(1))
                .setTextAlignment(Alignment.CenterLeft)
                .setFocusOnGuiOpen(true)
                .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD.withOffset(-1, -1, 2, 2))
                .setPos(START_X + SPACE_X * 0, START_Y + SPACE_Y * 0)
                .setSize(SPACE_X * 5 - 8, 12))
                .widget(
                    new CoverDataControllerWidget.CoverDataIndexedControllerWidget_ToggleButtons<>(
                        this::getCoverData,
                        this::setCoverData,
                        CoverEnderFluidLink.this::loadFromNbt,
                        (id, coverData) -> !getClickable(id, convert(coverData)),
                        (id, coverData) -> new LegacyCoverData(getNewCoverVariable(id, convert(coverData))))
                            .addToggleButton(
                                PUBLIC_BUTTON_ID,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_WHITELIST)
                                    .addTooltip(GTUtility.trans("326", "Public"))
                                    .setPos(START_X + SPACE_X * 0, START_Y + SPACE_Y * 2))
                            .addToggleButton(
                                PRIVATE_BUTTON_ID,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_BLACKLIST)
                                    .addTooltip(GTUtility.trans("327", "Private"))
                                    .setPos(START_X + SPACE_X * 1, START_Y + SPACE_Y * 2))
                            .addToggleButton(
                                IMPORT_BUTTON_ID,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_IMPORT)
                                    .addTooltip(GTUtility.trans("007", "Import"))
                                    .setPos(START_X + SPACE_X * 0, START_Y + SPACE_Y * 3))
                            .addToggleButton(
                                EXPORT_BUTTON_ID,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_EXPORT)
                                    .addTooltip(GTUtility.trans("006", "Export"))
                                    .setPos(START_X + SPACE_X * 1, START_Y + SPACE_Y * 3)))
                .widget(
                    new TextWidget(GTUtility.trans("328", "Channel")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(START_X + SPACE_X * 5, 4 + START_Y + SPACE_Y * 0))
                .widget(
                    new TextWidget(GTUtility.trans("329", "Public/Private")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(START_X + SPACE_X * 2, 4 + START_Y + SPACE_Y * 2))
                .widget(
                    new TextWidget(GTUtility.trans("229", "Import/Export")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(START_X + SPACE_X * 2, 4 + START_Y + SPACE_Y * 3));
        }

        private int getNewCoverVariable(int id, int coverVariable) {
            return switch (id) {
                case PUBLIC_BUTTON_ID, PRIVATE_BUTTON_ID -> toggleBit(coverVariable, PUBLIC_PRIVATE_MASK);
                case IMPORT_BUTTON_ID, EXPORT_BUTTON_ID -> toggleBit(coverVariable, IMPORT_EXPORT_MASK);
                default -> coverVariable;
            };
        }

        private boolean getClickable(int id, int coverVariable) {
            return switch (id) {
                case PUBLIC_BUTTON_ID -> testBit(coverVariable, PUBLIC_PRIVATE_MASK);
                case PRIVATE_BUTTON_ID -> !testBit(coverVariable, PUBLIC_PRIVATE_MASK);
                case IMPORT_BUTTON_ID -> testBit(coverVariable, IMPORT_EXPORT_MASK);
                case EXPORT_BUTTON_ID -> !testBit(coverVariable, IMPORT_EXPORT_MASK);
                default -> false;
            };
        }

        private UUID getUUID() {
            return getUIBuildContext().getPlayer()
                .getUniqueID();
        }
    }
}

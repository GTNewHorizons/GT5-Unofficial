package gregtech.common.gui.mui1.cover;

import static net.minecraft.util.StatCollector.translateToLocal;
import static tectech.thing.cover.CoverEnderFluidLink.PUBLIC_PRIVATE_MASK;
import static tectech.thing.cover.CoverEnderFluidLink.toggleBit;

import java.util.UUID;

import net.minecraftforge.fluids.IFluidHandler;

import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.common.covers.CoverLegacyData;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import tectech.mechanics.enderStorage.EnderLinkTag;
import tectech.mechanics.enderStorage.EnderWorldSavedData;
import tectech.thing.cover.CoverEnderFluidLink;

public class EnderFluidLinkUIFactory extends CoverLegacyDataUIFactory {

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

                    CoverLegacyData cover = getCover();
                    if (cover != null && CoverEnderFluidLink.testBit(cover.getVariable(), PUBLIC_PRIVATE_MASK)) {
                        uuid = getUUID();
                        if (!uuid.equals(CoverEnderFluidLink.getOwner(tank))) return;
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
                    this::getCover,
                    (id, coverData) -> !getClickable(id, coverData.getVariable()),
                    (id, coverData) -> coverData.setVariable(getNewCoverVariable(id, coverData.getVariable())),
                    getUIBuildContext())
                        .addToggleButton(
                            PUBLIC_BUTTON_ID,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_WHITELIST)
                                .addTooltip(translateToLocal("gt.interact.desc.public"))
                                .setPos(START_X + SPACE_X * 0, START_Y + SPACE_Y * 2))
                        .addToggleButton(
                            PRIVATE_BUTTON_ID,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_BLACKLIST)
                                .addTooltip(translateToLocal("gt.interact.desc.private"))
                                .setPos(START_X + SPACE_X * 1, START_Y + SPACE_Y * 2))
                        .addToggleButton(
                            IMPORT_BUTTON_ID,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_IMPORT)
                                .addTooltip(translateToLocal("gt.interact.desc.import.tooltip"))
                                .setPos(START_X + SPACE_X * 0, START_Y + SPACE_Y * 3))
                        .addToggleButton(
                            EXPORT_BUTTON_ID,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_EXPORT)
                                .addTooltip(translateToLocal("gt.interact.desc.export.tooltip"))
                                .setPos(START_X + SPACE_X * 1, START_Y + SPACE_Y * 3)))
            .widget(
                new TextWidget(translateToLocal("gt.interact.desc.channel"))
                    .setPos(START_X + SPACE_X * 5, 4 + START_Y + SPACE_Y * 0))
            .widget(
                new TextWidget(translateToLocal("gt.interact.desc.set_perm"))
                    .setPos(START_X + SPACE_X * 2, 4 + START_Y + SPACE_Y * 2))
            .widget(
                new TextWidget(translateToLocal("gt.interact.desc.set_io"))
                    .setPos(START_X + SPACE_X * 2, 4 + START_Y + SPACE_Y * 3));
    }

    private int getNewCoverVariable(int id, int coverVariable) {
        return switch (id) {
            case PUBLIC_BUTTON_ID, PRIVATE_BUTTON_ID -> toggleBit(coverVariable, PUBLIC_PRIVATE_MASK);
            case IMPORT_BUTTON_ID, EXPORT_BUTTON_ID -> toggleBit(coverVariable, CoverEnderFluidLink.IMPORT_EXPORT_MASK);
            default -> coverVariable;
        };
    }

    private boolean getClickable(int id, int coverVariable) {
        return switch (id) {
            case PUBLIC_BUTTON_ID -> CoverEnderFluidLink.testBit(coverVariable, PUBLIC_PRIVATE_MASK);
            case PRIVATE_BUTTON_ID -> !CoverEnderFluidLink.testBit(coverVariable, PUBLIC_PRIVATE_MASK);
            case IMPORT_BUTTON_ID -> !CoverEnderFluidLink
                .testBit(coverVariable, CoverEnderFluidLink.IMPORT_EXPORT_MASK);
            case EXPORT_BUTTON_ID -> CoverEnderFluidLink.testBit(coverVariable, CoverEnderFluidLink.IMPORT_EXPORT_MASK);
            default -> false;
        };
    }

    private UUID getUUID() {
        return getUIBuildContext().getPlayer()
            .getUniqueID();
    }
}

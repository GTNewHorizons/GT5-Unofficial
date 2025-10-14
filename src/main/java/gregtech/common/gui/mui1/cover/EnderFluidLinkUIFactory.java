package gregtech.common.gui.mui1.cover;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.UUID;

import net.minecraftforge.fluids.IFluidHandler;

import org.jetbrains.annotations.Nullable;

import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.common.covers.Cover;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import tectech.mechanics.enderStorage.EnderLinkTag;
import tectech.mechanics.enderStorage.EnderWorldSavedData;
import tectech.thing.cover.CoverEnderFluidLink;

public class EnderFluidLinkUIFactory extends CoverUIFactory<CoverEnderFluidLink> {

    private static final int START_X = 10;
    private static final int START_Y = 25;
    private static final int SPACE_X = 18;
    private static final int SPACE_Y = 18;

    public EnderFluidLinkUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @Override
    protected @Nullable CoverEnderFluidLink adaptCover(Cover cover) {
        if (cover instanceof CoverEnderFluidLink adapterCover) {
            return adapterCover;
        }
        return null;
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

                    CoverEnderFluidLink cover = getCover();
                    if (cover != null && cover.isPrivateChannel()) {
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
                new CoverDataControllerWidget<>(this::getCover, getUIBuildContext()).addFollower(
                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                    cover -> !cover.isPrivateChannel(),
                    (cover, state) -> {
                        cover.setPrivateChannel(!state);
                        return cover;
                    },
                    widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_WHITELIST)
                        .addTooltip(translateToLocal("gt.interact.desc.public"))
                        .setPos(START_X + SPACE_X * 0, START_Y + SPACE_Y * 2))

                    .addFollower(
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        CoverEnderFluidLink::isPrivateChannel,
                        (cover, state) -> {
                            cover.setPrivateChannel(state);
                            return cover;
                        },
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_BLACKLIST)
                            .addTooltip(translateToLocal("gt.interact.desc.private"))
                            .setPos(START_X + SPACE_X * 1, START_Y + SPACE_Y * 2))
                    .addFollower(
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        cover -> !cover.isExport(),
                        (cover, state) -> {
                            cover.setExport(!state);
                            return cover;
                        },
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_IMPORT)
                            .addTooltip(translateToLocal("gt.interact.desc.import.tooltip"))
                            .setPos(START_X + SPACE_X * 0, START_Y + SPACE_Y * 3))
                    .addFollower(
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        CoverEnderFluidLink::isExport,
                        (cover, state) -> {
                            cover.setExport(state);
                            return cover;
                        },
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

    private UUID getUUID() {
        return getUIBuildContext().getPlayer()
            .getUniqueID();
    }
}

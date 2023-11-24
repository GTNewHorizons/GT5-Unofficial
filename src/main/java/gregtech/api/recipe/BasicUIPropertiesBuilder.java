package gregtech.api.recipe;

import java.awt.Rectangle;
import java.util.Collections;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import gregtech.api.gui.modularui.FallbackableSteamTexture;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.gui.modularui.SteamTexture;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;

/**
 * Builder class for {@link BasicUIProperties}.
 */
@SuppressWarnings("UnusedReturnValue")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class BasicUIPropertiesBuilder {

    private int maxItemInputs, maxItemOutputs, maxFluidInputs, maxFluidOutputs;

    private BasicUIProperties.SlotOverlayGetter<IDrawable> slotOverlays = (index, isFluid, isOutput, isSpecial) -> null;
    private BasicUIProperties.SlotOverlayGetter<SteamTexture> slotOverlaysSteam = (index, isFluid, isOutput,
        isSpecial) -> null;

    @Nullable
    private FallbackableUITexture progressBarTexture;
    @Nullable
    private FallbackableSteamTexture progressBarTextureSteam;
    private ProgressBar.Direction progressBarDirection = ProgressBar.Direction.RIGHT;
    private Size progressBarSize = new Size(20, 18);
    private Pos2d progressBarPos = new Pos2d(78, 24);

    private boolean useSpecialSlot;

    private final ImmutableList.Builder<Rectangle> neiTransferRect = ImmutableList.builder();
    @Nullable
    private String neiTransferRectId;

    private final ImmutableList.Builder<Pair<IDrawable, Pair<Size, Pos2d>>> specialTextures = ImmutableList.builder();
    private final ImmutableList.Builder<Pair<SteamTexture, Pair<Size, Pos2d>>> specialTexturesSteam = ImmutableList
        .builder();

    private IDrawable logo = GT_UITextures.PICTURE_GT_LOGO_17x17_TRANSPARENT;
    private Size logoSize = new Size(17, 17);
    private Pos2d logoPos = new Pos2d(152, 63);

    private IntFunction<List<Pos2d>> itemInputPositionsGetter = UIHelper::getItemInputPositions;
    private IntFunction<List<Pos2d>> itemOutputPositionsGetter = UIHelper::getItemOutputPositions;
    private Supplier<Pos2d> specialItemPositionGetter = UIHelper::getSpecialItemPosition;
    private IntFunction<List<Pos2d>> fluidInputPositionsGetter = UIHelper::getFluidInputPositions;
    private IntFunction<List<Pos2d>> fluidOutputPositionsGetter = UIHelper::getFluidOutputPositions;

    private int amperage = 1;

    BasicUIPropertiesBuilder() {}

    public BasicUIProperties build() {
        if (maxItemInputs == 0 && maxItemOutputs == 0 && maxFluidInputs == 0 && maxFluidOutputs == 0) {
            throw new IllegalArgumentException("Set either of max I/O count");
        }
        List<Rectangle> builtNEITransferRect = neiTransferRect.build();
        if (builtNEITransferRect.isEmpty()) {
            builtNEITransferRect = Collections.singletonList(
                new Rectangle(
                    progressBarPos.x - (16 / 2),
                    progressBarPos.y,
                    progressBarSize.width + 16,
                    progressBarSize.height));
        }
        return new BasicUIProperties(
            maxItemInputs,
            maxItemOutputs,
            maxFluidInputs,
            maxFluidOutputs,
            slotOverlays,
            slotOverlaysSteam,
            progressBarTexture,
            progressBarTextureSteam,
            progressBarDirection,
            progressBarSize,
            progressBarPos,
            builtNEITransferRect,
            neiTransferRectId,
            useSpecialSlot,
            specialTextures.build(),
            specialTexturesSteam.build(),
            logo,
            logoSize,
            logoPos,
            itemInputPositionsGetter,
            itemOutputPositionsGetter,
            specialItemPositionGetter,
            fluidInputPositionsGetter,
            fluidOutputPositionsGetter,
            amperage);
    }

    public BasicUIPropertiesBuilder maxItemInputs(int maxItemInputs) {
        this.maxItemInputs = maxItemInputs;
        return this;
    }

    public BasicUIPropertiesBuilder maxItemOutputs(int maxItemOutputs) {
        this.maxItemOutputs = maxItemOutputs;
        return this;
    }

    public BasicUIPropertiesBuilder maxFluidInputs(int maxFluidInputs) {
        this.maxFluidInputs = maxFluidInputs;
        return this;
    }

    public BasicUIPropertiesBuilder maxFluidOutputs(int maxFluidOutputs) {
        this.maxFluidOutputs = maxFluidOutputs;
        return this;
    }

    public BasicUIPropertiesBuilder slotOverlays(BasicUIProperties.SlotOverlayGetter<IDrawable> slotOverlays) {
        this.slotOverlays = slotOverlays;
        return this;
    }

    public BasicUIPropertiesBuilder slotOverlaysSteam(
        BasicUIProperties.SlotOverlayGetter<SteamTexture> slotOverlaysSteam) {
        this.slotOverlaysSteam = slotOverlaysSteam;
        return this;
    }

    public BasicUIPropertiesBuilder progressBarTexture(@Nullable FallbackableUITexture progressBarTexture) {
        this.progressBarTexture = progressBarTexture;
        return this;
    }

    public BasicUIPropertiesBuilder progressBarTextureSteam(
        @Nullable FallbackableSteamTexture progressBarTextureSteam) {
        this.progressBarTextureSteam = progressBarTextureSteam;
        return this;
    }

    public BasicUIPropertiesBuilder progressBarDirection(ProgressBar.Direction progressBarDirection) {
        this.progressBarDirection = progressBarDirection;
        return this;
    }

    public BasicUIPropertiesBuilder progressBarSize(Size progressBarSize) {
        this.progressBarSize = progressBarSize;
        return this;
    }

    public BasicUIPropertiesBuilder progressBarPos(Pos2d progressBarPos) {
        this.progressBarPos = progressBarPos;
        return this;
    }

    public BasicUIPropertiesBuilder useSpecialSlot(boolean useSpecialSlot) {
        this.useSpecialSlot = useSpecialSlot;
        return this;
    }

    public BasicUIPropertiesBuilder addNEITransferRect(Rectangle neiTransferRect) {
        this.neiTransferRect.add(neiTransferRect);
        return this;
    }

    BasicUIPropertiesBuilder neiTransferRect(List<Rectangle> neiTransferRect) {
        this.neiTransferRect.addAll(neiTransferRect);
        return this;
    }

    public BasicUIPropertiesBuilder neiTransferRectId(@Nullable String neiTransferRectId) {
        this.neiTransferRectId = neiTransferRectId;
        return this;
    }

    public BasicUIPropertiesBuilder addSpecialTexture(Size size, Pos2d pos, IDrawable texture) {
        this.specialTextures.add(new ImmutablePair<>(texture, new ImmutablePair<>(size, pos)));
        return this;
    }

    BasicUIPropertiesBuilder specialTextures(List<Pair<IDrawable, Pair<Size, Pos2d>>> specialTextures) {
        this.specialTextures.addAll(specialTextures);
        return this;
    }

    public BasicUIPropertiesBuilder addSpecialTextureSteam(Size size, Pos2d pos, SteamTexture texture) {
        this.specialTexturesSteam.add(new ImmutablePair<>(texture, new ImmutablePair<>(size, pos)));
        return this;
    }

    BasicUIPropertiesBuilder specialTexturesSteam(List<Pair<SteamTexture, Pair<Size, Pos2d>>> specialTextures) {
        this.specialTexturesSteam.addAll(specialTextures);
        return this;
    }

    public BasicUIPropertiesBuilder logo(IDrawable logo) {
        this.logo = logo;
        return this;
    }

    public BasicUIPropertiesBuilder logoSize(Size logoSize) {
        this.logoSize = logoSize;
        return this;
    }

    public BasicUIPropertiesBuilder logoPos(Pos2d logoPos) {
        this.logoPos = logoPos;
        return this;
    }

    public BasicUIPropertiesBuilder itemInputPositionsGetter(IntFunction<List<Pos2d>> itemInputPositionsGetter) {
        this.itemInputPositionsGetter = itemInputPositionsGetter;
        return this;
    }

    public BasicUIPropertiesBuilder itemOutputPositionsGetter(IntFunction<List<Pos2d>> itemOutputPositionsGetter) {
        this.itemOutputPositionsGetter = itemOutputPositionsGetter;
        return this;
    }

    public BasicUIPropertiesBuilder specialItemPositionGetter(Supplier<Pos2d> specialItemPositionGetter) {
        this.specialItemPositionGetter = specialItemPositionGetter;
        return this;
    }

    public BasicUIPropertiesBuilder fluidInputPositionsGetter(IntFunction<List<Pos2d>> fluidInputPositionsGetter) {
        this.fluidInputPositionsGetter = fluidInputPositionsGetter;
        return this;
    }

    public BasicUIPropertiesBuilder fluidOutputPositionsGetter(IntFunction<List<Pos2d>> fluidOutputPositionsGetter) {
        this.fluidOutputPositionsGetter = fluidOutputPositionsGetter;
        return this;
    }

    public BasicUIPropertiesBuilder amperage(int amperage) {
        this.amperage = amperage;
        return this;
    }
}

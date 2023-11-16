package gregtech.api.recipe;

import java.awt.Rectangle;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang3.tuple.Pair;

import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import gregtech.api.gui.modularui.FallbackableSteamTexture;
import gregtech.api.gui.modularui.SteamTexture;
import gregtech.api.util.FieldsAreNonnullByDefault;
import gregtech.api.util.MethodsReturnNonnullByDefault;

/**
 * Data object to store properties, used to draw both basic machine GUI and NEI recipe GUI, mainly GUI widgets.
 * Not all the info used to draw NEI are listed here, see also {@link NEIRecipeProperties}.
 * <p>
 * Use {@link #builder()} for creation.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@FieldsAreNonnullByDefault
public final class BasicUIProperties {

    /**
     * Starts constructing BasicUIProperties.
     */
    public static BasicUIPropertiesBuilder builder() {
        return new BasicUIPropertiesBuilder();
    }

    /**
     * Creates new builder from this instance.
     */
    public BasicUIPropertiesBuilder toBuilder() {
        return new BasicUIPropertiesBuilder().maxItemInputs(maxItemInputs)
            .maxItemOutputs(maxItemOutputs)
            .maxFluidInputs(maxFluidInputs)
            .maxFluidOutputs(maxFluidOutputs)
            .slotOverlays(slotOverlays)
            .slotOverlaysSteam(slotOverlaysSteam)
            .progressBarTexture(progressBarTexture)
            .progressBarTextureSteam(progressBarTextureSteam)
            .progressBarDirection(progressBarDirection)
            .progressBarSize(progressBarSize)
            .progressBarPos(progressBarPos)
            .useSpecialSlot(useSpecialSlot)
            .neiTransferRect(neiTransferRect)
            .neiTransferRectId(neiTransferRectId)
            .specialTextures(specialTextures)
            .specialTexturesSteam(specialTexturesSteam)
            .logo(logo)
            .logoSize(logoSize)
            .logoPos(logoPos)
            .itemInputPositionsGetter(itemInputPositionsGetter)
            .itemOutputPositionsGetter(itemOutputPositionsGetter)
            .specialItemPositionGetter(specialItemPositionGetter)
            .fluidInputPositionsGetter(fluidInputPositionsGetter)
            .fluidOutputPositionsGetter(fluidOutputPositionsGetter)
            .amperage(amperage);
    }

    /**
     * How many item inputs does this recipemap usually has at most.
     * It does not actually restrict number of items used in the recipe.
     */
    public final int maxItemInputs;
    /**
     * How many item outputs does this recipemap usually has at most.
     * It does not actually restrict number of items used in the recipe.
     */
    public final int maxItemOutputs;
    /**
     * How many fluid inputs does this recipemap usually has at most.
     * It does not actually restrict number of items used in the recipe.
     */
    public final int maxFluidInputs;
    /**
     * How many fluid outputs does this recipemap usually has at most.
     * It does not actually restrict number of items used in the recipe.
     */
    public final int maxFluidOutputs;

    private final SlotOverlayGetter<IDrawable> slotOverlays;
    private final SlotOverlayGetter<SteamTexture> slotOverlaysSteam;

    /**
     * Progressbar used for BasicMachine GUI and NEI.
     */
    @Nullable
    public final FallbackableUITexture progressBarTexture;
    /**
     * Progressbar used for steam machine GUI.
     */
    @Nullable
    public final FallbackableSteamTexture progressBarTextureSteam;
    /**
     * Direction of progressbar animation.
     */
    public final ProgressBar.Direction progressBarDirection;
    /**
     * Size of the progressbar. (20, 36) by default.
     */
    public final Size progressBarSize;
    /**
     * Position of the progressbar. (78, 24) by default.
     */
    public final Pos2d progressBarPos;
    /**
     * Image size in the direction of progressbar. Used for non-smooth rendering.
     */
    public final int progressBarImageSize;

    /**
     * If special slot has its usage for this GUI.
     */
    public final boolean useSpecialSlot;

    /**
     * GUI area where clicking shows up all the recipes available.
     */
    public final Rectangle neiTransferRect;
    /**
     * ID used to open NEI recipe GUI when progressbar is clicked.
     */
    @Nullable
    public final String neiTransferRectId;

    /**
     * Additional textures shown on GUI.
     */
    public final List<Pair<IDrawable, Pair<Size, Pos2d>>> specialTextures;
    /**
     * Additional textures shown on steam machine GUI.
     */
    public final List<Pair<SteamTexture, Pair<Size, Pos2d>>> specialTexturesSteam;

    /**
     * Logo shown on GUI. GregTech logo by default.
     */
    public final IDrawable logo;
    /**
     * Size of logo. (17, 17) by default.
     */
    public final Size logoSize;
    /**
     * Position of logo. (152, 63) by default.
     */
    public final Pos2d logoPos;

    public final IntFunction<List<Pos2d>> itemInputPositionsGetter;
    public final IntFunction<List<Pos2d>> itemOutputPositionsGetter;
    public final Supplier<Pos2d> specialItemPositionGetter;
    public final IntFunction<List<Pos2d>> fluidInputPositionsGetter;
    public final IntFunction<List<Pos2d>> fluidOutputPositionsGetter;

    /**
     * Amperage for the recipemap. Even though this is placed at frontend because backend logic doesn't need it,
     * some machine logic also use this variable.
     */
    public final int amperage;

    BasicUIProperties(int maxItemInputs, int maxItemOutputs, int maxFluidInputs, int maxFluidOutputs,
        SlotOverlayGetter<IDrawable> slotOverlays, SlotOverlayGetter<SteamTexture> slotOverlaysSteam,
        @Nullable FallbackableUITexture progressBarTexture, @Nullable FallbackableSteamTexture progressBarTextureSteam,
        ProgressBar.Direction progressBarDirection, Size progressBarSize, Pos2d progressBarPos,
        Rectangle neiTransferRect, @Nullable String neiTransferRectId, boolean useSpecialSlot,
        List<Pair<IDrawable, Pair<Size, Pos2d>>> specialTextures,
        List<Pair<SteamTexture, Pair<Size, Pos2d>>> specialTexturesSteam, IDrawable logo, Size logoSize, Pos2d logoPos,
        IntFunction<List<Pos2d>> itemInputPositionsGetter, IntFunction<List<Pos2d>> itemOutputPositionsGetter,
        Supplier<Pos2d> specialItemPositionGetter, IntFunction<List<Pos2d>> fluidInputPositionsGetter,
        IntFunction<List<Pos2d>> fluidOutputPositionsGetter, int amperage) {
        if (maxItemInputs < 0 || maxItemOutputs < 0 || maxFluidInputs < 0 || maxFluidOutputs < 0) {
            throw new IllegalArgumentException(
                "maxItemInputs, maxItemOutputs, maxFluidInputs and maxFluidOutputs cannot be negative");
        }
        if (amperage < 1) {
            throw new IllegalArgumentException("Amperage cannot be lower than 1");
        }
        this.maxItemInputs = maxItemInputs;
        this.maxItemOutputs = maxItemOutputs;
        this.maxFluidInputs = maxFluidInputs;
        this.maxFluidOutputs = maxFluidOutputs;
        this.slotOverlays = slotOverlays;
        this.slotOverlaysSteam = slotOverlaysSteam;
        this.progressBarTexture = progressBarTexture;
        this.progressBarTextureSteam = progressBarTextureSteam;
        this.progressBarDirection = progressBarDirection;
        this.progressBarSize = progressBarSize;
        this.progressBarPos = progressBarPos;
        this.neiTransferRect = neiTransferRect;
        this.neiTransferRectId = neiTransferRectId;
        this.useSpecialSlot = useSpecialSlot;
        this.specialTextures = specialTextures;
        this.specialTexturesSteam = specialTexturesSteam;
        this.logo = logo;
        this.logoSize = logoSize;
        this.logoPos = logoPos;
        this.itemInputPositionsGetter = itemInputPositionsGetter;
        this.itemOutputPositionsGetter = itemOutputPositionsGetter;
        this.specialItemPositionGetter = specialItemPositionGetter;
        this.fluidInputPositionsGetter = fluidInputPositionsGetter;
        this.fluidOutputPositionsGetter = fluidOutputPositionsGetter;
        this.amperage = amperage;

        this.progressBarImageSize = switch (progressBarDirection) {
            case UP, DOWN -> progressBarSize.height;
            case CIRCULAR_CW -> Math.max(progressBarSize.width, progressBarSize.height);
            default -> progressBarSize.width;
        };
    }

    /**
     * Retrieves overlay for slot, with given matching conditions.
     */
    @Nullable
    public IDrawable getOverlayForSlot(int index, boolean isFluid, boolean isOutput, boolean isSpecial) {
        return slotOverlays.apply(index, isFluid, isOutput, isSpecial);
    }

    /**
     * Retrieves overlay for slot of steam machines, with given matching conditions.
     */
    @Nullable
    public SteamTexture getOverlayForSlotSteam(int index, boolean isFluid, boolean isOutput, boolean isSpecial) {
        return slotOverlaysSteam.apply(index, isFluid, isOutput, isSpecial);
    }

    public interface SlotOverlayGetter<T> {

        @Nullable
        T apply(int index, boolean isFluid, boolean isOutput, boolean isSpecial);
    }
}

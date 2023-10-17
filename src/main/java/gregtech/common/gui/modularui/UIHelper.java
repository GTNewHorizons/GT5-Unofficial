package gregtech.common.gui.modularui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.enums.SteamVariant;
import gregtech.api.gui.modularui.SteamTexture;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class UIHelper {

    /**
     * Iterates over candidates for slot placement.
     */
    public static void forEachSlots(ForEachSlot forEachItemInputSlot, ForEachSlot forEachItemOutputSlot,
        ForEachSlot forEachSpecialSlot, ForEachSlot forEachFluidInputSlot, ForEachSlot forEachFluidOutputSlot,
        IDrawable itemSlotBackground, IDrawable fluidSlotBackground, @Nullable RecipeMap<?> recipeMap,
        int itemInputCount, int itemOutputCount, int fluidInputCount, int fluidOutputCount, SteamVariant steamVariant,
        Pos2d offset) {
        forEachSlots(
            forEachItemInputSlot,
            forEachItemOutputSlot,
            forEachSpecialSlot,
            forEachFluidInputSlot,
            forEachFluidOutputSlot,
            itemSlotBackground,
            fluidSlotBackground,
            recipeMap != null ? recipeMap.getFrontend() : null,
            itemInputCount,
            itemOutputCount,
            fluidInputCount,
            fluidOutputCount,
            steamVariant,
            offset);
    }

    /**
     * Iterates over candidates for slot placement.
     */
    public static void forEachSlots(ForEachSlot forEachItemInputSlot, ForEachSlot forEachItemOutputSlot,
        ForEachSlot forEachSpecialSlot, ForEachSlot forEachFluidInputSlot, ForEachSlot forEachFluidOutputSlot,
        IDrawable itemSlotBackground, IDrawable fluidSlotBackground, @Nullable RecipeMapFrontend frontend,
        int itemInputCount, int itemOutputCount, int fluidInputCount, int fluidOutputCount, SteamVariant steamVariant,
        Pos2d offset) {
        List<Pos2d> itemInputPositions = frontend != null ? frontend.getItemInputPositions(itemInputCount)
            : UIHelper.getItemInputPositions(itemInputCount);
        itemInputPositions = itemInputPositions.stream()
            .map(p -> p.add(offset))
            .collect(Collectors.toList());
        for (int i = 0; i < itemInputPositions.size(); i++) {
            forEachItemInputSlot.accept(
                i,
                getBackgroundsForSlot(itemSlotBackground, frontend, false, false, i, false, steamVariant),
                itemInputPositions.get(i));
        }

        List<Pos2d> itemOutputPositions = frontend != null ? frontend.getItemOutputPositions(itemOutputCount)
            : UIHelper.getItemOutputPositions(itemOutputCount);
        itemOutputPositions = itemOutputPositions.stream()
            .map(p -> p.add(offset))
            .collect(Collectors.toList());
        for (int i = 0; i < itemOutputPositions.size(); i++) {
            forEachItemOutputSlot.accept(
                i,
                getBackgroundsForSlot(itemSlotBackground, frontend, false, true, i, false, steamVariant),
                itemOutputPositions.get(i));
        }

        forEachSpecialSlot.accept(
            0,
            getBackgroundsForSlot(itemSlotBackground, frontend, false, false, 0, true, steamVariant),
            (frontend != null ? frontend.getSpecialItemPosition() : UIHelper.getSpecialItemPosition()).add(offset));

        List<Pos2d> fluidInputPositions = frontend != null ? frontend.getFluidInputPositions(fluidInputCount)
            : UIHelper.getFluidInputPositions(fluidInputCount);
        fluidInputPositions = fluidInputPositions.stream()
            .map(p -> p.add(offset))
            .collect(Collectors.toList());
        for (int i = 0; i < fluidInputPositions.size(); i++) {
            forEachFluidInputSlot.accept(
                i,
                getBackgroundsForSlot(fluidSlotBackground, frontend, true, false, i, false, steamVariant),
                fluidInputPositions.get(i));
        }

        List<Pos2d> fluidOutputPositions = frontend != null ? frontend.getFluidOutputPositions(fluidOutputCount)
            : UIHelper.getFluidOutputPositions(fluidOutputCount);
        fluidOutputPositions = fluidOutputPositions.stream()
            .map(p -> p.add(offset))
            .collect(Collectors.toList());
        for (int i = 0; i < fluidOutputPositions.size(); i++) {
            forEachFluidOutputSlot.accept(
                i,
                getBackgroundsForSlot(fluidSlotBackground, frontend, true, true, i, false, steamVariant),
                fluidOutputPositions.get(i));
        }
    }

    /**
     * @return Display positions for GUI, including border (18x18 size)
     */
    public static List<Pos2d> getItemInputPositions(int itemInputCount) {
        return switch (itemInputCount) {
            case 0 -> Collections.emptyList();
            case 1 -> getGridPositions(itemInputCount, 52, 24, 1, 1);
            case 2 -> getGridPositions(itemInputCount, 34, 24, 2, 1);
            case 3 -> getGridPositions(itemInputCount, 16, 24, 3, 1);
            case 4 -> getGridPositions(itemInputCount, 34, 15, 2, 2);
            case 5, 6 -> getGridPositions(itemInputCount, 16, 15, 3, 2);
            default -> getGridPositions(itemInputCount, 16, 6, 3);
        };
    }

    /**
     * @return Display positions for GUI, including border (18x18 size)
     */
    public static List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return switch (itemOutputCount) {
            case 0 -> Collections.emptyList();
            case 1 -> getGridPositions(itemOutputCount, 106, 24, 1, 1);
            case 2 -> getGridPositions(itemOutputCount, 106, 24, 2, 1);
            case 3 -> getGridPositions(itemOutputCount, 106, 24, 3, 1);
            case 4 -> getGridPositions(itemOutputCount, 106, 15, 2, 2);
            case 5, 6 -> getGridPositions(itemOutputCount, 106, 15, 3, 2);
            default -> getGridPositions(itemOutputCount, 106, 6, 3);
        };
    }

    /**
     * @return Display position for GUI, including border (18x18 size)
     */
    public static Pos2d getSpecialItemPosition() {
        return new Pos2d(124, 62);
    }

    /**
     * @return Display positions for GUI, including border (18x18 size)
     */
    public static List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        List<Pos2d> results = new ArrayList<>();
        int x = 52;
        for (int i = 0; i < fluidInputCount; i++) {
            results.add(new Pos2d(x, 62));
            x -= 18;
        }
        return results;
    }

    /**
     * @return Display positions for GUI, including border (18x18 size)
     */
    public static List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        List<Pos2d> results = new ArrayList<>();
        int x = 106;
        for (int i = 0; i < fluidOutputCount; i++) {
            results.add(new Pos2d(x, 62));
            x += 18;
        }
        return results;
    }

    public static List<Pos2d> getGridPositions(int totalCount, int xOrigin, int yOrigin, int xDirMaxCount,
        int yDirMaxCount) {
        // 18 pixels to get to a new grid for placing an item tile since they are 16x16 and have 1 pixel buffers
        // around them.
        int distance = 18;

        List<Pos2d> results = new ArrayList<>();
        int count = 0;
        loop: for (int j = 0; j < yDirMaxCount; j++) {
            for (int i = 0; i < xDirMaxCount; i++) {
                if (count >= totalCount) break loop;
                results.add(new Pos2d(xOrigin + i * distance, yOrigin + j * distance));
                count++;
            }
        }
        return results;
    }

    public static List<Pos2d> getGridPositions(int totalCount, int xOrigin, int yOrigin, int xDirMaxCount) {
        return getGridPositions(totalCount, xOrigin, yOrigin, xDirMaxCount, 100);
    }

    /**
     * @deprecated Renamed to {@link #getGridPositions}
     */
    @Deprecated
    public static List<Pos2d> getItemGridPositions(int itemCount, int xOrigin, int yOrigin, int xDirMaxCount,
        int yDirMaxCount) {
        return getGridPositions(itemCount, xOrigin, yOrigin, xDirMaxCount, yDirMaxCount);
    }

    private static IDrawable[] getBackgroundsForSlot(IDrawable base, @Nullable RecipeMapFrontend frontend,
        boolean isFluid, boolean isOutput, int index, boolean isSpecial, SteamVariant steamVariant) {
        IDrawable overlay = getOverlay(frontend, isFluid, isOutput, index, isSpecial, steamVariant);
        if (overlay != null) {
            return new IDrawable[] { base, overlay };
        } else {
            return new IDrawable[] { base };
        }
    }

    @Nullable
    private static IDrawable getOverlay(@Nullable RecipeMapFrontend frontend, boolean isFluid, boolean isOutput,
        int index, boolean isSpecial, SteamVariant steamVariant) {
        if (frontend == null) {
            return null;
        }
        if (isSpecial && !frontend.getUIProperties().useSpecialSlot) {
            return null;
        }
        if (steamVariant != SteamVariant.NONE) {
            SteamTexture steamTexture = frontend.getUIProperties()
                .getOverlayForSlotSteam(index, isFluid, isOutput, isSpecial);
            if (steamTexture != null) {
                return steamTexture.get(steamVariant);
            } else {
                return null;
            }
        } else {
            return frontend.getUIProperties()
                .getOverlayForSlot(index, isFluid, isOutput, isSpecial);
        }
    }

    @FunctionalInterface
    public interface ForEachSlot {

        void accept(int index, IDrawable[] backgrounds, Pos2d pos);
    }
}

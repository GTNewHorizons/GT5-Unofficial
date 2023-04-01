package gregtech.common.gui.modularui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.enums.SteamVariant;
import gregtech.api.gui.modularui.SteamTexture;
import gregtech.api.util.GT_Recipe;

public class UIHelper {

    /**
     * Iterates over candidates for slot placement.
     */
    public static void forEachSlots(ForEachSlot forEachItemInputSlot, ForEachSlot forEachItemOutputSlot,
            ForEachSlot forEachSpecialSlot, ForEachSlot forEachFluidInputSlot, ForEachSlot forEachFluidOutputSlot,
            IDrawable itemSlotBackground, IDrawable fluidSlotBackground, @Nullable GT_Recipe.GT_Recipe_Map recipeMap,
            int itemInputCount, int itemOutputCount, int fluidInputCount, int fluidOutputCount,
            SteamVariant steamVariant, Pos2d offset) {
        List<Pos2d> itemInputPositions = recipeMap != null ? recipeMap.getItemInputPositions(itemInputCount)
                : UIHelper.getItemInputPositions(itemInputCount);
        itemInputPositions = itemInputPositions.stream()
                                               .map(p -> p.add(offset))
                                               .collect(Collectors.toList());
        for (int i = 0; i < itemInputPositions.size(); i++) {
            forEachItemInputSlot.accept(
                    i,
                    getBackgroundsForSlot(itemSlotBackground, recipeMap, false, false, i, false, steamVariant),
                    itemInputPositions.get(i));
        }

        List<Pos2d> itemOutputPositions = recipeMap != null ? recipeMap.getItemOutputPositions(itemOutputCount)
                : UIHelper.getItemOutputPositions(itemOutputCount);
        itemOutputPositions = itemOutputPositions.stream()
                                                 .map(p -> p.add(offset))
                                                 .collect(Collectors.toList());
        for (int i = 0; i < itemOutputPositions.size(); i++) {
            forEachItemOutputSlot.accept(
                    i,
                    getBackgroundsForSlot(itemSlotBackground, recipeMap, false, true, i, false, steamVariant),
                    itemOutputPositions.get(i));
        }

        forEachSpecialSlot.accept(
                0,
                getBackgroundsForSlot(itemSlotBackground, recipeMap, false, false, 0, true, steamVariant),
                (recipeMap != null ? recipeMap.getSpecialItemPosition() : UIHelper.getSpecialItemPosition()).add(
                        offset));

        List<Pos2d> fluidInputPositions = recipeMap != null ? recipeMap.getFluidInputPositions(fluidInputCount)
                : UIHelper.getFluidInputPositions(fluidInputCount);
        fluidInputPositions = fluidInputPositions.stream()
                                                 .map(p -> p.add(offset))
                                                 .collect(Collectors.toList());
        for (int i = 0; i < fluidInputPositions.size(); i++) {
            forEachFluidInputSlot.accept(
                    i,
                    getBackgroundsForSlot(fluidSlotBackground, recipeMap, true, false, i, false, steamVariant),
                    fluidInputPositions.get(i));
        }

        List<Pos2d> fluidOutputPositions = recipeMap != null ? recipeMap.getFluidOutputPositions(fluidOutputCount)
                : UIHelper.getFluidOutputPositions(fluidOutputCount);
        fluidOutputPositions = fluidOutputPositions.stream()
                                                   .map(p -> p.add(offset))
                                                   .collect(Collectors.toList());
        for (int i = 0; i < fluidOutputPositions.size(); i++) {
            forEachFluidOutputSlot.accept(
                    i,
                    getBackgroundsForSlot(fluidSlotBackground, recipeMap, true, true, i, false, steamVariant),
                    fluidOutputPositions.get(i));
        }
    }

    /**
     * @return Display positions for GUI, including border (18x18 size)
     */
    public static List<Pos2d> getItemInputPositions(int itemInputCount) {
        switch (itemInputCount) {
            case 0:
                return Collections.emptyList();
            case 1:
                return getGridPositions(itemInputCount, 52, 24, 1, 1);
            case 2:
                return getGridPositions(itemInputCount, 34, 24, 2, 1);
            case 3:
                return getGridPositions(itemInputCount, 16, 24, 3, 1);
            case 4:
                return getGridPositions(itemInputCount, 34, 15, 2, 2);
            case 5:
            case 6:
                return getGridPositions(itemInputCount, 16, 15, 3, 2);
            default:
                return getGridPositions(itemInputCount, 16, 6, 3);
        }
    }

    /**
     * @return Display positions for GUI, including border (18x18 size)
     */
    public static List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        switch (itemOutputCount) {
            case 0:
                return Collections.emptyList();
            case 1:
                return getGridPositions(itemOutputCount, 106, 24, 1, 1);
            case 2:
                return getGridPositions(itemOutputCount, 106, 24, 2, 1);
            case 3:
                return getGridPositions(itemOutputCount, 106, 24, 3, 1);
            case 4:
                return getGridPositions(itemOutputCount, 106, 15, 2, 2);
            case 5:
            case 6:
                return getGridPositions(itemOutputCount, 106, 15, 3, 2);
            default:
                return getGridPositions(itemOutputCount, 106, 6, 3);
        }
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

    private static IDrawable[] getBackgroundsForSlot(IDrawable base, GT_Recipe.GT_Recipe_Map recipeMap, boolean isFluid,
            boolean isOutput, int index, boolean isSpecial, SteamVariant steamVariant) {
        if (recipeMap != null) {
            IDrawable overlay;
            if (steamVariant != SteamVariant.NONE) {
                SteamTexture steamTexture = recipeMap.getOverlayForSlotSteam(isFluid, isOutput, index, isSpecial);
                if (steamTexture != null) {
                    overlay = steamTexture.get(steamVariant);
                } else {
                    overlay = null;
                }
            } else {
                overlay = recipeMap.getOverlayForSlot(isFluid, isOutput, index, isSpecial);
            }
            if (overlay != null) {
                return new IDrawable[] { base, overlay };
            }
        }
        return new IDrawable[] { base };
    }

    @FunctionalInterface
    public interface ForEachSlot {

        void accept(int index, IDrawable[] backgrounds, Pos2d pos);
    }
}

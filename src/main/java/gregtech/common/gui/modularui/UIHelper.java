package gregtech.common.gui.modularui;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import gregtech.api.gui.modularui.SteamTexture;
import gregtech.api.util.GT_Recipe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

public class UIHelper {

    /**
     * Iterates over candidates for slot placement.
     */
    public static void forEachSlots(
            ForEachSlot forEachItemInputSlot,
            ForEachSlot forEachItemOutputSlot,
            ForEachSlot forEachSpecialSlot,
            ForEachSlot forEachFluidInputSlot,
            ForEachSlot forEachFluidOutputSlot,
            IDrawable itemSlotBackground,
            IDrawable fluidSlotBackground,
            @Nullable GT_Recipe.GT_Recipe_Map recipeMap,
            int itemInputCount,
            int itemOutputCount,
            SteamTexture.Variant steamVariant,
            Pos2d offset) {
        final List<Pos2d> itemInputPositions = UIHelper.getItemInputPositions(itemInputCount, offset);
        for (int i = 0; i < itemInputPositions.size(); i++) {
            forEachItemInputSlot.accept(
                    i,
                    getBackgroundsForSlot(itemSlotBackground, recipeMap, false, false, i, false, steamVariant),
                    itemInputPositions.get(i));
        }

        final List<Pos2d> itemOutputPositions = UIHelper.getItemOutputPositions(itemOutputCount, offset);
        for (int i = 0; i < itemOutputPositions.size(); i++) {
            forEachItemOutputSlot.accept(
                    i,
                    getBackgroundsForSlot(itemSlotBackground, recipeMap, false, true, i, false, steamVariant),
                    itemOutputPositions.get(i));
        }

        forEachSpecialSlot.accept(
                0,
                getBackgroundsForSlot(itemSlotBackground, recipeMap, false, false, 0, true, steamVariant),
                UIHelper.getSpecialItemPosition(offset));

        final List<Pos2d> fluidInputPositions = UIHelper.getFluidInputPositions(offset);
        for (int i = 0; i < fluidInputPositions.size(); i++) {
            forEachFluidInputSlot.accept(
                    i,
                    getBackgroundsForSlot(fluidSlotBackground, recipeMap, true, false, i, false, steamVariant),
                    fluidInputPositions.get(i));
        }

        final List<Pos2d> fluidOutputPositions = UIHelper.getFluidOutputPositions(offset);
        for (int i = 0; i < fluidOutputPositions.size(); i++) {
            forEachFluidOutputSlot.accept(
                    i,
                    getBackgroundsForSlot(fluidSlotBackground, recipeMap, true, true, i, false, steamVariant),
                    fluidOutputPositions.get(i));
        }
    }

    /**
     * @return Display positions for basic machine, including border (18x18 size)
     */
    public static List<Pos2d> getItemInputPositions(int inputCount, Pos2d offset) {
        switch (inputCount) {
            case 0:
                return Collections.emptyList();
            case 1:
                return getItemGridPositions(inputCount, 52, 24, 1, 1, offset.x, offset.y);
            case 2:
                return getItemGridPositions(inputCount, 34, 24, 2, 1, offset.x, offset.y);
            case 3:
                return getItemGridPositions(inputCount, 16, 24, 3, 1, offset.x, offset.y);
            case 4:
            case 5:
                return getItemGridPositions(inputCount, 16, 24, 3, 2, offset.x, offset.y);
            case 6:
                return getItemGridPositions(inputCount, 16, 15, 3, 2, offset.x, offset.y);
            default:
                return getItemGridPositions(inputCount, 16, 6, 3, 3, offset.x, offset.y);
        }
    }

    /**
     * @return Display positions for basic machine, including border (18x18 size)
     */
    public static List<Pos2d> getItemOutputPositions(int outputCount, Pos2d offset) {
        switch (outputCount) {
            case 0:
                return Collections.emptyList();
            case 1:
                return getItemGridPositions(outputCount, 106, 24, 1, 1, offset.x, offset.y);
            case 2:
                return getItemGridPositions(outputCount, 106, 24, 2, 1, offset.x, offset.y);
            case 3:
                return getItemGridPositions(outputCount, 106, 24, 3, 1, offset.x, offset.y);
            case 4:
                return getItemGridPositions(outputCount, 106, 15, 2, 2, offset.x, offset.y);
            case 5:
            case 6:
                return getItemGridPositions(outputCount, 106, 15, 3, 2, offset.x, offset.y);
            default:
                return getItemGridPositions(outputCount, 106, 6, 3, 3, offset.x, offset.y);
        }
    }

    /**
     * @return Display position for basic machine, including border (18x18 size)
     */
    public static Pos2d getSpecialItemPosition(Pos2d offset) {
        return new Pos2d(124, 62).add(offset);
    }

    /**
     * @return Display positions for basic machine, including border (18x18 size)
     */
    public static List<Pos2d> getFluidInputPositions(Pos2d offset) {
        List<Pos2d> results = new ArrayList<>();
        results.add(new Pos2d(52, 62).add(offset));
        results.add(new Pos2d(34, 62).add(offset));
        return results;
    }

    /**
     * @return Display positions for basic machine, including border (18x18 size)
     */
    public static List<Pos2d> getFluidOutputPositions(Pos2d offset) {
        List<Pos2d> results = new ArrayList<>();
        results.add(new Pos2d(106, 62).add(offset));
        results.add(new Pos2d(124, 62).add(offset));
        return results;
    }

    private static List<Pos2d> getItemGridPositions(
            int itemCount, int xOrigin, int yOrigin, int xDirMaxCount, int yDirMaxCount, int xOffset, int yOffset) {
        // 18 pixels to get to a new grid for placing an item tile since they are 16x16 and have 1 pixel buffers
        // around them.
        int distanceGrid = 18;
        int xMax = xOrigin + xDirMaxCount * distanceGrid;

        List<Pos2d> results = new ArrayList<>();
        // Temp variables to keep track of current coordinates to place item at.
        int xCoord = xOrigin;
        int yCoord = yOrigin;

        for (int i = 0; i < itemCount; i++) {
            results.add(new Pos2d(xCoord + xOffset, yCoord + yOffset));
            xCoord += distanceGrid;
            if (xCoord == xMax) {
                xCoord = xOrigin;
                yCoord += distanceGrid;
            }
        }

        return results;
    }

    private static IDrawable[] getBackgroundsForSlot(
            IDrawable base,
            GT_Recipe.GT_Recipe_Map recipeMap,
            boolean isFluid,
            boolean isOutput,
            int index,
            boolean isSpecial,
            SteamTexture.Variant steamVariant) {
        if (recipeMap != null) {
            IDrawable overlay;
            if (steamVariant != SteamTexture.Variant.NONE) {
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
                return new IDrawable[] {base, overlay};
            }
        }
        return new IDrawable[] {base};
    }

    @FunctionalInterface
    public interface ForEachSlot {
        void accept(int index, IDrawable[] backgrounds, Pos2d pos);
    }
}

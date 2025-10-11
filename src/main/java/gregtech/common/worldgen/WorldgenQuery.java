package gregtech.common.worldgen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.world.World;

import galacticgreg.api.ModDimensionDef;
import galacticgreg.api.enums.DimensionDef;
import gregtech.api.enums.StoneCategory;
import gregtech.api.interfaces.IStoneCategory;
import gregtech.api.util.GTDataUtils;
import gregtech.common.WorldgenGTOreLayer;
import gregtech.common.WorldgenGTOreSmallPieces;

public class WorldgenQuery<TLayer extends IWorldgenLayer> {

    public List<TLayer> list;
    public int minY = -1, maxY = -1;
    public boolean checkWeight = true;
    public Set<String> dimensions;
    public Set<IStoneCategory> stoneCategories = new HashSet<>(Arrays.asList(StoneCategory.Stone));
    public boolean defaultStoneCategories = true;

    public WorldgenQuery(List<TLayer> list) {
        this.list = list;
    }

    public static WorldgenQuery<WorldgenGTOreLayer> veins() {
        return new WorldgenQuery<>(WorldgenGTOreLayer.sList);
    }

    public static WorldgenQuery<WorldgenGTOreSmallPieces> small() {
        return new WorldgenQuery<>(WorldgenGTOreSmallPieces.sList);
    }

    public WorldgenQuery<TLayer> withMinY(int minY) {
        this.minY = minY;
        return this;
    }

    public WorldgenQuery<TLayer> withMaxY(int maxY) {
        this.maxY = maxY;
        return this;
    }

    public WorldgenQuery<TLayer> withoutWeight() {
        this.checkWeight = false;
        return this;
    }

    public WorldgenQuery<TLayer> inDimension(String dimName) {
        if (dimensions == null) dimensions = new HashSet<>();

        dimensions.add(dimName);

        return this;
    }

    public WorldgenQuery<TLayer> inDimension(World world) {
        if (dimensions == null) dimensions = new HashSet<>();

        dimensions.add(DimensionDef.getDimensionName(world));

        return this;
    }

    public WorldgenQuery<TLayer> inDimension(ModDimensionDef def) {
        if (dimensions == null) dimensions = new HashSet<>();

        dimensions.add(def.getDimensionName());

        return this;
    }

    public WorldgenQuery<TLayer> inDimension(DimensionDef def) {
        if (dimensions == null) dimensions = new HashSet<>();

        dimensions.add(def.modDimensionDef.getDimensionName());

        return this;
    }

    public WorldgenQuery<TLayer> inStone(IStoneCategory stoneCategories) {
        if (defaultStoneCategories) {
            this.stoneCategories = new HashSet<>();
            defaultStoneCategories = false;
        }

        this.stoneCategories.addAll(Arrays.asList(stoneCategories));

        return this;
    }

    @Nullable
    public TLayer findRandom(Random random) {
        if (checkWeight) {
            return findRandomWithWeight(random);
        } else {
            return findRandomWithoutWeight(random);
        }
    }

    private TLayer findRandomWithWeight(Random random) {
        int totalAmount = 0;

        ArrayList<TLayer> matches = GTDataUtils.filterList(list, this::matches);

        for (TLayer layer : matches) {
            totalAmount += layer.getWeight();
        }

        if (totalAmount == 0) return null;

        int remainingAmount = random.nextInt(totalAmount);

        for (TLayer layer : matches) {
            remainingAmount -= layer.getWeight();

            if (remainingAmount <= 0) return layer;
        }

        return null;
    }

    private TLayer findRandomWithoutWeight(Random random) {
        int count = 0;

        for (TLayer layer : list) {
            if (matches(layer)) {
                count++;
            }
        }

        if (count == 0) return null;

        int remainingAmount = random.nextInt(count);

        for (TLayer layer : list) {
            if (!matches(layer)) continue;

            remainingAmount--;

            if (remainingAmount <= 0) return layer;
        }

        return null;
    }

    public boolean matches(TLayer layer) {
        if (minY != -1 && minY < layer.getMinY()) return false;
        if (maxY != -1 && maxY > layer.getMaxY()) return false;

        if (dimensions != null) {
            boolean any = false;

            for (String dimName : dimensions) {
                if (layer.canGenerateIn(dimName)) {
                    any = true;
                    break;
                }
            }

            if (!any) return false;
        }

        boolean mustBeStoneSpecific = stoneCategories != null;
        if (layer.isStoneSpecific() != mustBeStoneSpecific) return false;

        if (stoneCategories != null) {
            boolean any = false;

            for (IStoneCategory stoneCategory : stoneCategories) {
                if (layer.canGenerateIn(stoneCategory)) {
                    any = true;
                    break;
                }
            }

            if (!any) return false;
        }

        return true;
    }
}

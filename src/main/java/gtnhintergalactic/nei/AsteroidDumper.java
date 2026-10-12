package gtnhintergalactic.nei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import codechicken.nei.config.DataDumper;
import gregtech.api.enums.StoneType;
import gtnhintergalactic.recipe.AsteroidData;
import gtnhintergalactic.recipe.SpaceMiningRecipes;

public class AsteroidDumper extends DataDumper {

    public AsteroidDumper() {
        super("tools.dump.ig.asteroid");
    }

    @Override
    public String[] header() {
        return new String[] { "Asteroid", "Outputs", "Output Weights", "Min Size", "Max Size", "Min Distance",
            "Max Distance", "Computation", "Min Module Tier", "Base Duration (ticks)", "EU/t", "Min Drone Tier",
            "Max Drone Tier", "Weight" };
    }

    @Override
    public Iterable<String[]> dump(int mode) {
        List<String[]> list = new ArrayList<>();
        for (AsteroidData asteroid : SpaceMiningRecipes.uniqueAsteroidList.stream()
            .sorted(
                (a, b) -> a.getAsteroidNameLocalized()
                    .compareToIgnoreCase(b.getAsteroidNameLocalized()))
            .toArray(AsteroidData[]::new)) {
            String[] line = new String[14];
            line[0] = asteroid.getAsteroidNameLocalized();
            if (asteroid.output != null) {
                line[1] = dumpMaterials(asteroid.output, asteroid.stoneType);
            } else if (asteroid.outputItems != null) {
                line[1] = dumpItems(asteroid.outputItems);
            }
            line[2] = Arrays.stream(asteroid.chances)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(", "));
            line[3] = Integer.toString(asteroid.minSize);
            line[4] = Integer.toString(asteroid.maxSize);
            line[5] = Integer.toString(asteroid.minDistance);
            line[6] = Integer.toString(asteroid.maxDistance);
            line[7] = Integer.toString(asteroid.computation);
            line[8] = Integer.toString(asteroid.requiredModuleTier);
            line[9] = Integer.toString(asteroid.duration);
            line[10] = Integer.toString(asteroid.eut);
            line[11] = Integer.toString(asteroid.minDroneTier);
            line[12] = Integer.toString(asteroid.maxDroneTier);
            line[13] = Integer.toString(asteroid.recipeWeight);

            list.add(line);
        }
        return list;
    }

    private static String dumpMaterials(Material[] materials, @Nullable StoneType stoneType) {
        return dumpItems(
            Arrays.stream(materials)
                .map(material -> AsteroidData.oreStack(material, stoneType, 1))
                .filter(Objects::nonNull)
                .toArray(ItemStack[]::new));
    }

    private static String dumpItems(ItemStack[] items) {
        return Arrays.stream(items)
            .map(ItemStack::getDisplayName)
            .collect(Collectors.joining(", "));
    }

    @Override
    public int modeCount() {
        return 1;
    }

}

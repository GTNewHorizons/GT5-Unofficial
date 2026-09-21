package gtnhintergalactic.nei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import codechicken.nei.config.DataDumper;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gtnhintergalactic.recipe.AsteroidData;
import gtnhintergalactic.recipe.SpaceMiningRecipes;

public class AsteroidDumper extends DataDumper {

    public AsteroidDumper() {
        super("tools.dump.ig.asteroid");
    }

    @Override
    public String[] header() {
        return new String[] { StatCollector.translateToLocal("nei.options.tools.dump.ig.asteroids.headers.asteroids"),
            StatCollector.translateToLocal("nei.options.tools.dump.ig.asteroids.headers.outputs"),
            StatCollector.translateToLocal("nei.options.tools.dump.ig.asteroids.headers.outputWeights"),
            StatCollector.translateToLocal("nei.options.tools.dump.ig.asteroids.headers.minSize"),
            StatCollector.translateToLocal("nei.options.tools.dump.ig.asteroids.headers.maxSize"),
            StatCollector.translateToLocal("nei.options.tools.dump.ig.asteroids.headers.minDistance"),
            StatCollector.translateToLocal("nei.options.tools.dump.ig.asteroids.headers.maxDistance"),
            StatCollector.translateToLocal("nei.options.tools.dump.ig.asteroids.headers.computation"),
            StatCollector.translateToLocal("nei.options.tools.dump.ig.asteroids.headers.minModuleTier"),
            StatCollector.translateToLocal("nei.options.tools.dump.ig.asteroids.headers.duration"),
            StatCollector.translateToLocal("nei.options.tools.dump.ig.asteroids.headers.eut"),
            StatCollector.translateToLocal("nei.options.tools.dump.ig.asteroids.headers.minDroneTier"),
            StatCollector.translateToLocal("nei.options.tools.dump.ig.asteroids.headers.maxDroneTier"),
            StatCollector.translateToLocal("nei.options.tools.dump.ig.asteroids.headers.weight") };
    }

    @Override
    public Iterable<String[]> dump(int mode) {
        List<String[]> list = new ArrayList<>();
        for (AsteroidData asteroid : SpaceMiningRecipes.uniqueAsteroidList.stream()
            .sorted(
                (a, b) -> a.getAsteroidNameLocalized()
                    .compareToIgnoreCase(b.getAsteroidNameLocalized()))
            .toArray(AsteroidData[]::new)) {
            ArrayList<String> line = new ArrayList<>(14);
            line.add(asteroid.getAsteroidNameLocalized());
            if (asteroid.output != null && asteroid.orePrefixes != null) {
                line.add(dumpMaterials(asteroid.output, asteroid.orePrefixes));
            } else if (asteroid.outputItems != null) {
                line.add(dumpItems(asteroid.outputItems));
            }
            line.add(
                Arrays.stream(asteroid.chances)
                    .mapToObj(Integer::toString)
                    .collect(Collectors.joining(", ")));
            line.add(Integer.toString(asteroid.minSize));
            line.add(Integer.toString(asteroid.maxSize));
            line.add(Integer.toString(asteroid.minDistance));
            line.add(Integer.toString(asteroid.maxDistance));
            line.add(Integer.toString(asteroid.computation));
            line.add(Integer.toString(asteroid.requiredModuleTier));
            line.add(Integer.toString(asteroid.duration));
            line.add(Integer.toString(asteroid.eut));
            line.add(Integer.toString(asteroid.minDroneTier));
            line.add(Integer.toString(asteroid.maxDroneTier));
            line.add(Integer.toString(asteroid.recipeWeight));

            list.add(line.toArray(String[]::new));
        }
        return list;
    }

    private static String dumpMaterials(Materials[] mats, OrePrefixes prefix) {
        return dumpItems(
            Arrays.stream(mats)
                .map(x -> GTOreDictUnificator.get(prefix, x, 1))
                .toArray(ItemStack[]::new));
    }

    private static String dumpItems(ItemStack[] items) {
        return Arrays.stream(items)
            .map(ItemStack::getDisplayName)
            .sorted(String::compareToIgnoreCase)
            .collect(Collectors.joining(", "));
    }

    @Override
    public int modeCount() {
        return 1;
    }

}

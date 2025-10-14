package gregtech.mixin.mixins.early.forge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.common.GTWorldgenerator;

@Mixin(GameRegistry.class)
public class GameRegistryMixin {

    @ModifyArg(
        method = "computeSortedGeneratorList",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/ImmutableList;copyOf(Ljava/util/Collection;)Lcom/google/common/collect/ImmutableList;",
            remap = false),
        remap = false)
    private static Collection<IWorldGenerator> gt5u$worldgenOrderFix(Collection<IWorldGenerator> elements) {

        // EFR has its deepslate worldgen's priority set to Integer.MAX_VALUE, which puts it at the end of the list.
        // The ore world gen expects to be the last pass over the world, so we force it to actually be at the end here.

        List<IWorldGenerator> gtWorldgens = new ArrayList<>();

        // Filter and remove all GTWorldgenerator instances
        for (Iterator<IWorldGenerator> iterator = elements.iterator(); iterator.hasNext();) {
            IWorldGenerator worldgen = iterator.next();
            if (worldgen instanceof GTWorldgenerator) {
                gtWorldgens.add(worldgen);
                iterator.remove();
            }
        }

        // Add them onto the end of the list
        elements.addAll(gtWorldgens);

        return elements;
    }
}

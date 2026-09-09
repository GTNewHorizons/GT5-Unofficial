package gregtech.common.oredict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;

/**
 * Centralizes OreDictionary alias registration.
 * <p>
 * {@code aliasToOreDict} registers an ore under another OreDict name.<br>
 * {@code aliasToPrefix} registers an ore under another prefix.<br>
 * {@code aliasToMaterial} registers an ore under another prefix and material.
 */
final class OreDictAliases {

    private static final HashMap<String, List<Consumer<ItemStack>>> ALIASES = new HashMap<>();

    static {
        aliasToPrefix(OrePrefixes.crystal, Materials.CertusQuartz, OrePrefixes.gem);
        aliasToPrefix(OrePrefixes.crystal, Materials.NetherQuartz, OrePrefixes.gem);
        aliasToPrefix(OrePrefixes.crystal, Materials.Fluix, OrePrefixes.gem);

        aliasToOreDict(OrePrefixes.gem, Materials.Lapis, Dyes.dyeBlue.name());
        aliasToOreDict(OrePrefixes.gem, Materials.Sodalite, Dyes.dyeBlue.name());
        aliasToOreDict(OrePrefixes.gem, Materials.Lazurite, Dyes.dyeCyan.name());
        aliasToOreDict(OrePrefixes.gem, Materials.Chocolate, Dyes.dyeBrown.name());
        aliasToOreDict(OrePrefixes.gem, Materials.InfusedAir, "shardAir");
        aliasToOreDict(OrePrefixes.gem, Materials.InfusedWater, "shardWater");
        aliasToOreDict(OrePrefixes.gem, Materials.InfusedFire, "shardFire");
        aliasToOreDict(OrePrefixes.gem, Materials.InfusedEarth, "shardEarth");
        aliasToOreDict(OrePrefixes.gem, Materials.InfusedOrder, "shardOrder");
        aliasToOreDict(OrePrefixes.gem, Materials.InfusedEntropy, "shardEntropy");

        aliasToPrefix(OrePrefixes.gem, Materials.CertusQuartz, OrePrefixes.item);
        aliasToPrefix(OrePrefixes.gem, Materials.CertusQuartz, OrePrefixes.crystal);
        aliasToOreDict(OrePrefixes.gem, Materials.CertusQuartz, OreDictNames.craftingQuartz.name());

        aliasToPrefix(OrePrefixes.gem, Materials.NetherQuartz, OrePrefixes.item);
        aliasToPrefix(OrePrefixes.gem, Materials.NetherQuartz, OrePrefixes.crystal);
        aliasToOreDict(OrePrefixes.gem, Materials.NetherQuartz, OreDictNames.craftingQuartz.name());

        aliasToPrefix(OrePrefixes.gem, Materials.Fluix, OrePrefixes.crystal);
        aliasToOreDict(OrePrefixes.gem, Materials.Fluix, OreDictNames.craftingQuartz.name());

        aliasToPrefix(OrePrefixes.gem, Materials.Quartz, OrePrefixes.crystal);
        aliasToOreDict(OrePrefixes.gem, Materials.Quartz, OreDictNames.craftingQuartz.name());

        aliasToPrefix(OrePrefixes.gem, Materials.Quartzite, OrePrefixes.crystal);
        aliasToOreDict(OrePrefixes.gem, Materials.Quartzite, OreDictNames.craftingQuartz.name());

        aliasToOreDict(OrePrefixes.cableGt01, Materials.Tin, OreDictNames.craftingWireTin.name());
        aliasToOreDict(OrePrefixes.cableGt01, Materials.AnyCopper, OreDictNames.craftingWireCopper.name());
        aliasToOreDict(OrePrefixes.cableGt01, Materials.Gold, OreDictNames.craftingWireGold.name());
        aliasToOreDict(OrePrefixes.cableGt01, Materials.AnyIron, OreDictNames.craftingWireIron.name());

        aliasToPrefix(OrePrefixes.plate, Materials.Polyethylene, OrePrefixes.sheet);
        aliasToPrefix(OrePrefixes.plate, Materials.Rubber, OrePrefixes.sheet);
        aliasToPrefix(OrePrefixes.plate, Materials.Silicon, OrePrefixes.item);
        aliasToPrefix(OrePrefixes.plate, Materials.Wood, OrePrefixes.plank);

        aliasToOreDict(OrePrefixes.dust, Materials.Lapis, Dyes.dyeBlue.name());
        aliasToOreDict(OrePrefixes.dust, Materials.Sodalite, Dyes.dyeBlue.name());
        aliasToOreDict(OrePrefixes.dust, Materials.Lazurite, Dyes.dyeCyan.name());
        aliasToOreDict(OrePrefixes.dust, Materials.BrownLimonite, Dyes.dyeBrown.name());
        aliasToOreDict(OrePrefixes.dust, Materials.YellowLimonite, Dyes.dyeYellow.name());
        aliasToOreDict(OrePrefixes.dust, Materials.Coffee, Dyes.dyeBrown.name());
        aliasToOreDict(OrePrefixes.dust, Materials.Cocoa, Dyes.dyeBrown.name());
        aliasToOreDict(OrePrefixes.dust, Materials.Cocoa, "foodCocoapowder");
        aliasToOreDict(OrePrefixes.dust, Materials.Salt, "itemSalt");
        aliasToOreDict(OrePrefixes.dust, Materials.Wood, "pulpWood");
        aliasToOreDict(OrePrefixes.dust, Materials.Wheat, "foodFlour");

        aliasToOreDict(OrePrefixes.ingot, Materials.Rubber, "itemRubber");

        aliasToOreDict("stoneSmooth", "stone");
        aliasToOreDict("stoneCobble", "cobblestone");
        aliasToOreDict("woodRubber", "logRubber");

        aliasToMaterial("sheetPlastic", OrePrefixes.plate, Materials.Polyethylene);
        aliasToMaterial("sheetRubber", OrePrefixes.plate, Materials.Rubber);
        aliasToMaterial("craftingWireCopper", OrePrefixes.wire, Materials.Copper);
        aliasToMaterial("foodCocoapowder", OrePrefixes.dust, Materials.Cocoa);
    }

    private OreDictAliases() {}

    static void registerAliases(String oreName, ItemStack stack) {
        List<Consumer<ItemStack>> actions = ALIASES.get(oreName);
        if (actions != null) {
            for (Consumer<ItemStack> action : actions) {
                action.accept(stack);
            }
        }
    }

    private static void aliasToPrefix(OrePrefixes sourcePrefix, Materials material, OrePrefixes targetPrefix) {
        String oreName = sourcePrefix.getName() + material.getName();
        addAlias(oreName, stack -> GTOreDictUnificator.registerOre(targetPrefix, material, stack));
    }

    private static void aliasToOreDict(OrePrefixes sourcePrefix, Materials material, String targetOreName) {
        String oreName = sourcePrefix.getName() + material.getName();
        aliasToOreDict(oreName, targetOreName);
    }

    private static void aliasToOreDict(String oreName, String targetOreName) {
        addAlias(oreName, stack -> GTOreDictUnificator.registerOre(targetOreName, stack));
    }

    private static void aliasToMaterial(String oreName, OrePrefixes targetPrefix, Materials targetMaterial) {
        addAlias(oreName, stack -> GTOreDictUnificator.registerOre(targetPrefix, targetMaterial, stack));
    }

    private static void addAlias(String oreName, Consumer<ItemStack> action) {
        ALIASES.computeIfAbsent(oreName, ignored -> new ArrayList<>())
            .add(action);
    }
}

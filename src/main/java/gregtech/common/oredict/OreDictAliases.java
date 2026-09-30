package gregtech.common.oredict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Wildcard;
import gregtech.api.enums.materials.XModMaterials;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.OreDictUtils;

/// Centralizes OreDictionary alias registration.
///
/// `aliasToOreDict` registers an ore under another OreDict name.
/// `aliasToPrefix` registers an ore under another prefix.
/// `aliasToMaterial` registers an ore under another prefix and material.
///
/// A row taking a [Material] builds both names from the material's internal name; the string form names an
/// ore-dictionary entry whose material side is not a material at all, such as vanilla's `gemQuartz`.
final class OreDictAliases {

    private static final HashMap<String, List<Consumer<ItemStack>>> ALIASES = new HashMap<>();
    private static final HashSet<String> PREFIX_ALIASES = new HashSet<>();

    static {
        aliasToPrefix(OrePrefixes.crystal, Materials.CertusQuartz, OrePrefixes.gem);
        aliasToPrefix(OrePrefixes.crystal, Materials.NetherQuartz, OrePrefixes.gem);
        aliasToPrefix(OrePrefixes.crystal, XModMaterials.Fluix, OrePrefixes.gem);

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

        aliasToPrefix(OrePrefixes.gem, XModMaterials.Fluix, OrePrefixes.crystal);
        aliasToOreDict(OrePrefixes.gem, XModMaterials.Fluix, OreDictNames.craftingQuartz.name());

        aliasToOreDict("gemQuartz", "crystalQuartz");
        aliasToOreDict("gemQuartz", OreDictNames.craftingQuartz.name());

        aliasToPrefix(OrePrefixes.gem, Materials.Quartzite, OrePrefixes.crystal);
        aliasToOreDict(OrePrefixes.gem, Materials.Quartzite, OreDictNames.craftingQuartz.name());

        aliasToOreDict(OrePrefixes.cableGt01, Materials.Tin, OreDictNames.craftingWireTin.name());
        aliasToOreDict(Wildcard.AnyCopper.oreDictName(OrePrefixes.cableGt01), OreDictNames.craftingWireCopper.name());
        aliasToOreDict(OrePrefixes.cableGt01, Materials.Gold, OreDictNames.craftingWireGold.name());
        aliasToOreDict(Wildcard.AnyIron.oreDictName(OrePrefixes.cableGt01), OreDictNames.craftingWireIron.name());

        aliasToPrefix(OrePrefixes.plate, Materials.Plastic, OrePrefixes.sheet);
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

        aliasToMaterial("sheetPlastic", OrePrefixes.plate, Materials.Plastic);
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

    /// Whether `oreName` is the target of an alias declared with `aliasToPrefix`.
    static boolean isPrefixAlias(String oreName) {
        return PREFIX_ALIASES.contains(oreName);
    }

    private static void aliasToPrefix(OrePrefixes sourcePrefix, Material material, OrePrefixes targetPrefix) {
        String oreName = sourcePrefix.getName() + MaterialUtils.internalName(material);
        PREFIX_ALIASES.add(targetPrefix.getName() + MaterialUtils.internalName(material));
        addAlias(oreName, stack -> OreDictUtils.registerOre(targetPrefix, material, stack));
    }

    private static void aliasToOreDict(OrePrefixes sourcePrefix, Material material, String targetOreName) {
        String oreName = sourcePrefix.getName() + MaterialUtils.internalName(material);
        aliasToOreDict(oreName, targetOreName);
    }

    private static void aliasToOreDict(String oreName, String targetOreName) {
        addAlias(oreName, stack -> OreDictUtils.registerOre(targetOreName, stack));
    }

    private static void aliasToMaterial(String oreName, OrePrefixes targetPrefix, Material targetMaterial) {
        addAlias(oreName, stack -> OreDictUtils.registerOre(targetPrefix, targetMaterial, stack));
    }

    private static void addAlias(String oreName, Consumer<ItemStack> action) {
        ALIASES.computeIfAbsent(oreName, _ -> new ArrayList<>())
            .add(action);
    }
}

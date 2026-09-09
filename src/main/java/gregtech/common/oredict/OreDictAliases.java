package gregtech.common.oredict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeRegistrator;
import gregtech.api.util.GTUtility;

final class OreDictAliases {

    private static final HashMap<String, List<Consumer<ItemStack>>> aliases = new HashMap<>();

    static {
        registerMaterialAlias(OrePrefixes.crystal, Materials.CertusQuartz, OrePrefixes.gem);
        registerMaterialAlias(OrePrefixes.crystal, Materials.NetherQuartz, OrePrefixes.gem);
        registerMaterialAlias(OrePrefixes.crystal, Materials.Fluix, OrePrefixes.gem);

        registerMaterialAlias(OrePrefixes.gem, Materials.Lapis, Dyes.dyeBlue);
        registerMaterialAlias(OrePrefixes.gem, Materials.Sodalite, Dyes.dyeBlue);
        registerMaterialAlias(OrePrefixes.gem, Materials.Lazurite, Dyes.dyeCyan);
        registerMaterialAlias(OrePrefixes.gem, Materials.InfusedAir, "shardAir");
        registerMaterialAlias(OrePrefixes.gem, Materials.InfusedWater, "shardWater");
        registerMaterialAlias(OrePrefixes.gem, Materials.InfusedFire, "shardFire");
        registerMaterialAlias(OrePrefixes.gem, Materials.InfusedEarth, "shardEarth");
        registerMaterialAlias(OrePrefixes.gem, Materials.InfusedOrder, "shardOrder");
        registerMaterialAlias(OrePrefixes.gem, Materials.InfusedEntropy, "shardEntropy");
        registerMaterialAlias(OrePrefixes.gem, Materials.Chocolate, Dyes.dyeBrown);

        registerMaterialAlias(OrePrefixes.gem, Materials.CertusQuartz, OrePrefixes.item);
        registerMaterialAlias(OrePrefixes.gem, Materials.CertusQuartz, OrePrefixes.crystal);
        registerMaterialAlias(OrePrefixes.gem, Materials.CertusQuartz, OreDictNames.craftingQuartz);
        registerMaterialAlias(OrePrefixes.gem, Materials.NetherQuartz, OrePrefixes.item);
        registerMaterialAlias(OrePrefixes.gem, Materials.NetherQuartz, OrePrefixes.crystal);
        registerMaterialAlias(OrePrefixes.gem, Materials.NetherQuartz, OreDictNames.craftingQuartz);
        registerMaterialAlias(OrePrefixes.gem, Materials.Fluix, OrePrefixes.crystal);
        registerMaterialAlias(OrePrefixes.gem, Materials.Fluix, OreDictNames.craftingQuartz);
        registerMaterialAlias(OrePrefixes.gem, Materials.Quartz, OrePrefixes.crystal);
        registerMaterialAlias(OrePrefixes.gem, Materials.Quartz, OreDictNames.craftingQuartz);
        registerMaterialAlias(OrePrefixes.gem, Materials.Quartzite, OrePrefixes.crystal);
        registerMaterialAlias(OrePrefixes.gem, Materials.Quartzite, OreDictNames.craftingQuartz);

        registerMaterialAlias(OrePrefixes.cableGt01, Materials.Tin, OreDictNames.craftingWireTin);
        registerMaterialAlias(OrePrefixes.cableGt01, Materials.AnyCopper, OreDictNames.craftingWireCopper);
        registerMaterialAlias(OrePrefixes.cableGt01, Materials.Gold, OreDictNames.craftingWireGold);
        registerMaterialAlias(OrePrefixes.cableGt01, Materials.AnyIron, OreDictNames.craftingWireIron);

        registerMaterialAlias(OrePrefixes.plate, Materials.Polyethylene, OrePrefixes.sheet);
        registerMaterialAlias(OrePrefixes.plate, Materials.Rubber, OrePrefixes.sheet);
        registerMaterialAlias(OrePrefixes.plate, Materials.Silicon, OrePrefixes.item);

        registerMaterialAlias(OrePrefixes.dust, Materials.Salt, "itemSalt");
        registerMaterialAlias(OrePrefixes.dust, Materials.Wood, "pulpWood");
        registerMaterialAlias(OrePrefixes.dust, Materials.Wheat, "foodFlour");
        registerMaterialAlias(OrePrefixes.dust, Materials.Lapis, Dyes.dyeBlue);
        registerMaterialAlias(OrePrefixes.dust, Materials.Sodalite, Dyes.dyeBlue);
        registerMaterialAlias(OrePrefixes.dust, Materials.Lazurite, Dyes.dyeCyan);
        registerMaterialAlias(OrePrefixes.dust, Materials.Cocoa, Dyes.dyeBrown);
        registerMaterialAlias(OrePrefixes.dust, Materials.Cocoa, "foodCocoapowder");
        registerMaterialAlias(OrePrefixes.dust, Materials.Coffee, Dyes.dyeBrown);
        registerMaterialAlias(OrePrefixes.dust, Materials.BrownLimonite, Dyes.dyeBrown);
        registerMaterialAlias(OrePrefixes.dust, Materials.YellowLimonite, Dyes.dyeYellow);

        registerMaterialAlias(OrePrefixes.ingot, Materials.Rubber, "itemRubber");

        registerPrefixAlias(OrePrefixes.stoneSmooth, "", "stone");
        registerPrefixAlias(OrePrefixes.stoneCobble, "", "cobblestone");
        registerPrefixAlias(OrePrefixes.sheet, "Plastic", OrePrefixes.plate, Materials.Polyethylene);
        registerPrefixAlias(OrePrefixes.sheet, "Rubber", OrePrefixes.plate, Materials.Rubber);
        registerPrefixAlias(OrePrefixes.crafting, "WireCopper", OrePrefixes.wire, Materials.Copper);
        registerPrefixAlias(OrePrefixes.wood, "Rubber", "logRubber");
        registerPrefixAlias(OrePrefixes.food, "Cocoapowder", OrePrefixes.dust, Materials.Cocoa);
    }

    private OreDictAliases() {}

    static void registerAliases(String oreName, ItemStack stack, OrePrefixes prefix, Materials material,
        String materialName) {

        List<Consumer<ItemStack>> aliases = OreDictAliases.aliases.get(oreName);
        if (aliases != null) {
            for (Consumer<ItemStack> alias : aliases) {
                alias.accept(stack);
            }
            return;
        }

        registerSpecialAliases(stack, prefix, material, materialName);
    }

    private static void registerSpecialAliases(ItemStack stack, OrePrefixes prefix, Materials material,
        String materialName) {

        switch (prefix.getName()) {
            case "dye" -> {
                if (GTUtility.isStringValid(materialName)) {
                    GTOreDictUnificator.registerOre(OrePrefixes.dye, stack);
                }
            }
            case "lens" -> {
                if (material.contains(SubTag.TRANSPARENT) && material.mColor != Dyes._NULL) {
                    String color = material.mColor.name();
                    if (color.startsWith("dye")) color = color.substring(3);
                    GTOreDictUnificator.registerOre("craftingLens" + color, stack);
                }
            }
            case "plate" -> {
                if (material == Materials.Wood) {
                    GTOreDictUnificator.addToBlacklist(stack);
                    GTOreDictUnificator.registerOre(OrePrefixes.plank, material, stack);
                }
            }
            case "cell" -> {
                if (material == Materials.Empty) {
                    GTOreDictUnificator.addToBlacklist(stack);
                }
            }
            case "gearGt" -> GTOreDictUnificator.registerOre(OrePrefixes.gear, material, stack);
            case "stick" -> {
                if (!GTRecipeRegistrator.sRodMaterialList.contains(material)) {
                    GTRecipeRegistrator.sRodMaterialList.add(material);
                } else if (material == Materials.Wood) {
                    GTOreDictUnificator.addToBlacklist(stack);
                }
            }
            case "plank" -> {
                if (materialName.equals("Wood")) {
                    GTOreDictUnificator.addItemData(stack, new ItemData(Materials.Wood, GTValues.M));
                }
            }
            case "slab" -> {
                if (materialName.equals("Wood")) {
                    GTOreDictUnificator.addItemData(stack, new ItemData(Materials.Wood, GTValues.M / 2));
                }
            }
            case "crafting" -> {
                switch (materialName) {
                    case "ToolSolderingMetal" -> GregTechAPI.registerSolderingMetal(stack);
                    case "IndustrialDiamond" -> GTOreDictUnificator.addToBlacklist(stack);
                }
            }
            default -> {}
        }
    }

    private static void registerMaterialAlias(OrePrefixes sourcePrefix, Materials sourceMaterial,
        OrePrefixes targetPrefix) {

        registerAlias(
            sourcePrefix.get(sourceMaterial)
                .toString(),
            stack -> GTOreDictUnificator.registerOre(targetPrefix, sourceMaterial, stack));
    }

    private static void registerMaterialAlias(OrePrefixes sourcePrefix, Materials sourceMaterial, Object target) {
        registerAlias(
            sourcePrefix.get(sourceMaterial)
                .toString(),
            stack -> GTOreDictUnificator.registerOre(target, stack));
    }

    private static void registerPrefixAlias(OrePrefixes sourcePrefix, String sourceMaterial, Object target) {
        registerAlias(sourcePrefix.getName() + sourceMaterial, stack -> GTOreDictUnificator.registerOre(target, stack));
    }

    private static void registerPrefixAlias(OrePrefixes sourcePrefix, String sourceMaterial, OrePrefixes targetPrefix,
        Materials targetMaterial) {

        registerAlias(
            sourcePrefix.getName() + sourceMaterial,
            stack -> GTOreDictUnificator.registerOre(targetPrefix, targetMaterial, stack));
    }

    private static void registerAlias(String sourceName, Consumer<ItemStack> alias) {
        aliases.computeIfAbsent(sourceName, ignored -> new ArrayList<>())
            .add(alias);
    }
}

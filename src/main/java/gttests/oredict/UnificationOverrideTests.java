package gttests.oredict;

import static gregtech.GTLoggers.GT_FML_LOGGER;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialData;
import gregtech.api.objects.ItemData;
import gregtech.api.util.OreDictUtils;

/// Which item a material part unifies to when several mods register one. GregTech-preferred names keep a GregTech
/// item, and shape-override pairs resolve to the overriding item through every lookup.
@GameTestHolder(Mods.ModIDs.GREG_TECH)
public class UnificationOverrideTests {

    private static final String[] GREGTECH_PREFERRED = { "dustAlumina", "blockLead", "blockMagnesium", "blockMithril",
        "blockPlatinum", "blockTungsten", "cellCreosote" };

    @GameTest(timeoutTicks = 20, batch = "gt5.oredict")
    public static void gregTechPreferredNamesUnifyToGregTechItems(GameTestHelper helper) {
        List<String> foreignTargets = new ArrayList<>();
        for (String oreName : GREGTECH_PREFERRED) {
            if (OreDictionary.getOres(oreName)
                .isEmpty()) continue;
            ItemStack target = OreDictUtils.unificationTarget(oreName);
            String owner = ownerOf(target);
            GT_FML_LOGGER.info("Item standing for {} is {} ({})", oreName, describe(target), owner);
            if (!"gregtech".equals(owner) && !"materiallib".equals(owner)) {
                foreignTargets.add(oreName + " -> " + describe(target) + " of " + owner + candidates(oreName));
            }
        }
        helper.assertTrue(
            foreignTargets.isEmpty(),
            "GregTech-preferred ore names unify to foreign items: " + foreignTargets);
        helper.succeed();
    }

    /// A pair served by a shape override has the same item in MaterialLib, its oredict unification target, and
    /// its material association.
    @GameTest(timeoutTicks = 20, batch = "gt5.oredict")
    public static void overriddenPairsResolveToTheirVanillaItemEverywhere(GameTestHelper helper) {
        List<String> mismatches = new ArrayList<>();
        expectOverride(mismatches, OrePrefixes.gem, Shapes.gem, Materials.Diamond, Items.diamond, 0);
        expectOverride(mismatches, OrePrefixes.gem, Shapes.gem, Materials.Lapis, Items.dye, 4);
        expectOverride(mismatches, OrePrefixes.gem, Shapes.gem, Materials.Charcoal, Items.coal, 1);
        expectOverride(mismatches, OrePrefixes.ingot, Shapes.ingot, Materials.Iron, Items.iron_ingot, 0);
        expectOverride(mismatches, OrePrefixes.nugget, Shapes.nugget, Materials.Gold, Items.gold_nugget, 0);
        expectOverride(mismatches, OrePrefixes.dust, Shapes.dust, Materials.Redstone, Items.redstone, 0);
        expectOverride(mismatches, OrePrefixes.dust, Shapes.dust, Materials.Bone, Items.dye, 15);
        expectOverride(mismatches, OrePrefixes.stick, Shapes.stick, Materials.Wood, Items.stick, 0);
        expectOverride(mismatches, OrePrefixes.plate, Shapes.plate, Materials.Paper, Items.paper, 0);
        expectOverride(
            mismatches,
            OrePrefixes.block,
            BlockShapes.block,
            Materials.Iron,
            Item.getItemFromBlock(Blocks.iron_block),
            0);
        expectOverride(
            mismatches,
            OrePrefixes.block,
            BlockShapes.block,
            Materials.Gold,
            Item.getItemFromBlock(Blocks.gold_block),
            0);
        helper.assertTrue(mismatches.isEmpty(), "Overridden pairs disagree: " + mismatches);
        helper.succeed();
    }

    /// The same agreement for pairs served by another mod's item, skipping mods that are not installed.
    @GameTest(timeoutTicks = 20, batch = "gt5.oredict")
    public static void overriddenPairsResolveToTheirForeignItemEverywhere(GameTestHelper helper) {
        List<String> mismatches = new ArrayList<>();
        expectForeign(mismatches, OrePrefixes.ingot, Shapes.ingot, Materials.DarkSteel, "EnderIO", "itemAlloy", 6);
        expectForeign(mismatches, OrePrefixes.ingot, Shapes.ingot, Materials.Thaumium, "Thaumcraft", "ItemResource", 2);
        expectForeign(mismatches, OrePrefixes.nugget, Shapes.nugget, Materials.Void, "Thaumcraft", "ItemNugget", 7);
        expectForeign(
            mismatches,
            OrePrefixes.dust,
            Shapes.dust,
            Materials.Draconium,
            "DraconicEvolution",
            "draconiumDust",
            0);
        expectForeign(
            mismatches,
            OrePrefixes.dust,
            Shapes.dust,
            Materials.Electrotine,
            "ProjRed|Core",
            "projectred.core.part",
            56);
        expectForeign(mismatches, OrePrefixes.ingot, Shapes.ingot, Materials.Infinity, "Avaritia", "Resource", 6);
        expectForeign(mismatches, OrePrefixes.ingot, Shapes.ingot, Materials.Manasteel, "Botania", "manaResource", 0);
        expectForeign(
            mismatches,
            OrePrefixes.nugget,
            Shapes.nugget,
            Materials.Terrasteel,
            "Botania",
            "manaResource",
            18);
        expectForeign(mismatches, OrePrefixes.gem, Shapes.gem, Materials.InfusedAir, "Thaumcraft", "ItemShard", 0);
        expectForeign(mismatches, OrePrefixes.gem, Shapes.gem, Materials.Amber, "Thaumcraft", "ItemResource", 6);
        expectForeign(
            mismatches,
            OrePrefixes.block,
            BlockShapes.block,
            Materials.Amber,
            "Thaumcraft",
            "blockCosmeticOpaque",
            0);
        expectForeign(mismatches, OrePrefixes.nugget, Shapes.nugget, Materials.Iron, "Thaumcraft", "ItemNugget", 0);
        expectForeign(mismatches, OrePrefixes.gem, Shapes.gem, Materials.Firestone, "Railcraft", "firestone.raw", 0);
        expectForeign(
            mismatches,
            OrePrefixes.rawOre,
            Shapes.rawOre,
            Materials.MeteoricIron,
            "GalacticraftCore",
            "item.meteoricIronRaw",
            0);
        expectForeign(
            mismatches,
            OrePrefixes.ingot,
            Shapes.ingot,
            Materials.IronWood,
            "TwilightForest",
            "item.ironwoodIngot",
            0);
        expectForeign(
            mismatches,
            OrePrefixes.ingot,
            Shapes.ingot,
            Materials.Bedrockium,
            "ExtraUtilities",
            "bedrockiumIngot",
            0);
        expectForeign(
            mismatches,
            OrePrefixes.ingot,
            Shapes.ingot,
            Materials.BloodInfusedIron,
            "BloodArsenal",
            "blood_infused_iron",
            0);
        expectForeign(
            mismatches,
            OrePrefixes.rawOre,
            Shapes.rawOre,
            Materials.Desh,
            "GalacticraftMars",
            "item.null",
            0);
        expectForeign(mismatches, OrePrefixes.stick, Shapes.stick, Materials.Desh, "GalacticraftMars", "item.null", 1);
        expectForeign(
            mismatches,
            OrePrefixes.ingot,
            Shapes.ingot,
            Materials.Ichorium,
            "ThaumicTinkerer",
            "kamiResource",
            2);
        helper.assertTrue(mismatches.isEmpty(), "Overridden pairs disagree: " + mismatches);
        helper.succeed();
    }

    /// Other mods' parts with no MaterialLib pair do not unify.
    @GameTest(timeoutTicks = 20, batch = "gt5.oredict")
    public static void namesWithoutMaterialLibPartsDoNotUnify(GameTestHelper helper) {
        List<String> converted = new ArrayList<>();
        for (String oreName : new String[] { "nuggetDiamond", "nuggetEmerald" }) {
            for (ItemStack ore : OreDictionary.getOres(oreName)) {
                if (OreDictUtils.isUnifiedCopy(ore)) converted.add(describe(ore));
            }
        }
        helper.assertTrue(converted.isEmpty(), "Parts with no MaterialLib pair were unified: " + converted);
        helper.succeed();
    }

    private static void expectForeign(List<String> mismatches, OrePrefixes prefix, Shape shape, Material material,
        String modid, String itemName, int meta) {
        Item item = GameRegistry.findItem(modid, itemName);
        if (item != null) expectOverride(mismatches, prefix, shape, material, item, meta);
    }

    private static void expectOverride(List<String> mismatches, OrePrefixes prefix, Shape shape, Material material,
        Item item, int meta) {
        String pair = prefix + " " + material.getName();
        ItemStack fromMaterialLib = MaterialLibAPI.getStack(material, shape, 1);
        if (fromMaterialLib.getItem() != item || fromMaterialLib.getItemDamage() != meta) {
            mismatches.add(pair + ": MaterialLib serves " + describe(fromMaterialLib));
        }
        ItemStack named = OreDictUtils.unificationTarget(prefix.oreDictName(material));
        if (named == null || named.getItem() != item || named.getItemDamage() != meta) {
            mismatches.add(pair + ": the ore-dictionary name stands for " + describe(named));
        }
        ItemData association = MaterialData.getAssociation(new ItemStack(item, 1, meta));
        if (association == null || association.prefix != prefix
            || association.material == null
            || association.material.mMaterial != material) {
            mismatches.add(pair + ": association is " + association);
        }
    }

    private static String ownerOf(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return "none";
        GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        return id == null ? "unknown" : id.modId;
    }

    private static String describe(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return "null";
        GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        return (id == null ? stack.getUnlocalizedName() : id.toString()) + "@" + stack.getItemDamage();
    }

    private static String candidates(String oreName) {
        List<String> entries = new ArrayList<>();
        for (ItemStack candidate : OreDictionary.getOres(oreName)) entries.add(describe(candidate));
        return " (registered: " + entries + ")";
    }
}

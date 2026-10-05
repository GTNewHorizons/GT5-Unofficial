package gregtech.loaders.shapeconsumers;

import static gregtech.api.recipe.RecipeMaps.implosionRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;

import java.util.Set;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.enums.materials.XModMaterials;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;

/// Implodes four dust into three gems with eight tiny piles of Dark Ash, for every material generating a dust and a
/// gem MaterialLib mints itself. The implosion map copies each recipe into the electric implosion compressor.
///
/// A gem another mod's item stands in for is covered only when listed in `foreignGems`: the rest (Nether Star, Ender
/// Pearl, Thaumcraft shards) keep only the recipes their own loaders give them. Skipped outright are the gems
/// [gregtech.loaders.oreprocessing.ProcessingDust] implodes by name with their own amounts, Amalgatite, AE2's Fluix
/// and `NoBlast` materials, which [gregtech.loaders.oreprocessing.ProcessingGem] keeps out of implosion too.
public final class ConsumerGemImplosion {

    private ConsumerGemImplosion() {}

    static void register() {
        Set<Material> skipped = Set.of(
            Materials.Diamond,
            Materials.ManaDiamond,
            Materials.BotaniaDragonstone,
            Materials.Opal,
            Materials.Olivine,
            Materials.Emerald,
            Materials.Ruby,
            Materials.Sapphire,
            Materials.GreenSapphire,
            Materials.Topaz,
            Materials.BlueTopaz,
            Materials.Tanzanite,
            Materials.Amethyst,
            Materials.FoolsRuby,
            Materials.GarnetRed,
            Materials.GarnetYellow,
            Materials.Jasper,
            Materials.Amber,
            Materials.Monazite,
            Materials.Forcicium,
            Materials.Forcillium,
            Materials.Force,
            Materials.Amalgatite,
            XModMaterials.Fluix);
        Set<Material> foreignGems = Set.of(
            Materials.Coal,
            Materials.Charcoal,
            Materials.Lapis,
            Materials.NetherQuartz,
            Materials.Malachite,
            Materials.Firestone);
        ShapeConsumerSupport.delegate(
            Shapes.gem,
            OrePrefixes.gem,
            material -> !skipped.contains(material) && !MaterialUtils.hasSubTag(material, "NoBlast")
                && material.hasShape(Shapes.dust)
                && (MaterialParts.isMinted(OrePrefixes.gem, material) || foreignGems.contains(material)),
            () -> ConsumerGemImplosion::implode);
    }

    private static void implode(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack gem) {
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(material, Shapes.dust, 4))
            .itemOutputs(
                MaterialLibAPI.getStack(material, Shapes.gem, 3),
                MaterialLibAPI.getStack(Materials.DarkAsh, Shapes.dustTiny, 8))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(ADDITIVE_AMOUNT, 24)
            .addTo(implosionRecipes);
    }
}

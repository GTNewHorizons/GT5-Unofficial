package gregtech.loaders.oreprocessing;

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
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;

/// Generates implosion recipes turning four dust into three gems and eight tiny piles of Dark Ash, for every material
/// with a dust and a gem MaterialLib mints itself.
///
/// A gem another mod's item stands in for gets the recipe only when listed in `FOREIGN_GEMS`. `SKIPPED` holds the
/// gems [ProcessingDust] implodes by name, plus Amalgatite and Fluix. `NoBlast` materials never implode.
public final class ProcessingGemImplosion implements IOreRecipeRegistrator {

    public static final ProcessingGemImplosion INSTANCE = new ProcessingGemImplosion();

    private static final Set<Material> SKIPPED = Set.of(
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

    private static final Set<Material> FOREIGN_GEMS = Set.of(
        Materials.Coal,
        Materials.Charcoal,
        Materials.Lapis,
        Materials.NetherQuartz,
        Materials.Malachite,
        Materials.Firestone);

    private ProcessingGemImplosion() {}

    /// Whether `material` gets the generic implosion recipe.
    public static boolean implodes(Material material) {
        return !SKIPPED.contains(material) && !MaterialUtils.hasFlag(material, GTMaterialFlag.NO_BLAST)
            && material.hasShape(Shapes.dust)
            && (MaterialParts.isMinted(material, Shapes.gem) || FOREIGN_GEMS.contains(material));
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName, ItemStack gem) {
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

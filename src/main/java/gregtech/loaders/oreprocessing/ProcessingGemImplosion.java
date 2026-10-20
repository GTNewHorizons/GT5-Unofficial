package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.implosionRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.enums.materials.XModMaterials;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTUtility;

/// Generates the implosion recipes turning four dust into three gems and tiny piles of Dark Ash.
///
/// The gems in `YIELDS` take their listed Dark Ash and explosive amounts. Diamond yields IC2 Industrial Diamonds.
/// Every other material with a gem MaterialLib mints itself takes 8 Dark Ash and 24 explosives. A gem another mod's
/// item stands in for qualifies only when listed in `FOREIGN_GEMS`. Outside `YIELDS`, Amalgatite, Fluix and
/// [GTMaterialFlag#NO_BLAST] materials never implode.
public final class ProcessingGemImplosion implements PartRecipeGenerator {

    public static final ProcessingGemImplosion INSTANCE = new ProcessingGemImplosion();

    private record Yield(int darkAsh, int additive) {}

    private static final Map<Material, Yield> YIELDS = new IdentityHashMap<>();

    static {
        register(16, 32, Materials.Diamond, Materials.ManaDiamond, Materials.Dragonstone);
        register(
            12,
            24,
            Materials.Opal,
            Materials.Olivine,
            Materials.Emerald,
            Materials.Ruby,
            Materials.Sapphire,
            Materials.GreenSapphire,
            Materials.Topaz,
            Materials.BlueTopaz,
            Materials.Tanzanite,
            Materials.Amethyst);
        register(
            8,
            16,
            Materials.Spinel,
            Materials.GarnetRed,
            Materials.GarnetYellow,
            Materials.Jasper,
            Materials.Amber,
            Materials.Monazite,
            Materials.Forcicium,
            Materials.Forcillium,
            Materials.Force);
    }

    private static final Set<Material> NEVER = Set.of(Materials.Amalgatite, XModMaterials.Fluix);

    private static final Set<Material> FOREIGN_GEMS = Set.of(
        Materials.Coal,
        Materials.Charcoal,
        Materials.Lapis,
        Materials.NetherQuartz,
        Materials.Malachite,
        Materials.Firestone);

    private ProcessingGemImplosion() {}

    private static void register(int darkAsh, int additive, Material... materials) {
        for (Material material : materials) {
            YIELDS.put(material, new Yield(darkAsh, additive));
        }
    }

    @Override
    public void generate(OrePrefixes prefix, Material material, Shape shape, ItemStack dust) {
        Yield named = YIELDS.get(material);
        if (named != null) {
            ItemStack gems = material == Materials.Diamond ? ItemList.IC2_Industrial_Diamond.get(3L)
                : MaterialLibAPI.getStack(material, Shapes.gem, 3);
            implode(dust, gems, named);
        } else if (!NEVER.contains(material) && !MaterialUtils.hasFlag(material, GTMaterialFlag.NO_BLAST)
            && (MaterialParts.isMinted(material, Shapes.gem) || FOREIGN_GEMS.contains(material))) {
                implode(dust, MaterialLibAPI.getStack(material, Shapes.gem, 3), new Yield(8, 24));
            }
    }

    private static void implode(ItemStack dust, ItemStack gems, Yield yield) {
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(4, dust))
            .itemOutputs(gems, MaterialLibAPI.getStack(Materials.AshDark, Shapes.dustTiny, yield.darkAsh()))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(ADDITIVE_AMOUNT, yield.additive())
            .addTo(implosionRecipes);
    }
}

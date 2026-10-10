package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.millingRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.util.GTUtility;
import gregtech.common.ores.GTOreAdapter;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

/// Milling recipes making a material's milled ore from its ore blocks, raw ore and crushed ore, at the material's
/// [GTMaterialProperties#MILLING_VOLTAGE]. Netherrack mills from netherrack blocks.
public class ProcessingMilled implements PartRecipeGenerator {

    public static final ProcessingMilled INSTANCE = new ProcessingMilled();

    private ProcessingMilled() {}

    @Override
    public void generate(OrePrefixes prefix, Material material, Shape shape, ItemStack stack) {
        Long voltage = material.getProperty(GTMaterialProperties.MILLING_VOLTAGE);
        if (voltage == null) return;
        ItemStack soapstone = GregtechItemList.Milling_Ball_Soapstone.get(0);
        ItemStack alumina = GregtechItemList.Milling_Ball_Alumina.get(0);

        for (StoneType stoneType : StoneType.VALUES) {
            ItemStack ore = GTOreAdapter.INSTANCE.getVariantStack(material, stoneType, false);
            if (ore == null) continue;
            OrePrefixes orePrefix = stoneType.getPrefix();
            boolean rich = GTMod.proxy.mNetherOreYieldMultiplier && orePrefix == OrePrefixes.oreNetherrack
                || GTMod.proxy.mEndOreYieldMultiplier && orePrefix == OrePrefixes.oreEndstone;
            ore = GTUtility.copyAmount(rich ? 8 : 16, ore);
            mill(material, ore, soapstone, 11, 48, 2 * MINUTES + 30 * SECONDS, voltage);
            mill(material, ore, alumina, 10, 64, 2 * MINUTES, voltage);
        }

        ItemStack raw = MaterialLibAPI.findStack(material, Shapes.rawOre, 16);
        if (raw != null) {
            mill(material, raw, soapstone, 12, 48, 2 * MINUTES + 30 * SECONDS, voltage);
            mill(material, raw, alumina, 12, 64, 2 * MINUTES, voltage);
        }
        ItemStack crushed = MaterialLibAPI.findStack(material, Shapes.crushed, 16);
        if (crushed != null) {
            mill(material, crushed, soapstone, 11, 16, 1 * MINUTES + 15 * SECONDS, voltage);
            mill(material, crushed, alumina, 10, 32, 1 * MINUTES, voltage);
        }
        if (material == Materials.Netherrack) {
            ItemStack netherrack = new ItemStack(Blocks.netherrack, 256);
            mill(material, netherrack, alumina, 10, 32, 1 * MINUTES, voltage);
            mill(material, netherrack, soapstone, 11, 16, 1 * MINUTES, voltage);
        }
    }

    private static void mill(Material material, ItemStack input, ItemStack ball, int circuit, int milled, int duration,
        long voltage) {
        GTValues.RA.stdBuilder()
            .itemInputs(input, ball)
            .circuit(circuit)
            .itemOutputs(MaterialLibAPI.getStack(material, Shapes.milled, milled))
            .duration(duration)
            .eut(voltage)
            .addTo(millingRecipes);
    }
}

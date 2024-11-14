package gtPlusPlus.core.item.base.ore;

import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.millingRecipes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class BaseItemMilledOre extends BaseOreComponent {

    public BaseItemMilledOre(final Material material, int materialEU) {
        super(material, BaseOreComponent.ComponentTypes.MILLED);

        ItemStack oreStack = material.getOre(16);
        ItemStack rawStack = material.getRawOre(16);
        ItemStack crushedStack = material.getCrushed(16);

        ItemStack millingBall_Alumina = GregtechItemList.Milling_Ball_Alumina.get(0);
        ItemStack millingBall_Soapstone = GregtechItemList.Milling_Ball_Soapstone.get(0);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(10), oreStack, millingBall_Alumina)
            .itemOutputs(material.getMilled(64))
            .duration(2 * MINUTES)
            .eut(materialEU)
            .addTo(millingRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(11), oreStack, millingBall_Soapstone)
            .itemOutputs(material.getMilled(48))
            .duration(2 * MINUTES + 30 * SECONDS)
            .eut(materialEU)
            .addTo(millingRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(12), rawStack, millingBall_Soapstone)
            .itemOutputs(material.getMilled(48))
            .duration(2 * MINUTES + 30 * SECONDS)
            .eut(materialEU)
            .addTo(millingRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(12), rawStack, millingBall_Alumina)
            .itemOutputs(material.getMilled(64))
            .duration(2 * MINUTES)
            .eut(materialEU)
            .addTo(millingRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(10), crushedStack, millingBall_Alumina)
            .itemOutputs(material.getMilled(32))
            .duration(1 * MINUTES)
            .eut(materialEU)
            .addTo(millingRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(11), crushedStack, millingBall_Soapstone)
            .itemOutputs(material.getMilled(16))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(materialEU)
            .addTo(millingRecipes);
    }

    public static Item generate(Materials aMat, int aMaterialEU) {
        return generate(MaterialUtils.generateMaterialFromGtENUM(aMat), aMaterialEU);
    }

    public static Item generate(Material aMat, int aMaterialEU) {
        return new BaseItemMilledOre(aMat, aMaterialEU);
    }
}

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
        ItemStack crushedStack = material.getCrushed(16);

        ItemStack milledStackOres1 = material.getMilled(64);
        ItemStack milledStackCrushed1 = material.getMilled(32);
        ItemStack milledStackOres2 = material.getMilled(48);
        ItemStack milledStackCrushed2 = material.getMilled(16);

        ItemStack millingBall_Alumina = GregtechItemList.Milling_Ball_Alumina.get(0);
        ItemStack millingBall_Soapstone = GregtechItemList.Milling_Ball_Soapstone.get(0);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(10), oreStack, millingBall_Alumina)
            .itemOutputs(milledStackOres1)
            .duration(2 * MINUTES)
            .eut(materialEU)
            .addTo(millingRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(11), oreStack, millingBall_Soapstone)
            .itemOutputs(milledStackOres2)
            .duration(2 * MINUTES + 30 * SECONDS)
            .eut(materialEU)
            .addTo(millingRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(10), crushedStack, millingBall_Alumina)
            .itemOutputs(milledStackCrushed1)
            .duration(1 * MINUTES)
            .eut(materialEU)
            .addTo(millingRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(11), crushedStack, millingBall_Soapstone)
            .itemOutputs(milledStackCrushed2)
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

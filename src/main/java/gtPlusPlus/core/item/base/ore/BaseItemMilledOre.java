package gtPlusPlus.core.item.base.ore;

import static gregtech.api.recipe.RecipeMaps.millingRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.MU;
import gregtech.api.util.StringUtils;
import gregtech.loaders.oreprocessing.ProcessingDustGeneration;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class BaseItemMilledOre extends BaseOreComponent {

    public BaseItemMilledOre(final Material material, int materialEU) {
        super(material, BaseOreComponent.ComponentTypes.MILLED);

        ItemStack rawStack = ProcessingDustGeneration.stackOf(OrePrefixes.rawOre, material, 16);
        ItemStack crushedStack = ProcessingDustGeneration.stackOf(OrePrefixes.crushed, material, 16);

        ItemStack millingBall_Alumina = GregtechItemList.Milling_Ball_Alumina.get(0);
        ItemStack millingBall_Soapstone = GregtechItemList.Milling_Ball_Soapstone.get(0);

        for (OrePrefixes tPrefix : OrePrefixes.VALUES) {
            if ((tPrefix.getName()
                .startsWith("ore")) && (tPrefix != OrePrefixes.orePoor)
                && (tPrefix != OrePrefixes.oreSmall)
                && (tPrefix != OrePrefixes.oreNormal)
                && (tPrefix != OrePrefixes.oreRich)
                && (tPrefix != OrePrefixes.oreDense)
                && (tPrefix != OrePrefixes.oreEnd) // distinct from OrePrefixes.oreEndstone
                && (tPrefix != OrePrefixes.oreNether) // distinct from OrePrefixes.oreNetherrack
            ) {
                boolean tIsRich = (GTMod.proxy.mNetherOreYieldMultiplier && tPrefix == OrePrefixes.oreNetherrack)
                    || (GTMod.proxy.mEndOreYieldMultiplier && tPrefix == OrePrefixes.oreEndstone);
                ItemStack oreStack = ItemUtils.getItemStackOfAmountFromOreDictNoBroken(
                    tPrefix.getName() + StringUtils.sanitizeString(MU.internalName(material)),
                    tIsRich ? 8 : 16);

                if (oreStack == null) continue;

                GTValues.RA.stdBuilder()
                    .itemInputs(oreStack, millingBall_Soapstone)
                    .circuit(11)
                    .itemOutputs(ProcessingDustGeneration.stackOf(OrePrefixes.milled, material, 48))
                    .duration(2 * MINUTES + 30 * SECONDS)
                    .eut(materialEU)
                    .addTo(millingRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(oreStack, millingBall_Alumina)
                    .circuit(10)
                    .itemOutputs(ProcessingDustGeneration.stackOf(OrePrefixes.milled, material, 64))
                    .duration(2 * MINUTES)
                    .eut(materialEU)
                    .addTo(millingRecipes);
            }
        }

        GTValues.RA.stdBuilder()
            .itemInputs(rawStack, millingBall_Soapstone)
            .circuit(12)
            .itemOutputs(ProcessingDustGeneration.stackOf(OrePrefixes.milled, material, 48))
            .duration(2 * MINUTES + 30 * SECONDS)
            .eut(materialEU)
            .addTo(millingRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(rawStack, millingBall_Alumina)
            .circuit(12)
            .itemOutputs(ProcessingDustGeneration.stackOf(OrePrefixes.milled, material, 64))
            .duration(2 * MINUTES)
            .eut(materialEU)
            .addTo(millingRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(crushedStack, millingBall_Soapstone)
            .circuit(11)
            .itemOutputs(ProcessingDustGeneration.stackOf(OrePrefixes.milled, material, 16))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(materialEU)
            .addTo(millingRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(crushedStack, millingBall_Alumina)
            .circuit(10)
            .itemOutputs(ProcessingDustGeneration.stackOf(OrePrefixes.milled, material, 32))
            .duration(1 * MINUTES)
            .eut(materialEU)
            .addTo(millingRecipes);
    }

    public BaseItemMilledOre(final Material material, int materialEU, ItemStack custom) {
        super(material, BaseOreComponent.ComponentTypes.MILLED);

        // if ItemStack supplied as custom, will generate recipe with that itemstack as input

        ItemStack millingBall_Alumina = GregtechItemList.Milling_Ball_Alumina.get(0);
        ItemStack millingBall_Soapstone = GregtechItemList.Milling_Ball_Soapstone.get(0);

        GTValues.RA.stdBuilder()
            .itemInputs(custom, millingBall_Alumina)
            .circuit(10)
            .itemOutputs(ProcessingDustGeneration.stackOf(OrePrefixes.milled, material, 32))
            .duration(1 * MINUTES)
            .eut(materialEU)
            .addTo(millingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(custom, millingBall_Soapstone)
            .circuit(11)
            .itemOutputs(ProcessingDustGeneration.stackOf(OrePrefixes.milled, material, 16))
            .duration(1 * MINUTES)
            .eut(materialEU)
            .addTo(millingRecipes);

    }

    public static Item generate(Material aMat, long aMaterialEU) {
        return new BaseItemMilledOre(aMat, (int) aMaterialEU);
    }

    public static Item generate(Material aMat, long aMaterialEU, ItemStack custom) {
        return new BaseItemMilledOre(aMat, (int) aMaterialEU, custom);
    }
}

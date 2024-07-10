package gregtech.common;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.internal.IGT_RecipeAdder;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_RecipeBuilder;
import ic2.core.init.MainConfig;
import ic2.core.util.ConfigUtil;

public class GT_RecipeAdder implements IGT_RecipeAdder {

    @Override
    public GT_Recipe addIC2ReactorBreederCell(ItemStack input, ItemStack output, boolean reflector, int heatStep,
        int heatMultiplier, int requiredPulses) {
        return GT_Values.RA.stdBuilder()
            .itemInputs(input)
            .itemOutputs(output)
            .setNEIDesc(
                reflector ? "Neutron reflecting Breeder" : "Heat neutral Breeder",
                String.format("Every %d reactor hull heat", heatStep),
                String.format("increase speed by %d00%%", heatMultiplier),
                String.format("Required pulses: %d", requiredPulses))
            .duration(0)
            .eut(0)
            .addTo(RecipeMaps.ic2NuclearFakeRecipes)
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public GT_Recipe addIC2ReactorFuelCell(ItemStack input, ItemStack output, boolean aMox, float aHeat, float aEnergy,
        int aCells) {
        // for the mysterious constant 5.0f,
        // see ic2.core.block.reactor.tileentity.TileEntityNuclearReactorElectric.getOfferedEnergy
        // don't ask, just accept
        int pulses = aCells / 2 + 1;
        float nukePowerMult = 5.0f * ConfigUtil.getFloat(MainConfig.get(), "balance/energy/generator/nuclear");
        return GT_Values.RA.stdBuilder()
            .itemInputs(input)
            .itemOutputs(output)
            .setNEIDesc(
                aMox ? "MOX Model" : "Uranium Model",
                "Neutron Pulse: " + aCells,
                aCells == 1 ? String.format("Heat: %.1f * n1 * (n1 + 1)", aHeat / 2f)
                    : String.format("Heat: %.1f * (%d + n1) * (%d + n1)", aHeat * aCells / 2f, aCells, aCells + 1),
                String.format(
                    "Energy: %.1f + n2 * %.1f EU/t",
                    aEnergy * aCells * pulses * nukePowerMult,
                    aEnergy * nukePowerMult))
            .duration(0)
            .eut(0)
            .addTo(RecipeMaps.ic2NuclearFakeRecipes)
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public GT_RecipeBuilder stdBuilder() {
        return GT_RecipeBuilder.builder();
    }
}

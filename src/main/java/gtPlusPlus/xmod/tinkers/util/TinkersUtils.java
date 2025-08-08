package gtPlusPlus.xmod.tinkers.util;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gtPlusPlus.core.material.Material;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.CastingRecipe;
import tconstruct.library.crafting.FluidType;
import tconstruct.library.crafting.LiquidCasting;
import tconstruct.library.crafting.Smeltery;
import tconstruct.library.tools.DynamicToolPart;
import tconstruct.library.util.IPattern;
import tconstruct.smeltery.TinkerSmeltery;

public class TinkersUtils {

    /**
     * Add a new fluid as a valid Smeltery fuel.
     *
     * @param fluid    The fluid.
     * @param power    The temperature of the fluid. This also influences the melting speed. Lava is 1000.
     * @param duration How long one "portion" of liquid fuels the smeltery. Lava is 10.
     */
    public static void addSmelteryFuel(Fluid fluid, int power, int duration) {
        Smeltery.addSmelteryFuel(fluid, power, duration);
    }

    public static boolean registerFluidType(String name, Block block, int meta, int baseTemperature, Fluid fluid,
        boolean isToolpart) {
        FluidType.registerFluidType(name, block, meta, baseTemperature, fluid, isToolpart);
        return true;
    }

    public static void addMelting(ItemStack input, Block block, int metadata, int temperature, FluidStack liquid) {
        Smeltery.addMelting(input, block, metadata, temperature, liquid);
    }

    public static void addMelting(FluidType type, ItemStack input, int temperatureDifference, int fluidAmount) {
        Smeltery.addMelting(type, input, temperatureDifference, fluidAmount);
    }

    public static void addBasinRecipe(ItemStack output, FluidStack metal, ItemStack cast, boolean consume, int delay) {
        LiquidCasting tableCasting = TConstructRegistry.getTableCasting();
        tableCasting.addCastingRecipe(output, metal, cast, consume, delay);
    }

    public static void addCastingTableRecipe(ItemStack output, FluidStack metal, ItemStack cast, boolean consume,
        int delay) {
        LiquidCasting basinCasting = TConstructRegistry.getBasinCasting();
        basinCasting.addCastingRecipe(output, metal, cast, consume, delay);
    }

    public static List<CastingRecipe> getTableCastingRecipes() {
        LiquidCasting tableCasting = TConstructRegistry.getTableCasting();
        return tableCasting.getCastingRecipes();
    }

    public static void generateCastingRecipes(Material aMaterial, int aID) {
        Fluid aMoltenIron = TinkerSmeltery.moltenIronFluid;
        FluidType fluidType = FluidType.getFluidType(aMaterial.getLocalizedName());
        for (CastingRecipe recipe : getTableCastingRecipes()) {
            CastingRecipeHandler newRecipe = new CastingRecipeHandler(recipe);
            if (newRecipe.castingMetal.getFluid() == aMoltenIron && newRecipe.cast != null
                && newRecipe.cast.getItem() instanceof IPattern
                && newRecipe.getResult()
                    .getItem() instanceof DynamicToolPart) {
                ItemStack output = newRecipe.getResult()
                    .copy();
                output.setItemDamage(aID);
                FluidStack liquid = new FluidStack(
                    aMaterial.getFluidStack(0)
                        .getFluid(),
                    newRecipe.castingMetal.amount);
                addCastingTableRecipe(output, liquid, newRecipe.cast, newRecipe.consumeCast, newRecipe.coolTime);
                addMelting(fluidType, output, 0, liquid.amount / 2);
            }
        }
    }

    public static ItemStack getPattern(int aType) {
        return new ItemStack(TinkerSmeltery.metalPattern, aType, 0);
    }

    private static class CastingRecipeHandler {

        public ItemStack output;
        public FluidStack castingMetal;
        public ItemStack cast;
        public boolean consumeCast;
        public int coolTime;

        public CastingRecipeHandler(CastingRecipe castingRecipe) {
            this.output = castingRecipe.output;
            this.castingMetal = castingRecipe.castingMetal;
            this.cast = castingRecipe.cast;
            this.consumeCast = castingRecipe.consumeCast;
            this.coolTime = castingRecipe.coolTime;
        }

        public ItemStack getResult() {
            return this.output.copy();
        }
    }
}

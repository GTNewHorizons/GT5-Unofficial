package gttests.environments;

import static gregtech.api.recipe.RecipeEnvironment.CLEANROOM;
import static gregtech.api.recipe.RecipeEnvironment.LOW_GRAVITY;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Mods;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.recipe.RecipeEnvironmentRequirements;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipe;

@GameTestHolder(value = Mods.ModIDs.GREG_TECH, templatePrefix = "multiblock/environments")
public class EnvironmentIntegrationTests {

    @GameTest(template = "fusion_space", timeoutTicks = 200, batch = "gt5.environment_integration")
    public static void scriptBridgesPreserveConditionalRequirements(GameTestHelper helper) throws Exception {
        Class<?> minecraft = Class.forName("minetweaker.api.minecraft.MineTweakerMC");
        Class<?> ingredient = Class.forName("minetweaker.api.item.IIngredient");
        Class<?> item = Class.forName("minetweaker.api.item.IItemStack");
        Class<?> liquid = Class.forName("minetweaker.api.liquid.ILiquidStack");
        Method wrap = minecraft.getMethod("getIItemStack", ItemStack.class);
        Object output = wrap.invoke(null, new ItemStack(Items.apple));
        Object lens = wrap.invoke(null, new ItemStack(Items.quartz));
        Object water = Class.forName("minetweaker.mc1710.liquid.MCLiquidStack")
            .getConstructor(FluidStack.class)
            .newInstance(new FluidStack(FluidRegistry.WATER, 144));
        Method autoclave = Class.forName("gttweaker.mods.gregtech.machines.Autoclave")
            .getMethod("addRecipe", item, ingredient, liquid, int.class, int.class, int.class, boolean.class);
        Method laser = Class.forName("gttweaker.mods.gregtech.machines.PrecisionLaser")
            .getMethod("addRecipe", item, ingredient, ingredient, int.class, int.class, boolean.class);
        Collection<GTRecipe> autoclaveRecipes = new ArrayList<>();
        Collection<GTRecipe> laserRecipes = new ArrayList<>();
        helper.afterTest(
            () -> RecipeMaps.autoclaveRecipes.getBackend()
                .removeRecipes(autoclaveRecipes));
        helper.afterTest(
            () -> RecipeMaps.laserEngraverRecipes.getBackend()
                .removeRecipes(laserRecipes));
        for (int index = 0; index < 2; index++) {
            boolean required = index == 1;
            ItemStack input = new ItemStack(Blocks.command_block, 1, 150 + index);
            autoclave.invoke(null, output, wrap.invoke(null, input), water, 10000, 20, 8, required);
            GTRecipe registered = registeredWithInput(RecipeMaps.autoclaveRecipes, input);
            autoclaveRecipes.add(registered);
            helper.assertTrue(
                registered.getEnvironmentRequirements()
                    .contains(LOW_GRAVITY) == required,
                "Autoclave script bridge changed its conditional low-gravity requirement");
            input = new ItemStack(Blocks.command_block, 1, 152 + index);
            laser.invoke(null, output, wrap.invoke(null, input), lens, 20, 8, required);
            registered = registeredWithInput(RecipeMaps.laserEngraverRecipes, input);
            laserRecipes.add(registered);
            helper.assertTrue(
                registered.getEnvironmentRequirements()
                    .contains(CLEANROOM) == required,
                "Precision Laser script bridge changed its conditional cleanroom requirement");
        }
        Collection<GTRecipe> amplifierRecipes = new ArrayList<>();
        helper.afterTest(
            () -> RecipeMaps.amplifierRecipes.getBackend()
                .removeRecipes(amplifierRecipes));
        ItemStack input = new ItemStack(Blocks.command_block, 1, 154);
        Class.forName("gttweaker.mods.gregtech.machines.Amplifabricator")
            .getMethod("addRecipe", ingredient, int.class, int.class)
            .invoke(null, wrap.invoke(null, input), 20, 41);
        GTRecipe amplifier = registeredWithInput(RecipeMaps.amplifierRecipes, input);
        amplifierRecipes.add(amplifier);
        helper.assertTrue(
            amplifier.mFluidOutputs.length == 1
                && amplifier.mFluidOutputs[0]
                    .isFluidEqual(MaterialLibAPI.getFluidStack(Materials.UUAmplifier, FluidShapes.fluidLiquid, 41))
                && amplifier.mFluidOutputs[0].amount == 41,
            "Amplifabricator script bridge changed its UU amplifier output");
        helper.succeed();
    }

    private static GTRecipe registeredWithInput(RecipeMap<?> map, ItemStack input) {
        for (GTRecipe recipe : map.getAllRecipes()) {
            for (ItemStack stack : recipe.mInputs) {
                if (stack != null && stack.isItemEqual(input)) return recipe;
            }
        }
        throw new AssertionError("Script bridge did not register its recipe in " + map.unlocalizedName);
    }

    @GameTest(template = "fusion_space", timeoutTicks = 200, batch = "gt5.environment_integration")
    public static void externalReadersMatchCanonicalEnvironmentRequirements(GameTestHelper helper) throws Exception {
        Class<?> reader = Class.forName("com.github.dcysteine.neicustomdiagram.util.gregtech5.GregTechRecipeUtil");
        Method cleanroom = reader.getMethod("requiresCleanroom", GTRecipe.class);
        Method lowGravity = reader.getMethod("requiresLowGravity", GTRecipe.class);
        int cleanroomCount = 0;
        int lowGravityCount = 0;
        int cleanroomMismatches = 0;
        int lowGravityMismatches = 0;
        for (RecipeMap<?> map : new ArrayList<>(RecipeMap.ALL_RECIPE_MAPS.values())) {
            Collection<GTRecipe> recipes = new ArrayList<>(map.getAllRecipes());
            for (GTRecipe recipe : recipes) {
                RecipeEnvironmentRequirements requirements = recipe.getEnvironmentRequirements();
                boolean expectedCleanroom = requirements.contains(CLEANROOM);
                boolean expectedLowGravity = requirements.contains(LOW_GRAVITY);
                if (map == RecipeMaps.blastFurnaceRecipes) {
                    helper.assertTrue(!expectedCleanroom, "EBF recipe unexpectedly requires a cleanroom");
                }
                if (expectedCleanroom) cleanroomCount++;
                if (expectedLowGravity) lowGravityCount++;
                if (Boolean.TRUE.equals(cleanroom.invoke(null, recipe)) != expectedCleanroom) cleanroomMismatches++;
                if (Boolean.TRUE.equals(lowGravity.invoke(null, recipe)) != expectedLowGravity) lowGravityMismatches++;
            }
        }
        helper.assertTrue(cleanroomCount > 0 && lowGravityCount > 0, "Fullpack has no native environment fixtures");
        helper.assertTrue(
            cleanroomMismatches == 0 && lowGravityMismatches == 0,
            "NEI Custom Diagram lost requirements: cleanroom=" + cleanroomMismatches
                + "/"
                + cleanroomCount
                + ", lowGravity="
                + lowGravityMismatches
                + "/"
                + lowGravityCount);
        helper.succeed();
    }
}

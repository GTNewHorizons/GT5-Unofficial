package gregtech.test;

import static gregtech.api.enums.GTValues.RA;
import static net.minecraft.init.Blocks.chest;
import static net.minecraft.init.Blocks.log;
import static net.minecraft.init.Items.iron_ingot;
import static net.minecraft.init.Items.paper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.junit.jupiter.api.Test;

import goodgenerator.api.recipe.ExtremeHeatExchangerBackend;
import goodgenerator.api.recipe.ExtremeHeatExchangerRecipe;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.recipe.maps.FormingPressBackend;
import gregtech.api.recipe.maps.FuelBackend;
import gregtech.api.recipe.maps.NACRecipeMapBackend;
import gregtech.api.recipe.maps.OilCrackerBackend;
import gregtech.api.recipe.maps.PrinterBackend;
import gregtech.api.recipe.maps.ReplicatorBackend;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeConstants;
import gregtech.common.items.behaviors.BehaviourDataOrb;
import tectech.recipe.TecTechRecipeMaps;

class GTRecipeBackendLifecycleTest {

    private static final AtomicInteger IDS = new AtomicInteger();

    @Test
    void fuelBackendSideMapTracksRemoveClearAndReInit() {
        RecipeMap<FuelBackend> map = RecipeMapBuilder.of(uniqueName("fuel"), FuelBackend::new)
            .maxIO(1, 1, 1, 0)
            .build();
        GTRecipe waterFuel = addFluidFuel(map, FluidRegistry.WATER);
        GTRecipe lavaFuel = addFluidFuel(map, FluidRegistry.LAVA);

        assertSame(
            waterFuel,
            map.getBackend()
                .findFuel(FluidRegistry.WATER));
        assertSame(
            lavaFuel,
            map.getBackend()
                .findFuel(FluidRegistry.LAVA));

        map.getBackend()
            .removeRecipe(waterFuel);
        assertNull(
            map.getBackend()
                .findFuel(FluidRegistry.WATER));
        assertSame(
            lavaFuel,
            map.getBackend()
                .findFuel(FluidRegistry.LAVA));

        map.getBackend()
            .reInit();
        assertSame(
            lavaFuel,
            map.getBackend()
                .findFuel(FluidRegistry.LAVA));

        map.getBackend()
            .clearRecipes();
        assertNull(
            map.getBackend()
                .findFuel(FluidRegistry.LAVA));
    }

    @Test
    void extremeHeatExchangerInheritsFuelSideMapLifecycle() {
        RecipeMap<ExtremeHeatExchangerBackend> map = RecipeMapBuilder
            .of(uniqueName("extreme_heat_exchanger"), ExtremeHeatExchangerBackend::new)
            .maxIO(0, 0, 2, 3)
            .build();
        ExtremeHeatExchangerRecipe recipe = new ExtremeHeatExchangerRecipe(
            new FluidStack[] { new FluidStack(FluidRegistry.WATER, 1_000), new FluidStack(FluidRegistry.LAVA, 1_000) },
            new FluidStack[] { new FluidStack(FluidRegistry.WATER, 1), new FluidStack(FluidRegistry.LAVA, 1),
                new FluidStack(FluidRegistry.WATER, 1) },
            0);

        map.getBackend()
            .compileRecipe(recipe);
        assertSame(
            recipe,
            map.getBackend()
                .findFuel(FluidRegistry.WATER));

        map.getBackend()
            .removeRecipe(recipe);
        assertNull(
            map.getBackend()
                .findFuel(FluidRegistry.WATER));
    }

    @Test
    void oilCrackerCatalystSideMapTracksRemoveClearAndReInit() {
        RecipeMap<OilCrackerBackend> map = RecipeMapBuilder.of(uniqueName("oil_cracker"), OilCrackerBackend::new)
            .maxIO(0, 0, 2, 1)
            .build();
        GTRecipe recipe = onlyRecipe(
            RA.stdBuilder()
                .fluidInputs(new FluidStack(FluidRegistry.WATER, 1_000), new FluidStack(FluidRegistry.LAVA, 1_000))
                .fluidOutputs(new FluidStack(FluidRegistry.WATER, 1))
                .duration(1)
                .eut(1)
                .addTo(map));

        assertTrue(
            map.getBackend()
                .isValidCatalystFluid(new FluidStack(FluidRegistry.LAVA, 1)));

        map.getBackend()
            .removeRecipe(recipe);
        assertFalse(
            map.getBackend()
                .isValidCatalystFluid(new FluidStack(FluidRegistry.LAVA, 1)));

        GTRecipe secondRecipe = onlyRecipe(
            RA.stdBuilder()
                .fluidInputs(new FluidStack(FluidRegistry.WATER, 1_000), new FluidStack(FluidRegistry.LAVA, 1_000))
                .fluidOutputs(new FluidStack(FluidRegistry.WATER, 1))
                .duration(1)
                .eut(1)
                .addTo(map));
        map.getBackend()
            .reInit();
        assertSame(
            secondRecipe,
            map.getAllRecipes()
                .iterator()
                .next());
        assertTrue(
            map.getBackend()
                .isValidCatalystFluid(new FluidStack(FluidRegistry.LAVA, 1)));

        map.getBackend()
            .clearRecipes();
        assertFalse(
            map.getBackend()
                .isValidCatalystFluid(new FluidStack(FluidRegistry.LAVA, 1)));
    }

    @Test
    void nacMaxDurationTracksRemoveClearAndReInit() {
        RecipeMap<NACRecipeMapBackend> map = RecipeMapBuilder.of(uniqueName("nac"), NACRecipeMapBackend::new)
            .maxIO(1, 1, 0, 0)
            .build();
        GTRecipe shortRecipe = addItemRecipe(map, 10);
        GTRecipe longRecipe = addItemRecipe(map, 30);

        assertEquals(
            30,
            map.getBackend()
                .getMaxDuration());

        map.getBackend()
            .removeRecipe(longRecipe);
        assertEquals(
            10,
            map.getBackend()
                .getMaxDuration());

        map.getBackend()
            .reInit();
        assertSame(
            shortRecipe,
            map.getAllRecipes()
                .iterator()
                .next());
        assertEquals(
            10,
            map.getBackend()
                .getMaxDuration());

        map.getBackend()
            .clearRecipes();
        assertEquals(
            0,
            map.getBackend()
                .getMaxDuration());
    }

    @Test
    void replicatorMaterialSideMapTracksRemoveClearAndReInit() {
        RecipeMap<ReplicatorBackend> map = RecipeMapBuilder.of(uniqueName("replicator"), ReplicatorBackend::new)
            .maxIO(0, 1, 1, 1)
            .minInputs(0, 1)
            .useSpecialSlot()
            .build();
        GTRecipe recipe = addReplicatorRecipe(map, Materials.Iron);
        ItemStack ironOrb = elementalScanOrb(Materials.Iron);

        assertSame(
            recipe,
            map.findRecipeQuery()
                .specialSlot(ironOrb)
                .find());

        map.getBackend()
            .removeRecipe(recipe);
        assertNull(
            map.findRecipeQuery()
                .specialSlot(ironOrb)
                .find());

        GTRecipe secondRecipe = addReplicatorRecipe(map, Materials.Iron);
        map.getBackend()
            .reInit();
        assertSame(
            secondRecipe,
            map.findRecipeQuery()
                .specialSlot(ironOrb)
                .find());

        map.getBackend()
            .clearRecipes();
        assertNull(
            map.findRecipeQuery()
                .specialSlot(ironOrb)
                .find());
    }

    @Test
    void formingPressRenameFallbackRunsAfterTrieMissButNotDuringCollisionCheck() {
        RecipeMap<FormingPressBackend> map = RecipeMapBuilder.of(uniqueName("forming_press"), FormingPressBackend::new)
            .maxIO(2, 1, 0, 0)
            .build();
        ItemStack mold = ItemList.Shape_Mold_Name.get(1);
        mold.setStackDisplayName("Task 7 Mold Name");
        ItemStack input = new ItemStack(iron_ingot, 1);

        GTRecipe fallback = map.findRecipeQuery()
            .items(mold, input)
            .find();

        assertNotNull(fallback);
        assertEquals(mold.getDisplayName(), fallback.mOutputs[0].getDisplayName());
        assertFalse(
            map.findRecipeQuery()
                .items(mold, input)
                .checkCollision());
    }

    @Test
    void printerDataStickSpecialNbtIsAppliedByModifyFoundRecipe() {
        RecipeMap<PrinterBackend> map = RecipeMapBuilder.of(uniqueName("printer"), PrinterBackend::new)
            .maxIO(1, 1, 0, 0)
            .useSpecialSlot()
            .build();
        onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new ItemStack(paper, 1))
                .itemOutputs(ItemList.Paper_Printed_Pages.get(1))
                .duration(1)
                .eut(1)
                .addTo(map));
        ItemStack dataStick = ItemList.Tool_DataStick.get(1);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("title", "Task 7 Title");
        tag.setString("author", "Task 7 Author");
        dataStick.setTagCompound(tag);

        GTRecipe found = map.findRecipeQuery()
            .items(new ItemStack(paper, 1))
            .specialSlot(dataStick)
            .find();

        assertNotNull(found);
        assertFalse(found.mCanBeBuffered);
        assertEquals(
            "Task 7 Title",
            found.mOutputs[0].getTagCompound()
                .getString("title"));
        assertEquals(
            "Task 7 Author",
            found.mOutputs[0].getTagCompound()
                .getString("author"));
    }

    @Test
    void tecTechCondensateCompileStoresFluidInputsAsMetadataAndClearsSearchInputs() {
        RecipeMap<?> map = TecTechRecipeMaps.condensateAssemblingRecipes;
        FluidStack[] condensateInputs = new FluidStack[] { new FluidStack(FluidRegistry.WATER, 1_000),
            new FluidStack(FluidRegistry.LAVA, 500) };
        GTRecipe recipe = onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new ItemStack(log, 1, IDS.incrementAndGet()))
                .itemOutputs(new ItemStack(chest, 1))
                .fluidInputs(condensateInputs)
                .duration(1)
                .eut(1)
                .ignoreCollision()
                .addTo(map));

        try {
            FluidStack[] storedInputs = recipe.getMetadata(GTRecipeConstants.CONDENSATE_INPUT);
            assertSame(condensateInputs, storedInputs);
            assertEquals(0, recipe.mFluidInputs.length);
        } finally {
            map.getBackend()
                .removeRecipe(recipe);
        }
    }

    private static GTRecipe addFluidFuel(RecipeMap<FuelBackend> map, net.minecraftforge.fluids.Fluid fluid) {
        return onlyRecipe(
            RA.stdBuilder()
                .fluidInputs(new FluidStack(fluid, 1_000))
                .itemOutputs(new ItemStack(chest, 1))
                .duration(1)
                .eut(1)
                .addTo(map));
    }

    private static GTRecipe addItemRecipe(RecipeMap<NACRecipeMapBackend> map, int duration) {
        return onlyRecipe(
            RA.stdBuilder()
                .itemInputs(new ItemStack(log, 1, duration))
                .itemOutputs(new ItemStack(chest, 1))
                .duration(duration)
                .eut(1)
                .addTo(map));
    }

    private static GTRecipe addReplicatorRecipe(RecipeMap<ReplicatorBackend> map, Materials material) {
        GTRecipe recipe = RA.stdBuilder()
            .metadata(GTRecipeConstants.MATERIAL, material)
            .itemOutputs(new ItemStack(iron_ingot, 1))
            .duration(1)
            .eut(1)
            .build()
            .orElseThrow(AssertionError::new);
        return map.getBackend()
            .compileRecipe(recipe);
    }

    private static ItemStack elementalScanOrb(Materials material) {
        ItemStack orb = ItemList.Tool_DataOrb.get(1);
        BehaviourDataOrb.setDataTitle(orb, "Elemental-Scan");
        BehaviourDataOrb.setDataName(orb, material.mElement.name());
        return orb;
    }

    private static GTRecipe onlyRecipe(Collection<GTRecipe> recipes) {
        assertEquals(1, recipes.size());
        return recipes.iterator()
            .next();
    }

    private static String uniqueName(String suffix) {
        return "__backend_lifecycle_" + suffix + IDS.incrementAndGet();
    }
}

package gregtech.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.UUID;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gregtech.api.enums.Materials;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.misc.GlobalVariableStorage;
import gregtech.common.misc.WirelessNetworkManager;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.metaTileEntity.multi.godforge.MTEMoltenModule;

class GodforgeAlloySynthesisTest {

    private TestModule module;
    private GTRecipe recipe;
    private UUID owner;
    private static final BigInteger INITIAL_ENERGY = BigInteger.TEN.pow(30);

    @BeforeEach
    void setup() {
        owner = UUID.randomUUID();
        WirelessNetworkManager.strongCheckOrAddUser(owner);
        WirelessNetworkManager.setUserEU(owner, INITIAL_ENERGY);
        module = new TestModule(owner);
        ItemStack[] ingredients = items(1);
        ingredients[8].stackSize = 0;
        recipe = new GTRecipe(
            false,
            ingredients,
            new ItemStack[] { new ItemStack(Items.apple), new ItemStack(Items.stick), new ItemStack(Items.feather),
                new ItemStack(Items.flint), new ItemStack(Items.brick), new ItemStack(Items.clay_ball),
                new ItemStack(Items.wheat), new ItemStack(Items.wheat_seeds), new ItemStack(Items.bone) },
            null,
            null,
            null,
            null,
            null,
            fluids(1),
            new FluidStack[] { Materials.Iron.getMolten(144), Materials.Copper.getMolten(288),
                Materials.Tin.getMolten(432) },
            1024,
            30,
            0);
        RecipeMaps.alloyBlastSmelterRecipes.getBackend()
            .compileRecipe(recipe);
    }

    @AfterEach
    void cleanup() {
        RecipeMaps.alloyBlastSmelterRecipes.getBackend()
            .removeRecipe(recipe);
        GlobalVariableStorage.GlobalEnergy.remove(owner);
    }

    @Test
    void alloyModeDoesNotGainHeatOverclocksOrDiscounts() throws Exception {
        module.setAlloyMode(true);
        OverclockCalculator calculator = calculator().calculate();
        assertEquals(256, calculator.getDuration());
        assertEquals(480, calculator.getConsumption());
    }

    @Test
    void alloyModeKeepsModuleSpeedEnergyAndOverclockModifiers() throws Exception {
        module.setAlloyMode(true);
        module.setSpeedBonus(0.5);
        module.setEnergyDiscount(0.8);
        module.setOverclockTimeFactor(3);
        OverclockCalculator calculator = calculator().calculate();
        assertEquals(56, calculator.getDuration());
        assertEquals(384, calculator.getConsumption());
    }

    @Test
    void moltenModeKeepsHeatOverclocksAndDiscounts() throws Exception {
        recipe.mSpecialValue = 1800;
        OverclockCalculator actual = calculator().calculate();
        OverclockCalculator expected = new OverclockCalculator().setRecipeEUt(30)
            .setEUt(512)
            .setAmperage(1)
            .setDuration(1024)
            .setHeatOC(true)
            .setHeatDiscount(true)
            .setRecipeHeat(1800)
            .setMachineHeat(10000)
            .setHeatDiscountMultiplier(0.95)
            .calculate();
        assertEquals(expected.getConsumption(), actual.getConsumption());
        assertEquals(expected.getDuration(), actual.getDuration());
    }

    @Test
    void alloySpecialValueIsNotInterpretedAsHeat() throws Exception {
        recipe.mSpecialValue = 20000;
        recipe.mEUt = 513;
        assertEquals(CheckRecipeResultRegistry.insufficientHeat(20000), invoke("validateRecipe"));
        module.setAlloyMode(true);
        assertEquals(CheckRecipeResultRegistry.insufficientPower(513), invoke("validateRecipe"));
    }

    private OverclockCalculator calculator() throws Exception {
        return (OverclockCalculator) invoke("createOverclockCalculator");
    }

    private Object invoke(String name) throws Exception {
        ProcessingLogic logic = module.logic(items(1), fluids(1));
        Method method = logic.getClass()
            .getDeclaredMethod(name, GTRecipe.class);
        method.setAccessible(true);
        return method.invoke(logic, recipe);
    }

    @Test
    void advertisesBothMapsAndDefaultsToMolten() {
        assertSame(TecTechRecipeMaps.godforgeMoltenRecipes, module.getRecipeMap());
        assertTrue(
            module.getAvailableRecipeMaps()
                .contains(RecipeMaps.alloyBlastSmelterRecipes));
        module.setAlloyMode(true);
        assertSame(RecipeMaps.alloyBlastSmelterRecipes, module.getRecipeMap());
    }

    @Test
    void processesNineItemAndThreeFluidInputsAndKeepsAllOutputs() {
        module.setAlloyMode(true);
        module.setCalculatedMaxParallel(8);
        ItemStack[] items = items(8);
        FluidStack[] fluids = fluids(8);
        ProcessingLogic logic = module.logic(items, fluids);
        assertTrue(
            logic.process()
                .wasSuccessful());
        assertEquals(8, logic.getCurrentParallels());
        assertEquals(9, logic.getOutputItems().length);
        for (ItemStack output : logic.getOutputItems()) assertEquals(8, output.stackSize);
        assertEquals(3, logic.getOutputFluids().length);
        for (int i = 0; i < 3; i++) {
            assertEquals(recipe.mFluidOutputs[i].amount * 8, logic.getOutputFluids()[i].amount);
            assertEquals(0, fluids[i].amount);
        }
        for (int i = 0; i < 8; i++) assertEquals(0, items[i].stackSize);
        assertEquals(1, items[8].stackSize); // Non-consumed integrated circuit.
        assertEquals(256, logic.getDuration());
        assertEquals(BigInteger.valueOf(480L * 8 * 256), module.getPowerTally());
        assertEquals(INITIAL_ENERGY.subtract(module.getPowerTally()), WirelessNetworkManager.getUserEU(owner));
        assertEquals(0, logic.getCalculatedEut()); // Wireless energy is charged once at startup.
        assertEquals(0, module.getCurrentRecipeHeat());
    }

    @Test
    void largeParallelSplitsFluidOutputsWithoutOverflow() {
        module.setAlloyMode(true);
        module.setCalculatedMaxParallel(4_000_000);
        recipe.mFluidOutputs[2].amount = 1000;
        ProcessingLogic logic = module.logic(items(4_000_000), fluids(4_000_000));
        assertTrue(
            logic.process()
                .wasSuccessful());
        assertEquals(4_000_000, logic.getCurrentParallels());
        long total = 0;
        for (FluidStack output : logic.getOutputFluids()) {
            assertTrue(output.amount > 0);
            if (output.isFluidEqual(recipe.mFluidOutputs[2])) total += output.amount;
        }
        assertEquals(4_000_000_000L, total);
    }

    @Test
    void preservesOutputChances() {
        module.setAlloyMode(true);
        recipe.mOutputChances = new int[] { 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 0 };
        ProcessingLogic logic = module.logic(items(1), fluids(1));
        assertTrue(
            logic.process()
                .wasSuccessful());
        int byproducts = 0;
        for (ItemStack output : logic.getOutputItems()) {
            if (output.getItem() == Items.bone) byproducts += output.stackSize;
        }
        assertEquals(0, byproducts);
    }

    @Test
    void missingInputDoesNotConsumeResourcesOrEnergy() {
        module.setAlloyMode(true);
        ItemStack[] items = items(1);
        FluidStack[] fluids = fluids(1);
        fluids[2].amount = 0;
        assertFalse(
            module.logic(items, fluids)
                .process()
                .wasSuccessful());
        assertEquals(1, items[0].stackSize);
        assertEquals(INITIAL_ENERGY, WirelessNetworkManager.getUserEU(owner));
    }

    @Test
    void blockedOutputsDoNotConsumeInputsOrEnergy() {
        module.setAlloyMode(true);
        ItemStack[] items = items(1);
        ProcessingLogic logic = module.logic(items, fluids(1))
            .setVoidProtection(true, true);
        assertFalse(
            logic.process()
                .wasSuccessful());
        assertEquals(1, items[0].stackSize);
        assertEquals(INITIAL_ENERGY, WirelessNetworkManager.getUserEU(owner));
    }

    @Test
    void insufficientWirelessEnergyDoesNotConsumeInputs() {
        module.setAlloyMode(true);
        WirelessNetworkManager.setUserEU(owner, BigInteger.ZERO);
        ItemStack[] items = items(1);
        assertFalse(
            module.logic(items, fluids(1))
                .process()
                .wasSuccessful());
        assertEquals(1, items[0].stackSize);
        assertEquals(BigInteger.ZERO, WirelessNetworkManager.getUserEU(owner));
    }

    @Test
    void switchingMapsInvalidatesCachedRecipeAndLock() {
        module.setAlloyMode(true);
        ProcessingLogic logic = module.logic(items(1), fluids(1))
            .setRecipeLocking(module, true);
        assertTrue(
            logic.process()
                .wasSuccessful());
        assertNotNull(module.getSingleRecipeCheck());
        module.setAlloyMode(false);
        assertNull(module.getSingleRecipeCheck());
        logic.clear();
        ItemStack[] items = items(1);
        logic.setInputItems(items)
            .setInputFluids(fluids(1));
        assertFalse(
            logic.process()
                .wasSuccessful());
        assertEquals(1, items[0].stackSize);
    }

    @Test
    void savesAndRestoresAlloyModeAndRecipeLock() {
        module.setAlloyMode(true);
        module.setRecipeLocking(true);
        assertTrue(
            module.logic(items(1), fluids(1))
                .setRecipeLocking(module, true)
                .process()
                .wasSuccessful());
        NBTTagCompound saved = new NBTTagCompound();
        module.saveNBTData(saved);
        TestModule restored = new TestModule(owner);
        restored.loadNBTData(saved);
        assertTrue(restored.isAlloyModeOn());
        assertNotNull(restored.getSingleRecipeCheck());
        assertSame(
            recipe,
            restored.getSingleRecipeCheck()
                .getRecipe());
    }

    @Test
    void oldSaveWithoutModeKeyStaysInMoltenMode() {
        NBTTagCompound saved = new NBTTagCompound();
        module.saveNBTData(saved);
        saved.removeTag("alloyMode");
        module.setAlloyMode(true);
        module.loadNBTData(saved);
        assertFalse(module.isAlloyModeOn());
        assertSame(TecTechRecipeMaps.godforgeMoltenRecipes, module.getRecipeMap());
    }

    private static ItemStack[] items(int count) {
        ItemStack circuit = GTUtility.getIntegratedCircuit(31);
        circuit.stackSize = 1;
        return new ItemStack[] { new ItemStack(Items.apple, count), new ItemStack(Items.stick, count),
            new ItemStack(Items.feather, count), new ItemStack(Items.flint, count), new ItemStack(Items.brick, count),
            new ItemStack(Items.clay_ball, count), new ItemStack(Items.wheat, count),
            new ItemStack(Items.wheat_seeds, count), circuit };
    }

    private static FluidStack[] fluids(int count) {
        return new FluidStack[] { new FluidStack(FluidRegistry.WATER, 10 * count),
            new FluidStack(FluidRegistry.LAVA, 20 * count), Materials.Steam.getGas(30 * count) };
    }

    private static class TestModule extends MTEMoltenModule {

        TestModule(UUID owner) {
            super("alloy_synthesis_test");
            setBaseMetaTileEntity(new BaseMetaTileEntity() {

                @Override
                public ForgeDirection getFrontFacing() {
                    return ForgeDirection.NORTH;
                }
            });
            userUUID = owner;
            setProcessingVoltage(512);
            setHeat(10000);
            setHeatForOC(10000);
            setCalculatedMaxParallel(1);
            setSpeedBonus(1);
            setEnergyDiscount(1);
            setOverclockTimeFactor(2);
        }

        ProcessingLogic logic(ItemStack[] items, FluidStack[] fluids) {
            ProcessingLogic logic = createProcessingLogic();
            setProcessingLogicPower(logic);
            return logic.setMachine(this)
                .setRecipeMapSupplier(this::getRecipeMap)
                .setInputItems(items)
                .setInputFluids(fluids);
        }
    }
}

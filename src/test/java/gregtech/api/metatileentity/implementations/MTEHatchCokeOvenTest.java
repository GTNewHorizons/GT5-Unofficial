package gregtech.api.metatileentity.implementations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;

import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;
import com.gtnewhorizon.gtnhlib.util.ItemUtil;
import com.gtnewhorizons.modularui.common.fluid.FluidStackTank;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.multi.MTECokeOven;

class MTEHatchCokeOvenTest {

    @BeforeAll
    static void initializeControllerWithoutForgeLauncher() throws ClassNotFoundException {
        try (MockedStatic<ConfigurationManager> config = mockStatic(ConfigurationManager.class);
            MockedStatic<GTLog> logs = mockStatic(GTLog.class)) {
            Class.forName(GTMod.class.getName());
        }
        Block casing = GregTechAPI.sBlockCasings12;
        GregTechAPI.sBlockCasings12 = mock(Block.class);
        try (MockedStatic<GTUtility> utility = mockStatic(GTUtility.class)) {
            utility.when(GTUtility::isClient)
                .thenReturn(false);
            utility.when(() -> GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, 0))
                .thenReturn((16 << 7) | 80);
            Class.forName(MTECokeOven.class.getName());
        } finally {
            GregTechAPI.sBlockCasings12 = casing;
        }
    }

    private final Fluid water = new Fluid("test.water");
    private final Fluid lava = new Fluid("test.lava");
    private MTEHatchCokeOven hatch;
    private MTECokeOven oven;
    private IGregTechTileEntity base;
    private ItemStack stack;
    private MockedStatic<ItemUtil> items;
    private MockedStatic<GTUtility> utility;
    private MockedStatic<GTOreDictUnificator> unification;

    @BeforeEach
    void setUp() throws Exception {
        // Vanilla's Items.feather metadata accessor is unavailable outside the Forge launcher.
        items = mockStatic(ItemUtil.class, invocation -> {
            if (invocation.getMethod()
                .getName()
                .equals("getStackMeta")) {
                return invocation.<ItemStack>getArgument(0)
                    .getItemDamage();
            }
            return invocation.callRealMethod();
        });
        // Test items have no ore dictionary registration or vanilla metadata accessor.
        utility = mockStatic(GTUtility.class, CALLS_REAL_METHODS);
        utility.when(() -> GTUtility.areStacksEqual(any(), any()))
            .thenAnswer(invocation -> equalStacks(invocation.getArgument(0), invocation.getArgument(1), false));
        utility.when(() -> GTUtility.areStacksEqual(any(), any(), anyBoolean()))
            .thenAnswer(
                invocation -> equalStacks(
                    invocation.getArgument(0),
                    invocation.getArgument(1),
                    invocation.getArgument(2)));
        unification = mockStatic(GTOreDictUnificator.class);
        unification.when(() -> GTOreDictUnificator.get_nocopy(anyBoolean(), any()))
            .thenAnswer(invocation -> invocation.getArgument(1));
        unification.when(() -> GTOreDictUnificator.isInputStackEqual(any(), any(), any()))
            .thenAnswer(invocation -> equalStacks(invocation.getArgument(0), invocation.getArgument(2), true));
        hatch = new MTEHatchCokeOven("test.hatch", 0, new String[0], null);
        base = attachBase(hatch);
        oven = attachOven();
        stack = new ItemStack(new Item(), 8);
    }

    @AfterEach
    void closeItemUtility() {
        unification.close();
        utility.close();
        items.close();
    }

    private boolean equalStacks(ItemStack first, ItemStack second, boolean ignoreNbt) {
        return first != null && second != null
            && first.getItem() == second.getItem()
            && first.getItemDamage() == second.getItemDamage()
            && (ignoreNbt || ItemStack.areItemStackTagsEqual(first, second));
    }

    private IGregTechTileEntity attachBase(MTEHatchCokeOven attached) {
        IGregTechTileEntity tile = mock(IGregTechTileEntity.class, RETURNS_DEEP_STUBS);
        when(tile.getFrontFacing()).thenReturn(ForgeDirection.NORTH);
        when(tile.isServerSide()).thenReturn(true);
        when(tile.isAllowedToWork()).thenReturn(true);
        when(tile.getMetaTileEntity()).thenReturn(attached);
        when(
            tile.getCoverAtSide(any())
                .letsItemsIn(anyInt())).thenReturn(true);
        when(
            tile.getCoverAtSide(any())
                .letsItemsOut(anyInt())).thenReturn(true);
        attached.setBaseMetaTileEntity(tile);
        return tile;
    }

    private MTECokeOven attachOven() throws Exception {
        MTECokeOven controller = spy(new MTECokeOven("test.oven"));
        IGregTechTileEntity controllerBase = mock(IGregTechTileEntity.class);
        when(controllerBase.getMetaTileEntity()).thenReturn(controller);
        when(controllerBase.isAllowedToWork()).thenReturn(true);
        controller.setBaseMetaTileEntity(controllerBase);
        controller.mMachine = true;
        doReturn(mock(RecipeMap.class, RETURNS_DEEP_STUBS)).when(controller)
            .getRecipeMap();
        registerHatch(controller, base);
        return controller;
    }

    private void registerHatch(MTECokeOven controller, IGregTechTileEntity hatchBase) throws Exception {
        var addHatch = MTECokeOven.class.getDeclaredMethod("addHatch", IGregTechTileEntity.class, Short.class);
        addHatch.setAccessible(true);
        assertTrue((boolean) addHatch.invoke(controller, hatchBase, (short) 0));
    }

    private void setMode(int mode) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte("inputMode", (byte) mode);
        FluidStack stored = hatch.mFluid;
        try (MockedStatic<FluidStack> fluids = mockStatic(FluidStack.class)) {
            fluids.when(() -> FluidStack.loadFluidStackFromNBT(any()))
                .thenReturn(stored);
            hatch.loadNBTData(nbt);
        }
    }

    private FluidStack fluidStack(Fluid fluid, int amount) {
        FluidStack result = mock(FluidStack.class, CALLS_REAL_METHODS);
        result.amount = amount;
        doReturn(fluid).when(result)
            .getFluid();
        doAnswer(invocation -> fluidStack(fluid, result.amount)).when(result)
            .copy();
        doAnswer(invocation -> {
            NBTTagCompound nbt = invocation.getArgument(0);
            nbt.setString("FluidName", fluid.getName());
            nbt.setInteger("Amount", result.amount);
            return nbt;
        }).when(result)
            .writeToNBT(any());
        return result;
    }

    private void tickRecipe(MTECokeOven controller) throws Exception {
        var check = MTECokeOven.class.getDeclaredMethod("checkRecipeProgress", IGregTechTileEntity.class);
        check.setAccessible(true);
        check.invoke(controller, controller.getBaseMetaTileEntity());
    }

    private GTRecipe recipeFor(List<MTECokeOven> controllers, int inputCount) {
        GTRecipe recipe = mock(GTRecipe.class, CALLS_REAL_METHODS);
        ItemStack input = stack.copy();
        input.stackSize = inputCount;
        recipe.mInputs = new ItemStack[] { input };
        recipe.mOutputs = new ItemStack[] { new ItemStack(new Item()) };
        recipe.mFluidInputs = new FluidStack[0];
        recipe.mFluidOutputs = new FluidStack[0];
        recipe.mDuration = 100;
        RecipeMap<?> map = mock(RecipeMap.class, RETURNS_DEEP_STUBS);
        when(
            map.findRecipeQuery()
                .items(any(ItemStack[].class))
                .find()).thenReturn(recipe);
        controllers.forEach(
            controller -> doReturn(map).when(controller)
                .getRecipeMap());
        return recipe;
    }

    @Test
    void inputUsesOneLocalSlotAndTheFrontFace() {
        for (int slot = 0; slot < MTEHatchCokeOven.ITEM_SLOTS; slot++) {
            assertTrue(hatch.canInsertItem(slot, stack, ForgeDirection.NORTH.ordinal()));
            assertFalse(hatch.canInsertItem(slot, stack, ForgeDirection.SOUTH.ordinal()));
            assertFalse(hatch.canExtractItem(slot, stack, ForgeDirection.NORTH.ordinal()));
        }
        assertFalse(hatch.canInsertItem(1, stack, ForgeDirection.NORTH.ordinal()));
        assertArrayEquals(new int[] { 0 }, hatch.getAccessibleSlotsFromSide(2));
        assertArrayEquals(new int[0], hatch.getAccessibleSlotsFromSide(3));
        assertNull(
            hatch.getInventoryHandler()
                .insertItem(0, stack, false));
        assertEquals(8, hatch.getStackInSlot(0).stackSize);
        assertNull(oven.getStackInSlot(MTECokeOven.INPUT_SLOT));
    }

    @Test
    void fourOvensConsumeFromTheSameBufferWithoutHatchTicks() throws Exception {
        List<MTECokeOven> ovens = List.of(oven, attachOven(), attachOven(), attachOven());
        recipeFor(ovens, 1);
        hatch.getInventoryHandler()
            .insertItem(0, stack, false);
        for (MTECokeOven controller : ovens) {
            tickRecipe(controller);
            assertEquals(100, controller.mMaxProgresstime);
            assertNull(controller.getStackInSlot(MTECokeOven.INPUT_SLOT));
        }
        assertEquals(4, hatch.getStackInSlot(0).stackSize);
        verify(base, atLeast(4)).markDirty();
    }

    @Test
    void aSharedBufferCannotBeConsumedTwice() throws Exception {
        MTECokeOven other = attachOven();
        recipeFor(List.of(oven, other), 8);
        hatch.setInventorySlotContents(0, stack);
        tickRecipe(oven);
        tickRecipe(other);
        assertEquals(100, oven.mMaxProgresstime);
        assertEquals(0, other.mMaxProgresstime);
        assertNull(hatch.getStackInSlot(0));
    }

    @Test
    void recipeCanCombineControllerAndMultipleHatches() throws Exception {
        recipeFor(List.of(oven), 10);
        oven.setInventorySlotContents(MTECokeOven.INPUT_SLOT, stack.copy());
        ItemStack single = stack.copy();
        single.stackSize = 1;
        hatch.setInventorySlotContents(0, single.copy());
        MTEHatchCokeOven other = new MTEHatchCokeOven("test.other", 0, new String[0], null);
        registerHatch(oven, attachBase(other));
        other.setInventorySlotContents(0, single.copy());
        tickRecipe(oven);
        assertEquals(100, oven.mMaxProgresstime);
        assertNull(oven.getStackInSlot(MTECokeOven.INPUT_SLOT));
        assertNull(hatch.getStackInSlot(0));
        assertNull(other.getStackInSlot(0));
    }

    @Test
    void onlyConnectedInputHatchesSupplyRecipes() throws Exception {
        recipeFor(List.of(oven), 1);
        hatch.setInventorySlotContents(0, stack);
        setMode(1);
        assertTrue(
            oven.getStoredInputs()
                .isEmpty());
        setMode(0);
        hatch.setBaseMetaTileEntity(null);
        assertTrue(
            oven.getStoredInputs()
                .isEmpty());
        hatch.setBaseMetaTileEntity(base);
        oven.clearHatches();
        assertTrue(
            oven.getStoredInputs()
                .isEmpty());
        tickRecipe(oven);
        assertEquals(0, oven.mMaxProgresstime);
        assertEquals(8, hatch.getStackInSlot(0).stackSize);
    }

    @Test
    void unformedOvensCannotConsumeTheBuffer() throws Exception {
        recipeFor(List.of(oven), 1);
        hatch.setInventorySlotContents(0, stack);
        oven.mMachine = false;
        tickRecipe(oven);
        assertEquals(8, hatch.getStackInSlot(0).stackSize);
        assertEquals(0, oven.mMaxProgresstime);
        assertTrue(hatch.canInsertItem(0, stack, ForgeDirection.NORTH.ordinal()));
    }

    @Test
    void controllerInputStillWorksWithoutHatches() throws Exception {
        recipeFor(List.of(oven), 1);
        oven.clearHatches();
        oven.setInventorySlotContents(MTECokeOven.INPUT_SLOT, stack);
        tickRecipe(oven);
        assertEquals(7, oven.getStackInSlot(MTECokeOven.INPUT_SLOT).stackSize);
        assertEquals(100, oven.mMaxProgresstime);
    }

    @Test
    void itemOutputIsLocalPassiveAndFrontOnly() throws Exception {
        setMode(1);
        oven.setInventorySlotContents(MTECokeOven.OUTPUT_SLOT, stack);
        tickRecipe(oven);
        assertNull(oven.getStackInSlot(MTECokeOven.OUTPUT_SLOT));
        assertEquals(8, hatch.getStackInSlot(0).stackSize);
        assertTrue(hatch.canExtractItem(0, stack, ForgeDirection.NORTH.ordinal()));
        assertFalse(hatch.canExtractItem(0, stack, ForgeDirection.SOUTH.ordinal()));
        assertFalse(hatch.canInsertItem(0, stack, ForgeDirection.NORTH.ordinal()));
        hatch.onPostTick(base, 20);
        assertEquals(8, hatch.getStackInSlot(0).stackSize);
        assertEquals(
            3,
            hatch.getInventoryHandler()
                .extractItem(0, 3, false).stackSize);
        assertEquals(5, hatch.getStackInSlot(0).stackSize);
    }

    @Test
    void fourOvensCanDepositIntoOneSharedOutputBus() throws Exception {
        setMode(1);
        List<MTECokeOven> ovens = List.of(oven, attachOven(), attachOven(), attachOven());
        for (MTECokeOven controller : ovens) {
            controller.setInventorySlotContents(MTECokeOven.OUTPUT_SLOT, stack.copy());
            tickRecipe(controller);
            assertNull(controller.getStackInSlot(MTECokeOven.OUTPUT_SLOT));
        }
        assertEquals(32, hatch.getStackInSlot(0).stackSize);
    }

    @Test
    void fullOutputBusRetainsRemainderInController() throws Exception {
        setMode(1);
        for (int slot = 0; slot < MTEHatchCokeOven.ITEM_SLOTS; slot++) {
            ItemStack full = stack.copy();
            full.stackSize = 62;
            hatch.setInventorySlotContents(slot, full);
        }
        oven.setInventorySlotContents(MTECokeOven.OUTPUT_SLOT, stack);
        tickRecipe(oven);
        assertEquals(6, oven.getStackInSlot(MTECokeOven.OUTPUT_SLOT).stackSize);
        assertEquals(64, hatch.getStackInSlot(0).stackSize);
        tickRecipe(oven);
        assertEquals(6, oven.getStackInSlot(MTECokeOven.OUTPUT_SLOT).stackSize);
        hatch.decrStackSize(0, 6);
        tickRecipe(oven);
        assertNull(oven.getStackInSlot(MTECokeOven.OUTPUT_SLOT));
        assertEquals(64, hatch.getStackInSlot(0).stackSize);
    }

    @Test
    void blockedRecipeCompletionRetainsPendingOutputsWithoutDuplication() throws Exception {
        setMode(1);
        ItemStack full = stack.copy();
        full.stackSize = 64;
        for (int slot = 0; slot < MTEHatchCokeOven.ITEM_SLOTS; slot++)
            hatch.setInventorySlotContents(slot, full.copy());
        oven.setInventorySlotContents(MTECokeOven.OUTPUT_SLOT, full.copy());
        oven.setFluid(fluidStack(water, 63_000));
        oven.mMaxProgresstime = 1;
        oven.mOutputItems = new ItemStack[] { stack.copy() };
        oven.mOutputFluids = new FluidStack[] { fluidStack(water, 1_000) };
        tickRecipe(oven);
        tickRecipe(oven);
        assertEquals(1, oven.mMaxProgresstime);
        assertEquals(8, oven.mOutputItems[0].stackSize);
        assertEquals(63_000, oven.getFluid().amount);
        hatch.decrStackSize(0, 8);
        tickRecipe(oven);
        assertEquals(0, oven.mMaxProgresstime);
        assertNull(oven.mOutputItems);
        assertNull(oven.mOutputFluids);
        assertEquals(64_000, oven.getFluid().amount);
        assertEquals(64, hatch.getStackInSlot(0).stackSize);
        assertEquals(64, oven.getStackInSlot(MTECokeOven.OUTPUT_SLOT).stackSize);
    }

    @Test
    void blockedFluidOutputDoesNotDuplicateCompletedItems() throws Exception {
        oven.setFluid(fluidStack(water, 64_000));
        oven.mMaxProgresstime = 1;
        oven.mOutputItems = new ItemStack[] { stack.copy() };
        oven.mOutputFluids = new FluidStack[] { fluidStack(water, 1_000) };
        tickRecipe(oven);
        tickRecipe(oven);
        assertNull(oven.getStackInSlot(MTECokeOven.OUTPUT_SLOT));
        assertEquals(1, oven.mMaxProgresstime);
        oven.drain(ForgeDirection.UNKNOWN, 1_000, true);
        tickRecipe(oven);
        tickRecipe(oven);
        assertEquals(8, oven.getStackInSlot(MTECokeOven.OUTPUT_SLOT).stackSize);
        assertEquals(64_000, oven.getFluid().amount);
        assertEquals(0, oven.mMaxProgresstime);
    }

    @Test
    void fullControllerOutputDoesNotConsumeSharedInputs() throws Exception {
        GTRecipe recipe = recipeFor(List.of(oven), 1);
        ItemStack full = recipe.getOutput(0)
            .copy();
        full.stackSize = 64;
        oven.setInventorySlotContents(MTECokeOven.OUTPUT_SLOT, full);
        hatch.setInventorySlotContents(0, stack);
        tickRecipe(oven);
        assertEquals(8, hatch.getStackInSlot(0).stackSize);
        assertEquals(0, oven.mMaxProgresstime);
    }

    @Test
    void completedRecipeMovesBothOutputsIntoTheirOwnHatches() throws Exception {
        setMode(1);
        MTEHatchCokeOven fluidHatch = new MTEHatchCokeOven("test.fluid", 0, new String[0], null);
        IGregTechTileEntity fluidBase = attachBase(fluidHatch);
        registerHatch(oven, fluidBase);
        NBTTagCompound mode = new NBTTagCompound();
        mode.setByte("inputMode", (byte) 2);
        try (MockedStatic<FluidStack> fluids = mockStatic(FluidStack.class)) {
            fluidHatch.loadNBTData(mode);
        }
        oven.mMaxProgresstime = 1;
        oven.mOutputItems = new ItemStack[] { stack.copy() };
        oven.mOutputFluids = new FluidStack[] { fluidStack(water, 1_000) };
        tickRecipe(oven);
        assertEquals(8, hatch.getStackInSlot(0).stackSize);
        assertEquals(1_000, fluidHatch.getFluidAmount());
        assertNull(oven.getStackInSlot(MTECokeOven.OUTPUT_SLOT));
        assertNull(oven.getFluid());
    }

    @Test
    void itemHandlerSimulatesAndMarksTheHatchDirty() {
        IItemHandlerModifiable handler = hatch.getInventoryHandler();
        assertEquals(1, handler.getSlots());
        assertNull(handler.insertItem(0, stack, true));
        assertNull(hatch.getStackInSlot(0));
        assertNull(handler.insertItem(0, stack, false));
        assertNull(handler.extractItem(0, 3, false));
        setMode(1);
        assertSame(stack, handler.insertItem(0, stack, false));
        assertEquals(3, handler.extractItem(0, 3, true).stackSize);
        assertEquals(8, hatch.getStackInSlot(0).stackSize);
        clearInvocations(base);
        assertEquals(3, handler.extractItem(0, 3, false).stackSize);
        assertEquals(5, hatch.getStackInSlot(0).stackSize);
        verify(base, atLeastOnce()).markDirty();
        assertNull(handler.extractItem(0, -1, false));
        setMode(2);
        assertNull(handler.extractItem(0, 3, false));
        assertSame(stack, handler.insertItem(0, stack, false));
        assertEquals(5, hatch.getStackInSlot(0).stackSize);
    }

    @Test
    void additionalHatchesHaveSeparateInventoriesAndOwnTheirDrops() throws Exception {
        MTEHatchCokeOven other = new MTEHatchCokeOven("test.other", 0, new String[0], null);
        registerHatch(oven, attachBase(other));
        hatch.setInventorySlotContents(0, stack);
        assertNull(other.getStackInSlot(0));
        assertNull(oven.getStackInSlot(MTECokeOven.INPUT_SLOT));
        assertTrue(hatch.shouldDropItemAt(0));
        assertEquals(1, hatch.getRealInventory().length);
        assertSame(stack, hatch.getRealInventory()[0]);
    }

    @Test
    void fluidTankUsesOnlyHatchStorageAndRejectsExternalFills() throws Exception {
        setMode(2);
        oven.setFluid(fluidStack(water, 2_000));
        tickRecipe(oven);
        FluidStackTank tank = hatch.getFluidTank();
        assertNull(oven.getFluid());
        assertEquals(2_000, tank.getFluidAmount());
        assertEquals(8_000, tank.getCapacity());
        assertEquals(0, tank.fill(fluidStack(water, 500), true));
        assertEquals(0, hatch.fill(fluidStack(water, 500), true));
        assertEquals(500, tank.drain(500, false).amount);
        assertEquals(2_000, hatch.getFluidAmount());
        clearInvocations(base);
        assertEquals(500, tank.drain(500, true).amount);
        assertEquals(1_500, hatch.getFluidAmount());
        verify(base, atLeastOnce()).markDirty();
        assertNull(tank.drain(-1, true));
        setMode(0);
        assertNull(tank.drain(500, true));
        assertEquals(1_500, hatch.mFluid.amount);
    }

    @Test
    void fourOvensShareEightThousandLitersAndRetainOverflowInTheirCaches() throws Exception {
        setMode(2);
        List<MTECokeOven> ovens = List.of(oven, attachOven(), attachOven(), attachOven());
        for (MTECokeOven controller : ovens) {
            controller.setFluid(fluidStack(water, 20_000));
            tickRecipe(controller);
        }
        assertEquals(8_000, hatch.getFluidAmount());
        assertEquals(12_000, oven.getFluid().amount);
        for (int i = 1; i < ovens.size(); i++) {
            assertEquals(
                20_000,
                ovens.get(i)
                    .getFluid().amount);
            assertEquals(
                64_000,
                ovens.get(i)
                    .getCapacity());
        }
        assertEquals(0, hatch.storeFluidOutput(fluidStack(lava, 1_000)));
        hatch.drain(8_000, true);
        tickRecipe(ovens.get(3));
        assertEquals(
            12_000,
            ovens.get(3)
                .getFluid().amount);
        assertEquals(8_000, hatch.getFluidAmount());
    }

    @Test
    void fullHatchAndControllerCacheStopRecipesUntilFluidIsExtracted() throws Exception {
        setMode(2);
        GTRecipe recipe = recipeFor(List.of(oven), 1);
        recipe.mFluidOutputs = new FluidStack[] { fluidStack(water, 1_000) };
        hatch.storeFluidOutput(fluidStack(water, 8_000));
        oven.setFluid(fluidStack(water, 64_000));
        oven.setInventorySlotContents(MTECokeOven.INPUT_SLOT, stack);
        tickRecipe(oven);
        assertEquals(0, oven.mMaxProgresstime);
        assertEquals(8, stack.stackSize);
        assertEquals(8_000, hatch.getFluidAmount());
        assertEquals(64_000, oven.getFluid().amount);

        hatch.drain(1_000, true);
        tickRecipe(oven);
        assertEquals(100, oven.mMaxProgresstime);
        assertEquals(7, stack.stackSize);
        assertEquals(8_000, hatch.getFluidAmount());
        assertEquals(63_000, oven.getFluid().amount);
    }

    @Test
    void additionalFluidHatchesEachAddOnlyEightThousandLiters() throws Exception {
        setMode(2);
        MTEHatchCokeOven other = new MTEHatchCokeOven("test.other", 0, new String[0], null);
        registerHatch(oven, attachBase(other));
        NBTTagCompound mode = new NBTTagCompound();
        mode.setByte("inputMode", (byte) 2);
        try (MockedStatic<FluidStack> fluids = mockStatic(FluidStack.class)) {
            other.loadNBTData(mode);
        }
        oven.setFluid(fluidStack(water, 20_000));
        tickRecipe(oven);
        assertEquals(8_000, hatch.getFluidAmount());
        assertEquals(8_000, other.getFluidAmount());
        assertEquals(4_000, oven.getFluid().amount);
    }

    @Test
    void fluidOutputSupportsSimulatedTypedAndFrontOnlyDrains() {
        setMode(2);
        assertEquals(2_000, hatch.storeFluidOutput(fluidStack(water, 2_000)));
        assertEquals(2_000, hatch.getTankInfo(ForgeDirection.NORTH)[0].fluid.amount);
        assertEquals(1, hatch.getTankInfo(ForgeDirection.NORTH).length);
        assertTrue(hatch.canDrain(ForgeDirection.NORTH, water));
        assertFalse(hatch.canDrain(ForgeDirection.NORTH, lava));
        assertNull(hatch.drain(ForgeDirection.SOUTH, fluidStack(water, 500), true));
        assertNull(hatch.drain(ForgeDirection.NORTH, 0, true));
        assertNull(hatch.drain(ForgeDirection.NORTH, -1, true));
        assertEquals(500, hatch.drain(ForgeDirection.NORTH, 500, false).amount);
        assertEquals(2_000, hatch.getFluidAmount());
        assertEquals(500, hatch.drain(ForgeDirection.NORTH, fluidStack(water, 500), true).amount);
        assertEquals(1_500, hatch.drain(ForgeDirection.NORTH, 2_000, true).amount);
        assertNull(hatch.getFluid());
        assertFalse(hatch.canDrain(ForgeDirection.NORTH, water));
    }

    @Test
    void fluidAutoOutputUsesAdjacentFaceAndHonorsWorkControl() {
        setMode(2);
        hatch.storeFluidOutput(fluidStack(water, 2_000));
        IFluidHandler target = mock(IFluidHandler.class);
        when(base.getITankContainerAtSide(ForgeDirection.NORTH)).thenReturn(target);
        when(target.fill(eq(ForgeDirection.SOUTH), any(), anyBoolean())).thenReturn(1_000);
        when(base.drain(eq(ForgeDirection.NORTH), anyInt(), anyBoolean())).thenAnswer(
            invocation -> hatch
                .drain(ForgeDirection.NORTH, invocation.<Integer>getArgument(1), invocation.getArgument(2)));
        hatch.onPostTick(base, 20);
        assertEquals(1_000, hatch.getFluidAmount());
        verify(target).fill(eq(ForgeDirection.SOUTH), any(), eq(true));
        when(base.isAllowedToWork()).thenReturn(false);
        hatch.onPostTick(base, 40);
        assertEquals(1_000, hatch.getFluidAmount());
    }

    @Test
    void coversCanHideSlotsAndRightClickDoesNotOpenTheController() {
        when(
            base.getCoverAtSide(ForgeDirection.NORTH)
                .letsItemsIn(anyInt())).thenReturn(false);
        when(
            base.getCoverAtSide(ForgeDirection.NORTH)
                .letsItemsOut(anyInt())).thenReturn(false);
        assertArrayEquals(new int[0], hatch.getAccessibleSlotsFromSide(ForgeDirection.NORTH.ordinal()));
    }

    @Test
    void rightClickOpensTheHatchGuiInEveryModeWithoutAController() {
        oven.clearHatches();
        MTEHatchCokeOven localHatch = spy(hatch);
        EntityPlayer player = mock(EntityPlayer.class);
        doNothing().when(localHatch)
            .openGui(player);
        for (int mode = 0; mode < 3; mode++) {
            setMode(mode);
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setByte("inputMode", (byte) mode);
            try (MockedStatic<FluidStack> fluids = mockStatic(FluidStack.class)) {
                localHatch.loadNBTData(nbt);
            }
            assertTrue(localHatch.useMui2());
            assertEquals(mode == 2, localHatch.isFluidOutput());
            assertTrue(localHatch.onRightclick(base, player));
        }
        verify(localHatch, times(3)).openGui(player);
        verify(oven, never()).openGui(any());
    }

    @ParameterizedTest
    @CsvSource({ "0,0", "1,1", "2,2", "-1,0", "3,0", "127,0" })
    void preservesLegacyModeValuesAndDefaultsInvalidModes(int storedMode, int expectedMode) {
        setMode(storedMode);
        NBTTagCompound saved = new NBTTagCompound();
        hatch.saveNBTData(saved);
        assertEquals(expectedMode, saved.getByte("inputMode"));
    }

    @Test
    void oldHatchWithoutLocalStorageLoadsEmptyAndLeavesControllerContentsUntouched() {
        oven.setInventorySlotContents(MTECokeOven.INPUT_SLOT, stack);
        oven.setFluid(fluidStack(water, 12_000));
        try (MockedStatic<FluidStack> fluids = mockStatic(FluidStack.class)) {
            hatch.loadNBTData(new NBTTagCompound());
        }
        assertTrue(hatch.isInput());
        assertEquals(1, hatch.getSizeInventory());
        for (ItemStack item : hatch.getRealInventory()) assertNull(item);
        assertNull(hatch.getFluid());
        assertSame(stack, oven.getStackInSlot(MTECokeOven.INPUT_SLOT));
        assertEquals(12_000, oven.getFluid().amount);
    }

    @Test
    void coverBlockedScrewdriverPreservesContentsAndMode() {
        hatch.setInventorySlotContents(0, stack);
        hatch.mFluid = fluidStack(water, 1_000);
        EntityPlayer player = mock(EntityPlayer.class);
        hatch.onScrewdriverRightClick(ForgeDirection.NORTH, player, 0, 0, 0, null);
        assertTrue(hatch.isInput());
        assertSame(stack, hatch.getStackInSlot(0));
        assertEquals(1_000, hatch.mFluid.amount);
        verify(base, never()).issueTileUpdate();
    }

    @ParameterizedTest
    @CsvSource({ "0,false,1", "1,false,2", "2,false,0", "0,true,2", "1,true,0", "2,true,1" })
    void screwdriverDropsHatchItemsAndVoidsHatchFluid(int initialMode, boolean reverse, int expectedMode) {
        setMode(initialMode);
        hatch.setInventorySlotContents(0, stack);
        hatch.mFluid = fluidStack(water, 1_000);
        ItemStack controllerStack = stack.copy();
        oven.setInventorySlotContents(MTECokeOven.INPUT_SLOT, controllerStack);
        oven.setFluid(fluidStack(water, 12_000));
        EntityPlayer player = mock(EntityPlayer.class);
        when(
            base.getCoverAtSide(ForgeDirection.NORTH)
                .isGUIClickable()).thenReturn(true);
        when(player.isSneaking()).thenReturn(reverse);
        utility.when(() -> GTUtility.dropItemToBlockPos(any(), anyInt(), anyInt(), anyInt(), any()))
            .thenAnswer(invocation -> null);
        utility.clearInvocations();
        hatch.onScrewdriverRightClick(ForgeDirection.NORTH, player, 0, 0, 0, null);
        NBTTagCompound saved = new NBTTagCompound();
        hatch.saveNBTData(saved);
        assertEquals(expectedMode, saved.getByte("inputMode"));
        assertNull(hatch.getStackInSlot(0));
        assertNull(hatch.getFluid());
        assertSame(controllerStack, oven.getStackInSlot(MTECokeOven.INPUT_SLOT));
        assertEquals(12_000, oven.getFluid().amount);
        utility.verify(() -> GTUtility.dropItemToBlockPos(base.getWorld(), 0, 0, -1, stack));
        verify(base, atLeastOnce()).markDirty();
        verify(base).issueTileUpdate();
        hatch.onScrewdriverRightClick(ForgeDirection.NORTH, player, 0, 0, 0, null);
        utility.verify(() -> GTUtility.dropItemToBlockPos(any(), anyInt(), anyInt(), anyInt(), any()), times(1));
    }

    @Test
    void clientScrewdriverDoesNotDropOrClearContents() {
        hatch.setInventorySlotContents(0, stack);
        hatch.mFluid = fluidStack(water, 1_000);
        when(base.isClientSide()).thenReturn(true);
        hatch.onScrewdriverRightClick(ForgeDirection.NORTH, mock(EntityPlayer.class), 0, 0, 0, null);
        assertTrue(hatch.isInput());
        assertSame(stack, hatch.getStackInSlot(0));
        assertEquals(1_000, hatch.mFluid.amount);
        verify(base, never()).issueTileUpdate();
    }

    /** Exercises GT's standard inventory save/load path without a registered block or running world. */
    private static class PersistenceBase extends BaseMetaTileEntity {

        void initialize() {
            mID = 1;
            mMetaTileEntityValid = true;
        }

        @Override
        protected boolean createNewMetatileEntity(short id) {
            return true;
        }

        NBTTagCompound save() {
            NBTTagCompound nbt = new NBTTagCompound();
            saveMetaTileNBT(nbt);
            return nbt;
        }

        void load(NBTTagCompound nbt) {
            loadMetaTileNBT(nbt);
        }
    }

    @Test
    void hatchBuffersSaveAndReloadThroughStandardGtNbt() throws Exception {
        setMode(2);
        hatch.setInventorySlotContents(0, stack);
        hatch.storeFluidOutput(fluidStack(water, 4_000));
        PersistenceBase persistence = mock(PersistenceBase.class, CALLS_REAL_METHODS);
        persistence.initialize();
        doReturn(hatch).when(persistence)
            .getMetaTileEntity();
        NBTTagCompound saved = persistence.save();
        assertEquals(
            1,
            saved.getTagList("Inventory", 10)
                .tagCount());
        assertEquals(
            0,
            saved.getTagList("Inventory", 10)
                .getCompoundTagAt(0)
                .getInteger("IntSlot"));
        assertEquals(
            4_000,
            saved.getCompoundTag("mFluid")
                .getInteger("Amount"));

        MTEHatchCokeOven reloaded = new MTEHatchCokeOven("test.reload", 0, new String[0], null);
        attachBase(reloaded);
        doReturn(reloaded).when(persistence)
            .getMetaTileEntity();
        // The display-fluid registry item and Forge fluid registry are normally initialized by the launcher.
        Field displayStack = ItemList.class.getDeclaredField("mStack");
        Field displayUnset = ItemList.class.getDeclaredField("mHasNotBeenSet");
        displayStack.setAccessible(true);
        displayUnset.setAccessible(true);
        Object previousStack = displayStack.get(ItemList.Display_Fluid);
        boolean previousUnset = displayUnset.getBoolean(ItemList.Display_Fluid);
        displayStack.set(ItemList.Display_Fluid, null);
        displayUnset.setBoolean(ItemList.Display_Fluid, false);
        try (MockedStatic<FluidStack> fluids = mockStatic(FluidStack.class)) {
            utility.when(() -> GTUtility.loadItem(any(NBTTagCompound.class)))
                .thenAnswer(invocation -> {
                    ItemStack loaded = stack.copy();
                    loaded.stackSize = invocation.<NBTTagCompound>getArgument(0)
                        .getByte("Count");
                    return loaded;
                });
            fluids.when(() -> FluidStack.loadFluidStackFromNBT(any()))
                .thenAnswer(
                    invocation -> fluidStack(
                        water,
                        invocation.<NBTTagCompound>getArgument(0)
                            .getInteger("Amount")));
            persistence.load(saved);
            assertEquals(8, reloaded.getStackInSlot(0).stackSize);
            assertEquals(4_000, reloaded.getFluidAmount());
            assertFalse(reloaded.isInput());
            NBTTagCompound savedAgain = persistence.save();
            assertEquals(saved, savedAgain);
        } finally {
            displayStack.set(ItemList.Display_Fluid, previousStack);
            displayUnset.setBoolean(ItemList.Display_Fluid, previousUnset);
        }
    }
}

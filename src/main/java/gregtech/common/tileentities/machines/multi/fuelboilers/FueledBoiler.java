package gregtech.common.tileentities.machines.multi.fuelboilers;

import static gregtech.api.GregTech_API.*;
import static gregtech.api.recipe.check.CheckRecipeResultRegistry.*;
import static gregtech.api.util.GT_RecipeConstants.FUEL_VALUE;
import static net.minecraftforge.fluids.FluidRegistry.WATER;

import com.gtnewhorizon.gtnhlib.client.renderer.util.MathUtil;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

/**
 * The base class for fuel-based boilers. These burn fuels (like Benzene or Diesel) into Steam, instead of direct
 * combustion - in exchange for the extra step, they burn more efficiently at 1 EU to 2.4L steam.
 */
public abstract class FueledBoiler<T extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T>>
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T> {

    private static final int[] MAX_BHEAT_BY_TIER = {
        0, // there is no tier 0!
        50,
        100,
        200,
        500
    };

    // Static (while multi is running)
    protected int tier = 0;
    // These are floats so I don't have to do annoying casts later
    protected float waterMax = 10000, heatMax = 10000, steamMax = waterMax + heatMax;

    // Dynamic
    protected int water, heatBoost, steam;
    // I'm sure there's a method for this, but I'm not using GPL - this isn't a typical multi
    protected boolean isBurning;
    // Because we can't fractionally burn fuel, store it in a tiny buffer instead to guarantee 0 loss
    private int storedEU;
    // Same as above, except this store 80 "units" per L of water
    private int storedWater;
    private int eul;

    protected FueledBoiler(int id, String name, String localizedName) {
        super(id, name, localizedName);
    }

    public FueledBoiler(String name) {
        super(name);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true; // No part needed to operate, always return true
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.fuelBoilerFuels;
    }

    /**
     * Fuel and water consumption is handled here, it only happens when the multi runs.
     */
    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        isBurning = false;

        // There's gotta be a cleaner way to do this
        final boolean waterFirst = mInputHatches.get(0).mRecipeMap == RecipeMaps.waterOnly;
        final GT_MetaTileEntity_Hatch_Input waterIn = mInputHatches.get(waterFirst ? 0 : 1);
        final GT_MetaTileEntity_Hatch_Input fuelIn = mInputHatches.get(waterFirst ? 1 : 0);

        final GT_Recipe r = getRecipeMap().findRecipeQuery().fluids(fuelIn.getFluid()).find();
        if (r == null) return NO_FUEL_FOUND;

        // Fuel found, time to burn!

        // This never NPEs, due to earlier checks
        @SuppressWarnings("DataFlowIssue") final int eul = r.getMetadata(FUEL_VALUE);
        final FluidStack fuel = fuelIn.drain(getLitersToBurn(eul), true);
        if (fuel == null || fuel.amount < 1) return NO_FUEL_FOUND;

        // We can burn, is there water?
        final FluidStack w = waterIn.getFluid();
        if (w == null) return MISSING_WATER;
        water = w.getFluid() == WATER ? w.amount : 0;
        // We need 1/80 L water per EU this tick
        final int need = (int) Math.ceil((getEut(eul) - storedWater) / 80D);
        if (need > water) return MISSING_WATER; // TODO: BLEVE?

        // Do processing
        if (need > 0) storedWater += waterIn.drain(need, true).amount * 80;
        final int eu = fuel.amount * eul;
        storedEU += eu;
        storedWater -= eu;
        this.eul = eul;
        isBurning = true;
        return GENERATING;
    }

    /**
     * Fluid output and heat is handled here, as it occurs whether the multi is running or not.
     */
    @Override
    public void onPostTick(IGregTechTileEntity thiz, long tick) {
        super.onPostTick(thiz, tick);
        heatBoost += isBurning ? 1 : (int) -(tick % 2); // heat one degree per tick, cool at half that
        heatBoost = MathHelper.clamp_int(heatBoost, 0, MAX_BHEAT_BY_TIER[tier]); // not too much :D

        // Steam production
        if (!isBurning) return;
        final int eut = getEut(eul);
        storedEU -= eut;
        steam += eut * 2;
        if (steam > steamMax) {
            steam = (int) steamMax;
            ventSteam();
        }
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    private int getHeatBoost(int euPerL) {
        return Math.max(MAX_BHEAT_BY_TIER[tier], euPerL / 5);
    }

    private int getEut(int eul) {
        // EU/t is (heatBoost / 25) * 32 EU/t * 2^tier
        return getHeatBoost(eul) / 25 * (1 << (5 + tier));
    }

    private int getLitersToBurn(int eul) {
        // The fuel buffer needs to have at least 1 tick worth of EU in it every tick.
        // Integer division in various steps mean we never have to hit fractional EU/t targets, making my life much easier
        final int target = Math.max(0, getEut(eul) - storedEU);
        return (int) Math.ceil(target / (float) eul);
    }

    private void ventSteam() {
        // TODO: make a vent function
    }

    protected static int getTierCasing(Block b, int meta) {
        if (b == sBlockCasings1 && meta == 10) return 1; // Bronze
        if (b == sBlockCasings2 && meta == 0) return 2; // Steel
        // TODO: Stainless steel, Inconel, Incoloy
        return 0;
    }

    protected static int getTierPipe(Block b, int meta) {
        if (b == sBlockCasings2 && meta == 12) return 1; // Bronze
        if (b == sBlockCasings2 && meta == 13) return 2; // Steel
        // TODO: Stainless steel, Inconel, Incoloy
        return 0;
    }

    protected static int getTierHotplate(Block b, int meta) {
        if (b == sBlockMetal1 && meta == 3) return 2; // Annealed Copper
        // TODO: Stainless steel, Inconel, Incoloy? others?
        return 0;
    }

    protected static int getTierFirebox(Block b, int meta) {
        if (b == sBlockCasings3 && meta == 13) return 1; // Bronze
        if (b == sBlockCasings3 && meta == 14) return 2; // Steel
        // TODO: Stainless steel, Inconel, Incoloy
        return 0;
    }
}

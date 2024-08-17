package gregtech.common.tileentities.machines.multi.fuelboilers;

import static gregtech.api.GregTech_API.sBlockCasings1;
import static gregtech.api.GregTech_API.sBlockCasings2;
import static gregtech.api.GregTech_API.sBlockCasings3;
import static gregtech.api.GregTech_API.sBlockMetal1;
import static gregtech.api.recipe.check.CheckRecipeResultRegistry.MISSING_WATER;
import static gregtech.api.recipe.check.CheckRecipeResultRegistry.NO_FUEL_FOUND;
import static gregtech.api.recipe.check.CheckRecipeResultRegistry.SUCCESSFUL;
import static gregtech.api.util.GT_RecipeConstants.FUEL_VALUE;
import static net.minecraftforge.fluids.FluidRegistry.WATER;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.CrossAxisAlignment;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.IWindowCreator;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.*;

import gregtech.api.enums.SteamVariant;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;

/**
 * The base class for fuel-based boilers. These burn fuels (like Benzene or Diesel) into Steam, instead of direct
 * combustion - in exchange for the extra step, they burn more efficiently at 1 EU to 2.4L steam.
 */
public abstract class FueledBoiler<T extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T>>
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T> {

    private static final int[] MAX_BHEAT_BY_TIER = { 0, // there is no tier 0!
        50, 100, 200, 500 };

    // Static (while multi is running)
    protected int tier = 0;
    // These are floats so I don't have to do annoying casts later
    protected float waterMax = 1, heatMax = 1, steamMax = 1; // we divide by these, don't want a divide-by-zero

    // Dynamic
    protected int water, heat, steam;
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

    @Override
    public ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic();
    }

    /**
     * Fuel and water consumption is handled here, it only happens when the multi runs.
     */
    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        // There's gotta be a cleaner way to do this
        final boolean waterFirst = mInputHatches.get(0).mRecipeMap == RecipeMaps.waterOnly;
        final GT_MetaTileEntity_Hatch_Input waterIn = mInputHatches.get(waterFirst ? 0 : 1);
        final GT_MetaTileEntity_Hatch_Input fuelIn = mInputHatches.get(waterFirst ? 1 : 0);

        waterMax = waterIn.getCapacity();
        heatMax = 100 + MAX_BHEAT_BY_TIER[tier];

        final GT_Recipe r = getRecipeMap().findRecipeQuery()
            .fluids(fuelIn.getFluid())
            .find();
        if (r == null) return NO_FUEL_FOUND;

        // Fuel found, time to burn!

        // This never NPEs, due to earlier checks
        @SuppressWarnings("DataFlowIssue")
        final int eul = r.getMetadata(FUEL_VALUE);
        final int amt = getLitersToBurn(eul);
        if (fuelIn.getFluidAmount() < amt) return NO_FUEL_FOUND;

        // We can burn, is there water?
        final FluidStack w = waterIn.getFluid();
        if (w == null) return MISSING_WATER;
        water = w.getFluid() == WATER ? w.amount : 0;
        // We need 1/80 L water per EU this tick
        final int need = (int) Math.ceil((getEut(eul) - storedWater) / 80D);
        if (need > water) return MISSING_WATER; // TODO: BLEVE?

        // Processing can happen
        this.eul = eul;
        isBurning = true;
        mMaxProgresstime = 1;

        // Do processing and drain tanks
        if (need > 0) storedWater += waterIn.drain(need, true).amount * 80;
        final int eu = fuelIn.drain(amt, true).amount * eul;
        storedEU += eu;
        storedWater -= eu;
        return SUCCESSFUL;
    }

    @Override
    public void saveNBTData(NBTTagCompound nbt) {
        super.saveNBTData(nbt);
        nbt.setInteger("tier", tier);
        nbt.setInteger("storedEU", storedEU);
        nbt.setInteger("storedWater", storedWater);
        nbt.setInteger("heat", heat);
        nbt.setInteger("heatMax", (int) heatMax);
        nbt.setInteger("eul", eul);
        nbt.setBoolean("isBurning", isBurning);
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt) {
        super.loadNBTData(nbt);
        tier = nbt.getInteger("tier");
        storedEU = nbt.getInteger("storedEU");
        storedWater = nbt.getInteger("storedWater");
        heat = nbt.getInteger("heat");
        heatMax = nbt.getInteger("heatMax");
        eul = nbt.getInteger("eul");
        isBurning = nbt.getBoolean("isBurning");
    }

    /**
     * Fluid output and heat is handled here, as it occurs whether the multi is running or not.
     */
    @Override
    public void onPostTick(IGregTechTileEntity thiz, long tick) {
        super.onPostTick(thiz, tick);

        // Update displayed steam
        final GT_MetaTileEntity_Hatch_Output s;
        final boolean hasSteamHatch = !mOutputHatches.isEmpty();
        if (hasSteamHatch) {
            s = mOutputHatches.get(0);
            final FluidStack f = s.getFluid();
            final boolean hasSteam = f != null && f.getFluid() == FluidRegistry.getFluid("steam");
            steam = hasSteam ? s.getFluidAmount() : 0;
            steamMax = s.getCapacity();
        } else s = null;

        // Heat one degree per tick, cool at half that
        heat += isBurning ? 1 : (int) -(tick % 2);
        heat = MathHelper.clamp_int(heat, 0, 100 + MAX_BHEAT_BY_TIER[tier]);

        // Steam production
        if (!isBurning) return;
        final int eut = Math.min(storedEU, getEut(eul));
        storedEU -= eut;
        if (hasSteamHatch && heat >= 100) {
            final int amt = s.fill(GT_ModHandler.getSteam((long) (eut * 2L * (heat - 100) / (heatMax - 100))), true);
            if (amt < eut * 2) ventSteam();
            steam += amt;
        }
        isBurning = false;
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

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        buildContext.addSyncedWindow(10, this.createMonitorWindow);
        builder.widget(
            new ButtonWidget().setOnClick(
                (clickData, widget) -> {
                    if (!widget.isClient()) widget.getContext()
                        .openSyncedWindow(10);
                })
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> ret = new ArrayList<>();
                    ret.add(GT_UITextures.BUTTON_STANDARD);
                    // TODO: some flame texture? or steam?
                    ret.add(GT_UITextures.OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID);
                    return ret.toArray(new IDrawable[0]);
                })
                .addTooltip("Boiler Monitor")
                .setPos(174, 130));
    }

    private final IWindowCreator createMonitorWindow = (EntityPlayer ignored) -> {
        final int pBarOffset = 30;
        final int pBarPad = 10;
        final ModularWindow.Builder b = ModularWindow.builder(190, 50);
        b.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        b.setGuiTint(getGUIColorization());

        b.widget(new TextWidget("Boiler Monitor").setPos(5, 5));
        b.widget(
            ButtonWidget.closeWindowButton(true)
                .setPos(185, 3));
        b.widget(
            new DynamicPositionedColumn().setSynced(false)
                .widget(
                    new MultiChildWidget().addChild(new TextWidget("Water"))
                        .addChild(
                            new ProgressBar().setProgress(() -> water / waterMax)
                                .setDirection(ProgressBar.Direction.RIGHT)
                                .setTexture(
                                    GT_UITextures.PROGRESSBAR_BOILER_EMPTY_STEAM_R90
                                        .get(tier == 1 ? SteamVariant.BRONZE : SteamVariant.STEEL),
                                    GT_UITextures.PROGRESSBAR_BOILER_WATER_R90,
                                    54)
                                .setPos(pBarOffset, 0)
                                .setSizeProvider(
                                    (screenSize, window,
                                        parent) -> new Size(window.getSize().width - pBarOffset - pBarPad, 10))))
                .widget(
                    new MultiChildWidget().addChild(new TextWidget("Heat"))
                        .addChild(
                            new ProgressBar().setProgress(() -> heat / heatMax)
                                .setDirection(ProgressBar.Direction.RIGHT)
                                .setTexture(
                                    GT_UITextures.PROGRESSBAR_BOILER_EMPTY_STEAM_R90
                                        .get(tier == 1 ? SteamVariant.BRONZE : SteamVariant.STEEL),
                                    GT_UITextures.PROGRESSBAR_BOILER_HEAT_R90,
                                    54)
                                .setPos(pBarOffset, 0)
                                .setSizeProvider(
                                    (screenSize, window,
                                        parent) -> new Size(window.getSize().width - pBarOffset - pBarPad, 10))))
                .widget(
                    new MultiChildWidget().addChild(new TextWidget("Steam"))
                        .addChild(
                            new ProgressBar().setProgress(() -> steam / steamMax)
                                .setDirection(ProgressBar.Direction.RIGHT)
                                .setTexture(
                                    GT_UITextures.PROGRESSBAR_BOILER_EMPTY_STEAM_R90
                                        .get(tier == 1 ? SteamVariant.BRONZE : SteamVariant.STEEL),
                                    GT_UITextures.PROGRESSBAR_BOILER_STEAM_R90,
                                    54)
                                .setPos(pBarOffset, 0)
                                .setSizeProvider(
                                    (screenSize, window,
                                        parent) -> new Size(window.getSize().width - pBarOffset - pBarPad, 10))))
                .setAlignment(CrossAxisAlignment.END)
                .setPos(5, 15));

        return b.build();
    };

    @Override
    protected void setHatchRecipeMap(GT_MetaTileEntity_Hatch_Input hatch) {
        // Inheriting boilers should set these manually
    }

    protected boolean addHatchWithRecipeMap(IGregTechTileEntity te, int baseCasingIndex, RecipeMap<?> map) {
        if (te == null) return false;
        final IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte instanceof final GT_MetaTileEntity_Hatch_Input hatch) {
            hatch.updateTexture(baseCasingIndex);
            hatch.mRecipeMap = map;
            return mInputHatches.add(hatch);
        }
        return false;
    }

    protected int getHeatBoost(int euPerL) {
        return Math.max(MAX_BHEAT_BY_TIER[tier], euPerL / 5);
    }

    protected abstract int getEut(int eul);

    private int getLitersToBurn(int eul) {
        // The fuel buffer needs to have at least 1 tick worth of EU in it every tick.
        // Integer division in various steps mean we never have to hit fractional EU/t targets, making my life much
        // easier
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

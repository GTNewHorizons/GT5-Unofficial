/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bwcrossmod.galacticgreg;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import galacticgreg.api.ModDimensionDef;
import galacticgreg.api.enums.DimensionDef;
import gregtech.api.enums.GTValues;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ORE_DRILL;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ORE_DRILL_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ORE_DRILL_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ORE_DRILL_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.getCasingTextureForId;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.tileentities.machines.multi.MTEDrillerBase;
import gtneioreplugin.util.DimensionHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public abstract class MTEVoidMinerBase<T extends MTEVoidMinerBase<T>> extends MTEEnhancedMultiBlockBase<T>
    implements ISurvivalConstructable {

    private ModDimensionDef dimensionDef;
    private boolean canVoidMine = true;
    private VoidMinerUtility.DropMap dropMap = null;
    private VoidMinerUtility.DropMap extraDropMap = null;
    protected int casingTextureIndex;
    private float totalWeight;

    private int multiplier = 1;
    protected final byte TIER_MULTIPLIER;

    private boolean mBlacklist = false;

    public MTEVoidMinerBase(int aID, String aName, String aNameRegional, int tier) {
        super(aID, aName, aNameRegional);
        this.TIER_MULTIPLIER = (byte) Math.max(tier, 1);
        casingTextureIndex = getControllerTextureIndex();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("mBlacklist", this.mBlacklist);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.mBlacklist = aNBT.getBoolean("mBlacklist");
    }

    public MTEVoidMinerBase(String aName, int tier) {
        super(aName);
        this.TIER_MULTIPLIER = (byte) tier;
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        setElectricityStats();
        if (working()) {
            return SimpleCheckRecipeResult.ofSuccess("drill_extracting_ores");
        } else {
            return SimpleCheckRecipeResult.ofFailure("drill_extracting_ores_failed");
        }
    }

    protected int getMinTier() {
        return this.TIER_MULTIPLIER + 5; // min tier = LuV
    }

    protected void setElectricityStats() {
        this.mEUt = -Math.abs(Math.toIntExact(GTValues.V[this.getMinTier()]));
        this.mOutputItems = GTValues.emptyItemStackArray;
        this.mProgresstime = 0;
        this.mMaxProgresstime = 10 * (batchMode ? 16 : 1);
        this.mEfficiency = this.getCurrentEfficiency(null);
        this.mEfficiencyIncrease = 10000;
        this.mEUt = this.mEUt > 0 ? -this.mEUt : this.mEUt;
    }

    protected boolean working() {
        if (!canVoidMine) {
            return false;
        }

        if (this.totalWeight != 0.f) {
            this.handleFluidConsumption();
            this.handleOutputs();
            return true;
        } else {
            this.stopMachine(ShutDownReasonRegistry.NONE);
            return false;
        }
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Miner")
            .addInfo("Consumes " + GTValues.V[this.getMinTier()] + "EU/t")
            .addInfo(
                "Can be supplied with 2L/s of Neon(x4), Krypton(x8), Xenon(x16) or Oganesson(x64) for higher outputs.")
            .addInfo(
                "Will output " + 2 * this.TIER_MULTIPLIER
                    + " Ores per Second depending on the Dimension it is build in")
            .addInfo("Put the Ore into the input bus to set the Whitelist/Blacklist")
            .addInfo("Use a screwdriver to toggle Whitelist/Blacklist")
            .addInfo("You can enable batch mode with wire cutters." + EnumChatFormatting.BLUE + " 16x Time 16x Output")
            .addInfo(
                "Blacklist or non Whitelist Ore will be " + EnumChatFormatting.DARK_RED
                    + "VOIDED"
                    + EnumChatFormatting.RESET
                    + ".")
            .toolTipFinisher();
        return tt;
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);

        if (!canVoidMine) {
            String dimensionName = dimensionDef == null ? "unknown"
                : DimensionHelper.getDimLocalizedName(dimensionDef.getDimensionName());
            String text = I18n.format("GT5U.gui.text.no_void_mining", dimensionName);
            screenElements.addChild(new TextWidget(text).setTextAlignment(Alignment.TopLeft));
        }
    }

    protected List<IHatchElement<? super MTEDrillerBase>> getAllowedHatches() {
        return ImmutableList.of(InputHatch, InputBus, OutputBus, Maintenance, Energy);
    }

    /**
     * method used to pick the next ore in the dropMap.
     *
     * @return the chosen ore
     */
    private ItemStack nextOre() {
        float currentWeight = 0.f;
        while (true) {
            float randomNumber = XSTR.XSTR_INSTANCE.nextFloat() * this.totalWeight;
            for (Map.Entry<GTUtility.ItemId, Float> entry : this.dropMap.getInternalMap()
                .entrySet()) {
                currentWeight += entry.getValue();
                if (randomNumber < currentWeight) return entry.getKey()
                    .getItemStack();
            }
            for (Map.Entry<GTUtility.ItemId, Float> entry : this.extraDropMap.getInternalMap()
                .entrySet()) {
                currentWeight += entry.getValue();
                if (randomNumber < currentWeight) return entry.getKey()
                    .getItemStack();
            }
        }
    }

    /**
     * Method used to check the current gat and its corresponding multiplier
     *
     * @return the noble gas in the hatch. returns null if there is no noble gas found.
     */
    private FluidStack getNobleGasInputAndSetMultiplier() {
        for (FluidStack s : this.getStoredFluids()) {
            for (int i = 0; i < VoidMinerUtility.NOBLE_GASSES.length; i++) {
                FluidStack ng = VoidMinerUtility.NOBLE_GASSES[i];
                if (ng.isFluidEqual(s)) {
                    this.multiplier = this.TIER_MULTIPLIER * VoidMinerUtility.NOBLE_GASSES_MULTIPLIER[i];
                    return s;
                }
            }
        }
        return null;
    }

    /**
     * method used to decrement the quantity of gas in the hatch
     *
     * @param gasToConsume the fluid stack in the hatch
     * @return if yes or no it was able to decrement the quantity of the fluidStack
     */
    private boolean consumeNobleGas(FluidStack gasToConsume) {
        for (FluidStack s : this.getStoredFluids()) {
            if (s.isFluidEqual(gasToConsume) && s.amount >= 1) {
                s.amount -= 1;
                this.updateSlots();
                return true;
            }
        }
        return false;
    }

    /**
     * handler for the fluid consumption
     */
    private void handleFluidConsumption() {
        FluidStack storedNobleGas = this.getNobleGasInputAndSetMultiplier();
        if (storedNobleGas == null || !this.consumeNobleGas(storedNobleGas)) this.multiplier = this.TIER_MULTIPLIER;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        calculateDropMap();
    }

    /**
     * Computes first the ores related to the dim the VM is in, then the ores added manually, then it computes the
     * totalWeight for normalisation
     */
    private void calculateDropMap() {
        this.dropMap = null;
        this.extraDropMap = null;
        this.totalWeight = 0;
        this.canVoidMine = false;

        dimensionDef = DimensionDef.getDefForWorld(getBaseMetaTileEntity().getWorld());

        if (dimensionDef == null || !dimensionDef.canBeVoidMined()) return;

        this.canVoidMine = true;

        this.dropMap = VoidMinerUtility.dropMapsByDimName
            .getOrDefault(dimensionDef.getDimensionName(), new VoidMinerUtility.DropMap());
        this.extraDropMap = VoidMinerUtility.extraDropsByDimName
            .getOrDefault(dimensionDef.getDimensionName(), new VoidMinerUtility.DropMap());

        this.totalWeight = dropMap.getTotalWeight() + extraDropMap.getTotalWeight();
    }

    /**
     * Output logic of the VM
     */
    private void handleOutputs() {
        final List<ItemStack> inputOres = this.getStoredInputs()
            .stream()
            .filter(GTUtility::isOre)
            .collect(Collectors.toList());

        if (canVoidMine) {
            final ItemStack output = this.nextOre();
            output.stackSize = multiplier * (batchMode ? 16 : 1);

            boolean matchesFilter = contains(inputOres, output);

            if (inputOres.isEmpty() || (this.mBlacklist ? !matchesFilter : matchesFilter)) {
                this.addOutput(output);
            }
        }

        this.updateSlots();
    }

    private static boolean contains(List<ItemStack> list, ItemStack stack) {
        for (ItemStack cursor : list) {
            if (GTUtility.areStacksEqual(cursor, stack)) return true;
        }

        return false;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        this.mBlacklist = !this.mBlacklist;
        GTUtility.sendChatToPlayer(aPlayer, "Mode: " + (this.mBlacklist ? "Blacklist" : "Whitelist"));
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        this.batchMode = !this.batchMode;
        GTUtility.sendChatToPlayer(aPlayer, "Batch Mode: " + (this.batchMode ? "Enabled" : "Disabled"));
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] { getCasingTextureForId(casingTextureIndex), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ORE_DRILL_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ORE_DRILL_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { getCasingTextureForId(casingTextureIndex), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ORE_DRILL)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ORE_DRILL_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { getCasingTextureForId(casingTextureIndex) };
    }

    protected boolean checkHatches() {
        return !mMaintenanceHatches.isEmpty() && !mInputHatches.isEmpty()
            && !mOutputBusses.isEmpty()
            && !mEnergyHatches.isEmpty();
    }

    public abstract int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env);

    protected abstract int getControllerTextureIndex();

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }
}

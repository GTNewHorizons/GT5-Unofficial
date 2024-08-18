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

package com.github.bartimaeusnek.crossmod.galacticgreg;

import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.MULTIBLOCK_ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_Values.VN;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ImmutableList;

import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_DrillerBase;

public abstract class GT_TileEntity_VoidMiner_Base extends GT_MetaTileEntity_DrillerBase {

    private VoidMinerUtility.DropMap dropMap = null;
    private VoidMinerUtility.DropMap extraDropMap = null;
    private float totalWeight;
    private int multiplier = 1;

    protected final byte TIER_MULTIPLIER;

    private boolean mBlacklist = false;

    /**
     * @Deprecated Use {@link VoidMinerUtility#addBlockToDimensionList}
     */
    @Deprecated
    public static void addBlockToDimensionList(int dimId, Block block, int meta, float weight) {
        VoidMinerUtility.addBlockToDimensionList(dimId, block, meta, weight);
    }

    public GT_TileEntity_VoidMiner_Base(int aID, String aName, String aNameRegional, int tier) {
        super(aID, aName, aNameRegional);
        this.TIER_MULTIPLIER = (byte) Math.max(tier, 1);
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

    public GT_TileEntity_VoidMiner_Base(String aName, int tier) {
        super(aName);
        this.TIER_MULTIPLIER = (byte) tier;
    }

    @Override
    protected int getMinTier() {
        return this.TIER_MULTIPLIER + 5; // min tier = LuV
    }

    @Override
    protected boolean checkHatches() {
        return true;
    }

    @Override
    protected void setElectricityStats() {
        try {
            this.mEUt = this.isPickingPipes ? 60 : Math.toIntExact(GT_Values.V[this.getMinTier()]);
        } catch (ArithmeticException e) {
            e.printStackTrace();
            this.mEUt = Integer.MAX_VALUE - 7;
        }
        this.mOutputItems = new ItemStack[0];
        this.mProgresstime = 0;
        this.mMaxProgresstime = 10;
        this.mEfficiency = this.getCurrentEfficiency(null);
        this.mEfficiencyIncrease = 10000;
        this.mEUt = this.mEUt > 0 ? -this.mEUt : this.mEUt;
    }

    @Override
    protected boolean workingAtBottom(ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe,
        int yHead, int oldYHead) {
        // if the dropMap has never been initialised or if the dropMap is empty
        if (this.dropMap == null || this.totalWeight == 0) this.calculateDropMap();

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
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        String casings = this.getCasingBlockItem()
            .get(0)
            .getDisplayName();

        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Miner")
            .addInfo("Controller Block for the Void Miner " + GT_Values.VN[this.getMinTier()])
            .addInfo("Consumes " + GT_Values.V[this.getMinTier()] + "EU/t")
            .addInfo(
                "Can be supplied with 2L/s of Neon(x4), Krypton(x8), Xenon(x16) or Oganesson(x64) for higher outputs.")
            .addInfo(
                "Will output " + 2 * this.TIER_MULTIPLIER
                    + " Ores per Second depending on the Dimension it is build in")
            .addInfo("Put the Ore into the input bus to set the Whitelist/Blacklist")
            .addInfo("Use a screwdriver to toggle Whitelist/Blacklist")
            .addInfo(
                "Blacklist or non Whitelist Ore will be " + EnumChatFormatting.DARK_RED
                    + "VOIDED"
                    + EnumChatFormatting.RESET
                    + ".")
            .addSeparator()
            .beginStructureBlock(3, 7, 3, false)
            .addController("Front bottom")
            .addOtherStructurePart(casings, "form the 3x1x3 Base")
            .addOtherStructurePart(casings, "1x3x1 pillar above the center of the base (2 minimum total)")
            .addOtherStructurePart(
                this.getFrameMaterial().mName + " Frame Boxes",
                "Each pillar's side and 1x3x1 on top")
            .addEnergyHatch(VN[this.getMinTier()] + "+, Any base casing")
            .addMaintenanceHatch("Any base casing")
            .addInputBus("Mining Pipes or Ores, optional, any base casing")
            .addInputHatch("Optional noble gas, any base casing")
            .addOutputBus("Any base casing")
            .toolTipFinisher(MULTIBLOCK_ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS);
        return tt;
    }

    @Override
    protected List<IHatchElement<? super GT_MetaTileEntity_DrillerBase>> getAllowedHatches() {
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
            for (Map.Entry<GT_Utility.ItemId, Float> entry : this.dropMap.getInternalMap()
                .entrySet()) {
                currentWeight += entry.getValue();
                if (randomNumber < currentWeight) return entry.getKey()
                    .getItemStack();
            }
            for (Map.Entry<GT_Utility.ItemId, Float> entry : this.extraDropMap.getInternalMap()
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
                    this.multiplier = this.TIER_MULTIPLIER * VoidMinerUtility.NOBEL_GASSES_MULTIPLIER[i];
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

    /**
     * Handles the ores added manually with {@link VoidMinerUtility#addMaterialToDimensionList}
     *
     * @param id the specified dim id
     */
    private void handleExtraDrops(int id) {
        if (VoidMinerUtility.extraDropsDimMap.containsKey(id)) {
            extraDropMap = VoidMinerUtility.extraDropsDimMap.get(id);
        }
    }

    /**
     * Gets the DropMap of the dim for the specified dim id
     *
     * @param id the dim number
     */
    private void handleModDimDef(int id) {
        if (VoidMinerUtility.dropMapsByDimId.containsKey(id)) {
            this.dropMap = VoidMinerUtility.dropMapsByDimId.get(id);
        } else {
            String chunkProviderName = ((ChunkProviderServer) this.getBaseMetaTileEntity()
                .getWorld()
                .getChunkProvider()).currentChunkProvider.getClass()
                    .getName();

            if (VoidMinerUtility.dropMapsByChunkProviderName.containsKey(chunkProviderName)) {
                this.dropMap = VoidMinerUtility.dropMapsByChunkProviderName.get(chunkProviderName);
            }
        }
    }

    /**
     * Computes first the ores related to the dim the VM is in, then the ores added manually, then it computes the
     * totalWeight for normalisation
     */
    private void calculateDropMap() {
        this.dropMap = new VoidMinerUtility.DropMap();
        this.extraDropMap = new VoidMinerUtility.DropMap();
        int id = this.getBaseMetaTileEntity()
            .getWorld().provider.dimensionId;
        this.handleModDimDef(id);
        this.handleExtraDrops(id);
        this.totalWeight = dropMap.getTotalWeight() + extraDropMap.getTotalWeight();
    }

    /**
     * Output logic of the VM
     */
    private void handleOutputs() {
        final List<ItemStack> inputOres = this.getStoredInputs()
            .stream()
            .filter(GT_Utility::isOre)
            .collect(Collectors.toList());;
        final ItemStack output = this.nextOre();
        output.stackSize = multiplier;
        if (inputOres.size() == 0 || this.mBlacklist && inputOres.stream()
            .noneMatch(is -> GT_Utility.areStacksEqual(is, output))
            || !this.mBlacklist && inputOres.stream()
                .anyMatch(is -> GT_Utility.areStacksEqual(is, output)))
            this.addOutput(output);
        this.updateSlots();
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        this.mBlacklist = !this.mBlacklist;
        GT_Utility.sendChatToPlayer(aPlayer, "Mode: " + (this.mBlacklist ? "Blacklist" : "Whitelist"));
    }
}

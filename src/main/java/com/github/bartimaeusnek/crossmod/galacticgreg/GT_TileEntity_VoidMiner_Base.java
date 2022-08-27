/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.crossmod.galacticgreg;

import static bloodasp.galacticgreg.registry.GalacticGregRegistry.getModContainers;
import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.MULTIBLOCK_ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS;
import static gregtech.api.enums.GT_Values.VN;

import bloodasp.galacticgreg.GT_Worldgen_GT_Ore_Layer_Space;
import bloodasp.galacticgreg.GT_Worldgen_GT_Ore_SmallPieces_Space;
import bloodasp.galacticgreg.GalacticGreg;
import bloodasp.galacticgreg.api.ModDimensionDef;
import bloodasp.galacticgreg.bartworks.BW_Worldgen_Ore_Layer_Space;
import bloodasp.galacticgreg.bartworks.BW_Worldgen_Ore_SmallOre_Space;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.system.oregen.BW_OreLayer;
import com.github.bartimaeusnek.bartworks.util.Pair;
import com.google.common.collect.ArrayListMultimap;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Worldgen_GT_Ore_Layer;
import gregtech.common.GT_Worldgen_GT_Ore_SmallPieces;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_DrillerBase;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.fluids.FluidStack;

@SuppressWarnings("ALL")
public abstract class GT_TileEntity_VoidMiner_Base extends GT_MetaTileEntity_DrillerBase {

    private static ArrayListMultimap<Integer, Pair<Pair<Integer, Boolean>, Float>> extraDropsDimMap =
            ArrayListMultimap.create();
    private static FluidStack[] NOBLE_GASSES = new FluidStack[] {
        WerkstoffLoader.Neon.getFluidOrGas(1),
        WerkstoffLoader.Krypton.getFluidOrGas(1),
        WerkstoffLoader.Xenon.getFluidOrGas(1),
        WerkstoffLoader.Oganesson.getFluidOrGas(1)
    };

    private HashMap<Pair<Integer, Boolean>, Float> dropmap = null;
    private float totalWeight;
    private int multiplier = 1;

    protected byte TIER_MULTIPLIER;

    private boolean mBlacklist = false;

    /**
     * Public method giving other mods the ability to add manually a material with an ore version into the external dromap for a specified dim id
     * @param DimensionID the dim id targetted
     * @param Material the material with an ore version
     * @param weight the non normalised version of the given weight
     */
    public static void addMatierialToDimensionList(int DimensionID, ISubTagContainer Material, float weight) {
        if (Material instanceof Materials)
            getExtraDropsDimMap()
                    .put(DimensionID, new Pair<>(new Pair<>(((Materials) Material).mMetaItemSubID, false), weight));
        else if (Material instanceof Werkstoff)
            getExtraDropsDimMap()
                    .put(DimensionID, new Pair<>(new Pair<>((int) ((Werkstoff) Material).getmID(), true), weight));
    }

    // adding tellurium to OW to ensure a way to get it, as it's used in Magneto Resonatic Dust and Circuit Compound MK3
    // Dust
    static {
        addMatierialToDimensionList(0, Materials.Tellurium, 8.0f);
    }

    public GT_TileEntity_VoidMiner_Base(int aID, String aName, String aNameRegional, int tier) {
        super(aID, aName, aNameRegional);
        TIER_MULTIPLIER = (byte) Math.max(tier, 1);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("TIER_MULTIPLIER", TIER_MULTIPLIER);
        aNBT.setBoolean("mBlacklist", mBlacklist);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        TIER_MULTIPLIER = aNBT.getByte("TIER_MULTIPLIER");
        mBlacklist = aNBT.getBoolean("mBlacklist");
    }

    public GT_TileEntity_VoidMiner_Base(String aName, int tier) {
        super(aName);
        TIER_MULTIPLIER = (byte) tier;
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
    protected boolean workingAtBottom(
            ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe, int yHead, int oldYHead) {
        // if the dropmap has never been initialised or if the dropmap is empty
        if (dropmap == null || totalWeight == 0) calculateDropMap();

        if (totalWeight != 0.f) {
            handleFluidConsumption();
            handleOutputs();
            return true;
        } else {
            stopMachine();
            return false;
        }
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        String casings = getCasingBlockItem().get(0).getDisplayName();

        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Miner")
                .addInfo("Controller Block for the Void Miner " + GT_Values.VN[this.getMinTier()])
                .addInfo("Consumes " + GT_Values.V[this.getMinTier()] + "EU/t")
                .addInfo(
                        "Can be supplied with 2L/s of Neon(x4), Krypton(x8), Xenon(x16) or Oganesson(x64) for higher outputs.")
                .addInfo("Will output " + (2 * TIER_MULTIPLIER)
                        + " Ores per Second depending on the Dimension it is build in")
                .addInfo("Put the Ore into the input bus to set the Whitelist/Blacklist")
                .addInfo("Use a screwdriver to toggle Whitelist/Blacklist")
                .addInfo("Blacklist or non Whitelist Ore will be VOIDED")
                .addSeparator()
                .beginStructureBlock(3, 7, 3, false)
                .addController("Front bottom")
                .addOtherStructurePart(casings, "form the 3x1x3 Base")
                .addOtherStructurePart(casings, "1x3x1 pillar above the center of the base (2 minimum total)")
                .addOtherStructurePart(getFrameMaterial().mName + " Frame Boxes", "Each pillar's side and 1x3x1 on top")
                .addEnergyHatch(VN[getMinTier()] + "+, Any base casing")
                .addMaintenanceHatch("Any base casing")
                .addInputBus("Mining Pipes or Ores, optional, any base casing")
                .addInputHatch("Optional noble gas, any base casing")
                .addOutputBus("Any base casing")
                .toolTipFinisher(MULTIBLOCK_ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS);
        return tt;
    }

    /**
     * getter for the external drop map
     * @return the extraDriosDimMap
     */
    public static ArrayListMultimap<Integer, Pair<Pair<Integer, Boolean>, Float>> getExtraDropsDimMap() {
        return extraDropsDimMap;
    }

    /**
     * Makes a predicate for the GT normal ore veins worldgen
     * @return the predicate
     */
    private Predicate<GT_Worldgen_GT_Ore_Layer> makeOreLayerPredicate() {
        World world = this.getBaseMetaTileEntity().getWorld();
        switch (world.provider.dimensionId) {
            case -1:
                return gt_worldgen -> gt_worldgen.mNether;
            case 0:
                return gt_worldgen -> gt_worldgen.mOverworld;
            case 1:
                return gt_worldgen -> gt_worldgen.mEnd || gt_worldgen.mEndAsteroid;
            case 7:
                /*
                explicitely giving different dim numbers so it default to false in the config, keeping compat
                with the current worldgen config
                */

                return gt_worldgen -> gt_worldgen.isGenerationAllowed(world, 0, 7);
            default:
                throw new IllegalStateException();
        }
    }

    /**
     * Makes a predicate for the GT normal small ore worldgen
     * @return the predicate
     */
    private Predicate<GT_Worldgen_GT_Ore_SmallPieces> makeSmallOresPredicate() {
        World world = this.getBaseMetaTileEntity().getWorld();
        switch (world.provider.dimensionId) {
            case -1:
                return gt_worldgen -> gt_worldgen.mNether;
            case 0:
                return gt_worldgen -> gt_worldgen.mOverworld;
            case 1:
                return gt_worldgen -> gt_worldgen.mEnd;
            case 7:
                /*
                explicitely giving different dim numbers so it default to false in the config, keeping compat
                with the current worldgen config
                */
                return gt_worldgen -> gt_worldgen.isGenerationAllowed(world, 0, 7);
            default:
                throw new IllegalStateException();
        }
    }

    /**
     * Method used to add an ore to the droplist
     * @param key a Pair of <int, bool> where the int is the meta of the ore, and the bool is true for BW ores, false for GT ores
     * @param value the non normalised weight
     */
    private void addDrop(Pair<Integer, Boolean> key, float value) {
        final ItemStack ore = getOreItemStack(key);
        if (ConfigHandler.voidMinerBlacklist.contains(String.format(
                "%s:%d", GameRegistry.findUniqueIdentifierFor(ore.getItem()).toString(), ore.getItemDamage()))) return;
        if (!dropmap.containsKey(key)) dropmap.put(key, value);
        else dropmap.put(key, dropmap.get(key) + value);
    }

    /**
     * Method to add the ores of a vanilla GT worldgen
     * @param oreLayerPredicate the predicate made by makeOreLayerPredicate
     */
    private void getDropsVanillaVeins(Predicate<GT_Worldgen_GT_Ore_Layer> oreLayerPredicate) {
        GT_Worldgen_GT_Ore_Layer.sList.stream()
                .filter(gt_worldgen -> gt_worldgen.mEnabled && oreLayerPredicate.test(gt_worldgen))
                .forEach(element -> {
                    addDrop(new Pair<>((int) element.mPrimaryMeta, false), (float) element.mWeight);
                    addDrop(new Pair<>((int) element.mSecondaryMeta, false), (float) element.mWeight);
                    addDrop(new Pair<>((int) element.mSporadicMeta, false), (element.mWeight / 8f));
                    addDrop(new Pair<>((int) element.mBetweenMeta, false), (element.mWeight / 8f));
                });
    }

    /**
     * Method to add the small ores of a vanilla GT worldgen
     * @param smallOresPredicate the predicate made by makeSmallOresPredicate
     */
    private void getDropsVanillaSmallOres(Predicate<GT_Worldgen_GT_Ore_SmallPieces> smallOresPredicate) {
        GT_Worldgen_GT_Ore_SmallPieces.sList.stream()
                .filter(gt_worldgen -> gt_worldgen.mEnabled && smallOresPredicate.test(gt_worldgen))
                .forEach(element -> addDrop(new Pair<>((int) element.mMeta, false), (float) element.mAmount));
    }

    /**
     * add to the dropmap the ores from the gagreg space worldgen corresponding to the target dim
     * @param finalDef ModDimensionDef corresponding to the target dim
     */
    private void getDropsOreVeinsSpace(ModDimensionDef finalDef) {
        Set space = GalacticGreg.oreVeinWorldgenList.stream()
                .filter(gt_worldgen -> gt_worldgen.mEnabled
                        && gt_worldgen instanceof GT_Worldgen_GT_Ore_Layer_Space
                        && ((GT_Worldgen_GT_Ore_Layer_Space) gt_worldgen).isEnabledForDim(finalDef))
                .collect(Collectors.toSet());

        space.forEach(element -> {
            addDrop(new Pair<>((int) ((GT_Worldgen_GT_Ore_Layer_Space) element).mPrimaryMeta, false), (float)
                    ((GT_Worldgen_GT_Ore_Layer_Space) element).mWeight);
            addDrop(new Pair<>((int) ((GT_Worldgen_GT_Ore_Layer_Space) element).mSecondaryMeta, false), (float)
                    ((GT_Worldgen_GT_Ore_Layer_Space) element).mWeight);
            addDrop(
                    new Pair<>((int) ((GT_Worldgen_GT_Ore_Layer_Space) element).mSporadicMeta, false),
                    (((GT_Worldgen_GT_Ore_Layer_Space) element).mWeight / 8f));
            addDrop(
                    new Pair<>((int) ((GT_Worldgen_GT_Ore_Layer_Space) element).mBetweenMeta, false),
                    (((GT_Worldgen_GT_Ore_Layer_Space) element).mWeight / 8f));
        });
    }

    /**
     * add to the dropmap the small ores from the gagreg space worldgen corresponding to the target dim
     * @param finalDef ModDimensionDef corresponding to the target dim
     */
    private void getDropsSmallOreSpace(ModDimensionDef finalDef) {
        Set space = GalacticGreg.smallOreWorldgenList.stream()
                .filter(gt_worldgen -> gt_worldgen.mEnabled
                        && gt_worldgen instanceof GT_Worldgen_GT_Ore_SmallPieces_Space
                        && ((GT_Worldgen_GT_Ore_SmallPieces_Space) gt_worldgen).isEnabledForDim(finalDef))
                .collect(Collectors.toSet());

        space.forEach(element ->
                addDrop(new Pair<>((int) ((GT_Worldgen_GT_Ore_SmallPieces_Space) element).mMeta, false), (float)
                        ((GT_Worldgen_GT_Ore_SmallPieces_Space) element).mAmount));
    }

    /**
     * method used to pick the next key in the dropmap that will be used to generate the ore.
     * @return the chosen key
     */
    private Pair<Integer, Boolean> getOreDamage() {
        float curentWeight = 0.f;
        while (true) {
            float randomnumber = XSTR.XSTR_INSTANCE.nextFloat() * totalWeight;
            for (Map.Entry<Pair<Integer, Boolean>, Float> entry : dropmap.entrySet()) {
                curentWeight += entry.getValue();
                if (randomnumber < curentWeight) return entry.getKey();
            }
        }
    }

    /**
     * Method used to check the current gat and its corresponding multiplier
     * @return the noble gas in the hatch. returns null if there is no noble gas found.
     */
    private FluidStack getNobleGasInputAndSetMultiplier() {
        for (FluidStack s : this.getStoredFluids()) {
            for (int i = 0; i < NOBLE_GASSES.length; i++) {
                FluidStack ng = NOBLE_GASSES[i];
                if (ng.isFluidEqual(s)) {
                    multiplier = TIER_MULTIPLIER * (2 << (i == NOBLE_GASSES.length - 1 ? (i + 2) : (i + 1)));
                    return s;
                }
            }
        }
        return null;
    }

    /**
     * method used to decrement the quantity of gas in the hatch
     * @param gasToConsume the fluid stack in the hatch
     * @return if yes or no it was able to decrement the quantity of the fluidstack
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
        FluidStack storedNobleGas = getNobleGasInputAndSetMultiplier();
        if (storedNobleGas == null || !consumeNobleGas(storedNobleGas)) multiplier = TIER_MULTIPLIER;
    }

    /**
     * Handles the addition of Ross dims' ores into the drop map
     * @param aID dim id of Ross128b or Ross128ba
     */
    private void getDropMapRoss(int aID) {
        Consumer<BW_OreLayer> addToList = makeAddToList();
        BW_OreLayer.sList.stream()
                .filter(gt_worldgen -> gt_worldgen.mEnabled
                        && gt_worldgen instanceof BW_OreLayer
                        && gt_worldgen.isGenerationAllowed(null, aID, 0))
                .collect(Collectors.toSet())
                .forEach(addToList);
    }

    /**
     * Method used to generate a consumer used specifically to add BW ores into the dropmap
     * @return the consumer
     */
    private Consumer<BW_OreLayer> makeAddToList() {
        return element -> {
            List<Pair<Integer, Boolean>> data = element.getStacksRawData();
            for (int i = 0; i < data.size(); i++) {
                if (i < data.size() - 2) addDrop(data.get(i), (float) element.mWeight);
                else addDrop(data.get(i), (element.mWeight / 8f));
            }
        };
    }

    /**
     * Method used to build the ModDimensionDef object corresponding to the dimension the VM is in.
     * @return the ModDimensionDef object.
     */
    private ModDimensionDef makeModDimDef() {
        return getModContainers().stream()
                .flatMap(modContainer -> modContainer.getDimensionList().stream())
                .filter(modDimensionDef -> modDimensionDef
                        .getChunkProviderName()
                        .equals(((ChunkProviderServer)
                                        this.getBaseMetaTileEntity().getWorld().getChunkProvider())
                                .currentChunkProvider
                                .getClass()
                                .getName()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Handles the addition of small ores for bartwork dims
     * @param finalDef the ModDimensionDef object corresponding to the dim
     * @param addToList a consumer used to add the ores from the vein with proper weight
     */
    private void addOresVeinsBartworks(ModDimensionDef finalDef, Consumer<BW_OreLayer> addToList) {
        try {
            Set space = GalacticGreg.oreVeinWorldgenList.stream()
                    .filter(gt_worldgen -> gt_worldgen.mEnabled
                            && gt_worldgen instanceof BW_Worldgen_Ore_Layer_Space
                            && ((BW_Worldgen_Ore_Layer_Space) gt_worldgen).isEnabledForDim(finalDef))
                    .collect(Collectors.toSet());

            space.forEach(addToList);
        } catch (NullPointerException ignored) {
        }
    }

    /**
     * Handles the addition of small ores for bartwork dims
     * @param finalDef the ModDimensionDef object corresponding to the dim
     */
    private void addSmallOresBartworks(ModDimensionDef finalDef) {
        try {
            Set space = GalacticGreg.smallOreWorldgenList.stream()
                    .filter(gt_worldgen -> gt_worldgen.mEnabled
                            && gt_worldgen instanceof BW_Worldgen_Ore_SmallOre_Space
                            && ((BW_Worldgen_Ore_SmallOre_Space) gt_worldgen).isEnabledForDim(finalDef))
                    .collect(Collectors.toSet());

            space.forEach(element -> addDrop(
                    new Pair<>(
                            ((BW_Worldgen_Ore_SmallOre_Space) element).mPrimaryMeta,
                            ((BW_Worldgen_Ore_SmallOre_Space) element).bwOres != 0),
                    (float) ((BW_Worldgen_Ore_SmallOre_Space) element).mDensity));
        } catch (NullPointerException ignored) {
        }
    }

    /**
     * Handles the ores added manually with addMatierialToDimensionList
     * @param id the specified dim id
     */
    private void handleExtraDrops(int id) {
        Optional.ofNullable(getExtraDropsDimMap().get(id))
                .ifPresent(e -> e.forEach(f -> addDrop(f.getKey(), f.getValue())));
    }

    /**
     * Computes the total weight for normalisation
     */
    private void calculateTotalWeight() {
        totalWeight = 0.0f;
        dropmap.values().forEach(f -> totalWeight += f);
    }

    /**
     * Computes the ores of the dim for the specifed dim id
     * @param id the dim number
     */
    private void handleModDimDef(int id) {
        // vanilla dims or TF
        if ((id <= 1 && id >= -1) || id == 7) {
            getDropsVanillaVeins(makeOreLayerPredicate());
            getDropsVanillaSmallOres(makeSmallOresPredicate());

            // ross dims
        } else if (id == ConfigHandler.ross128BID || id == ConfigHandler.ross128BAID) {
            getDropMapRoss(id);

            // other space dims
        } else {
            Optional.ofNullable(makeModDimDef()).ifPresent(def -> {
                // normal space dim
                getDropsOreVeinsSpace(def);
                getDropsSmallOreSpace(def);

                // BW space dim
                Consumer<BW_OreLayer> addToList = makeAddToList();
                addOresVeinsBartworks(def, addToList);
                addSmallOresBartworks(def);
            });
        }
    }

    /**
     * Computes first the ores related to the dim the VM is in, then the ores added manually, then it computes the totalweight for normalisation
     */
    private void calculateDropMap() {
        dropmap = new HashMap<>();
        int id = this.getBaseMetaTileEntity().getWorld().provider.dimensionId;
        handleModDimDef(id);
        handleExtraDrops(id);
        calculateTotalWeight();
    }

    /**
     * Output logic of the VM
     */
    private void handleOutputs() {
        Pair<Integer, Boolean> stats = getOreDamage();
        final List<ItemStack> inputOres =
                this.getStoredInputs().stream().filter(GT_Utility::isOre).collect(Collectors.toList());
        final ItemStack output = getOreItemStack(stats);
        if (inputOres.size() == 0
                || (mBlacklist && inputOres.stream().allMatch(is -> !GT_Utility.areStacksEqual(is, output)))
                || (!mBlacklist && inputOres.stream().anyMatch(is -> GT_Utility.areStacksEqual(is, output))))
            this.addOutput(output);
        this.updateSlots();
    }

    /**
     * Builds the ore item stack from the key specified in the dropmap
     * @param stats the key of the dropmap
     * @return an ItemStack corresponding to the target ore, with a stacksize corresponding to the mutiplier induced by the gas used
     */
    private ItemStack getOreItemStack(Pair<Integer, Boolean> stats) {
        return new ItemStack(
                stats.getValue() ? WerkstoffLoader.BWOres : GregTech_API.sBlockOres1, multiplier, stats.getKey());
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        mBlacklist = !mBlacklist;
        GT_Utility.sendChatToPlayer(aPlayer, "Mode: " + (mBlacklist ? "Blacklist" : "Whitelist"));
    }
}

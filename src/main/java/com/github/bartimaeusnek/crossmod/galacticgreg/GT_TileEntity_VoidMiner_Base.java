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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static bloodasp.galacticgreg.registry.GalacticGregRegistry.getModContainers;
import static gregtech.api.enums.GT_Values.VN;

@SuppressWarnings("ALL")
public abstract class GT_TileEntity_VoidMiner_Base extends GT_MetaTileEntity_DrillerBase {

    private static ArrayListMultimap<Integer,Pair<Pair<Integer,Boolean>, Float>> extraDropsDimMap = ArrayListMultimap.create();
    private static FluidStack[] NOBLE_GASSES = new FluidStack[]{
            WerkstoffLoader.Neon.getFluidOrGas(1),
            WerkstoffLoader.Krypton.getFluidOrGas(1),
            WerkstoffLoader.Xenon.getFluidOrGas(1),
            WerkstoffLoader.Oganesson.getFluidOrGas(1)
    };

    private HashMap<Pair<Integer,Boolean>, Float> dropmap = null;
    private float totalWeight;
    private int multiplier = 1;

    protected byte TIER_MULTIPLIER;

    private boolean mBlacklist = false;

    public static void addMatierialToDimensionList(int DimensionID, ISubTagContainer Material, float weight) {
        if (Material instanceof Materials)
            getExtraDropsDimMap().put(DimensionID, new Pair<>(new Pair<>(((Materials)Material).mMetaItemSubID,false), weight));
        else if (Material instanceof Werkstoff)
            getExtraDropsDimMap().put(DimensionID, new Pair<>(new Pair<>((int) ((Werkstoff)Material).getmID(),true), weight));
    }

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
        aNBT.setByte("TIER_MULTIPLIER",TIER_MULTIPLIER);
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
        return this.TIER_MULTIPLIER + 5; //min tier = LuV
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
    protected boolean workingAtBottom(ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe, int yHead, int oldYHead) {
        makeDropMap();
        if(totalWeight != 0.f){
            handleFluidConsumption();
            handleOutputs();
            return true;
        }
        else{
            stopMachine();
            return false;
        }
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        String casings = getCasingBlockItem().get(0).getDisplayName();

        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Miner")
                .addInfo("Controller Block for the Void Miner "+ GT_Values.VN[this.getMinTier()])
                .addInfo("Consumes " + GT_Values.V[this.getMinTier()] + "EU/t")
                .addInfo("Can be supplied with 2L/s of Neon(x4), Krypton(x8), Xenon(x16) or Oganesson(x64) for higher outputs.")
                .addInfo("Will output "+(2*TIER_MULTIPLIER)+" Ores per Second depending on the Dimension it is build in")
                .addInfo("Put the Ore into the input bus to set the Whitelist/Blacklist")
                .addInfo("Use a screwdriver to toggle Whitelist/Blacklist")
                .addInfo("Blacklist or non Whitelist Ore will be VOIDED")
                .addSeparator()
                .beginStructureBlock(3, 7, 3, false)
                .addController("Front bottom")
                .addStructureInfo(casings + " form the 3x1x3 Base")
                .addOtherStructurePart(casings, " 1x3x1 pillar above the center of the base (2 minimum total)")
                .addOtherStructurePart(getFrameMaterial().mName + " Frame Boxes", "Each pillar's side and 1x3x1 on top")
                .addEnergyHatch(VN[getMinTier()] + "+, Any base casing")
                .addMaintenanceHatch("Any base casing")
                .addInputBus("Mining Pipes or Ores, optional, any base casing")
                .addInputHatch("Optional noble gas, any base casing")
                .addOutputBus("Any base casing")
                .toolTipFinisher("Gregtech");
        return tt;
    }

    public static ArrayListMultimap<Integer, Pair<Pair<Integer, Boolean>, Float>> getExtraDropsDimMap() {
        return extraDropsDimMap;
    }

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

    private void addDrop(Pair<Integer,Boolean> key, float value){
        if(!dropmap.containsKey(key))
            dropmap.put(key, value);
        else
            dropmap.put(key, dropmap.get(key) + value);
    }

    private void getDropsVanillaVeins(Predicate<GT_Worldgen_GT_Ore_Layer> oreLayerPredicate) {
        GT_Worldgen_GT_Ore_Layer.sList.stream().filter(gt_worldgen -> gt_worldgen.mEnabled && oreLayerPredicate.test(gt_worldgen)).forEach(element -> {
                    addDrop(new Pair<>((int) element.mPrimaryMeta,false), (float) element.mWeight);
                    addDrop(new Pair<>((int) element.mSecondaryMeta,false), (float) element.mWeight);
                    addDrop(new Pair<>((int) element.mSporadicMeta,false), (element.mWeight / 8f));
                    addDrop(new Pair<>((int) element.mBetweenMeta,false), (element.mWeight / 8f));
                }
        );
    }

    private void getDropsVanillaSmallOres(Predicate<GT_Worldgen_GT_Ore_SmallPieces> smallOresPredicate) {
        GT_Worldgen_GT_Ore_SmallPieces.sList.stream().filter(gt_worldgen -> gt_worldgen.mEnabled && smallOresPredicate.test(gt_worldgen)).forEach(element ->
                addDrop(new Pair<>((int) element.mMeta,false), (float) element.mAmount)
        );
    }

    private void getDropMapVanilla() {
        getDropsVanillaVeins(makeOreLayerPredicate());
        getDropsVanillaSmallOres(makeSmallOresPredicate());
    }

    private void getDropMapSpace(ModDimensionDef finalDef) {
        getDropsOreVeinsSpace(finalDef);
        getDropsSmallOreSpace(finalDef);
    }

    private void getDropsOreVeinsSpace(ModDimensionDef finalDef) {
        Set space = GalacticGreg.oreVeinWorldgenList.stream()
                .filter(gt_worldgen -> gt_worldgen.mEnabled && gt_worldgen instanceof GT_Worldgen_GT_Ore_Layer_Space && ((GT_Worldgen_GT_Ore_Layer_Space) gt_worldgen).isEnabledForDim(finalDef))
                .collect(Collectors.toSet());

        space.forEach(
                element -> {
                    addDrop(new Pair<>((int) ((GT_Worldgen_GT_Ore_Layer_Space) element).mPrimaryMeta,false), (float) ((GT_Worldgen_GT_Ore_Layer_Space) element).mWeight);
                    addDrop(new Pair<>((int) ((GT_Worldgen_GT_Ore_Layer_Space) element).mSecondaryMeta,false), (float) ((GT_Worldgen_GT_Ore_Layer_Space) element).mWeight);
                    addDrop(new Pair<>((int) ((GT_Worldgen_GT_Ore_Layer_Space) element).mSporadicMeta,false), (((GT_Worldgen_GT_Ore_Layer_Space) element).mWeight / 8f));
                    addDrop(new Pair<>((int) ((GT_Worldgen_GT_Ore_Layer_Space) element).mBetweenMeta,false), (((GT_Worldgen_GT_Ore_Layer_Space) element).mWeight / 8f));
                }
        );
    }

    private void getDropsSmallOreSpace(ModDimensionDef finalDef) {
        Set space = GalacticGreg.smallOreWorldgenList.stream()
                .filter(gt_worldgen -> gt_worldgen.mEnabled && gt_worldgen instanceof GT_Worldgen_GT_Ore_SmallPieces_Space && ((GT_Worldgen_GT_Ore_SmallPieces_Space) gt_worldgen).isEnabledForDim(finalDef))
                .collect(Collectors.toSet());

        space.forEach(
                element ->
                        addDrop(new Pair<>((int) ((GT_Worldgen_GT_Ore_SmallPieces_Space) element).mMeta,false), (float) ((GT_Worldgen_GT_Ore_SmallPieces_Space) element).mAmount)
        );
    }

    private Pair<Integer,Boolean> getOreDamage() {
        float curentWeight = 0.f;
        while (true) {
            float randomnumber = XSTR.XSTR_INSTANCE.nextFloat() * totalWeight;
            for (Map.Entry<Pair<Integer,Boolean>, Float> entry : dropmap.entrySet()) {
                curentWeight += entry.getValue();
                if (randomnumber < curentWeight)
                    return entry.getKey();
            }
        }
    }

    private FluidStack getNobleGasInputAndSetMultiplier() {
        for (FluidStack s : this.getStoredFluids()) {
            for (int i = 0; i < NOBLE_GASSES.length; i++) {
                FluidStack ng = NOBLE_GASSES[i];
                if (ng.isFluidEqual(s)) {
                    multiplier = TIER_MULTIPLIER * (2 << (i == NOBLE_GASSES.length - 1 ? (i+2) : (i+1)));
                    return s;
                }
            }
        }
        return null;
    }

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

    private void handleFluidConsumption() {
        FluidStack storedNobleGas = getNobleGasInputAndSetMultiplier();
        if (storedNobleGas == null || !consumeNobleGas(storedNobleGas))
            multiplier = TIER_MULTIPLIER;
    }

    private void getDropMapRoss(int aID) {
        Consumer<BW_OreLayer> addToList = makeAddToList();
        BW_OreLayer.sList.stream()
                .filter(gt_worldgen -> gt_worldgen.mEnabled && gt_worldgen instanceof BW_OreLayer && gt_worldgen.isGenerationAllowed(null, aID, 0))
                .collect(Collectors.toSet())
                .forEach(addToList);
    }

    private void getDropMapBartworks(ModDimensionDef finalDef) {
        Consumer<BW_OreLayer> addToList = makeAddToList();
        addOresVeinsBartworks(finalDef, addToList);
        addSmallOresBartworks(finalDef);
    }

    private Consumer<BW_OreLayer> makeAddToList() {
        return element -> {
            List<Pair<Integer,Boolean>> data = element.getStacksRawData();
            for (int i = 0; i < data.size(); i++) {
                if (i < data.size()-2)
                    addDrop(data.get(i), (float) element.mWeight);
                else
                    addDrop(data.get(i), (element.mWeight/8f));
            }
        };
    }

    private ModDimensionDef makeModDimDef() {
        return getModContainers().stream()
                .flatMap(modContainer -> modContainer.getDimensionList().stream())
                .filter(modDimensionDef -> modDimensionDef.getChunkProviderName()
                        .equals(((ChunkProviderServer) this.getBaseMetaTileEntity().getWorld().getChunkProvider()).currentChunkProvider.getClass().getName()))
                .findFirst().orElse(null);
    }

    private void addOresVeinsBartworks(ModDimensionDef finalDef, Consumer<BW_OreLayer> addToList) {
        try {
            Set space = GalacticGreg.oreVeinWorldgenList.stream()
                    .filter(gt_worldgen -> gt_worldgen.mEnabled && gt_worldgen instanceof BW_Worldgen_Ore_Layer_Space && ((BW_Worldgen_Ore_Layer_Space) gt_worldgen).isEnabledForDim(finalDef))
                    .collect(Collectors.toSet());

            space.forEach(addToList);
        } catch (NullPointerException ignored) {}
    }

    private void addSmallOresBartworks(ModDimensionDef finalDef) {
        try {
            Set space = GalacticGreg.smallOreWorldgenList.stream()
                    .filter(gt_worldgen -> gt_worldgen.mEnabled && gt_worldgen instanceof BW_Worldgen_Ore_SmallOre_Space && ((BW_Worldgen_Ore_SmallOre_Space) gt_worldgen).isEnabledForDim(finalDef))
                    .collect(Collectors.toSet());

            space.forEach(
                    element ->
                            addDrop(new Pair<>(((BW_Worldgen_Ore_SmallOre_Space) element).mPrimaryMeta, ((BW_Worldgen_Ore_SmallOre_Space) element).bwOres != 0), (float) ((BW_Worldgen_Ore_SmallOre_Space) element).mDensity)
            );
        } catch (NullPointerException ignored) {}
    }

    private void handleExtraDrops(int id) {
        Optional.ofNullable(getExtraDropsDimMap().get(id)).ifPresent(e -> e.forEach(f -> addDrop(f.getKey(), f.getValue())));
    }

    private void calculateTotalWeight() {
        totalWeight = 0.0f;
        dropmap.values().forEach(f -> totalWeight += f);
    }

    private void handleModDimDef(int id) {
        if ((id <= 1 && id >= -1) || id == 7) {
            getDropMapVanilla();
        } else if (id == ConfigHandler.ross128BID || id == ConfigHandler.ross128BAID) {
            getDropMapRoss(id);
        } else {
            Optional.ofNullable(makeModDimDef()).ifPresent(def -> {
                getDropMapSpace(def);
                getDropMapBartworks(def);
            });
        }
    }

    private void calculateDropMap() {
        dropmap = new HashMap<>();
        int id = this.getBaseMetaTileEntity().getWorld().provider.dimensionId;
        handleModDimDef(id);
        handleExtraDrops(id);
        calculateTotalWeight();
    }

    private void makeDropMap() {
        if (dropmap == null || totalWeight == 0)
            calculateDropMap();
    }

    private void handleOutputs() {
        Pair<Integer, Boolean> stats = getOreDamage();
        final List<ItemStack> inputOres = this.getStoredInputs().stream().filter(GT_Utility::isOre).collect(Collectors.toList());
        final ItemStack output = new ItemStack(stats.getValue() ? WerkstoffLoader.BWOres : GregTech_API.sBlockOres1, multiplier, stats.getKey());
        if (inputOres.size() == 0
            || (mBlacklist && inputOres.stream().allMatch(is -> !GT_Utility.areStacksEqual(is, output)))
            || (!mBlacklist && inputOres.stream().anyMatch(is -> GT_Utility.areStacksEqual(is, output)))
        ) this.addOutput(output);
        this.updateSlots();
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        mBlacklist = !mBlacklist;
        GT_Utility.sendChatToPlayer(aPlayer, "Mode: " + (mBlacklist ? "Blacklist" : "Whitelist"));
    }
}

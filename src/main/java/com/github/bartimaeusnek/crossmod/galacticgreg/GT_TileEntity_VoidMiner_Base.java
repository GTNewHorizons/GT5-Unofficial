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
import com.github.bartimaeusnek.bartworks.system.oregen.BW_WorldGenRoss128b;
import com.github.bartimaeusnek.bartworks.system.oregen.BW_WorldGenRoss128ba;
import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import com.github.bartimaeusnek.bartworks.util.Pair;
import com.google.common.collect.ArrayListMultimap;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.GT_Worldgen_GT_Ore_Layer;
import gregtech.common.GT_Worldgen_GT_Ore_SmallPieces;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_DrillerBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.fluids.FluidStack;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static bloodasp.galacticgreg.registry.GalacticGregRegistry.getModContainers;

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
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        TIER_MULTIPLIER = aNBT.getByte("TIER_MULTIPLIER");
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
        handleFluidConsumption();
        handleOutputs();
        return true;
    }

    @Override
    public String[] getDescription() {
        String casingName = GT_LanguageManager.getTranslation(GT_LanguageManager.getTranslateableItemStackName(this.getCasingBlockItem().get(1L)));
        return new String[]{"Controller Block for the Void Miner "+ GT_Values.VN[this.getMinTier()],
                "Size(WxHxD): 3x7x3",
                "Controller (Front middle at bottom)",
                "3x1x3 Base of " + casingName,
                "1x3x1 " + casingName + " pillar (Center of base)",
                "1x3x1 " + this.getFrameMaterial().mName + " Frame Boxes (Each pillar side and on top)",
                "1x Output Bus (One of base casings)",
                "Optional: 0+ Input Hatch (One of base casings)",
                "1x Maintenance Hatch (One of base casings)",
                "1x " + GT_Values.VN[this.getMinTier()] + "+ Energy Hatch (Any bottom layer casing)",
                "Consumes " + GT_Values.V[this.getMinTier()] + "EU/t",
                "Can be supplied with 2L/s of Neon(x4), Krypton(x8), Xenon(x16) or Oganesson(x64)",
                "for higher outputs.",
                "Will output "+(2*TIER_MULTIPLIER)+" Ores per Second depending on the Dimension it is build in",
                StatCollector.translateToLocal("tooltip.bw.1.name") + ChatColorHelper.DARKGREEN + " BartWorks"
        };
    }

    public static ArrayListMultimap<Integer, Pair<Pair<Integer, Boolean>, Float>> getExtraDropsDimMap() {
        return extraDropsDimMap;
    }

    private Predicate<GT_Worldgen_GT_Ore_Layer> makeOreLayerPredicate() {
        switch (this.getBaseMetaTileEntity().getWorld().provider.dimensionId) {
            case -1:
                return gt_worldgen -> gt_worldgen.mNether;
            case 0:
                return gt_worldgen -> gt_worldgen.mOverworld;
            case 1:
                return gt_worldgen -> gt_worldgen.mEnd || gt_worldgen.mEndAsteroid;
            default:
                throw new IllegalStateException();
        }
    }
    
    private Predicate<GT_Worldgen_GT_Ore_SmallPieces> makeSmallOresPredicate() {
        switch (this.getBaseMetaTileEntity().getWorld().provider.dimensionId) {
            case -1:
                return gt_worldgen -> gt_worldgen.mNether;
            case 0:
                return gt_worldgen -> gt_worldgen.mOverworld;
            case 1:
                return gt_worldgen -> gt_worldgen.mEnd;
            default:
                throw new IllegalStateException();
        }
    }
    
    private void getDropsVanillaVeins(Predicate<GT_Worldgen_GT_Ore_Layer> oreLayerPredicate) {
        GT_Worldgen_GT_Ore_Layer.sList.stream().filter(gt_worldgen -> gt_worldgen.mEnabled && oreLayerPredicate.test(gt_worldgen)).forEach(element -> {
                    dropmap.put(new Pair<>((int) element.mPrimaryMeta,false), (float) element.mWeight);
                    dropmap.put(new Pair<>((int) element.mSecondaryMeta,false), (float) element.mWeight);
                    dropmap.put(new Pair<>((int) element.mSporadicMeta,false), (element.mWeight / 8f));
                    dropmap.put(new Pair<>((int) element.mBetweenMeta,false), (element.mWeight / 8f));
                }
        );
    }

    private void getDropsVanillaSmallOres(Predicate<GT_Worldgen_GT_Ore_SmallPieces> smallOresPredicate) {
        GT_Worldgen_GT_Ore_SmallPieces.sList.stream().filter(gt_worldgen -> gt_worldgen.mEnabled && smallOresPredicate.test(gt_worldgen)).forEach(element ->
                dropmap.put(new Pair<>((int) element.mMeta,false), (float) element.mAmount)
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
                    dropmap.put(new Pair<>((int) ((GT_Worldgen_GT_Ore_Layer_Space) element).mPrimaryMeta,false), (float) ((GT_Worldgen_GT_Ore_Layer_Space) element).mWeight);
                    dropmap.put(new Pair<>((int) ((GT_Worldgen_GT_Ore_Layer_Space) element).mSecondaryMeta,false), (float) ((GT_Worldgen_GT_Ore_Layer_Space) element).mWeight);
                    dropmap.put(new Pair<>((int) ((GT_Worldgen_GT_Ore_Layer_Space) element).mSporadicMeta,false), (((GT_Worldgen_GT_Ore_Layer_Space) element).mWeight / 8f));
                    dropmap.put(new Pair<>((int) ((GT_Worldgen_GT_Ore_Layer_Space) element).mBetweenMeta,false), (((GT_Worldgen_GT_Ore_Layer_Space) element).mWeight / 8f));
                }
        );
    }

    private void getDropsSmallOreSpace(ModDimensionDef finalDef) {
        Set space = GalacticGreg.smallOreWorldgenList.stream()
                .filter(gt_worldgen -> gt_worldgen.mEnabled && gt_worldgen instanceof GT_Worldgen_GT_Ore_SmallPieces_Space && ((GT_Worldgen_GT_Ore_SmallPieces_Space) gt_worldgen).isEnabledForDim(finalDef))
                .collect(Collectors.toSet());

        space.forEach(
                element ->
                        dropmap.put(new Pair<>((int) ((GT_Worldgen_GT_Ore_SmallPieces_Space) element).mMeta,false), (float) ((GT_Worldgen_GT_Ore_SmallPieces_Space) element).mAmount)
        );
    }

    private Pair<Integer,Boolean> getOreDamage() {
        int curentWeight = 0;
        while (true) {
            int randomeint = (Math.abs(XSTR.XSTR_INSTANCE.nextInt((int) Math.ceil(totalWeight))));
            for (Map.Entry<Pair<Integer,Boolean>, Float> entry : dropmap.entrySet()) {
                curentWeight += entry.getValue();
                if (randomeint < curentWeight)
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
    
    private void getDropMapBartworks(ModDimensionDef finalDef, int aID) {
        Consumer<BW_OreLayer> addToList = makeAddToList();
        if (aID == ConfigHandler.ross128BID)
            BW_WorldGenRoss128b.sList.forEach(addToList);
        else if (aID == ConfigHandler.ross128BAID)
            BW_WorldGenRoss128ba.sList.forEach(addToList);
        else {
            addOresVeinsBartworks(finalDef, addToList);
            addSmallOresBartworks(finalDef);
        }
    }

    private Consumer<BW_OreLayer> makeAddToList() {
        return element -> {
            List<Pair<Integer,Boolean>> data = element.getStacksRawData();
            for (int i = 0; i < data.size(); i++) {
                if (i < data.size()-1)
                    dropmap.put(data.get(i), (float) element.mWeight);
                else
                    dropmap.put(data.get(i), (element.mWeight/8f));
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
                            dropmap.put(new Pair<>(((BW_Worldgen_Ore_SmallOre_Space) element).mPrimaryMeta, ((BW_Worldgen_Ore_SmallOre_Space) element).bwOres != 0), (float) ((BW_Worldgen_Ore_SmallOre_Space) element).mDensity)
            );
        } catch (NullPointerException ignored) {}
    }
    
    private void handleExtraDrops(int id) {
        Optional.ofNullable(getExtraDropsDimMap().get(id)).ifPresent(e -> e.forEach(f -> dropmap.put(f.getKey(), f.getValue())));
    }

    private void calculateTotalWeight() {
        totalWeight = 0.0f;
        dropmap.values().forEach(f -> totalWeight += f);
    }

    private void handleDimBasedDrops(ModDimensionDef finalDef, int id) {
        if (id != ConfigHandler.ross128BID && id != ConfigHandler.ross128BAID)
            getDropMapSpace(finalDef);
    }

    private void handleModDimDef(int id) {
        if (id <= 1 && id >= -1)
            getDropMapVanilla();
        Optional.ofNullable(makeModDimDef()).ifPresent(def -> {
            handleDimBasedDrops(def, id);
            getDropMapBartworks(def, id);
        });
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
        Pair<Integer,Boolean> stats = getOreDamage();
        this.addOutput(new ItemStack(stats.getValue() ? WerkstoffLoader.BWOres : GregTech_API.sBlockOres1, multiplier, stats.getKey()));
        this.updateSlots();
    }
}

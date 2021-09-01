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

package com.github.bartimaeusnek.bartworks.common.tileentities.multis;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.github.bartimaeusnek.bartworks.common.items.SimpleSubItemClass;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.bartworks.util.MathUtils;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.objects.XSTR;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class GT_TileEntity_HTGR extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_TileEntity_HTGR> {

    private static final int BASECASINGINDEX = 44;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_TileEntity_HTGR> STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_HTGR>builder()
            .addShape(STRUCTURE_PIECE_MAIN, transpose(new String[][]{
                    {"  BBBBBBB  "," BBBBBBBBB ","BBBBBBBBBBB","BBBBBBBBBBB","BBBBBBBBBBB","BBBBBBBBBBB","BBBBBBBBBBB","BBBBBBBBBBB","BBBBBBBBBBB"," BBBBBBBBB ","  BBBBBBB  "},
                    {"  ccccccc  "," c-------c ","c---------c","c---------c","c---------c","c---------c","c---------c","c---------c","c---------c"," c-------c ","  ccccccc  "},
                    {"  ccccccc  "," c-------c ","c---------c","c---------c","c---------c","c---------c","c---------c","c---------c","c---------c"," c-------c ","  ccccccc  "},
                    {"  ccccccc  "," c-------c ","c---------c","c---------c","c---------c","c---------c","c---------c","c---------c","c---------c"," c-------c ","  ccccccc  "},
                    {"  ccccccc  "," c-------c ","c---------c","c---------c","c---------c","c---------c","c---------c","c---------c","c---------c"," c-------c ","  ccccccc  "},
                    {"  ccccccc  "," c-------c ","c---------c","c---------c","c---------c","c---------c","c---------c","c---------c","c---------c"," c-------c ","  ccccccc  "},
                    {"  ccccccc  "," c-------c ","c---------c","c---------c","c---------c","c---------c","c---------c","c---------c","c---------c"," c-------c ","  ccccccc  "},
                    {"  ccccccc  "," c-------c ","c---------c","c---------c","c---------c","c---------c","c---------c","c---------c","c---------c"," c-------c ","  ccccccc  "},
                    {"  ccccccc  "," c-------c ","c---------c","c---------c","c---------c","c---------c","c---------c","c---------c","c---------c"," c-------c ","  ccccccc  "},
                    {"  ccccccc  "," c-------c ","c---------c","c---------c","c---------c","c---------c","c---------c","c---------c","c---------c"," c-------c ","  ccccccc  "},
                    {"  ccccccc  "," c-------c ","c---------c","c---------c","c---------c","c---------c","c---------c","c---------c","c---------c"," c-------c ","  ccccccc  "},
                    {"  bbb~bbb  "," bbbbbbbbb ","bbbbbbbbbbb","bbbbbbbbbbb","bbbbbbbbbbb","bbbbbbbbbbb","bbbbbbbbbbb","bbbbbbbbbbb","bbbbbbbbbbb"," bbbbbbbbb ","  bbbbbbb  "},
            }))
            .addElement('c', ofBlock(GregTech_API.sBlockCasings3, 12))
            .addElement('b', ofChain(
                    ofHatchAdder(GT_TileEntity_HTGR::addOutputToMachineList, BASECASINGINDEX, 1),
                    ofHatchAdder(GT_TileEntity_HTGR::addMaintenanceToMachineList, BASECASINGINDEX, 1),
                    ofBlock(GregTech_API.sBlockCasings3, 12)
            ))
            .addElement('B', ofHatchAdderOptional(GT_TileEntity_HTGR::addInputToMachineList, BASECASINGINDEX, 2, GregTech_API.sBlockCasings3, 12))
            .build();

    private static final int HELIUM_NEEDED = 730000;
    private int HeliumSupply;
    private int fueltype = -1, fuelsupply = 0;
    private boolean empty;
    private int emptyticksnodiff = 0;
    private int coolanttaking = 0;

    public GT_TileEntity_HTGR(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    private GT_TileEntity_HTGR(String aName) {
        super(aName);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    @Override
    public IStructureDefinition<GT_TileEntity_HTGR> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Breeder Reactor")
                .addInfo("Controller block for the High Temperature Gas-cooled Reactor (HTGR)")
                .addInfo("Needs a constant supply of coolant while running")
                .addInfo("Needs at least 72k Fuel pebbles to start operation (can hold up to 720k pebbles)")
                .addInfo("Consumes up to 0.5% of total Fuel Pellets per Operation depending on efficiency")
                .addInfo("Efficiency is calculated exponentially depending on the amount of pebbles in the internal buffer")
                .addInfo("Reactor will take 4 000L/s of coolant multiplied by efficiency and by fuel coolant value (check tooltips)")
                .addInfo("One Operation takes 1 hour")
                .addSeparator()
                .beginStructureBlock(11, 12, 11, true)
                .addController("Front bottom center")
                .addCasingInfo("Radiation Proof Casings", 0)
                .addStructureInfo("Corners and the 2 touching blocks are air (cylindric)")
                .addInputBus("Any top layer casing", 2)
                .addInputHatch("Any top layer casing", 2)
                .addOutputBus("Any bottom layer casing", 1)
                .addOutputHatch("Any bottom layer casing", 1)
                .addMaintenanceHatch("Any bottom layer casing", 1)
                .toolTipFinisher("Bartworks");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece("main", stackSize, hintsOnly, 5, 11, 0);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack itemStack) {
        return (
            checkPiece("main", 5, 11, 0) &&
            this.mMaintenanceHatches.size() == 1 &&
            this.mInputHatches.size() > 0 &&
            this.mOutputHatches.size() > 0 &&
            this.mInputBusses.size() > 0 &&
            this.mOutputBusses.size() > 0
        );
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.HeliumSupply = aNBT.getInteger("HeliumSupply");
        this.fueltype = aNBT.getInteger("fueltype");
        this.fuelsupply = aNBT.getInteger("fuelsupply");
        this.empty = aNBT.getBoolean("EmptyMode");
        this.coolanttaking = aNBT.getInteger("coolanttaking");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("HeliumSupply", this.HeliumSupply);
        aNBT.setInteger("fueltype", this.fueltype);
        aNBT.setInteger("fuelsupply", this.fuelsupply);
        aNBT.setBoolean("EmptyMode", this.empty);
        aNBT.setInteger("coolanttaking", this.coolanttaking);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && !this.empty){
            if (this.HeliumSupply < GT_TileEntity_HTGR.HELIUM_NEEDED){
                for (FluidStack fluidStack : this.getStoredFluids()){
                    if (fluidStack.isFluidEqual(Materials.Helium.getGas(1))) {
                        int toget = Math.min(GT_TileEntity_HTGR.HELIUM_NEEDED - this.HeliumSupply, fluidStack.amount);
                        fluidStack.amount -= toget;
                        this.HeliumSupply += toget;
                        if(GT_TileEntity_HTGR.HELIUM_NEEDED == this.HeliumSupply && fluidStack.amount == 0)
                            fluidStack = null;
                    }
                }
            }
            if(this.fuelsupply < 720000){
                for (ItemStack itemStack : this.getStoredInputs()) {
                    int type = -1;
                    if(itemStack == null) continue;
                    if(itemStack.getItem() != HTGRMaterials.aHTGR_Materials) continue;
                    int damage = HTGRMaterials.aHTGR_Materials.getDamage(itemStack);
                    if(!((damage + 1) % HTGRMaterials.MATERIALS_PER_FUEL == HTGRMaterials.USABLE_FUEL_INDEX + 1)) continue; // is fuel
                    type = damage / HTGRMaterials.MATERIALS_PER_FUEL;
                    if(this.fueltype == -1)
                        this.fueltype = type;
                    if(this.fueltype != type)
                        continue;
                    int toget = Math.min(720000 - this.fuelsupply, itemStack.stackSize);
                    this.fuelsupply += toget;
                    itemStack.stackSize -= toget;
                }
                this.updateSlots();
            }
        }
    }

    @Override
    public boolean checkRecipe(ItemStack controllerStack) {
        
        if(this.empty)
        {
            this.mEfficiency = 10000;
            this.mMaxProgresstime = 100;
            return true;
        }
        if (!(this.HeliumSupply >= GT_TileEntity_HTGR.HELIUM_NEEDED && this.fuelsupply >= 72000))
            return false;

        double eff = Math.min(Math.pow((double)this.fuelsupply/72000D, 2D), 100D)/100D - ((double)(getIdealStatus() - getRepairStatus()) / 10D);

        if(eff <= 0)
            return false;

        int toReduce = MathUtils.floorInt((double)this.fuelsupply * 0.005D * eff);

        this.fuelsupply -= toReduce;
        int burnedballs = toReduce/64;
        if(burnedballs > 0)
            toReduce -= burnedballs*64;

        int meta = (this.fueltype * HTGRMaterials.MATERIALS_PER_FUEL) + HTGRMaterials.BURNED_OUT_FUEL_INDEX;

        this.mOutputItems = new ItemStack[] {
            new ItemStack(HTGRMaterials.aHTGR_Materials, burnedballs, meta),
            new ItemStack(HTGRMaterials.aHTGR_Materials, toReduce, meta + 1)
        };
        
        this.updateSlots();

        this.coolanttaking = (int)(4000D * (((this.fueltype * 0.5D) + 1)) * eff);

        this.mEfficiency = (int)(eff*10000D);
        this.mEUt=0;
        this.mMaxProgresstime=72000;
        return true;
    }
    
    private int runningtick = 0;

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        runningtick++;

        if (this.empty){
            if(emptyticksnodiff > 20 && emptyticksnodiff % 20 != 0){
                emptyticksnodiff++;
                return true;
            }
            if(this.HeliumSupply > 0){
                this.addOutput(Materials.Helium.getGas(this.HeliumSupply));
                this.HeliumSupply = 0;
            }
            if(this.fuelsupply > 0)
            {
                ItemStack iStack = new ItemStack(HTGRMaterials.aHTGR_Materials, this.fuelsupply, (HTGRMaterials.MATERIALS_PER_FUEL * this.fueltype) + HTGRMaterials.USABLE_FUEL_INDEX);
                boolean storedAll = false;
                for (GT_MetaTileEntity_Hatch_OutputBus tHatch : this.mOutputBusses) {
                    if(!isValidMetaTileEntity(tHatch))
                        continue;
                    if (tHatch.storeAll(iStack)){
                        storedAll = true;
                        break;
                    }
                }
                if(!storedAll){
                    if(this.fuelsupply == iStack.stackSize) emptyticksnodiff++;
                    else {this.fuelsupply = iStack.stackSize; emptyticksnodiff = 0;}
                }
                else{
                    this.fuelsupply = 0;
                    this.fueltype = -1;
                    this.coolanttaking = 0;
                }
            }
            return true;
        }

        if(runningtick % 20 == 0)
        {
            int takecoolant = coolanttaking;
            int drainedamount = 0;

            for(GT_MetaTileEntity_Hatch_Input tHatch : this.mInputHatches){
                if (isValidMetaTileEntity(tHatch)) {
                    FluidStack tLiquid = tHatch.getFluid();
                    if (tLiquid != null && tLiquid.isFluidEqual(FluidRegistry.getFluidStack("ic2coolant",1))){
                        FluidStack drained = tHatch.drain(takecoolant, true);
                        takecoolant -= drained.amount;
                        drainedamount += drained.amount;
                        if(takecoolant <= 0)
                            break;
                    }
                }
            }
        
            if(drainedamount > 0)
                addOutput(FluidRegistry.getFluidStack("ic2hotcoolant", drainedamount));

            this.updateSlots();

            if(takecoolant > 0)
                this.stopMachine();
        }
        
        return true;
    }

    

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack itemStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack itemStack) {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_TileEntity_HTGR(this.mName);
    }


    @Override
    public String[] getInfoData() {
        return new String[]{
                "Mode:", this.empty ? "Emptying" : "Normal",
                "Progress:", GT_Utility.formatNumbers(this.mProgresstime / 20) + "s / " + GT_Utility.formatNumbers(this.mMaxProgresstime / 20) + "s",
                "Fuel type:", (this.fueltype == -1 ? "NONE" : ("TRISO (" + HTGRMaterials.sHTGR_Fuel[this.fueltype].sEnglish) + ")"),
                "Fuel amount:", GT_Utility.formatNumbers(this.fuelsupply) + " pcs.",
                "Helium-Level:", GT_Utility.formatNumbers(this.HeliumSupply) + "L / " + GT_Utility.formatNumbers(GT_TileEntity_HTGR.HELIUM_NEEDED) + "L",
                "Coolant:", GT_Utility.formatNumbers(coolanttaking) + "L/s",
                "Problems:", String.valueOf(this.getIdealStatus() - this.getRepairStatus())
        };
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        return aSide == aFacing ? new ITexture[]{Textures.BlockIcons.getCasingTextureForId(GT_TileEntity_HTGR.BASECASINGINDEX), TextureFactory.of(aActive ? TextureFactory.of(TextureFactory.of(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE), TextureFactory.builder().addIcon(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE_GLOW).glow().build()) : TextureFactory.of(TextureFactory.of(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER), TextureFactory.builder().addIcon(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_GLOW).glow().build()))} : new ITexture[]{Textures.BlockIcons.getCasingTextureForId(GT_TileEntity_HTGR.BASECASINGINDEX)};
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if(this.mMaxProgresstime > 0)
        {
            GT_Utility.sendChatToPlayer(aPlayer, "HTGR mode cannot be changed while the machine is running.");
            return;
        }
        this.empty = !this.empty;
        GT_Utility.sendChatToPlayer(aPlayer, "HTGR is now running in " + (this.empty ? "emptying mode." : "normal Operation"));
    }

    
    
    public static class HTGRMaterials{

        private static class CustomHTGRSimpleSubItemClass extends SimpleSubItemClass{
            HashMap<Integer, String> tooltip = null;
            public CustomHTGRSimpleSubItemClass(HashMap<Integer, String> tooltip, String... tex){
                super(tex);
                this.tooltip = tooltip;
            }
            @Override
            @SuppressWarnings("unchecked")
            public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List aList, boolean p_77624_4_) {
                if(tooltip.containsKey(getDamage(p_77624_1_)))
                    aList.add(tooltip.get(getDamage(p_77624_1_)));
                aList.add("Material for High Temperature Gas-cooled Reactor");
                super.addInformation(p_77624_1_, p_77624_2_, aList, p_77624_4_);
            }
        }

        private static class Base_{
            public String sName;
            public String sEnglish;
            public String sTooltip;
            public Base_(String a, String b){
                this.sName = a;
                this.sEnglish = b;
                this.sTooltip = "";
            }
            public Base_(String a, String b, String c){
                this.sName = a;
                this.sEnglish = b;
                this.sTooltip = c;
            }
        }
        static class Fuel_{
            public String sName;
            public String sEnglish;
            public ItemStack mainItem;
            public ItemStack secondaryItem;
            public ItemStack[] recycledItems = { GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI };
            public FluidStack recycledFluid;
            public int[] recycleChances;
            public String tooltip;
            public Fuel_(String sName, String sEnglish, ItemStack mainItem, ItemStack secondaryItem, FluidStack recycledFluid, ItemStack[] recycledItems, int[] recycleChances, String tooltip){
                this.sName = sName;
                this.sEnglish = sEnglish;
                this.mainItem = mainItem;
                this.secondaryItem = secondaryItem;
                this.recycledFluid = recycledFluid;
                for(int i = 0; i < recycledItems.length; i++)
                    this.recycledItems[i] = recycledItems[i];
                this.recycleChances = recycleChances;
                this.tooltip = tooltip;
            }
        }
        private static class LangEntry_{
            public String sName;
            public String sEnglish;
            public LangEntry_(String a, String b){
                this.sName = a;
                this.sEnglish = b;
            }
        }
        
        static final Base_[] sHTGR_Bases = new Base_[]{ 
            new Base_("HTGRFuelMixture", "HTGR fuel mixture"),
            new Base_("BISOPebbleCompound", "BISO pebble compound"),
            new Base_("TRISOPebbleCompound", "TRISO pebble compound"),
            new Base_("TRISOBall", "TRISO ball"),
            new Base_("TRISOPebble", "TRISO pebble"),
            new Base_("BurnedOutTRISOBall", "Burned out TRISO Ball"),
            new Base_("BurnedOutTRISOPebble", "Burned out TRISO Pebble"),
        };
        static final int MATERIALS_PER_FUEL = sHTGR_Bases.length;
        static final int USABLE_FUEL_INDEX = 4;
        static final int BURNED_OUT_FUEL_INDEX = 5;
        static final Fuel_[] sHTGR_Fuel = new Fuel_[]{
            new Fuel_("Thorium", "Thorium", WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 64), Materials.Uranium235.getDust(4), 
                GT_Values.NF, new ItemStack[]{ 
                    Materials.Silicon.getDustSmall(1), Materials.Graphite.getDustSmall(1), Materials.Carbon.getDustSmall(1),
                    Materials.Lutetium.getDustSmall(1), WerkstoffLoader.Thorium232.get(OrePrefixes.dustSmall,1)},
                new int[]{9000, 9000, 9000, 9000, 1000}, "Multiplies coolant by 1"),
            new Fuel_("Uranium", "Uranium", Materials.Uranium.getDust(60), Materials.Uranium235.getDust(8), 
                FluidRegistry.getFluidStack("krypton", 7), new ItemStack[]{ 
                    Materials.Silicon.getDustSmall(1), Materials.Graphite.getDustSmall(1), Materials.Carbon.getDustSmall(1),
                    Materials.Lead.getDustSmall(1),
                    Materials.Uranium.getDustSmall(1)},
                new int[]{9000, 9000, 9000, 6562, 937}, "Multiplies coolant by 1.5"),
            new Fuel_("Plutonium", "Plutonium", Materials.Plutonium.getDust(64), Materials.Plutonium241.getDust(4), 
                FluidRegistry.getFluidStack("xenon", 8), new ItemStack[]{ 
                    Materials.Silicon.getDustSmall(1), Materials.Graphite.getDustSmall(1), Materials.Carbon.getDustSmall(1),
                    Materials.Lead.getDustSmall(1),
                    Materials.Plutonium.getDustSmall(1)},
                new int[]{9000, 9000, 9000, 7000, 1000}, "Multiplies coolant by 2"),
        };
        static final CustomHTGRSimpleSubItemClass aHTGR_Materials;
        static final ArrayList<LangEntry_> aHTGR_Localizations = new ArrayList<LangEntry_>();
        static{
            String[] sHTGR_Materials = new String[sHTGR_Bases.length*sHTGR_Fuel.length];
            HashMap<Integer, String> tooltip = new HashMap<Integer, String>();
            int i = 0;
            for(Fuel_ fuel : sHTGR_Fuel)
                for(Base_ base : sHTGR_Bases)
                {
                    sHTGR_Materials[i] = "HTGR" + base.sName + fuel.sName;
                    aHTGR_Localizations.add(new LangEntry_("item." + sHTGR_Materials[i] + ".name", base.sEnglish + " (" + fuel.sEnglish + ")"));
                    if(((i+1) % MATERIALS_PER_FUEL == (USABLE_FUEL_INDEX + 1)) && fuel.tooltip != null && fuel.tooltip != "")
                        tooltip.put(i, fuel.tooltip);
                    i++;
                }
            aHTGR_Materials = new CustomHTGRSimpleSubItemClass(tooltip, sHTGR_Materials);
        }
        

        public static void registeraTHR_Materials(){
            for(LangEntry_ iName : aHTGR_Localizations)
                GT_LanguageManager.addStringLocalization(iName.sName, iName.sEnglish);
            GameRegistry.registerItem(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials,"bw.HTGRMaterials");
        }

        public static void registerTHR_Recipes(){
            GT_Values.RA.addCentrifugeRecipe(
                    Materials.Thorium.getDust(1),GT_Values.NI,GT_Values.NF,GT_Values.NF,
                    Materials.Thorium.getDustSmall(2),Materials.Thorium.getDustSmall(1),
                    WerkstoffLoader.Thorium232.get(OrePrefixes.dustTiny,1),WerkstoffLoader.Thorium232.get(OrePrefixes.dustTiny,1),
                    WerkstoffLoader.Thorium232.get(OrePrefixes.dustTiny,1),Materials.Lutetium.getDustTiny(1),
                    new int[]{1600,1500,200,200,50,50},
                    10000, BW_Util.getMachineVoltageFromTier(4));
            GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                    GT_OreDictUnificator.get(OrePrefixes.plateDense,Materials.Lead,6),
                    GT_OreDictUnificator.get(OrePrefixes.frameGt,Materials.TungstenSteel,1),
                    GT_OreDictUnificator.get(OrePrefixes.screw,Materials.Europium,24)
                    },
                    Materials.Concrete.getMolten(1296),
                    new ItemStack(GregTech_API.sBlockCasings3,1,12),
                    40,
                    BW_Util.getMachineVoltageFromTier(5)
            );
            int i = 0;
            for(Fuel_ fuel : sHTGR_Fuel){
                GT_Values.RA.addMixerRecipe(fuel.mainItem, fuel.secondaryItem ,GT_Utility.getIntegratedCircuit(1),null,null,null,new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 1, i),400,30);
                GT_Values.RA.addFormingPressRecipe(new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 1, i), Materials.Carbon.getDust(64),new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 1, i + 1),40,30);
                GT_Values.RA.addFormingPressRecipe(new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 1, i + 1), Materials.Silicon.getDust(64),new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 1, i + 2),40,30);
                GT_Values.RA.addFormingPressRecipe(new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 1, i + 2), Materials.Graphite.getDust(64),new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 1, i + 3),40,30);
                ItemStack[] pellets = new ItemStack[4];
                Arrays.fill(pellets,new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 64, i + 4));
                GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.addRecipe(false, new ItemStack[]{new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 1, i + 3), GT_Utility.getIntegratedCircuit(17)}, pellets, null, null, null, null, 32000, 30, 0);
                GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.addRecipe(false, new ItemStack[]{new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 1, i + 5), GT_Utility.getIntegratedCircuit(17)}, new ItemStack[]{new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 64, i + 6)}, null, null, null, null,48000,30,0);
                GT_Values.RA.addCentrifugeRecipe(
                        new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 1, i + 6), GT_Values.NI, GT_Values.NF,
                        fuel.recycledFluid,
                        fuel.recycledItems[0],
                        fuel.recycledItems[1],
                        fuel.recycledItems[2],
                        fuel.recycledItems[3],
                        fuel.recycledItems[4],
                        fuel.recycledItems[5],
                        fuel.recycleChances,
                        1200, 30);
                i += sHTGR_Bases.length;
            }
        }

    }
}

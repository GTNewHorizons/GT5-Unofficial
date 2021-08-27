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
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.objects.XSTR;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;

public class GT_TileEntity_THTR extends GT_MetaTileEntity_MultiBlockBase {

    private static final int BASECASINGINDEX = 44;
    private static final int HELIUM_NEEDED = 730000;
    private int HeliumSupply;
    private int fueltype = -1, fuelsupply = 0;
    private boolean empty;
    private int emptyticksnodiff = 0;
    private int coolanttaking = 0;

    public GT_TileEntity_THTR(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    private GT_TileEntity_THTR(String aName) {
        super(aName);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.HeliumSupply = aNBT.getInteger("HeliumSupply");
        this.fueltype = aNBT.getInteger("fueltype");
        this.fuelsupply = aNBT.getInteger("fuelsupply");
        this.empty = aNBT.getBoolean("EmptyMode");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("HeliumSupply", this.HeliumSupply);
        aNBT.setInteger("fueltype", this.fueltype);
        aNBT.setInteger("fuelsupply", this.fuelsupply);
        aNBT.setBoolean("EmptyMode", this.empty);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && !this.empty){
            if (this.HeliumSupply < GT_TileEntity_THTR.HELIUM_NEEDED){
                for (FluidStack fluidStack : this.getStoredFluids()){
                    if (fluidStack.isFluidEqual(Materials.Helium.getGas(1))) {
                        int toget = Math.min(GT_TileEntity_THTR.HELIUM_NEEDED - this.HeliumSupply, fluidStack.amount);
                        fluidStack.amount -= toget;
                        this.HeliumSupply += toget;
                        if(GT_TileEntity_THTR.HELIUM_NEEDED == this.HeliumSupply && fluidStack.amount == 0)
                            fluidStack = null;
                    }
                }
            }
            for (ItemStack itemStack : this.getStoredInputs()) {
                int type = -1;
                if(GT_Utility.areStacksEqual(itemStack, new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 1, 1)))
                    type = 0; // BISO
                if(GT_Utility.areStacksEqual(itemStack, new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 1, 3)))
                    type = 1; // TRISO
                if(type == -1)
                    continue;
                if(this.fueltype == -1)
                    this.fueltype = type;
                if(this.fueltype != type)
                    continue;
                if(this.fuelsupply < 720000)
                {
                    int toget = Math.min(720000 - this.fuelsupply, itemStack.stackSize);
                    this.fuelsupply += toget;
                    itemStack.stackSize -= toget;
                    this.updateSlots();
                }
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
        if (!(this.HeliumSupply >= GT_TileEntity_THTR.HELIUM_NEEDED && this.fuelsupply >= 72000))
            return false;

        double eff = Math.min(Math.pow(((double)this.fuelsupply-72000D)/72000D, 2D)+19D, 100D)/100D - ((double)(getIdealStatus() - getRepairStatus()) / 10D);

        if(eff <= 0)
            return false;

        int toReduce = MathUtils.floorInt((double)this.fuelsupply * 0.005D * eff);

        this.fuelsupply -= toReduce;
        int burnedballs = toReduce/64;
        if(burnedballs > 0)
            toReduce -= burnedballs*64;
        
        int meta = this.fueltype == 1 ? 7 : 6;

        this.mOutputItems = new ItemStack[] {
            new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, burnedballs, meta),
            new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, toReduce, meta+2)
        };
        
        this.updateSlots();

        this.mEfficiency = (int)(eff*10000D);
        this.mEUt=0;
        this.mMaxProgresstime=72000/120;
        return true;
    }
    
    @Override
    public boolean onRunningTick(ItemStack aStack) {
        
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
                ItemStack iStack = new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, this.fuelsupply, this.fueltype == 1 ? 3 : 1);
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
                }
            }
            return true;
        }

        
        int takecoolant = (int)(4000D * (this.fueltype == 1 ? 2D : 1D) * ((double)this.mEfficiency / 10000D)); // 100 LHEs btw, why?
        coolanttaking = takecoolant;
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

        this.updateSlots();

        if(takecoolant > 0)
            this.stopMachine();
        
        addOutput(FluidRegistry.getFluidStack("ic2coolant", drainedamount));
        
        return true;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack itemStack) {
        byte xz = 5;
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * xz;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * xz;
        for (int x = -xz; x <= xz; x++) {
                for (int z = -xz; z <= xz; z++) {
                    for (int y = 0; y < 12; y++) {
                        if (y == 0 || y == 11) {
                            if (
                                    !((Math.abs(z) == xz-1 && Math.abs(x) == xz)) &&
                                    !((Math.abs(z) == xz && Math.abs(x) == xz-1)) &&
                                    !((Math.abs(x) == Math.abs(z) && Math.abs(x) == xz))
                            ) {
                                if (x + xDir == 0 && y == 0 && z + zDir == 0)
                                    continue;
                                if (!(aBaseMetaTileEntity.getBlockOffset(xDir + x, y, zDir + z) == GregTech_API.sBlockCasings3 && aBaseMetaTileEntity.getMetaIDOffset(xDir + x, y, zDir + z) == 12)) {
                                    if (
                                            (
                                                    !(this.addInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + x, y, zDir + z), GT_TileEntity_THTR.BASECASINGINDEX) && y == 11) &&
                                                    !(this.addOutputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + x, y, zDir + z), GT_TileEntity_THTR.BASECASINGINDEX) && y == 0)) &&
                                                    !this.addMaintenanceToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + x, y, zDir + z), GT_TileEntity_THTR.BASECASINGINDEX)
                                    ) {
                                        return false;
                                    }
                                }
                            }
                        }


                        //else if (!((Math.abs(x) == 4 && Math.abs(z) == 4) || (Math.abs(x) == 3 && Math.abs(z) == 3)) && !(Math.abs(x) < 3 || Math.abs(z) < 3) && !((Math.abs(x) == Math.abs(z) && Math.abs(x) == 3) || Math.abs(x) == 4 || Math.abs(z) == 4)) {
                        else if (!((Math.abs(z) == xz-1 && Math.abs(x) == xz)))
                                        if (!((Math.abs(z) == xz && Math.abs(x) == xz-1)))
                                            if (!((Math.abs(x) == Math.abs(z) && Math.abs(x) == xz)))
                                                if (!(Math.abs(x) < xz && Math.abs(z) != xz))

                        {
                            if (!(aBaseMetaTileEntity.getBlockOffset(xDir + x, y, zDir + z) == GregTech_API.sBlockCasings3 && aBaseMetaTileEntity.getMetaIDOffset(xDir + x, y, zDir + z) == 12)) {
                            if (
                                    !this.addMaintenanceToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + x, y, zDir + z), GT_TileEntity_THTR.BASECASINGINDEX))
                            {
                                return false;
                            }
                        }
                    }
                }
            }

        }

        return this.mMaintenanceHatches.size() == 1;
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
        return new GT_TileEntity_THTR(this.mName);
    }


    @Override
    public String[] getInfoData() {
        return new String[]{
                "Mode:", this.empty ? "Emptying" : "Normal",
                "Progress:", GT_Utility.formatNumbers(this.mProgresstime / 20) + "s / " + GT_Utility.formatNumbers(this.mMaxProgresstime / 20) + "s",
                "Fuel type:", (this.fueltype == -1 ? "NONE" : (this.fueltype == 1 ? "TRISO" : "BISO")),
                "Fuel amount:", GT_Utility.formatNumbers(this.fuelsupply) + " pcs.",
                "Helium-Level:", GT_Utility.formatNumbers(this.HeliumSupply) + "L / " + GT_Utility.formatNumbers(GT_TileEntity_THTR.HELIUM_NEEDED) + "L",
                "Coolant/t:", GT_Utility.formatNumbers(coolanttaking) + "L/t",
                "Problems:", String.valueOf(this.getIdealStatus() - this.getRepairStatus())
        };
    }

    @Override
    public String[] getDescription() {
        return BW_Tooltip_Reference.getTranslatedBrandedTooltip("tooltip.tile.htr.0.name");
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        return aSide == aFacing ? new ITexture[]{Textures.BlockIcons.getCasingTextureForId(GT_TileEntity_THTR.BASECASINGINDEX), TextureFactory.of(aActive ? TextureFactory.of(TextureFactory.of(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE), TextureFactory.builder().addIcon(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE_GLOW).glow().build()) : TextureFactory.of(TextureFactory.of(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER), TextureFactory.builder().addIcon(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_GLOW).glow().build()))} : new ITexture[]{Textures.BlockIcons.getCasingTextureForId(GT_TileEntity_THTR.BASECASINGINDEX)};
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if(this.mMaxProgresstime > 0)
        {
            GT_Utility.sendChatToPlayer(aPlayer, "THTR mode cant be changed when running.");
            return;
        }
        this.empty = !this.empty;
        GT_Utility.sendChatToPlayer(aPlayer, "THTR is now running in " + (this.empty ? "emptying mode." : "normal Operation"));
    }

    public static class THTRMaterials{
        static final SimpleSubItemClass aTHTR_Materials = new SimpleSubItemClass(
                "BISOPelletCompound",
                "BISOPellet",
                "TRISOPelletCompound",
                "TRISOPellet",
                "BISOPelletBall",
                "TRISOPelletBall",
                "BurnedOutBISOPelletBall", // 6
                "BurnedOutTRISOPelletBall", // 7
                "BurnedOutBISOPellet", // 8
                "BurnedOutTRISOPellet" // 9
        );
        public static void registeraTHR_Materials(){
            GameRegistry.registerItem(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials,"bw.THTRMaterials");
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
                    GT_OreDictUnificator.get(OrePrefixes.frameGt,Materials.TungstenSteel,1)
                    },
                    Materials.Concrete.getMolten(1296),
                    new ItemStack(GregTech_API.sBlockCasings3,1,12),
                    40,
                    BW_Util.getMachineVoltageFromTier(5)
            );
            GT_Values.RA.addMixerRecipe(WerkstoffLoader.Thorium232.get(OrePrefixes.dust,64),Materials.Uranium235.getDust(4),GT_Utility.getIntegratedCircuit(1),null,null,null,new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials),400,30);
            GT_Values.RA.addFormingPressRecipe(new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials),Materials.Graphite.getDust(64),new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials,1,4),40,30);
            ItemStack[] pellets = new ItemStack[4];
            Arrays.fill(pellets,new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials,64,1));
            GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.addRecipe(false,new ItemStack[]{new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials,1,4),GT_Utility.getIntegratedCircuit(17)}, pellets,null,null,null,null,16000,30,0);
            GT_Values.RA.addFormingPressRecipe(new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials,1,4),Materials.Silicon.getDust(64),new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials,1,2),40,30);
            GT_Values.RA.addFormingPressRecipe(new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials,1,2),Materials.Graphite.getDust(64),new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials,1,5),40,30);
            pellets = new ItemStack[4];
            Arrays.fill(pellets,new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials,64,3));
            GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.addRecipe(false,new ItemStack[]{new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials,1,5),GT_Utility.getIntegratedCircuit(17)}, pellets,null,null,null,null,32000,30,0);
            GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.addRecipe(false,new ItemStack[]{new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials,1,6),GT_Utility.getIntegratedCircuit(17)}, new ItemStack[]{new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials,64,8)},null,null,null,null,48000,30,0);
            GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.addRecipe(false,new ItemStack[]{new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials,1,7),GT_Utility.getIntegratedCircuit(17)}, new ItemStack[]{new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials,64,9)},null,null,null,null,48000,30,0);
            //old
            //GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.addRecipe(false,new ItemStack[]{new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials,1,8)},new ItemStack[]{Materials.Lutetium.getDustSmall(2)},null,null,null,null,1200,30,0);
            //GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.addRecipe(false,new ItemStack[]{new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials,1,9)},new ItemStack[]{Materials.Lutetium.getDustSmall(4)},null,null,null,null,1200,30,0);
            GT_Values.RA.addCentrifugeRecipe(
                    new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials,1,8),GT_Values.NI,GT_Values.NF,GT_Values.NF,
                    Materials.Graphite.getDustSmall(1),
                    Materials.Lutetium.getDustSmall(1), WerkstoffLoader.Thorium232.get(OrePrefixes.dustSmall,1),
                    null, null, null,
                    new int[]{9000, 3750, 6250},
                    1200, 30);
            GT_Values.RA.addCentrifugeRecipe(
                    new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials,1,9),GT_Values.NI,GT_Values.NF,GT_Values.NF,
                    Materials.Silicon.getDustSmall(1), Materials.Graphite.getDustSmall(2),
                    Materials.Lutetium.getDustSmall(1), WerkstoffLoader.Thorium232.get(OrePrefixes.dustSmall,1),
                    null, null,
                    new int[]{9000, 9000, 9000, 1000},
                    1200, 30);
        }

    }
}

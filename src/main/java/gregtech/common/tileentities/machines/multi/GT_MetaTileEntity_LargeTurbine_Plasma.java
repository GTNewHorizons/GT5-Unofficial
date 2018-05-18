package gregtech.common.tileentities.machines.multi;

import java.util.ArrayList;
import java.util.Collection;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class GT_MetaTileEntity_LargeTurbine_Plasma extends GT_MetaTileEntity_LargeTurbine {

    public GT_MetaTileEntity_LargeTurbine_Plasma(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_LargeTurbine_Plasma(String aName) {
        super(aName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[1][aColorIndex + 1], aFacing == aSide ? aActive ? new GT_RenderedTexture(Textures.BlockIcons.LARGETURBINE_TU_ACTIVE5) : new GT_RenderedTexture(Textures.BlockIcons.LARGETURBINE_TU5) : Textures.BlockIcons.CASING_BLOCKS[60]};
    }


    public String[] getDescription() {
        return new String[]{
                "Controller Block for the Large Plasma Generator",
                "Size(WxHxD): 3x3x4 (Hollow), Controller (Front centered)",
                "1x Plasma Input Hatch (Side centered)",
                "1x Maintenance Hatch (Side centered)",
                "1x Dynamo Hatch (Back centered)",
                "Tungstensteel Turbine Casings for the rest (24 at least!)",
                "Needs a Turbine Item (Inside controller GUI)"};
    }

    public int getFuelValue(FluidStack aLiquid) {
        if (aLiquid == null || GT_Recipe_Map.sTurbineFuels == null) return 0;
        FluidStack tLiquid;
        Collection<GT_Recipe> tRecipeList = GT_Recipe_Map.sPlasmaFuels.mRecipeList;
        if (tRecipeList != null) for (GT_Recipe tFuel : tRecipeList)
            if ((tLiquid = GT_Utility.getFluidForFilledItem(tFuel.getRepresentativeInput(0), true)) != null)
                if (aLiquid.isFluidEqual(tLiquid)) return tFuel.mSpecialValue;
        return 0;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_LargeTurbine_Plasma(mName);
    }

    @Override
    public Block getCasingBlock() {
        return GregTech_API.sBlockCasings4;
    }

    @Override
    public byte getCasingMeta() {
        return 12;
    }

    @Override
    public byte getCasingTextureIndex() {
        return 60;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return 0;
    }

    @Override
    int fluidIntoPower(ArrayList<FluidStack> aFluids, int aOptFlow, int aBaseEff) {
        if (aFluids.size() >= 1) {
            aOptFlow *= 800;//CHANGED THINGS HERE, check recipe runs once per 20 ticks
            int tEU = 0;

        int actualOptimalFlow = 0;

            FluidStack firstFuelType = new FluidStack(aFluids.get(0), 0); // Identify a SINGLE type of fluid to process.  Doesn't matter which one. Ignore the rest!
            int fuelValue = getFuelValue(firstFuelType);
            actualOptimalFlow = GT_Utility.safeInt((long)Math.ceil((double)aOptFlow / (double)fuelValue));
            this.realOptFlow = actualOptimalFlow; // For scanner info

            int remainingFlow = GT_Utility.safeInt((long)(actualOptimalFlow * 1.25f)); // Allowed to use up to 125% of optimal flow.  Variable required outside of loop for multi-hatch scenarios.
            int flow = 0;
            int totalFlow = 0;

            storedFluid=0;
            int aFluids_sS=aFluids.size();
            for (int i = 0; i < aFluids_sS; i++) {
                if (aFluids.get(i).isFluidEqual(firstFuelType)) {
                    flow = Math.min(aFluids.get(i).amount, remainingFlow); // try to use up w/o exceeding remainingFlow
                    depleteInput(new FluidStack(aFluids.get(i), flow)); // deplete that amount
                    this.storedFluid += aFluids.get(i).amount;
                    remainingFlow -= flow; // track amount we're allowed to continue depleting from hatches
                    totalFlow += flow; // track total input used
                }
            }
            String fn = FluidRegistry.getFluidName(firstFuelType);
            String[] nameSegments = fn.split("\\.",2);
            if (nameSegments.length==2){
                String outputName=nameSegments[1];
                FluidStack output = FluidRegistry.getFluidStack(outputName, totalFlow);
                if (output==null){
                    output = FluidRegistry.getFluidStack("molten."+outputName, totalFlow);
                }
                if (output!=null) {
                    addOutput(output);
                }
            }
            if(totalFlow<=0)return 0;
            tEU = GT_Utility.safeInt((long)((fuelValue / 20D) * (double)totalFlow));

            //System.out.println(totalFlow+" : "+fuelValue+" : "+aOptFlow+" : "+actualOptimalFlow+" : "+tEU);

            if (totalFlow != actualOptimalFlow) {
                double efficiency = 1.0D - Math.abs((totalFlow - actualOptimalFlow) / (float)actualOptimalFlow);
                //if(totalFlow>actualOptimalFlow){efficiency = 1.0f;}
                //if (efficiency < 0)
                //    efficiency = 0; // Can happen with really ludicrously poor inefficiency.
                tEU = (int)(tEU * efficiency);
                tEU = GT_Utility.safeInt((long)(aBaseEff/10000D*tEU));
            } else {
                tEU = GT_Utility.safeInt((long)(aBaseEff/10000D*tEU));
            }

            return tEU;

        }
        return 0;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        if((counter&7)==0 && (aStack==null || !(aStack.getItem() instanceof GT_MetaGenerated_Tool)  || aStack.getItemDamage() < 170 || aStack.getItemDamage() >179)) {
            stopMachine();
            return false;
        }
        ArrayList<FluidStack> tFluids = getStoredFluids();
        if (tFluids.size() > 0) {
            if (baseEff == 0 || optFlow == 0 || counter >= 512 || this.getBaseMetaTileEntity().hasWorkJustBeenEnabled()
                    || this.getBaseMetaTileEntity().hasInventoryBeenModified()) {
                counter = 0;
                baseEff = GT_Utility.safeInt((long)((5F + ((GT_MetaGenerated_Tool) aStack.getItem()).getToolCombatDamage(aStack)) * 1000F));
                optFlow = GT_Utility.safeInt((long)Math.max(Float.MIN_NORMAL,
                        ((GT_MetaGenerated_Tool) aStack.getItem()).getToolStats(aStack).getSpeedMultiplier()
                                * ((GT_MetaGenerated_Tool) aStack.getItem()).getPrimaryMaterial(aStack).mToolSpeed
                                * 50));
            } else {
                counter++;
            }
        }

        if(optFlow<=0 || baseEff<=0){
            stopMachine();//in case the turbine got removed
            return false;
        }

        int newPower = fluidIntoPower(tFluids, optFlow, baseEff);  // How much the turbine should be producing with this flow

        int difference = newPower - this.mEUt; // difference between current output and new output

        // Magic numbers: can always change by at least 10 eu/t, but otherwise by at most 1 percent of the difference in power level (per tick)
        // This is how much the turbine can actually change during this tick
        int maxChangeAllowed = Math.max(200, GT_Utility.safeInt((long)Math.abs(difference)/5));

        if (Math.abs(difference) > maxChangeAllowed) { // If this difference is too big, use the maximum allowed change
            int change = maxChangeAllowed * (difference > 0 ? 1 : -1); // Make the change positive or negative.
            this.mEUt += change; // Apply the change
        } else
            this.mEUt = newPower;

        if (this.mEUt <= 0) {
            //stopMachine();
            this.mEUt=0;
            this.mEfficiency=0;
            return false;
        } else {
            this.mMaxProgresstime = 20;
            this.mEfficiencyIncrease = 200;
            if(this.mDynamoHatches.size()>0){
                for(GT_MetaTileEntity_Hatch dynamo:mDynamoHatches)
                    if(isValidMetaTileEntity(dynamo) && dynamo.maxEUOutput() < mEUt)
                        explodeMultiblock();
            }
            return true;
        }
    }

   /* 
    * moved to super
    * 
    * @Override
    public String[] getInfoData() {
        String tRunning = mMaxProgresstime>0 ?
                EnumChatFormatting.GREEN+"Turbine running"+EnumChatFormatting.RESET :
                EnumChatFormatting.RED+"Turbine stopped"+EnumChatFormatting.RESET;
        String tMaintainance = getIdealStatus() == getRepairStatus() ?
                EnumChatFormatting.GREEN+"No Maintainance issues"+EnumChatFormatting.RESET :
                EnumChatFormatting.RED+"Needs Maintainance"+EnumChatFormatting.RESET ;
        int tDura = 0;

        if (mInventory[1] != null && mInventory[1].getItem() instanceof GT_MetaGenerated_Tool_01) {
            tDura = GT_Utility.safeInt((long)(100.0f / GT_MetaGenerated_Tool.getToolMaxDamage(mInventory[1]) * (GT_MetaGenerated_Tool.getToolDamage(mInventory[1]))+1));
        }

        long storedEnergy=0;
        long maxEnergy=0;
        for(GT_MetaTileEntity_Hatch_Dynamo tHatch : mDynamoHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                storedEnergy+=tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy+=tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }

        return new String[]{
                EnumChatFormatting.BLUE+"Large Turbine"+EnumChatFormatting.RESET,
                "Stored Energy:",
                EnumChatFormatting.GREEN + Long.toString(storedEnergy) + EnumChatFormatting.RESET +" EU / "+
                        EnumChatFormatting.YELLOW + Long.toString(maxEnergy) + EnumChatFormatting.RESET +" EU",
                tRunning,
                "Current Output: "+EnumChatFormatting.RED+mEUt+EnumChatFormatting.RESET+" EU/t",
                "Optimal Flow: "+EnumChatFormatting.YELLOW+GT_Utility.safeInt((long)realOptFlow)+EnumChatFormatting.RESET+" L/s",
                "Fuel Remaining: "+EnumChatFormatting.GOLD+storedFluid+EnumChatFormatting.RESET+"L",
                "Current Speed: "+EnumChatFormatting.YELLOW+(mEfficiency/100F)+EnumChatFormatting.RESET+"%",
                "Turbine Damage: "+EnumChatFormatting.RED+Integer.toString(tDura)+EnumChatFormatting.RESET+"%",
                tMaintainance,
        };
    }*/
}

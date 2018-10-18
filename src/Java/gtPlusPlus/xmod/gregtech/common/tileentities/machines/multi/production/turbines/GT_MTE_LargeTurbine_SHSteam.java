package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines;

import java.util.ArrayList;

import gregtech.GT_Mod;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class GT_MTE_LargeTurbine_SHSteam extends GregtechMetaTileEntity_LargerTurbineBase {

    public boolean achievement = false;
    private boolean looseFit=false;

    public GT_MTE_LargeTurbine_SHSteam(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MTE_LargeTurbine_SHSteam(String aName) {
        super(aName);
    }

    public String[] getDescription() {
		if (mCasingName.toLowerCase().contains(".name")) {
			mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings4Misc, 9);
		} 
        return new String[]{
                "Controller Block for the XL High Pressure Steam Turbine",
                "Size(WxHxD): 3x3x4 (Hollow), Controller (Front centered)",
                "1x Superheated Steam Input Hatch (Side centered)",
                "1x Maintenance Hatch (Side centered)",
                "1x Dynamo Hatch (Back centered)",
                "1x Output Hatch for Steam (Side centered)",
                mCasingName+"s for the rest (24 at least!)",
                "Needs a Turbine Item (Inside controller GUI)",
                "Output depending on Rotor and fitting",
                "Use screwdriver to adjust fitting of turbine"};
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MTE_LargeTurbine_SHSteam(mName);
    }

    @Override
    public Block getCasingBlock() {
		return ModBlocks.blockCasings4Misc;
    }

    @Override
    public byte getCasingMeta() {
        return 9;
    }

    @Override
    public byte getCasingTextureIndex() {
        return 59;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return 0;
    }

    @Override
    int fluidIntoPower(ArrayList<FluidStack> aFluids, int aOptFlow, int aBaseEff) {
        if(looseFit) {
            aOptFlow*=4;
            if(aBaseEff>10000){
                aOptFlow*=Math.pow(1.1f,((aBaseEff-7500)/10000F)*20f);
                aBaseEff=7500;
            }else if(aBaseEff>7500){
                aOptFlow*=Math.pow(1.1f,((aBaseEff-7500)/10000F)*20f);
                aBaseEff*=0.75f;
            }else{
                aBaseEff*=0.75f;
            }
        }
        int tEU = 0;
        int totalFlow = 0; // Byproducts are based on actual flow
        int flow = 0;
        int remainingFlow = GT_Utility.safeInt((long)(aOptFlow * 1.25f)); // Allowed to use up to 125% of optimal flow.  Variable required outside of loop for multi-hatch scenarios.
        this.realOptFlow = aOptFlow;

        storedFluid=0;
        for (int i = 0; i < aFluids.size() && remainingFlow > 0; i++) {
            String fluidName = aFluids.get(i).getFluid().getUnlocalizedName(aFluids.get(i));
            if (fluidName.equals("ic2.fluidSuperheatedSteam")) {
                flow = Math.min(aFluids.get(i).amount, remainingFlow); // try to use up w/o exceeding remainingFlow
                depleteInput(new FluidStack(aFluids.get(i), flow)); // deplete that amount
                this.storedFluid += aFluids.get(i).amount;
                remainingFlow -= flow; // track amount we're allowed to continue depleting from hatches
                totalFlow += flow; // track total input used
                if (!achievement) {
                    try {
                        GT_Mod.achievements.issueAchievement(this.getBaseMetaTileEntity().getWorld().getPlayerEntityByName(this.getBaseMetaTileEntity().getOwnerName()), "efficientsteam");
                    } catch (Exception e) {
                    }
                    achievement = true;
                }
            }else if(fluidName.equals("fluid.steam") || fluidName.equals("ic2.fluidSteam") || fluidName.equals("fluid.mfr.steam.still.name")){
                depleteInput(new FluidStack(aFluids.get(i), aFluids.get(i).amount));
            }
        }
        if(totalFlow<=0)return 0;
        tEU = totalFlow;
        addOutput(GT_ModHandler.getSteam(totalFlow));
        if (totalFlow != aOptFlow) {
            float efficiency = 1.0f - Math.abs((totalFlow - aOptFlow) / (float)aOptFlow);
            //if(totalFlow>aOptFlow){efficiency = 1.0f;}
            tEU *= efficiency;
            tEU = Math.max(1, GT_Utility.safeInt((long)tEU * (long)aBaseEff / 10000L));
        } else {
            tEU = GT_Utility.safeInt((long)tEU * (long)aBaseEff / 10000L);
        }

        return tEU;
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aSide == getBaseMetaTileEntity().getFrontFacing()) {
            looseFit^=true;
            GT_Utility.sendChatToPlayer(aPlayer, looseFit ? trans("500", "Fitting: Loose - More Flow") : trans("501", "Fitting: Tight - More Efficiency"));
        }
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return (looseFit && CORE.RANDOM.nextInt(4)==0)?0:1;
    }
    
    @Override
    public String[] getExtraInfoData() {
    	super.looseFit = looseFit;
    	return super.getInfoData();
    }
    
    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("turbineFitting",looseFit);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        looseFit=aNBT.getBoolean("turbineFitting");
    }

	@Override
	public boolean hasSlotInGUI() {
		return true;
	}

	@Override
	public String getCustomGUIResourceName() {
		return null;
	}

	@Override
	public String getMachineType() {
		return "Large Super-heated Steam Turbine";
	}

}

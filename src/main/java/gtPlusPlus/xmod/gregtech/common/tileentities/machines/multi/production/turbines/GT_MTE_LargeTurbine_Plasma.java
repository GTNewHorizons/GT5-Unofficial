package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines;

import java.util.ArrayList;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

@SuppressWarnings("deprecation")
public class GT_MTE_LargeTurbine_Plasma extends GregtechMetaTileEntity_LargerTurbineBase {

    public GT_MTE_LargeTurbine_Plasma(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }


    public GT_MTE_LargeTurbine_Plasma(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MTE_LargeTurbine_Plasma(mName);
    }

    @Override
    public int getCasingMeta() {
        return 4;
    }

    @Override
    public byte getCasingTextureIndex() {
        return 60;
    }

	@Override
	protected boolean requiresOutputHatch() {
		return true;
	}

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return 0;
    }
    
    public int getFuelValue(FluidStack aLiquid) {
        if (aLiquid == null) return 0;
        GT_Recipe tFuel = GT_Recipe_Map.sPlasmaFuels.findFuel(aLiquid);
        if (tFuel != null) return tFuel.mSpecialValue;
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
            actualOptimalFlow = GT_Utility.safeInt((long) Math.ceil((double) aOptFlow / (double) fuelValue));
            this.realOptFlow = actualOptimalFlow; // For scanner info

            int remainingFlow = GT_Utility.safeInt((long) (actualOptimalFlow * 1.25f)); // Allowed to use up to 125% of optimal flow.  Variable required outside of loop for multi-hatch scenarios.
            int flow = 0;
            int totalFlow = 0;

            storedFluid = 0;
            for (FluidStack aFluid : aFluids) {
                if (aFluid.isFluidEqual(firstFuelType)) {
                    flow = Math.min(aFluid.amount, remainingFlow); // try to use up w/o exceeding remainingFlow
                    depleteInput(new FluidStack(aFluid, flow)); // deplete that amount
                    this.storedFluid += aFluid.amount;
                    remainingFlow -= flow; // track amount we're allowed to continue depleting from hatches
                    totalFlow += flow; // track total input used
                }
            }
            String fn = FluidRegistry.getFluidName(firstFuelType);
            String[] nameSegments = fn.split("\\.", 2);
            if (nameSegments.length == 2) {
                String outputName = nameSegments[1];
                FluidStack output = FluidRegistry.getFluidStack(outputName, totalFlow);
                if (output == null) {
                    output = FluidRegistry.getFluidStack("molten." + outputName, totalFlow);
                }
                if (output != null) {
                    addOutput(output);
                }
            }
            if (totalFlow <= 0) return 0;
            tEU = GT_Utility.safeInt((long) ((fuelValue / 20D) * (double) totalFlow));

            //GT_FML_LOGGER.info(totalFlow+" : "+fuelValue+" : "+aOptFlow+" : "+actualOptimalFlow+" : "+tEU);

            if (totalFlow == actualOptimalFlow) {
                tEU = GT_Utility.safeInt((long) (aBaseEff / 10000D * tEU));
            } else {
                double efficiency = 1.0D - Math.abs((totalFlow - actualOptimalFlow) / (float) actualOptimalFlow);

                tEU = (int) (tEU * efficiency);
                tEU = GT_Utility.safeInt((long) (aBaseEff / 10000D * tEU));
            }

            return tEU;

        }
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 1;
    }

	@Override
	public String getCustomGUIResourceName() {
		return null;
	}

	@Override
	public String getMachineType() {
		return "Large Plasma Turbine";
	}


	@Override
	protected String getTurbineType() {
		return "Plasma";
	}

	@Override
	protected String getCasingName() {
		return "Reinforced Plasma Turbine Casing";
	}
	
	@Override
	protected ITexture getTextureFrontFace() {
		return new GT_RenderedTexture(gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_TU5);
	}

	@Override
	protected ITexture getTextureFrontFaceActive() {
		return new GT_RenderedTexture(gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_TU_ACTIVE5);
	}

}

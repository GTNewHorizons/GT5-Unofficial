package gregtech.common.tileentities.machines.multi;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

import static gregtech.api.enums.Textures.BlockIcons.*;

public class GT_MetaTileEntity_LargeTurbine_Gas extends GT_MetaTileEntity_LargeTurbine {

    public GT_MetaTileEntity_LargeTurbine_Gas(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, "gt.recipe.gasturbinefuel");
    }

    public GT_MetaTileEntity_LargeTurbine_Gas(String aName) {
        super(aName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        return new ITexture[]{MACHINE_CASINGS[1][aColorIndex + 1],
                aFacing == aSide ?
                        (aActive ? TextureFactory.builder().addIcon(LARGETURBINE_SS_ACTIVE5).extFacing().build() : hasTurbine() ? TextureFactory.builder().addIcon(LARGETURBINE_SS5).extFacing().build() : TextureFactory.builder().addIcon(LARGETURBINE_SS_EMPTY5).extFacing().build())
                        : casingTexturePages[0][58]};
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Gas Turbine")
                .addInfo("Controller block for the Large Gas Turbine")
                .addInfo("Needs a Turbine, place inside controller")
                .addPollutionAmount(getPollutionPerSecond(null))
                .addSeparator()
                .beginStructureBlock(3, 3, 4, true)
                .addController("Front center")
                .addCasingInfo("Stainless Steel Turbine Casing", 24)
                .addDynamoHatch("Back center", 1)
                .addMaintenanceHatch("Side centered", 2)
                .addMufflerHatch("Side centered", 2)
                .addInputHatch("Gas Fuel, Side centered", 2)
                .toolTipFinisher("Gregtech");
        return tt;
    }

    public int getFuelValue(FluidStack aLiquid) {
        if (aLiquid == null) return 0;
        GT_Recipe tFuel = GT_Recipe_Map.sTurbineFuels.findFuel(aLiquid);
        if (tFuel != null) return tFuel.mSpecialValue;
        return 0;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_LargeTurbine_Gas(mName);
    }

    @Override
    public Block getCasingBlock() {
        return GregTech_API.sBlockCasings4;
    }

    @Override
    public byte getCasingMeta() {
        return 10;
    }

    @Override
    public byte getCasingTextureIndex() {
        return 58;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return GT_Mod.gregtechproxy.mPollutionLargeGasTurbinePerSecond;
    }

    @Override
    int fluidIntoPower(ArrayList<FluidStack> aFluids, int aOptFlow, int aBaseEff) {
        if (aFluids.size() >= 1) {
            int tEU = 0;
            int actualOptimalFlow = 0;

            FluidStack firstFuelType = new FluidStack(aFluids.get(0), 0); // Identify a SINGLE type of fluid to process.  Doesn't matter which one. Ignore the rest!
            int fuelValue = getFuelValue(firstFuelType);

            if (aOptFlow < fuelValue) {
                // turbine too weak and/or fuel too powerful
                // at least consume 1L
                this.realOptFlow = 1;
                // wastes the extra fuel and generate aOptFlow directly
                depleteInput(new FluidStack(firstFuelType, 1));
                this.storedFluid += 1;
                return GT_Utility.safeInt((long) aOptFlow * (long) aBaseEff / 10000L);
            }

            actualOptimalFlow = GT_Utility.safeInt((long) aOptFlow / fuelValue);
            this.realOptFlow = actualOptimalFlow;

            int remainingFlow = GT_Utility.safeInt((long) (actualOptimalFlow * 1.25f)); // Allowed to use up to 125% of optimal flow.  Variable required outside of loop for multi-hatch scenarios.
            int flow = 0;
            int totalFlow = 0;

            storedFluid = 0;
            for (FluidStack aFluid : aFluids) {
                if (aFluid.isFluidEqual(firstFuelType)) {
                    flow = Math.min(aFluid.amount, remainingFlow); // try to use up to 125% of optimal flow w/o exceeding remainingFlow
                    depleteInput(new FluidStack(aFluid, flow)); // deplete that amount
                    this.storedFluid += aFluid.amount;
                    remainingFlow -= flow; // track amount we're allowed to continue depleting from hatches
                    totalFlow += flow; // track total input used
                }
            }
            if (totalFlow <= 0) return 0;
            tEU = GT_Utility.safeInt((long) totalFlow * fuelValue);

            if (totalFlow == actualOptimalFlow) {
                tEU = GT_Utility.safeInt((long) tEU * (long) aBaseEff / 10000L);
            } else {
                float efficiency = 1.0f - Math.abs((totalFlow - actualOptimalFlow) / (float) actualOptimalFlow);
                tEU *= efficiency;
                tEU = GT_Utility.safeInt((long) tEU * (long) aBaseEff / 10000L);
            }

            return tEU;

        }
        return 0;
    }


}

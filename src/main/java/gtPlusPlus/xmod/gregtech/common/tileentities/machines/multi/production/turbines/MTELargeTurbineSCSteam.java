package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTELargeTurbineSCSteam extends MTELargerTurbineBase {

    public MTELargeTurbineSCSteam(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELargeTurbineSCSteam(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeTurbineSCSteam(mName);
    }

    @Override
    public int getCasingMeta() {
        return 15;
    }

    @Override
    public int getCasingTextureIndex() {
        return 1538;
    }

    @Override
    protected boolean requiresOutputHatch() {
        return true;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return 0;
    }

    @Override
    public int getFuelValue(FluidStack aLiquid) {
        return 0;
    }

    @Override
    long fluidIntoPower(ArrayList<FluidStack> aFluids, long aOptFlow, int aBaseEff, float[] flowMultipliers) {
        int tEU = 0;
        int totalFlow = 0; // Byproducts are based on actual flow
        int flow = 0;
        // Variable required outside of loop for
        // multi-hatch scenarios.
        this.realOptFlow = aOptFlow;
        // this.realOptFlow = (double) aOptFlow * (double) flowMultipliers[0];
        // Will there be an multiplier for SC?
        int remainingFlow = MathUtils.safeInt((long) (realOptFlow * 1.25f)); // Allowed to use up to
        // 125% of optimal flow.

        storedFluid = 0;
        FluidStack tSCSteam = FluidRegistry.getFluidStack("supercriticalsteam", 1);
        for (int i = 0; i < aFluids.size() && remainingFlow > 0; i++) {
            if (GTUtility.areFluidsEqual(aFluids.get(i), tSCSteam, true)) {
                flow = Math.min(aFluids.get(i).amount, remainingFlow); // try to use up w/o exceeding remainingFlow
                depleteInput(new FluidStack(aFluids.get(i), flow)); // deplete that amount
                this.storedFluid += aFluids.get(i).amount;
                remainingFlow -= flow; // track amount we're allowed to continue depleting from hatches
                totalFlow += flow; // track total input used
            }
        }
        if (totalFlow <= 0) return 0;
        tEU = totalFlow;
        addOutput(GTModHandler.getSteam(totalFlow * 100));
        if (totalFlow != realOptFlow) {
            float efficiency = 1.0f - Math.abs((totalFlow - (float) realOptFlow) / (float) realOptFlow);
            // if(totalFlow>aOptFlow){efficiency = 1.0f;}
            tEU *= efficiency;
            tEU = Math.max(1, MathUtils.safeInt((long) tEU * (long) aBaseEff / 10000L));
        } else {
            tEU = MathUtils.safeInt((long) tEU * (long) aBaseEff / 10000L);
        }

        return tEU * 100L;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 8;
    }

    @Override
    public String getMachineType() {
        return "Large Supercritical Steam Turbine";
    }

    @Override
    protected String getTurbineType() {
        return "Supercritical Steam";
    }

    @Override
    protected String getCasingName() {
        return "Reinforced SC Turbine Casing";
    }

    @Override
    protected ITexture getTextureFrontFace() {
        return TextureFactory.of(TexturesGtBlock.Overlay_Machine_Controller_Advanced);
    }

    @Override
    protected ITexture getTextureFrontFaceActive() {
        return TextureFactory.of(TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active);
    }
}

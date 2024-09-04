package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTELargeTurbineSCSteam extends MTELargerTurbineBase {

    private boolean hasConsumedSteam;
    private boolean looseFit = false;
    private boolean isUsingDenseSteam;

    public GT_MTE_LargeTurbine_SCSteam(int aID, String aName, String aNameRegional) {
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
        if (looseFit & isUsingDenseSteam) {
            aOptFlow *= 4;
            final double flowMultiplier = Math.pow(1.1f, ((aBaseEff - 7500) / 10000F) * 10f);
            if (aBaseEff > 10000) {
                aOptFlow *= flowMultiplier;
            }
            aBaseEff *= 0.75f;
        }

        int tEU = 0;
        int totalFlow = 0; // Byproducts are based on actual flow
        int flow = 0;
        float denseFlow = 0;
        float steamFlowForNextSteam = 0;
        int steamInHatch = 0;
        // Variable required outside of loop for
        // multi-hatch scenarios.
        this.realOptFlow = aOptFlow;
        // this.realOptFlow = (double) aOptFlow * (double) flowMultipliers[0];
        // Will there be an multiplier for SC?
        int remainingFlow = MathUtils.safeInt((long) (realOptFlow * 1.25f)); // Allowed to use up to
        // 125% of optimal flow.
        float remainingDenseFlow = 0;

        storedFluid = 0;
        FluidStack tSCSteam = FluidRegistry.getFluidStack("supercriticalsteam", 1);
        for (int i = 0; i < aFluids.size() && remainingFlow > 0; i++) {
            String fluidName = aFluids.get(i)
                .getFluid()
                .getUnlocalizedName(aFluids.get(i));
            if (GTUtility.areFluidsEqual(aFluids.get(i), tSCSteam, true)) {
                if (!hasConsumedSteam) {
                    hasConsumedSteam = true;
                    isUsingDenseSteam = false;
                } else if (isUsingDenseSteam) {
                    continue;
                }
                flow = Math.min(aFluids.get(i).amount, remainingFlow); // try to use up w/o exceeding remainingFlow
                depleteInput(new FluidStack(aFluids.get(i), flow)); // deplete that amount
                this.storedFluid += aFluids.get(i).amount;
                remainingFlow -= flow; // track amount we're allowed to continue depleting from hatches
                totalFlow += flow; // track total input used
            } else if (fluidName.equals("fluid.densesupercriticalsteam")) {
                if (!hasConsumedSteam) {
                    hasConsumedSteam = true;
                    isUsingDenseSteam = true;
                } else if (!isUsingDenseSteam) {
                    continue;
                }
                steamInHatch = aFluids.get(i).amount;
                remainingDenseFlow = (float) remainingFlow / 1000; // Dense Steam is 1000x the EU value
                denseFlow = Math.min(steamInHatch, remainingDenseFlow); // try to use up w/o exceeding
                                                                        // remainingDenseFlow
                depleteInput(new FluidStack(aFluids.get(i), (int) denseFlow)); // deplete that amount
                this.storedFluid += aFluids.get(i).amount;
                remainingFlow -= denseFlow * 1000; // track amount we're allowed to continue depleting from hatches
                totalFlow += denseFlow * 1000; // track total input used
                steamFlowForNextSteam += denseFlow;
            }
        }
        if (totalFlow <= 0) return 0;
        tEU = totalFlow;
        if (isUsingDenseSteam) {
            addOutput(Materials.DenseSuperheatedSteam.getGas((long) steamFlowForNextSteam));
        } else {
            addOutput(GTModHandler.getSteam(totalFlow * 100));
        }
        if (totalFlow != realOptFlow) {
            float efficiency = 1.0f - Math.abs((totalFlow - (float) realOptFlow) / (float) realOptFlow);
            // if(totalFlow>aOptFlow){efficiency = 1.0f;}
            tEU *= efficiency;
            tEU = Math.max(1, MathUtils.safeInt((long) tEU * (long) aBaseEff / 10000L));
        } else {
            tEU = MathUtils.safeInt((long) tEU * (long) aBaseEff / 10000L);
        }
        if (isUsingDenseSteam) {
            return tEU;
        }
        return tEU * 100L;
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        // Using a screwdriver to change modes should allow for any combination of Slow/Fast and Tight/Loose Mode
        // Whenever there's a mode switch, there will be two messages on the player chat
        // The two messages specify which two modes the turbine is on after the change
        // (Tight/Loose changes on every action, Slow/Fast changes every other action, all pairs are cycled this way)
        if (side == getBaseMetaTileEntity().getFrontFacing()) {
            looseFit ^= true;
            GT_Utility.sendChatToPlayer(
                aPlayer,
                looseFit ? "Fitting is Loose (Higher Flow)" : "Fitting is Tight (Higher Efficiency)");
        }

        if (looseFit) {
            super.onModeChangeByScrewdriver(side, aPlayer, aX, aY, aZ);
        } else if (mFastMode) {
            PlayerUtils.messagePlayer(aPlayer, "Running in Fast (48x) Mode.");
        } else {
            PlayerUtils.messagePlayer(aPlayer, "Running in Slow (16x) Mode.");
        }
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return (looseFit && GTPPCore.RANDOM.nextInt(4) == 0) ? 0 : 1;
    }

    @Override
    public boolean isLooseMode() {
        return looseFit;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("turbineFitting", looseFit);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        looseFit = aNBT.getBoolean("turbineFitting");
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

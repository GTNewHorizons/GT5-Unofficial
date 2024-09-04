package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines;

import static gtPlusPlus.core.lib.GTPPCore.RANDOM;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import gregtech.GTMod;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GTRenderedTexture;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.TurbineStatCalculator;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

@SuppressWarnings("deprecation")
public class MTELargeTurbineSteam extends MTELargerTurbineBase {

    private float water;
    private boolean achievement = false;
    private boolean looseFit = false;
    private boolean isUsingDenseSteam;

    public MTELargeTurbineSteam(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELargeTurbineSteam(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeTurbineSteam(mName);
    }

    @Override
    public int getCasingMeta() {
        return 1;
    }

    @Override
    public int getCasingTextureIndex() {
        return 16;
    }

    @Override
    protected boolean requiresOutputHatch() {
        return true;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return 0;
    }

    private int useWater(float input) {
        water = water + input;
        int usage = (int) water;
        water = water - usage;
        return usage;
    }

    @Override
    public int getFuelValue(FluidStack aLiquid) {
        return 0;
    }

    @Override
    long fluidIntoPower(ArrayList<FluidStack> aFluids, TurbineStatCalculator turbine) {

        long tEU = 0;
        int totalFlow = 0; // Byproducts are based on actual flow
        int flow = 0;
        float denseFlow = 0;
        float steamFlowForWater = 0;
        int steamInHatch = 0;

        // Variable required outside of loop for
        // multi-hatch scenarios.
        this.realOptFlow = looseFit ? turbine.getOptimalLooseSteamFlow() : turbine.getOptimalSteamFlow();

        int remainingFlow = MathUtils.safeInt((long) (realOptFlow * 1.25f)); // Allowed to
        // use up to
        // 125% of
        // optimal flow.
        float remainingDenseFlow = 0;

        boolean hasConsumedSteam = false;

        storedFluid = 0;
        for (int i = 0; i < aFluids.size() && remainingFlow > 0; i++) { // loop through each hatch; extract inputs and
                                                                        // track totals.
            String fluidName = aFluids.get(i)
                .getFluid()
                .getUnlocalizedName(aFluids.get(i));
            switch (fluidName) {
                case "fluid.steam", "ic2.fluidSteam", "fluid.mfr.steam.still.name" -> {
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
                    if (!achievement) {
                        GTMod.achievements.issueAchievement(
                            this.getBaseMetaTileEntity()
                                .getWorld()
                                .getPlayerEntityByName(
                                    this.getBaseMetaTileEntity()
                                        .getOwnerName()),
                            "muchsteam");
                        achievement = true;
                    }
                }
                case "fluid.densesteam" -> {
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
                    steamFlowForWater += denseFlow * 1000;
                }
                case "ic2.fluidSuperheatedSteam" -> depleteInput(new FluidStack(aFluids.get(i), aFluids.get(i).amount));
            }
        }
        if (totalFlow <= 0) return 0;
        tEU = totalFlow;
        int waterToOutput;
        if (isUsingDenseSteam) {
            // Water return is lower to counteract water generation from rounding errors
            waterToOutput = useWater(steamFlowForWater / 160.1f);
        } else {
            waterToOutput = useWater(totalFlow / 160.0f);
        }
        addOutput(GTModHandler.getDistilledWater(waterToOutput));
        if (totalFlow != realOptFlow) {
            float efficiency = 1.0f - Math.abs((totalFlow - (float) realOptFlow) / (float) realOptFlow);
            // if(totalFlow>aOptFlow){efficiency = 1.0f;}
            tEU *= efficiency;
            tEU = Math.max(1, MathUtils.safeInt((long) (tEU * (looseFit ? turbine.getLooseSteamEfficiency() : turbine.getSteamEfficiency()))));
        } else {
            tEU = MathUtils.safeInt((long) (tEU * (looseFit ? turbine.getLooseSteamEfficiency() : turbine.getSteamEfficiency())));
        }


        return tEU;
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        // Using a screwdriver to change modes should allow for any combination of Slow/Fast and Tight/Loose Mode
        // Whenever there's a mode switch, there will be two messages on the player chat
        // The two messages specify which two modes the turbine is on after the change
        // (Tight/Loose changes on every action, Slow/Fast changes every other action, all pairs are cycled this way)
        if (side == getBaseMetaTileEntity().getFrontFacing()) {
            looseFit ^= true;
            GTUtility.sendChatToPlayer(
                aPlayer,
                looseFit ? "Fitting: Loose - More Flow" : "Fitting: Tight - More Efficiency");
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
        return (looseFit && RANDOM.nextInt(4) == 0) ? 0 : 1;
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
        return "Large Steam Turbine";
    }

    @Override
    protected String getTurbineType() {
        return "Steam";
    }

    @Override
    protected String getCasingName() {
        return "Reinforced Steam Turbine Casing";
    }

    @Override
    protected ITexture getTextureFrontFace() {
        return new GTRenderedTexture(TexturesGtBlock.Overlay_Machine_Controller_Advanced);
    }

    @Override
    protected ITexture getTextureFrontFaceActive() {
        return new GTRenderedTexture(TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active);
    }
}

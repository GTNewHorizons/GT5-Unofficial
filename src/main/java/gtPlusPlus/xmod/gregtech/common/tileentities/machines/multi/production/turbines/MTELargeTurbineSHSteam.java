package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines;

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
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

@SuppressWarnings("deprecation")
public class MTELargeTurbineSHSteam extends MTELargerTurbineBase {

    public boolean achievement = false;
    private boolean looseFit = false;

    public MTELargeTurbineSHSteam(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELargeTurbineSHSteam(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeTurbineSHSteam(mName);
    }

    @Override
    public int getCasingMeta() {
        return 2;
    }

    @Override
    public int getCasingTextureIndex() {
        return 59;
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
        if (looseFit) {
            aOptFlow *= 4;
            if (aBaseEff > 10000) {
                aOptFlow *= Math.pow(1.1f, ((aBaseEff - 7500) / 10000F) * 20f);
                aBaseEff = 7500;
            } else if (aBaseEff > 7500) {
                aOptFlow *= Math.pow(1.1f, ((aBaseEff - 7500) / 10000F) * 20f);
                aBaseEff *= 0.75f;
            } else {
                aBaseEff *= 0.75f;
            }
        }
        // prevent overflow like that in SC Steam
        long tEU = 0;
        int totalFlow = 0; // Byproducts are based on actual flow
        int flow = 0;

        // Variable required outside of loop for
        // multi-hatch scenarios.
        this.realOptFlow = aOptFlow * flowMultipliers[0];

        int remainingFlow = MathUtils.safeInt((long) (realOptFlow * 1.25f)); // Allowed to use up to
        // 125% of optimal flow.

        storedFluid = 0;
        for (int i = 0; i < aFluids.size() && remainingFlow > 0; i++) {
            String fluidName = aFluids.get(i)
                .getFluid()
                .getUnlocalizedName(aFluids.get(i));
            if (fluidName.equals("ic2.fluidSuperheatedSteam")) {
                flow = Math.min(aFluids.get(i).amount, remainingFlow); // try to use up w/o exceeding remainingFlow
                depleteInput(new FluidStack(aFluids.get(i), flow)); // deplete that amount
                this.storedFluid += aFluids.get(i).amount;
                remainingFlow -= flow; // track amount we're allowed to continue depleting from hatches
                totalFlow += flow; // track total input used
                if (!achievement) {
                    try {
                        GTMod.achievements.issueAchievement(
                            this.getBaseMetaTileEntity()
                                .getWorld()
                                .getPlayerEntityByName(
                                    this.getBaseMetaTileEntity()
                                        .getOwnerName()),
                            "efficientsteam");
                    } catch (Exception e) {}
                    achievement = true;
                }
            } else if (fluidName.equals("fluid.steam") || fluidName.equals("ic2.fluidSteam")
                || fluidName.equals("fluid.mfr.steam.still.name")) {
                    depleteInput(new FluidStack(aFluids.get(i), aFluids.get(i).amount));
                }
        }
        if (totalFlow <= 0) return 0;
        tEU = totalFlow;
        addOutput(GTModHandler.getSteam(totalFlow));
        if (totalFlow != realOptFlow) {
            float efficiency = 1.0f - Math.abs((totalFlow - (float) realOptFlow) / (float) realOptFlow);
            // if(totalFlow>aOptFlow){efficiency = 1.0f;}
            tEU *= efficiency;
            tEU = Math.max(1L, tEU * aBaseEff / 10000L);
        } else {
            tEU = tEU * aBaseEff / 10000L;
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
        return "Large Super-heated Steam Turbine";
    }

    @Override
    protected String getTurbineType() {
        return "Super-heated Steam";
    }

    @Override
    protected String getCasingName() {
        return "Reinforced HP Steam Turbine Casing";
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

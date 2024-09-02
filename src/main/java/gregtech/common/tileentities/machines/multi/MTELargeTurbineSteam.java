package gregtech.common.tileentities.machines.multi;

import static gregtech.api.enums.GTValues.STEAM_PER_WATER;
import static gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_NEW5;
import static gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_NEW_ACTIVE5;
import static gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_NEW_EMPTY5;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTELargeTurbineSteam extends MTELargeTurbine {

    private int excessWater;
    private boolean achievement = false;
    private boolean looseFit = false;

    public MTELargeTurbineSteam(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELargeTurbineSteam(String aName) {
        super(aName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return new ITexture[] { MACHINE_CASINGS[1][colorIndex + 1],
            aFacing == side ? (aActive ? TextureFactory.builder()
                .addIcon(LARGETURBINE_NEW_ACTIVE5)
                .build()
                : hasTurbine() ? TextureFactory.builder()
                    .addIcon(LARGETURBINE_NEW5)
                    .build()
                    : TextureFactory.builder()
                        .addIcon(LARGETURBINE_NEW_EMPTY5)
                        .build())
                : casingTexturePages[0][57] };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Steam Turbine")
            .addInfo("Controller block for the Large Steam Turbine")
            .addInfo("Needs a Turbine, place inside controller")
            .addInfo("Outputs Distilled Water as well as producing power")
            .addInfo("Power output depends on turbine and fitting")
            .addInfo("Use screwdriver to adjust fitting of turbine")
            .addSeparator()
            .beginStructureBlock(3, 3, 4, true)
            .addController("Front center")
            .addCasingInfoRange("Turbine Casing", 8, 31, false)
            .addDynamoHatch("Back center", 1)
            .addMaintenanceHatch("Side centered", 2)
            .addInputHatch("Steam, Side centered", 2)
            .addOutputHatch("Distilled Water, Side centered", 2)
            .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeTurbineSteam(mName);
    }

    @Override
    public Block getCasingBlock() {
        return GregTechAPI.sBlockCasings4;
    }

    @Override
    public byte getCasingMeta() {
        return 9;
    }

    @Override
    public int getCasingTextureIndex() {
        return 16;
    }

    @Override
    public boolean isNewStyleRendering() {
        return true;
    }

    private int condenseSteam(int steam) {
        excessWater += steam;
        int water = excessWater / STEAM_PER_WATER;
        excessWater %= STEAM_PER_WATER;
        return water;
    }

    @Override
    int fluidIntoPower(ArrayList<FluidStack> aFluids, int aOptFlow, int aBaseEff, int overflowEfficiency,
        float[] flowMultipliers) {
        if (looseFit) {
            float[] calculatedFlow = calculateLooseFlow(aOptFlow, aBaseEff);
            aOptFlow = GTUtility.safeInt((long) calculatedFlow[0]);
            aBaseEff = GTUtility.safeInt((long) calculatedFlow[1]);
        }
        int tEU = 0;
        int totalFlow = 0; // Byproducts are based on actual flow
        int flow = 0;

        // Allowed to use up to 250% optimal flow rate, depending on the value of overflowMultiplier.
        // This value is chosen because the highest EU/t possible depends on the overflowMultiplier, and the formula
        // used
        // makes it so the flow rate for that max, per value of overflowMultiplier, is (percentage of optimal flow
        // rate):
        // - 150% if it is 1
        // - 200% if it is 2
        // - 250% if it is 3
        // Variable required outside of loop for multi-hatch scenarios.
        this.realOptFlow = aOptFlow * flowMultipliers[0];
        int remainingFlow = GTUtility.safeInt((long) (realOptFlow * (0.5f * overflowMultiplier + 1)));

        storedFluid = 0;
        for (int i = 0; i < aFluids.size() && remainingFlow > 0; i++) { // loop through each hatch; extract inputs and
                                                                        // track totals.
            final FluidStack aFluidStack = aFluids.get(i);
            if (GTModHandler.isAnySteam(aFluidStack)) {
                flow = Math.min(aFluidStack.amount, remainingFlow); // try to use up to the max flow defined just above
                depleteInput(new FluidStack(aFluidStack, flow)); // deplete that amount
                this.storedFluid += aFluidStack.amount;
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
            } else if (GTModHandler.isSuperHeatedSteam(aFluidStack)) {
                depleteInput(new FluidStack(aFluidStack, aFluidStack.amount));
            }
        }
        if (totalFlow <= 0) return 0;
        tEU = totalFlow;
        int waterToOutput = condenseSteam(totalFlow);
        addOutput(GTModHandler.getDistilledWater(waterToOutput));
        if (totalFlow == (GTUtility.safeInt((long) realOptFlow))) {
            tEU = GTUtility.safeInt((long) tEU * (long) aBaseEff / 20000L);
        } else {
            float efficiency = getOverflowEfficiency(
                totalFlow,
                (GTUtility.safeInt((long) realOptFlow)),
                overflowMultiplier);
            tEU *= efficiency;
            tEU = Math.max(1, GTUtility.safeInt((long) tEU * (long) aBaseEff / 20000L));
        }

        // If next output is above the maximum the dynamo can handle, set it to the maximum instead of exploding the
        // turbine
        // Raising the maximum allowed flow rate to account for the efficiency changes beyond the optimal flow rate can
        // explode turbines on world load
        if (tEU > getMaximumOutput()) {
            tEU = GTUtility.safeInt(getMaximumOutput());
        }

        return tEU;
    }

    @Override
    float getOverflowEfficiency(int totalFlow, int actualOptimalFlow, int overflowMultiplier) {
        // overflowMultiplier changes how quickly the turbine loses efficiency after flow goes beyond the optimal value
        // At the default value of 1, any flow will generate less EU/t than optimal flow, regardless of the amount of
        // fuel used
        // The bigger this number is, the slower efficiency loss happens as flow moves beyond the optimal value
        // Steam is the least efficient out of all turbine fuels in this regard
        float efficiency = 0;

        if (totalFlow > actualOptimalFlow) {
            efficiency = 1.0f
                - Math.abs((totalFlow - actualOptimalFlow)) / ((float) actualOptimalFlow * (overflowMultiplier + 1));
        } else {
            efficiency = 1.0f - Math.abs((totalFlow - actualOptimalFlow) / (float) actualOptimalFlow);
        }

        return efficiency;
    }

    public static float[] calculateLooseFlow(float aOptFlow, float aBaseEff) {
        aOptFlow *= 4f;
        if (aBaseEff >= 26000f) {
            aOptFlow = aOptFlow * (float) Math.pow(1.1f, ((aBaseEff - 8000f) / 10000f) * 20f);
            aBaseEff = aBaseEff * 0.6f;
        } else if (aBaseEff > 22000f) {
            aOptFlow = aOptFlow * (float) Math.pow(1.1f, ((aBaseEff - 7000f) / 10000f) * 20f);
            aBaseEff = aBaseEff * 0.65f;
        } else if (aBaseEff > 18000f) {
            aOptFlow = aOptFlow * (float) Math.pow(1.1f, ((aBaseEff - 6000f) / 10000f) * 20f);
            aBaseEff = aBaseEff * 0.70f;
        } else if (aBaseEff > 14000f) {
            aOptFlow = aOptFlow * (float) Math.pow(1.1f, ((aBaseEff - 5000f) / 10000f) * 20f);
            aBaseEff = aBaseEff * 0.75f;
        } else if (aBaseEff > 10000f) {
            aOptFlow = aOptFlow * (float) Math.pow(1.1f, ((aBaseEff - 4000f) / 10000f) * 20f);
            aBaseEff = aBaseEff * 0.8f;
        } else if (aBaseEff > 6000f) {
            aOptFlow = aOptFlow * (float) Math.pow(1.1f, ((aBaseEff - 3000f) / 10000f) * 20f);
            aBaseEff = aBaseEff * 0.85f;
        } else {
            aBaseEff = aBaseEff * 0.9f;
        }

        if (aBaseEff % 100 != 0) {
            aBaseEff -= aBaseEff % 100;
        }

        float[] looseFlow = new float[2];
        looseFlow[0] = aOptFlow;
        looseFlow[1] = aBaseEff;
        return looseFlow;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (side == getBaseMetaTileEntity().getFrontFacing()) {
            looseFit ^= true;
            GTUtility.sendChatToPlayer(
                aPlayer,
                looseFit ? GTUtility.trans("500", "Fitting: Loose - More Flow")
                    : GTUtility.trans("501", "Fitting: Tight - More Efficiency"));
        }
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return (looseFit && XSTR_INSTANCE.nextInt(4) == 0) ? 0 : 1;
    }

    @Override
    public String[] getInfoData() {
        super.looseFit = looseFit;
        return super.getInfoData();
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
}

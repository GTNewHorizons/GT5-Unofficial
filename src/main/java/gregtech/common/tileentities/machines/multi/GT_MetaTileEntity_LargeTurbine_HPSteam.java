package gregtech.common.tileentities.machines.multi;

import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;
import static gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeTurbine_Steam.calculateLooseFlow;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_LargeTurbine_HPSteam extends GT_MetaTileEntity_LargeTurbine {

    public boolean achievement = false;
    private boolean looseFit = false;

    public GT_MetaTileEntity_LargeTurbine_HPSteam(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_LargeTurbine_HPSteam(String aName) {
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
                : casingTexturePages[0][59] };
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Steam Turbine")
            .addInfo("Controller block for the Large High Pressure Steam Turbine")
            .addInfo("Needs a Turbine, place inside controller")
            .addInfo("Outputs Steam as well as producing power")
            .addInfo("Power output depends on turbine and fitting")
            .addInfo("Use screwdriver to adjust fitting of turbine")
            .addSeparator()
            .beginStructureBlock(3, 3, 4, true)
            .addController("Front center")
            .addCasingInfoRange("Titanium Turbine Casing", 8, 31, false)
            .addDynamoHatch("Back center", 1)
            .addMaintenanceHatch("Side centered", 2)
            .addInputHatch("Superheated Steam, Side centered", 2)
            .addOutputHatch("Steam, Side centered", 2)
            .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_LargeTurbine_HPSteam(mName);
    }

    @Override
    public Block getCasingBlock() {
        return GregTech_API.sBlockCasings4;
    }

    @Override
    public byte getCasingMeta() {
        return 11;
    }

    @Override
    public int getCasingTextureIndex() {
        return 59;
    }

    @Override
    public boolean isNewStyleRendering() {
        return true;
    }

    @Override
    int fluidIntoPower(ArrayList<FluidStack> aFluids, int aOptFlow, int aBaseEff, int overflowEfficiency,
        float[] flowMultipliers) {
        if (looseFit) {
            float[] calculatedFlow = calculateLooseFlow(aOptFlow, aBaseEff);
            aOptFlow = GT_Utility.safeInt((long) calculatedFlow[0]);
            aBaseEff = GT_Utility.safeInt((long) calculatedFlow[1]);
        }
        int tEU = 0;
        int totalFlow = 0; // Byproducts are based on actual flow
        int flow = 0;

        // Allowed to use up to 300% optimal flow rate, depending on the value of overflowMultiplier.
        // This value is chosen because the highest EU/t possible depends on the overflowMultiplier, and the formula
        // used
        // makes it so the flow rate for that max, per value of overflowMultiplier, is (percentage of optimal flow
        // rate):
        // - 200% if it is 1
        // - 250% if it is 2
        // - 300% if it is 3
        // Variable required outside of loop for multi-hatch scenarios.
        this.realOptFlow = aOptFlow * flowMultipliers[0];
        int remainingFlow = GT_Utility.safeInt((long) (realOptFlow * (0.5f * overflowMultiplier + 1.5)));

        storedFluid = 0;
        for (int i = 0; i < aFluids.size() && remainingFlow > 0; i++) {
            final FluidStack aFluidStack = aFluids.get(i);
            if (GT_ModHandler.isSuperHeatedSteam(aFluidStack)) {
                flow = Math.min(aFluidStack.amount, remainingFlow); // try to use up to the max flow defined just above
                depleteInput(new FluidStack(aFluidStack, flow)); // deplete that amount
                this.storedFluid += aFluidStack.amount;
                remainingFlow -= flow; // track amount we're allowed to continue depleting from hatches
                totalFlow += flow; // track total input used
                if (!achievement) {
                    try {
                        GT_Mod.achievements.issueAchievement(
                            this.getBaseMetaTileEntity()
                                .getWorld()
                                .getPlayerEntityByName(
                                    this.getBaseMetaTileEntity()
                                        .getOwnerName()),
                            "efficientsteam");
                    } catch (Exception ignored) {}
                    achievement = true;
                }
            } else if (GT_ModHandler.isAnySteam(aFluidStack)) {
                depleteInput(new FluidStack(aFluidStack, aFluidStack.amount));
            }
        }
        if (totalFlow <= 0) return 0;
        tEU = totalFlow;
        addOutput(GT_ModHandler.getSteam(totalFlow));
        if (totalFlow == (GT_Utility.safeInt((long) realOptFlow))) {
            tEU = GT_Utility.safeInt((long) tEU * (long) aBaseEff / 10000L);
        } else {
            float efficiency = getOverflowEfficiency(
                totalFlow,
                (GT_Utility.safeInt((long) realOptFlow)),
                overflowMultiplier);
            tEU *= efficiency;
            tEU = Math.max(1, GT_Utility.safeInt((long) tEU * (long) aBaseEff / 10000L));
        }

        // If next output is above the maximum the dynamo can handle, set it to the maximum instead of exploding the
        // turbine
        // Raising the maximum allowed flow rate to account for the efficiency changes beyond the optimal flow rate can
        // explode turbines on world load
        if (tEU > getMaximumOutput()) {
            tEU = GT_Utility.safeInt(getMaximumOutput());
        }

        return tEU;
    }

    @Override
    float getOverflowEfficiency(int totalFlow, int actualOptimalFlow, int overflowMultiplier) {
        // overflowMultiplier changes how quickly the turbine loses efficiency after flow goes beyond the optimal value
        // At the default value of 1, any flow will generate less EU/t than optimal flow, regardless of the amount of
        // fuel used
        // The bigger this number is, the slower efficiency loss happens as flow moves beyond the optimal value
        // Superheated steam is the second least efficient out of all turbine fuels in this regard, with steam being the
        // least efficient
        float efficiency = 0;

        if (totalFlow > actualOptimalFlow) {
            efficiency = 1.0f
                - Math.abs((totalFlow - actualOptimalFlow)) / ((float) actualOptimalFlow * (overflowMultiplier + 2));
        } else {
            efficiency = 1.0f - Math.abs((totalFlow - actualOptimalFlow) / (float) actualOptimalFlow);
        }

        return efficiency;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (side == getBaseMetaTileEntity().getFrontFacing()) {
            looseFit ^= true;
            GT_Utility.sendChatToPlayer(
                aPlayer,
                looseFit ? GT_Utility.trans("500", "Fitting: Loose - More Flow")
                    : GT_Utility.trans("501", "Fitting: Tight - More Efficiency"));
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

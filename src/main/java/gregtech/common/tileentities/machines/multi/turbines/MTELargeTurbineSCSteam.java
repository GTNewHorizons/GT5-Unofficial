package gregtech.common.tileentities.machines.multi.turbines;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.XSTR;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.TurbineStatCalculator;

public class MTELargeTurbineSCSteam extends MTELargeTurbineBase {

    private static final IIconContainer[] TURBINE_ON = { Textures.BlockIcons.custom("icons/turbines/TURBINE_05") };
    private static final IIconContainer[] TURBINE_OFF = { Textures.BlockIcons.custom("icons/turbines/TURBINE_15") };
    private static final IIconContainer[] TURBINE_EMPTY = { Textures.BlockIcons.custom("icons/turbines/TURBINE_25") };

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
    protected boolean useLegacyEfficiencyScaling() {
        return true;
    }

    @Override
    public Casings getTurbineCasing() {
        return Casings.SCTurbineCasing;
    }

    @Override
    public Materials getFrameMaterial() {
        return Materials.Polybenzimidazole;
    }

    @Override
    public Casings getPipeCasing() {
        return Casings.PBIPipeCasing;
    }

    @Override
    public int fluidIntoPower(ArrayList<FluidStack> aFluids, TurbineStatCalculator turbine) {
        int tEU = 0;
        int totalFlow = 0;

        realOptFlow = looseFit ? turbine.getOptimalLooseSteamFlow() : turbine.getOptimalSteamFlow();

        int remainingFlow = GTUtility.safeInt((long) (realOptFlow * 1.25f));
        storedFluid = 0;

        FluidStack tSCSteam = FluidRegistry.getFluidStack("supercriticalsteam", 1);
        if (tSCSteam == null) return 0;

        for (int i = 0; i < aFluids.size() && remainingFlow > 0; i++) {
            FluidStack aFluidStack = aFluids.get(i);
            if (aFluidStack == null) continue;

            if (tSCSteam.isFluidEqual(aFluidStack)) {
                int flow = Math.min(aFluidStack.amount, remainingFlow);
                depleteInput(new FluidStack(aFluidStack, flow));

                storedFluid += aFluidStack.amount;
                remainingFlow -= flow;
                totalFlow += flow;
            } else if (GTModHandler.isAnySteam(aFluidStack)) {
                depleteInput(new FluidStack(aFluidStack, aFluidStack.amount));
            }
        }

        if (totalFlow <= 0) return 0;

        tEU = totalFlow;
        addOutput(FluidRegistry.getFluidStack("ic2superheatedsteam", totalFlow));

        float turbineEfficiency = looseFit ? turbine.getLooseSteamEfficiency() : turbine.getSteamEfficiency();

        if (totalFlow == realOptFlow) {
            tEU = GTUtility.safeInt((long) (tEU * turbineEfficiency));
        } else {
            float efficiency = 1.0f - Math.abs((totalFlow - (float) realOptFlow) / (float) realOptFlow);
            tEU = Math.max(1, GTUtility.safeInt((long) (tEU * efficiency * turbineEfficiency)));
        }

        if (tEU > maxPower) {
            tEU = GTUtility.safeInt(maxPower);
        }

        return tEU;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return looseFit ? (XSTR.XSTR_INSTANCE.nextInt(2) == 0 ? 0 : 1) : 2;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return new ITexture[] { getTurbineCasing().getCasingTexture(),
            aFacing == side ? (aActive ? TextureFactory.builder()
                .addIcon(TURBINE_ON[0])
                .build()
                : hasTurbine() ? TextureFactory.builder()
                    .addIcon(TURBINE_OFF[0])
                    .build()
                    : TextureFactory.builder()
                        .addIcon(TURBINE_EMPTY[0])
                        .build())
                : getTurbineCasing().getCasingTexture() };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType("Steam Turbine, LST-SC")
            .addInfo("Needs a Turbine, place inside controller")
            .addInfo("Use Supercritical Steam to generate power")
            .addInfo("Outputs 1L of SH Steam per 1L of SC Steam as well as producing power")
            .addInfo("Power output depends on turbine and fitting")
            .addInfo("Use screwdriver to adjust fitting of turbine")
            .beginStructureBlock(3, 3, 6, false)
            .addController("Front center")
            .addCasingInfoRange("SC Turbine Casing", 8, 16, false)
            .addCasingInfoExactly("PBI Frame Box", 14, false)
            .addCasingInfoExactly("PBI Pipe Casing", 12, false)
            .addDynamoHatch("Back center", 1)
            .addMaintenanceHatch("Any SC Turbine Casing except the front 8", 2)
            .addInputHatch("Any SC Turbine Casing except the front 8", 2)
            .addOutputHatch("Any SC Turbine Casing except the front 8", 2)
            .addOtherStructurePart("Air", "3x3 area in front of controller")
            .addStructureAuthors(EnumChatFormatting.GOLD + "hugetrust")
            .toolTipFinisher();
    }
}

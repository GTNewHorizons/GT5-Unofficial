package gregtech.common.tileentities.machines.multi.turbines;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
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

@IMetaTileEntity.SkipGenerateDescription
public class MTELargeTurbineSCSteam extends MTELargeTurbineBase {

    private static final IIconContainer[] TURBINE_ON = {
        Textures.BlockIcons.custom(Mods.GregTech.resourceDomain, "icons/turbines/TURBINE_05") };
    private static final IIconContainer[] TURBINE_OFF = {
        Textures.BlockIcons.custom(Mods.GregTech.resourceDomain, "icons/turbines/TURBINE_15") };
    private static final IIconContainer[] TURBINE_EMPTY = {
        Textures.BlockIcons.custom(Mods.GregTech.resourceDomain, "icons/turbines/TURBINE_25") };

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
        addOutputPartial(FluidRegistry.getFluidStack("ic2superheatedsteam", totalFlow));

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
        return new MultiblockTooltipBuilder()
            .addMachineType(StatCollector.translateToLocalFormatted("gt.mbtt.machine_type.steam_turbine", "LST-SC"))
            .addMarkdown(new ResourceLocation("gregtech", "large-sc-steam-turbine"))
            .beginStructureBlock(3, 3, 6, false)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_center_2nd_layer"))
            .addCasing("14", OrePrefixes.frameGt.getLocalizedNameForItem(getFrameMaterial()), false)
            .addCasing("8-14", getTurbineCasing().getLocalizedName(), false)
            .addCasing("12", getPipeCasing().getLocalizedName(), false)
            .addDynamoHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.back_center_turbine_casing"), 2)
            .addMaintenanceHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_back_turbine_casing"), 1)
            .addInputHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_back_turbine_casing"), 1)
            .addOutputHatch("0+", StatCollector.translateToLocal("gt.mbtt.structure.any_back_turbine_casing"), 1)
            .addAir(StatCollector.translateToLocal("gt.mbtt.structure.area_3x3_in_front_of_controller"))
            .addStructureInfo("")
            .addStructureFooter(StatCollector.translateToLocal("GT5U.MBTT.Structure.DynamoLimit"))
            .toolTipFinisher();
    }
}

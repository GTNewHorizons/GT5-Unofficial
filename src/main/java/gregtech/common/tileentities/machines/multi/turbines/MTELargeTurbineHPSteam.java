package gregtech.common.tileentities.machines.multi.turbines;

import static gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_NEW5;
import static gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_NEW_ACTIVE5;
import static gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_NEW_EMPTY5;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import gregtech.GTMod;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.TurbineStatCalculator;

@IMetaTileEntity.SkipGenerateDescription
public class MTELargeTurbineHPSteam extends MTELargeTurbineBase {

    public boolean achievement = false;

    public MTELargeTurbineHPSteam(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELargeTurbineHPSteam(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeTurbineHPSteam(mName);
    }

    @Override
    public Casings getTurbineCasing() {
        return Casings.TitaniumTurbineCasing;
    }

    @Override
    public Materials getFrameMaterial() {
        return Materials.Titanium;
    }

    @Override
    public Casings getPipeCasing() {
        return Casings.TitaniumPipeCasing;
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
                : getTurbineCasing().getCasingTexture() };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(StatCollector.translateToLocalFormatted("gt.mbtt.machine_type.steam_turbine", "LST-HP"))
            .addMarkdown(new ResourceLocation("gregtech", "large-hp-steam-turbine"))
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
        return tt;
    }

    @Override
    public int fluidIntoPower(ArrayList<FluidStack> aFluids, TurbineStatCalculator turbine) {
        int tEU = 0;
        int totalFlow = 0;
        int flow = 0;

        this.realOptFlow = looseFit ? turbine.getOptimalLooseSteamFlow() : turbine.getOptimalSteamFlow();
        // HP steam allows up to 300% optimal flow (vs 250% for normal steam)
        int remainingFlow = GTUtility.safeInt((long) (realOptFlow * (0.5f * turbine.getOverflowEfficiency() + 1.5)));

        storedFluid = 0;
        for (int i = 0; i < aFluids.size() && remainingFlow > 0; i++) {
            final FluidStack aFluidStack = aFluids.get(i);
            if (GTModHandler.isSuperHeatedSteam(aFluidStack)) {
                flow = Math.min(aFluidStack.amount, remainingFlow);
                depleteInput(new FluidStack(aFluidStack, flow));
                this.storedFluid += aFluidStack.amount;
                remainingFlow -= flow;
                totalFlow += flow;
                if (!achievement) {
                    try {
                        GTMod.achievements.issueAchievement(
                            this.getBaseMetaTileEntity()
                                .getWorld()
                                .getPlayerEntityByName(
                                    this.getBaseMetaTileEntity()
                                        .getOwnerName()),
                            "efficientsteam");
                    } catch (Exception ignored) {}
                    achievement = true;
                }
            } else if (GTModHandler.isAnySteam(aFluidStack)) {
                // Consume regular steam as waste but don't count it toward power
                depleteInput(new FluidStack(aFluidStack, aFluidStack.amount));
            }
        }

        if (totalFlow <= 0) return 0;

        tEU = totalFlow;
        // HP steam outputs regular steam instead of distilled water
        addOutputPartial(Materials.Steam.getGas(totalFlow));

        if (totalFlow == GTUtility.safeInt((long) realOptFlow)) {
            tEU = GTUtility
                .safeInt((long) (tEU * (looseFit ? turbine.getLooseSteamEfficiency() : turbine.getSteamEfficiency())));
        } else {
            float efficiency = getOverflowEfficiency(
                totalFlow,
                GTUtility.safeInt((long) realOptFlow),
                overflowMultiplier);
            tEU *= efficiency;
            tEU = Math.max(
                1,
                GTUtility.safeInt(
                    (long) (tEU * (looseFit ? turbine.getLooseSteamEfficiency() : turbine.getSteamEfficiency()))));
        }

        if (tEU > getMaximumOutput()) {
            tEU = GTUtility.safeInt(getMaximumOutput());
        }

        return tEU;
    }

    @Override
    protected float getOverflowEfficiency(int totalFlow, int actualOptimalFlow, int overflowMultiplier) {
        // HP steam is second least efficient after regular steam
        // Uses overflowMultiplier + 2 instead of + 1 (steam) or * 3 + 1 (plasma)
        float efficiency;
        if (totalFlow > actualOptimalFlow) {
            efficiency = 1.0f
                - Math.abs((totalFlow - actualOptimalFlow)) / ((float) actualOptimalFlow * (overflowMultiplier + 2));
        } else {
            efficiency = 1.0f - Math.abs((totalFlow - actualOptimalFlow) / (float) actualOptimalFlow);
        }
        return efficiency;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return (looseFit && XSTR_INSTANCE.nextInt(4) == 0) ? 0 : 1;
    }
}

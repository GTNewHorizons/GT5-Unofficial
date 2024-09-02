package goodgenerator.blocks.tileEntity;

import static gregtech.api.util.GTUtility.trans;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import goodgenerator.blocks.tileEntity.base.MTELargeTurbineBase;
import goodgenerator.loader.Loaders;
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

public class MTESupercriticalFluidTurbine extends MTELargeTurbineBase {

    private boolean looseFit = false;

    private static final IIconContainer turbineOn = new Textures.BlockIcons.CustomIcon("icons/turbines/TURBINE_05");
    private static final IIconContainer turbineOff = new Textures.BlockIcons.CustomIcon("icons/turbines/TURBINE_15");
    private static final IIconContainer turbineEmpty = new Textures.BlockIcons.CustomIcon("icons/turbines/TURBINE_25");

    public MTESupercriticalFluidTurbine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTESupercriticalFluidTurbine(String aName) {
        super(aName);
    }

    @Override
    public int fluidIntoPower(ArrayList<FluidStack> aFluids, int aOptFlow, int aBaseEff) {
        if (looseFit) {
            aOptFlow *= 4;
            double pow = Math.pow(1.1f, ((aBaseEff - 7500) / 10000F) * 20f);
            if (aBaseEff > 10000) {
                aOptFlow *= pow;
                aBaseEff = 7500;
            } else if (aBaseEff > 7500) {
                aOptFlow *= pow;
                aBaseEff *= 0.75f;
            } else {
                aBaseEff *= 0.75f;
            }
        }
        int tEU = 0;
        int totalFlow = 0; // Byproducts are based on actual flow
        int flow = 0;
        int remainingFlow = GTUtility.safeInt((long) (aOptFlow * 1.25f)); // Allowed to use up to 125% of optimal flow.
                                                                          // Variable required outside of loop for
        // multi-hatch scenarios.
        this.realOptFlow = aOptFlow;

        storedFluid = 0;
        FluidStack tSCSteam = FluidRegistry.getFluidStack("supercriticalsteam", 1);
        for (int i = 0; i < aFluids.size() && remainingFlow > 0; i++) {
            final FluidStack aFluidStack = aFluids.get(i);
            if (tSCSteam.isFluidEqual(aFluidStack)) {
                flow = Math.min(aFluidStack.amount, remainingFlow);
                depleteInput(new FluidStack(aFluidStack, flow));
                this.storedFluid += aFluidStack.amount;
                remainingFlow -= flow;
                totalFlow += flow;
            } else if (GTModHandler.isAnySteam(aFluidStack)) {
                depleteInput(new FluidStack(aFluidStack, aFluidStack.amount));
            }
        }
        if (totalFlow <= 0) return 0;
        tEU = totalFlow;
        addOutput(GTModHandler.getSteam(totalFlow * 100));
        if (totalFlow == aOptFlow) {
            tEU = GTUtility.safeInt((long) tEU * (long) aBaseEff / 10000L);
        } else {
            float efficiency = 1.0f - Math.abs((totalFlow - aOptFlow) / (float) aOptFlow);
            tEU *= efficiency;
            tEU = Math.max(1, GTUtility.safeInt((long) tEU * (long) aBaseEff / 10000L));
        }

        if (tEU > maxPower) {
            tEU = GTUtility.safeInt(maxPower);
        }

        return tEU * 100;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (side == getBaseMetaTileEntity().getFrontFacing()) {
            looseFit ^= true;
            GTUtility.sendChatToPlayer(
                aPlayer,
                looseFit ? trans("500", "Fitting: Loose - More Flow")
                    : trans("501", "Fitting: Tight - More Efficiency"));
        }
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        // 2x more damage than normal turbine
        return looseFit ? (XSTR.XSTR_INSTANCE.nextInt(2) == 0 ? 0 : 1) : 2;
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

    @Override
    public Block getCasingBlock() {
        return Loaders.supercriticalFluidTurbineCasing;
    }

    @Override
    public int getCasingMeta() {
        return 0;
    }

    @Override
    public int getCasingTextureIndex() {
        return 1538;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Supercritical Steam Turbine")
            .addInfo("Controller block for Supercritical Fluid Turbine")
            .addInfo("Needs a Turbine, place inside controller")
            .addInfo("Use Supercritical Steam to generate power.")
            .addInfo("Outputs 100L of Steam per 1L of SC Steam as well as producing power")
            .addInfo("1L Supercritical Steam = 100 EU")
            .addInfo("Extreme Heated Steam will cause more damage to the turbine.")
            .addInfo("Power output depends on turbine and fitting")
            .addInfo("Use screwdriver to adjust fitting of turbine")
            .addSeparator()
            .beginStructureBlock(3, 3, 4, true)
            .addController("Front center")
            .addCasingInfo("SC Turbine Casing", 24)
            .addDynamoHatch("Back center", 1)
            .addMaintenanceHatch("Side centered", 2)
            .addInputHatch("Supercritical Fluid, Side centered", 2)
            .toolTipFinisher("Good Generator");
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESupercriticalFluidTurbine(mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(1538),
            facing == side
                ? (aActive ? TextureFactory.of(turbineOn)
                    : hasTurbine() ? TextureFactory.of(turbineOff) : TextureFactory.of(turbineEmpty))
                : Textures.BlockIcons.getCasingTextureForId(1538) };
    }
}

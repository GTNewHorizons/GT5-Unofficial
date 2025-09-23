package tectech.thing.metaTileEntity.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.Maintenance;
import static net.minecraft.util.StatCollector.translateToLocal;
import static tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.HatchElement.EnergyMulti;
import static tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.HatchElement.InputData;
import static tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.HatchElement.OutputData;

import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import gregtech.api.casing.Casings;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.structure.IStructureInstance;
import gregtech.api.structure.IStructureProvider;
import gregtech.api.structure.StructureWrapper;
import gregtech.api.structure.StructureWrapperInstanceInfo;
import gregtech.api.structure.StructureWrapperTooltipBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import tectech.mechanics.dataTransport.QuantumDataPacket;
import tectech.thing.casing.BlockGTCasingsTT;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataInput;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataOutput;
import tectech.thing.metaTileEntity.multi.base.INameFunction;
import tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import tectech.thing.metaTileEntity.multi.base.LedStatus;
import tectech.thing.metaTileEntity.multi.base.Parameters;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.render.TTRenderedExtendedFacingTexture;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class MTENetworkSwitch extends TTMultiblockBase
    implements ISurvivalConstructable, IStructureProvider<MTENetworkSwitch> {

    protected final StructureWrapper<MTENetworkSwitch> structure;
    protected final StructureWrapperInstanceInfo<MTENetworkSwitch> structureInstanceInfo;

    private static final INameFunction<MTENetworkSwitch> DEST_NAME = MTENetworkSwitch::getDestName;
    private static final IStatusFunction<MTENetworkSwitch> DEST_STATUS = MTENetworkSwitch::getDestStatus;

    private static final INameFunction<MTENetworkSwitch> WEIGHT_NAME = MTENetworkSwitch::getWeightName;
    private static final IStatusFunction<MTENetworkSwitch> WEIGHT_STATUS = MTENetworkSwitch::getWeightStatus;

    protected Parameters.Group.ParameterIn[] dst;
    protected Parameters.Group.ParameterIn[] weight;

    public MTENetworkSwitch(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);

        structure = new StructureWrapper<>(this);
        structureInstanceInfo = null;

        structure.loadStructure();
    }

    public MTENetworkSwitch(MTENetworkSwitch prototype) {
        super(prototype.mName);

        structure = prototype.structure;
        structureInstanceInfo = new StructureWrapperInstanceInfo<>(structure);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTENetworkSwitch(this);
    }

    @Override
    public String[][] getDefinition() {
        // spotless:off
        return transpose(new String[][] {
            { "BBB", "BAB", "BBB" },
            { "B~B", "AAA", "BAB" },
            { "BBB", "BAB", "BBB" }
        });
        // spotless:on
    }

    @Override
    public IStructureDefinition<MTENetworkSwitch> compile(String[][] definition) {
        structure.addCasing('A', Casings.AdvancedComputerCasing)
            .withUnlimitedHatches(1, Arrays.asList(Energy.or(EnergyMulti), Maintenance, InputData, OutputData));
        structure.addCasing('B', Casings.ComputerCasing)
            .withUnlimitedHatches(2, Arrays.asList(Energy.or(EnergyMulti), Maintenance, OutputData));

        return structure.buildStructure(definition);
    }

    @Override
    public IStructureInstance getStructureInstance() {
        return structureInstanceInfo;
    }

    @Override
    public IStructureDefinition<MTENetworkSwitch> getStructure_EM() {
        return structure.getStructureDefinition();
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structure.checkStructure(this);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structure.construct(this, stackSize, hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;

        return structure.survivalConstruct(this, stackSize, elementBudget, env);
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        StructureWrapperTooltipBuilder<MTENetworkSwitch> tt = new StructureWrapperTooltipBuilder<>(structure);

        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.switch.type"));

        tt.addInfo(translateToLocal("gt.blockmachines.multimachine.em.switch.desc.0"));
        tt.addInfo(translateToLocal("gt.blockmachines.multimachine.em.switch.desc.1"));
        tt.addInfo(translateToLocal("gt.blockmachines.multimachine.em.switch.desc.2"));
        tt.addSeparator();
        tt.addInfo(translateToLocal("gt.blockmachines.multimachine.em.switch.desc.3"));
        tt.addInfo(translateToLocal("gt.blockmachines.multimachine.em.switch.desc.4"));
        tt.addInfo(translateToLocal("gt.blockmachines.multimachine.em.switch.desc.5"));

        tt.addTecTechHatchInfo();

        tt.beginStructureBlock();
        tt.addController(translateToLocal("tt.keyword.Structure.FrontCenter"));

        tt.addCasingInfoAuto(Casings.ComputerCasing);
        tt.addCasingInfoAuto(Casings.AdvancedComputerCasing);

        tt.addHatch(Casings.AdvancedComputerCasing, InputData, 1);
        tt.addHatch(Casings.ComputerCasing, OutputData, 1, 2);
        tt.addHatch(Casings.ComputerCasing, Energy, 1, 2);
        tt.addHatch(Casings.ComputerCasing, Maintenance, 1, 2);

        tt.toolTipFinisher();

        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[BlockGTCasingsTT.texturePage][1],
                new TTRenderedExtendedFacingTexture(aActive ? TTMultiblockBase.ScreenON : TTMultiblockBase.ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[BlockGTCasingsTT.texturePage][1] };
    }

    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.TECTECH_MACHINES_FX_HIGH_FREQ;
    }

    private static String getDestName(MTENetworkSwitch base, Parameters.IParameter p) {
        return translateToLocal("tt.keyword.Destination") + " " + (p.hatchId() + 1);
    }

    private static LedStatus getDestStatus(MTENetworkSwitch base, Parameters.IParameter p) {
        if (base.weight[p.hatchId()].getStatus(false).isOk) {
            double v = p.get();
            if (Double.isNaN(v)) return LedStatus.STATUS_WRONG;
            v = (int) v;
            if (v <= 0) return LedStatus.STATUS_TOO_LOW;
            return LedStatus.STATUS_OK;
        }
        return LedStatus.STATUS_NEUTRAL;
    }

    private static String getWeightName(MTENetworkSwitch base, Parameters.IParameter p) {
        return translateToLocal("tt.keyword.Weight") + " " + (p.hatchId() + 1);
    }

    private static LedStatus getWeightStatus(MTENetworkSwitch base, Parameters.IParameter p) {
        double v = p.get();
        if (Double.isNaN(v)) return LedStatus.STATUS_WRONG;
        if (v < 0) return LedStatus.STATUS_TOO_LOW;
        if (v == 0) return LedStatus.STATUS_LOW;
        if (Double.isInfinite(v)) return LedStatus.STATUS_HIGH;
        return LedStatus.STATUS_OK;
    }

    @Override
    protected void parametersInstantiation_EM() {
        dst = new Parameters.Group.ParameterIn[10];
        weight = new Parameters.Group.ParameterIn[10];
        for (int i = 0; i < 10; i++) {
            Parameters.Group hatch = parametrization.getGroup(i);
            dst[i] = hatch.makeInParameter(0, i + 1, DEST_NAME, DEST_STATUS);
            weight[i] = hatch.makeInParameter(1, 0, WEIGHT_NAME, WEIGHT_STATUS);
        }
    }

    @Override
    @NotNull
    protected CheckRecipeResult checkProcessing_EM() {
        short thingsActive = 0;
        for (MTEHatchDataInput di : eInputData) {
            if (di.q != null) {
                thingsActive++;
            }
        }

        if (thingsActive > 0) {
            thingsActive += eOutputData.size();
            mEUt = -(int) V[7];
            eAmpereFlow = 1 + (thingsActive >> 2);
            mMaxProgresstime = 20;
            mEfficiencyIncrease = 10000;
            return SimpleCheckRecipeResult.ofSuccess("routing");
        }
        return SimpleCheckRecipeResult.ofFailure("no_routing");
    }

    @Override
    public void outputAfterRecipe_EM() {
        if (!eOutputData.isEmpty()) {
            double total = 0;
            double weight;
            for (int i = 0; i < 10; i++) { // each param pair
                weight = this.weight[i].get();
                if (weight > 0 && dst[i].get() >= 0) {
                    total += weight; // Total weighted div
                }
            }

            Vec3Impl pos = new Vec3Impl(
                getBaseMetaTileEntity().getXCoord(),
                getBaseMetaTileEntity().getYCoord(),
                getBaseMetaTileEntity().getZCoord());

            QuantumDataPacket pack = new QuantumDataPacket(0L).unifyTraceWith(pos);
            if (pack == null) {
                return;
            }
            for (MTEHatchDataInput hatch : eInputData) {
                if (hatch.q == null || hatch.q.contains(pos)) {
                    continue;
                }
                pack = pack.unifyPacketWith(hatch.q);
                if (pack == null) {
                    return;
                }
            }

            long remaining = pack.getContent();

            double dest;
            for (int i = 0; i < 10; i++) {
                dest = dst[i].get();
                weight = this.weight[i].get();
                if (weight > 0 && dest >= 0) {
                    int outIndex = (int) dest - 1;
                    if (outIndex < 0 || outIndex >= eOutputData.size()) {
                        continue;
                    }
                    MTEHatchDataOutput out = eOutputData.get(outIndex);
                    if (Double.isInfinite(total)) {
                        if (Double.isInfinite(weight)) {
                            out.q = new QuantumDataPacket(remaining).unifyTraceWith(pack);
                            break;
                        }
                    } else {
                        long part = (long) Math.floor(pack.getContent() * weight / total);
                        if (part > 0) {
                            remaining -= part;
                            if (remaining > 0) {
                                out.q = new QuantumDataPacket(part).unifyTraceWith(pack);
                            } else if (part + remaining > 0) {
                                out.q = new QuantumDataPacket(part + remaining).unifyTraceWith(pack);
                                break;
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}

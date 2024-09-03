package tectech.thing.metaTileEntity.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.util.GTStructureUtility.ofHatchAdderOptional;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.MultiblockTooltipBuilder;
import tectech.Reference;
import tectech.mechanics.dataTransport.QuantumDataPacket;
import tectech.thing.casing.BlockGTCasingsTT;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataInput;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataOutput;
import tectech.thing.metaTileEntity.multi.base.INameFunction;
import tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import tectech.thing.metaTileEntity.multi.base.LedStatus;
import tectech.thing.metaTileEntity.multi.base.Parameters;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.render.TTRenderedExtendedFacingTexture;
import tectech.util.CommonValues;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class MTENetworkSwitch extends TTMultiblockBase implements IConstructable {

    // region structure
    private static final String[] description = new String[] {
        EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
        "1 - Classic/Data Hatches or Computer casing", // 1 - Classic/Data Hatches or Computer casing
    };

    private static final IStructureDefinition<MTENetworkSwitch> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTENetworkSwitch>builder()
        .addShape(
            "main",
            transpose(new String[][] { { "BBB", "BBB", "BBB" }, { "B~B", "BAB", "BBB" }, { "BBB", "BBB", "BBB" } }))
        .addElement('A', ofBlock(TTCasingsContainer.sBlockCasingsTT, 3))
        .addElement(
            'B',
            ofHatchAdderOptional(
                MTENetworkSwitch::addClassicToMachineList,
                BlockGTCasingsTT.textureOffset + 1,
                1,
                TTCasingsContainer.sBlockCasingsTT,
                1))
        .build();
    // endregion

    // region parameters
    private static final INameFunction<MTENetworkSwitch> ROUTE_NAME = (base,
        p) -> (p.parameterId() == 0 ? translateToLocal("tt.keyword.Destination") + " "
            : translateToLocal("tt.keyword.Weight") + " ") + p.hatchId();
    private static final IStatusFunction<MTENetworkSwitch> WEI_STATUS = (base, p) -> {
        double v = p.get();
        if (Double.isNaN(v)) return LedStatus.STATUS_WRONG;
        if (v < 0) return LedStatus.STATUS_TOO_LOW;
        if (v == 0) return LedStatus.STATUS_LOW;
        if (Double.isInfinite(v)) return LedStatus.STATUS_HIGH;
        return LedStatus.STATUS_OK;
    };
    private static final IStatusFunction<MTENetworkSwitch> DST_STATUS = (base, p) -> {
        if (base.weight[p.hatchId()].getStatus(false).isOk) {
            double v = p.get();
            if (Double.isNaN(v)) return LedStatus.STATUS_WRONG;
            v = (int) v;
            if (v <= 0) return LedStatus.STATUS_TOO_LOW;
            return LedStatus.STATUS_OK;
        }
        return LedStatus.STATUS_NEUTRAL;
    };
    protected Parameters.Group.ParameterIn[] dst;
    protected Parameters.Group.ParameterIn[] weight;
    // endregion

    public MTENetworkSwitch(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTENetworkSwitch(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTENetworkSwitch(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM("main", 1, 1, 0);
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

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.switch.name")) // Machine Type: Network
                                                                                            // Switch With QoS
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.switch.desc.0")) // Controller block of the
                                                                                         // Network
            // Switch With QoS
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.switch.desc.1")) // Used to route and
                                                                                         // distribute computation
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.switch.desc.2")) // Needs a Parametrizer to
                                                                                         // be configured
            .addSeparator()
            .beginStructureBlock(3, 3, 3, false)
            .addController(translateToLocal("tt.keyword.Structure.FrontCenter")) // Controller: Front center
            .addCasingInfoMin(translateToLocal("gt.blockcasingsTT.1.name"), 0, false) // 0x Computer Casing
                                                                                      // (minimum)
            .addOtherStructurePart(
                translateToLocal("gt.blockcasingsTT.3.name"),
                translateToLocal("tt.keyword.Structure.Center")) // Advanced Computer Casing: Center
            .addOtherStructurePart(
                translateToLocal("tt.keyword.Structure.DataConnector"),
                translateToLocal("tt.keyword.Structure.AnyComputerCasing"),
                2) // Data Connector: Any Computer Casing
            .addOtherStructurePart(
                translateToLocal("gt.blockmachines.hatch.param.tier.05.name"),
                translateToLocal("tt.keyword.Structure.AnyComputerCasing"),
                2) // Parametrizer: Any Computer Casing
            .addEnergyHatch(translateToLocal("tt.keyword.Structure.AnyComputerCasing"), 1) // Energy Hatch: Any
                                                                                           // Computer Casing
            .addMaintenanceHatch(translateToLocal("tt.keyword.Structure.AnyComputerCasing"), 1) // Maintenance
                                                                                                // Hatch: Any
                                                                                                // Computer Casing
            .toolTipFinisher(CommonValues.TEC_MARK_EM);
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

    public static final ResourceLocation activitySound = new ResourceLocation(Reference.MODID + ":fx_hi_freq");

    @Override
    @SideOnly(Side.CLIENT)
    protected ResourceLocation getActivitySound() {
        return activitySound;
    }

    @Override
    protected void parametersInstantiation_EM() {
        dst = new Parameters.Group.ParameterIn[10];
        weight = new Parameters.Group.ParameterIn[10];
        for (int i = 0; i < 10; i++) {
            Parameters.Group hatch = parametrization.getGroup(i);
            dst[i] = hatch.makeInParameter(0, i, ROUTE_NAME, DST_STATUS);
            weight[i] = hatch.makeInParameter(1, 0, ROUTE_NAME, WEI_STATUS);
        }
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM("main", 1, 1, 0, stackSize, hintsOnly);
    }

    @Override
    public IStructureDefinition<MTENetworkSwitch> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }
}

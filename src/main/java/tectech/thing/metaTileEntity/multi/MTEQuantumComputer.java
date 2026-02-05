package tectech.thing.metaTileEntity.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.recipe.RecipeMaps.quantumComputerFakeRecipes;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTUtility.validMTEList;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.common.WirelessComputationPacket;
import tectech.mechanics.dataTransport.QuantumDataPacket;
import tectech.thing.casing.BlockGTCasingsTT;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataInput;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataOutput;
import tectech.thing.metaTileEntity.hatch.MTEHatchRack;
import tectech.thing.metaTileEntity.hatch.MTEHatchWirelessComputationOutput;
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
public class MTEQuantumComputer extends TTMultiblockBase implements ISurvivalConstructable {

    // region variables
    private final ArrayList<MTEHatchRack> eRacks = new ArrayList<>();

    private final ArrayList<MTEHatchWirelessComputationOutput> eWirelessComputationOutputs = new ArrayList<>();

    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;
    // endregion

    // region structure
    private static final String[] description = new String[] {
        EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
        translateToLocal("gt.blockmachines.multimachine.em.computer.hint.0"), // 1 - Classic/Data Hatches or
                                                                              // Computer casing
        translateToLocal("gt.blockmachines.multimachine.em.computer.hint.1"), // 2 - Rack Hatches or Advanced
                                                                              // computer casing
    };

    private static final IStructureDefinition<MTEQuantumComputer> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTEQuantumComputer>builder()
        .addShape("front", transpose(new String[][] { { " AA" }, { " AA" }, { " ~A" }, { " AA" } }))
        .addShape("cap", transpose(new String[][] { { "-CB" }, { " DD" }, { " DD" }, { "-CB" } }))
        .addShape("slice", transpose(new String[][] { { "-CB" }, { " ED" }, { " ED" }, { "-CB" } }))
        .addShape("back", transpose(new String[][] { { " AA" }, { " AA" }, { " AA" }, { " AA" } }))
        .addElement('B', ofBlock(TTCasingsContainer.sBlockCasingsTT, 1))
        .addElement('C', ofBlock(TTCasingsContainer.sBlockCasingsTT, 2))
        .addElement('D', ofBlock(TTCasingsContainer.sBlockCasingsTT, 3))
        .addElement(
            'A',
            buildHatchAdder(MTEQuantumComputer.class)
                .atLeast(
                    Energy.or(HatchElement.EnergyMulti),
                    Maintenance,
                    HatchElement.Uncertainty,
                    HatchElement.InputData,
                    HatchElement.OutputData,
                    WirelessComputationHatchElement.INSTANCE)
                .casingIndex(BlockGTCasingsTT.textureOffset + 1)
                .dot(1)
                .buildAndChain(ofBlock(TTCasingsContainer.sBlockCasingsTT, 1)))
        .addElement(
            'E',
            RackHatchElement.INSTANCE
                .newAnyOrCasing(BlockGTCasingsTT.textureOffset + 3, 2, TTCasingsContainer.sBlockCasingsTT, 3))
        .build();
    // endregion

    // region parameters
    protected Parameters.Group.ParameterIn overclock, overvolt;
    protected Parameters.Group.ParameterOut maxCurrentTemp, availableData;

    private static final INameFunction<MTEQuantumComputer> OC_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.em.computer.cfgi.0"); // Overclock ratio
    private static final INameFunction<MTEQuantumComputer> OV_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.em.computer.cfgi.1"); // Overvoltage ratio
    private static final INameFunction<MTEQuantumComputer> MAX_TEMP_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.em.computer.cfgo.0"); // Current max. heat
    private static final INameFunction<MTEQuantumComputer> COMPUTE_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.em.computer.cfgo.1"); // Produced computation
    private static final IStatusFunction<MTEQuantumComputer> OC_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), 0, 1, 3, 5);
    private static final IStatusFunction<MTEQuantumComputer> OV_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), 0, 1, 3, 5);
    private static final IStatusFunction<MTEQuantumComputer> MAX_TEMP_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), 0, 2000, 8000, 10000);
    private static final IStatusFunction<MTEQuantumComputer> COMPUTE_STATUS = (base, p) -> {
        if (base.eAvailableData < 0) {
            return LedStatus.STATUS_TOO_LOW;
        }
        if (base.eAvailableData == 0) {
            return LedStatus.STATUS_NEUTRAL;
        }
        return LedStatus.STATUS_OK;
    };
    // endregion

    public MTEQuantumComputer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        eCertainMode = 5;
        eCertainStatus = -128; // no-brain value
    }

    public MTEQuantumComputer(String aName) {
        super(aName);
        eCertainMode = 5;
        eCertainStatus = -128; // no-brain value
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return quantumComputerFakeRecipes;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEQuantumComputer(mName);
    }

    @Override
    protected void parametersInstantiation_EM() {
        Parameters.Group hatch_0 = parametrization.getGroup(0);
        overclock = hatch_0.makeInParameter(0, 1, OC_NAME, OC_STATUS);
        overvolt = hatch_0.makeInParameter(1, 1, OV_NAME, OV_STATUS);
        maxCurrentTemp = hatch_0.makeOutParameter(0, 0, MAX_TEMP_NAME, MAX_TEMP_STATUS);
        availableData = hatch_0.makeOutParameter(1, 0, COMPUTE_NAME, COMPUTE_STATUS);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        for (MTEHatchRack rack : validMTEList(eRacks)) {
            rack.getBaseMetaTileEntity()
                .setActive(false);
        }
        eRacks.clear();
        if (!structureCheck_EM("front", 1, 2, 0)) {
            return false;
        }
        if (!structureCheck_EM("cap", 1, 2, -1)) {
            return false;
        }
        byte offset = -2, totalLen = 4;
        while (offset > -16) {
            if (!structureCheck_EM("slice", 1, 2, offset)) {
                break;
            }
            totalLen++;
            offset--;
        }
        if (totalLen > 17) {
            return false;
        }
        if (!structureCheck_EM("cap", 1, 2, ++offset)) {
            return false;
        }
        if (!structureCheck_EM("back", 1, 2, --offset)) {
            return false;
        }
        eCertainMode = (byte) Math.min(totalLen / 3, 5);
        for (MTEHatchRack rack : validMTEList(eRacks)) {
            rack.getBaseMetaTileEntity()
                .setActive(iGregTechTileEntity.isActive());
        }
        return eUncertainHatches.size() == 1;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setDouble("computation", availableData.get());
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (availableData != null) {
            availableData.set(aNBT.getDouble("computation"));
            eAvailableData = (long) availableData.get();
        }
        WirelessComputationPacket.enableWirelessNetWork(getBaseMetaTileEntity());
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && mMachine
            && !aBaseMetaTileEntity.isActive()
            && aTick % 20 == CommonValues.MULTI_CHECK_AT) {
            double maxTemp = 0;
            for (MTEHatchRack rack : validMTEList(eRacks)) {
                if (rack.heat > maxTemp) {
                    maxTemp = rack.heat;
                }
            }
            maxCurrentTemp.set(maxTemp);
        }
    }

    @Override
    @NotNull
    protected CheckRecipeResult checkProcessing_EM() {
        parametrization.setToDefaults(false, true);
        eAvailableData = 0;
        double maxTemp = 0;
        double overClockRatio = overclock.get();
        double overVoltageRatio = overvolt.get();
        if (Double.isNaN(overClockRatio) || Double.isNaN(overVoltageRatio)) {
            return SimpleCheckRecipeResult.ofFailure("no_computing");
        }
        if (overclock.getStatus(true).isOk && overvolt.getStatus(true).isOk) {
            float eut = Math.max(V[6], V[7] * (float) overClockRatio * (float) overVoltageRatio);
            if (eut < Integer.MAX_VALUE - 7) {
                mEUt = -(int) eut;
            } else {
                mEUt = -(int) V[7];
                return CheckRecipeResultRegistry.POWER_OVERFLOW;
            }
            short thingsActive = 0;
            int rackComputation;

            for (MTEHatchRack rack : validMTEList(eRacks)) {
                if (rack.heat > maxTemp) {
                    maxTemp = rack.heat;
                }
                rackComputation = rack.tickComponents((float) overClockRatio, (float) overVoltageRatio);
                if (rackComputation > 0) {
                    eAvailableData += rackComputation;
                    thingsActive += 4;
                }
                rack.getBaseMetaTileEntity()
                    .setActive(true);
            }

            for (MTEHatchDataInput di : eInputData) {
                if (di.q != null) // ok for power losses
                {
                    thingsActive++;
                }
            }

            if (thingsActive > 0 && eCertainStatus == 0) {
                thingsActive += eOutputData.size();
                eAmpereFlow = 1 + (thingsActive >> 2);
                mMaxProgresstime = 20;
                mEfficiencyIncrease = 10000;
                maxCurrentTemp.set(maxTemp);
                availableData.set(eAvailableData);
                return SimpleCheckRecipeResult.ofSuccess("computing");
            } else {
                eAvailableData = 0;
                mEUt = -(int) V[7];
                eAmpereFlow = 1;
                mMaxProgresstime = 20;
                mEfficiencyIncrease = 10000;
                maxCurrentTemp.set(maxTemp);
                availableData.set(eAvailableData);
                return SimpleCheckRecipeResult.ofSuccess("no_computing");
            }
        }
        return SimpleCheckRecipeResult.ofFailure("no_computing");
    }

    @Override
    public void outputAfterRecipe_EM() {
        if (!eOutputData.isEmpty()) {
            Vec3Impl pos = new Vec3Impl(
                getBaseMetaTileEntity().getXCoord(),
                getBaseMetaTileEntity().getYCoord(),
                getBaseMetaTileEntity().getZCoord());

            int eHatchData = 0;

            for (MTEHatchDataInput hatch : eInputData) {
                if (hatch.q == null || hatch.q.contains(pos)) {
                    continue;
                }
                eHatchData += hatch.q.getContent();
            }

            QuantumDataPacket pack = new QuantumDataPacket((eAvailableData + eHatchData) / eOutputData.size())
                .unifyTraceWith(pos);
            if (pack == null) {
                return;
            }

            for (MTEHatchDataOutput o : eOutputData) {
                o.providePacket(pack);
            }
        }
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.computer.machinetype")) // Machine Type:
                                                                                                     // Quantum
            // Computer
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.computer.desc.0")) // Controller block of
                                                                                           // the Quantum Computer
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.computer.desc.1")) // Used to generate
                                                                                           // computation (and heat)
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.computer.desc.2")) // Use screwdriver to
                                                                                           // toggle
                                                                                           // wireless mode
            .addTecTechHatchInfo()
            .beginVariableStructureBlock(2, 2, 4, 4, 5, 16, false)
            .addOtherStructurePart(
                translateToLocal("gt.blockmachines.hatch.certain.tier.07.name"),
                translateToLocal("tt.keyword.Structure.AnyComputerCasingFirstOrLastSlice"),
                1) // Uncertainty Resolver: Any Computer Casing on first or last slice
            .addOtherStructurePart(
                translateToLocal("tt.keyword.Structure.DataConnector"),
                translateToLocal("tt.keyword.Structure.AnyComputerCasingFirstOrLastSlice"),
                1) // Optical Connector: Any Computer Casing on first or last slice
            .addOtherStructurePart(
                translateToLocal("gt.blockmachines.hatch.rack.tier.08.name"),
                translateToLocal("tt.keyword.Structure.AnyAdvComputerCasingExceptOuter"),
                2) // Computer Rack: Any Advanced Computer Casing, except the outer ones
            .addEnergyHatch(translateToLocal("tt.keyword.Structure.AnyComputerCasingFirstOrLastSlice"), 1) // Energy
                                                                                                           // Hatch:
                                                                                                           // Any
                                                                                                           // Computer
                                                                                                           // Casing
                                                                                                           // on
                                                                                                           // first
                                                                                                           // or
                                                                                                           // last
                                                                                                           // slice
            .addMaintenanceHatch(translateToLocal("tt.keyword.Structure.AnyComputerCasingFirstOrLastSlice"), 1) // Maintenance
                                                                                                                // Hatch:
                                                                                                                // Any
                                                                                                                // Computer
                                                                                                                // Casing
                                                                                                                // on
                                                                                                                // first
                                                                                                                // or
                                                                                                                // last
                                                                                                                // slice
            .toolTipFinisher();
        return tt;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_COMPUTER");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_COMPUTER_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[BlockGTCasingsTT.texturePage][3],
                new TTRenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[BlockGTCasingsTT.texturePage][3] };
    }

    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.TECTECH_MACHINES_FX_HIGH_FREQ;
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        for (MTEHatchRack rack : validMTEList(eRacks)) {
            rack.getBaseMetaTileEntity()
                .setActive(false);
        }
    }

    @Override
    protected void extraExplosions_EM() {
        for (MetaTileEntity tTileEntity : eRacks) {
            tTileEntity.getBaseMetaTileEntity()
                .doExplosion(V[8]);
        }
    }

    @Override
    protected long getAvailableData_EM() {
        return eAvailableData;
    }

    @Override
    public void stopMachine(@Nonnull ShutDownReason reason) {
        super.stopMachine(reason);
        eAvailableData = 0;
        for (MTEHatchRack rack : validMTEList(eRacks)) {
            rack.getBaseMetaTileEntity()
                .setActive(false);
        }
    }

    @Override
    protected void afterRecipeCheckFailed() {
        super.afterRecipeCheckFailed();
        for (MTEHatchRack rack : validMTEList(eRacks)) {
            rack.getBaseMetaTileEntity()
                .setActive(false);
        }
    }

    public final boolean addRackToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof MTEHatchRack) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eRacks.add((MTEHatchRack) aMetaTileEntity);
        }
        return false;
    }

    public final boolean addWirelessDataOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof MTEHatchWirelessComputationOutput) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            // Add to wireless computation outputs, so we can detect these and turn on wireless mode,
            // but also add to regular outputs, so they are used as output data hatches by the quantum computer
            return eWirelessComputationOutputs.add((MTEHatchWirelessComputationOutput) aMetaTileEntity)
                && eOutputData.add((MTEHatchWirelessComputationOutput) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM("front", 1, 2, 0, stackSize, hintsOnly);
        structureBuild_EM("cap", 1, 2, -1, stackSize, hintsOnly);

        byte offset = -2;
        for (int rackSlices = Math.min(stackSize.stackSize, 12); rackSlices > 0; rackSlices--) {
            structureBuild_EM("slice", 1, 2, offset--, stackSize, hintsOnly);
        }

        structureBuild_EM("cap", 1, 2, offset--, stackSize, hintsOnly);
        structureBuild_EM("back", 1, 2, offset, stackSize, hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        int built;
        built = survivalBuildPiece("front", stackSize, 1, 2, 0, elementBudget, source, actor, false, true);
        if (built >= 0) return built;
        built = survivalBuildPiece("cap", stackSize, 1, 2, -1, elementBudget, source, actor, false, true);
        if (built >= 0) return built;

        byte offset = -2;
        for (int rackSlices = Math.min(stackSize.stackSize, 12); rackSlices > 0; rackSlices--) {
            built = survivalBuildPiece("slice", stackSize, 1, 2, offset--, elementBudget, source, actor, false, true);
            if (built >= 0) return built;
        }
        built = survivalBuildPiece("cap", stackSize, 1, 2, offset--, elementBudget, source, actor, false, true);
        if (built >= 0) return built;
        return survivalBuildPiece("back", stackSize, 1, 2, offset, elementBudget, source, actor, false, true);
    }

    @Override
    public IStructureDefinition<MTEQuantumComputer> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> data = new ArrayList<>(Arrays.asList(super.getInfoData()));

        WirelessComputationPacket wirelessComputationPacket = WirelessComputationPacket
            .getPacketByUserId(getBaseMetaTileEntity().getOwnerUuid());
        data.add(StatCollector.translateToLocal("tt.infodata.qc.wireless_mode.enabled"));
        data.add(
            StatCollector.translateToLocalFormatted(
                "tt.infodata.qc.total_wireless_computation",
                "" + EnumChatFormatting.YELLOW + wirelessComputationPacket.getAvailableComputationStored()));

        return data.toArray(new String[] {});
    }

    private enum RackHatchElement implements IHatchElement<MTEQuantumComputer> {

        INSTANCE;

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(MTEHatchRack.class);
        }

        @Override
        public IGTHatchAdder<? super MTEQuantumComputer> adder() {
            return MTEQuantumComputer::addRackToMachineList;
        }

        @Override
        public long count(MTEQuantumComputer t) {
            return t.eRacks.size();
        }
    }

    private enum WirelessComputationHatchElement implements IHatchElement<MTEQuantumComputer> {

        INSTANCE;

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(MTEHatchWirelessComputationOutput.class);
        }

        @Override
        public IGTHatchAdder<? super MTEQuantumComputer> adder() {
            return MTEQuantumComputer::addWirelessDataOutputToMachineList;
        }

        @Override
        public long count(MTEQuantumComputer gtMetaTileEntityEmComputer) {
            return gtMetaTileEntityEmComputer.eWirelessComputationOutputs.size();
        }
    }
}

package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GTStructureUtility.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.ILayerProducer;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.MTELayerSignal;

public class MTECuringMachine extends MTEExtendedPowerMultiBlockBase<MTECuringMachine>
    implements ISurvivalConstructable, ILayerProducer {

    private static IStructureDefinition<MTECuringMachine> STRUCTURE_DEFINITION = null;
    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final int OFFSET_X = 2;
    private static final int OFFSET_Y = 4;
    private static final int OFFSET_Z = 0;

    private static final int PARALLEL_PER_TIER = 4;
    private static final float SPEED = 1f;
    private static final float EU_EFFICIENCY = 1f;

    public MTECuringMachine(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTECuringMachine(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTECuringMachine(this.mName);
    }

    @Override
    public IStructureDefinition<MTECuringMachine> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTECuringMachine>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    // spotless:off
                    new String[][]{{
                        "     ",
                        " EEE ",
                        "     ",
                        "     ",
                        " E~E ",
                        " EEE "
                    },{
                        " EEE ",
                        "EFFFE",
                        " AAA ",
                        " AAA ",
                        "EGGGE",
                        "EEEEE"
                    },{
                        "EEEEE",
                        "EFDFE",
                        " A A ",
                        " A A ",
                        "EGCGE",
                        "EEEEE"
                    },{
                        "EEEEE",
                        "EFFFE",
                        " AAA ",
                        "EAAAE",
                        "EGGGE",
                        "EEEEE"
                    },{
                        "EEEEE",
                        "E   E",
                        "EBBBE",
                        "EBBBE",
                        "E   E",
                        "EEEEE"
                    },{
                        "EEEEE",
                        "EE EE",
                        "H B H",
                        "H B H",
                        "EE EE",
                        "EEEEE"
                    },{
                        "EEEEE",
                        "E   E",
                        "EB BE",
                        "EB BE",
                        "E E E",
                        "EEEEE"
                    },{
                        " EEE ",
                        " EEE ",
                        " EHE ",
                        " EHE ",
                        " EEE ",
                        " EEE "
                    }})
                // spotless:on
                .addElement('A', chainAllGlasses())
                .addElement('B', Casings.SolidifierRadiator.asElement())
                .addElement('C', Casings.FluxedElectrumItemPipeCasing.asElement())
                .addElement('D', Casings.TungstensteelPipeCasing.asElement())
                .addElement(
                    'E',
                    buildHatchAdder(MTECuringMachine.class)
                        .atLeast(
                            InputBus,
                            OutputBus,
                            Maintenance,
                            Energy,
                            InputHatch,
                            MTELayerSignal.LayerSignalHatchElement.LayerSignal)
                        .casingIndex(Casings.RadiantNaquadahAlloyCasing.textureId)
                        .hint(1)
                        .buildAndChain(
                            onElementPass(
                                MTECuringMachine::onCasingAdded,
                                Casings.RadiantNaquadahAlloyCasing.asElement())))
                .addElement('F', Casings.HighEnergyUltravioletEmitterCasing.asElement())
                .addElement('G', Casings.UVSolenoidSuperconductorCoil.asElement())
                .addElement('H', ofFrame(Materials.Infinity))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    private final ArrayList<MTELayerSignal> signalHatches = new ArrayList<>();

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Casings.RadiantNaquadahAlloyCasing.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_MULTI_BREWERY_ACTIVE)
                    .extFacing()
                    .build() };
            return new ITexture[] { Casings.RadiantNaquadahAlloyCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_MULTI_BREWERY)
                .extFacing()
                .build() };
        }
        return new ITexture[] { Casings.RadiantNaquadahAlloyCasing.getCasingTexture() };
    }

    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Curing Machine")
            .addBulkMachineInfo(PARALLEL_PER_TIER, SPEED, EU_EFFICIENCY)
            .beginStructureBlock(5, 6, 8, false)
            .addController("Front center, 2nd layer")
            .addCasing("6-121", "Radiant Naquadah Alloy Casing", false)
            .addCasing("16", "Any Tiered Glass", false)
            .addCasing("6", "Infinity Frame Box", false)
            .addCasing("1", "Fluxed Electrum Item Pipe Casing", false)
            .addCasing("1", "Tungstensteel Pipe Casing", false)
            .addCasing("8", "High Energy Ultraviolet Emitter Casing", false)
            .addCasing("8", "UV Solenoid Superconductor Coil", false)
            .addCasing("12", "Solidifier Radiator", false)
            .addMiscHatch(
                "0+",
                StatCollector.translateToLocal("GT5U.tooltip.structure.layer_signal_hatch"),
                "Any casing",
                1)
            .addEnergyHatch("1+", "Any casing", 1)
            .addMaintenanceHatch("1", "Any casing", 1)
            .addInputBus("1+", "Any casing", 1)
            .addInputHatch("1+", "Any casing", 1)
            .addOutputBus("1+", "Any casing", 1)
            .addStructureInfo("")
            .addSubChannel(GTStructureChannels.BOROGLASS)
            .toolTipFinisher();
        return tt;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setEuModifier(EU_EFFICIENCY)
            .setSpeedBonus(1F / SPEED)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (PARALLEL_PER_TIER * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    private int casingAmount;

    private void onCasingAdded() {
        casingAmount++;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_X,
            OFFSET_Y,
            OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        casingAmount = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, errors)) return;
        checkCasingMin(errors, casingAmount, 6);
        checkHasEnergyHatch(errors);
        checkHasMaintenanceHatch(errors);
        checkHasInputBus(errors);
        checkHasInputHatch(errors);
        checkHasOutputBus(errors);
    }

    @Override
    public List<MTELayerSignal> getLayerSignalHatches() {
        return Collections.unmodifiableList(signalHatches);
    }

    @Override
    public boolean addLayerSignalHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null && aTileEntity.getMetaTileEntity() instanceof MTELayerSignal sensor) {
            sensor.updateTexture(aBaseCasingIndex);
            return signalHatches.add(sensor);
        }
        return false;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.curingMachineRecipes;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }
}

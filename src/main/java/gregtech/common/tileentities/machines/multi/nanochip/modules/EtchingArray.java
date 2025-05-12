package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.NAC_MODULE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_CC;

import java.util.ArrayList;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings4;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;
import gtnhlanth.common.beamline.BeamInformation;
import gtnhlanth.common.beamline.Particle;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;

public class EtchingArray extends MTENanochipAssemblyModuleBase<EtchingArray> {

    protected static final int STRUCTURE_OFFSET_X = 3;
    protected static final int STRUCTURE_OFFSET_Y = 3;
    protected static final int STRUCTURE_OFFSET_Z = -2;

    private final ArrayList<MTEHatchInputBeamline> mInputBeamline = new ArrayList<>();

    protected static final String STRUCTURE_PIECE_MAIN = "main";

    private static final String[][] structure = new String[][] { { "  AAA  ", "  ABA  ", "  AAA  " },
        { "  AAA  ", "  A A  ", "  AAA  " }, { "  AAA  ", "  AAA  ", "  AAA  " } };

    public static final IStructureDefinition<EtchingArray> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<EtchingArray>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        .addElement('A', ofBlock(GregTechAPI.sBlockCasings4, 0))
        .addElement(
            'B',
            buildHatchAdder(EtchingArray.class).hatchClass(MTEHatchInputBeamline.class)
                .casingIndex(((BlockCasings4) GregTechAPI.sBlockCasings4).getTextureIndex(0))
                .dot(5)
                .adder(EtchingArray::addBeamLineInputHatch)
                .build())
        .build();

    private boolean addBeamLineInputHatch(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;

        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;

        if (mte instanceof MTEHatchInputBeamline) {
            return this.mInputBeamline.add((MTEHatchInputBeamline) mte);
        }

        return false;
    }

    @Nullable
    private BeamInformation getInputInformation() {
        for (MTEHatchInputBeamline in : this.mInputBeamline) {
            if (in.dataPacket == null) return new BeamInformation(0, 0, 0, 0);
            return in.dataPacket.getContent();
        }
        return null;
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {

        BeamInformation inputInfo = this.getInputInformation();
        if (inputInfo == null) return CheckRecipeResultRegistry.NO_RECIPE;

        float inputEnergy = inputInfo.getEnergy();
        Particle inputParticle = Particle.getParticleFromId(inputInfo.getParticleId());

        if (inputEnergy <= 1234) return CheckRecipeResultRegistry.NO_RECIPE;
        if (inputParticle != Particle.getParticleFromId(0)) return CheckRecipeResultRegistry.NO_RECIPE;

        return CheckRecipeResultRegistry.SUCCESSFUL;

    }

    public EtchingArray(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected EtchingArray(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<EtchingArray> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        buildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            hintsOnly,
            STRUCTURE_OFFSET_X,
            STRUCTURE_OFFSET_Y,
            STRUCTURE_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            STRUCTURE_OFFSET_X,
            STRUCTURE_OFFSET_Y,
            STRUCTURE_OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // Check base structure
        if (!super.checkMachine(aBaseMetaTileEntity, aStack)) return false;
        // Now check module structure
        return checkPiece(STRUCTURE_PIECE_MAIN, STRUCTURE_OFFSET_X, STRUCTURE_OFFSET_Y, STRUCTURE_OFFSET_Z);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addInfo(NAC_MODULE)
            .addInfo("Etches your Chip " + TOOLTIP_CC + "s")
            .addInfo("Outputs into the VCO with the same color as the input VCI")
            .addStructureInfo("Any base casing - Vacuum Conveyor Input")
            .addStructureInfo("Any base casing - Vacuum Conveyor Output")
            .toolTipFinisher("GregTech");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new EtchingArray(this.mName);
    }

    public static void registerLocalName(String unprocessedName, CircuitComponent component) {
        component.fallbackLocalizedName = "Etched " + unprocessedName;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipEtchingArray;
    }
}

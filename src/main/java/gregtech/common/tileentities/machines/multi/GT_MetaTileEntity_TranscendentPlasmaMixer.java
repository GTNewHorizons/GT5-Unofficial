package gregtech.common.tileentities.machines.multi;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IGlobalWirelessEnergy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Recipe;
import net.minecraft.item.ItemStack;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import net.minecraftforge.fluids.FluidStack;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofCoil;
import static gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_PlasmaForge.*;

public class GT_MetaTileEntity_TranscendentPlasmaMixer extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_TranscendentPlasmaMixer> implements IGlobalWirelessEnergy {

    private static final String[][] structure = new String[][]{{
        " CAC ",
        " ABA ",
        " ABA ",
        " A~A ",
        " ABA ",
        " ABA ",
        " CAC "
    },{
        "CBBBC",
        "A   A",
        "A   A",
        "A   A",
        "A   A",
        "A   A",
        "CBBBC"
    },{
        "ABBBA",
        "B   B",
        "B   B",
        "B   B",
        "B   B",
        "B   B",
        "ABBBA"
    },{
        "CBBBC",
        "A   A",
        "A   A",
        "A   A",
        "A   A",
        "A   A",
        "CBBBC"
    },{
        " CAC ",
        " ABA ",
        " ABA ",
        " ABA ",
        " ABA ",
        " ABA ",
        " CAC "
    }};

    private static final String STRUCTURE_PIECE_MAIN = "MAIN";
    private static final IStructureDefinition<GT_MetaTileEntity_TranscendentPlasmaMixer> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_TranscendentPlasmaMixer>builder().addShape(STRUCTURE_PIECE_MAIN, structure)
        .addElement(
            'B',
            buildHatchAdder(GT_MetaTileEntity_TranscendentPlasmaMixer.class)
                .atLeast(InputHatch, OutputHatch, InputBus, Maintenance)
                .casingIndex(DIM_INJECTION_CASING).dot(1)
                .buildAndChain(GregTech_API.sBlockCasings1, DIM_INJECTION_CASING))
        .addElement('A', ofBlock(GregTech_API.sBlockCasings1, DIM_TRANS_CASING))
        .addElement('C', ofBlock(GregTech_API.sBlockCasings1, DIM_BRIDGE_CASING)).build();


    private static final long crudeEUPerL = 14_514_983;
    private static final long prosaicEUPerL = 66_768_460;
    private static final long transcendentEUPerL = 269_326_451;
    private static final long exoticEUPerL = 1_073_007_393;

    private String ownerUUID;

    public GT_MetaTileEntity_TranscendentPlasmaMixer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_TranscendentPlasmaMixer(String aName) {
        super(aName);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {

    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_TranscendentPlasmaMixer> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addInfo("Transcending Dimensional Boundaries.");
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_TranscendentPlasmaMixer(mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive) return new ITexture[] { casingTexturePages[0][DIM_BRIDGE_CASING],
                TextureFactory.builder().addIcon(OVERLAY_DTPF_ON).extFacing().build(),
                TextureFactory.builder().addIcon(OVERLAY_FUSION1_GLOW).extFacing().glow().build() };
            return new ITexture[] { casingTexturePages[0][DIM_BRIDGE_CASING],
                TextureFactory.builder().addIcon(OVERLAY_DTPF_OFF).extFacing().build() };
        }
        return new ITexture[] { casingTexturePages[0][DIM_BRIDGE_CASING] };
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return false;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        return processRecipe(getCompactedInputs(), getCompactedFluids());
    }

    boolean processRecipe(ItemStack[] items, FluidStack[] fluids) {

        GT_Recipe gtRecipe = GT_Recipe.GT_Recipe_Map.sTranscendentPlasmaMixerRecipes
            .findRecipe(getBaseMetaTileEntity(), false, Long.MAX_VALUE, fluids, items);

        if (gtRecipe == null) {
            return false;
        }

        if (!addEUToGlobalEnergyMap(ownerUUID, 1)) {
            return false;
        }

        mMaxProgresstime = 1000;

        // Output items/fluids.
        mOutputItems = gtRecipe.mOutputs.clone();
        mOutputFluids = gtRecipe.mFluidOutputs.clone();
        updateSlots();

        return true;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {

        // Check the main structure
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 0, 0, -1)) {
            return false;
        }

        if (mMaintenanceHatches.size() != 1) {
            return false;
        }

        return false;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }


    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {

        super.onPreTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide() && (aTick == 1)) {
            // Adds player to the wireless network if they do not already exist on it.
            ownerUUID = processInitialSettings(aBaseMetaTileEntity);
        }
    }
}

package gregtech.common.tileentities.machines.multi;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IGlobalWirelessEnergy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Recipe;
import gregtech.common.items.GT_IntegratedCircuit_Item;
import net.minecraft.item.ItemStack;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import net.minecraftforge.fluids.FluidStack;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_Values.AuthorColen;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_PlasmaForge.*;
import static java.lang.Math.max;
import static net.minecraft.util.EnumChatFormatting.GOLD;
import static net.minecraft.util.EnumChatFormatting.GRAY;

public class GT_MetaTileEntity_TranscendentPlasmaMixer extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_TranscendentPlasmaMixer> implements IGlobalWirelessEnergy, ISurvivalConstructable {

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



    private String ownerUUID;

    public GT_MetaTileEntity_TranscendentPlasmaMixer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_TranscendentPlasmaMixer(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_TranscendentPlasmaMixer> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Transcendent Mixer").addInfo("Controller block for the Assembling Line")
            .addInfo("Assisting in all your DTPF needs.")
            .addInfo("This multiblock will run in parallel according to the circuit provided to the")
            .addInfo("controller slot. E.g. 3x Circuit #16 = 48x parallel. All inputs will scale,")
            .addInfo("except time. All EU is deducted from wireless EU networks only.")
            .addInfo(AuthorColen)
            .addSeparator().beginStructureBlock(5, 7, 5, false)
            .addStructureInfo(GOLD + "1+ " + GRAY + "Input Hatch")
            .addStructureInfo(GOLD + "1+ " + GRAY + "Output Hatch")
            .addStructureInfo(GOLD + "1+ " + GRAY + "Input Bus")
            .addStructureInfo(GOLD + "1 " + GRAY + "Maintenance Hatch")
            .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_TranscendentPlasmaMixer(mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive) return new ITexture[] { casingTexturePages[0][DIM_TRANS_CASING],
                TextureFactory.builder().addIcon(OVERLAY_DTPF_ON).extFacing().build(),
                TextureFactory.builder().addIcon(OVERLAY_FUSION1_GLOW).extFacing().glow().build() };
            return new ITexture[] { casingTexturePages[0][DIM_TRANS_CASING],
                TextureFactory.builder().addIcon(OVERLAY_DTPF_OFF).extFacing().build() };
        }

        return new ITexture[] { casingTexturePages[0][DIM_TRANS_CASING] };
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    int multiplier = 1;

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        if (aStack.getItem() instanceof GT_IntegratedCircuit_Item) {
            multiplier = max(1, aStack.getItemDamage());
        }

        return processRecipe(getCompactedInputs(), getCompactedFluids());
    }

    boolean processRecipe(ItemStack[] items, FluidStack[] fluids) {

        GT_Recipe originalRecipe = GT_Recipe.GT_Recipe_Map.sTranscendentPlasmaMixerRecipes
            .findRecipe(getBaseMetaTileEntity(), false, Long.MAX_VALUE, fluids, items);

        if (originalRecipe == null) {
            return false;
        }

        if (!addEUToGlobalEnergyMap(ownerUUID, 1000 * originalRecipe.mEUt * multiplier)) {
            return false;
        }

        // Fluid handling.
        {
            // Output items/fluids.
            GT_Recipe modifiedRecipe = originalRecipe.copy();

            // Multiply up the input plasmas.
            for (FluidStack fluidStack : modifiedRecipe.mFluidInputs) {
                fluidStack.amount *= multiplier;
            }

            // Multiply up the output fluid.
            modifiedRecipe.mFluidOutputs[0].amount *= multiplier;

            // Takes items/fluids from hatches/busses.
            if (!modifiedRecipe.isRecipeInputEqual(true, fluids, items)) return false;

            mOutputFluids = modifiedRecipe.mFluidOutputs;
            mOutputItems = modifiedRecipe.mOutputs;
        }

        mMaxProgresstime = 100;
        mEUt = 0;

        updateSlots();

        return true;
    }

    private static final int HORIZONTAL_OFFSET = 2;
    private static final int VERTICAL_OFFSET = 3;
    private static final int DEPTH_OFFSET = 0;

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET);
    }

    @Override
    public String[] getInfoData() {
        return new String[] {"test"};
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return new String[] {"gh","hio"};
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {

        // Check the main structure
        if (!checkPiece(STRUCTURE_PIECE_MAIN, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET)) {
            return false;
        }

        if (mMaintenanceHatches.size() != 1) {
            return false;
        }

        return true;
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

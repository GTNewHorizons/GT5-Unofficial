package gregtech.common.tileentities.machines.multi.purification;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.GT_Values.AuthorNotAPenguin;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_StructureUtility;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings_Abstract;

public class GT_MetaTileEntity_PurificationUnitPhAdjustment
    extends GT_MetaTileEntity_PurificationUnitBase<GT_MetaTileEntity_PurificationUnitPhAdjustment>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int STRUCTURE_X_OFFSET = 7;
    private static final int STRUCTURE_Y_OFFSET = 4;
    private static final int STRUCTURE_Z_OFFSET = 1;

    private static final String[][] structure = new String[][] {
        // spotless:off
        { "E   E     E   E", "EAAAE     ECCCE", "EAGAE     ECHCE", "EAGAE     ECHCE", "EAGAE     ECHCE", "EAAAE     ECCCE" },
        { " AAA       CCC ", "A   A     C   C", "A   A     C   C", "A   A     C   C", "A   ABB~BBC   C", "AAAAA     CCCCC" },
        { " AXA       CYC ", "A   A     C   C", "G   A     C   H", "G   ABBBBBC   H", "G             H", "AAAAAIIRIICCCCC" },
        { " AAA       CCC ", "A   A     C   C", "A   A     C   C", "A   A     C   C", "A   ABBBBBC   C", "AAAAA     CCCCC" },
        { "E   E     E   E", "EAAAE     ECCCE", "EAGAE     ECHCE", "EAGAE     ECHCE", "EAGAE     ECHCE", "EAAAE     ECCCE" } };
    // spotless:on

    private static final int CASING_INDEX_MIDDLE = getTextureIndex(GregTech_API.sBlockCasings8, 0);
    // Acidity tower -> input = NaOH (base)
    private static final int CASING_INDEX_ACID = getTextureIndex(GregTech_API.sBlockCasings4, 1);
    // Alkaninity tower -> input = HCl (acid)
    private static final int CASING_INDEX_BASE = getTextureIndex(GregTech_API.sBlockCasings8, 7);

    private static final IStructureDefinition<GT_MetaTileEntity_PurificationUnitPhAdjustment> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_PurificationUnitPhAdjustment>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        .addElement('A', ofBlock(GregTech_API.sBlockCasings4, 1))
        // PLACEHOLDER: Chemically inert machine casing
        .addElement('B', ofBlock(GregTech_API.sBlockCasings8, 0))
        .addElement('C', ofBlock(GregTech_API.sBlockCasings8, 7))
        .addElement('E', ofFrame(Materials.NaquadahAlloy))
        // PLACEHOLDER: Needs to become acid and alkaline resistant glass
        .addElement('G', ofBlock(Blocks.stained_glass, 7))
        .addElement('H', ofBlock(Blocks.stained_glass, 7))
        // Regular I/O hatches
        .addElement(
            'I',
            ofChain(
                lazy(
                    t -> GT_StructureUtility.<GT_MetaTileEntity_PurificationUnitPhAdjustment>buildHatchAdder()
                        .atLeastList(t.getAllowedHatches())
                        .dot(1)
                        .hint(() -> "Input Hatch, Output Hatch")
                        .casingIndex(CASING_INDEX_MIDDLE)
                        .disallowOnly(ForgeDirection.DOWN)
                        .build()),
                // PLACEHOLDER: Chemically inert machine casing
                ofBlock(GregTech_API.sBlockCasings8, 0)))
        // PLACEHOLDER: Redstone hatch - use dot 2
        .addElement('R', ofBlock(GregTech_API.sBlockCasings8, 0))
        // Special I/O hatches
        .addElement(
            'X',
            lazy(
                t -> GT_StructureUtility.<GT_MetaTileEntity_PurificationUnitPhAdjustment>buildHatchAdder()
                    .atLeast(InputBus)
                    .dot(3)
                    .hint(() -> "Input Bus (Sodium Hydroxide)")
                    .casingIndex(CASING_INDEX_ACID)
                    .allowOnly(ForgeDirection.UP)
                    .build()))
        .addElement(
            'Y',
            lazy(
                t -> GT_StructureUtility.<GT_MetaTileEntity_PurificationUnitPhAdjustment>buildHatchAdder()
                    .atLeast(InputHatch)
                    .dot(4)
                    .hint(() -> "Input Hatch (Hydrochloric Acid)")
                    .casingIndex(CASING_INDEX_BASE)
                    .allowOnly(ForgeDirection.UP)
                    .build()))
        .build();

    private static int getTextureIndex(Block block, int meta) {
        return ((GT_Block_Casings_Abstract) block).getTextureIndex(meta);
    }

    private List<IHatchElement<? super GT_MetaTileEntity_PurificationUnitPhAdjustment>> getAllowedHatches() {
        return ImmutableList.of(InputHatch, OutputHatch);
    }

    public GT_MetaTileEntity_PurificationUnitPhAdjustment(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_PurificationUnitPhAdjustment(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_PurificationUnitPhAdjustment(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[1][48] };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            hintsOnly,
            STRUCTURE_X_OFFSET,
            STRUCTURE_Y_OFFSET,
            STRUCTURE_Z_OFFSET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            STRUCTURE_X_OFFSET,
            STRUCTURE_Y_OFFSET,
            STRUCTURE_Z_OFFSET,
            elementBudget,
            env,
            true);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_PurificationUnitPhAdjustment> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Purification Unit")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.BOLD
                    + "Water Tier: "
                    + EnumChatFormatting.WHITE
                    + GT_Utility.formatNumbers(getWaterTier())
                    + EnumChatFormatting.RESET)
            .addInfo("Controller block for the pH Adjustment Purification Unit.")
            .addInfo("Must be linked to a Purification Plant to work.")
            .addSeparator()
            .addInfo(AuthorNotAPenguin)
            .beginStructureBlock(7, 4, 7, false)
            .addController("Front center")
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getWaterTier() {
        return 3;
    }

    @Override
    public long getActivePowerUsage() {
        return TierEU.RECIPE_ZPM;
    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, STRUCTURE_X_OFFSET, STRUCTURE_Y_OFFSET, STRUCTURE_Z_OFFSET)) return false;
        return super.checkMachine(aBaseMetaTileEntity, aStack);
    }
}

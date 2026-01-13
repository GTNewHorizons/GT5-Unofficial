package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BOARD_PROCESSOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BOARD_PROCESSOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BOARD_PROCESSOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BOARD_PROCESSOR_GLOW;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.CASING_INDEX_WHITE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.NAC_MODULE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_CC;
import static gtPlusPlus.xmod.thermalfoundation.block.TFBlocks.blockFluidEnder;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.MTEBoardProcessorModuleGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleTypes;
import gtPlusPlus.core.material.MaterialsAlloy;

public class MTEBoardProcessorModule extends MTENanochipAssemblyModuleBase<MTEBoardProcessorModule> {

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int BOARD_OFFSET_X = 3;
    protected static final int BOARD_OFFSET_Y = 4;
    protected static final int BOARD_OFFSET_Z = 0;
    protected static final String[][] BOARD_STRING = new String[][] { { "       ", " A   A ", " A   A ", " B   B " },
        { " A   A ", "CCC CCC", "DDD DDD", "DDD DDD" }, { "  A A  ", "CBCCCBC", "DEDDDED", "DEDDDED" },
        { "   A   ", "CBCCCBC", "DEDADED", "DEDADED" }, { "  A A  ", "CBCCCBC", "DEDDDED", "DEDDDED" },
        { " A   A ", "CCC CCC", "DDD DDD", "DDD DDD" }, { "       ", " A   A ", " A   A ", " B   B " } };
    public static final IStructureDefinition<MTEBoardProcessorModule> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<MTEBoardProcessorModule>builder()
        .addShape(STRUCTURE_PIECE_MAIN, BOARD_STRING)
        // Octiron frame
        .addElement(
            'A',
            lazy(
                t -> ofBlock(
                    Block.getBlockFromItem(
                        MaterialsAlloy.OCTIRON.getFrameBox(1)
                            .getItem()),
                    MaterialsAlloy.OCTIRON.getFrameBox(1)
                        .getItemDamage())))
        // Nanochip Primary Casing
        .addElement('B', Casings.NanochipPrimaryCasing.asElement())
        // Nanochip Secondary Casing
        .addElement('C', Casings.NanochipSecondaryCasing.asElement())
        // Nanochip Glass
        .addElement('D', Casings.NanochipGlass.asElement())
        // Source block of flowing ender
        .addElement('E', lazy(unused -> ofBlock(blockFluidEnder, 0)))
        .build();

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_BOARD_PROCESSOR_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_BOARD_PROCESSOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_BOARD_PROCESSOR)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_BOARD_PROCESSOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE) };
    }

    public MTEBoardProcessorModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEBoardProcessorModule(String aName) {
        super(aName);
    }

    @Override
    public ModuleTypes getModuleType() {
        return ModuleTypes.BoardProcessor;
    }

    @Override
    public IStructureDefinition<MTEBoardProcessorModule> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        buildPiece(STRUCTURE_PIECE_MAIN, trigger, hintsOnly, BOARD_OFFSET_X, BOARD_OFFSET_Y, BOARD_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            BOARD_OFFSET_X,
            BOARD_OFFSET_Y,
            BOARD_OFFSET_Z,
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
        return checkPiece(STRUCTURE_PIECE_MAIN, BOARD_OFFSET_X, BOARD_OFFSET_Y, BOARD_OFFSET_Z);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType("NAC Module")
            .addInfo(NAC_MODULE)
            .addInfo("Processes your Board " + TOOLTIP_CC + "s")
            .addInfo("Outputs into the VCO with the same color as the input VCI")
            .addStructureInfo("Any base casing - Vacuum Conveyor Input")
            .addStructureInfo("Any base casing - Input Hatch")
            .addStructureInfo("Any base casing - Vacuum Conveyor Output")
            .toolTipFinisher("GregTech");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBoardProcessorModule(this.mName);
    }

    public static void registerLocalName(String unprocessedName, CircuitComponent component) {
        component.fallbackLocalizedName = unprocessedName + " Die";
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipBoardProcessorRecipes;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui getGui() {
        return new MTEBoardProcessorModuleGui(this);
    }

}

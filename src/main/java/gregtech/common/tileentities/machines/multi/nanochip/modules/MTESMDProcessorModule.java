package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SMD_PROCESSOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SMD_PROCESSOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SMD_PROCESSOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SMD_PROCESSOR_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.gui.modularui.multiblock.MTENanochipAssemblyComplexGui.colorString;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.CASING_INDEX_WHITE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.NAC_MODULE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_CC;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_CCs;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_VCI;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_VCO;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleTypes;

public class MTESMDProcessorModule extends MTENanochipAssemblyModuleBase<MTESMDProcessorModule> {

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int SMD_OFFSET_X = 3;
    protected static final int SMD_OFFSET_Y = 3;
    protected static final int SMD_OFFSET_Z = 0;
    protected static final String[][] SMD_STRING = new String[][] { { " B   B ", " EA AE ", " BB BB " },
        { "BBDDDBB", "ECA ACE", "BBB BBB" }, { " D   D ", "AAA AAA", "BBBDBBB" }, { " D   D ", "       ", "  DDD  " },
        { " D   D ", "AAA AAA", "BBBDBBB" }, { "BBDDDBB", "ECA ACE", "BBB BBB" }, { " B   B ", " EA AE ", " BB BB " } };
    public static final IStructureDefinition<MTESMDProcessorModule> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<MTESMDProcessorModule>builder()
        .addShape(STRUCTURE_PIECE_MAIN, SMD_STRING)
        // Nanochip Primary Casing
        .addElement('A', Casings.NanochipPrimaryCasing.asElement())
        // Nanochip Secondary Casing
        .addElement('B', Casings.NanochipSecondaryCasing.asElement())
        // UEV Machine Casings
        .addElement('C', ofBlock(GregTechAPI.sBlockCasingsNH, 10))
        // Radox polymer frame
        .addElement('D', ofFrame(Materials.RadoxPolymer))
        // Nanochip Glass
        .addElement('E', Casings.NanochipGlass.asElement())
        .build();

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SMD_PROCESSOR_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SMD_PROCESSOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SMD_PROCESSOR)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SMD_PROCESSOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE) };
    }

    public MTESMDProcessorModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTESMDProcessorModule(String aName) {
        super(aName);
    }

    @Override
    public ModuleTypes getModuleType() {
        return ModuleTypes.SMDProcessor;
    }

    @Override
    public IStructureDefinition<MTESMDProcessorModule> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        buildPiece(STRUCTURE_PIECE_MAIN, trigger, hintsOnly, SMD_OFFSET_X, SMD_OFFSET_Y, SMD_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            SMD_OFFSET_X,
            SMD_OFFSET_Y,
            SMD_OFFSET_Z,
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
        return checkPiece(STRUCTURE_PIECE_MAIN, SMD_OFFSET_X, SMD_OFFSET_Y, SMD_OFFSET_Z);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType("NAC Module")
            .addInfo(NAC_MODULE)
            .addSeparator()
            .addInfo("Processes your SMD " + TOOLTIP_CCs)
            .addInfo("Outputs are placed in the " + TOOLTIP_VCO + " with the same " + colorString() + " as the input " + TOOLTIP_VCI)
            .addInfo("Has " + EnumChatFormatting.WHITE + EnumChatFormatting.UNDERLINE + "unlimited parallel")
            .addSeparator()
            .addInfo(
                EnumChatFormatting.LIGHT_PURPLE + ""
                    + EnumChatFormatting.ITALIC
                    + "Processing the core logical components of circuits requires a lot of thought")
            .addStructureInfo("Any base casing - Vacuum Conveyor Input")
            .addStructureInfo("Any base casing - Vacuum Conveyor Output")
            .toolTipFinisher("GregTech");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESMDProcessorModule(this.mName);
    }

    public static void registerLocalName(String unprocessedName, CircuitComponent component) {
        // Processed SMDs can be given a name like 'SMD Inductor Tray'
        component.fallbackLocalizedName = unprocessedName + " Tray";
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipSMDProcessorRecipes;
    }
}

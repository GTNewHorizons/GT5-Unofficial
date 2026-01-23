package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENCASEMENT_WRAPPER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENCASEMENT_WRAPPER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENCASEMENT_WRAPPER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENCASEMENT_WRAPPER_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.CASING_INDEX_WHITE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.NAC_MODULE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_CCs;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_VCI;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_VCO;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

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

public class MTEEncasementWrapperModule extends MTENanochipAssemblyModuleBase<MTEEncasementWrapperModule> {

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int ENCASEMENT_WRAPPER_OFFSET_X = 3;
    protected static final int ENCASEMENT_WRAPPER_OFFSET_Y = 6;
    protected static final int ENCASEMENT_WRAPPER_OFFSET_Z = 0;
    protected static final String[][] ENCASEMENT_WRAPPER_STRUCTURE_STRING = new String[][] {
        { "       ", "  DBD  ", "  DBD  ", " CDBDC ", " CDBDC ", " CDBDC " },
        { "  BBB  ", " A   A ", " A C A ", "CA   AC", "CA C AC", "CA   AC" },
        { " BAAAB ", "D     D", "D AAA D", "D     D", "D AAA D", "D     D" },
        { " BAAAB ", "B     B", "BCAAACB", "B     B", "BCAAACB", "B     B" },
        { " BAAAB ", "D     D", "D AAA D", "D     D", "D AAA D", "D     D" },
        { "  BBB  ", " A   A ", " A C A ", "CA   AC", "CA C AC", "CA   AC" },
        { "       ", "  DBD  ", "  DBD  ", " CDBDC ", " CDBDC ", " CDBDC " } };
    public static final IStructureDefinition<MTEEncasementWrapperModule> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<MTEEncasementWrapperModule>builder()
        .addShape(STRUCTURE_PIECE_MAIN, ENCASEMENT_WRAPPER_STRUCTURE_STRING)
        // Nanochip Primary Casing
        .addElement('A', Casings.NanochipPrimaryCasing.asElement())
        // Nanochip Secondary Casing
        .addElement('B', Casings.NanochipSecondaryCasing.asElement())
        // Quantium Frame Box
        .addElement('C', ofFrame(Materials.Quantium))
        // Nanochip Glass
        .addElement('D', Casings.NanochipGlass.asElement())
        .build();

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ENCASEMENT_WRAPPER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ENCASEMENT_WRAPPER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ENCASEMENT_WRAPPER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ENCASEMENT_WRAPPER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE) };
    }

    public MTEEncasementWrapperModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEEncasementWrapperModule(String aName) {
        super(aName);
    }

    @Override
    public ModuleTypes getModuleType() {
        return ModuleTypes.EncasementWrapper;
    }

    @Override
    public IStructureDefinition<MTEEncasementWrapperModule> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        buildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            hintsOnly,
            ENCASEMENT_WRAPPER_OFFSET_X,
            ENCASEMENT_WRAPPER_OFFSET_Y,
            ENCASEMENT_WRAPPER_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            ENCASEMENT_WRAPPER_OFFSET_X,
            ENCASEMENT_WRAPPER_OFFSET_Y,
            ENCASEMENT_WRAPPER_OFFSET_Z,
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
        return checkPiece(
            STRUCTURE_PIECE_MAIN,
            ENCASEMENT_WRAPPER_OFFSET_X,
            ENCASEMENT_WRAPPER_OFFSET_Y,
            ENCASEMENT_WRAPPER_OFFSET_Z);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType("NAC Module")
            .addInfo(NAC_MODULE)
            .addSeparator()
            .addInfo("Able to process sheet and framebox " + TOOLTIP_CCs + " into wrapper spools and circuit casings.")
            .addInfo("Outputs are placed in the " + TOOLTIP_VCO + " with the same color as the input " + TOOLTIP_VCI)
            .addSeparator()
            .addInfo(
                EnumChatFormatting.LIGHT_PURPLE + ""
                    + EnumChatFormatting.ITALIC
                    + "Constructing strong casings for your circuits since 1849.")
            .addStructureInfo("Any base casing - Vacuum Conveyor Input")
            .addStructureInfo("Any base casing - Vacuum Conveyor Output")
            .toolTipFinisher("GregTech");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEEncasementWrapperModule(this.mName);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipEncasementWrapper;
    }

}

package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SHEET_SUPERVISOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SHEET_SUPERVISOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SHEET_SUPERVISOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SHEET_SUPERVISOR_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.CASING_INDEX_WHITE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.NAC_MODULE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_CC;

import net.minecraft.item.ItemStack;
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

public class MTESheetSupervisorModule extends MTENanochipAssemblyModuleBase<MTESheetSupervisorModule> {

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int SHEET_OFFSET_X = 3;
    protected static final int SHEET_OFFSET_Y = 6;
    protected static final int SHEET_OFFSET_Z = 0;
    protected static final String[][] SHEET_STRING = new String[][] {
        { "       ", "  DBD  ", "  DBD  ", " CDBDC ", " CDBDC ", " CDBDC " },
        { "  BBB  ", " A   A ", " A C A ", "CA   AC", "CA C AC", "CA   AC" },
        { " BAAAB ", "D     D", "D AAA D", "D     D", "D AAA D", "D     D" },
        { " BAAAB ", "B     B", "BCAAACB", "B     B", "BCAAACB", "B     B" },
        { " BAAAB ", "D     D", "D AAA D", "D     D", "D AAA D", "D     D" },
        { "  BBB  ", " A   A ", " A C A ", "CA   AC", "CA C AC", "CA   AC" },
        { "       ", "  DBD  ", "  DBD  ", " CDBDC ", " CDBDC ", " CDBDC " } };
    public static final IStructureDefinition<MTESheetSupervisorModule> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<MTESheetSupervisorModule>builder()
        .addShape(STRUCTURE_PIECE_MAIN, SHEET_STRING)
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
                    .addIcon(OVERLAY_FRONT_SHEET_SUPERVISOR_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SHEET_SUPERVISOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SHEET_SUPERVISOR)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SHEET_SUPERVISOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE) };
    }

    public MTESheetSupervisorModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTESheetSupervisorModule(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTESheetSupervisorModule> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        buildPiece(STRUCTURE_PIECE_MAIN, trigger, hintsOnly, SHEET_OFFSET_X, SHEET_OFFSET_Y, SHEET_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            SHEET_OFFSET_X,
            SHEET_OFFSET_Y,
            SHEET_OFFSET_Z,
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
        return checkPiece(STRUCTURE_PIECE_MAIN, SHEET_OFFSET_X, SHEET_OFFSET_Y, SHEET_OFFSET_Z);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType("NAC Module")
            .addInfo(NAC_MODULE)
            .addInfo("Atomically Supervises your Sheet " + TOOLTIP_CC + "s")
            .addInfo("Outputs into the VCO with the same color as the input VCI")
            .addStructureInfo("Any base casing - Vacuum Conveyor Input")
            .addStructureInfo("Any base casing - Vacuum Conveyor Output")
            .toolTipFinisher("GregTech");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESheetSupervisorModule(this.mName);
    }

    public static void registerLocalName(String unprocessedName, CircuitComponent component) {
        component.fallbackLocalizedName = "Observed " + unprocessedName;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipSheetSupervisor;
    }

}

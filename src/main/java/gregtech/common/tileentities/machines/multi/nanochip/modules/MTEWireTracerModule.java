package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_WIRE_TRACER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_WIRE_TRACER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_WIRE_TRACER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_WIRE_TRACER_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTStructureUtility.ofSheetMetal;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.CASING_INDEX_WHITE;
import static net.minecraft.util.StatCollector.translateToLocal;

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
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleTypes;

public class MTEWireTracerModule extends MTENanochipAssemblyModuleBase<MTEWireTracerModule> {

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int WIRE_OFFSET_X = 3;
    protected static final int WIRE_OFFSET_Y = 5;
    protected static final int WIRE_OFFSET_Z = 0;
    protected static final String[][] WIRE_STRING = new String[][] {
        { "       ", "  BBB  ", "  BAB  ", "  BAB  ", " BBABB " },
        { " DAAAD ", " E D E ", " E A E ", " E   E ", "BEDDDEB" },
        { " AEEEA ", "B     B", "B     B", "B     B", "BD   DB" },
        { " AEEEA ", "BD   DB", "AA C AA", "A  B  A", "AD B DA" },
        { " AEEEA ", "B     B", "B     B", "B     B", "BD   DB" },
        { " DAAAD ", " E D E ", " E A E ", " E   E ", "BEDDDEB" },
        { "       ", "  BBB  ", "  BAB  ", "  BAB  ", " BBABB " } };
    public static final IStructureDefinition<MTEWireTracerModule> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<MTEWireTracerModule>builder()
        .addShape(STRUCTURE_PIECE_MAIN, WIRE_STRING)
        // Nanochip Mesh Interface Casing
        .addElement('A', Casings.NanochipMeshInterfaceCasing.asElement())
        // Nanochip Reinforcement Casing
        .addElement('B', Casings.NanochipReinforcementCasing.asElement())
        // Superconductor UEV Base Sheetmetal
        .addElement('C', ofSheetMetal(Materials.SuperconductorUEVBase))
        // Superconductor UHV Base Framebox
        .addElement('D', ofFrame(Materials.SuperconductorUHVBase))
        // Nanochip Glass
        .addElement('E', Casings.NanochipComplexGlass.asElement())
        .build();

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_WIRE_TRACER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_WIRE_TRACER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_WIRE_TRACER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_WIRE_TRACER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_WIRE_TRACER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE) };
    }

    public MTEWireTracerModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEWireTracerModule(String aName) {
        super(aName);
    }

    @Override
    public ModuleTypes getModuleType() {
        return ModuleTypes.WireTracer;
    }

    @Override
    public IStructureDefinition<MTEWireTracerModule> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        buildPiece(STRUCTURE_PIECE_MAIN, trigger, hintsOnly, WIRE_OFFSET_X, WIRE_OFFSET_Y, WIRE_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            WIRE_OFFSET_X,
            WIRE_OFFSET_Y,
            WIRE_OFFSET_Z,
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
        return checkPiece(STRUCTURE_PIECE_MAIN, WIRE_OFFSET_X, WIRE_OFFSET_Y, WIRE_OFFSET_Z);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType(machineInfoText("Wire Tracer"))
            .addInfo(TOOLTIP_MODULE_DESCRIPTION)
            .addSeparator()
            .addInfo("Traces your Wire " + TOOLTIP_CCs)
            .addInfo(TOOLTIP_COLOR_MATCH_VCS)
            .addInfo(TOOLTIP_INFINITE_PARALLEL)
            .addSeparator()
            .addInfo(tooltipFlavorText(translateToLocal("GT5U.tooltip.nac.module.wire_tracer.flavor.1")))
            .addStructureInfo(TOOLTIP_STRUCTURE_BASE_VCI)
            .addStructureInfo(TOOLTIP_STRUCTURE_BASE_VCO)
            .toolTipFinisher();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEWireTracerModule(this.mName);
    }

    public static void registerLocalName(String unprocessedName, CircuitComponent component) {
        component.fallbackLocalizedName = "Traced " + unprocessedName;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipWireTracer;
    }
}

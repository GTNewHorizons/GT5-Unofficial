package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_WIRE_TRACER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_WIRE_TRACER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_WIRE_TRACER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_WIRE_TRACER_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTStructureUtility.ofSheetMetal;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
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
        return createNanochipModuleTextures(
            side,
            aFacing,
            aActive,
            OVERLAY_FRONT_WIRE_TRACER,
            OVERLAY_FRONT_WIRE_TRACER_GLOW,
            OVERLAY_FRONT_WIRE_TRACER_ACTIVE,
            OVERLAY_FRONT_WIRE_TRACER_ACTIVE_GLOW);
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
    public int structureOffsetX() {
        return WIRE_OFFSET_X;
    }

    @Override
    public int structureOffsetY() {
        return WIRE_OFFSET_Y;
    }

    @Override
    public int structureOffsetZ() {
        return WIRE_OFFSET_Z;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType(getModuleType().getMachineModeText())
            .addInfo(TOOLTIP_MODULE_DESCRIPTION)
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.module.wire_tracer.action", TOOLTIP_CCs))
            .addSeparator()
            .addInfo(tooltipFlavorText(translateToLocal("GT5U.tooltip.nac.module.wire_tracer.flavor.1")))
            .beginStructureBlock(7, 8, 7, false)
            .addController(translateToLocal("GT5U.tooltip.nac.interface.structure.module_controller"))
            // Nanochip Reinforcement Casing
            .addCasing("46", translateToLocal("gt.blockcasings12.2.name"), false)
            // Nanochip Mesh Interface Casing
            .addCasing("28", translateToLocal("gt.blockcasings12.1.name"), false)
            // Nanochip Complex Glass
            .addCasing("25", translateToLocal("gt.blockglass1.8.name"), false)
            // Superconductor Base UHV Frame Box
            .addCasing("20", "Superconductor Base UHV Frame Box", false)
            // Superconductor Base UEV Sheetmetal
            .addCasing("1", OrePrefixes.sheetmetal.getDefaultLocalNameForItem(Materials.SuperconductorUEVBase), false)
            .addMiscHatch(
                "0+",
                TOOLTIP_VCI_LONG,
                translateToLocal("GT5U.tooltip.nac.interface.structure.module_hatches"),
                3)
            .addMiscHatch(
                "0+",
                TOOLTIP_VCO_LONG,
                translateToLocal("GT5U.tooltip.nac.interface.structure.module_hatches"),
                3)
            .addStructureInfo("")
            .addStructureFooter(translateToLocal("GT5U.tooltip.nac.interface.structure.module_cost"))
            .addStructureFooter(translateToLocal("GT5U.tooltip.nac.interface.structure.module_power"))
            .toolTipFinisher();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEWireTracerModule(this.mName);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipWireTracer;
    }
}

package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENCASEMENT_WRAPPER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENCASEMENT_WRAPPER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENCASEMENT_WRAPPER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENCASEMENT_WRAPPER_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
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
        // Nanochip Mesh Interface Casing
        .addElement('A', Casings.NanochipMeshInterfaceCasing.asElement())
        // Nanochip Reinforcement Casing
        .addElement('B', Casings.NanochipReinforcementCasing.asElement())
        // Quantium Frame Box
        .addElement('C', ofFrame(Materials.Quantium))
        // Nanochip Glass
        .addElement('D', Casings.NanochipComplexGlass.asElement())
        .build();

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return createNanochipModuleTextures(
            side,
            aFacing,
            aActive,
            OVERLAY_FRONT_ENCASEMENT_WRAPPER,
            OVERLAY_FRONT_ENCASEMENT_WRAPPER_GLOW,
            OVERLAY_FRONT_ENCASEMENT_WRAPPER_ACTIVE,
            OVERLAY_FRONT_ENCASEMENT_WRAPPER_ACTIVE_GLOW);
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
    public int structureOffsetX() {
        return ENCASEMENT_WRAPPER_OFFSET_X;
    }

    @Override
    public int structureOffsetY() {
        return ENCASEMENT_WRAPPER_OFFSET_Y;
    }

    @Override
    public int structureOffsetZ() {
        return ENCASEMENT_WRAPPER_OFFSET_Z;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType(getModuleType().getMachineModeText())
            .addInfo(TOOLTIP_MODULE_DESCRIPTION)
            .addInfo(
                translateToLocalFormatted(
                    "GT5U.tooltip.nac.module.encasement_wrapper.action",
                    TOOLTIP_CCs,
                    TOOLTIP_CCs))
            .addSeparator()
            .addInfo(tooltipFlavorText(translateToLocal("GT5U.tooltip.nac.module.encasement_wrapper.flavor.1")))
            .beginStructureBlock(7, 9, 7, false)
            .addController(translateToLocal("GT5U.tooltip.nac.interface.structure.module_controller"))
            // Nanochip Mesh Interface Casing
            .addCasing("47", translateToLocal("gt.blockcasings12.1.name"), false)
            // Nanochip Complex Glass
            .addCasing("40", translateToLocal("gt.blockglass1.8.name"), false)
            // Nanochip Reinforcement Casing
            .addCasing("32", translateToLocal("gt.blockcasings12.2.name"), false)
            // Quantium Frame Box
            .addCasing("32", "Quantium Frame Box", false)
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
        return new MTEEncasementWrapperModule(this.mName);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipEncasementWrapper;
    }

}

package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EXOTIC_ENTANGLE_ENGINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EXOTIC_ENTANGLE_ENGINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EXOTIC_ENTANGLE_ENGINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EXOTIC_ENTANGLE_ENGINE_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleTypes;

public class MTEExoticEntangleEngineModule extends MTENanochipAssemblyModuleBase<MTEExoticEntangleEngineModule> {

    protected static final int ENTANGLE_ENGINE_OFFSET_X = 3;
    protected static final int ENTANGLE_ENGINE_OFFSET_Y = 5;
    protected static final int ENTANGLE_ENGINE_OFFSET_Z = 0;
    // Slices run front to back, rows top to bottom; the bottom row stays empty so the shared base can occupy it.
    protected static final String[][] ENTANGLE_ENGINE_STRING = new String[][] {
        { "AAAAAAA", "A     A", "A     A", "A     A", "A     A", "       " },
        { "ABBBBBA", "B     B", "D     D", "D     D", "C     C", "       " },
        { "ABDDDBA", "B  A  B", "D     D", "D  A  D", "C     C", "       " },
        { "ABDDDBA", "B  D  B", "D ADA D", "D  D  D", "C ADA C", "       " },
        { "ABDDDBA", "B  A  B", "D     D", "D  A  D", "C     C", "       " },
        { "ABBBBBA", "B     B", "D     D", "D     D", "C     C", "       " },
        { "AAAAAAA", "A     A", "A     A", "A     A", "A     A", "       " } };
    public static final IStructureDefinition<MTEExoticEntangleEngineModule> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<MTEExoticEntangleEngineModule>builder()
        .addShape(STRUCTURE_PIECE_MAIN, ENTANGLE_ENGINE_STRING)
        // Exo-Halkonite Frame Box, also forms the entangled strands in the interior
        .addElement('A', ofFrame(Materials.ExoHalkonite))
        // Nanochip Mesh Interface Casing
        .addElement('B', Casings.NanochipMeshInterfaceCasing.asElement())
        // Nanochip Reinforcement Casing
        .addElement('C', Casings.NanochipReinforcementCasing.asElement())
        // Nanochip Complex Glass
        .addElement('D', Casings.NanochipComplexGlass.asElement())
        .build();

    public MTEExoticEntangleEngineModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEExoticEntangleEngineModule(String aName) {
        super(aName);
    }

    @Override
    public ModuleTypes getModuleType() {
        return ModuleTypes.ExoticEntangleEngine;
    }

    @Override
    public IStructureDefinition<MTEExoticEntangleEngineModule> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        super.checkMachine(aBaseMetaTileEntity, aStack, errors);
        if (!errors.isEmpty()) return;
        checkHasInputHatch(errors);
    }

    @Override
    public int structureOffsetX() {
        return ENTANGLE_ENGINE_OFFSET_X;
    }

    @Override
    public int structureOffsetY() {
        return ENTANGLE_ENGINE_OFFSET_Y;
    }

    @Override
    public int structureOffsetZ() {
        return ENTANGLE_ENGINE_OFFSET_Z;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return createNanochipModuleTextures(
            side,
            aFacing,
            aActive,
            OVERLAY_FRONT_EXOTIC_ENTANGLE_ENGINE,
            OVERLAY_FRONT_EXOTIC_ENTANGLE_ENGINE_GLOW,
            OVERLAY_FRONT_EXOTIC_ENTANGLE_ENGINE_ACTIVE,
            OVERLAY_FRONT_EXOTIC_ENTANGLE_ENGINE_ACTIVE_GLOW);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType(getModuleType().getMachineModeText())
            .addInfo(TOOLTIP_MODULE_DESCRIPTION)
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.module.exotic_entangle_engine.action", TOOLTIP_CCs))
            .addSeparator()
            .addInfo(translateToLocal("GT5U.tooltip.nac.module.exotic_entangle_engine.body.1"))
            .addSeparator()
            .addInfo(tooltipFlavorText(translateToLocal("GT5U.tooltip.nac.module.exotic_entangle_engine.flavor.1")))
            .beginStructureBlock(7, 8, 7, false)
            .addController(translateToLocal("GT5U.tooltip.nac.interface.structure.module_controller"))
            // Nanochip Mesh Interface Casing
            .addCasing("26", translateToLocal("gt.blockcasings12.1.name"), false)
            // Nanochip Reinforcement Casing
            .addCasing("10", translateToLocal("gt.blockcasings12.2.name"), false)
            // Exo-Halkonite Frame Box
            .addCasing("48", "Exo-Halkonite Frame Box", false)
            // Nanochip Complex Glass
            .addCasing("33", translateToLocal("gt.blockglass1.8.name"), false)
            .addInputHatch("1+", translateToLocal("GT5U.tooltip.nac.interface.structure.module_hatches"), 3)
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
        return new MTEExoticEntangleEngineModule(this.mName);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipExoticEntangleEngine;
    }
}

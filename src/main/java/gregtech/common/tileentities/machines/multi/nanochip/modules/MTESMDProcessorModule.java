package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SMD_PROCESSOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SMD_PROCESSOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SMD_PROCESSOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SMD_PROCESSOR_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.CASING_INDEX_WHITE;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

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
        // Nanochip Mesh Interface Casing
        .addElement('A', Casings.NanochipMeshInterfaceCasing.asElement())
        // Nanochip Reinforcement Casing
        .addElement('B', Casings.NanochipReinforcementCasing.asElement())
        // UEV Machine Casings
        .addElement('C', ofBlock(GregTechAPI.sBlockCasingsNH, 10))
        // Radox polymer frame
        .addElement('D', ofFrame(Materials.RadoxPolymer))
        // Nanochip Glass
        .addElement('E', Casings.NanochipComplexGlass.asElement())
        .build();

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SMD_PROCESSOR)
                    .extFacing()
                    .build(),
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

    public int structureOffsetX() {
        return SMD_OFFSET_X;
    }

    public int structureOffsetY() {
        return SMD_OFFSET_Y;
    }

    public int structureOffsetZ() {
        return SMD_OFFSET_Z;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType(getModuleType().getMachineModeText())
            .addInfo(TOOLTIP_MODULE_DESCRIPTION)
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.module.smd_processor.action", TOOLTIP_CCs))
            .addSeparator()
            .addInfo(tooltipFlavorText(translateToLocal("GT5U.tooltip.nac.module.smd_processor.flavor.1")))
            .beginStructureBlock(7, 3, 7, false)
            .addController(translateToLocal("GT5U.tooltip.nac.module.controller"))
            // Nanochip Reinforcement Casing
            .addCasingInfoExactly(translateToLocal("gt.blockcasings12.2.name"), 44, false)
            // Nanochip Mesh Interface Casing
            .addCasingInfoExactly(translateToLocal("gt.blockcasings12.1.name"), 20, false)
            // Radox Polymer Frame Box
            .addCasingInfoExactly(
                translateToLocal("gt.blockframes.979.name").replace("%material", Materials.RadoxPolymer.getLocalizedName()),
                17,
                false)
            // Nanochip Complex Glass
            .addCasingInfoExactly(translateToLocal("gt.blockglass1.8.name"), 8, false)
            // UEV Machine Casing
            .addCasingInfoExactly(translateToLocal("gt.blockcasingsNH.10.name"), 4, false)
            .addStructureInfo(TOOLTIP_STRUCTURE_BASE_VCI)
            .addStructureInfo(TOOLTIP_STRUCTURE_BASE_VCO)
            .toolTipFinisher();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESMDProcessorModule(this.mName);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipSMDProcessorRecipes;
    }
}

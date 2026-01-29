package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_CUTTING_CHAMBER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_CUTTING_CHAMBER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_CUTTING_CHAMBER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_CUTTING_CHAMBER_GLOW;
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
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleTypes;

public class MTECuttingChamberModule extends MTENanochipAssemblyModuleBase<MTECuttingChamberModule> {

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int CUTTING_OFFSET_X = 3;
    protected static final int CUTTING_OFFSET_Y = 5;
    protected static final int CUTTING_OFFSET_Z = 0;
    protected static final String[][] CUTTING_STRUCTURE = new String[][] {
        { "       ", "       ", "       ", " A   A ", " A   A " },
        { "  CCC  ", " BBBBB ", " CEEEC ", "ACEEECA", "ACEEECA" },
        { " CCDCC ", "B     B", "B     B", "B     B", "B DDD B" },
        { " DDDDD ", "BCCCCCB", "EAAAAAE", "E     E", "E DDD E" },
        { " CCDCC ", "B     B", "B     B", "B     B", "B DDD B" },
        { "  CCC  ", " BBBBB ", " CEEEC ", "ACEEECA", "ACEEECA" },
        { "       ", "       ", "       ", " A   A ", " A   A " } };

    public static final IStructureDefinition<MTECuttingChamberModule> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<MTECuttingChamberModule>builder()
        .addShape(STRUCTURE_PIECE_MAIN, CUTTING_STRUCTURE)
        // Neutronium Frame Box
        .addElement('A', ofFrame(Materials.Neutronium))
        // Nanochip Mesh Interface Casing
        .addElement('B', Casings.NanochipMeshInterfaceCasing.asElement())
        // Nanochip Reinforcement Casing
        .addElement('C', Casings.NanochipReinforcementCasing.asElement())
        // Naquadria-Reinforced Water Plant Casing
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings9, 12))
        // Nanochip Glass
        .addElement('E', Casings.NanochipComplexGlass.asElement())

        .build();

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_CUTTING_CHAMBER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_CUTTING_CHAMBER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_CUTTING_CHAMBER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_CUTTING_CHAMBER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_CUTTING_CHAMBER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE) };
    }

    public MTECuttingChamberModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTECuttingChamberModule(String aName) {
        super(aName);
    }

    @Override
    public ModuleTypes getModuleType() {
        return ModuleTypes.CuttingChamber;
    }

    @Override
    public IStructureDefinition<MTECuttingChamberModule> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    public int structureOffsetX() {
        return CUTTING_OFFSET_X;
    }

    public int structureOffsetY() {
        return CUTTING_OFFSET_Y;
    }

    public int structureOffsetZ() {
        return CUTTING_OFFSET_Z;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType(getModuleType().getMachineModeText())
            .addInfo(TOOLTIP_MODULE_DESCRIPTION)
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.module.cutting_chamber.action", TOOLTIP_CCs))
            .addSeparator()
            .addInfo(tooltipFlavorText(translateToLocal("GT5U.tooltip.nac.module.cutting_chamber.flavor.1")))
            .addInputHatch(TOOLTIP_STRUCTURE_BASE_CASING)
            .addStructureInfo(TOOLTIP_STRUCTURE_BASE_VCI)
            .addStructureInfo(TOOLTIP_STRUCTURE_BASE_VCO)
            .toolTipFinisher();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTECuttingChamberModule(this.mName);
    }

    public static void registerLocalName(String unprocessedName, CircuitComponent component) {
        component.fallbackLocalizedName = "Cut " + unprocessedName;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipCuttingChamber;
    }
}

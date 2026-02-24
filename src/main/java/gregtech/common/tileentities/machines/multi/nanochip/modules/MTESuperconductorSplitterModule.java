package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SUPERCONDUCTOR_SPLITTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SUPERCONDUCTOR_SPLITTER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SUPERCONDUCTOR_SPLITTER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SUPERCONDUCTOR_SPLITTER_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.CASING_INDEX_WHITE;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleTypes;

public class MTESuperconductorSplitterModule extends MTENanochipAssemblyModuleBase<MTESuperconductorSplitterModule> {

    protected static final int COOLANT_CONSUMED_PER_SEC = 1000;

    private MTEHatchInput coolantInputHatch;

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int SUPERCOND_SPLITTER_OFFSET_X = 3;
    protected static final int SUPERCOND_SPLITTER_OFFSET_Y = 7;
    protected static final int SUPERCOND_SPLITTER_OFFSET_Z = 0;
    protected static final String[][] SUPERCOND_SPLITTER_STRUCTURE = new String[][] {
        { "       ", "       ", " DEEED ", "       ", "       ", "       ", " DEEED " },
        { "       ", " D   D ", "DBD DBD", " B   B ", " B   B ", " B   B ", "DDD DDD" },
        { "   C   ", "  CFC  ", "EDCFCDE", "  CFC  ", "  CFC  ", "  CFC  ", "EDCFCDE" },
        { "  CCC  ", "  FAF  ", "E FAF E", "  FAF  ", "  FAF  ", "  FAF  ", "E FAF E" },
        { "   C   ", "  CFC  ", "EDCFCDE", "  CFC  ", "  CFC  ", "  CFC  ", "EDCFCDE" },
        { "       ", " D   D ", "DBD DBD", " B   B ", " B   B ", " B   B ", "DDD DDD" },
        { "       ", "       ", " DEEED ", "       ", "       ", "       ", " DEEED " } };

    public static final IStructureDefinition<MTESuperconductorSplitterModule> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<MTESuperconductorSplitterModule>builder()
        .addShape(STRUCTURE_PIECE_MAIN, SUPERCOND_SPLITTER_STRUCTURE)
        // UHV Solenoid
        .addElement('A', ofBlock(GregTechAPI.sSolenoidCoilCasings, 7))
        // UEV Solenoid
        .addElement('B', ofBlock(GregTechAPI.sSolenoidCoilCasings, 8))
        // Nanochip Mesh Interface Casing
        .addElement('C', Casings.NanochipMeshInterfaceCasing.asElement())
        // Nanochip Reinforcement Casing
        .addElement('D', Casings.NanochipReinforcementCasing.asElement())
        // Naquadria Frame box
        .addElement('E', ofFrame(Materials.Naquadria))
        // Nanochip Glass
        .addElement('F', Casings.NanochipComplexGlass.asElement())
        .build();

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SUPERCONDUCTOR_SPLITTER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SUPERCONDUCTOR_SPLITTER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SUPERCONDUCTOR_SPLITTER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SUPERCONDUCTOR_SPLITTER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SUPERCONDUCTOR_SPLITTER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE) };
    }

    public MTESuperconductorSplitterModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTESuperconductorSplitterModule(String aName) {
        super(aName);
    }

    @Override
    public ModuleTypes getModuleType() {
        return ModuleTypes.SuperconductorSplitter;
    }

    @Override
    public IStructureDefinition<MTESuperconductorSplitterModule> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    public int structureOffsetX() {
        return SUPERCOND_SPLITTER_OFFSET_X;
    }

    public int structureOffsetY() {
        return SUPERCOND_SPLITTER_OFFSET_Y;
    }

    public int structureOffsetZ() {
        return SUPERCOND_SPLITTER_OFFSET_Z;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // Check base structure
        if (!super.checkMachine(aBaseMetaTileEntity, aStack)) return false;
        // Add coolant hatch
        if (!findCoolantHatch()) return false;
        return true;
    }

    private boolean findCoolantHatch() {
        if (!mInputHatches.isEmpty()) {
            coolantInputHatch = mInputHatches.get(0);
            return true;
        }
        return false;

    }

    private int ticker = 0;

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (!super.onRunningTick(aStack)) {
            return false;
        }

        if (ticker % 20 == 0) {
            FluidStack fluidToBeDrained = Materials.SuperCoolant.getFluid(COOLANT_CONSUMED_PER_SEC);
            if (!drain(coolantInputHatch, fluidToBeDrained, true)) {
                stopMachine(ShutDownReasonRegistry.outOfFluid(fluidToBeDrained));
                return false;
            }
            ticker = 0;
        }

        ticker++;

        return true;
    }

    /**
     * Try to find a recipe in the recipe map using the given stored inputs
     *
     * @return A recipe if one was found, null otherwise
     */
    protected GTRecipe findRecipe(ArrayList<ItemStack> inputs) {
        RecipeMap<?> recipeMap = this.getRecipeMap();
        return recipeMap.findRecipeQuery()
            .items(inputs.toArray(new ItemStack[] {}))
            .find();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType(getModuleType().getMachineModeText())
            .addInfo(TOOLTIP_MODULE_DESCRIPTION)
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.module.superconductor_splitter.action", TOOLTIP_CCs))
            .addSeparator()
            .addInfo(translateToLocal("GT5U.tooltip.nac.module.superconductor_splitter.body.1")) // todo: maybe make
                                                                                                 // this
                                                                                                 // more interesting /
                                                                                                 // use higher tier
                                                                                                 // coolants for higher
                                                                                                 // tier sc
            .addSeparator()
            .addInfo(tooltipFlavorText(translateToLocal("GT5U.tooltip.nac.module.superconductor_splitter.flavor.1")))
            .beginStructureBlock(7, 10, 7, false)
            .addController(translateToLocal("GT5U.tooltip.nac.module.controller"))
            // Nanochip Reinforcement Casing
            .addCasingInfoExactly(translateToLocal("gt.blockcasings12.2.name"), 40, false)
            // Nanochip Mesh Interface Casing
            .addCasingInfoExactly(translateToLocal("gt.blockcasings12.1.name"), 29, false)
            // Nanochip Complex Glass
            .addCasingInfoExactly(translateToLocal("gt.blockglass1.8.name"), 24, false)
            // Naquadria Frame Box
            .addCasingInfoExactly(
                translateToLocal("gt.blockframes.327.name")
                    .replace("%material", Materials.Naquadria.getLocalizedName()),
                24,
                false)
            // UEV Solenoid Superconductor Coil
            .addCasingInfoExactly(translateToLocal("gt.blockcasings.cyclotron_coils.8.name"), 16, false)
            // UHV Solenoid Superconductor Coil
            .addCasingInfoExactly(translateToLocal("gt.blockcasings.cyclotron_coils.7.name"), 6, false)
            .addInputHatch(TOOLTIP_STRUCTURE_BASE_CASING)
            .addStructureInfo(TOOLTIP_STRUCTURE_BASE_VCI)
            .addStructureInfo(TOOLTIP_STRUCTURE_BASE_VCO)
            .toolTipFinisher();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESuperconductorSplitterModule(this.mName);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipSuperconductorSplitter;
    }
}

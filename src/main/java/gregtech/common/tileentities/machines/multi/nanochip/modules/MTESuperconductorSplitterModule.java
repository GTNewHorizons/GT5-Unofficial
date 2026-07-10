package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SUPERCONDUCTOR_SPLITTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SUPERCONDUCTOR_SPLITTER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SUPERCONDUCTOR_SPLITTER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SUPERCONDUCTOR_SPLITTER_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.HatchElement;
import gregtech.api.enums.Materials;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.structure.error.ErrorType;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
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
        .addElement('E', ofFrame(Materials2Materials.Naquadria))
        // Nanochip Glass
        .addElement('F', Casings.NanochipComplexGlass.asElement())
        .build();

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return createNanochipModuleTextures(
            side,
            aFacing,
            aActive,
            OVERLAY_FRONT_SUPERCONDUCTOR_SPLITTER,
            OVERLAY_FRONT_SUPERCONDUCTOR_SPLITTER_GLOW,
            OVERLAY_FRONT_SUPERCONDUCTOR_SPLITTER_ACTIVE,
            OVERLAY_FRONT_SUPERCONDUCTOR_SPLITTER_ACTIVE_GLOW);
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

    @Override
    public int structureOffsetX() {
        return SUPERCOND_SPLITTER_OFFSET_X;
    }

    @Override
    public int structureOffsetY() {
        return SUPERCOND_SPLITTER_OFFSET_Y;
    }

    @Override
    public int structureOffsetZ() {
        return SUPERCOND_SPLITTER_OFFSET_Z;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        super.checkMachine(aBaseMetaTileEntity, aStack, errors);
        // Check base structure
        if (!errors.isEmpty()) return;
        // Add coolant hatch
        findCoolantHatch(errors);
    }

    private void findCoolantHatch(List<StructureError> errors) {
        if (!mInputHatches.isEmpty()) {
            coolantInputHatch = mInputHatches.get(0);
            if (mInputHatches.size() > 1) {
                errors.add(
                    StructureErrors.hatchCount(ErrorType.TOO_MANY, HatchElement.InputHatch, mInputHatches.size(), 1));
            }
        } else {
            errors.add(StructureErrors.missingHatch(HatchElement.InputHatch));
        }
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
    @Override
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
            .beginStructureBlock(7, 7, 10, false)
            .addController(translateToLocal("GT5U.tooltip.nac.interface.structure.module_controller"))
            // Nanochip Reinforcement Casing
            .addCasing("40", translateToLocal("gt.blockcasings12.2.name"), false)
            // Nanochip Mesh Interface Casing
            .addCasing("29", translateToLocal("gt.blockcasings12.1.name"), false)
            // Nanochip Complex Glass
            .addCasing("24", translateToLocal("gt.blockglass1.8.name"), false)
            // Naquadria Frame Box
            .addCasing("24", "Naquadria Frame Box", false)
            // UEV Solenoid Superconductor Coil
            .addCasing("16", translateToLocal("gt.blockcasings.cyclotron_coils.8.name"), false)
            // UHV Solenoid Superconductor Coil
            .addCasing("6", translateToLocal("gt.blockcasings.cyclotron_coils.7.name"), false)
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
        return new MTESuperconductorSplitterModule(this.mName);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipSuperconductorSplitter;
    }
}

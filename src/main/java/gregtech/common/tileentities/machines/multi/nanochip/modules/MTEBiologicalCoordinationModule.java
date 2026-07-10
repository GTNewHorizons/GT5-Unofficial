package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BIOLOGICAL_COORDINATION;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BIOLOGICAL_COORDINATION_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BIOLOGICAL_COORDINATION_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BIOLOGICAL_COORDINATION_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.extensions.ArrayExt;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleTypes;

public class MTEBiologicalCoordinationModule extends MTENanochipAssemblyModuleBase<MTEBiologicalCoordinationModule> {

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int BIO_OFFSET_X = 3;
    protected static final int BIO_OFFSET_Y = 5;
    protected static final int BIO_OFFSET_Z = 0;
    protected static final String[][] BIO_STRING = new String[][] {
        { "       ", " AAAAA ", "  C C  ", "  C C  ", "  C C  ", "   ~   " },
        { "  CCC  ", "AABBBAA", " ABDBA ", " ABDBA ", " ABDBA ", "       " },
        { " C   C ", "ABDDDBA", "CB   BC", "CB   BC", "CB   BC", "       " },
        { " C   C ", "ABDADBA", " D   D ", " D   D ", " D   D ", "       " },
        { " C   C ", "ABDDDBA", "CB   BC", "CB   BC", "CB   BC", "       " },
        { "  CCC  ", "AABBBAA", " ABDBA ", " ABDBA ", " ABDBA ", "       " },
        { "       ", " AAAAA ", "  C C  ", "  C C  ", "  C C  ", "       " } };
    public static final IStructureDefinition<MTEBiologicalCoordinationModule> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<MTEBiologicalCoordinationModule>builder()
        .addShape(STRUCTURE_PIECE_MAIN, BIO_STRING)
        // Nanochip Mesh Interface Casing
        .addElement('A', Casings.NanochipMeshInterfaceCasing.asElement())
        // Nanochip Reinforcement Casing
        .addElement('B', Casings.NanochipReinforcementCasing.asElement())
        // Tritanium Frame Box
        .addElement('C', ofFrame(Materials.Tritanium))
        // Circuit Complex Glass
        .addElement('D', Casings.NanochipComplexGlass.asElement())
        .build();

    public MTEBiologicalCoordinationModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEBiologicalCoordinationModule(String aName) {
        super(aName);
    }

    @Override
    public ModuleTypes getModuleType() {
        return ModuleTypes.BiologicalCoordinator;
    }

    @Override
    public IStructureDefinition<MTEBiologicalCoordinationModule> getStructureDefinition() {
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
        return BIO_OFFSET_X;
    }

    @Override
    public int structureOffsetY() {
        return BIO_OFFSET_Y;
    }

    @Override
    public int structureOffsetZ() {
        return BIO_OFFSET_Z;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return createNanochipModuleTextures(
            side,
            aFacing,
            aActive,
            OVERLAY_FRONT_BIOLOGICAL_COORDINATION,
            OVERLAY_FRONT_BIOLOGICAL_COORDINATION_GLOW,
            OVERLAY_FRONT_BIOLOGICAL_COORDINATION_ACTIVE,
            OVERLAY_FRONT_BIOLOGICAL_COORDINATION_ACTIVE_GLOW);
    }

    /**
     * potential gimmick:
     * Takes in AOs with certain stats, after AOs are merged.
     * For now, can just drain bio/growth cat.
     */
    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType(getModuleType().getMachineModeText())
            .addInfo(TOOLTIP_MODULE_DESCRIPTION)
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.module.biological_coordinator.action", TOOLTIP_CCs))
            .addSeparator()
            .addInfo(translateToLocal("GT5U.tooltip.nac.module.biological_coordinator.body.1"))
            .addInfo(translateToLocal("GT5U.tooltip.nac.module.biological_coordinator.body.2"))
            .addSeparator()
            .addInfo(tooltipFlavorText(translateToLocal("GT5U.tooltip.nac.module.biological_coordinator.flavor.1")))
            .addInfo(tooltipFlavorText(translateToLocal("GT5U.tooltip.nac.module.biological_coordinator.flavor.2")))
            .beginStructureBlock(7, 7, 8, false)
            .addController(translateToLocal("GT5U.tooltip.nac.interface.structure.module_controller"))
            // Nanochip Mesh Interface Casing
            .addCasing("37", translateToLocal("gt.blockcasings12.1.name"), false)
            // Nanochip Reinforcement Casing
            .addCasing("36", translateToLocal("gt.blockcasings12.2.name"), false)
            // Tritanium Frame Box
            .addCasing("36", "Tritanium Frame Box", false)
            // Nanochip Complex Glass
            .addCasing("20", translateToLocal("gt.blockglass1.8.name"), false)
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
    protected GTRecipe findRecipe(ArrayList<ItemStack> inputs) {
        RecipeMap<?> recipeMap = this.getRecipeMap();
        final List<FluidStack> fakeFluids = new ArrayList<>(getStoredFluids());
        if (baseMulti.wetwareT3Active) {
            fakeFluids.add(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.GrowthMediumSterilized,
                    Materials2FluidShapes.shapeFluidLiquid,
                    Integer.MAX_VALUE));
        }
        if (baseMulti.bioT3Active) {
            fakeFluids.add(Materials.BioMediumSterilized.getFluid(Integer.MAX_VALUE));
        }
        FluidStack[] inputFluids = fakeFluids.toArray(new FluidStack[] {});
        this.fluidInputs = inputFluids;

        return recipeMap.findRecipeQuery()
            .items(inputs.toArray(new ItemStack[] {}))
            .fluids(inputFluids)
            .find();
    }

    @Override
    public GTRecipe transformRecipe(GTRecipe recipe) {
        GTRecipe transformedRecipe = super.transformRecipe(recipe);
        FluidStack[] fluidInputs = transformedRecipe.mFluidInputs;
        for (int i = 0; i < fluidInputs.length; i++) {
            FluidStack stack = fluidInputs[i];
            if (stack == null) continue;
            if (baseMulti.wetwareT3Active && stack.getFluid()
                .equals(
                    MaterialLibAPI
                        .getFluidStack(
                            Materials2Materials.GrowthMediumSterilized,
                            Materials2FluidShapes.shapeFluidLiquid,
                            1)
                        .getFluid()))
                fluidInputs[i] = null;
            if (baseMulti.bioT3Active && stack.getFluid()
                .equals(Materials.BioMediumSterilized.mFluid)) fluidInputs[i] = null;
        }
        transformedRecipe.setFluidInputs(ArrayExt.removeNullFluids(fluidInputs));
        return transformedRecipe;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBiologicalCoordinationModule(this.mName);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipBiologicalCoordinator;
    }
}

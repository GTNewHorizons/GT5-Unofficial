package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BIOLOGICAL_COORDINATION;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BIOLOGICAL_COORDINATION_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BIOLOGICAL_COORDINATION_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BIOLOGICAL_COORDINATION_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.CASING_INDEX_WHITE;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
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

    public int structureOffsetX() {
        return BIO_OFFSET_X;
    }

    public int structureOffsetY() {
        return BIO_OFFSET_Y;
    }

    public int structureOffsetZ() {
        return BIO_OFFSET_Z;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_BIOLOGICAL_COORDINATION)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_BIOLOGICAL_COORDINATION_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_BIOLOGICAL_COORDINATION_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_BIOLOGICAL_COORDINATION)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_BIOLOGICAL_COORDINATION_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE) };
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
            .beginStructureBlock(7, 8, 7, false)
            .addController(translateToLocal("GT5U.tooltip.nac.module.controller"))
            // Nanochip Mesh Interface Casing
            .addCasingInfoExactly(translateToLocal("gt.blockcasings12.1.name"), 37, false)
            // Nanochip Reinforcement Casing
            .addCasingInfoExactly(translateToLocal("gt.blockcasings12.2.name"), 36, false)
            // Tritanium Frame Box
            .addCasingInfoExactly(
                translateToLocal("gt.blockframes.10.name").replace("%material", Materials.Tritanium.getLocalizedName()),
                36,
                false)
            // Nanochip Complex Glass
            .addCasingInfoExactly(translateToLocal("gt.blockglass1.8.name"), 20, false)
            .addStructureInfo(TOOLTIP_STRUCTURE_BASE_VCI)
            .addStructureInfo(TOOLTIP_STRUCTURE_BASE_VCO)
            .toolTipFinisher();
    }

    @Override
    protected GTRecipe findRecipe(ArrayList<ItemStack> inputs) {
        RecipeMap<?> recipeMap = this.getRecipeMap();
        final List<FluidStack> fakeFluids = new ArrayList<>(getStoredFluids());
        if (baseMulti.wetwareT3Active) {
            fakeFluids.add(Materials.GrowthMediumSterilized.getFluid(Integer.MAX_VALUE));
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
                .equals(Materials.GrowthMediumSterilized.mFluid)) fluidInputs[i] = null;
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

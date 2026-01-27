package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OPTICAL_ORGANIZER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OPTICAL_ORGANIZER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OPTICAL_ORGANIZER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OPTICAL_ORGANIZER_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.gui.modularui.multiblock.MTENanochipAssemblyComplexGui.colorString;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.CASING_INDEX_WHITE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.INFINITE_PARALLEL;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.NAC_MODULE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_CCs;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_VCI;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_VCO;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleTypes;

// todo look over and cleanup. the functionality is present
public class MTEOpticalOrganizerModule extends MTENanochipAssemblyModuleBase<MTEOpticalOrganizerModule> {

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int OPTICAL_OFFSET_X = 3;
    protected static final int OPTICAL_OFFSET_Y = 7;
    protected static final int OPTICAL_OFFSET_Z = 0;
    protected static final String[][] OPTICAL_STRING = new String[][] {
        { "       ", " BBCBB ", " AACAA ", "   C   ", "   C   ", "   C   ", " AACAA " },
        { " AAAAA ", "BBBCBBB", "ABD DBA", " BD DB ", " BD DB ", " BD DB ", "ABD DBA" },
        { " A   A ", "BBCCCBB", "AD   DA", " DCCCD ", " D   D ", " DCCCD ", "AD   DA" },
        { " A   A ", "CCCBCCC", "C  C  C", "C CCC C", "C     C", "C CCC C", "C  C  C" },
        { " A   A ", "BBCCCBB", "AD   DA", " DCC D ", " D   D ", " DCCCD ", "AD   DA" },
        { " AAAAA ", "BBBCBBB", "ABD DBA", " BD DB ", " BD DB ", " BD DB ", "ABD DBA" },
        { "       ", " BBCBB ", " AACAA ", "   C   ", "   C   ", "   C   ", " AACAA " } };
    public static final IStructureDefinition<MTEOpticalOrganizerModule> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<MTEOpticalOrganizerModule>builder()
        .addShape(STRUCTURE_PIECE_MAIN, OPTICAL_STRING)
        // Awakened Draconium Frame Box
        .addElement('A', ofFrame(Materials.DraconiumAwakened))
        // Nanochip Mesh Interface Casing
        .addElement('B', Casings.NanochipMeshInterfaceCasing.asElement())
        // Nanochip Reinforcement Casing
        .addElement('C', Casings.NanochipReinforcementCasing.asElement())
        // Nanochip Glass
        .addElement('D', Casings.NanochipComplexGlass.asElement())
        .build();

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OPTICAL_ORGANIZER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OPTICAL_ORGANIZER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OPTICAL_ORGANIZER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OPTICAL_ORGANIZER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OPTICAL_ORGANIZER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE) };
    }

    public MTEOpticalOrganizerModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEOpticalOrganizerModule(String aName) {
        super(aName);
    }

    @Override
    public ModuleTypes getModuleType() {
        return ModuleTypes.OpticalOrganizer;
    }

    @Override
    public IStructureDefinition<MTEOpticalOrganizerModule> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        buildPiece(STRUCTURE_PIECE_MAIN, trigger, hintsOnly, OPTICAL_OFFSET_X, OPTICAL_OFFSET_Y, OPTICAL_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            OPTICAL_OFFSET_X,
            OPTICAL_OFFSET_Y,
            OPTICAL_OFFSET_Z,
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
        return checkPiece(STRUCTURE_PIECE_MAIN, OPTICAL_OFFSET_X, OPTICAL_OFFSET_Y, OPTICAL_OFFSET_Z)
            && this.mInputHatches.size() <= 2
            && !this.mInputHatches.isEmpty();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType("NAC Module")
            .addInfo(NAC_MODULE)
            .addSeparator()
            .addInfo("Optimizes your Optical " + TOOLTIP_CCs)
            .addInfo(
                "Outputs are placed in the " + TOOLTIP_VCO
                    + " with the same "
                    + colorString()
                    + " as the input "
                    + TOOLTIP_VCI)
            .addSeparator()
            .addInfo(
                "Requires 2 " + EnumChatFormatting.AQUA
                    + "Purified Waters"
                    + EnumChatFormatting.GRAY
                    + " to be supplied")

            .addInfo(
                "Will " + EnumChatFormatting.DARK_GREEN
                    + "drain X L/s"
                    + EnumChatFormatting.GRAY
                    + " of "
                    + EnumChatFormatting.AQUA
                    + "Waters"
                    + EnumChatFormatting.GRAY
                    + " when running to provide "
                    + EnumChatFormatting.WHITE
                    + "Bonuses")
            .addInfo(
                EnumChatFormatting.WHITE + "Bonuses"
                    + EnumChatFormatting.GRAY
                    + " of "
                    + EnumChatFormatting.AQUA
                    + "Waters"
                    + EnumChatFormatting.GRAY
                    + " in the same pair do NOT stack!")
            .addInfo(INFINITE_PARALLEL)
            .addSeparator()
            // scuffed but works i guess.
            .addInfo(getWaterTooltipLine("3", WATER_LIST.get(0).amount, "0.8x Water Cost", TooltipHelper.SPEED_COLOR))
            .addInfo(getWaterTooltipLine("4", WATER_LIST.get(1).amount, "0.6x Water Cost", TooltipHelper.SPEED_COLOR))
            .addInfo(getWaterTooltipLine("5", WATER_LIST.get(2).amount, "1.3x Speed", EnumChatFormatting.WHITE))
            .addInfo(getWaterTooltipLine("6", WATER_LIST.get(3).amount, "1.8x Speed", EnumChatFormatting.WHITE))
            .addInfo(getWaterTooltipLine("7", WATER_LIST.get(4).amount, "0.8x EU", TooltipHelper.PARALLEL_COLOR))
            .addInfo(getWaterTooltipLine("8", WATER_LIST.get(5).amount, "0.5x EU", TooltipHelper.PARALLEL_COLOR))
            .addSeparator()
            .addInfo(
                EnumChatFormatting.LIGHT_PURPLE + ""
                    + EnumChatFormatting.ITALIC
                    + "Mix and match your preferred flavor of water to optimize your optical circuit parts")
            .addStructureInfo("Any base casing - Vacuum Conveyor Input")
            .addStructureInfo("Any base casing - Vacuum Conveyor Output")
            .addInputHatch("Any base casing", 2)
            .toolTipFinisher();
    }

    public String getWaterTooltipLine(String grade, int amount, String effect, EnumChatFormatting effectColor) {
        return "Grade " + EnumChatFormatting.AQUA
            + grade
            + EnumChatFormatting.GRAY
            + "/"
            + EnumChatFormatting.DARK_GREEN
            + amount
            + EnumChatFormatting.GRAY
            + "/"
            + effectColor
            + effect;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEOpticalOrganizerModule(this.mName);
    }

    public static void registerLocalName(String unprocessedName, CircuitComponent component) {
        component.fallbackLocalizedName = "Optimized " + unprocessedName;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipOpticalOrganizer;
    }

    float speedModifier = 1;
    float euMultiplier = 1;
    float waterDiscount = 1;

    @Override
    protected float getBonusSpeedModifier() {
        return speedModifier;
    }

    @Override
    protected float getEUDiscountModifier() {
        return euMultiplier;
    }

    BoostingWater firstWater = null;
    BoostingWater secondWater = null;

    @Override
    public @NotNull CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
        waterDiscount = 1;
        speedModifier = 1;
        euMultiplier = 1;
        firstWater = null;
        secondWater = null;

        for (MTEHatchInput hatch : mInputHatches) {
            if (firstWater != null && secondWater != null) break;

            final List<BoostingWater> fluid = WATER_LIST.stream()
                .filter(candidate -> drain(hatch, candidate.water.getFluid(candidate.amount), false))
                .collect(Collectors.toList());

            if (fluid.size() >= 2) {
                firstWater = fluid.get(0);
                secondWater = fluid.get(1);
            } else if (fluid.size() == 1) {
                if (firstWater == null) {
                    firstWater = fluid.get(0);
                } else {
                    secondWater = fluid.get(0);
                }
            }
        }
        if (firstWater == null || secondWater == null || (firstWater.water == secondWater.water)) {
            return CheckRecipeResultRegistry.NAC_OPTICAL_MISSING_WATER;
        }
        firstWater.boosterFunction.accept(this);
        secondWater.boosterFunction.accept(this);
        return super.validateRecipe(recipe);
    }

    @Override
    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.runMachine(aBaseMetaTileEntity, aTick);
        if (mMaxProgresstime > 0 && aTick % 20 == 0) {
            if (this.firstWater != null) {
                FluidStack firstStack = firstWater.getStack(waterDiscount);
                if (!this.depleteInput(firstStack)) stopMachine(ShutDownReasonRegistry.outOfFluid(firstStack));
            }
            if (this.secondWater != null) {
                FluidStack secondStack = secondWater.getStack(waterDiscount);
                if (!this.depleteInput(secondStack)) stopMachine(ShutDownReasonRegistry.outOfFluid(secondStack));
            }
        }
    }

    private static final List<BoostingWater> WATER_LIST = ImmutableList.of(
        new BoostingWater(Materials.Grade3PurifiedWater, 1000, module -> { module.waterDiscount = 0.8f; }),
        new BoostingWater(Materials.Grade4PurifiedWater, 500, module -> { module.waterDiscount = 0.6f; }),
        new BoostingWater(Materials.Grade5PurifiedWater, 600, module -> { module.speedModifier = 1.3f; }),
        new BoostingWater(Materials.Grade6PurifiedWater, 300, module -> { module.speedModifier = 1.8f; }),
        new BoostingWater(Materials.Grade7PurifiedWater, 200, module -> { module.euMultiplier = 0.8f; }),
        new BoostingWater(Materials.Grade8PurifiedWater, 100, module -> { module.euMultiplier = 0.5f; }));

    private static class BoostingWater {

        public final Materials water;
        public final int amount;
        public final Consumer<MTEOpticalOrganizerModule> boosterFunction;

        public BoostingWater(Materials water, int amount, Consumer<MTEOpticalOrganizerModule> boosterFunction) {
            this.water = water;
            this.amount = amount;
            this.boosterFunction = boosterFunction;
        }

        public FluidStack getStack(float waterDiscount) {
            return this.water.getFluid((long) (waterDiscount * this.amount));
        }
    }
}

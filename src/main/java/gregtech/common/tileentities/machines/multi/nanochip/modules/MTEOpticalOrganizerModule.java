package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OPTICAL_ORGANIZER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OPTICAL_ORGANIZER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OPTICAL_ORGANIZER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OPTICAL_ORGANIZER_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.CASING_INDEX_WHITE;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
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
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleTypes;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

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
        { " A   A ", "BBCCCBB", "AD   DA", " DCCCD ", " D   D ", " DCCCD ", "AD   DA" },
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
    public int structureOffsetX() {
        return OPTICAL_OFFSET_X;
    }

    @Override
    public int structureOffsetY() {
        return OPTICAL_OFFSET_Y;
    }

    @Override
    public int structureOffsetZ() {
        return OPTICAL_OFFSET_Z;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!super.checkMachine(aBaseMetaTileEntity, aStack)) return false;
        return this.mInputHatches.size() <= 2 && !this.mInputHatches.isEmpty();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        // spotless:off
        return new MultiblockTooltipBuilder().addMachineType(getModuleType().getMachineModeText())
            .addInfo(TOOLTIP_MODULE_DESCRIPTION)
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.module.optical_organizer.action", TOOLTIP_CCs))
            .addSeparator()
            .addInfo(translateToLocal("GT5U.tooltip.nac.module.optical_organizer.body.1"))
            .addInfo(translateToLocal("GT5U.tooltip.nac.module.optical_organizer.body.2"))
            .addInfo(translateToLocal("GT5U.tooltip.nac.module.optical_organizer.body.3"))
            .addInfo(translateToLocal("GT5U.tooltip.nac.module.optical_organizer.body.4"))
            .addSeparator()
            .addInfo(getWaterTooltipLine("3", BoostingWater.Grade3.amount, translateToLocalFormatted("GT5U.tooltip.nac.module.optical_organizer.body.water34","0.8x"), EnumChatFormatting.WHITE))
            .addInfo(getWaterTooltipLine("4", BoostingWater.Grade4.amount, translateToLocalFormatted("GT5U.tooltip.nac.module.optical_organizer.body.water34","0.6x"), EnumChatFormatting.WHITE))
            .addInfo(getWaterTooltipLine("5", BoostingWater.Grade5.amount, translateToLocalFormatted("GT5U.tooltip.nac.module.optical_organizer.body.water56","0.9x"), TooltipHelper.SPEED_COLOR))
            .addInfo(getWaterTooltipLine("6", BoostingWater.Grade6.amount, translateToLocalFormatted("GT5U.tooltip.nac.module.optical_organizer.body.water56","0.7x"), TooltipHelper.SPEED_COLOR))
            .addInfo(getWaterTooltipLine("7", BoostingWater.Grade7.amount, translateToLocalFormatted("GT5U.tooltip.nac.module.optical_organizer.body.water78","0.9x"), TooltipHelper.EFF_COLOR))
            .addInfo(getWaterTooltipLine("8", BoostingWater.Grade8.amount, translateToLocalFormatted("GT5U.tooltip.nac.module.optical_organizer.body.water78","0.7x"), TooltipHelper.EFF_COLOR))
            .addSeparator()
            .addInfo(tooltipFlavorText(translateToLocal("GT5U.tooltip.nac.module.optical_organizer.flavor.1")))
            .beginStructureBlock(7, 10, 7, false)
            .addController(translateToLocal("GT5U.tooltip.nac.interface.structure.module_controller"))
            // Nanochip Reinforcement Casing
            .addCasingInfoExactly(translateToLocal("gt.blockcasings12.2.name"), 56, false)
            // Nanochip Mesh Interface Casing
            .addCasingInfoExactly(translateToLocal("gt.blockcasings12.1.name"), 49, false)
            // Awakened Draconium Frame Box
            .addCasingInfoExactly(
                translateToLocal("gt.blockframes.10.name").replace("%material", Materials.DraconiumAwakened.getLocalizedName()),
                48,
                false)
            // Nanochip Complex Glass
            .addCasingInfoExactly(translateToLocal("gt.blockglass1.8.name"), 40, false)
            .addInputHatch(TOOLTIP_STRUCTURE_BASE_CASING)
            .addStructureInfo(TOOLTIP_STRUCTURE_BASE_VCI)
            .addStructureInfo(TOOLTIP_STRUCTURE_BASE_VCO)
            .addStructureInfoSeparator()
            .addStructureInfo(translateToLocal("GT5U.tooltip.nac.interface.structure.module_description"))
            .toolTipFinisher();
        // spotless:on
    }

    public String getWaterTooltipLine(String grade, int amount, String effect, EnumChatFormatting effectColor) {
        return translateToLocalFormatted(
            "GT5U.tooltip.nac.module.optical_organizer.body.water",
            grade,
            amount,
            effectColor,
            effect);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEOpticalOrganizerModule(this.mName);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipOpticalOrganizer;
    }

    float speedModifier = 1;
    float euMultiplier = 1;
    float waterDiscount = 1;

    @Override
    protected float getModuleDurationModifier() {
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

        for (FluidStack fluid : getStoredFluids()) {
            if (firstWater != null && secondWater != null) break;
            BoostingWater candidate = BoostingWater.getByFluid(fluid.getFluid());
            if (candidate == null) continue;
            if (firstWater == null) {
                firstWater = candidate;
            } else if (secondWater == null && candidate != firstWater) {
                secondWater = candidate;
            }
        }

        if (firstWater == null || secondWater == null) {
            return CheckRecipeResultRegistry.NAC_OPTICAL_MISSING_WATER;
        }
        if (baseMulti.opticalT3Active) {
            firstWater.boosterFunction.accept(this);
            secondWater.boosterFunction.accept(this);
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

    private enum BoostingWater {

        Grade3(Materials.Grade3PurifiedWater, 1000, module -> module.waterDiscount *= 0.8f),
        Grade4(Materials.Grade4PurifiedWater, 800, module -> module.waterDiscount *= 0.6f),
        Grade5(Materials.Grade5PurifiedWater, 800, module -> module.speedModifier *= 0.9f),
        Grade6(Materials.Grade6PurifiedWater, 600, module -> module.speedModifier *= 0.7f),
        Grade7(Materials.Grade7PurifiedWater, 600, module -> module.euMultiplier *= 0.9f),
        Grade8(Materials.Grade8PurifiedWater, 400, module -> module.euMultiplier *= 0.7f),

        ;

        private static final Map<Fluid, BoostingWater> FLUID_MAP = new Object2ObjectOpenHashMap<>();
        private final Fluid water;
        private final int amount;
        private final Consumer<MTEOpticalOrganizerModule> boosterFunction;

        private static final BoostingWater[] values = values();

        BoostingWater(Materials water, int amount, Consumer<MTEOpticalOrganizerModule> boosterFunction) {
            this.water = water.mFluid;
            this.amount = amount;
            this.boosterFunction = boosterFunction;
        }

        public static BoostingWater getByFluid(Fluid fluid) {
            if (FLUID_MAP.isEmpty()) {
                Arrays.stream(values)
                    .forEach(water -> FLUID_MAP.put(water.water, water));
            }
            return FLUID_MAP.get(fluid);
        }

        public FluidStack getStack(float waterDiscount) {
            return new FluidStack(water, (int) (waterDiscount * (float) this.amount));
        }
    }
}

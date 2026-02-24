package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BOARD_PROCESSOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BOARD_PROCESSOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BOARD_PROCESSOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BOARD_PROCESSOR_GLOW;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.CASING_INDEX_WHITE;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import goodgenerator.items.GGMaterial;
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
import gregtech.api.recipe.metadata.BoardProcessingModuleFluidKey;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.MTEBoardProcessorModuleGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleTypes;
import gtPlusPlus.core.material.MaterialsAlloy;

public class MTEBoardProcessorModule extends MTENanochipAssemblyModuleBase<MTEBoardProcessorModule> {

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int BOARD_OFFSET_X = 3;
    protected static final int BOARD_OFFSET_Y = 4;
    protected static final int BOARD_OFFSET_Z = 0;
    protected static final String[][] BOARD_STRING = new String[][] { { "       ", " A   A ", " A   A ", " B   B " },
        { " A   A ", "CCC CCC", "DDD DDD", "DDD DDD" }, { "  A A  ", "CBCCCBC", "D DDD D", "D DDD D" },
        { "   A   ", "CBCCCBC", "D DAD D", "D DAD D" }, { "  A A  ", "CBCCCBC", "D DDD D", "D DDD D" },
        { " A   A ", "CCC CCC", "DDD DDD", "DDD DDD" }, { "       ", " A   A ", " A   A ", " B   B " } };
    public static final IStructureDefinition<MTEBoardProcessorModule> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<MTEBoardProcessorModule>builder()
        .addShape(STRUCTURE_PIECE_MAIN, BOARD_STRING)
        // Octiron frame
        .addElement(
            'A',
            lazy(
                t -> ofBlock(
                    Block.getBlockFromItem(
                        MaterialsAlloy.OCTIRON.getFrameBox(1)
                            .getItem()),
                    MaterialsAlloy.OCTIRON.getFrameBox(1)
                        .getItemDamage())))
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
                    .addIcon(OVERLAY_FRONT_BOARD_PROCESSOR)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_BOARD_PROCESSOR_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_BOARD_PROCESSOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_BOARD_PROCESSOR)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_BOARD_PROCESSOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE) };
    }

    public MTEBoardProcessorModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEBoardProcessorModule(String aName) {
        super(aName);
    }

    @Override
    public ModuleTypes getModuleType() {
        return ModuleTypes.BoardProcessor;
    }

    @Override
    public IStructureDefinition<MTEBoardProcessorModule> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    public int structureOffsetX() {
        return BOARD_OFFSET_X;
    }

    public int structureOffsetY() {
        return BOARD_OFFSET_Y;
    }

    public int structureOffsetZ() {
        return BOARD_OFFSET_Z;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType(getModuleType().getMachineModeText())
            .addInfo(TOOLTIP_MODULE_DESCRIPTION)
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.module.board_processor.action", TOOLTIP_CCs))
            .addSeparator()
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.module.board_processor.body.1"))
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.module.board_processor.body.2"))
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.module.board_processor.body.3"))
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.module.board_processor.body.4"))
            .addSeparator()
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.module.board_processor.body.5"))
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.module.board_processor.body.6"))
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.module.board_processor.body.7"))
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.module.board_processor.body.8"))
            .addSeparator()
            .addInfo(tooltipFlavorText(translateToLocal("GT5U.tooltip.nac.module.board_processor.flavor.1")))
            .beginStructureBlock(7, 7, 7, false)
            .addController(translateToLocal("GT5U.tooltip.nac.module.controller"))
            // Nanochip Complex Glass
            .addCasingInfoExactly(translateToLocal("gt.blockglass1.8.name"), 52, false)
            // Nanochip Reinforcement Casing
            .addCasingInfoExactly(translateToLocal("gt.blockcasings12.2.name"), 27, false)
            // Octiron Frame Box
            .addCasingInfoExactly(
                translateToLocal("gt.blockframes.10.name")
                    .replace("%material", MaterialsAlloy.OCTIRON.getLocalizedName()),
                19,
                false)
            // Nanochip Mesh Interface Casing
            .addCasingInfoExactly(translateToLocal("gt.blockcasings12.1.name"), 10, false)
            .addInputHatch(TOOLTIP_STRUCTURE_BASE_CASING)
            .addStructureInfo(TOOLTIP_STRUCTURE_BASE_VCI)
            .addStructureInfo(TOOLTIP_STRUCTURE_BASE_VCO)
            .toolTipFinisher();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBoardProcessorModule(this.mName);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipBoardProcessorRecipes;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui getGui() {
        return new MTEBoardProcessorModuleGui(this);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("impurityFluidAmount", this.impurityFluidAmount);
        aNBT.setInteger("fluidAmount", this.fluidAmount);
        aNBT.setInteger("processedItems", this.processedItems);
        aNBT.setInteger("automationPercentage", this.autoFlushPercentage);
        aNBT.setString(
            "storedFluid",
            storedFluidStack == null ? ""
                : storedFluidStack.getFluid()
                    .getName());
        aNBT.setString(
            "impurityFluid",
            this.impurityFluidStack == null ? ""
                : impurityFluidStack.getFluid()
                    .getName());
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        impurityFluidAmount = aNBT.getInteger("impurityFluidAmount");
        fluidAmount = aNBT.getInteger("fluidAmount");
        processedItems = aNBT.getInteger("processedItems");
        autoFlushPercentage = aNBT.getInteger("automationPercentage");
        if (!Objects.equals(aNBT.getString("storedFluid"), "")) {
            storedFluidStack = FluidRegistry.getFluidStack(aNBT.getString("storedFluid"), fluidAmount);
        } else {
            storedFluidStack = null;
        }
        if (!Objects.equals(aNBT.getString("impurityFluid"), "")) {
            impurityFluidStack = FluidRegistry.getFluidStack(aNBT.getString("impurityFluid"), impurityFluidAmount);
        } else {
            impurityFluidStack = null;
        }
    }

    @Override
    protected float getEUDiscountModifier() {
        return euMultiplier;
    }

    float euMultiplier = 1;

    protected int fluidCapacity = 1000000;
    protected FluidStack storedFluidStack;
    protected int fluidAmount;

    private int processedItems;

    protected FluidStack impurityFluidStack;
    protected int impurityFluidAmount;
    protected double impurityPercentage;

    private static final int IMPURITY_THRESHOLD = 1000;
    private static final int IMPURITY_INCREASE = 100;

    private int autoFlushPercentage = 100;
    private double fillPercentage = 0;

    protected static final HashSet<Fluid> LEGAL_FLUIDS = new HashSet<>(
        Arrays.asList(
            Materials.IronIIIChloride.mFluid,
            Materials.GrowthMediumSterilized.mFluid,
            Materials.BioMediumSterilized.mFluid,
            Materials.PrismaticAcid.mFluid));

    @NotNull
    @Override
    public CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
        euMultiplier = 1;

        if (storedFluidStack == null) {
            return CheckRecipeResultRegistry.NO_IMMERSION_FLUID;
        }

        if (fillPercentage < 0.5) {
            return CheckRecipeResultRegistry.NO_IMMERSION_FLUID;
        }

        if (impurityPercentage == 1) {
            return CheckRecipeResultRegistry.NO_IMMERSION_FLUID;
        }

        if (recipe.getMetadata(BoardProcessingModuleFluidKey.INSTANCE) == 1
            && !storedFluidStack.isFluidEqual(Materials.IronIIIChloride.getFluid(0))) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        if (recipe.getMetadata(BoardProcessingModuleFluidKey.INSTANCE) == 2
            && !storedFluidStack.isFluidEqual(Materials.GrowthMediumSterilized.getFluid(0))) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        if (recipe.getMetadata(BoardProcessingModuleFluidKey.INSTANCE) == 3
            && !storedFluidStack.isFluidEqual(Materials.BioMediumSterilized.getFluid(0))) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        if (recipe.getMetadata(BoardProcessingModuleFluidKey.INSTANCE) == 4
            && !storedFluidStack.isFluidEqual(Materials.PrismaticAcid.getFluid(0))) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        if (impurityPercentage <= 0.15) {
            euMultiplier = (float) (1 - 0.3 + impurityPercentage * 2);
        } else if (impurityPercentage >= 0.65) {
            euMultiplier = (float) (1 + 2 * (impurityPercentage - 0.65));
        }

        return super.validateRecipe(recipe);
    }

    @Override
    public void endRecipeProcessing() {
        if (storedFluidStack != null && impurityFluidStack != null) {
            if (mOutputItems != null) {
                processedItems += mOutputItems[0].stackSize;
                while (processedItems >= IMPURITY_THRESHOLD) {
                    processedItems -= IMPURITY_THRESHOLD;
                    impurityFluidAmount += (int) Math.min(
                        IMPURITY_INCREASE * (1 / Math.pow(fillPercentage, 1.5)),
                        fluidAmount - impurityFluidAmount);
                    impurityFluidStack.amount = impurityFluidAmount;
                    impurityPercentage = (double) impurityFluidAmount / fluidAmount;
                }
            }
        }
        super.endRecipeProcessing();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {

        if (aTick % 20 == 0) {

            if (impurityPercentage >= ((double) autoFlushPercentage / 100)) {
                flushTank();
            }

            fillTank();

        }

        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        return super.checkProcessing();
    }

    public void fillTank() {
        ArrayList<FluidStack> inputFluid = getStoredFluids();
        for (FluidStack fluid : inputFluid) {
            if (LEGAL_FLUIDS.contains(fluid.getFluid())) {
                if (storedFluidStack == null) {
                    FluidStack toDeplete = new FluidStack(fluid.getFluid(), Math.min(fluidCapacity, fluid.amount));
                    depleteInput(toDeplete);
                    storedFluidStack = toDeplete;
                } else if (storedFluidStack.isFluidEqual(fluid)) {
                    FluidStack toDeplete = new FluidStack(
                        fluid.getFluid(),
                        Math.min(fluidCapacity - storedFluidStack.amount, fluid.amount));
                    depleteInput(toDeplete);
                    storedFluidStack.amount += toDeplete.amount;
                }

                if (storedFluidStack != null) {
                    fluidAmount = storedFluidStack.amount;
                    fillPercentage = (double) fluidAmount / fluidCapacity;
                    if (storedFluidStack.isFluidEqual(Materials.IronIIIChloride.getFluid(0))) {
                        impurityFluidStack = GGMaterial.ferrousChloride.getFluidOrGas(0);
                    } else if (storedFluidStack.isFluidEqual(Materials.GrowthMediumSterilized.getFluid(0))) {
                        impurityFluidStack = Materials.GrowthMediumRaw.getFluid(0);
                    } else if (storedFluidStack.isFluidEqual(Materials.BioMediumSterilized.getFluid(0))) {
                        impurityFluidStack = Materials.BioMediumRaw.getFluid(0);
                    } else if (storedFluidStack.isFluidEqual(Materials.PrismaticAcid.getFluid(0))) {
                        impurityFluidStack = Materials.PrismaticGas.getFluid(0);
                    }
                }
            }
        }
    }

    public void flushTank() {
        if ((storedFluidStack != null) && (!mOutputHatches.isEmpty())) {
            impurityFluidAmount = fluidAmount;
            FluidStack toFlush = new FluidStack(
                impurityFluidStack.getFluid(),
                impurityFluidStack.getFluid() == Materials.PrismaticGas.mFluid ? impurityFluidAmount / 4
                    : impurityFluidAmount);
            addOutput(toFlush);
            storedFluidStack = null;
            impurityFluidStack = null;
            fluidAmount = 0;
            impurityFluidAmount = 0;
            impurityPercentage = 0;
            fillPercentage = 0;
        }
    }

    public FluidStack getStoredFluidStack() {
        return storedFluidStack;
    }

    public void setStoredFluidStack(FluidStack fluid) {
        this.storedFluidStack = fluid;
    }

    public int getCapacity() {
        return fluidCapacity;
    }

    public int getProcessedItems() {
        return processedItems;
    }

    public FluidStack getImpurityFluidStack() {
        return impurityFluidStack;
    }

    public void setImpurityFluidStack(FluidStack impurityFluidStack) {
        this.impurityFluidStack = impurityFluidStack;
    }

    public double getImpurityPercentage() {
        return impurityPercentage;
    }

    public float getEuMultiplier() {
        return euMultiplier;
    }

    public int getAutoFlushPercentage() {
        return autoFlushPercentage;
    }

    public void setAutoFlushPercentage(int autoFlushPercentage) {
        this.autoFlushPercentage = autoFlushPercentage;
    }
}

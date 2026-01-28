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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

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
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
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

    public int structureOffsetX() { return BOARD_OFFSET_X; }

    public int structureOffsetY() { return BOARD_OFFSET_Y; }

    public int structureOffsetZ() { return BOARD_OFFSET_Z; }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType(getModuleType().getMachineModeText())
            .addInfo(TOOLTIP_MODULE_DESCRIPTION)
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.module.board_processor.action", TOOLTIP_CCs))
            .addSeparator()
            .addInfo(translateToLocalFormatted("GT5U.tooltip.nac.module.board_processor.body1")) // todo mechanic text
                                                                                                 // NOC!!!!!!!!!!!!1
            .addSeparator()
            .addInfo(tooltipFlavorText(translateToLocal("GT5U.tooltip.nac.module.board_processor.flavor.1"))) // todo
                                                                                                              // flavor
                                                                                                              // text
            .addInputHatch(TOOLTIP_STRUCTURE_BASE_CASING)
            .addStructureInfo(TOOLTIP_STRUCTURE_BASE_VCI)
            .addStructureInfo(TOOLTIP_STRUCTURE_BASE_VCO)
            .toolTipFinisher();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBoardProcessorModule(this.mName);
    }

    public static void registerLocalName(String unprocessedName, CircuitComponent component) {
        component.fallbackLocalizedName = unprocessedName + " Die";
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
        aNBT.setInteger("impurityFluidAmount", this.ImpurityFluidAmount);
        aNBT.setInteger("fluidAmount", this.FluidAmount);
        aNBT.setInteger("processedItems", this.ProcessedItems);
        aNBT.setInteger("automationPercentage", this.AutomationPercentage);
        aNBT.setString(
            "storedFluid",
            StoredFluid == null ? ""
                : StoredFluid.getFluid()
                    .getName());
        aNBT.setString(
            "impurityFluid",
            this.ImpurityFluid == null ? ""
                : ImpurityFluid.getFluid()
                    .getName());
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        ImpurityFluidAmount = aNBT.getInteger("impurityFluidAmount");
        FluidAmount = aNBT.getInteger("fluidAmount");
        ProcessedItems = aNBT.getInteger("processedItems");
        AutomationPercentage = aNBT.getInteger("automationPercentage");
        if (!Objects.equals(aNBT.getString("storedFluid"), "")) {
            StoredFluid = FluidRegistry.getFluidStack(aNBT.getString("storedFluid"), FluidAmount);
        } else {
            StoredFluid = null;
        }
        if (!Objects.equals(aNBT.getString("impurityFluid"), "")) {
            ImpurityFluid = FluidRegistry.getFluidStack(aNBT.getString("impurityFluid"), ImpurityFluidAmount);
        } else {
            ImpurityFluid = null;
        }
    }

    @Override
    protected float getEUDiscountModifier() {
        return euMultiplier;
    }

    float euMultiplier = 1;

    protected int Capacity = 10000;
    protected FluidStack StoredFluid;
    protected int FluidAmount;

    private int ProcessedItems;

    protected FluidStack ImpurityFluid;
    protected int ImpurityFluidAmount;
    protected double ImpurityPercentage;

    private int ImpurityThreshold = 1000;
    private final int ImpurityIncrease = 100;

    private int AutomationPercentage = 100;
    private double FillPercentage = 0;

    protected static final HashSet<Fluid> LegalFluids = new HashSet<>(Arrays.asList(Materials.IronIIIChloride.mFluid));

    @NotNull
    @Override
    public CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
        euMultiplier = 1;

        if (StoredFluid == null) {
            return CheckRecipeResultRegistry.NO_IMMERSION_FLUID;
        }

        if (ImpurityPercentage == 1) {
            return CheckRecipeResultRegistry.NO_IMMERSION_FLUID;
        }

        if (recipe.getMetadata(BoardProcessingModuleFluidKey.INSTANCE) == 1
            && !StoredFluid.isFluidEqual(Materials.IronIIIChloride.getFluid(0))) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        if (ImpurityPercentage <= 0.15) {
            euMultiplier = (float) (1 - 0.3 + ImpurityPercentage * 2);
        } else if (ImpurityPercentage >= 0.65) {
            euMultiplier = (float) (1 + 2 * (ImpurityPercentage - 0.65));
        }

        return super.validateRecipe(recipe);
    }

    @Override
    public void endRecipeProcessing() {
        if (StoredFluid != null && ImpurityFluid != null) {
            if (mOutputItems != null) {
                ProcessedItems += mOutputItems[0].stackSize;
                while (ProcessedItems >= ImpurityThreshold) {
                    ProcessedItems -= ImpurityThreshold;
                    ImpurityFluidAmount += (int) Math
                        .min(ImpurityIncrease * (1 / Math.pow(FillPercentage, 1.5)), FluidAmount - ImpurityFluidAmount);
                    ImpurityFluid.amount = ImpurityFluidAmount;
                    ImpurityPercentage = (double) ImpurityFluidAmount / FluidAmount;
                }
            }
        }
        super.endRecipeProcessing();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {

        if (aTick % 20 == 0) {

            if (ImpurityPercentage >= ((double) AutomationPercentage / 100)) {
                FlushTank();
            }

            if (StoredFluid == null) {
                FillTank();
            }

        }

        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        return super.checkProcessing();
    }

    public void FillTank() {
        if (StoredFluid == null) {
            ArrayList<FluidStack> inputFluid = getStoredFluids();
            for (FluidStack fluid : inputFluid) {
                if (LegalFluids.contains(fluid.getFluid())) {
                    if (fluid.amount >= Capacity / 2) {
                        FluidStack toDeplete = new FluidStack(fluid.getFluid(), Math.min(Capacity, fluid.amount));
                        depleteInput(toDeplete);
                        StoredFluid = toDeplete;
                        FluidAmount = toDeplete.amount;
                        FillPercentage = (double) FluidAmount / Capacity;
                        if (StoredFluid.isFluidEqual(Materials.IronIIIChloride.getFluid(0))) {
                            ImpurityFluid = GGMaterial.ferrousChloride.getFluidOrGas(0);
                        }
                    }
                }
            }
        }
    }

    public void FlushTank() {
        if ((StoredFluid != null) && (!mOutputHatches.isEmpty())) {
            ImpurityFluidAmount = FluidAmount;
            FluidStack toFlush = new FluidStack(ImpurityFluid.getFluid(), ImpurityFluidAmount);
            addOutput(toFlush);
            StoredFluid = null;
            ImpurityFluid = null;
            FluidAmount = 0;
            ImpurityFluidAmount = 0;
            ImpurityPercentage = 0;
            FillPercentage = 0;
        }
    }

    public FluidStack getStoredFluid() {
        return StoredFluid;
    }

    public void setStoredFluid(FluidStack fluid) {
        this.StoredFluid = fluid;
    }

    public int getCapacity() {
        return Capacity;
    }

    public int getProcessedItems() {
        return ProcessedItems;
    }

    public FluidStack getImpurityFluid() {
        return ImpurityFluid;
    }

    public void setImpurityFluid(FluidStack impurityFluid) {
        ImpurityFluid = impurityFluid;
    }

    public double getImpurityPercentage() {
        return ImpurityPercentage;
    }

    public float getEuMultiplier() {
        return euMultiplier;
    }

    public int getAutomationPercentage() {
        return AutomationPercentage;
    }

    public void setAutomationPercentage(int automationPercentage) {
        AutomationPercentage = automationPercentage;
    }
}

package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofCoil;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.util.GTStreamUtil;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrorRegistry;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.pollution.PollutionConfig;

public class MTEIndustrialDehydrator extends MTEExtendedPowerMultiBlockBase<MTEIndustrialDehydrator>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private HeatingCoilLevel heatingCapacity;
    private int casingCount;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    // spotless:off
    private static final String[][] structure =
        {   { "CCC", "CCC", "CCC" },
            { "HHH", "H-H", "HHH" },
            { "HHH", "H-H", "HHH" },
            { "HHH", "H-H", "HHH" },
            { "C~C", "CCC", "CCC" },};
    // spotless:on
    private static final int OFFSET_X = 1;
    private static final int OFFSET_Y = 4;
    private static final int OFFSET_Z = 0;
    private static IStructureDefinition<MTEIndustrialDehydrator> STRUCTURE_DEFINITION = null;
    private static final int MACHINE_MODE_VACUUM_FURNACE = 0;
    private static final int MACHINE_MODE_DEHYDRATOR = 1;

    public MTEIndustrialDehydrator(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialDehydrator(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialDehydrator(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Vacuum Furnace, Dehydrator")
            .addInfo("Factory Grade Vacuum Furnace")
            .addStaticParallelInfo(4)
            .addStaticSpeedInfo(2.2f)
            .addStaticEuEffInfo(0.5f)
            .addInfo("Can toggle the operation temperature with a Screwdriver")
            .addInfo("All Dehydrator recipes are Low Temp recipes")
            .addInfo(
                "Reduces " + TooltipHelper.effText("EU Usage")
                    + " by "
                    + EnumChatFormatting.WHITE
                    + "5%"
                    + EnumChatFormatting.GRAY
                    + " every "
                    + EnumChatFormatting.RED
                    + "900K"
                    + EnumChatFormatting.GRAY
                    + " above the recipe requirement")
            .addInfo(
                "Every " + EnumChatFormatting.RED
                    + "1800K"
                    + EnumChatFormatting.GRAY
                    + " over the recipe requirement grants 1 "
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "Perfect Overclock")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 5, 3, true)
            .addController("Front bottom center")
            .addCasing("5-12", "Vacuum Casing", false)
            .addCasing("24", "Heating Coil", true)
            .addEnergyHatch("1+", "Any casing", 1)
            .addMaintenanceHatch("1", "Any casing", 1)
            .addMufflerHatch("1", "Any casing", 1)
            .addInputAny("1+", "Any casing", 1)
            .addOutputAny("1+", "Any casing", 1)
            .addAir("Interior of the structure")
            .addStructureInfo("")
            .addSubChannel(GTStructureChannels.HEATING_COIL)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialDehydrator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialDehydrator>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(structure))
                .addElement(
                    'C',
                    buildHatchAdder(MTEIndustrialDehydrator.class)
                        .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch, OutputHatch)
                        .casingIndex(Casings.VacuumCasing.textureId)
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.casingCount, Casings.VacuumCasing.asElement())))
                .addElement(
                    'H',
                    GTStructureChannels.HEATING_COIL.use(
                        activeCoils(
                            ofCoil(MTEIndustrialDehydrator::setCoilLevel, MTEIndustrialDehydrator::getCoilLevel))))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 4, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_X,
            OFFSET_Y,
            OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        casingCount = 0;
        setCoilLevel(HeatingCoilLevel.None);
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 1, 4, 0, errors)) return;
        checkCasingMin(errors, casingCount, 4);
        if (getCoilLevel() == HeatingCoilLevel.None) {
            errors.add(StructureErrorRegistry.COIL_LEVEL_NOT_ENOUGH);
        }
        checkHasEnergyHatch(errors);
        checkHasMaintenanceHatch(errors);
        checkHasMufflerHatch(errors);
        checkHasAnyInput(errors);
        checkHasAnyOutput(errors);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing, int colorIndex, boolean active, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            facing,
            active,
            TexturesGtBlock.oMCAIndustrialDehydrator,
            TexturesGtBlock.oMCAIndustrialDehydratorGlow,
            TexturesGtBlock.oMCAIndustrialDehydratorActive,
            TexturesGtBlock.oMCAIndustrialDehydratorActiveGlow
            );
    }

    @Override
    public ITexture getCasingTexture() {
        return Casings.VacuumCasing.getCasingTexture();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return (machineMode == MACHINE_MODE_VACUUM_FURNACE) ? RecipeMaps.vacuumFurnaceRecipes
            : RecipeMaps.chemicalDehydratorNonCellRecipes;
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.chemicalDehydratorNonCellRecipes, RecipeMaps.vacuumFurnaceRecipes);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                return recipe.mSpecialValue <= getCoilLevel().getHeat() ? CheckRecipeResultRegistry.SUCCESSFUL
                    : CheckRecipeResultRegistry.insufficientHeat(recipe.mSpecialValue);
            }

            @NotNull
            @Override
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).setHeatOC(true)
                    .setHeatDiscount(true)
                    .setRecipeHeat(recipe.mSpecialValue)
                    .setMachineHeat((int) getCoilLevel().getHeat());
            }
        }.noRecipeCaching()
            .setSpeedBonus(1F / 2.2F)
            .setEuModifier(0.5F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return 4;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialDehydrator;
    }

    public HeatingCoilLevel getCoilLevel() {
        return heatingCapacity;
    }

    public void setCoilLevel(HeatingCoilLevel coilLevel) {
        heatingCapacity = coilLevel;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ, ItemStack aTool) {
        setMachineMode(nextMachineMode());
        GTUtility
            .sendChatTrans(aPlayer, "GT5U.MULTI_MACHINE_CHANGE", new ChatComponentTranslation(getMachineModeKey()));
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsMachineModeSwitch() {
        return true;
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_STEAM);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID);
    }

    @Override
    public String getMachineModeKey() {
        return "GT5U.GTPP_MULTI_INDUSTRIAL_DEHYDRATOR.mode." + machineMode;
    }

    @Override
    public void loadNBTData(NBTTagCompound NBT) {
        // Migrates old NBT tag to the new one
        if (NBT.hasKey("mDehydratorMode")) {
            machineMode = NBT.getBoolean("mDehydratorMode") ? MACHINE_MODE_DEHYDRATOR : MACHINE_MODE_VACUUM_FURNACE;
        }
        super.loadNBTData(NBT);
    }

    @Override
    public void getExtraWailaNBT(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        tag.setString("mode", getMachineModeName());
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEMultiBlockBaseGui<>(this).withMachineModeIcons(
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_STEAM,
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID);
    }
}

package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
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
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.TAE;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEIndustrialDehydrator extends GTPPMultiBlockBase<MTEIndustrialDehydrator>
    implements ISurvivalConstructable {

    private static int CASING_TEXTURE_ID;
    private static final String mCasingName = "Vacuum Casing";
    private HeatingCoilLevel mHeatingCapacity;
    private int mCasing;
    private static IStructureDefinition<MTEIndustrialDehydrator> STRUCTURE_DEFINITION = null;
    private static final int MACHINEMODE_VACUUMFURNACE = 0;
    private static final int MACHINEMODE_DEHYDRATOR = 1;

    public MTEIndustrialDehydrator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        CASING_TEXTURE_ID = TAE.getIndexFromPage(3, 10);
    }

    public MTEIndustrialDehydrator(String aName) {
        super(aName);
        CASING_TEXTURE_ID = TAE.getIndexFromPage(3, 10);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialDehydrator(mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
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
            .addController("Bottom Center")
            .addCasingInfoMin(mCasingName, 5, false)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addOutputHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addMufflerHatch("Any Casing", 1)
            .addSubChannelUsage(GTStructureChannels.HEATING_COIL)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialDehydrator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialDehydrator>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "CCC", "CCC", "CCC" }, { "HHH", "H-H", "HHH" }, { "HHH", "H-H", "HHH" },
                            { "HHH", "H-H", "HHH" }, { "C~C", "CCC", "CCC" }, }))
                .addElement(
                    'C',
                    buildHatchAdder(MTEIndustrialDehydrator.class)
                        .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch, OutputHatch)
                        .casingIndex(CASING_TEXTURE_ID)
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings4Misc, 10))))
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
        buildPiece(mName, stackSize, hintsOnly, 1, 4, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, 1, 4, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        setCoilLevel(HeatingCoilLevel.None);
        return checkPiece(mName, 1, 4, 0) && mCasing >= 5 && getCoilLevel() != HeatingCoilLevel.None && checkHatch();
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCAIndustrialDehydratorActive;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.oMCAIndustrialDehydratorActiveGlow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCAIndustrialDehydrator;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.oMCAIndustrialDehydratorGlow;
    }

    @Override
    protected int getCasingTextureId() {
        return CASING_TEXTURE_ID;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return (machineMode == MACHINEMODE_VACUUMFURNACE) ? GTPPRecipeMaps.vacuumFurnaceRecipes
            : GTPPRecipeMaps.chemicalDehydratorNonCellRecipes;
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(GTPPRecipeMaps.chemicalDehydratorNonCellRecipes, GTPPRecipeMaps.vacuumFurnaceRecipes);
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialDehydrator;
    }

    @Override
    public String getMachineType() {
        return "Vacuum Furnace, Dehydrator";
    }

    @Override
    public int getMaxParallelRecipes() {
        return 4;
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
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        setMachineMode(nextMachineMode());
        GTUtility
            .sendChatToPlayer(aPlayer, translateToLocalFormatted("GT5U.MULTI_MACHINE_CHANGE", getMachineModeName()));
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
    public String getMachineModeName() {
        return translateToLocal("GT5U.GTPP_MULTI_INDUSTRIAL_DEHYDRATOR.mode." + machineMode);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        // Migrates old NBT tag to the new one
        if (aNBT.hasKey("mDehydratorMode")) {
            machineMode = aNBT.getBoolean("mDehydratorMode") ? MACHINEMODE_DEHYDRATOR : MACHINEMODE_VACUUMFURNACE;
        }
        super.loadNBTData(aNBT);
    }

    public HeatingCoilLevel getCoilLevel() {
        return mHeatingCapacity;
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        mHeatingCapacity = aCoilLevel;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setString("mode", getMachineModeName());
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui getGui() {
        return new MTEMultiBlockBaseGui(this).withMachineModeIcons(
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_STEAM,
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID);
    }
}

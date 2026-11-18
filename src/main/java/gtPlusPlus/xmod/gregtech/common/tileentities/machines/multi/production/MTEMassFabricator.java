package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

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
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableMap;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.TAE;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

@IMetaTileEntity.SkipGenerateDescription
public class MTEMassFabricator extends GTPPMultiBlockBase<MTEMassFabricator> implements ISurvivalConstructable {

    private static final int MODE_SCRAP = 1;
    private static final int MODE_UU = 0;
    private static final int SCRAP_PARALLELS = 64;
    private static final int UU_PARALLELS_PER_TIER = 8;
    // Chance out of 10000, so 1000 is 10%
    private static final int SCRAP_CHANCE_PER_TIER = 1000;

    private int mCasing;
    private static IStructureDefinition<MTEMassFabricator> STRUCTURE_DEFINITION = null;

    public MTEMassFabricator(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMassFabricator(final String aName) {
        super(aName);
    }

    @Override
    public String getMachineType() {
        return StatCollector.translateToLocal("gt.mbtt.machine_type.mass_fabricator");
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addMarkdown(
                new ResourceLocation("gregtech", "mass-fabricator"),
                ImmutableMap.of(
                    "scrap_parallels",
                    SCRAP_PARALLELS,
                    "uu_parallels",
                    UU_PARALLELS_PER_TIER,
                    "scrap_chance",
                    SCRAP_CHANCE_PER_TIER / 100))
            .addStaticSpeedInfo(1f)
            .addStaticEuEffInfo(0.8f)
            .addPerfectOCInfo()
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(5, 4, 5, true)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_bottom_center"))
            .addCasing("35-44", Casings.MatterFabricatorCasing.getLocalizedName(), false)
            .addCasing("24", Casings.ContainmentCasing.getLocalizedName(), false)
            .addCasing("9", Casings.MatterGenerationCoil.getLocalizedName(), false)
            .addEnergyHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_fabricator_casing"), 1)
            .addMaintenanceHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_fabricator_casing"), 1)
            .addMufflerHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_fabricator_casing"), 1)
            .addInputBus("0+", StatCollector.translateToLocal("gt.mbtt.structure.any_fabricator_casing"), 1)
            .addInputHatch("0+", StatCollector.translateToLocal("gt.mbtt.structure.any_fabricator_casing"), 1)
            .addOutputAny("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_fabricator_casing"), 1)
            .addAir(StatCollector.translateToLocal("gt.mbtt.structure.interior"))
            .toolTipFinisher();
        return tt;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.Overlay_MatterFab_Active_Animated;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.Overlay_MatterFab_Active_Animated_Glow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.Overlay_MatterFab_Animated;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.Overlay_MatterFab_Animated_Glow;
    }

    @Override
    protected int getCasingTextureId() {
        return TAE.GTPP_INDEX(9);
    }

    @Override
    public IStructureDefinition<MTEMassFabricator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEMassFabricator>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "CCCCC", "CCCCC", "CCCCC", "CCCCC", "CCCCC" },
                            { "CGGGC", "G---G", "G---G", "G---G", "CGGGC" },
                            { "CGGGC", "G---G", "G---G", "G---G", "CGGGC" },
                            { "CC~CC", "CHHHC", "CHHHC", "CHHHC", "CCCCC" }, }))
                .addElement(
                    'C',
                    buildHatchAdder(MTEMassFabricator.class)
                        .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy, Muffler)
                        .casingIndex(TAE.GTPP_INDEX(9))
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasingsMisc, 9))))
                .addElement('H', ofBlock(ModBlocks.blockCasingsMisc, 8))
                .addElement('G', ofBlock(ModBlocks.blockCasings3Misc, 15))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 2, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, 2, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        mCasing = 0;
        if (!checkPiece(mName, 2, 3, 0, errors)) return;
        checkCasingMin(errors, mCasing, 35);
        checkHasEnergyHatch(errors);
        checkHasMaintenanceHatch(errors);
        checkHasMufflerHatch(errors);

        if (machineMode == MODE_SCRAP) {
            checkHasOutputBus(errors);
        } else {
            checkHasOutputHatch(errors);
        }
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiMassFabricator;
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEMassFabricator(this.mName);
    }

    /**
     * Special Recipe Handling
     */
    @Override
    public RecipeMap<?> getRecipeMap() {
        return machineMode == MODE_SCRAP ? RecipeMaps.recyclerRecipes : RecipeMaps.multiblockMassFabricatorRecipes;
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.recyclerRecipes, RecipeMaps.multiblockMassFabricatorRecipes);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (machineMode == MODE_SCRAP) {
                    if (recipe.mOutputs == null) {
                        return SimpleCheckRecipeResult.ofSuccess("no_scrap");
                    }
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Nonnull
            @Override
            protected Stream<GTRecipe> findRecipeMatches(@Nullable RecipeMap<?> map) {
                if (machineMode == MODE_SCRAP) {
                    if (inputItems != null) {
                        for (ItemStack item : inputItems) {
                            if (item == null || item.stackSize == 0) continue;
                            ItemStack aPotentialOutput = GTModHandler
                                .getRecyclerOutput(GTUtility.copyAmount(1, item), 0);
                            GTRecipe recipe = new GTRecipe(
                                new ItemStack[] { GTUtility.copyAmount(1, item) },
                                aPotentialOutput == null ? null
                                    : new ItemStack[] { aPotentialOutput, aPotentialOutput },
                                null,
                                null,
                                new int[] { 1250 + GTUtility.getTier(getMaxInputVoltage()) * SCRAP_CHANCE_PER_TIER,
                                    Math.max(
                                        1250 + GTUtility.getTier(getMaxInputVoltage()) * SCRAP_CHANCE_PER_TIER - 10000,
                                        0) },
                                null,
                                null,
                                null,
                                null,
                                40,
                                (int) TierEU.RECIPE_LV,
                                0);
                            return Stream.of(recipe);
                        }
                    }
                    return Stream.empty();
                }
                return super.findRecipeMatches(map);
            }
        }.setEuModifier(0.8D)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    protected void setupProcessingLogic(ProcessingLogic logic) {
        super.setupProcessingLogic(logic);
        logic.enablePerfectOverclock();
    }

    @Override
    public int getMaxParallelRecipes() {
        return machineMode == MODE_SCRAP ? SCRAP_PARALLELS
            : UU_PARALLELS_PER_TIER * (Math.max(1, GTUtility.getTier(getMaxInputVoltage())));
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        setMachineMode(nextMachineMode());
        GTUtility
            .sendChatTrans(aPlayer, "GT5U.MULTI_MACHINE_CHANGE", new ChatComponentTranslation(getMachineModeKey()));
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey("mMode")) {
            machineMode = aNBT.getInteger("mMode");
            aNBT.removeTag("mMode");
        }
        super.loadNBTData(aNBT);
    }

    @Override
    public void getExtraWailaNBT(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        tag.setString("mode", getMachineModeName());
    }

    @Override
    public String getMachineModeKey() {
        return "GT5U.GTPP_MULTI_MASS_FABRICATOR.mode." + machineMode;
    }

    @Override
    public boolean supportsMachineModeSwitch() {
        return true;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEMultiBlockBaseGui<>(this).withMachineModeIcons(
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_MASS_FABRICATING,
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_RECYCLING);
    }
}

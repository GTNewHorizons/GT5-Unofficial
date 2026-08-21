package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

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
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
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
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEMassFabricator extends MTEExtendedPowerMultiBlockBase<MTEMassFabricator>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private static final int MODE_SCRAP = 1;

    private int casingAmount;
    private static final String[][] structure = { { "CCCCC", "CCCCC", "CCCCC", "CCCCC", "CCCCC" },
        { "CGGGC", "G---G", "G---G", "G---G", "CGGGC" }, { "CGGGC", "G---G", "G---G", "G---G", "CGGGC" },
        { "CC~CC", "CHHHC", "CHHHC", "CHHHC", "CCCCC" }, };
    private static IStructureDefinition<MTEMassFabricator> STRUCTURE_DEFINITION = null;
    private static final String STRUCTURE_PIECE_MAIN = "main";

    public MTEMassFabricator(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMassFabricator(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEMassFabricator(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Mass Fabricator, Recycler")
            .addInfo(
                "Parallel: Scrap = " + TooltipHelper.parallelText(64)
                    + " | UU = "
                    + TooltipHelper.parallelText(8)
                    + " per "
                    + TooltipHelper.tierText("Voltage")
                    + " Tier")
            .addStaticSpeedInfo(1f)
            .addStaticEuEffInfo(0.8f)
            .addInfo("Produces UU-A, UU-M & Scrap")
            .addInfo("Change mode with screwdriver")
            .addInfo(
                EnumChatFormatting.LIGHT_PURPLE + "+10%"
                    + EnumChatFormatting.GRAY
                    + " scrap chance per "
                    + TooltipHelper.tierText("Voltage")
                    + " Tier in recycler mode")
            .addPerfectOCInfo()
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(5, 4, 5, true)
            .addController("Front bottom center")
            .addCasing("35-44", "Matter Fabricator Casing", false)
            .addCasing("24", "Containment Casing", false)
            .addCasing("9", "Matter Generation Coil", false)
            .addEnergyHatch("1+", "Any fabricator casing", 1)
            .addMaintenanceHatch("1", "Any fabricator casing", 1)
            .addMufflerHatch("1", "Any fabricator casing", 1)
            .addInputBus("0+", "Any fabricator casing", 1)
            .addInputHatch("0+", "Any fabricator casing", 1)
            .addOutputAny("1+", "Any fabricator casing", 1)
            .addAir("Interior of the structure")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEMassFabricator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEMassFabricator>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(structure))
                .addElement(
                    'C',
                    buildHatchAdder(MTEMassFabricator.class)
                        .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy, Muffler)
                        .casingIndex(Casings.MatterFabricatorCasing.textureId)
                        .hint(1)
                        .buildAndChain(
                            onElementPass(x -> ++x.casingAmount, Casings.MatterFabricatorCasing.asElement())))
                .addElement('G', Casings.ContainmentCasing.asElement())
                .addElement('H', Casings.MatterGenerationCoil.asElement())
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 2, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        casingAmount = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 2, 3, 0, errors)) return;
        checkCasingMin(errors, casingAmount, 35);
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
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            facing,
            active,
            TexturesGtBlock.Overlay_MatterFab,
            TexturesGtBlock.Overlay_MatterFab_Glow,
            TexturesGtBlock.Overlay_MatterFab_Active,
            TexturesGtBlock.Overlay_MatterFab_Active_Glow);
    }

    @Override
    public ITexture getCasingTexture() {
        return Casings.MatterFabricatorCasing.getCasingTexture();
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
                                new int[] { 1250 + GTUtility.getTier(getMaxInputVoltage()) * 1000,
                                    Math.max(1250 + GTUtility.getTier(getMaxInputVoltage()) * 1000 - 10000, 0) },
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
        }.setEuModifier(0.8F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    protected void setupProcessingLogic(ProcessingLogic logic) {
        super.setupProcessingLogic(logic);
        logic.enablePerfectOverclock();
    }

    @Override
    public int getMaxParallelRecipes() {
        return machineMode == MODE_SCRAP ? 64 : 8 * (Math.max(1, GTUtility.getTier(getMaxInputVoltage())));
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiMassFabricator;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        setMachineMode(nextMachineMode());
        GTUtility
            .sendChatTrans(aPlayer, "GT5U.MULTI_MACHINE_CHANGE", new ChatComponentTranslation(getMachineModeKey()));
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
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEMultiBlockBaseGui<>(this).withMachineModeIcons(
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_MASS_FABRICATING,
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_RECYCLING);
    }
}

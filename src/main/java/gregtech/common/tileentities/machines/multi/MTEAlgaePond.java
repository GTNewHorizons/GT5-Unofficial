package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofAnyWater;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cofh.asmhooks.block.BlockTickingWater;
import cofh.asmhooks.block.BlockWater;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.casing.Casings;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTStreamUtil;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEAlgaePond extends MTEExtendedPowerMultiBlockBase<MTEAlgaePond> implements ISurvivalConstructable {

    private static final int OFFSET_X = 1;
    private static final int OFFSET_Y = 3;
    private static final int OFFSET_Z = 0;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private int tier = -1;
    private int glassTier = -1;
    private int casingAmount;
    private static final String[][] shape = { { "BBB", "BCB", "BCB", "B~B", "BBB", "B B" },
        { "AAA", "AEA", "AEA", "AEA", " A ", "   " }, { " A ", "AEA", "ADA", "AEA", " A ", "   " },
        { " A ", "AEA", "ADA", "AEA", " A ", "   " }, { " A ", "AEA", "ADA", "AEA", " A ", "   " },
        { " A ", "AEA", "ADA", "AEA", " A ", "   " }, { " A ", "AEA", "ADA", "AEA", " A ", "   " },
        { " A ", "AEA", "ADA", "AEA", " A ", "   " }, { "AAA", "AEA", "AEA", "AEA", " A ", "   " },
        { "BBB", "BCB", "BCB", "BCB", "BBB", "B B" } };

    private static IStructureDefinition<MTEAlgaePond> STRUCTURE_DEFINITION = null;

    public MTEAlgaePond(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEAlgaePond(final String aName) {
        super(aName);
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEAlgaePond(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Algae Pond")
            .addInfo("Grows Algae!")
            .addInfo("Provide compost to boost production by one tier")
            .addInfo("Machine tier is equal to the highest energy hatch tier, capped by glass tier")
            .addInfo(
                GTUtility.getColoredTierNameFromTier((byte) 12) + EnumChatFormatting.GRAY
                    + "-glass unlocks all above energy tiers")
            .addInfo("Accepts exactly 1 Energy Hatch. Does not need Maintenance")
            .addInfo("Fill Input Hatch with Water to fill the inside of the multiblock")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 6, 10, false)
            .addController("Front center, 3rd layer")
            .addCasingInfoMin("Algae Casing", 20, false)
            .addCasingInfoExactly("Stainless Steel Frame Box", 6, false)
            .addCasingInfoExactly("Any Tiered Glass", 68, true)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addStructureAuthors(EnumChatFormatting.GOLD + "IX")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEAlgaePond> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEAlgaePond>builder()
                .addShape(STRUCTURE_PIECE_MAIN, shape)
                .addElement('A', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
                .addElement(
                    'B',
                    buildHatchAdder(MTEAlgaePond.class).atLeast(InputBus, OutputBus, Energy, InputHatch)
                        .casingIndex(Casings.AlgaeCasing.textureId)
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.casingAmount, Casings.AlgaeCasing.asElement())))
                .addElement('C', Casings.FilterMachineCasing.asElement())
                .addElement('D', ofFrame(Materials.StainlessSteel))
                .addElement('E', ofChain(isAir(), ofAnyWater(false)))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
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
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmount = 0;
        glassTier = -1;
        tier = -1;

        if (checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z) && casingAmount >= 20
            && !mInputHatches.isEmpty()
            && !mOutputBusses.isEmpty()
            && mEnergyHatches.size() == 1) {
            int inputTier = (int) getInputVoltageTier();
            if (glassTier < VoltageIndex.UMV && inputTier > glassTier) {
                return false;
            }
            tier = inputTier;
            return true;
        }
        return false;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.GTCEU_LOOP_BATH;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Casings.AlgaeCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(TexturesGtBlock.oMCDAlgaePondBaseActive)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDAlgaePondBaseActiveGlow)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.AlgaeCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(TexturesGtBlock.oMCDAlgaePondBase)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDAlgaePondBaseGlow)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.AlgaeCasing.getCasingTexture() };
    }

    public boolean checkForWater() {
        IGregTechTileEntity base = this.getBaseMetaTileEntity();
        boolean allFilled = true;

        for (int localZ = 0; localZ < shape.length; localZ++) {
            String[] rows = shape[localZ];
            for (int localY = 0; localY < rows.length; localY++) {
                String row = rows[localY];
                for (int localX = 0; localX < row.length(); localX++) {
                    if (row.charAt(localX) != 'E') continue;

                    int[] abc = new int[] { localX - OFFSET_X, localY - OFFSET_Y, localZ - OFFSET_Z };
                    int[] xyz = new int[] { 0, 0, 0 };
                    getExtendedFacing().getWorldOffset(abc, xyz);

                    int worldX = base.getXCoord() + xyz[0];
                    int worldY = base.getYCoord() + xyz[1];
                    int worldZ = base.getZCoord() + xyz[2];

                    Block block = base.getWorld()
                        .getBlock(worldX, worldY, worldZ);
                    boolean isCOFHWater = Mods.COFHCore.isModLoaded()
                        && (block instanceof BlockWater || block instanceof BlockTickingWater);
                    if (block == Blocks.water || isCOFHWater) {
                        continue;
                    }

                    allFilled = false;

                    boolean isAir = block == Blocks.air || block == Blocks.flowing_water;
                    if (!isAir) {
                        return false;
                    }

                    ArrayList<FluidStack> stored = this.getStoredFluids();
                    if (stored == null) {
                        return false;
                    }

                    boolean filled = false;
                    for (FluidStack fs : stored) {
                        if (!fs.isFluidEqual(Materials.Water.getFluid(1))) {
                            continue;
                        }
                        if (fs.amount < 1000) {
                            continue;
                        }

                        fs.amount -= 1000;
                        base.getWorld()
                            .setBlock(worldX, worldY, worldZ, Blocks.water, 0, 3);
                        filled = true;
                        break;
                    }

                    if (!filled) {
                        allFilled = false;
                    }
                }
            }
        }
        return allFilled;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiAlgaePond;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        // Silly Client Syncing
        if (aBaseMetaTileEntity.isClientSide()) {
            this.tier = (int) getInputVoltageTier();
        }
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Nonnull
            @Override
            protected Stream<GTRecipe> findRecipeMatches(@Nullable RecipeMap<?> map) {
                return GTStreamUtil.ofNullable(getRecipe(tier, inputItems));
            }

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (!checkForWater()) {
                    return SimpleCheckRecipeResult.ofFailure("no_water");
                }
                long recipeEUt = getRecipeEUt();
                if (recipeEUt > availableVoltage * availableAmperage) {
                    return CheckRecipeResultRegistry.insufficientPower(recipeEUt);
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Nonnull
            @Override
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                return OverclockCalculator.ofNoOverclock(getRecipeEUt(), recipe.mDuration);
            }
        }.setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.algaePondRecipes;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_ALGAE_LOOP;
    }

    private long getRecipeEUt() {
        if (tier < 0 || tier >= GTValues.V.length) {
            return 0;
        }
        return GTValues.V[tier] * 9L / 10L;
    }

    private static GTRecipe getRecipe(int tier, ItemStack[] itemInputs) {
        if (tier == -1) return null;

        GTRecipe matchingRecipe = null;

        final ItemStack[] inputs;

        if (isUsingCompost(tier, itemInputs)) {
            inputs = new ItemStack[] { GregtechItemList.Compost.get(compostForTier(tier)) };
            tier++;
        } else {
            inputs = GTValues.emptyItemStackArray;
        }

        for (GTRecipe recipe : GTPPRecipeMaps.algaePondRecipes.getAllRecipes()) {
            // We assume the unicity of tiered recipes
            if (recipe.mSpecialValue == tier) {
                matchingRecipe = recipe.copyShallow();
                matchingRecipe.mInputs = inputs;
                break;
            }
        }

        return matchingRecipe;
    }

    private static boolean isUsingCompost(int tier, ItemStack[] itemInputs) {
        ItemStack aCompost = GregtechItemList.Compost.get(1);
        final int compostForTier = compostForTier(tier);
        int compostFound = 0;
        for (ItemStack i : itemInputs) {
            if (GTUtility.areStacksEqual(aCompost, i)) {
                compostFound += i.stackSize;
                if (compostFound >= compostForTier) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int compostForTier(int aTier) {
        return aTier > 1 ? (int) Math.min(64, GTUtility.powInt(2, aTier - 1)) : 1;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }
}

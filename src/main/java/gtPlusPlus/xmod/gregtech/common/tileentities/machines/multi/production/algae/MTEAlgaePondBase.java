package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.algae;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllCasings;

import java.util.ArrayList;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cofh.asmhooks.block.BlockWater;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTStreamUtil;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEAlgaePondBase extends GTPPMultiBlockBase<MTEAlgaePondBase> implements ISurvivalConstructable {

    private int tier = -1;
    private int mCasing;
    private static IStructureDefinition<MTEAlgaePondBase> STRUCTURE_DEFINITION = null;
    private int checkMeta;

    private static final int MINIMUM_CASINGS = 75;

    public MTEAlgaePondBase(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEAlgaePondBase(final String aName) {
        super(aName);
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEAlgaePondBase(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Algae Pond";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Grows Algae!")
            .addInfo("Provide compost to boost production by one tier")
            .addInfo("Does not require power or maintenance")
            .addInfo("All Machine Casings must be the same tier, this dictates machine speed")
            .addInfo("Fill Input Hatch with Water to fill the inside of the multiblock")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(9, 3, 9, true)
            .addController("Front Center")
            .addCasingInfoMin("Machine Casings", MINIMUM_CASINGS, true)
            .addCasingInfoExactly("Sterile Farm Casings", 64, false)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .toolTipFinisher();
        return tt;
    }

    public void setMeta(int meta) {
        checkMeta = meta;
    }

    public int getMeta() {
        return checkMeta;
    }

    @Override
    public IStructureDefinition<MTEAlgaePondBase> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEAlgaePondBase>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] {
                            { "XXXXXXXXX", "X       X", "X       X", "X       X", "X       X", "X       X", "X       X",
                                "X       X", "XXXXXXXXX" },
                            { "XXXXXXXXX", "X       X", "X       X", "X       X", "X       X", "X       X", "X       X",
                                "X       X", "XXXXXXXXX" },
                            { "CCCC~CCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC",
                                "CCCCCCCCC", "CCCCCCCCC" }, }))
                .addElement(
                    'C',
                    ofChain(
                        buildHatchAdder(MTEAlgaePondBase.class).atLeast(InputHatch, InputBus, OutputBus)
                            .casingIndex(TAE.getIndexFromPage(1, 15))
                            .hint(1)
                            .build(),
                        onElementPass(
                            x -> ++x.mCasing,
                            chainAllCasings(-1, MTEAlgaePondBase::setMeta, MTEAlgaePondBase::getMeta))))
                .addElement('X', ofBlock(ModBlocks.blockCasings2Misc, 15))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 4, 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, 4, 2, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        tier = -1;
        checkMeta = -1;

        if (checkPiece(mName, 4, 2, 0) && mCasing >= MINIMUM_CASINGS
            && checkMeta > 0
            && !mInputHatches.isEmpty()
            && !mOutputBusses.isEmpty()) {
            tier = checkMeta - 1;
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
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCDAlgaePondBaseActive;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.oMCDAlgaePondBaseActiveGlow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCDAlgaePondBase;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.oMCDAlgaePondBaseGlow;
    }

    @Override
    protected int getCasingTextureId() {
        return TAE.getIndexFromPage(1, 15);
    }

    public boolean checkForWater() {

        // Get Facing direction
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
        int mCurrentDirectionX;
        int mCurrentDirectionZ;
        int mOffsetX_Lower = 0;
        int mOffsetX_Upper = 0;
        int mOffsetZ_Lower = 0;
        int mOffsetZ_Upper = 0;

        mCurrentDirectionX = 4;
        mCurrentDirectionZ = 4;

        mOffsetX_Lower = -4;
        mOffsetX_Upper = 4;
        mOffsetZ_Lower = -4;
        mOffsetZ_Upper = 4;

        final int xDir = aBaseMetaTileEntity.getBackFacing().offsetX * mCurrentDirectionX;
        final int zDir = aBaseMetaTileEntity.getBackFacing().offsetZ * mCurrentDirectionZ;
        boolean success = true;

        for (int i = mOffsetX_Lower + 1; i <= mOffsetX_Upper - 1; ++i) {
            for (int j = mOffsetZ_Lower + 1; j <= mOffsetZ_Upper - 1; ++j) {

                Block block = aBaseMetaTileEntity.getBlockOffset(xDir + i, 1, zDir + j);

                boolean isCOFHCoreWater = Mods.COFHCore.isModLoaded() && (block instanceof BlockWater);
                boolean isWater = (block == Blocks.water) || isCOFHCoreWater;
                boolean isAir = block == Blocks.air || block == Blocks.flowing_water;
                if (isWater) continue;

                if (!isAir) { // invalid block to process
                    success = false;
                    continue;
                }
                // no fluids to fill a non static water block
                // we return directly here because we cannot fill water so there is no point into processing next blocks
                if (this.getStoredFluids() == null) return false;

                boolean hasBeenFilled = false;

                // trying to fill with water
                for (FluidStack stored : this.getStoredFluids()) {
                    if (!stored.isFluidEqual(Materials.Water.getFluid(1))) continue;

                    if (stored.amount < 1000) continue;

                    stored.amount -= 1000;
                    Block fluidUsed = Blocks.water;
                    aBaseMetaTileEntity.getWorld()
                        .setBlock(
                            aBaseMetaTileEntity.getXCoord() + xDir + i,
                            aBaseMetaTileEntity.getYCoord() + 1,
                            aBaseMetaTileEntity.getZCoord() + zDir + j,
                            fluidUsed);
                    hasBeenFilled = true;

                    break; // don't deplete other water sources
                }

                if (!hasBeenFilled) success = false; // did not get filled with water

            }
        }
        return success;
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
            this.tier = getCasingTier();
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
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }.setEuModifier(0F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    private int getCasingTier() {
        if (this.getBaseMetaTileEntity()
            .getWorld() == null) {
            return -1;
        }
        try {
            Block aInitStructureCheck;
            int aInitStructureCheckMeta;
            IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
            int xDir = aBaseMetaTileEntity.getBackFacing().offsetX;
            int zDir = aBaseMetaTileEntity.getBackFacing().offsetZ;
            aInitStructureCheck = aBaseMetaTileEntity.getBlockOffset(xDir, -1, zDir);
            aInitStructureCheckMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir, -1, zDir);
            if (aInitStructureCheck == GregTechAPI.sBlockCasings1
                || aInitStructureCheck == GregTechAPI.sBlockCasingsNH) {
                return aInitStructureCheckMeta - 1;
            }
            return 0;
        } catch (Exception t) {
            t.printStackTrace();
            return -1;
        }
    }

    @Override
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
    public String[] getExtraInfoData() {
        ArrayList<String> mInfo = new ArrayList<>();
        mInfo.add(StatCollector.translateToLocalFormatted("GTPP.multiblock.ap.tier", this.tier));
        return mInfo.toArray(new String[0]);
    }
}

package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.*;
import com.gtnewhorizon.structurelib.util.ItemStackPredicate;

import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.interfaces.IHeatingCoil;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Single_Recipe_Check;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_LargeChemicalReactor
        extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_LargeChemicalReactor>
        implements ISurvivalConstructable {

    private static final int CASING_INDEX = 176;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_MetaTileEntity_LargeChemicalReactor> STRUCTURE_DEFINITION = StructureDefinition.<GT_MetaTileEntity_LargeChemicalReactor>builder()
                                                                                                                                .addShape(
                                                                                                                                        STRUCTURE_PIECE_MAIN,
                                                                                                                                        transpose(
                                                                                                                                                new String[][] {
                                                                                                                                                        { "ccc", "cxc",
                                                                                                                                                                "ccc" },
                                                                                                                                                        { "c~c", "xPx",
                                                                                                                                                                "cxc" },
                                                                                                                                                        { "ccc", "cxc",
                                                                                                                                                                "ccc" }, }))
                                                                                                                                .addElement(
                                                                                                                                        'P',
                                                                                                                                        ofBlock(
                                                                                                                                                GregTech_API.sBlockCasings8,
                                                                                                                                                1))
                                                                                                                                .addElement(
                                                                                                                                        'c',
                                                                                                                                        buildHatchAdder(
                                                                                                                                                GT_MetaTileEntity_LargeChemicalReactor.class).atLeast(
                                                                                                                                                        InputHatch,
                                                                                                                                                        OutputHatch,
                                                                                                                                                        InputBus,
                                                                                                                                                        OutputBus,
                                                                                                                                                        Maintenance,
                                                                                                                                                        Energy)
                                                                                                                                                                                             .casingIndex(
                                                                                                                                                                                                     CASING_INDEX)
                                                                                                                                                                                             .dot(
                                                                                                                                                                                                     1)
                                                                                                                                                                                             .buildAndChain(
                                                                                                                                                                                                     onElementPass(
                                                                                                                                                                                                             GT_MetaTileEntity_LargeChemicalReactor::onCasingAdded,
                                                                                                                                                                                                             ofBlock(
                                                                                                                                                                                                                     GregTech_API.sBlockCasings8,
                                                                                                                                                                                                                     0))))
                                                                                                                                .addElement(
                                                                                                                                        'x',
                                                                                                                                        buildHatchAdder(
                                                                                                                                                GT_MetaTileEntity_LargeChemicalReactor.class).atLeast(
                                                                                                                                                        InputHatch,
                                                                                                                                                        OutputHatch,
                                                                                                                                                        InputBus,
                                                                                                                                                        OutputBus,
                                                                                                                                                        Maintenance,
                                                                                                                                                        Energy)
                                                                                                                                                                                             .casingIndex(
                                                                                                                                                                                                     CASING_INDEX)
                                                                                                                                                                                             .dot(
                                                                                                                                                                                                     1)
                                                                                                                                                                                             .buildAndChain(
                                                                                                                                                                                                     CoilStructureElement.INSTANCE,
                                                                                                                                                                                                     onElementPass(
                                                                                                                                                                                                             GT_MetaTileEntity_LargeChemicalReactor::onCasingAdded,
                                                                                                                                                                                                             ofBlock(
                                                                                                                                                                                                                     GregTech_API.sBlockCasings8,
                                                                                                                                                                                                                     0))))
                                                                                                                                .build();

    private int mCasingAmount;
    private int mCoilAmount;

    public GT_MetaTileEntity_LargeChemicalReactor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_LargeChemicalReactor(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_LargeChemicalReactor(this.mName);
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Chemical Reactor")
          .addInfo("Controller block for the Large Chemical Reactor")
          .addInfo("Does not lose efficiency when overclocked")
          .addInfo("Accepts fluids instead of fluid cells")
          .addSeparator()
          .beginStructureBlock(3, 3, 3, false)
          .addController("Front center")
          .addCasingInfoRange("Chemically Inert Machine Casing", 8, 22, false)
          .addOtherStructurePart("PTFE Pipe Machine Casing", "Center")
          .addOtherStructurePart("Heating Coil", "Adjacent to the PTFE Pipe Machine Casing", 1)
          .addEnergyHatch("Any casing", 1, 2)
          .addMaintenanceHatch("Any casing", 1, 2)
          .addInputBus("Any casing", 1, 2)
          .addInputHatch("Any casing", 1, 2)
          .addOutputBus("Any casing", 1, 2)
          .addOutputHatch("Any casing", 1, 2)
          .addStructureInfo("You can have multiple hatches/busses")
          .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
            boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive) return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                                                                                          .addIcon(
                                                                                                  OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                                                                                          .extFacing()
                                                                                          .build(),
                    TextureFactory.builder()
                                  .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                                  .extFacing()
                                  .glow()
                                  .build() };
            return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                                                                             .addIcon(
                                                                                     OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR)
                                                                             .extFacing()
                                                                             .build(),
                    TextureFactory.builder()
                                  .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW)
                                  .extFacing()
                                  .glow()
                                  .build() };
        }
        return new ITexture[] { casingTexturePages[1][48] };
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        long tVoltage = getMaxInputVoltage();
        byte tier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
        GT_Recipe tRecipe;

        if (mLockedToSingleRecipe && mSingleRecipeCheck != null) {
            if (!mSingleRecipeCheck.checkRecipeInputsSingleStack(true)) {
                return false;
            }
            tRecipe = mSingleRecipeCheck.getRecipe();
        } else {
            ArrayList<ItemStack> tInputList = getStoredInputs();
            ArrayList<FluidStack> tFluidList = getStoredFluids();

            ItemStack[] inputs = tInputList.toArray(new ItemStack[0]);
            FluidStack[] fluids = tFluidList.toArray(new FluidStack[0]);

            if (inputs.length == 0 && fluids.length == 0) {
                return false;
            }

            GT_Single_Recipe_Check.Builder tSingleRecipeCheckBuilder = null;
            if (mLockedToSingleRecipe) {
                // We're locked to a single recipe, but haven't built the recipe checker yet.
                // Build the checker on next successful recipe.
                tSingleRecipeCheckBuilder = GT_Single_Recipe_Check.builder(this)
                                                                  .setBefore();
            }

            tRecipe = GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes.findRecipe(
                    getBaseMetaTileEntity(),
                    false,
                    false,
                    gregtech.api.enums.GT_Values.V[tier],
                    fluids,
                    inputs);

            if (tRecipe == null || !tRecipe.isRecipeInputEqual(true, fluids, inputs)) {
                return false;
            }

            if (mLockedToSingleRecipe) {
                mSingleRecipeCheck = tSingleRecipeCheckBuilder.setAfter()
                                                              .setRecipe(tRecipe)
                                                              .build();
            }
        }

        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;

        calculatePerfectOverclockedNessMulti(tRecipe.mEUt, tRecipe.mDuration, 1, tVoltage);
        // In case recipe is too OP for that machine
        if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1) return false;
        if (this.mEUt > 0) {
            this.mEUt = (-this.mEUt);
        }

        this.mOutputItems = tRecipe.mOutputs;
        this.mOutputFluids = tRecipe.mFluidOutputs;
        this.updateSlots();
        return true;
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_LargeChemicalReactor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    private void onCasingAdded() {
        mCasingAmount++;
    }

    private void onCoilAdded() {
        mCoilAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        mCoilAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, 1, 1, 0) && mCasingAmount >= 8
                && mCoilAmount == 1
                && !mEnergyHatches.isEmpty()
                && mMaintenanceHatches.size() == 1;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        mCoilAmount = 0;
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        mCoilAmount = 0;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    private enum CoilStructureElement implements IStructureElement<GT_MetaTileEntity_LargeChemicalReactor> {

        INSTANCE;

        @Override
        public boolean check(GT_MetaTileEntity_LargeChemicalReactor t, World world, int x, int y, int z) {
            Block block = world.getBlock(x, y, z);
            if (block instanceof IHeatingCoil
                    && ((IHeatingCoil) block).getCoilHeat(world.getBlockMetadata(x, y, z)) != HeatingCoilLevel.None) {
                return t.mCoilAmount++ == 0;
            } else {
                return false;
            }
        }

        @Override
        public boolean spawnHint(GT_MetaTileEntity_LargeChemicalReactor t, World world, int x, int y, int z,
                ItemStack trigger) {
            StructureLibAPI.hintParticle(world, x, y, z, GregTech_API.sBlockCasings5, 0);
            return true;
        }

        @Override
        public boolean placeBlock(GT_MetaTileEntity_LargeChemicalReactor t, World world, int x, int y, int z,
                ItemStack trigger) {
            if (t.mCoilAmount > 0) return false;
            boolean b = world.setBlock(x, y, z, GregTech_API.sBlockCasings5, 0, 3);
            if (b) t.mCoilAmount++;
            return b;
        }

        @Override
        public PlaceResult survivalPlaceBlock(GT_MetaTileEntity_LargeChemicalReactor t, World world, int x, int y,
                int z, ItemStack trigger, IItemSource s, EntityPlayerMP actor, Consumer<IChatComponent> chatter) {
            return survivalPlaceBlock(t, world, x, y, z, trigger, AutoPlaceEnvironment.fromLegacy(s, actor, chatter));
        }

        @Override
        public BlocksToPlace getBlocksToPlace(
                GT_MetaTileEntity_LargeChemicalReactor gt_metaTileEntity_largeChemicalReactor, World world, int x,
                int y, int z, ItemStack trigger, AutoPlaceEnvironment env) {
            return BlocksToPlace.create(
                    IntStream.range(0, 8)
                             .mapToObj(i -> new ItemStack(GregTech_API.sBlockCasings5, 1, i))
                             .collect(Collectors.toList()));
        }

        @Override
        public PlaceResult survivalPlaceBlock(GT_MetaTileEntity_LargeChemicalReactor t, World world, int x, int y,
                int z, ItemStack trigger, AutoPlaceEnvironment env) {
            if (t.mCoilAmount > 0) return PlaceResult.SKIP;
            if (check(t, world, x, y, z)) return PlaceResult.SKIP;
            if (!StructureLibAPI.isBlockTriviallyReplaceable(world, x, y, z, env.getActor())) return PlaceResult.REJECT;
            ItemStack result = env.getSource()
                                  .takeOne(ItemStackPredicate.from(GregTech_API.sBlockCasings5), true);
            if (result == null) return PlaceResult.REJECT;
            PlaceResult ret = StructureUtility.survivalPlaceBlock(
                    result,
                    ItemStackPredicate.NBTMode.EXACT,
                    null,
                    true,
                    world,
                    x,
                    y,
                    z,
                    env.getSource(),
                    env.getActor(),
                    env.getChatter());
            if (ret == PlaceResult.ACCEPT) t.mCoilAmount++;
            return ret;
        }
    }
}

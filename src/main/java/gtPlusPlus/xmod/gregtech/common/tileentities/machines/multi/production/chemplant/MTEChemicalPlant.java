package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.chemplant;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.filterByMTETier;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.metatileentity.implementations.MTEHatchMaintenance;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.api.util.tooltip.TooltipTier;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.IDualInputHatch;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.item.chemistry.general.ItemGenericChemBase;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers.MTEHatchCatalysts;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEChemicalPlant extends GTPPMultiBlockBase<MTEChemicalPlant> implements ISurvivalConstructable {

    private int mSolidCasingTier = 0;
    private int mMachineCasingTier = 0;
    private int mPipeCasingTier = 0;
    private int mCoilTier = 0;
    private HeatingCoilLevel checkCoil;
    private final int[] checkCasing = new int[8];
    private int checkMachine;
    private int checkPipe;
    private int maxTierOfHatch;
    private int mCasing;
    private static IStructureDefinition<MTEChemicalPlant> STRUCTURE_DEFINITION = null;

    public static final Set<GTUtility.ItemId> CHEMPLANT_CATALYSTS = new HashSet<>();

    private final ArrayList<MTEHatchCatalysts> mCatalystBuses = new ArrayList<>();

    private static final HashMap<Integer, Triple<Block, Integer, Integer>> mTieredBlockRegistry = new HashMap<>();

    public MTEChemicalPlant(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEChemicalPlant(final String aName) {
        super(aName);
    }

    public static boolean registerMachineCasingForTier(int aTier, Block aBlock, int aMeta, int aCasingTextureID) {
        Triple<Block, Integer, Integer> aCasingData = Triple.of(aBlock, aMeta, aCasingTextureID);
        if (mTieredBlockRegistry.containsKey(aTier)) {
            Logger.ERROR(
                "Tried to register a Machine casing for tier " + aTier
                    + " to the Chemical Plant, however this tier already contains one.");
            throw new IllegalStateException();
        }
        mTieredBlockRegistry.put(aTier, aCasingData);
        GTStructureChannels.METAL_MACHINE_CASING.registerAsIndicator(new ItemStack(aBlock, 1, aMeta), aTier + 1);
        return true;
    }

    private static int getCasingTextureIdForTier(int aTier) {
        if (!mTieredBlockRegistry.containsKey(aTier)) {
            return 10;
        }
        return mTieredBlockRegistry.get(aTier)
            .getRight();
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEChemicalPlant(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Chemical Plant";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType(getMachineType())
            .addInfo("Heavy Industry, now right at your doorstep!")
            .addInfo("Plant tier is determined by casing tier")
            .addInfo("Hatch tiers can't be higher than machine casing tier, UHV casing unlocks all tiers")
            .addDynamicParallelInfo(2, TooltipTier.PIPE_CASING)
            .addInfo(
                "+20% chance of not damaging catalyst per " + TooltipHelper.tierText(TooltipTier.PIPE_CASING) + " Tier")
            .addDynamicSpeedInfo(0.5f, TooltipTier.COIL)
            .addInfo("Any catalyst must be placed in the catalyst housing")
            .addInfo("Awakened Draconium coils combined with Tungstensteel pipe casing makes catalyst unbreakable")
            .addController("Bottom Center")
            .addOtherStructurePart("Catalyst Housing", "Any Casing")
            .addStructureHint("item.GTPP.catalyst_housing.name", 1)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addOutputHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addSubChannelUsage(GTStructureChannels.METAL_MACHINE_CASING, "metal machine casing (minimum 70)")
            .addSubChannelUsage(GTStructureChannels.TIER_MACHINE_CASING)
            .addSubChannelUsage(GTStructureChannels.HEATING_COIL)
            .addSubChannelUsage(GTStructureChannels.PIPE_CASING)
            .toolTipFinisher();
    }

    public void setMachineMeta(int meta) {
        checkMachine = meta;
    }

    public int getMachineMeta() {
        return checkMachine;
    }

    public void setPipeMeta(int meta) {
        checkPipe = meta;
    }

    public int getPipeMeta() {
        return checkPipe;
    }

    public void setCoilMeta(HeatingCoilLevel meta) {
        checkCoil = meta;
    }

    public HeatingCoilLevel getCoilMeta() {
        return checkCoil;
    }

    @Override
    public IStructureDefinition<MTEChemicalPlant> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            IStructureElement<MTEChemicalPlant> allCasingsElement = GTStructureChannels.METAL_MACHINE_CASING.use(
                ofChain(
                    IntStream.range(0, 8)
                        .mapToObj(MTEChemicalPlant::ofSolidCasing)
                        .collect(Collectors.toList())));
            STRUCTURE_DEFINITION = StructureDefinition.<MTEChemicalPlant>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] {
                            { "CCCCCCC", "CCCCCCC", "CCCCCCC", "CCCCCCC", "CCCCCCC", "CCCCCCC", "CCCCCCC" },
                            { "C     C", " MMMMM ", " MHHHM ", " MHHHM ", " MHHHM ", " MMMMM ", "C     C" },
                            { "C     C", "       ", "  PPP  ", "  PPP  ", "  PPP  ", "       ", "C     C" },
                            { "C     C", "       ", "  HHH  ", "  HHH  ", "  HHH  ", "       ", "C     C" },
                            { "C     C", "       ", "  PPP  ", "  PPP  ", "  PPP  ", "       ", "C     C" },
                            { "C     C", " MMMMM ", " MHHHM ", " MHHHM ", " MHHHM ", " MMMMM ", "C     C" },
                            { "CCC~CCC", "CMMMMMC", "CMMMMMC", "CMMMMMC", "CMMMMMC", "CMMMMMC", "CCCCCCC" }, }))
                .addElement(
                    'C',
                    ofChain(
                        buildHatchAdder(MTEChemicalPlant.class).atLeast(Maintenance)
                            .casingIndex(getCasingTextureID())
                            .hint(1)
                            .build(),
                        buildHatchAdder(MTEChemicalPlant.class).atLeast(InputHatch, OutputHatch, InputBus, OutputBus)
                            .adder(MTEChemicalPlant::addChemicalPlantList)
                            .hatchItemFilterAnd(
                                (t, s) -> filterByMTETier(
                                    Integer.MIN_VALUE,
                                    s.stackSize >= 10 ? Integer.MAX_VALUE : s.stackSize))
                            .casingIndex(getCasingTextureID())
                            .hint(1)
                            .build(),
                        buildHatchAdder(MTEChemicalPlant.class).hatchClass(MTEHatchCatalysts.class)
                            .shouldReject(t -> !t.mCatalystBuses.isEmpty())
                            .adder(MTEChemicalPlant::addChemicalPlantList)
                            .casingIndex(getCasingTextureID())
                            .hint(1)
                            .build(),
                        allCasingsElement))
                .addElement(
                    'M',
                    GTStructureChannels.TIER_MACHINE_CASING.use(
                        addTieredBlock(
                            GregTechAPI.sBlockCasings1,
                            MTEChemicalPlant::setMachineMeta,
                            MTEChemicalPlant::getMachineMeta,
                            10)))
                .addElement(
                    'H',
                    GTStructureChannels.HEATING_COIL
                        .use(activeCoils(ofCoil(MTEChemicalPlant::setCoilMeta, MTEChemicalPlant::getCoilMeta))))
                .addElement(
                    'P',
                    GTStructureChannels.PIPE_CASING.use(
                        addTieredBlock(
                            GregTechAPI.sBlockCasings2,
                            MTEChemicalPlant::setPipeMeta,
                            MTEChemicalPlant::getPipeMeta,
                            12,
                            16)))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    private static IStructureElement<MTEChemicalPlant> ofSolidCasing(int aIndex) {
        return new IStructureElement<MTEChemicalPlant>() {

            @Override
            public boolean check(MTEChemicalPlant t, World world, int x, int y, int z) {
                if (check(aIndex, world, x, y, z)) {
                    t.checkCasing[aIndex]++;
                    t.mCasing++;
                    return true;
                } else return false;
            }

            @Override
            public boolean couldBeValid(MTEChemicalPlant mteChemicalPlant, World world, int x, int y, int z,
                ItemStack trigger) {
                return check(aIndex, world, x, y, z);
            }

            private boolean check(int aIndex, World world, int x, int y, int z) {
                Block block = world.getBlock(x, y, z);
                int meta = world.getBlockMetadata(x, y, z);
                Block target = mTieredBlockRegistry.get(aIndex)
                    .getLeft();
                int targetMeta = mTieredBlockRegistry.get(aIndex)
                    .getMiddle();
                return target.equals(block) && meta == targetMeta;
            }

            int getIndex(int size) {
                if (size > 8) size = 8;
                return size - 1;
            }

            @Override
            public boolean spawnHint(MTEChemicalPlant t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(
                    world,
                    x,
                    y,
                    z,
                    mTieredBlockRegistry.get(getIndex(trigger.stackSize))
                        .getLeft(),
                    mTieredBlockRegistry.get(getIndex(trigger.stackSize))
                        .getMiddle());
                return true;
            }

            @Override
            public boolean placeBlock(MTEChemicalPlant t, World world, int x, int y, int z, ItemStack trigger) {
                return world.setBlock(
                    x,
                    y,
                    z,
                    mTieredBlockRegistry.get(getIndex(trigger.stackSize))
                        .getLeft(),
                    mTieredBlockRegistry.get(getIndex(trigger.stackSize))
                        .getMiddle(),
                    3);
            }

            @Nullable
            @Override
            public BlocksToPlace getBlocksToPlace(MTEChemicalPlant gregtechMTE_chemicalPlant, World world, int x, int y,
                int z, ItemStack trigger, AutoPlaceEnvironment env) {
                return BlocksToPlace.create(
                    mTieredBlockRegistry.get(getIndex(trigger.stackSize))
                        .getLeft(),
                    mTieredBlockRegistry.get(getIndex(trigger.stackSize))
                        .getMiddle());
            }

            @Override
            public PlaceResult survivalPlaceBlock(MTEChemicalPlant t, World world, int x, int y, int z,
                ItemStack trigger, AutoPlaceEnvironment env) {
                if (check(getIndex(trigger.stackSize), world, x, y, z)) return PlaceResult.SKIP;
                return StructureUtility.survivalPlaceBlock(
                    mTieredBlockRegistry.get(getIndex(trigger.stackSize))
                        .getLeft(),
                    mTieredBlockRegistry.get(getIndex(trigger.stackSize))
                        .getMiddle(),
                    world,
                    x,
                    y,
                    z,
                    env.getSource(),
                    env.getActor(),
                    env.getChatter());
            }
        };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 3, 6, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, 3, 6, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        for (int i = 0; i < 8; i++) {
            checkCasing[i] = 0;
        }
        checkPipe = 0;
        checkMachine = 0;
        mSolidCasingTier = 0;
        mMachineCasingTier = 0;
        mPipeCasingTier = 0;
        mCoilTier = 0;
        maxTierOfHatch = 0;
        mCatalystBuses.clear();
        setCoilMeta(HeatingCoilLevel.None);
        if (checkPiece(mName, 3, 6, 0) && mCasing >= 70) {
            for (int i = 0; i < 8; i++) {
                if (checkCasing[i] == mCasing) {
                    mSolidCasingTier = i;
                } else if (checkCasing[i] > 0) return false;
            }
            mMachineCasingTier = checkMachine - 1;
            mPipeCasingTier = checkPipe - 12;
            mCoilTier = checkCoil.getTier();
            getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, getUpdateData());
            updateHatchTexture();
            return (mMachineCasingTier >= 9 || mMachineCasingTier >= maxTierOfHatch) && mCatalystBuses.size() <= 1;
        }
        return false;
    }

    public void updateHatchTexture() {
        for (MTEHatch h : mCatalystBuses) h.updateTexture(getCasingTextureID());
        for (IDualInputHatch h : mDualInputHatches) h.updateTexture(getCasingTextureID());
        for (MTEHatch h : mInputBusses) h.updateTexture(getCasingTextureID());
        for (MTEHatch h : mMaintenanceHatches) h.updateTexture(getCasingTextureID());
        for (MTEHatch h : mEnergyHatches) h.updateTexture(getCasingTextureID());
        for (MTEHatch h : mOutputBusses) h.updateTexture(getCasingTextureID());
        for (MTEHatch h : mInputHatches) h.updateTexture(getCasingTextureID());
        for (MTEHatch h : mOutputHatches) h.updateTexture(getCasingTextureID());
    }

    public final boolean addChemicalPlantList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof MTEHatchCatalysts) {
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            } else if (aMetaTileEntity instanceof MTEHatchInputBus) {
                maxTierOfHatch = Math.max(maxTierOfHatch, ((MTEHatchInputBus) aMetaTileEntity).mTier);
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            } else if (aMetaTileEntity instanceof MTEHatchMaintenance) {
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            } else if (aMetaTileEntity instanceof MTEHatchEnergy) {
                maxTierOfHatch = Math.max(maxTierOfHatch, ((MTEHatchEnergy) aMetaTileEntity).mTier);
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            } else if (aMetaTileEntity instanceof MTEHatchOutputBus) {
                maxTierOfHatch = Math.max(maxTierOfHatch, ((MTEHatchOutputBus) aMetaTileEntity).mTier);
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            } else if (aMetaTileEntity instanceof MTEHatchInput) {
                maxTierOfHatch = Math.max(maxTierOfHatch, ((MTEHatchInput) aMetaTileEntity).mTier);
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            } else if (aMetaTileEntity instanceof MTEHatchOutput) {
                maxTierOfHatch = Math.max(maxTierOfHatch, ((MTEHatchOutput) aMetaTileEntity).mTier);
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            }
        }
        return false;
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.GTCEU_LOOP_CHEMICAL;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCAChemicalPlantActive;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.oMCAChemicalPlantActiveGlow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCAChemicalPlant;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.oMCAChemicalPlantGlow;
    }

    @Override
    protected int getCasingTextureId() {
        return getCasingTextureID();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.chemicalPlantRecipes;
    }

    @Override
    public int getMaxParallelRecipes() {
        return 2 * getPipeCasingTier();
    }

    private int getSolidCasingTier() {
        return this.mSolidCasingTier;
    }

    private int getMachineCasingTier() {
        return mMachineCasingTier;
    }

    private int getPipeCasingTier() {
        return mPipeCasingTier;
    }

    private int getCasingTextureID() {
        // Check the Tier Client Side
        int aTier = mSolidCasingTier;
        return getCasingTextureIdForTier(aTier);
    }

    public boolean addToMachineList(IGregTechTileEntity aTileEntity) {
        int aMaxTier = getMachineCasingTier();
        final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTETieredMachineBlock aMachineBlock) {
            int aTileTier = aMachineBlock.mTier;
            if (aTileTier > aMaxTier) {
                log("Hatch tier too high.");
                return false;
            } else {
                return addToMachineList(aTileEntity, getCasingTextureID());
            }
        } else {
            log("Bad Tile Entity being added to hatch map."); // Shouldn't ever happen, but.. ya know..
            return false;
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mSolidCasingTier", this.mSolidCasingTier);
        aNBT.setInteger("mMachineCasingTier", this.mMachineCasingTier);
        aNBT.setInteger("mPipeCasingTier", this.mPipeCasingTier);
        aNBT.setInteger("mCoilTier", this.mCoilTier);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mSolidCasingTier = aNBT.getInteger("mSolidCasingTier");
        mMachineCasingTier = aNBT.getInteger("mMachineCasingTier");
        mPipeCasingTier = aNBT.getInteger("mPipeCasingTier");
        mCoilTier = aNBT.getInteger("mCoilTier");
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof MTEHatchCatalysts) {
            log("Found MTEHatchCatalysts");
            return addToMachineListInternal(mCatalystBuses, aMetaTileEntity, aBaseCasingIndex);
        }
        return super.addToMachineList(aTileEntity, aBaseCasingIndex);
    }

    public int getMaxCatalystDurability() {
        return 50;
    }

    @Override
    public byte getUpdateData() {
        return (byte) mSolidCasingTier;
    }

    @Override
    public void receiveClientEvent(byte aEventID, byte aValue) {
        super.receiveClientEvent(aEventID, aValue);
        if (aEventID == GregTechTileClientEvents.CHANGE_CUSTOM_DATA && (aValue & 0x80) == 0) {
            // received an update data from above method
            // if no &0x80 clause it might catch the noop texture page event
            mSolidCasingTier = aValue;
        }
    }

    /**
     * @return if the catalyst item is fully destroyed as a result of the damage applied.
     */
    private boolean damageCatalyst(@Nonnull ItemStack aStack, int minParallel) {
        // Awakened Draconium Coils with Tungstensteel Pipe Casings (or above) no longer consume catalysts.
        if (!isCatalystDamageable()) return false;
        for (int i = 0; i < minParallel; i++) {
            if (MathUtils.randFloat(0, 10000000) / 10000000f < (1.2f - (0.2 * this.mPipeCasingTier))) {
                int damage = getDamage(aStack) + 1;
                if (damage >= getMaxCatalystDurability()) {
                    addOutputPartial(GregtechItemList.EmptyCatalystCarrier.get(1));
                    aStack.stackSize -= 1;
                    return aStack.stackSize == 0;
                } else {
                    setDamage(aStack, damage);
                }
            }
        }
        return false;
    }

    private boolean isCatalystDamageable() {
        return this.mCoilTier < 10 || this.mPipeCasingTier < 4;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            ItemStack catalyst;

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (recipe.mSpecialValue > mSolidCasingTier) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(recipe.mSpecialValue + 1);
                }
                // checks if it has a catalyst
                ItemStack catalystInRecipe = null;
                for (ItemStack item : recipe.mInputs) {
                    if (MTEChemicalPlant.isCatalyst(item)) {
                        catalystInRecipe = item;
                        break;
                    }
                }

                if (catalystInRecipe != null) {
                    catalyst = findCatalyst(getCatalystInputs().toArray(new ItemStack[0]), catalystInRecipe);
                    if (catalyst == null) {
                        return SimpleCheckRecipeResult.ofFailure("no_catalyst");
                    }
                } else {
                    // remove reference to the old catalyst if our new recipe doesn't use it
                    catalyst = null;
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            public CheckRecipeResult process() {
                ArrayList<ItemStack> inputItemsList = new ArrayList<>(Arrays.asList(inputItems));
                inputItemsList.addAll(getCatalystInputs());
                inputItems = inputItemsList.toArray(new ItemStack[0]);
                return super.process();
            }

            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@NotNull GTRecipe recipe) {
                if (!GTUtility.isStackValid(catalyst) || damageCatalyst(catalyst, getCurrentParallels())) {
                    // remove reference to the catalyst if it is invalid, or if the damage destroys it
                    catalyst = null;
                }
                return super.onRecipeStart(recipe);
            }
        }.setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    protected void setupProcessingLogic(ProcessingLogic logic) {
        super.setupProcessingLogic(logic);
        // Same speed bonus as pyro oven
        logic.setSpeedBonus(2F / (1 + this.mCoilTier));
    }

    @Override
    public void updateSlots() {
        super.updateSlots();
        for (MTEHatchCatalysts h : mCatalystBuses) {
            h.tryFillUsageSlots();
            h.updateSlots();
        }
    }

    private ItemStack findCatalyst(ItemStack[] aItemInputs, ItemStack catalyst) {
        if (aItemInputs != null) {
            for (ItemStack item : aItemInputs) {
                if (GTUtility.areStacksEqual(item, catalyst, true)) {
                    return item;
                }
            }
        }
        return null;
    }

    private int getDamage(@Nonnull ItemStack aStack) {
        return ItemGenericChemBase.getCatalystDamage(aStack);
    }

    private void setDamage(@Nonnull ItemStack aStack, int aAmount) {
        ItemGenericChemBase.setCatalystDamage(aStack, aAmount);
    }

    /*
     * Catalyst Handling
     */
    public ArrayList<ItemStack> getCatalystInputs() {
        ArrayList<ItemStack> tItems = new ArrayList<>();
        for (MTEHatchCatalysts tHatch : validMTEList(mCatalystBuses)) {
            ArrayList<ItemStack> aHatchContent = tHatch.getContentUsageSlots();
            if (!aHatchContent.isEmpty()) {
                tItems.addAll(aHatchContent);
            }
        }
        return tItems;
    }

    public static void registerChemplantCatalyst(ItemStack stack) {
        if (stack == null) return;

        CHEMPLANT_CATALYSTS.add(GTUtility.ItemId.createWithoutNBT(stack));
    }

    public static boolean isCatalyst(ItemStack aStack) {
        return aStack != null && CHEMPLANT_CATALYSTS.contains(GTUtility.ItemId.createWithoutNBT(aStack));
    }

    static {
        registerChemplantCatalyst(GregtechItemList.BlueMetalCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.BrownMetalCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.OrangeMetalCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.PurpleMetalCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.RedMetalCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.YellowMetalCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.PinkMetalCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.FormaldehydeCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.SolidAcidCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.InfiniteMutationCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.GreenMetalCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.PlatinumGroupCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.PlasticPolymerCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.RubberPolymerCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.AdhesionPromoterCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.TitaTungstenIndiumCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.RadioactivityCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.RareEarthGroupCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.SimpleNaquadahCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.HellishForceCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.CrystalColorizationCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.AdvancedNaquadahCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.RawIntelligenceCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.UltimatePlasticCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.BiologicalIntelligenceCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.TemporalHarmonyCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.ParticleAccelerationCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.SynchrotronCapableCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.AlgagenicGrowthPromoterCatalyst.get(1));
    }
}

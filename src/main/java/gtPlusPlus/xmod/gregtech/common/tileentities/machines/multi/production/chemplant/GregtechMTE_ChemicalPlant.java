package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.chemplant;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.filterByMTETier;
import static gregtech.api.util.GT_StructureUtility.ofCoil;
import static gregtech.api.util.GT_Utility.filterValidMTEs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Triplet;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.item.chemistry.general.ItemGenericChemBase;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers.GT_MetaTileEntity_Hatch_Catalysts;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMTE_ChemicalPlant extends GregtechMeta_MultiBlockBase<GregtechMTE_ChemicalPlant>
        implements ISurvivalConstructable {

    private int mSolidCasingTier = 0;
    private int mMachineCasingTier = 0;
    private int mPipeCasingTier = 0;
    private int mCoilTier = 0;
    private HeatingCoilLevel checkCoil;
    private int[] checkCasing = new int[8];
    private int checkMachine;
    private int checkPipe;
    private int maxTierOfHatch;
    private int mCasing;
    private static IStructureDefinition<GregtechMTE_ChemicalPlant> STRUCTURE_DEFINITION = null;

    private final ArrayList<GT_MetaTileEntity_Hatch_Catalysts> mCatalystBuses = new ArrayList<>();

    private static final HashMap<Integer, Triplet<Block, Integer, Integer>> mTieredBlockRegistry = new HashMap<>();

    public GregtechMTE_ChemicalPlant(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMTE_ChemicalPlant(final String aName) {
        super(aName);
    }

    public static boolean registerMachineCasingForTier(int aTier, Block aBlock, int aMeta, int aCasingTextureID) {
        Triplet<Block, Integer, Integer> aCasingData = new Triplet<>(aBlock, aMeta, aCasingTextureID);
        if (mTieredBlockRegistry.containsKey(aTier)) {
            CORE.crash(
                    "Tried to register a Machine casing for tier " + aTier
                            + " to the Chemical Plant, however this tier already contains one.");
        }
        mTieredBlockRegistry.put(aTier, aCasingData);
        return true;
    }

    private static int getCasingTextureIdForTier(int aTier) {
        if (!mTieredBlockRegistry.containsKey(aTier)) {
            return 10;
        }
        int aCasingID = mTieredBlockRegistry.get(aTier).getValue_3();
        // Logger.INFO("Found casing texture ID "+aCasingID+" for tier "+aTier);
        return aCasingID;
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMTE_ChemicalPlant(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Chemical Plant";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Controller Block for the Chemical Plant")
                .addInfo("Heavy Industry, now right at your doorstep!")
                .addInfo("Please read the user manual for more information on construction and usage").addSeparator()
                .addController("Bottom Center").addStructureHint("Catalyst Housing", 1).addInputBus("Bottom Casing", 1)
                .addOutputBus("Bottom Casing", 1).addInputHatch("Bottom Casing", 1).addOutputHatch("Bottom Casing", 1)
                .addEnergyHatch("Bottom Casing", 1).addMaintenanceHatch("Bottom Casing", 1)
                .addSubChannelUsage("casing", "metal machine casing")
                .addSubChannelUsage("machine", "tier machine casing").addSubChannelUsage("coil", "heating coil blocks")
                .addSubChannelUsage("pipe", "pipe casing blocks").toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
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
    public IStructureDefinition<GregtechMTE_ChemicalPlant> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            IStructureElement<GregtechMTE_ChemicalPlant> allCasingsElement = withChannel(
                    "casing",
                    ofChain(
                            IntStream.range(0, 8).mapToObj(GregtechMTE_ChemicalPlant::ofSolidCasing)
                                    .collect(Collectors.toList())));
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMTE_ChemicalPlant>builder().addShape(
                    mName,
                    transpose(
                            new String[][] {
                                    { "XXXXXXX", "XXXXXXX", "XXXXXXX", "XXXXXXX", "XXXXXXX", "XXXXXXX", "XXXXXXX" },
                                    { "X     X", " MMMMM ", " MHHHM ", " MHHHM ", " MHHHM ", " MMMMM ", "X     X" },
                                    { "X     X", "       ", "  PPP  ", "  PPP  ", "  PPP  ", "       ", "X     X" },
                                    { "X     X", "       ", "  HHH  ", "  HHH  ", "  HHH  ", "       ", "X     X" },
                                    { "X     X", "       ", "  PPP  ", "  PPP  ", "  PPP  ", "       ", "X     X" },
                                    { "X     X", " MMMMM ", " MHHHM ", " MHHHM ", " MHHHM ", " MMMMM ", "X     X" },
                                    { "CCC~CCC", "CMMMMMC", "CMMMMMC", "CMMMMMC", "CMMMMMC", "CMMMMMC", "CCCCCCC" }, }))
                    .addElement(
                            'C',
                            ofChain(
                                    buildHatchAdder(GregtechMTE_ChemicalPlant.class).atLeast(Maintenance)
                                            .casingIndex(getCasingTextureID()).dot(1).build(),
                                    buildHatchAdder(GregtechMTE_ChemicalPlant.class)
                                            .atLeast(InputHatch, OutputHatch, InputBus, OutputBus)
                                            .adder(GregtechMTE_ChemicalPlant::addChemicalPlantList)
                                            .hatchItemFilterAnd(
                                                    (t, s) -> filterByMTETier(
                                                            Integer.MIN_VALUE,
                                                            s.stackSize >= 10 ? Integer.MAX_VALUE : s.stackSize))
                                            .casingIndex(getCasingTextureID()).dot(1).build(),
                                    buildHatchAdder(GregtechMTE_ChemicalPlant.class)
                                            .hatchClass(GT_MetaTileEntity_Hatch_Catalysts.class)
                                            .shouldReject(t -> t.mCatalystBuses.size() >= 1)
                                            .adder(GregtechMTE_ChemicalPlant::addChemicalPlantList)
                                            .casingIndex(getCasingTextureID()).dot(1).build(),
                                    allCasingsElement))
                    .addElement('X', allCasingsElement)
                    .addElement(
                            'M',
                            withChannel(
                                    "machine",
                                    addTieredBlock(
                                            GregTech_API.sBlockCasings1,
                                            GregtechMTE_ChemicalPlant::setMachineMeta,
                                            GregtechMTE_ChemicalPlant::getMachineMeta,
                                            10)))
                    .addElement(
                            'H',
                            withChannel(
                                    "coil",
                                    ofCoil(
                                            GregtechMTE_ChemicalPlant::setCoilMeta,
                                            GregtechMTE_ChemicalPlant::getCoilMeta)))
                    .addElement(
                            'P',
                            withChannel(
                                    "pipe",
                                    addTieredBlock(
                                            GregTech_API.sBlockCasings2,
                                            GregtechMTE_ChemicalPlant::setPipeMeta,
                                            GregtechMTE_ChemicalPlant::getPipeMeta,
                                            12,
                                            16)))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    private static IStructureElement<GregtechMTE_ChemicalPlant> ofSolidCasing(int aIndex) {
        return new IStructureElement<GregtechMTE_ChemicalPlant>() {

            @Override
            public boolean check(GregtechMTE_ChemicalPlant t, World world, int x, int y, int z) {
                if (check(aIndex, world, x, y, z)) {
                    t.checkCasing[aIndex]++;
                    t.mCasing++;
                    return true;
                } else return false;
            }

            private boolean check(int aIndex, World world, int x, int y, int z) {
                Block block = world.getBlock(x, y, z);
                int meta = world.getBlockMetadata(x, y, z);
                Block target = mTieredBlockRegistry.get(aIndex).getValue_1();
                int targetMeta = mTieredBlockRegistry.get(aIndex).getValue_2();
                return target.equals(block) && meta == targetMeta;
            }

            int getIndex(int size) {
                if (size > 8) size = 8;
                return size - 1;
            }

            @Override
            public boolean spawnHint(GregtechMTE_ChemicalPlant t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(
                        world,
                        x,
                        y,
                        z,
                        mTieredBlockRegistry.get(getIndex(trigger.stackSize)).getValue_1(),
                        mTieredBlockRegistry.get(getIndex(trigger.stackSize)).getValue_2());
                return true;
            }

            @Override
            public boolean placeBlock(GregtechMTE_ChemicalPlant t, World world, int x, int y, int z,
                    ItemStack trigger) {
                return world.setBlock(
                        x,
                        y,
                        z,
                        mTieredBlockRegistry.get(getIndex(trigger.stackSize)).getValue_1(),
                        mTieredBlockRegistry.get(getIndex(trigger.stackSize)).getValue_2(),
                        3);
            }

            @Nullable
            @Override
            public BlocksToPlace getBlocksToPlace(GregtechMTE_ChemicalPlant gregtechMTE_chemicalPlant, World world,
                    int x, int y, int z, ItemStack trigger, AutoPlaceEnvironment env) {
                return BlocksToPlace.create(
                        mTieredBlockRegistry.get(getIndex(trigger.stackSize)).getValue_1(),
                        mTieredBlockRegistry.get(getIndex(trigger.stackSize)).getValue_2());
            }

            @Override
            public PlaceResult survivalPlaceBlock(GregtechMTE_ChemicalPlant t, World world, int x, int y, int z,
                    ItemStack trigger, AutoPlaceEnvironment env) {
                if (check(getIndex(trigger.stackSize), world, x, y, z)) return PlaceResult.SKIP;
                return StructureUtility.survivalPlaceBlock(
                        mTieredBlockRegistry.get(getIndex(trigger.stackSize)).getValue_1(),
                        mTieredBlockRegistry.get(getIndex(trigger.stackSize)).getValue_2(),
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
        return survivialBuildPiece(mName, stackSize, 3, 6, 0, elementBudget, env, false, true);
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
            return mMachineCasingTier >= 9 || mMachineCasingTier >= maxTierOfHatch;
        }
        return false;
    }

    public void updateHatchTexture() {
        for (GT_MetaTileEntity_Hatch h : mCatalystBuses) h.updateTexture(getCasingTextureID());
        for (GT_MetaTileEntity_Hatch h : mInputBusses) h.updateTexture(getCasingTextureID());
        for (GT_MetaTileEntity_Hatch h : mMaintenanceHatches) h.updateTexture(getCasingTextureID());
        for (GT_MetaTileEntity_Hatch h : mEnergyHatches) h.updateTexture(getCasingTextureID());
        for (GT_MetaTileEntity_Hatch h : mOutputBusses) h.updateTexture(getCasingTextureID());
        for (GT_MetaTileEntity_Hatch h : mInputHatches) h.updateTexture(getCasingTextureID());
        for (GT_MetaTileEntity_Hatch h : mOutputHatches) h.updateTexture(getCasingTextureID());
    }

    public final boolean addChemicalPlantList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Catalysts) {
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
                maxTierOfHatch = Math.max(maxTierOfHatch, ((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity).mTier);
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
                maxTierOfHatch = Math.max(maxTierOfHatch, ((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity).mTier);
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
                maxTierOfHatch = Math.max(maxTierOfHatch, ((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity).mTier);
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
                maxTierOfHatch = Math.max(maxTierOfHatch, ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mTier);
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
                maxTierOfHatch = Math.max(maxTierOfHatch, ((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity).mTier);
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            }
        }
        return false;
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Advanced;
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
        if (aMetaTileEntity instanceof GT_MetaTileEntity_TieredMachineBlock aMachineBlock) {
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
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Catalysts) {
            log("Found GT_MetaTileEntity_Hatch_Catalysts");
            return addToMachineListInternal(mCatalystBuses, aMetaTileEntity, aBaseCasingIndex);
        }
        return super.addToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(final ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    // Same speed bonus as pyro oven
    public int getSpeedBonus() {
        return 50 * (this.mCoilTier + 1);
    }

    public int getMaxCatalystDurability() {
        return 50;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (this.mUpdate == 1 || this.mStartUpCheck == 1) {
                this.mCatalystBuses.clear();
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
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

    private void damageCatalyst(@Nonnull ItemStack aStack, int minParallel) {
        // Awakened Draconium Coils with Tungstensteel Pipe Casings (or above) no longer consume catalysts.
        if (!isCatalystDamageable()) return;
        for (int i = 0; i < minParallel; i++) {
            if (MathUtils.randFloat(0, 10000000) / 10000000f < (1.2f - (0.2 * this.mPipeCasingTier))) {
                int damage = getDamage(aStack) + 1;
                if (damage >= getMaxCatalystDurability()) {
                    addOutput(CI.getEmptyCatalyst(1));
                    aStack.stackSize -= 1;
                } else {
                    setDamage(aStack, damage);
                }
            }
        }
    }

    private boolean isCatalystDamageable() {
        return this.mCoilTier < 10 || this.mPipeCasingTier < 4;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            ItemStack catalystRecipe;

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GT_Recipe recipe) {
                if (recipe.mSpecialValue > mSolidCasingTier) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(recipe.mSpecialValue);
                }
                // checks if it has a catalyst

                boolean needsCalayst = false;
                for (ItemStack item : recipe.mInputs) {
                    if (ItemUtils.isCatalyst(item)) {
                        needsCalayst = true;
                        break;
                    }
                }
                if (needsCalayst) {
                    catalystRecipe = findCatalyst(inputItems, recipe.mInputs);
                    if (catalystRecipe == null || mCatalystBuses.size() != 1) {
                        return SimpleCheckRecipeResult.ofFailure("no_catalyst");
                    }
                } else {
                    catalystRecipe = null;
                }

                // checks if it has enough catalyst durability
                if (catalystRecipe != null) {
                    maxParallel = getParallelLimitedByCatalyst(inputItems, catalystRecipe, maxParallel);
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            protected GT_ParallelHelper createParallelHelper(@NotNull GT_Recipe recipe) {
                return super.createParallelHelper(recipe)
                        .setInputConsumer((recipeToConsume, amountMultiplier, aFluidInputs, aInputs) -> {
                            // Correct parallel is already calculated by ProcessingLogic#validateRecipe,
                            // so we don't need to set MaxParallelCalculator
                            recipeToConsume.consumeInput(amountMultiplier, aFluidInputs, aInputs);
                            if (catalystRecipe != null) {
                                damageCatalyst(catalystRecipe, amountMultiplier);
                            }
                        });
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    protected void setupProcessingLogic(ProcessingLogic logic) {
        super.setupProcessingLogic(logic);
        logic.setSpeedBonus(100F / (100F + getSpeedBonus()));
    }

    @Override
    public void updateSlots() {
        super.updateSlots();
        for (GT_MetaTileEntity_Hatch_Catalysts h : mCatalystBuses) {
            h.updateSlots();
            h.tryFillUsageSlots();
        }
    }

    private int getParallelLimitedByCatalyst(ItemStack[] aItemInputs, ItemStack aRecipeCatalyst, int aMaxParallel) {
        if (!isCatalystDamageable()) {
            return aMaxParallel;
        }
        for (final ItemStack aInput : aItemInputs) {
            if (aRecipeCatalyst.isItemEqual(aInput)) {
                int aDurabilityRemaining = getMaxCatalystDurability() - getDamage(aInput);
                return Math.min(aMaxParallel, aDurabilityRemaining);
            }
        }
        return 0;
    }

    private ItemStack findCatalyst(ItemStack[] aItemInputs, ItemStack[] aRecipeInputs) {
        if (aItemInputs != null) {
            for (final ItemStack aInput : aItemInputs) {
                if (aInput != null) {
                    if (ItemUtils.isCatalyst(aInput)) {
                        for (ItemStack aRecipeInput : aRecipeInputs) {
                            if (GT_Utility.areStacksEqual(aRecipeInput, aInput, true)) {
                                return aInput;
                            }
                        }
                    }
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
    @Override
    public ArrayList<ItemStack> getStoredInputs() {
        ArrayList<ItemStack> tItems = super.getStoredInputs();
        if (this.getControllerSlot() != null) {
            tItems.add(this.getControllerSlot());
        }
        for (GT_MetaTileEntity_Hatch_Catalysts tHatch : filterValidMTEs(mCatalystBuses)) {
            AutoMap<ItemStack> aHatchContent = tHatch.getContentUsageSlots();
            if (!aHatchContent.isEmpty()) {
                tItems.addAll(aHatchContent);
            }
        }
        return tItems;
    }
}

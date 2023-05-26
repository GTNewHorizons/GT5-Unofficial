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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

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
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Triplet;
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

    private final ArrayList<GT_MetaTileEntity_Hatch_Catalysts> mCatalystBuses = new ArrayList<GT_MetaTileEntity_Hatch_Catalysts>();

    private static final HashMap<Integer, Triplet<Block, Integer, Integer>> mTieredBlockRegistry = new HashMap<>();

    public GregtechMTE_ChemicalPlant(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMTE_ChemicalPlant(final String aName) {
        super(aName);
    }

    public static boolean registerMachineCasingForTier(int aTier, Block aBlock, int aMeta, int aCasingTextureID) {
        Triplet<Block, Integer, Integer> aCasingData = new Triplet<Block, Integer, Integer>(
                aBlock,
                aMeta,
                aCasingTextureID);
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

    public int coilTier(int meta) {
        switch (meta) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 5;
            case 5:
                return 7;
            case 6:
                return 8;
            case 7:
                return 10;
            case 8:
                return 11;
            case 9:
                return 6;
            case 10:
                return 9;
            case 11:
                return 12;
            case 12:
                return 13;
            case 13:
                return 14;
            default:
                return 0;
        }
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
    public String getSound() {
        return SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP.toString();
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
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        if (GTPP_Recipe.GTPP_Recipe_Map.sChemicalPlant_GT.mRecipeList.size() == 0) {
            generateRecipes();
        }
        return GTPP_Recipe.GTPP_Recipe_Map.sChemicalPlant_GT;
    }

    public static void generateRecipes() {
        for (GT_Recipe i : GTPP_Recipe.GTPP_Recipe_Map.sChemicalPlantRecipes.mRecipeList) {
            GTPP_Recipe.GTPP_Recipe_Map.sChemicalPlant_GT.add(i);
        }
    }

    @Override
    public int getMaxParallelRecipes() {
        return 2 * getPipeCasingTier();
    }

    @Override
    public int getEuDiscountForParallelism() {
        return 100;
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
        if (aMetaTileEntity instanceof GT_MetaTileEntity_TieredMachineBlock) {
            GT_MetaTileEntity_TieredMachineBlock aMachineBlock = (GT_MetaTileEntity_TieredMachineBlock) aMetaTileEntity;
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
    public int getAmountOfOutputs() {
        return 1;
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

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public boolean checkRecipe(final ItemStack aStack) {
        return checkRecipeGeneric(getMaxParallelRecipes(), getEuDiscountForParallelism(), getSpeedBonus());
    }

    @Override
    public boolean checkRecipeGeneric(int aMaxParallelRecipes, long aEUPercent, int aSpeedBonusPercent,
            int aOutputChanceRoll) {
        ArrayList<ItemStack> tItems = getStoredInputs();
        ArrayList<FluidStack> tFluids = getStoredFluids();
        ItemStack[] tItemInputs = tItems.toArray(new ItemStack[tItems.size()]);
        FluidStack[] tFluidInputs = tFluids.toArray(new FluidStack[tFluids.size()]);
        return checkRecipeGeneric(
                tItemInputs,
                tFluidInputs,
                aMaxParallelRecipes,
                aEUPercent,
                aSpeedBonusPercent,
                aOutputChanceRoll);
    }

    @Override
    public boolean checkRecipeGeneric(ItemStack[] aItemInputs, FluidStack[] aFluidInputs, int aMaxParallelRecipes,
            long aEUPercent, int aSpeedBonusPercent, int aOutputChanceRoll, GT_Recipe aRecipe) {

        // Based on the Processing Array. A bit overkill, but very flexible.

        // Reset outputs and progress stats
        this.lEUt = 0;
        this.mMaxProgresstime = 0;
        this.mOutputItems = new ItemStack[] {};
        this.mOutputFluids = new FluidStack[] {};

        long tVoltage = getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
        long tEnergy = getMaxInputEnergy();

        // GT_Recipe tRecipe = findRecipe(getBaseMetaTileEntity(), mLastRecipe, false,
        // gregtech.api.enums.GT_Values.V[tTier], aFluidInputs, aItemInputs);
        GT_Recipe tRecipe = findRecipe(
                mLastRecipe,
                gregtech.api.enums.GT_Values.V[tTier],
                getSolidCasingTier(),
                aItemInputs,
                aFluidInputs);

        if (tRecipe == null) {
            return false;
        }

        // checks if it has a catalyst
        ItemStack tCatalystRecipe = null;
        boolean aDoesRecipeNeedCatalyst = false;
        for (ItemStack aInputItem : tRecipe.mInputs) {
            if (ItemUtils.isCatalyst(aInputItem)) {
                aDoesRecipeNeedCatalyst = true;
                break;
            }
        }
        if (aDoesRecipeNeedCatalyst) {
            tCatalystRecipe = findCatalyst(aItemInputs, tRecipe.mInputs);
            if (tCatalystRecipe == null) {
                return false;
            }
            if (mCatalystBuses.size() != 1) {
                return false;
            }
        }

        // Remember last recipe - an optimization for findRecipe()
        this.mLastRecipe = tRecipe;

        if (tRecipe.mSpecialValue > this.mSolidCasingTier) {
            return false;
        }

        // checks if it has enough catalyst durability
        ArrayList<ItemStack> tCatalysts = null;
        int tMaxParallelCatalyst = aMaxParallelRecipes;
        if (tCatalystRecipe != null) {
            tCatalysts = new ArrayList<ItemStack>();
            tMaxParallelCatalyst = getCatalysts(aItemInputs, tCatalystRecipe, aMaxParallelRecipes, tCatalysts);
        }

        if (tMaxParallelCatalyst == 0) {
            return false;
        }

        GT_ParallelHelper helper = new GT_ParallelHelper().setRecipe(tRecipe).setItemInputs(aItemInputs)
                .setFluidInputs(aFluidInputs).setAvailableEUt(tEnergy).setMaxParallel(tMaxParallelCatalyst)
                .enableConsumption().enableOutputCalculation();
        if (!voidExcess) {
            helper.enableVoidProtection(this);
        }

        if (batchMode) {
            helper.enableBatchMode(128);
        }

        helper.build();

        if (helper.getCurrentParallel() == 0) {
            return false;
        }

        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;

        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(tRecipe.mEUt).setEUt(tEnergy)
                .setDuration(tRecipe.mDuration).setEUtDiscount(100.0f / aEUPercent)
                .setSpeedBoost((100.0f / aSpeedBonusPercent))
                .setParallel((int) Math.floor(helper.getCurrentParallel() / helper.getDurationMultiplier()))
                .calculate();
        lEUt = -calculator.getConsumption();
        mMaxProgresstime = (int) Math.ceil(calculator.getDuration() * helper.getDurationMultiplier());

        mOutputItems = helper.getItemOutputs();
        mOutputFluids = helper.getFluidOutputs();
        updateSlots();
        for (GT_MetaTileEntity_Hatch_Catalysts h : mCatalystBuses) {
            h.updateSlots();
            h.tryFillUsageSlots();
        }

        // Play sounds (GT++ addition - GT multiblocks play no sounds)
        startProcess();

        return true;
    }

    private static final HashMap<Long, AutoMap<GT_Recipe>> mTieredRecipeMap = new HashMap<Long, AutoMap<GT_Recipe>>();
    private static final AutoMap<GT_Recipe> aTier0Recipes = new AutoMap<GT_Recipe>();
    private static final AutoMap<GT_Recipe> aTier1Recipes = new AutoMap<GT_Recipe>();
    private static final AutoMap<GT_Recipe> aTier2Recipes = new AutoMap<GT_Recipe>();
    private static final AutoMap<GT_Recipe> aTier3Recipes = new AutoMap<GT_Recipe>();
    private static final AutoMap<GT_Recipe> aTier4Recipes = new AutoMap<GT_Recipe>();
    private static final AutoMap<GT_Recipe> aTier5Recipes = new AutoMap<GT_Recipe>();
    private static final AutoMap<GT_Recipe> aTier6Recipes = new AutoMap<GT_Recipe>();
    private static final AutoMap<GT_Recipe> aTier7Recipes = new AutoMap<GT_Recipe>();
    private static boolean mInitRecipeCache = false;

    private static void initRecipeCaches() {
        if (!mInitRecipeCache) {
            mTieredRecipeMap.put((long) 0, aTier0Recipes);
            mTieredRecipeMap.put((long) 1, aTier1Recipes);
            mTieredRecipeMap.put((long) 2, aTier2Recipes);
            mTieredRecipeMap.put((long) 3, aTier3Recipes);
            mTieredRecipeMap.put((long) 4, aTier4Recipes);
            mTieredRecipeMap.put((long) 5, aTier5Recipes);
            mTieredRecipeMap.put((long) 6, aTier6Recipes);
            mTieredRecipeMap.put((long) 7, aTier7Recipes);
            for (GT_Recipe aRecipe : GTPP_Recipe.GTPP_Recipe_Map.sChemicalPlant_GT.mRecipeList) {
                if (aRecipe != null) {
                    switch (aRecipe.mSpecialValue) {
                        case 0:
                            aTier0Recipes.add(aRecipe);
                            continue;
                        case 1:
                            aTier1Recipes.add(aRecipe);
                            continue;
                        case 2:
                            aTier2Recipes.add(aRecipe);
                            continue;
                        case 3:
                            aTier3Recipes.add(aRecipe);
                            continue;
                        case 4:
                            aTier4Recipes.add(aRecipe);
                            continue;
                        case 5:
                            aTier5Recipes.add(aRecipe);
                            continue;
                        case 6:
                            aTier6Recipes.add(aRecipe);
                            continue;
                        case 7:
                            aTier7Recipes.add(aRecipe);
                            continue;
                    }
                }
            }
            mInitRecipeCache = true;
        }
    }

    public GT_Recipe findRecipe(final GT_Recipe aRecipe, final long aVoltage, final long aSpecialValue,
            ItemStack[] aInputs, final FluidStack[] aFluids) {
        if (!mInitRecipeCache) {
            initRecipeCaches();
        }
        if (this.getRecipeMap().mRecipeList.isEmpty()) {
            log("No Recipes in Map to search through.");
            return null;
        } else {
            log("Checking tier " + aSpecialValue + " recipes and below. Using Input Voltage of " + aVoltage + "V.");
            log("We have " + aInputs.length + " Items and " + aFluids.length + " Fluids.");
            // Try check the cached recipe first
            if (aRecipe != null) {
                if (aRecipe.isRecipeInputEqual(false, aFluids, aInputs)) {
                    if (aRecipe.mEUt <= aVoltage) {
                        Logger.INFO("Using cached recipe.");
                        return aRecipe;
                    }
                }
            }

            // Get all recipes for the tier
            AutoMap<AutoMap<GT_Recipe>> aMasterMap = new AutoMap<AutoMap<GT_Recipe>>();
            for (long i = 0; i <= aSpecialValue; i++) {
                aMasterMap.add(mTieredRecipeMap.get(i));
            }
            GT_Recipe aFoundRecipe = null;

            // Iterate the tiers recipes until we find the one with all inputs matching
            master: for (AutoMap<GT_Recipe> aTieredMap : aMasterMap) {
                for (GT_Recipe aRecipeToCheck : aTieredMap) {
                    if (aRecipeToCheck.isRecipeInputEqual(false, aFluids, aInputs)) {
                        log("Found recipe with matching inputs!");
                        if (aRecipeToCheck.mSpecialValue <= aSpecialValue) {
                            if (aRecipeToCheck.mEUt <= aVoltage) {
                                aFoundRecipe = aRecipeToCheck;
                                break master;
                            }
                        }
                    }
                }
            }

            // If we found a recipe, return it
            if (aFoundRecipe != null) {
                log("Found valid recipe.");
                return aFoundRecipe;
            }
        }
        log("Did not find valid recipe.");
        return null;
    }

    private int getCatalysts(ItemStack[] aItemInputs, ItemStack aRecipeCatalyst, int aMaxParrallel,
            ArrayList<ItemStack> aOutPut) {
        int allowedParallel = 0;
        for (final ItemStack aInput : aItemInputs) {
            if (aRecipeCatalyst.isItemEqual(aInput)) {
                int aDurabilityRemaining = getMaxCatalystDurability() - getDamage(aInput);
                return Math.min(aMaxParrallel, aDurabilityRemaining);
            }
        }
        return allowedParallel;
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

    private void damageCatalyst(ItemStack aStack, int parallelRecipes) {
        for (int i = 0; i < parallelRecipes; i++) {
            // Awakened Draconium Coils with Tungstensteel Pipe Casings (or above) no longer consume catalysts.
            if (this.mCoilTier >= 10 && this.mPipeCasingTier >= 4) {
                log("not consuming catalyst");
            } else if (MathUtils.randFloat(0, 10000000) / 10000000f < (1.2f - (0.2 * this.mPipeCasingTier))) {
                int damage = getDamage(aStack) + 1;
                log("damage catalyst " + damage);
                if (damage >= getMaxCatalystDurability()) {
                    log("consume catalyst");
                    addOutput(CI.getEmptyCatalyst(1));
                    aStack.stackSize -= 1;
                } else {
                    log("damaging catalyst");
                    setDamage(aStack, damage);
                }
            } else {
                log("not consuming catalyst");
            }
        }
    }

    private int getDamage(ItemStack aStack) {
        return ItemGenericChemBase.getCatalystDamage(aStack);
    }

    private void setDamage(ItemStack aStack, int aAmount) {
        ItemGenericChemBase.setCatalystDamage(aStack, aAmount);
    }

    /*
     * Catalyst Handling
     */
    @Override
    public ArrayList<ItemStack> getStoredInputs() {
        ArrayList<ItemStack> tItems = super.getStoredInputs();
        if (this.getGUIItemStack() != null) {
            tItems.add(this.getGUIItemStack());
        }
        for (GT_MetaTileEntity_Hatch_Catalysts tHatch : mCatalystBuses) {
            tHatch.mRecipeMap = getRecipeMap();
            if (isValidMetaTileEntity(tHatch)) {
                AutoMap<ItemStack> aHatchContent = tHatch.getContentUsageSlots();
                if (!aHatchContent.isEmpty()) {
                    tItems.addAll(aHatchContent);
                }
            }
        }
        return tItems;
    }
}

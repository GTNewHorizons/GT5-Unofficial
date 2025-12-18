package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
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
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

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
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.chemistry.general.ItemGenericChemBase;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers.MTEHatchMillingBalls;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;

public class MTEIsaMill extends GTPPMultiBlockBase<MTEIsaMill> implements ISurvivalConstructable {

    protected boolean boostEu = false;
    private int mCasing;
    private static IStructureDefinition<MTEIsaMill> STRUCTURE_DEFINITION = null;

    private static final IIconContainer frontFaceActive = new CustomIcon("iconsets/Grinder/GRINDER_ACTIVE5");
    private static final IIconContainer frontFace = new CustomIcon("iconsets/Grinder/GRINDER5");

    private final ArrayList<MTEHatchMillingBalls> mMillingBallBuses = new ArrayList<>();
    private static final DamageSource mIsaMillDamageSource = new DamageSource("gtpp.grinder").setDamageBypassesArmor();

    public MTEIsaMill(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIsaMill(String aName) {
        super(aName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo(EnumChatFormatting.GREEN + "G.O.G, Grinds Ores Good")
            .addPerfectOCInfo()
            .addPollutionAmount(getPollutionPerSecond(null))
            .addInfo(EnumChatFormatting.GREEN + "It'sa mill!")
            .beginStructureBlock(3, 3, 7, false)
            .addController("Front Center")
            .addCasingInfoMin("IsaMill Exterior Casing", 40, false)
            .addOtherStructurePart("IsaMill Gearbox", "5x, Inner Blocks")
            .addOtherStructurePart("IsaMill Piping", "8x, ring around controller")
            .addOtherStructurePart("Ball Housing", "Any Casing")
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addMufflerHatch("Any Casing", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIsaMill> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIsaMill>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "DDD", "CCC", "CCC", "CCC", "CCC", "CCC", "CCC" },
                            { "D~D", "CGC", "CGC", "CGC", "CGC", "CGC", "CCC" },
                            { "DDD", "CCC", "CCC", "CCC", "CCC", "CCC", "CCC" }, }))
                .addElement(
                    'C',
                    ofChain(
                        buildHatchAdder(MTEIsaMill.class).adder(MTEIsaMill::addMillingBallsHatch)
                            .hatchClass(MTEHatchMillingBalls.class)
                            .shouldReject(t -> !t.mMillingBallBuses.isEmpty())
                            .casingIndex(getCasingTextureIndex())
                            .hint(1)
                            .build(),
                        buildHatchAdder(MTEIsaMill.class)
                            .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy, Muffler)
                            .casingIndex(getCasingTextureIndex())
                            .hint(1)
                            .build(),
                        onElementPass(x -> ++x.mCasing, ofBlock(getCasingBlock(), getCasingMeta()))))
                .addElement('D', ofBlock(getIntakeBlock(), getIntakeMeta()))
                .addElement('G', ofBlock(getGearboxBlock(), getGearboxMeta()))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        mMillingBallBuses.clear();
        return checkPiece(mName, 1, 1, 0) && mCasing >= 48 - 8 && checkHatch();
    }

    @Override
    public boolean checkHatch() {
        return super.checkHatch() && mMillingBallBuses.size() == 1;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return frontFaceActive;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return frontFace;
    }

    @Override
    protected int getCasingTextureId() {
        return TAE.GTPP_INDEX(2);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return getMaxEfficiency(aStack) > 0;
    }

    private boolean addMillingBallsHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof MTEHatchMillingBalls) {
                return addToMachineListInternal(mMillingBallBuses, aMetaTileEntity, aBaseCasingIndex);
            }
        }
        return false;
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {

        final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof MTEHatchMillingBalls) {
            log("Found MTEHatchMillingBalls");
            return addToMachineListInternal(mMillingBallBuses, aMetaTileEntity, aBaseCasingIndex);
        }
        return super.addToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.millingRecipes;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (this.mUpdate == 1 || this.mStartUpCheck == 1) {
                this.mMillingBallBuses.clear();
            }
        }
        if (aTick % 20 == 0 && isMachineRunning()) {
            checkForEntities(aBaseMetaTileEntity, aTick);
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    private final ArrayList<BlockPos> mFrontBlockPosCache = new ArrayList<>();

    public void checkForEntities(IGregTechTileEntity aBaseMetaTileEntity, long aTime) {

        if (aTime % 100 == 0) {
            mFrontBlockPosCache.clear();
        }
        if (mFrontBlockPosCache.isEmpty()) {
            ForgeDirection tSide = aBaseMetaTileEntity.getBackFacing();
            int aTileX = aBaseMetaTileEntity.getXCoord();
            int aTileY = aBaseMetaTileEntity.getYCoord();
            int aTileZ = aBaseMetaTileEntity.getZCoord();
            boolean xFacing = tSide.offsetX != 0;
            boolean zFacing = tSide.offsetZ != 0;

            // Check Casings
            int aDepthOffset = (tSide == ForgeDirection.NORTH || tSide == ForgeDirection.WEST) ? 1 : -1;
            for (int aHorizontalOffset = -1; aHorizontalOffset < 2; aHorizontalOffset++) {
                for (int aVerticalOffset = -1; aVerticalOffset < 2; aVerticalOffset++) {
                    int aX = !xFacing ? (aTileX + aHorizontalOffset) : (aTileX + aDepthOffset);
                    int aY = aTileY + aVerticalOffset;
                    int aZ = !zFacing ? (aTileZ + aHorizontalOffset) : (aTileZ + aDepthOffset);
                    mFrontBlockPosCache.add(new BlockPos(aX, aY, aZ, aBaseMetaTileEntity.getWorld()));
                }
            }
        }

        final ArrayList<EntityLivingBase> aEntities = getEntities(mFrontBlockPosCache, aBaseMetaTileEntity.getWorld());
        final boolean generateParticles = aBaseMetaTileEntity.isClientSide() && aBaseMetaTileEntity.isActive();

        for (EntityLivingBase aFoundEntity : aEntities) {
            if (aFoundEntity.getHealth() <= 0) continue;

            if (aFoundEntity instanceof EntityPlayer aPlayer) {
                if (aPlayer.capabilities.isCreativeMode) continue;
                if (aPlayer.capabilities.disableDamage) continue;
                final int damage = getPlayerDamageValue(aPlayer, 10);
                aFoundEntity.attackEntityFrom(mIsaMillDamageSource, damage);
            } else {
                final int damage = Math.max(1, (int) (aFoundEntity.getMaxHealth() / 3));
                aFoundEntity.attackEntityFrom(mIsaMillDamageSource, damage);
            }

            if (generateParticles) generateParticles(aFoundEntity);
        }
    }

    // 20 armor points add 80% damage reduction, more points add more damage reduction
    private int getPlayerDamageValue(EntityPlayer player, int damage) {
        int armorValue = player.getTotalArmorValue();
        int reducedDamage = (int) (damage - damage * (armorValue * 0.04));
        return Math.max(reducedDamage, 0);
    }

    private static ArrayList<EntityLivingBase> getEntities(ArrayList<BlockPos> aPositionsToCheck, World aWorld) {
        ArrayList<EntityLivingBase> aEntities = new ArrayList<>();
        HashSet<Chunk> aChunksToCheck = new HashSet<>();
        if (!aPositionsToCheck.isEmpty()) {
            Chunk aLocalChunk;
            for (BlockPos aPos : aPositionsToCheck) {
                aLocalChunk = aWorld.getChunkFromBlockCoords(aPos.xPos, aPos.zPos);
                aChunksToCheck.add(aLocalChunk);
            }
        }
        // early exit
        if (aChunksToCheck.isEmpty()) return aEntities;

        for (Chunk aChunk : aChunksToCheck) {
            if (!aChunk.isChunkLoaded) continue;

            List[] aEntityLists = aChunk.entityLists;
            for (List aEntitySubList : aEntityLists) {
                for (Object aEntity : aEntitySubList) {
                    if (!(aEntity instanceof EntityLivingBase aPlayer)) continue;

                    BlockPos aPlayerPos = EntityUtils.findBlockPosOfEntity(aPlayer);
                    for (BlockPos aBlockSpaceToCheck : aPositionsToCheck) {
                        if (aBlockSpaceToCheck.equals(aPlayerPos)) {
                            aEntities.add(aPlayer);
                        }
                    }
                }
            }

        }

        return aEntities;
    }

    private static void generateParticles(EntityLivingBase aEntity) {
        BlockPos aPlayerPosBottom = EntityUtils.findBlockPosOfEntity(aEntity);
        BlockPos aPlayerPosTop = aPlayerPosBottom.getUp();
        ArrayList<BlockPos> aEntityPositions = new ArrayList<>();
        aEntityPositions.add(aPlayerPosBottom);
        aEntityPositions.add(aPlayerPosTop);
        for (int i = 0; i < 64; i++) {
            BlockPos aEffectPos = aEntityPositions.get(aEntity.height > 1f ? MathUtils.randInt(0, 1) : 0);
            float aOffsetX = MathUtils.randFloat(-0.35f, 0.35f);
            float aOffsetY = MathUtils.randFloat(-0.25f, 0.35f);
            float aOffsetZ = MathUtils.randFloat(-0.35f, 0.35f);
            aEntity.worldObj.spawnParticle(
                "reddust",
                aEffectPos.xPos + aOffsetX,
                aEffectPos.yPos + 0.3f + aOffsetY,
                aEffectPos.zPos + aOffsetZ,
                0.0D,
                0.0D,
                0.0D);
        }
    }

    public Block getCasingBlock() {
        return ModBlocks.blockCasings5Misc;
    }

    public byte getCasingMeta() {
        return 0;
    }

    public Block getIntakeBlock() {
        return ModBlocks.blockCasings5Misc;
    }

    public byte getIntakeMeta() {
        return 1;
    }

    public Block getGearboxBlock() {
        return ModBlocks.blockCasings5Misc;
    }

    public byte getGearboxMeta() {
        return 2;
    }

    public byte getCasingTextureIndex() {
        return 66;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEIsaMill(this.mName);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 1;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIsaMill;
    }

    @Override
    public String[] getExtraInfoData() {
        return new String[] { "IsaMill Grinding Machine", "Current Efficiency: " + (mEfficiency / 100) + "%",
            getIdealStatus() == getRepairStatus() ? "No Maintainance issues" : "Needs Maintainance" };
    }

    @Override
    public String getMachineType() {
        return "Grinding Machine, IGM";
    }

    /*
     * Milling Ball Handling
     */

    @Override
    public ArrayList<ItemStack> getStoredInputsForColor(Optional<Byte> color) {
        ArrayList<ItemStack> tItems = super.getStoredInputsForColor(color);
        for (MTEHatchMillingBalls tHatch : validMTEList(mMillingBallBuses)) {
            byte busColor = tHatch.getBaseMetaTileEntity()
                .getColorization();
            if (color.isPresent() && busColor != -1 && busColor != color.get()) continue;
            ArrayList<ItemStack> aHatchContent = tHatch.getContentUsageSlots();
            if (!aHatchContent.isEmpty()) {
                tItems.addAll(aHatchContent);
            }
        }
        return tItems;
    }

    public int getMaxBallDurability(ItemStack aStack) {
        return ItemGenericChemBase.getMaxBallDurability(aStack);
    }

    public static boolean isMillingBall(ItemStack aStack) {
        if (GTUtility.areStacksEqual(aStack, GregtechItemList.Milling_Ball_Alumina.get(1), true)) {
            return true;
        }
        return GTUtility.areStacksEqual(aStack, GregtechItemList.Milling_Ball_Soapstone.get(1), true);
    }

    private ItemStack findMillingBall(ItemStack[] aItemInputs) {
        if (mMillingBallBuses.size() != 1) return null;

        MTEHatchMillingBalls aBus = mMillingBallBuses.get(0);

        if (aBus == null) return null;

        ArrayList<ItemStack> aAvailableItems = aBus.getContentUsageSlots();

        if (aAvailableItems.isEmpty()) return null;

        for (final ItemStack aInput : aItemInputs) {
            if (!isMillingBall(aInput)) continue;

            for (ItemStack aBall : aAvailableItems) {
                if (!GTUtility.areStacksEqual(aBall, aInput, true)) continue;

                Logger.INFO("Found a valid milling ball to use.");
                return aBall;
            }

        }

        return null;
    }

    private void damageMillingBall(ItemStack aStack) {
        if (MathUtils.randFloat(0, 10000000) / 10000000f < (1.2f - (0.2 * 1))) {
            int damage = getMillingBallDamage(aStack) + 1;
            log("damage milling ball " + damage);
            if (damage >= getMaxBallDurability(aStack)) {
                log("consuming milling ball");
                aStack.stackSize -= 1;
            } else {
                setDamage(aStack, damage);
            }
        } else {
            log("not damaging milling ball");
        }
    }

    private int getMillingBallDamage(ItemStack aStack) {
        return ItemGenericChemBase.getMillingBallDamage(aStack);
    }

    private void setDamage(ItemStack aStack, int aAmount) {
        ItemGenericChemBase.setMillingBallDamage(aStack, aAmount);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            ItemStack millingBall;

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                millingBall = findMillingBall(inputItems);
                if (millingBall == null) {
                    return SimpleCheckRecipeResult.ofFailure("no_milling_ball");
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            public CheckRecipeResult process() {
                CheckRecipeResult result = super.process();
                if (result.wasSuccessful()) {
                    damageMillingBall(millingBall);
                }
                return result;
            }
        }.enablePerfectOverclock();
    }
}

package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.pollution.PollutionConfig;
import gregtech.common.tileentities.machines.IDualInputHatch;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEIndustrialMacerator extends GTPPMultiBlockBase<MTEIndustrialMacerator>
    implements ISurvivalConstructable {

    private int controllerTier = 1;
    private int structureTier;
    private int mCasing;

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final int HORIZONTAL_OFF_SET = 1;
    private static final int VERTICAL_OFF_SET = 5;
    private static final int DEPTH_OFF_SET = 0;
    private static IStructureDefinition<MTEIndustrialMacerator> STRUCTURE_DEFINITION = null;

    @Nullable
    private static Integer getStructureCasingTier(Block b, int m) {
        if (b == GregTechAPI.sBlockCasings4 && m == 2) return 1;
        if (b == ModBlocks.blockCasingsMisc && m == 7) return 2;
        return null;
    }

    public MTEIndustrialMacerator(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialMacerator(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialMacerator(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Macerator, IMS";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo(TooltipHelper.parallelText("Voltage Tier * n") + " Parallels")
            .addInfo("n=2 initially. n=8 after inserting Maceration Upgrade Chip")
            .addStaticSpeedInfo(1.6f)
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 6, 3, true)
            .addController("Bottom center")
            .addCasingInfoMin("Maceration Stack Casings (After upgrade)", 26, false)
            .addCasingInfoMin("Stable Titanium Casings (Before upgrade)", 26, false)
            .addInputBus("Any casing", 1)
            .addOutputBus("Any casing", 1)
            .addEnergyHatch("Any casing", 1)
            .addMaintenanceHatch("Any casing", 1)
            .addMufflerHatch("Any casing", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialMacerator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialMacerator>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    // spotless:off
                    transpose(
                        new String[][] {
                            {"AAA","AAA","AAA"},
                            {"AAA","A A","AAA"},
                            {"AAA","A A","AAA"},
                            {"AAA","A A","AAA"},
                            {"AAA","A A","AAA"},
                            {"A~A","AAA","AAA"} }))
                //spotless:on
                .addElement(
                    'A',
                    ofChain(
                        buildHatchAdder(MTEIndustrialMacerator.class)
                            .atLeast(Energy, Maintenance, InputBus, Muffler, OutputBus)
                            .casingIndex(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings4, 2))
                            .allowOnly(ForgeDirection.NORTH)
                            .hint(1)
                            .build(),
                        onElementPass(
                            m -> m.mCasing++,
                            ofBlocksTiered(
                                MTEIndustrialMacerator::getStructureCasingTier,
                                ImmutableList
                                    .of(Pair.of(GregTechAPI.sBlockCasings4, 2), Pair.of(ModBlocks.blockCasingsMisc, 7)),
                                -1,
                                (m, t) -> m.structureTier = t,
                                m -> m.structureTier))))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            hintsOnly,
            HORIZONTAL_OFF_SET,
            VERTICAL_OFF_SET,
            DEPTH_OFF_SET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            HORIZONTAL_OFF_SET,
            VERTICAL_OFF_SET,
            DEPTH_OFF_SET,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        structureTier = -1;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET)) return false;
        if (structureTier < 1 || mCasing < 26 || !checkHatch()) return false;
        updateHatchTexture();
        return structureTier >= controllerTier;
    }

    protected void updateHatchTexture() {
        int textureID = getCasingTextureId();
        for (MTEHatch h : mInputBusses) h.updateTexture(textureID);
        for (IDualInputHatch h : mDualInputHatches) h.updateTexture(textureID);
        for (MTEHatch h : mOutputBusses) h.updateTexture(textureID);
        for (MTEHatch h : mMaintenanceHatches) h.updateTexture(textureID);
        for (MTEHatch h : mMufflerHatches) h.updateTexture(textureID);
        for (MTEHatch h : mEnergyHatches) h.updateTexture(textureID);
    }

    @Override
    public boolean checkHatch() {
        return !mMufflerHatches.isEmpty() && !mMaintenanceHatches.isEmpty()
            && !mOutputBusses.isEmpty()
            && (!mInputBusses.isEmpty() || !mDualInputHatches.isEmpty());
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.GTCEU_LOOP_MACERATOR;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.Overlay_MatterFab_Active;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.Overlay_MatterFab_Active_Glow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.Overlay_MatterFab;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.Overlay_MatterFab_Glow;
    }

    @Override
    protected int getCasingTextureId() {
        if (structureTier == 2) return TAE.GTPP_INDEX(7);
        return GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings4, 2);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.maceratorRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -10;
    }

    @Override
    public void onPreTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if ((aBaseMetaTileEntity.isClientSide()) && (aBaseMetaTileEntity.isActive())
            && (aBaseMetaTileEntity.getFrontFacing() != ForgeDirection.UP)
            && (!aBaseMetaTileEntity.hasCoverAtSide(ForgeDirection.UP))
            && (!aBaseMetaTileEntity.getOpacityAtSide(ForgeDirection.UP))) {
            final Random tRandom = aBaseMetaTileEntity.getWorld().rand;
            aBaseMetaTileEntity.getWorld()
                .spawnParticle(
                    "smoke",
                    (aBaseMetaTileEntity.getXCoord() + 0.8F) - (tRandom.nextFloat() * 0.6F),
                    aBaseMetaTileEntity.getYCoord() + 0.3f + (tRandom.nextFloat() * 0.2F),
                    (aBaseMetaTileEntity.getZCoord() + 1.2F) - (tRandom.nextFloat() * 1.6F),
                    0.0D,
                    0.0D,
                    0.0D);
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && aTick % 20 == 0 && controllerTier == 1) {
            ItemStack aGuiStack = this.getControllerSlot();
            if (GregtechItemList.Maceration_Upgrade_Chip.isStackEqual(aGuiStack, false, true)) {
                controllerTier = 2;
                mInventory[1] = ItemUtils.depleteStack(aGuiStack, 1);
                markDirty();
                // schedule a structure check
                mUpdated = true;
            }
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        if (controllerTier == 1 && !aPlayer.isSneaking()) {
            ItemStack heldItem = aPlayer.getHeldItem();
            if (GregtechItemList.Maceration_Upgrade_Chip.isStackEqual(heldItem, false, true)) {
                controllerTier = 2;
                aPlayer.setCurrentItemOrArmor(0, ItemUtils.depleteStack(heldItem, 1));
                if (getBaseMetaTileEntity().isServerSide()) {
                    markDirty();
                    aPlayer.inventory.markDirty();
                    // schedule a structure check
                    mUpdated = true;
                }
                return true;
            }
        }
        return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);
    }

    @Override
    public void onValueUpdate(byte aValue) {
        structureTier = aValue;
    }

    @Override
    public byte getUpdateData() {
        return (byte) structureTier;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mTier", (byte) controllerTier);
        aNBT.setByte("structureTier", (byte) structureTier);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (!aNBT.hasKey("mTier", NBT.TAG_BYTE))
            // we assume old macerators are all T2 variants, as they were made before price reduction and shouldn't need
            // to worry about upgrading
            controllerTier = 2;
        else controllerTier = aNBT.getByte("mTier");

        structureTier = aNBT.getByte("structureTier");
    }

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {
        super.initDefaultModes(aNBT);
        if (aNBT == null || !aNBT.hasKey("mTier")) {
            controllerTier = 1;
        } else {
            controllerTier = aNBT.getByte("mTier");
        }
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        super.setItemNBT(aNBT);
        if (controllerTier > 1) aNBT.setByte("mTier", (byte) controllerTier);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().noRecipeCaching()
            .setSpeedBonus(1F / 1.6F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        final long tVoltage = getMaxInputVoltage();
        final byte tTier = (byte) Math.max(1, GTUtility.getTier(tVoltage));
        return Math.max(1, (controllerTier == 1 ? 2 : 8) * tTier);
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialMacerator;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("tier", controllerTier);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.hasKey("tier")) {
            currentTip.add(
                StatCollector.translateToLocal("GT5U.machines.tier") + ": "
                    + EnumChatFormatting.YELLOW
                    + formatNumber(tag.getInteger("tier"))
                    + EnumChatFormatting.RESET);
        }
    }
}

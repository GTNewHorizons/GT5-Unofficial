package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GregtechMetaTileEntity_IndustrialMacerator
    extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialMacerator> implements ISurvivalConstructable {

    private int controllerTier = 1;
    private int mCasing;

    private static final String tier1 = "tier1";
    private static final String tier2 = "tier2";

    private static final int HORIZONTAL_OFF_SET = 1;
    private static final int VERTICAL_OFF_SET = 5;
    private static final int DEPTH_OFF_SET = 0;
    private static IStructureDefinition<GregtechMetaTileEntity_IndustrialMacerator> STRUCTURE_DEFINITION = null;

    public GregtechMetaTileEntity_IndustrialMacerator(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_IndustrialMacerator(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_IndustrialMacerator(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Macerator/Pulverizer";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType())
            .addInfo("Controller block for the Industrial Maceration Stack")
            .addInfo("60% faster than using single block machines of the same voltage")
            .addInfo("Maximum of n*tier parallels, LV = Tier 1, MV = Tier 2, etc.")
            .addInfo("n=2 initially. n=8 after inserting Maceration Upgrade Chip.")
            .addPollutionAmount(getPollutionPerSecond(null))
            .addSeparator()
            .beginStructureBlock(3, 6, 3, true)
            .addController("Bottom center")
            .addCasingInfoMin("Maceration Stack Casings (After upgrade)", 26, false)
            .addCasingInfoMin("Stable Titanium Casings (Before upgrade)", 26, false)
            .addInputBus("Bottom casing", 1)
            .addEnergyHatch("Any casing", 1)
            .addMaintenanceHatch("Any casing", 1)
            .addOutputBus("One per layer except bottom layer", 2)
            .addMufflerHatch("Any casing except bottom layer", 2)
            .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    // spotless:off
    @Override
    public IStructureDefinition<GregtechMetaTileEntity_IndustrialMacerator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialMacerator>builder()
                .addShape(
                    tier1,
                    transpose(
                        new String[][] {
                            {"AAA","AAA","AAA"},
                            {"AAA","A A","AAA"},
                            {"AAA","A A","AAA"},
                            {"AAA","A A","AAA"},
                            {"AAA","A A","AAA"},
                            {"A~A","AAA","AAA"} }))
                .addShape(
                    tier2,
                    transpose(
                        new String[][] {
                            {"BBB","BBB","BBB"},
                            {"BBB","B B","BBB"},
                            {"BBB","B B","BBB"},
                            {"BBB","B B","BBB"},
                            {"BBB","B B","BBB"},
                            {"B~B","BBB","BBB"} }))
                .addElement(
                    'B',
                    ofChain(
                        buildHatchAdder(GregtechMetaTileEntity_IndustrialMacerator.class)
                            .atLeast(Energy, Maintenance, InputBus, Muffler, OutputBus)
                            .casingIndex(TAE.GTPP_INDEX(7))
                            .allowOnly(ForgeDirection.NORTH)
                            .dot(1)
                            .build(),
                        onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasingsMisc, 7))))
                .addElement(
                    'A',
                    ofChain(
                        buildHatchAdder(GregtechMetaTileEntity_IndustrialMacerator.class)
                            .atLeast(Energy, Maintenance, InputBus, Muffler, OutputBus)
                            .casingIndex(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings4, 2))
                            .allowOnly(ForgeDirection.NORTH)
                            .dot(1)
                            .build(),
                        onElementPass(x -> ++x.mCasing, ofBlock(GregTech_API.sBlockCasings4, 2))))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }
    //spotless:on

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        if (stackSize.stackSize == 1) {
            this.buildPiece(tier1, stackSize, hintsOnly, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET);
        } else {
            this.buildPiece(tier2, stackSize, hintsOnly, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        int built = 0;
        if (stackSize.stackSize == 1) {
            controllerTier = 1;
            updateHatchTexture();
            built += this.survivialBuildPiece(
                tier1,
                stackSize,
                HORIZONTAL_OFF_SET,
                VERTICAL_OFF_SET,
                DEPTH_OFF_SET,
                elementBudget,
                env,
                false,
                true);
        } else {
            controllerTier = 2;
            updateHatchTexture();
            built += this.survivialBuildPiece(
                tier2,
                stackSize,
                HORIZONTAL_OFF_SET,
                VERTICAL_OFF_SET,
                DEPTH_OFF_SET,
                elementBudget,
                env,
                false,
                true);
        }
        return built;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        if (controllerTier == 1) {
            if (checkPiece(tier1, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET)) {
                return mCasing >= 26 && checkHatch();
            }
        } else if (checkPiece(tier2, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET)) {
            return mCasing >= 26 && checkHatch();
        }
        return false;
    }

    protected void updateHatchTexture() {
        for (GT_MetaTileEntity_Hatch h : mInputBusses) h.updateTexture(getCasingTextureId());
        for (GT_MetaTileEntity_Hatch h : mOutputBusses) h.updateTexture(getCasingTextureId());
        for (GT_MetaTileEntity_Hatch h : mMaintenanceHatches) h.updateTexture(getCasingTextureId());
        for (GT_MetaTileEntity_Hatch h : mMufflerHatches) h.updateTexture(getCasingTextureId());
        for (GT_MetaTileEntity_Hatch h : mEnergyHatches) h.updateTexture(getCasingTextureId());
    }

    @Override
    public boolean checkHatch() {
        return !mMufflerHatches.isEmpty() && !mMaintenanceHatches.isEmpty()
            && !mOutputBusses.isEmpty()
            && !mInputBusses.isEmpty();

    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.IC2_MACHINES_MACERATOR_OP;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.Overlay_MatterFab_Active;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.Overlay_MatterFab;
    }

    @Override
    protected int getCasingTextureId() {
        if (controllerTier == 2) return TAE.GTPP_INDEX(7);
        return GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings4, 2);
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
            && (aBaseMetaTileEntity.getCoverIDAtSide(ForgeDirection.UP) == 0)
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
                mInventory[1] = ItemUtils.depleteStack(aGuiStack);
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
                aPlayer.setCurrentItemOrArmor(0, ItemUtils.depleteStack(heldItem));
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
        controllerTier = aValue;
    }

    @Override
    public byte getUpdateData() {
        return (byte) controllerTier;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mTier", (byte) controllerTier);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (!aNBT.hasKey("mTier", NBT.TAG_BYTE))
            // we assume old macerators are all T2 variants, as they were made before price reduction and shouldn't need
            // to worry about upgrading
            controllerTier = 2;
        else controllerTier = aNBT.getByte("mTier");
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
        aNBT.setByte("mTier", (byte) controllerTier);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 1.6F)
            .setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public int getMaxParallelRecipes() {
        final long tVoltage = getMaxInputVoltage();
        final byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
        return Math.max(1, (controllerTier == 1 ? 2 : 8) * tTier);
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialMacerator;
    }

    @Override
    public boolean isOverclockerUpgradable() {
        return true;
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
                "Tier: " + EnumChatFormatting.YELLOW
                    + GT_Utility.formatNumbers(tag.getInteger("tier"))
                    + EnumChatFormatting.RESET);
        }
    }
}

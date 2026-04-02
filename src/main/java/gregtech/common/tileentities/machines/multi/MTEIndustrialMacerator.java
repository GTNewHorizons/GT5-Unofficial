package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.List;
import java.util.Random;

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

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.INEIPreviewModifier;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.pollution.PollutionConfig;
import gregtech.common.tileentities.machines.IDualInputHatch;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEIndustrialMacerator extends MTEExtendedPowerMultiBlockBase<MTEIndustrialMacerator>
    implements ISurvivalConstructable, INEIPreviewModifier {

    private int controllerTier = 1;
    private int structureTier;
    private int casingAmount;

    private static final String STRUCTURE_PIECE_MAIN_T1 = "main_t1";
    private static final String STRUCTURE_PIECE_MAIN_T2 = "main_t2";

    private static final int OFFSET_X_T1 = 1;
    private static final int OFFSET_Y_T1 = 5;
    private static final int OFFSET_Z_T1 = 0;

    private static final int OFFSET_X_T2 = 2;
    private static final int OFFSET_Y_T2 = 6;
    private static final int OFFSET_Z_T2 = 0;

    private static IStructureDefinition<MTEIndustrialMacerator> STRUCTURE_DEFINITION = null;

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
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Macerator, IMS")
            .addInfo(TooltipHelper.parallelText("Voltage Tier * n") + " Parallels")
            .addInfo("n=2 initially. n=8 after inserting Maceration Upgrade Chip")
            .addStaticSpeedInfo(1.6f)
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 6, 3, true)
            .addController("Front bottom center")
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
                    STRUCTURE_PIECE_MAIN_T1,
                    transpose(
                        new String[][] { { "CCC", "CCC", "CCC" }, { "CCC", "C C", "CCC" }, { "CCC", "C C", "CCC" },
                            { "CCC", "C C", "CCC" }, { "CCC", "C C", "CCC" }, { "C~C", "CCC", "CCC" } }))
                .addShape(
                    STRUCTURE_PIECE_MAIN_T2,
                    transpose(
                        new String[][] { { "CCCCC", "C   C", "C   C", "C   C", "CCCCC" },
                            { "DCCCD", "CA AC", "CA AC", "CA AC", "DCECD" },
                            { "DCECD", "C B C", "C B C", "C B C", "DCECD" },
                            { "DCECD", "CA AC", "CA AC", "CA AC", "DCECD" },
                            { "DCECD", "C B C", "C B C", "C B C", "DCECD" },
                            { "DCCCD", "CA AC", "CA AC", "CA AC", "DCECD" },
                            { "CC~CC", "CCCCC", "CCCCC", "CCCCC", "CCCCC" } }))
                .addElement(
                    'C',
                    buildHatchAdder(MTEIndustrialMacerator.class)
                        .atLeast(Energy, Maintenance, InputBus, Muffler, OutputBus)
                        .casingIndex(Casings.StableTitaniumMachineCasing.textureId)
                        .hint(1)
                        .allowOnly(ForgeDirection.NORTH)
                        .buildAndChain(
                            onElementPass(m -> m.casingAmount++, Casings.StableTitaniumMachineCasing.asElement())))
                .addElement('A', Casings.SteelGearBoxCasing.asElement())
                .addElement('B', Casings.GrateMachineCasing.asElement())
                .addElement('E', chainAllGlasses())
                .addElement('D', ofFrame(Materials.Titanium))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        int tier = getTierFromHint(stackSize);
        String piece = tier == 2 ? STRUCTURE_PIECE_MAIN_T2 : STRUCTURE_PIECE_MAIN_T1;
        int offX = tier == 2 ? OFFSET_X_T2 : OFFSET_X_T1;
        int offY = tier == 2 ? OFFSET_Y_T2 : OFFSET_Y_T1;
        int offZ = tier == 2 ? OFFSET_Z_T2 : OFFSET_Z_T1;
        this.buildPiece(piece, stackSize, hintsOnly, offX, offY, offZ);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        int tier = getTierFromHint(stackSize);
        String piece = tier == 2 ? STRUCTURE_PIECE_MAIN_T2 : STRUCTURE_PIECE_MAIN_T1;
        int offX = tier == 2 ? OFFSET_X_T2 : OFFSET_X_T1;
        int offY = tier == 2 ? OFFSET_Y_T2 : OFFSET_Y_T1;
        int offZ = tier == 2 ? OFFSET_Z_T2 : OFFSET_Z_T1;
        return survivalBuildPiece(piece, stackSize, offX, offY, offZ, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmount = 0;
        structureTier = -1;
        if (!checkPiece(getActiveStructurePiece(), getActiveOffsetX(), getActiveOffsetY(), getActiveOffsetZ()))
            return false;
        if (controllerTier == 2) {
            structureTier = 2;
        } else structureTier = 1;
        if (structureTier < 1 || casingAmount < 26 || !checkHatch()) return false;
        updateHatchTexture();
        return true;
    }

    protected void updateHatchTexture() {
        int textureID = Casings.StableTitaniumMachineCasing.textureId;
        for (MTEHatch h : mInputBusses) h.updateTexture(textureID);
        for (IDualInputHatch h : mDualInputHatches) h.updateTexture(textureID);
        for (MTEHatch h : mOutputBusses) h.updateTexture(textureID);
        for (MTEHatch h : mMaintenanceHatches) h.updateTexture(textureID);
        for (MTEHatch h : mMufflerHatches) h.updateTexture(textureID);
        for (MTEHatch h : mEnergyHatches) h.updateTexture(textureID);
    }

    public boolean checkHatch() {
        return !mMufflerHatches.isEmpty() && !mOutputBusses.isEmpty()
            && (!mInputBusses.isEmpty() || !mDualInputHatches.isEmpty());
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Casings.StableTitaniumMachineCasing.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.Overlay_MatterFab_Active)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.Overlay_MatterFab_Active_Glow)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.StableTitaniumMachineCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(TexturesGtBlock.Overlay_MatterFab)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.Overlay_MatterFab_Glow)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.StableTitaniumMachineCasing.getCasingTexture() };
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.GTCEU_LOOP_MACERATOR;
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

    private String getActiveStructurePiece() {
        return controllerTier == 2 ? STRUCTURE_PIECE_MAIN_T2 : STRUCTURE_PIECE_MAIN_T1;
    }

    private int getActiveOffsetX() {
        return controllerTier == 2 ? OFFSET_X_T2 : OFFSET_X_T1;
    }

    private int getActiveOffsetY() {
        return controllerTier == 2 ? OFFSET_Y_T2 : OFFSET_Y_T1;
    }

    private int getActiveOffsetZ() {
        return controllerTier == 2 ? OFFSET_Z_T2 : OFFSET_Z_T1;
    }

    private int getTierFromHint(ItemStack stackSize) {
        if (stackSize == null || stackSize.stackSize <= 1) return 1;
        return 2;
    }

    @Override
    public void onPreviewConstruct(@NotNull ItemStack trigger) {
        if (trigger.stackSize >= 2) {
            controllerTier = 2;
        }
    }
}

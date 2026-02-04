package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;
import static gregtech.api.util.GTWaila.getMachineProgressString;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.input.Keyboard;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.HarvestTool;
import gregtech.api.enums.ParticleFX;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.SteamVariant;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.interfaces.ISecondaryDescribable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IGetTitleColor;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.RecipeMapWorkable;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.WorldSpawnedEventBuilder;
import gregtech.api.util.WorldSpawnedEventBuilder.ParticleEventBuilder;
import gregtech.client.GTSoundLoop;
import gregtech.common.gui.modularui.multiblock.MTEBrickedBlastFurnaceGui;
import gregtech.common.pollution.Pollution;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEBrickedBlastFurnace extends MetaTileEntity
    implements IAlignment, ISurvivalConstructable, RecipeMapWorkable, IGetTitleColor, ISecondaryDescribable {

    public static final int INPUT_SLOTS = 3, OUTPUT_SLOTS = 3;
    private static final IStructureDefinition<MTEBrickedBlastFurnace> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTEBrickedBlastFurnace>builder()
        .addShape(
            "main",
            transpose(
                new String[][] { { "ccc", "c-c", "ccc" }, { "ccc", "clc", "ccc" }, { "c~c", "clc", "ccc" },
                    { "ccc", "ccc", "ccc" }, }))
        .addElement('c', lazy(t -> ofBlock(GregTechAPI.sBlockCasings4, 15)))
        .addElement('l', ofChain(isAir(), ofBlockAnyMeta(Blocks.lava, 1), ofBlockAnyMeta(Blocks.flowing_lava, 1)))
        .build();
    private static final ITexture[] FACING_SIDE = { TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_DENSEBRICKS) };
    private static final ITexture[] FACING_FRONT = {
        TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_BRICKEDBLASTFURNACE_INACTIVE) };
    private static final ITexture[] FACING_ACTIVE = {
        TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_BRICKEDBLASTFURNACE_ACTIVE), TextureFactory.builder()
            .addIcon(Textures.BlockIcons.MACHINE_CASING_BRICKEDBLASTFURNACE_ACTIVE_GLOW)
            .glow()
            .build() };

    private MultiblockTooltipBuilder tooltipBuilder;

    public int mMaxProgresstime = 0;
    private volatile boolean mUpdated;
    public int mUpdate = 5;
    public int mProgresstime = 0;
    public boolean mMachine = false;

    @SideOnly(Side.CLIENT)
    protected GTSoundLoop activitySoundLoop;

    public ItemStack[] mOutputItems = new ItemStack[OUTPUT_SLOTS];

    public MTEBrickedBlastFurnace(Args args) {
        super(
            args.toBuilder()
                .inventorySlotCount(INPUT_SLOTS + OUTPUT_SLOTS)
                .build());
    }

    @Deprecated
    public MTEBrickedBlastFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, INPUT_SLOTS + OUTPUT_SLOTS);
    }

    @Deprecated
    public MTEBrickedBlastFurnace(String aName) {
        super(aName, INPUT_SLOTS + OUTPUT_SLOTS);
    }

    @Override
    public String[] getDescription() {
        return getCurrentDescription();
    }

    @Override
    public boolean isDisplaySecondaryDescription() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
    }

    @Override
    public String[] getPrimaryDescription() {
        return getTooltip().getInformation();
    }

    @Override
    public String[] getSecondaryDescription() {
        return getTooltip().getStructureInformation();
    }

    protected MultiblockTooltipBuilder getTooltip() {
        if (tooltipBuilder == null) {
            tooltipBuilder = new MultiblockTooltipBuilder();
            tooltipBuilder.addMachineType("Blast Furnace, BBF")
                .addInfo("Usable for Steel and general Pyrometallurgy")
                .addInfo("Has a useful interface, unlike other gregtech multis")
                .addPollutionAmount(GTMod.proxy.mPollutionPrimitveBlastFurnacePerSecond)
                .beginStructureBlock(3, 4, 3, true)
                .addController("Front center")
                .addOtherStructurePart("Firebricks", "Everything except the controller")
                .addStructureInfo("The top block is also empty")
                .addStructureInfo("You can share the walls of GT multis, so")
                .addStructureInfo("each additional one costs less, up to 4")
                .toolTipFinisher();
        }
        return tooltipBuilder;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return getTooltip().getStructureHint();
    }

    @Override
    public boolean isTeleporterCompatible() {
        return false;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return (facing.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            return aActive ? FACING_ACTIVE : FACING_FRONT;
        }
        return FACING_SIDE;
    }

    @Override
    public int getProgresstime() {
        return this.mProgresstime;
    }

    @Override
    public int maxProgresstime() {
        return this.mMaxProgresstime;
    }

    @Override
    public int increaseProgress(int aProgress) {
        this.mProgresstime += aProgress;
        return this.mMaxProgresstime - this.mProgresstime;
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, ItemStack coverItem) {
        return (CoverRegistry.getCoverPlacer(coverItem)
            .allowOnPrimitiveBlock()) && (super.allowCoverOnSide(side, coverItem));
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBrickedBlastFurnace(getPrototype());
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mProgresstime", this.mProgresstime);
        aNBT.setInteger("mMaxProgresstime", this.mMaxProgresstime);
        if (this.mOutputItems != null) {
            for (int i = 0; i < mOutputItems.length; i++) {
                if (this.mOutputItems[i] != null) {
                    NBTTagCompound tNBT = new NBTTagCompound();
                    this.mOutputItems[i].writeToNBT(tNBT);
                    aNBT.setTag("mOutputItem" + i, tNBT);
                }
            }
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.mUpdate = 5;
        this.mProgresstime = aNBT.getInteger("mProgresstime");
        this.mMaxProgresstime = aNBT.getInteger("mMaxProgresstime");
        this.mOutputItems = new ItemStack[OUTPUT_SLOTS];
        for (int i = 0; i < OUTPUT_SLOTS; i++) {
            this.mOutputItems[i] = GTUtility.loadItem(aNBT, "mOutputItem" + i);
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public ExtendedFacing getExtendedFacing() {
        return ExtendedFacing.of(getBaseMetaTileEntity().getFrontFacing());
    }

    @Override
    public void setExtendedFacing(ExtendedFacing alignment) {
        getBaseMetaTileEntity().setFrontFacing(alignment.getDirection());
    }

    @Override
    public IAlignmentLimits getAlignmentLimits() {
        return (d, r, f) -> (d.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0 && r.isNotRotated()
            && f.isNotFlipped();
    }

    private boolean checkMachine() {
        return STRUCTURE_DEFINITION.check(
            this,
            "main",
            getBaseMetaTileEntity().getWorld(),
            getExtendedFacing(),
            getBaseMetaTileEntity().getXCoord(),
            getBaseMetaTileEntity().getYCoord(),
            getBaseMetaTileEntity().getZCoord(),
            1,
            2,
            0,
            !mMachine);
    }

    @Override
    public void onMachineBlockUpdate() {
        mUpdated = true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        final int lavaX = aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1);
        final int lavaZ = aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1);
        if (aBaseMetaTileEntity.isClientSide()) {
            if (aBaseMetaTileEntity.isActive() && activitySoundLoop == null) {
                updateSound(aBaseMetaTileEntity);
            } else if (!aBaseMetaTileEntity.isActive() && activitySoundLoop != null) {
                activitySoundLoop = null;
            }

            if (aBaseMetaTileEntity.isActive()) {
                new WorldSpawnedEventBuilder.ParticleEventBuilder().setMotion(0D, 0.3D, 0D)
                    .setIdentifier(ParticleFX.LARGE_SMOKE)
                    .setPosition(
                        lavaX + XSTR_INSTANCE.nextFloat(),
                        aBaseMetaTileEntity.getOffsetY(aBaseMetaTileEntity.getBackFacing(), 1),
                        lavaZ + XSTR_INSTANCE.nextFloat())
                    .setWorld(getBaseMetaTileEntity().getWorld())
                    .run();
            }
        }
        if (aBaseMetaTileEntity.isServerSide()) {
            if (mUpdated) {
                // duct tape fix for too many updates on an overloaded server, causing the structure check to not run
                if (mUpdate < 0) mUpdate = 5;
                mUpdated = false;
            }
            if (this.mUpdate-- == 0) {
                this.mMachine = checkMachine();
            }
            if (this.mMachine) {
                if (this.mMaxProgresstime > 0) {
                    if (++this.mProgresstime >= this.mMaxProgresstime) {
                        addOutputProducts();
                        this.mOutputItems = null;
                        this.mProgresstime = 0;
                        this.mMaxProgresstime = 0;
                        GTMod.achievements.issueAchievement(
                            aBaseMetaTileEntity.getWorld()
                                .getPlayerEntityByName(aBaseMetaTileEntity.getOwnerName()),
                            "steel");
                    }
                } else if (aBaseMetaTileEntity.isAllowedToWork()) {
                    checkRecipe();
                }
            }
            if (this.mMaxProgresstime > 0 && (aTimer % 20L == 0L)) {
                Pollution
                    .addPollution(this.getBaseMetaTileEntity(), GTMod.proxy.mPollutionPrimitveBlastFurnacePerSecond);
            }

            aBaseMetaTileEntity.setActive((this.mMaxProgresstime > 0) && (this.mMachine));
            final short lavaY = aBaseMetaTileEntity.getYCoord();
            if (aBaseMetaTileEntity.isActive()) {
                if (aBaseMetaTileEntity.getAir(lavaX, lavaY, lavaZ)) {
                    aBaseMetaTileEntity.getWorld()
                        .setBlock(lavaX, lavaY, lavaZ, Blocks.lava, 1, 2);
                    this.mUpdate = 1;
                }
                if (aBaseMetaTileEntity.getAir(lavaX, lavaY + 1, lavaZ)) {
                    aBaseMetaTileEntity.getWorld()
                        .setBlock(lavaX, lavaY + 1, lavaZ, Blocks.lava, 1, 2);
                    this.mUpdate = 1;
                }
            } else {
                Block lowerLava = aBaseMetaTileEntity.getBlock(lavaX, lavaY, lavaZ);
                Block upperLava = aBaseMetaTileEntity.getBlock(lavaX, lavaY + 1, lavaZ);
                if (lowerLava == Blocks.lava) {
                    aBaseMetaTileEntity.getWorld()
                        .setBlock(lavaX, lavaY, lavaZ, Blocks.air, 0, 2);
                    this.mUpdate = 1;
                }
                if (upperLava == Blocks.lava) {
                    aBaseMetaTileEntity.getWorld()
                        .setBlock(lavaX, lavaY + 1, lavaZ, Blocks.air, 0, 2);
                    this.mUpdate = 1;
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void updateSound(IGregTechTileEntity aBaseMetaTileEntity) {
        activitySoundLoop = new GTSoundLoop(
            SoundResource.GTCEU_LOOP_FIRE.resourceLocation,
            aBaseMetaTileEntity,
            false,
            true);
        Minecraft.getMinecraft()
            .getSoundHandler()
            .playSound(activitySoundLoop);
    }

    /**
     * Draws random flames and smoke particles in front of Primitive Blast Furnace when active
     *
     * @param aBaseMetaTileEntity The entity that will handle the {@link Block#randomDisplayTick}
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void onRandomDisplayTick(IGregTechTileEntity aBaseMetaTileEntity) {
        if (aBaseMetaTileEntity.isActive()) {

            final ForgeDirection frontFacing = aBaseMetaTileEntity.getFrontFacing();

            final double oX = aBaseMetaTileEntity.getOffsetX(frontFacing, 1) + 0.5D;
            final double oY = aBaseMetaTileEntity.getOffsetY(frontFacing, 1);
            final double oZ = aBaseMetaTileEntity.getOffsetZ(frontFacing, 1) + 0.5D;
            final double offset = -0.48D;
            final double horizontal = XSTR_INSTANCE.nextFloat() * 8D / 16D - 4D / 16D;

            final double x, y, z;

            y = oY + XSTR_INSTANCE.nextFloat() * 10D / 16D + 5D / 16D;

            if (frontFacing == ForgeDirection.WEST) {
                x = oX - offset;
                z = oZ + horizontal;
            } else if (frontFacing == ForgeDirection.EAST) {
                x = oX + offset;
                z = oZ + horizontal;
            } else if (frontFacing == ForgeDirection.NORTH) {
                x = oX + horizontal;
                z = oZ - offset;
            } else // if (frontFacing == ForgeDirection.SOUTH.ordinal())
            {
                x = oX + horizontal;
                z = oZ + offset;
            }

            ParticleEventBuilder particleEventBuilder = (new ParticleEventBuilder()).setMotion(0D, 0D, 0D)
                .setPosition(x, y, z)
                .setWorld(getBaseMetaTileEntity().getWorld());
            particleEventBuilder.setIdentifier(ParticleFX.SMOKE)
                .run();
            particleEventBuilder.setIdentifier(ParticleFX.FLAME)
                .run();
        }
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.primitiveBlastRecipes;
    }

    private void addOutputProducts() {
        if (this.mOutputItems == null) {
            return;
        }
        int limit = Math.min(mOutputItems.length, OUTPUT_SLOTS);
        for (int i = 0; i < limit; i++) {
            int absi = INPUT_SLOTS + i;
            if (this.mInventory[absi] == null) {
                this.mInventory[absi] = GTUtility.copyOrNull(this.mOutputItems[i]);
            } else if (GTUtility.areStacksEqual(this.mInventory[absi], this.mOutputItems[i])) {
                this.mInventory[absi].stackSize = Math.min(
                    this.mInventory[absi].getMaxStackSize(),
                    this.mInventory[absi].stackSize + this.mOutputItems[i].stackSize);
            }
        }
    }

    private boolean spaceForOutput(ItemStack outputStack, int relativeOutputSlot) {
        int absoluteSlot = relativeOutputSlot + INPUT_SLOTS;
        if (this.mInventory[absoluteSlot] == null || outputStack == null) {
            return true;
        }
        return ((this.mInventory[absoluteSlot].stackSize + outputStack.stackSize
            <= this.mInventory[absoluteSlot].getMaxStackSize())
            && (GTUtility.areStacksEqual(this.mInventory[absoluteSlot], outputStack)));
    }

    private boolean checkRecipe() {
        if (!this.mMachine) {
            return false;
        }
        ItemStack[] inputs = new ItemStack[INPUT_SLOTS];
        System.arraycopy(mInventory, 0, inputs, 0, INPUT_SLOTS);
        GTRecipe recipe = getRecipeMap().findRecipeQuery()
            .items(inputs)
            .find();
        if (recipe == null) {
            this.mOutputItems = null;
            return false;
        }
        for (int i = 0; i < OUTPUT_SLOTS; i++) {
            if (!spaceForOutput(recipe.getOutput(i), i)) {
                this.mOutputItems = null;
                return false;
            }
        }

        if (!recipe.isRecipeInputEqual(true, null, inputs)) {
            this.mOutputItems = null;
            return false;
        }
        for (int i = 0; i < INPUT_SLOTS; i++) {
            if (mInventory[i] != null && mInventory[i].stackSize == 0) {
                mInventory[i] = null;
            }
        }

        this.mMaxProgresstime = recipe.mDuration;
        this.mOutputItems = recipe.mOutputs;
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public byte getTileEntityBaseType() {
        return HarvestTool.PickaxeLevel2.toTileEntityBaseType();
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return STRUCTURE_DEFINITION.survivalBuild(
            this,
            stackSize,
            "main",
            getBaseMetaTileEntity().getWorld(),
            getExtendedFacing(),
            getBaseMetaTileEntity().getXCoord(),
            getBaseMetaTileEntity().getYCoord(),
            getBaseMetaTileEntity().getZCoord(),
            1,
            2,
            0,
            elementBudget,
            env,
            false);
    }

    @Override
    public IStructureDefinition<MTEBrickedBlastFurnace> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        STRUCTURE_DEFINITION.buildOrHints(
            this,
            stackSize,
            "main",
            getBaseMetaTileEntity().getWorld(),
            getExtendedFacing(),
            getBaseMetaTileEntity().getXCoord(),
            getBaseMetaTileEntity().getYCoord(),
            getBaseMetaTileEntity().getZCoord(),
            1,
            2,
            0,
            hintsOnly);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        if (!this.getBaseMetaTileEntity()
            .isInvalidTileEntity()) {
            NBTTagCompound nbt = accessor.getNBTData();
            currenttip.add(
                getMachineProgressString(
                    this.getBaseMetaTileEntity()
                        .isActive(),
                    nbt.getInteger("mMaxProgressTime"),
                    nbt.getInteger("mProgressTime")));
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        if (!this.getBaseMetaTileEntity()
            .isInvalidTileEntity()) {
            tag.setInteger("mProgressTime", this.getProgresstime());
            tag.setInteger("mMaxProgressTime", this.maxProgresstime());
        }
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    protected GTGuiTheme getGuiTheme() {
        return GTGuiThemes.PRIMITIVE;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEBrickedBlastFurnaceGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    public SteamVariant getSteamVariant() {
        return SteamVariant.PRIMITIVE;
    }

    @Override
    public GUITextureSet getGUITextureSet() {
        return GUITextureSet.STEAM.apply(getSteamVariant());
    }

    @Override
    public int getTitleColor() {
        return COLOR_TITLE_WHITE.get();
    }
}

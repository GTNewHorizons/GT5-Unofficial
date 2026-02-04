package gregtech.common.tileentities.machines.multi.drone;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.AuthorSilverMoon;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.HashMultimap;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import appeng.api.util.DimensionalCoord;
import appeng.client.render.highlighter.BlockPosHighlighter;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.covers.CoverControlsWork;
import gregtech.common.covers.conditions.RedstoneCondition;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.gui.modularui.multiblock.dronecentre.DroneCentreGuiUtil;
import gregtech.common.gui.modularui.multiblock.dronecentre.MTEDroneCentreGui;
import gregtech.common.items.ItemTierDrone;
import gregtech.common.tileentities.machines.multi.drone.production.ProductionRecord;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEDroneCentre extends MTEExtendedPowerMultiBlockBase<MTEDroneCentre> implements ISurvivalConstructable {

    private static final IIconContainer ACTIVE = new Textures.BlockIcons.CustomIcon("iconsets/DRONE_CENTRE_ACTIVE");
    private static final IIconContainer FACE = new Textures.BlockIcons.CustomIcon("iconsets/DRONE_CENTRE_FACE");
    private static final IIconContainer INACTIVE = new Textures.BlockIcons.CustomIcon("iconsets/DRONE_CENTRE_INACTIVE");
    private static final int CASING_INDEX = GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings4, 2);
    private static final int CASINGS_MIN = 85;
    private int mCasingAmount = 0;
    private Vec3Impl centreCoord;
    private int droneLevel = 0;

    private int selectedTime = 10;
    private int activeGroup = 0;
    private String searchFilter = "";
    private boolean useRender = true;
    private boolean searchOriginalName;
    private boolean editMode;
    private boolean autoUpdate = true;
    private DroneCentreGuiUtil.SortMode sortMode = DroneCentreGuiUtil.SortMode.NAME;
    private String key = "";

    public List<String> group = IntStream.rangeClosed(0, 7)
        .mapToObj(String::valueOf)
        .collect(Collectors.toList());
    public ProductionRecord productionDataRecorder = new ProductionRecord();
    public List<DroneConnection> connectionList = new ArrayList<>();

    // Save centre by dimID
    private static final HashMultimap<Integer, MTEDroneCentre> droneMap = HashMultimap.create();
    // spotless off
    private static final IStructureDefinition<MTEDroneCentre> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEDroneCentre>builder()
        .addShape(
            "main",
            transpose(
                new String[][] { { "     ", "     ", "     ", "     ", "CCCCC", "CCCCC", "CCCCC", "CCCCC", "CCCCC" },
                    { "CC~CC", "C   C", "C   C", "C   C", "CAAAC", "CCCCC", "CAAAC", "C   C", "CCCCC" },
                    { "CCCCC", "CBBBC", "CBDBC", "CBBBC", "CCCCC", "CCCCC", "CCCCC", "CCCCC", "CCCCC" },
                    { "C   C", "     ", "     ", "     ", "     ", "     ", "     ", "     ", "C   C" } }))
        .addElement(
            'C',
            buildHatchAdder(MTEDroneCentre.class).atLeast(InputBus)
                .casingIndex(CASING_INDEX)
                .dot(1)
                .buildAndChain(onElementPass(MTEDroneCentre::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings4, 2))))
        .addElement('A', chainAllGlasses())
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings1, 11))
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings4, 0))
        .build();

    // spotless on
    public MTEDroneCentre(String name) {
        super(name);
    }

    public MTEDroneCentre(int ID, String Name, String NameRegional) {
        super(ID, Name, NameRegional);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEDroneCentre(super.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (getBaseMetaTileEntity().isActive()) {
                return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                    TextureFactory.builder()
                        .addIcon(ACTIVE)
                        .extFacing()
                        .build() };
            } else {
                return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                    TextureFactory.builder()
                        .addIcon(INACTIVE)
                        .extFacing()
                        .build() };
            }
        } else if (side == aFacing.getOpposite()) {
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX), TextureFactory.builder()
                .addIcon(FACE)
                .extFacing()
                .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX) };
    }

    @Override
    public IStructureDefinition<MTEDroneCentre> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean supportsPowerPanel() {
        return true;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Drone Centre")
            .addInfo(EnumChatFormatting.AQUA + "Drone #10032, cleared for takeoff!")
            .addInfo("Monitors multiblock machines in range")
            .addInfo("Replace maintenance hatch on other multi with drone downlink module")
            .addInfo("Provides maintenance, power control, monitoring, and more")
            .addSeparator()
            .addInfo("Operation range is determined by drone tier:")
            .addInfo("T1-128, T2-512, T3-4096, T4-4096(Auto)/Infinite(Key)")
            .addInfo("T4 drone allows cross-dimension connection!")
            .addInfo(EnumChatFormatting.RED + "To enable cross-dimension,")
            .addInfo(EnumChatFormatting.RED + "downlink module must have the same key with centre.")
            .addInfo(EnumChatFormatting.RED + "But it's not necessary for auto connection in range")
            .addSeparator()
            .addInfo("Place drones in input bus; only one needed to operate")
            .addInfo("Automatically upgrade based on the drone level in the input bus")
            .addInfo("There is a chance per second that the drone will crash")
            .addInfo("Chance is determined by drone tier: T1: 1/28800, T2: 1/172800, T3 & T4: 0")
            .beginStructureBlock(5, 4, 9, false)
            .addController("Front center")
            .addCasingInfoRange("Stable Titanium Machine Casing", CASINGS_MIN, 91, false)
            .addCasingInfoExactly("Heat Proof Machine Casing", 8, false)
            .addCasingInfoExactly("Robust Tungstensteel Machine Casing", 1, false)
            .addCasingInfoExactly("Any tiered glass", 6, false)
            .addInputBus("Any Titanium Casing", 1)
            .addStructureInfo("No maintenance hatch needed")
            .toolTipFinisher(AuthorSilverMoon);
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece("main", stackSize, hintsOnly, 2, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stack, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece("main", stack, 2, 1, 0, elementBudget, env, false, true);
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        // I don't think a drone can take off HORIZONTALLY!
        return (d, r, f) -> (d.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0 && r.isNotRotated()
            && !f.isVerticallyFliped();
    }

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        return checkPiece("main", 2, 1, 0) && mCasingAmount >= CASINGS_MIN;
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, aTool);
        useRender = !useRender;
        aPlayer.addChatComponentMessage(
            new ChatComponentTranslation(
                "GT5U.machines.dronecentre." + (useRender ? "enableRender" : "disableRender")));
        if (useRender) {
            createRenderBlock();
        } else {
            destroyRenderBlock();
        }
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        destroyRenderBlock();
        super.stopMachine(reason);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (aTick % 20 == 0) {
                if (switch (droneLevel) {
                    case 1 -> aBaseMetaTileEntity.getRandomNumber(28800);
                    case 2 -> aBaseMetaTileEntity.getRandomNumber(172800);
                    default -> 1;
                } == 0) {
                    droneLevel = 0;
                    startRecipeProcessing();
                    if (!tryConsumeDrone()) stopMachine(ShutDownReasonRegistry.outOfStuff("Any Drone", 1));
                    endRecipeProcessing();
                }
            }
            if (aTick % 200 == 0) {
                productionDataRecorder.update();
                // In rare cases, invalid connection may stay on the list, so we need to a check here too.
                connectionList.removeIf(connection -> !connection.isValid());
            }
        }
        if (mMaxProgresstime > 0 && mMaxProgresstime - mProgresstime == 1) destroyRenderBlock();
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        droneLevel = aNBT.getInteger("drone");
        useRender = aNBT.getBoolean("useRender");
        sortMode = DroneCentreGuiUtil.SortMode.valueOf(aNBT.getString("sort"));
        productionDataRecorder.readFromNBT(aNBT.getCompoundTag("productionData"));
        NBTTagCompound GroupNBT = aNBT.getCompoundTag("Group");
        for (int i = 0; i < 8; i++) group.set(i, GroupNBT.getString(String.valueOf(i)));
        activeGroup = aNBT.getInteger("activeGroup");
        autoUpdate = aNBT.getBoolean("dynamicUpdate");
        key = aNBT.getString("key");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("drone", droneLevel);
        aNBT.setBoolean("useRender", useRender);
        aNBT.setString("sort", sortMode.toString());
        aNBT.setTag("productionData", productionDataRecorder.writeToNBT());
        NBTTagCompound GroupNBT = new NBTTagCompound();
        for (int i = 0; i < 8; i++) GroupNBT.setString(String.valueOf(i), group.get(i));
        aNBT.setTag("Group", GroupNBT);
        aNBT.setInteger("activeGroup", activeGroup);
        aNBT.setBoolean("dynamicUpdate", autoUpdate);
        aNBT.setString("key", key);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("connectionCount", connectionList.size());
        if (droneLevel != 0) tag.setInteger("droneLevel", droneLevel);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();
        currenttip.add(
            EnumChatFormatting.AQUA + StatCollector
                .translateToLocalFormatted("GT5U.waila.drone_downlink.droneLevel", tag.getInteger("droneLevel")));
        currenttip.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.waila.drone_downlink.connectionCount",
                tag.getInteger("connectionCount")));
        super.getWailaBody(itemStack, currenttip, accessor, config);
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        if (droneLevel == 0) {
            if (!tryConsumeDrone()) return SimpleCheckRecipeResult.ofFailure("drone_noDrone");
        }
        if (droneLevel < 4) tryUpdateDrone();
        mMaxProgresstime = 200 * droneLevel;
        createRenderBlock();
        return SimpleCheckRecipeResult.ofSuccess("drone_operating");
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (aBaseMetaTileEntity.isServerSide()) {
            if (droneMap.containsValue(this)) return;
            centreCoord = new Vec3Impl(
                aBaseMetaTileEntity.getXCoord(),
                aBaseMetaTileEntity.getYCoord(),
                aBaseMetaTileEntity.getZCoord());
            droneMap.put(aBaseMetaTileEntity.getWorld().provider.dimensionId, this);
            if (droneLevel == 4) droneMap.put(Integer.MAX_VALUE, this);
        }
    }

    @Override
    public void onBlockDestroyed() {
        destroyRenderBlock();
        connectionList.clear();
        if (droneLevel != 0) spawnDroneItem();
        super.onBlockDestroyed();
    }

    private void spawnDroneItem() {
        ItemStack insideDrone = new ItemStack(switch (droneLevel) {
            case 1:
                yield ItemList.TierdDrone0.getItem();
            case 2:
                yield ItemList.TierdDrone1.getItem();
            case 3:
                yield ItemList.TierdDrone2.getItem();
            case 4:
                yield ItemList.TierdDrone3.getItem();
            default:
                yield null;
        }, 1);
        final EntityItem tItemEntity = new EntityItem(
            getBaseMetaTileEntity().getWorld(),
            getBaseMetaTileEntity().getXCoord() + XSTR_INSTANCE.nextFloat() * 0.8F + 0.1F,
            getBaseMetaTileEntity().getYCoord() + XSTR_INSTANCE.nextFloat() * 0.8F + 0.1F,
            getBaseMetaTileEntity().getZCoord() + XSTR_INSTANCE.nextFloat() * 0.8F + 0.1F,
            insideDrone);
        tItemEntity.motionX = (XSTR_INSTANCE.nextGaussian() * 0.05D);
        tItemEntity.motionY = (XSTR_INSTANCE.nextGaussian() * 0.25D);
        tItemEntity.motionZ = (XSTR_INSTANCE.nextGaussian() * 0.05D);
        getBaseMetaTileEntity().getWorld()
            .spawnEntityInWorld(tItemEntity);
    }

    @Override
    public void onRemoval() {
        droneMap.remove(getBaseMetaTileEntity().getWorld().provider.dimensionId, this);
        if (droneLevel == 4) droneMap.remove(Integer.MAX_VALUE, this);
    }

    @Override
    public void onUnload() {
        droneMap.remove(getBaseMetaTileEntity().getWorld().provider.dimensionId, this);
        if (droneLevel == 4) droneMap.remove(Integer.MAX_VALUE, this);
    }

    public int getRange() {
        return switch (droneLevel) {
            case 1 -> 128;
            case 2 -> 512;
            case 3, 4 -> 4096;
            default -> 0;
        };
    }

    public Vec3Impl getCoords() {
        return centreCoord;
    }

    private boolean tryConsumeDrone() {
        List<ItemStack> inputs = getStoredInputs();
        if (inputs.isEmpty()) return false;
        for (ItemStack item : inputs) {
            if (item != null && item.getItem() instanceof ItemTierDrone drone) {
                this.droneLevel = drone.getLevel();
                item.stackSize--;
                updateSlots();
                return true;
            }
        }
        return false;
    }

    private void tryUpdateDrone() {
        List<ItemStack> inputs = getStoredInputs();
        if (inputs.isEmpty()) return;
        for (ItemStack item : inputs) {
            if (item != null && item.getItem() instanceof ItemTierDrone drone) {
                if (drone.getLevel() <= this.droneLevel) continue;
                this.droneLevel = drone.getLevel();
                item.stackSize--;
                updateSlots();
                if (droneLevel == 4) {
                    droneMap.put(Integer.MAX_VALUE, this);
                }
                return;
            }
        }
    }

    private void createRenderBlock() {
        if (!useRender) return;
        int x = getBaseMetaTileEntity().getXCoord() + 2 * getExtendedFacing().getRelativeBackInWorld().offsetX;
        int y = getBaseMetaTileEntity().getYCoord() + 2 * getExtendedFacing().getRelativeBackInWorld().offsetY;
        int z = getBaseMetaTileEntity().getZCoord() + 2 * getExtendedFacing().getRelativeBackInWorld().offsetZ;

        World world = this.getBaseMetaTileEntity()
            .getWorld();
        if (world.isAirBlock(x, y, z)) {
            world.setBlock(x, y, z, GregTechAPI.sDroneRender);
        }
    }

    private void destroyRenderBlock() {
        int x = getBaseMetaTileEntity().getXCoord() + 2 * getExtendedFacing().getRelativeBackInWorld().offsetX;
        int y = getBaseMetaTileEntity().getYCoord() + 2 * getExtendedFacing().getRelativeBackInWorld().offsetY;
        int z = getBaseMetaTileEntity().getZCoord() + 2 * getExtendedFacing().getRelativeBackInWorld().offsetZ;

        World world = this.getBaseMetaTileEntity()
            .getWorld();
        if (world.getBlock(x, y, z)
            .equals(GregTechAPI.sDroneRender)) {
            world.setBlock(x, y, z, Blocks.air);
        }
    }

    public void turnOnAll() {
        for (DroneConnection droneConnection : connectionList) {
            if (droneConnection.isValid() && (activeGroup == 0 || droneConnection.getGroup() == activeGroup)) {
                droneConnection.getLinkedMachine()
                    .enableWorking();
            }
        }
    }

    public void turnOffAll(boolean force) {
        for (DroneConnection droneConnection : connectionList) {
            if (droneConnection.isValid() && (activeGroup == 0 || droneConnection.getGroup() == activeGroup)) {
                MTEMultiBlockBase mte = droneConnection.getLinkedMachine();
                mte.disableWorking();
                if (force && mte.getBaseMetaTileEntity() != null) {
                    for (int i = 0; i < 6; i++) {
                        if (mte.getBaseMetaTileEntity()
                            .hasCoverAtSide(ForgeDirection.getOrientation(i))
                            && mte.getBaseMetaTileEntity()
                                .getCoverAtSide(ForgeDirection.getOrientation(i)) instanceof CoverControlsWork cover) {
                            cover.setRedstoneCondition(RedstoneCondition.DISABLE);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEDroneCentreGui(this);
    }

    // Just like HIGHLIGHT_INTERFACE (and exactly from it)
    public static void highlightMachine(EntityPlayer player, ChunkCoordinates machineCoord) {
        DimensionalCoord blockPos = new DimensionalCoord(
            machineCoord.posX,
            machineCoord.posY,
            machineCoord.posZ,
            player.dimension);
        BlockPosHighlighter.highlightBlocks(player, Collections.singletonList(blockPos), null, null);
    }

    public static HashMultimap<Integer, MTEDroneCentre> getCentreMap() {
        return droneMap;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    public List<DroneConnection> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<DroneConnection> connectionList) {
        this.connectionList = connectionList;
    }

    public Optional<MTEMultiBlockBase> findConnection(UUID uuid) {
        return connectionList.stream()
            .filter(connection -> connection.uuid.equals(uuid))
            .findFirst()
            .map(DroneConnection::getLinkedMachine);
    }

    public DroneCentreGuiUtil.SortMode getSortMode() {
        return sortMode;
    }

    public void setSortMode(DroneCentreGuiUtil.SortMode sortMode) {
        this.sortMode = sortMode;
    }

    public String getSearchBarText() {
        return searchFilter;
    }

    public void setSearchBarText(String text) {
        searchFilter = text;
    }

    public boolean getSearchOriginalName() {
        return searchOriginalName;
    }

    public void setSearchOriginalName(boolean searchOriginalName) {
        this.searchOriginalName = searchOriginalName;
    }

    public boolean getEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public boolean shouldUpdate() {
        return autoUpdate;
    }

    public void setUpdate(boolean dynamicUpdate) {
        this.autoUpdate = dynamicUpdate;
    }

    public int getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(int selectedTime) {
        this.selectedTime = selectedTime;
    }

    public int getActiveGroup() {
        return activeGroup;
    }

    public void setActiveGroup(int activeGroup) {
        this.activeGroup = activeGroup;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

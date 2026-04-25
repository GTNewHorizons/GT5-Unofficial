package gregtech.common.tileentities.machines.multi.drone;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GTAuthors.AuthorSilverMoon;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;
import static gregtech.api.util.GTStructureUtility.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
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

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
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

    private static final IIconContainer ACTIVE = Textures.BlockIcons.custom("iconsets/DRONE_CENTRE_ACTIVE");
    private static final IIconContainer FACE = Textures.BlockIcons.custom("iconsets/DRONE_CENTRE_FACE");
    private static final IIconContainer INACTIVE = Textures.BlockIcons.custom("iconsets/DRONE_CENTRE_INACTIVE");
    private static final int CASINGS_MIN = 20;
    private static final int OFFSET_X = 5;
    private static final int OFFSET_Y = 3;
    private static final int OFFSET_Z = 0;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_MAIN_LEGACY = "main_legacy";

    private int casingAmount = 0;
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
    private static IStructureDefinition<MTEDroneCentre> STRUCTURE_DEFINITION = null;

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
                return new ITexture[] { Casings.SolidSteelMachineCasing.getCasingTexture(), TextureFactory.builder()
                    .addIcon(ACTIVE)
                    .extFacing()
                    .build() };
            } else {
                return new ITexture[] { Casings.SolidSteelMachineCasing.getCasingTexture(), TextureFactory.builder()
                    .addIcon(INACTIVE)
                    .extFacing()
                    .build() };
            }
        } else if (side == aFacing.getOpposite()) {
            return new ITexture[] { Casings.SolidSteelMachineCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(FACE)
                .extFacing()
                .build() };
        }
        return new ITexture[] { Casings.SolidSteelMachineCasing.getCasingTexture() };
    }

    @Override
    public IStructureDefinition<MTEDroneCentre> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEDroneCentre>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN_LEGACY,
                    transpose(
                        new String[][] {
                            { "     ", "     ", "     ", "     ", "HHHHH", "HHHHH", "HHHHH", "HHHHH", "HHHHH" },
                            { "HH~HH", "H   H", "H   H", "H   H", "HFFFH", "HHHHH", "HFFFH", "H   H", "HHHHH" },
                            { "HHHHH", "HGGGH", "HGIGH", "HGGGH", "HHHHH", "HHHHH", "HHHHH", "HHHHH", "HHHHH" },
                            { "H   H", "     ", "     ", "     ", "     ", "     ", "     ", "     ", "H   H" } }))
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    new String[][] { { "    CCC    ", "           ", "           ", "    A~A    " },
                        { "  CC   CC  ", "     D     ", "    D D    ", "  AABBBAA  " },
                        { " C       C ", "   DDEDD   ", "  D DDD D  ", " ABBBBBBBA " },
                        { " C       C ", "  DEEEEED  ", "   D   D   ", " ABBBBBBBA " },
                        { "C         C", "  DEEEEED  ", " DD     DD ", "ABBBBBBBBBA" },
                        { "C         C", " DEEEEEEED ", "  D     D  ", "ABBBBBBBBBA" },
                        { "C         C", "  DEEEEED  ", " DD     DD ", "ABBBBBBBBBA" },
                        { " C       C ", "  DEEEEED  ", "       D   ", " ABBBBBBBA " },
                        { " C       C ", "   DDEDD   ", "  D DDD D  ", " ABBBBBBBA " },
                        { "  CC   CC  ", "     D     ", "    D D    ", "  AABBBAA  " },
                        { "    CCC    ", "           ", "           ", "    AAA    " } })
                .addElement(
                    'A',
                    buildHatchAdder(MTEDroneCentre.class).atLeast(InputBus)
                        .casingIndex(Casings.SolidSteelMachineCasing.textureId)
                        .hint(1)
                        .buildAndChain(
                            onElementPass(t -> t.casingAmount++, Casings.SolidSteelMachineCasing.asElement())))
                .addElement('B', Casings.SteelPipeCasing.asElement())
                .addElement('C', ofFrame(Materials.Iron))
                .addElement('D', ofFrame(Materials.Steel))
                .addElement('E', lazy(t -> {
                    if (Mods.Chisel.isModLoaded()) {
                        Block hempcrete = GameRegistry.findBlock(Mods.Chisel.ID, "hempcrete");
                        return ofBlockAnyMeta(hempcrete, 15);
                    } else {
                        return ofSheetMetal(Materials.Steel);
                    }
                }))
                .addElement(
                    'H',
                    buildHatchAdder(MTEDroneCentre.class).atLeast(InputBus)
                        .casingIndex(Casings.StableTitaniumMachineCasing.textureId)
                        .hint(1)
                        .buildAndChain(
                            onElementPass(t -> t.casingAmount++, Casings.StableTitaniumMachineCasing.asElement())))
                .addElement('F', chainAllGlasses())
                .addElement('G', Casings.HeatProofMachineCasing.asElement())
                .addElement('I', Casings.RobustTungstenSteelMachineCasing.asElement())
                .build();
        }
        return STRUCTURE_DEFINITION;
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
            .beginStructureBlock(11, 4, 11, false)
            .addController("Front bottom center")
            .addCasingInfoMin("Solid Steel Machine Casing", CASINGS_MIN, false)
            .addCasingInfoExactly("Iron Frame Box", 28, false)
            .addCasingInfoExactly("Steel Frame Box", 32, false)
            .addCasingInfoExactly("Steel Pipe Casing", 61, false)
            .addCasingInfoExactly("Hempcrete", 29, false)
            .addInputBus("Any Solid Steel Machine Casing", 1)
            .addStructureInfo("No maintenance hatch needed")
            .addStructureAuthors(EnumChatFormatting.GOLD + "omegacubed")
            .toolTipFinisher(AuthorSilverMoon);
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stack, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stack,
            OFFSET_X,
            OFFSET_Y,
            OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        // I don't think a drone can take off HORIZONTALLY!
        return (d, r, f) -> (d.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0 && r.isNotRotated()
            && !f.isVerticallyFliped();
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmount = 0;
        if (checkPiece(STRUCTURE_PIECE_MAIN_LEGACY, 2, 1, 0)) {
            return true;
        }

        clearHatches();
        casingAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z) && casingAmount >= CASINGS_MIN
            && !mInputBusses.isEmpty();
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

    public int getDroneLevel() {
        return droneLevel;
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

    public static HashMultimap<Integer, MTEDroneCentre> getCentreMap() {
        return droneMap;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
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

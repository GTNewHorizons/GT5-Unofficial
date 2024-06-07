package gregtech.common.tileentities.machines.multi.drone;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_Values.AuthorSilverMoon;
import static gregtech.api.multitileentity.multiblock.casing.Glasses.chainAllGlasses;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
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
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.MainAxisAlignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedRow;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.Scrollable;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

import appeng.api.util.DimensionalCoord;
import appeng.api.util.WorldCoord;
import appeng.client.render.BlockPosHighlighter;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.widget.ShutDownReasonSyncer;
import gregtech.common.items.GT_TierDrone;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GT_MetaTileEntity_DroneCentre extends
    GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_DroneCentre> implements ISurvivalConstructable {

    private static final IIconContainer ACTIVE = new Textures.BlockIcons.CustomIcon("iconsets/DRONE_CENTRE_ACTIVE");
    private static final IIconContainer FACE = new Textures.BlockIcons.CustomIcon("iconsets/DRONE_CENTRE_FACE");
    private static final IIconContainer INACTIVE = new Textures.BlockIcons.CustomIcon("iconsets/DRONE_CENTRE_INACTIVE");
    private final int MACHINE_LIST_WINDOW_ID = 10;
    private final int CUSTOM_NAME_WINDOW_ID = 11;
    private static final int CASINGS_MIN = 85;
    private int mCasingAmount = 0;
    private Vec3Impl centreCoord;
    private int droneLevel = 0;
    private int buttonID;
    private String searchFilter = "";
    private boolean useRender = true;
    private boolean showLocalizedName = false;
    private String sort = "distance";
    private List<DroneConnection> connectionList = new ArrayList<>();
    public HashMap<String, String> tempNameList = new HashMap<>();
    // Save centre by dimID
    private static final HashMultimap<Integer, GT_MetaTileEntity_DroneCentre> droneMap = HashMultimap.create();
    // spotless off
    private static final IStructureDefinition<GT_MetaTileEntity_DroneCentre> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_DroneCentre>builder()
        .addShape(
            "main",
            transpose(
                new String[][] { { "     ", "     ", "     ", "     ", "CCCCC", "CCCCC", "CCCCC", "CCCCC", "CCCCC" },
                    { "CC~CC", "C   C", "C   C", "C   C", "CAAAC", "CCCCC", "CAAAC", "C   C", "CCCCC" },
                    { "CCCCC", "CBBBC", "CBDBC", "CBBBC", "CCCCC", "CCCCC", "CCCCC", "CCCCC", "CCCCC" },
                    { "C   C", "     ", "     ", "     ", "     ", "     ", "     ", "     ", "C   C" } }))
        .addElement(
            'C',
            buildHatchAdder(GT_MetaTileEntity_DroneCentre.class).atLeast(InputBus)
                .casingIndex(59)
                .dot(1)
                .buildAndChain(
                    onElementPass(
                        GT_MetaTileEntity_DroneCentre::onCasingAdded,
                        ofBlock(GregTech_API.sBlockCasings4, 2))))
        .addElement('A', chainAllGlasses())
        .addElement('B', ofBlock(GregTech_API.sBlockCasings1, 11))
        .addElement('D', ofBlock(GregTech_API.sBlockCasings4, 0))
        .build();

    // spotless on
    public GT_MetaTileEntity_DroneCentre(String name) {
        super(name);
    }

    public GT_MetaTileEntity_DroneCentre(int ID, String Name, String NameRegional) {
        super(ID, Name, NameRegional);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_DroneCentre(super.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (getBaseMetaTileEntity().isActive()) {
                return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(59), TextureFactory.builder()
                    .addIcon(ACTIVE)
                    .extFacing()
                    .build() };
            } else {
                return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(59), TextureFactory.builder()
                    .addIcon(INACTIVE)
                    .extFacing()
                    .build() };
            }
        } else if (side == aFacing.getOpposite()) {
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(59), TextureFactory.builder()
                .addIcon(FACE)
                .extFacing()
                .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(59) };
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_DroneCentre> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Drone Centre")
            .addInfo("Drone Center Controller")
            .addInfo(EnumChatFormatting.AQUA + "Drone #10032, cleared for takeoff!")
            .addInfo("Monitors multiblock machines in range.")
            .addInfo("Replace maintenance hatch on other multi with drone downlink module.")
            .addInfo("Provides maintenance, power control, monitoring and etc.")
            .addInfo("Range is determined by drone tier: T1-128, T2-512, T3-4096")
            .addInfo("Place drones in input bus; only one needed to operate.")
            .addInfo("Automatically upgrade based on the drone level in the input bus.")
            .addInfo("There is a chance per second that the drone will crash.")
            .addInfo("Chance is determined by drone tier: T1-1/28800, T2-1/172800, T3-0")
            .addInfo("If machine is too far, remote control would not available")
            .addInfo(AuthorSilverMoon)
            .addSeparator()
            .beginStructureBlock(5, 4, 9, false)
            .addController("Front center")
            .addCasingInfoRange("Stable Titanium Machine Casing", CASINGS_MIN, 91, false)
            .addCasingInfoExactly("Heat Proof Machine Casing", 8, false)
            .addCasingInfoExactly("Robust Tungstensteel Machine Casing", 1, false)
            .addCasingInfoExactly("Any tiered glass", 6, false)
            .addInputBus("Any Titanium Casing", 1)
            .addStructureInfo("No maintenance hatch needed")
            .addSeparator()
            .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece("main", stackSize, hintsOnly, 2, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stack, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece("main", stack, 2, 1, 0, elementBudget, env, false, true);
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        // I don't think a drone can take off HORIZONTALLY!
        return (d, r, f) -> (d.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0 && r.isNotRotated()
            && !f.isVerticallyFliped();
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
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
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ);
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
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return true;
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
                    case 1 -> getBaseMetaTileEntity().getRandomNumber(28800);
                    case 2 -> getBaseMetaTileEntity().getRandomNumber(172800);
                    default -> 1;
                } == 0) {
                    droneLevel = 0;
                    startRecipeProcessing();
                    if (!tryConsumeDrone()) stopMachine(ShutDownReasonRegistry.outOfStuff("Any Drone", 1));
                    endRecipeProcessing();
                }
            }
            // Clean invalid connections every 4 seconds
            if (aTick % 80 == 0) connectionList.removeIf(v -> !v.isValid());
        }
        if (mMaxProgresstime > 0 && mMaxProgresstime - mProgresstime == 1) destroyRenderBlock();
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        droneLevel = aNBT.getInteger("drone");
        useRender = aNBT.getBoolean("useRender");
        sort = aNBT.getString("sort");
        NBTTagCompound nameList = aNBT.getCompoundTag("conList");
        for (String s : nameList.func_150296_c()) {
            tempNameList.put(s, nameList.getString(s));
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("drone", droneLevel);
        aNBT.setBoolean("useRender", useRender);
        aNBT.setString("sort", sort);
        NBTTagCompound conList = new NBTTagCompound();
        for (DroneConnection con : connectionList) {
            if (!con.customName.equals(con.machine.getLocalName()))
                conList.setString(con.machineCoord.toString(), con.customName);
        }
        aNBT.setTag("conList", conList);
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
            EnumChatFormatting.AQUA + StatCollector.translateToLocal("GT5U.waila.drone_downlink.droneLevel")
                + tag.getInteger("droneLevel"));
        currenttip.add(
            StatCollector.translateToLocal("GT5U.waila.drone_downlink.connectionCount")
                + tag.getInteger("connectionCount"));
        super.getWailaBody(itemStack, currenttip, accessor, config);
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        if (droneLevel == 0) {
            if (!tryConsumeDrone()) return SimpleCheckRecipeResult.ofFailure("drone_noDrone");
        }
        if (droneLevel == 1 || droneLevel == 2) tryUpdateDrone();
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
                getBaseMetaTileEntity().getXCoord(),
                getBaseMetaTileEntity().getYCoord(),
                getBaseMetaTileEntity().getZCoord());
            droneMap.put(getBaseMetaTileEntity().getWorld().provider.dimensionId, this);
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
    }

    public List<DroneConnection> getConnectionList() {
        return connectionList;
    }

    public int getRange() {
        return switch (droneLevel) {
            case 1 -> 128;
            case 2 -> 512;
            case 3 -> 4096;
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
            if (item != null && item.getItem() instanceof GT_TierDrone drone) {
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
            if (item != null && item.getItem() instanceof GT_TierDrone drone) {
                if (drone.getLevel() <= this.droneLevel) continue;
                this.droneLevel = drone.getLevel();
                item.stackSize--;
                updateSlots();
                return;
            }
        }
    }

    private void createRenderBlock() {
        if (!useRender) return;
        int x = getBaseMetaTileEntity().getXCoord();
        int y = getBaseMetaTileEntity().getYCoord();
        int z = getBaseMetaTileEntity().getZCoord();

        double xOffset = 2 * getExtendedFacing().getRelativeBackInWorld().offsetX;
        double zOffset = 2 * getExtendedFacing().getRelativeBackInWorld().offsetZ;
        double yOffset = 2 * getExtendedFacing().getRelativeBackInWorld().offsetY;

        this.getBaseMetaTileEntity()
            .getWorld()
            .setBlock((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset), Blocks.air);
        this.getBaseMetaTileEntity()
            .getWorld()
            .setBlock((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset), GregTech_API.sDroneRender);
    }

    private void destroyRenderBlock() {
        int x = getBaseMetaTileEntity().getXCoord();
        int y = getBaseMetaTileEntity().getYCoord();
        int z = getBaseMetaTileEntity().getZCoord();

        double xOffset = 2 * getExtendedFacing().getRelativeBackInWorld().offsetX;
        double zOffset = 2 * getExtendedFacing().getRelativeBackInWorld().offsetZ;
        double yOffset = 2 * getExtendedFacing().getRelativeBackInWorld().offsetY;

        this.getBaseMetaTileEntity()
            .getWorld()
            .setBlock((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset), Blocks.air);
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        buildContext.addSyncedWindow(MACHINE_LIST_WINDOW_ID, this::createMachineListWindow);
        buildContext.addSyncedWindow(CUSTOM_NAME_WINDOW_ID, this::createCustomNameWindow);
        builder.widget(// Machine List
            new ButtonWidget().setOnClick(
                (clickData, widget) -> {
                    if (!widget.isClient()) widget.getContext()
                        .openSyncedWindow(MACHINE_LIST_WINDOW_ID);
                })
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> UI = new ArrayList<>();
                    UI.add(GT_UITextures.BUTTON_STANDARD);
                    UI.add(GT_UITextures.OVERLAY_BUTTON_WHITELIST);
                    return UI.toArray(new IDrawable[0]);
                })
                .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.drone_open_list"))
                .setPos(94, 91)
                .setEnabled(var -> getBaseMetaTileEntity().isActive()))
            .widget(// Turn on ALL machines
                new ButtonWidget().setOnClick((clickData, widget) -> {
                    if (!widget.isClient()) {
                        if (!getBaseMetaTileEntity().isActive()) {
                            widget.getContext()
                                .getPlayer()
                                .addChatComponentMessage(
                                    new ChatComponentTranslation("GT5U.machines.dronecentre.shutdown"));
                            return;
                        }
                        for (DroneConnection mte : connectionList) {
                            mte.machine.getBaseMetaTileEntity()
                                .enableWorking();
                        }
                        widget.getContext()
                            .getPlayer()
                            .addChatComponentMessage(new ChatComponentTranslation("GT5U.machines.dronecentre.turnon"));
                        widget.getContext()
                            .getPlayer()
                            .closeScreen();
                    }
                })
                    .setSize(16, 16)
                    .setBackground(() -> {
                        List<UITexture> UI = new ArrayList<>();
                        UI.add(GT_UITextures.BUTTON_STANDARD);
                        UI.add(GT_UITextures.OVERLAY_BUTTON_POWER_SWITCH_ON);
                        return UI.toArray(new IDrawable[0]);
                    })
                    .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.drone_poweron_all"))
                    .setPos(146, 91)
                    .setEnabled(var -> getBaseMetaTileEntity().isActive()))
            .widget(// Turn off ALL machines
                new ButtonWidget().setOnClick((clickData, widget) -> {
                    if (!widget.isClient()) {
                        if (!getBaseMetaTileEntity().isActive()) {
                            widget.getContext()
                                .getPlayer()
                                .addChatComponentMessage(
                                    new ChatComponentTranslation("GT5U.machines.dronecentre.shutdown"));
                            return;
                        }
                        for (DroneConnection mte : connectionList) {
                            mte.machine.getBaseMetaTileEntity()
                                .disableWorking();
                        }
                        widget.getContext()
                            .getPlayer()
                            .addChatComponentMessage(new ChatComponentTranslation("GT5U.machines.dronecentre.turnoff"));
                        widget.getContext()
                            .getPlayer()
                            .closeScreen();
                    }
                })
                    .setSize(16, 16)
                    .setBackground(() -> {
                        List<UITexture> UI = new ArrayList<>();
                        UI.add(GT_UITextures.BUTTON_STANDARD);
                        UI.add(GT_UITextures.OVERLAY_BUTTON_POWER_SWITCH_OFF);
                        return UI.toArray(new IDrawable[0]);
                    })
                    .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.drone_poweroff_all"))
                    .setPos(120, 91)
                    .setEnabled(var -> getBaseMetaTileEntity().isActive()))
            .widget(new FakeSyncWidget.ListSyncer<>(() -> connectionList, var1 -> {
                connectionList.clear();
                connectionList.addAll(var1);
            }, (buffer, j) -> {
                try {
                    buffer.writeNBTTagCompoundToBuffer(j.transConnectionToNBT());
                } catch (IOException e) {
                    GT_Log.err.println(e.getCause());
                }
            }, buffer -> {
                try {
                    return new DroneConnection(buffer.readNBTTagCompoundFromBuffer());
                } catch (IOException e) {
                    GT_Log.err.println(e.getCause());
                }
                return null;
            }));
    }

    protected ModularWindow createMachineListWindow(final EntityPlayer player) {
        int heightCoff = getBaseMetaTileEntity().isServerSide() ? 0
            : Minecraft.getMinecraft().currentScreen.height - 40;
        ModularWindow.Builder builder = ModularWindow.builder(260, heightCoff);
        builder.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.widget(
            ButtonWidget.closeWindowButton(true)
                .setPos(245, 3));
        builder.widget(
            new TextWidget(EnumChatFormatting.BOLD + StatCollector.translateToLocal("GT5U.gui.text.drone_title"))
                .setScale(2)
                .setTextAlignment(Alignment.Center)
                .setPos(0, 10)
                .setSize(260, 8));
        // SearchBar
        builder.widget(new TextFieldWidget() {

            @Override
            public void onRemoveFocus() {
                super.onRemoveFocus();
                syncToServer(2, buffer -> {});
            }

            // Refresh
            @Override
            public void readOnServer(int id, PacketBuffer buf) {
                switch (id) {
                    case 1 -> super.readOnServer(id, buf);
                    case 2 -> {
                        getContext().closeWindow(MACHINE_LIST_WINDOW_ID);
                        getContext().openSyncedWindow(MACHINE_LIST_WINDOW_ID);
                    }
                }
            }
        }.setGetter(() -> searchFilter)
            .setSetter(var -> searchFilter = var)
            .setTextAlignment(Alignment.CenterLeft)
            .setTextColor(Color.WHITE.dark(1))
            .setFocusOnGuiOpen(false)
            .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD_LIGHT_GRAY.withOffset(-1, -1, 2, 2))
            .addTooltip(StatCollector.translateToLocal("GT5U.gui.text.drone_search"))
            .setPos(50, 30)
            .setSize(200, 16))
            // Sort button
            .widget(new ButtonWidget() {

                @Override
                public ClickResult onClick(int buttonId, boolean doubleClick) {
                    ClickResult result = super.onClick(buttonId, doubleClick);
                    syncToServer(2, buffer -> {});
                    return result;
                }

                @Override
                public void readOnServer(int id, PacketBuffer buf) {
                    switch (id) {
                        case 1 -> super.readOnServer(id, buf);
                        case 2 -> {
                            getContext().closeWindow(MACHINE_LIST_WINDOW_ID);
                            getContext().openSyncedWindow(MACHINE_LIST_WINDOW_ID);
                        }
                    }
                }
            }.setOnClick((clickData, widget) -> {
                switch (sort) {
                    case "name" -> sort = "distance";
                    case "distance" -> sort = "error";
                    case "error" -> sort = "name";
                }
            })
                .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.drone_" + sort))
                .setBackground(
                    () -> new IDrawable[] { GT_UITextures.BUTTON_STANDARD, GT_UITextures.OVERLAY_BUTTON_SORTING_MODE })
                .setPos(10, 30)
                .setSize(16, 16))
            .widget(new FakeSyncWidget.StringSyncer(() -> sort, var1 -> sort = var1))
            // Localized Button
            .widget(new ButtonWidget() {

                @Override
                public ClickResult onClick(int buttonId, boolean doubleClick) {
                    ClickResult result = super.onClick(buttonId, doubleClick);
                    syncToServer(2, buffer -> {});
                    return result;
                }

                @Override
                public void readOnServer(int id, PacketBuffer buf) {
                    switch (id) {
                        case 1 -> super.readOnServer(id, buf);
                        case 2 -> {
                            getContext().closeWindow(MACHINE_LIST_WINDOW_ID);
                            getContext().openSyncedWindow(MACHINE_LIST_WINDOW_ID);
                        }
                    }
                }
            }.setOnClick((clickData, widget) -> showLocalizedName = !showLocalizedName)
                .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.drone_showLocalName"))
                .setBackground(
                    () -> new IDrawable[] {
                        showLocalizedName ? GT_UITextures.BUTTON_STANDARD_PRESSED : GT_UITextures.BUTTON_STANDARD,
                        GT_UITextures.OVERLAY_BUTTON_CYCLIC })
                .setPos(30, 30)
                .setSize(16, 16));
        // Sort first
        switch (sort) {
            case "name" -> connectionList = connectionList.stream()
                .sorted(
                    (o1, o2) -> Collator.getInstance(Locale.UK)
                        .compare(o1.getCustomName(false), o2.getCustomName(false)))
                .collect(Collectors.toList());
            case "distance" -> connectionList = connectionList.stream()
                .sorted(Comparator.comparing(DroneConnection::getDistanceSquared))
                .collect(Collectors.toList());
            case "error" -> connectionList = connectionList.stream()
                .sorted(
                    Comparator.comparing(DroneConnection::isMachineShutdown)
                        .reversed()
                        .thenComparing(DroneConnection::getDistanceSquared))
                .collect(Collectors.toList());
        }

        Scrollable MachineContainer = new Scrollable().setVerticalScroll();
        int posY = 0;
        for (int i = 0; i < connectionList.size(); i++) {
            DroneConnection connection = connectionList.get(i);
            if (!connection.customName.toLowerCase()
                .contains(searchFilter.toLowerCase())) continue;
            ItemStackHandler drawitem = new ItemStackHandler(1);
            drawitem.setStackInSlot(0, connection.machineItem);
            DynamicPositionedRow row = new DynamicPositionedRow().setSynced(false);
            GT_MetaTileEntity_MultiBlockBase coreMachine = connection.machine;
            int finalI = i;
            row.widget(
                SlotWidget.phantom(drawitem, 0)
                    .disableInteraction()
                    .setPos(0, 0))
                .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                    buttonID = finalI;
                    if (!widget.isClient()) widget.getContext()
                        .openSyncedWindow(CUSTOM_NAME_WINDOW_ID);
                })
                    .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.drone_setname"))
                    .setBackground(
                        () -> new IDrawable[] { GT_UITextures.BUTTON_STANDARD, GT_UITextures.OVERLAY_BUTTON_PRINT })
                    .setSize(16, 16));
            // Client can't handle unloaded machines
            row.widget(
                new ButtonWidget().setOnClick(
                    (clickData, widget) -> Optional.ofNullable(coreMachine)
                        .ifPresent(machine -> {
                            if (!getBaseMetaTileEntity().isActive()) {
                                player.addChatComponentMessage(
                                    new ChatComponentTranslation("GT5U.machines.dronecentre.shutdown"));
                                return;
                            }
                            if (machine.isAllowedToWork()) {
                                machine.disableWorking();
                            } else {
                                machine.enableWorking();
                            }
                        }))
                    .setPlayClickSoundResource(
                        () -> Optional.ofNullable(coreMachine)
                            .filter(GT_MetaTileEntity_MultiBlockBase::isAllowedToWork)
                            .map(var -> SoundResource.GUI_BUTTON_UP.resourceLocation)
                            .orElse(SoundResource.GUI_BUTTON_DOWN.resourceLocation))
                    .setBackground(
                        () -> Optional.ofNullable(coreMachine)
                            .map(
                                machine -> machine.isAllowedToWork()
                                    ? new IDrawable[] { GT_UITextures.BUTTON_STANDARD_PRESSED,
                                        GT_UITextures.OVERLAY_BUTTON_POWER_SWITCH_ON }
                                    : new IDrawable[] { GT_UITextures.BUTTON_STANDARD,
                                        GT_UITextures.OVERLAY_BUTTON_POWER_SWITCH_OFF })
                            .orElse(new IDrawable[] { GT_UITextures.OVERLAY_BUTTON_CROSS }))
                    .attachSyncer(
                        new FakeSyncWidget.BooleanSyncer(
                            () -> Optional.ofNullable(coreMachine)
                                .map(GT_MetaTileEntity_MultiBlockBase::isAllowedToWork)
                                .orElse(false),
                            var -> Optional.ofNullable(coreMachine)
                                .ifPresent(machine -> {
                                    if (var) machine.enableWorking();
                                    else machine.disableWorking();
                                })),
                        builder)
                    .addTooltip(
                        coreMachine != null ? StatCollector.translateToLocal("GT5U.gui.button.power_switch")
                            : StatCollector.translateToLocal("GT5U.gui.button.drone_outofrange"))
                    .setSize(16, 16))
                .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                    if (widget.isClient()) {
                        highlightMachine(player, connection.machineCoord);
                        player.closeScreen();
                    }
                })
                    .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.drone_highlight"))
                    .setBackground(
                        new IDrawable[] { GT_UITextures.BUTTON_STANDARD, GT_UITextures.OVERLAY_BUTTON_INVERT_REDSTONE })
                    .setSize(16, 16));
            // Show the reason why the machine shutdown
            row.widget(
                new DrawableWidget().dynamicTooltip(
                    () -> Collections.singletonList(
                        Optional.ofNullable(coreMachine)
                            .map(
                                machine -> machine.getBaseMetaTileEntity()
                                    .getLastShutDownReason()
                                    .getDisplayString())
                            .orElse("")))
                    .setBackground(GT_UITextures.PICTURE_STALLED_ELECTRICITY)
                    .setSize(16, 16)
                    .setEnabled(
                        var -> coreMachine != null && coreMachine.shouldDisplayShutDownReason()
                            && !coreMachine.getBaseMetaTileEntity()
                                .isActive()
                            && GT_Utility.isStringValid(
                                coreMachine.getBaseMetaTileEntity()
                                    .getLastShutDownReason()
                                    .getDisplayString())
                            && coreMachine.getBaseMetaTileEntity()
                                .wasShutdown())
                    .attachSyncer(
                        new ShutDownReasonSyncer(
                            () -> Optional.ofNullable(coreMachine)
                                .map(
                                    var -> coreMachine.getBaseMetaTileEntity()
                                        .getLastShutDownReason())
                                .orElse(ShutDownReasonRegistry.NONE),
                            reason -> Optional.ofNullable(coreMachine)
                                .ifPresent(
                                    machine -> coreMachine.getBaseMetaTileEntity()
                                        .setShutDownReason(reason))),
                        builder)
                    .attachSyncer(
                        new FakeSyncWidget.BooleanSyncer(
                            () -> Optional.ofNullable(coreMachine)
                                .map(
                                    var -> coreMachine.getBaseMetaTileEntity()
                                        .wasShutdown())
                                .orElse(false),
                            wasShutDown -> Optional.ofNullable(coreMachine)
                                .ifPresent(
                                    machine -> coreMachine.getBaseMetaTileEntity()
                                        .setShutdownStatus(wasShutDown))),
                        builder));
            row.widget(
                new TextWidget(
                    connectionList.get(i)
                        .getCustomName(showLocalizedName)).setTextAlignment(Alignment.CenterLeft)
                            .setPos(0, 4));
            MachineContainer.widget(
                row.setAlignment(MainAxisAlignment.SPACE_BETWEEN)
                    .setSpace(4)
                    .setPos(0, posY));
            posY += 20;
        }
        return builder.widget(
            MachineContainer.setPos(10, 50)
                .setSize(240, heightCoff - 60))
            .setDraggable(false)
            .build();
    }

    protected ModularWindow createCustomNameWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(150, 40);
        builder.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        return builder.widget(
            ButtonWidget.closeWindowButton(true)
                .setPos(135, 3))
            .widget(
                new TextWidget(StatCollector.translateToLocal("GT5U.gui.text.drone_custom_name"))
                    .setTextAlignment(Alignment.Center)
                    .setPos(0, 5)
                    .setSize(150, 8))
            .widget(new TextFieldWidget() {

                @Override
                public void onDestroy() {
                    if (isClient()) return;
                    getContext().closeWindow(MACHINE_LIST_WINDOW_ID);
                    getContext().openSyncedWindow(MACHINE_LIST_WINDOW_ID);
                }
            }.setGetter(
                () -> connectionList.get(buttonID)
                    .getCustomName(false))
                .setSetter(
                    var -> connectionList.get(buttonID)
                        .setCustomName(var))
                .setTextAlignment(Alignment.CenterLeft)
                .setTextColor(Color.WHITE.dark(1))
                .setFocusOnGuiOpen(true)
                .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD_LIGHT_GRAY.withOffset(-1, -1, 2, 2))
                .setPos(10, 16)
                .setSize(130, 16))
            .build();
    }

    // Just like HIGHLIGHT_INTERFACE (and exactly from it)
    private void highlightMachine(EntityPlayer player, ChunkCoordinates machineCoord) {
        DimensionalCoord blockPos = new DimensionalCoord(
            machineCoord.posX,
            machineCoord.posY,
            machineCoord.posZ,
            player.dimension);
        WorldCoord blockPos2 = new WorldCoord((int) player.posX, (int) player.posY, (int) player.posZ);
        BlockPosHighlighter.highlightBlock(
            blockPos,
            System.currentTimeMillis() + 500 * WorldCoord.getTaxicabDistance(blockPos, blockPos2));
    }

    public static HashMultimap<Integer, GT_MetaTileEntity_DroneCentre> getCentreMap() {
        return droneMap;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}

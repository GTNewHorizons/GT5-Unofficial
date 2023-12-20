package gregtech.common.tileentities.machines.multi.drone;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_Values.AuthorSilverMoon;
import static gregtech.api.multitileentity.multiblock.casing.Glasses.chainAllGlasses;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
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
import gregtech.common.items.GT_TierDrone;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GT_MetaTileEntity_DroneCentre extends
    GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_DroneCentre> implements ISurvivalConstructable {

    private static final IIconContainer Active = new Textures.BlockIcons.CustomIcon("iconsets/droneCentre");
    private static final IIconContainer Inactive = new Textures.BlockIcons.CustomIcon("iconsets/droneCentre");
    public Vec3Impl vec3;
    public int range;
    public int droneLevel = 0;
    public int buttonID;
    public List<droneConnection> connectionList = new ArrayList<>();
    // Save centre by dimID
    private static final HashMultimap<Integer, GT_MetaTileEntity_DroneCentre> droneMap = HashMultimap.create();
    // spotless off
    private static final IStructureDefinition<GT_MetaTileEntity_DroneCentre> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_DroneCentre>builder()
        .addShape(
            "main",
            transpose(
                new String[][] { { "     ", "     ", "     ", "     ", "CCCCC", "CCCCC", "CCCCC", "CCCCC", "CCCCC" },
                    { "CE~EC", "C   C", "C   C", "C   C", "CAAAC", "CCCCC", "CAAAC", "C   C", "CCCCC" },
                    { "CEEEC", "CBBBC", "CBDBC", "CBBBC", "CCCCC", "CCCCC", "CCCCC", "CCCCC", "CCCCC" },
                    { "C   C", "     ", "     ", "     ", "     ", "     ", "     ", "     ", "C   C" },
                    { "C   C", "     ", "     ", "     ", "     ", "     ", "     ", "     ", "C   C" },
                    { "C   C", "     ", "     ", "     ", "     ", "     ", "     ", "     ", "C   C" } }))
        .addElement(
            'E',
            buildHatchAdder(GT_MetaTileEntity_DroneCentre.class).atLeast(InputBus)
                .casingIndex(59)
                .dot(1)
                .buildAndChain(ofBlock(GregTech_API.sBlockCasings4, 2)))
        .addElement('C', ofBlock(GregTech_API.sBlockCasings4, 2))
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
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(59), TextureFactory.builder()
                .addIcon(Active)
                .extFacing()
                .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(59), TextureFactory.builder()
                .addIcon(Inactive)
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
            .addInfo("Range is determined by drone tier: T1-64, T2-256, T3-1024")
            .addInfo("Place drones in input bus; only one needed to operate.")
            .addInfo("Automatically upgrade based on the drone level in the input bus.")
            .addInfo("There is a 0.028%*(3-N) chance per second that the drone will crash.")
            .addInfo("If there is no backup drone, it will shut down!")
            .addInfo(
                "Once the maintenance center shuts down, all multi-block machines within the range will malfunction.")
            .addInfo(AuthorSilverMoon)
            .addSeparator()
            .beginStructureBlock(5, 6, 9, false)
            .addStructureInfo("Do not need maintenance hatch")
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

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece("main", 2, 1, 0);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
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
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            fixAll();
            if (aTick % 200 == 0 && (droneLevel == 1 || droneLevel == 2)
                && getBaseMetaTileEntity().getRandomNumber(360 * (3 - droneLevel)) == 0) {
                droneLevel = 0;
                if (!tryConsumeDrone()) criticalStopMachine();
            }
            // Clean list every 4 second
            if (aTick % 80 == 0) connectionList.removeIf(v -> !v.isValid());
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        droneLevel = aNBT.getInteger("drone");
        connectionList.clear();
        for (String id : aNBT.getCompoundTag("conList")
            .func_150296_c()) {
            connectionList.add(
                new droneConnection(
                    aNBT.getCompoundTag("conList")
                        .getCompoundTag(id)));
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("drone", droneLevel);
        NBTTagCompound conList = new NBTTagCompound();
        for (droneConnection con : connectionList) {
            conList.setTag(String.valueOf(con.id), con.transConnectionToNBT());
        }
        aNBT.setTag("conList", conList);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        if (droneLevel != 0) tag.setInteger("droneLevel", droneLevel);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();
        currenttip.add(EnumChatFormatting.AQUA + "Drone Level: " + tag.getInteger("droneLevel"));
        super.getWailaBody(itemStack, currenttip, accessor, config);
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        if (droneLevel == 0) {
            if (!tryConsumeDrone()) return SimpleCheckRecipeResult.ofFailure("noDrone");
        }
        if (droneLevel == 1 || droneLevel == 2) tryUpdateDrone();
        mMaxProgresstime = 200 * droneLevel;
        createRenderBlock();
        return SimpleCheckRecipeResult.ofSuccess("operating");
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (aBaseMetaTileEntity.isServerSide()) {
            if (droneMap.containsValue(this)) return;
            vec3 = new Vec3Impl(
                getBaseMetaTileEntity().getXCoord(),
                getBaseMetaTileEntity().getYCoord(),
                getBaseMetaTileEntity().getZCoord());
            droneMap.put(getBaseMetaTileEntity().getWorld().provider.dimensionId, this);
        }
    }

    @Override
    public void onRemoval() {
        droneMap.remove(getBaseMetaTileEntity().getWorld().provider.dimensionId, this);
    }

    public int getRange() {
        return switch (droneLevel) {
            case 1 -> 64;
            case 2 -> 256;
            case 3 -> 1024;
            default -> 0;
        };
    }

    public boolean tryConsumeDrone() {
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

    public void tryUpdateDrone() {
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

    public void createRenderBlock() {
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

    public void fixAll() {
        this.mWrench = this.mScrewdriver = this.mSoftHammer = this.mHardHammer = this.mCrowbar = this.mSolderingTool = true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        buildContext.addSyncedWindow(10, this::createMachineListWindow);
        buildContext.addSyncedWindow(11, this::createCustomNameWindow);
        builder.widget(// Machine List
            new ButtonWidget().setOnClick(
                (clickData, widget) -> {
                    if (!widget.isClient()) widget.getContext()
                        .openSyncedWindow(10);
                })
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> UI = new ArrayList<>();
                    UI.add(GT_UITextures.BUTTON_STANDARD);
                    UI.add(GT_UITextures.OVERLAY_BUTTON_WHITELIST);
                    return UI.toArray(new IDrawable[0]);
                })
                .addTooltip("Open Machine List")
                .setPos(94, 91))
            .widget(// Turn on ALL machines
                new ButtonWidget().setOnClick((clickData, widget) -> {
                    if (!widget.isClient()) {
                        for (droneConnection mte : connectionList) {
                            mte.machine.getBaseMetaTileEntity()
                                .enableWorking();
                        }
                    }
                })
                    .setSize(16, 16)
                    .setBackground(() -> {
                        List<UITexture> UI = new ArrayList<>();
                        UI.add(GT_UITextures.BUTTON_STANDARD);
                        UI.add(GT_UITextures.OVERLAY_BUTTON_POWER_SWITCH_ON);
                        return UI.toArray(new IDrawable[0]);
                    })
                    .addTooltip("Turn ON all machines connected to centre")
                    .setPos(146, 91))
            .widget(// Turn off ALL machines
                new ButtonWidget().setOnClick((clickData, widget) -> {
                    if (!widget.isClient()) {
                        for (droneConnection mte : connectionList) {
                            mte.machine.getBaseMetaTileEntity()
                                .disableWorking();
                        }
                    }
                })
                    .setSize(16, 16)
                    .setBackground(() -> {
                        List<UITexture> UI = new ArrayList<>();
                        UI.add(GT_UITextures.BUTTON_STANDARD);
                        UI.add(GT_UITextures.OVERLAY_BUTTON_POWER_SWITCH_OFF);
                        return UI.toArray(new IDrawable[0]);
                    })
                    .addTooltip("Turn OFF all machines connected to centre")
                    .setPos(120, 91))
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
                    return new droneConnection(buffer.readNBTTagCompoundFromBuffer());
                } catch (IOException e) {
                    GT_Log.err.println(e.getCause());
                }
                return null;
            }));
    }

    protected ModularWindow createMachineListWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(260, 215);
        builder.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.widget(
            new TextWidget(EnumChatFormatting.BOLD + "Drone Control Centre").setScale(2)
                .setTextAlignment(Alignment.Center)
                .setPos(0, 10)
                .setSize(260, 8));
        Scrollable MachineContainer = new Scrollable().setVerticalScroll();
        for (int i = 0; i < connectionList.size(); i++) {
            droneConnection connection = connectionList.get(i);
            if (!connection.isValid()) continue;
            ItemStackHandler drawitem = new ItemStackHandler(1);
            drawitem.setStackInSlot(0, connection.machine.getStackForm(1));
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
                        .openSyncedWindow(11);
                })
                    .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.setname"))
                    .setBackground(
                        () -> new IDrawable[] { GT_UITextures.BUTTON_STANDARD, GT_UITextures.OVERLAY_BUTTON_PRINT })
                    .setSize(16, 16))
                .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                    if (coreMachine.isAllowedToWork()) {
                        coreMachine.disableWorking();
                    } else {
                        coreMachine.enableWorking();
                    }
                })
                    .setPlayClickSoundResource(
                        () -> coreMachine.isAllowedToWork() ? SoundResource.GUI_BUTTON_UP.resourceLocation
                            : SoundResource.GUI_BUTTON_DOWN.resourceLocation)
                    .setBackground(() -> {
                        if (coreMachine.isAllowedToWork()) {
                            return new IDrawable[] { GT_UITextures.BUTTON_STANDARD_PRESSED,
                                GT_UITextures.OVERLAY_BUTTON_POWER_SWITCH_ON };
                        } else {
                            return new IDrawable[] { GT_UITextures.BUTTON_STANDARD,
                                GT_UITextures.OVERLAY_BUTTON_POWER_SWITCH_OFF };
                        }
                    })
                    .attachSyncer(new FakeSyncWidget.BooleanSyncer(coreMachine::isAllowedToWork, val -> {
                        if (val) coreMachine.enableWorking();
                        else coreMachine.disableWorking();
                    }), builder)
                    .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.power_switch"))
                    .setSize(16, 16))
                .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                    highlightMachine(player, coreMachine);
                    player.closeScreen();
                })
                    .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.highlight"))
                    .setBackground(
                        () -> new IDrawable[] { GT_UITextures.BUTTON_STANDARD,
                            GT_UITextures.OVERLAY_BUTTON_INVERT_REDSTONE })
                    .setSize(16, 16))
                .widget(
                    new TextWidget(
                        connectionList.get(i)
                            .getCustomName()).setTextAlignment(Alignment.CenterLeft)
                                .setPos(0, 4));
            MachineContainer.widget(
                row.setAlignment(MainAxisAlignment.SPACE_BETWEEN)
                    .setSpace(4)
                    .setPos(0, i * 20));
        }
        return builder.widget(
            MachineContainer.setPos(10, 30)
                .setSize(240, 160))
            .build();
    }

    protected ModularWindow createCustomNameWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(150, 40);
        builder.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        return builder.widget(
            new TextWidget("Custom Machine Name").setTextAlignment(Alignment.Center)
                .setPos(0, 5)
                .setSize(150, 8))
            .widget(
                new TextFieldWidget().setGetter(
                    () -> connectionList.get(buttonID)
                        .getCustomName())
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
    private void highlightMachine(EntityPlayer player, GT_MetaTileEntity_MultiBlockBase machine) {
        DimensionalCoord blockPos = new DimensionalCoord(
            machine.getBaseMetaTileEntity()
                .getXCoord(),
            machine.getBaseMetaTileEntity()
                .getYCoord(),
            machine.getBaseMetaTileEntity()
                .getZCoord(),
            machine.getBaseMetaTileEntity()
                .getWorld().provider.dimensionId);
        WorldCoord blockPos2 = new WorldCoord((int) player.posX, (int) player.posY, (int) player.posZ);
        BlockPosHighlighter.highlightBlock(
            blockPos,
            System.currentTimeMillis() + 500 * WorldCoord.getTaxicabDistance(blockPos, blockPos2));
    }

    public static HashMultimap<Integer, GT_MetaTileEntity_DroneCentre> getCentreMap() {
        return droneMap;
    }

}

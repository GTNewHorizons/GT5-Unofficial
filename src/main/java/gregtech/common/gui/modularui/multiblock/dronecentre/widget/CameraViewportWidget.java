package gregtech.common.gui.modularui.multiblock.dronecentre.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Mouse;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.drawable.GuiDraw;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetThemeEntry;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;
import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;

import gregtech.GTMod;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.data.drone.CameraViewportClientManager;
import gregtech.common.data.drone.CameraViewportManager;
import gregtech.common.gui.modularui.multiblock.dronecentre.panel.CameraObservePanel;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class CameraViewportWidget extends ParentWidget<CameraViewportWidget> implements Interactable {

    private static final int OVERLAY_MARGIN = 4;
    private static final int INDICATOR_MARGIN = 8;
    private static final float INDICATOR_TEXT_SCALE = 0.7F;
    private static final float INFO_BOX_TEXT_SCALE = 0.7F;
    private static final float SIGNAL_LOST_TEXT_SCALE = 2.0F;
    private static final long SIGNAL_LOST_BLINK_MILLIS = 500;

    // Static noise effect colors 
    private static final int NOISE_SCANLINE_COLOR = Color.WHITE.main;
    private static final int NOISE_BAND_COLOR = Color.BLACK.main;
    private static final int NOISE_FLASH_COLOR = Color.WHITE.darker(5);

    private final CameraViewportClientManager cameraManager = (CameraViewportClientManager) GTMod.proxy.cameraViewportManager;

    /** Lines of the info box, rebuilt only when the hovered machine or its synced status changes. */
    private final List<String> infoBoxLines = new ArrayList<>();
    private long infoBoxMachineCoord = CameraViewportManager.NULL_COORD;
    private NBTTagCompound infoBoxStatus = null;

    public CameraViewportWidget() {
        child(createInfoBox())
            .child(
                createIndicator(IKey.lang("GT5U.gui.text.drone_zoom", () -> new Object[] { cameraManager.zoomLevel }))
                    .left(INDICATOR_MARGIN)
                    .bottom(OVERLAY_MARGIN)
                    .setEnabledIf(_ -> cameraManager.zoomLevel > 1.0F && !cameraManager.isSignalLost()))
            .child(
                createIndicator(IKey.lang("GT5U.gui.text.drone_flashlight")).right(INDICATOR_MARGIN)
                    .bottom(OVERLAY_MARGIN)
                    .setEnabledIf(_ -> cameraManager.flashlightActive && !cameraManager.isSignalLost()))
            .child(createSignalLostScreen());
    }

    private IWidget createInfoBox() {
        return IKey.dynamic(() -> String.join("\n", infoBoxLines))
            .asWidget()
            .widgetTheme(GTWidgetThemes.DRONE_CAMERA_INFO_BOX)
            .scale(INFO_BOX_TEXT_SCALE)
            .textAlign(Alignment.CENTER)
            .padding(5, 3)
            .top(OVERLAY_MARGIN)
            .leftRel(0.5F)
            .setEnabledIf(_ -> !infoBoxLines.isEmpty() && !cameraManager.isSignalLost());
    }

    private static TextWidget<?> createIndicator(IKey text) {
        return text.asWidget()
            .widgetTheme(GTWidgetThemes.DRONE_CAMERA_INDICATOR)
            .scale(INDICATOR_TEXT_SCALE);
    }

    private IWidget createSignalLostScreen() {
        return IKey.lang("GT5U.gui.text.drone_signal_interrupted")
            .asWidget()
            .full()
            .widgetTheme(GTWidgetThemes.DRONE_CAMERA_SIGNAL_LOST)
            .scale(SIGNAL_LOST_TEXT_SCALE)
            .textAlign(Alignment.CENTER)
            .onUpdateListener(
                screen -> screen.widgetTheme(
                    isBlinkPhase() ? GTWidgetThemes.DRONE_CAMERA_SIGNAL_LOST_BLINK
                        : GTWidgetThemes.DRONE_CAMERA_SIGNAL_LOST))
            .setEnabledIf(_ -> cameraManager.isSignalLost());
    }

    private static boolean isBlinkPhase() {
        return (System.currentTimeMillis() / SIGNAL_LOST_BLINK_MILLIS) % 2 == 0;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (cameraManager.activeConnection == null) {
            return;
        }
        updateMouseGrab();
        updateHoveredMachine();
    }

    private void updateMouseGrab() {
        if (cameraManager.returningFromRemoteGui) {
            cameraManager.returningFromRemoteGui = false;
            if (!cameraManager.isSignalLost()) {
                Mouse.setGrabbed(true);
            }
        }
        if (cameraManager.isSignalLost() && Mouse.isGrabbed()) {
            Mouse.setGrabbed(false);
        }
    }

    /**
     * Which GT machine the camera is looking at (so the server sends its status), and
     * rebuilds the info box once that status has arrived.
     */
    private void updateHoveredMachine() {
        Minecraft mc = Minecraft.getMinecraft();
        MovingObjectPosition mop = mc.objectMouseOver;
        BaseMetaTileEntity hoveredMachine = null;
        if (!cameraManager.isSignalLost() && mop != null
            && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
            && mc.theWorld.getTileEntity(mop.blockX, mop.blockY, mop.blockZ) instanceof BaseMetaTileEntity gte
            && gte.getMetaTileEntity() != null) {
            hoveredMachine = gte;
        }

        if (hoveredMachine == null) {
            cameraManager.hoveredMachineCoord = CameraViewportManager.NULL_COORD;
            clearInfoBox();
            return;
        }

        long hoveredCoord = CoordinatePacker.pack(mop.blockX, mop.blockY, mop.blockZ);
        cameraManager.hoveredMachineCoord = hoveredCoord;

        NBTTagCompound status = cameraManager.observedMachineStatus;
        boolean statusMatchesMachine = status != null && status.getLong("observePos") == hoveredCoord;
        if (!statusMatchesMachine) {
            clearInfoBox();
            return;
        }
        if (hoveredCoord == infoBoxMachineCoord && status == infoBoxStatus) {
            return;
        }

        infoBoxMachineCoord = hoveredCoord;
        infoBoxStatus = status;
        IMetaTileEntity mte = hoveredMachine.getMetaTileEntity();
        List<String> wailaLines = generateWailaLines(mc, mop, hoveredMachine, mte, status);
        infoBoxLines.clear();
        infoBoxLines.addAll(toInfoBoxLines(wailaLines));
    }

    private void clearInfoBox() {
        infoBoxLines.clear();
        infoBoxMachineCoord = CameraViewportManager.NULL_COORD;
        infoBoxStatus = null;
    }

    /** Draws static noise over the camera feed that gets stronger as the signal gets weaker. */
    @Override
    public void draw(ModularGuiContext context, WidgetThemeEntry<?> widgetTheme) {
        if (cameraManager.activeConnection == null || cameraManager.isSignalLost()) {
            return;
        }

        int w = getArea().width;
        int h = getArea().height;
        int signal = cameraManager.getSignalStrength();
        if (signal < 100) {
            double noiseFactor = (100.0 - signal) / 90.0;
            ThreadLocalRandom rand = ThreadLocalRandom.current();

            int snowCount = (int) (noiseFactor * 120);
            for (int i = 0; i < snowCount; i++) {
                int nx = rand.nextInt(w);
                int ny = rand.nextInt(h);
                int nw = rand.nextInt(5) + 2;
                int nh = rand.nextInt(3) + 2;
                int grey = rand.nextInt(80) + 160;
                int opacity = rand.nextInt(120) + 30;
                int noiseColor = (opacity << 24) | (grey << 16) | (grey << 8) | grey;
                GuiDraw.drawRect(nx, ny, Math.min(w, nx + nw) - nx, Math.min(h, ny + nh) - ny, noiseColor);
            }

            int scanlineCount = (int) (noiseFactor * 6);
            for (int i = 0; i < scanlineCount; i++) {
                int sy = rand.nextInt(h);
                int sh = rand.nextInt(3) + 1;
                int opacity = rand.nextInt(60) + 20;
                int color = Color.withAlpha(NOISE_SCANLINE_COLOR, opacity);
                GuiDraw.drawRect(0, sy, w, sh, color);
            }

            if (rand.nextFloat() < noiseFactor * 0.4F) {
                int bandY = rand.nextInt(h);
                int bandH = rand.nextInt(15) + 5;
                int opacity = rand.nextInt(40) + 10;
                int bandColor = Color.withAlpha(NOISE_BAND_COLOR, opacity);
                GuiDraw.drawRect(0, bandY, w, bandH, bandColor);
            }

            if (signal < 30 && rand.nextFloat() < (1.0 - (signal / 30.0)) * 0.25F) {
                int flashOpacity = rand.nextInt(80) + 40;
                int flashColor = Color.withAlpha(NOISE_FLASH_COLOR, flashOpacity);
                GuiDraw.drawRect(0, 0, w, h, flashColor);
            }
        }
    }

    @Override
    public @NotNull Result onMousePressed(int mouseButton) {
        if (mouseButton == 0 && !cameraManager.isSignalLost()) {
            Mouse.setGrabbed(true);
            return Result.SUCCESS;
        }
        return Result.IGNORE;
    }

    private static List<String> generateWailaLines(final Minecraft mc, final MovingObjectPosition mop,
        final BaseMetaTileEntity gte, final IMetaTileEntity mte, final NBTTagCompound tag) {
        List<String> wailaLines = new ArrayList<>();
        wailaLines.add(EnumChatFormatting.AQUA + mte.getLocalName());

        IWailaDataAccessor accessor = new CameraWailaAccessor(mc, mop, gte, tag);
        ItemStack itemStack = mte.getStackForm(1);
        if (itemStack == null) {
            Block block = mc.theWorld.getBlock(mop.blockX, mop.blockY, mop.blockZ);
            if (block != null && block != Blocks.air && Item.getItemFromBlock(block) != null) {
                itemStack = new ItemStack(block, 1, mc.theWorld.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ));
            }
        }
        if (itemStack != null) {
            gte.getWailaBody(itemStack, wailaLines, accessor, CONFIG_INSTANCE);
        }

        if (tag != null) {
            long storedEU = tag.getLong("mStoredEnergy");
            if (storedEU >= Long.MAX_VALUE - 1) {
                wailaLines.add(
                    EnumChatFormatting.AQUA + StatCollector
                        .translateToLocalFormatted("GT5U.gui.text.drone_stored_energy_max", EnumChatFormatting.GREEN));
            } else if (storedEU > 0) {
                wailaLines.add(
                    EnumChatFormatting.AQUA + StatCollector.translateToLocalFormatted(
                        "GT5U.gui.text.drone_stored_energy",
                        EnumChatFormatting.GREEN,
                        NumberFormatUtil.formatNumber(storedEU)));
            }

            int maxParallel = tag.getInteger("maxParallelRecipes");
            if (maxParallel > 1) {
                wailaLines.add(
                    EnumChatFormatting.AQUA + StatCollector.translateToLocalFormatted(
                        "GT5U.gui.text.drone_max_parallel",
                        EnumChatFormatting.GREEN,
                        maxParallel));
            }
        }

        return wailaLines;
    }

    /**
     * Turns the full WAILA body into the short summary shown in the info box: the machine name plus basic status.
     */
    private static List<String> toInfoBoxLines(List<String> wailaLines) {
        List<String> infoBoxLines = new ArrayList<>();
        infoBoxLines.add(CameraObservePanel.cleanWailaLine(wailaLines.getFirst()));

        String producingLabel = StatCollector.translateToLocal("GT5U.waila.producing")
            .toLowerCase();
        String andMorePattern = StatCollector.translateToLocal("GT5U.waila.producing.andmore")
            .toLowerCase();
        String lockedRecipeLabel = StatCollector.translateToLocal("GT5U.waila.multiblock.status.locked_recipe")
            .toLowerCase();

        for (int i = 1; i < wailaLines.size(); i++) {
            String clean = CameraObservePanel.cleanWailaLine(wailaLines.get(i));
            String lower = clean.toLowerCase();
            if (lower.isEmpty()) continue;

            // Indented lines are the individual outputs listed under "Producing"
            boolean isIndented = clean.startsWith("  ") || clean.startsWith("  ");
            if (isIndented || lower.contains(producingLabel)
                || matchesAndMore(lower, andMorePattern)
                || lower.contains(lockedRecipeLabel)) {
                continue;
            }
            infoBoxLines.add(clean);
        }
        return infoBoxLines;
    }

    /** Whether the line is the "...and %d more" line, by checking it contains every fixed part of the pattern. */
    private static boolean matchesAndMore(String lowerCaseLine, String lowerCaseAndMorePattern) {
        if (lowerCaseAndMorePattern.isEmpty()) {
            return false;
        }
        for (String part : lowerCaseAndMorePattern.split("%d")) {
            String trimmedPart = part.trim();
            if (!trimmedPart.isEmpty() && !lowerCaseLine.contains(trimmedPart)) {
                return false;
            }
        }
        return true;
    }

    private record CameraWailaAccessor(Minecraft mc, MovingObjectPosition mop, TileEntity te, NBTTagCompound tag)
        implements IWailaDataAccessor {

        @Override
        public World getWorld() {
            return mc.theWorld;
        }

        @Override
        public EntityPlayer getPlayer() {
            return mc.thePlayer;
        }

        @Override
        public Block getBlock() {
            return mc.theWorld.getBlock(mop.blockX, mop.blockY, mop.blockZ);
        }

        @Override
        public int getBlockID() {
            Block block = getBlock();
            return block != null ? Block.blockRegistry.getIDForObject(block) : 0;
        }

        @Override
        public String getBlockQualifiedName() {
            Block block = getBlock();
            return block != null ? Block.blockRegistry.getNameForObject(block) : "";
        }

        @Override
        public int getMetadata() {
            return mc.theWorld.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);
        }

        @Override
        public TileEntity getTileEntity() {
            return te;
        }

        @Override
        public MovingObjectPosition getPosition() {
            return mop;
        }

        @Override
        public Vec3 getRenderingPosition() {
            return Vec3.createVectorHelper(mop.blockX, mop.blockY, mop.blockZ);
        }

        @Override
        public NBTTagCompound getNBTData() {
            return tag;
        }

        @Override
        public int getNBTInteger(NBTTagCompound tag, String keyname) {
            return tag != null ? tag.getInteger(keyname) : 0;
        }

        @Override
        public double getPartialFrame() {
            return 0.0;
        }

        @Override
        public ForgeDirection getSide() {
            return ForgeDirection.getOrientation(mop.sideHit);
        }

        @Override
        public ItemStack getStack() {
            Block block = getBlock();
            if (block == null || block == Blocks.air || Item.getItemFromBlock(block) == null) {
                return null;
            }
            return new ItemStack(block, 1, getMetadata());
        }
    }

    private static final CameraWailaConfig CONFIG_INSTANCE = new CameraWailaConfig();

    private record CameraWailaConfig() implements IWailaConfigHandler {

        @Override
        public Set<String> getModuleNames() {
            return Collections.emptySet();
        }

        @Override
        public HashMap<String, String> getConfigKeys(String modName) {
            return new HashMap<>();
        }

        @Override
        public boolean getConfig(String key, boolean defvalue) {
            return true;
        }

        @Override
        public boolean getConfig(String key) {
            return true;
        }
    }
}

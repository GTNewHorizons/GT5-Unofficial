package gregtech.common.gui.modularui.multiblock.dronecentre.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
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
import org.lwjgl.opengl.GL11;

import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.drawable.GuiDraw;
import com.cleanroommc.modularui.drawable.text.TextRenderer;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetThemeEntry;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.widget.Widget;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;
import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;

import gregtech.GTMod;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.common.data.drone.CameraViewportClientManager;
import gregtech.common.data.drone.CameraViewportManager;
import gregtech.common.gui.modularui.multiblock.dronecentre.panel.CameraObservePanel;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class CameraViewportWidget extends Widget<CameraViewportWidget> implements Interactable {

    private static final int COLOR_BLACK_SCREEN = Color.BLACK.main;
    private static final int COLOR_SIGNAL_INTERRUPTED = Color.RED_ACCENT.main;
    private static final int COLOR_SIGNAL_INTERRUPTED_ALT = Color.RED.darkerSafe(3);
    private static final int COLOR_INDICATOR = Color.GREEN.main;
    private static final int COLOR_HUD_TEXT = Color.WHITE.main;
    private static final int COLOR_HUD_BG = Color.withAlpha(Color.BLACK.brighter(1), 128);
    private static final int COLOR_HUD_BORDER = Color.withAlpha(Color.GREY.main, 96);
    private static final int BASE_COLOR_SCANLINE = Color.WHITE.main;
    private static final int BASE_COLOR_FLASH = Color.WHITE.darker(5);

    private final CameraViewportClientManager cameraManager = (CameraViewportClientManager) GTMod.proxy.cameraViewportManager;

    /** Lines of the info box, rebuilt only when the hovered machine or its synced status changes. */
    private final List<String> infoBoxLines = new ArrayList<>();
    private long infoBoxMachineCoord = CameraViewportManager.NULL_COORD;
    private NBTTagCompound infoBoxStatus = null;

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

    @Override
    public void draw(ModularGuiContext context, WidgetThemeEntry<?> widgetTheme) {
        if (cameraManager.activeConnection == null) {
            return;
        }

        int w = getArea().width;
        int h = getArea().height;

        if (cameraManager.isSignalLost()) {
            // A black screen
            GuiDraw.drawRect(0, 0, w, h, COLOR_BLACK_SCREEN);

            String msg = StatCollector.translateToLocal("GT5U.gui.text.drone_signal_interrupted");
            int textW = TextRenderer.getFontRenderer()
                .getStringWidth(msg);
            float scale = 2.0F;
            float scaleDivisor = 0.5F;
            float tx = (w - (textW * scale)) * scaleDivisor;
            float ty = (h - (TextRenderer.getFontRenderer().FONT_HEIGHT * scale)) * scaleDivisor;

            TextRenderer.SHARED.setSimulate(false);
            TextRenderer.SHARED.setShadow(true);
            TextRenderer.SHARED.setScale(scale);
            TextRenderer.SHARED.setPos((int) tx, (int) ty);
            TextRenderer.SHARED.setAlignment(Alignment.CenterLeft, w);

            int color = COLOR_SIGNAL_INTERRUPTED;
            if ((System.currentTimeMillis() / 500) % 2 == 0) {
                color = COLOR_SIGNAL_INTERRUPTED_ALT;
            }
            TextRenderer.SHARED.setColor(color);
            TextRenderer.SHARED.draw(msg);

            drawThinBorder(w, h);
            return;
        }

        // Zoom indicator
        if (cameraManager.zoomLevel > 1.0F) {
            String zoomText = String
                .format(StatCollector.translateToLocal("GT5U.gui.text.drone_zoom"), cameraManager.zoomLevel);
            TextRenderer.SHARED.setSimulate(false);
            TextRenderer.SHARED.setShadow(true);
            TextRenderer.SHARED.setScale(0.7F);
            TextRenderer.SHARED.setPos(8, h - 12);
            TextRenderer.SHARED.setColor(COLOR_INDICATOR);
            TextRenderer.SHARED.setAlignment(Alignment.CenterLeft, w);
            TextRenderer.SHARED.draw(zoomText);
        }

        // Flashlight
        if (cameraManager.flashlightActive) {
            String zoomText = StatCollector.translateToLocal("GT5U.gui.text.drone_flashlight");
            TextRenderer.SHARED.setSimulate(false);
            TextRenderer.SHARED.setShadow(true);
            TextRenderer.SHARED.setScale(0.7F);
            TextRenderer.SHARED.setPos(0, h - 12);
            TextRenderer.SHARED.setColor(COLOR_INDICATOR);
            TextRenderer.SHARED.setAlignment(Alignment.CenterRight, w - 8);
            TextRenderer.SHARED.draw(zoomText);
        }

        if (!infoBoxLines.isEmpty()) {
            drawBasicInfoHUD(w, infoBoxLines);
        }

        // Noise
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
                int color = Color.withAlpha(BASE_COLOR_SCANLINE, opacity);
                GuiDraw.drawRect(0, sy, w, sh, color);
            }

            if (rand.nextFloat() < noiseFactor * 0.4F) {
                int bandY = rand.nextInt(h);
                int bandH = rand.nextInt(15) + 5;
                int opacity = rand.nextInt(40) + 10;
                int bandColor = Color.withAlpha(COLOR_BLACK_SCREEN, opacity);
                GuiDraw.drawRect(0, bandY, w, bandH, bandColor);
            }

            if (signal < 30 && rand.nextFloat() < (1.0 - (signal / 30.0)) * 0.25F) {
                int flashOpacity = rand.nextInt(80) + 40;
                int flashColor = Color.withAlpha(BASE_COLOR_FLASH, flashOpacity);
                GuiDraw.drawRect(0, 0, w, h, flashColor);
            }
        }

        // Border
        drawThinBorder(w, h);
    }

    private void drawThinBorder(int w, int h) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glLineWidth(1.0F);
        GL11.glColor4f(0.3F, 0.3F, 0.3F, 1.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        tessellator.startDrawing(GL11.GL_LINE_LOOP);
        tessellator.addVertex(0, 0, 0);
        tessellator.addVertex(w, 0, 0);
        tessellator.addVertex(w, h, 0);
        tessellator.addVertex(0, h, 0);
        tessellator.draw();
        GL11.glPopAttrib();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
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

    private void drawBasicInfoHUD(int w, List<String> basicInfo) {
        float scale = Math.clamp(w / 346.6F, 0.5F, 0.8F);
        int maxW = 100;
        for (String line : basicInfo) {
            int lw = TextRenderer.getFontRenderer()
                .getStringWidth(line);
            if (lw > maxW) {
                maxW = lw;
            }
        }

        int boxW = (int) (maxW * scale) + 10;
        int boxH = (int) (basicInfo.size() * 10 * scale) + 6;

        float absX = (w - boxW) / 2.0F;
        float absY = 4.0F;

        int left = (int) absX;
        int top = (int) absY;
        GuiDraw.drawRect(left, top, boxW, boxH, COLOR_HUD_BG);
        GuiDraw.drawRect(left, top, boxW, 1, COLOR_HUD_BORDER);
        GuiDraw.drawRect(left, top + boxH - 1, boxW, 1, COLOR_HUD_BORDER);
        GuiDraw.drawRect(left, top, 1, boxH, COLOR_HUD_BORDER);
        GuiDraw.drawRect(left + boxW - 1, top, 1, boxH, COLOR_HUD_BORDER);

        int textY = (int) absY + 3;
        for (String line : basicInfo) {
            int lw = TextRenderer.getFontRenderer()
                .getStringWidth(line);
            int tx = (int) absX + (boxW - (int) (lw * scale)) / 2;
            TextRenderer.SHARED.setSimulate(false);
            TextRenderer.SHARED.setShadow(false);
            TextRenderer.SHARED.setScale(scale);
            TextRenderer.SHARED.setPos(tx, textY);
            TextRenderer.SHARED.setColor(COLOR_HUD_TEXT);
            TextRenderer.SHARED.setAlignment(Alignment.CenterLeft, boxW);
            TextRenderer.SHARED.draw(line);
            textY += (int) (10 * scale);
        }
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

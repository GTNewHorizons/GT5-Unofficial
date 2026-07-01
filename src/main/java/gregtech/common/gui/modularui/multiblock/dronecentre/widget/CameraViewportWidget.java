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
import com.cleanroommc.modularui.widget.Widget;

import gregtech.GTMod;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.common.data.drone.CameraViewportClientManager;
import gregtech.common.gui.modularui.multiblock.dronecentre.panel.CameraObservePanel;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class CameraViewportWidget extends Widget<CameraViewportWidget> implements Interactable {

    private long lastStateHash = 0L;
    private List<String> lastLines = null;

    public CameraViewportWidget() {}

    @Override
    public void draw(ModularGuiContext context, WidgetThemeEntry<?> widgetTheme) {
        CameraViewportClientManager cvm = (CameraViewportClientManager) GTMod.proxy.cameraViewportManager;

        if (cvm.activeConnection == null) {
            return;
        }

        int w = getArea().width;
        int h = getArea().height;

        Minecraft mc = Minecraft.getMinecraft();

        int signal = cvm.getSignalStrength();
        if (signal <= 10) {
            if (Mouse.isGrabbed()) {
                Mouse.setGrabbed(false);
            }
            cvm.hoveredMachineX = -1;
            cvm.hoveredMachineY = -1;
            cvm.hoveredMachineZ = -1;
            cvm.cachedWailaLines = null;

            // A black screen
            GuiDraw.drawRect(0, 0, w, h, 0xFF000000);

            String msg = StatCollector.translateToLocal("GT5U.gui.text.drone_signal_interrupted");
            int textW = TextRenderer.getFontRenderer()
                .getStringWidth(msg);
            float scale = 2.0F;
            float tx = (w - (textW * scale)) / 2.0F;
            float ty = (h - (TextRenderer.getFontRenderer().FONT_HEIGHT * scale)) / 2.0F;

            TextRenderer.SHARED.setSimulate(false);
            TextRenderer.SHARED.setShadow(true);
            TextRenderer.SHARED.setScale(scale);
            TextRenderer.SHARED.setPos((int) tx, (int) ty);
            TextRenderer.SHARED.setAlignment(Alignment.CenterLeft, w);

            int color = 0xFFFF0000;
            if ((System.currentTimeMillis() / 500) % 2 == 0) {
                color = 0xFF880000;
            }
            TextRenderer.SHARED.setColor(color);
            TextRenderer.SHARED.draw(msg);

            drawThinBorder(w, h);
            return;
        }

        // Zoom indicator
        if (cvm.zoomLevel > 1.0F) {
            String zoomText = String.format(StatCollector.translateToLocal("GT5U.gui.text.drone_zoom"), cvm.zoomLevel);
            TextRenderer.SHARED.setSimulate(false);
            TextRenderer.SHARED.setShadow(true);
            TextRenderer.SHARED.setScale(0.7F);
            TextRenderer.SHARED.setPos(8, h - 12);
            TextRenderer.SHARED.setColor(0xFF00FF00);
            TextRenderer.SHARED.setAlignment(Alignment.CenterLeft, w);
            TextRenderer.SHARED.draw(zoomText);
        }

        // Flashlight
        if (cvm.flashlightActive) {
            String zoomText = StatCollector.translateToLocal("GT5U.gui.text.drone_flashlight");
            TextRenderer.SHARED.setSimulate(false);
            TextRenderer.SHARED.setShadow(true);
            TextRenderer.SHARED.setScale(0.7F);
            TextRenderer.SHARED.setPos(0, h - 12);
            TextRenderer.SHARED.setColor(0xFF00FF00);
            TextRenderer.SHARED.setAlignment(Alignment.CenterRight, w - 8);
            TextRenderer.SHARED.draw(zoomText);
        }

        // WAILA
        MovingObjectPosition mop = mc.objectMouseOver;
        boolean hasHovered = false;
        if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            TileEntity te = mc.theWorld.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
            if (te instanceof BaseMetaTileEntity gte) {
                IMetaTileEntity mte = gte.getMetaTileEntity();
                if (mte != null) {
                    hasHovered = true;
                    cvm.hoveredMachineX = mop.blockX;
                    cvm.hoveredMachineY = mop.blockY;
                    cvm.hoveredMachineZ = mop.blockZ;

                    net.minecraft.nbt.NBTTagCompound status = cvm.observedMachineStatus;
                    boolean hasSyncedNBT = (status != null && status.getInteger("observeX") == mop.blockX
                        && status.getInteger("observeY") == mop.blockY
                        && status.getInteger("observeZ") == mop.blockZ);

                    if (hasSyncedNBT) {
                        long stateHash = 17L;
                        stateHash = 31L * stateHash + mop.blockX;
                        stateHash = 31L * stateHash + mop.blockY;
                        stateHash = 31L * stateHash + mop.blockZ;
                        stateHash = 31L * stateHash + System.identityHashCode(status);

                        if (stateHash == lastStateHash) {
                            cvm.cachedWailaLines = lastLines;
                        } else {
                            cvm.cachedWailaLines = generateWailaLines(
                                mc,
                                mop,
                                te,
                                gte,
                                mte,
                                status,
                                mte.getLocalName());
                            lastStateHash = stateHash;
                            lastLines = cvm.cachedWailaLines;
                        }
                    } else {
                        cvm.cachedWailaLines = null;
                        lastStateHash = 0L;
                        lastLines = null;
                    }
                }
            }
        }

        if (!hasHovered) {
            cvm.hoveredMachineX = -1;
            cvm.hoveredMachineY = -1;
            cvm.hoveredMachineZ = -1;
            cvm.cachedWailaLines = null;
            lastStateHash = 0L;
            lastLines = null;
        }

        if (cvm.cachedWailaLines != null && !cvm.cachedWailaLines.isEmpty()) {
            drawBasicInfoHUD(w, cvm.cachedWailaLines);
        }

        // Noise
        signal = cvm.getSignalStrength();
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
                int color = (opacity << 24) | 0x00FFFFFF;
                GuiDraw.drawRect(0, sy, w, sh, color);
            }

            if (rand.nextFloat() < noiseFactor * 0.4F) {
                int bandY = rand.nextInt(h);
                int bandH = rand.nextInt(15) + 5;
                int opacity = rand.nextInt(40) + 10;
                int bandColor = (opacity << 24);
                GuiDraw.drawRect(0, bandY, w, bandH, bandColor);
            }

            if (signal < 30 && rand.nextFloat() < (1.0 - (signal / 30.0)) * 0.25F) {
                int flashOpacity = rand.nextInt(80) + 40;
                int flashColor = (flashOpacity << 24) | 0x00D0D0D0;
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
        if (mouseButton == 0
            && ((CameraViewportClientManager) GTMod.proxy.cameraViewportManager).getSignalStrength() > 10) {
            Mouse.setGrabbed(true);
            return Result.SUCCESS;
        }
        return Result.IGNORE;
    }

    public static List<String> generateWailaLines(final Minecraft mc, final MovingObjectPosition mop,
        final TileEntity te, final BaseMetaTileEntity gte, final IMetaTileEntity mte, final NBTTagCompound tag,
        String nameToUse) {
        List<String> wailaLines = new ArrayList<>();
        wailaLines.add("§b" + nameToUse);

        IWailaDataAccessor accessor = new CameraWailaAccessor(mc, mop, te, tag);
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
        return wailaLines;
    }

    private void drawBasicInfoHUD(int w, List<String> lines) {
        List<String> basicInfo = new ArrayList<>();
        basicInfo.add(CameraObservePanel.cleanWailaLine(lines.getFirst()));

        String producingLabel = StatCollector.translateToLocal("GT5U.waila.producing")
            .toLowerCase();
        String andMorePattern = StatCollector.translateToLocal("GT5U.waila.producing.andmore")
            .toLowerCase();
        String lockedRecipeLabel = StatCollector.translateToLocal("GT5U.waila.multiblock.status.locked_recipe")
            .toLowerCase();

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String clean = CameraObservePanel.cleanWailaLine(line);
            String lower = clean.toLowerCase();
            if (lower.isEmpty()) continue;

            boolean isAndMore = false;
            if (!andMorePattern.isEmpty()) {
                String[] andMoreParts = andMorePattern.split("%d");
                if (andMoreParts.length > 0) {
                    isAndMore = true;
                    for (String part : andMoreParts) {
                        String p = part.trim();
                        if (!p.isEmpty() && !lower.contains(p)) {
                            isAndMore = false;
                            break;
                        }
                    }
                }
            }

            if (clean.startsWith("  ") || clean.startsWith("  ")
                || lower.contains(producingLabel)
                || isAndMore
                || lower.contains(lockedRecipeLabel)) {
                continue;
            }

            basicInfo.add(clean);
        }

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
        GuiDraw.drawRect(left, top, boxW, boxH, 0x80101010);
        GuiDraw.drawRect(left, top, boxW, 1, 0x60A0A0A0);
        GuiDraw.drawRect(left, top + boxH - 1, boxW, 1, 0x60A0A0A0);
        GuiDraw.drawRect(left, top, 1, boxH, 0x60A0A0A0);
        GuiDraw.drawRect(left + boxW - 1, top, 1, boxH, 0x60A0A0A0);

        int textY = (int) absY + 3;
        for (String line : basicInfo) {
            int lw = TextRenderer.getFontRenderer()
                .getStringWidth(line);
            int tx = (int) absX + (boxW - (int) (lw * scale)) / 2;
            TextRenderer.SHARED.setSimulate(false);
            TextRenderer.SHARED.setShadow(false);
            TextRenderer.SHARED.setScale(scale);
            TextRenderer.SHARED.setPos(tx, textY);
            TextRenderer.SHARED.setColor(0xFFFFFFFF);
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

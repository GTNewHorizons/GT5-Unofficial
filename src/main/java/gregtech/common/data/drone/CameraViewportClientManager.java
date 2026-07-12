package gregtech.common.data.drone;

import static gregtech.api.enums.GTValues.NW;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.cleanroommc.modularui.drawable.GuiDraw;
import com.cleanroommc.modularui.screen.GuiContainerWrapper;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import codechicken.nei.NEIClientConfig;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.net.PacketObserveMachine;
import gregtech.api.net.PacketOpenRemoteMteGui;
import gregtech.common.entity.EntityDrone;
import gregtech.common.tileentities.machines.multi.drone.DroneConnection;
import gregtech.common.tileentities.machines.multi.drone.MTEDroneCentre;

@SideOnly(Side.CLIENT)
public class CameraViewportClientManager extends CameraViewportManager {

    public DroneConnection activeConnection = null;
    public NBTTagCompound observedMachineStatus = null;

    public double cameraX, cameraY, cameraZ;
    public double spawnX, spawnY, spawnZ;
    public float cameraYaw, cameraPitch;
    public float spawnYaw;
    public int hoveredMachineX = -1;
    public int hoveredMachineY = -1;
    public int hoveredMachineZ = -1;
    public java.util.List<String> cachedWailaLines = null;
    public boolean flashlightActive = false;
    public boolean switchingToRemoteGui = false;
    public float zoomLevel = 1.0F;

    private ModularPanel originalMainPanelRef = null;
    private EntityDrone dummyCamera = null;
    public EntityLivingBase originalViewEntity = null;

    private int backgroundTextureId = -1;
    private int sendUpdateTimer = 0;
    private int switchingToRemoteGuiTimeout = 0;

    private boolean wasNeiHidden = false;
    private boolean wasLDown = false;
    private boolean wasGDown = false;
    private boolean hadNightVisionBefore = false;
    private boolean wasTabDown = false;
    private boolean wasF5Down = false;
    private float originalFov = -1.0F;
    private boolean wasPageUpDown = false;
    private int lastHoveredX = -1;
    private int lastHoveredY = -1;
    private int lastHoveredZ = -1;

    @Override
    public boolean isObservingActive() {
        return activeConnection != null;
    }

    @Override
    public boolean isObservingActive(UUID uuid) {
        return isObservingActive();
    }

    @Override
    public void setObservedMachineStatus(NBTTagCompound tag) {
        this.observedMachineStatus = tag;
    }

    @Override
    public void resetStatus() {
        super.resetStatus();
        activeConnection = null;
        observedMachineStatus = null;
    }

    @Override
    public boolean isSwitchingToRemoteGui() {
        return switchingToRemoteGui;
    }

    @Override
    public void setSwitchingToRemoteGui(boolean val) {
        this.switchingToRemoteGui = val;
    }

    private void updateFlashlightPotionEffects(boolean active) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        if (active) {
            PotionEffect existing = mc.thePlayer.getActivePotionEffect(Potion.nightVision);
            if (existing != null) {
                hadNightVisionBefore = true;
            } else {
                mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.id, 999999, 0, true));
                hadNightVisionBefore = false;
            }
            if (dummyCamera != null) {
                dummyCamera.addPotionEffect(new PotionEffect(Potion.nightVision.id, 999999, 0, true));
            }
        } else {
            if (!hadNightVisionBefore) {
                mc.thePlayer.removePotionEffect(Potion.nightVision.id);
            }
            if (dummyCamera != null) {
                dummyCamera.removePotionEffect(Potion.nightVision.id);
            }
            hadNightVisionBefore = false;
        }
    }

    private boolean isNeiHidden() {
        return NEIClientConfig.isHidden();
    }

    private void setNeiHidden(boolean hidden) {
        NEIClientConfig.getSetting("inventory.hidden")
            .setBooleanValue(hidden);
    }

    public void hideScreenMainPanel() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen instanceof GuiContainerWrapper wrapper) {
            hideScreenMainPanel(wrapper.getScreen());
        }
    }

    public void hideScreenMainPanel(ModularScreen screenObj) {
        if (screenObj != null) {
            ModularPanel panel = screenObj.getMainPanel();
            if (panel != null) {
                originalMainPanelRef = panel;
                panel.setEnabledIf(_ -> false);
                screenObj.drawDarkBackground(false);
            }
        }
    }

    private void restoreScreenMainPanel() {
        if (originalMainPanelRef != null) {
            originalMainPanelRef.setEnabledIf(_ -> true);
        }
        originalMainPanelRef = null;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen instanceof GuiContainerWrapper wrapper) {
            wrapper.getScreen()
                .drawDarkBackground(true);
        }
    }

    public void startObserving(DroneConnection connection) {
        if (this.activeConnection == null) {
            FMLCommonHandler.instance()
                .bus()
                .register(this);
            MinecraftForge.EVENT_BUS.register(this);
        }
        Minecraft mc = Minecraft.getMinecraft();
        int width = mc.displayWidth;
        int height = mc.displayHeight;
        if (backgroundTextureId != -1) {
            GL11.glDeleteTextures(backgroundTextureId);
            backgroundTextureId = -1;
        }
        backgroundTextureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, backgroundTextureId);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL11.glCopyTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, 0, 0, width, height, 0);

        this.activeConnection = connection;
        double spawnX = connection.getMachineCoord().posX + 0.5;
        double spawnY = connection.getMachineCoord().posY + 1.5;
        double spawnZ = connection.getMachineCoord().posZ + 0.5;
        float spawnYaw = 0;

        ForgeDirection facing = ForgeDirection.getOrientation(connection.getMachineFacing());
        switch (facing) {
            case NORTH:
                spawnZ -= 1.2;
                spawnYaw = 0;
                break;
            case SOUTH:
                spawnZ += 1.2;
                spawnYaw = 180;
                break;
            case WEST:
                spawnX -= 1.2;
                spawnYaw = 270;
                break;
            case EAST:
                spawnX += 1.2;
                spawnYaw = 90;
                break;
            default:
                break;
        }

        this.cameraX = spawnX;
        this.cameraY = spawnY;
        this.cameraZ = spawnZ;
        this.cameraYaw = spawnYaw;
        this.cameraPitch = 0;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.spawnZ = spawnZ;
        this.spawnYaw = spawnYaw;
        this.sendUpdateTimer = 0;
        this.flashlightActive = false;
        this.wasLDown = false;
        this.wasF5Down = false;
        this.hadNightVisionBefore = false;
        this.switchingToRemoteGui = false;
        this.hoveredMachineX = -1;
        this.hoveredMachineY = -1;
        this.hoveredMachineZ = -1;
        this.lastHoveredX = -1;
        this.lastHoveredY = -1;
        this.lastHoveredZ = -1;

        if (dummyCamera == null || dummyCamera.worldObj != mc.theWorld) {
            dummyCamera = new EntityDrone(mc.theWorld);
            if (mc.theWorld instanceof WorldClient) {
                mc.theWorld.addEntityToWorld(-9999, dummyCamera);
            }
        }

        dummyCamera.setPosition(cameraX, cameraY, cameraZ);
        dummyCamera.prevPosX = cameraX;
        dummyCamera.prevPosY = cameraY;
        dummyCamera.prevPosZ = cameraZ;
        dummyCamera.lastTickPosX = cameraX;
        dummyCamera.lastTickPosY = cameraY;
        dummyCamera.lastTickPosZ = cameraZ;
        dummyCamera.rotationYaw = cameraYaw;
        dummyCamera.rotationPitch = cameraPitch;
        dummyCamera.prevRotationYaw = cameraYaw;
        dummyCamera.prevRotationPitch = cameraPitch;

        if (mc.renderViewEntity != dummyCamera) {
            this.originalViewEntity = mc.renderViewEntity;
            mc.renderViewEntity = dummyCamera;
        }

        this.wasNeiHidden = isNeiHidden();
        if (!wasNeiHidden) {
            setNeiHidden(true);
        }

        hideScreenMainPanel();

        sendObservePacket(true);
    }

    public void stopObserving() {
        if (activeConnection == null) return;
        if (backgroundTextureId != -1) {
            GL11.glDeleteTextures(backgroundTextureId);
            backgroundTextureId = -1;
        }
        if (flashlightActive) {
            updateFlashlightPotionEffects(false);
        }
        sendObservePacket(false);
        FMLCommonHandler.instance()
            .bus()
            .unregister(this);
        MinecraftForge.EVENT_BUS.unregister(this);
        activeConnection = null;
        observedMachineStatus = null;
        hoveredMachineX = -1;
        hoveredMachineY = -1;
        hoveredMachineZ = -1;
        lastHoveredX = -1;
        lastHoveredY = -1;
        lastHoveredZ = -1;
        switchingToRemoteGui = false;

        Minecraft mc = Minecraft.getMinecraft();
        if (dummyCamera != null && mc.theWorld instanceof WorldClient) {
            mc.theWorld.removeEntityFromWorld(-9999);
        }
        dummyCamera = null;

        if (Mouse.isGrabbed()) {
            Mouse.setGrabbed(false);
        }

        restoreScreenMainPanel();

        if (!wasNeiHidden) {
            setNeiHidden(false);
        }

        if (originalFov != -1.0F) {
            mc.gameSettings.fovSetting = originalFov;
            originalFov = -1.0F;
        }
        zoomLevel = 1.0F;
        wasPageUpDown = false;
        wasTabDown = false;
        wasF5Down = false;

        if (originalViewEntity != null) {
            mc.renderViewEntity = originalViewEntity;
            originalViewEntity = null;
        } else if (mc.thePlayer != null) {
            mc.renderViewEntity = mc.thePlayer;
        }
    }

    private void sendObservePacket(boolean isObserving) {
        if (activeConnection == null) return;
        PacketObserveMachine pkt = new PacketObserveMachine(
            activeConnection.getMachineWorld(),
            activeConnection.getCentreCoord().posX,
            activeConnection.getCentreCoord().posY,
            activeConnection.getCentreCoord().posZ,
            activeConnection.getMachineCoord().posX,
            activeConnection.getMachineCoord().posY,
            activeConnection.getMachineCoord().posZ,
            isObserving,
            cameraX,
            cameraY,
            cameraZ,
            cameraYaw);
        pkt.hoveredX = this.hoveredMachineX;
        pkt.hoveredY = this.hoveredMachineY;
        pkt.hoveredZ = this.hoveredMachineZ;
        NW.sendToServer(pkt);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (activeConnection == null) return;

        double oldX = cameraX;
        double oldY = cameraY;
        double oldZ = cameraZ;

        Minecraft mc = Minecraft.getMinecraft();
        if (switchingToRemoteGui && mc.currentScreen == null) {
            switchingToRemoteGuiTimeout++;
            if (switchingToRemoteGuiTimeout > 100) {
                switchingToRemoteGui = false;
                switchingToRemoteGuiTimeout = 0;
            }
        } else {
            switchingToRemoteGuiTimeout = 0;
        }

        if (mc.theWorld == null || (mc.currentScreen == null && !switchingToRemoteGui)) {
            stopObserving();
            return;
        }

        if (switchingToRemoteGui) {
            if (dummyCamera != null) {
                dummyCamera.setPosition(cameraX, cameraY, cameraZ);
            }
            return;
        }

        boolean isLDown = Keyboard.isKeyDown(Keyboard.KEY_L);
        if (isLDown && !wasLDown) {
            flashlightActive = !flashlightActive;
            mc.getSoundHandler()
                .playSound(PositionedSoundRecord.func_147673_a(new ResourceLocation("gui.button.press")));
            updateFlashlightPotionEffects(flashlightActive);
        }
        wasLDown = isLDown;

        boolean isF5Down = Keyboard.isKeyDown(Keyboard.KEY_F5);
        if (isF5Down && !wasF5Down) {
            mc.gameSettings.thirdPersonView = mc.gameSettings.thirdPersonView == 0 ? 1 : 0;
        }
        wasF5Down = isF5Down;

        boolean isGDown = Keyboard.isKeyDown(Keyboard.KEY_G);
        if (isGDown && !wasGDown) {
            int rx = this.hoveredMachineX;
            int ry = this.hoveredMachineY;
            int rz = this.hoveredMachineZ;
            World world = mc.theWorld;
            if (world != null && rx != -1) {
                TileEntity te = world.getTileEntity(rx, ry, rz);
                if (te instanceof BaseMetaTileEntity baseMte) {
                    IMetaTileEntity mte = baseMte.getMetaTileEntity();
                    if (mte instanceof MTEMultiBlockBase && !(mte instanceof MTEDroneCentre)) {
                        boolean openedFromItem = false;
                        if (mc.currentScreen instanceof GuiContainerWrapper wrapper) {
                            ModularScreen screen = wrapper.getScreen();
                            if (screen.getMainPanel() != null) {
                                openedFromItem = "remoteControl".equals(
                                    screen.getMainPanel()
                                        .getName());
                            }
                        }
                        switchingToRemoteGui = true;
                        if (Mouse.isGrabbed()) {
                            Mouse.setGrabbed(false);
                        }
                        PacketOpenRemoteMteGui pkt = new PacketOpenRemoteMteGui(
                            CoordinatePacker.pack(rx, ry, rz),
                            openedFromItem);
                        NW.sendToServer(pkt);
                    }
                }
            }
        }
        wasGDown = isGDown;

        boolean isZoomIn = Keyboard.isKeyDown(Keyboard.KEY_EQUALS) || Keyboard.isKeyDown(Keyboard.KEY_ADD)
            || Keyboard.isKeyDown(Keyboard.KEY_PRIOR);
        boolean isZoomOut = Keyboard.isKeyDown(Keyboard.KEY_MINUS) || Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)
            || Keyboard.isKeyDown(Keyboard.KEY_NEXT);

        if (isZoomIn && !wasPageUpDown) {
            if (zoomLevel < 6.0F) {
                zoomLevel += 0.5F;
                mc.getSoundHandler()
                    .playSound(PositionedSoundRecord.func_147673_a(new ResourceLocation("gui.button.press")));
            }
        }
        if (isZoomOut && !wasPageUpDown) {
            if (zoomLevel > 1.0F) {
                zoomLevel -= 0.5F;
                mc.getSoundHandler()
                    .playSound(PositionedSoundRecord.func_147673_a(new ResourceLocation("gui.button.press")));
            }
        }
        wasPageUpDown = isZoomIn || isZoomOut;

        if (dummyCamera == null || dummyCamera.worldObj != mc.theWorld) {
            dummyCamera = new EntityDrone(mc.theWorld);
            if (mc.theWorld instanceof WorldClient) {
                mc.theWorld.addEntityToWorld(-9999, dummyCamera);
            }
            if (flashlightActive) {
                dummyCamera.addPotionEffect(new PotionEffect(Potion.nightVision.id, 999999, 0, true));
            }
        }

        if (mc.renderViewEntity != dummyCamera) {
            this.originalViewEntity = mc.renderViewEntity;
            mc.renderViewEntity = dummyCamera;
        }

        boolean isTabDown = Keyboard.isKeyDown(Keyboard.KEY_TAB);
        if (isTabDown && !wasTabDown) {
            if (getSignalStrength() > 10) {
                Mouse.setGrabbed(!Mouse.isGrabbed());
            }
        }
        wasTabDown = isTabDown;

        if (Mouse.isGrabbed()) {
            if (getSignalStrength() <= 10) {
                Mouse.setGrabbed(false);
                return;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                Mouse.setGrabbed(false);
                return;
            }

            boolean slow = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
            double speed = slow ? 0.1 : 0.4;
            double moveX = 0;
            double moveY = 0;
            double moveZ = 0;

            double radYaw = Math.toRadians(cameraYaw);

            if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
                moveX -= Math.sin(radYaw) * speed;
                moveZ += Math.cos(radYaw) * speed;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
                moveX += Math.sin(radYaw) * speed;
                moveZ -= Math.cos(radYaw) * speed;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
                moveX += Math.cos(radYaw) * speed;
                moveZ += Math.sin(radYaw) * speed;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
                moveX -= Math.cos(radYaw) * speed;
                moveZ -= Math.sin(radYaw) * speed;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                moveY += speed;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                moveY -= speed;
            }

            dummyCamera.setPosition(cameraX, cameraY, cameraZ);
            dummyCamera.moveEntity(moveX, moveY, moveZ);

            double centerX = activeConnection.getMachineCoord().posX + 0.5;
            double centerZ = activeConnection.getMachineCoord().posZ + 0.5;
            double dx = dummyCamera.posX - centerX;
            double dz = dummyCamera.posZ - centerZ;
            double dist = Math.sqrt(dx * dx + dz * dz);
            double R = 32.0;

            if (dist > R) {
                cameraX = centerX + (dx / dist) * R;
                cameraZ = centerZ + (dz / dist) * R;
            } else {
                cameraX = dummyCamera.posX;
                cameraZ = dummyCamera.posZ;
            }
            cameraY = Math.clamp(dummyCamera.posY, 0, 256);
        }

        dummyCamera.lastTickPosX = oldX;
        dummyCamera.lastTickPosY = oldY;
        dummyCamera.lastTickPosZ = oldZ;
        dummyCamera.prevPosX = oldX;
        dummyCamera.prevPosY = oldY;
        dummyCamera.prevPosZ = oldZ;

        dummyCamera.setPosition(cameraX, cameraY, cameraZ);

        boolean hoveredChanged = (hoveredMachineX != lastHoveredX || hoveredMachineY != lastHoveredY
            || hoveredMachineZ != lastHoveredZ);
        lastHoveredX = hoveredMachineX;
        lastHoveredY = hoveredMachineY;
        lastHoveredZ = hoveredMachineZ;

        if (hoveredChanged) {
            sendUpdateTimer = 0;
            sendObservePacket(true);
        } else {
            sendUpdateTimer++;
            if (sendUpdateTimer >= 10) {
                sendUpdateTimer = 0;
                sendObservePacket(true);
            }
        }
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (activeConnection == null || dummyCamera == null) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (event.phase == TickEvent.Phase.START
            && (mc.theWorld == null || (mc.currentScreen == null && !switchingToRemoteGui))) {
            stopObserving();
            return;
        }

        if (switchingToRemoteGui) return;

        if (event.phase == TickEvent.Phase.START) {
            if (originalFov == -1.0F) {
                originalFov = mc.gameSettings.fovSetting;
            }
            mc.gameSettings.fovSetting = originalFov / zoomLevel;
        } else if (event.phase == TickEvent.Phase.END) {
            if (originalFov != -1.0F) {
                mc.gameSettings.fovSetting = originalFov;
                originalFov = -1.0F;
            }
        }

        if (event.phase != TickEvent.Phase.START) return;

        if (Mouse.isGrabbed()) {
            float mouseSensitivity = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            float f = mouseSensitivity * mouseSensitivity * mouseSensitivity * 8.0F;
            float dx = (float) Mouse.getDX() * f * 0.15F;
            float dy = (float) Mouse.getDY() * f * 0.15F;

            cameraYaw += dx;
            cameraPitch -= dy;
            cameraPitch = Math.clamp(cameraPitch, -90.0F, 90.0F);

            dummyCamera.prevRotationYaw = cameraYaw;
            dummyCamera.prevRotationPitch = cameraPitch;
            dummyCamera.rotationYaw = cameraYaw;
            dummyCamera.rotationPitch = cameraPitch;
        }
    }

    @SubscribeEvent
    public void onDrawScreenPre(GuiScreenEvent.DrawScreenEvent.Pre event) {
        if (activeConnection == null || switchingToRemoteGui) return;

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int sw = sr.getScaledWidth();
        int sh = sr.getScaledHeight();

        int panelW = (int) (sw * 0.8);
        int panelH = (int) (sh * 0.8);

        int x0 = (sw - panelW) / 2;
        int y0 = (sh - panelH) / 2;

        if (backgroundTextureId != -1) {
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, backgroundTextureId);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            drawTexturedRect(0, 0, x0, sh, 0.0F, 1.0F, (float) x0 / sw, 0.0F);
            drawTexturedRect(x0 + panelW, 0, sw, sh, (float) (x0 + panelW) / sw, 1.0F, 1.0F, 0.0F);
            drawTexturedRect(
                x0,
                0,
                x0 + panelW,
                y0,
                (float) x0 / sw,
                1.0F,
                (float) (x0 + panelW) / sw,
                1.0F - (float) y0 / sh);
            drawTexturedRect(
                x0,
                y0 + panelH,
                x0 + panelW,
                sh,
                (float) x0 / sw,
                1.0F - (float) (y0 + panelH) / sh,
                (float) (x0 + panelW) / sw,
                0.0F);
        } else {
            GuiDraw.drawRect(0, 0, x0, sh, 0xFF000000);
            GuiDraw.drawRect(x0 + panelW, 0, sw - (x0 + panelW), sh, 0xFF000000);
            GuiDraw.drawRect(x0, 0, panelW, y0, 0xFF000000);
            GuiDraw.drawRect(x0, y0 + panelH, panelW, sh - (y0 + panelH), 0xFF000000);
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (activeConnection == null) return;

        double renderX = RenderManager.renderPosX;
        double renderY = RenderManager.renderPosY;
        double renderZ = RenderManager.renderPosZ;

        double centerX = activeConnection.getMachineCoord().posX + 0.5;
        double centerY = activeConnection.getMachineCoord().posY + 0.5;
        double centerZ = activeConnection.getMachineCoord().posZ + 0.5;

        GL11.glPushMatrix();
        GL11.glTranslated(centerX - renderX, centerY - renderY, centerZ - renderZ);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(false);

        float alpha = 0.5F + 0.2F * (float) Math.sin(System.currentTimeMillis() / 600.0);
        float r = 0.0F;
        float g = 0.9F;
        float b = 1.0F;

        GL11.glLineWidth(3.0F);
        int segments = 120;
        double R = 32.0;
        double[] heights = { -16.0, 0.0, 16.0, 32.0, 48.0 };

        for (double h : heights) {
            GL11.glColor4f(r, g, b, alpha);
            GL11.glBegin(GL11.GL_LINE_LOOP);
            for (int i = 0; i < segments; i++) {
                double theta = 2.0 * Math.PI * i / segments;
                double x = R * Math.cos(theta);
                double z = R * Math.sin(theta);
                GL11.glVertex3d(x, h, z);
            }
            GL11.glEnd();
        }

        GL11.glLineWidth(1.5F);
        GL11.glColor4f(r, g, b, alpha * 0.5F);
        GL11.glBegin(GL11.GL_LINES);
        int verticalLines = 16;
        for (int i = 0; i < verticalLines; i++) {
            double theta = 2.0 * Math.PI * i / (double) verticalLines;
            double x = R * Math.cos(theta);
            double z = R * Math.sin(theta);
            GL11.glVertex3d(x, -16.0, z);
            GL11.glVertex3d(x, 48.0, z);
        }
        GL11.glEnd();

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public int getSignalStrength() {
        if (activeConnection == null) return 0;
        double centerX = activeConnection.getMachineCoord().posX + 0.5;
        double centerZ = activeConnection.getMachineCoord().posZ + 0.5;
        double dx = cameraX - centerX;
        double dz = cameraZ - centerZ;
        double r = Math.sqrt(dx * dx + dz * dz);
        double distToCircleEdge = 32.0 - r;

        if (distToCircleEdge > 12.0) {
            return 100;
        } else if (distToCircleEdge <= 0.0) {
            return 10;
        } else {
            return 10 + (int) (distToCircleEdge * 7.5);
        }
    }

    public void resetToSpawn() {
        if (activeConnection == null) return;
        this.cameraX = this.spawnX;
        this.cameraY = this.spawnY;
        this.cameraZ = this.spawnZ;
        this.cameraYaw = this.spawnYaw;
        this.cameraPitch = 0.0F;
        if (dummyCamera != null) {
            dummyCamera.setPosition(cameraX, cameraY, cameraZ);
            dummyCamera.prevPosX = cameraX;
            dummyCamera.prevPosY = cameraY;
            dummyCamera.prevPosZ = cameraZ;
            dummyCamera.lastTickPosX = cameraX;
            dummyCamera.lastTickPosY = cameraY;
            dummyCamera.lastTickPosZ = cameraZ;
            dummyCamera.rotationYaw = cameraYaw;
            dummyCamera.rotationPitch = 0.0F;
            dummyCamera.prevRotationYaw = cameraYaw;
            dummyCamera.prevRotationPitch = 0.0F;
        }
        sendObservePacket(true);
    }

    private void drawTexturedRect(int x1, int y1, int x2, int y2, float u1, float v1, float u2, float v2) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x1, y2, 0.0D, u1, v2);
        tessellator.addVertexWithUV(x2, y2, 0.0D, u2, v2);
        tessellator.addVertexWithUV(x2, y1, 0.0D, u2, v1);
        tessellator.addVertexWithUV(x1, y1, 0.0D, u1, v1);
        tessellator.draw();
    }

    @SubscribeEvent
    public void onRenderHand(RenderHandEvent event) {
        if (activeConnection != null) {
            event.setCanceled(true);
        }
    }

}

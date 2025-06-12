package gregtech.common.data.maglev;

import java.util.WeakHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.WorldEvent;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import com.gtnewhorizon.gtnhlib.datastructs.spatialhashgrid.SpatialHashGrid;
import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gregtech.common.config.Client;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;

@SuppressWarnings("unused")
@EventBusSubscriber
public class TetherManager {

    /**
     * DimID, list of active pylons
     * <br>
     * Initialized with empty set on world load and removed on unload
     **/
    public static final Int2ObjectOpenHashMap<SpatialHashGrid<Tether>> ACTIVE_PYLONS = new Int2ObjectOpenHashMap<>();

    /**
     * Used by pylons to determine if a player is connected
     */
    public static final WeakHashMap<EntityPlayer, Tether> PLAYER_TETHERS = new WeakHashMap<>();

    public static final Object2IntArrayMap<BlockPos> PLAYER_RENDER_LINES = new Object2IntArrayMap<>();
    private static final int MAX_LINE_TICKS = 14;
    private static final float MAX_LINE_WIDTH = 13f;

    private final static int BASE_PYLON_RANGE = 16;

    /**
     * MV (2) = 16
     * HV (3) = 32
     * EV (4) = 48
     */
    public static int getRange(int tier, boolean powered) {
        return (int) ((powered ? 1 : 0.5) * (tier - 1) * BASE_PYLON_RANGE);
    }

    public static long getPowerCost(int tier) {
        return GTValues.VP[tier];
    }

    // client clean up
    @SubscribeEvent
    public static void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent ignored) {
        PLAYER_TETHERS.clear();
        ACTIVE_PYLONS.clear();
    }

    // server side
    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        // just in case
        if (event.player instanceof FakePlayer) return;
        PLAYER_TETHERS.remove(event.player);
    }

    // server side
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        // just in case
        if (event.player instanceof FakePlayer) return;
        PLAYER_TETHERS.putIfAbsent(event.player, null);
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        ACTIVE_PYLONS.computeIfAbsent(
            event.world.provider.dimensionId,
            v -> new SpatialHashGrid<>(
                16,
                (vec, tether) -> vec.set(tether.sourceX(), tether.sourceY(), tether.sourceZ())));
    }

    public static void onPlayerChangeDim(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.player instanceof FakePlayer) return;
        PLAYER_TETHERS.replace(event.player, null);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;

        var iter = PLAYER_RENDER_LINES.object2IntEntrySet()
            .iterator();
        while (iter.hasNext()) {
            var entry = iter.next();
            var time = entry.getIntValue();

            time++;
            if (time > MAX_LINE_TICKS) {
                iter.remove();
            } else {
                entry.setValue(time);
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onRenderWorldLast(RenderWorldLastEvent e) {
        if (!Client.render.renderMagLevTethers) return;
        if (PLAYER_RENDER_LINES.isEmpty()) return;
        Entity entity = Minecraft.getMinecraft().renderViewEntity;

        double pX = entity.prevPosX + (entity.posX - entity.prevPosX) * e.partialTicks;
        double pY = entity.prevPosY + (entity.posY - entity.prevPosY) * e.partialTicks;
        double pZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * e.partialTicks;

        GL11.glPushMatrix();
        GL11.glTranslated(-pX, -pY, -pZ);

        var iter = PLAYER_RENDER_LINES.object2IntEntrySet()
            .fastIterator();
        while (iter.hasNext()) {
            var entry = iter.next();
            BlockPos b = entry.getKey();
            int tick = entry.getIntValue();
            renderLineToPlayer(b, pX, pY, pZ, tick, e.partialTicks);
        }

        GL11.glPopMatrix();
    }

    private static void renderLineToPlayer(BlockPos pos, double pX, double pY, double pZ, int renderTick,
        float partialTicks) {
        if (pos == null) return;
        var player = Minecraft.getMinecraft().thePlayer;

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_LIGHTING);
        double progress = (double) renderTick / MAX_LINE_TICKS;
        double timeFactor = (Math.sin(Math.PI * progress) + 1.0) / 2.0;

        GL11.glLineWidth((float) (MAX_LINE_WIDTH * timeFactor));

        final Tessellator tess = Tessellator.instance;
        // todo: quads? not all gpus support lineWidth > 1
        tess.startDrawing(GL11.GL_LINES);
        tess.setColorRGBA(195, 92, 193, (int) (255 * timeFactor));
        tess.addVertex(pX, pY - (player.height / 2), pZ);
        tess.addVertex(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5);
        tess.draw();

        GL11.glPopAttrib();
    }
}

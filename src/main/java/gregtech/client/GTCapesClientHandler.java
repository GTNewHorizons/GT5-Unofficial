package gregtech.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;
import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import cpw.mods.fml.relauncher.Side;
import gregtech.common.config.Client;
import gregtech.mixin.interfaces.accessors.AbstractClientPlayerAccessor;

@EventBusSubscriber(side = Side.CLIENT)
@ParametersAreNonnullByDefault
public class GTCapesClientHandler {

    private static final Map<UUID, ResourceLocation> CAPE_CACHE = new HashMap<>();

    // spotless:off
    // GT + addons
    private static final ResourceLocation CAPE_BRAINTECHCAPE = new ResourceLocation("gregtech:textures/BrainTechCape.png");
    private static final ResourceLocation CAPE_GREGTECHCAPE  = new ResourceLocation("gregtech:textures/GregTechCape.png");
    private static final ResourceLocation CAPE_MRBRAINCAPE   = new ResourceLocation("gregtech:textures/MrBrainCape.png");
    private static final ResourceLocation CAPE_GREGORIUSCAPE = new ResourceLocation("gregtech:textures/GregoriusCape.png");
    private static final ResourceLocation CAPE_DONORCAPE     = new ResourceLocation("gregtech:textures/DonorCape.png");
    private static final ResourceLocation CAPE_DEVCAPE       = new ResourceLocation("gregtech:textures/DevCape.png");
    private static final ResourceLocation CAPE_STEAM         = new ResourceLocation("gregtech:textures/Steam.png");
    private static final ResourceLocation CAPE_TITANIUM      = new ResourceLocation("gregtech:textures/Titanium.png");
    private static final ResourceLocation CAPE_NEUTRONIUM    = new ResourceLocation("gregtech:textures/Neutronium.png");
    private static final ResourceLocation CAPE_STARGATE      = new ResourceLocation("gregtech:textures/Stargate.png");

    // Galacticraft
    private static final ResourceLocation CAPE_BLUE_GC       = new ResourceLocation("galacticraftcore:textures/capes/capeBlue.png");
    private static final ResourceLocation CAPE_BROWN_GC      = new ResourceLocation("galacticraftcore:textures/capes/capeBrown.png");
    private static final ResourceLocation CAPE_CYAN_GC       = new ResourceLocation("galacticraftcore:textures/capes/capeCyan.png");
    private static final ResourceLocation CAPE_DARK_GRAY_GC  = new ResourceLocation("galacticraftcore:textures/capes/capeDarkGray.png");
    private static final ResourceLocation CAPE_DARK_GREEN_GC = new ResourceLocation("galacticraftcore:textures/capes/capeDarkGreen.png");
    private static final ResourceLocation CAPE_LIGHT_BLUE_GC = new ResourceLocation("galacticraftcore:textures/capes/capeLightBlue.png");
    private static final ResourceLocation CAPE_LIGHT_GRAY_GC = new ResourceLocation("galacticraftcore:textures/capes/capeLightGray.png");
    private static final ResourceLocation CAPE_LIME_GC       = new ResourceLocation("galacticraftcore:textures/capes/capeLime.png");
    private static final ResourceLocation CAPE_MAGENTA_GC    = new ResourceLocation("galacticraftcore:textures/capes/capeMagenta.png");
    private static final ResourceLocation CAPE_ORANGE_GC     = new ResourceLocation("galacticraftcore:textures/capes/capeOrange.png");
    private static final ResourceLocation CAPE_PINK_GC       = new ResourceLocation("galacticraftcore:textures/capes/capePink.png");
    private static final ResourceLocation CAPE_PURPLE_GC     = new ResourceLocation("galacticraftcore:textures/capes/capePurple.png");
    private static final ResourceLocation CAPE_RAINBOW_GC    = new ResourceLocation("galacticraftcore:textures/capes/capeRainbow.png");
    private static final ResourceLocation CAPE_RED_GC        = new ResourceLocation("galacticraftcore:textures/capes/capeRed.png");
    private static final ResourceLocation CAPE_YELLOW_GC     = new ResourceLocation("galacticraftcore:textures/capes/capeYellow.png");

    // GalaxySpace
    private static final ResourceLocation CAPE_BLUE_GS       = new ResourceLocation("galaxyspace:textures/capes/capeBlue.png");
    private static final ResourceLocation CAPE_BROWN_GS      = new ResourceLocation("galaxyspace:textures/capes/capeBrown.png");
    private static final ResourceLocation CAPE_CYAN_GS       = new ResourceLocation("galaxyspace:textures/capes/capeCyan.png");
    private static final ResourceLocation CAPE_DARK_GRAY_GS  = new ResourceLocation("galaxyspace:textures/capes/capeDarkGray.png");
    private static final ResourceLocation CAPE_DARK_GREEN_GS = new ResourceLocation("galaxyspace:textures/capes/capeDarkGreen.png");
    private static final ResourceLocation CAPE_DEV_GS        = new ResourceLocation("galaxyspace:textures/capes/capeDev.png");
    private static final ResourceLocation CAPE_GRAYC_GS      = new ResourceLocation("galaxyspace:textures/capes/capeGrayC.png");
    private static final ResourceLocation CAPE_LIGHT_BLUE_GS = new ResourceLocation("galaxyspace:textures/capes/capeLightBlue.png");
    private static final ResourceLocation CAPE_LIGHT_GRAY_GS = new ResourceLocation("galaxyspace:textures/capes/capeLightGray.png");
    private static final ResourceLocation CAPE_LIME_GS       = new ResourceLocation("galaxyspace:textures/capes/capeLime.png");
    private static final ResourceLocation CAPE_MAGENTA_GS    = new ResourceLocation("galaxyspace:textures/capes/capeMagenta.png");
    private static final ResourceLocation CAPE_ORANGE_GS     = new ResourceLocation("galaxyspace:textures/capes/capeOrange.png");
    private static final ResourceLocation CAPE_PINK_GS       = new ResourceLocation("galaxyspace:textures/capes/capePink.png");
    private static final ResourceLocation CAPE_PURPLE_GS     = new ResourceLocation("galaxyspace:textures/capes/capePurple.png");
    private static final ResourceLocation CAPE_RED_GS        = new ResourceLocation("galaxyspace:textures/capes/capeRed.png");
    private static final ResourceLocation CAPE_YELLOW_GS     = new ResourceLocation("galaxyspace:textures/capes/capeYellow.png");
    // spotless:on

    @SubscribeEvent
    public static void onDisconnect(ClientDisconnectionFromServerEvent event) {
        CAPE_CACHE.clear();
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.entity instanceof AbstractClientPlayerAccessor accessor) {
            accessor.gt5u$setCape(CAPE_CACHE.get(event.entity.getUniqueID()));
        }
    }

    public static void setCapes(Map<UUID, String> capes) {
        Minecraft mc = Minecraft.getMinecraft();
        for (Entry<UUID, String> p : capes.entrySet()) {
            UUID uuid = p.getKey();
            String capeName = p.getValue();
            ResourceLocation cape = capeFromString(capeName);
            EntityPlayer player = mc.theWorld.func_152378_a(uuid);
            if (player instanceof AbstractClientPlayerAccessor accessor) {
                // if the player exists in the current world, set the cape directly
                accessor.gt5u$setCape(cape);
            }
            if (player == mc.thePlayer) {
                // the server acknowledged the selected cape
                Client.preference.selectedCape = capeName;
                ConfigurationManager.save(Client.class);
            }
            // cache the ResourceLocation
            CAPE_CACHE.put(uuid, cape);
        }
    }

    public static void printCapes(Collection<String> capes) {
        Minecraft.getMinecraft().thePlayer
            .addChatMessage(new ChatComponentText("Available capes: " + String.join(", ", capes)));
    }

    private static ResourceLocation capeFromString(@Nullable String capeName) {
        return switch (capeName) {
            case "capefriedi" -> CAPE_BRAINTECHCAPE;
            case "capebrain" -> CAPE_MRBRAINCAPE;
            case "capegregoriust" -> CAPE_GREGORIUSCAPE;
            case "capedonor" -> CAPE_DONORCAPE;
            case "capedev" -> CAPE_DEVCAPE;
            case "cape_steam" -> CAPE_STEAM;
            case "cape_titanium" -> CAPE_TITANIUM;
            case "cape_neutronium" -> CAPE_NEUTRONIUM;
            case "cape_stargate" -> CAPE_STARGATE;
            case "cape_gt" -> CAPE_GREGTECHCAPE;

            case "capeBlueGC" -> CAPE_BLUE_GC;
            case "capeBrownGC" -> CAPE_BROWN_GC;
            case "capeCyanGC" -> CAPE_CYAN_GC;
            case "capeDarkGrayGC" -> CAPE_DARK_GRAY_GC;
            case "capeDarkGreenGC" -> CAPE_DARK_GREEN_GC;
            case "capeLightBlueGC" -> CAPE_LIGHT_BLUE_GC;
            case "capeLightGrayGC" -> CAPE_LIGHT_GRAY_GC;
            case "capeLimeGC" -> CAPE_LIME_GC;
            case "capeMagentaGC" -> CAPE_MAGENTA_GC;
            case "capeOrangeGC" -> CAPE_ORANGE_GC;
            case "capePinkGC" -> CAPE_PINK_GC;
            case "capePurpleGC" -> CAPE_PURPLE_GC;
            case "capeRainbowGC" -> CAPE_RAINBOW_GC;
            case "capeRedGC" -> CAPE_RED_GC;
            case "capeYellowGC" -> CAPE_YELLOW_GC;

            case "capeBlueGS" -> CAPE_BLUE_GS;
            case "capeBrownGS" -> CAPE_BROWN_GS;
            case "capeCyanGS" -> CAPE_CYAN_GS;
            case "capeDarkGrayGS" -> CAPE_DARK_GRAY_GS;
            case "capeDarkGreenGS" -> CAPE_DARK_GREEN_GS;
            case "capeDevGS" -> CAPE_DEV_GS;
            case "capeGrayCGS" -> CAPE_GRAYC_GS;
            case "capeLightBlueGS" -> CAPE_LIGHT_BLUE_GS;
            case "capeLightGrayGS" -> CAPE_LIGHT_GRAY_GS;
            case "capeLimeGS" -> CAPE_LIME_GS;
            case "capeMagentaGS" -> CAPE_MAGENTA_GS;
            case "capeOrangeGS" -> CAPE_ORANGE_GS;
            case "capePinkGS" -> CAPE_PINK_GS;
            case "capePurpleGS" -> CAPE_PURPLE_GS;
            case "capeRedGS" -> CAPE_RED_GS;
            case "capeYellowGS" -> CAPE_YELLOW_GS;
            default -> null; // indirectly includes "cape_mc"
        };
    }
}

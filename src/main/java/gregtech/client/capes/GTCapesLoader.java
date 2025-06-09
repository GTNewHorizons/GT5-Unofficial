package gregtech.client.capes;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import gregtech.api.util.GTLog;

public class GTCapesLoader implements Runnable {

    // spotless:off
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
    // spotless:on

    @Override
    public void run() {
        GTLog.out.println("GTMod: Downloading Cape List");
        String GTNH_CAPE_LIST_URL = "https://raw.githubusercontent.com/GTNewHorizons/CustomGTCapeHook-Cape-List/master/capes.txt";
        Map<UUID, ResourceLocation> map = new HashMap<>();
        Pattern pattern = Pattern
            .compile("^([0-9a-z]{8}-?[0-9a-z]{4}-?[0-9a-z]{4}-?[0-9a-z]{4}-?[0-9a-z]{12})(?:$|\\:(cape\\w+).*$)");
        try (final Scanner scanner = new Scanner(new URL(GTNH_CAPE_LIST_URL).openStream())) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine()
                    .toLowerCase();
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    UUID uuid = uuidFromString(matcher.group(1));
                    if (uuid != null) {
                        ResourceLocation cape;
                        if (matcher.groupCount() == 2) {
                            cape = capeFromString(matcher.group(2));
                        } else {
                            cape = capeFromString("");
                        }
                        map.put(uuid, cape);
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace(GTLog.err);
        }
        // add scheduled task on the main thread
        Minecraft.getMinecraft()
            .func_152343_a(() -> {
                if (!map.isEmpty()) {
                    MinecraftForge.EVENT_BUS.register(new GTCapesEventHandler(map));
                }
                GTLog.out.println("GTMod: Loaded " + map.size() + " Capes");
                return null;
            });
    }

    private static UUID uuidFromString(String uuid) {
        if (uuid == null) return null;
        if (uuid.length() == 32) {
            StringBuilder sb = new StringBuilder(uuid);
            sb.insert(8 + 4 + 4 + 4, '-');
            sb.insert(8 + 4 + 4, '-');
            sb.insert(8 + 4, '-');
            sb.insert(8, '-');
            return UUID.fromString(sb.toString());
        }
        if (uuid.length() == 36) {
            return UUID.fromString(uuid);
        }
        return null;
    }

    private static ResourceLocation capeFromString(String capeName) {
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
            default -> CAPE_GREGTECHCAPE;
        };
    }

}

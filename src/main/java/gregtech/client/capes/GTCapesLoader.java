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

    @Override
    public void run() {
        GTLog.out.println("GTMod: Downloading Cape List");
        Map<UUID, ResourceLocation> uuidMap = new HashMap<>();
        Map<String, ResourceLocation> nameMap = new HashMap<>();
        downloadGTNHUUIDCapes(uuidMap);
        downloadGTNHNameCapes(nameMap);
        downloadGregoriusCapes(nameMap);
        addHarcodedCapes(nameMap);
        // add scheduled task on the main thread
        Minecraft.getMinecraft().func_152343_a(() -> {
            if (!uuidMap.isEmpty() || !nameMap.isEmpty()) {
                MinecraftForge.EVENT_BUS.register(new GTCapesEventHandler(uuidMap, nameMap));
            }
            GTLog.out.println("GTMod: Loaded " + (uuidMap.size() + nameMap.size()) + " Capes");
            return null;
        });
    }

    private static void downloadGTNHUUIDCapes(Map<UUID, ResourceLocation> map) {
        /*                                       |------------------------------UUID------------------------------|:capeName(optional)*/
        Pattern pattern = Pattern.compile("^([0-9a-z]{8}-?[0-9a-z]{4}-?[0-9a-z]{4}-?[0-9a-z]{4}-?[0-9a-z]{12})(?:$|\\:(cape\\w+).*$)");
        String url = "https://raw.githubusercontent.com/GTNewHorizons/CustomGTCapeHook-Cape-List/master/capesUUID.txt";
        try (final Scanner scanner = new Scanner(new URL(url).openStream())) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().toLowerCase();
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    UUID uuid = uuidFromString(matcher.group(1));
                    if (uuid != null) {
                        ResourceLocation cape;
                        if (matcher.groupCount() == 2) {
                            cape = capeFromString(matcher.group(2));
                        } else {
                            cape = CAPE_GREGTECHCAPE;
                        }
                        map.put(uuid, cape);
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace(GTLog.err);
        }
    }

    private static void downloadGTNHNameCapes(Map<String, ResourceLocation> map) {
        String url = "https://raw.githubusercontent.com/GTNewHorizons/CustomGTCapeHook-Cape-List/master/capes.txt";
        try (final Scanner scanner = new Scanner(new URL(url).openStream())) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().toLowerCase();
                if (line.contains(":")) {
                    int index = line.indexOf(":");
                    String name = line.substring(0, index);
                    String capeName = line.substring(index);
                    if (!map.containsKey(name)) {
                        map.put(name, capeFromString(capeName));
                    }
                } else {
                    map.put(line, CAPE_GREGTECHCAPE);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace(GTLog.err);
        }
    }

    private static void downloadGregoriusCapes(Map<String, ResourceLocation> nameMap) {
        String url = "http://gregtech.overminddl1.com/com/gregoriust/gregtech/supporterlist.txt";
        try (final Scanner scanner = new Scanner(new URL(url).openStream())) {
            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine().toLowerCase();
                nameMap.put(line, CAPE_GREGTECHCAPE);
            }
        } catch (Throwable e) {
            e.printStackTrace(GTLog.err);
        }
    }
    // spotless:on

    private static void addHarcodedCapes(Map<String, ResourceLocation> map) {
        map.put("friedi4321", CAPE_BRAINTECHCAPE);
        map.put("mr_brain", CAPE_MRBRAINCAPE);
        map.put("gregoriust", CAPE_GREGORIUSCAPE);
        final String[] arr = { "renadi", "hanakocz", "MysteryDump", "Flaver4", "x_Fame", "Peluche321",
            "Goshen_Ithilien", "manf", "Bimgo", "leagris", "IAmMinecrafter02", "Cerous", "Devilin_Pixy", "Bkarlsson87",
            "BadAlchemy", "CaballoCraft", "melanclock", "Resursator", "demanzke", "AndrewAmmerlaan", "Deathlycraft",
            "Jirajha", "Axlegear", "kei_kouma", "Dracion", "dungi", "Dorfschwein", "Zero Tw0", "mattiagraz85",
            "sebastiank30", "Plem", "invultri", "grillo126", "malcanteth", "Malevolence_", "Nicholas_Manuel", "Sirbab",
            "kehaan", "bpgames123", "semig0d", "9000bowser", "Sovereignty89", "Kris1432", "xander_cage_", "samuraijp",
            "bsaa", "SpwnX", "tworf", "Kadah", "kanni", "Stute", "Hegik", "Onlyme", "t3hero", "Hotchi", "jagoly",
            "Nullav", "BH5432", "Sibmer", "inceee", "foxxx0", "Hartok", "TMSama", "Shlnen", "Carsso", "zessirb",
            "meep310", "Seldron", "yttr1um", "hohounk", "freebug", "Sylphio", "jmarler", "Saberawr", "r00teniy",
            "Neonbeta", "yinscape", "voooon24", "Quintine", "peach774", "lepthymo", "bildeman", "Kremnari", "Aerosalo",
            "OndraSter", "oscares91", "mr10movie", "Daxx367x2", "EGERTRONx", "aka13_404", "Abouttabs", "Johnstaal",
            "djshiny99", "megatronp", "DZCreeper", "Kane_Hart", "Truculent", "vidplace7", "simon6689", "MomoNasty",
            "UnknownXLV", "goreacraft", "Fluttermine", "Daddy_Cecil", "MrMaleficus", "TigersFangs", "cublikefoot",
            "chainman564", "NikitaBuker", "Misha999777", "25FiveDetail", "AntiCivilBoy", "michaelbrady",
            "xXxIceFirexXx", "Speedynutty68", "GarretSidzaka", "HallowCharm977", "mastermind1919", "The_Hypersonic",
            "diamondguy2798", "zF4ll3nPr3d4t0r", "CrafterOfMines57", "XxELIT3xSNIP3RxX", "SuterusuKusanagi",
            "xavier0014", "adamros", "alexbegt" };
        for (String name : arr) {
            map.put(name.toLowerCase(), CAPE_GREGTECHCAPE);
        }
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

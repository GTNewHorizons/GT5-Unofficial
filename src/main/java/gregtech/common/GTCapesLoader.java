package gregtech.common;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.StringUtils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerDisconnectionFromClientEvent;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.net.cape.GTPacketBroadcastCapes;
import gregtech.api.util.GTLog;

@EventBusSubscriber
@ParametersAreNonnullByDefault
public class GTCapesLoader implements Runnable {

    private static final Multimap<UUID, String> AVAILABLE_CAPES_UUID = HashMultimap.create();
    private static final Multimap<String, String> AVAILABLE_CAPES_NAME = HashMultimap.create();
    private static final Map<UUID, String> SELECTED_CAPES = new HashMap<>();

    @Override
    public void run() {
        GTLog.out.println("GTMod: Downloading Cape List");
        downloadGTNHUUIDCapes();
        downloadGregoriusCapes();
        if (Mods.GalacticraftCore.isModLoaded()) {
            downloadGalacticraftCapes();
        }
        if (Mods.GalaxySpace.isModLoaded()) {
            downloadGalaxySpaceCapes();
        }
        addHarcodedCapes();
    }

    @SubscribeEvent
    public static void onClientConnect(ServerConnectionFromClientEvent event) {
        if (event.handler instanceof NetHandlerPlayServer handlerPlayServer) {
            GTValues.NW.sendToPlayer(new GTPacketBroadcastCapes(SELECTED_CAPES), handlerPlayServer.playerEntity);
        }
    }

    @SubscribeEvent
    public static void onClientDisconnect(ServerDisconnectionFromClientEvent event) {
        if (event.handler instanceof NetHandlerPlayServer handlerPlayServer) {
            SELECTED_CAPES.remove(handlerPlayServer.playerEntity.getUniqueID());
        }
    }

    public static void setSelectedCape(EntityPlayer player, String cape) {
        SELECTED_CAPES.put(player.getUniqueID(), cape);
    }

    public static void clearSelectedCapes() {
        SELECTED_CAPES.clear();
    }

    public static Collection<String> getAvailableCapes(@Nullable EntityPlayer player) {
        if (player == null) {
            return Collections.emptySet();
        }

        UUID uuid = player.getUniqueID();
        Collection<String> capes = new HashSet<>();
        capes.add("cape_mc");
        capes.addAll(AVAILABLE_CAPES_UUID.get(uuid));
        capes.addAll(
            AVAILABLE_CAPES_NAME.get(
                player.getCommandSenderName()
                    .toLowerCase(Locale.ROOT)));
        return capes;
    }

    private static void downloadGTNHUUIDCapes() {
        // spotless:off
        //                                       |---------------------------------------UUID--------------------------------------|:capeName(optional)
        Pattern pattern = Pattern.compile("^([0-9a-fA-F]{8}-?[0-9a-fA-F]{4}-?[0-9a-fA-F]{4}-?[0-9a-fA-F]{4}-?[0-9a-fA-F]{12})(?:$|\\:(cape\\w+).*$)");
        // spotless:on
        String url = "https://raw.githubusercontent.com/GTNewHorizons/CustomGTCapeHook-Cape-List/master/capesUUID.txt";
        try (final Scanner scanner = new Scanner(new URL(url).openStream())) {
            while (scanner.hasNextLine()) {
                Matcher matcher = pattern.matcher(scanner.nextLine());
                if (matcher.find()) {
                    if (matcher.groupCount() == 2) {
                        putUUID(matcher.group(1), matcher.group(2));
                    } else {
                        putUUID(matcher.group(1));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(GTLog.err);
        }
    }

    private static void downloadGregoriusCapes() {
        String url = "https://gregtech.overminddl1.com/com/gregoriust/gregtech/supporterlist.txt";
        try (final Scanner scanner = new Scanner(new URL(url).openStream())) {
            while (scanner.hasNextLine()) {
                putName(scanner.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace(GTLog.err);
        }
    }

    private static void downloadGalacticraftCapes() {
        String url = "https://raw.github.com/micdoodle8/Galacticraft/master/capes.txt";
        try (final Scanner scanner = new Scanner(new URL(url).openStream())) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    putName(parts[0], parts[1] + "GC");
                } else {
                    GTLog.err.println("Invalid cape mapping: " + line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(GTLog.err);
        }
    }

    private static void downloadGalaxySpaceCapes() {
        String url = "https://demigods.at.ua/capes.txt";
        try (final Scanner scanner = new Scanner(new URL(url).openStream())) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    putName(parts[0], parts[1] + "GS");
                } else {
                    GTLog.err.println("Invalid cape mapping: " + line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(GTLog.err);
        }
    }

    private static void addHarcodedCapes() {
        putBoth("friedi4321", "15552162561244a9a748c8d71a7d180d", "capefriedi");
        putBoth("mr_brain", "10833ef6956d4dc1a10a46b3d07ec4d8", "capebrain");
        putBoth("gregoriust", "68af6d40a79e4a2998469d4a319ec774", "capegregoriust");
        putBoth("renadi", "fc363256d8bd44cda3167aaf399ea9b8");
        putBoth("hanakocz", "03f9ec5b90e943c0a83cb0ba433e46c4");
        putBoth("MysteryDump", "11c7a3235f5d4203b04425964f5f2e93");
        putName("Flaver4"); // name changed or account deleted -> UUID unknown
        putName("x_Fame"); // name changed or account deleted -> UUID unknown
        putName("Peluche321"); // name changed or account deleted -> UUID unknown
        putBoth("Goshen_Ithilien", "f71dc90f789c44879ab00627c4fbd68e");
        putBoth("manf", "0975b0b9ee7b4e179fb8848c99931987");
        putBoth("Bimgo", "24f84f5106ab40fc8843c649e5d28e35");
        putBoth("leagris", "09752aa38b9c4f8fb04f5421e799547d");
        putName("IAmMinecrafter02"); // name changed or account deleted -> UUID unknown
        putBoth("Cerous", "b2414f04725547549bd424ee15c5b9ee");
        putBoth("Devilin_Pixy", "2a5b9ab7c42440aaa2ebd4405e225a6a");
        putName("Bkarlsson87"); // name changed or account deleted -> UUID unknown
        putBoth("BadAlchemy", "1779b561840d4c30ae2b561dca1bd388");
        putBoth("CaballoCraft", "06479e4cd4c544379b67f665ae44fb67");
        putBoth("melanclock", "3d03bdd274a34d32b8d1609e55634cf1");
        putBoth("Resursator", "c973398685874a1eaad3ed5403043f58");
        putBoth("demanzke", "b076081584f846de8ce89f844d3da330");
        putName("AndrewAmmerlaan"); // name changed or account deleted -> UUID unknown
        putBoth("Deathlycraft", "0699ccfe0dde4c68b481c6a3f418baff");
        putBoth("Jirajha", "910ec6c4b7cd411eb562bad1c164ba0e");
        putBoth("Axlegear", "221812767a2641398a6b526242adf668");
        putBoth("kei_kouma", "9778b73ec456401ebdba38318535140f");
        putBoth("Dracion", "d05d7477c4e44e8ea08254609fdee37e");
        putBoth("dungi", "af117d8ccab940b1a31b9f5b24f5c960");
        putBoth("Dorfschwein", "fe18f13ee5a542f3915b6691461abb8c");
        putBoth("Zero_Tw0", "59639f87621d429795cb3ff27affd999"); // originally "Zero Tw0", which does not exist
        putName("mattiagraz85"); // name changed or account deleted -> UUID unknown
        putBoth("sebastiank30", "fd0f5c1ce3704ec0b79c82632a91e19e");
        putBoth("Plem", "e1e15b6556254ad99fbe262a4ca2c28c");
        putBoth("invultri", "6208249874634eef9c13f949a8e129f5");
        putBoth("grillo126", "08929089bffa4b9088c993804b4a966d");
        putName("malcanteth"); // name changed or account deleted -> UUID unknown
        putBoth("Malevolence_", "794db9e178124537b928c64e589acf24");
        putName("Nicholas_Manuel"); // name changed or account deleted -> UUID unknown
        putName("Sirbab"); // name changed or account deleted -> UUID unknown
        putBoth("kehaan", "2cea0fadf8934ea4ae97a7b98cb104a6");
        putName("bpgames123"); // name changed or account deleted -> UUID unknown
        putBoth("semig0d", "5a84bc8e42464c50920fa9b37504a798");
        putName("9000bowser"); // name changed or account deleted -> UUID unknown
        putBoth("Sovereignty89", "c5cce1d9397e4fed9a88979625097bdb");
        putBoth("Kris1432", "12cad9638dca4540b00207f3039cc5a7");
        putName("xander_cage_"); // name changed or account deleted -> UUID unknown
        putBoth("samuraijp", "e00d0e7f8b534e98922959ad61b71e2b");
        putBoth("bsaa", "05b7fd136bb54a57b6a1ff67eb83ef23");
        putBoth("SpwnX", "e7778d79c85447d8b16169664c0bf444");
        putBoth("tworf", "c440c5aea3984d57b32d561201c34603");
        putBoth("Kadah", "029192bf53fa4bebb91ea2442165b2b2");
        putBoth("kanni", "f1a1318dff0143f1ae81c7cfd0eda099");
        putBoth("Stute", "21b71568d9e94a73bf2d8918bf315edd");
        putBoth("Hegik", "309c38bfc778446387be3c2d63e379cc");
        putBoth("Onlyme", "a2c622fc10974ea9ad2652cd2857acd7");
        putBoth("t3hero", "2016b5650a634a2d800bb786ac256288");
        putBoth("Hotchi", "8064e7a53d67473a9cb4750937929d55");
        putBoth("jagoly", "121519fb74114e05a821bb1d7ca01a48");
        putBoth("Nullav", "3022ddd44e6040ad818d079bb8fe66ed");
        putBoth("BH5432", "2d7d76ed7acb460184ede035405dbcaf");
        putBoth("Sibmer", "8e887e22411942d1926569e688db0ad2");
        putBoth("inceee", "24630ce4ff704dff867bba0e71b48940");
        putBoth("foxxx0", "3043bce6c15f4318a9df679dda391d63");
        putBoth("Hartok", "6b6710a2446747aeba02ae9e80e9d64e");
        putBoth("TMSama", "efda63851ad64121b814dcc6c89a268e");
        putName("Shlnen"); // name changed or account deleted -> UUID unknown
        putBoth("Carsso", "7287350967624aa9949416886064a4c1");
        putBoth("zessirb", "6a161ea2c7f84fd5b47e2de4e960cdbb");
        putBoth("meep310", "014cbd05be5b450daa5197bd9f9f91cc");
        putBoth("Seldron", "dbc325fc3fdd4cd3b6f545aed8b4e51f");
        putBoth("yttr1um", "f17a78f2f78c4987a62d51e49a7ab147");
        putBoth("hohounk", "c5d25043dc334538892c1c70ce03134c");
        putBoth("freebug", "ac70160404f54210ab004e342a99f2a2");
        putBoth("Sylphio", "0f8cdb5cada1481aaf696a62c4093666");
        putBoth("jmarler", "9c4c9812253446dea7da2135e88f0a30");
        putName("Saberawr"); // name changed or account deleted -> UUID unknown
        putBoth("r00teniy", "8d302f192584486b94aa6547a385df26");
        putBoth("Neonbeta", "20829ffa91094154bb3b4cd39c07cd62");
        putBoth("yinscape", "4e5087038fd74224b56c7e2d1194f963");
        putBoth("voooon24", "2c1ca510ab7c45f680ac2a18874084a5");
        putBoth("Quintine", "86fa9a01066f4186907a34e0f8638e29");
        putBoth("peach774", "cd056ffabc124556b9f62bf6166183c2");
        putBoth("lepthymo", "fd64a24674fa4bfdb1e60058939d362b");
        putName("bildeman"); // name changed or account deleted -> UUID unknown
        putBoth("Kremnari", "5d51a232c5864697a1f735f4501f3797");
        putBoth("Aerosalo", "69d489a9fd174de3bb73dc8add7b0c2a");
        putName("mr10movie"); // name changed or account deleted -> UUID unknown
        putName("OndraSter"); // name changed or account deleted -> UUID unknown
        putBoth("oscares91", "2c04fdcdad6f465399947d95f2c8dd5f");
        putName("Daxx367x2"); // name changed or account deleted -> UUID unknown
        putBoth("EGERTRONx", "91171de3db7044f4b8af55b1e3f5fc99");
        putBoth("aka13_404", "f6d247253ea4409ba3fc6fc55767d392");
        putBoth("Abouttabs", "305cbb19345c46f3ad460f908c8a0b12");
        putBoth("Johnstaal", "5473c1654b7a43e8bed82bc71ac2b2f4");
        putName("djshiny99"); // name changed or account deleted -> UUID unknown
        putName("megatronp"); // name changed or account deleted -> UUID unknown
        putBoth("DZCreeper", "8d04e5dfe6a14c4981d047f32c219d1c");
        putBoth("Kane_Hart", "88d1fcdc0b18442ab78f6fa25acabb97");
        putBoth("Truculent", "dae28db1649b4e4cb88efed0cdc32917");
        putBoth("vidplace7", "ce7fb761bfa746789476827587a931cd");
        putBoth("simon6689", "0c0cb210d7fa4b9fb2ccc3a59634a865");
        putBoth("MomoNasty", "f0389907f88443f7a2df3d03bcab6232");
        putBoth("UnknownXLV", "aae7b38be491442497855de04397f45c");
        putBoth("goreacraft", "3172f7495c784005965523dcddec845d");
        putName("Fluttermine"); // name changed or account deleted -> UUID unknown
        putBoth("Daddy_Cecil", "6e91ba4128a14316a5b6348eb51d5dea");
        putBoth("MrMaleficus", "7e76edc9299d4556a4855879015bebc2");
        putBoth("TigersFangs", "639d699489d84282bc0152c6d7a204e8");
        putBoth("cublikefoot", "b03d96563bfc43168bf120342fa75f5b");
        putBoth("chainman564", "b301495d4d27419085047481b1b238bb");
        putBoth("NikitaBuker", "c6716b9a9ffc4ee18dc1d56cf10066df");
        putBoth("Misha999777", "a6f8c815444b4caea0e10eaf3a0fc199");
        putBoth("25FiveDetail", "a41b475064874091a7fefd55c8ee2f5f");
        putName("AntiCivilBoy"); // name changed or account deleted -> UUID unknown
        putBoth("michaelbrady", "573b94e7cd6d42a691c5e3757c434eec");
        putName("xXxIceFirexXx"); // name changed or account deleted -> UUID unknown
        putName("Speedynutty68"); // name changed or account deleted -> UUID unknown
        putBoth("GarretSidzaka", "dd107a4c1cab4b4baaa456438e3c92f7");
        putBoth("HallowCharm977", "4e2a8683aed84d53a6330ff0da2cbe4d");
        putBoth("mastermind1919", "c83e6e8e76974448909137f399ed1f43");
        putBoth("The_Hypersonic", "defa4366812c4c72a4fe4de6afa38852");
        putBoth("diamondguy2798", "03c8ced8cb52486ea2a0ea4929b07ef6");
        putBoth("zF4ll3nPr3d4t0r", "7ed0a7db9d4b4528a78269175839a187");
        putName("CrafterOfMines57"); // name changed or account deleted -> UUID unknown
        putName("XxELIT3xSNIP3RxX"); // name changed or account deleted -> UUID unknown
        putName("SuterusuKusanagi"); // name changed or account deleted -> UUID unknown
        putBoth("xavier0014", "42aa6765a6084d28aca3d6d6027bd4ca");
        putBoth("adamros", "cf299609ac554d9d8803f136b87193e9");
        putBoth("alexbegt", "e45e3c6b131f47718100f501bd3b675d");
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

    private static void putBoth(String name, String uuidString, String cape) {
        putUUID(uuidString, cape);
        putName(name, cape);
    }

    private static void putBoth(String name, String uuidString) {
        putUUID(uuidString);
        putName(name);
    }

    private static void putUUID(String uuidString) {
        putUUID(uuidString, "cape_gt");
    }

    private static void putUUID(String uuidString, String cape) {
        UUID uuid = uuidFromString(uuidString);
        if (uuid != null) {
            AVAILABLE_CAPES_UUID.put(uuid, cape);
        }
    }

    private static void putName(String name) {
        putName(name, "cape_gt");
    }

    private static void putName(String name, String cape) {
        if (!StringUtils.isNullOrEmpty(name)) {
            AVAILABLE_CAPES_NAME.put(name.toLowerCase(Locale.ROOT), cape);
        }
    }
}

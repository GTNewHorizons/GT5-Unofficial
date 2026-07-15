package gregtech.common.misc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import com.gtnewhorizon.gtnhlib.util.data.ImmutableBlockMeta;

import gregtech.api.enums.OreMixes;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.util.GTUtility;
import gregtech.common.OreMixBuilder;
import gregtech.common.ores.IOreAdapter;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;

final class OreShowroom {

    private static final int PAD_WIDTH = 5;
    private static final int AISLE_GAP = 5;
    private static final int AISLE_PITCH = PAD_WIDTH + AISLE_GAP;
    private static final int DISPLAY_HEIGHT = 4;
    private static final int PLAYER_OFFSET = 5;
    private static final int SIGN_LINE_LENGTH = 15;
    private static final String SIGN_ARROW = "          ->";

    private OreShowroom() {}

    static void process(EntityPlayerMP player, String[] args) {
        List<Aisle> aisles = collectAisles();
        if (aisles.isEmpty()) {
            tell(player, EnumChatFormatting.RED + "No registered ore showroom displays were found.");
            return;
        }

        int floorY = MathHelper.floor_double(player.posY) - 1;
        if (floorY < 0 || floorY + DISPLAY_HEIGHT >= player.worldObj.getActualHeight()) {
            tell(player, EnumChatFormatting.RED + "There is not enough vertical room at this Y level.");
            return;
        }

        if (args.length != 2 || !"confirm".equalsIgnoreCase(args[1])) {
            int displayCount = aisles.stream()
                .map(Aisle::materials)
                .mapToInt(List::size)
                .sum();
            int maxLength = aisles.stream()
                .map(Aisle::materials)
                .mapToInt(materials -> materials.size() * 2 + 1)
                .max()
                .orElse(0);
            int width = aisles.size() * PAD_WIDTH + (aisles.size() - 1) * AISLE_GAP;
            int originX = MathHelper.floor_double(player.posX) + PLAYER_OFFSET;
            int playerZ = MathHelper.floor_double(player.posZ);
            int originZ = centeredOriginZ(playerZ, aisles.size());

            tell(
                player,
                EnumChatFormatting.YELLOW + "Ore showroom: "
                    + aisles.size()
                    + " aisles, "
                    + displayCount
                    + " displays, up to "
                    + (maxLength + 2)
                    + " x "
                    + width
                    + " blocks.");
            tell(
                player,
                EnumChatFormatting.YELLOW + "It starts at "
                    + originX
                    + ", "
                    + floorY
                    + ", "
                    + originZ
                    + "; the aisles are centered around your Z position and build toward +X. The StoneType pads will be replaced and the entire four-block-high footprint cleared.");
            tell(player, EnumChatFormatting.GOLD + "Run /gt ore-showroom confirm without moving to generate it.");
            return;
        }

        generate(
            player,
            aisles,
            MathHelper.floor_double(player.posX) + PLAYER_OFFSET,
            floorY,
            centeredOriginZ(MathHelper.floor_double(player.posZ), aisles.size()));
    }

    private static List<Aisle> collectAisles() {
        Set<IOreMaterial> materialSet = new HashSet<>();
        for (OreMixes mix : OreMixes.VALUES) {
            OreMixBuilder builder = mix.oreMixBuilder;
            materialSet.add(builder.primary);
            materialSet.add(builder.secondary);
            materialSet.add(builder.between);
            materialSet.add(builder.sporadic);
        }
        materialSet.remove(null);

        List<IOreMaterial> materials = new ArrayList<>(materialSet);
        materials.sort(
            Comparator.comparing(IOreMaterial::getDefaultLocalName, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(IOreMaterial::getInternalName)
                .thenComparingInt(IOreMaterial::getId));

        Map<StoneType, List<IOreMaterial>> unsorted = new EnumMap<>(StoneType.class);
        for (IOreMaterial material : materials) {
            for (StoneType stoneType : OreMixes.getStoneTypesFromMixes(material)) {
                if (stoneType.isEnabled() && getExactOreBlock(material, stoneType, false) != null) {
                    unsorted.computeIfAbsent(stoneType, ignored -> new ArrayList<>())
                        .add(material);
                }
            }
        }

        List<StoneType> stoneTypes = new ArrayList<>(unsorted.keySet());
        stoneTypes.sort(Comparator.comparing(OreShowroom::getAisleName, String.CASE_INSENSITIVE_ORDER));

        List<Aisle> aisles = new ArrayList<>();
        for (StoneType stoneType : stoneTypes) {
            List<IOreMaterial> aisleMaterials = unsorted.get(stoneType);
            if (stoneType == StoneType.Stone && aisleMaterials.size() > 1) {
                int split = (aisleMaterials.size() + 1) / 2;
                aisles.add(new Aisle(stoneType, "Stone 1/2", new ArrayList<>(aisleMaterials.subList(0, split))));
                aisles.add(
                    new Aisle(
                        stoneType,
                        "Stone 2/2",
                        new ArrayList<>(aisleMaterials.subList(split, aisleMaterials.size()))));
            } else {
                aisles.add(new Aisle(stoneType, getAisleName(stoneType), aisleMaterials));
            }
        }
        return aisles;
    }

    private static void generate(EntityPlayerMP player, List<Aisle> aisles, int originX, int floorY, int originZ) {
        World world = player.worldObj;
        int missingSmallOres = 0;
        int missingRawOres = 0;
        int failedFrames = 0;
        int aisleIndex = 0;

        tell(player, EnumChatFormatting.YELLOW + "Generating ore showroom...");

        int maxLength = aisles.stream()
            .map(Aisle::materials)
            .mapToInt(materials -> materials.size() * 2 + 1)
            .max()
            .orElse(0);
        int width = aisles.size() * PAD_WIDTH + (aisles.size() - 1) * AISLE_GAP;
        clearShowroom(world, originX - 1, floorY, originZ, maxLength + 2, width);

        // ponytail: this is synchronous for a one-shot dev world; batch by server tick if watchdogs become a problem.
        for (Aisle aisle : aisles) {
            StoneType stoneType = aisle.stoneType();
            List<IOreMaterial> materials = aisle.materials();
            ImmutableBlockMeta stone = stoneType.getStone();
            int wallZ = originZ + 2 + aisleIndex * AISLE_PITCH;
            int length = materials.size() * 2 + 1;

            preparePad(world, stone, originX, floorY, wallZ, length);

            for (int x = originX; x < originX + length; x++) {
                setBlock(world, x, floorY + DISPLAY_HEIGHT, wallZ, stone);
            }

            for (int displayIndex = 0; displayIndex < materials.size(); displayIndex++) {
                IOreMaterial material = materials.get(displayIndex);
                int separatorX = originX + displayIndex * 2;
                int sampleX = separatorX + 1;
                ImmutableBlockMeta regularOre = getExactOreBlock(material, stoneType, false);
                ImmutableBlockMeta smallOre = getExactOreBlock(material, stoneType, true);

                for (int y = floorY + 1; y <= floorY + DISPLAY_HEIGHT; y++) {
                    setBlock(world, separatorX, y, wallZ, stone);
                }
                setBlock(world, sampleX, floorY + 1, wallZ, regularOre);
                setBlock(world, sampleX, floorY + 2, wallZ, smallOre == null ? stone : smallOre);
                setBlock(world, sampleX, floorY + 3, wallZ, stone);
                setBlock(world, sampleX, floorY, wallZ - 1, regularOre);
                setBlock(world, sampleX, floorY, wallZ + 1, regularOre);

                String[] materialSign = wrapSignText(material.getDefaultLocalName(), true);
                placeSign(world, separatorX + 2, floorY + 2, wallZ - 1, 2, materialSign);
                placeSign(world, separatorX, floorY + 2, wallZ + 1, 3, materialSign);

                if (smallOre == null) missingSmallOres++;

                ItemStack rawOre = material.getPart(OrePrefixes.rawOre, 1);
                if (GTUtility.isStackValid(rawOre)) {
                    if (!placeFrame(world, sampleX, floorY + 3, wallZ, 2, rawOre)) failedFrames++;
                    if (!placeFrame(world, sampleX, floorY + 3, wallZ, 0, rawOre)) failedFrames++;
                } else {
                    missingRawOres++;
                }
            }

            int lastX = originX + length - 1;
            for (int y = floorY + 1; y <= floorY + DISPLAY_HEIGHT; y++) {
                setBlock(world, lastX, y, wallZ, stone);
            }
            placeEndcapSigns(world, originX, lastX, floorY + 3, wallZ, aisle.name());
            aisleIndex++;
        }

        int displayCount = aisles.stream()
            .map(Aisle::materials)
            .mapToInt(List::size)
            .sum();
        tell(
            player,
            EnumChatFormatting.GREEN + "Generated " + aisles.size() + " aisles and " + displayCount + " displays.");
        if (missingSmallOres > 0 || missingRawOres > 0) {
            tell(
                player,
                EnumChatFormatting.YELLOW + "Used StoneType fillers for "
                    + missingSmallOres
                    + " missing small ores; omitted "
                    + missingRawOres
                    + " missing raw-ore frames.");
        }
        if (failedFrames > 0) {
            tell(
                player,
                EnumChatFormatting.RED + "Could not place "
                    + failedFrames
                    + " item frames, usually because another entity occupies the spot.");
        }
    }

    private static void preparePad(World world, ImmutableBlockMeta stone, int originX, int floorY, int wallZ,
        int length) {
        for (int x = originX; x < originX + length; x++) {
            for (int z = wallZ - 2; z <= wallZ + 2; z++) {
                setBlock(world, x, floorY, z, stone);
            }
        }
    }

    private static void clearShowroom(World world, int originX, int floorY, int originZ, int length, int width) {
        for (int x = originX; x < originX + length; x++) {
            for (int z = originZ; z < originZ + width; z++) {
                for (int y = floorY + 1; y <= floorY + DISPLAY_HEIGHT; y++) {
                    if (!world.isAirBlock(x, y, z)) world.setBlock(x, y, z, Blocks.air, 0, 2);
                }
            }
        }
    }

    private static ImmutableBlockMeta getExactOreBlock(IOreMaterial material, StoneType stoneType, boolean small) {
        try (OreInfo<IOreMaterial> requested = OreInfo.getNewInfo()) {
            requested.material = material;
            requested.stoneType = stoneType;
            requested.isSmall = small;
            requested.isNatural = true;

            IOreAdapter<?> adapter = OreManager.getAdapter(requested);
            if (adapter == null) return null;

            ImmutableBlockMeta block = adapter.getBlock(requested);
            if (block == null) return null;

            try (OreInfo<IOreMaterial> actual = OreManager.getOreInfo(block.getBlock(), block.getBlockMeta())) {
                if (actual == null || !material.equals(actual.material)
                    || actual.stoneType != stoneType
                    || actual.isSmall != small) {
                    return null;
                }
            }
            return block;
        }
    }

    private static void setBlock(World world, int x, int y, int z, ImmutableBlockMeta block) {
        int metadata = block.getBlockMeta();
        if (metadata == OreDictionary.WILDCARD_VALUE) metadata = 0;
        world.setBlock(x, y, z, block.getBlock(), metadata, 2);
    }

    private static void placeSign(World world, int x, int y, int z, int metadata, String[] lines) {
        world.setBlock(x, y, z, Blocks.wall_sign, metadata, 2);
        TileEntity tile = world.getTileEntity(x, y, z);
        if (!(tile instanceof TileEntitySign sign)) return;

        System.arraycopy(lines, 0, sign.signText, 0, sign.signText.length);
        sign.markDirty();
        world.markBlockForUpdate(x, y, z);
    }

    private static boolean placeFrame(World world, int x, int y, int z, int direction, ItemStack displayedItem) {
        EntityItemFrame frame = new EntityItemFrame(world, x, y, z, direction);
        frame.setDisplayedItem(displayedItem.copy());
        return frame.onValidSurface() && world.spawnEntityInWorld(frame);
    }

    private static void placeEndcapSigns(World world, int firstX, int lastX, int y, int wallZ, String aisleName) {
        String[] lines = wrapSignText(aisleName, false);
        placeSign(world, firstX - 1, y, wallZ, 4, lines);
        placeSign(world, lastX + 1, y, wallZ, 5, lines);
        placeSign(world, firstX, y, wallZ - 1, 2, lines);
        placeSign(world, firstX, y, wallZ + 1, 3, lines);
        placeSign(world, lastX, y, wallZ - 1, 2, lines);
        placeSign(world, lastX, y, wallZ + 1, 3, lines);
    }

    private static String getAisleName(StoneType stoneType) {
        String name = stoneType.getPrefix()
            .getDefaultLocalName();
        if ("Ores".equals(name)) return "Stone";
        return name.endsWith(" Ores") ? name.substring(0, name.length() - " Ores".length()) : name;
    }

    static int centeredOriginZ(int playerZ, int aisleCount) {
        int rightSideStart = playerZ + AISLE_GAP / 2 + 1;
        return rightSideStart - aisleCount / 2 * AISLE_PITCH;
    }

    static String[] wrapSignText(String text, boolean includeArrow) {
        String[] lines = { "", "", "", includeArrow ? SIGN_ARROW : "" };
        String remaining = text.trim();
        int textLineCount = includeArrow ? 3 : 4;

        for (int line = 0; line < textLineCount && !remaining.isEmpty(); line++) {
            if (remaining.length() <= SIGN_LINE_LENGTH) {
                lines[line] = remaining;
                break;
            }
            if (line == textLineCount - 1) {
                lines[line] = remaining.substring(0, SIGN_LINE_LENGTH - 3) + "...";
                break;
            }

            int split = remaining.lastIndexOf(' ', SIGN_LINE_LENGTH);
            if (split <= 0) split = SIGN_LINE_LENGTH;
            lines[line] = remaining.substring(0, split)
                .trim();
            remaining = remaining.substring(split)
                .trim();
        }
        return lines;
    }

    private static void tell(EntityPlayerMP player, String message) {
        player.addChatMessage(new ChatComponentText(message));
    }

    private record Aisle(StoneType stoneType, String name, List<IOreMaterial> materials) {}
}

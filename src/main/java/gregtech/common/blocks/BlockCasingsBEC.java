package gregtech.common.blocks;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.translatedText;
import static gregtech.api.util.GTMathUtils.vec;
import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import org.joml.Vector3i;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;

public class BlockCasingsBEC extends BlockCasingsAbstract {

    public BlockCasingsBEC() {
        super(ItemCasings.class, "gt.blockcasings.bec", MaterialCasings.INSTANCE, 16);

        register(0, ItemList.SuperconductivePlasmaEnergyConduit, translatedText("gt.blockcasings.bec.0.tooltip"));
        register(1, ItemList.ElectromagneticallyIsolatedCasing, translatedText("gt.blockcasings.bec.1.tooltip"));
        register(2, ItemList.FineStructureConstantManipulator, translatedText("gt.blockcasings.bec.2.tooltip"));
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (16 << 7) | (aMeta + 112);
    }

    @Override
    public int getRenderType() {
        // Don't use the casing ISBRH, render as a normal block
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        int meta = world.getBlockMetadata(x, y, z);

        if (meta == 8) {
            return getCTMIcon(world, x, y, z, ForgeDirection.getOrientation(side));
        } else {
            return getIcon(side, meta);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return switch (meta) {
            case 0 -> Textures.BlockIcons.BEC_CONDUIT.getIcon();
            case 1 -> Textures.BlockIcons.BEC_CASING.getIcon();
            case 2 -> Textures.BlockIcons.BEC_MANIPULATOR.getIcon();
            default -> Minecraft.getMinecraft()
                .getTextureMapBlocks()
                .getAtlasSprite("missingno");
        };
    }

    private enum IconType {
        NONE,
        NORMAL,
        ROTATED
    }

    private static final IconType[][] ICONS = { { // facing_x
        IconType.NORMAL, // down
        IconType.NORMAL, // up
        IconType.NORMAL, // north
        IconType.NORMAL, // south
        IconType.NONE, // west
        IconType.NONE, // east
        }, { // facing_y
            IconType.NONE, // down
            IconType.NONE, // up
            IconType.ROTATED, // north
            IconType.ROTATED, // south
            IconType.ROTATED, // west
            IconType.ROTATED, // east
        }, { // facing_z
            IconType.ROTATED, // down
            IconType.ROTATED, // up
            IconType.NONE, // north
            IconType.NONE, // south
            IconType.NORMAL, // west
            IconType.NORMAL, // east
        }, { // plane_x
            IconType.ROTATED, // down
            IconType.ROTATED, // up
            IconType.ROTATED, // north
            IconType.ROTATED, // south
            IconType.NONE, // west
            IconType.NONE, // east
        }, { // plane_y
            IconType.NONE, // down
            IconType.NONE, // up
            IconType.NORMAL, // north
            IconType.NORMAL, // south
            IconType.NORMAL, // west
            IconType.NORMAL, // east
        }, { // plane_z
            IconType.NORMAL, // down
            IconType.NORMAL, // up
            IconType.NONE, // north
            IconType.NONE, // south
            IconType.ROTATED, // west
            IconType.ROTATED, // east
        }, };

    @SideOnly(Side.CLIENT)
    private IIcon getCTMIcon(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        Orientation orientation = getConduitOrientation(world, x, y, z);

        return switch (ICONS[orientation.ordinal()][side.ordinal()]) {
            case NONE -> Textures.BlockIcons.BEC_CONDUIT_BLANK.getIcon();
            case NORMAL -> Textures.BlockIcons.BEC_CONDUIT.getIcon();
            case ROTATED -> Textures.BlockIcons.BEC_CONDUIT_90.getIcon();
        };
    }

    private static final ForgeDirection[] HALF = { SOUTH, UP, EAST };

    private enum Orientation {

        FACING_X,
        FACING_Y,
        FACING_Z,
        PLANE_X,
        PLANE_Y,
        PLANE_Z;

        public static Orientation facing(ForgeDirection dir) {
            return switch (dir) {
                case DOWN, UP -> FACING_Y;
                case NORTH, SOUTH -> FACING_Z;
                case EAST, WEST -> FACING_X;
                case UNKNOWN -> null;
            };
        }

        public static Orientation plane(ForgeDirection normal) {
            return switch (normal) {
                case DOWN, UP -> PLANE_Y;
                case NORTH, SOUTH -> PLANE_Z;
                case EAST, WEST -> PLANE_X;
                case UNKNOWN -> null;
            };
        }
    }

    private static final ForgeDirection[][] SIDES = { {}, // down
        { EAST, SOUTH, WEST, NORTH }, // up
        {}, // north
        { UP, EAST, DOWN, WEST }, // south
        {}, // west
        { UP, SOUTH, DOWN, NORTH }, // east
    };

    private Orientation getConduitOrientation(IBlockAccess world, int x, int y, int z) {
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            Block block = world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);

            if (block != this) continue;

            int meta = world.getBlockMetadata(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);

            if (meta == 8) return Orientation.facing(dir);
        }

        int max = 0;
        ForgeDirection maxNormal = null;
        Vector3i v = new Vector3i();

        for (ForgeDirection normal : HALF) {
            int count = 0;

            for (int i = 0; i < 4; i++) {
                ForgeDirection a = SIDES[normal.ordinal()][i];
                ForgeDirection b = SIDES[normal.ordinal()][(i + 1) % 4];

                v.set(x, y, z)
                    .add(vec(a))
                    .add(vec(b));

                Block block = world.getBlock(v.x, v.y, v.z);

                if (block != this) continue;

                int meta = world.getBlockMetadata(v.x, v.y, v.z);

                if (meta == 8) count++;
            }

            if (count > 0 && count > max) {
                maxNormal = normal;
                max = count;

                if (count == 4) break;
            }
        }

        if (maxNormal != null) return Orientation.plane(maxNormal);

        max = 0;
        ForgeDirection maxDir = null;

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            Block block = world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);

            if (block != this) continue;

            int meta = world.getBlockMetadata(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);

            int value = switch (meta) {
                // Adjacent to a EM isolated casing
                case 9 -> 1;
                // Adjacent to a fine structure manipulator
                case 10 -> 2;
                default -> 0;
            };

            if (value > max) {
                maxDir = dir;
                max = value;
            }
        }

        if (maxDir != null) return Orientation.facing(maxDir);

        return Orientation.plane(UP);
    }
}

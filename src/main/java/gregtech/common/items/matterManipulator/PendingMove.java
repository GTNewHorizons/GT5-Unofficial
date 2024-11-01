package gregtech.common.items.matterManipulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import WayofTime.alchemicalWizardry.api.event.TeleposeEvent;
import gregtech.api.enums.Mods;
import gregtech.api.enums.Mods.Names;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IIC2Enet;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.common.items.matterManipulator.ItemMatterManipulator.ManipulatorTier;
import gregtech.common.items.matterManipulator.NBTState.Location;
import gregtech.common.items.matterManipulator.NBTState.PendingBlock;
import it.unimi.dsi.fastutil.Pair;

/**
 * Handles all moving logic.
 */
public class PendingMove extends AbstractBuildable {

    private List<Pair<Location, Location>> moves = null;
    private boolean[] remaining = null;

    private int cursor = 0;

    public PendingMove(EntityPlayer player, NBTState state, ManipulatorTier tier) {
        super(player, state, tier);
    }

    @Override
    public void tryPlaceBlocks(ItemStack stack, EntityPlayer player) {
        if (moves == null) {
            initMoves();
        }

        World world = player.worldObj;

        // try to move `placeSpeed` blocks from here to there
        for (int i = 0; i < Math.min(tier.placeSpeed, moves.size()); i++) {
            Pair<Location, Location> move = moves.get(cursor);
            int curr = cursor;
            cursor = (cursor + 1) % moves.size();

            Location s = move.left();
            Location d = move.right();

            if (!remaining[curr] || world.isAirBlock(s.x, s.y, s.z)) {
                remaining[curr] = false;
                continue;
            }

            // if either block is protected, ignore them completely and print a warning
            if (!isEditable(world, s.x, s.y, s.z) || !isEditable(world, d.x, d.y, d.z)) {
                continue;
            }

            PendingBlock source = PendingBlock.fromBlock(world, s.x, s.y, s.z);

            if (source.getBlock()
                .getBlockHardness(world, s.x, s.y, s.z) < 0) {
                GTUtility.sendErrorToPlayer(
                    player,
                    String.format("Could not move invulnerable source block X=%d, Y=%d, Z=%d", s.x, s.y, s.z));
                continue;
            }

            PendingBlock target = PendingBlock.fromBlock(world, d.x, d.y, d.z);

            Block existingBlock = target == null ? Blocks.air : target.getBlock();

            // check if we can remove the existing target block
            boolean canPlace = switch (state.config.removeMode) {
                case NONE -> existingBlock.isAir(world, d.x, d.y, d.z);
                case REPLACEABLE -> existingBlock.isReplaceable(world, d.x, d.y, d.z);
                case ALL -> true;
            };

            canPlace &= existingBlock.getBlockHardness(world, d.x, d.y, d.z) >= 0;

            if (!canPlace) {
                GTUtility.sendErrorToPlayer(
                    player,
                    String.format("Destination was blocked for source block X=%d, Y=%d, Z=%d", d.x, d.y, d.z));
                continue;
            }

            // remove the existing block if needed
            if (!existingBlock.isAir(world, d.x, d.y, d.z)) {
                if (!tryConsumePower(stack, target)) {
                    GTUtility.sendErrorToPlayer(player, "Matter Manipulator ran out of EU.");
                    break;
                }

                removeBlock(world, d.x, d.y, d.z, existingBlock, target == null ? 0 : target.metadata);
            }

            // if we can't move the source block then skip it for now
            if (!source.getBlock()
                .canPlaceBlockAt(world, d.x, d.y, d.z)) {
                continue;
            }

            if (!tryConsumePower(stack, source)) {
                GTUtility.sendErrorToPlayer(player, "Matter Manipulator ran out of EU.");
                break;
            }

            // try to move the source block into the (now empty) target block
            if (!swapBlocks(source, target)) {
                GTUtility.sendErrorToPlayer(
                    player,
                    "Could not swap block: " + source
                        + " ("
                        + source.getBlock()
                            .getLocalizedName()
                        + "@"
                        + source.metadata
                        + ")");
            }

            playSound(world, s.x, s.y, s.z, SoundResource.MOB_ENDERMEN_PORTAL);
            playSound(world, d.x, d.y, d.z, SoundResource.MOB_ENDERMEN_PORTAL);
            remaining[curr] = false;
        }

        playSounds();
        actuallyGivePlayerStuff();

        // bail if there are any remaining blocks
        for (int i = 0; i < remaining.length; i++) {
            if (remaining[i]) {
                return;
            }
        }

        // make sure all of the source blocks are actually air
        for (Pair<Location, Location> move : moves) {
            Location source = move.left();

            if (!world.isAirBlock(source.x, source.y, source.z)) {
                return;
            }
        }

        GTUtility.sendChatToPlayer(
            player,
            EnumChatFormatting.ITALIC.toString() + EnumChatFormatting.GRAY + "Finished placing blocks.");
    }

    private void initMoves() {
        moves = new ArrayList<>();

        Location startA = state.config.coordA;
        Location startB = state.config.coordB;
        Location dest = state.config.coordC;

        int x1 = startA.x;
        int y1 = startA.y;
        int z1 = startA.z;
        int x2 = startB.x;
        int y2 = startB.y;
        int z2 = startB.z;

        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);
        int maxZ = Math.max(z1, z2);

        int worldId = startA.worldId;

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    int dX = x - x1;
                    int dY = y - y1;
                    int dZ = z - z1;

                    moves.add(
                        Pair.of(
                            new Location(worldId, x, y, z),
                            new Location(worldId, dest.x + dX, dest.y + dY, dest.z + dZ)));
                }
            }
        }

        remaining = new boolean[moves.size()];
        Arrays.fill(remaining, true);
    }

    // 'borrowed' from
    // https://github.com/GTNewHorizons/BloodMagic/blob/master/src/main/java/WayofTime/alchemicalWizardry/common/block/BlockTeleposer.java#L158
    public static boolean swapBlocks(PendingBlock s, PendingBlock d) {

        World worldI = s.getWorld();
        int xi = s.x;
        int yi = s.y;
        int zi = s.z;
        World worldF = d.getWorld();
        int xf = d.x;
        int yf = d.y;
        int zf = d.z;

        TileEntity tileEntityI = worldI.getTileEntity(xi, yi, zi);
        TileEntity tileEntityF = worldF.getTileEntity(xf, yf, zf);

        NBTTagCompound nbttag1 = new NBTTagCompound();
        NBTTagCompound nbttag2 = new NBTTagCompound();

        if (tileEntityI != null) {
            tileEntityI.writeToNBT(nbttag1);
        }

        if (tileEntityF != null) {
            tileEntityF.writeToNBT(nbttag2);
        }

        Block blockI = worldI.getBlock(xi, yi, zi);
        Block blockF = worldF.getBlock(xf, yf, zf);

        if (blockI.equals(Blocks.air) && blockF.equals(Blocks.air)) {
            return false;
        }

        int metaI = worldI.getBlockMetadata(xi, yi, zi);
        int metaF = worldF.getBlockMetadata(xf, yf, zf);

        if (Mods.BloodMagic.isModLoaded()) {
            if (!allowTelepose(worldI, worldF, s, d)) {
                return false;
            }
        }

        // CLEAR TILES
        Block finalBlock = blockF;

        if (finalBlock != null) {
            TileEntity tileToSet = finalBlock.createTileEntity(worldF, metaF);

            worldF.setTileEntity(xf, yf, zf, tileToSet);
        }

        if (blockI != null) {
            TileEntity tileToSet = blockI.createTileEntity(worldI, metaI);

            worldI.setTileEntity(xi, yi, zi, tileToSet);
        }

        // TILES CLEARED
        worldF.setBlock(xf, yf, zf, blockI, metaI, 3);

        if (tileEntityI != null) {
            TileEntity newTileEntityI = TileEntity.createAndLoadEntity(nbttag1);

            worldF.setTileEntity(xf, yf, zf, newTileEntityI);

            newTileEntityI.xCoord = xf;
            newTileEntityI.yCoord = yf;
            newTileEntityI.zCoord = zf;
        }

        worldI.setBlock(xi, yi, zi, finalBlock, metaF, 3);

        if (tileEntityF != null) {
            TileEntity newTileEntityF = TileEntity.createAndLoadEntity(nbttag2);

            worldI.setTileEntity(xi, yi, zi, newTileEntityF);

            newTileEntityF.xCoord = xi;
            newTileEntityF.yCoord = yi;
            newTileEntityF.zCoord = zi;

            if (newTileEntityF instanceof IGregTechTileEntity igte
                && igte.getMetaTileEntity() instanceof BaseMetaTileEntity bmte) {
                bmte.setCableUpdateDelay(100);
            }

            if (newTileEntityF instanceof IIC2Enet enet) {
                enet.doEnetUpdate();
            }
        }

        return true;
    }

    @cpw.mods.fml.common.Optional.Method(modid = Names.BLOOD_MAGIC)
    private static boolean allowTelepose(World worldI, World worldF, PendingBlock s, PendingBlock d) {
        TeleposeEvent evt = new TeleposeEvent(
            worldI,
            s.x,
            s.y,
            s.z,
            s.getBlock(),
            s.metadata,
            worldF,
            d.x,
            d.y,
            d.z,
            d.getBlock(),
            d.metadata);
        if (MinecraftForge.EVENT_BUS.post(evt)) return false;
        return true;
    }
}

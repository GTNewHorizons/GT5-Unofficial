package gregtech.api.util;

import java.util.Optional;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableSet;

import appeng.api.implementations.tiles.IColorableTile;
import appeng.api.util.AEColor;
import appeng.block.networking.BlockCableBus;
import appeng.integration.IntegrationRegistry;
import appeng.integration.IntegrationType;
import appeng.integration.abstraction.IFMP;
import appeng.tile.networking.TileCableBus;
import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.tileentity.IColoredTileEntity;

/**
 * Used to provide a consistent interface for dealing with colors of blocks for the various spray can items.
 * <p>
 * Call {@link #getInstance(EntityPlayer, MovingObjectPosition)} or
 * {@link #getInstance(EntityPlayer, int, int, int, ForgeDirection)}
 * to acquire an instance of this class.
 */
public abstract class ColoredBlockContainer {

    private final static ColoredBlockContainer NULL_INSTANCE = new NullContainer();

    /**
     * Sets the color of the block.
     *
     * @param newColor a color from 0-15
     * @return true if the block was changed
     */
    public abstract boolean setColor(int newColor);

    /**
     * Removes the color of the block.
     *
     * @return true if color was removed
     */
    public abstract boolean removeColor();

    /**
     * Returns if the requested block is colorable.
     *
     * @return true if the block is colorable
     */
    public boolean isValid() {
        return true;
    }

    /**
     * Get the color of the block.
     *
     * @return an Optional with the color of the block inside, or {@link Optional#empty()} if the block is uncolored or
     *         invalid
     */
    public abstract Optional<Integer> getColor();

    private ColoredBlockContainer() {}

    public static ColoredBlockContainer getInstance(@NotNull EntityPlayer player,
        @NotNull MovingObjectPosition position) {
        if (position.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            return NULL_INSTANCE;
        }
        return getInstance(
            player,
            position.blockX,
            position.blockY,
            position.blockZ,
            ForgeDirection.getOrientation(position.sideHit));
    }

    public static ColoredBlockContainer getInstance(@NotNull EntityPlayer player, @NotNull TileEntity tileEntity,
        @NotNull ForgeDirection side) {
        return getInstance(player, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, side);
    }

    public static ColoredBlockContainer getInstance(EntityPlayer player, int x, int y, int z, ForgeDirection side) {
        final World world = player.getEntityWorld();
        final Block block = world.getBlock(x, y, z);

        // The vanilla method returns Blocks.air instead of null for a negative result
        if (block != Blocks.air) {
            if (VanillaBlockContainer.ALLOWED_VANILLA_BLOCKS.contains(block) || block instanceof BlockColored) {
                return new VanillaBlockContainer(block, world, x, y, z, side);
            } else if (block instanceof final BlockCableBus bus) {
                return new AE2BlockCableBusContainer(bus, world, x, y, z, side, player);
            } else {
                final TileEntity tileEntity = world.getTileEntity(x, y, z);

                if (tileEntity instanceof final IColorableTile colorableTile) {
                    return new AE2ColorableTileContainer(colorableTile, side, player);
                } else if (tileEntity instanceof final IColoredTileEntity coloredTileEntity) {
                    return new GTColoredBlockContainer(coloredTileEntity);
                }
            }
        }
        return NULL_INSTANCE;
    }

    /**
     * Provides functionality for various types of vanilla blocks that use their metadata value for color. Also performs
     * some transformations of blocks, e.g. glass is transformed to stained glass when sprayed.
     */
    private static class VanillaBlockContainer extends ColoredBlockContainer {

        private static final Set<Block> ALLOWED_VANILLA_BLOCKS = ImmutableSet.of(
            Blocks.glass,
            Blocks.glass_pane,
            Blocks.stained_glass,
            Blocks.stained_glass_pane,
            Blocks.carpet,
            Blocks.hardened_clay,
            Blocks.stained_hardened_clay);
        private static final BiMap<Block, Block> TRANSFORMATIONS = ImmutableBiMap.of(
            Blocks.glass,
            Blocks.stained_glass,
            Blocks.glass_pane,
            Blocks.stained_glass_pane,
            Blocks.hardened_clay,
            Blocks.stained_hardened_clay);

        private final int originalColor;
        private final World world;
        private final int x;
        private final int y;
        private final int z;
        private final Block block;
        private final ForgeDirection side;

        public VanillaBlockContainer(final Block block, final World world, final int x, final int y, final int z,
            final ForgeDirection side) {
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
            this.block = block;
            this.side = side;
            originalColor = world.getBlockMetadata(x, y, z);
        }

        @Override
        public Optional<Integer> getColor() {
            return Optional.of(Dyes.transformDyeIndex(originalColor));
        }

        @Override
        public boolean setColor(final int newColor) {
            final int transformedColor = Dyes.transformDyeIndex(newColor);

            if (TRANSFORMATIONS.containsKey(block)) {
                world.setBlock(x, y, z, TRANSFORMATIONS.get(block), transformedColor, 3);
                return true;
            } else {
                if (originalColor != transformedColor) {
                    if (block instanceof BlockColored) {
                        return block.recolourBlock(world, x, y, z, side, transformedColor);
                    }
                    return world.setBlockMetadataWithNotify(x, y, z, transformedColor, 3);
                }
            }

            return block.recolourBlock(world, x, y, z, side, transformedColor);
        }

        @Override
        public boolean removeColor() {
            if (TRANSFORMATIONS.containsValue(block)) {
                world.setBlock(
                    x,
                    y,
                    z,
                    TRANSFORMATIONS.inverse()
                        .get(block),
                    0,
                    3);
                return true;
            }

            return false;
        }
    }

    /**
     * Provides functionality for full AE2 blocks like the ME Chest and Security Terminal.
     */
    private static class AE2ColorableTileContainer extends ColoredBlockContainer {

        private final IColorableTile colorableTile;
        private final ForgeDirection side;
        private final EntityPlayer player;

        public AE2ColorableTileContainer(final IColorableTile colorableTile, final ForgeDirection side,
            final EntityPlayer player) {
            this.colorableTile = colorableTile;
            this.side = side;
            this.player = player;
        }

        @Override
        public Optional<Integer> getColor() {
            return Optional.of(
                Dyes.transformDyeIndex(
                    colorableTile.getColor()
                        .ordinal()));
        }

        @Override
        public boolean setColor(final int newColor) {
            return colorableTile.recolourBlock(side, AEColor.values()[Dyes.transformDyeIndex(newColor)], player);
        }

        @Override
        public boolean removeColor() {
            return colorableTile.recolourBlock(side, AEColor.Transparent, player);
        }
    }

    /**
     * Provides functionality for AE2 cables and other multipart things that go on cables.
     */
    private static class AE2BlockCableBusContainer extends ColoredBlockContainer {

        private final BlockCableBus bus;
        private final World world;
        private final int x;
        private final int y;
        private final int z;
        private final ForgeDirection side;
        private final EntityPlayer player;

        public AE2BlockCableBusContainer(final BlockCableBus bus, final World world, final int x, final int y,
            final int z, final ForgeDirection side, final EntityPlayer player) {
            this.bus = bus;
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
            this.side = side;
            this.player = player;
        }

        @Override
        public boolean setColor(final int newColor) {
            return bus.recolourBlock(world, x, y, z, side, Dyes.transformDyeIndex(newColor), player);
        }

        @Override
        public boolean removeColor() {
            return bus.recolourBlock(world, x, y, z, side, AEColor.Transparent.ordinal(), player);
        }

        @Override
        public Optional<Integer> getColor() {
            final TileEntity te = world.getTileEntity(x, y, z);
            AEColor aeColor = null;

            // Code stolen from an AE2 private method.
            if (te instanceof final TileCableBus cableBus) {
                aeColor = cableBus.getCableBus().getColor();
            } else if (IntegrationRegistry.INSTANCE.isEnabled(IntegrationType.FMP)) {
                aeColor = ((IFMP) IntegrationRegistry.INSTANCE.getInstance(IntegrationType.FMP)).getCableContainer(te).getColor();
            }

            return aeColor == null ? Optional.empty() : Optional.of(Dyes.transformDyeIndex(aeColor.ordinal()));
        }
    }

    /**
     * Provides functionality for GT machines, cables, pipes, etc.
     */
    private static class GTColoredBlockContainer extends ColoredBlockContainer {

        private final IColoredTileEntity coloredTileEntity;

        public GTColoredBlockContainer(final IColoredTileEntity coloredTileEntity) {
            this.coloredTileEntity = coloredTileEntity;
        }

        @Override
        public boolean setColor(final int newColor) {
            coloredTileEntity.setColorization((byte) newColor);
            return true;
        }

        @Override
        public boolean removeColor() {
            if (coloredTileEntity.getColorization() > -1) {
                coloredTileEntity.setColorization((byte) -1);
                return true;
            }
            return false;
        }

        @Override
        public Optional<Integer> getColor() {
            final int colorization = coloredTileEntity.getColorization();
            if (colorization == -1) {
                return Optional.empty();
            }
            return Optional.of(colorization);
        }
    }

    /**
     * Returned when the block is invalid or otherwise has no color functionality. Calling {@link #setColor(int)} does
     * nothing, so it's safe to call without verifying the exact instance of the returned {@link ColoredBlockContainer}.
     */
    private static class NullContainer extends ColoredBlockContainer {

        @Override
        public boolean setColor(final int newColor) {
            return false;
        }

        @Override
        public boolean removeColor() {
            return false;
        }

        @Override
        public Optional<Integer> getColor() {
            return Optional.empty();
        }

        @Override
        public boolean isValid() {
            return false;
        }
    }
}

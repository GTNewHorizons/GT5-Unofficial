package detrav.items.tools;

import static detrav.enums.DetravScannerMode.ALL_ORES;
import static detrav.enums.DetravScannerMode.BIG_ORES;
import static detrav.enums.DetravScannerMode.FLUIDS;
import static detrav.enums.DetravScannerMode.POLLUTION;
import static gregtech.api.enums.Mods.VisualProspecting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.MapMaker;
import com.sinthoras.visualprospecting.VisualProspecting_API;

import detrav.enums.DetravScannerMode;
import detrav.net.DetravNetwork;
import detrav.net.ProspectingPacket;
import gregtech.api.task.CooperativeScheduler;
import gregtech.common.UndergroundOil;
import gregtech.common.items.tools.ToolItemBase;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;
import gregtech.common.pollution.Pollution;

/**
 * The scanning half of the Electric Prospector's Scanner, moved out of
 * {@code BehaviourDetravToolElectricProspector}. Everything the hand scanner does on a block it still does through
 * {@link DetravProspectorActions}; this adds the area scan that fills the map GUI.
 */
public class DetravElectricProspectorActions extends DetravProspectorActions {

    /**
     * The scan a player has running, so that starting another one cancels it. Static because it outlives the single
     * use these objects are built for.
     */
    private static final Map<EntityPlayer, Future<?>> PENDING_SCANS = new MapMaker().weakValues()
        .makeMap();

    public DetravElectricProspectorActions(int costs, int legacyMeta) {
        super(costs, legacyMeta);
    }

    /**
     * Right-clicking air: sneaking cycles the mode, otherwise the tool scans everything around the player and sends
     * the result to the map GUI.
     *
     * @return whether the click was consumed.
     */
    public boolean onItemRightClick(ToolItemBase item, ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote) return true;

        Future<?> pending = PENDING_SCANS.remove(player);

        if (pending != null && !pending.isDone()) {
            pending.cancel(true);
            player.addChatMessage(new ChatComponentText("Cancelled pending scan"));
        }

        if (player.isSneaking()) {
            int data = item.getMode(stack) + 1;
            if (data >= DetravScannerMode.COUNT) data = 0;
            player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("detrav.scanner.mode." + data)));

            item.setMode(stack, (byte) data);
            return true;
        }

        final int data = item.getMode(stack);

        final int cX = ((int) player.posX) >> 4;
        final int cZ = ((int) player.posZ) >> 4;
        final List<Chunk> chunks = new ArrayList<>();
        player.addChatMessage(new ChatComponentText("Scanning..."));

        final int radius = item.getHarvestLevel(stack, "");

        int scanRadius = radius + 1;
        for (int i = -scanRadius; i <= scanRadius; i++) {
            for (int j = -scanRadius; j <= scanRadius; j++) {
                if (i != -scanRadius && i != scanRadius && j != -scanRadius && j != scanRadius) {
                    chunks.add(world.getChunkFromChunkCoords(cX + i, cZ + j));
                }
            }
        }

        if (!player.capabilities.isCreativeMode) item.doDamage(stack, (long) this.costs * chunks.size());

        final ProspectingPacket packet = new ProspectingPacket(
            cX,
            cZ,
            (int) player.posX,
            (int) player.posZ,
            radius,
            data);

        Future<?> task = CooperativeScheduler.INSTANCE.schedule(ctx -> {
            while (!ctx.shouldYield()) {
                if (chunks.isEmpty()) {
                    ctx.stop(null);
                    break;
                }

                Chunk c = chunks.remove(chunks.size() - 1);

                switch (data) {
                    case BIG_ORES, ALL_ORES -> {
                        for (int x = 0; x < 16; x++) {
                            for (int z = 0; z < 16; z++) {
                                final int height = c.getHeightValue(x, z);

                                for (int y = 1; y < height; y++) {
                                    Block block = c.getBlock(x, y, z);
                                    int meta = c.getBlockMetadata(x, y, z);

                                    try (OreInfo<?> info = OreManager.getOreInfo(block, meta)) {
                                        if (info == null || !info.isNatural) continue;
                                        if (data != ALL_ORES && info.isSmall) continue;

                                        packet.addBlock(c.xPosition * 16 + x, y, c.zPosition * 16 + z, block, meta);
                                    }
                                }
                            }
                        }
                    }
                    case FLUIDS -> {
                        FluidStack fluid = UndergroundOil.undergroundOil(c, -1);

                        packet.addFluid(c.xPosition, c.zPosition, fluid);
                    }
                    case POLLUTION -> {
                        int pollution = Pollution.getPollution(c);

                        packet.addPollution(c.xPosition, c.zPosition, pollution);
                    }
                }
            }
        })
            .onFinished(x -> {
                PENDING_SCANS.remove(player);

                DetravNetwork.INSTANCE.sendToPlayer(packet, (EntityPlayerMP) player);

                if (VisualProspecting.isModLoaded()) {
                    if (data == BIG_ORES || data == ALL_ORES) {
                        VisualProspecting_API.LogicalServer.sendProspectionResultsToClient(
                            (EntityPlayerMP) player,
                            VisualProspecting_API.LogicalServer.prospectOreVeinsWithinRadius(
                                world.provider.dimensionId,
                                (int) player.posX,
                                (int) player.posZ,
                                radius * 16),
                            new ArrayList<>());
                    } else if (data == FLUIDS) {
                        VisualProspecting_API.LogicalServer.sendProspectionResultsToClient(
                            (EntityPlayerMP) player,
                            new ArrayList<>(),
                            VisualProspecting_API.LogicalServer.prospectUndergroundFluidsWithingRadius(
                                world,
                                (int) player.posX,
                                (int) player.posZ,
                                radius * 16));
                    }
                }
            });

        PENDING_SCANS.put(player, task);
        return true;
    }

    @Override
    void addChatMessageByValue(EntityPlayer player, int value, String name) {
        if (value < 0) {
            player.addChatMessage(
                new ChatComponentText(StatCollector.translateToLocalFormatted("detrav.scanner.found.texts.6", name)));
        } else if (value < 1) {
            player.addChatMessage(
                new ChatComponentText(StatCollector.translateToLocalFormatted("detrav.scanner.found.texts.6", "")));
        } else player.addChatMessage(
            new ChatComponentText(
                StatCollector.translateToLocalFormatted("detrav.scanner.found.texts.6", name) + " " + value));
    }

    /**
     * Right-clicking a block: what it does depends on the mode, from prospecting the one chunk through to reading the
     * chunk's pollution.
     */
    @Override
    public boolean onItemUse(ToolItemBase item, ItemStack stack, EntityPlayer player, World world, int x, int y,
        int z) {
        int data = item.getMode(stack);
        if (data < FLUIDS) {
            if (world.getBlock(x, y, z) == Blocks.bedrock) {
                if (!world.isRemote) {
                    FluidStack fluid = UndergroundOil.undergroundOil(world.getChunkFromBlockCoords(x, z), -1);
                    addChatMessageByValue(player, fluid.amount, fluid.getLocalizedName());
                    if (!player.capabilities.isCreativeMode) item.doDamage(stack, this.costs);
                }
            } else {
                if (!world.isRemote) {
                    prospectSingleChunk(item, stack, player, world, x, y, z);
                }
            }
            return true;
        }
        if (data < POLLUTION) {
            if (!world.isRemote) {
                FluidStack fluid = UndergroundOil.undergroundOil(world.getChunkFromBlockCoords(x, z), -1);
                addChatMessageByValue(player, fluid.amount, fluid.getLocalizedName());
                if (!player.capabilities.isCreativeMode) item.doDamage(stack, this.costs);
            }
            return true;
        }
        if (!world.isRemote) {
            addChatMessageByValue(player, getPollution(world, x, z), "Pollution");
        }
        return true;
    }
}

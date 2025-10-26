package detrav.items.behaviours;

import static detrav.items.DetravMetaGeneratedTool01.MODE_ALL_ORES;
import static detrav.items.DetravMetaGeneratedTool01.MODE_BIG_ORES;
import static detrav.items.DetravMetaGeneratedTool01.MODE_FLUIDS;
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

import detrav.items.DetravMetaGeneratedTool01;
import detrav.net.DetravNetwork;
import detrav.net.ProspectingPacket;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.task.CooperativeScheduler;
import gregtech.common.UndergroundOil;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;
import gregtech.common.pollution.Pollution;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class BehaviourDetravToolElectricProspector extends BehaviourDetravToolProspector {

    private Map<EntityPlayer, Future<?>> PENDING_SCANS = new MapMaker().weakValues()
        .makeMap();

    public BehaviourDetravToolElectricProspector(int aCosts) {
        super(aCosts);
    }

    public ItemStack onItemRightClick(MetaBaseItem aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        if (!aWorld.isRemote) {
            Future<?> scan = PENDING_SCANS.remove(aPlayer);

            if (scan != null && !scan.isDone()) {
                scan.cancel(true);
                aPlayer.addChatMessage(new ChatComponentText("Cancelled pending scan"));
            }

            if (aPlayer.isSneaking()) {
                int data = DetravMetaGeneratedTool01.INSTANCE.getToolGTDetravData(aStack)
                    .intValue();

                data++;
                if (data > 3) data = 0;
                aPlayer.addChatMessage(
                    new ChatComponentText(StatCollector.translateToLocal("detrav.scanner.mode." + data)));

                DetravMetaGeneratedTool01.INSTANCE.setToolGTDetravData(aStack, data);
                return super.onItemRightClick(aItem, aStack, aWorld, aPlayer);
            }

            int data = DetravMetaGeneratedTool01.INSTANCE.getToolGTDetravData(aStack)
                .intValue();

            final int cX = ((int) aPlayer.posX) >> 4;
            final int cZ = ((int) aPlayer.posZ) >> 4;
            final List<Chunk> chunks = new ArrayList<>();
            aPlayer.addChatMessage(new ChatComponentText("Scanning..."));

            final int radius = aItem.getHarvestLevel(aStack, "");

            int scanRadius = radius + 1;
            for (int i = -scanRadius; i <= scanRadius; i++) {
                for (int j = -scanRadius; j <= scanRadius; j++) {
                    if (i != -scanRadius && i != scanRadius && j != -scanRadius && j != scanRadius) {
                        chunks.add(aWorld.getChunkFromChunkCoords(cX + i, cZ + j));
                    }
                }
            }

            final DetravMetaGeneratedTool01 tool = (DetravMetaGeneratedTool01) aItem;
            if (!aPlayer.capabilities.isCreativeMode) tool.doDamage(aStack, (long) this.mCosts * chunks.size());

            final ProspectingPacket packet = new ProspectingPacket(
                cX,
                cZ,
                (int) aPlayer.posX,
                (int) aPlayer.posZ,
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
                        case MODE_BIG_ORES, MODE_ALL_ORES -> {
                            for (int x = 0; x < 16; x++) {
                                for (int z = 0; z < 16; z++) {
                                    final int height = c.getHeightValue(x, z);

                                    for (int y = 1; y < height; y++) {
                                        Block block = c.getBlock(x, y, z);
                                        int meta = c.getBlockMetadata(x, y, z);

                                        try (OreInfo<?> info = OreManager.getOreInfo(block, meta)) {
                                            if (info == null || !info.isNatural) continue;
                                            if (data != MODE_ALL_ORES && info.isSmall) continue;

                                            packet.addBlock(c.xPosition * 16 + x, y, c.zPosition * 16 + z, block, meta);
                                            continue;
                                        }
                                    }
                                }
                            }
                        }
                        case MODE_FLUIDS -> {
                            FluidStack fluid = UndergroundOil.undergroundOil(c, -1);

                            packet.addFluid(c.xPosition, c.zPosition, fluid);
                        }
                        case DetravMetaGeneratedTool01.MODE_POLLUTION -> {
                            int pollution = Pollution.getPollution(c);

                            packet.addPollution(c.xPosition, c.zPosition, pollution);
                        }
                    }
                }
            })
                .onFinished(x -> {
                    PENDING_SCANS.remove(aPlayer);

                    DetravNetwork.INSTANCE.sendToPlayer(packet, (EntityPlayerMP) aPlayer);

                    if (VisualProspecting.isModLoaded()) {
                        if (data == MODE_BIG_ORES || data == MODE_ALL_ORES) {
                            VisualProspecting_API.LogicalServer.sendProspectionResultsToClient(
                                (EntityPlayerMP) aPlayer,
                                VisualProspecting_API.LogicalServer.prospectOreVeinsWithinRadius(
                                    aWorld.provider.dimensionId,
                                    (int) aPlayer.posX,
                                    (int) aPlayer.posZ,
                                    radius * 16),
                                new ArrayList<>());
                        } else if (data == MODE_FLUIDS) {
                            VisualProspecting_API.LogicalServer.sendProspectionResultsToClient(
                                (EntityPlayerMP) aPlayer,
                                new ArrayList<>(),
                                VisualProspecting_API.LogicalServer.prospectUndergroundFluidsWithingRadius(
                                    aWorld,
                                    (int) aPlayer.posX,
                                    (int) aPlayer.posZ,
                                    radius * 16));
                        }
                    }
                });

            PENDING_SCANS.put(aPlayer, task);
        } else {
            // This tells minecraft that the item was used.
            return aStack.copy();
        }
        return super.onItemRightClick(aItem, aStack, aWorld, aPlayer);
    }

    void addChatMassageByValue(EntityPlayer aPlayer, int value, String name) {
        if (value < 0) {
            aPlayer.addChatMessage(
                new ChatComponentText(StatCollector.translateToLocal("detrav.scanner.found.texts.6") + name));
        } else if (value < 1) {
            aPlayer
                .addChatMessage(new ChatComponentText(StatCollector.translateToLocal("detrav.scanner.found.texts.6")));
        } else aPlayer.addChatMessage(
            new ChatComponentText(StatCollector.translateToLocal("detrav.scanner.found.texts.6") + name + " " + value));
    }

    @Override
    public boolean onItemUse(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY,
        int aZ, int aSide, float hitX, float hitY, float hitZ) {
        long data = DetravMetaGeneratedTool01.INSTANCE.getToolGTDetravData(aStack);
        if (data < 2) {
            if (aWorld.getBlock(aX, aY, aZ) == Blocks.bedrock) {
                if (!aWorld.isRemote) {
                    FluidStack fStack = UndergroundOil.undergroundOil(aWorld.getChunkFromBlockCoords(aX, aZ), -1);
                    addChatMassageByValue(aPlayer, fStack.amount, fStack.getLocalizedName());
                    if (!aPlayer.capabilities.isCreativeMode)
                        ((DetravMetaGeneratedTool01) aItem).doDamage(aStack, this.mCosts);
                }
            } else {
                if (!aWorld.isRemote) {
                    prospectSingleChunk(aItem, aStack, aPlayer, aWorld, aX, aY, aZ);
                }
            }
            return true;
        }
        if (data < 3) if (!aWorld.isRemote) {
            FluidStack fStack = UndergroundOil.undergroundOil(aWorld.getChunkFromBlockCoords(aX, aZ), -1);
            addChatMassageByValue(aPlayer, fStack.amount, fStack.getLocalizedName());
            if (!aPlayer.capabilities.isCreativeMode) ((DetravMetaGeneratedTool01) aItem).doDamage(aStack, this.mCosts);
            return true;
        }
        if (!aWorld.isRemote) {
            addChatMassageByValue(aPlayer, getPollution(aWorld, aX, aZ), "Pollution");
        }
        return true;
    }

}

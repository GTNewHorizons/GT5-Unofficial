package com.detrav.items.behaviours;

import com.detrav.items.DetravMetaGeneratedTool01;
import com.detrav.utils.DetravNetwork;
import com.detrav.utils.DetravProPickPacket01;
import gregtech.api.GregTech_API;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_TileEntity_Ores;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ChunkLoader;
import net.minecraftforge.common.ForgeChunkManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class BehaviourDetravToolElectricProPick extends BehaviourDetravToolProPick {

    public BehaviourDetravToolElectricProPick(int aCosts) {
        super(aCosts);
    }

    public ItemStack onItemRightClick(GT_MetaBase_Item aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer) {

        if (!aWorld.isRemote) {
            //aPlayer.openGui();
            DetravMetaGeneratedTool01 tool = (DetravMetaGeneratedTool01) aItem;
            //aWorld.getChunkFromBlockCoords()
            int cX = ((int) aPlayer.posX) >> 4;
            int cZ = ((int) aPlayer.posZ) >> 4;
            int size = aItem.getHarvestLevel(aStack, "") + 1;
            List<Chunk> chunks = new ArrayList<Chunk>();
            //aPlayer.addChatMessage(new ChatComponentText("Scanning Begin, wait!"));
            //DetravProPickPacket01 packet = new DetravProPickPacket01();
            for (int i = -size; i <= size; i++)
                for (int j = -size; j <= size; j++)
                    if (i != -size && i != size && j != -size && j != size)
                        chunks.add(aWorld.getChunkFromChunkCoords(cX + i, cZ + j));
            size = size - 1;
            //c.gene
            DetravProPickPacket01 packet = new DetravProPickPacket01();
            packet.chunkX = cX;
            packet.chunkZ = cZ;
            packet.size = size;
            for (Chunk c : chunks) {
                for (int x = 0; x < 16; x++)
                    for (int z = 0; z < 16; z++) {
                        int ySize = c.getHeightValue(x, z);
                        for (int y = 1; y < ySize; y++) {
                            Block b = c.getBlock(x, y, z);
                            if (b == GregTech_API.sBlockOres1) {
                                TileEntity entity = c.getTileEntityUnsafe(x, y, z);
                                if (entity != null && entity instanceof GT_TileEntity_Ores) {
                                    GT_TileEntity_Ores gt_entity = (GT_TileEntity_Ores) entity;
                                    String name = GT_LanguageManager.getTranslation(
                                            b.getUnlocalizedName() + "." + gt_entity.getMetaData() + ".name");
                                    if (name.startsWith("Small")) continue;
                                    packet.addBlock(c.xPosition * 16 + x, y, c.zPosition * 16 + z, gt_entity.getMetaData());
                                }
                            }
                        }
                    }
            }
            packet.level = ((DetravMetaGeneratedTool01) aItem).getHarvestLevel(aStack, "");
            DetravNetwork.INSTANCE.sendToPlayer(packet, (EntityPlayerMP) aPlayer);
            if (!aPlayer.capabilities.isCreativeMode)
                tool.doDamage(aStack, this.mCosts * chunks.size());
        }
        return super.onItemRightClick(aItem, aStack, aWorld, aPlayer);
    }
}
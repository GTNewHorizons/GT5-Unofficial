package com.detrav.items.behaviours;

import com.detrav.items.DetravMetaGeneratedTool01;
import com.detrav.net.DetravNetwork;
import com.detrav.net.DetravProPickPacket00;
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
import net.minecraftforge.fluids.FluidStack;

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
            int data = DetravMetaGeneratedTool01.INSTANCE.getToolGTDetravData(aStack).intValue();
            //Проверяем если нажат шифт
            if (aPlayer.isSneaking()) {
                data++;
                if(data>3) data = 0;
                switch (data)
                {
                    case 0:
                        aPlayer.addChatMessage(new ChatComponentText("Set Mode: Ore, Any Rock Block"));
                        break;
                    case 1:
                        aPlayer.addChatMessage(new ChatComponentText("Set Mode: Ore (with small), Any Rock Block"));
                        break;
                    case 2:
                        aPlayer.addChatMessage(new ChatComponentText("Set Mode: Oil, Any Block"));
                        break;
                    case 3:
                        aPlayer.addChatMessage(new ChatComponentText("Set Mode: Pollution, Any Block"));
                        break;
                    default:
                        aPlayer.addChatMessage(new ChatComponentText("Set Mode: ERROR"));
                        break;
                }
                DetravMetaGeneratedTool01.INSTANCE.setToolGTDetravData(aStack, (long)data);
                return super.onItemRightClick(aItem, aStack, aWorld, aPlayer);
            }

            //aPlayer.openGui();
            DetravMetaGeneratedTool01 tool = (DetravMetaGeneratedTool01) aItem;
            //aWorld.getChunkFromBlockCoords()
            int cX = ((int) aPlayer.posX) >> 4;
            int cZ = ((int) aPlayer.posZ) >> 4;
            int size = aItem.getHarvestLevel(aStack, "") + 1;
            List<Chunk> chunks = new ArrayList<Chunk>();
            //aPlayer.addChatMessage(new ChatComponentText("Scanning Begin, wait!"));
            //DetravProPickPacket00 packet = new DetravProPickPacket00();
            for (int i = -size; i <= size; i++)
                for (int j = -size; j <= size; j++)
                    if (i != -size && i != size && j != -size && j != size)
                        chunks.add(aWorld.getChunkFromChunkCoords(cX + i, cZ + j));
            size = size - 1;
            //c.gene
            DetravProPickPacket00 packet = new DetravProPickPacket00();
            packet.ptype = (int)data;
            packet.chunkX = cX;
            packet.chunkZ = cZ;
            packet.size = size;
            for (Chunk c : chunks) {
                for (int x = 0; x < 16; x++)
                    for (int z = 0; z < 16; z++) {
                        int ySize = c.getHeightValue(x, z);
                        for (int y = 1; y < ySize; y++) {
                            switch (data) {
                                case 0:
                                case 1:
                                    Block b = c.getBlock(x, y, z);
                                    if (b == GregTech_API.sBlockOres1) {
                                        TileEntity entity = c.getTileEntityUnsafe(x, y, z);
                                        if (entity != null && entity instanceof GT_TileEntity_Ores) {
                                            GT_TileEntity_Ores gt_entity = (GT_TileEntity_Ores) entity;
                                            String name = GT_LanguageManager.getTranslation(
                                                    b.getUnlocalizedName() + "." + gt_entity.getMetaData() + ".name");
                                            if (name.startsWith("Small")) if (data != 1) continue;
                                            packet.addBlock(c.xPosition * 16 + x, y, c.zPosition * 16 + z, gt_entity.getMetaData());
                                        }
                                    }
                                    break;
                                case 2:
                                    FluidStack fStack = getUndergroundOil(aWorld, c.xPosition * 16 + x, c.zPosition * 16 + z);
                                    if (fStack.amount > 10000) {
                                        packet.addBlock(c.xPosition * 16 + x, 2, c.zPosition * 16 + z, (short) (fStack.amount/5000));
                                        packet.addBlock(c.xPosition * 16 + x, 1, c.zPosition * 16 + z, (short) fStack.getFluidID());
                                    }
                                    break;
                            }
                            if(data > 1 ) break;;
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

    void addChatMassageByValue(EntityPlayer aPlayer, int value, String name) {
        if (value < 0) {
            aPlayer.addChatMessage(new ChatComponentText(foundTexts[6] + name));
        } else if (value < 1) {
            aPlayer.addChatMessage(new ChatComponentText(foundTexts[0]));
        } else
            aPlayer.addChatMessage(new ChatComponentText(foundTexts[6] + name + ": " + value));
    }

    public boolean onItemUse(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
        long data = DetravMetaGeneratedTool01.INSTANCE.getToolGTDetravData(aStack);
        if (data < 2)
            return super.onItemUse(aItem, aStack, aPlayer, aWorld, aX, aY, aZ, aSide, hitX, hitY, hitZ);
        if (!aWorld.isRemote) {
            FluidStack fStack = getUndergroundOil(aWorld,aX,aZ);
            addChatMassageByValue(aPlayer,fStack.amount/5000,fStack.getLocalizedName());
            if (!aPlayer.capabilities.isCreativeMode)
                ((DetravMetaGeneratedTool01)aItem).doDamage(aStack, this.mCosts);
        }
        return true;
    }
}
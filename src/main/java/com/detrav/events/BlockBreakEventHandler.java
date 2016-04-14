package com.detrav.events;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
//import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

/**
 * Created by wital_000 on 13.04.2016.
 */
public class BlockBreakEventHandler {
        @SubscribeEvent
        public void onBreakBlock(BlockEvent.BreakEvent ev) {
            if (ev.isCanceled())
                return;
            if(ev instanceof DetravBlockBreakEvent)
                return;
            if (!ev.world.isRemote) {
                EntityPlayer player =  ev.getPlayer();
                if((player instanceof EntityPlayerMP) && ((EntityPlayerMP)player).theItemInWorldManager.getGameType() != WorldSettings.GameType.SURVIVAL)
                    return;
                if(player.capabilities.isCreativeMode)
                    return;
                NBTTagCompound entityData = player.getEntityData();
                if(entityData.getLong("detrav.minning.mode")==0) return;
                Vec3 vec3 = Vec3.createVectorHelper(player.posX, player.posY + 1.62f, player.posZ);
                Vec3 vec31 = ev.getPlayer().getLook(1.0F);
                Vec3 vec32 = vec3.addVector(vec31.xCoord * 4.5F, vec31.yCoord * 4.5F, vec31.zCoord * 4.5F);
                int side = rayTrace(ev.world, ev.block,ev.blockMetadata,vec3, vec32,ev.x,ev.y,ev.z, false, false, true);
                //ev.getPlayer().addChatMessage(new ChatComponentText(ev.block.getLocalizedName() + " | " + ev.getPlayer().rotationYaw + " | " + ev.getPlayer().rotationPitch));
                //ev.getPlayer().addChatMessage(new ChatComponentText(ev.block.getLocalizedName() + " | " + side));
                switch (side) {
                    case 0:
                    case 1:
                        //    if (ev.getPlayer().rotationPitch > 45 || ev.getPlayer().rotationPitch < -45) {
                        for (int i = ev.x - 1; i <= ev.x + 1; i++)
                            for (int j = ev.z - 1; j <= ev.z + 1; j++)
                                if (i != ev.x || j != ev.z)
                                    tryHarvestBlock(i, ev.y, j, ev);
                        break;
                    // and (-45 > x > -135) and (-135 > x > -225) and (-225 > x > -315) or (-315 > x > -45)
                    //    } else if ((-135 <= ev.getPlayer().rotationYaw && ev.getPlayer().rotationYaw <= -45) || (-315 <= ev.getPlayer().rotationYaw && ev.getPlayer().rotationYaw <= -225)) {
                    case 4:
                    case 5:
                        for (int i = ev.z - 1; i <= ev.z + 1; i++)
                            for (int j = ev.y - 1; j <= ev.y + 1; j++)
                                if (i != ev.z || j != ev.y)
                                    tryHarvestBlock(ev.x, j, i, ev);
                        break;
                    //    } else if ((-225 <= ev.getPlayer().rotationYaw && ev.getPlayer().rotationYaw <= -135) || -45 <= ev.getPlayer().rotationYaw || ev.getPlayer().rotationYaw <= -315) {
                    case 2:
                    case 3:
                        for (int i = ev.x - 1; i <= ev.x + 1; i++)
                            for (int j = ev.y - 1; j <= ev.y + 1; j++)
                                if (i != ev.x || j != ev.y)
                                    tryHarvestBlock(i, j, ev.z, ev);
                        break;
                        //    } else
                        //        ev.getPlayer().addChatMessage(new ChatComponentText("Unknown | yaw = " + ev.getPlayer().rotationYaw + " | pitch = " + ev.getPlayer().rotationPitch));
                }
            }
        }

    static private boolean tryHarvestBlock(int x, int y, int z,BlockEvent.BreakEvent event2) {
        if (!(event2.getPlayer() instanceof EntityPlayerMP))
            return false;
        EntityPlayer thisPlayerMP = event2.getPlayer();
        ItemStack stack = thisPlayerMP.getCurrentEquippedItem();
        if(stack == null ) return false;
        World theWorld = event2.world;
        Block block = theWorld.getBlock(x, y, z);
        // dirt and pickaxe -> if(true && false) -> exit
        // dirt and shovel -> if(true && false) -> exit

        if (!stack.func_150998_b(block))
            return false;

        BlockEvent.BreakEvent event = onDetravBlockBreakEvent(event2.world, WorldSettings.GameType.SURVIVAL, (EntityPlayerMP) event2.getPlayer(), x, y, z);
        if (event.isCanceled())
            return false;

        if (stack != null && stack.getItem().onBlockStartBreak(stack, x, y, z, thisPlayerMP)) {
            return false;
        }
        int l = theWorld.getBlockMetadata(x, y, z);
        theWorld.playAuxSFXAtEntity(thisPlayerMP, 2001, x, y, z, Block.getIdFromBlock(block) + (theWorld.getBlockMetadata(x, y, z) << 12));
        boolean flag = false;
        ItemStack itemstack = thisPlayerMP.getCurrentEquippedItem();
        boolean flag1 = block.canHarvestBlock(thisPlayerMP, l);

        if (itemstack != null) {
            itemstack.func_150999_a(theWorld, block, x, y, z, thisPlayerMP);
            if (itemstack.stackSize == 0) {
                thisPlayerMP.destroyCurrentEquippedItem();
            }
        }
        flag = removeBlock(x, y, z, flag1, event);
        if (flag && flag1) {
            block.harvestBlock(theWorld, thisPlayerMP, x, y, z, l);
        }
        if (flag && event != null) {
            block.dropXpOnBlockBreak(theWorld, x, y, z, event.getExpToDrop());
        }
        return flag;
    }

    static private boolean removeBlock(int x, int y, int z, boolean canHarvest,BlockEvent.BreakEvent event)
    {
        World theWorld = event.world;
        EntityPlayer thisPlayerMP = event.getPlayer();
        Block block = theWorld.getBlock(x, y, z);
        int l = theWorld.getBlockMetadata(x, y, z);
        block.onBlockHarvested(theWorld, x, y, z, l, thisPlayerMP);
        boolean flag = block.removedByPlayer(theWorld, thisPlayerMP, x, y, z, canHarvest);

        if (flag)
        {
            block.onBlockDestroyedByPlayer(theWorld, x, y, z, l);
        }

        return flag;
    }

    public static BlockEvent.BreakEvent onDetravBlockBreakEvent(World world, WorldSettings.GameType gameType, EntityPlayerMP entityPlayer, int x, int y, int z)
    {
        // Logic from tryHarvestBlock for pre-canceling the event
        boolean preCancelEvent = false;
        if (gameType.isAdventure() && !entityPlayer.isCurrentToolAdventureModeExempt(x, y, z))
        {
            preCancelEvent = true;
        }
        else if (gameType.isCreative() && entityPlayer.getHeldItem() != null && entityPlayer.getHeldItem().getItem() instanceof ItemSword)
        {
            preCancelEvent = true;
        }

        // Tell client the block is gone immediately then process events
        if (world.getTileEntity(x, y, z) == null)
        {
            S23PacketBlockChange packet = new S23PacketBlockChange(x, y, z, world);
            packet.field_148883_d = Blocks.air;
            packet.field_148884_e = 0;
            entityPlayer.playerNetServerHandler.sendPacket(packet);
        }

        // Post the block break event
        Block block = world.getBlock(x, y, z);
        int blockMetadata = world.getBlockMetadata(x, y, z);
        BlockEvent.BreakEvent event = new DetravBlockBreakEvent(x, y, z, world, block, blockMetadata, entityPlayer);
        event.setCanceled(preCancelEvent);
        MinecraftForge.EVENT_BUS.post(event);

        // Handle if the event is canceled
        if (event.isCanceled())
        {
            // Let the client know the block still exists
            entityPlayer.playerNetServerHandler.sendPacket(new S23PacketBlockChange(x, y, z, world));

            // Update any tile entity data for this block
            TileEntity tileentity = world.getTileEntity(x, y, z);
            if (tileentity != null)
            {
                Packet pkt = tileentity.getDescriptionPacket();
                if (pkt != null)
                {
                    entityPlayer.playerNetServerHandler.sendPacket(pkt);
                }
            }
        }
        return event;
    }

    static int rayTrace(World w,Block block,int blockMetadata,Vec3 playerPos, Vec3 lookAtPos, int xBlock,int yBlock, int zBlock, boolean unknownVar1, boolean unknownVar2, boolean unknownVar3) {
        if (!Double.isNaN(playerPos.xCoord) && !Double.isNaN(playerPos.yCoord) && !Double.isNaN(playerPos.zCoord))
            if (!Double.isNaN(lookAtPos.xCoord) && !Double.isNaN(lookAtPos.yCoord) && !Double.isNaN(lookAtPos.zCoord)) {

                if ((!unknownVar2 || block.getCollisionBoundingBoxFromPool(w, xBlock, yBlock, zBlock) != null) && block.canCollideCheck(blockMetadata, unknownVar1)) {
                    MovingObjectPosition movingobjectposition = block.collisionRayTrace(w, xBlock, yBlock, zBlock, playerPos, lookAtPos);

                    if (movingobjectposition != null) {
                        return movingobjectposition.sideHit;
                    }
                }
                return -1;
            }
        return -1;
    }

    static boolean inited = false;
    public static void register()
    {
        if(!inited) {
            inited = true;
            BlockBreakEventHandler handler = new BlockBreakEventHandler();
            MinecraftForge.EVENT_BUS.register(handler);
            FMLCommonHandler.instance().bus().register(handler);
        }
    }
}
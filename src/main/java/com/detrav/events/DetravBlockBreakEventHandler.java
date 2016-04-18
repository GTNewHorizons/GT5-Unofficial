package com.detrav.events;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.items.GT_Generic_Item;
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
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

/**
 * Created by wital_000 on 13.04.2016.
 */
public class DetravBlockBreakEventHandler {
    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent ev) {
        if (ev.isCanceled())
            return;
        if (ev instanceof DetravBlockBreakEvent)
            return;
        if (!ev.world.isRemote) {
            EntityPlayer player = ev.getPlayer();
            if ((player instanceof EntityPlayerMP) && ((EntityPlayerMP) player).theItemInWorldManager.getGameType() != WorldSettings.GameType.SURVIVAL)
                return;
            if (player.capabilities.isCreativeMode)
                return;
            NBTTagCompound entityData = player.getEntityData();
            long minningMode = entityData.getLong("detrav.minning.mode");
            if (minningMode == 0) return;
            int side = 0;
            switch ((int) minningMode) {
                case 1:
                    Vec3 vec3 = Vec3.createVectorHelper(player.posX, player.posY + 1.62f, player.posZ);
                    Vec3 vec31 = ev.getPlayer().getLook(1.0F);
                    Vec3 vec32 = vec3.addVector(vec31.xCoord * 4.5F, vec31.yCoord * 4.5F, vec31.zCoord * 4.5F);
                    side = rayTrace(ev.world, ev.block, ev.blockMetadata, vec3, vec32, ev.x, ev.y, ev.z, false, false, true);
                    break;
                case 2:
                    side = 0;
                    break;
                case 3:
                    float rotationYaw = ev.getPlayer().rotationYaw;
                    while (rotationYaw > 0) rotationYaw -= 360F;
                    while (rotationYaw < -360) rotationYaw += 360F;
                    if ((-135 <= rotationYaw && rotationYaw <= -45) || (-315 <= rotationYaw && rotationYaw <= -225))
                        side = 4;
                    else if ((-225 <= rotationYaw && rotationYaw <= -135) || -45 <= rotationYaw || rotationYaw <= -315)
                        side = 2;
                    else side = -1;
                    break;
            }
            //ev.getPlayer().addChatMessage(new ChatComponentText(ev.block.getLocalizedName() + " | " + ev.getPlayer().rotationYaw + " | " + ev.getPlayer().rotationPitch));
            //ev.getPlayer().addChatMessage(new ChatComponentText(ev.block.getLocalizedName() + " | " + side));
            if (minningMode < 4)
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
            else {
                //    if (ev.getPlayer().rotationPitch > 45 || ev.getPlayer().rotationPitch < -45) {
                for (int i = ev.x - 1; i <= ev.x + 1; i++)
                    for (int j = ev.y - 1; j <= ev.y + 1; j++)
                        for (int k = ev.z - 1; k <= ev.z + 1; k++)
                            if (i != ev.x || j != ev.y || k != ev.z)
                                if (ev.block == ev.world.getBlock(i, j, k) && (ev.blockMetadata == ev.world.getBlockMetadata(i, j, k)))
                                    tryHarvestBlock(i, j, k, ev);
            }
        }
    }

    static private boolean tryHarvestBlock(int x, int y, int z, BlockEvent.BreakEvent event2) {
        if (!(event2.getPlayer() instanceof EntityPlayerMP))
            return false;//если это не игрок то выходим
        EntityPlayer thisPlayerMP = event2.getPlayer();
        ItemStack stack = thisPlayerMP.getCurrentEquippedItem();
        //получаем текущий тулс
        if (stack == null) return false;
        //выходим если в руках ничего нет
        World theWorld = event2.world;
        Block block = theWorld.getBlock(x, y, z);
        //int blockMetadata = theWorld.getBlockMetadata(x, y, z);
        // dirt and pickaxe -> if(true && false) -> exit
        // dirt and shovel -> if(true && false) -> exit

        //чекаем может ли предмет добыть блок
        //херня какаято с этими названиями функций

        //thisPlayerMP.addChatMessage(new ChatComponentText("Mining Speed: " + stack.getItem().getDigSpeed(stack,block,blockMetadata)));
        if (!isToolEffective(stack, theWorld, x, y, z))
            return false;
        int blockMetadata = theWorld.getBlockMetadata(x, y, z);
        if ((stack.getItem() instanceof GT_Generic_Item) && !(stack.getItem().getDigSpeed(stack, block, blockMetadata) > 0.0F))
            return false;

        BlockEvent.BreakEvent event = onDetravBlockBreakEvent(event2.world, WorldSettings.GameType.SURVIVAL, (EntityPlayerMP) event2.getPlayer(), x, y, z);
        if (event.isCanceled())
            return false;

        if (stack != null && stack.getItem().onBlockStartBreak(stack, x, y, z, thisPlayerMP)) {
            return false;
        }

        theWorld.playAuxSFXAtEntity(thisPlayerMP, 2001, x, y, z, Block.getIdFromBlock(block) + (theWorld.getBlockMetadata(x, y, z) << 12));
        boolean flag = false;
        ItemStack itemstack = thisPlayerMP.getCurrentEquippedItem();
        boolean flag1 = block.canHarvestBlock(thisPlayerMP, blockMetadata);

        if (itemstack != null) {
            itemstack.func_150999_a(theWorld, block, x, y, z, thisPlayerMP);
            if (itemstack.stackSize == 0) {
                thisPlayerMP.destroyCurrentEquippedItem();
            }
        }
        flag = removeBlock(x, y, z, flag1, event);
        if (flag && flag1) {
            block.harvestBlock(theWorld, thisPlayerMP, x, y, z, blockMetadata);
        }
        if (flag && event != null) {
            block.dropXpOnBlockBreak(theWorld, x, y, z, event.getExpToDrop());
        }
        return flag;
    }

    static private boolean removeBlock(int x, int y, int z, boolean canHarvest, BlockEvent.BreakEvent event) {
        World theWorld = event.world;
        EntityPlayer thisPlayerMP = event.getPlayer();
        Block block = theWorld.getBlock(x, y, z);
        int l = theWorld.getBlockMetadata(x, y, z);
        block.onBlockHarvested(theWorld, x, y, z, l, thisPlayerMP);
        boolean flag = block.removedByPlayer(theWorld, thisPlayerMP, x, y, z, canHarvest);

        if (flag) {
            block.onBlockDestroyedByPlayer(theWorld, x, y, z, l);
        }

        return flag;
    }

    public static BlockEvent.BreakEvent onDetravBlockBreakEvent(World world, WorldSettings.GameType gameType, EntityPlayerMP entityPlayer, int x, int y, int z) {
        // Logic from tryHarvestBlock for pre-canceling the event
        boolean preCancelEvent = false;
        if (gameType.isAdventure() && !entityPlayer.isCurrentToolAdventureModeExempt(x, y, z)) {
            preCancelEvent = true;
        } else if (gameType.isCreative() && entityPlayer.getHeldItem() != null && entityPlayer.getHeldItem().getItem() instanceof ItemSword) {
            preCancelEvent = true;
        }

        // Tell client the block is gone immediately then process events
        if (world.getTileEntity(x, y, z) == null) {
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
        if (event.isCanceled()) {
            // Let the client know the block still exists
            entityPlayer.playerNetServerHandler.sendPacket(new S23PacketBlockChange(x, y, z, world));

            // Update any tile entity data for this block
            TileEntity tileentity = world.getTileEntity(x, y, z);
            if (tileentity != null) {
                Packet pkt = tileentity.getDescriptionPacket();
                if (pkt != null) {
                    entityPlayer.playerNetServerHandler.sendPacket(pkt);
                }
            }
        }
        return event;
    }

    static int rayTrace(World w, Block block, int blockMetadata, Vec3 playerPos, Vec3 lookAtPos, int xBlock, int yBlock, int zBlock, boolean unknownVar1, boolean unknownVar2, boolean unknownVar3) {
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

    public static void register() {
        if (!inited) {
            inited = true;
            DetravBlockBreakEventHandler handler = new DetravBlockBreakEventHandler();
            MinecraftForge.EVENT_BUS.register(handler);
            FMLCommonHandler.instance().bus().register(handler);
        }
    }


    public static boolean isToolEffective(ItemStack stack, World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        if (block.getBlockHardness(world, x, y, z) < 0) //unbreakable
            return false;
        else
            return isToolEffective(stack, block, world.getBlockMetadata(x, y, z)) || block.getBlockHardness(world, x, y, z) == 0;
    }

    public static boolean isToolEffective(ItemStack stack, Block block, int meta) {
        if (block == null)
            return false;

        if (stack != null) {
            for (String toolClass : stack.getItem().getToolClasses(stack)) {
                if (toolClass.equals(block.getHarvestTool(meta)))
                    return stack.getItem().getHarvestLevel(stack, toolClass) >= block.getHarvestLevel(meta);
            }

            return stack.getItem().canHarvestBlock(block, stack);
        }
        return false;
    }
}
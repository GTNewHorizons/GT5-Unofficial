package com.github.technus.tectech.mechanics.data;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import eu.usrv.yamcore.network.client.AbstractClientMessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import thaumcraft.client.fx.bolt.FXLightningBolt;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;

public class RendererMessage implements IMessage {
    HashSet<ThaumSpark> sparkList = new HashSet<ThaumSpark>();

    public RendererMessage() {
    }

    @Override
    public void fromBytes(ByteBuf pBuffer) {
        try {
            //I'd love to know why I need to offset by one byte for this to work
            byte[] boop = pBuffer.array();
            boop = Arrays.copyOfRange(boop, 1, boop.length);
            InputStream is = new ByteArrayInputStream(boop);
            ObjectInputStream ois = new ObjectInputStream(is);
            Object data = ois.readObject();
            sparkList = (HashSet<ThaumSpark>) data;
        } catch (IOException | ClassNotFoundException ex) {
        }
    }

    @Override
    public void toBytes(ByteBuf pBuffer) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(sparkList);
            oos.flush();
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            pBuffer.writeBytes(is, baos.toByteArray().length);
        } catch (IOException ex) {
        }
    }

    public static class RendererData extends RendererMessage {
        public RendererData() {
        }

        public RendererData(HashSet<ThaumSpark> eSparkList) {
            sparkList = eSparkList;
        }
    }


    public static class ClientHandler extends AbstractClientMessageHandler<RendererData> {
        @Override
        public IMessage handleClientMessage(EntityPlayer pPlayer, RendererData pMessage, MessageContext pCtx) {
            for (ThaumSpark sp : pMessage.sparkList) {
                thaumLightning(sp.x, sp.y, sp.z, sp.xR, sp.yR, sp.zR, sp.wID);
            }
            pMessage.sparkList.clear();
            return null;
        }
    }

    private static void thaumLightning(int tX, int tY, int tZ, int tXN, int tYN, int tZN, int wID) {
        //This is enough to check for thaum, since it only ever matters for client side effects (Tested not to crash)
        if (Loader.isModLoaded("Thaumcraft")) {
            World world = DimensionManager.getWorld(wID);
            FXLightningBolt bolt = new FXLightningBolt(world, tX + 0.5F, tY + 0.5F, tZ + 0.5F, tX + tXN + 0.5F, tY + tYN + 0.5F, tZ + tZN + 0.5F, world.rand.nextLong(), 6, 0.5F, 8);
            bolt.defaultFractal();
            bolt.setType(2);
            bolt.setWidth(0.125F);
            bolt.finalizeBolt();
        }
    }
}
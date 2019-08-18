package com.github.technus.tectech.mechanics.data;

import com.github.technus.tectech.Util;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import eu.usrv.yamcore.network.client.AbstractClientMessageHandler;
import eu.usrv.yamcore.network.server.AbstractServerMessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import thaumcraft.client.fx.bolt.FXLightningBolt;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;

public class RendererMessage implements IMessage {
    HashSet<Util.thaumSpark> sparkList = new HashSet<Util.thaumSpark>();

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
            sparkList = (HashSet<Util.thaumSpark>) data;
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


    public static class RendererQuery extends RendererMessage {
        public RendererQuery() {
        }
    }

    public static class RendererData extends RendererMessage {
        public RendererData() {
        }

        public RendererData(RendererQuery query) {
            sparkList = query.sparkList;
        }

        public RendererData(HashSet<Util.thaumSpark> eSparkList) {
            sparkList = eSparkList;
        }
    }


    public static class ClientHandler extends AbstractClientMessageHandler<RendererData> {
        @Override
        public IMessage handleClientMessage(EntityPlayer pPlayer, RendererData pMessage, MessageContext pCtx) {
            for (Util.thaumSpark sp : pMessage.sparkList) {
                thaumLightning(sp.x, sp.y, sp.z, sp.xR, sp.yR, sp.zR, sp.wID);
            }
            pMessage.sparkList.clear();
            return null;
        }
    }

    public static class ServerHandler extends AbstractServerMessageHandler<RendererQuery> {
        @Override
        public IMessage handleServerMessage(EntityPlayer pPlayer, RendererQuery pMessage, MessageContext pCtx) {
            return new RendererData(pMessage);
        }
    }

    private static void thaumLightning(int tX, int tY, int tZ, int tXN, int tYN, int tZN, int wID) {
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

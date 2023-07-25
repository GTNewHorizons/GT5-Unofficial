package com.github.technus.tectech.mechanics.spark;

import static gregtech.api.enums.Mods.Thaumcraft;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import eu.usrv.yamcore.network.client.AbstractClientMessageHandler;
import io.netty.buffer.ByteBuf;
import thaumcraft.client.fx.bolt.FXLightningBolt;

// TODO Re-work how sparks are distributed
public class RendererMessage implements IMessage {

    HashSet<ThaumSpark> sparkList;

    public RendererMessage() {}

    @SuppressWarnings("unchecked")
    @Override
    public void fromBytes(ByteBuf pBuffer) {
        if (FMLCommonHandler.instance().getSide().isServer()) return;
        try {
            // I'd love to know why I need to offset by one byte for this to work
            byte[] boop = pBuffer.array();
            boop = Arrays.copyOfRange(boop, 1, boop.length);
            InputStream is = new ByteArrayInputStream(boop);
            ObjectInputStream ois = new ValidatingObjectInputStream(is);
            Object data = ois.readObject();
            sparkList = (HashSet<ThaumSpark>) data;
        } catch (IOException | ClassNotFoundException ignored) {}
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
        } catch (IOException ignore) {}
    }

    public static class RendererData extends RendererMessage {

        public RendererData() {}

        public RendererData(HashSet<ThaumSpark> eSparkList) {
            sparkList = eSparkList;
        }
    }

    public static class ClientHandler extends AbstractClientMessageHandler<RendererData> {

        @Override
        public IMessage handleClientMessage(EntityPlayer pPlayer, RendererData pMessage, MessageContext pCtx) {
            // disgusting
            Random localRand = Minecraft.getMinecraft().theWorld.rand;
            int[] zapsToUse = new int[4];
            for (int i = 0; i < 3; i++) {
                zapsToUse[i] = localRand.nextInt(pMessage.sparkList.size());
            }
            int i = 0;
            for (ThaumSpark sp : pMessage.sparkList) {
                for (int j : zapsToUse) {
                    if (i == j) {
                        thaumLightning(sp.x, sp.y, sp.z, sp.xR, sp.yR, sp.zR, sp.wID);
                    }
                }
                i++;
            }
            pMessage.sparkList.clear();
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    private static void thaumLightning(int tX, int tY, int tZ, int tXN, int tYN, int tZN, int wID) {
        // This is enough to check for thaum, since it only ever matters for client side effects (Tested not to crash)
        if (Thaumcraft.isModLoaded()) {
            World world = Minecraft.getMinecraft().theWorld;
            if (world.provider.dimensionId == wID) {
                FXLightningBolt bolt = new FXLightningBolt(
                        world,
                        tX + 0.5F,
                        tY + 0.5F,
                        tZ + 0.5F,
                        tX + tXN + 0.5F,
                        tY + tYN + 0.5F,
                        tZ + tZN + 0.5F,
                        world.rand.nextLong(),
                        6,
                        0.5F,
                        8);
                bolt.defaultFractal();
                bolt.setType(2);
                bolt.setWidth(0.125F);
                bolt.finalizeBolt();
            }
        }
    }

    private static class ValidatingObjectInputStream extends ObjectInputStream {

        private static final Logger logger = LogManager.getLogger();
        private static final Marker securityMarker = MarkerManager.getMarker("SuspiciousPackets");

        private ValidatingObjectInputStream(InputStream in) throws IOException {
            super(in);
        }

        @Override
        protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            String name = desc.getName();
            if (!name.equals("java.util.HashSet")
                    && !name.equals("com.github.technus.tectech.mechanics.spark.ThaumSpark")) {
                logger.warn(securityMarker, "Received packet containing disallowed class: " + name);
                throw new RuntimeException();
            }
            return super.resolveClass(desc);
        }
    }
}

package tectech.mechanics.spark;

import static gregtech.api.enums.Mods.Thaumcraft;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import thaumcraft.client.fx.bolt.FXLightningBolt;

// TODO Re-work how sparks are distributed
public class RendererMessage implements IMessage {

    Set<ThaumSpark> sparkList;

    public RendererMessage() {}

    @Override
    public void fromBytes(ByteBuf buffer) {
        sparkList = new HashSet<>();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++) {
            sparkList.add(new ThaumSpark(buffer));
        }
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(sparkList.size());
        for (ThaumSpark spark : sparkList) {
            spark.writeToBuf(buffer);
        }
    }

    public static class RendererData extends RendererMessage {

        public RendererData() {}

        public RendererData(Set<ThaumSpark> eSparkList) {
            sparkList = eSparkList;
        }
    }

    public static class ClientHandler implements IMessageHandler<RendererData, IMessage> {

        @Override
        public IMessage onMessage(RendererData message, MessageContext ctx) {
            // disgusting
            Random localRand = Minecraft.getMinecraft().theWorld.rand;
            int[] zapsToUse = new int[4];
            for (int i = 0; i < 3; i++) {
                zapsToUse[i] = localRand.nextInt(message.sparkList.size());
            }
            int i = 0;
            for (ThaumSpark spark : message.sparkList) {
                for (int j : zapsToUse) {
                    if (i == j) {
                        thaumLightning(spark);
                    }
                }
                i++;
            }
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    private static void thaumLightning(ThaumSpark spark) {
        // This is enough to check for thaum, since it only ever matters for client side effects (Tested not to crash)
        if (Thaumcraft.isModLoaded()) {
            World world = Minecraft.getMinecraft().theWorld;
            if (world.provider.dimensionId == spark.wID) {
                FXLightningBolt bolt = new FXLightningBolt(
                    world,
                    spark.x + 0.5F,
                    spark.y + 0.5F,
                    spark.z + 0.5F,
                    spark.x + spark.xR + 0.5F,
                    spark.y + spark.yR + 0.5F,
                    spark.z + spark.zR + 0.5F,
                    world.rand.nextLong(),
                    10,
                    0.5F,
                    8);
                bolt.defaultFractal();
                bolt.setType(2);
                bolt.setWidth(0.125F);
                bolt.finalizeBolt();
            }
        }
    }
}

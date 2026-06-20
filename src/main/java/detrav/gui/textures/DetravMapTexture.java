package detrav.gui.textures;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import detrav.items.DetravMetaGeneratedTool01;
import detrav.net.ProspectingPacket;
import gregtech.api.util.GTUtility;

/**
 * Created by wital_000 on 21.03.2016.
 */
public class DetravMapTexture extends AbstractTexture {

    public final ProspectingPacket packet;
    private String selected = "All";
    public int width = -1;
    public int height = -1;
    public boolean invert = false;
    private short[] topId = null;
    private int[] topY = null;

    public DetravMapTexture(ProspectingPacket aPacket) {
        packet = aPacket;
    }

    private BufferedImage getImage() {
        final int backgroundColor = invert ? Color.GRAY.getRGB() : Color.WHITE.getRGB();
        final int blockSize = (packet.size * 2 + 1) * 16;
        final int chunkSize = packet.size * 2 + 1;
        topId = null;
        topY = null;

        BufferedImage image = new BufferedImage(blockSize, blockSize, BufferedImage.TYPE_INT_ARGB);
        WritableRaster raster = image.getRaster();

        int playerI = packet.posX - (packet.chunkX - packet.size) * 16 - 1; // Correct player offset
        int playerJ = packet.posZ - (packet.chunkZ - packet.size) * 16 - 1;

        for (int y = 0; y < blockSize; y++) {
            for (int x = 0; x < blockSize; x++) {
                image.setRGB(x, y, backgroundColor);
            }
        }

        switch (packet.ptype) {
            case DetravMetaGeneratedTool01.MODE_BIG_ORES, DetravMetaGeneratedTool01.MODE_ALL_ORES -> {

                short[] depth = new short[blockSize * blockSize];
                Arrays.fill(depth, (short) 0);

                topId = new short[blockSize * blockSize];
                Arrays.fill(topId, (short) -1);
                topY = new int[blockSize * blockSize];

                short selectedId = -1;

                if (!selected.equals("All")) {
                    for (var e : packet.objects.short2ObjectEntrySet()) {
                        if (selected.equals(
                            e.getValue()
                                .left())) {
                            selectedId = e.getShortKey();
                            break;
                        }
                    }
                }

                for (var e : packet.map.long2ShortEntrySet()) {
                    if (selectedId != -1 && selectedId != e.getShortValue()) continue;

                    long coord = e.getLongKey();

                    int x = CoordinatePacker.unpackX(coord);
                    int y = CoordinatePacker.unpackY(coord);
                    int z = CoordinatePacker.unpackZ(coord);

                    int idx = x + z * blockSize;
                    if (y < depth[idx]) continue;
                    depth[idx] = (short) y;

                    var object = packet.objects.get(e.getShortValue());

                    if (object == null) continue;

                    topId[idx] = e.getShortValue();
                    topY[idx] = y;
                    image.setRGB(x, z, object.rightInt());
                }
            }
            case DetravMetaGeneratedTool01.MODE_FLUIDS -> {
                for (int cZ = 0; cZ < chunkSize; cZ++) {
                    for (int cX = 0; cX < chunkSize; cX++) {
                        int amount = packet.getAmount(cX, cZ);

                        var object = packet.objects.get(packet.map.get(CoordinatePacker.pack(cX, 0, cZ)));

                        if (object == null) continue;

                        String name = object.left();
                        int rgba = object.rightInt();

                        if (!selected.equals("All") && !selected.equals(name)) continue;

                        for (int x = 0; x < 16; x++) {
                            for (int y = 0; y < 16; y++) {
                                if ((x + y * 16) * 3 < amount + 48) {
                                    image.setRGB(cX * 16 + x, cZ * 16 + y, rgba);
                                }
                            }
                        }
                    }
                }
            }
            case DetravMetaGeneratedTool01.MODE_POLLUTION -> {
                for (int cZ = 0; cZ < chunkSize; cZ++) {
                    for (int cX = 0; cX < chunkSize; cX++) {
                        int amount = packet.getAmount(cX, cZ);

                        if (amount == 0) continue;

                        float mult = amount / 500000f;

                        if (!invert) mult = 1f - mult;

                        mult = GTUtility.clamp(mult, 0, 1);

                        for (int x = 0; x < 16; x++) {
                            for (int y = 0; y < 16; y++) {
                                int x2 = cX * 16 + x;
                                int y2 = cZ * 16 + y;

                                raster.setSample(x2, y2, 0, (int) (raster.getSample(x2, y2, 0) * mult));
                                raster.setSample(x2, y2, 1, (int) (raster.getSample(x2, y2, 1) * mult));
                                raster.setSample(x2, y2, 2, (int) (raster.getSample(x2, y2, 2) * mult));
                            }
                        }
                    }
                }
            }
        }

        for (int y = 0; y < blockSize; y++) {
            for (int x = 0; x < blockSize; x++) {
                // draw grid
                if (x % 16 == 0 || y % 16 == 0) {
                    raster.setSample(x, y, 0, raster.getSample(x, y, 0) / 2);
                    raster.setSample(x, y, 1, raster.getSample(x, y, 1) / 2);
                    raster.setSample(x, y, 2, raster.getSample(x, y, 2) / 2);
                }

                // draw player pos
                if (x == playerI || y == playerJ) {
                    raster.setSample(x, y, 0, (raster.getSample(x, y, 0) + 255) / 2);
                    raster.setSample(x, y, 1, raster.getSample(x, y, 1) / 2);
                    raster.setSample(x, y, 2, raster.getSample(x, y, 2) / 2);
                }
            }
        }

        return image;
    }

    /** Display name of the topmost ore drawn at the given block column. */
    public String getTopOreName(int x, int z) {
        int bs = packet.getSize();
        if (topId == null || x < 0 || z < 0 || x >= bs || z >= bs) return null;
        short id = topId[x + z * bs];
        if (id < 0) return null;
        var object = packet.objects.get(id);
        return object == null ? null : object.left();
    }

    /** World Y of the topmost ore drawn at the given block column. */
    public int getTopOreY(int x, int z) {
        int bs = packet.getSize();
        if (topY == null || x < 0 || z < 0 || x >= bs || z >= bs) return 0;
        return topY[x + z * bs];
    }

    @Override
    public void loadTexture(IResourceManager resourceManager) {
        this.deleteGlTexture();
        if (packet != null) {
            int tId = getGlTextureId();
            if (tId < 0) return;
            TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), getImage(), false, false);
            width = packet.getSize();
            height = packet.getSize();
        }
    }

    public void loadTexture(IResourceManager resourceManager, boolean invert) {
        this.invert = invert;
        loadTexture(resourceManager);
    }

    public void loadTexture(IResourceManager resourceManager, String selected, boolean invert) {
        this.selected = selected;
        loadTexture(resourceManager, invert);
    }

    public int glBindTexture() {
        if (this.glTextureId < 0) return this.glTextureId;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.getGlTextureId());
        return this.glTextureId;
    }

    public void draw(int x, int y) {
        draw(x, y, width, height);
    }

    public void draw(int x, int y, int w, int h) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + h, 0, 0F, 1F);
        tessellator.addVertexWithUV(x + w, y + h, 0, 1F, 1F);
        tessellator.addVertexWithUV(x + w, y, 0, 1F, 0F);
        tessellator.addVertexWithUV(x, y, 0, 0F, 0F);
        tessellator.draw();
    }

}

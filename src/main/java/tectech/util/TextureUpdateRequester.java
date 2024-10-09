package tectech.util;

import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.client.renderer.CapturingTessellator;
import com.gtnewhorizon.gtnhlib.client.renderer.TessellatorManager;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.DefaultVertexFormat;

// Ugly hack to wake up angelica/hodgepodge to update the texture by fake rendering out a block
public class TextureUpdateRequester {

    private final HashSet<Pair<Block, Integer>> blocks = new HashSet<>();

    public void add(Block block, int meta) {
        blocks.add(Pair.of(block, meta));
    }

    // Using capturing tesselator just to make sure we dont render anything out
    public void requestUpdate() {
        GL11.glPushMatrix();
        TessellatorManager.startCapturing();
        CapturingTessellator tess = (CapturingTessellator) TessellatorManager.get();
        FaceCulledRenderBlocks renderer = new FaceCulledRenderBlocks(Minecraft.getMinecraft().theWorld);
        renderer.setFaceVisibility(new FaceVisibility());
        for (Pair<Block, Integer> block : blocks) {
            renderer.renderBlockAsItem(block.getLeft(), block.getRight(), 1f);
        }
        tess.setTranslation(0, 0, 0);
        TessellatorManager.stopCapturingToBuffer(DefaultVertexFormat.POSITION_TEXTURE_NORMAL);
        GL11.glPopMatrix();
    }
}

package tectech.util;

import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;

import org.apache.commons.lang3.tuple.Pair;

import com.gtnewhorizon.gtnhlib.client.renderer.CapturingTessellator;
import com.gtnewhorizon.gtnhlib.client.renderer.TessellatorManager;
import com.gtnewhorizon.gtnhlib.client.renderer.vbo.VertexBuffer;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.DefaultVertexFormat;

public class StructureVBO {

    private String[][] structure;

    private HashSet<Character> values = new HashSet<>();
    private HashMap<Character, Pair<Block, Integer>> mapper = new HashMap<>();

    public StructureVBO assignStructure(String[][] structure) {
        this.structure = structure;
        return this;
    }

    public StructureVBO addMapping(char letter, Block block) {
        mapper.put(letter, Pair.of(block, 0));
        return this;
    }

    public StructureVBO addMapping(char letter, Block block, int meta) {
        mapper.put(letter, Pair.of(block, meta));
        return this;
    }

    private boolean isOpaqueAt(int x, int y, int z) {
        char letter = structure[x][y].charAt(z);
        if (letter == ' ') return false;
        Pair<Block, Integer> info = mapper.get(letter);
        if (info == null) return false;
        if (info.getLeft() == Blocks.air) return false;
        return info.getLeft()
            .isOpaqueCube();
    }

    private FaceVisibility getVisibleFaces(int x, int y, int z) {
        FaceVisibility visibility = new FaceVisibility();
        int maxX = structure.length - 1;
        int maxY = structure[0].length - 1;
        int maxZ = structure[0][0].length() - 1;
        // X is ordered from left to right
        if ((x > 0) && (isOpaqueAt(x - 1, y, z))) visibility.left = false;
        if ((x < maxX) && (isOpaqueAt(x + 1, y, z))) visibility.right = false;
        // Y is ordered from top to bottom
        if ((y > 0) && (isOpaqueAt(x, y - 1, z))) visibility.top = false;
        if ((y < maxY) && (isOpaqueAt(x, y + 1, z))) visibility.bottom = false;
        // Z is ordered from front to back
        if ((z > 0) && (isOpaqueAt(x, y, z - 1))) visibility.back = false;
        if ((z < maxZ) && (isOpaqueAt(x, y, z + 1))) visibility.front = false;
        return visibility;
    }

    public VertexBuffer build() {
        TessellatorManager.startCapturing();
        CapturingTessellator tess = (CapturingTessellator) TessellatorManager.get();
        FaceCulledRenderBlocks renderer = new FaceCulledRenderBlocks(Minecraft.getMinecraft().theWorld);
        renderer.enableAO = false;

        for (int x = 0; x < structure.length; x++) {
            String[] plane = structure[x];
            for (int y = 0; y < plane.length; y++) {
                String row = plane[y];
                for (int z = 0; z < row.length(); z++) {
                    char letter = row.charAt(z);
                    if (letter == ' ') continue;
                    Pair<Block, Integer> info = mapper.get(letter);
                    if (info == null) {
                        values.add(letter);
                        continue;
                    }
                    if (info.getLeft() == Blocks.air) continue;

                    FaceVisibility faceInfo = getVisibleFaces(x, y, z);
                    renderer.setFaceVisibility(faceInfo);
                    // The floor division is intended to produce proper offsets
                    tess.setTranslation(
                        -structure.length / 2f + x,
                        plane.length / 2f - y, // y needs to be drawn from top to bottom
                        -row.length() / 2f + z);
                    renderer.renderBlockAsItem(info.getLeft(), info.getRight(), 1f);
                }
            }
        }

        for (char value : values) {
            System.out.println(value);
        }

        return TessellatorManager.stopCapturingToVBO(DefaultVertexFormat.POSITION_TEXTURE_NORMAL);
    }
}

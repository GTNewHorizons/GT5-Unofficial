package gregtech.common.misc;

import com.github.bsideup.jabel.Desugar;

@Desugar
public record WorkAreaChunk(int chunkX, int chunkZ, int order) {}

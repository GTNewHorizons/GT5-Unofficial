package gregtech.common.data.maglev;

import com.github.bsideup.jabel.Desugar;

@Desugar
public record Tether(int sourceX, int sourceY, int sourceZ, int dimID, int range, byte tier) {

}

package gregtech.common;

import net.minecraft.block.Block;

public class StoneBuilder {

    public String stoneName;
    public Block block;
    public int blockMeta;
    public int dimension;
    public int minY, maxY;
    public int amount, size, probability;
    public boolean enabledByDefault = true;
    public boolean allowToGenerateInVoid;

    public StoneBuilder name(String name) {
        this.stoneName = name;
        return this;
    }

    public StoneBuilder block(Block block) {
        this.block = block;
        return this;
    }

    public StoneBuilder blockMeta(int blockMeta) {
        this.blockMeta = blockMeta;
        return this;
    }

    public StoneBuilder disabledByDefault() {
        this.enabledByDefault = false;
        return this;
    }

    public StoneBuilder generationInVoidEnabled(boolean allowToGenerateInVoid) {
        this.allowToGenerateInVoid = allowToGenerateInVoid;
        return this;
    }

    public StoneBuilder heightRange(int minY, int maxY) {
        this.minY = minY;
        this.maxY = maxY;
        return this;
    }

    public StoneBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public StoneBuilder size(int size) {
        this.size = size;
        return this;
    }

    public StoneBuilder probability(int probability) {
        this.probability = probability;
        return this;
    }

    public StoneBuilder dimension(int dimension) {
        this.dimension = dimension;
        return this;
    }
}

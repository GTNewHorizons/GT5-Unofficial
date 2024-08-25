package gregtech.common;

import gregtech.api.enums.Materials;

public class OreMixBuilder {
    private String oreMixName;
    private boolean enabledByDefault = true;
    private boolean inOverworld = false, inNether = false, inEnd = false;
    private int minY, maxY, weight, density, size;
    private Materials primary, secondary, between, sporadic;

    public GT_Worldgen_GT_Ore_Layer add(){
        return new GT_Worldgen_GT_Ore_Layer(oreMixName, enabledByDefault, minY, maxY, weight, density, size, inOverworld, inNether, inEnd, primary, secondary, between, sporadic);
    }

    public OreMixBuilder name(String name){
        this.oreMixName = name;
        return this;
    }

    public OreMixBuilder disabledByDefault(){
        this.enabledByDefault = false;
        return this;
    }

    public OreMixBuilder inOverworld(){
        this.inOverworld = true;
        return this;
    }

    public OreMixBuilder inNether(){
        this.inNether = true;
        return this;
    }

    public OreMixBuilder inEnd(){
        this.inEnd = true;
        return this;
    }

    public OreMixBuilder heightRange(int minY, int maxY){
        this.minY = minY;
        this.maxY = maxY;
        return this;
    }

    public OreMixBuilder density(int density){
        this.density = density;
        return this;
    }

    public OreMixBuilder weight(int weight){
        this.weight = weight;
        return this;
    }

    public OreMixBuilder size(int size){
        this.size = size;
        return this;
    }

    public OreMixBuilder primary(Materials primary){
        this.primary = primary;
        return this;
    }

    public OreMixBuilder secondary(Materials secondary){
        this.secondary = secondary;
        return this;
    }

    public OreMixBuilder inBetween(Materials between){
        this.between = between;
        return this;
    }

    public OreMixBuilder sporadic(Materials sporadic){
        this.sporadic = sporadic;
        return this;
    }

}

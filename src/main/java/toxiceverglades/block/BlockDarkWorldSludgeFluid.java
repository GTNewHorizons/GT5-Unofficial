package toxiceverglades.block;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraftforge.fluids.Fluid;

import gregtech.api.GregTechAPI;

public class BlockDarkWorldSludgeFluid extends Fluid implements Runnable {

    public static final Material SLUDGE = new MaterialLiquid(MapColor.dirtColor);

    private int mapColor = 0xFFFFFFFF;
    private float overlayAlpha;
    private Material material = SLUDGE;

    public BlockDarkWorldSludgeFluid(String fluidName, int rgbColour) {
        this(fluidName, rgbColour, null);
    }

    public BlockDarkWorldSludgeFluid(String fluidName, int rgbColour, Float overlayAlpha) {
        super(fluidName);

        setColor(rgbColour);
        setAlpha(overlayAlpha != null ? overlayAlpha : 0.0F);

        if (GregTechAPI.sGTBlockIconload != null) {
            GregTechAPI.sGTBlockIconload.add(this);
        }
    }

    @Override
    public void run() {
        this.setIcons(
            GregTechAPI.sBlockIcons.registerIcon(GTPlusPlus.ID + ":fluid/Fluid_Sludge_Still"),
            GregTechAPI.sBlockIcons.registerIcon(GTPlusPlus.ID + ":fluid/Fluid_Sludge_Flow"));
    }

    @Override
    public int getColor() {
        return mapColor;
    }

    public BlockDarkWorldSludgeFluid setColor(int color) {
        this.mapColor = color;
        return this;
    }

    public float getAlpha() {
        return overlayAlpha;
    }

    public BlockDarkWorldSludgeFluid setAlpha(float alpha) {
        this.overlayAlpha = alpha;
        return this;
    }

    public BlockDarkWorldSludgeFluid setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public Material getMaterial() {
        return material;
    }
}

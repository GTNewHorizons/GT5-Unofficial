package gtPlusPlus.core.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.client.model.tabula.ModelTabulaBase;
import gtPlusPlus.core.client.renderer.tabula.RenderTabulaBase;
import gtPlusPlus.core.tileentities.general.TileEntityEggBox;

/**
 * ModelEggBox - Alkalus Created using Tabula 4.1.1
 */
public class ModelEggBox extends ModelTabulaBase {

    private final AutoMap<Pair<ModelRenderer, Float>> mParts = new AutoMap<Pair<ModelRenderer, Float>>();

    private static RenderTabulaBase mRendererInstance;

    public ModelRenderer bottom;
    // EggBox_full.png

    public ModelEggBox() {
        super(64, 64);
        this.textureWidth = 64;
        this.textureHeight = 64;

        this.bottom = new ModelRenderer(this, 0, 19);
        this.bottom.setRotationPoint(1.0F, 6.0F, 1.0F);
        this.bottom.addBox(0.0F, 0.0F, 0.0F, 14, 10, 14, 0.0F);
        mParts.add(new Pair<ModelRenderer, Float>(bottom, 0f));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        // Logger.INFO("Rendering EggBox");
        this.bottom.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    @Override
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    protected AutoMap<Pair<ModelRenderer, Float>> getModelParts() {
        AutoMap<Pair<ModelRenderer, Float>> aParts = new AutoMap<Pair<ModelRenderer, Float>>();
        aParts.add(new Pair<ModelRenderer, Float>(bottom, 0.0625F));
        return aParts;
        // return mParts;
    }

    public static RenderTabulaBase getRenderer() {
        if (mRendererInstance == null) {
            mRendererInstance = new RenderTabulaBase(
                    new ModelEggBox(),
                    "textures/blocks/TileEntities/EggBox_full.png",
                    TileEntityEggBox.class);
        }
        return mRendererInstance;
    }
}

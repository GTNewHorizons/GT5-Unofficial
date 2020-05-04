package gtPlusPlus.core.client.model.tabula;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

/**
 * ModelEggBox - Alkalus
 * Created using Tabula 4.1.1
 */
public abstract class ModelTabulaBase extends ModelBase {
	
	
    public ModelTabulaBase(int aTexWidth, int aTexHeight) {
        this.textureWidth = aTexWidth;
        this.textureHeight = aTexHeight;
    }
    
    protected abstract AutoMap<Pair<ModelRenderer, Float>> getModelParts();
    
    public void renderAll() {
    	for (Pair<ModelRenderer, Float> part : getModelParts()) {
        	//Logger.INFO("Rendering EggBox");
    		part.getKey().render(part.getValue());
    	}
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}

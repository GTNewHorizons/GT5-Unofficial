package gtPlusPlus.core.client.renderer.tabula;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.client.model.tabula.ModelTabulaBase;

@SideOnly(Side.CLIENT)
public class RenderTabulaBase extends TileEntitySpecialRenderer {

    private final ModelTabulaBase mModel;
    private final ResourceLocation mTexture;
    private final Class mTileClass;

    public final int mRenderID;
    public final RenderTabulaBase mInstance;

    public RenderTabulaBase(ModelTabulaBase aModel, String aTexturePath, Class aTileClass) {
        mModel = aModel;
        mTexture = new ResourceLocation(GTPlusPlus.ID, aTexturePath);
        mTileClass = aTileClass;
        this.mRenderID = RenderingRegistry.getNextAvailableRenderId();
        mInstance = this;
    }

    public void renderTileEntityAt(Object aTile, double p_147500_2_, double p_147500_4_, double p_147500_6_,
            float p_147500_8_) {
        if (mTileClass.isInstance(aTile)) {
            // Logger.INFO("Rendering EggBox");
            this.bindTexture(mTexture);
            mModel.renderAll();
        }
    }

    public void renderTileEntityAt(TileEntity aTile, double p_147500_2_, double p_147500_4_, double p_147500_6_,
            float p_147500_8_) {
        if (mTileClass != null && aTile != null) {
            if (mTileClass.isInstance(aTile)) {
                this.renderTileEntityAt((Object) aTile, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_);
            }
        }
    }
}

package gregtech.api.modernmaterials;

import gregtech.api.modernmaterials.blocks.blocktypes.blockof.special.UniversiumBlockOfBlockRenderer;
import gregtech.api.modernmaterials.blocks.blocktypes.blockof.special.UniversiumBlockOfItemRenderer;
import gregtech.api.modernmaterials.blocks.blocktypes.framebox.special.UniversiumFrameBlockRenderer;
import gregtech.api.modernmaterials.blocks.blocktypes.framebox.special.UniversiumFrameItemRenderer;
import gregtech.api.modernmaterials.blocks.registration.BlocksEnum;

public class UniversiumRendererRegistration extends RendererRegistration {
    @Override
    protected void registerRenderers(ModernMaterial.ModernMaterialBuilder builder) {
        builder.setCustomBlockRenderer(
            BlocksEnum.FrameBox,
            new UniversiumFrameItemRenderer(),
            new UniversiumFrameBlockRenderer())
            .setCustomBlockRenderer(
                BlocksEnum.SolidBlock,
                new UniversiumBlockOfItemRenderer(),
                new UniversiumBlockOfBlockRenderer());
    }
}

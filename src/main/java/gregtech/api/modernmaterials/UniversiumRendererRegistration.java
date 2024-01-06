package gregtech.api.modernmaterials;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.modernmaterials.blocks.blocktypes.blockof.special.UniversiumBlockOfBlockRenderer;
import gregtech.api.modernmaterials.blocks.blocktypes.blockof.special.UniversiumBlockOfItemRenderer;
import gregtech.api.modernmaterials.blocks.blocktypes.framebox.special.UniversiumFrameBlockRenderer;
import gregtech.api.modernmaterials.blocks.blocktypes.framebox.special.UniversiumFrameItemRenderer;
import gregtech.api.modernmaterials.blocks.registration.BlocksEnum;

public class UniversiumRendererRegistration extends RendererRegistration {

    public static void initRenderersSafely(ModernMaterial.ModernMaterialBuilder builder) {
        registerRenderers(builder);
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenderers(ModernMaterial.ModernMaterialBuilder builder) {
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

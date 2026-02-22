package gregtech.mixin.mixins.late.galaxyspace;


import galaxyspace.core.item.block.ItemBlockTerraformableMeta;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemBlockTerraformableMeta.class)
public abstract class MixinGalacticSpaceOreMetaDataFix extends ItemBlock {

    public MixinGalacticSpaceOreMetaDataFix(Block p_i45328_1_) {
        super(p_i45328_1_);
    }

    public int getMetadata(int p_77647_1_) {
        return p_77647_1_;
    }
}

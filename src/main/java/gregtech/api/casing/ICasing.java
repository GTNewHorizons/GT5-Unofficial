package gregtech.api.casing;

import static gregtech.api.enums.Textures.BlockIcons.getCasingTextureForId;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.util.data.ImmutableBlockMeta;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import gregtech.api.interfaces.ITexture;

public interface ICasing extends ImmutableBlockMeta {

    int getTextureId();

    default ItemStack toStack(int amount) {
        return new ItemStack(getBlock(), amount, getBlockMeta());
    }

    default String getLocalizedName() {
        return new ItemStack(getBlock(), 1, getBlockMeta()).getDisplayName();
    }

    /**
     * The context for converting an ICasing to an IStructureElement.
     * Currently, this just contains the group. This exists primarily to make refactoring easier if we ever need to
     * include another field here.
     */
    interface CasingElementContext {

        ICasingGroup getGroup();
    }

    default <T> IStructureElement<T> asElement(CasingElementContext context) {
        return StructureUtility.lazy(() -> StructureUtility.ofBlock(getBlock(), getBlockMeta()));
    }

    default ITexture getCasingTexture() {
        return getCasingTextureForId(getTextureId());
    }

    default boolean isTiered() {
        return false;
    }

    static ICasing ofBlock(Block block, int meta) {
        return new ICasing() {

            @Override
            public int getTextureId() {
                return -1;
            }

            @Override
            public @NotNull Block getBlock() {
                return block;
            }

            @Override
            public int getBlockMeta() {
                return meta;
            }
        };
    }
}

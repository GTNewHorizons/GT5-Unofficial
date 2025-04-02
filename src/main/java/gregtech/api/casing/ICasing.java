package gregtech.api.casing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.Textures.BlockIcons.getCasingTextureForId;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.gtnhlib.util.data.ImmutableBlockMeta;
import com.gtnewhorizon.structurelib.structure.IStructureElement;

import gregtech.api.interfaces.ITexture;

public interface ICasing extends ImmutableBlockMeta {

    int getTextureId();

    default ItemStack toStack(int amount) {
        return new ItemStack(getBlock(), amount, getBlockMeta());
    }

    default String getLocalizedName() {
        return new ItemStack(getBlock(), 1, getBlockMeta()).getDisplayName();
    }

    default <T> IStructureElement<T> asElement() {
        return lazy(() -> ofBlock(getBlock(), getBlockMeta()));
    }

    default ITexture getCasingTexture() {
        return getCasingTextureForId(getTextureId());
    }
}

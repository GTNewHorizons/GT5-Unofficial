package gregtech.api.casing;

import static gregtech.api.enums.Textures.BlockIcons.getCasingTextureForId;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.util.data.ImmutableBlockMeta;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.IStructureInstance;

public interface ICasing extends ImmutableBlockMeta {

    /**
     * Gets the casing texture id. Used to update the background of hatches. If this casing does not have a texture id,
     * this method must throw an {@link UnsupportedOperationException} or an equivalent exception.
     */
    int getTextureId();

    /**
     * Gets a tiered casing background texture, if possible. Defaults to the standard untiered background.
     */
    default <T> int getTextureId(T t, CasingElementContext<T> context) {
        return getTextureId();
    }

    /**
     * Gets an ITexture for this casing. May return a valid value when {@link #getTextureId()} does not since textures
     * do not have to be registered in the GT texture index.
     */
    default ITexture getCasingTexture() {
        return getCasingTextureForId(getTextureId());
    }

    /**
     * A combination of {@link #getCasingTexture()} and {@link #getTextureId(Object, CasingElementContext)}.
     */
    default <T> ITexture getCasingTexture(T t, CasingElementContext<T> context) {
        return getCasingTextureForId(getTextureId(t, context));
    }

    /**
     * Must return true when the casing can be represented by more than one block+meta. If this casing only represents a
     * single block+meta, this must return false.
     */
    boolean isTiered();

    default ItemStack toStack(int amount) {
        Item item = Item.getItemFromBlock(getBlock());

        if (item == null) {
            throw new NullPointerException(
                "Block " + getBlock() + " was not registered, causing it to not have an item (casing: " + this + ")");
        }

        return new ItemStack(getBlock(), 1, getBlockMeta());
    }

    default String getLocalizedName() {
        return toStack(1).getDisplayName();
    }

    /**
     * The context for converting an ICasing to an IStructureElement. This exists primarily to make refactoring easier
     * if we ever need to include another field here.
     */
    interface CasingElementContext<T> {

        ICasingGroup getGroup();

        /** Gets the structure instance from the generic context object (which is likely a multi). */
        IStructureInstance<T> getInstance(T t);
    }

    default <T> IStructureElement<T> asElement(CasingElementContext<T> context) {
        return StructureUtility.lazy(() -> StructureUtility.ofBlock(getBlock(), getBlockMeta()));
    }

    default <T> IStructureElement<T> asElement() {
        return StructureUtility.lazy(() -> StructureUtility.ofBlock(getBlock(), getBlockMeta()));
    }

    static ICasing ofBlock(ImmutableBlockMeta block) {
        return new ICasing() {

            @Override
            public int getTextureId() {
                return -1;
            }

            @Override
            public ITexture getCasingTexture() {
                return TextureFactory.of(getBlock(), getBlockMeta());
            }

            @Override
            public @NotNull Block getBlock() {
                return block.getBlock();
            }

            @Override
            public int getBlockMeta() {
                return block.getBlockMeta();
            }

            @Override
            public boolean isTiered() {
                return false;
            }
        };
    }
}

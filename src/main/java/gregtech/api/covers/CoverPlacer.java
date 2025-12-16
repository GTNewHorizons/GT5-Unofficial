package gregtech.api.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.common.covers.Cover;

public final class CoverPlacer {

    public static CoverPlacerBuilder builder() {
        return new CoverPlacerBuilder();
    }

    private final CoverPlacementPredicate coverPlacementPredicate;
    private final boolean allowOnPrimitiveBlock;
    private final boolean isGuiClickable;

    private CoverPlacer(CoverPlacementPredicate coverPlacementPredicate, boolean allowOnPrimitiveBlock,
        boolean isGuiClickable) {
        this.coverPlacementPredicate = coverPlacementPredicate;
        this.allowOnPrimitiveBlock = allowOnPrimitiveBlock;
        this.isGuiClickable = isGuiClickable;
    }

    public boolean isCoverPlaceable(ForgeDirection side, ItemStack coverItem, ICoverable tileEntity) {
        return coverPlacementPredicate.isCoverPlaceable(side, coverItem, tileEntity);
    }

    public boolean allowOnPrimitiveBlock() {
        return allowOnPrimitiveBlock;
    }

    /**
     * If it lets you rightclick the Machine normally
     */
    public boolean isGuiClickable() {
        return isGuiClickable;
    }

    /**
     * sets the Cover upon placement.
     */
    public void placeCover(EntityPlayer player, ItemStack coverItem, ICoverable tileEntity, ForgeDirection side) {
        Cover cover = CoverRegistry.buildCover(coverItem, side, tileEntity);
        tileEntity.attachCover(cover);
        cover.onPlayerAttach(player, coverItem);
    }

    public static final class CoverPlacerBuilder {

        private CoverPlacerBuilder() {}

        private CoverPlacementPredicate coverPlacementPredicate = (d, s, c) -> true;
        private boolean allowOnPrimitiveBlock = false;
        private boolean isGuiClickable = true;

        public CoverPlacerBuilder onlyPlaceIf(CoverPlacementPredicate coverPlacementPredicate) {
            this.coverPlacementPredicate = coverPlacementPredicate;
            return this;
        }

        public CoverPlacerBuilder allowOnPrimitiveBlock() {
            allowOnPrimitiveBlock = true;
            return this;
        }

        public CoverPlacerBuilder blocksCoverableGuiOpening() {
            isGuiClickable = false;
            return this;
        }

        public CoverPlacer build() {
            return new CoverPlacer(coverPlacementPredicate, allowOnPrimitiveBlock, isGuiClickable);
        }
    }
}

package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fluids.Fluid;

import gregtech.api.covers.CoverContext;
import gregtech.api.util.GTUtility;

public class CoverDecorative extends CoverLegacyData {

    /**
     * This is the Dummy, if there is a generic Cover without behavior
     */
    public CoverDecorative(CoverContext context) {
        super(context);
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        this.coverData = ((this.coverData + 1) & 15);
        IChatComponent message = new ChatComponentText("");
        if ((this.coverData & 1) != 0) {
            message.appendSibling(new ChatComponentTranslation("GT5U.chat.cover.decorative.redstone"))
                .appendText(" ");
        }
        if ((this.coverData & 2) != 0) {
            message.appendSibling(new ChatComponentTranslation("GT5U.chat.cover.decorative.energy"))
                .appendText(" ");
        }
        if ((this.coverData & 4) != 0) {
            message.appendSibling(new ChatComponentTranslation("GT5U.chat.cover.decorative.fluids"))
                .appendText(" ");
        }
        if ((this.coverData & 8) != 0) {
            message.appendSibling(new ChatComponentTranslation("GT5U.chat.cover.decorative.items"))
                .appendText(" ");
        }
        GTUtility.sendChatComp(aPlayer, message);
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return (coverData & 1) != 0;
    }

    @Override
    public boolean letsRedstoneGoOut() {
        return (coverData & 1) != 0;
    }

    @Override
    public boolean letsEnergyIn() {
        return (coverData & 2) != 0;
    }

    @Override
    public boolean letsEnergyOut() {
        return (coverData & 2) != 0;
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        return (coverData & 4) != 0;
    }

    @Override
    public boolean letsFluidOut(Fluid aFluid) {
        return (coverData & 4) != 0;
    }

    @Override
    public boolean letsItemsIn(int aSlot) {
        return (coverData & 8) != 0;
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return (coverData & 8) != 0;
    }
}

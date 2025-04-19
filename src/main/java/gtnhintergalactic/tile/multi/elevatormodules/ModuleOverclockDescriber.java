package gtnhintergalactic.tile.multi.elevatormodules;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.EnumChatFormatting;

import gregtech.api.enums.GTValues;
import gregtech.api.objects.overclockdescriber.EUNoOverclockDescriber;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gtnhintergalactic.recipe.IGRecipeMaps;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModuleOverclockDescriber extends EUNoOverclockDescriber {

    private final int moduleTier;

    public ModuleOverclockDescriber(byte tier, int moduleTier) {
        super(tier, 1);
        this.moduleTier = moduleTier;
    }

    @Nonnull
    @Override
    public String getTierString() {
        return GTValues.TIER_COLORS[tier] + "MK " + moduleTier + EnumChatFormatting.RESET;
    }

    @Override
    public boolean canHandle(GTRecipe recipe) {
        return super.canHandle(recipe) && this.moduleTier >= recipe.getMetadataOrDefault(IGRecipeMaps.MODULE_TIER, 1);
    }
}

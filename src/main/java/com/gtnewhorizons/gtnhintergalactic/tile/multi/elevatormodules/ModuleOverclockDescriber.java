package com.gtnewhorizons.gtnhintergalactic.tile.multi.elevatormodules;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.EnumChatFormatting;

import gregtech.api.enums.GT_Values;
import gregtech.api.objects.overclockdescriber.EUNoOverclockDescriber;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.MethodsReturnNonnullByDefault;

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
        return GT_Values.TIER_COLORS[tier] + "MK " + moduleTier + EnumChatFormatting.RESET;
    }

    @Override
    public boolean canHandle(GT_Recipe recipe) {
        return super.canHandle(recipe) && this.moduleTier >= recipe.mSpecialValue;
    }
}

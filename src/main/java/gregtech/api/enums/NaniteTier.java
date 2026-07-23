package gregtech.api.enums;

import java.text.MessageFormat;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.MU;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;

public enum NaniteTier {

    Carbon(1),
    Glowstone(1),
    Silver(2),
    Neutronium(2),
    Gold(3),
    // intentionally above gold because transmetal nanites are harder to make
    Transcendent(4),
    SixPhasedCopper(5),
    // eoh t1-t3
    WhiteDwarf(6),
    // eoh t4-t6
    BlackDwarf(7),
    // eoh t7-t9
    Universium(8),
    Eternity(9),
    MagMatter(10);

    public final int tier;

    private ItemStack stack = null;

    NaniteTier(int tier) {
        this.tier = tier;
    }

    public int getTier() {
        return tier;
    }

    public Material getMaterial() {
        return switch (this) {
            case Carbon -> Materials2Materials.Carbon;
            case Glowstone -> Materials2Materials.Glowstone;
            case Silver -> Materials2Materials.Silver;
            case Neutronium -> Materials2Materials.Neutronium;
            case Gold -> Materials2Materials.Gold;
            case Transcendent -> Materials2Materials.TranscendentMetal;
            case WhiteDwarf -> Materials2Materials.WhiteDwarfMatter;
            case BlackDwarf -> Materials2Materials.BlackDwarfMatter;
            case SixPhasedCopper -> Materials2Materials.SixPhasedCopper;
            case Universium -> Materials2Materials.Universium;
            case MagMatter -> Materials2Materials.Magmatter;
            case Eternity -> Materials2Materials.Eternity;
        };
    }

    public ItemStack getStack() {
        if (this.stack == null) {
            stack = GTOreDictUnificator.get(OrePrefixes.nanite, getMaterial(), 1);
        }

        return stack.copy();
    }

    public String describe() {
        String localizedName = StatCollector.translateToLocal(
            "Material." + MU.internalName(getMaterial())
                .toLowerCase());
        return MessageFormat.format("{0} (Tier {1})", localizedName, tier);
    }

    public static NaniteTier fromMaterial(Material mat) {
        if (mat == Materials2Materials.Carbon) return Carbon;
        if (mat == Materials2Materials.Glowstone) return Glowstone;
        if (mat == Materials2Materials.Silver) return Silver;
        if (mat == Materials2Materials.Neutronium) return Neutronium;
        if (mat == Materials2Materials.Gold) return Gold;
        if (mat == Materials2Materials.TranscendentMetal) return Transcendent;
        if (mat == Materials2Materials.WhiteDwarfMatter) return WhiteDwarf;
        if (mat == Materials2Materials.BlackDwarfMatter) return BlackDwarf;
        if (mat == Materials2Materials.SixPhasedCopper) return SixPhasedCopper;
        if (mat == Materials2Materials.Universium) return Universium;
        if (mat == Materials2Materials.Magmatter) return MagMatter;
        if (mat == Materials2Materials.Eternity) return Eternity;

        return null;
    }

    public static NaniteTier fromStack(ItemStack stack) {
        if (stack == null) return null;

        ItemData data = GTOreDictUnificator.getAssociation(stack);

        if (data == null) return null;
        if (data.mPrefix != OrePrefixes.nanite) return null;

        return fromMaterial(data.mMaterial.mMaterial);
    }
}

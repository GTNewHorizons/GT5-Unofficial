package gregtech.api.items;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.hazards.HazardProtection;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTUtility;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import ic2.core.IC2Potion;

/**
 * Used for {@link gregtech.api.enums.ItemList#RodLithium} and {@link gregtech.api.enums.ItemList#RodGlowstone}.
 *
 * @author glee8e
 */
public class ItemBreederCell extends GTGenericItem implements IReactorComponent {

    protected final int mHeatBonusStep;
    protected final int mHeatBonusMultiplier;
    protected ItemStack mProduct;
    protected boolean deflector = false;
    protected boolean hidden = false;
    protected boolean neiAdded = false;

    public ItemBreederCell(String aUnlocalized, String aEnglish, String aEnglishTooltip, int aHeatBonusStep,
        int aHeatBonusMultiplier, int aRequiredPulse, Supplier<ItemStack> aProduct) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        this.mHeatBonusStep = aHeatBonusStep;
        this.mHeatBonusMultiplier = aHeatBonusMultiplier;
        this.setMaxDamage(aRequiredPulse);
        setNoRepair();
        GregTechAPI.sAfterGTPostload.add(() -> {
            mProduct = aProduct.get();
            if (!hidden && !neiAdded) {
                GTValues.RA.stdBuilder()
                    .itemInputs(new ItemStack(this))
                    .itemOutputs(mProduct)
                    .setNEIDesc(
                        GTUtility.breakLines(
                            translateToLocal(
                                deflector ? "GT5U.nei.nuclear.breeder.neutron_reflecting"
                                    : "GT5U.nei.nuclear.breeder.heat_neutral"),
                            translateToLocalFormatted(
                                "GT5U.nei.nuclear.breeder.reactor_hull_heat",
                                mHeatBonusStep,
                                mHeatBonusMultiplier),
                            translateToLocalFormatted("GT5U.nei.nuclear.breeder.required_pulse", getMaxDamage())))
                    .duration(0)
                    .eut(0)
                    .addTo(RecipeMaps.ic2NuclearFakeRecipes);

                neiAdded = true;
            }
        });
    }

    public ItemBreederCell setDeflector() {
        deflector = true;
        return this;
    }

    public ItemBreederCell setHidden() {
        hidden = true;
        return this;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slotIndex, boolean isCurrentItem) {
        if ((entity instanceof EntityLivingBase entityLiving)) {
            if (!HazardProtection.isWearingFullRadioHazmat(entityLiving)) {
                IC2Potion.radiation.applyTo(entityLiving, 20, 1);
            }
        }
    }

    @Override
    public void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
        aList.add(translateToLocal("gt.item.desc.breeder_tip"));
        int rDmg = aStack.getItemDamage() * 4 / getMaxDamage();
        EnumChatFormatting color2 = switch (rDmg) {
            case 0 -> EnumChatFormatting.DARK_GRAY;
            case 1, 2 -> EnumChatFormatting.GRAY;
            default -> EnumChatFormatting.WHITE;
        };
        aList.add(
            translateToLocalFormatted(
                "gt.item.desc.progress",
                color2 + formatNumber(aStack.getItemDamage()) + EnumChatFormatting.RESET,
                formatNumber(getMaxDamage())));
        if (aStack.getItemDamage() > 0) aList.add(EnumChatFormatting.RED + translateToLocal("gt.item.desc.haz_rad"));
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return stack.getItemDamage() == 0 ? maxStackSize : 1;
    }

    @Override
    public void processChamber(IReactor reactor, ItemStack yourStack, int x, int y, boolean heatrun) {}

    @Override
    public boolean acceptUraniumPulse(IReactor reactor, ItemStack yourStack, ItemStack pulsingStack, int youX, int youY,
        int pulseX, int pulseY, boolean heatrun) {
        if (heatrun) {
            int myLevel = getNewDamage(reactor, yourStack);
            if (myLevel >= getMaxDamage()) reactor.setItemAt(youX, youY, mProduct.copy());
            else yourStack.setItemDamage(myLevel);
        }

        return deflector;
    }

    protected int getNewDamage(IReactor reactor, ItemStack stack) {
        return stack.getItemDamage() + 1 + reactor.getHeat() / mHeatBonusStep * mHeatBonusMultiplier;
    }

    @Override
    public boolean canStoreHeat(IReactor reactor, ItemStack yourStack, int x, int y) {
        return false;
    }

    @Override
    public int getMaxHeat(IReactor reactor, ItemStack yourStack, int x, int y) {
        return 0;
    }

    @Override
    public int getCurrentHeat(IReactor reactor, ItemStack yourStack, int x, int y) {
        return 0;
    }

    @Override
    public int alterHeat(IReactor reactor, ItemStack yourStack, int x, int y, int heat) {
        return heat;
    }

    @Override
    public float influenceExplosion(IReactor reactor, ItemStack yourStack) {
        return 0.0F;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1.0D - (double) stack.getItemDamageForDisplay() / (double) stack.getMaxDamage();
    }
}

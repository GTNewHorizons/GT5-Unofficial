package gregtech.api.items;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import gregtech.api.enums.GTValues;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTUtility;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import ic2.core.IC2Potion;
import ic2.core.init.MainConfig;
import ic2.core.util.ConfigUtil;

public class ItemRadioactiveCellIC extends ItemRadioactiveCell implements IReactorComponent {

    private static final int MYSTERIOUS_MULTIPLIER_HEAT = 4;
    public final int numberOfCells;
    public final float sEnergy;
    public final int sRadiation;
    public final float sHeat;
    public final ItemStack sDepleted;
    public final boolean sMox;

    public ItemRadioactiveCellIC(String aUnlocalized, String aEnglish, int aCellcount, int maxDamage, float aEnergy,
        int aRadiation, float aHeat, ItemStack aDepleted, boolean aMox) {
        super(aUnlocalized, aEnglish, aCellcount);
        setMaxStackSize(64);
        this.maxDmg = maxDamage;
        this.numberOfCells = aCellcount;
        this.sEnergy = aEnergy;
        this.sRadiation = aRadiation;
        this.sHeat = aHeat;
        this.sDepleted = aDepleted;
        this.sMox = aMox;
        if (aDepleted != null && aEnergy > 0 && aHeat > 0) {
            // avoid adding depleted cells to recipe map

            int pulses = aCellcount / 2 + 1;

            // for the mysterious constant 5.0f,
            // see ic2.core.block.reactor.tileentity.TileEntityNuclearReactorElectric.getOfferedEnergy
            // don't ask, just accept
            float nukePowerMult = 5.0f * ConfigUtil.getFloat(MainConfig.get(), "balance/energy/generator/nuclear");
            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(this))
                .itemOutputs(aDepleted)
                .setNEIDesc(
                    aMox ? "MOX Model" : "Uranium Model",
                    "Neutron Pulse: " + aCellcount,
                    aCellcount == 1
                        ? String.format("Heat: %.1f * n1 * (n1 + 1)", (aHeat * MYSTERIOUS_MULTIPLIER_HEAT) / 2f)
                        : String.format(
                            "Heat: %.1f * (%d + n1) * (%d + n1)",
                            (aHeat * MYSTERIOUS_MULTIPLIER_HEAT) * aCellcount / 2f,
                            aCellcount,
                            aCellcount + 1),
                    String.format(
                        "Energy: %.1f + n2 * %.1f EU/t",
                        aEnergy * aCellcount * pulses * nukePowerMult,
                        aEnergy * nukePowerMult))
                .duration(0)
                .eut(0)
                .addTo(RecipeMaps.ic2NuclearFakeRecipes);
        }
    }

    private static int checkPulseable(IReactor reactor, int x, int y, ItemStack me, int mex, int mey, boolean heatrun) {
        ItemStack other = reactor.getItemAt(x, y);
        if ((other != null) && ((other.getItem() instanceof IReactorComponent))
            && (((IReactorComponent) other.getItem())
                .acceptUraniumPulse(reactor, other, me, x, y, mex, mey, heatrun))) {
            return 1;
        }
        return 0;
    }

    @Override
    public void processChamber(IReactor reactor, ItemStack yourStack, int x, int y, boolean heatrun) {
        if (!reactor.produceEnergy()) {
            return;
        }
        for (int iteration = 0; iteration < this.numberOfCells; iteration++) {
            int pulses = 1 + this.numberOfCells / 2;
            if (!heatrun) {
                for (int i = 0; i < pulses; i++) {
                    acceptUraniumPulse(reactor, yourStack, yourStack, x, y, x, y, heatrun);
                }
                checkPulseable(reactor, x - 1, y, yourStack, x, y, heatrun);
                checkPulseable(reactor, x + 1, y, yourStack, x, y, heatrun);
                checkPulseable(reactor, x, y - 1, yourStack, x, y, heatrun);
                checkPulseable(reactor, x, y + 1, yourStack, x, y, heatrun);
            } else {
                pulses += checkPulseable(reactor, x - 1, y, yourStack, x, y, heatrun)
                    + checkPulseable(reactor, x + 1, y, yourStack, x, y, heatrun)
                    + checkPulseable(reactor, x, y - 1, yourStack, x, y, heatrun)
                    + checkPulseable(reactor, x, y + 1, yourStack, x, y, heatrun);

                // int heat = sumUp(pulses) * 4;

                int heat = triangularNumber(pulses) * MYSTERIOUS_MULTIPLIER_HEAT;

                heat = getFinalHeat(reactor, yourStack, x, y, heat);

                ArrayList<ItemStackCoord> heatAcceptors = new ArrayList<>();
                checkHeatAcceptor(reactor, x - 1, y, heatAcceptors);
                checkHeatAcceptor(reactor, x + 1, y, heatAcceptors);
                checkHeatAcceptor(reactor, x, y - 1, heatAcceptors);
                checkHeatAcceptor(reactor, x, y + 1, heatAcceptors);
                heat = Math.round(heat * sHeat);
                while ((heatAcceptors.size() > 0) && (heat > 0)) {

                    int dheat = heat / heatAcceptors.size();
                    heat -= dheat;
                    dheat = ((IReactorComponent) heatAcceptors.get(0).stack.getItem()).alterHeat(
                        reactor,
                        heatAcceptors.get(0).stack,
                        heatAcceptors.get(0).x,
                        heatAcceptors.get(0).y,
                        dheat);
                    heat += dheat;
                    heatAcceptors.remove(0);
                }
                if (heat > 0) {
                    reactor.addHeat(heat);
                }
            }
        }
        if (getDamageOfStack(yourStack) >= getMaxDamageEx() - 1) {
            reactor.setItemAt(x, y, sDepleted.copy());
        } else if (heatrun) {
            damageItemStack(yourStack, 1);
        }
    }

    protected int getFinalHeat(IReactor reactor, ItemStack stack, int x, int y, int heat) {
        if (sMox && reactor.isFluidCooled()) {
            float breedereffectiveness = (float) reactor.getHeat() / (float) reactor.getMaxHeat();
            if (breedereffectiveness > 0.5D) {
                heat *= 2;
            }
        }
        return heat;
    }

    private void checkHeatAcceptor(IReactor reactor, int x, int y, ArrayList<ItemStackCoord> heatAcceptors) {
        ItemStack thing = reactor.getItemAt(x, y);
        if ((thing != null) && ((thing.getItem() instanceof IReactorComponent))
            && (((IReactorComponent) thing.getItem()).canStoreHeat(reactor, thing, x, y))) {
            heatAcceptors.add(new ItemStackCoord(thing, x, y));
        }
    }

    @Override
    public boolean acceptUraniumPulse(IReactor reactor, ItemStack yourStack, ItemStack pulsingStack, int youX, int youY,
        int pulseX, int pulseY, boolean heatrun) {
        if (!heatrun) {
            if (sMox) {
                float breedereffectiveness = (float) reactor.getHeat() / (float) reactor.getMaxHeat();
                float ReaktorOutput = 1.5F * breedereffectiveness + 1.0F;
                reactor.addOutput(ReaktorOutput * this.sEnergy);
            } else {
                reactor.addOutput(1.0F * this.sEnergy);
            }
        }
        return true;
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
        return 2 * this.numberOfCells;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slotIndex, boolean isCurrentItem) {
        if (this.sRadiation > 0 && (entity instanceof EntityLivingBase entityLiving)) {
            if (!GTUtility.isWearingFullRadioHazmat(entityLiving)) {
                IC2Potion.radiation.applyTo(entityLiving, sRadiation * 20, sRadiation * 10);
            }
        }
    }

    private static class ItemStackCoord {

        public ItemStack stack;
        public int x;
        public int y;

        public ItemStackCoord(ItemStack stack1, int x1, int y1) {
            this.stack = stack1;
            this.x = x1;
            this.y = y1;
        }
    }
}

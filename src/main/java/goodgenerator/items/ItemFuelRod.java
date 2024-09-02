package goodgenerator.items;

import static goodgenerator.util.DescTextLocalization.addText;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GTUtility;
import ic2.api.item.IBoxable;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import ic2.core.util.StackUtil;
import ic2.core.util.Util;

public class ItemFuelRod extends RadioactiveItem implements IReactorComponent, IBoxable {

    private final int numberOfCells;
    private final int maxDmg;
    private final float Power;
    private final int Heat;
    private float HeatBonus = 0;
    private final ItemStack result;

    public ItemFuelRod(String aName, int aCells, int aEUt, int aHeat, int aRads, int aDuration, ItemStack aResult,
        CreativeTabs Tab) {
        super(aName, Tab, aRads);
        this.setMaxStackSize(64);
        this.numberOfCells = aCells;
        this.maxDmg = aDuration;
        this.Power = (float) aEUt / 25.0F;
        this.result = aResult;
        this.Heat = aHeat;
        setMaxDamage(100);
    }

    public ItemFuelRod(String aName, int aCells, int aEUt, int aHeat, int aRads, int aDuration, float aHeatBonus,
        ItemStack aResult, CreativeTabs Tab) {
        super(aName, Tab, aRads);
        this.setMaxStackSize(64);
        this.numberOfCells = aCells;
        this.maxDmg = aDuration;
        this.Power = (float) aEUt / 25.0F;
        this.result = aResult;
        this.Heat = aHeat;
        this.HeatBonus = aHeatBonus;
        setMaxDamage(100);
    }

    public void processChamber(IReactor reactor, ItemStack stack, int x, int y, boolean heatRun) {
        if (reactor.produceEnergy()) {
            for (int iteration = 0; iteration < this.numberOfCells; ++iteration) {
                int pulses = 1 + this.numberOfCells / 2;
                int heat;
                if (!heatRun) {
                    for (heat = 0; heat < pulses; ++heat) {
                        this.acceptUraniumPulse(reactor, stack, stack, x, y, x, y, heatRun);
                    }
                    checkPulseable(reactor, x - 1, y, stack, x, y, heatRun);
                    checkPulseable(reactor, x + 1, y, stack, x, y, heatRun);
                    checkPulseable(reactor, x, y - 1, stack, x, y, heatRun);
                    checkPulseable(reactor, x, y + 1, stack, x, y, heatRun);
                } else {
                    pulses += checkPulseable(reactor, x - 1, y, stack, x, y, heatRun)
                        + checkPulseable(reactor, x + 1, y, stack, x, y, heatRun)
                        + checkPulseable(reactor, x, y - 1, stack, x, y, heatRun)
                        + checkPulseable(reactor, x, y + 1, stack, x, y, heatRun);
                    heat = sumUp(pulses) * this.Heat;
                    ArrayList<ItemFuelRod.ItemStackCoord> heatAcceptors = new ArrayList<>();
                    this.checkHeatAcceptor(reactor, x - 1, y, heatAcceptors);
                    this.checkHeatAcceptor(reactor, x + 1, y, heatAcceptors);
                    this.checkHeatAcceptor(reactor, x, y - 1, heatAcceptors);
                    this.checkHeatAcceptor(reactor, x, y + 1, heatAcceptors);

                    while (heatAcceptors.size() > 0 && heat > 0) {
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
            if (this.getCustomDamage(stack) >= this.getMaxCustomDamage(stack) - 1) {
                reactor.setItemAt(x, y, GTUtility.copyAmount(1, result));
            } else if (heatRun) {
                this.applyCustomDamage(stack, 1, null);
            }
        }
    }

    private static int checkPulseable(IReactor reactor, int x, int y, ItemStack me, int mex, int mey, boolean heatrun) {
        ItemStack other = reactor.getItemAt(x, y);
        return other != null && other.getItem() instanceof IReactorComponent
            && ((IReactorComponent) other.getItem()).acceptUraniumPulse(reactor, other, me, x, y, mex, mey, heatrun) ? 1
                : 0;
    }

    private static int sumUp(int x) {
        return (x * x + x) / 2;
    }

    private void checkHeatAcceptor(IReactor reactor, int x, int y,
        ArrayList<ItemFuelRod.ItemStackCoord> heatAcceptors) {
        ItemStack thing = reactor.getItemAt(x, y);
        if (thing != null && thing.getItem() instanceof IReactorComponent
            && ((IReactorComponent) thing.getItem()).canStoreHeat(reactor, thing, x, y)) {
            heatAcceptors.add(new ItemStackCoord(thing, x, y));
        }
    }

    public boolean acceptUraniumPulse(IReactor reactor, ItemStack yourStack, ItemStack pulsingStack, int youX, int youY,
        int pulseX, int pulseY, boolean heatrun) {
        if (!heatrun) {
            reactor.addOutput(Power * (1 + HeatBonus * ((float) reactor.getHeat() / (float) reactor.getMaxHeat())));
        }
        return true;
    }

    public boolean canStoreHeat(IReactor reactor, ItemStack yourStack, int x, int y) {
        return false;
    }

    public int getMaxHeat(IReactor reactor, ItemStack yourStack, int x, int y) {
        return 0;
    }

    public int getCurrentHeat(IReactor reactor, ItemStack yourStack, int x, int y) {
        return 0;
    }

    public int alterHeat(IReactor reactor, ItemStack yourStack, int x, int y, int heat) {
        return heat;
    }

    public float influenceExplosion(IReactor reactor, ItemStack yourStack) {
        return (float) (2 * this.numberOfCells);
    }

    @Override
    public boolean canBeStoredInToolbox(ItemStack itemStack) {
        return true;
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

    public int getCustomDamage(ItemStack stack) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        return nbt.getInteger("advDmg");
    }

    public int getMaxCustomDamage(ItemStack stack) {
        return this.maxDmg;
    }

    public void setCustomDamage(ItemStack stack, int damage) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        nbt.setInteger("advDmg", damage);
        int maxStackDamage = stack.getMaxDamage();
        if (maxStackDamage > 2) {
            stack.setItemDamage(1 + (int) Util.map(damage, this.maxDmg, maxStackDamage - 2));
        }
    }

    public boolean applyCustomDamage(ItemStack stack, int damage, EntityLivingBase src) {
        this.setCustomDamage(stack, this.getCustomDamage(stack) + damage);
        return true;
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List tooltip, boolean p_77624_4_) {
        super.addInformation(item, player, tooltip, p_77624_4_);
        tooltip.add(
            String.format(
                addText("fuelrod.tooltip", 1)[0],
                getMaxCustomDamage(item) - getCustomDamage(item),
                getMaxCustomDamage(item)));
        double tMut = this.Heat / 4.0;
        if (this.Heat == 4) {
            tooltip.add(StatCollector.translateToLocal("fuelrodheat.tooltip.0"));
        } else {
            tooltip.add(String.format(StatCollector.translateToLocal("fuelrodheat.tooltip.1"), tMut));
        }
        if (this.HeatBonus != 0) tooltip.add(StatCollector.translateToLocal("fuelrodheat.tooltip.2"));
    }
}

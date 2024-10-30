package gregtech.common.items.matterManipulator;

import org.jetbrains.annotations.Nullable;

import appeng.api.AEApi;
import appeng.api.definitions.IItemDefinition;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PatternItemProvider implements IItemProvider {

    private Integer amount;
    private NBTTagCompound pattern;

    private static final IItemDefinition BLANK_PATTERN = AEApi.instance().definitions().materials().blankPattern();
    private static final IItemDefinition PATTERN = AEApi.instance().definitions().items().encodedPattern();

    public PatternItemProvider() { }

    public static PatternItemProvider fromPattern(ItemStack stack) {
        if (!PATTERN.isSameAs(stack)) {
            return null;
        }

        PatternItemProvider pattern = new PatternItemProvider();

        pattern.amount = stack.stackSize != 1 ? stack.stackSize : null;
        pattern.pattern = stack.getTagCompound();

        return pattern;
    }

    @Override
    public @Nullable ItemStack getStack(IPseudoInventory inv, boolean consume) {
        ItemStack stack = PATTERN.maybeStack(1).get();

        stack.stackSize = amount == null ? 1 : amount;
        stack.setTagCompound(pattern != null ? (NBTTagCompound) pattern.copy() : null);
        
        if (consume) {
            if (!inv.tryConsumeItems(stack)) {
                ItemStack toConsume = BLANK_PATTERN.maybeStack(amount == null ? 1 : amount).get();
                if (!inv.tryConsumeItems(toConsume)) {
                    return null;
                }
            }
        }

        return stack;
    }
}

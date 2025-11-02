package gregtech.api.metatileentity.implementations.gui;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

import com.github.bsideup.jabel.Desugar;

// Needed for easier merging of same item stacks into one stacksize. Minecraft's Item does not retain its damage value
// So you end up with, 3 ignis powder instead of 1 aqua, 1 periditio, 1 ordo
@Desugar
public record ItemDisplayKey(Item item, int damage, NBTTagCompound nbt) {}

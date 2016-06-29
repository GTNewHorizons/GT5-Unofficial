package miscutil.core.intermod.forestry.apiculture.multiblock.inventory;

import miscutil.core.intermod.forestry.apiculture.multiblock.TileAlvearyMutatron;
import net.minecraft.item.ItemStack;
import forestry.api.apiculture.BeeManager;
import forestry.core.inventory.InventoryAdapterTile;
import forestry.core.utils.ItemStackUtil;

public class FR_InventoryMutatron extends InventoryAdapterTile<TileAlvearyMutatron> {
	public FR_InventoryMutatron(TileAlvearyMutatron alvearyMutatron) {
		super(alvearyMutatron, 4, "mutatronInv");
	}
	@Override
	public boolean canSlotAccept(int slotIndex, ItemStack itemStack) {
		return ItemStackUtil.containsItemStack(BeeManager.inducers.keySet(), itemStack);
	}
}


/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package miscutil.core.intermod.forestry.apiculture.multiblock.inventory;

import miscutil.core.intermod.forestry.apiculture.multiblock.TileAlvearyMutatron;
import net.minecraft.entity.player.InventoryPlayer;
import forestry.core.gui.ContainerTile;
import forestry.core.gui.slots.SlotFiltered;

public class FR_ContainerAlvearyMutatron extends ContainerTile<TileAlvearyMutatron> {
	public FR_ContainerAlvearyMutatron(InventoryPlayer player, TileAlvearyMutatron tile) {
		super(tile, player, 8, 87);
		this.addSlotToContainer(new SlotFiltered(tile, 0, 79, 52));
		this.addSlotToContainer(new SlotFiltered(tile, 1, 100, 39));
		this.addSlotToContainer(new SlotFiltered(tile, 2, 58, 39));
		this.addSlotToContainer(new SlotFiltered(tile, 3, 79, 26));
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

package miscutil.core.xmod.forestry.apiculture.multiblock.inventory;

import miscutil.core.lib.CORE;
import miscutil.core.xmod.forestry.apiculture.multiblock.TileAlvearyMutatron;
import miscutil.core.xmod.forestry.core.gui.FR_GuiForestryTitled;
import net.minecraft.entity.player.InventoryPlayer;

public class FR_GuiAlvearyMutatron extends FR_GuiForestryTitled<FR_ContainerAlvearyMutatron, TileAlvearyMutatron> {
	
		public FR_GuiAlvearyMutatron(InventoryPlayer inventory, TileAlvearyMutatron tile) {
			super(CORE.MODID+"/swarmer.png", new FR_ContainerAlvearyMutatron(inventory, tile), tile);
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
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
package gtPlusPlus.plugin.villagers.trade;

import java.util.Random;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.village.MerchantRecipeList;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;

public abstract class TradeHandlerBase implements IVillageTradeHandler {

	@SuppressWarnings("unchecked")
	@Override
	public abstract void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random);

}

package wanion.lib.common;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.TIntLongMap;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collection;

@SuppressWarnings("unused")
public final class MetaItem
{
	private static final FMLControlledNamespacedRegistry<Item> itemRegistry = GameData.getItemRegistry();

	private MetaItem() {}

	public static int get(final ItemStack itemStack)
	{
		Item item;
		if (itemStack == null || (item = itemStack.getItem()) == null)
			return 0;
		final int id = itemRegistry.getId(item);
		return id > 0 ? item.getDamage(itemStack) == OreDictionary.WILDCARD_VALUE ? id : id | item.getDamage(itemStack) + 1 << 16 : 0;
	}

	public static int get(final Item item)
	{
		if (item == null)
			return 0;
		final int id = itemRegistry.getIDForObject(item);
		return id > 0 ? id | 65536 : 0;
	}

	public static ItemStack toItemStack(final int metaItemKey)
	{
		return metaItemKey > 0 ? new ItemStack(itemRegistry.getRaw(metaItemKey ^ (metaItemKey & 65536)), 0, metaItemKey >> 16) : null;
	}

	public static int[] getArray(final Collection<ItemStack> itemStackCollection)
	{
		return getList(itemStackCollection).toArray();
	}

	public static TIntList getList(final Collection<ItemStack> itemStackCollection)
	{
		final TIntList keys = new TIntArrayList();
		int hash;
		for (final ItemStack itemStack : itemStackCollection)
			if ((hash = get(itemStack)) != 0)
				keys.add(hash);
		return keys;
	}

	public static TIntSet getSet(final Collection<ItemStack> itemStackCollection)
	{
		return new TIntHashSet(getList(itemStackCollection));
	}

	public static <E> void populateMap(final Collection<ItemStack> itemStackCollection, final TIntObjectMap<E> map, final E defaultValue)
	{
		for (final int id : getArray(itemStackCollection))
			map.put(id, defaultValue);
	}

	public static void populateMap(final Collection<ItemStack> itemStackCollection, final TIntLongMap map, long defaultValue)
	{
		for (final int id : getArray(itemStackCollection))
			map.put(id, defaultValue);
	}
}
package io.github.cottonmc.edibles;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.FoodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.crafting.SpecialCraftingRecipe;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class AddJellyRecipe extends SpecialCraftingRecipe {

	private int foodSlot = 0;
	ItemStack jelly = ItemStack.EMPTY;

	public AddJellyRecipe(Identifier id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingInventory inv, World world) {
		ItemStack targetFood = ItemStack.EMPTY;
		ItemStack targetJelly = ItemStack.EMPTY;
		if (inv == null) return false;
		else {
			for (int i = 0; i < inv.getInvSize(); i++) {
				ItemStack stack = inv.getInvStack(i);
				if (!stack.isEmpty()) {
					if (stack.getItem() instanceof FoodItem && !(stack.getItem() instanceof JellyItem)) {
						if (!targetFood.isEmpty()) return false;
						targetFood = stack;
						foodSlot = i;
					} else if (stack.getItem() instanceof JellyItem) {
						if (!targetJelly.isEmpty()) return false;
						targetJelly = stack;
					} else {
						return false;
					}
				}
			}
		}
		if (!targetJelly.isEmpty()) jelly = targetJelly;
		return (!targetFood.hasTag() || (!targetFood.getTag().containsKey("jellied") && !targetFood.getTag().containsKey("super_jellied"))) && !targetJelly.isEmpty();
	}

	@Override
	public ItemStack craft(CraftingInventory inv) {
		ItemStack food = inv.getInvStack(foodSlot).copy();
		if (!food.hasTag()) food.setTag(new CompoundTag());
		CompoundTag tag = food.getTag().copy();
		if (jelly.getItem() == Edibles.SUPER_JELLY) tag.putByte("super_jellied", (byte)0);
		else tag.putByte("jellied", (byte)0);
		food.setTag(tag);
		return food;
	}

	@Override
	public boolean fits(int x, int y) {
		return x >= 2 && y >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Edibles.ADD_JELLY_SERIALIZER;
	}
}

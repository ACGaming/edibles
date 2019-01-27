package io.github.cottonmc.edibles;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class SpecialFoodItem extends FoodItem {

	public SpecialFoodItem(int hunger, float saturation, boolean wolfFood, Settings settings) {
		super(hunger, saturation, wolfFood, settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (Edibles.config.edibleNuggets) return super.use(world, player, hand);
		else return new TypedActionResult<>(ActionResult.PASS, stack);
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return Edibles.config.edibleNuggets? UseAction.EAT : UseAction.NONE;
	}
}

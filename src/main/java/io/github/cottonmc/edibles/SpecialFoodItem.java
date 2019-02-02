package io.github.cottonmc.edibles;

import net.fabricmc.fabric.events.PlayerInteractionEvent;
import net.minecraft.advancement.criterion.Criterions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
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
		if (Edibles.config.edibleNuggets || Edibles.config.omnivoreEnabled) return super.use(world, player, hand);
		else return new TypedActionResult<>(ActionResult.PASS, stack);
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return (Edibles.config.edibleNuggets || Edibles.config.omnivoreEnabled)? UseAction.EAT : UseAction.NONE;
	}

	@Override
	public ItemStack onItemFinishedUsing(ItemStack stack, World world, LivingEntity entity) {
		if (Edibles.config.edibleNuggets) return super.onItemFinishedUsing(stack, world, entity);
		else if (Edibles.config.omnivoreEnabled) {
			if (entity instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) entity;
				player.getHungerManager().add(Edibles.config.omnivoreFoodRestore, Edibles.config.omnivoreSaturationRestore);
				world.playSound(null, player.x, player.y, player.z, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYER, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
				player.incrementStat(Stats.USED.getOrCreateStat(this));
				if (player instanceof ServerPlayerEntity) {
					Criterions.CONSUME_ITEM.handle((ServerPlayerEntity) player, stack);
				}
			}
			stack.subtractAmount(1);
		}
		return stack;
	}
}

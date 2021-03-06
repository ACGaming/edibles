package io.github.cottonmc.edibles.mixins;

import io.github.cottonmc.edibles.Edibles;
import io.github.cottonmc.edibles.SpecialFoodItem;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Items.class)
public class MixinEdibleItems {

	@Shadow
	@Mutable
	@Final
	public static Item IRON_NUGGET = Edibles.mixRegister("iron_nugget", new SpecialFoodItem(2, 0.1F, false, (new Item.Settings()).itemGroup(ItemGroup.MATERIALS)));

	@Shadow
	@Mutable
	@Final
	public static Item GOLD_NUGGET = Edibles.mixRegister("gold_nugget", new SpecialFoodItem(4, 0.3F, false, (new Item.Settings()).itemGroup(ItemGroup.MATERIALS)));

	@Shadow
	@Mutable
	@Final
	public static Item GLISTERING_MELON_SLICE = Edibles.mixRegister("glistering_melon_slice", new FoodItem(4, 0.5F, false, (new Item.Settings()).itemGroup(ItemGroup.BREWING)).setStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 90, 0), 1.0F));
}

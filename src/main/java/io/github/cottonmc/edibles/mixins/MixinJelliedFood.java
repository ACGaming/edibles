package io.github.cottonmc.edibles.mixins;

import net.minecraft.client.item.TooltipOptions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(FoodItem.class)
public class MixinJelliedFood extends Item {
	public MixinJelliedFood(Settings settings) {
		super(settings);
	}

	@Inject(method = "onItemFinishedUsing",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getHungerManager()Lnet/minecraft/entity/player/HungerManager;"),
//			locals = LocalCapture.CAPTURE_FAILEXCEPTION,
			remap = false)
	public void eatJelliedFood(ItemStack itemStack_1, World world_1, LivingEntity livingEntity_1, CallbackInfoReturnable<ItemStack> cir) {
		if (itemStack_1.hasTag()) {
			if (itemStack_1.getTag().containsKey("jellied")) {
				PlayerEntity player = (PlayerEntity)livingEntity_1;
				player.getHungerManager().add(2, 0.5f);
			}
		}
	}

	@Override
	public void buildTooltip(ItemStack stack, World world, List<TextComponent> tooltips, TooltipOptions tooltipOptions) {
		if (stack.hasTag())  {
			if (stack.getTag().containsKey("jellied")) {
				tooltips.add(new TranslatableTextComponent("tooltip.edibles.jellied").applyFormat(TextFormat.GRAY));
			}
		}
		super.buildTooltip(stack, world, tooltips, tooltipOptions);
	}

}

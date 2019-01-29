package io.github.cottonmc.edibles.mixins;

import io.github.cottonmc.edibles.Edibles;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.sortme.Hopper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.loot.context.LootContext;
import net.minecraft.world.loot.context.Parameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(HopperBlockEntity.class)
public abstract class MixinHopperHarvesting {

	@Inject(method = "extract(Lnet/minecraft/sortme/Hopper;)Z", at = @At("HEAD"), cancellable = true)
	private static void hopperHarvest(Hopper hopper, CallbackInfoReturnable cir) {
		if (Edibles.config.hopperHarvest) {
			BlockPos pos = new BlockPos(hopper.getHopperX(), hopper.getHopperY(), hopper.getHopperZ());
			BlockState state = hopper.getWorld().getBlockState(pos.offset(Direction.UP, 2));
			if (state.getBlock() instanceof CropBlock) {
				CropBlock crop = (CropBlock) state.getBlock();
				if (crop.getCropAgeMaximum() == state.get(crop.getAgeProperty())) {
					Inventory inv = HopperBlockEntity.getInventoryAt(hopper.getWorld(), pos);
					int madeTransfers = 0;
					List<ItemStack> results = state.getDroppedStacks(new LootContext.Builder(hopper.getWorld().getServer().getWorld(hopper.getWorld().dimension.getType()))
							.put(Parameters.POSITION, pos).put(Parameters.TOOL, ItemStack.EMPTY));
					for (int i = 0; i < results.size(); i++) {
						boolean inserted = false;
						for (int j = 0; j < inv.getInvSize(); j++) {
							if (!inserted) {
								ItemStack stack = results.get(i);
								ItemStack existing = inv.getInvStack(j);
								if (!existing.isEmpty()) {
									if (existing.getItem() != stack.getItem()) continue;
									else stack.addAmount(existing.getAmount());
								}

								inv.setInvStack(j, stack);
								madeTransfers++;
								inserted = true;
							}
						}
					}
					if (madeTransfers < results.size()) {
						for (int i = madeTransfers; i < results.size(); i++) {
							hopper.getWorld().spawnEntity(new ItemEntity(hopper.getWorld(), pos.getX(), pos.getY() + 2, pos.getZ(), results.get(i)));
						}
					}
					hopper.getWorld().setBlockState(pos.offset(Direction.UP, 2), state.with(crop.getAgeProperty(), 0));
					cir.setReturnValue(true);
				}
			}
		}
	}
}

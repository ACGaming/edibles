package io.github.cottonmc.edibles.mixins;

import io.github.cottonmc.edibles.Edibles;
import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.CocoaBlock;
import net.minecraft.block.CropBlock;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sortme.Hopper;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.loot.context.LootContext;
import net.minecraft.world.loot.context.Parameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(HopperBlockEntity.class)
public class MixinHopperHarvesting {

	@Inject(method = "extract(Lnet/minecraft/sortme/Hopper;)Z", at = @At("HEAD"), cancellable = true)
	private static void hopperHarvest(Hopper hopper, CallbackInfoReturnable cir) {
		if (Edibles.config.hopperHarvest) {
			World world = hopper.getWorld();
			BlockPos pos = new BlockPos(hopper.getHopperX(), hopper.getHopperY(), hopper.getHopperZ());
			BlockState state = world.getBlockState(pos.offset(Direction.UP, 2));
			if (state.getBlock() instanceof CropBlock) {
				if (harvestCrop(world, pos, state)) cir.setReturnValue(true);
			} else if (state.getBlock() instanceof SweetBerryBushBlock) {
				if (harvestBerries(world, pos, state)) cir.setReturnValue(true);
			} else if (state.getBlock() instanceof AttachedStemBlock) {
				if (harvestGourd(world, pos)) cir.setReturnValue(true);
			} else if (world.getBlockState(pos.offset(Direction.UP)).getBlock().matches(BlockTags.JUNGLE_LOGS)) {
				if (harvestCocoa(world, pos)) cir.setReturnValue(true);
			}
		}
	}

	private static boolean harvestCrop(World world, BlockPos pos, BlockState state) {
		CropBlock crop = (CropBlock) state.getBlock();
		if (crop.getCropAgeMaximum() == state.get(crop.getAgeProperty())) {
			Inventory inv = HopperBlockEntity.getInventoryAt(world, pos);
			List<ItemStack> results = state.getDroppedStacks(getLootContext(world, pos));
			List<ItemStack> remaining = attemptCollect(inv, results);
			if (remaining.equals(results)) {
				if (remaining.size() > 0) {
					spawnResults(world, pos.offset(Direction.UP, 2), remaining);
				}
				world.setBlockState(pos.offset(Direction.UP, 2), state.with(crop.getAgeProperty(), 0));
				return true;
			}
		}
		return false;
	}

	private static boolean harvestBerries(World world, BlockPos pos, BlockState state) {
		Inventory inv = HopperBlockEntity.getInventoryAt(world, pos);
		int age = state.get(SweetBerryBushBlock.AGE);
		if (age == 3) {
			int berriesToDrop = 2 + world.random.nextInt(2);
			List<ItemStack> results = new ArrayList<>();
			results.add(new ItemStack(Items.SWEET_BERRIES, berriesToDrop));
			List<ItemStack> remaining = attemptCollect(inv, results);
			if (remaining.equals(results)) {
				if (remaining.size() > 0) {
					spawnResults(world, pos.offset(Direction.UP, 2), remaining);
				}
				world.setBlockState(pos.offset(Direction.UP, 2), state.with(SweetBerryBushBlock.AGE, 1));
				return true;
			}
		}
		return false;
	}

	private static boolean harvestGourd(World world, BlockPos pos) {
		Inventory inv = HopperBlockEntity.getInventoryAt(world, pos);
		BlockPos stemPos = pos.offset(Direction.UP, 2);
		BlockState stem = world.getBlockState(stemPos);
		BlockPos gourdPos = stemPos.offset(stem.get(AttachedStemBlock.FACING));
		BlockState state = world.getBlockState(gourdPos);
		List<ItemStack> results = state.getDroppedStacks(getLootContext(world, pos));
		List<ItemStack> remaining = attemptCollect(inv, results);
		if (remaining.equals(results)) {
			if (remaining.size() > 0) {
				spawnResults(world, gourdPos, remaining);
			}
			world.breakBlock(gourdPos, false);
			return true;
		}
		return false;
	}

	private static boolean harvestCocoa(World world, BlockPos pos) {
		BlockPos logPos = pos.offset(Direction.UP);
		boolean didHarvest = false;
		for (Direction dir : Direction.values()) {
			if (dir == Direction.UP || dir == Direction.DOWN) continue;
			BlockPos checkPos = logPos.offset(dir);
			BlockState checkState = world.getBlockState(checkPos);
			if (checkState.getBlock() == Blocks.COCOA) {
				int age = checkState.get(CocoaBlock.AGE);
				if (age == 2) {
					Inventory inv = HopperBlockEntity.getInventoryAt(world, pos);
					List<ItemStack> results = checkState.getDroppedStacks(getLootContext(world, checkPos));
					List<ItemStack> remaining = attemptCollect(inv, results);
					if (remaining.equals(results)) {
						if (remaining.size() > 0) {
							spawnResults(world, checkPos, remaining);
						}
						world.setBlockState(checkPos, checkState.with(CocoaBlock.AGE, 0));
						didHarvest = true;
					}
				}
			}
		}
		return didHarvest;
	}

	private static LootContext.Builder getLootContext(World world, BlockPos pos) {
		return new LootContext
				.Builder(world.getServer().getWorld(world.dimension.getType()))
				.put(Parameters.POSITION, pos).put(Parameters.TOOL, ItemStack.EMPTY);
	}

	private static List<ItemStack> attemptCollect(Inventory inv, List<ItemStack> results) {
		List<ItemStack> remaining = new ArrayList<>();
		for (int i = 0; i < results.size(); i++) {
			ItemStack insert = HopperBlockEntity.transfer(null, inv, results.get(i), null);
			remaining.add(insert);
		}
		return remaining;
	}

	private static void spawnResults(World world, BlockPos spawnAt, List<ItemStack> toSpawn) {
		for (int i = 0; i < toSpawn.size(); i++) {
			world.spawnEntity(new ItemEntity(world, spawnAt.getX(), spawnAt.getY(), spawnAt.getZ(), toSpawn.get(i)));
		}
	}
}

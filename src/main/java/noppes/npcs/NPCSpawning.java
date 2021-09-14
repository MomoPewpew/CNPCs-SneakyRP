package noppes.npcs;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import noppes.npcs.controllers.SpawnController;
import noppes.npcs.controllers.data.SpawnData;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class NPCSpawning {
	private static Set eligibleChunksForSpawning = Sets.newHashSet();

	public static void findChunksForSpawning(WorldServer world) {
		if (!SpawnController.instance.data.isEmpty() && world.getWorldInfo().getWorldTotalTime() % 400L == 0L) {
			eligibleChunksForSpawning.clear();

			int k1;
			int l1;
			for (int i = 0; i < world.playerEntities.size(); ++i) {
				EntityPlayer entityplayer = (EntityPlayer) world.playerEntities.get(i);
				if (!entityplayer.isSpectator()) {
					int j = MathHelper.floor(entityplayer.posX / 16.0D);
					int k = MathHelper.floor(entityplayer.posZ / 16.0D);
					byte size = 7;

					for (k1 = -size; k1 <= size; ++k1) {
						for (l1 = -size; l1 <= size; ++l1) {
							ChunkPos chunkcoordintpair = new ChunkPos(k1 + j, l1 + k);
							if (!eligibleChunksForSpawning.contains(chunkcoordintpair)
									&& world.getWorldBorder().contains(chunkcoordintpair)) {
								PlayerChunkMapEntry playerinstance = world.getPlayerChunkMap()
										.getEntry(chunkcoordintpair.x, chunkcoordintpair.z);
								if (playerinstance != null && playerinstance.isSentToPlayers()) {
									eligibleChunksForSpawning.add(chunkcoordintpair);
								}
							}
						}
					}
				}
			}

			if (countNPCs(world) <= eligibleChunksForSpawning.size() / 16) {
				ArrayList tmp = new ArrayList(eligibleChunksForSpawning);
				Collections.shuffle(tmp);
				Iterator iterator = tmp.iterator();

				while (iterator.hasNext()) {
					ChunkPos chunkcoordintpair1 = (ChunkPos) iterator.next();
					BlockPos chunkposition = getChunk(world, chunkcoordintpair1.x, chunkcoordintpair1.z);
					int j1 = chunkposition.getX();
					k1 = chunkposition.getY();
					l1 = chunkposition.getZ();

					for (int i = 0; i < 3; ++i) {
						byte b1 = 6;
						int x = j1 + (world.rand.nextInt(b1) - world.rand.nextInt(b1));
						int y = k1 + (world.rand.nextInt(1) - world.rand.nextInt(1));
						int z = l1 + (world.rand.nextInt(b1) - world.rand.nextInt(b1));
						BlockPos pos = new BlockPos(x, y, z);
						IBlockState state = world.getBlockState(pos);
						String name = world.getBiomeForCoordsBody(pos).biomeName;
						SpawnData data = SpawnController.instance.getRandomSpawnData(name,
								state.getMaterial() == Material.AIR);
						if (data != null && canCreatureTypeSpawnAtLocation(data, world, pos)
								&& world.getClosestPlayer((double) x, (double) y, (double) z, 24.0D, false) == null) {
							spawnData(data, world, pos);
						}
					}
				}

			}
		}
	}

	public static int countNPCs(World world) {
		int count = 0;
		List list = world.loadedEntityList;
		Iterator var3 = list.iterator();

		while (var3.hasNext()) {
			Entity entity = (Entity) var3.next();
			if (entity instanceof EntityNPCInterface) {
				++count;
			}
		}

		return count;
	}

	protected static BlockPos getChunk(World world, int x, int z) {
		Chunk chunk = world.getChunk(x, z);
		int k = x * 16 + world.rand.nextInt(16);
		int l = z * 16 + world.rand.nextInt(16);
		int i1 = MathHelper.roundUp(chunk.getHeight(new BlockPos(k, 0, l)) + 1, 16);
		int j1 = world.rand.nextInt(i1 > 0 ? i1 : chunk.getTopFilledSegment() + 16 - 1);
		return new BlockPos(k, j1, l);
	}

	public static void performWorldGenSpawning(World world, int x, int z, Random rand) {
		Biome biome = world.getBiomeForCoordsBody(new BlockPos(x + 8, 0, z + 8));

		while (true) {
			SpawnData data;
			do {
				if (rand.nextFloat() >= biome.getSpawningChance()) {
					return;
				}

				data = SpawnController.instance.getRandomSpawnData(biome.biomeName, true);
			} while (data == null);

			int size = 16;
			int j1 = x + rand.nextInt(size);
			int k1 = z + rand.nextInt(size);
			int l1 = j1;
			int i2 = k1;

			for (int k2 = 0; k2 < 4; ++k2) {
				BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(j1, 0, k1));
				if (canCreatureTypeSpawnAtLocation(data, world, pos)) {
					if (spawnData(data, world, pos)) {
						break;
					}
				} else {
					j1 += rand.nextInt(5) - rand.nextInt(5);

					for (k1 += rand.nextInt(5) - rand.nextInt(5); j1 < x || j1 >= x + size || k1 < z
							|| k1 >= z + size; k1 = i2 + rand.nextInt(5) - rand.nextInt(5)) {
						j1 = l1 + rand.nextInt(5) - rand.nextInt(5);
					}
				}
			}
		}
	}

	private static boolean spawnData(SpawnData data, World world, BlockPos pos) {
		EntityLiving entityliving;
		try {
			Entity entity = EntityList.createEntityFromNBT(data.compound1, world);
			if (entity == null || !(entity instanceof EntityLiving)) {
				return false;
			}

			entityliving = (EntityLiving) entity;
			if (entity instanceof EntityCustomNpc) {
				EntityCustomNpc npc = (EntityCustomNpc) entity;
				npc.stats.spawnCycle = 4;
				npc.stats.respawnTime = 0;
				npc.ais.returnToStart = false;
				npc.ais.setStartPos(pos);
			}

			entity.setLocationAndAngles((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D,
					world.rand.nextFloat() * 360.0F, 0.0F);
		} catch (Exception var6) {
			var6.printStackTrace();
			return false;
		}

		Result canSpawn = ForgeEventFactory.canEntitySpawn(entityliving, world, (float) pos.getX() + 0.5F,
				(float) pos.getY(), (float) pos.getZ() + 0.5F);
		if (canSpawn != Result.DENY && (canSpawn != Result.DEFAULT || entityliving.getCanSpawnHere())) {
			world.spawnEntity(entityliving);
			return true;
		} else {
			return false;
		}
	}

	public static boolean canCreatureTypeSpawnAtLocation(SpawnData data, World world, BlockPos pos) {
		if (!world.getWorldBorder().contains(pos)) {
			return false;
		} else if (data.type == 1 && world.getLight(pos) > 8 || data.type == 2 && world.getLight(pos) <= 8) {
			return false;
		} else {
			IBlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			if (data.liquid) {
				return state.getMaterial().isLiquid() && world.getBlockState(pos.down()).getMaterial().isLiquid()
						&& !world.getBlockState(pos.up()).isNormalCube();
			} else {
				BlockPos blockpos1 = pos.down();
				IBlockState state1 = world.getBlockState(blockpos1);
				Block block1 = state1.getBlock();
				if (!state1.isSideSolid(world, blockpos1, EnumFacing.UP)) {
					return false;
				} else {
					boolean flag = block1 != Blocks.BEDROCK && block1 != Blocks.BARRIER;
					BlockPos down = blockpos1.down();
					flag |= world.getBlockState(down).getBlock().canCreatureSpawn(world.getBlockState(down), world,
							down, SpawnPlacementType.ON_GROUND);
					return flag && !state.isNormalCube() && !state.getMaterial().isLiquid()
							&& !world.getBlockState(pos.up()).isNormalCube();
				}
			}
		}
	}
}

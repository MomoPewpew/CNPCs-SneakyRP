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
          if (!SpawnController.instance.data.isEmpty() && world.func_72912_H().func_82573_f() % 400L == 0L) {
               eligibleChunksForSpawning.clear();

               int k1;
               int l1;
               for(int i = 0; i < world.field_73010_i.size(); ++i) {
                    EntityPlayer entityplayer = (EntityPlayer)world.field_73010_i.get(i);
                    if (!entityplayer.func_175149_v()) {
                         int j = MathHelper.func_76128_c(entityplayer.field_70165_t / 16.0D);
                         int k = MathHelper.func_76128_c(entityplayer.field_70161_v / 16.0D);
                         byte size = 7;

                         for(k1 = -size; k1 <= size; ++k1) {
                              for(l1 = -size; l1 <= size; ++l1) {
                                   ChunkPos chunkcoordintpair = new ChunkPos(k1 + j, l1 + k);
                                   if (!eligibleChunksForSpawning.contains(chunkcoordintpair) && world.func_175723_af().func_177730_a(chunkcoordintpair)) {
                                        PlayerChunkMapEntry playerinstance = world.func_184164_w().func_187301_b(chunkcoordintpair.field_77276_a, chunkcoordintpair.field_77275_b);
                                        if (playerinstance != null && playerinstance.func_187274_e()) {
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

                    while(iterator.hasNext()) {
                         ChunkPos chunkcoordintpair1 = (ChunkPos)iterator.next();
                         BlockPos chunkposition = getChunk(world, chunkcoordintpair1.field_77276_a, chunkcoordintpair1.field_77275_b);
                         int j1 = chunkposition.func_177958_n();
                         k1 = chunkposition.func_177956_o();
                         l1 = chunkposition.func_177952_p();

                         for(int i = 0; i < 3; ++i) {
                              byte b1 = 6;
                              int x = j1 + (world.field_73012_v.nextInt(b1) - world.field_73012_v.nextInt(b1));
                              int y = k1 + (world.field_73012_v.nextInt(1) - world.field_73012_v.nextInt(1));
                              int z = l1 + (world.field_73012_v.nextInt(b1) - world.field_73012_v.nextInt(b1));
                              BlockPos pos = new BlockPos(x, y, z);
                              IBlockState state = world.func_180495_p(pos);
                              String name = world.getBiomeForCoordsBody(pos).field_76791_y;
                              SpawnData data = SpawnController.instance.getRandomSpawnData(name, state.func_185904_a() == Material.field_151579_a);
                              if (data != null && canCreatureTypeSpawnAtLocation(data, world, pos) && world.func_184137_a((double)x, (double)y, (double)z, 24.0D, false) == null) {
                                   spawnData(data, world, pos);
                              }
                         }
                    }

               }
          }
     }

     public static int countNPCs(World world) {
          int count = 0;
          List list = world.field_72996_f;
          Iterator var3 = list.iterator();

          while(var3.hasNext()) {
               Entity entity = (Entity)var3.next();
               if (entity instanceof EntityNPCInterface) {
                    ++count;
               }
          }

          return count;
     }

     protected static BlockPos getChunk(World world, int x, int z) {
          Chunk chunk = world.func_72964_e(x, z);
          int k = x * 16 + world.field_73012_v.nextInt(16);
          int l = z * 16 + world.field_73012_v.nextInt(16);
          int i1 = MathHelper.func_154354_b(chunk.func_177433_f(new BlockPos(k, 0, l)) + 1, 16);
          int j1 = world.field_73012_v.nextInt(i1 > 0 ? i1 : chunk.func_76625_h() + 16 - 1);
          return new BlockPos(k, j1, l);
     }

     public static void performWorldGenSpawning(World world, int x, int z, Random rand) {
          Biome biome = world.getBiomeForCoordsBody(new BlockPos(x + 8, 0, z + 8));

          while(true) {
               SpawnData data;
               do {
                    if (rand.nextFloat() >= biome.func_76741_f()) {
                         return;
                    }

                    data = SpawnController.instance.getRandomSpawnData(biome.field_76791_y, true);
               } while(data == null);

               int size = 16;
               int j1 = x + rand.nextInt(size);
               int k1 = z + rand.nextInt(size);
               int l1 = j1;
               int i2 = k1;

               for(int k2 = 0; k2 < 4; ++k2) {
                    BlockPos pos = world.func_175672_r(new BlockPos(j1, 0, k1));
                    if (canCreatureTypeSpawnAtLocation(data, world, pos)) {
                         if (spawnData(data, world, pos)) {
                              break;
                         }
                    } else {
                         j1 += rand.nextInt(5) - rand.nextInt(5);

                         for(k1 += rand.nextInt(5) - rand.nextInt(5); j1 < x || j1 >= x + size || k1 < z || k1 >= z + size; k1 = i2 + rand.nextInt(5) - rand.nextInt(5)) {
                              j1 = l1 + rand.nextInt(5) - rand.nextInt(5);
                         }
                    }
               }
          }
     }

     private static boolean spawnData(SpawnData data, World world, BlockPos pos) {
          EntityLiving entityliving;
          try {
               Entity entity = EntityList.func_75615_a(data.compound1, world);
               if (entity == null || !(entity instanceof EntityLiving)) {
                    return false;
               }

               entityliving = (EntityLiving)entity;
               if (entity instanceof EntityCustomNpc) {
                    EntityCustomNpc npc = (EntityCustomNpc)entity;
                    npc.stats.spawnCycle = 4;
                    npc.stats.respawnTime = 0;
                    npc.ais.returnToStart = false;
                    npc.ais.setStartPos(pos);
               }

               entity.func_70012_b((double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o(), (double)pos.func_177952_p() + 0.5D, world.field_73012_v.nextFloat() * 360.0F, 0.0F);
          } catch (Exception var6) {
               var6.printStackTrace();
               return false;
          }

          Result canSpawn = ForgeEventFactory.canEntitySpawn(entityliving, world, (float)pos.func_177958_n() + 0.5F, (float)pos.func_177956_o(), (float)pos.func_177952_p() + 0.5F);
          if (canSpawn != Result.DENY && (canSpawn != Result.DEFAULT || entityliving.func_70601_bi())) {
               world.func_72838_d(entityliving);
               return true;
          } else {
               return false;
          }
     }

     public static boolean canCreatureTypeSpawnAtLocation(SpawnData data, World world, BlockPos pos) {
          if (!world.func_175723_af().func_177746_a(pos)) {
               return false;
          } else if (data.type == 1 && world.func_175699_k(pos) > 8 || data.type == 2 && world.func_175699_k(pos) <= 8) {
               return false;
          } else {
               IBlockState state = world.func_180495_p(pos);
               Block block = state.func_177230_c();
               if (data.liquid) {
                    return state.func_185904_a().func_76224_d() && world.func_180495_p(pos.func_177977_b()).func_185904_a().func_76224_d() && !world.func_180495_p(pos.func_177984_a()).func_185915_l();
               } else {
                    BlockPos blockpos1 = pos.func_177977_b();
                    IBlockState state1 = world.func_180495_p(blockpos1);
                    Block block1 = state1.func_177230_c();
                    if (!state1.isSideSolid(world, blockpos1, EnumFacing.UP)) {
                         return false;
                    } else {
                         boolean flag = block1 != Blocks.field_150357_h && block1 != Blocks.field_180401_cv;
                         BlockPos down = blockpos1.func_177977_b();
                         flag |= world.func_180495_p(down).func_177230_c().canCreatureSpawn(world.func_180495_p(down), world, down, SpawnPlacementType.ON_GROUND);
                         return flag && !state.func_185915_l() && !state.func_185904_a().func_76224_d() && !world.func_180495_p(pos.func_177984_a()).func_185915_l();
                    }
               }
          }
     }
}

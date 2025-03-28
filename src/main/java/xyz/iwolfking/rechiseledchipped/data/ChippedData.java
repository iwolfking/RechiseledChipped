 package xyz.iwolfking.rechiseledchipped.data;
 
 import earth.terrarium.chipped.common.registry.base.ChippedPaletteRegistry;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import net.minecraft.core.registries.BuiltInRegistries;
 import net.minecraft.resources.ResourceLocation;
 import net.minecraft.world.level.block.Block;
 import net.minecraftforge.registries.ForgeRegistries;


 public class ChippedData
 {
   public static final Map<Block, ResourceLocation> CHIPPED_CHISELING_CONFIGS = new HashMap<>();
   public static final List<ChippedPaletteRegistry<Block>> CHIPPED_BLOCK_REGISTRIES = new ArrayList<>();
   
   public static ResourceLocation rechiseledLocation(String name) {
     return new ResourceLocation("rechiseled_chipped", name);
   }
   public static void addEntry(Block block) {
     if (CHIPPED_CHISELING_CONFIGS.containsKey(block)) {
       return;
     }
     CHIPPED_CHISELING_CONFIGS.put(block, rechiseledLocation(ForgeRegistries.BLOCKS.getKey(block).getPath()));
   }
   
   public static void addRegistry(ChippedPaletteRegistry<Block> registry) {
     if (CHIPPED_BLOCK_REGISTRIES.contains(registry)) {
       return;
     }
     CHIPPED_BLOCK_REGISTRIES.add(registry);
   }
 }



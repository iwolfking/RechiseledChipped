 package xyz.iwolfking.rechiseledchipped.data;
 import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
 import earth.terrarium.chipped.common.registry.base.ChippedPaletteRegistry;
 import net.minecraft.resources.ResourceLocation;
 import net.minecraft.world.level.ItemLike;
 import xyz.iwolfking.rechiseledchipped.RechiseledChipped;
 
 public class Recipes {
   public static ResourceLocation location(String name) {
     return new ResourceLocation("rechiseled_chipped", name);
   }
 
 
   
   public static void init() {
       ChippedData.CHIPPED_BLOCK_REGISTRIES.forEach((registry) -> {
               RechiseledChipped.REGISTRATION.chiselingEntry(ChippedData.CHIPPED_CHISELING_CONFIGS.get(registry.getBase()), () -> registry.getBase().asItem(), null);
               registry.getEntries().forEach(blockRegistryEntry -> {
                   RechiseledChipped.REGISTRATION.chiselingEntry(ChippedData.CHIPPED_CHISELING_CONFIGS.get(registry.getBase()), () -> blockRegistryEntry.get().asItem(), null);
               });
       });
   }
 }



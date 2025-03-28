 package xyz.iwolfking.rechiseledchipped;
 
 import com.mojang.logging.LogUtils;
 import com.supermartijn642.rechiseled.api.registration.RechiseledRegistration;
 import net.minecraft.client.Minecraft;
 import net.minecraft.world.item.Item;
 import net.minecraft.world.level.block.Blocks;
 import net.minecraftforge.api.distmarker.Dist;
 import net.minecraftforge.common.MinecraftForge;
 import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
 import net.minecraftforge.event.server.ServerStartingEvent;
 import net.minecraftforge.eventbus.api.IEventBus;
 import net.minecraftforge.eventbus.api.SubscribeEvent;
 import net.minecraftforge.fml.ModLoadingContext;
 import net.minecraftforge.fml.common.Mod;
 import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
 import net.minecraftforge.fml.config.IConfigSpec;
 import net.minecraftforge.fml.config.ModConfig;
 import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
 import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
 import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
 import net.minecraftforge.registries.ForgeRegistries;
 import org.slf4j.Logger;

 @Mod(RechiseledChipped.MODID)
 public class RechiseledChipped
 {
   public static final String MODID = "rechiseled_chipped";
   private static final Logger LOGGER = LogUtils.getLogger();
   
   public static final RechiseledRegistration REGISTRATION = RechiseledRegistration.get(MODID);
 

   public RechiseledChipped() {
     IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

     MinecraftForge.EVENT_BUS.register(this);
   }
 }



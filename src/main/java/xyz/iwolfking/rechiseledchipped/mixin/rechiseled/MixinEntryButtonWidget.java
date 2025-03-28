 package xyz.iwolfking.rechiseledchipped.mixin.rechiseled;
 
 import com.supermartijn642.core.gui.ScreenUtils;
 import com.supermartijn642.core.gui.widget.BaseWidget;
 import com.supermartijn642.core.gui.widget.WidgetRenderContext;
 import com.supermartijn642.rechiseled.chiseling.ChiselingEntry;
 import com.supermartijn642.rechiseled.screen.EntryButtonWidget;
 import com.supermartijn642.rechiseled.screen.ScreenItemRender;
 import java.util.function.Consumer;
 import java.util.function.Supplier;
 import net.minecraft.network.chat.Component;
 import net.minecraft.resources.ResourceLocation;
 import net.minecraft.world.item.Item;
 import org.spongepowered.asm.mixin.Final;
 import org.spongepowered.asm.mixin.Mixin;
 import org.spongepowered.asm.mixin.Overwrite;
 import org.spongepowered.asm.mixin.Shadow;
 
 @Mixin(value = {EntryButtonWidget.class}, remap = false)
 public abstract class MixinEntryButtonWidget
   extends BaseWidget
 {
   @Shadow
   @Final
   private Supplier<ChiselingEntry> entry;
   
   public MixinEntryButtonWidget(int x, int y, int width, int height) {
     super(x, y, width, height);
   }
   @Shadow
   @Final
   private Supplier<ChiselingEntry> selectedEntry; @Shadow
   @Final
   private Supplier<Boolean> connecting;
   /**
    * @author iwolfking
    * @reason Replace entry rendering
    */
   @Overwrite
   public void render(WidgetRenderContext context, int mouseX, int mouseY) {
     ChiselingEntry entry = this.entry.get();
     
     boolean hasEntry = (entry != null);
     boolean selected = (hasEntry && this.selectedEntry.get() == entry);
     boolean hasCorrectItem = (hasEntry && (((Boolean)this.connecting.get()).booleanValue() ? entry.hasConnectingItem() : entry.hasRegularItem()));
     
     if (hasEntry) {
       ScreenUtils.bindTexture(TEXTURE);
       ScreenUtils.drawTexture(context.poseStack(), this.x, this.y, this.width, this.height, 0.0F, (selected ? 1 : hasEntry ? hasCorrectItem ? this.isFocused() ? 2 : 0 : this.isFocused() ? 4 : 3 : 0) / 5f, 1.0F, 0.2F);
       
       Item item = ((((Boolean)this.connecting.get()).booleanValue() && entry.hasConnectingItem()) || !entry.hasRegularItem()) ? entry.getConnectingItem() : entry.getRegularItem();
       ScreenItemRender.drawItem(context.poseStack(), item, this.x + this.width / 2.0D, this.y + this.height / 2.0D, (this.width - 4), 0.0F, 0.0F, false);
     } 
   }
 
 
   
   protected void getTooltips(Consumer<Component> tooltips) {
     ChiselingEntry entry = this.entry.get();
     if (entry == null) {
       return;
     }
     Item item = ((((Boolean)this.connecting.get()).booleanValue() && entry.hasConnectingItem()) || !entry.hasRegularItem()) ? entry.getConnectingItem() : entry.getRegularItem();
     if (item == null) {
       return;
     }
     tooltips.accept(item.getDefaultInstance().getHoverName());
   }
   
   @Shadow
   @Final
   private static ResourceLocation TEXTURE;
 }



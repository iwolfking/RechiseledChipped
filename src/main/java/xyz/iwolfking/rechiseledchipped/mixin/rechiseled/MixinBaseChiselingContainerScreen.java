 package xyz.iwolfking.rechiseledchipped.mixin.rechiseled;
 
 import com.supermartijn642.core.gui.widget.BaseContainerWidget;
 import com.supermartijn642.core.gui.widget.Widget;
 import com.supermartijn642.core.gui.widget.WidgetRenderContext;
 import com.supermartijn642.rechiseled.chiseling.ChiselingEntry;
 import com.supermartijn642.rechiseled.chiseling.ChiselingRecipe;
 import com.supermartijn642.rechiseled.screen.BaseChiselingContainer;
 import com.supermartijn642.rechiseled.screen.BaseChiselingContainerScreen;
 import com.supermartijn642.rechiseled.screen.ChiselAllWidget;
 import com.supermartijn642.rechiseled.screen.ConnectingToggleWidget;
 import com.supermartijn642.rechiseled.screen.EntryButtonWidget;
 import com.supermartijn642.rechiseled.screen.EntryPreviewWidget;
 import com.supermartijn642.rechiseled.screen.PreviewModeButtonWidget;
 import java.util.Objects;
 import java.util.function.Supplier;
 import net.minecraft.world.item.Item;
 import org.spongepowered.asm.mixin.Mixin;
 import org.spongepowered.asm.mixin.Overwrite;
 import org.spongepowered.asm.mixin.Shadow;
 import xyz.iwolfking.rechiseledchipped.gui.ScrollWidget;
 
 @Mixin(value = {BaseChiselingContainerScreen.class}, remap = false)
 public abstract class MixinBaseChiselingContainerScreen<T extends BaseChiselingContainer>
   extends BaseContainerWidget<T> {
   @Shadow
   public static int previewMode;
   @Shadow
   private ChiselAllWidget chiselAllWidget;
   
   public MixinBaseChiselingContainerScreen(int x, int y, int width, int height) {
     super(x, y, width, height);
     this.currentScroll = 0;
   }
   private ScrollWidget scrollWidget;
   private int numColumns = 4;
   private int numRows = 5;
   private int numScrolls;
   private int currentScroll;
   private ChiselingRecipe previousRecipe;
   
   /**
    * @author iwolfking
    * @reason Replace Rechiseled screen
    */
   @Overwrite
   protected void addWidgets() {
     this.scrollWidget = new ScrollWidget(96, 17, 14, 110, this::scrollChanged);
     addWidget((Widget)this.scrollWidget);
     
     this.previousRecipe = ((BaseChiselingContainer)this.container).currentRecipe;
     updateScrollData();
     
     int offsetX = 9;
     int offsetY = 17;
     int entryWidth = 20;
     int entryHeight = 22;
     for (int row = 0; row < 5; row++) {
       for (int column = 0; column < 4; column++) {
         int index = row * 4 + column;
         int x = offsetX + entryWidth * column;
         int y = offsetY + entryHeight * row;
         
         EntryButtonWidget entryWidget = new EntryButtonWidget(x, y, entryWidth, entryHeight, () -> getEntry(4 * this.currentScroll + index), () -> ((BaseChiselingContainer)this.container).currentEntry, () -> selectEntry(4 * this.currentScroll + index), () -> Boolean.valueOf(((BaseChiselingContainer)this.container).connecting));
 
 
 
 
         
         addWidget((Widget)entryWidget);
       } 
     } 
     
     addWidget((Widget)new EntryPreviewWidget(117, 17, 68, 69, () -> {
             ChiselingEntry entry = ((BaseChiselingContainer)this.container).currentEntry;
 
             
             return (entry == null) ? null : (((((BaseChiselingContainer)this.container).connecting && entry.hasConnectingItem()) || !entry.hasRegularItem()) ? entry.getConnectingItem() : entry.getRegularItem());
           }, () -> previewMode));
     Supplier<Boolean> enablePreviewButtons = () -> {
         ChiselingEntry entry = ((BaseChiselingContainer)this.container).currentEntry;
         if (entry == null)
           return Boolean.valueOf(false); 
         Item currentItem = ((((BaseChiselingContainer)this.container).connecting && entry.hasConnectingItem()) || !entry.hasRegularItem()) ? entry.getConnectingItem() : entry.getRegularItem();
         return Boolean.valueOf(currentItem instanceof net.minecraft.world.item.BlockItem);
       };
     addWidget((Widget)new PreviewModeButtonWidget(193, 18, 19, 21, 2, () -> Integer.valueOf(previewMode), enablePreviewButtons, () -> previewMode = 2));
     addWidget((Widget)new PreviewModeButtonWidget(193, 41, 19, 21, 1, () -> Integer.valueOf(previewMode), enablePreviewButtons, () -> previewMode = 1));
     addWidget((Widget)new PreviewModeButtonWidget(193, 64, 19, 21, 0, () -> Integer.valueOf(previewMode), enablePreviewButtons, () -> previewMode = 0));
     addWidget((Widget)new ConnectingToggleWidget(193, 99, 19, 21, () -> Boolean.valueOf(((BaseChiselingContainer)this.container).connecting), () -> ((BaseChiselingContainer)this.container).currentEntry, this::toggleConnecting));
     this.chiselAllWidget = (ChiselAllWidget)addWidget((Widget)new ChiselAllWidget(127, 99, 19, 21, () -> ((BaseChiselingContainer)this.container).currentEntry, this::chiselAll));
   }
 
   
   public void render(WidgetRenderContext context, int mouseX, int mouseY) {
     if (this.previousRecipe != ((BaseChiselingContainer)this.container).currentRecipe) {
       updateScrollData();
       this.previousRecipe = ((BaseChiselingContainer)this.container).currentRecipe;
     } 
     
     super.render(context, mouseX, mouseY);
   }
 
   
   public boolean mouseScrolled(int mouseX, int mouseY, double scrollAmount, boolean hasBeenHandled) {
     if (mouseX >= 8 && mouseX <= 110 && mouseY >= 16 && mouseY <= 127) {
       this.currentScroll -= (int)scrollAmount;
       
       clampCurrentScroll();
       
       if (this.scrollWidget != null)
         this.scrollWidget.setScrollRatio((this.numScrolls == 0) ? 0.0F : (this.currentScroll / this.numScrolls)); 
     } 
     return super.mouseScrolled(mouseX, mouseY, scrollAmount, hasBeenHandled);
   }
   
   private void scrollChanged() {
     this.currentScroll = (int)(this.numScrolls * this.scrollWidget.getScrollRatio());
     clampCurrentScroll();
   }
   
   private void updateScrollData() {
     boolean hasRecipe = (((BaseChiselingContainer)this.container).currentRecipe != null);
     int numRecipes = hasRecipe ? ((BaseChiselingContainer)this.container).currentRecipe.getEntries().size() : 0;
     if (((BaseChiselingContainer)this.container).currentRecipe == null) {
       this.numScrolls = 0;
     } else {
       this.numScrolls = (int)Math.ceil(numRecipes / 4.0D) - 5;
       if (this.numScrolls < 0) {
         this.numScrolls = 0;
       }
     } 
     if (hasRecipe) {
       if (((BaseChiselingContainer)this.container).currentEntry != null) {
         int indexOfItem = ((BaseChiselingContainer)this.container).currentRecipe.getEntries().indexOf(((BaseChiselingContainer)this.container).currentEntry);
         Objects.requireNonNull(this); int itemRow = (int)Math.ceil((indexOfItem + 1) / 4.0D);
         
         this.currentScroll = itemRow - 5;
         
         clampCurrentScroll();
       } 
     } else {
       this.currentScroll = 0;
     } 
     
     if (this.scrollWidget != null) {
       this.scrollWidget.setIntSnapMax(this.numScrolls);
       this.scrollWidget.setIsActive((this.numScrolls > 0));
       this.scrollWidget.setScrollRatio((this.numScrolls == 0.0F) ? 0.0F : (this.currentScroll / this.numScrolls));
     } 
   }
   
   private void clampCurrentScroll() {
     if (this.currentScroll < 0)
       this.currentScroll = 0; 
     if (this.currentScroll > this.numScrolls)
       this.currentScroll = this.numScrolls; 
   }
   
   @Shadow
   protected abstract ChiselingEntry getEntry(int paramInt);
   
   @Shadow
   protected abstract void selectEntry(int paramInt);
   
   @Shadow
   protected abstract void toggleConnecting();
   
   @Shadow
   protected abstract void chiselAll();
 }



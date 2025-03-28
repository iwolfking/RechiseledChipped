 package xyz.iwolfking.rechiseledchipped.gui;
 import com.supermartijn642.core.gui.ScreenUtils;
 import com.supermartijn642.core.gui.widget.BaseWidget;
 import com.supermartijn642.core.gui.widget.WidgetRenderContext;
 import java.util.Objects;
 import net.minecraft.network.chat.Component;
 import net.minecraft.resources.ResourceLocation;
 
 public class ScrollWidget extends BaseWidget {
   private final Runnable onScrollChanged;
   private final int handleWidth = 14;
   private final int handleHeight = 17;
   private boolean isDragging;
   private static final ResourceLocation HANDLES = new ResourceLocation("rechiseled", "textures/screen/handles.png"); private float scrollRatio;
   
   public ScrollWidget(int x, int y, int width, int height, Runnable onScrollChanged) {
     super(x, y, width, height);
     
     this.isDragging = false;
     
     this.scrollRatio = 0.0F;
     
     this.intSnapMax = 0;
     
     this.isActive = true;
     this.onScrollChanged = onScrollChanged;
   } private int intSnapMax; private boolean isActive; public void setScrollRatio(float scrollRatio) {
     if (!this.isActive) {
       return;
     }
     if (scrollRatio > 1.0F) {
       scrollRatio = 1.0F;
     }
     if (scrollRatio < 0.0F) {
       scrollRatio = 0.0F;
     }
     this.scrollRatio = scrollRatio;
   }
   
   public float getScrollRatio() {
     return this.scrollRatio;
   }
   
   public void setIntSnapMax(int intSnapMax) {
     this.intSnapMax = intSnapMax;
   }
   
   public int getIntSnapMax() {
     return this.intSnapMax;
   }
   
   public void setIsActive(boolean isActive) {
     this.isActive = isActive;
     if (!isActive)
       this.scrollRatio = 0.0F; 
   }
   
   public boolean getIsActive() {
     return this.isActive;
   }
   
   public float getHandleY() {
     Objects.requireNonNull(this); return this.y + (this.height - 17) * this.scrollRatio;
   }
   
   public float getHandleHalfHeight() {
     Objects.requireNonNull(this); return 17.0F * 0.5F;
   }
   
   public Component getNarrationMessage() {
     return null;
   }
   
   public void render(WidgetRenderContext context, int mouseX, int mouseY) {
     if (this.isActive && this.isDragging) {
       float handleHalfHeight = getHandleHalfHeight();
       Objects.requireNonNull(this); float mouseYRelative = (mouseY - this.y - handleHalfHeight) / Math.max(1, this.height - 17);
       
       if (this.intSnapMax > 0) {
         mouseYRelative = (float)Math.floor((mouseYRelative * this.intSnapMax)) / this.intSnapMax;
       }
       setScrollRatio(mouseYRelative);
       if (this.onScrollChanged != null) {
         this.onScrollChanged.run();
       }
     } 
     ScreenUtils.bindTexture(HANDLES);
     ScreenUtils.drawTexture(context.poseStack(), this.x, getHandleY(), 14.0F, 17.0F, 0.0F, this.isActive ? 0.0F : 0.5F, 1.0F, 0.5F);
     
     super.render(context, mouseX, mouseY);
   }
 
   
   public boolean mousePressed(int mouseX, int mouseY, int button, boolean hasBeenHandled) {
     this.isDragging = (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height);
     
     return super.mousePressed(mouseX, mouseY, button, hasBeenHandled);
   }
 
   
   public boolean mouseReleased(int mouseX, int mouseY, int button, boolean hasBeenHandled) {
     this.isDragging = false;
     
     return super.mouseReleased(mouseX, mouseY, button, hasBeenHandled);
   }
 }



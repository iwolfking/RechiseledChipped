// Updated MixinBaseChiselingContainerScreen with search box filtering
// NOTE: This is a template; adjust coordinates and widget imports as needed.

package xyz.iwolfking.rechiseledchipped.mixin.rechiseled;

import com.supermartijn642.core.gui.widget.*;
import com.supermartijn642.core.gui.widget.premade.TextFieldWidget;
import com.supermartijn642.rechiseled.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipe;
import com.supermartijn642.rechiseled.screen.*;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import xyz.iwolfking.rechiseledchipped.gui.ScrollWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mixin(value = {BaseChiselingContainerScreen.class}, remap = false)
public abstract class MixinBaseChiselingContainerScreen<T extends BaseChiselingContainer> extends BaseContainerWidget<T> {

    @Shadow public static int previewMode;
    @Shadow private ChiselAllWidget chiselAllWidget;

    public MixinBaseChiselingContainerScreen(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.currentScroll = 0;
    }

    private ScrollWidget scrollWidget;
    private TextFieldWidget searchBox;

    private int numColumns = 4;
    private int numRows = 5;

    private int numScrolls;
    private int currentScroll;

    private ChiselingRecipe previousRecipe;
    private List<ChiselingEntry> filteredEntries = new ArrayList<>();

    /**
     * @author
     * @reason
     */
    @Overwrite
    protected void addWidgets() {
        // Search box widget at top-left above entries
        this.searchBox = new TextFieldWidget(8, 2, 100, 12, "", 64, this::onSearchChanged);
        addWidget(this.searchBox);

        this.scrollWidget = new ScrollWidget(96, 17, 14, 110, this::scrollChanged);
        addWidget(this.scrollWidget);

        this.previousRecipe = ((BaseChiselingContainer)this.container).currentRecipe;
        updateFilteredEntries();
        updateScrollData();

        int offsetX = 9;
        int offsetY = 17;
        int entryWidth = 20;
        int entryHeight = 22;

        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numColumns; column++) {
                int index = row * numColumns + column;
                int x = offsetX + entryWidth * column;
                int y = offsetY + entryHeight * row;

                EntryButtonWidget entryWidget = new EntryButtonWidget(
                        x, y, entryWidth, entryHeight,
                        () -> getFilteredEntry(numColumns * currentScroll + index),
                        () -> ((BaseChiselingContainer)this.container).currentEntry,
                        () -> selectFilteredEntry(numColumns * currentScroll + index),
                        () -> ((BaseChiselingContainer)this.container).connecting
                );

                addWidget(entryWidget);
            }
        }

        // PREVIEW + buttons unchanged
        addWidget(new EntryPreviewWidget(117, 17, 68, 69, () -> {
            ChiselingEntry entry = ((BaseChiselingContainer)this.container).currentEntry;
            return (entry == null) ? null : ((((BaseChiselingContainer)this.container).connecting && entry.hasConnectingItem()) || !entry.hasRegularItem()) ? entry.getConnectingItem() : entry.getRegularItem();
        }, () -> previewMode));

        Supplier<Boolean> enablePreviewButtons = () -> {
            ChiselingEntry entry = ((BaseChiselingContainer)this.container).currentEntry;
            if (entry == null) return false;
            Item currentItem = ((((BaseChiselingContainer)this.container).connecting && entry.hasConnectingItem()) || !entry.hasRegularItem()) ? entry.getConnectingItem() : entry.getRegularItem();
            return currentItem instanceof net.minecraft.world.item.BlockItem;
        };

        addWidget(new PreviewModeButtonWidget(193, 18, 19, 21, 2, () -> previewMode, enablePreviewButtons, () -> previewMode = 2));
        addWidget(new PreviewModeButtonWidget(193, 41, 19, 21, 1, () -> previewMode, enablePreviewButtons, () -> previewMode = 1));
        addWidget(new PreviewModeButtonWidget(193, 64, 19, 21, 0, () -> previewMode, enablePreviewButtons, () -> previewMode = 0));
        addWidget(new ConnectingToggleWidget(193, 99, 19, 21, () -> ((BaseChiselingContainer)this.container).connecting, () -> ((BaseChiselingContainer)this.container).currentEntry, this::toggleConnecting));

        this.chiselAllWidget = (ChiselAllWidget)addWidget(new ChiselAllWidget(127, 99, 19, 21, () -> ((BaseChiselingContainer)this.container).currentEntry, this::chiselAll));
    }

    @Override
    public void render(WidgetRenderContext context, int mouseX, int mouseY) {
        if (this.previousRecipe != ((BaseChiselingContainer)this.container).currentRecipe) {
            updateFilteredEntries();
            updateScrollData();
            this.previousRecipe = ((BaseChiselingContainer)this.container).currentRecipe;
        }
        super.render(context, mouseX, mouseY);
    }

    private void onSearchChanged(String text) {
        updateFilteredEntries();
        updateScrollData();
    }

    private void updateFilteredEntries() {
        ChiselingRecipe recipe = ((BaseChiselingContainer)this.container).currentRecipe;
        filteredEntries.clear();
        if (recipe == null) return;

        String filter = searchBox.getText().toLowerCase();
        for (ChiselingEntry entry : recipe.getEntries()) {
            String itemName = entry.getRegularItem().getDescription().getString().toLowerCase();
            if (itemName.contains(filter)) filteredEntries.add(entry);
        }
    }

    private ChiselingEntry getFilteredEntry(int index) {
        if (index < 0 || index >= filteredEntries.size()) return null;
        return filteredEntries.get(index);
    }

    private void selectFilteredEntry(int index) {
        ChiselingEntry entry = getFilteredEntry(index);
        if (entry != null) selectEntry(((BaseChiselingContainer)this.container).currentRecipe.getEntries().indexOf(entry));
    }

    private void updateScrollData() {
        int size = filteredEntries.size();

        numScrolls = (int)Math.ceil(size / (double)numColumns) - numRows;
        if (numScrolls < 0)
            numScrolls = 0;

        if (currentScroll > numScrolls)
            currentScroll = numScrolls;

        if (scrollWidget != null) {
            scrollWidget.setIntSnapMax(numScrolls);
            scrollWidget.setIsActive(numScrolls > 0);
            scrollWidget.setScrollRatio(numScrolls == 0 ? 0f : (float)currentScroll / numScrolls);
        }
    }


    private void scrollChanged() {
        currentScroll = (int)(numScrolls * scrollWidget.getScrollRatio());

        if (currentScroll < 0)
            currentScroll = 0;
        if (currentScroll > numScrolls)
            currentScroll = numScrolls;
    }


    @Override
    public boolean mouseScrolled(int mouseX, int mouseY, double scrollAmount, boolean hasBeenHandled) {
        int top = 17;
        int bottom = top + (22 * numRows);
        int left = 8;
        int right = 110;

        // Check if mouse is over the entry grid area
        if (mouseX >= left && mouseX <= right && mouseY >= top && mouseY <= bottom) {
            // Invert scroll direction depending on wheel
            currentScroll -= (scrollAmount > 0 ? 1 : -1);

            if (currentScroll < 0)
                currentScroll = 0;
            if (currentScroll > numScrolls)
                currentScroll = numScrolls;

            if (scrollWidget != null) {
                scrollWidget.setScrollRatio(numScrolls == 0 ? 0f : (float) currentScroll / numScrolls);
            }
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, scrollAmount, hasBeenHandled);
    }

    @Override
    public boolean keyPressed(int keyCode, boolean hasBeenHandled) {
        if (this.searchBox != null && this.searchBox.isFocused()) {
            boolean ctrl =
                    org.lwjgl.glfw.GLFW.glfwGetKey(
                            Minecraft.getInstance().getWindow().getWindow(),
                            org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL
                    ) == org.lwjgl.glfw.GLFW.GLFW_PRESS
                            || org.lwjgl.glfw.GLFW.glfwGetKey(
                            Minecraft.getInstance().getWindow().getWindow(),
                            org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_CONTROL
                    ) == org.lwjgl.glfw.GLFW.GLFW_PRESS;

            if (ctrl && keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_A) {
                // Consume the event to prevent GUI-wide highlight
                // Optional: clear text for overwriting
                this.searchBox.setText("");
                return true;
            }
        }

        return super.keyPressed(keyCode, hasBeenHandled);
    }







    @Shadow protected abstract ChiselingEntry getEntry(int index);
    @Shadow protected abstract void selectEntry(int index);
    @Shadow protected abstract void toggleConnecting();
    @Shadow protected abstract void chiselAll();
}

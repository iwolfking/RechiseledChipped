package xyz.iwolfking.rechiseledchipped;

import com.supermartijn642.rechiseled.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipe;

import java.util.List;

public class ChiselingRecipeJEIPage {

    private final ChiselingRecipe parentRecipe;
    private final List<ChiselingEntry> pageEntries;

    public ChiselingRecipeJEIPage(ChiselingRecipe parentRecipe, List<ChiselingEntry> pageEntries) {
        this.parentRecipe = parentRecipe;
        this.pageEntries = pageEntries;
    }

    public List<ChiselingEntry> getEntries() {
        return pageEntries;
    }

    public ChiselingRecipe getParentRecipe() {
        return parentRecipe;
    }
}
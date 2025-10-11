package grill24.adaptiveores.neoforge.data;

import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.neoforge.AdaptiveOresNeoForge;
import grill24.adaptiveores.data.CommonBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class ModBlockTags extends net.neoforged.neoforge.common.data.BlockTagsProvider {

    public ModBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        // NeoForge's BlockTagsProvider expects four arguments in its constructor; pass null for the existing file helper
        super(output, lookupProvider, AdaptiveOres.MOD_ID, null);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Delegate vanilla tag registration to the common helper using a lambda that calls this.tag(...).add(...)
        CommonBlockTags.addVanillaTags((tag, blocks) -> this.tag(tag).add(blocks));
    }
}

package grill24.adaptiveores;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

@FunctionalInterface
public interface TagAdder<T> {
    void add(TagKey<T> tag, ResourceKey<T>... items);
}


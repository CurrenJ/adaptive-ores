package grill24.adaptiveores.platform.registry.fabric;

import grill24.adaptiveores.data.AdaptiveOreSettings;
import grill24.adaptiveores.data.AdaptiveOreSettingsRegistry;
import grill24.adaptiveores.platform.registry.IRegistryHelper;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class FabricRegistryHelper implements IRegistryHelper {
    private final String modId;

    public FabricRegistryHelper(String modId) {
        this.modId = modId;
    }

    @Override
    public Holder<Block> registerBlock(ResourceLocation name, Supplier<Block> blockSupplier) {
        Block block = blockSupplier.get();
        Registry.register(BuiltInRegistries.BLOCK, name, block);
        return Holder.direct(block);
    }

    @Override
    public Holder<Block> registerBlock(String name, Supplier<Block> blockSupplier) {
        return registerBlock(ResourceLocation.fromNamespaceAndPath(modId, name), blockSupplier);
    }

    @Override
    public Holder<Item> registerItem(ResourceLocation name, Supplier<Item> itemSupplier) {
        Item item = itemSupplier.get();
        Registry.register(BuiltInRegistries.ITEM, name, item);
        return Holder.direct(item);
    }

    @Override
    public Holder<Item> registerItem(String name, Supplier<Item> itemSupplier) {
        return registerItem(ResourceLocation.fromNamespaceAndPath(modId, name), itemSupplier);
    }

    @Override
    public Holder<BlockEntityType<?>> registerBlockEntity(ResourceLocation name, Supplier<BlockEntityType<?>> blockEntitySupplier) {
        BlockEntityType<?> blockEntityType = blockEntitySupplier.get();
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, name, blockEntityType);
        return Holder.direct(blockEntityType);
    }

    @Override
    public Holder<BlockEntityType<?>> registerBlockEntity(String name, Supplier<BlockEntityType<?>> blockEntitySupplier) {
        return registerBlockEntity(ResourceLocation.fromNamespaceAndPath(modId, name), blockEntitySupplier);
    }

    @Override
    public Holder<CreativeModeTab> registerCreativeTab(ResourceLocation name, Supplier<CreativeModeTab> creativeModeTabSupplier) {
        CreativeModeTab creativeModeTab = creativeModeTabSupplier.get();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, name, creativeModeTab);
        return Holder.direct(creativeModeTab);
    }

    @Override
    public Holder<CreativeModeTab> registerCreativeTab(String name, Supplier<CreativeModeTab> creativeModeTabSupplier) {
        return registerCreativeTab(ResourceLocation.fromNamespaceAndPath(modId, name), creativeModeTabSupplier);
    }

    @Override
    public Registry<AdaptiveOreSettings> getAdaptiveOreSettingsRegistry() {
        return FabricRegistryBuilder.createSimple(AdaptiveOreSettingsRegistry.REGISTRY_KEY).buildAndRegister();
    }

    @Override
    public void registerAdaptiveOreSettingsRegistry() {
        // Already registered in getAdaptiveOreSettingsRegistry
    }
}

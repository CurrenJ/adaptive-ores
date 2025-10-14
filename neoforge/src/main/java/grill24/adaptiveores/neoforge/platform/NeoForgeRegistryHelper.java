package grill24.adaptiveores.neoforge.platform;

import grill24.adaptiveores.data.AdaptiveOreSettings;
import grill24.adaptiveores.data.AdaptiveOreSettingsRegistry;
import grill24.adaptiveores.platform.IRegistryHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NeoForgeRegistryHelper implements IRegistryHelper {
    private final DeferredRegister<Block> blockRegister;
    private final DeferredRegister<Item> itemRegister;
    private final DeferredRegister<BlockEntityType<?>> blockEntityRegister;
    private final DeferredRegister<CreativeModeTab> creativeTabRegister;

    public NeoForgeRegistryHelper(String modId) {
        this.blockRegister = DeferredRegister.create(Registries.BLOCK, modId);
        this.itemRegister = DeferredRegister.create(Registries.ITEM, modId);
        this.blockEntityRegister = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, modId);
        this.creativeTabRegister = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, modId);
    }

    public void registerToEventBus(net.neoforged.bus.api.IEventBus eventBus) {
        blockRegister.register(eventBus);
        itemRegister.register(eventBus);
        blockEntityRegister.register(eventBus);
        creativeTabRegister.register(eventBus);
    }

    @Override
    public Holder<Block> registerBlock(ResourceLocation name, Supplier<Block> blockSupplier) {
        return registerBlock(name.getPath(), blockSupplier);
    }

    @Override
    public Holder<Block> registerBlock(String name, Supplier<Block> blockSupplier) {
        return blockRegister.register(name, blockSupplier);
    }

    @Override
    public Holder<Item> registerItem(ResourceLocation name, Supplier<Item> itemSupplier) {
        return registerItem(name.getPath(), itemSupplier);
    }

    @Override
    public Holder<Item> registerItem(String name, Supplier<Item> itemSupplier) {
        return itemRegister.register(name, itemSupplier);
    }

    @Override
    public Holder<BlockEntityType<?>> registerBlockEntity(ResourceLocation name, Supplier<BlockEntityType<?>> blockEntitySupplier) {
        return registerBlockEntity(name.getPath(), blockEntitySupplier);
    }

    @Override
    public Holder<BlockEntityType<?>> registerBlockEntity(String name, Supplier<BlockEntityType<?>> blockEntitySupplier) {
        return blockEntityRegister.register(name, blockEntitySupplier);
    }

    @Override
    public Holder<CreativeModeTab> registerCreativeTab(ResourceLocation name, Supplier<CreativeModeTab> creativeModeTabSupplier) {
        return registerCreativeTab(name.getPath(), creativeModeTabSupplier);
    }

    @Override
    public Holder<CreativeModeTab> registerCreativeTab(String name, Supplier<CreativeModeTab> creativeModeTabSupplier) {
        return creativeTabRegister.register(name, creativeModeTabSupplier);
    }

    @Override
    public Registry<AdaptiveOreSettings> getAdaptiveOreSettingsRegistry() {
        return AdaptiveOreSettingsRegistry.REGISTRY;
    }

    @Override
    public void registerAdaptiveOreSettingsRegistry() {
        // Already registered in getAdaptiveOreSettingsRegistry
    }
}

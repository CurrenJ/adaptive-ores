package grill24.adaptiveores.platform;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public interface IRegistryHelper {
    Holder<Block> registerBlock(ResourceLocation name, Supplier<Block> blockSupplier);
    Holder<Block> registerBlock(String name, Supplier<Block> blockSupplier);
    Holder<Item> registerItem(ResourceLocation name, Supplier<Item> itemSupplier);
    Holder<Item> registerItem(String name, Supplier<Item> itemSupplier);
    Holder<BlockEntityType<?>> registerBlockEntity(ResourceLocation name, Supplier<BlockEntityType<?>> blockEntitySupplier);
    Holder<BlockEntityType<?>> registerBlockEntity(String name, Supplier<BlockEntityType<?>> blockEntitySupplier);
    Holder<CreativeModeTab> registerCreativeTab(ResourceLocation name, Supplier<CreativeModeTab> creativeModeTabSupplier);
    Holder<CreativeModeTab> registerCreativeTab(String name, Supplier<CreativeModeTab> creativeModeTabSupplier);
}

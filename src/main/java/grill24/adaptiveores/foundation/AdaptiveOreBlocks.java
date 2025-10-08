package grill24.adaptiveores.foundation;

import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.content.block.AdaptiveOreBlock;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType; 

@EventBusSubscriber(modid = AdaptiveOres.MODID, bus = EventBusSubscriber.Bus.MOD)
public class AdaptiveOreBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AdaptiveOres.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AdaptiveOres.MODID);

    // Base properties for adaptive ore blocks
    private static BlockBehaviour.Properties baseOreProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .requiresCorrectToolForDrops()
                .strength(3.0F, 3.0F)
                .sound(SoundType.STONE);
    }
    
    // Adaptive ore blocks
    public static final DeferredBlock<AdaptiveOreBlock> ADAPTIVE_COAL_ORE = BLOCKS.register("adaptive_coal_ore", 
        () -> new AdaptiveOreBlock(baseOreProperties(), OreType.COAL));
    
    public static final DeferredBlock<AdaptiveOreBlock> ADAPTIVE_IRON_ORE = BLOCKS.register("adaptive_iron_ore", 
        () -> new AdaptiveOreBlock(baseOreProperties().strength(3.0F, 3.0F), OreType.IRON));
    
    public static final DeferredBlock<AdaptiveOreBlock> ADAPTIVE_COPPER_ORE = BLOCKS.register("adaptive_copper_ore", 
        () -> new AdaptiveOreBlock(baseOreProperties().strength(3.0F, 3.0F), OreType.COPPER));
    
    public static final DeferredBlock<AdaptiveOreBlock> ADAPTIVE_GOLD_ORE = BLOCKS.register("adaptive_gold_ore", 
        () -> new AdaptiveOreBlock(baseOreProperties().strength(3.0F, 3.0F), OreType.GOLD));
    
    public static final DeferredBlock<AdaptiveOreBlock> ADAPTIVE_REDSTONE_ORE = BLOCKS.register("adaptive_redstone_ore", 
        () -> new AdaptiveOreBlock(baseOreProperties().strength(3.0F, 3.0F), OreType.REDSTONE));
    
    public static final DeferredBlock<AdaptiveOreBlock> ADAPTIVE_LAPIS_ORE = BLOCKS.register("adaptive_lapis_ore", 
        () -> new AdaptiveOreBlock(baseOreProperties().strength(3.0F, 3.0F), OreType.LAPIS));
    
    public static final DeferredBlock<AdaptiveOreBlock> ADAPTIVE_DIAMOND_ORE = BLOCKS.register("adaptive_diamond_ore", 
        () -> new AdaptiveOreBlock(baseOreProperties().strength(3.0F, 3.0F), OreType.DIAMOND));
    
    public static final DeferredBlock<AdaptiveOreBlock> ADAPTIVE_EMERALD_ORE = BLOCKS.register("adaptive_emerald_ore", 
        () -> new AdaptiveOreBlock(baseOreProperties().strength(3.0F, 3.0F), OreType.EMERALD));

    // Block items
    public static final DeferredItem<BlockItem> ADAPTIVE_COAL_ORE_ITEM = ITEMS.registerSimpleBlockItem(ADAPTIVE_COAL_ORE);
    public static final DeferredItem<BlockItem> ADAPTIVE_IRON_ORE_ITEM = ITEMS.registerSimpleBlockItem(ADAPTIVE_IRON_ORE);
    public static final DeferredItem<BlockItem> ADAPTIVE_COPPER_ORE_ITEM = ITEMS.registerSimpleBlockItem(ADAPTIVE_COPPER_ORE);
    public static final DeferredItem<BlockItem> ADAPTIVE_GOLD_ORE_ITEM = ITEMS.registerSimpleBlockItem(ADAPTIVE_GOLD_ORE);
    public static final DeferredItem<BlockItem> ADAPTIVE_REDSTONE_ORE_ITEM = ITEMS.registerSimpleBlockItem(ADAPTIVE_REDSTONE_ORE);
    public static final DeferredItem<BlockItem> ADAPTIVE_LAPIS_ORE_ITEM = ITEMS.registerSimpleBlockItem(ADAPTIVE_LAPIS_ORE);
    public static final DeferredItem<BlockItem> ADAPTIVE_DIAMOND_ORE_ITEM = ITEMS.registerSimpleBlockItem(ADAPTIVE_DIAMOND_ORE);
    public static final DeferredItem<BlockItem> ADAPTIVE_EMERALD_ORE_ITEM = ITEMS.registerSimpleBlockItem(ADAPTIVE_EMERALD_ORE);

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(ADAPTIVE_COAL_ORE.value(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ADAPTIVE_IRON_ORE.value(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ADAPTIVE_COPPER_ORE.value(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ADAPTIVE_GOLD_ORE.value(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ADAPTIVE_REDSTONE_ORE.value(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ADAPTIVE_LAPIS_ORE.value(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ADAPTIVE_DIAMOND_ORE.value(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ADAPTIVE_EMERALD_ORE.value(), RenderType.translucent());
    }
}
package com.syntheticdev.torchmaster_extras.common;

import com.syntheticdev.torchmaster_extras.TorchMasterExtras;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.common.items.TMItemBlock;

public final class ModItems {

    public static final DeferredRegister<Item> DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, TorchMasterExtras.MOD_ID);

    public static final RegistryObject<TMItemBlock> COBBLEMON_TORCH = registerBlock(ModBlocks.COBBLEMON_TORCH, new Item.Properties());

    private static <B extends Block> RegistryObject<TMItemBlock> registerBlock(RegistryObject<B> block, Item.Properties properties) {
        return DEFERRED_REGISTER.register(block.getId().getPath(), () -> new TMItemBlock(block.get(), properties));
    }

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == Torchmaster.CreativeTab.getKey()) {
            event.accept(COBBLEMON_TORCH);
        }
    }

}

package com.syntheticdev.torchmaster_extras.datagen;

import com.syntheticdev.torchmaster_extras.TorchMasterExtras;
import com.syntheticdev.torchmaster_extras.common.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, TorchMasterExtras.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.COBBLEMON_TORCH);
    }

    private void blockWithItem(RegistryObject<? extends Block> blockRegistryObject) {
        ModelFile cobblemonTorchModel = this.models().getExistingFile(blockRegistryObject.getId());
        simpleBlockWithItem(blockRegistryObject.get(), cobblemonTorchModel);
    }
}

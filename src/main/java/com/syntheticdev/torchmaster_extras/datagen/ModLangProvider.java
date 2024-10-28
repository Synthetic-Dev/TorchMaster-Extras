package com.syntheticdev.torchmaster_extras.datagen;

import com.syntheticdev.torchmaster_extras.TorchMasterExtras;
import com.syntheticdev.torchmaster_extras.common.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLangProvider extends LanguageProvider {

    public ModLangProvider(PackOutput output, String locale) {
        super(output, TorchMasterExtras.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        this.addBlock(ModBlocks.COBBLEMON_TORCH, "Cobblemon Torch");
        this.add(ModBlocks.COBBLEMON_TORCH.get().getDescriptionId() + ".tooltip", "Prevents natural spawning of pokemon in a large radius around the torch");
    }
}

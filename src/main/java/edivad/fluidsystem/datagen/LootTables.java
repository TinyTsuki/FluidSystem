package edivad.fluidsystem.datagen;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import edivad.fluidsystem.setup.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.registries.DeferredHolder;

public class LootTables extends BlockLootSubProvider {

  public LootTables(HolderLookup.Provider registries) {
    super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
  }

  public static LootTableProvider create(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> provider) {
    return new LootTableProvider(packOutput, Set.of(), List.of(
        new LootTableProvider.SubProviderEntry(LootTables::new, LootContextParamSets.BLOCK)
    ), provider);
  }

  @Override
  protected void generate() {
    this.dropSelf(Registration.STRUCTURAL_TANK_BLOCK.get());
    this.dropSelf(Registration.CONTROLLER_TANK_BLOCK.get());
    this.dropSelf(Registration.INTERFACE_TANK_BLOCK.get());
    this.dropSelf(Registration.INPUT_TANK_BLOCK.get());
    this.dropSelf(Registration.INFINITE_WATER_SOURCE.get());
    this.dropSelf(Registration.PIPE.get());
    this.dropSelf(Registration.INPUT_PIPE.get());
    this.dropSelf(Registration.OUTPUT_PIPE.get());
    this.dropSelf(Registration.PIPE_CONTROLLER.get());
  }

  @Override
  protected Iterable<Block> getKnownBlocks() {
    return Registration.getBlockEntries().stream().map(DeferredHolder::get).map(Block.class::cast).toList();
  }
}

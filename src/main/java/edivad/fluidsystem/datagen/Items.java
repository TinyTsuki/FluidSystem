package edivad.fluidsystem.datagen;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.blocks.pipe.PipeBlock;
import edivad.fluidsystem.setup.Registration;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class Items extends ItemModelProvider {

    public Items(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, Main.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //Tank
        parentedBlock(Registration.STRUCTURAL_TANK_BLOCK.get());
        parentedBlock(Registration.CONTROLLER_TANK_BLOCK.get());
        parentedBlock(Registration.INTERFACE_TANK_BLOCK.get());
        parentedBlock(Registration.INPUT_TANK_BLOCK.get());

        //Pipes
        parentedPipe(Registration.PIPE.get());

        //Other blocks
        parentedBlockActivable(Registration.INPUT_PIPE.get(), false);
        parentedBlock(Registration.OUTPUT_PIPE.get());
        parentedBlock(Registration.INFINITE_WATER_SOURCE.get());
        parentedBlockActivable(Registration.PIPE_CONTROLLER.get(), false);
    }

    private void parentedBlock(Block block) {
        String name = ForgeRegistries.BLOCKS.getKey(block).getPath();
        getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + name)));
    }

    private void parentedBlockActivable(Block block, boolean state) {
        String name = ForgeRegistries.BLOCKS.getKey(block).getPath();
        getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + name + (state ? "_on" : "_off"))));
    }

    private void parentedPipe(PipeBlock pipe) {
        String name = ForgeRegistries.BLOCKS.getKey(pipe).getPath();
        getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + name + "_center")));
    }
}

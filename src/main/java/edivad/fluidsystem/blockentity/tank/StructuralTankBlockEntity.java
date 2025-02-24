package edivad.fluidsystem.blockentity.tank;

import edivad.fluidsystem.setup.Registration;
import edivad.fluidsystem.tools.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class StructuralTankBlockEntity extends BaseTankBlockEntity {

    public StructuralTankBlockEntity(BlockPos pos, BlockState state) {
        super(Registration.STRUCTURAL_TANK_BLOCK_TILE.get(), pos, state);
    }

    @Override
    public boolean isMaster() {
        return false;
    }

    @Override
    public int blockCapacity() {
        return Config.BLOCK_CAPACITY.get();
    }
}

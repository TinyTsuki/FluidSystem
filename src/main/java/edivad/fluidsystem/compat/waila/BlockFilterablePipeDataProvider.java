package edivad.fluidsystem.compat.waila;

import edivad.fluidsystem.blockentity.pipe.FilterablePipeBlockEntity;
import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class BlockFilterablePipeDataProvider implements IServerDataProvider<BlockEntity> {

    @Override
    public void appendServerData(CompoundTag data, ServerPlayer player, Level level, BlockEntity tileEntity, boolean showDetails) {
        if(tileEntity instanceof FilterablePipeBlockEntity output) {
            Fluid fluid = output.getFluidFilter();
            if(!fluid.isSame(Fluids.EMPTY)) {
                data.putBoolean("isFluidPresent", true);
                data.putString("fluid", fluid.getAttributes().getDisplayName(null).getString());
            }
            else
                data.putBoolean("isFluidPresent", false);
        }
    }
}

package edivad.fluidsystem.network.packet;

import edivad.fluidsystem.FluidSystem;
import edivad.fluidsystem.blockentity.pipe.FilterablePipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpdateFilterablePipeBlock(
    BlockPos pos, FluidStack fluidStack) implements CustomPacketPayload {

  public static final Type<UpdateFilterablePipeBlock> TYPE =
      new Type<>(FluidSystem.rl("update_filterable_pipe_block"));


  public static final StreamCodec<RegistryFriendlyByteBuf, UpdateFilterablePipeBlock> STREAM_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC, UpdateFilterablePipeBlock::pos,
          FluidStack.OPTIONAL_STREAM_CODEC, UpdateFilterablePipeBlock::fluidStack,
          UpdateFilterablePipeBlock::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(UpdateFilterablePipeBlock message, IPayloadContext ctx) {
    var level = ctx.player().level();
    if (level.isLoaded(message.pos)) {
      var be = level.getBlockEntity(message.pos);
      if (be instanceof FilterablePipeBlockEntity output) {
        output.setFilteredFluid(message.fluidStack.getFluid());
      }
    }
  }
}

package edivad.fluidsystem.network.packet;

import edivad.fluidsystem.FluidSystem;
import edivad.fluidsystem.blockentity.tank.ControllerTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpdateControllerTankBlock(
    BlockPos pos, FluidStack fluidStack,
    int tanksBlock, int totalCapacity) implements CustomPacketPayload {

  public static final Type<UpdateControllerTankBlock> TYPE =
      new Type<>(FluidSystem.rl("update_controller_tank_block"));

  public static final StreamCodec<RegistryFriendlyByteBuf, UpdateControllerTankBlock> STREAM_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC, UpdateControllerTankBlock::pos,
          FluidStack.OPTIONAL_STREAM_CODEC, UpdateControllerTankBlock::fluidStack,
          ByteBufCodecs.VAR_INT, UpdateControllerTankBlock::tanksBlock,
          ByteBufCodecs.VAR_INT, UpdateControllerTankBlock::totalCapacity,
          UpdateControllerTankBlock::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(UpdateControllerTankBlock message, IPayloadContext ctx) {
    var level = ctx.player().level();
    if (level.isLoaded(message.pos)) {
      var be = level.getBlockEntity(message.pos);
      if (be instanceof ControllerTankBlockEntity controller) {
        controller.clientFluidStack = message.fluidStack;
        controller.tanksBlock = message.tanksBlock;
        controller.totalCapacity = message.totalCapacity;
      }
    }
  }
}

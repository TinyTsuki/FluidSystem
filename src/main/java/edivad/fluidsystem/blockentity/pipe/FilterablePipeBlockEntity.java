package edivad.fluidsystem.blockentity.pipe;

import edivad.fluidsystem.api.IFluidSystemFilterable;
import edivad.fluidsystem.network.packet.UpdateFilterablePipeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.PacketDistributor;

public class FilterablePipeBlockEntity extends BlockEntity implements IFluidSystemFilterable {

  private FluidStack fluidFilter = FluidStack.EMPTY;

  public FilterablePipeBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos,
      BlockState state) {
    super(blockEntityType, pos, state);
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    tag.put("fluid", FluidStack.OPTIONAL_CODEC
        .encode(this.fluidFilter, NbtOps.INSTANCE, new CompoundTag())
        .getOrThrow());
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    fluidFilter = FluidStack.OPTIONAL_CODEC.parse(NbtOps.INSTANCE, tag.get("fluid")).getOrThrow();
  }

  //Synchronizing on block update
  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    var tag = new CompoundTag();
    tag.put("fluid", FluidStack.OPTIONAL_CODEC
        .encode(this.fluidFilter, NbtOps.INSTANCE, new CompoundTag())
        .getOrThrow());
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt,
      HolderLookup.Provider lookupProvider) {
    var tag = pkt.getTag();
    fluidFilter = FluidStack.OPTIONAL_CODEC.parse(NbtOps.INSTANCE, tag.get("fluid")).getOrThrow();
  }

  //Synchronizing on chunk load
  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    var tag = super.getUpdateTag(registries);
    tag.put("fluid", FluidStack.OPTIONAL_CODEC
        .encode(this.fluidFilter, NbtOps.INSTANCE, new CompoundTag())
        .getOrThrow());
    return tag;
  }

  @Override
  public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
    super.handleUpdateTag(tag, lookupProvider);
    fluidFilter = FluidStack.OPTIONAL_CODEC.parse(NbtOps.INSTANCE, tag.get("fluid")).getOrThrow();
  }

  @Override
  public void setFilteredFluid(Fluid fluid) {
    if (fluid.isSame(Fluids.EMPTY)) {
      fluidFilter = FluidStack.EMPTY;
    } else {
      fluidFilter = new FluidStack(fluid, 1000);
    }
    setChanged();
    if (!level.isClientSide) {
      PacketDistributor.sendToAllPlayers(new UpdateFilterablePipeBlock(getBlockPos(), fluidFilter));
    }
  }

  @Override
  public Fluid getFluidFilter() {
    return fluidFilter.getFluid();
  }
}

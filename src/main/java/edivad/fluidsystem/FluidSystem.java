package edivad.fluidsystem;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import edivad.edivadlib.setup.UpdateChecker;
import edivad.fluidsystem.blockentity.InfinityWaterSourceBlockEntity;
import edivad.fluidsystem.blockentity.tank.ControllerTankBlockEntity;
import edivad.fluidsystem.blockentity.tank.InterfaceTankBlockEntity;
import edivad.fluidsystem.client.screen.ScreenModularTank;
import edivad.fluidsystem.compat.top.TOPProvider;
import edivad.fluidsystem.datagen.BlockStates;
import edivad.fluidsystem.datagen.Items;
import edivad.fluidsystem.datagen.Lang;
import edivad.fluidsystem.datagen.LootTables;
import edivad.fluidsystem.datagen.Recipes;
import edivad.fluidsystem.datagen.TagsProvider;
import edivad.fluidsystem.network.packet.UpdateControllerTankBlock;
import edivad.fluidsystem.network.packet.UpdateFilterablePipeBlock;
import edivad.fluidsystem.setup.ClientSetup;
import edivad.fluidsystem.setup.Config;
import edivad.fluidsystem.setup.FluidsystemCreativeModeTabs;
import edivad.fluidsystem.setup.Registration;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(FluidSystem.ID)
public class FluidSystem {

  public static final String ID = "fluidsystem";
  public static final String NAME = "FluidSystem";
  public static final Logger LOGGER = LogUtils.getLogger();

  public FluidSystem(ModContainer modContainer, Dist dist) {
    var modEventBus = modContainer.getEventBus();
    Registration.init(modEventBus);
    modEventBus.addListener(this::handleCommonSetup);
    modEventBus.addListener(this::handleClientSetup);
    modEventBus.addListener(this::handleRegisterMenuScreens);
    modEventBus.addListener(this::handleGatherData);
    modEventBus.addListener(this::handleRegisterCapabilities);
    modEventBus.addListener(this::registerPayloads);
    FluidsystemCreativeModeTabs.register(modEventBus);
    Config.registerConfig(modContainer);

    if (dist.isClient()) {
      ClientSetup.init(modEventBus);
    }
  }

  private void handleCommonSetup(FMLCommonSetupEvent event) {
    // Register TheOneProbe
    if (ModList.get().isLoaded("theoneprobe")) {
      InterModComms.sendTo("theoneprobe", "getTheOneProbe", TOPProvider::new);
    }
  }

  private void handleClientSetup(FMLClientSetupEvent event) {
    NeoForge.EVENT_BUS.register(new UpdateChecker(ID));
  }

  private void handleRegisterMenuScreens(RegisterMenuScreensEvent event) {
    event.register(Registration.CONTROLLER_TANK_BLOCK_MENU.get(), ScreenModularTank::new);
  }

  private void handleGatherData(GatherDataEvent event) {
    var generator = event.getGenerator();
    var packOutput = generator.getPackOutput();
    var lookupProvider = event.getLookupProvider();
    var fileHelper = event.getExistingFileHelper();

    generator.addProvider(event.includeServer(), new Recipes(packOutput, lookupProvider));
    generator.addProvider(event.includeServer(), LootTables.create(packOutput, lookupProvider));
    generator.addProvider(event.includeServer(),
        new TagsProvider(packOutput, lookupProvider, fileHelper));
    generator.addProvider(event.includeServer(), new Lang(packOutput));
    generator.addProvider(event.includeClient(), new BlockStates(packOutput, fileHelper));
    generator.addProvider(event.includeClient(),
        new Items(packOutput, event.getExistingFileHelper()));
  }

  private void handleRegisterCapabilities(RegisterCapabilitiesEvent event) {
    event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
        Registration.INFINITE_WATER_SOURCE_BLOCK_ENTITY.get(),
        InfinityWaterSourceBlockEntity::getFluidCap);
    event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
        Registration.INTERFACE_TANK_BLOCK_ENTITY.get(),
        InterfaceTankBlockEntity::getFluidCap);
    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
        Registration.CONTROLLER_TANK_BLOCK_ENTITY.get(),
        ControllerTankBlockEntity::getItemCap);
  }

  private void registerPayloads(RegisterPayloadHandlersEvent event) {
    var registrar = event.registrar(ID).versioned("1");
    registrar.playToClient(UpdateControllerTankBlock.TYPE, UpdateControllerTankBlock.STREAM_CODEC, UpdateControllerTankBlock::handle);
    registrar.playToClient(UpdateFilterablePipeBlock.TYPE, UpdateFilterablePipeBlock.STREAM_CODEC, UpdateFilterablePipeBlock::handle);
  }

  public static ResourceLocation rl(String path) {
    return ResourceLocation.fromNamespaceAndPath(ID, path);
  }
}

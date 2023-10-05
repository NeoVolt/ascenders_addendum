package neovolt.ascenders_addendum;

import com.mojang.logging.LogUtils;
import neovolt.ascenders_addendum.content.item.equipment.tools.RuneBatteryItem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.common.registration.item.AoAItems;
import org.slf4j.Logger;

import java.util.ArrayList;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AscendersAddendum.MODID)
public class AscendersAddendum
{
    public static final String MODID = "ascenders_addendum";

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    //public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
    //public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));


    //single-rune battery
    public static final RegistryObject<Item> WIND_RUNE_BATTERY = ITEMS.register("wind_rune_battery", () ->
            new RuneBatteryItem(new Item.Properties(), new RuneBatteryItem.RuneSlot(AoAItems.WIND_RUNE, 256)));
    public static final RegistryObject<Item> FIRE_RUNE_BATTERY = ITEMS.register("fire_rune_battery", () ->
            new RuneBatteryItem(new Item.Properties(), new RuneBatteryItem.RuneSlot(AoAItems.FIRE_RUNE, 256)));
    public static final RegistryObject<Item> WATER_RUNE_BATTERY = ITEMS.register("water_rune_battery", () ->
            new RuneBatteryItem(new Item.Properties(), new RuneBatteryItem.RuneSlot(AoAItems.WATER_RUNE, 256)));
    public static final RegistryObject<Item> WITHER_RUNE_BATTERY = ITEMS.register("wither_rune_battery", () ->
            new RuneBatteryItem(new Item.Properties(), new RuneBatteryItem.RuneSlot(AoAItems.WITHER_RUNE, 256)));
    public static final RegistryObject<Item> ENERGY_RUNE_BATTERY = ITEMS.register("energy_rune_battery", () ->
            new RuneBatteryItem(new Item.Properties(), new RuneBatteryItem.RuneSlot(AoAItems.ENERGY_RUNE, 256)));
    public static final RegistryObject<Item> LUNAR_RUNE_BATTERY = ITEMS.register("lunar_rune_battery", () ->
            new RuneBatteryItem(new Item.Properties(), new RuneBatteryItem.RuneSlot(AoAItems.LUNAR_RUNE, 256)));
    public static final RegistryObject<Item> KINETIC_RUNE_BATTERY = ITEMS.register("kinetic_rune_battery", () ->
            new RuneBatteryItem(new Item.Properties(), new RuneBatteryItem.RuneSlot(AoAItems.KINETIC_RUNE, 256)));
    public static final RegistryObject<Item> DISTORTION_RUNE_BATTERY = ITEMS.register("distortion_rune_battery", () ->
            new RuneBatteryItem(new Item.Properties(), new RuneBatteryItem.RuneSlot(AoAItems.DISTORTION_RUNE, 256)));
    public static final RegistryObject<Item> POISON_RUNE_BATTERY = ITEMS.register("poison_rune_battery", () ->
            new RuneBatteryItem(new Item.Properties(), new RuneBatteryItem.RuneSlot(AoAItems.POISON_RUNE, 256)));
    public static final RegistryObject<Item> STRIKE_RUNE_BATTERY = ITEMS.register("strike_rune_battery", () ->
            new RuneBatteryItem(new Item.Properties(), new RuneBatteryItem.RuneSlot(AoAItems.STRIKE_RUNE, 256)));
    public static final RegistryObject<Item> POWER_RUNE_BATTERY = ITEMS.register("power_rune_battery", () ->
            new RuneBatteryItem(new Item.Properties(), new RuneBatteryItem.RuneSlot(AoAItems.POWER_RUNE, 256)));
    public static final RegistryObject<Item> STORM_RUNE_BATTERY = ITEMS.register("storm_rune_battery", () ->
            new RuneBatteryItem(new Item.Properties(), new RuneBatteryItem.RuneSlot(AoAItems.STORM_RUNE, 256)));
    public static final RegistryObject<Item> LIFE_RUNE_BATTERY = ITEMS.register("life_rune_battery", () ->
            new RuneBatteryItem(new Item.Properties(), new RuneBatteryItem.RuneSlot(AoAItems.LIFE_RUNE, 256)));
    public static final RegistryObject<Item> COMPASS_RUNE_BATTERY = ITEMS.register("compass_rune_battery", () ->
            new RuneBatteryItem(new Item.Properties(), new RuneBatteryItem.RuneSlot(AoAItems.COMPASS_RUNE, 256)));

    //combo-rune battery
    public static final RegistryObject<Item> NETHENGEIC_BATTERY = ITEMS.register("nethengeic_battery", () ->
            new RuneBatteryItem(new Item.Properties(), new ArrayList<RuneBatteryItem.RuneSlot>() {{
                add(new RuneBatteryItem.RuneSlot(AoAItems.FIRE_RUNE, 128, 1));
                add(new RuneBatteryItem.RuneSlot(AoAItems.WITHER_RUNE, 128, 1));
            }}));


    public static final RegistryObject<CreativeModeTab> AMMO = CREATIVE_MODE_TABS.register("ammo", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT) //sets the tab to be directly after, todo: preferably set this to be after the AoA tabs for release
            .icon(() -> WIND_RUNE_BATTERY.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(WIND_RUNE_BATTERY.get());
                output.accept(FIRE_RUNE_BATTERY.get());
                output.accept(WATER_RUNE_BATTERY.get());
                output.accept(WITHER_RUNE_BATTERY.get());
                output.accept(ENERGY_RUNE_BATTERY.get());
                output.accept(LUNAR_RUNE_BATTERY.get());
                output.accept(KINETIC_RUNE_BATTERY.get());
                output.accept(DISTORTION_RUNE_BATTERY.get());
                output.accept(POISON_RUNE_BATTERY.get());
                output.accept(STRIKE_RUNE_BATTERY.get());
                output.accept(POWER_RUNE_BATTERY.get());
                output.accept(STORM_RUNE_BATTERY.get());
                output.accept(LIFE_RUNE_BATTERY.get());
                output.accept(COMPASS_RUNE_BATTERY.get());

                output.accept(NETHENGEIC_BATTERY.get());
            }).build());

    public AscendersAddendum()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        //BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        //no config yet
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        //if (Config.logDirtBlock)
            //LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        //LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        //Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        //this method is just for if I want to add things to an existing tab
        //if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            //event.accept(EXAMPLE_BLOCK_ITEM);
        //}
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            //LOGGER.info("HELLO FROM CLIENT SETUP");
            //LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}

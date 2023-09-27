package neovolt.ascenders_addendum.content.item.equipment.tools;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RuneBatteryItem extends Item {

    //TODO: REMAINING LIST OF METHODS TO MAKE + THEIR FUNCTIONS
    /*
        int getCap(ItemStack battery, int index, boolean includeEnchantments) {
            gets the cap of the specific slot of the battery, with an additional method for enchantment modifications
        }

        boolean isSlotFull(ItemStack battery, int index) {
            checks to see if the specified RuneSlot is full
        }
    */
    //TODO: THINGS TO ADD TO OTHER FUNCTIONS:
    /*
        overrideStackOnOther:
        - add a sound when runes are deposited, and make it play a different sound if the action filled up the battery

        inventoryTick:
        - add a sound when a battery reaches full capacity or is emptied completely
    */

    private ArrayList<RuneSlot> runeSlots = new ArrayList<RuneSlot>();
    private int timeUntilRegen = 2400;

    // single-rune battery constructor
    public RuneBatteryItem(Properties p, RuneSlot slot) {
        super(p.stacksTo(1));
        runeSlots.add(slot);
    }

    // multi-rune battery constructor
    public RuneBatteryItem(Properties p, ArrayList<RuneSlot> slots) {
        super(p.stacksTo(1));
        runeSlots = slots;
    }

    // manages setting the description of the item and all that
    @Override
    public void appendHoverText(ItemStack battery, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if(!battery.getOrCreateTag().contains("RuneSlots")) {
            createRuneSlots(battery);
        }
        fixBrokenNBT(battery);
        ListTag listTag = battery.getOrCreateTag().getList("RuneSlots", 10);
        for (int i = 0; i < listTag.size(); i++) {
            //long ass inefficient ass line of code right here
            //all in a day's work around these parts
            tooltip.add(Component.literal(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(listTag.getCompound(i).get("id").getAsString()))).getDisplayName().getString()
                    + ": " + listTag.getCompound(i).get("charge").getAsString() + "/" + listTag.getCompound(i).get("cap").getAsString()));
        }
    }

    // handles adding the runes to the battery
    @Override
    public boolean overrideStackedOnOther(ItemStack battery, Slot slot, ClickAction click, Player p) {
        if(battery.getCount() != 1) {
            return false;
        } else if(!battery.getOrCreateTag().contains("RuneSlots")) {
            createRuneSlots(battery);
        } else {
            fixBrokenNBT(battery);
            ItemStack usedOn = slot.getItem();
            if(usedOn.isEmpty()) {
                return false;
            } else {
                for (int i = 0; i < battery.getTag().getList("RuneSlots",10).size(); i++) {
                    String runeSlotString = battery.getTag().getList("RuneSlots",10).getCompound(i).get("id").getAsString();
                    ResourceLocation usedOnString = ForgeRegistries.ITEMS.getKey(usedOn.getItem());

                    if (runeSlotString.equals(usedOnString.getNamespace()+":"+usedOnString.getPath())) {
                        return addChargeFromItem(battery, usedOn, click, p, i);
                    }
                }
            }
        }
        return false;
    }

    //handles the natural regen of rune energy and stuff
    @Override
    public void inventoryTick(ItemStack battery, Level level, Entity holder, int p_41407_, boolean p_41408_) {
        if(this.timeUntilRegen <= 0) {
            this.timeUntilRegen = 2400;
            for(int i = 0; i < battery.getTag().getList("RuneSlots", 10).size(); i++) {
                fixBrokenNBT(battery);
                CompoundTag runeSlot = battery.getTag().getList("RuneSlots", 10).getCompound(i);

                if(runeSlot.getInt("charge") >= runeSlot.getInt("cap"))
                    return;

                addCharge(battery, i, runeSlot.getInt("baseRegen"));
            }
        } else {
            this.timeUntilRegen -= 1;
        }
    }

    // handles the addition of rune charges and the depletion of the stack the item was used on
    private boolean addChargeFromItem(ItemStack battery, ItemStack usedOn, ClickAction click, Player p, int index) {
        CompoundTag runeSlot = battery.getTag().getList("RuneSlots", 10).getCompound(index);
        // left click
        if(click == ClickAction.PRIMARY) {
            if(runeSlot.getInt("charge") != runeSlot.getInt("cap")) {
                addCharge(battery, index, 1);
                usedOn.shrink(1);
                return true;
            }
        } else
        // right click
        if(runeSlot.getInt("charge") + usedOn.getCount() <= runeSlot.getInt("cap")) {
            addCharge(battery, index, usedOn.getCount());
            usedOn.shrink(usedOn.getCount());
            return true;
        } else {
            int temp = runeSlot.getInt("cap") - runeSlot.getInt("charge");
            addCharge(battery, index, temp);
            usedOn.shrink(temp);
            return true;
        }
        return false;
    }

    //adds the specified amount of charge to the rune battery
    public static void addCharge(ItemStack battery, int index, int amount) {
        CompoundTag runeSlot = battery.getTag().getList("RuneSlots", 10).getCompound(index);
        runeSlot.putInt("charge", Math.max(0, Math.min(runeSlot.getInt("charge") + amount, runeSlot.getInt("cap"))));
    }

    // creates the tags for the item, only called if there's currently no RuneSlot tags
    public void createRuneSlots(ItemStack battery) {
        CompoundTag compoundTag = battery.getOrCreateTag();

        ListTag listTag = compoundTag.getList("RuneSlots", 10); //todo: what is the 10 for??
        for(int i = 0; i < runeSlots.size(); i++) {
            listTag.add(runeSlots.get(i).toCompoundTag());
        }

        compoundTag.put("RuneSlots", listTag);
        battery.setTag(compoundTag);
    }

    //makes sure everything regarding the NBT data is in order, because crashing the game is bad
    public void fixBrokenNBT(ItemStack battery) {
        ListTag listTag = battery.getOrCreateTag().getList("RuneSlots", 10);
        for(int i = 0; i < listTag.size(); i++) {
            CompoundTag runeSlot = listTag.getCompound(i);
            //for each slot, check for the presence of each parameter, and if not present or invalid, fixNBT will be called with the tag to fix and its index
            if(!runeSlot.contains("id"))
                runeSlot.putString("id","minecraft:air");

            if(!runeSlot.contains("charge"))
                runeSlot.putInt("charge", 0);

            if(!runeSlot.contains("cap") || runeSlot.getInt("cap") < 1)
                runeSlot.putInt("cap", 1);

            if(!runeSlot.contains("baseRegen"))
                runeSlot.putShort("baseRegen", (short)0);

            if(!runeSlot.contains("regenScale"))
                runeSlot.putShort("regenScale", (short)2);
        }
    }

    // this is a RuneSlot, a class that's used for one specific thing:
    // telling the game which RuneSlot tags to auto-generate if there's none available
    public static class RuneSlot {
        private RegistryObject<Item> item;
        private int charge = 0;
        private int cap = 0;
        private int baseRegen = 0;
        private int regenScale = 2;

        public RuneSlot(RegistryObject<Item> item, int cap) {
            this.item = item;
            this.cap = Math.max(1, cap);
            this.baseRegen = 0;
            this.regenScale = 2;
        }

        public RuneSlot(RegistryObject<Item> item, int cap, int regenScale) {
            this.item = item;
            this.cap = Math.max(1, cap);
            this.baseRegen = 0;
            this.regenScale = regenScale;
        }

        private CompoundTag toCompoundTag() {
            CompoundTag tag = new CompoundTag();

            tag.putString("id",item.getId().toString());
            tag.putInt("charge",charge);
            tag.putInt("cap",cap);
            tag.putShort("baseRegen",(short)baseRegen);
            tag.putShort("regenScale",(short)regenScale);

            return tag;
        }
    }
}

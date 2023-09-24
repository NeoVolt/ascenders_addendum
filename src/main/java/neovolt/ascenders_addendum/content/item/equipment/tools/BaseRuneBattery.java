package neovolt.ascenders_addendum.content.item.equipment.tools;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;

public class BaseRuneBattery extends Item {
    //admittedly some of this code is kinda stolen from the BaseStaff code, don't know how to feel about that

    //maybe a HashMap isn't the right play here? idk, a 2d ArrayList could work better
    public HashMap<Item, Integer> storedRunes = new HashMap<Item, Integer>(3);
    public int maxCapacity = 128;

    //todo: adjust this system so that it can easily make combo batteries and stuff
    public BaseRuneBattery(Properties p, int cap, Item rune) {
        super(p.stacksTo(1));
        maxCapacity = cap;
        storedRunes.put(rune, 0);
    }

    public HashMap<Item, Integer> getRunes() {
        return storedRunes;
    }

    //handles adding the runes to the battery
    @Override
    public boolean overrideStackedOnOther(ItemStack battery, Slot slot, ClickAction click, Player p) {
        if(battery.getCount() != 1) {
            return false;
        } else {
            ItemStack usedOn = slot.getItem();
            if(usedOn.isEmpty()) {
                return false;
            } else {
                //for each rune in the hashmap, check if it's equal to the rune that the usedOn stack is
                if(this.getRunes().containsKey(usedOn)) {
                    addItems(battery, usedOn, click, p);
                }
            }
            return false;
        }
    }

    public void addItems(ItemStack battery, ItemStack usedOn, ClickAction click, Player p) {
        if(click == ClickAction.PRIMARY) {
            if(this.getRunes().get(usedOn) < this.maxCapacity) {
                usedOn.shrink(1);

            }
        } else if(this.getRunes().get(usedOn) + usedOn.getCount() <= this.maxCapacity) {

        } else {

        }
    }

    public void addRunes(Item type, int amount) {
        //shrinks the item
    }

    public static class BatterySlot {
        public Item rune = Items.AIR;
        public int charge = 0;
        public int cap = 0;
        public int generatorAmount = 2;

        public BatterySlot(Item item, int charge, int cap) {
            this.rune = item;
            this.charge = charge;
            this.cap = cap;
        }

        public BatterySlot(Item item, int charge, int cap, int generatorAmount) {
            this.rune = item;
            this.charge = charge;
            this.cap = cap;
            this.generatorAmount = generatorAmount;
        }
    }
}

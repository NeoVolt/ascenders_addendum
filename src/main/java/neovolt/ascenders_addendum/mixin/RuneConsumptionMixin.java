package neovolt.ascenders_addendum.mixin;

import neovolt.ascenders_addendum.AATags;
import neovolt.ascenders_addendum.AscendersAddendum;
import neovolt.ascenders_addendum.content.item.equipment.tools.RuneBatteryItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.tslat.aoa3.common.registration.AoATags;
import net.tslat.aoa3.content.item.armour.AdventArmour;
import net.tslat.aoa3.util.ItemUtil;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.*;

@Mixin(ItemUtil.class)
public class RuneConsumptionMixin {

    // fun fact about this mixin! I found a bug while writing it, was pretty neat to go through line by line and see exactly what went wrong in tslat's code
    // he's damn good at coding, but I suppose we all make little mistakes like that. makes me feel a little better about my code, despite how horrendous it is
    @Inject(remap = false, method = "findAndConsumeRunes", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void findAndConsumeRuneBattery(HashMap<Item, Integer> runeMap, ServerPlayer player, boolean allowBuffs, ItemStack heldItem, CallbackInfoReturnable<Boolean> cir,
                                              AdventArmour.Type armour, int archmage, boolean nightmareArmour, boolean greed,
                                              HashMap requiredRunes, HashSet runeSlots, HashMap runeCounter, ItemStack mainHandStack, ItemStack offHandStack) {
        cir.setReturnValue(false);
        //TODO: when the bug with findAndConsumeRunes is fixed, add in the ability for this to check for mainhand and offhand slots
        ArrayList<int[]> batterySlots = new ArrayList<>();

        //inventory stuff
        if (!runeCounter.isEmpty()) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);

                if (stack.is(AATags.RUNE_BATTERIES)) {
                    ListTag list = stack.getTag().getList("RuneSlots",10);
                    for(int k = 0; k < list.size(); k++) {
                        Item rune = ForgeRegistries.ITEMS.getValue(new ResourceLocation(list.getCompound(k).getString("id")));
                        if (runeCounter.containsKey(rune)) {
                            int amount = (int) runeCounter.get(rune);

                            int a[] = {i, k};
                            batterySlots.add(a);
                            amount -= list.getCompound(k).getInt("charge");

                            if (amount > 0) {
                                runeCounter.put(rune, amount);
                            } else {
                                runeCounter.remove(rune);
                            }

                            if (runeCounter.isEmpty())
                                break;
                        }
                    }
                }
            }
        }

        if(runeCounter.isEmpty()) {
            //default class stuff, maybe should consider having 2 different mixins for better code efficiency...

            //I don't know why I had to cast this to an object and then later back into an integer. I do not understand.
            //it works fine in the code of the base method.
            //but it doesn't work here like that. so I have to do this.
            for (Object slotId : runeSlots) {
                ItemStack rune = player.getInventory().getItem((Integer)slotId);
                Item type = rune.getItem();
                int amount = (int) requiredRunes.get(type);
                int remaining = amount - rune.getCount();

                rune.shrink(amount);

                if (remaining <= 0) {
                    requiredRunes.remove(type);
                }
                else {
                    requiredRunes.put(type, remaining);
                }

                if (requiredRunes.isEmpty())
                    break;
            }

            for(int i = 0; i < batterySlots.size(); i++) {
                ItemStack stack = player.getInventory().getItem(batterySlots.get(i)[0]);
                ListTag list = stack.getTag().getList("RuneSlots",10);
                Item rune = ForgeRegistries.ITEMS.getValue(new ResourceLocation(list.getCompound(batterySlots.get(i)[1]).getString("id")));
                int amount = (int) requiredRunes.get(rune);
                int remaining = amount - list.getCompound(batterySlots.get(i)[1]).getInt("charge");
                System.out.println("amount: " + amount);

                RuneBatteryItem.addCharge(stack, batterySlots.get(i)[1], -amount);

                if (remaining <= 0) {
                    requiredRunes.remove(rune);
                }
                else {
                    requiredRunes.put(rune, remaining);
                }
                //I think these lines are important for something, but iirc it was causing issues. it's a future me problem now
                //if (requiredRunes.isEmpty())
                //    break;
            }
            cir.setReturnValue(true);
        }

    }

}

package nl.sugcube.dirtyarrows.listener;

import nl.sugcube.dirtyarrows.DirtyArrows;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class EntityListener implements Listener {
	
	public static DirtyArrows plugin;

	public EntityListener(DirtyArrows instance) {
		plugin = instance;
	}
	
	Random ran = new Random();
	boolean extra = false;
	Player player;
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		try {
			if (player.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_MOBS) && event.getDrops() != null) {
				ItemStack i = player.getItemInHand();
				if (i.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) == 1) {
					if (ran.nextInt(9) == 0) {
						extra = true;
					}
				} else if (i.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) == 2) {
					if (ran.nextInt(8) == 0) {
						extra = true;
					}
				} else {
					if (ran.nextInt(7) == 0) {
						extra = true;
					}
				}
				
				List<ItemStack> drops = event.getDrops();
				if (ran.nextInt(3) == 0) {
					for (ItemStack drop : drops) {
						int amount;
						if (ran.nextInt(3) != 0) {
							amount = i.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) - 1;
						} else if (ran.nextInt(3) != 0) {
							amount = i.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
						} else {
							amount = i.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) + 1;
						}
						drop.setAmount(drop.getAmount() + amount);
					}
				}
				if (extra) {
					for (ItemStack drop : drops) {
						drop.setAmount(drop.getAmount() + 1);
					}
				}
				if (ran.nextInt(i.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS)) != 0) {
					event.setDroppedExp(event.getDroppedExp() + i.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS));
				}
			}
		} catch (Exception e) { }
	}

}

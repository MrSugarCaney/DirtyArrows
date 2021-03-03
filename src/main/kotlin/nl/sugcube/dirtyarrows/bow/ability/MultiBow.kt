package nl.sugcube.dirtyarrows.bow.ability

import nl.sugcube.dirtyarrows.DirtyArrows
import nl.sugcube.dirtyarrows.bow.BowAbility
import nl.sugcube.dirtyarrows.bow.DefaultBow
import nl.sugcube.dirtyarrows.util.copyOf
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

/**
 * Shoots 8 arrows simultaneously in slightly different directions.
 *
 * @author SugarCaney
 */
open class MultiBow(plugin: DirtyArrows) : BowAbility(
        plugin = plugin,
        type = DefaultBow.MULTI,
        canShootInProtectedRegions = true,
        removeArrow = false,
        costRequirements = listOf(ItemStack(Material.ARROW, 8))
) {

    override fun launch(player: Player, arrow: Arrow, event: ProjectileLaunchEvent) {
        val bowInHand = player.bowItem()

        // Launch 7 extra arrows.
        repeat(7) {
            val multiArrow = player.world.spawn(arrow.location, Arrow::class.java).apply {
                shooter = player
                velocity = arrow.velocity.copyOf(
                        x = arrow.velocity.x + Random.nextDouble() / 2.0 - 0.254,
                        y = arrow.velocity.y + Random.nextDouble() / 2.0 - 0.254,
                        z = arrow.velocity.z + Random.nextDouble() / 2.0 - 0.254
                )

                // When these are inifinity arrows, they cannot be picked up.
                if (bowInHand?.containsEnchantment(Enchantment.ARROW_INFINITE) == false) {
                    pickupStatus = Arrow.PickupStatus.ALLOWED
                }

                // Set punch enchantment.
                knockbackStrength = bowInHand?.getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK) ?: 0

                // Make them critcal if the bow has power.
                // Does not fully account for all levels, but I wouldn't bother too much. This is fine.
                isCritical = bowInHand?.containsEnchantment(Enchantment.ARROW_DAMAGE) == true

                // Set on fire when the bow has flame.
                if (bowInHand?.containsEnchantment(Enchantment.ARROW_FIRE) == true) {
                    fireTicks = 1200
                }
            }
            registerArrow(multiArrow)
        }
    }

    override fun Player.consumeBowItems() {
        if (gameMode == GameMode.CREATIVE) return

        val hasInfinity = bowItem()?.enchantments?.containsKey(Enchantment.ARROW_INFINITE) == true
        if (hasInfinity.not()) {
            // Only subtract 7 instead of 8, because 1 gets fired by default.
            inventory.removeItem(ItemStack(Material.ARROW, 7))
        }
    }
}
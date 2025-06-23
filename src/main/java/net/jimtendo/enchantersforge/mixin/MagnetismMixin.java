package net.jimtendo.enchantersforge.mixin;

import net.jimtendo.enchantersforge.enchantment.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class MagnetismMixin {
   @Inject(method = "tick", at = @At("HEAD"))
   private void applyMagnetism(CallbackInfo ci) {
      PlayerEntity player = (PlayerEntity)(Object)this;

      // Check if player is in creative or spectator mode
      if (player.isSpectator()) return;

      // Get enchantment levels from all armor pieces
      int helmetLevel = EnchantmentHelper.getLevel(ModEnchantments.MAGNETISM, player.getEquippedStack(EquipmentSlot.HEAD));
      int chestLevel = EnchantmentHelper.getLevel(ModEnchantments.MAGNETISM, player.getEquippedStack(EquipmentSlot.CHEST));
      int legsLevel = EnchantmentHelper.getLevel(ModEnchantments.MAGNETISM, player.getEquippedStack(EquipmentSlot.LEGS));
      int bootsLevel = EnchantmentHelper.getLevel(ModEnchantments.MAGNETISM, player.getEquippedStack(EquipmentSlot.FEET));

      // Combine all levels
      int totalLevel = helmetLevel + chestLevel + legsLevel + bootsLevel;

      if (totalLevel > 0) {
         // Calculate radius based on total enchantment level (3 blocks per level)
         double radius = 3.0 * totalLevel;

         // Create a box around the player
         Box box = new Box(
                 player.getX() - radius, player.getY() - radius, player.getZ() - radius,
                 player.getX() + radius, player.getY() + radius, player.getZ() + radius
         );

         // Get all item entities within the box
         List<ItemEntity> items = player.getWorld().getEntitiesByType(
                 EntityType.ITEM,
                 box,
                 item -> !item.hasNoGravity() && item.isAlive() && !item.hasVehicle()
         );

         // Pull each item towards the player
         for (ItemEntity item : items) {
            // Calculate vector from item to player
            Vec3d itemPos = item.getPos();
            Vec3d playerPos = player.getPos().add(0, 0.5, 0); // Adjust height to target player's center
            Vec3d pullVector = playerPos.subtract(itemPos);

            // Calculate distance
            double distance = pullVector.length();

            // Skip if item is too close (prevents jittering)
            if (distance < 1.0) continue;

            // Normalize and scale the pull vector (stronger pull at higher levels)
            double pullStrength = 0.3 + (totalLevel * 0.1); // Base pull + level bonus
            Vec3d normalizedPull = pullVector.normalize().multiply(pullStrength);

            // Apply velocity to the item
            item.setVelocity(normalizedPull);
            item.velocityModified = true;
            item.velocityDirty = true;
         }
      }
   }
}
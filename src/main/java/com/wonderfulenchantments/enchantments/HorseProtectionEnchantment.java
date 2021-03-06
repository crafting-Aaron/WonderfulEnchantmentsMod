package com.wonderfulenchantments.enchantments;

import com.mlib.EquipmentSlotTypes;
import com.mlib.attributes.AttributeHandler;
import com.mlib.config.IntegerConfig;
import com.mlib.enchantments.EnchantmentHelperPlus;
import com.wonderfulenchantments.Instances;
import com.wonderfulenchantments.RegistryHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.HorseArmorItem;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Enchantment that increases horse's armor. */
@Mod.EventBusSubscriber
public class HorseProtectionEnchantment extends WonderfulEnchantment {
	private static final AttributeHandler ATTRIBUTE_HANDLER = new AttributeHandler( "f7f6f46b-23a1-4d3b-8e83-3160c6390f9a", "HorseProtectionBonus",
		Attributes.ARMOR, AttributeModifier.Operation.ADDITION
	);
	protected final IntegerConfig armorBonus;

	public HorseProtectionEnchantment() {
		super( Rarity.UNCOMMON, RegistryHandler.HORSE_ARMOR, EquipmentSlotTypes.ARMOR, "HorseProtection" );
		String comment = "Horse armor bonus per enchantment level.";
		this.armorBonus = new IntegerConfig( "armor_bonus", comment, false, 2, 1, 10 );
		this.enchantmentGroup.addConfig( this.armorBonus );

		setMaximumEnchantmentLevel( 4 );
		setDifferenceBetweenMinimumAndMaximum( 10 );
		setMinimumEnchantabilityCalculator( level->( 1 + 6 * ( level - 1 ) ) );
	}

	/** Event that updates the armor bonus on each animal entity equipment change. */
	@SubscribeEvent
	public static void onEquipmentChange( LivingEquipmentChangeEvent event ) {
		LivingEntity entity = event.getEntityLiving();

		if( entity instanceof AnimalEntity )
			ATTRIBUTE_HANDLER.setValueAndApply( entity, getArmorBonus( ( AnimalEntity )entity ) );
	}

	/**
	 Calculating the armor bonus of equipped horse armor.

	 @param animal Animal on which the armor bonus is calculated.
	 */
	protected static double getArmorBonus( AnimalEntity animal ) {
		int protectionLevel = EnchantmentHelperPlus.calculateEnchantmentSumIfIsInstanceOf( Instances.HORSE_PROTECTION, animal.getArmorInventoryList(),
			HorseArmorItem.class
		);

		return protectionLevel * Instances.HORSE_PROTECTION.armorBonus.get();
	}
}

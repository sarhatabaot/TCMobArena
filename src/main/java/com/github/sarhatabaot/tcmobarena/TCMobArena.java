package com.github.sarhatabaot.tcmobarena;

import media.xen.tradingcards.TradingCards;
import media.xen.tradingcards.api.addons.AddonLogger;
import media.xen.tradingcards.api.addons.TradingCardsAddon;
import org.bukkit.plugin.java.JavaPlugin;

public final class TCMobArena extends JavaPlugin implements TradingCardsAddon {
	private AddonLogger addonLogger;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		TradingCards tradingCards = (TradingCards) getServer().getPluginManager().getPlugin("TradingCards");
		addonLogger = new AddonLogger(getName(), tradingCards);
		getServer().getPluginManager().registerEvents(new MobArenaListener(this,tradingCards),this);
	}

	@Override
	public JavaPlugin getJavaPlugin() {
		return this;
	}

	@Override
	public AddonLogger getAddonLogger() {
		return addonLogger;
	}
}

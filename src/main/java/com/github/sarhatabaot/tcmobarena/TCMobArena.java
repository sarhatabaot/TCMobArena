package com.github.sarhatabaot.tcmobarena;


import net.tinetwork.tradingcards.api.TradingCardsPlugin;
import net.tinetwork.tradingcards.api.addons.AddonLogger;
import net.tinetwork.tradingcards.api.addons.TradingCardsAddon;
import org.bukkit.plugin.java.JavaPlugin;

public final class TCMobArena extends JavaPlugin implements TradingCardsAddon {
	private AddonLogger addonLogger;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		TradingCardsPlugin tradingCards = (TradingCardsPlugin) getServer().getPluginManager().getPlugin("TradingCards");
		addonLogger = new AddonLogger(getName(), tradingCards);
		getServer().getPluginManager().registerEvents(new MobArenaListener(this,tradingCards, this),this);
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

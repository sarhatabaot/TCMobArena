package com.github.sarhatabaot.tcmobarena;


import net.tinetwork.tradingcards.api.TradingCardsPlugin;
import net.tinetwork.tradingcards.api.addons.AddonLogger;
import net.tinetwork.tradingcards.api.addons.TradingCardsAddon;
import net.tinetwork.tradingcards.api.card.Card;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class TCMobArena extends JavaPlugin implements TradingCardsAddon {
	private AddonLogger addonLogger;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		TradingCardsPlugin<? extends Card<? extends Card>> tradingCards = (TradingCardsPlugin<? extends Card<? extends Card>>) getServer().getPluginManager().getPlugin("TradingCards");
		addonLogger = new AddonLogger(getName(), tradingCards);
		getServer().getPluginManager().registerEvents(new MobArenaListener(this,tradingCards),this);
	}

	@Override
	public @NotNull JavaPlugin getJavaPlugin() {
		return this;
	}

	@Override
	public AddonLogger getAddonLogger() {
		return addonLogger;
	}

	@Override
	public void onReload() {
		onDisable();
		onEnable();
	}
}

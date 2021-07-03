package com.github.sarhatabaot.tcmobarena;

import com.garbagemule.MobArena.MobArena;

import net.tinetwork.tradingcards.api.TradingCardsPlugin;
import net.tinetwork.tradingcards.api.addons.AddonListener;
import net.tinetwork.tradingcards.api.addons.TradingCardsAddon;
import net.tinetwork.tradingcards.api.blacklist.Blacklist;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class MobArenaListener extends AddonListener {
    private final TradingCardsPlugin tradingCards;
    private TCMobArena addonPlugin;
    private CommentedConfigurationNode configurationNode;

    public MobArenaListener(final TradingCardsAddon tradingCardsAddon, final TradingCardsPlugin tradingCards, final TCMobArena addonPlugin) {
        super(tradingCardsAddon);
        this.tradingCards = tradingCards;
        this.addonPlugin = addonPlugin;
        try {
            this.configurationNode = YamlConfigurationLoader.builder()
                    .path(Path.of("config.yml"))
                    .build().load();
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
    }

	/*public MobArenaListener(final TradingCards tradingCards, final JavaPlugin plugin) {
		this.tradingCards = tradingCards;
		this.plugin = plugin;
	}*/

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        MobArena maPlugin = (MobArena) Bukkit.getServer().getPluginManager().getPlugin("MobArena");
        if (maPlugin == null || !maPlugin.isEnabled() || !configurationNode.node("disable-in-arena").getBoolean()) {
            return;
        }

        boolean canDropPlayer = false;
        boolean canDropWorld = false;

        if (event.getEntity().getKiller() != null) {
            Player p = event.getEntity().getKiller();
            canDropPlayer = tradingCards.getPlayerBlacklist().isAllowed(p); //TODO
            canDropWorld = tradingCards.getWorldBlacklist().isAllowed(p.getWorld().getName());
        }
        if (!canDropPlayer || !canDropWorld) {
            return;
        }

        String rare = tradingCards.getCardManager().getRandomRarity(event.getEntityType(), false);
        var cancelled = false;
        if ("None".equalsIgnoreCase(rare))
            return;

        if (tradingCards.getConfig().getBoolean("General.Spawner-Block") && event.getEntity().getCustomName() != null && event.getEntity().getCustomName().equals(tradingCards.getConfig().getString("General.Spawner-Mob-Name"))) {
            tradingCards.debug("Mob came from spawner, not dropping card.");
            cancelled = true;
        }

        if (!cancelled) {
            event.getDrops().add(getRandomDrop(rare));
        }
    }

    private ItemStack getRandomDrop(final String rarity) {
        if (configurationNode.node("use-active-series").getBoolean())
            return tradingCards.getCardManager().getRandomActiveCard(rarity, false).build();
        return tradingCards.getCardManager().getRandomCard(rarity, false).build();
    }
}

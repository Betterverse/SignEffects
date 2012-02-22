package net.betterverse.signthatchest;

import java.util.logging.Logger;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SignThatChest extends JavaPlugin
{
  public static final Logger log = Logger.getLogger("Minecraft");
  SignChestListener SignChestListener = new SignChestListener(this);
  SignThatBlockListener SignThatBlockListener = new SignThatBlockListener(this);

  public void onEnable()
  {
    PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(SignChestListener, this);
		pm.registerEvents(SignThatBlockListener, this);
    log.info("SignThatChest version " + getDescription().getVersion() + 
      " is enabled.");
  }

  public void onDisable() {
    log.info("SignThatChest version " + getDescription().getVersion() + 
      " is disabled.");
  }
}
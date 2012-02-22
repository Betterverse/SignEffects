package net.betterverse.signthatchest;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignThatBlockListener implements Listener
{
  public static SignThatChest plugin;

  public SignThatBlockListener(SignThatChest instance)
  {
    plugin = instance;
  }
  @EventHandler
  public void onSignChange(SignChangeEvent event) {
    if (!event.getLine(0).toLowerCase().contains("[stc]")) {
      return;
    }
    if (!event.getPlayer().hasPermission( "SignThatChest.stc")) {
      event.getPlayer().sendMessage("You're not allowed to create an [stc] sign.");
      event.setLine(0, "");
    }
  }
}
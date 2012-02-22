package net.betterverse.signthatchest;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.IInventory;
import net.minecraft.server.InventoryLargeChest;
import net.minecraft.server.TileEntityDispenser;
import net.minecraft.server.TileEntityFurnace;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.ContainerBlock;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class SignChestListener implements Listener
{
  public static SignThatChest plugin;

  public SignChestListener(SignThatChest instance)
  {
    plugin = instance;
  }

	@EventHandler
  public void onPlayerInteract(PlayerInteractEvent event)
  {
    if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
      return;
    }
    Block block = event.getClickedBlock();
    Player player = event.getPlayer();

    if (block.getType() == Material.SIGN_POST) {
      if (player.hasPermission("SignThatChest.attach")) {
        return;
      }

      BlockFace signPostOrientation = null;
      Byte wallSignData;
      switch (event.getClickedBlock().getData()) {
      case 0:
        signPostOrientation = BlockFace.WEST;
        wallSignData = (byte) 3;
        break;
      case 4:
        signPostOrientation = BlockFace.NORTH;
        wallSignData = (byte) 4;
        break;
      case 8:
        signPostOrientation = BlockFace.EAST;
        wallSignData = (byte) 2;
        break;
      case 12:
        signPostOrientation = BlockFace.SOUTH;
        wallSignData = (byte) 5;
        break;
      default:
        return;
      }
      Block blockBehind = event.getClickedBlock().getRelative(signPostOrientation.getOppositeFace());
      if (!(blockBehind.getState() instanceof ContainerBlock)) {
        return;
      }
      Sign sign = (Sign)block.getState();
      String[] lines = sign.getLines();

      block.setType(Material.WALL_SIGN);
      block.setData(wallSignData.byteValue());

      sign = (Sign)block.getState();

      for (int i = 0; i < lines.length; i++) {
        sign.setLine(i, lines[i]);
      }

      sign.update();
      return;
    }
    if (block.getType() == Material.WALL_SIGN) {
      if (!((Sign)block.getState()).getLine(0).equalsIgnoreCase("[stc]")) {
        return;
      }
      if (!player.hasPermission("SignThatChest.stc")) {
        player.sendMessage("You do not have permission to use this sign!");
        return;
      }

      BlockFace signOrientation = null;

      switch (event.getClickedBlock().getData()) {
      case 2:
        signOrientation = BlockFace.EAST.getOppositeFace();
        break;
      case 3:
        signOrientation = BlockFace.WEST.getOppositeFace();
        break;
      case 4:
        signOrientation = BlockFace.SOUTH;
        break;
      case 5:
        signOrientation = BlockFace.NORTH;
        break;
      default:
        return;
      }

      Block blockBehind = event.getClickedBlock()
        .getRelative(signOrientation);

      if (!(blockBehind.getState() instanceof ContainerBlock)) {
        player.sendMessage("The block behind the sign is not a container!");
        return;
      }
      CraftPlayer cp = (CraftPlayer)player;
      EntityPlayer ep = cp.getHandle();
      Inventory inventory = ((ContainerBlock)blockBehind.getState())
        .getInventory();
      IInventory inv = ((CraftInventory)inventory).getInventory();

      IInventory inv2 = null;

      Block maybeNeighboringChest = blockBehind.getRelative(-1, 0, 0);
      if (maybeNeighboringChest.getType().equals(Material.CHEST))
      {
        Inventory inventory2 = ((ContainerBlock)maybeNeighboringChest.getState()).getInventory();
        inv2 = ((CraftInventory)inventory2).getInventory();
      }

      if (inv2 == null)
      {
        maybeNeighboringChest = blockBehind.getRelative(1, 0, 0);
        if (maybeNeighboringChest.getType().equals(Material.CHEST))
        {
          Inventory inventory2 = ((ContainerBlock)maybeNeighboringChest.getState()).getInventory();
          inv2 = ((CraftInventory)inventory2).getInventory();
        }
      }

      if (inv2 == null)
      {
        maybeNeighboringChest = blockBehind.getRelative(0, 0, -1);
        if (maybeNeighboringChest.getType().equals(Material.CHEST))
        {
          Inventory inventory2 = ((ContainerBlock)maybeNeighboringChest.getState()).getInventory();
          inv2 = ((CraftInventory)inventory2).getInventory();
        }
      }

      if (inv2 == null)
      {
        maybeNeighboringChest = blockBehind.getRelative(0, 0, 1);
        if (maybeNeighboringChest.getType().equals(Material.CHEST))
        {
          Inventory inventory2 = ((ContainerBlock)maybeNeighboringChest.getState()).getInventory();
          inv2 = ((CraftInventory)inventory2).getInventory();
        }

      }

      if ((blockBehind.getState() instanceof Furnace)) {
        ep.a((TileEntityFurnace)inv);
      } else if ((blockBehind.getState() instanceof Dispenser)) {
        ep.a((TileEntityDispenser)inv);
      } else if (inv2 == null) {
        ep.a(inv);
      } else {
        InventoryLargeChest largeinv = new InventoryLargeChest("Large chest", inv, inv2);
        ep.a(largeinv);
      }
    }
  }
}
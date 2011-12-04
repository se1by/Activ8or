package me.se1by.activ8or;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class activ8or extends JavaPlugin{
	String chatpre = ChatColor.DARK_GREEN + "[Activ8or] ";
	private YamlConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/activ8or/config.yml"));
	String setpw1 = config.getString("Password1");
	String setpw2 = config.getString("Password2");
	String permGroup = config.getString("UserGroup");
	boolean enabled = true;

	@Override
	public void onDisable() {
		System.out.println("[Activ8or] disabled");
		
	}

	@Override
	public void onEnable() {
		System.out.println("[Activ8or] enabled");
		
		boolean exists = new File("plugins/activ8or/config.yml").exists();
	    if (!exists)
	    {
	      try {
	    	  new File("plugins/activ8or").mkdir();
	    	  new File("plugins/activ8or/config.yml").createNewFile();
	      } catch (IOException e) {
			System.out.println("[Activ8or] Unable to create file config.yml!");
			e.printStackTrace();
	      }
	      config.set("Password1", "se1by");
	      config.set("Password2", "cat");
	      config.set("UserGroup", "User");
	      
	      try {
			config.save(new File("plugins/activ8or/config.yml"));
		} catch (IOException e) {
			System.out.println("[Activ8or] Unable to save file config.yml!");
			e.printStackTrace();
		}
	    }
	}
	
	 public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	  {
	    if (cmd.getName().equalsIgnoreCase("activ8or"))
	    {
	      if (!(sender instanceof Player))
	      {
	        sender.sendMessage("[Activ8or] This command isn't compatible with the console in this version.");
	        return true;
	      }
	      Player player = (Player) sender;
	      if(args.length == 0){
	    	  if (player.hasPermission("Activ8or.Toggle")){
	    		  if (enabled){
	    			  enabled = false;
	    			  player.sendMessage(chatpre + ChatColor.GREEN + "disabled!");
	    		  }
	    		  else{
	    			  enabled = true;
	    			  player.sendMessage(chatpre + ChatColor.GREEN + "enabled!");
	    		  }
	    	  }
	    	  else{
	    		  player.sendMessage(chatpre + ChatColor.RED + "Insufficient permissions!");
	    	  }
	    	  return true;
	      }
	      else if (args.length == 2){
	    	  if (enabled){
	    		  String pw1 = args[0];
	    		  String pw2 = args[1];
	    	  
	    		  if (pw1.equalsIgnoreCase(setpw1) && pw2.equalsIgnoreCase(setpw2)){
	    			  player.sendMessage(chatpre + ChatColor.GREEN + "Passwords accepted!");
	    		  
	    			  givePerm(player);
	    		  
	    			  player.sendMessage(chatpre + ChatColor.GREEN + "Account promoted!");
	    		  }
	    		  else{
	    			  player.sendMessage(chatpre + ChatColor.RED + "Wrong passwords!");
	    		  }
	    	  }
	    	  else{
	    		  player.sendMessage(chatpre + ChatColor.RED + "This plugin is not activated!");
	    	  }
	    	  return true;
	      }
	      else if (args.length == 3){
	    	  if (player.hasPermission("Activ8or.Set")){
	    		  if (args[0].equalsIgnoreCase("set")){
	    			  this.setpw1 = args[1];
	    			  this.setpw2 = args[2];
	    		  
	    			  config.set("Password1", setpw1);
	    			  config.set("Password2", setpw2);
	    			  
	    			  player.sendMessage(chatpre + ChatColor.GREEN + "Passwords changed!");
	    			  try {
	    				  config.save(new File("plugins/activ8or/config.yml"));
	    			  } catch (IOException e) {
	    				  System.out.println("[Activ8or] Unable to save file config.yml!");
	    				  e.printStackTrace();
	    			  }
	    		  }
	    	  }
	    	  return true;
	      }
	      else{
	    	  showHelp(player);
	    	  return true;
	      }
	    }
	    return false;
	  }

	private void showHelp(Player player) {
		player.sendMessage(chatpre + ChatColor.DARK_BLUE + "Type in /activ8or [password1] [password2] to get promoted!");
		if (player.hasPermission("Activ8or.Set")){
			player.sendMessage(chatpre + ChatColor.DARK_BLUE + "Type in /activ8or set [password1] [password2] to set new passwords!");
		}
		if (player.hasPermission("Activ8or.Toggle")){
			player.sendMessage(chatpre + ChatColor.DARK_BLUE + "Type in /activ8or to enable or disable this plugin!");
		}
	}
	
	private String getPermissionSystem(){
		 Plugin pex = getServer().getPluginManager().getPlugin("PermissionsEx");
		    Plugin bPerm = getServer().getPluginManager().getPlugin("bPermissions");
		    Plugin permBuk = getServer().getPluginManager().getPlugin("PermissionsBukkit");

		    if (pex != null)
		    {
		      return "pex";
		    }

		    if (bPerm != null)
		    {
		      return "bPerm";
		    }
		    
		    if (permBuk != null)
		    {
		    	return "permBuk";
		    }

		    return "none";
	}
	
	private void givePerm(Player player){  		  
  		  String PermPlugin = getPermissionSystem();
  		  if (PermPlugin == "pex")
    	    {
    	      CommandSender cs = getServer().getConsoleSender();
    	      getServer().dispatchCommand(cs, "pex user " + player.getName() + " group add " + permGroup);
    	    }

    	    else if (PermPlugin == "bPerm")
    	    {
    	      CommandSender cs = getServer().getConsoleSender();
    	      getServer().dispatchCommand(cs, "/permissions global addgroup " + permGroup + " " + player.getName());
    	    }
    	    else if (PermPlugin == "permBuk")
    	    {
    	    	CommandSender cs = getServer().getConsoleSender();
    	    	getServer().dispatchCommand(cs, "perm player addgroup" + player.getName() + " " + permGroup);
    	    }
	}
	
	

}

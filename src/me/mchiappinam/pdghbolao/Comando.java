package me.mchiappinam.pdghbolao;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Comando implements CommandExecutor {
  private Main plugin;
  protected static double acumulado = -1.0;

  public Comando(Main main) {
    plugin = main;
  }

	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (cmd.getName().equalsIgnoreCase("bolao")) {
			if ((args.length==0) || (args.length>1)) {
				help(((Player)sender));
				return true;
			}
			if(args[0].equalsIgnoreCase("apostar")) {
				if(acumulado==-1.0) {
			    	sender.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §cNenhum bolão acontecendo no momento.");
			    	if (sender.hasPermission("pdgh.admin")) {
			    		sender.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §4Para iniciar uma rodada digite §a/bolao iniciar");
			    		sender.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §4Para cancelar uma rodada digite §a/bolao cancelar");
			    		sender.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §4Para finalizar uma rodada digite §a/bolao finalizar");
			    	}
			    	return true;
			    }
		        if (!(Main.econ.getBalance(sender.getName()) >= plugin.getConfig().getDouble("TaxaMoney"))) {
				    sender.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §cVocê não tem money suficiente.");
				    sender.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §cMoney necessário: §a$"+plugin.getConfig().getDouble("TaxaMoney")+"§c.");
				    return true;
		        }
	        	Main.econ.withdrawPlayer(sender.getName(), plugin.getConfig().getDouble("TaxaMoney"));
	        	Main.apostadores.add(sender.getName());
	        	acumulado = acumulado + plugin.getConfig().getDouble("TaxaMoney");
			    sender.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §aApostado com sucesso. Taxa: §c$"+plugin.getConfig().getDouble("TaxaMoney")+"§a.");
			    sender.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §aQuanto mais você apostar mais serão suas chances de ganhar!");
			}else if(args[0].equalsIgnoreCase("info")) {
				if(acumulado==-1.0) {
			    	sender.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §cNenhum bolão acontecendo no momento.");
			    	if (sender.hasPermission("pdgh.admin")) {
			    		sender.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §4Para iniciar uma rodada digite §a/bolao iniciar");
			    		sender.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §4Para cancelar uma rodada digite §a/bolao cancelar");
			    		sender.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §4Para finalizar uma rodada digite §a/bolao finalizar");
			    	}
			    	return true;
			    }else{
			    	sender.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §cPara apostar digite §a/bolao apostar");
			    	sender.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §cMoney necessário: §a$"+plugin.getConfig().getDouble("TaxaMoney")+"§c.");
			    	sender.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §cAcumulado: §a$"+acumulado+"§c.");
			    	sender.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §cQuanto mais você apostar mais serão suas chances de ganhar!");
			    }
			}else if(args[0].equalsIgnoreCase("iniciar")) {
		    	if (!sender.hasPermission("pdgh.admin")) {
		    		sender.sendMessage("§cSem permissao.");
		    		return true;
		    	}
				if(!(acumulado==-1.0)) {
			    	sender.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §cJá tem um bolão acontecendo no momento.");
			    	return true;
			    }
				plugin.delay5Min();
			}else if(args[0].equalsIgnoreCase("cancelar")) {
		    	if (!sender.hasPermission("pdgh.admin")) {
		    		sender.sendMessage("§cSem permissao.");
		    		return true;
		    	}
				if(acumulado==-1.0) {
			    	sender.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §cNenhum bolão acontecendo no momento.");
			    	if (sender.hasPermission("pdgh.admin")) {
			    		sender.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §4Para iniciar uma rodada digite §a/bolao iniciar");
			    		sender.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §4Para cancelar uma rodada digite §a/bolao cancelar");
			    		sender.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §4Para finalizar uma rodada digite §a/bolao finalizar");
			    	}
			    	return true;
			    }
				plugin.cancelar(((Player)sender));
			}else if(args[0].equalsIgnoreCase("finalizar")) {
		    	if (!sender.hasPermission("pdgh.admin")) {
		    		sender.sendMessage("§cSem permissao.");
		    		return true;
		    	}
				plugin.finalizar(((Player)sender));
			}
		}
		return true;
	}
	
	public void help(Player p) {
		p.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §cPara apostar digite §a/bolao apostar");
		p.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §cPara informações digite §a/bolao info");
		p.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §cMoney necessário para cada aposta: §a$"+plugin.getConfig().getDouble("TaxaMoney")+"§c.");
		p.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §cQuanto mais você apostar mais serão suas chances de ganhar!");
	}
	
	
	
	
	
}
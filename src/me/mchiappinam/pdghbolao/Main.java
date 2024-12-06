package me.mchiappinam.pdghbolao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.milkbowl.vault.economy.Economy;

import me.mchiappinam.pdghbolao.Comando;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;

public class Main extends JavaPlugin implements Listener {
	protected static Economy econ = null;
	int tDelay;
	protected static List<String> apostadores = new ArrayList<String>();
	protected static List<String> ganhadores = new ArrayList<String>();
	
	public void onEnable() {
		File file = new File(getDataFolder(),"config.yml");
		if(!file.exists()) {
			try {
				saveResource("config_template.yml",false);
				File file2 = new File(getDataFolder(),"config_template.yml");
				file2.renameTo(new File(getDataFolder(),"config.yml"));
			}
			catch(Exception e) {}
		}
		getServer().getPluginCommand("bolao").setExecutor(new Comando(this));
		getServer().getPluginManager().registerEvents(this, this);
		if(!setupEconomy()) {
			getLogger().warning("ERRO: Vault (Economia) nao encontrado!");
			getLogger().warning("Desativando o plugin...");
			getServer().getPluginManager().disablePlugin(this);
        }
		delay1H();
		getServer().getConsoleSender().sendMessage("§3[PDGHBolao] §2ativado - Plugin by: mchiappinam");
		getServer().getConsoleSender().sendMessage("§3[PDGHBolao] §2Acesse: http://pdgh.com.br/");
	}

	public void onDisable() {
		getServer().getConsoleSender().sendMessage("§3[PDGHBolao] §2desativando...");
		cancelar(null);
		getServer().getConsoleSender().sendMessage("§3[PDGHBolao] §2desativado - Plugin by: mchiappinam");
		getServer().getConsoleSender().sendMessage("§3[PDGHBolao] §2Acesse: http://pdgh.com.br/");
	  }
	
	public void delay1H() {
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				delay5Min();
			}
		}, 60*60*20L);
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	private void onChat(ChatMessageEvent e) {
		if ((e.getTags().contains("bolao")) && (ganhadores.contains(e.getSender().getName().toLowerCase()))) {
			e.setTagValue("bolao", "§2Ⓑⓞⓛⓐⓞ");
		}
	}
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

	public void delay5Min() {
		tDelay = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			int timer = 300;
			public void run() {
				if(timer <0) {
					cDelay();
				}
				if((timer == 300) || (timer == 240) || (timer == 180) || (timer == 120) || (timer == 60)) {
					if(timer==300) {
						if(Comando.acumulado==-1.0) {
							Comando.acumulado=0;
					    }else{
					    	return;
					    }
						getServer().broadcastMessage("§3[Ⓑⓞⓛⓐⓞ] §aBolão iniciado!");
					}else{
						getServer().broadcastMessage("§3[Ⓑⓞⓛⓐⓞ] §aBolão!");
					}
					if(timer==60) {
						getServer().broadcastMessage("§3[Ⓑⓞⓛⓐⓞ] §c1 §aminuto restante.");
					}else{
						getServer().broadcastMessage("§3[Ⓑⓞⓛⓐⓞ] §c"+timer/60+" §aminutos restantes.");
					}
					getServer().broadcastMessage("§3[Ⓑⓞⓛⓐⓞ] §aPara ver os comandos/taxa digite §c/bolao");
					getServer().broadcastMessage("§3[Ⓑⓞⓛⓐⓞ] §aQuanto mais você apostar mais serão suas chances de ganhar!");
				}
				if((timer == 10) || (timer == 20) || (timer == 30) || (timer == 40) || (timer == 50)) {
					getServer().broadcastMessage("§3[Ⓑⓞⓛⓐⓞ] §aBolão!");
					getServer().broadcastMessage("§3[Ⓑⓞⓛⓐⓞ] §c"+timer+" §asegundos restantes.");
					getServer().broadcastMessage("§3[Ⓑⓞⓛⓐⓞ] §aPara ver os comandos/taxa digite §c/bolao");
					getServer().broadcastMessage("§3[Ⓑⓞⓛⓐⓞ] §aQuanto mais você apostar mais serão suas chances de ganhar!");
				}
				if(timer == 0) {
					finalizar(null);
				}
				timer--;
			}
		}, 0, 20*1);
	}

	private void cDelay() {
		Bukkit.getScheduler().cancelTask(tDelay);
	}

	public void finalizar(Player p) {
	    if(Comando.acumulado==-1.0) {
		    if(!(p==null)) {
		    	p.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §cNenhum bolão acontecendo no momento.");
		    	if (p.hasPermission("pdgh.admin")) {
		    		p.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §4Para iniciar uma rodada digite §a/bolao iniciar");
		    		p.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §4Para cancelar uma rodada digite §a/bolao cancelar");
		    		p.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §4Para finalizar uma rodada digite §a/bolao finalizar");
		    	}
		    }
	    	return;
	    }
	    if(p==null) {
	    	getServer().broadcastMessage("§3[Ⓑⓞⓛⓐⓞ] §aBolão finalizado!");
	    }else{
	    	getServer().broadcastMessage("§3[Ⓑⓞⓛⓐⓞ] §aBolão finalizado pelo §c"+p.getName()+"§a!");
	    }
	    if (!Main.apostadores.isEmpty()) {
	    	Random r = new Random();
	    	int ganhador = r.nextInt(Main.apostadores.size());
	    	String vencedor = (String)Main.apostadores.get(ganhador);
	    	Main.econ.depositPlayer(vencedor, Comando.acumulado);
	    	ganhadores.add(vencedor.toLowerCase());
	    	getServer().broadcastMessage("§3[Ⓑⓞⓛⓐⓞ] §c"+vencedor+" §afoi o vencedor, parabéns!");
	    	getServer().broadcastMessage("§3[Ⓑⓞⓛⓐⓞ] §c$"+Comando.acumulado+" §aera o acumulado.");
	    	Comando.acumulado = -1.0;
	    	cDelay();
	    	Main.apostadores.clear();
	    }else{
	    	getServer().broadcastMessage("§3[Ⓑⓞⓛⓐⓞ] §aSem apostadores!");
	    	Comando.acumulado = -1.0;
	    	cDelay();
	    }
	}

	public void cancelar(Player p) {
	    if(Comando.acumulado==-1.0) {
		    if(!(p==null)) {
		    	p.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §cNenhum bolão acontecendo no momento.");
		    	if (p.hasPermission("pdgh.admin")) {
		    		p.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §4Para iniciar uma rodada digite §a/bolao iniciar");
		    		p.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §4Para cancelar uma rodada digite §a/bolao cancelar");
		    		p.sendMessage("§3[Ⓑⓞⓛⓐⓞ] §4Para finalizar uma rodada digite §a/bolao finalizar");
		    	}
		    }
	    	return;
	    }
	    if(p==null) {
	    	getServer().broadcastMessage("§3[Ⓑⓞⓛⓐⓞ] §aBolão cancelado!");
	    }else{
	    	getServer().broadcastMessage("§3[Ⓑⓞⓛⓐⓞ] §aBolão cancelado pelo §c"+p.getName()+"§a!");
	    }
	    if (!Main.apostadores.isEmpty()) {
	    	for(String n : apostadores) {
	    		Main.econ.depositPlayer(n, getConfig().getDouble("TaxaMoney"));
	    	}
	    	getServer().broadcastMessage("§3[Ⓑⓞⓛⓐⓞ] §c$"+Comando.acumulado+" §aera o acumulado.");
	    	getServer().broadcastMessage("§3[Ⓑⓞⓛⓐⓞ] §aQuantidade gasta de cada jogador devolvida.");
	    	Comando.acumulado = -1.0;
	    	cDelay();
	    	Main.apostadores.clear();
	    }else{
	    	getServer().broadcastMessage("§3[Ⓑⓞⓛⓐⓞ] §aSem apostadores!");
	    	Comando.acumulado = -1.0;
	    	cDelay();
	    }
	}
	
	
	
	
	
	
	
	
	
}
package com.poixson.ecotick;

import static com.poixson.utils.BukkitUtils.GarbageCollect;
import static com.poixson.utils.BukkitUtils.SafeCancel;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.utils.ThreadUtils;


public class LaggerTask extends BukkitRunnable {

	protected final EcoTickPlugin plugin;

	protected AtomicLong state = new AtomicLong(0L);
	protected final long delay;



	public LaggerTask(final EcoTickPlugin plugin, final long delay) {
		this.plugin = plugin;
		this.delay  = delay;
	}



	public void start() {
		this.runTaskTimer(this.plugin, 200L, 19L);
	}
	public void stop() {
		SafeCancel(this);
	}



	@Override
	public void run() {
		if (Bukkit.getOnlinePlayers().size() == 0) {
			final long state = this.state.getAndIncrement();
			if (state == this.delay) {
				this.log().info("Slowing the server..");
				GarbageCollect();
			} else
			if (state > this.delay) {
				// once every 5 minutes
				if (state % 300L == 0)
					GarbageCollect();
				ThreadUtils.Sleep(1000L);
			}
		} else {
			if (this.state.getAndSet(0L) != 0L)
				this.log().info("Resuming normal ticks..");
		}
	}



	public Logger log() {
		return this.plugin.getLogger();
	}



}

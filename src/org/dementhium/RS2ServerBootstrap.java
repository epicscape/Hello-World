package org.dementhium;

import java.io.File;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.dementhium.cache.Cache;
import org.dementhium.event.EventManager;
import org.dementhium.model.World;
import org.dementhium.model.player.Player;
import org.dementhium.mysql.ForumIntegration;
import org.dementhium.net.DementhiumPipelineFactory;
import org.dementhium.util.Constants;
import org.dementhium.util.MapXTEA;
import org.dementhium.util.Misc;
import org.dementhium.util.NullLogger;
import org.dementhium.util.ServerLogger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * @author `Discardedx2 <the_shawn@discardedx2.info>
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class RS2ServerBootstrap {

	/**
	 * If sql is disabled (I added this, considering my ip kept changing. ~Emperor)
	 */
	public static boolean sqlDisabled = false;

	//https://market.android.com/details?id=com.fasterlonger.TurboDroid12&feature=apps_topselling_free
	
	/**
	 * If we should debug messages.
	 */
	public static final boolean DEBUG_ENABLED = true;
	
	public static void main(String[] args) throws Exception {
		if (!Misc.isVPS()) {
			System.setOut(new ServerLogger(System.out));
		} else {
			System.setOut(new NullLogger(System.out));
		}
		if(args.length > 0 && args[0].equalsIgnoreCase("sqlDisabled"))
			sqlDisabled = true;
		Cache.init();
		MapXTEA.init();
		World.getWorld().load();
		SaveManager.getSaveManager();
		EventManager.getEventManager().load();
		if (Constants.CONNECTING_TO_FORUMS) {
			System.out.println("Connecting to forums...");
			ForumIntegration.init();
		}
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable t) {
				t.printStackTrace();
				System.out.println("Uncaught exception has occured");
				restart("[SERVER] Restart due to uncaught exception - contact the Dementhium team at www.dementhium.com.");
			}
		});
		System.gc();
		System.runFinalization();
		Runtime.getRuntime().addShutdownHook(DementhiumShutdownHook.getSingleton());
		//restart("");
		bind(43594);
	}

	/**
	 * Restarts the server.
	 *
	 * @param message The message the players will receive.
	 * @return {@code True}, always.
	 */
	public static final boolean restart(String message) {
		return restart(message, 3000);
	}

	/**
	 * Restarts the server.
	 *
	 * @param message The message the players will receive.
	 * @param delay   The delay between sending the message and restarting.
	 * @return {@code True}, always.
	 */
	public static final boolean restart(String message, long delay) {
		for (Player player : World.getWorld().getPlayers()) {
			synchronized (player) {
				player.sendMessage(message);
			}
		}
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		executor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					if (Misc.isWindows()) {
						File file = new File("./Run.bat");
						Runtime.getRuntime().exec("cmd.exe /C start " + file.getPath());
					} else {
						Runtime.getRuntime().exec(new String[] {"cd Dropbox", "cd Dem*", "./run.sh", ""});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				System.exit(1);
			}
		}, delay, TimeUnit.MILLISECONDS);
		return true;
	}

	public static void bind(int port) throws IOException {
		Executor executor = Executors.newCachedThreadPool();

		ServerBootstrap bootstrap = new ServerBootstrap();
		ServerSocketChannelFactory socketFactory = new NioServerSocketChannelFactory(executor, executor, Runtime.getRuntime().availableProcessors());
		ChannelPipelineFactory pipelineFactory = new DementhiumPipelineFactory();

		bootstrap.setOption("localAddress", new InetSocketAddress(port));
		bootstrap.setOption("child.tcpNoDelay", true);

		bootstrap.setFactory(socketFactory);
		bootstrap.setPipelineFactory(pipelineFactory);
		bootstrap.bind();
		System.err.println("Server bound to port - " + port);
	}

	//	public static void dump() throws IOException {
	//		RandomAccessFile raf = new RandomAccessFile("data/packedKeys.bin", "rw");
	//		for(String string : FileUtilities.readFile("data/xtea650.txt")) {
	//			int regionId = Integer.valueOf(string.split(" ")[0]);
	//			string = string.substring(string.indexOf(")") + 2);
	//			String[] sKeys = string.split("\\.");
	//			raf.writeShort(regionId);
	//			for(int i = 0; i < 4; i++) {
	//				raf.writeInt(Integer.parseInt(sKeys[i]));
	//			}
	//		}
	//	}
}

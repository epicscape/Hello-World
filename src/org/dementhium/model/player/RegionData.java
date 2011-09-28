package org.dementhium.model.player;

import org.dementhium.model.Location;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

public class RegionData {

	private boolean didTeleport;
	private boolean didMapRegionChange;
	private boolean NeedReload;
	private Location lastMapRegion;
	private Location lastLocation;

	private Player player;

	public RegionData(Player entity) {
		this.player = entity;
		this.setLastLocation(Location.locate(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ()));
	}

	public void teleport(int coordX, int coordY, int height) {
		getLastMapRegion();
		player.getWalkingQueue().reset();
		player.setAttribute("cantMove", Boolean.TRUE);
		final Location futurelocation = Location.locate(coordX, coordY, height);
		if (lastMapRegion.getRegionX() - futurelocation.getRegionX() >= 4 || lastMapRegion.getRegionX() - futurelocation.getRegionX() <= -4) {
			setDidMapRegionChange(true);
		} else if (lastMapRegion.getRegionY() - futurelocation.getRegionY() >= 4 || lastMapRegion.getRegionY() - futurelocation.getRegionY() <= -4) {
			setDidMapRegionChange(true);
		}
		this.lastLocation = player.getLocation();
		player.setLocation(futurelocation);
		setDidTeleport(true);
		if (isDidMapRegionChange()) {
			player.updateMap();
			setDidMapRegionChange(false);
		}
		if (player.getSettings().isResting()) {
			ActionSender.sendBConfig(player, 119, 0);
			player.getSettings().setResting(false);
		}
		player.submitTick("resetMove", new Tick(1) {
			public void execute() {
				stop();
				player.removeAttribute("cantMove");
			}
		});
	}


	public void reset() {
		if (player.getWalkingQueue().isDidTele())
			this.setDidTeleport(false);
		this.setDidMapRegionChange(false);
		this.setNeedReload(false);
	}

	public void setDidMapRegionChange(boolean b) {
		didMapRegionChange = b;
	}

	public boolean isDidMapRegionChange() {
		return didMapRegionChange;
	}

	public void setLastMapRegion(Location lastMapRegion) {
		this.lastMapRegion = lastMapRegion;
	}

	public Location getLastMapRegion() {
		if (lastMapRegion == null) {
			lastMapRegion = player.getLocation();
		}
		return lastMapRegion;
	}

	public void setDidTeleport(boolean didTeleport) {
		this.didTeleport = didTeleport;
	}

	public boolean isDidTeleport() {
		return didTeleport;
	}

	public void setNeedReload(boolean needReload) {
		NeedReload = needReload;
	}

	public boolean isNeedReload() {
		return NeedReload;
	}

	public void setLastLocation(Location lastLocation) {
		this.lastLocation = lastLocation;
	}

	public Location getLastLocation() {
		return lastLocation;
	}

	public void teleport(Location pos) {
		teleport(pos.getX(), pos.getY(), pos.getZ());
	}
}

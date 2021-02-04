/*
 * TraincartsDynmap - Renders Traincarts trains on Dynmap
 * Copyright (C) 2021 Underscore11
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.wolvhaven.traincartsdynmap;

import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartGroupStore;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.markers.CircleMarker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

public final class TraincartsDynmap extends JavaPlugin {

  @Override
  public void onEnable() {
    // Plugin startup logic

    this.getServer().getScheduler().runTaskTimer(this, () -> {
      MarkerAPI markerAPI = ((DynmapCommonAPI) TraincartsDynmap.getProvidingPlugin(DynmapCommonAPI.class)).getMarkerAPI();
      MarkerSet trainsSet = markerAPI.getMarkerSet("trains");
      if (trainsSet == null) {
        trainsSet = markerAPI.createMarkerSet("trains", "Trains", null, false);
      }

      for (CircleMarker circleMarker : trainsSet.getCircleMarkers()) {
        circleMarker.deleteMarker();
      }

      for (MinecartGroup group : MinecartGroupStore.getGroups()) {
        for (MinecartMember<?> member : group) {
          Block loc = member.getBlock();
          trainsSet.createCircleMarker(String.valueOf(member.hashCode()),
                  group.getProperties().getDisplayName(),
                  false,
                  group.getWorld().getName(),
                  loc.getX(),
                  loc.getY(),
                  loc.getZ(),
                  0,
                  0,
                  false);
        }
      }
    }, 10L, 10L);
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}

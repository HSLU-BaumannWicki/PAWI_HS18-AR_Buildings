package eu.kudan.ar.dto;

import android.content.res.AssetManager;
import android.location.Location;
import eu.kudan.kudan.ARModelNode;

public interface Building {
    public ARModelNode getKudanModelNode();
    public Location getBuildingLocation();
}

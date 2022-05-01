package it.fktcod.ktykshrk.module.mods.addon;

import java.util.ArrayList;

import it.fktcod.ktykshrk.utils.NodeProcessor.Node;
import net.minecraft.util.Vec3;;

public class TeleportResult {
	
	public ArrayList<Vec3> positions;
	public ArrayList<Vec3> positionsBack;
	public ArrayList<Node> triedPaths;
	public Vec3 lastPos;
	public ArrayList<Node> path;
	public boolean foundPath;
	
	public TeleportResult(ArrayList<Vec3> positions, ArrayList<Vec3> positionsBack, ArrayList<Node> triedPaths, ArrayList<Node> path, Vec3 lastPos, boolean foundPath) {
		this.positions = positions;
		this.positionsBack = positionsBack;
		this.triedPaths = triedPaths;
		this.path = path;
		this.foundPath = foundPath;
		this.lastPos = lastPos;
	}
	
}

package com.brett.world.blocks;

import java.util.HashMap;

import org.lwjgl.util.vector.Vector3f;

import com.brett.renderer.Loader;
import com.brett.renderer.datatypes.ModelTexture;
import com.brett.sound.AudioController;
import com.brett.tools.Maths;
import com.brett.world.VoxelWorld;
import com.tester.Main;

/**
*
* @author brett
*
*/

public class Block {
	
	public static final HashMap<Short, Block> blocks = new HashMap<Short, Block>();
	
	public ModelTexture model;
	private int[] breakSound = {0};
	
	public Block(ModelTexture model) {
		this.model = model;
	}
	
	public void onBlockCreated(float x, float y, float z, VoxelWorld world) {
		
	}
	
	public void onBlockPlaced(float x, float y, float z, VoxelWorld world) {
		
	}
	
	public void onBlockBreaked(float x, float y, float z, VoxelWorld world) {
		
	}
	
	public Block setBreakSound(int[] sound) {
		this.breakSound = sound;
		return this;
	}
	
	public Block setBreakSound(int sound) {
		this.breakSound[0] = sound;
		return this;
	}
	
	public void playBreakSound(Vector3f pos) {
		playBreakSound(pos.x, pos.y, pos.z);
	}
	
	public void playBreakSound(float x, float y, float z) {
		Main.staticSource.setPosition(x, y, z);
		Main.staticSource.play(breakSound[Maths.randInt(0, breakSound.length-1)]);
		Main.staticSource.setReferenceDistance(6);
		Main.staticSource.setRollOffFactor(5);
		Main.staticSource.setMaxDistance(15);
	}
	
	public int[] getBreakSounds() {
		return breakSound;
	}
	
	public int getBreakSound() {
		return breakSound[0];
	}
	
	public static void registerBlocks(Loader loader) {
		blocks.put((short) 0, new BlockAir());
		blocks.put((short) 1, new Block(new ModelTexture(loader.loadTexture("stone"))).setBreakSound(AudioController.loadSound("bounce.ogg")));
		blocks.put((short) 2, new Block(new ModelTexture(loader.loadTexture("dirt"))).setBreakSound(AudioController.loadSound("bounce.ogg")));
		blocks.put((short) 3, new Block(new ModelTexture(loader.loadTexture("icon/logo"))).setBreakSound(AudioController.loadSound("bounce.ogg")));
		blocks.put((short) 4, new Block(new ModelTexture(loader.loadTexture("grassy2"))).setBreakSound(AudioController.loadSound("bounce.ogg")));
		blocks.put((short) 5, new Block(new ModelTexture(loader.loadTexture("sand"))).setBreakSound(AudioController.loadSound("bounce.ogg")));
		blocks.put((short) 6, new Block(new ModelTexture(loader.loadTexture("clay"))).setBreakSound(AudioController.loadSound("bounce.ogg")));
		blocks.put((short) 7, new Block(new ModelTexture(loader.loadTexture("snow"))).setBreakSound(AudioController.loadSound("bounce.ogg")));
	}
	
}

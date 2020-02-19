package com.brett.renderer.particles;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.brett.DisplayManager;
import com.brett.world.cameras.Camera;
import com.brett.world.entities.Entity;

public class Particle {

	private Vector3f position;
	private Vector3f velocity;
	private float gravityEffect;
	private float lifeLength;
	private float rotation;
	private float scale;
	
	private float elapsedTime = 0;

	private Vector3f reusableVector3 = new Vector3f(0,0,0);
	
	private ParticleTexture texture;
	private Vector2f texOffset1 = new Vector2f();
	private Vector2f texOffset2 = new Vector2f();
	private float blend;
	private float distance;

	public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation,
			float scale) {
		this.texture = texture;
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		ParticleMaster.addParticle(this);
	}
	
	public Particle(ParticleTexture texture, Entity entity, Vector3f velocity, float gravityEffect, float lifeLength, float rotation,
			float scale) {
		this.texture = texture;
		this.position = new Vector3f(entity.getPosition());
		this.velocity = Vector3f.add(entity.getVelocity(), velocity, velocity);
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		ParticleMaster.addParticle(this);
	}
	
	public boolean update(Camera camera) {
		velocity.y += Entity.GRAVITY * gravityEffect * DisplayManager.getFrameTimeSeconds();
		reusableVector3.set(velocity);
		reusableVector3.scale(DisplayManager.getFrameTimeSeconds());
		Vector3f.add(reusableVector3, position, position);
		// TODO: Oh god i don't like this. GL_ONE < - as blending (ONLY FOR GLOW)
		distance = Vector3f.sub(camera.getPosition(), position, null).lengthSquared();
		updateTextureCoordInfo();
		elapsedTime += DisplayManager.getFrameTimeSeconds();
		return elapsedTime < lifeLength;
	}
	
	private void updateTextureCoordInfo() {
		float lifeFactor = elapsedTime / lifeLength;
		int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows();
		float atlasProgress = lifeFactor * stageCount;
		int indexStage1 = (int) Math.floor(atlasProgress);
		int indexStage2 = indexStage1 < stageCount - 1 ? indexStage1 + 1 : indexStage1;
		this.blend = atlasProgress % 1;
		setTextureOffset(texOffset1, indexStage1);
		setTextureOffset(texOffset2, indexStage2);
	}
	
	private void setTextureOffset(Vector2f offset, int index) {
		int column = index % texture.getNumberOfRows();
		int row = index / texture.getNumberOfRows();
		offset.x = (float) column / texture.getNumberOfRows();
		offset.y = (float) row / texture.getNumberOfRows();
	}
	
	public ParticleTexture getTexture() {
		return texture;
	}

	public float getDistance() {
		return distance;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}
	
	public Vector2f getTexOffset1() {
		return texOffset1;
	}

	public Vector2f getTexOffset2() {
		return texOffset2;
	}

	public float getBlend() {
		return blend;
	}
	
}

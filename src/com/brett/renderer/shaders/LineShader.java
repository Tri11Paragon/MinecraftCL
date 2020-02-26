/** 
*	Brett Terpstra
*	Feb 25, 2020
*	
*/ 

package com.brett.renderer.shaders;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.brett.tools.Maths;
import com.brett.world.cameras.Camera;

public class LineShader extends ShaderProgram {

	private int vao = 0;
	private int count = 0;
	private int vbo = 0;
	
	private static final String VERTEX_FILE = "lineVertexShader.txt";
	private static final String FRAGMENT_FILE = "lineFragmentShader.txt";
	
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_translationMatrix;
	
	public LineShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		this.start();
		this.loadTranslationMatrix();
		this.stop();
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_translationMatrix = super.getUniformLocation("translationMatrix");
	}
	
	public void renderIN(Vector3f pos1, Vector3f pos2) {
		this.start();
		int vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		float[] f = {pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z};
		int vboID = storeDataInAttributeList(0, 3, f);
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_LINES, 0, f.length);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		GL15.glDeleteBuffers(vboID);
		GL30.glDeleteVertexArrays(vaoID);
		this.stop();
	}
	
	public void createStaticLine(Vector3f pos1, Vector3f pos2) {
		if (vao != 0)
			GL30.glDeleteVertexArrays(vao);
		if (vbo != 0)
			GL15.glDeleteBuffers(vbo);
		vao = 0;
		vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		float[] f = {pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z};
		count = f.length;
		vbo = 0;
		vbo = storeDataInAttributeList(0, 3, f);
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * renderers the last static line.
	 */
	public void render() {
		if (vao == 0)
			return;
		this.start();
		GL30.glBindVertexArray(vao);
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_LINES, 0, count);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		this.stop();
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private int storeDataInAttributeList(int attributeNumber, int coordinateSize,float[] data){
		int vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber,coordinateSize,GL11.GL_FLOAT,false,0,0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vboID;
	}
	
	@Override
	public void cleanUp() {
		super.cleanUp();
		GL30.glDeleteVertexArrays(vao);
		GL15.glDeleteBuffers(vbo);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadTranslationMatrix() {
		super.loadMatrix(location_translationMatrix, Maths.createTransformationMatrix(new Vector3f(0,0,0), 0, 0, 0, 1));
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

}

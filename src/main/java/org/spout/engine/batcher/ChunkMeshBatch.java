/*
 * This file is part of Spout.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
 * Spout is licensed under the SpoutDev License Version 1.
 *
 * Spout is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spout is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.engine.batcher;

import org.lwjgl.opengl.GL11;
import org.spout.api.render.RenderMaterial;
import org.spout.api.render.Renderer;
import org.spout.api.render.effect.SnapshotBatch;
import org.spout.engine.renderer.BatchVertex;
import org.spout.engine.renderer.BatchVertexRenderer;

/**
 * Represents a group of chunk meshes to be rendered.
 */
public class ChunkMeshBatch {	
	private int x,y,z;
	private Renderer renderer = BatchVertexRenderer.constructNewBatch(GL11.GL_TRIANGLES);
	private BatchVertex batchVertex = null;
	private boolean hasVertices = false;
	private final RenderMaterial material;
	private boolean closed = false;

	public ChunkMeshBatch(int x, int y, int z, RenderMaterial material) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.material = material;
	}

	public Renderer getRenderer(){
		return renderer;
	}

	public void update() {
		if (closed) {
			throw new IllegalStateException("Already closed");
		}

		if(batchVertex.getVertexCount() == 0){
			hasVertices = false;
			batchVertex = null;
			return;
		}

		hasVertices = true;

		renderer.begin();

		SnapshotBatch snapshotBatch = new SnapshotBatch(material);

		material.preBatch(snapshotBatch);
		((BatchVertexRenderer)renderer).setBatchVertex(batchVertex);
		material.postBatch(snapshotBatch);

		renderer.end();

		//Free batchVertex
		//batchVertex = null; // Needed tp keep it for GL 2 & 3
	}

	public boolean hasVertices() {
		return hasVertices;
	}

	public void render(RenderMaterial material) {
		if (closed) {
			throw new IllegalStateException("Already closed");
		}
		if (hasVertices) {
			renderer.render(material);
		}
	}

	public void finalize() {
		if (closed) {
			throw new IllegalStateException("Already closed");
		}
		((BatchVertexRenderer)renderer).release();
		closed = true;
	}

	@Override
	public String toString() {
		return "ChunkMeshBatch [x=" + x + ", y=" + y +  ", z=" + z + "]";
	}

	public void setMesh(BatchVertex batchVertex) {
		this.batchVertex = batchVertex;
	}

	public BatchVertex getMesh() {
		return batchVertex;
	}

	public RenderMaterial getMaterial() {
		return material;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

}
package graphics.scenery

import cleargl.GLVector
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.util.*
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * Constructs a cylinder with the given [radius] and number of [segments].
 * Main [axis] can be specified and defaults to positive Y axis.
 * Adapted from https://www.freemancw.com/2012/06/opengl-cone-function/
 *
 * @author Ulrik Günther <hello@ulrik.is>
 * @param[radius] The radius of the sphere
 * @param[segments] Number of segments in latitude and longitude.
 */

class Cone(val radius: Float, val height: Float, val segments: Int, axis: GLVector = GLVector(0.0f, 1.0f, 0.0f)) : Node("cone"), HasGeometry {
    override val vertexSize = 3
    override val texcoordSize = 2
    override var geometryType = GeometryType.TRIANGLES

    override var vertices: FloatBuffer = BufferUtils.allocateFloat(2 * 3 * segments * 3)
    override var normals: FloatBuffer = BufferUtils.allocateFloat(2 * 3 * segments * 3)
    override var texcoords: FloatBuffer = BufferUtils.allocateFloat(2 * 2 * segments * 3)
    override var indices: IntBuffer = BufferUtils.allocateInt(0)

    val axis = axis.normalized

    init {
        val vbuffer = ArrayList<GLVector>(segments * segments * 2 * 3)
        val nbuffer = ArrayList<GLVector>(segments * segments * 2 * 3)
        val tbuffer = ArrayList<GLVector>(segments * segments * 2 * 2)

        val apex = axis * height
        val center = apex - axis * height

        val e0 = perp(axis)
        val e1 = e0.cross(axis)

        // cone is split into [segments] sections
        val delta = 2.0f/segments * PI.toFloat()

        // draw cone by creating triangles between adjacent points on the
        // base and connecting one triangle to the apex, and one to the center
        for (i in 0 until segments) {
            val rad = delta * i
            val rad2 = delta * (i + 1)
            val v1 = center + (e0 * cos(rad) + e1 * sin(rad)) * radius
            val v2 = center + (e0 * cos(rad2) + e1 * sin(rad2)) * radius

            vbuffer.add(v1)
            vbuffer.add(apex)
            vbuffer.add(v2)

            vbuffer.add(v2)
            vbuffer.add(center)
            vbuffer.add(v1)

            val normalSide = (apex - v2).cross(v2 - v1).normalized
            val normalBottom = axis * (-1.0f)
            nbuffer.add(normalSide)
            nbuffer.add(normalSide)
            nbuffer.add(normalSide)

            nbuffer.add(normalBottom)
            nbuffer.add(normalBottom)
            nbuffer.add(normalBottom)

            tbuffer.add(GLVector(cos(rad) * 0.5f + 0.5f, sin(rad) * 0.5f + 0.5f))
            tbuffer.add(GLVector(0.5f, 0.5f))
            tbuffer.add(GLVector(cos(rad2) * 0.5f + 0.5f, sin(rad2) * 0.5f + 0.5f))

            tbuffer.add(GLVector(cos(rad2) * 0.5f + 0.5f, sin(rad2) * 0.5f + 0.5f))
            tbuffer.add(GLVector(0.5f, 0.5f))
            tbuffer.add(GLVector(cos(rad) * 0.5f + 0.5f, sin(rad) * 0.5f + 0.5f))
        }

        vbuffer.forEach { v -> vertices.put(v.toFloatArray()) }
        nbuffer.forEach { n -> normals.put(n.toFloatArray()) }
        tbuffer.forEach { uv -> texcoords.put(uv.toFloatArray()) }

        vertices.flip()
        normals.flip()
        texcoords.flip()

        boundingBox = generateBoundingBox()
    }

    fun perp(v: GLVector): GLVector {
        var min = v.x()
        var cardinalAxis = GLVector(1.0f, 0.0f, 0.0f)

        if(abs(v.y()) < min) {
            min = abs(v.y())
            cardinalAxis = GLVector(0.0f, 1.0f, 0.0f)
        }

        if(abs(v.z()) < min) {
            cardinalAxis = GLVector(0.0f, 0.0f, 1.0f)
        }

        return cardinalAxis
    }

}

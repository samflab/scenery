package graphics.scenery.tests.unit

import cleargl.GLVector
import graphics.scenery.Cone
import graphics.scenery.Scene
import graphics.scenery.numerics.Random
import graphics.scenery.utils.LazyLogger
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Tests for the [Cone] primitive.
 *
 * @author Aryaman Gupta <aryaman1994@gmail.com>
 */
class ConeTests {
    private val logger by LazyLogger()

    /**
     * Tests the creation of cones and their bounding boxes.
     */
    @Test
    fun testCreation() {
        logger.info("Testing Cone creating and bounding box calculation ...")
        val s = Scene()
        val radius = Random.randomFromRange(0.1f, 5000.0f)
        val height = Random.randomFromRange(0.1f, 5000.0f)
        val segments = kotlin.random.Random.nextInt(1, 5)
        val axisN = GLVector(0.0f, 1.0f, 0.0f)
        //TODO: Generalise

        val epsilon = 0.0001f

        val c = Cone(radius, height, 4 * segments, axisN)
        s.addChild(c)
        c.updateWorld(true)

        val bb = c.boundingBox

        assertNotNull(bb, "Bounding Box should not be null")

        val d = -1 * ((bb.min.x() * axisN.x()) + (bb.min.y() * axisN.y()) + (bb.min.z() * axisN.z()))
        val expHeight = ((bb.max.x() * axisN.x()) + (bb.max.y() * axisN.y()) + (bb.max.z() * axisN.z())) + d
        //TODO: Add test for radius

        assertTrue { expHeight <= height + epsilon }
    }
}

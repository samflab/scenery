package graphics.scenery

import cleargl.GLVector

/**
 * Point light class.
 *
 * Point lights have an extent given as [lightRadius].
 * They also have an optional [Box] to accompany them for easier visualisation.
 *
 * @author Ulrik Günther <hello@ulrik.is>
 * @constructor Creates a PointLight with default settings, e.g. white emission color.
 */
class DirectionalLight(var direction: GLVector = GLVector(0.0f, 1.0f, 0.0f)) : Light("DirectionalLight") {
    /** The intensity of the point light. Bound to [0.0, 1.0] if using non-HDR rendering. */
    @ShaderProperty
    override var intensity: Float = 1.0f

    /** The emission color of the point light. Setting it will also affect the accompanying Box' color. */
    @ShaderProperty
    override var emissionColor: GLVector = GLVector(1.0f, 1.0f, 1.0f)

    /** Type of the light, can be DirectionalLight or PointLight */
    @ShaderProperty
    override val lightType: LightType = LightType.DirectionalLight

    @ShaderProperty private var lightRadius: Float = 1.0f

    /** Node name of the Point Light */
    override var name = "PointLight"

    @Suppress("unused") // will be serialised into ShaderProperty buffer
    @ShaderProperty val worldPosition
        get() = direction

    @Suppress("unused") // will be serialised into ShaderProperty buffer
    @ShaderProperty var debugMode = 0

    init {
        // fake geometry
        this.vertices = BufferUtils.allocateFloatAndPut(
            floatArrayOf(
                -1.0f, -1.0f, 0.0f,
                1.0f, -1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                -1.0f, 1.0f, 0.0f))

        this.normals = BufferUtils.allocateFloatAndPut(
            floatArrayOf(
                1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f))

        this.texcoords = BufferUtils.allocateFloatAndPut(
            floatArrayOf(
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 1.0f))

        this.indices = BufferUtils.allocateIntAndPut(
            intArrayOf(0, 1, 2, 0, 2, 3))

        this.geometryType = GeometryType.TRIANGLES
        this.vertexSize = 3
        this.texcoordSize = 2

        material.blending.transparent = true
        material.blending.colorBlending = Blending.BlendOp.add
        material.blending.sourceColorBlendFactor = Blending.BlendFactor.One
        material.blending.destinationColorBlendFactor = Blending.BlendFactor.One
        material.blending.sourceAlphaBlendFactor = Blending.BlendFactor.One
        material.blending.destinationAlphaBlendFactor = Blending.BlendFactor.One
        material.blending.alphaBlending = Blending.BlendOp.add
        material.cullingMode = Material.CullingMode.Front
        material.depthTest = Material.DepthTest.Greater
    }
}

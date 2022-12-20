package com.terrencealuda.tcardio.predictor

import android.content.Context
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.nnapi.NnApiDelegate
import org.tensorflow.lite.support.common.FileUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class HeartPredictor(context: Context) {

    private val tflite by lazy {
        Interpreter(
            FileUtil.loadMappedFile(context, MODEL_PATH)
        )
    }

    fun doInference(
        trbpsPar: Float,
        thalachPar: Float,
        age: Float,
        sex: Float,
        chol: Float,
        fbs: Float
    ): Map<String, FloatBuffer?> {
        val input = floatArrayOf(
            age,
            sex,
            3.0F,
            trbpsPar,
            chol,
            fbs,
            0.0F,
            thalachPar,
            0.0F,
            2.3F,
            0.0F,
            0.0F,
            1.0F
        )
        val inF = floatArrayToBuffer(input)
        var outs = floatArrayOf(0.0F)
        var outF = floatArrayToBuffer(outs)

        //inputs.
        val inputs: Map<String, FloatBuffer?> = mapOf("x" to inF)
        var outputs: Map<String, FloatBuffer?> = mutableMapOf("prediction" to outF)

        tflite.runSignature(inputs, outputs, "predictor")
        return outputs
    }

    fun floatArrayToBuffer(floatArray: FloatArray): FloatBuffer? {
        val byteBuffer: ByteBuffer = ByteBuffer
            .allocateDirect(floatArray.size * 4)
        byteBuffer.order(ByteOrder.nativeOrder())
        val floatBuffer: FloatBuffer = byteBuffer.asFloatBuffer()
        floatBuffer.put(floatArray)
        floatBuffer.position(0)
        return floatBuffer
    }

    companion object {
        private const val MODEL_PATH = "tcardio_model.tflite"
    }
}
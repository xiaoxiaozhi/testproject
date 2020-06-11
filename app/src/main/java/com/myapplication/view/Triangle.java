package com.myapplication.view;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

//三角形比较简单自己写顶点和片元
public class Triangle {

    //三角形的顶点，集合，依次是 x y z
    //正常的xy 坐标系点到点之间是一个向量
    static float triangleCoords[] = {
            0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
    };
    private FloatBuffer vertexBuffer;

    public void onDrawFrame(GL10 gl) {
        //在GPU里面申明空间, float四字节
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());//给内存排个顺序
        vertexBuffer = bb.asFloatBuffer();//管道
        //gl语言建模

    }
}

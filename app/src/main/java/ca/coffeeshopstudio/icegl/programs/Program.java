/*
 * Copyright 2015 Terence Doerksen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ca.coffeeshopstudio.icegl.programs;

import android.opengl.GLES20;

/**
 * GL Shader Program abstract used by the GL Library
 */
public abstract class Program
{
    //Program variables
    protected int shaderProgramHandle;

    private int positionHandle;
    private int textureCoordinateHandle;
    private int matrixHandle;

    /**
     * retrieve the code we will be using to compile the vertex shader program
     * @return GL Program for compiling a vertex shader
     */
    public abstract String getVertexShaderProgram();

    /**
     * retrieve the code we will be using to compile the fragment shader program
     * @return GL Program for compiling a fragment shader
     */
    public abstract String getFragmentShaderProgram();

    /**
     * Load the specified shader in
     * @param type ID of GLES20.GL_VERTEX_SHADER or GLES20.GL_FRAGMENT_SHADER
     * @return pointer to the shader program
     * @throws IllegalArgumentException
     */
    protected int loadShader(int type)
    {
        if (type != GLES20.GL_VERTEX_SHADER &&
                type != GLES20.GL_FRAGMENT_SHADER)
            throw new IllegalArgumentException("Shader type MUST be GLES20.GL_VERTEX_SHADER or GLES20.GL_FRAGMENT_SHADER");

        String shaderCode;
        if (type == GLES20.GL_VERTEX_SHADER)
            shaderCode = getVertexShaderProgram();
        else //if (type == GLES20.GL_FRAGMENT_SHADER)
            shaderCode = getFragmentShaderProgram();

        if (shaderCode == null || shaderCode.isEmpty())
            throw new IllegalArgumentException("Shader program you are attempting to load is empty");

        //create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        //or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shaderHandle = GLES20.glCreateShader(type);

        //add the source code to the shader and compile it
        GLES20.glShaderSource(shaderHandle, shaderCode);
        GLES20.glCompileShader(shaderHandle);

        //Get the compilation status.
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        //If the compilation failed, delete the shader.
        if (compileStatus[0] == 0)
        {
            String result = GLES20.glGetShaderInfoLog(shaderHandle);
            GLES20.glDeleteShader(shaderHandle);
            throw new RuntimeException("Error creating shader. " + result);
        }

        //return the shader
        return shaderHandle;
    }

    /**
     * Build the shader programs
     */
    public void buildShaders() {
        //create the shaders
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER);

        shaderProgramHandle = GLES20.glCreateProgram();             //create empty OpenGL ES Program

        GLES20.glAttachShader(shaderProgramHandle, vertexShader);   //add vertex shader to program
        GLES20.glAttachShader(shaderProgramHandle, fragmentShader); //add fragment shader to program

        //creates OpenGL ES program executables
        GLES20.glLinkProgram(shaderProgramHandle);

        //Bind required attributes
        //GLES20.glBindAttribLocation(shaderProgramHandle, 1, "vColor");

        //get handle to vertex shaders vPosition member
        positionHandle = GLES20.glGetAttribLocation(shaderProgramHandle, "vPosition");
        //Get handle to texture coordinates location
        textureCoordinateHandle = GLES20.glGetAttribLocation(shaderProgramHandle, "a_texCoord" );
        //Get handle to shape's transformation matrix
        matrixHandle = GLES20.glGetUniformLocation(shaderProgramHandle, "uMVPMatrix");

        //Get the link status.
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(shaderProgramHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

        //If the link failed, delete the program.
        if (linkStatus[0] == 0)
        {
            GLES20.glDeleteProgram(shaderProgramHandle);
            shaderProgramHandle = 0;
            throw new RuntimeException("Error building program. ");
        }
    }

    /**
     * Get the pointer to our shader
     * @return Shader pointer or handle
     */
    public int getPositionHandle()
    {
        return positionHandle;
    }

    /**
     * Get the texture coordinates stored in memory
     * @return where the texture coordinates are located
     */
    public int getTextureCoordinateHandle()
    {
        return textureCoordinateHandle;
    }

    /**
     * ID of our matrix
     * @return matrix handle
     */
    public int getMatrixHandle()
    {
        return matrixHandle;
    }

    /**
     * Return the ID of our image program
     * @return image program ID
     */
    public int getProgramID()
    {
        return shaderProgramHandle;
    }
}
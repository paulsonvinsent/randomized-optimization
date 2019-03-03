package com.distribuitech;

import opt.ga.Vertex;

import java.util.Random;

public class Helper {

    public static Vertex[] getGraph(Integer N, Integer L) {
        Random random = new Random(N * L);
        // create the random velocity
        Vertex[] vertices = new Vertex[N];
        for (int i = 0; i < N; i++) {
            Vertex vertex = new Vertex();
            vertices[i] = vertex;
            vertex.setAdjMatrixSize(L);
            for (int j = 0; j < L; j++) {
                vertex.getAadjacencyColorMatrix().add(random.nextInt(N * L));
            }
        }
        return vertices;
    }
}

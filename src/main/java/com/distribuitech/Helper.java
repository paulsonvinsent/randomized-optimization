package com.distribuitech;

import opt.ga.Vertex;
import shared.DataSet;
import shared.Instance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;

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


    public static Instance[] readWineQalityData(String path, Integer count, Integer classesCount) throws Exception {

        double[][][] attributes = new double[count][][];
        BufferedReader br = new BufferedReader(new FileReader(new File(path)));

        for (int i = 0; i < attributes.length; i++) {

            attributes[i] = new double[2][];
            attributes[i][0] = new double[11];
            attributes[i][1] = new double[1];
            Scanner scan = new Scanner(br.readLine());
            scan.useDelimiter(",");

            for (int j = 0; j < 11; j++)
                attributes[i][0][j] = Double.parseDouble(scan.next());

            attributes[i][1][0] = Double.parseDouble(scan.next());
        }

        Instance[] instances = new Instance[attributes.length];

        for (int i = 0; i < instances.length; i++) {
            instances[i] = new Instance(attributes[i][0]);
            int c = (int) attributes[i][1][0];
            double[] classes = new double[classesCount];
            classes[c] = 1.0;
            instances[i].setLabel(new Instance(classes));

        }

       return instances;
    }
}

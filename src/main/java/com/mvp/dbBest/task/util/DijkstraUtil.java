package com.mvp.dbBest.task.util;

import com.mvp.dbBest.task.Dijkstra;
import com.mvp.dbBest.task.Graph;
import com.mvp.dbBest.task.Node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DijkstraUtil {
    private static final String DELIMITER = ";";
    private static String BASIC_FILE = "input.csv";
    private static String DATA_TO_FIND_VERTEX = "vertex.csv";
    private static final String INPUT = "input//";
    private static final String OUTPUT = "output//";
    private static final String FILENAME = "output.csv";
    private static Map<String, Node> nodes = new HashMap<>();

    private static void printDataToFile(String string) throws IOException {
        printToFile(FILENAME, string);
    }

    public static void showProgram(){
        try {
            solution();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void solution() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Graph graph = createTheGraph();
        List<String[]> strings = DijkstraUtil.readVertexFromFile();
        if (strings != null) {
            for (int i = 0; i < strings.size(); i++) {
                Node nodeFromGraph = DijkstraUtil.getNodeFromGraph(graph, strings.get(i)[0]);
                DijkstraUtil.searchByNode(graph, nodeFromGraph);
                boolean isExistPath = DijkstraUtil.isPathExists(graph, strings.get(i)[0], strings.get(i)[1]);
                if (isExistPath) {
                    stringBuilder.append(true);
                    stringBuilder.append(";");
                    stringBuilder.append(DijkstraUtil.
                            minDistance(graph, strings.get(i)[0], strings.get(i)[1]));
                    stringBuilder.append("\n");
                } else {
                    stringBuilder.append(false);
                    stringBuilder.append(";\n");
                }
            }
            printDataToFile(stringBuilder.toString());
        }
    }


    private static void printToFile(String nameOfFile, String str) throws IOException {
        if (!Files.exists(Paths.get(OUTPUT))) {
            Files.createDirectory(Paths.get(OUTPUT));
        }
        if (!Files.exists(Paths.get(OUTPUT + nameOfFile))) {
            Files.createFile(Paths.get(OUTPUT + nameOfFile));
        }
        StringBuilder stringBuilder = new StringBuilder("ROUTE EXISTS;MIN LENGTH\n");
        stringBuilder.append(str);
        Files.writeString(Path.of(OUTPUT + FILENAME), stringBuilder.toString());
    }

    private static boolean isPathExists(Graph graph, String firstNode, String lastNode) {
        Set<Node> nodes = graph.getNodes();
        Node node1 = null;
        Node temp = null;
        for (Node node : nodes) {
            node1 = node;
            for (Node node2 : node1.getShortestPath()) {
                temp = node2;
            }
            if (!node1.getShortestPath().isEmpty()) {
                if (temp != null && node1.getShortestPath().get(0).getName().equals(firstNode)) {
                    if (temp.getName().equals(lastNode)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static int minDistance(Graph graph, String firstNode, String lastNode) {
        int min = 0;
        Set<Node> nodes = graph.getNodes();
        Node node1 = null;
        Node temp = null;
        for (Node node : nodes) {
            node1 = node;
            for (Node node2 : node1.getShortestPath()) {
                temp = node2;
            }
            if (!node1.getShortestPath().isEmpty()) {
                if (temp != null && node1.getShortestPath().get(0).getName().equals(firstNode)) {
                    if (temp.getName().equals(lastNode)) {
                        return temp.getDistance();
                    }
                }
            }
        }
        return min;
    }

    private static List<String[]> readDataFromFile(String fileName) throws IOException {
        return parseCsv(fileName);
    }

    private static List<String[]> readDataFromFile() throws IOException {
        return parseCsv(BASIC_FILE);
    }

    private static List<String[]> readVertexFromFile() throws IOException {
        return parseCsv(DATA_TO_FIND_VERTEX);
    }

    private static List<String[]> parseCsv(String fileName) throws IOException {
        if (Files.exists(Paths.get(INPUT + fileName))) {
            String content = Files.readString(Path.of(INPUT + fileName));
            List<String[]> strings = content.lines()
                    .skip(1)
                    .map((s -> s.split(DELIMITER)))
                    .collect(Collectors.toList());
            return strings;
        } else {
            if (!Files.exists(Paths.get(INPUT))) {
                Files.createDirectory(Paths.get(INPUT));
            }
            if (!Files.exists(Paths.get(INPUT + BASIC_FILE))) {
                Files.createFile(Paths.get(INPUT + BASIC_FILE));
            }
            if (!Files.exists(Paths.get(INPUT + DATA_TO_FIND_VERTEX))) {
                Files.createFile(Paths.get(INPUT + DATA_TO_FIND_VERTEX));
            }


        }
        return null;
    }

    private static Graph createTheGraph() throws IOException {

        List<String[]> strings = readDataFromFile();
        if (strings != null) {
            for (String[] string : strings) {
                String str0 = string[0];
                String str1 = string[1];

                Node nodeX = nodes.getOrDefault(str0, new Node(str0));
                Node nodeY = nodes.getOrDefault(str1, new Node(str1));
                nodeX.addDestination(nodeY, Integer.parseInt(string[2]));
                nodes.put(str0, nodeX);
                nodes.put(str1, nodeY);
            }
            Graph graph = new Graph(nodes.values());
            return graph;
        }
        return null;
    }

    private static Node getNodeFromGraph(Graph graph, String vertexName) {
        Set<Node> nodes = graph.getNodes();
        for (Node n :
                nodes) {
            if (n.getName().equals(vertexName)) {
                return n;
            }
        }
        return null;
    }

    private static void searchByNode(Graph graph, Node node) {
        Dijkstra.calculateShortestPathFromSource(node);
    }

}

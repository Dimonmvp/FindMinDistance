package com.mvp.dbBest.task;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Graph {

    private Set <Node> nodes = new HashSet<>();

    public Graph() {
    }

    public Graph(Collection<Node> nodes)
    {
        this.nodes = new HashSet<>(nodes);
    }

    public Graph(Set<Node> nodes) {
        this.nodes = nodes;
    }

    public void addNode(Node nodeA) {
        nodes.add(nodeA);
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public void setNodes(Set<Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Graph{");
        sb.append("nodes=").append(nodes);
        sb.append('}');
        return sb.toString();
    }
}
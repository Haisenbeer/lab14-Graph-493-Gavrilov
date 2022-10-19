package com.example.lab14graph;

import com.example.lab14graph.model.Link;
import com.example.lab14graph.model.Node;

import java.util.ArrayList;

public class Graph
{
    int ID;

    String name;

    int countNode = 0;
    int countLink = 0;

    ArrayList<Node> nodes = new ArrayList<Node>();
    ArrayList<Link> links = new ArrayList<Link>();

    public void addNode(float x, float y)
    {
        nodes.add(new Node(countNode++, x, y, ""));
    }

    public void deleteNode(int index)
    {
        if (index < 0) return;

        for (int i = 0; i < nodes.size(); i++)
        {
            if (nodes.get(i).ID == index)
            {
                nodes.remove(i);
                return;
            }
        }
    }

    public Node getNode(int ID)
    {
        for (int i = 0; i < nodes.size(); i++)
        {
            if (nodes.get(i).ID == ID)
            {
                Node n = nodes.get(i);
                return n;
            }
        }

        return null;
    }

    public void deleteAllNodes()
    {
        countNode = 0;
        nodes.clear();
    }

    public void addLink(int selectedNode1, int selectedNode2, int typeLink)
    {
        links.add(new Link(countLink++, selectedNode1, selectedNode2, typeLink, 0));
    }

    public void deleteLink(int linkID)
    {
        if (linkID < 0) return;

        for (int i = 0; i < links.size(); i++)
        {
            if (links.get(i).ID == linkID)
            {
                links.remove(i);
                return;
            }
        }
    }

    public Link getLink(int ID)
    {
        for (int i = 0; i < links.size(); i++)
        {
            if (links.get(i).ID == ID)
            {
                Link l = links.get(i);
                return l;
            }
        }

        return null;
    }

    public void deleteAllLinks()
    {
        countLink = 0;
        links.clear();
    }

    @Override
    public String toString() {
        return name;
    }
}

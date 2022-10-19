package com.example.lab14graph;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab14graph.model.Link;
import com.example.lab14graph.model.Node;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    GraphView gv;

    int newGraphID = 1;
    int curGraphID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gv = findViewById(R.id.graphView);
        gv.gr.ID = newGraphID;

        g.graphs = new DB(this, "graphs.db", null, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        String nameGraph;

        int id = item.getItemId();

        AlertDialog.Builder builder;
        LayoutInflater inflater;
        View view;

        switch (id)
        {
            case R.id.new_graph:
                gv.clearGraph();
                curGraphID = -1;
                break;
            case R.id.save_graph:
                if (curGraphID < 0)
                {
                    newGraphID = g.graphs.getMaxGraphId() + 1;
                    nameGraph = "Graph" + newGraphID;

                    g.graphs.insertGraph(nameGraph);
                }
                else
                {
                    newGraphID = curGraphID;
                    nameGraph = gv.gr.name;
                    g.graphs.deleteLinkFromGraph(curGraphID);
                    g.graphs.deleteNodeFromGraph(curGraphID);

                    g.graphs.renameGraph(newGraphID, nameGraph);
                }

                ArrayList<Node> graphNodes = gv.gr.nodes;
                ArrayList<Link> graphLinks = gv.gr.links;

                for (int i = 0; i < graphNodes.size(); i++)
                {
                    Node n = graphNodes.get(i);

                    g.graphs.insertNode(n.ID, n.x, n.y, n.caption, newGraphID);
                }

                for (int i = 0; i < graphLinks.size(); i++)
                {
                    Link l = graphLinks.get(i);

                    g.graphs.insertLink(l.ID, l.a, l.b, l.typeLink, l.value, newGraphID);
                }

                curGraphID = gv.gr.ID;
                break;
            case R.id.load_graph:
                ArrayList<Graph> gr = new ArrayList<Graph>();
                g.graphs.getAllGraphs(gr);

                int sizeGr = gr.size();

                final String[] curName = {""};
                String[] arrGraph = new String[sizeGr];

                for (int i = 0; i < sizeGr; i ++)
                {
                    arrGraph[i] = gr.get(i).toString();
                }

                builder = new AlertDialog.Builder(this);

                builder.setItems(arrGraph, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            curName[0] = arrGraph[which];

                            for (int i = 0; i < sizeGr; i ++)
                            {
                                if (curName[0] == gr.get(i).toString())
                                {
                                    Graph curGr = g.graphs.getGraphs(gr.get(i).ID);
                                    gv.gr = curGr;
                                    gv.gr.countNode = curGr.nodes.size();
                                    gv.gr.countLink = curGr.links.size();

                                    curGraphID = curGr.ID;

                                    gv.invalidate();
                                    break;
                                }
                            }
                            }
                        })
                        .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());

                builder.show();


                break;
            case R.id.copy_graph:
                if (curGraphID < 0) return true;

                newGraphID = g.graphs.getMaxGraphId() + 1;
                nameGraph = gv.gr.name + "_Copy";

                g.graphs.insertGraph(nameGraph);

                Graph copyGraph = new Graph();

                copyGraph.nodes = gv.gr.nodes;
                copyGraph.links = gv.gr.links;

                ArrayList<Node> copyGraphNodes = copyGraph.nodes;
                ArrayList<Link> copyGraphLinks = copyGraph.links;

                for (int i = 0; i < copyGraphNodes.size(); i++)
                {
                    Node n = copyGraphNodes.get(i);

                    g.graphs.insertNode(n.ID, n.x, n.y, n.caption, newGraphID);
                }

                for (int i = 0; i < copyGraphLinks.size(); i++)
                {
                    Link l = copyGraphLinks.get(i);

                    g.graphs.insertLink(l.ID, l.a, l.b, l.typeLink, l.value, newGraphID);
                }
                break;
            case R.id.rename_graph:
                if (curGraphID < 0) return true;

                builder = new AlertDialog.Builder(this);

                inflater = this.getLayoutInflater();
                view = inflater.inflate(R.layout.dialog_new_graph_name, null);
                builder.setView(view);

                EditText et = view.findViewById(R.id.ed_NewGraphName);

                et.setText(gv.gr.name);

                builder.setPositiveButton("Save", (dialogInterface, i) ->
                {
                    String newName = et.getText().toString();
                    gv.gr.name = newName;
                    g.graphs.renameGraph(curGraphID, newName);

                    dialogInterface.cancel();
                })
                        .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());

                builder.show();
                break;
            case R.id.delete_graph:
                if (curGraphID < 0) return true;

                builder = new AlertDialog.Builder(this);

                builder.setPositiveButton("ОК", (dialogInterface, i) ->
                {
                    g.graphs.deleteLinkFromGraph(curGraphID);
                    g.graphs.deleteNodeFromGraph(curGraphID);
                    g.graphs.deleteGraph(curGraphID);

                    gv.gr = new Graph();
                    curGraphID = -1;
                    gv.invalidate();

                    dialogInterface.cancel();
                })
                        .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                        .setTitle("Delete this graph?");

                builder.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addNode_Click(View v)
    {
        gv.addNode();
    }

    public void captionNode_Click(View v)
    {
        if (gv.lastHitNode < 0) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_node_caption, null);
        builder.setView(view);

        builder.setTitle("Caption node")
                .setPositiveButton("Save", (dialogInterface, i) ->
                {
                    EditText et = view.findViewById(R.id.et_CaptionNode);
                    gv.setCaptionNode(et.getText().toString());
                    dialogInterface.cancel();
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());

        builder.show();
    }

    public void deleteNode_Click(View v)
    {
        gv.deleteNode();
    }

    public void addLink_Click(View v)
    {
        if (gv.selected1 < 0) return;
        if (gv.selected2 < 0) return;

        final int[] typeLink = new int[1];

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_type_link, null);
        builder.setView(view);

        builder.setTitle("Type link")
                .setPositiveButton("OK", (dialogInterface, i) ->
                {
                    Spinner sp = view.findViewById(R.id.sp_TypeLink);
                    typeLink[0] = (int) sp.getSelectedItemId();
                    gv.addLink(typeLink[0]);
                    dialogInterface.cancel();
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());

        builder.show();
    }

    public void captionLink_Click(View v)
    {
        if (gv.lastHitLink < 0) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_link_caption, null);
        builder.setView(view);

        EditText ed1 = view.findViewById(R.id.ed_CaptionOne);
        ed1.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

        builder.setTitle("Caption link")
                .setPositiveButton("Save", (dialogInterface, i) ->
                {
                    String strNewValue = ed1.getText().toString();

                    try
                    {
                        gv.setValueLink(Float.parseFloat(strNewValue));
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(this, "New value is not number", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    dialogInterface.cancel();
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());

        builder.show();
    }

    public void deleteLink_Click(View v)
    {
        gv.deleteLink();
    }
}
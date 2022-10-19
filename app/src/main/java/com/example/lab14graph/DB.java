package com.example.lab14graph;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.lab14graph.model.Link;
import com.example.lab14graph.model.Node;

import java.util.ArrayList;

public class DB extends SQLiteOpenHelper {
    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql;

        sql = "CREATE TABLE Graphs (graphID INTEGER primary key autoincrement, name TEXT);";
        db.execSQL(sql);
        sql = "CREATE TABLE Nodes (nodeID INT, posX REAL, posY REAL, caption TEXT, graphID INT, " +
                "foreign key (graphID) references Graphs(graphID), CONSTRAINT nodePK PRIMARY KEY (nodeID, graphID));";
        db.execSQL(sql);
        sql = "CREATE TABLE Links (linkID INT, nodeA INT, nodeB INT, typeLink INT, value REAL, graphID INT, " +
                "foreign key (nodeA) references Nodes(nodeID), foreign key (nodeB) references Nodes(nodeID), foreign key (graphID) references Graphs(graphID), CONSTRAINT linkPK PRIMARY KEY (linkID, graphID));";
        db.execSQL(sql);
    }

    public int getMaxGraphId()
    {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT MAX(graphID) FROM Graphs;";
        Cursor cur = db.rawQuery(sql, null);
        if (cur.moveToFirst()==true) return cur.getInt(0);
        return 0;
    }

    //Добавление графа в БД
    public void insertGraph(String name)
    {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "INSERT INTO Graphs (name) VALUES('" + name + "');";

        try
        {
            db.execSQL(sql);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //Переименование графа
    public void renameGraph(int graphID, String newName)
    {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "UPDATE Graphs SET name = '" + newName + "' WHERE graphID = " + graphID +";";

        try
        {
            db.execSQL(sql);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void deleteGraph(int graphID)
    {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "DELETE FROM Graphs WHERE graphID = " + graphID +";";

        try
        {
            db.execSQL(sql);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Graph getGraphs(int ID)
    {

        Graph gr = new Graph();
        ArrayList<Node> nodes = new ArrayList<Node>();
        ArrayList<Link> links = new ArrayList<Link>();

        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM Graphs WHERE graphID = " + ID +";";
        Cursor cur = db.rawQuery(sql, null);

        if (cur.moveToFirst() == true)
        {
            do {
                gr.ID = cur.getInt(0);
                gr.name = cur.getString(1);

                sql = "SELECT * FROM Nodes WHERE graphID = " + ID +";";
                cur = db.rawQuery(sql, null);

                if (cur.moveToFirst() == true)
                {
                    do {
                        nodes.add(new Node(cur.getInt(0), cur.getFloat(1), cur.getFloat(2), cur.getString(3)));
                    }while (cur.moveToNext() == true);
                }

                sql = "SELECT * FROM Links WHERE graphID = " + ID +";";
                cur = db.rawQuery(sql, null);

                if (cur.moveToFirst() == true)
                {
                    do {
                        links.add(new Link(cur.getInt(0), cur.getInt(1), cur.getInt(2), cur.getInt(3), cur.getFloat(4)));
                    }while (cur.moveToNext() == true);
                }

                gr.nodes = nodes;
                gr.links = links;
            }while (cur.moveToNext() == true);
        }

        return gr;
    }

    public void getAllGraphs(ArrayList<Graph> lst)
    {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM Graphs;";
        Cursor cur = db.rawQuery(sql, null);
        if (cur.moveToFirst() == true)
        {
            do {
                Graph gr = new Graph();
                gr.ID = cur.getInt(0);
                gr.name = cur.getString(1);
                lst.add(gr);
            }while (cur.moveToNext() == true);
        }
    }

    //Добавление узла в БД
    public void insertNode(int ID, float x, float y, String caption, int graphID)
    {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "INSERT INTO Nodes VALUES(" + ID + ", " + x + ", " + y + ", '" + caption + "', " + graphID + ");";

        try
        {
            db.execSQL(sql);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void deleteNodeFromGraph(int graphID)
    {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "DELETE FROM Nodes WHERE graphID = " + graphID +";";

        try
        {
            db.execSQL(sql);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //Добавление связи в БД
    public void insertLink(int ID, int nodeA, int nodeB, int typeLink, float value, int graphID)
    {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "INSERT INTO Links VALUES(" + ID + ", " + nodeA + ", " + nodeB + ", " + typeLink + ", " + value + ", " + graphID + ");";

        try
        {
            db.execSQL(sql);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //Удаление связи из БД
    public void deleteLinkFromGraph(int graphID)
    {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "DELETE FROM Links WHERE graphID = " + graphID + ";";

        try
        {
            db.execSQL(sql);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

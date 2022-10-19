package com.example.lab14graph.model;

public class Link
{
    public int ID;
    public int typeLink;
    public float value;

    public int a;
    public int b;

    public Link(int ID, int a, int b, int typeLink, float value)
    {
        this.ID = ID;
        this.a = a;
        this.b = b;
        this.typeLink = typeLink;
        this.value = value;
    }
}

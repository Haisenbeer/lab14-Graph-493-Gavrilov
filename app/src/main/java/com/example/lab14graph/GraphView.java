package com.example.lab14graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.widget.Button;

import com.example.lab14graph.model.Link;
import com.example.lab14graph.model.Node;

public class GraphView extends SurfaceView
{
    public Graph gr;
    private Paint p;
    private Paint p_text;

    public int lastHitNode = -1;
    public int lastHitLink = -1;
    public int selected1 = -1;
    public int selected2 = -1;
    private int selectedLink = -1;

    private float rad = 50.0f;

    private float last_x = 0;
    private float last_y = 0;

    Boolean selected1Check = false;

    public GraphView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        gr = new Graph();

        p = new Paint();
        p.setStrokeWidth(5.0f);

        p_text = new Paint();
        p_text.setColor(Color.BLACK);
        p_text.setTextSize(30.0f);
        p_text.setTextAlign(Paint.Align.CENTER);

        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        //Заливка фона белым
        canvas.drawColor(Color.WHITE);

        //Обход всех связей
        for (int i = 0; i < gr.links.size(); i ++)
        {
            Link l = gr.links.get(i);
            Node nA = gr.getNode(l.a);
            Node nB = gr.getNode(l.b);
            String linkValue = String.valueOf(l.value);

            if (l.ID == selectedLink)
            {
                p.setColor(Color.rgb(255,0,0));
            }
            else
            {
                p.setColor(Color.argb(127 ,0,0,0));
            }

            int typeLink = l.typeLink;

            float nodeAX = nA.x;
            float nodeAY = nA.y;
            float nodeBX = nB.x;
            float nodeBY = nB.y;

            float lengthArrow = rad * 1.5f;
            float angleArrow = (float) (Math.PI / 25);

            float sideB = Math.abs(nodeAY - nodeBY);
            float sideC = Math.abs(nodeAX - nodeBX);
            float sideA = (float) Math.sqrt(sideB * sideB + sideC * sideC);

            float angle = (float) Math.acos(sideC / sideA);

            float newNodeAX = (float) Math.cos(angle);
            float newNodeAY = (float) Math.sin(angle);

            float newNodeBX = -newNodeAX;
            float newNodeBY = -newNodeAY;

            float arrowNodeAX1 = 0;
            float arrowNodeAY1 = 0;
            float arrowNodeAX2 = 0;
            float arrowNodeAY2 = 0;

            float arrowNodeBX1 = 0;
            float arrowNodeBY1 = 0;
            float arrowNodeBX2 = 0;
            float arrowNodeBY2 = 0;

            switch (typeLink)
            {
                case 1:
                    arrowNodeBX1 = (float) -Math.cos(angle + angleArrow);
                    arrowNodeBY1 = (float) -Math.sin(angle + angleArrow);
                    arrowNodeBX2 = (float) -Math.cos(angle - angleArrow);
                    arrowNodeBY2 = (float) -Math.sin(angle - angleArrow);
                    break;
                case 2:
                    arrowNodeAX1 = (float) Math.cos(angle + angleArrow);
                    arrowNodeAY1 = (float) Math.sin(angle + angleArrow);
                    arrowNodeAX2 = (float) Math.cos(angle - angleArrow);
                    arrowNodeAY2 = (float) Math.sin(angle - angleArrow);
                    break;
                case 3:
                    arrowNodeAX1 = (float) Math.cos(angle + angleArrow);
                    arrowNodeAY1 = (float) Math.sin(angle + angleArrow);
                    arrowNodeAX2 = (float) Math.cos(angle - angleArrow);
                    arrowNodeAY2 = (float) Math.sin(angle - angleArrow);

                    arrowNodeBX1 = (float) -Math.cos(angle + angleArrow);
                    arrowNodeBY1 = (float) -Math.sin(angle + angleArrow);
                    arrowNodeBX2 = (float) -Math.cos(angle - angleArrow);
                    arrowNodeBY2 = (float) -Math.sin(angle - angleArrow);
                    break;
            }

            if (nodeAX > nodeBX)
            {
                newNodeAX *= -1;
                newNodeBX *= -1;

                arrowNodeAX1 *= -1;
                arrowNodeAX2 *= -1;
                arrowNodeBX1 *= -1;
                arrowNodeBX2 *= -1;
            }

            if (nodeAY > nodeBY)
            {
                newNodeAY *= -1;
                newNodeBY *= -1;

                arrowNodeAY1 *= -1;
                arrowNodeAY2 *= -1;
                arrowNodeBY1 *= -1;
                arrowNodeBY2 *= -1;
            }

            newNodeAX = newNodeAX * rad + nodeAX;
            newNodeAY = newNodeAY * rad + nodeAY;

            newNodeBX = newNodeBX * rad + nodeBX;
            newNodeBY = newNodeBY * rad + nodeBY;

            canvas.drawLine(newNodeAX, newNodeAY, newNodeBX, newNodeBY, p);

            arrowNodeAX1 = arrowNodeAX1 * lengthArrow + nodeAX;
            arrowNodeAY1 = arrowNodeAY1 * lengthArrow + nodeAY;
            arrowNodeAX2 = arrowNodeAX2 * lengthArrow + nodeAX;
            arrowNodeAY2 = arrowNodeAY2 * lengthArrow + nodeAY;

            arrowNodeBX1 = arrowNodeBX1 * lengthArrow + nodeBX;
            arrowNodeBY1 = arrowNodeBY1 * lengthArrow + nodeBY;
            arrowNodeBX2 = arrowNodeBX2 * lengthArrow + nodeBX;
            arrowNodeBY2 = arrowNodeBY2 * lengthArrow + nodeBY;

            switch (typeLink)
            {
                case 1:
                    canvas.drawLine(newNodeBX, newNodeBY, arrowNodeBX1, arrowNodeBY1, p);
                    canvas.drawLine(newNodeBX, newNodeBY, arrowNodeBX2, arrowNodeBY2, p);
                    break;
                case 2:
                    canvas.drawLine(newNodeAX, newNodeAY, arrowNodeAX1, arrowNodeAY1, p);
                    canvas.drawLine(newNodeAX, newNodeAY, arrowNodeAX2, arrowNodeAY2, p);
                    break;
                case 3:
                    canvas.drawLine(newNodeAX, newNodeAY, arrowNodeAX1, arrowNodeAY1, p);
                    canvas.drawLine(newNodeAX, newNodeAY, arrowNodeAX2, arrowNodeAY2, p);

                    canvas.drawLine(newNodeBX, newNodeBY, arrowNodeBX1, arrowNodeBY1, p);
                    canvas.drawLine(newNodeBX, newNodeBY, arrowNodeBX2, arrowNodeBY2, p);
                    break;
            }

            canvas.drawText(linkValue,(nodeAX + nodeBX) * 0.5f, (nodeAY + nodeBY) * 0.5f + rad, p_text);
        }

        //Обход всех узлов
        for(int i = 0; i < gr.nodes.size(); i ++)
        {
            //Получение узла
            Node n = gr.nodes.get(i);
            float nodeX = n.x;
            float nodeY = n.y;
            String captionNode = n.caption;

            p.setStyle(Paint.Style.FILL);

            if (n.ID == selected1)
            {
                p.setColor(Color.argb(50,127, 0 ,255));
                canvas.drawText("1", nodeX, nodeY, p_text);

            }
            else if(n.ID == selected2)
            {
                p.setColor(Color.argb(50,255, 0,50));
                canvas.drawText("2", nodeX, nodeY, p_text);
            }
            else
            {
                p.setColor(Color.argb(50,0, 127,255));
            }

            canvas.drawCircle(nodeX, nodeY, rad, p);

            p.setStyle(Paint.Style.STROKE);

            if (n.ID == selected1)
            {
                p.setColor(Color.rgb(127, 0 ,255));
            }
            else if(n.ID == selected2)
            {
                p.setColor(Color.rgb(255, 0,50));
            }
            else
            {
                p.setColor(Color.rgb(0, 127,255));
            }

            canvas.drawCircle(nodeX, nodeY, rad, p);
            canvas.drawText(captionNode, nodeX, nodeY + rad * 1.7f, p_text);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        //Получение типа нажатия
        int action = event.getAction();

        //Получение места нажатия
        float actionX = event.getX();
        float actionY = event.getY();

        //Определение действий
        switch (action)
        {
            //Нажатие на экран
            case MotionEvent.ACTION_DOWN:
                lastHitNode = get_node_at_xy(actionX, actionY);

                if (lastHitNode < 0)
                {
                    selected1 = -1;
                    selected2 = -1;

                    lastHitLink = get_link_at_xy(actionX, actionY);

                    if (lastHitLink < 0)
                    {
                        selectedLink = -1;
                    }
                    else
                    {
                        selectedLink = lastHitLink;
                    }
                }
                else
                {
                    selectedLink = -1;

                    if (selected1 < 0)
                    {
                        selected1Check = false;
                    }

                    if (selected1 >= 0 && selected1Check)
                    {
                        selected2 = lastHitNode;
                        selected1Check = false;
                    }
                    else
                    {
                        selected1 = lastHitNode;
                        selected1Check = true;
                    }
                }

                last_x = actionX;
                last_y = actionY;

                invalidate();
                return true;

            //Передвижение по экрану
            case MotionEvent.ACTION_MOVE:
                if (lastHitNode >= 0)
                {
                    Node n = gr.getNode(lastHitNode);
                    n.x += actionX - last_x;
                    n.y += actionY - last_y;
                    invalidate();
                }

                last_x = actionX;
                last_y = actionY;
                return true;
        }

        return super.onTouchEvent(event);
    }

    private int get_node_at_xy(float x, float y)
    {
        //Определение узла на который нажали
        for(int i = gr.nodes.size() - 1; i >= 0; i--)
        {
            if (i == -1) return -1;

            Node n = gr.nodes.get(i);
            float dx = x - n.x;
            float dy = y - n.y;

            if (dx * dx + dy * dy <= rad * rad)
            {
                return n.ID;
            }
        }

        return -1;
    }

    private int get_link_at_xy(float x, float y)
    {
        //Определение узла на который нажали
        for(int i = 0; i < gr.links.size(); i++)
        {
            if (i == -1) return -1;

            Link l = gr.links.get(i);
            Node nA = gr.getNode(l.a);
            Node nB = gr.getNode(l.b);

            float bx = (nA.x + nB.x) * 0.5f;
            float by = (nA.y + nB.y) * 0.5f;

            if (x >= bx - rad && x <= bx + rad && y >= by - rad && y <= by + rad)
                return l.ID;
        }

        return -1;
    }

    public void addNode()
    {
        gr.addNode(100.0f, 100.0f);
        lastHitNode--;

        invalidate();
    }

    public void setCaptionNode(String caption)
    {
        if (lastHitNode < 0) return;

        gr.getNode(lastHitNode).caption = caption;
        invalidate();
    }

    public void deleteNode()
    {
        if (lastHitNode < 0) return;

        for (int i = 0; i < gr.links.size(); i++)
        {
            Link l = gr.links.get(i);
            Node nA = gr.getNode(l.a);
            Node nB = gr.getNode(l.b);

            if (nA.ID == lastHitNode || nB.ID == lastHitNode)
            {
                gr.deleteLink(l.ID);
                i--;
            }
        }

        gr.deleteNode(lastHitNode);

        if (selected1 == lastHitNode)
        {
            selected1 = selected2;
            lastHitNode = selected1;
            selected2 = -1;
        }

        if (selected2 == lastHitNode)
        {
            lastHitNode = selected1;
        }

        invalidate();
    }

    public void addLink(int typeLink)
    {
        if (selected1 < 0) return;
        if (selected2 < 0) return;

        for (int i = 0; i < gr.links.size(); i++)
        {
            Link l = gr.links.get(i);
            int nA = l.a;
            int nB = l.b;

            if (nA == selected1 && nB == selected2 || nA == selected2 && nB == selected1)
            {
                return;
            }
        }

        gr.addLink(selected1, selected2, typeLink);

        invalidate();
    }

    public void setValueLink(float value)
    {
        if (lastHitLink < 0) return;

        gr.getLink(lastHitLink).value = value;
        invalidate();
    }

    public void deleteLink()
    {
        if (selectedLink < 0) return;

        for (int i = 0; i < gr.links.size(); i++)
        {
            Link l = gr.links.get(i);

            if (selectedLink == l.ID)
            {
                gr.deleteLink(selectedLink);
                selectedLink = -1;
            }
        }

        invalidate();
    }

    public void clearGraph()
    {
        gr.deleteAllLinks();
        gr.deleteAllNodes();
        invalidate();
    }
}

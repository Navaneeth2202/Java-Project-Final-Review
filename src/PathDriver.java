import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;


public class PathDriver {

    //creating a JFrame
    JFrame frame;

    //creating General Variables
    public int cells = 40;
    public int delay = 5;
    public int startx = -1;
    public int starty = -1;
    public int finishx = -1;
    public int finishy = -1;
    public int tool = 0;
    public int checks = 0;
    public int length = 0;
    public int curAlg = 0;
    public int WIDTH = 610;
    public final int HEIGHT = 800;
    public final int MSIZE = 590;
    public int CSIZE = MSIZE / cells;


    public String[] tools = {"Start", "Finish", "Wall", "Eraser"};

    public boolean solving = false;

    Node[][] map;
    Algorithm Alg = new Algorithm(this);

    JLabel toolL = new JLabel("Toolbox");
    JLabel checkL = new JLabel("Checks: " + checks);
    JLabel lengthL = new JLabel("Path Length: " + length);

    JButton searchB = new JButton("Start Search");
    JButton resetB = new JButton("Reset");
    JButton clearMapB = new JButton("Clear Map");

    JComboBox toolBx = new JComboBox(tools);

    JPanel toolP = new JPanel();

    Map canvas;

    Initialize i = new Initialize(this);

    Border blackline = BorderFactory.createLineBorder(Color.black);

    Color wall = new Color(40, 44, 52);
    Color search = new Color(0, 185, 189);
    Color finishc = new Color(255, 65, 151);
    Color startc = new Color(239, 71, 111);
    Color route = new Color(255, 209, 102);

    public static void main(String[] args) {

        new PathDriver();
    }

    //constructor
    public PathDriver() {
        clearMap(); //clears the map
        i.initialize();//initializes the buttons and their positions
    }

    //clear map
    public void clearMap() {
        finishx = -1;
        finishy = -1;
        startx = -1;
        starty = -1;
        map = new Node[cells][cells];
        for (int x = 0; x < cells; x++) {
            for (int y = 0; y < cells; y++) {
                map[x][y] = new Node(3, x, y);
            }
        }
        reset(); //resets some variables
    } //end of clearMap()

    //reset method : mentioned above
    public void reset() {
        solving = false;
        length = 0;
        checks = 0;
    }//end of reset()

    //reset the map: clear output but walls and start stop remain same
    public void resetMap() {
        for (int x = 0; x < cells; x++) {
            for (int y = 0; y < cells; y++) {
                Node current = map[x][y];
                if (current.getType() == 4 || current.getType() == 5)
                    map[x][y] = new Node(3, x, y);
            }
        }

        if (startx > -1 && starty > -1) {    //RESET THE START AND FINISH
            map[startx][starty] = new Node(0, startx, starty);
            map[startx][starty].setJumps(0);
        }

        if (finishx > -1 && finishy > -1)
            map[finishx][finishy] = new Node(1, finishx, finishy);

        reset();    //RESET SOME VARIABLES
    }//end of resetMap()

    //start search function
    public void startSearch() {
        if (solving) {
            Alg.Dijkstra();
        }
        pause(); //pause state
    }

    //pause function
    public void pause() {
        int i = 0;
        while (!solving) {
            i++;
            if (i > 500)
                i = 0;
            try {
                Thread.sleep(1);
            } catch (Exception e) {
            }
        }
        startSearch();
    }

    //delay function
    public void delay() {
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
        }
    }

    //update function: updates the elements of the GUI
    public void Update() {
        CSIZE = MSIZE / cells;
        canvas.repaint();
        lengthL.setText("Path Length: " + length);
        checkL.setText("Checks: " + checks);
    }

    class Map extends JPanel implements MouseListener, MouseMotionListener {

        public Map() {
            addMouseListener(this);
            addMouseMotionListener(this);
        }

        public void paintComponent(Graphics g) {    //REPAINT
            super.paintComponent(g);
            for (int x = 0; x < cells; x++) {    //PAINT EACH NODE IN THE GRID
                for (int y = 0; y < cells; y++) {
                    switch (map[x][y].getType()) {
                        case 0:
                            g.setColor(startc);
                            break;
                        case 1:
                            g.setColor(finishc);
                            break;
                        case 2:
                            g.setColor(wall);
                            break;
                        case 3:
                            g.setColor(Color.WHITE);
                            break;
                        case 4:
                            g.setColor(search);
                            break;
                        case 5:
                            g.setColor(route);
                            break;
                    }
                    g.fillRect(x * CSIZE, y * CSIZE, CSIZE, CSIZE); //gives fill color to the rectangles
                    //g.setColor(search);
                    g.drawRect(x * CSIZE, y * CSIZE, CSIZE, CSIZE); //draws/makes the grid box visible

                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            try {
                int x = e.getX() / CSIZE;
                int y = e.getY() / CSIZE;
                Node current = map[x][y];
                if ((tool == 2 || tool == 3) && (current.getType() != 0 && current.getType() != 1))
                    current.setType(tool);
                Update();
            } catch (Exception z) {
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            resetMap();    //RESET THE MAP WHENEVER CLICKED
            try {
                int x = e.getX() / CSIZE;    //GET THE X AND Y OF THE MOUSE CLICK IN RELATION TO THE SIZE OF THE GRID
                int y = e.getY() / CSIZE;
                Node current = map[x][y];
                switch (tool) {
                    case 0: {    //START NODE
                        if (current.getType() != 2) {    //IF NOT WALL
                            if (startx > -1 && starty > -1) {    //IF START EXISTS SET IT TO EMPTY
                                map[startx][starty].setType(3);
                                map[startx][starty].setJumps(-1);
                            }
                            current.setJumps(0);
                            startx = x;    //SET THE START X AND Y
                            starty = y;
                            current.setType(0);    //SET THE NODE CLICKED TO BE START
                        }
                        break;
                    }
                    case 1: {//FINISH NODE
                        if (current.getType() != 2) {    //IF NOT WALL
                            if (finishx > -1 && finishy > -1)    //IF FINISH EXISTS SET IT TO EMPTY
                                map[finishx][finishy].setType(3);
                            finishx = x;    //SET THE FINISH X AND Y
                            finishy = y;
                            current.setType(1);    //SET THE NODE CLICKED TO BE FINISH
                        }
                        break;
                    }
                    default:
                        if (current.getType() != 0 && current.getType() != 1)
                            current.setType(tool);
                        break;
                }
                Update();
            } catch (Exception z) {
            }    //EXCEPTION HANDLER
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
    }


    class Node {

        // 0 = start, 1 = finish, 2 = wall, 3 = empty, 4 = checked, 5 = finalpath
        public int cellType = 0;
        public int jumps;
        public int x;
        public int y;
        public int lastX;
        public int lastY;
        public double dToEnd = 0;

        //constructor
        public Node(int type, int x, int y) {
            cellType = type;
            this.x = x;
            this.y = y;
            jumps = -1;
        }

        //get methods
        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getLastX() {
            return lastX;
        }

        public int getLastY() {
            return lastY;
        }

        public int getType() {
            return cellType;
        }

        public int getJumps() {
            return jumps;
        }

        //set methods
        public void setType(int type) {
            cellType = type;
        }

        public void setLastNode(int x, int y) {
            lastX = x;
            lastY = y;
        }

        public void setJumps(int jumps) {
            this.jumps = jumps;
        }
    }

}

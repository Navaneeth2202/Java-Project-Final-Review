import java.util.ArrayList;


public class Algorithm {

    private PathDriver p;
    public Algorithm(PathDriver p){
        this.p=p;
    }

    public void Dijkstra() {
        ArrayList<PathDriver.Node> priority = new ArrayList<PathDriver.Node>();
        priority.add(p.map[p.startx][p.starty]);
        while (p.solving) {
            if (priority.size() <= 0) {
                p.solving = false;
                break;
            }
            int jumps = priority.get(0).getJumps() + 1;
            ArrayList<PathDriver.Node> explored = exploreNeighbors(priority.get(0), jumps);
            if (explored.size() > 0) {
                priority.remove(0);
                priority.addAll(explored);
                p.Update();
                p.delay();
            } else {
                priority.remove(0);
            }
        }
    }

    public ArrayList<PathDriver.Node> exploreNeighbors(PathDriver.Node current, int jumps) {
        ArrayList<PathDriver.Node> explored = new ArrayList<PathDriver.Node>();
        for (int a = -1; a <= 1; a++) {
            for (int b = -1; b <= 1; b++) {
                int xbound = current.getX() + a;
                int ybound = current.getY() + b;
                if ((xbound > -1 && xbound < p.cells) && (ybound > -1 && ybound < p.cells)) {
                    if ((a == -1 && b == -1) || (a == 1 && b == -1) || (a == -1 && b == 1) || (a == 1 && b == 1)) {

                    } else {
                        PathDriver.Node neighbor = p.map[xbound][ybound];
                        if ((neighbor.getJumps() == -1 || neighbor.getJumps() > jumps) && neighbor.getType() != 2) {
                            explore(neighbor, current.getX(), current.getY(), jumps);
                            explored.add(neighbor);
                        }
                    }
                }


            }
        }
        return explored;
    }
    public void explore(PathDriver.Node current, int lastx, int lasty, int jumps) {
        if (current.getType() != 0 && current.getType() != 1)
            current.setType(4);
        current.setLastNode(lastx, lasty);
        current.setJumps(jumps);
        p.checks++;
        if (current.getType() == 1) {
            backtrack(current.getLastX(), current.getLastY(), jumps);
        }
    }

    public void backtrack(int lx, int ly, int jumps) {
        p.length = jumps;
        while (jumps > 1) {
            PathDriver.Node current = p.map[lx][ly];
            current.setType(5);
            lx = current.getLastX();
            ly = current.getLastY();
            jumps--;
        }
        p.solving = false;
    }
}

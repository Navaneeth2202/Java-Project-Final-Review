import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class Initialize {
    private PathDriver p;

    public Initialize(PathDriver p){
        this.p=p;
    }

    public void initialize() {
        p.frame = new JFrame();
        p.frame.setVisible(true);
        p.frame.setResizable(false);
        p.frame.setSize(p.WIDTH, p.HEIGHT);
        p.frame.setTitle("Path Finding Algorithm: Java Project");
        p.frame.setLocationRelativeTo(null);
        p.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        p.frame.getContentPane().setLayout(null);

        p.toolP.setBorder(BorderFactory.createTitledBorder(p.blackline, "Controls"));
        int gap = 25;
        int extra = 45;
        int gaps = 20;
        int gapss = 50;

        //panel
        p.toolP.setLayout(null);
        p.toolP.setBounds(10, 600, 570, 150);

        //search button
        p.searchB.setBounds(40, gap, 120, 25);
        p.toolP.add(p.searchB);
        gap += extra;

        //reset button
        p.resetB.setBounds(40, gap, 120, 25);
        p.toolP.add(p.resetB);
        gap += extra;

        //clear map button
        p.clearMapB.setBounds(40, gap, 120, 25);
        p.toolP.add(p.clearMapB);

        //tool text
        p.toolL.setBounds(220, gaps, 120, 25);
        p.toolP.add(p.toolL);
        gaps += 25;

        //tool drop down
        p.toolBx.setBounds(220, gaps, 120, 25);
        p.toolP.add(p.toolBx);
        gaps += extra;

        //checks
        p.checkL.setBounds(400, gapss, 100, 25);
        p.toolP.add(p.checkL);
        gapss += extra;

        //length
        p.lengthL.setBounds(400, gapss, 100, 25);
        p.toolP.add(p.lengthL);
        gapss += extra;

        p.frame.getContentPane().add(p.toolP); //adds tool Panel to the frame

        //the canvas for the grid
        p.canvas = p.new Map();
        p.canvas.setBounds(10, 10, p.MSIZE + 1, p.MSIZE + 1);
        p.frame.getContentPane().add(p.canvas);


        //ACTION LISTENERS
        p.searchB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p.reset();
                if ((p.startx > -1 && p.starty > -1) && (p.finishx > -1 && p.finishy > -1))
                    p.solving = true;
            }
        });

        p.resetB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p.resetMap();
                p.Update();
            }
        });

        p.clearMapB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p.clearMap();
                p.Update();
            }
        });

        p.toolBx.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                p.tool = p.toolBx.getSelectedIndex();
            }
        });

        p.startSearch();

    }

}

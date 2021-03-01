import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/******************************************************************************
 *  Compilation:  javac Universe.java
 *  Execution:    java Universe dt input.txt
 *  Dependencies: Body.java Vector.java StdIn.java StdDraw.java
 *  Datafiles:    http://www.cs.princeton.edu/introcs/34nbody/2body.txt
 *                http://www.cs.princeton.edu/introcs/34nbody/3body.txt
 *                http://www.cs.princeton.edu/introcs/34nbody/4body.txt
 *                http://www.cs.princeton.edu/introcs/34nbody/2bodyTiny.txt
 *
 *  This data-driven program simulates motion in the universe defined
 *  by the standard input stream, increasing time at the rate on the
 *  command line.
 *
 *  %  java Universe 25000 4body.txt
 *
 *
 ******************************************************************************/

public class Universe {
    public  int n;             // number of bodies
    public  Body[] bodies;     // array of n bodies


    // read universe from standard input
    public Universe(String fname) {
        try {
            Scanner in = new Scanner(new FileReader(fname));
            n = Integer.parseInt(in.next());
            // number of bodies
            System.out.println("n=" + n);

            // the set scale for drawing on screen
            double radius = Double.parseDouble(in.next());
            System.out.println("radius=" + radius);
            StdDraw.setXscale(-radius, +radius);
            StdDraw.setYscale(-radius, +radius);

            // read in the n bodies
            bodies = new Body[n];
            for (int i = 0; i < n; i++) {
                System.out.println("i=" + i);
                double rx = Double.parseDouble(in.next());
                double ry = Double.parseDouble(in.next());
                double vx = Double.parseDouble(in.next());
                double vy = Double.parseDouble(in.next());
                double mass = Double.parseDouble(in.next());
                double[] position = {rx, ry};
                double[] velocity = {vx, vy};
                Vector r = new Vector(position);
                Vector v = new Vector(velocity);
                bodies[i] = new Body(r, v, mass);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // increment time by dt units, assume forces are constant in given interval
    public void increaseTime(double dt) {

        // initialize the forces to zero
        Vector[] f = new Vector[n];
        for (int i = 0; i < n; i++) {
            f[i] = new Vector(new double[2]);
        }

        // compute the forces
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    f[i] = f[i].plus(bodies[i].forceFrom(bodies[j]));
                }
            }
        }

        // move the bodies
        for (int i = 0; i < n; i++) {
            bodies[i].move(f[i], dt);
        }
    }

    // draw the n bodies
    public void draw() {
        for (int i = 0; i < n; i++) {
            bodies[i].draw();
        }
    }


    // client to simulate a universe
    // In IntelliJ : Run -> Run... -> Edit configurations -> Program arguments 1000 data/3body.txt
    public static void main(String[] args) {
        Universe newton;
        double dt = Double.parseDouble(args[0]);
        System.out.println("dt=" + dt);
        String fname = args[1];
        newton = new Universe(fname);
        StdDraw.enableDoubleBuffering();
        int pause = 0;
        while (true) {
            StdDraw.clear();
            newton.increaseTime(dt);
            newton.draw();
            StdDraw.show();
            StdDraw.pause(pause);
        }
    }
}

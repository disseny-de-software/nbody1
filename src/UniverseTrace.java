import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/******************************************************************************
 *  Compilation:  javac UniverseTrace.java
 *  Execution:    java UniverseTrace dt  input.txt
 *  Dependencies: Body.java Vector.java StdIn.java StdDraw.java
 *  Datafiles:    http://www.cs.princeton.edu/introcs/34nbody/2body.txt
 *                http://www.cs.princeton.edu/introcs/34nbody/3body.txt
 *                http://www.cs.princeton.edu/introcs/34nbody/4body.txt
 *                http://www.cs.princeton.edu/introcs/34nbody/2bodyTiny.txt
 *
 *  Universe.java, modified to leave a trace of the bodies.
 *  Also, draw bodies[1] and bodies[2] at twice the radius of the others.
 *  Also, pauses for 50ms between updates.
 *
 *  %  java UniverseTrace 25000  3body.txt
 *
 *
 ******************************************************************************/

public class UniverseTrace {
    private int n;             // number of bodies
    private Body[] bodies;     // array of n bodies

    // read universe from standard input
    public UniverseTrace(String fname) {
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
            // if (i == 1 || i == 2) bodies[i].draw(0.05);
            bodies[i].draw(0.025);
        }
    } 


    // client to simulate a universe
    // In IntelliJ : Run -> Run... -> Edit configurations -> Program arguments 10000 data/3body.txt
    public static void main(String[] args) {
        double dt = Double.parseDouble(args[0]);
        System.out.println("dt=" + dt);
        String fname = args[1];

        StdDraw.setCanvasSize(700, 700);
        UniverseTrace newton = new UniverseTrace(fname);
        StdDraw.clear(StdDraw.GRAY);
        StdDraw.enableDoubleBuffering();

        while (true) {
            StdDraw.setPenColor(StdDraw.WHITE);
            newton.draw(); 
            newton.increaseTime(dt); 
            StdDraw.setPenColor(StdDraw.BLACK);
            newton.draw(); 
            StdDraw.show();
            StdDraw.pause(10);
        } 
    } 
}

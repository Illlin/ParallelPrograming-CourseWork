import java.awt.Color ;
import java.awt.image.BufferedImage ;

import javax.imageio.ImageIO;

import java.io.File ;

public class ParallelMandelbrot extends Thread {

    final static int N = 4096 ;
    final static int CUTOFF = 1000 ;

    static int threads = 12;

    static int [] [] set = new int [N] [N] ;

    public static void main(String [] args) throws Exception {

        // Calculate set

        long startTime = System.currentTimeMillis();

        ParallelMandelbrot[] pool = new ParallelMandelbrot[threads];

        for (int i = 0 ; i < threads ; i++){
            pool[i] = new ParallelMandelbrot(i);
            pool[i].start();
        }

        for (int i = 0 ; i < threads ; i++){
            pool[i].join();
        }

        /*
        ParallelMandelbrot thread0 = new ParallelMandelbrot(0) ;
        ParallelMandelbrot thread1 = new ParallelMandelbrot(1) ;

        thread0.start() ;
        thread1.start() ;

        thread0.join() ;
        thread1.join() ;*/

        long endTime = System.currentTimeMillis();

        System.out.println("Calculation completed in " +
                (endTime - startTime) + " milliseconds");

        // Plot image

        BufferedImage img = new BufferedImage(N, N,
                BufferedImage.TYPE_INT_ARGB) ;

        // Draw pixels

        for (int i = 0 ; i < N ; i++) {
            for (int j = 0 ; j < N/2 ; j++) {

                int k = set [i] [j] ;

                float level ;
                if(k < CUTOFF) {
                    level = (float) k / CUTOFF ;
                }
                else {
                    level = 0 ;
                }
                Color c = new Color(0, level, 0) ;  // Green
                img.setRGB(i, j, c.getRGB()) ;
                img.setRGB(i, N-j-1, c.getRGB()) ;
            }
        }


        // Print file

        ImageIO.write(img, "PNG", new File("Mandelbrot.png"));
    }

    int me ;

    public ParallelMandelbrot(int me) {
        this.me = me;
    }

    public void run() {

        int begin, end ;

        begin = (me)*(N/threads);
        end = (begin + N/threads);
        //System.out.println("Thread" + me);
        //for(int i = begin ; i < end ; i++) {

        for(int i = me ; i < N ; i+=threads) {
            for (int j = 0; j < N/2; j++) {

                double cr = (4.0 * i - 2 * N) / N;
                double ci = (4.0 * j - 2 * N) / N;

                double zr = cr, zi = ci;

                int k = 0;
                while (k < CUTOFF && zr * zr + zi * zi < 4.0) {

                    // z = c + z * z

                    double newr = cr + zr * zr - zi * zi;
                    double newi = ci + 2 * zr * zi;

                    zr = newr;
                    zi = newi;

                    k++;
                }

                set[i][j] = k;
            }
        }
    }

}
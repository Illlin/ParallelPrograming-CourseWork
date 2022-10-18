public class ParallelPi extends Thread {

    public static void main(String[] args) throws Exception {

        long startTime = System.nanoTime();
        
        // Setting up threads
        int numThreads = 2;
        ParallelPi[] threads = new ParallelPi[numThreads];

        // Make and start threads
        float stepCount = numSteps / numThreads;
        float current = 0;

        for (int i = 0; i < numThreads; i++){
            threads[i] = new ParallelPi();
            threads[i].begin = (int) current;
            current += stepCount;
            threads[i].end = (int) current;

            threads[i].start();
        }
        // Join Threads
        for (int i = 0; i < numThreads; i++){
            threads[i].join();
        }

        long endTime = System.nanoTime();

        int sum = 0;
        for (int i = 0; i < numThreads; i++){
            sum += threads[i].sum;
        }
        double pi = step * (sum) ;
      
        System.out.println("Value of pi: " + pi);

        System.out.println("Calculated in " +
                           (float)(endTime - startTime)/1000000 + " milliseconds");
    }

    static int numSteps = 10000000;
  
    static double step = 1.0 / (double) numSteps;

    double sum ;  
    int begin, end ;

    public void run() {

        sum = 0.0 ;

        for(int i = begin ; i < end ; i++){
            double x = (i + 0.5) * step ;
            sum += 4.0 / (1.0 + x * x);
        }
    }
}

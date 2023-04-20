import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Class {
    public static BlockingQueue<String> queue_a = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queue_b = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queue_c = new ArrayBlockingQueue<>(100);
    public static void main(String[] args) throws InterruptedException {
     Thread generatorText = new Thread(new Runnable() {
         @Override
         public void run() {
             for(int i = 0; i < 10_000; i++) {
                 String text = generateText("abc", 100_000);
                 try {
                     queue_a.put(text);
                     queue_b.put(text);
                     queue_c.put(text);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
         }
     });
     generatorText.start();
     Thread checkingForA = new Thread(new Runnable() {
         @Override
         public void run() {
             char letter = 'a';
             int max_a = findMaxCharCount(queue_a, letter);
             System.out.println("Максимальное количество: " + letter + " - " + max_a);
         }
     });
     checkingForA.start();
     Thread checkingForB = new Thread(new Runnable() {
         @Override
         public void run() {
             char letter = 'b';
             int max_b = findMaxCharCount(queue_b, letter);
             System.out.println("Максимальное количество: " + letter + " - " + max_b);
         }
     });
     checkingForB.start();
     Thread checkingForC = new Thread(new Runnable() {
         @Override
         public void run() {
             char letter = 'c';
             int max_c = findMaxCharCount(queue_c, letter);
             System.out.println("Максимальное количество: " + letter + " - " + max_c);
         }
     });
     checkingForC.start();
     checkingForA.join();
     checkingForB.join();
     checkingForC.join();
    }
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int findMaxCharCount(BlockingQueue<String> queue, char letter) {
        int count = 0;
        int max = 0;
        String text;
        try {
            for (int i = 0; i < 10000; i++) {
                text = queue.take();
                for (char c : text.toCharArray()) {
                    if (c == letter) count++;
                }
                if (count > max) max = count;
                count = 0;
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + "was interrupted");
            return -1;
        }
        return max;
    }
}

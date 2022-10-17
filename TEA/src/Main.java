import java.nio.charset.StandardCharsets;

public class Main {
    //method to encrypt input with key(chance to change datatype as code is implemented)
    int delta = 0x9e3779b9;
    int sum = 0;
    int key[] = new int[]{4, 4, 3 ,6};
    int padding[] = new int[]{5004,38349};
    int L,R;
    public static  long encrypt(){
        long MASK32 = (1l << 32) - 1;
        int delta = 0x9e3779b9;
        int sum = 0;
        int key[] = new int[]{4, 4, 3 ,6};
        String test = "this is a test";
        byte[] testBytes = test.getBytes(StandardCharsets.UTF_8);
        int padding[] = new int[]{5004,38349};
        int L,R;
        L = (int) testBytes[0] >>> 32;
        R = testBytes[1];

        for (int i=1; i<=32;i++){
            sum += delta;
            L += ( ((R << 4) + (key[0])) ^ (R + sum) ^ ((R >> 5) + (key[1])));
            R += ( ((L << 4) + (key[2])) ^ (L + sum) ^ ((L >> 5) + (key[3])));
        }
        return L  | R;
    }
    //method to decrypt input with key(chance to change datatype as code is implemented)
    public static void decrypt(String input, long key ){

    }

    public static void main(String[] args) {
        long n =0;
        n = encrypt();
        System.out.println(n);
    }
}
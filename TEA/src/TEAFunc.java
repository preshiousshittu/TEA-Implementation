import java.nio.charset.StandardCharsets;

public class TEAFunc
{
    private final int delta = 0x9e3779b9;
    private int[] key;
    private final int MASK32 = 0xFFFFFFFF;

    public TEAFunc(int[] keyIn)
    {
        key = keyIn;
    }

    public int[] getKey()
    {
        return key;
    }

    public void setKey(int[] newKey)
    {
        key = newKey;
    }

    public int[] encrypt(String textToEncrypt)
    {
        int sum = 0;
        byte[] testBytes = textToEncrypt.getBytes(StandardCharsets.UTF_8);
        int padding[] = new int[]{5004,38349};
        int L = (int) (testBytes[0] >>> 32);
        int R = (int) (testBytes[1] >>> 32);
        System.out.println(L);
        System.out.println(R);
        for (int i=1; i<=32;i++){
            sum += delta;
            L += ( (((R << 4) & MASK32) + (key[0])) ^ (R + sum) ^ ((R >> 5) + (key[1])));
            R += ( (((L << 4) & MASK32) + (key[2])) ^ (L + sum) ^ ((L >> 5) + (key[3])));
        }
        return new int[]{L, R};
    }

    public int[] decrypt(int[] toDecrypt)
    {
        int sum = (delta << 5) & MASK32;
        int L = toDecrypt[0];
        int R = toDecrypt[1];
        for(int i = 0; i < 31; i++)
        {
            R -= ( (((L << 4) & MASK32) + (key[2])) ^ (L + sum) ^ ((L >> 5) + (key[3])));
            L -= ( (((R << 4) & MASK32) + (key[0])) ^ (R + sum) ^ ((R >> 5) + (key[1])));
            sum -= delta;
        }
        return new int[]{L, R};
    }
}

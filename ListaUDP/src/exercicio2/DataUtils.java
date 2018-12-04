package exercicio2;

public class DataUtils {
    
    public static void copyPartToArray(byte[] src, byte[] dest, int pos) {
        System.arraycopy(src, 0, dest, pos, src.length);
    }
    
    public static void copyPartFromArray(byte[] src, int from, byte[] dest) {
        System.arraycopy(src, from, dest, 0, dest.length);
    }
}

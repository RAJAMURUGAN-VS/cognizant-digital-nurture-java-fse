public class MathHelper {

    public int multiply(int a, int b) {
        return a * b;
    }

    public Double divide(int a, int b) {
        if (b == 0) return null;
        return (double) a / b;
    }

    public boolean isEven(int n) {
        return n % 2 == 0;
    }
}

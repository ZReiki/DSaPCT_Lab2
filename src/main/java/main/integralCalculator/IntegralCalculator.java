package main.integralCalculator;

import main.function.Function;

import java.util.concurrent.Callable;
import java.util.stream.IntStream;

public class IntegralCalculator implements Callable<Number> {
    private final double a, b;
    private final int n;
    private final Function f;

    public IntegralCalculator(double a, double b, int n, Function f) {
        this.a = a;
        this.b = b;
        this.n = n;
        this.f = f;
    }

    @Override
    public Number call() {
        double h = (b - a) / n;
        return (IntStream.range(1, n).mapToDouble(i -> a + i * h).map(f).sum() + f.applyAsDouble(b)) * h;
    }
}

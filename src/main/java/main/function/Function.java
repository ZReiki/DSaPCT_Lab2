package main.function;

import java.util.function.DoubleUnaryOperator;

import static java.lang.Math.*;

public class Function implements DoubleUnaryOperator {
    @Override
    public double applyAsDouble(double t) {
        return exp(t) * sqrt(1 - exp(t));
    }
}

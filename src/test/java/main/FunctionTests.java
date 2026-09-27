package main;

import main.function.Function;
import main.integralCalculator.IntegralCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.offset;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FunctionTests {
    private Function f;
    private final double EPS = 1e-6;

    @BeforeEach
    void setUp() {
        f = new Function();
    }

    @ParameterizedTest
    @CsvSource({
            " 0.0,  0.0",
            "-0.2,  0.348581",
            "-0.5,  0.380459",
            "-1.0,  0.292486",
            "-2.0,  0.125845",
            "-3.3,  0.036196",
            "-5.0,  0.006715",
            "-10.0, 0.000045"
    })
    @DisplayName("Перевірка значень підінтегральної функції в контрольних точках")
    void fTest(double x, double expected) {
        double result = f.applyAsDouble(x);
        assertThat(result).isCloseTo(expected, offset(EPS));
    }

    @ParameterizedTest
    @CsvSource({
            "-1.0,  0.0, 100,  0.333377",
            "-1.0,  0.0, 1000, 0.334896",
            " 0.0, -1.0, 100, -0.336302",
            "-0.5,  0.0, 200,  0.164039",
            "-1.1, -3.0, 200, -0.253179",
            "-2.0, -2.2, 300, -0.022965",
            "-5.0, -10.0, 500, -0.006647"
    })
    @DisplayName("Перевірка обчислення інтеграла методом правих прямокутників")
    void icTest(double a, double b, int n, double expected) {
        IntegralCalculator ic = new IntegralCalculator(a, b, n, f);
        double result = ic.call().doubleValue();
        assertThat(result).isCloseTo(expected, offset(EPS));
    }
}
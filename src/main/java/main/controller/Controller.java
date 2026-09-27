package main.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import main.function.Function;
import main.integralCalculator.IntegralCalculator;
import javafx.scene.control.TextField;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller {
    // Inputs
    @FXML public TextField intervalsQuantity, threadsQuantity;
    // Outputs
    @FXML private TextFlow outputText;

    private double sum;
    private int finished;

    public void calculate() {
        double a = -1.0, b = 0.0;
        sum = 0;
        finished = 0;

        int n = Integer.parseInt(intervalsQuantity.getText());
        int threads = Integer.parseInt((threadsQuantity.getText()));

        System.out.println("=== New calculation ===");

        long startTime = System.currentTimeMillis();

        try (ExecutorService executor = Executors.newFixedThreadPool(threads)) {
            double delta = (b - a) / threads;
            for(int i = 0; i < threads; i++){
                double ai = a + i * delta;
                double bi = ai + delta;
                int ni = n / threads;

                final int threadNumber = i;

                executor.submit(() -> {
                    try {
                        sendResult(threadNumber, new IntegralCalculator(ai, bi, ni, new Function()).call());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            try {
                synchronized (this){
                    while(finished < threads) wait();
                }
            } catch(InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        long endTime = System.currentTimeMillis();

        printResult(endTime - startTime);
    }

    public synchronized void sendResult(int i, Number calculationResult) {
        System.out.println("Thread [" + i + "]\t->\tSend result: " + calculationResult);
        sum += calculationResult.doubleValue();
        finished++;
        notifyAll();
    }

    public void printResult(long time){
        outputText.getChildren().clear();
        outputText.getChildren().addAll(new Text("Кількість: інтервалів = " + intervalsQuantity.getText() + "; потоків = " +
                        threadsQuantity.getText() + "\n"),
                new Text("\tРезультат розрахунків: " + String.format("%.6f", sum) + "\n" + "\tЧас виконання розрахунків: " + time + "ms\n"));
    }
}

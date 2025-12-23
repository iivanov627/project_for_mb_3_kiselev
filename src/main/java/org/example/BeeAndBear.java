package org.example;

public class BeeAndBear {
    public static void main(String[] args) throws InterruptedException {
        int numBees = 5;
        int potCapacity = 10;
        int rounds = 5;


        Pot pot = new Pot(potCapacity, rounds);

        Bear bear = new Bear(pot);
        bear.start();
        Bee[] bees = new Bee[numBees];
        for (int i = 0; i < numBees; i++) {
            bees[i] = new Bee(i + 1, pot);
            bees[i].start();
        }

        if (rounds > 0) {
            bear.join();
            for (Bee b : bees) b.interrupt();
        }

        // Ждем завершения всех пчел
        for (Bee b : bees) {
            try {
                b.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Выводим статистику сбора и потребления меда
        pot.printStatistics();
        System.out.println("end");
    }
}

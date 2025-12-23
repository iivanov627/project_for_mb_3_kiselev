package org.example;


import java.util.Random;

class Bee extends Thread {
    private final int id;
    private final Pot pot;
    private final Random rnd = new Random();


    public Bee(int id, Pot pot) {
        this.id = id;
        this.pot = pot;
        setName("Bee-" + id);
    }


    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && !pot.isFinished()) {
            try {
                Thread.sleep(50 + rnd.nextInt(200));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }


            boolean ok = pot.addHoney(id);
            if (!ok) break;
        }


        System.out.printf("Пчела %d завершила работу.\n", id);
    }
}

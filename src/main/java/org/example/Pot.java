package org.example;

class Pot {
    private final int capacity;
    private int current = 0;
    private final int maxRounds;
    private int eatenRounds = 0;
    private boolean finished = false;


    public Pot(int capacity, int maxRounds) {
        this.capacity = capacity;
        this.maxRounds = maxRounds;
    }


    public synchronized boolean addHoney(int beeId) {
        if (finished) return false;

        while (current >= capacity && !finished) {
            try {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            } catch (IllegalMonitorStateException ignored) {}
        }


        if (finished) return false;

        current++;
        System.out.printf("Пчела %d принесла глоток. Горшок: %d/%d\n", beeId, current, capacity);


        if (current >= capacity) {
            System.out.println("Горшок полон — разбудим медведя.");
            notifyAll();
        }


        return true;
    }
    public synchronized boolean eatAll() {
        while (current < capacity && !finished) {
            try {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            } catch (IllegalMonitorStateException ignored) {}
        }


        if (finished) return false;

        System.out.printf("Медведь проснулся и ест весь мед (%d глотков).\n", current);
        current = 0;
        eatenRounds++;

        if (maxRounds > 0 && eatenRounds >= maxRounds) {
            finished = true;
            System.out.println("Медведь достиг лимита просыпаний. Работа завершается.");
            notifyAll();
            return false;
        }

        System.out.printf("Медведь поел (%d раз). Горшок очищен.\n", eatenRounds);
        notifyAll();
        return true;
    }


    public synchronized boolean isFinished() {
        return finished;
    }
}

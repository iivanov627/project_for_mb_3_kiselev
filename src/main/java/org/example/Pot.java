package org.example;

class Pot {
    private final int capacity;
    private int current = 0;
    private final int maxRounds;
    private int eatenRounds = 0;
    private boolean finished = false;
    // Статистика сбора и потребления меда (Honey Statistics Tracker)
    private long totalHoneyCollected = 0;  // Общее количество собранного меда
    private long totalHoneyEaten = 0;      // Общее количество съеденного меда


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
        totalHoneyCollected++;  // Увеличиваем счетчик собранного меда
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
        totalHoneyEaten += current;  // Добавляем к общему количеству съеденного меда
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

    // Методы для получения статистики
    public synchronized long getTotalHoneyCollected() {
        return totalHoneyCollected;
    }

    public synchronized long getTotalHoneyEaten() {
        return totalHoneyEaten;
    }

    public synchronized void printStatistics() {
        System.out.println("\n========== СТАТИСТИКА СБОРА И ПОТРЕБЛЕНИЯ МЕДА ==========");
        System.out.printf("Всего собрано меда пчелами: %d глотков\n", totalHoneyCollected);
        System.out.printf("Всего съедено меда медведем: %d глотков\n", totalHoneyEaten);
        System.out.printf("Количество циклов (просыпаний медведя): %d\n", eatenRounds);
        if (eatenRounds > 0) {
            System.out.printf("Среднее количество меда за цикл: %.2f глотков\n", 
                (double) totalHoneyEaten / eatenRounds);
        }
        System.out.println("========================================================\n");
    }
}

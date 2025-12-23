package org.example;

class Bear extends Thread {
    private final Pot pot;


    public Bear(Pot pot) {
        this.pot = pot;
        setName("Bear");
    }

    @Override
    public void run() {
        while (!pot.isFinished()) {
            boolean ok = pot.eatAll();
            if (!ok) break;

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }


        System.out.println("Медведь завершил работу.");
    }
}

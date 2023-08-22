package io.deeplay.server;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.move.Move;
import io.deeplay.model.player.Player;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerPlayer extends Player {
    private final ConcurrentLinkedQueue<Move> movesQueue;

    public ServerPlayer(Color color) {
        super(color);
        movesQueue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public synchronized Move getMove(Board board, Color playerColor) {
        while (movesQueue.isEmpty()) {
            try {
                wait(); // Ждем, пока не будет доступен ход от клиента
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return movesQueue.poll();
    }

    public synchronized void addMove(Move move) {
        movesQueue.offer(move);
        notify(); // Уведомляем о наличии нового хода
    }

    public void sendMove() {

    }

    private Move getMoveFromQueue() {
        return movesQueue.poll();
    }
}

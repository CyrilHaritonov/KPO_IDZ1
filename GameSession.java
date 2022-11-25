import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class GameSession {
    Player firstPlayer;
    Player secondPlayer;

    Field gamingField;

    Player nowMoves;

    Boolean checkIfLost() {
        List<Field.Cell> freeCells = gamingField.getCellsOfColor("none");
        List<Field.Cell> blackCells = gamingField.getCellsOfColor("черный");
        List<Field.Cell> whiteCells = gamingField.getCellsOfColor("белый");
        Field fieldCopy = gamingField.copyField();
        String whiteMove = fieldCopy.AIMove("белый");
        fieldCopy = gamingField.copyField();
        String blackMove = fieldCopy.AIMove("черный");
        if (freeCells.size() == 0 || blackCells.size() == 0 || whiteCells.size() == 0
                || !Objects.equals(whiteMove, "Противник (лёгкий бот) сделал ход.")
                || !Objects.equals(blackMove, "Противник (лёгкий бот) сделал ход.")) {
            gamingField.printField();
            System.out.println("Игра окончена!");
            determineWinner();
            return false;
        }
        return true;
    }

    void determineWinner() {
        List<Field.Cell> blackCells = gamingField.getCellsOfColor("черный");
        List<Field.Cell> whiteCells = gamingField.getCellsOfColor("белый");
        if (blackCells.size() > whiteCells.size()) {
            System.out.println("Черные выиграли");
        } else if (blackCells.size() == whiteCells.size()) {
            System.out.println("Ничья");
        } else {
            System.out.println("Белые выиграли");
        }
    }

    GameSession(Player firstPlayer, Player secondPlayer, Scanner scanner) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.gamingField = new Field();
        this.nowMoves = firstPlayer;
        while (checkIfLost()){
            gamingField.printField();
            if (nowMoves == firstPlayer) {
                firstPlayer.getNextMove(gamingField, scanner);
                nowMoves = secondPlayer;
            } else {
                secondPlayer.getNextMove(gamingField, scanner);
                nowMoves = firstPlayer;
            }
        }
    }
}

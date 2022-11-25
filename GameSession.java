import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class GameSession {
    private Field gamingField;

    public void setGamingField(Field gamingField) {
        this.gamingField = gamingField;
    }

    public Field getGamingField() {
        return gamingField;
    }

    Boolean checkIfLost() {
        List<Field.Cell> freeCells = gamingField.getCellsOfColor("none");
        List<Field.Cell> blackCells = gamingField.getCellsOfColor("черный");
        List<Field.Cell> whiteCells = gamingField.getCellsOfColor("белый");
        if (freeCells.size() == 0 || blackCells.size() == 0 || whiteCells.size() == 0
                || (gamingField.getPossibleMoves("белый").size() == 0
                && gamingField.getPossibleMoves("черный").size() == 0)) {
            gamingField.printField("белый");
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
        System.out.println("У белых " + whiteCells.size() + " очков, у черных " + blackCells.size() + " очков.");
    }

    private Boolean moveEvaluation(Player player, Player enemy, Scanner scanner) {
        if (player.getClass() != RealPlayer.class) {
            player.getNextMove(this.gamingField, scanner);
            return true;
        }
        Field gamingFieldCopy = gamingField.copyField();
        player.getNextMove(gamingFieldCopy, scanner);
        System.out.print("Ответные ходы противника: ");
        enemy.printPossibleMoves(gamingFieldCopy);
        gamingFieldCopy.printField(enemy.getColor());
        System.out.println("Введите Y чтобы утвердить ход или N чтобы отменить его");
        String answer = scanner.next();
        while (!Objects.equals(answer, "Y") && !Objects.equals(answer, "N")) {
            answer = scanner.next();
        }
        if (Objects.equals(answer, "Y")) {
            this.setGamingField(gamingFieldCopy);
            return true;
        } else {
            System.out.println("Ход отменен");
            return false;
        }
    }
    GameSession(Player firstPlayer, Player secondPlayer, Scanner scanner, Boolean opponentMoveEvaluation) {
        this.gamingField = new Field();
        Player nowMoves = firstPlayer;
        while (checkIfLost()){
            gamingField.printField(nowMoves.getColor());
            if (opponentMoveEvaluation) {
                if (moveEvaluation(nowMoves, nowMoves == firstPlayer ? secondPlayer : firstPlayer, scanner)) {
                    if (nowMoves == firstPlayer) {
                        nowMoves = secondPlayer;
                    } else {
                        nowMoves = firstPlayer;
                    }
                }
            } else {
                nowMoves.getNextMove(gamingField, scanner);
                if (nowMoves == firstPlayer) {
                    nowMoves = secondPlayer;
                } else {
                    nowMoves = firstPlayer;
                }
            }
        }
    }
}

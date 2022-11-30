import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;

public class GameSession {
    private Field gamingField;

    private final Stack<Field> previousFields;

    public Stack<Field> getPreviousFields() {
        return previousFields;
    }

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
        Leaderboard.setLeaderboardValue(Math.max(whiteCells.size(), blackCells.size()));
    }

    private Boolean moveEvaluation(Player player, Player enemy, Scanner scanner) {
        if (player.getClass() != RealPlayer.class) {
            player.getNextMove(this, scanner);
            return true;
        }
        player.getNextMove(this, scanner);
        System.out.print("Ответные ходы противника: ");
        enemy.printPossibleMoves(gamingField);
        gamingField.printField(enemy.getColor());
        System.out.println("Введите Y чтобы утвердить ход или N чтобы отменить его");
        String answer = scanner.next();
        while (!Objects.equals(answer, "Y") && !Objects.equals(answer, "N")) {
            answer = scanner.next();
        }
        if (Objects.equals(answer, "Y")) {
            return true;
        } else {
            this.CancelMove();
            return false;
        }
    }

    void CancelMove() {
        if (previousFields.empty()) {
            System.out.println("Нет предыдущего хода!");
        } else {
            System.out.println("Ход отменен!");
            setGamingField(previousFields.pop());
        }
    }

    GameSession(Player firstPlayer, Player secondPlayer, Scanner scanner, Boolean opponentMoveEvaluation) {
        this.gamingField = new Field();
        this.previousFields = new Stack<>();
        Player nowMoves = firstPlayer;
        while (checkIfLost()) {
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
                if (firstPlayer.getClass() == RealPlayer.class && secondPlayer.getClass() == RealPlayer.class) {
                    nowMoves.getNextMove(this, scanner);
                    if (nowMoves == firstPlayer) {
                        nowMoves = secondPlayer;
                    } else {
                        nowMoves = firstPlayer;
                    }
                } else {
                    if (nowMoves.getNextMove(this, scanner)) {
                        if (nowMoves == firstPlayer) {
                            nowMoves = secondPlayer;
                        } else {
                            nowMoves = firstPlayer;
                        }
                    }
                }
            }
        }
    }
}

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GameSession {
    class Cell {
        String color;
        double s;
        double ss;
        int x;
        int y;
        Cell(String color, double s, double ss, int x, int y) {
            this.color = color;
            this.s = s;
            this.ss = ss;
            this.x = x;
            this.y = y;
        }
    }

    class CellWithR{
        double R = 0;
        final Cell cell;
        CellWithR (Cell cell) {
            this.R = R;
            this.cell = cell;
        }
    }

    interface player {
        Cell getNextMove();
    }
    class Field {
        player getFirstPlayerMove;
        player getSecondPlayerMove;
        List<List<Cell>> gamingField;
        Field(player FirstPlayer, player SecondPlayer) {
            List<List<Cell>> gamingField = new ArrayList<>();
            for (int i = 1; i <= 8; i++) {
                List<Cell> row = new ArrayList<Cell>();
                for (int j = 1; j <= 8; j++) {
                    double s = 1, ss = 0;
                    if (i == 1 || i == 8 || j == 1 || j == 8) {
                        s = 2;
                        ss = 0.4;
                    }
                    if (i == 1 || i == 8 && j == 1 || j == 8) {
                        ss = 0.8;
                    }
                    row.add(new Cell("none", s, ss, i ,j));
                }
                gamingField.add(row);
            }
            this.gamingField = gamingField;
            this.getFirstPlayerMove = FirstPlayer.getNextMove();
        }
        List<Cell> getCellsOfColor (String color) {
            List<Cell> answer = new ArrayList<Cell>();
            for (List<Cell> row : gamingField) {
                for (Cell cell : row) {
                    if (Objects.equals(cell.color, color)) {
                        answer.add(cell);
                    }
                }
            }
            return answer;
        }

        Boolean checkForCell (List<Cell> set, int x, int y) {
            return set.stream().anyMatch(cell -> cell.x == x && cell.y == y);
        }

        Boolean checkForCellInField (int x, int y) {
            return x >= 1 && x <= 8 && y >= 1 && y <= 8;
        }

        Cell getCell (int x, int y) {
            return gamingField.get(x - 1).get(y - 1);
        }

        List<CellWithR> getFreeNeighbouringCells (String enemyColor) {
            List<CellWithR> answer = new ArrayList<CellWithR>();
            List<Cell> freeCells = getCellsOfColor("none");
            List<Cell> enemyCells = getCellsOfColor(enemyColor);
            for (Cell cell : enemyCells) {
                int[] offsets = {-1, 0, 1};
                for (int offsetX: offsets) {
                    for (int offsetY: offsets) {
                        if (!(offsetX == 0 && offsetY == 0) && checkForCell(freeCells, cell.x + offsetX, cell.y + offsetY)) {
                            answer.add(new CellWithR(getCell(cell.x + offsetX, cell.y + offsetY)));
                        }
                    }
                }
            }
            return answer;
        }

        List<Cell> getCellsToClosureWith (Cell cell) {
            List<Cell> answer = new ArrayList<Cell>();
            int[][] offsets = {{0, 1}, {0, -1}, {1, 1}, {1, -1}, {1, 0}, {-1, -1}, {-1, 0}, {-1, 1}};
            for (int i = 1; i < 8; i++) {
                for (int[] offset : offsets) {
                    if (!(offset[0] == 0 && offset[1] == 0) && checkForCellInField(cell.x + offset[0], cell.y + offset[1])) {
                        Cell cellToCheck = getCell(cell.x + offset[0]*i, cell.y + offset[1]*i);
                        if (Objects.equals(cellToCheck.color, cell.color)) {
                            answer.add(cellToCheck);
                            offset[0] = 0;
                            offset[1] = 0;
                        }
                    }
                }
            }
            return answer;
        }

        Cell makeTheMove (String playerColor) {
            String enemyColor;
            if (Objects.equals(playerColor, "white")) {
                enemyColor = "black";
            } else {
                enemyColor = "white";
            }
            List<CellWithR> cellsSuitableForMove = getFreeNeighbouringCells(enemyColor);
            for (CellWithR cellSuitableForMove: cellsSuitableForMove) {
                cellSuitableForMove.R = cellSuitableForMove.cell.ss;
                List<Cell> closure = getCellsToClosureWith(cellSuitableForMove.cell);
                for (Cell cellFromClosure: closure) {
                    int[] offset = {0, 0};
                    if (cellSuitableForMove.cell.x - cellFromClosure.x != 0) {
                        offset[0] = (cellSuitableForMove.cell.x - cellFromClosure.x)/Math.abs(cellSuitableForMove.cell.x - cellFromClosure.x);
                    }
                    if (cellSuitableForMove.cell.y - cellFromClosure.y != 0) {
                        offset[1] = (cellSuitableForMove.cell.y - cellFromClosure.y)/Math.abs(cellSuitableForMove.cell.y - cellFromClosure.y);
                    }
                    for (int i = 1; i < Math.max(Math.abs(cellSuitableForMove.cell.x - cellFromClosure.x), Math.abs(cellSuitableForMove.cell.y - cellFromClosure.y)); i++) {
                        if (checkForCellInField(cellSuitableForMove.cell.x + offset[0]*i, cellSuitableForMove.cell.y + offset[1]*i)) {
                            cellSuitableForMove.R += getCell(cellSuitableForMove.cell.x + offset[0]*i, cellSuitableForMove.cell.y + offset[1]*i).s;
                        }
                    }
                }
            }
            Optional<CellWithR> answer =  cellsSuitableForMove.stream().reduce((firstCell, secondCell) -> firstCell.R > secondCell.R ? firstCell: secondCell);
            if (answer.isPresent()) {
                return answer.get().cell;
            } else {
                return new Cell("no moves", 0, 0, -1, -1);
            }
        }
    }
}

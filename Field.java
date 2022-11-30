import java.util.*;

public class Field {
    static class Cell {
        private String color;
        private double s;
        private double ss;
        private int x;
        private int y;

        Cell(String color, double s, double ss, int x, int y) {
            this.color = color;
            this.s = s;
            this.ss = ss;
            this.x = x;
            this.y = y;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }

        public void setSs(double ss) {
            this.ss = ss;
        }

        public double getSs() {
            return ss;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getX() {
            return x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getY() {
            return y;
        }

        public double getS() {
            return s;
        }

        public void setS(double s) {
            this.s = s;
        }
    }

    static class CellWithR {
        private double R = 0;
        private final Cell cell;

        CellWithR(Cell cell) {
            this.cell = cell;
        }

        public double getR() {
            return R;
        }

        public void setR(double r) {
            R = r;
        }

        public Cell getCell() {
            return cell;
        }
    }

    private final List<List<Field.Cell>> field;

    Field() {
        List<List<Field.Cell>> gamingField = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            List<Field.Cell> row = new ArrayList<>();
            for (int j = 1; j <= 8; j++) {
                double s = 1, ss = 0;
                if (i == 1 || i == 8 || j == 1 || j == 8) {
                    s = 2;
                    ss = 0.4;
                }
                if (i == 1 || i == 8 && j == 1 || j == 8) {
                    ss = 0.8;
                }
                if (i == 4 && j == 4 || i == 5 && j == 5) {
                    row.add(new Cell("черный", s, ss, i, j));
                } else if (i == 4 && j == 5 || i == 5 && j == 4) {
                    row.add(new Cell("белый", s, ss, i, j));
                } else {
                    row.add(new Cell("none", s, ss, i, j));
                }
            }
            gamingField.add(row);
        }
        this.field = gamingField;
    }

    List<Field.Cell> getCellsOfColor(String color) {
        List<Field.Cell> answer = new ArrayList<>();
        for (List<Field.Cell> row : field) {
            for (Field.Cell cell : row) {
                if (Objects.equals(cell.getColor(), color)) {
                    answer.add(cell);
                }
            }
        }
        return answer;
    }

    static Boolean checkForCell(List<Field.Cell> set, int x, int y) {
        return set.stream().anyMatch(cell -> cell.getX() == x && cell.getY() == y);
    }

    Boolean checkForCellInField(int x, int y) {
        return x >= 1 && x <= 8 && y >= 1 && y <= 8;
    }

    Field.Cell getCell(int x, int y) {
        return field.get(x - 1).get(y - 1);
    }

    List<Field.CellWithR> getFreeNeighbouringCells(String enemyColor) {
        List<Field.CellWithR> answer = new ArrayList<>();
        List<Field.Cell> freeCells = getCellsOfColor("none");
        List<Field.Cell> enemyCells = getCellsOfColor(enemyColor);
        for (Field.Cell cell : enemyCells) {
            int[] offsets = {-1, 0, 1};
            for (int offsetX : offsets) {
                for (int offsetY : offsets) {
                    if (!(offsetX == 0 && offsetY == 0)
                            && checkForCell(freeCells, cell.getX() + offsetX, cell.getY() + offsetY)
                            && !checkForCell(answer.stream().map(CellWithR::getCell).toList(), cell.getX() + offsetX, cell.getY() + offsetY)) {
                        answer.add(new CellWithR(getCell(cell.getX() + offsetX, cell.getY() + offsetY)));
                    }
                }
            }
        }
        return answer;
    }

    List<Field.Cell> getCellsToClosureWith(Field.Cell cell, String enemyColor, String color) {
        List<Field.Cell> answer = new ArrayList<>();
        int[][] offsets = {{0, 1}, {0, -1}, {1, 1}, {1, -1}, {1, 0}, {-1, -1}, {-1, 0}, {-1, 1}};
        for (int[] offset : offsets) {
            List<Field.Cell> partOfAnswer = new ArrayList<>();
            for (int i = 1; i < 8; i++) {
                if (!(offset[0] == 0 && offset[1] == 0) && checkForCellInField(cell.getX() + offset[0] * i, cell.getY() + offset[1] * i)) {
                    Field.Cell cellToCheck = getCell(cell.getX() + offset[0] * i, cell.getY() + offset[1] * i);
                    if (Objects.equals(enemyColor, cellToCheck.getColor())) {
                        partOfAnswer.add(cellToCheck);
                    }
                    if (Objects.equals(color, cellToCheck.getColor())) {
                        answer.addAll(partOfAnswer);
                        break;
                    }
                    if (Objects.equals("none", cellToCheck.getColor())) {
                        break;
                    }
                }
            }
        }
        return answer;
    }

    void paintCellsFromClosure(Field.Cell cell, String color) {
        String enemyColor = "забагалось";
        if (Objects.equals(color, "белый")) {
            enemyColor = "черный";
        } else if (Objects.equals(color, "черный")) {
            enemyColor = "белый";
        }
        getCellsToClosureWith(cell, enemyColor, color).forEach((item) -> item.setColor(color));
    }

    List<Field.CellWithR> getPossibleMoves(String playerColor) {
        String enemyColor;
        if (Objects.equals(playerColor, "белый")) {
            enemyColor = "черный";
        } else {
            enemyColor = "белый";
        }
        List<Field.CellWithR> cellsSuitableForMove = getFreeNeighbouringCells(enemyColor);
        for (Field.CellWithR cellSuitableForMove : cellsSuitableForMove) {
            cellSuitableForMove.setR(cellSuitableForMove.getCell().getSs());
            List<Field.Cell> closure = getCellsToClosureWith(cellSuitableForMove.getCell(), enemyColor, playerColor);
            for (Field.Cell cellFromClosure : closure) {
                int[] offset = {0, 0};
                if (cellSuitableForMove.getCell().getX() - cellFromClosure.getX() != 0) {
                    offset[0] = (cellFromClosure.getX() - cellSuitableForMove.getCell().getX()) / Math.abs(cellFromClosure.getX() - cellSuitableForMove.getCell().getX());
                }
                if (cellSuitableForMove.getCell().getY() - cellFromClosure.y != 0) {
                    offset[1] = (cellFromClosure.y - cellSuitableForMove.getCell().getY()) / Math.abs(cellFromClosure.y - cellSuitableForMove.getCell().getY());
                }
                for (int i = 1; i <= Math.max(Math.abs(cellSuitableForMove.getCell().getX() - cellFromClosure.getX()), Math.abs(cellSuitableForMove.getCell().getY() - cellFromClosure.y)); i++) {
                    if (checkForCellInField(cellSuitableForMove.getCell().getX() + offset[0] * i, cellSuitableForMove.getCell().getY() + offset[1] * i)) {
                        cellSuitableForMove.R += getCell(cellSuitableForMove.getCell().getX() + offset[0] * i, cellSuitableForMove.getCell().getY() + offset[1] * i).s;
                    }
                }
            }
        }
        return cellsSuitableForMove.stream().filter(cellWithR -> cellWithR.getR() >= 1).toList();
    }

    public void paintCell(Cell cellToPaint, String playerColor) {
        getCell(cellToPaint.getX(), cellToPaint.getY()).setColor(playerColor);
        paintCellsFromClosure(cellToPaint, playerColor);
    }

    void printField(String playerColor) {
        System.out.println("Y");
        List<Cell> possibleMoves = getPossibleMoves(playerColor).stream().map(CellWithR::getCell).toList();
        for (int i = 8; i > 0; i--) {
            System.out.print(i + " ");
            for (int j = 0; j < 8; j++) {
                String color = field.get(j).get(i - 1).getColor();
                if (Objects.equals(color, "черный")) {
                    System.out.print("ч ");
                } else if (Objects.equals(color, "белый")) {
                    System.out.print("б ");
                } else if (checkForCell(possibleMoves, j + 1, i)) {
                    System.out.print("+ ");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.print("\n");
        }
        System.out.println("  1 2 3 4 5 6 7 8 X");
    }

    Field copyField() {
        Field fieldCopy = new Field();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Cell cell = this.field.get(i).get(j);
                fieldCopy.field.get(i).get(j).setColor(cell.getColor());
                fieldCopy.field.get(i).get(j).setS(cell.getS());
                fieldCopy.field.get(i).get(j).setSs(cell.getSs());
                fieldCopy.field.get(i).get(j).setX(cell.getX());
                fieldCopy.field.get(i).get(j).setY(cell.getY());
            }
        }
        return fieldCopy;
    }
}
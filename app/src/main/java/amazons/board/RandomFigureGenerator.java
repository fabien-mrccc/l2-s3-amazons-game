package amazons.board;
import java.util.*;

import amazons.figures.Amazon;
import amazons.figures.Figure;
import amazons.figures.MovableFigure;
import static amazons.figures.EmptyFigure.EMPTY_FIGURE;
public class RandomFigureGenerator implements FigureGenerator{
    private final List<MovableFigure> movableFigures;
    private final Random random;
    private final Iterator<Position> positionIterator;
    private final Set<Figure> usedFigures;

    public RandomFigureGenerator(Random random, List<MovableFigure> movableFigures, Iterator<Position> positionIterator){
        this.movableFigures = new ArrayList<>(movableFigures);
        Collections.shuffle(this.movableFigures);

        this.random = random;
        this.positionIterator = positionIterator;
        usedFigures = new HashSet<>();
    }

    @Override
    public Figure nextFigure(Position position) {
        try {
            Position currentPosition = positionIterator.next();
            int randomProbability = calculateRandomProbability(random, movableFigures, usedFigures, (MatrixIterator<Position>) positionIterator, currentPosition);

            if(randomProbability == 0){
                int indexFigureToAssign = countNumberOfFiguresToAssign(movableFigures, usedFigures) -1;
                if(movableFigures.get(indexFigureToAssign) instanceof Amazon amazon){
                    amazon.setPosition(position);
                }
                usedFigures.add( (Figure) movableFigures.get(indexFigureToAssign) );
                return (Figure) movableFigures.get(indexFigureToAssign);
            }
            return EMPTY_FIGURE;
        }
        catch (NoSuchElementException exception){
            return null;
        }
    }

    /**
     * Calculate the probability that the figure returned is an EMPTY_FIGURE or a figure belonging to movableFigures list.
     * The method is based on two important value:
     * - numberOfFiguresToAssign: returns 1 if it is equal to 0 to inform that only EMPTY_FIGURE have to be assigned.
     * - numberOfSquaresToAssignPerFigure: defines the number of chance that we have to generate a figure from movableFigures. For example,
     * if numberOfSquaresToAssignPerFigure equals to 15, we have 1/15 chance of generate a specific figure, so a value between 0 and 14 (0 represents the
     * case where the specific figure is generated).
     * @param random the random object that we used to generate a random integer
     * @param movableFigures the list of figures that we have to generate
     * @param usedFigures the list of figures that have already been generated
     * @param matrixIterator the matrixIterator that permits us to obtain the board configuration of the game
     * @param currentPosition the currentPosition where the figure will be generated
     * @return return 1 to inform that our next figure to assign is empty OR return value between 0 and numberOfSquaresToAssignPerFigure -1
     */
    private int calculateRandomProbability(Random random, List<MovableFigure> movableFigures, Set<Figure> usedFigures, MatrixIterator<Position> matrixIterator, Position currentPosition){

        int numberOfSquaresBoard = countNumberOfSquaresBoard(matrixIterator);
        int numberOfSquaresAssigned = countNumberOfSquaresAssigned(matrixIterator, currentPosition);
        int numberOfSquaresToAssign = calculateNumberOfSquaresToAssign(numberOfSquaresBoard, numberOfSquaresAssigned);

        int numberOfFiguresToAssign = countNumberOfFiguresToAssign(movableFigures, usedFigures);
        if(numberOfFiguresToAssign == 0){
            return 1;
        }

        int numberOfSquaresToAssignPerFigure = calculateNumberOfSquaresToAssignPerFigure(numberOfSquaresToAssign, numberOfFiguresToAssign);
        if(numberOfSquaresToAssignPerFigure == 0) {
            return 1;
        }

        return Math.abs(random.nextInt()) % numberOfSquaresToAssignPerFigure;
    }

    private int countNumberOfFiguresToAssign(List<MovableFigure> movableFigures, Set<Figure> usedFigures){
        return movableFigures.size() - usedFigures.size();
    }

    private int countNumberOfSquaresAssigned(MatrixIterator<Position> matrixIterator, Position currentPosition){
        return currentPosition.rowIndex()
                * matrixIterator.getNumberOfColumns()
                + currentPosition.columnIndex();
    }

    private int countNumberOfSquaresBoard(MatrixIterator<Position> matrixIterator){
        return matrixIterator.getNumberOfColumns() * matrixIterator.getNumberOfRows();
    }

    private int calculateNumberOfSquaresToAssign(int numberOfSquaresBoard, int numberOfSquaresAssigned){
        return numberOfSquaresBoard - numberOfSquaresAssigned;
    }

    private int calculateNumberOfSquaresToAssignPerFigure(int numberOfSquaresToAssign, int numberOfFiguresToAssign){
        return numberOfSquaresToAssign / numberOfFiguresToAssign;
    }

    public List<MovableFigure> getMovableFigures(){
        return movableFigures;
    }
}

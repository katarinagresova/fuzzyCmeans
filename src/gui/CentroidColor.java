package gui;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class CentroidColor {

    private List<Color> centroidColor;

    public CentroidColor() {
        this.centroidColor = initColors();
    }

    private List initColors() {
        List<Color> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.GRAY);
        colors.add(Color.GREEN);
        colors.add(Color.GOLD);
        colors.add(Color.FIREBRICK);
        colors.add(Color.YELLOW);
        colors.add(Color.SANDYBROWN);
        colors.add(Color.SALMON);
        colors.add(Color.LAVENDER);
        return colors;
    }

    public Color getColor(int index) {
        return this.centroidColor.get(index);
    }
}

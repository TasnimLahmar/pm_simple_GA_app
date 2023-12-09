import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends Application {
    private List<int[]> population;
    private List<int[]> newPopulation;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Simple Genetic Example");

        // Initialize UI components
        List<Label> indLabels = new ArrayList<>();
        List<Label> selLabels = new ArrayList<>();

        for (int i = 1; i <= 8; i++) {
            indLabels.add(createStyledLabel());
        }

        for (int i = 1; i <= 4; i++) {
            selLabels.add(createStyledLabel());
        }

        Button sortButton = createStyledButton("Sort");
        Button selectionButton = createStyledButton("Selection");
        Button mutateButton = createStyledButton("Mutate");
        Button fillPopButton = createStyledButton("Fill Pop");

        // Create layout
        VBox mainLayout = new VBox(10);
        VBox buttons_labels=new VBox(30);

        // Left side with labels
        HBox labelsLayout = new HBox(50);
        VBox indLabelsVBox = new VBox(30);
        VBox selLabelsVBox = new VBox(30);
        HBox buttonsLayout = new HBox(10);
        // Right side with buttons

        buttonsLayout.getChildren().addAll(sortButton, selectionButton, mutateButton, fillPopButton);
        buttonsLayout.getStyleClass().add("button-layout");
        mainLayout.getChildren().add(buttonsLayout);



        indLabelsVBox.getChildren().addAll(indLabels);
        labelsLayout.getChildren().addAll(indLabelsVBox, selLabelsVBox);
        buttons_labels.getChildren().addAll(buttonsLayout,labelsLayout);
        mainLayout.getChildren().add( buttons_labels);


        // Set up the scene
        Scene scene = new Scene(mainLayout, 1000, 1000);
        scene.getStylesheets().add("style.css"); // Add a CSS file for styling
        primaryStage.setScene(scene);
        mainLayout.setStyle("-fx-background-color: #383838;");

        primaryStage.show();

        // Initialize population
        population = generatePopulation();
        displayPopulation(indLabels);

        sortButton.setOnAction(e -> {
            // Sort function
            population.sort((i1, i2) -> computeFitness(i2) - computeFitness(i1));
            displayPopulation(indLabels);
        });

        selectionButton.setOnAction(e -> {
            newPopulation = new ArrayList<>();
            newPopulation.add(population.get(0));
            newPopulation.add(crossOver(population.get(0), population.get(1)));
            newPopulation.add(crossOver(population.get(0), population.get(2)));
            newPopulation.add(crossOver(population.get(1), population.get(2)));
            displayPopulation(selLabelsVBox, newPopulation);
        });

        mutateButton.setOnAction(e -> {
            for (int i = 0; i < 4; i++) {
                int[] individual = newPopulation.get(i);
                int geneToMutate = new Random().nextInt(individual.length);
                individual[geneToMutate] = 1 - individual[geneToMutate]; // Toggle 0 to 1 and vice versa
            }
            displayPopulation(selLabelsVBox, newPopulation);
        });

        fillPopButton.setOnAction(e -> {
            population.clear();
            population.add(newPopulation.get(0));
            population.add(newPopulation.get(1));
            population.add(newPopulation.get(2));
            population.add(newPopulation.get(3));

            population.add(generateIndividual());
            population.add(generateIndividual());
            population.add(generateIndividual());
            population.add(generateIndividual());

            displayPopulation(indLabels);

            clearLabels(selLabels);

        });
    }


    private Label createStyledLabel() {
        Label label = new Label("");
        label.getStyleClass().add("styled-label");
        return label;
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("styled-button");
        button.setOnAction(e -> {
            // Change button color on click
            button.getStyleClass().add("clicked-button");

            // Remove the style class after the click event has been handled
            button.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
                button.getStyleClass().remove("clicked-button");
            });
        });
        return button;
    }

    private List<int[]> generatePopulation() {
        List<int[]> pop = new ArrayList<>();
        for (int p = 0; p < 8; p++) {
            pop.add(generateIndividual());
        }
        return pop;
    }

    private int[] generateIndividual() {
        int[] individual = new int[8];
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            individual[i] = random.nextBoolean() ? 1 : 0;
        }
        return individual;
    }

    private void displayPopulation(List<Label> labels) {
        for (int p = 0; p < labels.size(); p++) {
            String st = individualToString(p,population.get(p));
            labels.get(p).setText(st);
        }
    }


private void displayPopulation(VBox labelsVBox, List<int[]> population) {
    labelsVBox.getChildren().clear();
    for (int p = 0; p < population.size(); p++) {
        Label label = createStyledLabel();
        String st = individualToString(p, population.get(p));
        label.setText(st);
        labelsVBox.getChildren().add(label);
    }
}

    private void clearLabels(List<Label> labels) {
        for (Label label : labels) {
            label.setText("");
        }
    }

    private String individualToString(int index,int[] individual) {
        StringBuilder st = new StringBuilder();
        st.append(index + 1).append(".  "); // Adding 1 to index for 1-based numbering

        for (int gene : individual) {
            st.append(gene);
        }
        return st.toString();
    }

    private int computeFitness(int[] individual) {
        int score = 0;
        for (int gene : individual) {
            score += gene;
        }
        return score;
    }

    private int[] crossOver(int[] i1, int[] i2) {
        return new int[]{i1[0], i1[1], i1[2], i1[3], i2[4], i2[5], i2[6], i2[7]};
    }
}

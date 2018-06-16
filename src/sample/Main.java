package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.Arrays;

public class Main extends Application {
    private int tries;
    private int n;

    private ArrayList<Double> valArr = new ArrayList<>();
    private ArrayList<Double> doe = new ArrayList<>(Arrays.asList(0.0,0.0,0.0,0.0,0.0));
    private ArrayList<Double> info = new ArrayList<>(Arrays.asList(3.0, 2.0, 1.0, 4.0, 2.0));


    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage window;
        window = primaryStage;
        window.setTitle("Virtual Localization");


//----------------------------------GRID PANE INITIALISATION--------------------------------
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);

        Label head = new Label("Localisation");
        GridPane.setConstraints(head, 0, 0);

        Label attempt = new Label("Enter the number of Tries: ");
        TextField aInput = new TextField();


        Button seta = new Button("Set");
        seta.setOnAction(e ->{
           String temp= aInput.getText();
           try{
               Double t = Double.parseDouble(temp);
               tries= t.intValue();
               System.out.println("Tries:"+tries);

           }
           catch(NumberFormatException x){
               System.out.println("Exceptional");
           }

        });






        GridPane.setConstraints(seta,1,2);


        Label speakers = new Label("Enter the number of Speakers: ");
        TextField sInput = new TextField();

        Button setb = new Button("Set");
        setb.setOnAction(e ->{
            String t = sInput.getText();
            try{Double x = Double.parseDouble(t);
                n= x.intValue();
                System.out.println("Speakers:"+n);

                System.out.println("hi");
                for(int j=0;j<tries;j++){
                    double r = info.get(j);
                    info.set(j,((360/n)*(r-1)));
                }
                System.out.println("Contents of info:");
                for(double v : info){

                    System.out.println(v);
                }
            }
            catch (NumberFormatException y){
                System.out.println("Exceptional");
            }


        });
/*        System.out.println("hi");
        for(int j=1;j<tries;j++){
            double r = info.get(j);
            info.set(j,((360/n)*(r-1)));
        }

        for(double v : valArr){
            System.out.println("Contents:");
            System.out.println(v);
        }*/
        GridPane.setConstraints(setb,1,4);

        GridPane.setConstraints(attempt, 0, 1);
        GridPane.setConstraints(aInput, 0, 2);

        GridPane.setConstraints(speakers, 0, 3);
        GridPane.setConstraints(sInput, 0, 4);

        Label info = new Label("Enter 1 for UP,2 for RIGHT,3 for DOWN,4 for RIGHT");


        GridPane.setConstraints(info, 0, 8);

        TextField input = new TextField();
        GridPane.setConstraints(input, 5, 10);


        Button confirm = new Button("Confirm");
        GridPane.setConstraints(confirm, 5, 11);

        Button submit = new Button("Submit");
        GridPane.setConstraints(submit,5,14);


//-----------------------------------G P END-------------------------------------------------

//------------------------------------FUNCTIONS-----------------------------------------------
        confirm.setOnAction(e -> DOEinput(input,n));

        submit.setOnAction(e ->{
            System.out.println("Contents of valArr:");
            for(double v : valArr){

                System.out.println(v);
            }
            double result = DOEcalc(tries);
            Label res = new Label("Result is: " + result);
            GridPane.setConstraints(res, 0, 14);
            grid.getChildren().add(res);

        });

//------------------------------------FUNCTIONS END------------------------------------------

        grid.getChildren().addAll(head, attempt,seta,setb, aInput, speakers, sInput, info, input, confirm,submit);

        Scene scene = new Scene(grid, 600, 400);
        window.setScene(scene);
        window.show();
    }

    private void DOEinput(TextField value, int n)
    {
        String s = value.getText();
        Double b = Double.parseDouble(s);
        int data = b.intValue();
        double degree = (360/n)*(data-1);
        valArr.add(degree);
    }

    private double DOEcalc(double tries){
        double sum=0;
        for (int i =0;i<tries;i++){
            double x=valArr.get(i);
            double y=info.get(i);
            if (Math.abs(x-y )== 90.0 || Math.abs(x-y) == 270.0) {
                doe.set(i,90.0);
            }
            else if(Math.abs(x-y)==180.0)
            {
                doe.set(i,180.0);
            }
            else if((x-y)==0.0)
            {
                doe.set(i,0.0);
            }
        }

        for(int i=0;i<tries;i++){
            sum = sum + (doe.get(i))*(doe.get(i));
        }

        double res = sum/tries;
        return (Math.sqrt(res));

    }

    public static void main(String[] args) {
        launch(args);
    }
}

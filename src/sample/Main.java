package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main extends Application {
    private GridPane grid;
    private int tries;
    private Label attempt;
    private int n;
    private static int t;

    private ArrayList<Double> valArr = new ArrayList<>();
    private ArrayList<Double> doe = new ArrayList<>();
    private ArrayList<Double> info = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {

        XSSFWorkbook workbook = new XSSFWorkbook();
        try{workbook = new XSSFWorkbook(new FileInputStream(new File("C:\\Users\\user\\IdeaProjects\\AIISH GUI new\\src\\sample\\Data.xlsx")));}
        catch(FileNotFoundException e)
        {
            System.out.println("Exceptional");

        }

        XSSFSheet sheet = workbook.getSheetAt(0);
        Row row;
        double cellVal;
        for(int rowIndex =1;rowIndex<=sheet.getLastRowNum();rowIndex++)
        {
            row = sheet.getRow(rowIndex);
            if(row!=null){
                org.apache.poi.ss.usermodel.Cell cell = row.getCell(0);
                if(cell!=null){
                    cellVal= cell.getNumericCellValue();
                    info.add(cellVal);
                }
            }
        }
        tries = sheet.getLastRowNum();


        Stage window;
        window = primaryStage;
        window.setTitle("Virtual Localization");


//----------------------------------GRID PANE INITIALISATION--------------------------------
        grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);

        Label head = new Label("Localization");
        head.setFont(new Font(32));
        GridPane.setConstraints(head, 0, 0);

        attempt = new Label("The number of attempts Left = "+tries);
        GridPane.setConstraints(attempt,1,9);


        Label speakers = new Label("Enter the number of Speakers: ");
        TextField sInput = new TextField();

        Button set = new Button("Set");
        set.setOnAction(e ->{

            String t = sInput.getText();
            try{Double x = Double.parseDouble(t);
                n= x.intValue();
                System.out.println("Speakers:"+n);

                System.out.println("Contents of info:");
                for(double v : info){

                    System.out.println(v);

                }
            }
            catch (NumberFormatException y){
                System.out.println("Exceptional");
            }


        });

        GridPane.setConstraints(set,0,5);

        GridPane.setConstraints(speakers, 0, 3);
        GridPane.setConstraints(sInput, 0, 4);

        Label det = new Label("Enter 1 for UP,2 for RIGHT,3 for DOWN,4 for LEFT");


        GridPane.setConstraints(det, 0, 8);

        TextField input = new TextField();
        GridPane.setConstraints(input, 5, 10);


        Button confirm = new Button("Confirm");
        GridPane.setConstraints(confirm, 5, 11);

        Button submit = new Button("Submit");
        GridPane.setConstraints(submit,5,14);

        Label inf = new Label("INFO CONTENTS:");
//-----------------------------------G P END-------------------------------------------------

//------------------------------------FUNCTIONS-----------------------------------------------
        t = tries;
        confirm.setOnAction(e -> DOEinput(input,n));

        for (double v : info)
        {
            String n = Double.toString(v);
            inf.setText(inf.getText()+ n+",");
        }
        GridPane.setConstraints(inf,5,16);
        submit.setOnAction(e ->{
            System.out.println("Contents of valArr:");
            for(double v : valArr){

                System.out.println(v);
            }


           //GridPane.setConstraints(inf,5,16);


            double result = DOEcalc(tries);
            Label res = new Label("DOE is: " + result);
            res.setFont(new Font(30));
            GridPane.setConstraints(res, 0, 20);
            grid.getChildren().add(res);

        });



//------------------------------------FUNCTIONS END------------------------------------------

        grid.getChildren().addAll(head,attempt,inf,set, speakers, sInput, det, input, confirm,submit);

        Scene scene = new Scene(grid, 970, 600);
        window.setScene(scene);
        window.show();
    }


//------------------------------------FUNCTION DEFNS------------------------------------------
    private void DOEinput(TextField value, int n)
    {
        String s = value.getText();
        Double b = Double.parseDouble(s);
        int data = b.intValue();
        double degree = (360/n)*(data-1);
        valArr.add(degree);
        t--;
        attempt.setText("The number of attempts Left = "+ t);

        Label valu = new Label("valArr Contents:");
        for(double v: valArr) {
            valu.setText(valu.getText() + v +",");
        }
        GridPane.setConstraints(valu,5,18);
        grid.getChildren().add(valu);
    }

    private double DOEcalc(double tries){
        double sum=0;
        for (int i =0;i<valArr.size();i++){
            double x=valArr.get(i);
            double y=info.get(i);
            if (Math.abs(x-y )== 90.0 || Math.abs(x-y) == 270.0) {
                doe.add(i,90.0);
            }
            else if(Math.abs(x-y)==180.0)
            {
                doe.add(i,180.0);
            }
            else if((x-y)==0.0)
            {
                doe.add(i,0.0);
            }
        }

        System.out.println("Contents of DOE: ");
        for (Double aDoe : doe) {
            System.out.println(aDoe);
            sum = sum + (aDoe * aDoe);
        }

        double res = sum/tries;
        return (Math.sqrt(res));

    }
//-------------------------------------FUNCTION DEFN ENDS-----------------------------------------
    public static void main(String[] args) {
        launch(args);
    }
}

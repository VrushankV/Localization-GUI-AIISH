package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Main extends Application {
    private Pane pane = new Pane();


    private ArrayList<Double> valArr = new ArrayList<>();
    private ArrayList<Double> info = new ArrayList<>();
    private ArrayList<Integer> sNumbers = new ArrayList<>();
    private ArrayList<Double> uniqInfo = new ArrayList<>();
    private HashMap<Integer,Double> map = new HashMap<>();

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
        Double cv;
        for(int rowIndex =1;rowIndex<=sheet.getLastRowNum();rowIndex++)
        {
            row = sheet.getRow(rowIndex);
            if(row!=null){
                org.apache.poi.ss.usermodel.Cell cell2 = row.getCell(1);
                Cell cell1 = row.getCell(0);
                if(cell1!=null &&cell2!=null){
                    cellVal= cell2.getNumericCellValue();
                    info.add(cellVal);
                    cv = cell1.getNumericCellValue();
                    sNumbers.add(cv.intValue());
                }
            }

        }

        int tries = sheet.getLastRowNum() - 1;

        for (Double x : info){
            if(!uniqInfo.contains(x)){
                uniqInfo.add(x);
            }
        }
       // int n = uniqInfo.size();

        for(int i=0;i<tries;i++){
            if(!map.containsKey(sNumbers.get(i))){
                map.put(sNumbers.get(i),uniqInfo.get(i));
            }

        }


        Stage window;
        window = primaryStage;
        window.setTitle("Virtual Localization");


//----------------------------------GRID PANE INITIALISATION--------------------------------

        Label head = new Label("Localization");
        head.setFont(new Font(32));
        head.setLayoutX(200);
        head.setLayoutY(20);
        pane.getChildren().add(head);



//-----------------------------------G P END-------------------------------------------------

//------------------------------------FUNCTIONS-----------------------------------------------
        generator();


//------------------------------------FUNCTIONS END------------------------------------------



        Button calculate = new Button("Calculate");
        calculate.setLayoutY(400);
        calculate.setLayoutX(100);
        calculate.setOnAction(this::handleButtonAction);
        pane.getChildren().addAll(calculate);


        Scene scene = new Scene(pane,1000,650);
        window.setScene(scene);
        window.show();
    }


//------------------------------------FUNCTION DEFNS------------------------------------------


//-------------------------------------FUNCTION DEFN ENDS-----------------------------------------
    public static void main(String[] args) {
        launch(args);
    }

    private void generator(){

        Circle circle = new Circle();
        circle.setCenterX(675);
        circle.setCenterY(325);
        circle.setRadius(300);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.BEIGE);

        //-----------------------------------CIRCLE BUTTONS------------------------------------------

        pane.getChildren().addAll(circle);
        generateButtons(circle.getRadius(),circle.getCenterX(),circle.getCenterY(),uniqInfo);

        //-----------------------------------CIRCLE BUTTONS END--------------------------------------

    }

    private void generateButtons(double r, double x, double y, ArrayList<Double> selected) {
        double targetx, targety;
        int i = 0;
        Collections.sort(selected);

        for (double sel : selected) {
            i++;
            if (0 <= sel && sel < 90) {
                sel = 270 + sel;
            } else {
                sel = sel - 90;
            }
            targetx = r * (Math.cos(Math.PI * sel / 180));
            targety = r * (Math.sin(Math.PI * sel / 180));
            Button btn = new Button(Integer.toString(i));
            btn.setLayoutX(x + targetx);
            btn.setLayoutY(y + targety);
            btn.setOnAction(this::handleButtonAction);
            pane.getChildren().add(btn);
        }
    }

    private void handleButtonAction(ActionEvent event) {
        if(((Button) event.getSource()).getText().equals("Calculate")){
            DOEcalc();
        }
        else{
            String s = ((Button)event.getSource()).getText();
            valArr.add(map.get(Integer.parseInt(s)));
        }

    }

    private void DOEcalc(){
        System.out.println(valArr.size());
        double sum=0;

        for (double x : info){
            System.out.println(x);
        }
        for (double x : valArr){
            System.out.println(x);
        }

        for (int i =0;i<valArr.size();i++){
            double r=valArr.get(i);
            double s=info.get(i);
            double z = r-s;
            if(Math.abs(r-s)<=180) {
                sum = sum + Math.pow((z), 2);
            }
            else{


                if(r>180){
                    r = 360-r;
                }
                else if (s>180){
                    s=360-s;
                }
                z=r+s;
                sum = sum + Math.pow((z), 2);
            }
        }
        System.out.println(sum);

        double result = sum/info.size();
        result = Math.sqrt(result);
        Label res = new Label("DOE is: " + result);
        res.setFont(new Font(30));
        res.setLayoutY(600);
        res.setLayoutX(100);
        pane.getChildren().add(res);


    }
}

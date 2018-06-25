package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Application {
    private Pane pane = new Pane();
    private String url=null;
    private int count;

    private int tries;
    private Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
    private double height = bounds.getHeight()-50;
    private double width = bounds.getWidth();
    private double r;
    private double a=0;


    private ArrayList<Double> dps = new ArrayList<>();
    private ArrayList<Double> valArr = new ArrayList<>();
    private ArrayList<Double> info = new ArrayList<>();
    private ArrayList<Integer> sNumbers = new ArrayList<>();
    private ArrayList<Double> uniqInfo = new ArrayList<>();
    private ArrayList<Integer> uniqsNumbers = new ArrayList<>();
    private HashMap<Integer,Double> map = new HashMap<>();
    private Label attemptsLeft;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Button openFile = new Button("Open a file...");
        openFile.setLayoutX(width*0.05);
        openFile.setLayoutY(height*0.2);
        openFile.setFont(new Font(22));
        pane.getChildren().add(openFile);


        openFile.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("EXCEL FILES", "*.xlsx"));
            File file = fileChooser.showOpenDialog(null);

            String add = file.getAbsolutePath();
            String address = add.replace("\\","\\\\");
            getUrl(address);
            loadData();
            count = info.size();
            attemptsLeft = new Label("Attempts Left = "+count);
            attemptsLeft.setLayoutY(height*0.3);
            attemptsLeft.setLayoutX(width*0.05);
            attemptsLeft.setFont(new Font(22));
            pane.getChildren().add(attemptsLeft);
            generator();
        });





        Stage window;
        window = primaryStage;
        window.setTitle("Virtual Localization");


//----------------------------------PANE INITIALISATION--------------------------------

        Label head = new Label("Localization");
        head.setFont(new Font(40));
        head.setLayoutX(width*0.05);
        head.setLayoutY(height*0.05);
        pane.getChildren().add(head);


        Button calculate = new Button("Calculate");
        calculate.setLayoutY(height*0.4);
        calculate.setLayoutX(width*0.05);
        calculate.setFont(new Font(22));
        calculate.setOnAction(this::handleButtonAction);
        pane.getChildren().addAll(calculate);

//-----------------------------------P END-------------------------------------------------

        Scene scene = new Scene(pane,width,height);
        window.setScene(scene);
        window.resizableProperty().setValue(Boolean.FALSE);
        window.show();
    }










    public static void main(String[] args) {
        launch(args);
    }

    //------------------------------------FUNCTION DEFNS------------------------------------------
    private void getUrl(String address){
        url = address;
    }

    private void loadData(){
        XSSFWorkbook workbook = new XSSFWorkbook();
        try{workbook = new XSSFWorkbook(new FileInputStream(new File(url)));}
        catch(Exception e)
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

        tries = sheet.getLastRowNum();


        for (Double x : info){
            if(!uniqInfo.contains(x)){
                uniqInfo.add(x);
            }
        }

        for (Integer x: sNumbers){
            if(!uniqsNumbers.contains(x)){
                uniqsNumbers.add(x);
            }
        }



        for(int i = 0; i<tries; i++){
            if(!map.containsKey(sNumbers.get(i))){
                map.put(sNumbers.get(i),info.get(i));
            }
        }

    }


    private void generator(){

        Circle circle = new Circle();
        circle.setCenterX(width*0.65);
        circle.setCenterY(height*0.5);
        circle.setRadius(height*0.45);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.BEIGE);

        //-----------------------------------CIRCLE BUTTONS------------------------------------------

        pane.getChildren().addAll(circle);
        generateButtons(circle.getRadius(),circle.getCenterX(),circle.getCenterY());

    }

    private void generateButtons(double r, double x, double y) {
        double targetx, targety;
        int i = 0;
        for (double sel : uniqInfo) {
            if (0 <= sel && sel < 90) {
                sel = 270 + sel;
            } else {
                sel = sel - 90;
            }
            targetx = r * (Math.cos(Math.PI * sel / 180));
            targety = r * (Math.sin(Math.PI * sel / 180));
            Button btn = new Button(Integer.toString(uniqsNumbers.get(i)));
            btn.setLayoutX(x + targetx);
            btn.setLayoutY(y + targety);
            btn.setOnAction(this::handleButtonAction);
            pane.getChildren().add(btn);
            i++;
        }
    }
    //-----------------------------------CIRCLE BUTTONS END--------------------------------------
    private void handleButtonAction(ActionEvent event) {
        count--;

        if(((Button) event.getSource()).getText().equals("Calculate")){
            count++;
            attemptsLeft.setText("Attempts Left: "+count);
            DOEcalc();
            DOEperSpeaker();
            writed();
        }
        else{
            attemptsLeft.setText("Attempts Left: "+count);
            String s = ((Button)event.getSource()).getText();
            valArr.add(map.get(Integer.parseInt(s)));
        }

    }

    private void DOEperSpeaker() {
        VBox box = new VBox();
        box.setPadding(new Insets(5));
        box.setLayoutX(width*0.05);
        box.setLayoutY(height*0.6);
        Label l = new Label("DOE PER SPEAKER:");
        l.setFont(new Font(20));
        box.getChildren().add(l);

        for (Integer uniqsNumber : uniqsNumbers) {
            double sum = 0;
            double result;
            double z,r,s;
            int x;
            ArrayList<Double> tempValues = new ArrayList<>();
            for (int j = 0; j < sNumbers.size(); j++) {
                if (uniqsNumber.equals(sNumbers.get(j))) {
                    tempValues.add(valArr.get(j));
                }
            }
            for (int i=0;i<tempValues.size();i++) {
                x = sNumbers.indexOf(uniqsNumber);
                s = map.get(sNumbers.get(x));
                r = tempValues.get(i);
                z = r-s;
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
            result = sum / tempValues.size();
            result = Math.sqrt(result);
            dps.add(result);
            a = a + result;
            box.getChildren().add(new Label("SPEAKER "+ String.valueOf(uniqsNumber)+"=" + String.valueOf(result)));

        }
        a = a/uniqsNumbers.size();
        box.getChildren().add(new Label("Average DOE per speaker = "+String.valueOf(a)));
        pane.getChildren().add(box);

    }

    private void DOEcalc(){
        double sum=0;

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

        r = sum/info.size();
        r = Math.sqrt(r);
        Label res = new Label("DOE is: " + r);
        res.setFont(new Font(30));
        res.setLayoutY(height*0.9);
        res.setLayoutX(width*0.05);
        pane.getChildren().add(res);

    }

    private void writed() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        try{workbook = new XSSFWorkbook(new FileInputStream(new File(url)));}
        catch(Exception e)
        {
            System.out.println("Exceptional");

        }

        XSSFSheet sheet = workbook.getSheetAt(0);
        Row row;

        int i=0;
        row = sheet.createRow(tries+4);
        Cell res = row.createCell(3);
        res.setCellValue("DOE=");
        Cell ult = row.createCell(4);
        ult.setCellValue(r);

        row = sheet.getRow(1);
        Cell c = row.createCell(5);
        c.setCellValue("DOE PER  SPEAKER");

        row = sheet.getRow(2);
        Cell c1 = row.createCell(5);
        c1.setCellValue("Speaker No.");

        Cell c2 = row.createCell(6);
        c2.setCellValue("DOE");
        for(int rowIndex =1;rowIndex<=tries;rowIndex++)
        {
            //------Response---------
            row = sheet.getRow(rowIndex);
            org.apache.poi.ss.usermodel.Cell cell=row.createCell(2);
            cell.setCellValue(valArr.get(i));
            //--------- R END--------
            //-----DOE PER SPEAKER-------
            row = sheet.getRow(rowIndex+2);
            if(i<uniqsNumbers.size()) {
                Cell cell1 = row.createCell(5);
                cell1.setCellValue(uniqsNumbers.get(i));
            }
            if (i<dps.size()){
                Cell cell2 = row.createCell(6);
                cell2.setCellValue(dps.get(i));
            }
            //--------DPS END ------------
            i++;
        }

        row = sheet.getRow(uniqsNumbers.size()+3);
        Cell cx = row.createCell(5);
        cx.setCellValue("AVG. DOE=");

        cx = row.createCell(6);
        cx.setCellValue(a);
        FileOutputStream file = null;
        try {
            file = new FileOutputStream(new File(url));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            workbook.write(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//-------------------------------------FUNCTION DEFN ENDS-----------------------------------------

}

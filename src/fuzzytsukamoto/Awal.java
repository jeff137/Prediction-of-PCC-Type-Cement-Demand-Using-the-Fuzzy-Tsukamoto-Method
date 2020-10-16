/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fuzzytsukamoto;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSetMetaData;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author aceqr
 */
public class Awal extends javax.swing.JFrame {

   private Statement stt;    
    private ResultSet rss;
    private ResultSetMetaData rssd;
    private DefaultTableModel modelData;
    private DefaultTableModel modelTernormalisasi;
    private DefaultTableModel modelHitung;
    private DefaultTableModel modelUjiData;
    private DefaultTableModel modelPermintaanNormal;
    private DefaultTableModel modelPermintaanDenormal;
    private DefaultTableModel modelMape;
    private JTable tabel;
    private konekk konek = new konekk();
    private Connection con = konek.KoneksiDB();
    @SuppressWarnings("MismatchedReadAndWriteOfArray") 
    static int CountColumn = 0;
    @SuppressWarnings("MismatchedReadAndWriteOfArray")
    static private String DataColumnX[] = new String[200];        
    String fileInput;    
    private int Temp;
    DecimalFormat cf = new DecimalFormat("#.######");
    DecimalFormat df = new DecimalFormat("#.#");
    
    /**
     * Creates new form Awal
     */
    public Awal() {
 
        initComponents();  
        this.setTitle("Fuzzy.JRE");
        CountColumn = CountCol();
//        StokTengah.setText(double.toString(DataAverageNormalisasi()));

    }

    private boolean DropTable(){        
        try {            
            String sql2 = "DROP TABLE Fuzzy";
            stt = con.createStatement();
            stt.executeUpdate(sql2);
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
    
    private boolean DropTableMape(){        
        try {            
            String sql2 = "DROP TABLE Mape";
            stt = con.createStatement();
            stt.executeUpdate(sql2);
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
    
    private int CountCol(){
            try {
            String sql2 = "SELECT * FROM Fuzzy";
            stt = con.createStatement();
            rss = stt.executeQuery(sql2);
            rssd = rss.getMetaData();                
            return rssd.getColumnCount();
               
    }   catch (SQLException ex) {
            Logger.getLogger(Awal.class.getName()).log(Level.SEVERE, null, ex);
        }
            return 0; 
    }
    
    private String[] NameCol(){
        try {                        
            @SuppressWarnings("MismatchedReadAndWriteOfArray")
            String[] Name = new String[CountCol()+1];            
            String sql2 = "SELECT * FROM Fuzzy";
            stt = con.createStatement();
            rss = stt.executeQuery(sql2);
            rssd = rss.getMetaData();                
            rssd.getColumnCount();
            for(int i = 1; i<=rssd.getColumnCount();i++){                
                Name[i] = rssd.getColumnName(i);
            }
            return Name;
               
    }   catch (SQLException ex) {
            Logger.getLogger(Awal.class.getName()).log(Level.SEVERE, null, ex);
        }
            return null;      
    }
    
    public void setInputFile(String fileInputX) {
        fileInput = fileInputX;
    }
    
    public void ReadFile_Mape() throws IOException, BiffException  {        
        DropTableMape();
        File fileExcel = new File(fileInput);
        Workbook w;
        String kolom;                
        w = Workbook.getWorkbook(fileExcel);        
        // Ambil sheet pertama, nomer 0 menandakan sheet ke 1
        Sheet sheet = w.getSheet(0);
        CountColumn = sheet.getColumns(); 
        for (int j = 0; j < sheet.getColumns(); j++){                        
            Cell cell = sheet.getCell(j,0);                        
            DataColumnX[j] = cell.getContents();                        
            
        }        
         try{             
                String sql2 = "CREATE TABLE Mape(id INT AUTO_INCREMENT PRIMARY KEY)";                            
                stt = con.createStatement();
                stt.executeUpdate(sql2);
                for(int j = 0; j < sheet.getColumns(); j++){
                        Cell cell = sheet.getCell(j,0);
                        if(!cell.getContents().equalsIgnoreCase("tahun")){                            
                            sql2 = "ALTER TABLE Mape ADD "+cell.getContents()+" DOUBLE(200)";
                            stt = con.createStatement();
                            stt.executeUpdate(sql2);
                        }else{
                            sql2 = "ALTER TABLE Mape ADD "+cell.getContents()+" INT";
                            stt = con.createStatement();
                            stt.executeUpdate(sql2);
                        }
                    }                                                    
           }catch(SQLException e){
               System.out.println("error here : 101");
                System.out.println(e);                                
        }        
        try{            
            for(int i = 1; i < sheet.getRows(); i++){
                
                String sql2 = "INSERT INTO Mape values(NULL";            
                for(int j = 0; j < sheet.getColumns(); j++){
                        Cell cell = sheet.getCell(j,i);
                        if(j==0){
                            if(cell.getContents().equalsIgnoreCase("")){  
                                sql2 = sql2+" ,"+Temp+"";
                            }else{
                                sql2 = sql2+" ,"+cell.getContents()+"";
                                Temp = Integer.parseInt(cell.getContents());
                            }                                                            
                    }else{
                            sql2 = sql2+" ,"+cell.getContents()+"";          
                    }            
                }
                    sql2 = sql2+")";
                    stt = con.createStatement();
                    stt.executeUpdate(sql2);
            }            
        }catch(SQLException e){
            System.out.println("error here : 202");
            System.out.println(e);
        }
    }
    
    public void ReadFile() throws IOException, BiffException  {        
        DropTable();
        File fileExcel = new File(fileInput);
        Workbook w;
        String kolom;                
        w = Workbook.getWorkbook(fileExcel);        
        // Ambil sheet pertama, nomer 0 menandakan sheet ke 1
        Sheet sheet = w.getSheet(0);
        CountColumn = sheet.getColumns();
        for (int j = 0; j < sheet.getColumns(); j++){                        
            Cell cell = sheet.getCell(j,0);                        
            DataColumnX[j] = cell.getContents();                        
            
        }        
         try{
                String sql2 = "CREATE TABLE Fuzzy(id INT AUTO_INCREMENT PRIMARY KEY)";                            
                stt = con.createStatement();
                stt.executeUpdate(sql2);
                for(int j = 0; j < sheet.getColumns(); j++){
                        Cell cell = sheet.getCell(j,0);
                        if(!cell.getContents().equalsIgnoreCase("tahun")){                            
                            sql2 = "ALTER TABLE Fuzzy ADD "+cell.getContents()+" DOUBLE(200)";
                            stt = con.createStatement();
                            stt.executeUpdate(sql2);
                        }else{
                            sql2 = "ALTER TABLE Fuzzy ADD "+cell.getContents()+" INT";
                            stt = con.createStatement();
                            stt.executeUpdate(sql2);
                        }
                    }                                                    
           }catch(SQLException e){
               System.out.println("error here : 101");
                System.out.println(e);                                
        }        
        try{            
            for(int i = 1; i < sheet.getRows()-1; i++){
                String sql2 = "INSERT INTO FUZZY values(NULL";            
                for(int j = 0; j < sheet.getColumns(); j++){
                        Cell cell = sheet.getCell(j,i);
                        if(j==0){
                            if(cell.getContents().equalsIgnoreCase("")){  
                                sql2 = sql2+" ,"+Temp+"";
                            }else{
                                sql2 = sql2+" ,"+cell.getContents()+"";
                                Temp = Integer.parseInt(cell.getContents());
                            }                                                            
                    }else{
                            sql2 = sql2+" ,"+cell.getContents()+"";          
                    }            
                }
                    sql2 = sql2+")";
                    stt = con.createStatement();
                    stt.executeUpdate(sql2);
            }            
        }catch(SQLException e){
            System.out.println("error here : 202");
            System.out.println(e);
        }
    }
    
    private void InitTableData()
    {   
        if(CountCol() == 0){
        modelData = new DefaultTableModel();
        modelData.addColumn("ID");
        modelData.addColumn("Stok");        
        modelData.addColumn("Penjualan");                        
        modelData.addColumn("Permintaan");             
        jdataAwal.setModel(modelData);        
        }else{
        modelData = new DefaultTableModel();   
        for(int i = 1;i<=CountCol();i++){
            modelData.addColumn(NameCol()[i]);
        }
        jdataAwal.setModel(modelData);        
        }
    }
     
    private void InitTableNormalisasi()
    {   
        if(CountCol() == 0){
        modelTernormalisasi = new DefaultTableModel();
        modelTernormalisasi.addColumn("ID");
        modelTernormalisasi.addColumn("Stok");        
        modelTernormalisasi.addColumn("Penjualan");                        
        modelTernormalisasi.addColumn("Permintaan");             
        jTableNormalisasi1.setModel(modelTernormalisasi);        
        }else{
        modelTernormalisasi = new DefaultTableModel();   
        for(int i = 1;i<=CountCol();i++){
            modelTernormalisasi.addColumn(NameCol()[i]);
        }
        jTableNormalisasi1.setModel(modelTernormalisasi);        
        }
    }
    
    private void InitTableUjiData()
    {   
        
        modelUjiData = new DefaultTableModel();
        modelUjiData.addColumn("ID");
        modelUjiData.addColumn("STOK");        
        modelUjiData.addColumn("PENJUALAN");                                           
        jTableUji1.setModel(modelUjiData );        
                
    }
    
    private void InitTablePermintaanNormal()
    {   
        
        modelPermintaanNormal = new DefaultTableModel(); 
        modelPermintaanNormal.addColumn("PERMINTAAN");                                           
        jPermintaanNormalisasi.setModel(modelPermintaanNormal );        
                
    }
    private void InitTablePermintaanDenormal()
    {   
        
        modelPermintaanDenormal= new DefaultTableModel(); 
        modelPermintaanDenormal.addColumn("PERMINTAAN");                                           
        jPermintaanDenormalisasi.setModel(modelPermintaanDenormal );        
                
    }
    private void InitTableMape()
    {   
        
        modelMape= new DefaultTableModel(); 
        modelMape.addColumn("ID");
        modelMape.addColumn("PERMINTAAN");                                           
        jDataMape.setModel(modelMape );        
                
    }
    private void TampilData(){
                try{
            String sql = "SELECT * FROM Fuzzy";
            stt = con.createStatement();
            rss = stt.executeQuery(sql);
            rssd = rss.getMetaData();
            while(rss.next()){            
               Object[] o = new Object[rssd.getColumnCount()];                
               for(int i=1;i<=rssd.getColumnCount();i++){
               o[i-1] = rss.getString(rssd.getColumnName(i));               
               }                
               modelData.addRow(o);
            }     
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    private void TampilMape(){
                try{
            String sql = "SELECT * FROM Mape";
            stt = con.createStatement();
            rss = stt.executeQuery(sql);
            rssd = rss.getMetaData();
            while(rss.next()){            
               Object[] o = new Object[rssd.getColumnCount()];                
               for(int i=1;i<=rssd.getColumnCount();i++){
               o[i-1] = rss.getString(rssd.getColumnName(i));               
               }                
               modelMape.addRow(o);
            }     
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    } 
    
    private void Normalisasi(){
        
//           System.out.println(modelData.getValueAt(1, 1).toString());            
        this.jPilihtahun.removeAllItems();
        Double Temp = 0.0;
        int Temp_2 = 0;
        for(int i = 0;i<modelData.getRowCount();i++){
            Object[] o = new Object[modelData.getRowCount()+1];
            for(int j=0;j<modelData.getColumnCount();j++){                
                if(j != 1 && j != 0){                                                          
                 Temp = Double.parseDouble(modelData.getValueAt(i, j).toString()); 
                    Temp = (0.8*(Double.parseDouble(jdataAwal.getValueAt(i,j).toString())-DataMaxMinAwal(0,j))/
                               ((DataMaxMinAwal(1,j)-DataMaxMinAwal(0,j))))+0.1;
                    o[j] = Temp;                    
                }
                else if(j == 1){
                    if(Temp_2 != Integer.parseInt(modelData.getValueAt(i, j).toString())){
                        jPilihtahun.addItem(modelData.getValueAt(i, j).toString());
                        Temp_2 = Integer.parseInt(modelData.getValueAt(i, j).toString());                        
                    }
                    o[j] = modelData.getValueAt(i, j).toString();
                }
                else{
                    o[j] = modelData.getValueAt(i, j).toString();
                }
            }            
            modelTernormalisasi.addRow(o);
        }                  
    }
    private void Mape(){
       
    }
    private void DataMape(){
 
    }
    
    
    private void HitungMape(){
        Double[] Hitung = new Double[modelPermintaanDenormal.getRowCount()+1];
        Double SumHitung = 0.0;
        Double FinalHitung;
        Double Denormal;
        Double Mape;    
        
        for(int i = 0;i<modelPermintaanDenormal.getColumnCount();i++){//                                            
            for(int j=0;j<modelPermintaanDenormal.getRowCount();j++){    
                Denormal = Double.parseDouble((modelPermintaanDenormal.getValueAt(j, 0).toString()));                                                
                Mape = Double.parseDouble((modelMape.getValueAt(j, 1).toString()));                                                
                Hitung[j] = (Math.abs(Mape-Denormal))/Mape;                
                SumHitung = SumHitung + Hitung[j];
            }            
            }
        FinalHitung = (SumHitung/modelPermintaanDenormal.getRowCount())*100;
        jHasilMape.setText(cf.format(FinalHitung));
        FinalHitung = (FinalHitung*10)/100;                
        jPersentaseMape.setText(df.format(FinalHitung));
        
    }
    
    private Double DataAverageNormalisasi(){
        
        double rowsCount = jTableNormalisasi1.getRowCount();
        double sum =0;
        for(int i = 0; i < jTableNormalisasi1.getRowCount(); i++){
            sum = sum+(Double.parseDouble(jTableNormalisasi1.getValueAt(i,2).toString()));
        }
        double average = sum/rowsCount;
        return average;
        
    }
    
    private Double DataMaxMinAwal(int Check, int Column){
        
           ArrayList<Double> list = new ArrayList<Double>();
        for(int i = 0; i < jdataAwal.getRowCount(); i++){
            list.add(Double.parseDouble(jdataAwal.getValueAt(i,Column).toString()));
        }
        Double max = Collections.max(list);
        Double min = Collections.min(list);
        if(Check == 1)
        return max;
        else
        return min;
    }
    
    //      INI DATA MAX-MIN dari DATA NORMALISASI
    
   private Double DataMaxMinNormalisasi(int Check, int Column){
        
           ArrayList<Double> list = new ArrayList<Double>();
        for(int i = 0; i < jTableNormalisasi1.getRowCount(); i++){
            list.add(Double.parseDouble(jTableNormalisasi1.getValueAt(i,Column).toString()));
        }
        
        Double maxN = Collections.max(list);
        Double minN = Collections.min(list);
        if(Check == 1)
        return maxN;
        else
        return minN;
    } 
    
    private void UjiData(){
        String value = jPilihtahun.getSelectedItem().toString();
        modelUjiData.setRowCount(0);
        for(int i = 0;i<modelTernormalisasi.getRowCount();i++){            
                Object[] o = new Object[modelTernormalisasi.getRowCount()+1];
                
                if(modelTernormalisasi.getValueAt(i, 1).toString().equalsIgnoreCase(jPilihtahun.getSelectedItem().toString())){
                    for(int j=0;j<modelTernormalisasi.getColumnCount();j++){                    
                            if(j!=4 && j!=0){
                                o[j] = modelTernormalisasi.getValueAt(i, j+1).toString();                            
                            }else if(j==0){
                                o[j] = modelTernormalisasi.getValueAt(i, j).toString();                            
                            }
                    }                        
                    modelUjiData.addRow(o);            
                }
        }               
    }
    
    private Double HitungNilai(){
        @SuppressWarnings("UnusedAssignment")
        Double zTot = 0.0;
        Double Stok; 
        Double Penjualan;
        modelPermintaanNormal.setRowCount(0);
        for(int i = 0;i<modelUjiData.getRowCount();i++){
            
            Stok = Double.parseDouble(modelUjiData.getValueAt(i, 1).toString());
            Penjualan = Double.parseDouble(modelUjiData.getValueAt(i, 2).toString());
            
            Object[] o = new Object[modelUjiData.getRowCount()+1];
            for(int j=0;j<modelUjiData.getColumnCount();j++){
                      zTot = ((Inferensi1(Stok,Penjualan)*InferensiZ1(Stok,Penjualan))+(Inferensi2(Stok,Penjualan)*InferensiZ2(Stok,Penjualan))+
                           (Inferensi3(Stok,Penjualan)*InferensiZ3(Stok,Penjualan))+(Inferensi4(Stok,Penjualan)*InferensiZ4(Stok,Penjualan))+
                            (Inferensi5(Stok,Penjualan)*InferensiZ5(Stok,Penjualan))+(Inferensi6(Stok,Penjualan)*InferensiZ6(Stok,Penjualan)))
                            /(Inferensi1(Stok,Penjualan)+Inferensi2(Stok,Penjualan)+Inferensi3(Stok,Penjualan)+Inferensi4(Stok,Penjualan)+
                            Inferensi5(Stok,Penjualan)+Inferensi6(Stok,Penjualan));
                    o[j] = zTot;                    
            }            
            modelPermintaanNormal.addRow(o);
        }  
        return zTot;
    }
    
    private void Denormalisasi(){
        Double Denormal = 0.0;
        Double Permintaan = 0.0;
        modelPermintaanDenormal.setRowCount(0);
        Object[] o = new Object[modelPermintaanNormal.getRowCount()+1];
        for(int i = 0;i<modelPermintaanNormal.getColumnCount();i++){//                                            
            for(int j=0;j<modelPermintaanNormal.getRowCount();j++){    
                Permintaan = Double.parseDouble((modelPermintaanNormal.getValueAt(j, 0).toString()));                                
                Denormal = (((Permintaan-0.1)*(DataMaxMinAwal(1,4)-DataMaxMinAwal(0,4))/0.8)+DataMaxMinAwal(0,4));                                
                o[i] = Denormal;                    
                modelPermintaanDenormal.addRow(o);
                System.out.println(DataMaxMinAwal(0,4));
            }            
            }                            
    }
    
    
    private Double StokSedikit(Double stok){
        double m1_sedikit = 0.0;                 
        Object[] o = new Object[modelUjiData.getRowCount()+1];             
        if(stok<=(DataMaxMinNormalisasi(0,2))) { //nilai MIN
            m1_sedikit=1;
        }
        else if(stok>=(DataMaxMinNormalisasi(1,2))){ //Nilai Max
            m1_sedikit=0;
        }
        else{
            m1_sedikit = ((DataMaxMinNormalisasi(1,2))-stok)/((DataMaxMinNormalisasi(1,2))-(DataMaxMinNormalisasi(0,2)));
        }    
       return m1_sedikit;
   }
    
    private Double StokCukup(Double stok){
        double m1_cukup=6.9;                         
        Object[] o = new Object[modelUjiData.getRowCount()+1]; 
//        System.out.println(DataMaxMinNormalisasi(0,2)+"ini ave"+stok);
        if(stok<=(DataMaxMinNormalisasi(0,2)) || stok>=(DataMaxMinNormalisasi(1,2))) { //nilai MIN
            m1_cukup=0;                        
        }
        else if(stok==(DataAverageNormalisasi())){ 
            m1_cukup=1;            
        }
        else if(stok>=(DataMaxMinNormalisasi(0,2)) && stok<=(DataAverageNormalisasi())){ 
            m1_cukup= (stok-DataMaxMinNormalisasi(0,2))/((DataAverageNormalisasi())-(DataMaxMinNormalisasi(0,2)));              
        }
        else { 
            m1_cukup= (DataMaxMinNormalisasi(1,2)-stok)/((DataMaxMinNormalisasi(1,2))-DataAverageNormalisasi());            
        }        
       return m1_cukup;
   }
    
   private Double StokBanyak(Double stok){
        double m1_banyak = 0.0; 
        Object[] o = new Object[modelUjiData.getRowCount()+1]; 
        if(stok<=(DataAverageNormalisasi())) { //nilai MIN
            m1_banyak=0;
        }
        else if(stok>=(DataMaxMinNormalisasi(1,2))){ //Nilai Max
            m1_banyak=1;
        }
        else{
            m1_banyak = ((stok-(DataAverageNormalisasi()))/((DataMaxMinNormalisasi(1,2))-(DataAverageNormalisasi())));
            
        }
        return m1_banyak;
    }
    
   private Double PenjualanSedikit(Double penjualan){
        double m2_sedikit = 0.0; 
        Object[] o = new Object[modelUjiData.getRowCount()+1]; 
        if(penjualan<=(DataMaxMinNormalisasi(0,3))) { //nilai MIN
            m2_sedikit=1;
        }
        else if(penjualan>=(DataMaxMinNormalisasi(1,3))){ //Nilai Max
            m2_sedikit=0;
        }
        else{
            m2_sedikit = ((DataMaxMinNormalisasi(1,3))-penjualan)/((DataMaxMinNormalisasi(1,3))-(DataMaxMinNormalisasi(0,3)));
        }
       return m2_sedikit;
   }
   
   private Double PenjualanBanyak(Double penjualan){
        double m2_banyak = 0.0; 
        Object[] o = new Object[modelUjiData.getRowCount()+1];                                
        if(penjualan<=(DataMaxMinNormalisasi(0,3))) { //nilai MIN
            m2_banyak=0;
            return m2_banyak;
        }
        else if(penjualan>=(DataMaxMinNormalisasi(1,3))){ //Nilai Max
            m2_banyak=1;
            return m2_banyak;
        }
        else{
            m2_banyak = (penjualan-(DataMaxMinNormalisasi(0,3)))/((DataMaxMinNormalisasi(1,3))-(DataMaxMinNormalisasi(0,3)));
            return m2_banyak;
        }
    }     
   
  // INFERENSI
   
   private Double Inferensi1(Double Stok, Double Penjualan){
       double min_r1;
       min_r1 = Math.min(StokSedikit(Stok),PenjualanSedikit(Penjualan)); // MENCARI NILAI TERKECIL ANTARA 2 NILAI
       return min_r1;
   }
   
   private Double InferensiZ1(Double Stok, Double Penjualan){
       double z_r1;
       if(Inferensi1(Stok,Penjualan)==0){
            z_r1=DataMaxMinNormalisasi(1,4); //nilai MAX
        }
        else if(Inferensi1(Stok,Penjualan)==1){
            z_r1=DataMaxMinNormalisasi(0,4);// nilai MIN
        }
        else                 
            z_r1=DataMaxMinNormalisasi(1,4)-(Inferensi1(Stok,Penjualan)*(DataMaxMinNormalisasi(1,4)-DataMaxMinNormalisasi(0,4)));
        
        return z_r1;        
   }
   
   private Double Inferensi2(Double Stok, Double Penjualan){
       double min_r2;
       min_r2 = Math.min(StokSedikit(Stok),PenjualanBanyak(Penjualan));
       return min_r2;
   }
   
   private Double InferensiZ2(Double Stok, Double Penjualan){
       double z_r2;
       if(Inferensi2(Stok,Penjualan)==0)
            z_r2=DataMaxMinNormalisasi(0,4); //nilai MIN
       else if(Inferensi2(Stok,Penjualan)==1)
            z_r2=DataMaxMinNormalisasi(1,4);// nilai MAX
       else                 
            z_r2=(Inferensi2(Stok,Penjualan)*(DataMaxMinNormalisasi(1,4)-DataMaxMinNormalisasi(0,4))+DataMaxMinNormalisasi(0,4));
       return z_r2;        
   }
   
   private Double Inferensi3(Double Stok, Double Penjualan){
       double min_r3;
       min_r3 = Math.min(StokBanyak(Stok),PenjualanSedikit(Penjualan));
       return min_r3;
   }
   private Double InferensiZ3(Double Stok, Double Penjualan){
       double z_r3;
       if(Inferensi3(Stok,Penjualan)==0)
            z_r3=DataMaxMinNormalisasi(1,4); //nilai MAX
       else if(Inferensi3(Stok,Penjualan)==1)
            z_r3=DataMaxMinNormalisasi(0,4);// nilai MIN
       else                 
            z_r3=DataMaxMinNormalisasi(1,4)-(Inferensi3(Stok,Penjualan)*(DataMaxMinNormalisasi(1,4)-DataMaxMinNormalisasi(0,4)));
        
       return z_r3;        
   }
   
   private Double Inferensi4(Double Stok, Double Penjualan){
       double min_r4;
       min_r4 = Math.min(StokBanyak(Stok),PenjualanBanyak(Penjualan));
       return min_r4;
   }
   
   private Double InferensiZ4(Double Stok, Double Penjualan){
       double z_r4;
       if(Inferensi2(Stok,Penjualan)==0)
            z_r4=DataMaxMinNormalisasi(0,4); //nilai MIN
       else if(Inferensi4(Stok,Penjualan)==1)
            z_r4=DataMaxMinNormalisasi(1,4);// nilai MAX
       else                 
            z_r4=(Inferensi4(Stok,Penjualan)*(DataMaxMinNormalisasi(1,4)-DataMaxMinNormalisasi(0,4))+DataMaxMinNormalisasi(0,4));
       return z_r4;        
   }
   
   private Double Inferensi5(Double Stok, Double Penjualan){
       double min_r5;
       min_r5 = Math.min(StokCukup(Stok),PenjualanSedikit(Penjualan));
       return min_r5;
   }
   
   private Double InferensiZ5(Double Stok, Double Penjualan){
       double z_r5;
       if(Inferensi5(Stok,Penjualan)==0)
            z_r5=DataMaxMinNormalisasi(1,4); //nilai MAX
       else if(Inferensi5(Stok,Penjualan)==1)
            z_r5=DataMaxMinNormalisasi(0,4);// nilai MIN
       else                 
            z_r5=DataMaxMinNormalisasi(1,4)-(Inferensi5(Stok,Penjualan)*(DataMaxMinNormalisasi(1,4)-DataMaxMinNormalisasi(0,4)));
        
       return z_r5;        
   }
   
   private Double Inferensi6(Double Stok, Double Penjualan){
       double min_r6;
       min_r6 = Math.min(StokCukup(Stok),PenjualanBanyak(Penjualan));
       return min_r6;
   }
   
   private Double InferensiZ6(Double Stok, Double Penjualan){
       double z_r6;
       if(Inferensi2(Stok,Penjualan)==0)
            z_r6=DataMaxMinNormalisasi(0,4); //nilai MIN
       else if(Inferensi6(Stok,Penjualan)==1)
            z_r6=DataMaxMinNormalisasi(1,4);// nilai MAX
       else                 
            z_r6=(Inferensi6(Stok,Penjualan)*(DataMaxMinNormalisasi(1,4)-DataMaxMinNormalisasi(0,4))+DataMaxMinNormalisasi(0,4));
       return z_r6;       
   }
   
   
  
//      INI nilai AVERAGE Khusus untuk data STOK dari DATA NORMALISASI
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        ImportBTN = new javax.swing.JButton();
        ResetImportBTN = new javax.swing.JButton();
        NormalisasiBTN = new javax.swing.JButton();
        TombolImportBNT1 = new javax.swing.JButton();
        HitungNilaiBTN = new javax.swing.JButton();
        jPilihtahun = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTableNormalisasi1 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPermintaanDenormalisasi = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jDataMape = new javax.swing.JTable();
        HitungMapeImportBTN = new javax.swing.JButton();
        jHasilMape = new javax.swing.JTextField();
        jPersentaseMape = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jdataAwal = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableUji1 = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPermintaanNormalisasi = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        TombolMAPEImportBNT2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 153, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 51, 0)));
        jPanel1.setForeground(new java.awt.Color(0, 51, 51));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(153, 153, 255));
        jPanel2.setLayout(null);

        ImportBTN.setText("Import From Excel");
        ImportBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ImportBTNActionPerformed(evt);
            }
        });
        jPanel2.add(ImportBTN);
        ImportBTN.setBounds(10, 10, 160, 23);

        ResetImportBTN.setText("Reset");
        ResetImportBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetImportBTNActionPerformed(evt);
            }
        });
        jPanel2.add(ResetImportBTN);
        ResetImportBTN.setBounds(300, 50, 77, 23);

        NormalisasiBTN.setText("Normalisasi Data");
        NormalisasiBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NormalisasiBTNActionPerformed(evt);
            }
        });
        jPanel2.add(NormalisasiBTN);
        NormalisasiBTN.setBounds(10, 50, 160, 23);

        TombolImportBNT1.setText("Update");
        TombolImportBNT1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TombolImportBNT1ActionPerformed(evt);
            }
        });
        jPanel2.add(TombolImportBNT1);
        TombolImportBNT1.setBounds(280, 10, 100, 23);

        HitungNilaiBTN.setText("Hitung");
        HitungNilaiBTN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                HitungNilaiBTNMouseClicked(evt);
            }
        });
        HitungNilaiBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HitungNilaiBTNActionPerformed(evt);
            }
        });
        jPanel2.add(HitungNilaiBTN);
        HitungNilaiBTN.setBounds(180, 50, 109, 23);

        jPilihtahun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPilihtahunActionPerformed(evt);
            }
        });
        jPanel2.add(jPilihtahun);
        jPilihtahun.setBounds(190, 10, 70, 20);

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 390, 80));

        jLabel9.setText("DATA AWAL");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 120, -1, -1));

        jTableNormalisasi1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Tahun", "Stok", "Penjualan", "Permintaan"
            }
        ));
        jScrollPane6.setViewportView(jTableNormalisasi1);

        jPanel1.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 430, 220));

        jPermintaanDenormalisasi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Permintaan "
            }
        ));
        jScrollPane4.setViewportView(jPermintaanDenormalisasi);

        jPanel1.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 140, 100, 220));

        jLabel8.setText("DENORMALISASI");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 120, -1, -1));

        jDataMape.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "No.", "Permintaan "
            }
        ));
        jScrollPane5.setViewportView(jDataMape);

        jPanel1.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 390, 190, 220));

        HitungMapeImportBTN.setText("Hitung MAPE");
        HitungMapeImportBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HitungMapeImportBTNActionPerformed(evt);
            }
        });
        jPanel1.add(HitungMapeImportBTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 390, -1, -1));

        jHasilMape.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jHasilMapeActionPerformed(evt);
            }
        });
        jPanel1.add(jHasilMape, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 430, 99, 30));

        jPersentaseMape.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPersentaseMapeActionPerformed(evt);
            }
        });
        jPanel1.add(jPersentaseMape, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 470, 99, 30));

        jLabel11.setText("%");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 480, -1, -1));

        jdataAwal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Tahun", "Stok", "Penjualan", "Permintaaan"
            }
        ));
        jScrollPane1.setViewportView(jdataAwal);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 430, 220));

        jTableUji1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "No.", "Stok", "Penjualan"
            }
        ));
        jScrollPane2.setViewportView(jTableUji1);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 140, 260, 220));

        jLabel7.setText("DATA UJI");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 120, -1, -1));

        jPermintaanNormalisasi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Permintaan "
            }
        ));
        jScrollPane3.setViewportView(jPermintaanNormalisasi);

        jPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 140, 100, 220));

        jLabel12.setText("NORMALISASI");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 120, -1, -1));

        jLabel13.setText("DATA NORMALISASI");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 370, -1, -1));

        TombolMAPEImportBNT2.setText("Import Data Mape");
        TombolMAPEImportBNT2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TombolMAPEImportBNT2ActionPerformed(evt);
            }
        });
        jPanel1.add(TombolMAPEImportBNT2, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 410, 120, -1));

        jPanel3.setBackground(new java.awt.Color(51, 255, 102));
        jPanel3.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED)));
        jPanel3.setForeground(new java.awt.Color(153, 255, 102));
        jPanel3.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setText("PREDIKSI PERMINTAAN SEMEN");
        jPanel3.add(jLabel1);
        jLabel1.setBounds(70, 30, 390, 50);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel2.setText("FUZZY TSUKAMOTO");
        jPanel3.add(jLabel2);
        jLabel2.setBounds(140, 0, 270, 40);

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, 520, 80));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 960, 630));

        pack();
    }// </editor-fold>//GEN-END:initComponents
   
    private void HitungMapeImportBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HitungMapeImportBTNActionPerformed
        HitungMape();
    }//GEN-LAST:event_HitungMapeImportBTNActionPerformed

    private void jHasilMapeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jHasilMapeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jHasilMapeActionPerformed

    private void jPersentaseMapeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPersentaseMapeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPersentaseMapeActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_formComponentShown

    private void HitungNilaiBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HitungNilaiBTNActionPerformed
        HitungNilai();        
        Denormalisasi();
    }//GEN-LAST:event_HitungNilaiBTNActionPerformed

    private void HitungNilaiBTNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HitungNilaiBTNMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_HitungNilaiBTNMouseClicked

    private void TombolImportBNT1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TombolImportBNT1ActionPerformed
        UjiData();
    }//GEN-LAST:event_TombolImportBNT1ActionPerformed

    private void NormalisasiBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NormalisasiBTNActionPerformed
        Normalisasi();
    }//GEN-LAST:event_NormalisasiBTNActionPerformed

    private void ResetImportBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResetImportBTNActionPerformed
        Reset();
    }//GEN-LAST:event_ResetImportBTNActionPerformed

    private void ImportBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImportBTNActionPerformed
        JFileChooser fileopen = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("c files", "c");
        fileopen.addChoosableFileFilter(filter);

        int ret = fileopen.showDialog(null, "Open file");

        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
            Awal test = new Awal();
            setInputFile(file.toString());
            try {
                ReadFile();
            } catch (IOException | BiffException ex) {
                System.out.println(ex);
            }
        }
        InitTableData();
        InitTableNormalisasi();
        InitTableHitung();
        InitTableUjiData();
        InitTablePermintaanNormal();
        InitTablePermintaanDenormal();
        
        TampilData();
        Mape();
    }//GEN-LAST:event_ImportBTNActionPerformed

    private void jPilihtahunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPilihtahunActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPilihtahunActionPerformed

    private void TombolMAPEImportBNT2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TombolMAPEImportBNT2ActionPerformed
       JFileChooser fileopen = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("c files", "c");
        fileopen.addChoosableFileFilter(filter);

        int ret = fileopen.showDialog(null, "Open file");

        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
            Awal test2 = new Awal();
            setInputFile(file.toString());
            try {
                ReadFile_Mape();
            } catch (IOException | BiffException ex) {
                System.out.println(ex);
            }
        }
        InitTableMape();
        TampilMape();
    }//GEN-LAST:event_TombolMAPEImportBNT2ActionPerformed
    
    private void Reset(){
        
        modelMape.setRowCount(0);
        jHasilMape.setText("");
        jPersentaseMape.setText("");
        modelPermintaanDenormal.setRowCount(0);       
        modelPermintaanNormal.setRowCount(0);
        modelUjiData.setRowCount(0);
        jPilihtahun.removeAllItems();
        modelTernormalisasi.setRowCount(0);
        modelData.setRowCount(0); 
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Awal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Awal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Awal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Awal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Awal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton HitungMapeImportBTN;
    private javax.swing.JButton HitungNilaiBTN;
    private javax.swing.JButton ImportBTN;
    private javax.swing.JButton NormalisasiBTN;
    private javax.swing.JButton ResetImportBTN;
    private javax.swing.JButton TombolImportBNT1;
    private javax.swing.JButton TombolMAPEImportBNT2;
    private javax.swing.JTable jDataMape;
    private javax.swing.JTextField jHasilMape;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTable jPermintaanDenormalisasi;
    private javax.swing.JTable jPermintaanNormalisasi;
    private javax.swing.JTextField jPersentaseMape;
    private javax.swing.JComboBox<String> jPilihtahun;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable jTableNormalisasi1;
    private javax.swing.JTable jTableUji1;
    private javax.swing.JTable jdataAwal;
    // End of variables declaration//GEN-END:variables

   

    

    private void InitTableHitung() {
        
    }



}

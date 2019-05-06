package com.example.rbbncasemanagementtool;



import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;


public class Overview extends AppCompatActivity {
    String TAG ="main";
    //-------
    String columnSeverity = "Severity";
    String filterCritical = "Critical";
    String filterE2 = "E2";
    String columnOutFolUp = "Outage Follow-Up";
    String filter1 = "1";
    String columnEscaltBy = "Escalated By";
    String filterNotSet = "NotSet";
    String filterBusCritic = "Business Critical";
    String columnSupHotLvl = "Support Hotlist Level";
    String columnCurrentRes = "Currently Responsible";
    String filterCustAct = "Customer action";
    String filterCustUpd = "Customer updated";
    String filterMajor = "Major";
    String filterMinor = "Minor";
    String columnCaseOwn = "Case Owner";
    String filterTS = "TS";
    String e2TableSelectPS = "PS";
    String columnNextCaseUpd = "Next Case Update";
    String columnStatus = "Status";
    String filterDevSolution = "Develop Solution";
    TextView tv;
    boolean viewChanged ;
    int clicked=0;
    int rowCount;

    Button btnE1Cases, btnE2Cases, btnOutFollow, btnEscalated, btnBCCases, btnHotIssues;



    //-------
    int e1Cases,e2Cases,outFollow,queueTS,queuePS,updateToday,updateMissed,updateNull,hotlist,escCase,bcCases,inactiveCases,
            wohCases,bcDue,misBCdue,custActBC,custRpdBC,BCds,BCpc,BCwip,dueMJday,misMJdue,misMNdue,custActMJ,custRpdMJ,
            MJds,MJpc,MJwip= 0;
    //-------
    int caseAccountRef,caseNumCellRef,caseSupTypeRefCell,caseStatRefCell,caseSevRefCell,caseRespRefCell,caseOwnerRefCell,
            caseCoOwnerRefCell,caseEscalatedRefCell,caseHotListRefCell,caseOutFolRefCell,caseAgeRefCell,mycaseNumCellRef,
            mycaseSupTypeRefCell,mycaseStatRefCell,mycaseSevRefCell,mycaseRespRefCell,mycaseOwnerRefCell,mycaseEscalatedRefCell,
            mycaseHotListRefCell,mycaseOutFolRefCell,mycaseAgeRefCell,mycaseUpdateCell,myCoOwnCaseRefCell,myCoOwnQueueRefCell= 0;
    int caseCellRef,caseCellRef2,myCaseCellRef1,caseNextUpdateDateRef,caseProductRef,customerE1,customerE2,customerOutFol,
            customerHot,customerEsc,customerBC,customerWoh= 0;
    //-----------------
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_overview);
        insertNumbers();


        //textView = findViewById(R.id.textview);
        //readExcelFileFromAssets();

       // findCaseCounts();
        ClickEvents();

       // Toast.makeText(this, "This is my Toast message!",
               // Toast.LENGTH_LONG).show();

    }
    //---------One Filter Overview------------------
    //category based filter
    private void oneFilterTableView(String columnSelect, String filter1, Boolean bool) {

        int caseCount = 0;
        TableLayout stk = (TableLayout) findViewById(R.id.table_main);


        try  {
            InputStream myInput;
            // Initialize asset manager
            AssetManager assetManager = getAssets();
            //  Open excel sheet
            myInput = assetManager.open("cmt_case_data_V3.xls");
            // Create a POI File System object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator<Row> rowIter = mySheet.rowIterator();
            // Iterate through the cells.
           // Iterator<Row> rowIter = mySheet.rowIterator();
            //---------------------------------------------
            int cellnum = mySheet.getRow(0).getLastCellNum();
            int lastRow = mySheet.getLastRowNum();
            HSSFCell cellVal;
            HSSFCell cellVal2;

            for (int i = 0; i < cellnum; i++) {

                String filterColName = mySheet.getRow(0).getCell(i).toString();
                if (filterColName.equals(columnSelect)) {
                    caseCellRef = i;
                }
                if (filterColName.equals("Age (Days)")) {
                    caseAgeRefCell = i;
                }
                if (filterColName.equals("Next Case Update")) {
                    caseNextUpdateDateRef = i;
                }
                if (filterColName.equals("Status")) {
                    caseStatRefCell = i;
                }
            }
            for (int k = 0; k < lastRow + 1; k++) {
                TableRow tbrow = new TableRow(this);
                tbrow.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                cellVal = mySheet.getRow(k).getCell(caseCellRef);
                String cellValToCompare = cellVal.getStringCellValue();
                cellVal2 = mySheet.getRow(k).getCell(caseStatRefCell);
                String caseStatus = cellVal2.getStringCellValue();

                if (!bool) {
                    if (!cellValToCompare.equals(filter1) && !caseStatus.equals("Pending Closure") && !caseStatus.equals("Future Availability")) {
                        //ArrayList<String> array = new ArrayList<>();
                        int cellnumara = mySheet.getRow(k).getLastCellNum();
                        Iterator<Cell> iterCells = mySheet.getRow(k).cellIterator();
                        for (int i = 0; i <cellnumara; i++) {

                            //array.add(mySheet.getRow(k).getCell(i).toString());

                            TextView txv = new TextView(this);
                            txv.setText( mySheet.getRow(k).getCell(i).toString());
                            txv.setTextColor(Color.WHITE);
                            txv.setPadding(110,0,110,0);
                            txv.setGravity(3);
                            tbrow.setGravity(3);
                            tbrow.setTag(i);
                            tbrow.setId(i);
                            tbrow.addView(txv);
                            tbrow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    for(int i = 0, j = stk.getChildCount(); i < j; i++) {
                                        View view = stk.getChildAt(i);
                                        if (view instanceof TableRow) {
                                            // then, you can remove the the row you want...
                                            // for instance...
                                            TableRow row = (TableRow) view;
                                            row.setBackgroundColor(Color.parseColor("#3d455b"));

                                        }
                                    }
                                        tbrow.setBackgroundColor(Color.BLUE);



                                                                    }
                            });
                        }
                        stk.addView(tbrow);


                    }
                } else {
                    if (cellValToCompare.equals(filter1) && (!caseStatus.equals("Pending Closure") && !caseStatus.equals("Future Availability"))) {
                        //ArrayList<String> array = new ArrayList<>();
                        int cellnumara = mySheet.getRow(k).getLastCellNum();
                        Iterator<Cell> iterCells = mySheet.getRow(k).cellIterator();
                        for (int i = 0; i <cellnumara; i++) {

                            //array.add(mySheet.getRow(k).getCell(i).toString());

                            TextView txv = new TextView(this);
                            txv.setText( mySheet.getRow(k).getCell(i).toString());
                            txv.setTextColor(Color.WHITE);
                            txv.setPadding(110,0,110,0);
                            //txv.setRotationX();
                            txv.setGravity(3);
                            tbrow.setGravity(3);
                            tbrow.setTag(i);
                            tbrow.setId(i);
                            tbrow.addView(txv);
                            tbrow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    for(int i = 0, j = stk.getChildCount(); i < j; i++) {
                                        View view = stk.getChildAt(i);
                                        if (view instanceof TableRow) {
                                            // then, you can remove the the row you want...
                                            // for instance...
                                            TableRow row = (TableRow) view;
                                            row.setBackgroundColor(Color.parseColor("#3d455b"));

                                        }
                                    }

                                        tbrow.setBackgroundColor(Color.BLUE);





                                }
                            });

                        }
                        stk.addView(tbrow);


                    }
                }



            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //------------One Filter Overview----------------


        public void ClickEvents(){
            btnE1Cases =  findViewById(R.id.btnE1Cases) ;
            btnE2Cases =  findViewById(R.id.btnE2Cases) ;
            btnOutFollow =  findViewById(R.id.btnOutFollow) ;
            btnEscalated =  findViewById(R.id.btnEscalated) ;
            btnBCCases =  findViewById(R.id.btnBCCases) ;
            btnHotIssues =  findViewById(R.id.btnHotIssues) ;

            btnE1Cases.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    setContentView(R.layout.activity_overview);
                    oneFilterTableView(columnSeverity, filterCritical, true);
                    viewChanged=true;
                    tv= findViewById(R.id.tV);
                    tv.setText("CRITICAL (OUTAGE) CASES");
                }
            });
            btnE2Cases.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    setContentView(R.layout.activity_overview);
                    oneFilterTableView(columnSeverity, filterE2,  true);
                    viewChanged=true;
                    tv= findViewById(R.id.tV);
                    tv.setText("E2 CASES");
                }
            });
            btnOutFollow.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    setContentView(R.layout.activity_overview);
                    oneFilterTableView(columnOutFolUp, filter1, true );
                    viewChanged=true;
                    tv= findViewById(R.id.tV);
                    tv.setText("E1 FOLLOW-UP CASES");
                }
            });
            btnEscalated.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    setContentView(R.layout.activity_overview);
                    oneFilterTableView(columnEscaltBy, filterNotSet, false );
                    viewChanged=true;
                    tv= findViewById(R.id.tV);
                    tv.setText("ESCALATED CASES");
                }
            });
            btnBCCases.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    setContentView(R.layout.activity_overview);
                    oneFilterTableView(columnSeverity, filterBusCritic, true);
                    viewChanged=true;
                    tv= findViewById(R.id.tV);
                    tv.setText("BUISNESS CRITICAL CASES");
                }
            });
            btnHotIssues.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    setContentView(R.layout.activity_overview);
                    oneFilterTableView(columnSupHotLvl, filterNotSet, false);
                    viewChanged=true;
                    tv= findViewById(R.id.tV);
                    tv.setText("HOT ISSUES");
                }
            });




        }

    @Override
    public void onBackPressed() {

        if(viewChanged == true){
            setContentView(R.layout.general_overview);
            viewChanged = false;
            ClickEvents();
            insertNumbers();
        }
        else{
            super.onBackPressed();
        }
    }


    //-------------------------------------------------------------------------------------------------------------

    //-------------------------------------------------------------------------------------------------------------
    public void insertNumbers(){
        btnE1Cases =  findViewById(R.id.btnE1Cases) ;
        btnE2Cases =  findViewById(R.id.btnE2Cases) ;
        btnOutFollow =  findViewById(R.id.btnOutFollow) ;
        btnEscalated =  findViewById(R.id.btnEscalated) ;
        btnBCCases =  findViewById(R.id.btnBCCases) ;
        btnHotIssues =  findViewById(R.id.btnHotIssues) ;
        MainActivity main = new MainActivity();
        int critical = getIntent().getIntExtra("critical",0);
        int e2 = getIntent().getIntExtra("e2",0);
        int folUp = getIntent().getIntExtra("folUp",0);
        int escalt = getIntent().getIntExtra("escalt",0)-1;
        int busCritic = getIntent().getIntExtra("busCritic",0);
        int hot = getIntent().getIntExtra("hot",0)-1;


        btnE1Cases.setText(btnE1Cases.getText()+"\n"+critical);
        btnE2Cases.setText(btnE2Cases.getText()+"\n"+e2);
        btnOutFollow.setText(btnOutFollow.getText()+"\n"+folUp);
        btnEscalated.setText(btnEscalated.getText()+"\n"+escalt);
        btnBCCases.setText(btnBCCases.getText()+"\n"+busCritic);
        btnHotIssues.setText(btnHotIssues.getText()+"\n"+hot);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu, menu);
        menu.add(0, 1, 1, "asd").setIcon(R.drawable.ic_file_download).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.info:
                new AlertDialog.Builder(this)
                        .setTitle("RBBN CMT")
                        .setMessage("For any issues/requests please inform us:" + "\n"+ "\n"
                        +"Alper Simsek  asimsek@rbbn.com" + "\n"+ "\n"
                        +"Vehbi Benli  vbenli@rbbn.com" + "\n"+ "\n"
                        +"RBBN RSD Version 1.07")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
        }

        return super.onOptionsItemSelected(item);
    }

public void createFile(){

}


}

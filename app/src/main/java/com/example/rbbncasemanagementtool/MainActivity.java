package com.example.rbbncasemanagementtool;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.InputStream;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    //---
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
    int  criticalRowCount, e2RowCount, folUpRowCount, escaltRowCount, busCriticRowCount, hotRowCount;
    //---
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
    Toolbar toolbar;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Typeface font= Typeface.createFromAsset(getAssets(), "fonts/fa-regular-400.ttf");
        //toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryText));

        findCaseCounts();
        Toast.makeText(this, "This is my Toast message!"+busCriticRowCount,
                Toast.LENGTH_LONG).show();
        btn = findViewById(R.id.oV);
        btn.setTypeface(font);
         btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getApplicationContext(), Overview.class);
                 intent.putExtra("critical", criticalRowCount);
                 intent.putExtra("e2", e2RowCount);
                 intent.putExtra("folUp", folUpRowCount);
                 intent.putExtra("escalt", escaltRowCount);
                 intent.putExtra("busCritic", busCriticRowCount);
                 intent.putExtra("hot", hotRowCount);
                 startActivity(intent);
             }
         });
    }
    //-------------------------------------------------------------------------------------------------------------
    private int oneFilterTableView1(String columnSelect, String filter1, Boolean bool, int rowCount) {




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
                //TableRow tbrow = new TableRow(this);
                // tbrow.setLayoutParams(new ViewGroup.LayoutParams(
                // ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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


                        }
                        rowCount++;




                    }
                } else {
                    if (cellValToCompare.equals(filter1) && (!caseStatus.equals("Pending Closure") && !caseStatus.equals("Future Availability"))) {
                        //ArrayList<String> array = new ArrayList<>();
                        int cellnumara = mySheet.getRow(k).getLastCellNum();
                        Iterator<Cell> iterCells = mySheet.getRow(k).cellIterator();
                        for (int i = 0; i <cellnumara; i++) {



                        }
                        rowCount++;



                    }
                }



            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowCount;

    }

    public void findCaseCounts(){



        criticalRowCount=oneFilterTableView1(columnSeverity, filterCritical, true, criticalRowCount);
        //criticalRowCount =rowCount;
        e2RowCount=oneFilterTableView1(columnSeverity, filterE2,  true, e2RowCount);
       // e2RowCount =rowCount;
        folUpRowCount=oneFilterTableView1(columnOutFolUp, filter1, true, folUpRowCount );
        //folUpRowCount =rowCount;
        escaltRowCount=oneFilterTableView1(columnEscaltBy, filterNotSet, false, escaltRowCount );
        //escaltRowCount =rowCount;
        busCriticRowCount=oneFilterTableView1(columnSeverity, filterBusCritic, true, busCriticRowCount);
        //busCriticRowCount =rowCount;
        hotRowCount=oneFilterTableView1(columnSupHotLvl, filterNotSet, false, hotRowCount);
        //hotRowCount =rowCount;
    }

    //-------------------------------------------------------------------------------------------------------------

//---

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu, menu);
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
}

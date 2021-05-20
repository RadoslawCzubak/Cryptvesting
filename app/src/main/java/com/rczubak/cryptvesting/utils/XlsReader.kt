package com.rczubak.cryptvesting.utils

import android.content.Context
import android.util.Log
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import timber.log.Timber
import java.io.InputStream

class XlsReader {
    fun readXls(context: Context){
        val inputStream: InputStream = context.assets.open("transactions.xlsx")
        val workspace = XSSFWorkbook(inputStream)
        val sheet = workspace.getSheetAt(0)
        sheet.removeRow(sheet.getRow(0))
        for(row in sheet.rowIterator()){
            for(cell in row.cellIterator()) {
                Log.d("Radek123", cell.stringCellValue)
            }
        }
    }
}
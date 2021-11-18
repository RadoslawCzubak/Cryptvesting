package com.rczubak.cryptvesting.common

import com.rczubak.cryptvesting.domain.model.TransactionModel
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream

class XlsReader {
    /**
     * This method read Binance transactions history from .xlsx file.
     *
     * <p>
     * File contains:
     * First row is for headers.
     * |---------------------Headers--------------------|
     * |Date|Market|Type|Price|Amount|Total|Fee|Fee Coin|
     */


    fun readTransactionsFromXlsx(xlsxInputStream: InputStream): ArrayList<TransactionModel> {
        val workspace = XSSFWorkbook(xlsxInputStream)
        val sheet = workspace.getSheetAt(0)
        sheet.removeRow(sheet.getRow(0))
        val transactions = ArrayList<TransactionModel>()
        for (row in sheet.rowIterator()) {
            val transactionRowStrings: ArrayList<String> = ArrayList()
            for (cell in row.cellIterator()) {
                transactionRowStrings.add(cell.stringCellValue)
            }
            val splittedCoins =
                splitMarketCoins(transactionRowStrings[TransactionsHeaders.MARKET.index])
            val buyCoin = splittedCoins[0]
            val sellCoin = splittedCoins[1]
            transactions.add(
                TransactionModel.createFromStrings(
                    buyCoin,
                    sellCoin,
                    transactionRowStrings[TransactionsHeaders.PRICE.index],
                    transactionRowStrings[TransactionsHeaders.AMOUNT.index],
                    transactionRowStrings[TransactionsHeaders.TYPE.index],
                    transactionRowStrings[TransactionsHeaders.DATE.index],
                )
            )
        }
        return transactions
    }

    /** Temporarily only USDT as sell coin */
    private fun splitMarketCoins(market: String): List<String> {
        return listOf(market.replace("USDT", ""), "USDT")
    }

    private enum class TransactionsHeaders(val index: Int) {
        DATE(0),
        MARKET(1),
        TYPE(2),
        PRICE(3),
        AMOUNT(4),
        TOTAL(5),
        FEE(6),
        FEE_COIN(7)
    }
}
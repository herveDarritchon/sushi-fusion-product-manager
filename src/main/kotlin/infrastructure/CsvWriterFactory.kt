package infrastructure

import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import com.github.doyaaaaaken.kotlincsv.dsl.context.WriteQuoteMode
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter

object CsvWriterFactory {
    fun build(separator: Char = ';'): CsvWriter = csvWriter {
        charset = "UTF-8"
        delimiter = separator
        nullCode = ""
        lineTerminator = "\n"
        outputLastLineTerminator = true
        quote {
            mode = WriteQuoteMode.ALL
            char = '\"'
        }
    }
}
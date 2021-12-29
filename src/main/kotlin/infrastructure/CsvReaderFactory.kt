package infrastructure

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader

object CsvReaderFactory {
    fun build(separator: Char = ','): CsvReader {
        return csvReader {
            charset = "UTF-8"
            quoteChar = '"'
            delimiter = separator
        }
    }
}
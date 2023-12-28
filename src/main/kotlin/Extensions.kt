import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


fun OffsetDateTime.toDb(): String = this.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

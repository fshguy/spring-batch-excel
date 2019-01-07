package org.springframework.batch.item.excel;

/**
 * Created by Krishna Mishra on 10/17/2016.
 */

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.excel.mapping.PassThroughRowMapper;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Created by mishrk3 on 3/17/2016.
 */
public class PoiItemReaderWithBlankRowSheetTest {

	private final Log logger = LogFactory.getLog(this.getClass());

	private AbstractExcelItemReader<String[]> itemReader;

	@Before
	public void setup() throws Exception {
		this.itemReader = new PoiItemReader<>();
		this.itemReader.setResource(new ClassPathResource("blankRow.xlsx"));
		this.itemReader.setLinesToSkip(1); // First line is column names
		this.itemReader.setRowMapper(new PassThroughRowMapper());
		this.itemReader.setSkippedRowsCallback(rs -> logger.info("Skipping: " + Arrays.toString(rs.getCurrentRow())));
		this.itemReader.afterPropertiesSet();

		ExecutionContext executionContext = new ExecutionContext();
		this.itemReader.open(executionContext);
	}

	@Test
	public void readExcelFileWithBlankRow() throws Exception {
		assertEquals(1, this.itemReader.getNumberOfSheets());
		String[] row;
		do {
			row = this.itemReader.read();
			this.logger.debug("Read: " + Arrays.toString(row));
			if (row != null) {
				assertEquals(4, row.length);
			}
		} while (row != null);
		int readCount = (Integer) ReflectionTestUtils.getField(this.itemReader, "currentItemCount");
		assertEquals(7, readCount);
	}
}
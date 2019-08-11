package sales;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.crypto.Data;
import java.util.*;

import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class SalesAppTest {

	@Mock
	SalesReportDao salesReportDao;
	@Mock
	SalesDao salesDao;
	@InjectMocks
	SalesApp salesApp = new SalesApp();

	@Test
	public void testGenerateReport() {
		
		SalesApp salesApp = new SalesApp();
		salesApp.generateSalesActivityReport("DUMMY", 1000, false, false);
		
	}

	@Test
	public void testGetSalesReportDataByMaxRow_givenReportDataListAndMaxRow_thenReturnMaxRowList() {
		//given
		SalesReportData salesReportData =new SalesReportData();
		SalesReportData salesReportData1 = new SalesReportData();
		List<SalesReportData> salesReportDataList = new ArrayList<>();
		salesReportDataList.add(salesReportData);
		salesReportDataList.add(salesReportData1);
		//when
		SalesApp salesApp = new SalesApp();
		List<SalesReportData> salesReportDataByMaxRow = salesApp.getSalesReportDataByMaxRow(1, salesReportDataList);
		int result = salesReportDataByMaxRow.size();
		//then
		Assert.assertEquals(1, result);

	}

	@Test
	public void testGetFilteredReportDataList_givenIsSupervisorTrue_thenReturnFilteredReportListSizeEqualOne() {
		//given
		SalesReportData salesReportData = Mockito.mock(SalesReportData.class);
		Mockito.when(salesReportData.getType()).thenReturn("SalesActivity");
		Mockito.when(salesReportData.isConfidential()).thenReturn(true);
		boolean isSupervisor = true;
		List<SalesReportData> salesReportDataList = new ArrayList<>();
		salesReportDataList.add(salesReportData);
		//when
		SalesApp salesApp = new SalesApp();
		List<SalesReportData> resultList = salesApp.getFilteredReportDataList(isSupervisor, salesReportDataList);
		int resultSize = resultList.size();
		//then
		Assert.assertEquals(1, resultSize);

	}

	@Test
	public void testGetFilteredReportDataList_givenIsSupervisorFalse_thenReturnFilteredReportListSizeEqualZero() {
		//given
		SalesReportData salesReportData = Mockito.mock(SalesReportData.class);
		Mockito.when(salesReportData.getType()).thenReturn("SalesActivity");
		Mockito.when(salesReportData.isConfidential()).thenReturn(true);
		boolean isSupervisor = false;
		List<SalesReportData> salesReportDataList = new ArrayList<>();
		salesReportDataList.add(salesReportData);
		//when
		SalesApp salesApp = new SalesApp();
		List<SalesReportData> resultList = salesApp.getFilteredReportDataList(isSupervisor, salesReportDataList);
		int resultSize = resultList.size();
		//then
		Assert.assertEquals(0, resultSize);

	}

	@Test
	public void testGetHeaders_givenIsNatTradeTrue_thenReturnListContainTime() {
		//given
		boolean isNatTrade = true;
		//when
		SalesApp salesApp = new SalesApp();
		List<String> headers = salesApp.getHeaders(isNatTrade);
		boolean result = headers.contains("Time");
		//then
		Assert.assertTrue(result);

	}

	@Test
	public void testGetSalesReportDataList_givenSales_thenGetDataFromDao() {
		//given
		Sales sales = new Sales();
		//when
		List<SalesReportData> result = salesApp.getSalesReportDataList(sales);
		//then
		Mockito.verify(salesReportDao, Mockito.times(1)).getReportData(any());

	}

	@Test
	public void testGetSalesBySalesId_givenSalesId_thenGetDataFromSalesDao() {
		//given
		String salesId = "test";
		//when
		Sales result = salesApp.getSales(salesId);
		//then
		Mockito.verify(salesDao, Mockito.times(1)).getSalesBySalesId(any());
	}

	@Test
	public void testIsSaleValid_givenSalesEffectiveToBeforeToday_thenReturnFalse() {
		//given
		String salesId = "test";
		Sales sales = Mockito.mock(Sales.class);
		Date beforeToday = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(beforeToday);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		beforeToday = calendar.getTime();
		Mockito.when(sales.getEffectiveTo()).thenReturn(beforeToday);
		//when
		boolean result = salesApp.isSaleValid(salesId, sales);
		//then
		Assert.assertFalse(result);
	}




}

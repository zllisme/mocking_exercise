package sales;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SalesApp {

    private SalesReportDao salesReportDao;

    private SalesDao salesDao;

    public SalesApp() {
        this.salesReportDao = new SalesReportDao();
        this.salesDao = new SalesDao();
    }

    public void generateSalesActivityReport(String salesId, int maxRow, boolean isNatTrade, boolean isSupervisor) {
		Sales sales = getSales(salesId);
		if(!isSaleValid(salesId, sales)) {
			return;
		}
		List<SalesReportData> reportDataList = getSalesReportDataList(sales);

		List<SalesReportData> filteredReportDataList = getFilteredReportDataList(isSupervisor, reportDataList);
		reportDataList = filteredReportDataList;

		List<SalesReportData> tempList = getSalesReportDataByMaxRow(maxRow, reportDataList);
		reportDataList = tempList;

		List<String> headers = getHeaders(isNatTrade);
		SalesActivityReport report = this.generateReport(headers, reportDataList);
		
		EcmService ecmService = new EcmService();
		ecmService.uploadDocument(report.toXml());
		
	}

	public boolean isSaleValid(String salesId, Sales sales) {
		boolean result = true;
    	if (salesId == null) {
			result = false;
		}

		Date today = new Date();
		if (today.after(sales.getEffectiveTo())
				|| today.before(sales.getEffectiveFrom())){
			result = false;
		}
		return result;
	}

	public List<SalesReportData> getSalesReportDataByMaxRow(int maxRow, List<SalesReportData> reportDataList) {
		List<SalesReportData> tempList = new ArrayList<SalesReportData>();
		for (int i=0; i < reportDataList.size() && i < maxRow; i++) {
			tempList.add(reportDataList.get(i));
		}
		return tempList;
	}

	public List<SalesReportData> getFilteredReportDataList(boolean isSupervisor, List<SalesReportData> reportDataList) {
		List<SalesReportData> filteredReportDataList = new ArrayList<SalesReportData>();

		for (SalesReportData data : reportDataList) {
			if ("SalesActivity".equalsIgnoreCase(data.getType())) {
				if (data.isConfidential()) {
					if (isSupervisor) {
						filteredReportDataList.add(data);
					}
				}else {
					filteredReportDataList.add(data);
				}
			}
		}
		return filteredReportDataList;
	}


	public List<SalesReportData> getSalesReportDataList(Sales sales) {
		return salesReportDao.getReportData(sales);
	}

	public Sales getSales(String salesId) {
		return salesDao.getSalesBySalesId(salesId);
	}

	public List<String> getHeaders(boolean isNatTrade) {
		List<String> headers = null;

		if (isNatTrade) {
			headers = Arrays.asList("Sales ID", "Sales Name", "Activity", "Time");
		} else {
			headers = Arrays.asList("Sales ID", "Sales Name", "Activity", "Local Time");
		}
		return headers;
	}

	private SalesActivityReport generateReport(List<String> headers, List<SalesReportData> reportDataList) {
		// TODO Auto-generated method stub
		return null;
	}

}

package app.juntrack.chart.client.request;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import app.juntrack.common.data.domain.LiveRecord;
import app.juntrack.common.data.mapper.LiveRecordMapper;

@Component
public class ChartApiRequestJsonCreator {

	@Autowired
	protected LiveRecordMapper recordMapper;

	private static SimpleDateFormat FORMATTER = new SimpleDateFormat("M/d HH:mm");

	public String createRequestJson(String contentId) {
		List<LiveRecord> liveRecords = recordMapper.selectByContentIdOrderByRegisteredAtAsc(contentId);
		return new Gson().toJson(createChartApiRequest(liveRecords));
	}

	private ChartApiRequest createChartApiRequest(List<LiveRecord> liveRecords) {
		ChartApiRequest request = new ChartApiRequest();
		request.setChart(createChart(liveRecords));
		return request;
	}

	private Chart createChart(List<LiveRecord> liveRecords) {
		Chart chart = new Chart();
		chart.setType("line");
		chart.setData(createData(liveRecords));
		return chart;
	}

	private Data createData(List<LiveRecord> liveRecords) {
		Data data = new Data();
		data.setLabels(createLables(liveRecords));
		data.setDatasets(createDatasets(liveRecords));
		return data;
	}

	private List<Dataset> createDatasets(List<LiveRecord> liveRecords) {
		Dataset dataSet = new Dataset();
		dataSet.setLabel("同接数推移");
		dataSet.setBorderColor("blue");
		dataSet.setFill(false);
		dataSet.setData(createDataList(liveRecords));
		return Arrays.asList(dataSet);
	}

	private List<Integer> createDataList(List<LiveRecord> liveRecords) {
		List<Integer> data = new ArrayList<>();
		for (int i = 0; i < liveRecords.size(); i++) {
			data.add(liveRecords.get(i).getViewers());
		}
		return data;
	}

	private List<String> createLables(List<LiveRecord> liveRecords) {
		List<String> labels = new ArrayList<>();
		for (int i = 0; i < liveRecords.size(); i++) {
			labels.add(FORMATTER.format(liveRecords.get(i).getRegisteredAt()));
		}
		return labels;
	}

}

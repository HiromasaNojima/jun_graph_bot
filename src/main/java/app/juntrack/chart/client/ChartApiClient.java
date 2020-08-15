package app.juntrack.chart.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.juntrack.chart.client.request.ChartApiRequestJsonCreator;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
@Slf4j
public class ChartApiClient {

	private final OkHttpClient httpClient = new OkHttpClient();

	@Autowired
	protected ChartApiRequestJsonCreator requestJsonCreator;

	public Response postGraph(String contentId) {
		Request request = new Request.Builder()
				.url("https://quickchart.io/chart")
				.post(this.createRequestBody(contentId)).build();

		Response response = null;
		try {
			response = httpClient.newCall(request).execute();
		} catch (Exception exc) {
			log.error("グラフの生成に失敗しました。{}", exc);
		}

		return response;
	}

	public RequestBody createRequestBody(String contentId) {
		return RequestBody.create(requestJsonCreator.createRequestJson(contentId),
				MediaType.parse("application/json; charset=utf-8"));
	}

}

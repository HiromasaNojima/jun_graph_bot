package app.juntrack.twitch;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import app.juntrack.common.RecordDto;
import app.juntrack.common.StreamingSiteType;
import app.juntrack.twitch.client.http.client.TwitchApiClient;
import app.juntrack.twitch.client.http.endpoint.TwitchEndpoint;
import app.juntrack.twitch.client.http.response.streams.TwitchGetStreamsResponse;
import app.juntrack.twitch.wrapper.TwitchCredentialWrapper;
import app.juntrack.twitch.wrapper.TwitchUserIdWrapper;

@Component
public class TwitchRecordClient {

	private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

	private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 3L, TimeUnit.SECONDS, queue);

	@Autowired
	ApplicationContext context;

	@Autowired
	TwitchCredentialWrapper credential;

	@Autowired
	TwitchUserIdWrapper userId;

	@Scheduled(initialDelay = 0, fixedDelayString = "${jun.live.search.interval}")
	public void searchJunLive() {
		if (threadPoolExecutor.getActiveCount() != 0) {
			return;
		}

		TwitchGetStreamsResponse response = new TwitchApiClient<TwitchGetStreamsResponse>(credential.getAccessToken(),
				credential.getClientId()).sendGetRequest(
						TwitchEndpoint.STREAMS.getUrl() + "?user_id=" + userId.getUserId(),
						TwitchEndpoint.STREAMS.getApiName(), TwitchGetStreamsResponse.class);
		if (response == null || CollectionUtils.isEmpty(response.getData())) {
			return;
		}

		TwitchRecordWorker recoder = context.getBean(TwitchRecordWorker.class);
		recoder.setRecordDto(new RecordDto.Builder()
				.setContentId(response.getData().get(0).getId())
				.setStreamingSiteType(StreamingSiteType.TWITCH)
				.setTitle(response.getData().get(0).getTitle())
				.build());
		threadPoolExecutor.execute(recoder);
	}
}

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
import app.juntrack.twitch.client.http.client.TwitchGetStreamsClient;
import app.juntrack.twitch.client.http.response.streams.TwitchGetStreamsResponse;

@Component
public class TwitchRecordClient {

	private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

	private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 3L, TimeUnit.SECONDS, queue);

	@Autowired
	private TwitchGetStreamsClient streamsClient;

	@Autowired
	ApplicationContext context;

	@Scheduled(initialDelay = 0, fixedDelayString = "${jun.live.search.interval}")
	public void searchJunLive() {
		if (threadPoolExecutor.getActiveCount() != 0) {
			return;
		}

		TwitchGetStreamsResponse response = streamsClient.getStreams();
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

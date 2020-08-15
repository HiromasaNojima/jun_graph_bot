package app.juntrack.youtube.wrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class YoutubeCredentialWrapper {

	private static int index = 0;

	private final String[] credentials;

	private final int credentialsLength;

	public YoutubeCredentialWrapper(@Autowired Environment env) {
		credentials = env.getProperty("app.youtube.credentials").split(",");
		credentialsLength = credentials.length;
	}

	public synchronized String getCredential() {
		String credentialPath = null;
		if (index < credentialsLength) {
			credentialPath = credentials[index];
		} else {
			index = 0;
			credentialPath = credentials[index];
		}

		log.info(String.valueOf(index));
		index++;
		return credentialPath;
	}

}
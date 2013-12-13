package com.github.mobile.Accounts;

import com.github.kevinsawicki.http.HttpRequest;
import org.eclipse.egit.github.core.client.GitHubClient;

import java.net.HttpURLConnection;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.FROYO;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 10/12/13
 * Time: 15:59
 */
public class DefaultClient extends GitHubClient {

    private static final String USER_AGENT = "GitHubAndroid/1.6";

    static {
        // Disable http.keepAlive on Froyo and below
        if (SDK_INT <= FROYO)
            HttpRequest.keepAlive(false);
    }

    public DefaultClient() {
        super();

        setSerializeNulls(false);
        setUserAgent(USER_AGENT);
    }

    @Override
    protected HttpURLConnection configureRequest(HttpURLConnection request) {
        super.configureRequest(request);

        request.setRequestProperty(HEADER_ACCEPT, "application/vnd.github.beta.full+json");

        return request;
    }
}
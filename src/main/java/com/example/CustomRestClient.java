package com.example;

import com.netflix.client.RequestSpecificRetryHandler;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.IClientConfig;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpResponse;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.client.http.RestClient;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import org.springframework.cloud.netflix.ribbon.RibbonUtils;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;

import java.net.URI;

/* Most of this code comes from RibbonClientConfiguration.OverrideRestClient but we can't extend
 * that class directly.  the only custom code here is in the execute method.  In addition
 * getRequestSpecificRetryHandler returns a custom RetryHandler, however the logic in this
 * method is the same as what is in RibbonClientConfiguration.
 */
public class CustomRestClient extends RestClient {
    private IClientConfig config;
    private ServerIntrospector serverIntrospector;

    public CustomRestClient(IClientConfig config, ServerIntrospector serverIntrospector) {
        this.config = config;
        this.serverIntrospector = serverIntrospector;
        this.initWithNiwsConfig(this.config);
    }

    public URI reconstructURIWithServer(Server server, URI original) {
        URI uri = RibbonUtils.updateToHttpsIfNeeded(original, this.config, this.serverIntrospector, server);
        return super.reconstructURIWithServer(server, uri);
    }

    protected Client apacheHttpClientSpecificInitialization() {
        ApacheHttpClient4 apache = (ApacheHttpClient4)super.apacheHttpClientSpecificInitialization();
        apache.getClientHandler().getHttpClient().getParams().setParameter("http.protocol.cookie-policy", "ignoreCookies");
        return apache;
    }

    @Override
    public HttpResponse execute(HttpRequest task, IClientConfig requestConfig) throws Exception {
        HttpResponse superResponse =  super.execute(task, requestConfig);
        if(superResponse.getStatus() >= 500) {
            throw new ZuulRetryerApplication.ServiceException("downstream service instance encountored a problem");
        } else {
            return superResponse;
        }
    }

    @Override
    public RequestSpecificRetryHandler getRequestSpecificRetryHandler(HttpRequest request, IClientConfig requestConfig) {
        if (!request.isRetriable()) {
            return new CustomRetryHandler(false, false, this.getRetryHandler(), requestConfig);
        }
        if (this.config.get(CommonClientConfigKey.OkToRetryOnAllOperations, false)) {
            return new RequestSpecificRetryHandler(true, true, this.getRetryHandler(), requestConfig);
        }
        if (request.getVerb() != HttpRequest.Verb.GET) {
            return new CustomRetryHandler(true, false, this.getRetryHandler(), requestConfig);
        } else {
            return new CustomRetryHandler(true, true, this.getRetryHandler(), requestConfig);
        }
    }
}

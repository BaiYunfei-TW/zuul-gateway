package com.example.zuulgateway.loadBalanceRole;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.WeightedResponseTimeRule;
import com.netflix.util.Pair;
import com.netflix.zuul.context.RequestContext;
import org.aspectj.weaver.ast.Var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MyRandomRule extends AbstractLoadBalancerRule {
    private static final String TRIED_SERVERS = "tried-servers";

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }

    @Override
    public Server choose(Object key) {
        ILoadBalancer lb = getLoadBalancer();
        if (lb == null) {
            return null;
        }
        Server server = null;

        while (server == null) {
            if (Thread.interrupted()) {
                return null;
            }
            List<Server> upList = lb.getReachableServers();
            List<Server> allList = lb.getAllServers();

            int serverCount = allList.size();
            if (serverCount == 0) {
                /*
                 * No servers. End regardless of pass, because subsequent passes
                 * only get more restrictive.
                 */
                return null;
            }

            server = upList.get(new Random().nextInt(upList.size()));

            if (server == null) {
                /*
                 * The only time this should happen is if the server list were
                 * somehow trimmed. This is a transient condition. Retry after
                 * yielding.
                 */
                Thread.yield();
                continue;
            }

            RequestContext ctx = RequestContext.getCurrentContext();
            if (ctx.get(TRIED_SERVERS) == null) {
                ctx.set(TRIED_SERVERS, new HashSet<String>());
            }
            Set<String> triedServers = (HashSet<String>) ctx.get(TRIED_SERVERS);
            if (server.isAlive() && !triedServers.contains(server.getId())) {
                triedServers.add(server.getId());
                ctx.set(TRIED_SERVERS, triedServers);
                logger.debug("choose server: " + server.getId());
                return (server);
            }

            // Shouldn't actually happen.. but must be transient or a bug.
            server = null;
            Thread.yield();
        }
        return server;
    }
}

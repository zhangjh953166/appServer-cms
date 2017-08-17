package com.sanmao.server.cms.dubbo.hystrix;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcResult;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.sanmao.server.model.Order;

public class DubboHystrixCommand extends HystrixCommand<Result> {

	private static Logger logger = Logger.getLogger(DubboHystrixCommand.class);
	private static final int DEFAULT_THREADPOOL_CORE_SIZE = 30;
	private Invoker<?> invoker;
	private Invocation invocation;
	
	public DubboHystrixCommand(Invoker<?> invoker,Invocation invocation){
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(invoker.getInterface().getName()))
				.andCommandKey(HystrixCommandKey.Factory.asKey(String.format("%s_%d", invocation.getMethodName(),
						invocation.getArguments()==null?0:invocation.getArguments().length)))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withCircuitBreakerRequestVolumeThreshold(2)//10秒钟内至少19此请求失败，熔断器才发挥起作用
						.withCircuitBreakerSleepWindowInMilliseconds(30000)//熔断器中断请求30秒后会进入半打开状态,放部分流量过去重试
						.withCircuitBreakerErrorThresholdPercentage(5)//错误率达到10开启熔断保护
						.withExecutionTimeoutEnabled(false))
				.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(getThreadPoolCoreSize(invoker.getUrl()))));//使用dubbo的超时，禁用这里的超时
		this.invoker = invoker;
		this.invocation = invocation;
	}
	
	/**
     * 获取线程池大小
     * 
     * @param url
     * @return
     */
    private static int getThreadPoolCoreSize(URL url) {
        if (url != null) {
            int size = url.getParameter("ThreadPoolCoreSize", DEFAULT_THREADPOOL_CORE_SIZE);
            if (logger.isDebugEnabled()) {
                logger.debug("ThreadPoolCoreSize:" + size);
            }
            return size;
        }
        return DEFAULT_THREADPOOL_CORE_SIZE;
    }
	
	@Override
	protected Result run() throws Exception {
//		throw new RuntimeException("Hystrix throw exp");
		try{
			Result result = invoker.invoke(invocation);
			if(result.hasException()){
				logger.info("DubboHystrixCommand has exp");
				throw new Exception(result.getException());
			}
			return result;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw e;
		}finally{
			logger.info("dfea");
		}
	}
	@Override
	protected Result getFallback(){
		logger.info("enter getFallback");
		List<Order> list =new ArrayList<Order>();
		list.add(new Order());
		return new RpcResult(list);
	}

}

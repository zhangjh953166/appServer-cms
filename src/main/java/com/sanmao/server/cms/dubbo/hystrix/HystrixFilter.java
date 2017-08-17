package com.sanmao.server.cms.dubbo.hystrix;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;

@Activate(group=Constants.CONSUMER ,order = 10000)
public class HystrixFilter implements Filter {

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		DubboHystrixCommand command = new DubboHystrixCommand(invoker,invocation);
		return command.execute();
	}

}

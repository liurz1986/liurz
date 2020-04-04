package cn.com.usercenter.util;

import java.util.concurrent.Callable;

/**
 * 多线程处理：
 * 
 * @ClassName: ProcessCallable
 * @Description: TODO
 * @author lwx393577：
 * @date 2020年3月30日 下午9:13:52
 * 
 * @param <V>
 */
public abstract class ProcessCallable<V> implements Callable<V> {

	@Override
	public V call() throws Exception {

		return process();
	}

	public abstract V process() throws Exception;

}

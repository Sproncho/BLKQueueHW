package telran.mediation;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class 	BlkQueue<T> implements IBlkQueue<T> {
	LinkedList<T> queue = new LinkedList<>();
	int maxSize;
	Lock mutex = new ReentrantLock();
	Condition producerWaitingCondition = mutex.newCondition();
	Condition consumerWaitingCondition = mutex.newCondition();

	public BlkQueue(int maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	public void push(T message) {
		mutex.lock();
		try {
			while (queue.size() == maxSize){
				try {
					producerWaitingCondition.await();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				queue.push(message);
				consumerWaitingCondition.signal();
			}
		}finally {
			mutex.unlock();
		}

	}

	@Override
	public T pop() {
		mutex.lock();
		try {
			while (queue.size()  == 0){
				try {
					producerWaitingCondition.await();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			T res  = queue.pop();
			consumerWaitingCondition.signal();
			return res;
		}finally {
			mutex.unlock();
		}
	}
}
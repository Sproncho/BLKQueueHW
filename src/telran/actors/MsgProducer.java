package telran.actors;
import telran.mediation.IBlkQueue;

public class MsgProducer extends Thread {
	IBlkQueue<String> blkQueue;
	int nMessages;
	int sendIntervalMillis;

	public MsgProducer(IBlkQueue<String> blkQueue, int nMessages, int sendIntervalMillis) {
		super();
		this.blkQueue = blkQueue;
		this.nMessages = nMessages;
		this.sendIntervalMillis = sendIntervalMillis;
	}

	@Override
	public void run() {
		for (int i = 0; i < nMessages; i++) {
			try {
				Thread.sleep(sendIntervalMillis);
			} catch (InterruptedException e) {
				// noop
			}
			String message = "message#" + i;
			blkQueue.push(message);
			System.out.printf("%s <== producer %d\n", message, getId());
		}
	}
}


package cn.com.test.one;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

public class ConnectionWatcher implements Watcher {
	// 连接超时时间设置
    private static final int SESSION_TIMEOUT = 5000;
    
    protected ZooKeeper zk;
    
    CountDownLatch connectedSignal = new CountDownLatch(1);
    
    public void connect(String host) throws IOException, InterruptedException {
        zk = new ZooKeeper(host, SESSION_TIMEOUT, this);
        connectedSignal.await();
    }

    public void process(WatchedEvent event) {
        if (event.getState() == KeeperState.SyncConnected) {
            connectedSignal.countDown();
        }
    }
    public void close() throws InterruptedException{
        zk.close();
    }
}
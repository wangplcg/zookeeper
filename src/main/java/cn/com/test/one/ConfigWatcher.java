package cn.com.test.one;

import java.io.IOException;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

public class ConfigWatcher implements Watcher{
	
    private ActiveKeyValueStore store;

    @Override
    public void process(WatchedEvent event) {
    	System.out.println(event.getState().name());
    	System.out.println(event.getType());
    	System.out.println(event.getPath());
    	
        if(event.getType() == EventType.NodeDataChanged){
            try {
                dispalyConfig();
            } catch (InterruptedException e){
                System.err.println("Interrupted. exiting. ");
                Thread.currentThread().interrupt();
            } catch (KeeperException e) {
                System.out.printf("KeeperException锛?s. Exiting.\n", e);
            }
        }
    }
    
    public ConfigWatcher(String hosts) throws IOException, InterruptedException {
        store = new ActiveKeyValueStore();
        store.connect(hosts);
    }
    
    public void dispalyConfig() throws KeeperException, InterruptedException{
        String value=store.read(ConfigUpdater.PATH, this);
        System.out.printf("Read %s as %s\n", ConfigUpdater.PATH, value);
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ConfigWatcher configWatcher = new ConfigWatcher("47.100.23.48");
        configWatcher.dispalyConfig();
        Thread.sleep(Long.MAX_VALUE);
    }
}
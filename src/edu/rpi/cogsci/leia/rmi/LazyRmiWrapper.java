/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rpi.cogsci.leia.rmi;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author pb
 */
public class LazyRmiWrapper<T extends Remote> {
    
    private String id;
    private String cls;
    
    private String host;
    private int port;
    
    public LazyRmiWrapper(Class<T> cls) {
        this(cls, "127.0.0.1", 1066);
    }
    
    public LazyRmiWrapper(Class<T> cls, String host, int port) {
        this.id = cls.getSimpleName();
        this.cls = cls.getCanonicalName();
        this.host = host;
        this.port = port;
    }
    
    public T getService() throws InterruptedException, Exception {
        T service = null;
        
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            
            System.out.println(String.format("Looking up %s in registry at %s:%d...", id, host, port));
            service = (T) registry.lookup(id);
            
            System.out.println(String.format("A running %s detected... (First time call may take longer.)", id));
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println(String.format("No running %s detected. Starting one up...", id));
            
            spawnService();
            //Note: Let it start
            Thread.sleep(3000);
            
            return getService();
        }
        return service;
    }
    
    private void spawnService() throws Exception {
	String separator = System.getProperty("file.separator");
	String classpath = System.getProperty("java.class.path");
        String home = System.getProperty("java.home");
	String path = String.format("%s%sbin%sjava", home, separator, separator);
	
        ProcessBuilder processBuilder = 
            new ProcessBuilder(
                path, 
                "-cp", 
                classpath, 
                "-Xmx3g",
                RmiServiceLauncher.class.getName(),
                cls,
                id,
                String.valueOf(port));
        
        System.out.println("Executing");
        processBuilder.command().stream().forEach(arg -> System.out.println(arg));
        
	Process process = processBuilder.start();
    }
}

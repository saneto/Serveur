import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServeurThreadPool implements Runnable
{
	protected int          serverPort   = 8080;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;
    protected ExecutorService threadPool =
        Executors.newFixedThreadPool(10);

    public ServeurThreadPool(int port){
        this.serverPort = port;
        System.out.println(port);
    }
    @Override
    public void run(){
        synchronized(this)
        {
            this.runningThread = Thread.currentThread();
        }
      //Ouverture de a et lancement de l'ecoute du serveur
        openServerSocket();
        System.out.println(serverSocket.getInetAddress());
        while(! isStopped()){
            Socket clientSocket = null;
            try {
            	// on accepte la connexion 
                clientSocket = this.serverSocket.accept();
                System.out.println(clientSocket.getInetAddress());
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    break;
                }
                throw new RuntimeException(
                    "Error accepting client connection", e);
            }
            this.threadPool.execute(new Serveur(clientSocket,"Thread Pooled Server"));
        }
        this.threadPool.shutdown();
        System.out.println("Server Stopped.") ;
    }


    private synchronized boolean isStopped()
    {
        return this.isStopped;
    }

    public synchronized void stop()
    {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
        	
            this.serverSocket = new ServerSocket(this.serverPort);
            System.out.println(serverSocket.getInetAddress());
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port"+this.serverPort, e);
        }
    }
}

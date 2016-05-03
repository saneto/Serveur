import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import Device.Device;
import Device.DeviceList;
import Message.NbNom;

public class TestServ 
{
	public static void main (String[] args) throws UnknownHostException
	{
		int AdrP=0;
		System.out.println(InetAddress.getLocalHost());
		DeviceList.device.add(new Device("Aymane","155440",InetAddress.getLocalHost().toString(),20000,"B1-51",true));
		DeviceList.device.get(0).nbContact.add(new NbNom("toto"));
		DeviceList.device.get(0).nbContact.get(0).AjoutMessage("toto:SALUT CV");
		DeviceList.device.add(new Device("toto","155440",InetAddress.getLocalHost().toString(),20000,"B1-51",true));
		DeviceList.device.get(1).nbContact.add(new NbNom("Aymane"));
		DeviceList.device.get(1).nbContact.get(0).AjoutMessage("Aymane:SALUT CV");
		/*System.out.println(DeviceList.device.get(1).nbContact.get(0).getALLMessage()+DeviceList.device.get(1).nbContact.get(0).getNom());
		System.out.println(DeviceList.device.get(0).nbContact.get(0).getALLMessage()+DeviceList.device.get(0).nbContact.get(0).getNom());
		System.out.println("Veuillez saisir le port");*/
		/*Scanner sc = new Scanner(System.in);
		AdrP= Integer.parseInt(sc.nextLine());
		System.out.println(AdrP);*/
		ServeurThreadPool STP=new ServeurThreadPool(12345);
		new Thread(STP).start();
		try{
			Thread.sleep(20*20000);
		}catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		System.out.println("Stopping Server");
		STP.stop();
	}
}

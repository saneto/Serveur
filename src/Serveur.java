import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import Device.Device;
import Device.DeviceList;
import Forum.FOFO;
import Forum.Forum;
import Message.NbNom;


public class Serveur implements Runnable
{

	protected Socket  clientSocket= null;
	protected String serverText   = null;
	
	public Serveur(Socket clientSocket, String serverText) 
	{
		this.clientSocket = clientSocket;
		this.serverText   = serverText;
	}
	@Override
	public void run()
	{
		try {
			//Variable d'ecriture 
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			
			//Variable de lecture
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			int i=0;
			String Msg=null;
			boolean present=false;
			boolean connexion = false;
			//Lecture du flux lors de la connexion 
			Msg=in.readLine();
			String Persone=Msg.substring(0,Msg.indexOf("@"));
			//s'il n y a aucun utilisateur enregistrer on rajoute un nouveau 
			if(DeviceList.device.size()==0)
			{
				DeviceList.device.add(new Device(Msg.substring(0,Msg.indexOf("@")),
						Msg.substring(Msg.indexOf("@")+1,Msg.indexOf(";")),
						clientSocket.getInetAddress().toString(),
						Integer.valueOf(Msg.substring(Msg.indexOf("$")+1,Msg.indexOf("{"))),
						Msg.substring(Msg.indexOf("{")+1,Msg.indexOf("}")), true));
				connexion =true;
				
			}else//et s'il y a deja des personnes enregistrer on cherche dans la liste  
			{

				System.out.println(Msg);
				for( i=0;i<DeviceList.device.size();i++)
				{
					//si l'utilisateur est deja enregistrer on active on met la varaible connexion a true 
					if(Msg.substring(0,Msg.indexOf("@")).equals(DeviceList.device.get(i).nom))
					{
						DeviceList.device.get(i).connecter=true;
						present=true;
						connexion =true;
						System.out.println("Bienvenu toto");
					}
				}
				if(!present)
				{
					//sinon on le rajoute
					DeviceList.device.add(new Device(Msg.substring(0,Msg.indexOf("@")),
							Msg.substring(Msg.indexOf("@")+1,Msg.indexOf(";")),
							clientSocket.getInetAddress().toString(),
							Integer.valueOf(Msg.substring(Msg.indexOf("$")+1,Msg.indexOf("{"))),
							Msg.substring(Msg.indexOf("{")+1,Msg.indexOf("}")), true));
					connexion=true;
				}
			}
			out.println("OK");
			out.flush();
			String Ret;
			do{
					Msg=in.readLine();
					System.out.println(Msg);
					switch(Msg.charAt(0))
					{
						case 'L':
								Ret="";
								if(Forum.forum.size()==0)
								{	
									
									Ret=Forum.forum.get(0).getMSG();
									
								}else if(Forum.forum.size()<=25)
								{
									for(i=0;i<Forum.forum.size() ;i++)
									{
										Ret=Ret.concat(Forum.forum.get(i).getMSG());
									}
								}else
								{
									for(i=Forum.forum.size()-25;i<Forum.forum.size() ;i++)
									{
										Ret=Ret.concat(Forum.forum.get(i).getMSG());
									}
								}
								System.out.println(Ret);
								out.println(Ret);
								out.flush();
						break;
						//F pour cibler le forum on rajoute a la suite et on lui renvoie les 25 derniers message
						case 'F':
									FOFO f=new FOFO(Msg.substring(Msg.indexOf("~")+1)+"/");
									Forum.forum.add(f);
									if(Forum.forum.size()>=25)
									{
										Forum.forum.remove(0);
									}
						break;
						case 'A':
							for( i=0;i<DeviceList.device.size();i++)
							{
								System.out.println(i);
								if(Msg.substring(Msg.indexOf("~")+1,Msg.indexOf(":")).equals(DeviceList.device.get(i).nom))
								{
									for(int w=0;w<DeviceList.device.get(i).nbContact.size();w++)
									{
										if(Msg.substring(Msg.indexOf(":")+1,Msg.indexOf("$")).equals(DeviceList.device.get(i).nbContact.get(w).getNom()))
										{
											System.out.println("+1"+Msg.substring(Msg.indexOf(":")+1,Msg.indexOf("$")));
											DeviceList.device.get(i).nbContact.get(w).AjoutMessage(Msg.substring(Msg.indexOf("~")+1,Msg.indexOf(":"))+": "+Msg.substring(Msg.indexOf("$")+1));
										}
									}
								}else if(Msg.substring(Msg.indexOf(":")+1,Msg.indexOf("$")).equals(DeviceList.device.get(i).nom))
								{
									for(int g=0;g<DeviceList.device.get(i).nbContact.size();g++)
									{
										if(Msg.substring(Msg.indexOf("~")+1,Msg.indexOf(":")).equals(DeviceList.device.get(i).nbContact.get(g).getNom()))
										{
											DeviceList.device.get(i).nbContact.get(g).AjoutMessage(Msg.substring(Msg.indexOf("~")+1,Msg.indexOf(":"))+": "+Msg.substring(Msg.indexOf("$")+1));
											System.out.println("+2");
										}
									}
								}
							}
							
						break;
						case 'B':
							Ret="";
							for( i=0;i<DeviceList.device.size();i++)
							{
								//System.out.println(i+" "+DeviceList.device.size()+" "+DeviceList.device.get(i).nom);
								//si l'utilisateur est deja enregistrer on active on met la varaible connexion a true 
								if(Msg.substring(Msg.indexOf("~")+1,Msg.indexOf(":")).equals(DeviceList.device.get(i).nom))
								{
									System.out.println(i+" "+DeviceList.device.size()+" "+DeviceList.device.get(i).nom);
									for(int g=0;g<DeviceList.device.get(i).nbContact.size();g++)
									{
										//System.out.println(g+DeviceList.device.get(i).nbContact.get(g).getNom()+DeviceList.device.get(i).nbContact.get(0).getNom());
										if(Msg.substring(Msg.indexOf(":")+1).equals(DeviceList.device.get(i).nbContact.get(g).getNom()))
										{
											Ret=DeviceList.device.get(i).nbContact.get(g).getALLMessage();
										}
									}
								}
							}
							System.out.println(Ret);
							out.println(Ret);
							out.flush();
						break;
						//R pour récupere l'addresse ip et le port d'un autre utilisateur 
						case 'R':
							boolean P=false;
								for(int v=0; v<DeviceList.device.size();v++)
								{
									if(DeviceList.device.get(v).nom.equals(Msg.substring(Msg.indexOf("~")+1,Msg.indexOf(":"))))
									{
										for(int m=0; m<DeviceList.device.size();m++)
										{
											if(DeviceList.device.get(m).nom.equals(Msg.substring(Msg.indexOf(":")+1)))
											{
												/*DeviceList.device.get(m).nbContact.add(new NbNom(Msg.substring(Msg.indexOf("~")+1,Msg.indexOf(":"))));
												DeviceList.device.get(v).nbContact.add(new NbNom(Msg.substring(Msg.indexOf(":")+1)));*/
												System.out.println("présent");
												P=true;
											}
										}
										
									}
								}
								if(P)
								{
									out.println("Pr");
									out.flush();
								}else
								{
									out.println("Non");
									out.flush();
								}
						break;
						case 'P':
							//P pour récupere la position de tout les utilisateurs 
								for( i=0; i<DeviceList.device.size();i++)
								{
									
									if(Integer.valueOf(DeviceList.device.get(i).position.charAt(2))==Integer.valueOf(Msg.charAt(Msg.indexOf("/")+2)))
									{
										out.println(DeviceList.device.get(i).position);
										out.flush();
									}
								}
						break;
						case 'E':
							System.out.println("FIND du serveur");
							connexion=false;
						break;
					}
			}while(connexion);
			for( i=0;i<DeviceList.device.size();i++)
			{
				if(Persone.equals(DeviceList.device.get(i).nom))
				{
					DeviceList.device.get(i).connecter=false;
				}
			}
			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {
		e.printStackTrace();
		}
	}
}
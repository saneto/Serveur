package Message;

import java.util.ArrayList;
import java.util.List;

public class NbNom 
{
	private ArrayList<Message> M=new ArrayList<Message>();
	private String Nom;
	private int Nbmessagetourner=1;
	public NbNom(String Nom)
	{
		this.Nom=Nom;
	}
	public String getNom() {
		return Nom;
	}
	public void setNom(String nom) 
	{
		Nom = nom;
	}
	public void AjoutMessage(String message)
	{
		M.add(new Message(message,true));
		System.out.println(message);
		System.out.println(M.size());
		if(Nbmessagetourner>0)
		{
			Nbmessagetourner--;
		}
		if(M.size()>=50)
		{
			M.remove(1);
			M.remove(0);
		}
	}
	public String getALLMessage()
	{
		String retour="";
		System.out.println(M.size());
		for(int i=0;i<M.size();i++)
		{
			if(M.get(i).isValide())
			{
				retour=retour.concat(M.get(i).getMSG()+"/");
				System.out.println(M.get(i).getMSG()+"/");
				Nbmessagetourner++;
				M.get(i).setValide(false);
			}
				
		}
		return retour;
	}
}

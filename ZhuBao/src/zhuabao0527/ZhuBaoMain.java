package zhuabao0527;
import com.sun.org.apache.regexp.internal.recompile;

import jpcap.*;
import jpcap.packet.*;
public class ZhuBaoMain
{

	public static void main(String[] args)throws Exception
	{
		NetworkInterface[] devices = JpcapCaptor.getDeviceList();
//		for (int i = 0; i < devices.length; i++) 
//		{
//			  //��ӡ�������ƺ�����
//			  System.out.println(i+": "+devices[i].name + "(" + devices[i].description+")");
//
//			  //��ӡ����datalink�����ƺ�����
//			  System.out.println(" datalink: "+devices[i].datalink_name + "(" + devices[i].datalink_description+")");
//
//			  //��ӡ����MAC��ַ
//			  System.out.print(" MAC address:");
//			  for (byte b : devices[i].mac_address)
//			    System.out.print(Integer.toHexString(b&0xff) + ":");
//			  System.out.println();
//
//			  //��ӡ����IP��ַ����������͹㲥��ַ
//			  for (NetworkInterfaceAddress a : devices[i].addresses)
//			    System.out.println(" address:"+a.address + " " + a.subnet + " "+ a.broadcast);
//		}
		JpcapCaptor captor=JpcapCaptor.openDevice(devices[3], 65535, true, 20);//��������ȡ��Ϣ����������true������ʱ
		
		for(int i=0;i<10;i++)
		{

			Packet packet=captor.getPacket();
			System.out.println("=============");
			if( (packet instanceof UDPPacket))
			{
				System.out.println("����"+packet.toString());
				
				System.out.println("pca���ȣ�"+packet.caplen+"����");
				System.out.println("���ݣ�"+byteArrayToString(packet.data,"="));
				System.out.println("datalink��"+packet.datalink);//�õ�������֡
				//datalink��jpcap.packet.EthernetPacket@1f40b69 80:fa:5b:22:5b:67->ff:ff:ff:ff:ff:ff (2054)
				
				System.out.println("ͷ����"+byteArrayToString(packet.header,"="));//�õ�վ��ARP��Ϣȫ��
				//ͷ����ff=ff=ff=ff=ff=ff=80=fa=5b=22=5b=67=8=6
				//=0=1=8=0=6=4=0=1=80=fa=5b=22=5b=67=a=d3=1d=87=0=0=0=0=0=0=a=d3=1a=15=
				
				System.out.println("���ȣ�"+packet.len);
				System.out.println("miao��"+Long.toString(packet.sec));
				
			}
			
		}
		captor.close();
		
//		for(int j=0;j<100;j++)
//		{
//			Packet pp=captor.getPacket();
//			if(pp instanceof IPPacket)
//			{
//				IPPacket ipPacket=(IPPacket)pp;
//				System.out.println("Դ��ַ"+ipPacket.src_ip+"Ŀ�ĵ�ַ��"+ipPacket.dst_ip);
//			}
//			else if(pp instanceof ARPPacket)
//			{
//				ARPPacket arpPacket=(ARPPacket)pp;
//				
//				System.out.println("MacԴ��ַ"+arpPacket.getSenderProtocolAddress());
//				System.out.print("MacĿ��"+arpPacket.getTargetProtocolAddress());
//				
//				System.out.println();
//				
//			}
			//System.out.println(pp.toString());
//		}
		//captor.processPacket(100,new JieShou());

	}
	public static String judgeType(Packet pac)
	{
		if(pac instanceof ARPPacket)
			return "ARP";
		else if (pac instanceof UDPPacket)
			return "UDP";
		else if (pac instanceof ICMPPacket)
			return "ICMP";
		else if (pac instanceof TCPPacket)
			return "TCP";
		else if(pac instanceof IPPacket)
			return "IP";
		
		else return "unknow";
			
	}
	public static String byteArrayToString(byte[]  bytes,String spl)//����ת��byte[]��String
	{
		String str="";
		
		try
		{
			for (byte b : bytes)
			{
				str += Integer.toHexString(b & 0xff) + spl;
			} 
		} catch (Exception e)
		{
			str=null;
		}
		return str; 
	}

}
class JieShou implements PacketReceiver
{
	@Override
	public void receivePacket(Packet pp)
	{
//		//System.out.println("====="+pp.toString());
//		if(pp instanceof IPPacket)
//		{
//			IPPacket ipPacket=(IPPacket)pp;
//			System.out.println("Դ��ַ"+ipPacket.src_ip+"Ŀ�ĵ�ַ��"+ipPacket.dst_ip);
//		}
//		else if(pp instanceof IPPacket)
//		{
//			ARPPacket arpPacket=(ARPPacket)pp;
//			for(byte i:arpPacket.sender_hardaddr)
//				System.out.println(i+":");
//		}
//		else 
//		{
//			System.out.println("");
//		}
////		System.out.println(ZhuBaoMain.judgeType(pp));
////		System.out.println(pp.toString());
////	
////		for (byte bb:pp.data)
////		{
////			System.out.print(bb+" ");
////		}
////		System.out.println();
////			
	}
}

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
//			  //打印它的名称和描述
//			  System.out.println(i+": "+devices[i].name + "(" + devices[i].description+")");
//
//			  //打印它的datalink的名称和描述
//			  System.out.println(" datalink: "+devices[i].datalink_name + "(" + devices[i].datalink_description+")");
//
//			  //打印它的MAC地址
//			  System.out.print(" MAC address:");
//			  for (byte b : devices[i].mac_address)
//			    System.out.print(Integer.toHexString(b&0xff) + ":");
//			  System.out.println();
//
//			  //打印它的IP地址、子网掩码和广播地址
//			  for (NetworkInterfaceAddress a : devices[i].addresses)
//			    System.out.println(" address:"+a.address + " " + a.subnet + " "+ a.broadcast);
//		}
		JpcapCaptor captor=JpcapCaptor.openDevice(devices[3], 65535, true, 20);//网卡，获取信息数量，混杂true，捕获超时
		
		for(int i=0;i<10;i++)
		{

			Packet packet=captor.getPacket();
			System.out.println("=============");
			if( (packet instanceof UDPPacket))
			{
				System.out.println("包："+packet.toString());
				
				System.out.println("pca长度："+packet.caplen+"结束");
				System.out.println("数据："+byteArrayToString(packet.data,"="));
				System.out.println("datalink："+packet.datalink);//拿到了整个帧
				//datalink：jpcap.packet.EthernetPacket@1f40b69 80:fa:5b:22:5b:67->ff:ff:ff:ff:ff:ff (2054)
				
				System.out.println("头部："+byteArrayToString(packet.header,"="));//拿到站和ARP信息全部
				//头部：ff=ff=ff=ff=ff=ff=80=fa=5b=22=5b=67=8=6
				//=0=1=8=0=6=4=0=1=80=fa=5b=22=5b=67=a=d3=1d=87=0=0=0=0=0=0=a=d3=1a=15=
				
				System.out.println("长度："+packet.len);
				System.out.println("miao："+Long.toString(packet.sec));
				
			}
			
		}
		captor.close();
		
//		for(int j=0;j<100;j++)
//		{
//			Packet pp=captor.getPacket();
//			if(pp instanceof IPPacket)
//			{
//				IPPacket ipPacket=(IPPacket)pp;
//				System.out.println("源地址"+ipPacket.src_ip+"目的地址："+ipPacket.dst_ip);
//			}
//			else if(pp instanceof ARPPacket)
//			{
//				ARPPacket arpPacket=(ARPPacket)pp;
//				
//				System.out.println("Mac源地址"+arpPacket.getSenderProtocolAddress());
//				System.out.print("Mac目标"+arpPacket.getTargetProtocolAddress());
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
	public static String byteArrayToString(byte[]  bytes,String spl)//用于转换byte[]到String
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
//			System.out.println("源地址"+ipPacket.src_ip+"目的地址："+ipPacket.dst_ip);
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
